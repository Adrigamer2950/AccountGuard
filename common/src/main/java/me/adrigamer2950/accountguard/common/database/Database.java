package me.adrigamer2950.accountguard.common.database;

import me.adrigamer2950.accountguard.common.AccountGuard;
import me.adrigamer2950.accountguard.common.logger.SubLogger;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public abstract class Database {

    protected final SubLogger LOGGER;

    protected final AccountGuard plugin;
    protected final DatabaseType type;

    protected final HashMap<UUID, Set<String>> ips;

    protected Database(AccountGuard plugin, DatabaseType type) {
        this.LOGGER = new SubLogger(type.name() + " Database", plugin.getAPILogger());

        this.plugin = plugin;
        this.type = type;

        this.ips = new HashMap<>();
    }

    public abstract void loadData();

    public abstract void saveData();

    public abstract void addIP(UUID uuid, String ip);

    public abstract void removeIP(UUID uuid, String ip);

    public abstract boolean hasIP(UUID uuid, String ip);

    public abstract Set<String> getIPs(UUID uuid);
}
