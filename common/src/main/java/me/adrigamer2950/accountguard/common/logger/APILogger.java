package me.adrigamer2950.accountguard.common.logger;

import java.util.logging.Logger;

/**
 * Main Logger class
 */
public class APILogger {

    private final String name;
    private final APILogger parent;

    private final Logger javaLogger;

    public APILogger(String name, APILogger parent) {
        this.name = name;
        this.parent = parent;

        this.javaLogger = Logger.getGlobal();
    }

    public void info(String message) {
        this.javaLogger.info(
                String.format("[%s] %s",
                        parent == null ? name : parent.name + " - " + name,
                        message
                )
        );
    }

    public void warn(String message) {
        this.javaLogger.warning(
                String.format("&r[%s&r] %s",
                        parent == null ? name : parent.name + " - " + name,
                        message
                )
        );
    }

    public void error(String message) {
        this.javaLogger.severe(
                String.format("&r[%s&r] %s",
                        parent == null ? name : parent.name + " - " + name,
                        message
                )
        );
    }
}
