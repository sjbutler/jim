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
package uk.ac.open.crc.jim;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import uk.ac.open.crc.jim.persistence.EntityStoreSingleton;
import uk.ac.open.crc.jim.queue.QueueManager;

/**
 * Entry class for Jim.
 * 
 */
public final class Jim {

    public final static String BANNER = String.format(
            "Jim v%d.%d.%d\n"
            + "%s\n"
            + "Contains Java grammar Copyright \u00a9 2006, Sun Microsystems, Inc. "
            + "All rights reserved.\n"
            + "Contains Java grammar Copyright \u00a9 2013, Terence Parr, Sam Harwell "
            + "All rights reserved.",
            Version.MAJOR,
            Version.MINOR,
            Version.PATCH_LEVEL,
            Version.COPYRIGHT );

    private static final Logger LOGGER = Logger.getLogger( Jim.class.getCanonicalName() );
    private static FileHandler fileHandler;
    private static ConsoleHandler consoleHandler;

    // constants for command line options
//    private static final String CONFIGURATION_PATH = "c";
    private static final String DATABASE_PATH = "d";
    private static final String INCLUDE_GENERATED_CODE = "g";
    private static final String HELP_OPTION = "h";
    private static final String MIN_THREADS = "min";
    private static final String MAX_THREADS = "max";
    private static final String PROJECT_NAME = "p";
//    private static final String SOURCE_PATH = "s";
    private static final String INCLUDE_TEST_CODE = "t";
    private static final String PROJECT_VERSION = "v";
    private static final String VERBOSE_LOGGING = "V";
    
    private static final String INTT_RECURSIVE = "intt-recursive";
    private static final String INTT_MODAL_EXPANSION = "intt-modal-expansion";
    
    public static void main( String[] args ) {
        Jim programInstance = new Jim();
        programInstance.setUp( args );
        programInstance.run();
    }

    private final ArrayList<File> fileArgumentList;
    
    private final Settings settings;

        
    /**
     * Convenience constructor to create non static context to start
     * application.
     *
     */
    private Jim() {
        this.fileArgumentList = new ArrayList<>();
        this.settings = Settings.getInstance();
        // set up the log file
        try {
            fileHandler = new FileHandler( 
                    System.currentTimeMillis() / 1000 + "-jim-log.xml" );
            LOGGER.addHandler( fileHandler );
            consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel( Level.WARNING );
            LOGGER.addHandler( consoleHandler );
        }
        catch ( IOException ioEx ) {
            LOGGER.log( 
                    Level.SEVERE, 
                    "Cannot open log file for writing: {0}", 
                    ioEx.toString() );
        }
        LOGGER.setLevel( Level.WARNING ); // try to cut down the noise for other users
    }
    
    
    private void setUp( String[] args ) {
        Options options = createCommandLineOptions();
        // say hello
        System.out.println(BANNER );

        // simplistic check saves some effort
        if ( args.length < 1 ) {
            dieUsage( "No arguments given.", options );
        }

        CommandLineParser parser = new DefaultParser();
        
        try {
            CommandLine cl = parser.parse( options, args );
            
            // process the command line arguments and 
            // do any necessary sanity checks
            
            // check for help first
            if ( cl.hasOption( HELP_OPTION ) ) {
                dieUsage( "", options );
            }
            
            List<String> optionsChosen = new ArrayList<>();
            optionsChosen.addAll( processCompulsoryArguments( cl ) );
            
            optionsChosen.addAll( processThreadSettings( cl ) );
            
            optionsChosen.addAll( processBooleanOptions( cl ) );
            
            optionsChosen.addAll( processUnparsedArguments( cl ) );
            
            // now tell the user how jim is configured
            String newLine = System.lineSeparator();
            String selectedOptions = 
                    optionsChosen.stream().collect( 
                            Collectors.joining( newLine ) );
            LOGGER.log( 
                    Level.INFO,
                    "Command line flags:{0}{1}",
                    new Object[]{ newLine, selectedOptions } );
        }
        catch ( ParseException e ) {
            LOGGER.log(
                    Level.SEVERE, 
                    "Error parsing command line: {0}", 
                    e.getMessage());
            dieUsage( "Unable to parse command line arguments.", options );
        }
        catch ( CommandLineArgumentException e ) {
            LOGGER.log( 
                    Level.SEVERE,
                    "Error in arguments: {0}", 
                    e.getMessage() );
            dieUsage( "", options );  // surely there is a more approprite/helpful message
        }
    }
    
