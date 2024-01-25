package me.adrigamer2950.accountguard.bukkit.config;

import me.adrigamer2950.adriapi.api.config.yaml.YamlConfig;

public final class Config {

    public final String PREFIX;
    public final Database Database;

    public Config(YamlConfig yaml) {
        this.PREFIX = yaml.getYaml().getString("prefix");

        this.Database = new Database(yaml);
    }

    private static final class Database {

        public final String DRIVER;

        public final String MYSQL_HOSTNAME;
        public final int MYSQL_PORT;
        public final String MYSQL_DATABASE;
        public final String MYSQL_USERNAME;
        public final String MYSQL_PASSWORD;

        public final String REDIS_HOSTNAME;
        public final int REDIS_PORT;
        public final String REDIS_USERNAME;
        public final String REDIS_PASSWORD;

        public Database(YamlConfig yaml) {
            this.DRIVER = yaml.getYaml().getString("database.driver");

            this.MYSQL_HOSTNAME = yaml.getYaml().getString("database.mysql.hostname");
            this.MYSQL_PORT = yaml.getYaml().getInt("database.mysql.port");
            this.MYSQL_DATABASE = yaml.getYaml().getString("database.mysql.database");
            this.MYSQL_USERNAME = yaml.getYaml().getString("database.mysql.username");
            this.MYSQL_PASSWORD = yaml.getYaml().getString("database.mysql.password");

            this.REDIS_HOSTNAME = yaml.getYaml().getString("database.redis.hostname");
            this.REDIS_PORT = yaml.getYaml().getInt("database.redis.port");
            this.REDIS_USERNAME = yaml.getYaml().getString("database.redis.username");
            this.REDIS_PASSWORD = yaml.getYaml().getString("database.redis.password");
        }
    }
}
