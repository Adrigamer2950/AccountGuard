package me.adrigamer2950.accountguard.common.config;

public abstract class Config {

    public final String Prefix;

    public final DatabaseConfig Database;

    protected Config(String Prefix, DatabaseConfig database) {
        this.Prefix = Prefix;
        this.Database = database;
    }

    public record DatabaseConfig(String DRIVER, String MYSQL_HOSTNAME, int MYSQL_PORT, String MYSQL_DATABASE,
                                 String MYSQL_USERNAME, String MYSQL_PASSWORD, String REDIS_HOSTNAME, int REDIS_PORT,
                                 String REDIS_USERNAME, String REDIS_PASSWORD) {

    }
}
