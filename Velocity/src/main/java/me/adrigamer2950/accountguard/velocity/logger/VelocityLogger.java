package me.adrigamer2950.accountguard.velocity.logger;

import me.adrigamer2950.accountguard.common.logger.APILogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VelocityLogger extends APILogger {

    private final Logger logger;

    public VelocityLogger(String name, APILogger parent) {
        super(name, parent);

        this.logger = LoggerFactory.getLogger(parent == null ? name : parent.name + " - " + name);
    }

    @Override
    public void info(String msg) {
        this.logger.info(msg);
    }
}
