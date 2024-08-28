package me.adrigamer2950.accountguard.bukkit;

import dev.dejvokep.boostedyaml.YamlDocument;
import lombok.SneakyThrows;
import me.adrigamer2950.accountguard.api.AccountGuard;
import me.adrigamer2950.accountguard.api.AccountGuardProvider;
import me.adrigamer2950.accountguard.bukkit.commands.MainCommand;
import me.adrigamer2950.accountguard.common.AGLoader;
import me.adrigamer2950.accountguard.common.database.h2.WhitelistH2Database;
import me.adrigamer2950.accountguard.common.database.mysql.WhitelistMySQLDatabase;
import me.adrigamer2950.accountguard.common.database.sql.SqlLikeDatabase;
import me.adrigamer2950.accountguard.common.database.sqlite.WhitelistSQLiteDatabase;
import me.adrigamer2950.accountguard.common.database.yaml.WhitelistYAMLDatabase;
import me.adrigamer2950.accountguard.bukkit.listeners.PlayerListener;
import me.adrigamer2950.accountguard.common.config.Config;
import me.adrigamer2950.accountguard.common.database.Database;
import me.adrigamer2950.accountguard.common.messages.Messages;
import me.adrigamer2950.accountguard.common.util.IPUtil;
import me.adrigamer2950.adriapi.api.APIPlugin;
import net.byteflux.libby.BukkitLibraryManager;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public final class AGBukkit extends APIPlugin implements AccountGuard {

    private YamlDocument configYaml;
    private YamlDocument messagesYaml;

    public Config config;
    public Messages messages;
    public Database database;

    @Override
    public void onPreLoad() {
        getApiLogger().info("&6Loading...");

        AGLoader.loadLibraries(new BukkitLibraryManager(this));

        try {
            this.configYaml = YamlDocument.create(
                    new File(this.getDataFolder(), "config.yml"),
                    Objects.requireNonNull(getResource("config.yml"))
            );

            this.reloadConfig();

            switch (this.config.database().driver()) {
                case YAML -> this.database = new WhitelistYAMLDatabase(
                        new File(this.getDataFolder().toPath().resolve("data").toFile(), "whitelist.yml")
                );
                case H2 -> this.database = new WhitelistH2Database(this.getDataFolder().toPath().resolve("data").toAbsolutePath().toString());
                case SQLITE -> this.database = new WhitelistSQLiteDatabase(this.getDataFolder().toPath().resolve("data").toAbsolutePath().toString());
                case MYSQL -> this.database = new WhitelistMySQLDatabase(
                        "jdbc:mysql://%s:%s/%s".formatted(
                                this.config.database().mysql().hostname(),
                                this.config.database().mysql().port(),
                                this.config.database().mysql().database()
                        ),
                        this.config.database().mysql().username(),
                        this.config.database().mysql().password()
                );
            }

            this.messagesYaml = YamlDocument.create(
                    new File(this.getDataFolder(), "messages.yml"),
                    Objects.requireNonNull(getResource("messages.yml"))
            );

            this.reloadMessages();
        } catch (IOException | SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onPostLoad() {
        registerCommand(new MainCommand(this, "ag"));
        registerListener(new PlayerListener(this));

        AccountGuardProvider.register(this);

        getApiLogger().info("&aEnabled!");
    }

    @Override
    public void onUnload() {
        if (this.database != null) {
            this.database.saveData();

            if (this.database instanceof SqlLikeDatabase)
                try {
                    ((SqlLikeDatabase) this.database).closeConnection();
                } catch (SQLException ignored) {}
        }

        this.config = null;
        this.configYaml = null;
        this.messages = null;
        this.messagesYaml = null;
        this.database = null;

        AccountGuardProvider.unRegister();

        getApiLogger().info("&cDisabled!");
    }

    @SneakyThrows
    public void reloadConfig() {
        this.configYaml.reload();

        this.config = new Config(
                configYaml.getString("prefix") + " ",
                new Config.Database(
                        Config.Database.Type.valueOf(configYaml.getString("database.driver").toUpperCase()),
                        new Config.Database.MySQL(
                                configYaml.getString("database.mysql.hostname"),
                                configYaml.getInt("database.mysql.port"),
                                configYaml.getString("database.mysql.database"),
                                configYaml.getString("database.mysql.username"),
                                configYaml.getString("database.mysql.password")
                        )
                )
        );
    }

    @SneakyThrows
    public void reloadMessages() {
        this.messagesYaml.reload();

        this.messages = new Messages(
                this.messagesYaml.getString("NO_PERMISSION"),
                this.messagesYaml.getString("NO_CHANGE_OTHER_WHITELIST_PERMISSION"),
                this.messagesYaml.getString("NO_VIEW_OTHER_WHITELIST_PERMISSION"),
                this.messagesYaml.getString("IP_NOT_SPECIFIED"),
                this.messagesYaml.getString("INVALID_IP"),
                this.messagesYaml.getString("PLAYER_NAME_NOT_SPECIFIED_FROM_CONSOLE"),
                this.messagesYaml.getString("PLAYER_NOT_FOUND"),
                this.messagesYaml.getString("IP_ADDED_IN_WHITELIST"),
                this.messagesYaml.getString("IP_ALREADY_IN_WHITELIST"),
                this.messagesYaml.getString("IP_REMOVED_FROM_WHITELIST"),
                this.messagesYaml.getString("IP_NOT_IN_WHITELIST"),
                this.messagesYaml.getString("WHITELIST_IP_LIST"),
                this.messagesYaml.getString("RELOAD_MESSAGE")
        );
    }

    @Override
    protected int getBStatsServiceId() {
        return 20823;
    }

    @Override
    public Set<String> getIPs(UUID uuid) {
        return database.getIPs(uuid);
    }

    @Override
    public boolean isValidIP(String ip) {
        return IPUtil.checkIP(ip);
    }

    @Override
    public boolean addIP(UUID uuid, String ip) {
        return database.addIP(uuid, ip);
    }

    @Override
    public boolean removeIP(UUID uuid, String ip) {
        return database.removeIP(uuid, ip);
    }

    @Override
    public boolean hasIP(UUID uuid, String ip) {
        return database.hasIP(uuid, ip);
    }
}
