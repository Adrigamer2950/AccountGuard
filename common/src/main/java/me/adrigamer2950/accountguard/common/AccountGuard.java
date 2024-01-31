package me.adrigamer2950.accountguard.common;

import me.adrigamer2950.accountguard.common.logger.APILogger;

public interface AccountGuard {

    APILogger getAPILogger();

    void reloadConfig();

    void reloadMessages();
}
