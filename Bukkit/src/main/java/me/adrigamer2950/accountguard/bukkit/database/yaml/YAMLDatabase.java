package me.adrigamer2950.accountguard.bukkit.database.yaml;

import me.adrigamer2950.accountguard.bukkit.AGBukkit;
import me.adrigamer2950.accountguard.common.database.Database;
import me.adrigamer2950.accountguard.common.database.DatabaseType;
import me.adrigamer2950.adriapi.api.config.yaml.YamlConfig;

import java.io.IOException;
import java.util.*;

public class YAMLDatabase extends Database {

    private final YamlConfig yaml;

    public YAMLDatabase(AGBukkit plugin, DatabaseType type) {
        super(plugin, type);

        this.yaml = new YamlConfig(plugin.getDataFolder().getAbsolutePath(), "data", plugin, true, false);

        loadData();
    }

    @Override
    public void loadData() {
        try {
            this.yaml.loadConfig();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (String uuidS : this.yaml.getYaml().getRoot().getKeys(false)) {
            UUID uuid;
            try {
                uuid = UUID.fromString(uuidS);
            } catch (IllegalArgumentException e) {
                continue;
            }

            Set<String> ips = new HashSet<>(this.yaml.getYaml().getStringList(uuidS));

            this.ips.put(uuid, ips);
        }
    }

    @Override
    public void saveData() {
        this.ips.forEach((uuid, ips) -> {
            if (ips.isEmpty()) this.ips.remove(uuid);
            else this.yaml.getYaml().set(uuid.toString(), ips.stream().toList());
        });

        try {
            this.yaml.saveConfig();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addIP(UUID uuid, String ip) {
        Set<String> ips = this.getIPs(uuid);

        if (ips.contains(ip))
            return;

        ips.add(ip);

        this.ips.put(uuid, ips);
    }

    @Override
    public void removeIP(UUID uuid, String ip) {
        Set<String> ips = this.getIPs(uuid);

        if (!ips.contains(ip))
            return;

        ips.remove(ip);

        this.ips.put(uuid, ips);
    }

    @Override
    public boolean hasIP(UUID uuid, String ip) {
        if(this.ips == null) return false;

        if(this.ips.get(uuid) == null) return false;

        return this.ips.get(uuid).contains(ip);
    }

    @Override
    public Set<String> getIPs(UUID uuid) {
        if(this.ips == null)
            return new HashSet<>();

        if(this.ips.get(uuid) == null)
            return new HashSet<>();


        return this.ips.get(uuid);
    }
}
