package me.adrigamer2950.accountguard.common.config;

public record Config (
        String Prefix,
        DatabaseConfig Database
) {

    public record DatabaseConfig(String DRIVER, String MYSQL_HOSTNAME, int MYSQL_PORT, String MYSQL_DATABASE,
                                 String MYSQL_USERNAME, String MYSQL_PASSWORD, String REDIS_HOSTNAME, int REDIS_PORT,
                                 String REDIS_USERNAME, String REDIS_PASSWORD) {

    }
}
