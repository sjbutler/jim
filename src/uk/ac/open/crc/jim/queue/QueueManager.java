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

import java.io.File;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.open.crc.jim.Settings;

/**
 * A multi-threaded queueing system that coordinates the search for Java
 * files and the parsers that extract data.
 */
public class QueueManager {

    private static final Logger LOGGER
            = LoggerFactory.getLogger( QueueManager.class );
    // default values for the upper and lower population of the thread pool
    // TODO - reconsider the order in which the following are assigned
    // might the assignments be better off in the constructor? rather than as 
    // static calls.
    private final int MINIMUM_THREADS
            = Integer.parseInt( Settings.getInstance().get( "default.threads.minimum" ) );
    private final int MAXIMUM_THREADS
            = Integer.parseInt( Settings.getInstance().get( "default.threads.maximum" ) );

    private final ThreadPoolExecutor readerExecutor;

    private final boolean analyseTests;

    /**
     * Constructs a new QueueManager. The default behaviour is for a
     * minimum of 10 threads and a maximum of 20.
     *
     */
    public QueueManager () {
        // recover the values set on the command line (if any)
        int minimum
                = Integer.parseInt( Settings.getInstance().get( "threads.minimum" ) );
        int maximum
                = Integer.parseInt( Settings.getInstance().get( "threads.maximum" ) );
        // trap out stupid arguments
        if ( minimum <= 0 || ( minimum > maximum ) ) {
            minimum = MINIMUM_THREADS;
            maximum = MAXIMUM_THREADS;

            LOGGER.info( "minimum and maximum size of threadpool set to "
                    + "defaults because of irrational values set on command line." );
        }
        this.readerExecutor = new ThreadPoolExecutor(
                minimum, //min
                maximum, //max
                50000L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>() );
        this.analyseTests
                = Boolean.parseBoolean( Settings.getInstance().get( "analyse.tests" ) );
    }

    /**
     * Initiates the process of recovering file names from the system
     * and parsing files to extract data.
     *
     * @param files a {@code List} of {@code File}s
     */
    public void start ( List<File> files ) {
        // recursive descent of file system that places files
        // in concurrent queue for processing
        files.stream().forEach( (file) -> {
            processFileNames( file );
        } );

        this.readerExecutor.shutdown();

        // now wait for the thread pool to complete the jobs
        try {
            while ( !this.readerExecutor.awaitTermination( 2, TimeUnit.SECONDS ) ) {
                int running = this.readerExecutor.getActiveCount();
                int queued = this.readerExecutor.getQueue().size();
                long completed = this.readerExecutor.getCompletedTaskCount();
                LOGGER.info(
                        "Tasks queued: {}\nTasks running: {}\nPercentage completed: {}",
                        queued,
                        running,
                        (int) ( completed * 100 / ( running + completed + queued ) ) );
            }
        }
        catch ( InterruptedException e ) {
            LOGGER.warn(
                    "Process interrupted.\n"
                    + e.getMessage() );
            this.readerExecutor.shutdownNow();
        }
    }

    /**
     * Undertakes a recursive descent of a file tree adding files to the queue.
     *
     *
     * @param fileObject the {@code File} to start from.
     *                   Could be a directory, could be a file.
     */
    private void processFileNames ( File fileObject ) {
        if ( !fileObject.isHidden() ) { // prevents us following .svn and that ilk
            if ( fileObject.isDirectory() ) {
                File[] files = fileObject.listFiles();
                for ( File file : files ) {
                    // now exclude test directories, if required
                    if ( !this.analyseTests ) {
                        if ( file.isDirectory()
                                && ( file.getName().equalsIgnoreCase( "test" )
                                || file.getName().equalsIgnoreCase( "tests" ) ) ) {
                            continue;
                        }
                    }
                    // add directory name filtering here
                    // recurse down directories
                    processFileNames( file );
                }
            }
            else {
                // package-info.java are excluded because they are not
                // well-formed Java files
                if ( fileObject.toString().endsWith( ".java" )
                        && !fileObject.toString().endsWith( "package-info.java" ) ) {
                    this.readerExecutor.execute( new FileReader( fileObject ) );
                }
            }
        }
    }

}
