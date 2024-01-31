package me.adrigamer2950.accountguard.bukkit.config;

import me.adrigamer2950.accountguard.common.config.Config;
import me.adrigamer2950.adriapi.api.config.yaml.YamlConfig;

public final class BukkitConfig extends Config {

    public BukkitConfig(YamlConfig yaml) {
        super(yaml.getYaml().getString("prefix"),
                new DatabaseConfig(
                        yaml.getYaml().getString("database.driver"),
                        yaml.getYaml().getString("database.mysql.hostname"),
                        yaml.getYaml().getInt("database.mysql.port"),
                        yaml.getYaml().getString("database.mysql.database"),
                        yaml.getYaml().getString("database.mysql.username"),
                        yaml.getYaml().getString("database.mysql.password"),
                        yaml.getYaml().getString("database.redis.hostname"),
                        yaml.getYaml().getInt("database.redis.port"),
                        yaml.getYaml().getString("database.redis.username"),
                        yaml.getYaml().getString("database.redis.password")
                )
        );
    }
}
