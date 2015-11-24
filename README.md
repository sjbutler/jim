# README

Java Identifier Miner - abbreviated as JIM - is an application that extracts 
identifier names from Java source code and stores them in a database making 
both the identifier names and their tokens available with metadata about their 
location in source code. The resulting database can then be used as a 
source of information for empirical research that reduces the overhead
of repeatedly parsing large bodies of source code. 

## Requirements

JIM requires Java v8 to run. JIM runs best on computers with multiple cores 
and 4 GB or more of RAM, and is known to run under Linux, MacOS X and Windows.

## Installation

JIM is available from https://github.com/sjbutler/jim as source code and in 
binary form (follow the 'release' link). Building from source code (currently) 
requires some work as there are no build scripts available. They will be 
available soon. There are binary releases available that contain a zip archive 
of the jar file and the dependencies. The archive is named jim-0.6.x.zip 
Unzip the file to a suitable location in your file system. JIM can be invoked 
from the command line with the command: java -jar [path_to_jar]jim.jar

## Running

JIM takes the following  command line arguments:

 -d   | Specifies the path to an exisiting database, or the path to a database to be created. -d is a compulsory argument
 -g   | Includes the parsing of generated source code. (optional)
 --min| The minimum number of threads to use for the parsers. (default -min=10)
 --max| The maximum number of threads to use for the parsers. (default -max=20)
      | NB Both --min and --max are optional, but the relationship min >= 1 and max >= min must be true
 -p   | The name of the project. This is recorded in the database to facilitate access to data for a specific project in combination with the value of the -v flag. -p is compulsory and the string cannot contain spaces or hyphens.
 -t   | Includes test files for parsing. (optional)
 -v   | Specifies a version string for the project specified using -p. -v is compulsory and the string cannot contain spaces or hyphens.
 --intt-recursive | Causes the name tokeniser to try to tokenise strings it doesn't recognise that contain no typographical token boundaries, e.g. Outputfilename is analysed and tokenised
 --intt-modal-expansion | Causes the identifier name tokeniser to expand negated modal verbs, e.g. 'cant' is exapnded to 'can not'
 
The command is followed by a path for JIM to search for Java source code. 
For example:

        java -Xmx3g -jar jim.jar -d=common -p=netbeans -v=6.9.1 
              -t sources/netbeans-6.9.1-src/

NB the options -d, -p and -v are required as is a path to source code 
to extract names from.

Any option flag that doesn't take a value switches a feature on, e.g. -t
includes test files and -g includes generated files.

JIM can be quite memory hungry, especially for larger source code projects, 
so allocating a lot of heap using the -Xmx java option may help.

## Database

JIM creates databases using Apache Derby. Details of the database and the 
version of Derby used can be found in the file README.database.

## Copyright and Licenses

JIM is Copyright (C) 2009-2015 The Open University and is released as open 
source software under the terms of the Apapche Licence v2.0, a copy of which 
is included with the source code.

Any use of JIM to support research should be attributed by referencing:  
[Reference to be confirmed].

JIM incorporates the following elements: 

* Modified versions of Java grammars distributed with JavaCC 
  (http://javacc.java.net/). The grammars are Copyright (C) 2006, Sun 
  Microsystems, Inc. All rights reserved, and are made available under the 
  following terms:

  Redistribution and use in source and binary forms, with or without 
  modification, are permitted provided that the following conditions are met:

  - Redistributions of source code must retain the above copyright notice, 
    this list of conditions and the following disclaimer.
  - Redistributions in binary form must reproduce the above copyright notice, 
    this list of conditions and the following disclaimer in the documentation 
    and/or other materials provided with the distribution.
  - Neither the name of the Sun Microsystems, Inc. nor the names of its 
    contributors may be used to endorse or promote products derived from this 
    software without specific prior written permission.

  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
  AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
  ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE 
  LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
  SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
  INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
  CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
  POSSIBILITY OF SUCH DAMAGE.

* The Java v7 grammar available from 
  https://github.com/antlr/grammars-v4/tree/master/java which is Copyright (C) 
  2013 Terence Parr, Sam Harwell All rights reserved, and licensed under the 
  terms of the BSD Licence.

* The Apache Derby database library, which is Copyright (C) 2004–2015 The 
  Apache Software Foundation and is released under the terms of the Apache 
  Licence, Version 2.0.

* The Apache Commons CLI command line parser, which is Copyright (C) 2002-2015 
  The Apache Software Foundation and is released under the terms of the Apache 
  Licence, Version 2.0.

* JIMdb, a persistence library for JIM, which is Copyright (C) 2011-2015 The 
  Open University and is released under the terms of the Apache Licence, 
  Version 2.0. JIMdb is available from https://github.com/sjbutler/jimdb 
  
* INTT, an identifier name tokenisation library, which is Copyright (C) 2011-2015 
  The Open University and is released under the terms of the Apache Licence, 
  Version 2.0. INTT is available from https://github.com/sjbutler/intt  

JIM is distributed from one specified location. From November 2015 this will be 
https://github.com/sjbutler/jim  
A few older binary versions are available from http://www.facetus.org.uk/jim
The older versions are not supported. Versions of JIM found on other servers 
are not supported.

## Known issues

 * The recording of types is under development. While it is consistently 
   accurate, the level of detail recorded varies. Type names are recorded as 
   either fully qualified names, or as the type name's identifier name 
   depending on the information available to the parser.
 * Writing to the database is slow. JIM uses multiple threads to parse source 
   code, but only a single thread to write to the database. Improvements are 
   planned. 
 * JIM prints a lot of logging messages that are not always relevant to 
   the user.

## Contact

Simon Butler <simon [at] facetus [dot] org [dot] uk

