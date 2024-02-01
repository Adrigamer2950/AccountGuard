package me.adrigamer2950.accountguard.common.database;

import me.adrigamer2950.accountguard.common.AccountGuard;
import me.adrigamer2950.accountguard.common.logger.SubLogger;

import java.util.HashMap;
import java.util.HashSet;
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

    public void addIP(UUID uuid, String ip) {
        Set<String> ips = this.getIPs(uuid);

        if (ips.contains(ip))
            return;

        ips.add(ip);

        this.ips.put(uuid, ips);
    }

    public void removeIP(UUID uuid, String ip) {
        Set<String> ips = this.getIPs(uuid);

        if (!ips.contains(ip))
            return;

        ips.remove(ip);

        this.ips.put(uuid, ips);
    }

    public boolean hasIP(UUID uuid, String ip) {
        if(this.ips == null) return false;

        if(this.ips.get(uuid) == null) return false;

        return this.ips.get(uuid).contains(ip);
    }

    public Set<String> getIPs(UUID uuid) {
        if(this.ips == null)
            return new HashSet<>();

        if(this.ips.get(uuid) == null)
            return new HashSet<>();

        return this.ips.get(uuid);
    }
}
