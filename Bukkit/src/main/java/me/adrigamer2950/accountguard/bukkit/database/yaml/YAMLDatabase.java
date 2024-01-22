package me.adrigamer2950.accountguard.bukkit.database.yaml;

import me.adrigamer2950.accountguard.bukkit.AGBukkit;
import me.adrigamer2950.accountguard.bukkit.database.Database;
import me.adrigamer2950.accountguard.bukkit.database.DatabaseType;
import me.adrigamer2950.adriapi.api.config.yaml.YamlConfig;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

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
        this.yaml.getYaml().setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(Objects.requireNonNull(this.plugin.getResource("data.yml")), StandardCharsets.UTF_8)));

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
        Set<String> ips = this.ips.get(uuid);

        if (ips == null) return;

        if (ips.contains(ip)) return;

        ips.add(ip);

        this.ips.put(uuid, ips);
    }

    @Override
    public void removeIP(UUID uuid, String ip) {
        Set<String> ips = this.ips.get(uuid);

        if (ips == null) return;

        ips.remove(ip);

        this.ips.put(uuid, ips);
    }
}
