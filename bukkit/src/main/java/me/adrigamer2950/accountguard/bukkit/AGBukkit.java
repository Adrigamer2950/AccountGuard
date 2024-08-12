package me.adrigamer2950.accountguard.bukkit;

import dev.dejvokep.boostedyaml.YamlDocument;
import me.adrigamer2950.accountguard.bukkit.database.yaml.YAMLDatabase;
import me.adrigamer2950.accountguard.common.config.Config;
import me.adrigamer2950.accountguard.common.database.Database;
import me.adrigamer2950.adriapi.api.APIPlugin;

import java.io.File;
import java.io.IOException;

public final class AGBukkit extends APIPlugin {

    public Config config;
    public Database database;

    @Override
    public void onPreLoad() {
        getApiLogger().info("&6Loading...");

        try {
            YamlDocument configYaml = YamlDocument.create(
                    new File(this.getDataFolder(), "config.yml"),
                    getResource("config.yml")
            );

            this.config = new Config(
                    new Config.Database(
                            Config.Database.Type.valueOf(configYaml.getString("database.driver"))
                    )
            );

            this.database = switch (this.config.database().driver()) {
                case YAML -> new YAMLDatabase(
                        new File(this.getDataFolder(), "data.yml")
                );
            };
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onPostLoad() {
        getApiLogger().info("&aEnabled!");
    }

    @Override
    public void onUnload() {
        this.config = null;
        this.database = null;

        getApiLogger().info("&cDisabled!");
    }
}
