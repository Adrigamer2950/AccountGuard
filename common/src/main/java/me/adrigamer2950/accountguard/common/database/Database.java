package me.adrigamer2950.accountguard.common.database;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public abstract class Database {

    protected final HashMap<UUID, Set<String>> ips = new HashMap<>();

    public abstract void loadData();

    public abstract void saveData();

    public Set<String> getIPs(UUID uuid) {
        return this.ips.getOrDefault(uuid, new HashSet<>());
    }

    public boolean addIP(UUID uuid, String ip) {
        Set<String> ips = this.getIPs(uuid);

        if (ips.contains(ip))
            return false;

        ips.add(ip);
        this.ips.put(uuid, ips);

        return true;
    }

    public boolean removeIP(UUID uuid, String ip) {
        Set<String> ips = this.getIPs(uuid);

        if (!ips.contains(ip))
            return false;

        ips.remove(ip);
        this.ips.put(uuid, ips);

        return true;
    }

    public boolean hasIP(UUID uuid, String ip) {
        return this.getIPs(uuid).contains(ip);
    }
}
