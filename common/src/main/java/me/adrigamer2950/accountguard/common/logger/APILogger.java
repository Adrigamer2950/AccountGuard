package me.adrigamer2950.accountguard.common.logger;

import java.util.logging.Logger;

/**
 * Main Logger class
 */
public class APILogger extends Logger {

    private final String name;

    public APILogger(String name, APILogger parent) {
        super(parent == null ? name : parent.name + " - " + name, null);

        this.name = name;
    }
}
