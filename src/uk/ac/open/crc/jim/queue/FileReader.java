/*
 Copyright (C) 2010-2015 The Open University

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
*/

package uk.ac.open.crc.jim.queue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.DefaultErrorStrategy;
import org.antlr.v4.runtime.InputMismatchException;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.open.crc.jim.parser.java14.ASTCompilationUnit;
import uk.ac.open.crc.jim.parser.java14.Java14Parser;
import uk.ac.open.crc.jim.parser.java15.Java15Parser;
import uk.ac.open.crc.jim.parser.java17.Java17VisitorImplementation;
import uk.ac.open.crc.jim.parser.java17.JavaLexer;
import uk.ac.open.crc.jim.parser.java17.JavaParser;
import uk.ac.open.crc.jim.persistence.EntityStoreSingleton;
import uk.ac.open.crc.jim.Settings;

/**
 * Processes source code files.
 */
class FileReader implements Runnable {

    File javaFile;
    Settings settings;

    private static final Logger LOGGER = 
            LoggerFactory.getLogger( FileReader.class );

    /**
     * A {@code Runnable} implementation that initiates file parsing.
     *
     * @param javaFile a {@code File} object referencing a Java file to be
     * parsed.
     *
     */
    FileReader( File javaFile ) {
        this.javaFile = javaFile;
        this.settings = Settings.getInstance();
    }

    @Override
    public void run() {
        if ( this.javaFile != null ) {
            if ( ! Boolean.parseBoolean( this.settings.get( "analyse.generated" ) ) ) {
                if ( isGenerated( this.javaFile ) ) {
                    return;
                }
            }

            LOGGER.info( "Trying Java 7 parser" );
            boolean fileParsed = parseAsJava17( javaFile );
            
            // Java 1.5 parser is redundant
            // as all Java 5 is legal Java 7.
//            if ( ! fileParsed ) {
//                LOGGER.info( "Trying Java 5 parser" );
//                fileParsed = parseAsJava15( javaFile );
//            }
            
            // - if 1.7/1.5 parser fails try Java14Parser
            // - if both fail log the fact
            if ( ! fileParsed ) {
                LOGGER.info( "Trying Java 4 parser" );
                fileParsed = parseAsJava14( javaFile );
                if ( ! fileParsed ) {
                    LOGGER.warn( 
                            "Unable to parse file: \"{}\"", 
                            javaFile.getAbsolutePath() );
                }
            }
        }
    }

    // A two stage parsing approach is implemented in this method.
    // First the SLL* approach, which is less computationally expensive, 
    // and switches to LL* if that fails. 
    // see example at https://theantlrguy.atlassian.net/wiki/pages/viewpage.action?pageId=1900591
    // There's a nested try/catch because of retrying 
    // the parser in a different configuration
                
    private boolean parseAsJava17( File sourceFile ) {
        ANTLRInputStream input;
        try ( BufferedReader reader = 
                new BufferedReader(new InputStreamReader( new FileInputStream( sourceFile ) ) ) ) {
            input = new ANTLRInputStream( reader );

            JavaLexer javaLexer = new JavaLexer( input );
            CommonTokenStream tokens = new CommonTokenStream( javaLexer );

            JavaParser javaParser = new JavaParser( tokens );
            javaParser.removeErrorListeners();
            javaParser.addErrorListener( new LogListener() );
            javaParser.setErrorHandler( new BailErrorStrategy() );
            javaParser.getInterpreter().setPredictionMode( PredictionMode.SLL );

            ParseTree parseTree;
            
            try {
                parseTree = javaParser.compilationUnit();
            }
            catch ( RecognitionException e ) {
                // in here when the SLL(*) parser fails.
                // log it
                LOGGER.warn( 
                        "Syntax error encountered parsing file \"{}\" "
                                + "using SLL(*), switching to LL(*)", 
                        sourceFile.getAbsolutePath());
                tokens.reset();
                javaParser.reset();
                javaParser.getInterpreter().setPredictionMode( PredictionMode.LL );
                try {
                    parseTree = javaParser.compilationUnit();
                }
                catch ( RecognitionException ex ) {
                    // log it
                    LOGGER.warn( 
                            "Syntax error encountered parsing file \"{}\" "
                                    + "using LL(*).\n\"{}\"", 
                            sourceFile.getAbsolutePath(), 
                            ex.getMessage() );
                    return false;
                }
                catch ( RuntimeException ex ) {
                    // log it
                    LOGGER.warn( 
                            "ANTLR threw a runtime exception while parsing "
                                    + "file \"{}\" using LL(*).\n\"{}\"",
                            sourceFile.getAbsolutePath(),
                            ex.getMessage() );
                    return false;
                }
            }
            catch ( RuntimeException e ) {
                // this is a catch-all for ANTLR to retain control
                // and can hand off to other parsers.
                // log it
                LOGGER.warn( 
                        "ANTLR threw a runtime exception while parsing "
                                + "file \"{}\" using SLL(*)\n\"{}\"",
                        sourceFile.getAbsolutePath(),
                        e.getMessage() );
                return false;
            }
            
            Java17VisitorImplementation javaVisitor = new Java17VisitorImplementation( 
                    sourceFile.getName(), 
                    EntityStoreSingleton.getInstance() );
            javaVisitor.visit( parseTree );
        }
        catch( IOException ioEx ) {
            LOGGER.error( 
                    "File \"{}\" passed to ANTLR parser "
                            + "either cannot be found or opened.\n\"{}\"", 
                    sourceFile.getAbsolutePath(),
                    ioEx.getMessage() );
            return false; // bail on the exception
        }
        
        return true;
    }
    