    private Options createCommandLineOptions() {
                Options options = new Options();
        
        //options.addOption( "c", "config", true, "path to a configuration file to read options from");
        options.addOption( 
                DATABASE_PATH, 
                "database-path", 
                true, 
                "path to database" );
        options.addOption( 
                INCLUDE_GENERATED_CODE, 
                "include-generated", 
                false, 
                "include files containing generated code" );
        options.addOption( 
                HELP_OPTION, 
                "help", 
                false, 
                "displays help/usage message" );
        options.addOption( 
                PROJECT_NAME, 
                "project", 
                true, 
                "the project name" );
        options.addOption( 
                INCLUDE_TEST_CODE, 
                "include-test-files", 
                false, 
                "includes files containing tests" );
        options.addOption( 
                PROJECT_VERSION, 
                "version", 
                true, 
                "the project version" );
        options.addOption( 
                VERBOSE_LOGGING, 
                "verbose", 
                false, 
                "displays more detailed log messages" );
        
        //long options only
        options.addOption(Option.builder()
                .longOpt( MIN_THREADS )
                .desc( "the minimum number of threads in the thread pool" )
                .hasArg()
                .argName( "MIN_THREADS" )
                .build() );
        
        options.addOption(Option.builder()
                .longOpt( MAX_THREADS )
                .desc( "the maximum number of threads in the thread pool" )
                .hasArg()
                .argName( "MAX_THREADS" )
                .build() );
        
        options.addOption( Option.builder()
                .longOpt( INTT_RECURSIVE )
                .desc( "enable recursive splits of single case identifier names" )
                .build() );
        
        options.addOption( Option.builder()
                .longOpt( INTT_MODAL_EXPANSION )
                .desc( "enable the expansion of modal verb contractions in intt" )
                .build() );

        return options;
    }
    
    // refactor?
    private List<String> processCompulsoryArguments( CommandLine cl ) 
            throws CommandLineArgumentException {
        boolean hasDatabasePath = false;
        boolean hasProjectVersion = false;
        boolean hasProjectName = false;

        List<String> errorMessages = new ArrayList<>();

        List<String> optionsChosen = new ArrayList<>();

        if ( cl.hasOption( DATABASE_PATH ) ) {
            this.settings.set( "output.database", "true" );
            String databasePath = cl.getOptionValue( DATABASE_PATH );
            if ( databasePath != null && ! databasePath.isEmpty() ) {
                this.settings.set( "output.database.location", databasePath );
                optionsChosen.add( "-" + DATABASE_PATH + " " + databasePath );
                hasDatabasePath = true;
            }
            else {
                errorMessages.add( "-d option database path: no value found" );
            }
        }
        else {
            errorMessages.add( "-d option database path is compulsory." );
        }

        if ( cl.hasOption( PROJECT_NAME ) ) {
            String projectName = cl.getOptionValue( PROJECT_NAME );
            if ( projectName != null && ! projectName.isEmpty() ) {
                this.settings.set( "project.name", projectName );
                optionsChosen.add( "-" + PROJECT_NAME + " " + projectName );
                hasProjectName = true;
            }
            else {
                errorMessages.add( "-p option program name: no value found" );
            }
        }
        else {
            errorMessages.add( "-p option program name is compulsory" );
        }

        if ( cl.hasOption( PROJECT_VERSION ) ) {
            String projectVersion = cl.getOptionValue( PROJECT_VERSION );
            if ( projectVersion != null && ! projectVersion.isEmpty() ) {
                this.settings.set( "project.version", projectVersion );
                optionsChosen.add( "-" + PROJECT_VERSION+ " " + projectVersion );
                hasProjectVersion = true;
            }
            else {
                errorMessages.add( "-v option program version: no value found " );
            }
        }
        else {
            errorMessages.add( "-v option program version is compulsory" );
        }

        // bail if the user hasn't grasped the fundamentals
        if ( ! ( hasDatabasePath && hasProjectName && hasProjectVersion ) ) {
            String errorMessage = errorMessages.stream()
                    .collect( Collectors.joining( System.lineSeparator() ) );
            throw new CommandLineArgumentException( errorMessage );
        }
           
        return optionsChosen;
    }
    
    private List<String> processBooleanOptions( CommandLine cl ) {
        List<String> booleanOptions = new ArrayList<>();
        // now for the optional flags
        if ( cl.hasOption( INCLUDE_GENERATED_CODE ) ) {
            this.settings.set( "analyse.generated", "true" );
            booleanOptions.add( "-" + INCLUDE_GENERATED_CODE );
        }

        if ( cl.hasOption( INCLUDE_TEST_CODE ) ) {
            this.settings.set( "analyse.tests", "true" );
            booleanOptions.add( "-" + INCLUDE_TEST_CODE );
        }

        // intt options are not compulsory
        if ( cl.hasOption( INTT_MODAL_EXPANSION ) ) {
            this.settings.set( "intt.modal.expansion", "true" );
            booleanOptions.add( "--" + INTT_MODAL_EXPANSION );
        }

        if ( cl.hasOption( INTT_RECURSIVE ) ) {  
            this.settings.set( "intt.recursive.split", "true" );
            booleanOptions.add( "--" + INTT_RECURSIVE );
        }

        // verbose logging
        if ( cl.hasOption( VERBOSE_LOGGING ) ) {
            LOGGER.setLevel( Level.INFO );
            this.settings.set( "log.level", "INFO" );
            booleanOptions.add( "-" + VERBOSE_LOGGING );
        }

        return booleanOptions;
    }
    
