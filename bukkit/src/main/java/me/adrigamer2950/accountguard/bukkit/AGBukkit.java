package me.adrigamer2950.accountguard.bukkit;

import dev.dejvokep.boostedyaml.YamlDocument;
import lombok.SneakyThrows;
import me.adrigamer2950.accountguard.bukkit.commands.MainCommand;
import me.adrigamer2950.accountguard.bukkit.database.yaml.YAMLDatabase;
import me.adrigamer2950.accountguard.bukkit.listeners.PlayerListener;
import me.adrigamer2950.accountguard.common.config.Config;
import me.adrigamer2950.accountguard.common.database.Database;
import me.adrigamer2950.adriapi.api.APIPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public final class AGBukkit extends APIPlugin {

    public YamlDocument configYaml;

    public Config config;
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
                case YAML -> this.database = new YAMLDatabase(
                        new File(this.getDataFolder(), "data.yml")
                );
                default -> throw new IllegalArgumentException("Other types of databases are not available for now");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onPostLoad() {
        registerCommand(new MainCommand(this, "ag"));
        registerListener(new PlayerListener(this));

        getApiLogger().info("&aEnabled!");
    }

    @Override
    public void onUnload() {
        this.database.saveData();

        this.config = null;
        this.database = null;

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
}
