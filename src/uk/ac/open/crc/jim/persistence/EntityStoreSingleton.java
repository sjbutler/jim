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

package uk.ac.open.crc.jim.persistence;

import java.sql.SQLException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.open.crc.jim.Settings;
import uk.ac.open.crc.jimdb.DatabaseManager;
import uk.ac.open.crc.jimdb.DatabaseWriter;
import uk.ac.open.crc.jimdb.DatabaseWriterFactory;
import uk.ac.open.crc.jimdb.RawProgramEntity;

/**
 * Acts as a wrapper for the jimdb API and manages the instantiation of the
 * database.
 *
 */
public class EntityStoreSingleton implements EntityStore {

    private static final int MINIMUM_THREADS = 1;
    private static final int MAXIMUM_THREADS = 1;

    private static final Logger LOGGER = 
            LoggerFactory.getLogger( EntityStoreSingleton.class );

    private static EntityStoreSingleton instance = null;

    public static EntityStoreSingleton getInstance () {
        if ( instance == null ) {
            instance = new EntityStoreSingleton();

            // now set up the database
            try {
                instance.openDatabase();
            }
            catch ( SQLException sqlEx ) {
                LOGGER.error(
                        "Error occurred while attempting to open database: "
                        + "{}\nSQL state: {}\nError code: {}",
                        sqlEx.getMessage(),
                        sqlEx.getSQLState(),
                        sqlEx.getErrorCode() );
                // set the return value for the caller.
                instance = null;
            }
        }

        return instance;
    }

    private final String databaseLocation;

    // queue
    private final ThreadPoolExecutor writerExecutor;

    private EntityStoreSingleton () {
        this.databaseLocation = 
                Settings.getInstance().get( "output.database.location" );
        this.writerExecutor = new ThreadPoolExecutor(
                MINIMUM_THREADS, //min
                MAXIMUM_THREADS, //max
                50000L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>() );
    }

    void openDatabase () throws SQLException {
        DatabaseManager.initialiseAndCreate( this.databaseLocation );
        // set the intt factory options -- only bother with the non-defaults atm
        boolean recursiveSplits
                = Boolean.parseBoolean( Settings.getInstance().get( "intt.recursive.split" ) );
        boolean modalExpansion
                = Boolean.parseBoolean( Settings.getInstance().get( "intt.modal.explansion" ) );
        if ( recursiveSplits ) {
            DatabaseManager.setInttRecursiveSplitOn();
        }
        if ( modalExpansion ) {
            DatabaseManager.setInttModalExpansionOn();
        }
        // specific to this run
        DatabaseManager.setProjectName( Settings.getInstance().get( "project.name" ) );
        DatabaseManager.setProjectVersion( Settings.getInstance().get( "project.version" ) );
        DatabaseManager.setLoggingLevel( Settings.getInstance().get( "log.level" ) );
    }

    @Override
    public synchronized void add ( RawProgramEntity programEntity ) {
        this.writerExecutor.execute( new Writer( programEntity ) );
    }

    @Override
    public void closeDown () {
        this.writerExecutor.shutdown();

        // now check if the executor has terminated.
        try {
            while ( !this.writerExecutor.awaitTermination( 3, TimeUnit.SECONDS ) ) {
                int running = this.writerExecutor.getActiveCount();
                int queued = this.writerExecutor.getQueue().size();
                long completed = this.writerExecutor.getCompletedTaskCount();
                LOGGER.info(
                        "Tasks queued: {}\n"
                        + "Tasks running: {}\n"
                        + "Percentage completed: {}",
                        queued,
                        running,
                        (int) ( completed * 100 / ( running + completed + queued ) ) );
            }
        }
        catch ( InterruptedException e ) {
            LOGGER.warn(
                    "Process interrupted, shutting down database.\n"
                    + e.getMessage() );
            this.writerExecutor.shutdownNow();
        }
    }

    private class Writer implements Runnable {

        RawProgramEntity programEntity;

        Writer ( RawProgramEntity programEntity ) {
            this.programEntity = programEntity;
        }

        @Override
        public void run () {
            DatabaseWriter writer = DatabaseWriterFactory.create();
            writer.store( this.programEntity );
        }
    }
}