    // Candidate for deletion.
    // No longer used because legal Java 1.5 is legal Java 1.7
    @Deprecated
    private boolean parseAsJava15( File sourceFile ) {
        Java15Parser java15Parser = new Java15Parser( sourceFile );
        try {
            uk.ac.open.crc.jim.parser.java15.ASTCompilationUnit compilationUnit15 
                    = java15Parser.CompilationUnit();
            uk.ac.open.crc.jim.parser.java15.IdentifierDeclarationVisitor declarationVisitor15
                    = new uk.ac.open.crc.jim.parser.java15.IdentifierDeclarationVisitor( this.javaFile.getName(), EntityStoreSingleton.getInstance() );
            compilationUnit15.jjtAccept( declarationVisitor15, null );
        }
        catch ( uk.ac.open.crc.jim.parser.java15.ParseException pEx ) {
            // log parse failure
            StringBuilder message
                    = new StringBuilder( "ParseException encountered parsing: " );
            message.append( sourceFile.getAbsolutePath() );
            message.append( "\n" );
            message.append( pEx.getMessage() );
            message.append( "\nMoving to next file.\n" );
            LOGGER.warn( message.toString() );

            // which should be to bail & fail
            return false;
        }
        
        return true;
    }
    
    private boolean parseAsJava14( File sourceFile ) {
        Java14Parser java14Parser = new Java14Parser( sourceFile );
        try {
            ASTCompilationUnit compilationUnit14 = java14Parser.CompilationUnit();
            uk.ac.open.crc.jim.parser.java14.IdentifierDeclarationVisitor declarationVisitor14
                    = new uk.ac.open.crc.jim.parser.java14.IdentifierDeclarationVisitor( this.javaFile.getName(), EntityStoreSingleton.getInstance() );
            compilationUnit14.jjtAccept( declarationVisitor14, null );
        }
        catch ( uk.ac.open.crc.jim.parser.java14.ParseException pEx ) {
            // log parse failure
            StringBuilder message
                    = new StringBuilder( "ParseException encountered parsing: " );
            message.append( sourceFile.getAbsolutePath() );
            message.append( "\n" );
            message.append( pEx.getMessage() );
            message.append( "\nMoving to next file.\n" );
            LOGGER.warn( message.toString() );

            // and bail & fail
            return false;
        }
        
        return true;
    }
    
    private boolean isGenerated( File sourceFile ) {
        // Check whether the file is generated.
        // Processing the file twice!!
        boolean generated = false;
        try (BufferedReader in = new BufferedReader( new java.io.FileReader( sourceFile ) )) {
            int lineCount = 0;
            final int linesToRead = 100; // should be in the first few lines, but be safe, just in case of licence statements.
            String line;
            // read file - 100 lines is very generous, but probably flawed
            while ( (line = in.readLine()) != null && lineCount < linesToRead ) {
                // Consider making this a single regex once it is thoroughly tested
                if ( line.matches( "\\/\\/ \\$ANTLR.*" )
                        || line.matches( "\\/\\* Generated.*(JavaCC|JJTree).*" )
                        || line.matches( "^.*\\* Generated by the IDL-to-Java compiler.*$" )
                        || line.matches( "^.*\\* This file was auto-generated.*$" )// apache axis, which has changed statements. The version used is a compromise to catch different versions
                        || line.matches( "^.*Generated by the protocol buffer compiler.*$" ) // google web toolkit
                        || line.matches( "^.*generated by make_pydocs\\.py.*$" ) // jython
                        || line.matches( "^.*Generated file, do not modify.*$" ) // jython
                        ) {
                    generated = true;
                    break;
                }
                lineCount++;
            }
        }
        catch ( FileNotFoundException e ) {
            LOGGER.error( "Cannot open file: {}\n{}",
                    new Object[]{sourceFile.toString(), e.toString()} );
        }
        catch ( IOException e ) {
            LOGGER.error( "problem opening: {}: {}",
                    sourceFile.toString(), e.toString() );
        }

        return generated;
    }

    
    // Error strategy class for ANTLR see ANTLR 4 book pp172-3
    // this forces ANTLR to bail out on a syntax error
    // instead of trying to recover.
    public class BailErrorStrategy extends DefaultErrorStrategy {
        
        // wrap and rethrow exception
        @Override
        public void recover( Parser recognizer, RecognitionException e ) {
            throw new RuntimeException( e );
        }
        
        // ensure that attempts are not made to recover inline
        @Override
        public Token recoverInline( Parser recognizer ) throws RecognitionException {
            throw new RuntimeException( new InputMismatchException( recognizer ));
        }
        
        // prevents recovery from problems in subrules
        @Override
        public void sync( Parser recognizer ) {}
    }
    
    
    // error listener to ensure ANTLR errors are sent to 
    // see ANTLR 4 book p154
    public class LogListener extends BaseErrorListener {
        @Override
        public void syntaxError( 
                Recognizer<?,?> recognizer, 
                Object offendingSymbol, 
                int line, 
                int charPositionInLine, 
                String msg, 
                RecognitionException e ) {
            List<String> stack = ((Parser) recognizer).getRuleInvocationStack();
            Collections.reverse( stack );
            LOGGER.warn( "In file: {}", javaFile);
            LOGGER.warn( "Rule stack: {}", stack);
            LOGGER.warn( 
                    "Line: {}\nSymbol: \"{}\"\nMessage: {}", 
                    line, offendingSymbol, msg );
        } 
    }
}