    private List<String> processThreadSettings( CommandLine cl ) 
            throws CommandLineArgumentException {
        // exit jim if options are given and values are missing or non-digit
        List<String> optionsSelected = new ArrayList<>();
        boolean hasMin = cl.hasOption( MIN_THREADS );
        boolean hasMax = cl.hasOption( MAX_THREADS );
        
        if ( hasMin && hasMax ) {
            // now are the values set?
            String minValueString = cl.getOptionValue( MIN_THREADS );
            String maxValueString = cl.getOptionValue( MAX_THREADS );

            if ( ( minValueString != null && ! minValueString.isEmpty() ) 
                    && ( maxValueString != null && ! maxValueString.isEmpty() ) ) {
                // so are values parsable into ints?
                int minValue = 0;
                int maxValue = 0;
                try {
                    minValue = Integer.parseUnsignedInt( minValueString );
                    maxValue = Integer.parseUnsignedInt( maxValueString );
                }
                catch ( NumberFormatException e ) {
                    // no, so throw a wobbly and die
                    throw new CommandLineArgumentException( 
                            "Require numeric value for number of threads." );
                }

                // yes -- so populate the values in setting and 
                // leave the detection of irrational values to QueueManager
                this.settings.set( "threads.minimum", 
                            Integer.toString( minValue ) );
                optionsSelected.add( "--" + MIN_THREADS + " " + minValue );
                this.settings.set( "threads.maximum", 
                            Integer.toString( maxValue ) );
                optionsSelected.add( "--" + MAX_THREADS + " " + maxValue );
            }
            else {
                // tell user a value is missing
                throw new CommandLineArgumentException( 
                        "Missing value for --" + MIN_THREADS 
                                + " or --" + MAX_THREADS + "." );
            }
        }
        else if ( hasMin || hasMax ) {
            // tell user both are required and bail
            throw new CommandLineArgumentException(
                    "When setting size of thread pool "
                            + "both --" + MIN_THREADS + " and --" 
                            + MAX_THREADS + " must be defined." );
        }
        
        return optionsSelected;
    }
    

    List<String> processUnparsedArguments( CommandLine cl ) 
            throws CommandLineArgumentException {

        List<String> paths = new ArrayList<>();
        // at least one spare argument value should remain which is the 
        // folder to start recursive descent from or a single Java file
        // unless behaviour is modified to allow multiple folders/files
        // to be specified.
        List<String> unparsedArguments = cl.getArgList();
        if ( unparsedArguments.isEmpty() ) {
            throw new CommandLineArgumentException( 
                    "Missing folder(s) or file(s) to process" );
        }

        // so we have a file name or a path or two to process
        // NB: more readable to use for/in than generating the exception in a lambda
        for ( String argument : unparsedArguments ){
            String cleanedString = sanitiseString( argument );
            File fileArgument = new File( cleanedString );
            if ( fileArgument.isFile() || fileArgument.isDirectory() ) {
                this.fileArgumentList.add( fileArgument );
                paths.add( "source path: " + fileArgument );
            }
            else {
                throw new CommandLineArgumentException( 
                        "Argument must be a valid filename or path: \"" 
                                + argument + "\"" );
            } 
        }
        
        return paths;
    }
    
    
    private void run() {
        // instantiate the entity store to set up the database connection
        EntityStoreSingleton entityStore = EntityStoreSingleton.getInstance();
        if ( entityStore == null ) {
            // there has been a problem connecting to the database
            // which has been logged, and we need to bail
            // before digging too big a hole
            LOGGER.warning( 
                    "Shutting down - encountered problems accessing database." );
            System.exit( 2 );
        }

        QueueManager queueManager = new QueueManager();

        queueManager.start( this.fileArgumentList ); // start recursive directory traversal
        // NB the QueueManager shuts down its threadpool automatically.

        // now close down the database
        entityStore.closeDown();
    }

    /**
     * Exits the program with a usage message.
     * <p>
     * Only called prior to execution following the processing of the command
     * line arguments. Consequently the hard call to System.exit() should not be
     * a problem as the program has not opened any databases or undertaken much
     * in the way of processing.
     * </p>
     * @param message a message stating why the program is exiting
     * @param options command line options set in Commons CLI
     */
    private void dieUsage( String message, Options options ) {
        if ( ! message.isEmpty() ) {
            System.out.println( "Fatal error: " + message );
        }
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp( 
                "jim -d database -p name -v version [flags] path [path]", 
                options );
        System.exit( 1 );
    }

    /**
     * Provides a mechanism to remove meta-characters and wildcards from strings that 
     * have been submitted by the user on the command line. Currently this 
     * is used to sanitise strings used as file names or paths, so we can only 
     * remove very few characters (only '^', ", ', `, * and ?).
     * @param incoming A string submitted by the user
     * @return A string with some metacharacters removed
     */
    private String sanitiseString( String incoming ) {
        // review this 
        // (1) -- is it removing the correct characters?
        // (2) -- should it log any changes?
        return incoming.replaceAll( "[\\^\\\"\\\'\\*\\?\\`]", "" );
    }
    
    // An exception for internal use.
    private class CommandLineArgumentException extends Exception {
        CommandLineArgumentException( String message ) {
            super( message );
        }
    }
}
