package me.adrigamer2950.accountguard.bukkit.database;

import me.adrigamer2950.accountguard.bukkit.AGBukkit;
import me.adrigamer2950.accountguard.bukkit.database.yaml.YAMLDatabase;
import me.adrigamer2950.adriapi.api.logger.SubLogger;

import java.util.HashMap;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public abstract class Database {

    protected final SubLogger LOGGER;

    protected final AGBukkit plugin;
    protected final DatabaseType type;

    protected final HashMap<UUID, Set<String>> ips;

    protected Database(AGBukkit plugin, DatabaseType type) {
        this.LOGGER = new SubLogger("Database", plugin.LOGGER);

        this.plugin = plugin;
        this.type = type;

        this.ips = new HashMap<>();
    }

    public static Database getDatabase(AGBukkit plugin, DatabaseType type) {
        Objects.requireNonNull(plugin, "Plugin is null!");
        Objects.requireNonNull(type, "Database type is null!");

        switch (type) {
            case YAML -> {
                return new YAMLDatabase(plugin, type);
            }
            default -> {
                return null;
            }
        }
    }

    public abstract void loadData();

    public abstract void saveData();

    public abstract void addIP(UUID uuid, String ip);

    public abstract void removeIP(UUID uuid, String ip);

    public abstract boolean hasIP(UUID uuid, String ip);

    public abstract Set<String> getIPs(UUID uuid);
}
