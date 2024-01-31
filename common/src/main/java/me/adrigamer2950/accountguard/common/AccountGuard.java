package me.adrigamer2950.accountguard.common;

import me.adrigamer2950.accountguard.common.config.Config;
import me.adrigamer2950.accountguard.common.database.Database;
import me.adrigamer2950.accountguard.common.logger.APILogger;

public interface AccountGuard {

    APILogger getAPILogger();

    Database getDatabase();

    void reloadConfig();

    void reloadMessages();
}
