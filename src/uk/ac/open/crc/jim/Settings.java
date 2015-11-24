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

import java.util.HashMap;

/**
 * A central store for settings including those set on the command line
 * by the user.
 */
public class Settings {

    // --------- Class fields and methods -------

    /**
     * Singleton containing some default settings, and used to pass 
     * some values set on the command line.
     */
    private static Settings instance = null;

    /**
     * Retrieves the instance of the {@code Settings} registry.
     *
     * @return the instance of the {@code Settings} registry
     */
    public static synchronized Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }

        return instance;
    }

    // --------------- Instance methods & fields --------

    private final HashMap<String,String> settings;

    /**
     * Private constructor.
     */
    private Settings() {
        settings = new HashMap<>();

        // now add the defaults
        settings.put("output.csv", "false");
        settings.put("output.xml", "false");

        // control flags
        settings.put("analyse.generated", "false");
        settings.put("analyse.tests", "false");

        // for the thread pool
        settings.put("threads.minimum", "10");
        settings.put("threads.maximum", "20");
        settings.put("default.threads.minimum", "10");
        settings.put("default.threads.maximum", "20");
        
        // intt settings
        settings.put("intt.recursive.split", "false");
        settings.put("intt.modal.explansion", "false");
        
        // logging level -- warning by default
        settings.put("log.level", "WARNING");
    }


    /**
     * Retrieves the string stored with the name provided in {@code key}.
     *
     * @param key a {@code String} containing a setting key
     * @return  the value of the string associated with the key, or {@code null}
     *          if the key does not exist.
     */
    public synchronized String get(String key) {
        return this.settings.get(key);
    }

    /**
     * Sets the value associated with the {@code key}.
     *
     * @param key a key
     * @param value a value associated with the key
     */
    public synchronized void set(String key, String value) {
        settings.put(key, value);
    }
}
