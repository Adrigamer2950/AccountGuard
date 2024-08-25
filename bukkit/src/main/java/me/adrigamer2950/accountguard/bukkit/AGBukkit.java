package me.adrigamer2950.accountguard.bukkit;

import dev.dejvokep.boostedyaml.YamlDocument;
import lombok.SneakyThrows;
import me.adrigamer2950.accountguard.api.AccountGuard;
import me.adrigamer2950.accountguard.api.AccountGuardProvider;
import me.adrigamer2950.accountguard.bukkit.commands.MainCommand;
import me.adrigamer2950.accountguard.common.database.yaml.WhitelistDatabase;
import me.adrigamer2950.accountguard.bukkit.listeners.PlayerListener;
import me.adrigamer2950.accountguard.common.config.Config;
import me.adrigamer2950.accountguard.common.database.Database;
import me.adrigamer2950.accountguard.common.messages.Messages;
import me.adrigamer2950.accountguard.common.util.IPUtil;
import me.adrigamer2950.adriapi.api.APIPlugin;

import java.io.File;
import java.io.IOException;
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

        try {
            this.configYaml = YamlDocument.create(
                    new File(this.getDataFolder(), "config.yml"),
                    Objects.requireNonNull(getResource("config.yml"))
            );

            this.reloadConfig();

            switch (this.config.database().driver()) {
                case YAML -> this.database = new WhitelistDatabase(
                        new File(this.getDataFolder().toPath().resolve("data").toFile(), "whitelist.yml")
                );
                default -> throw new IllegalArgumentException("Other types of databases are not available for now");
            }

            this.messagesYaml = YamlDocument.create(
                    new File(this.getDataFolder(), "messages.yml"),
                    Objects.requireNonNull(getResource("messages.yml"))
            );

            this.reloadMessages();
        } catch (IOException e) {
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
        this.database.saveData();

        this.config = null;
        this.database = null;

        AccountGuardProvider.unRegister();

        getApiLogger().info("&cDisabled!");
    }

    @SneakyThrows
    public void reloadConfig() {
        this.configYaml.reload();

        this.config = new Config(
                new Config.Database(
                        Config.Database.Type.valueOf(configYaml.getString("database.driver"))
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
