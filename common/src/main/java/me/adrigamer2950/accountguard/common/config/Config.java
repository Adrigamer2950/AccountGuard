package me.adrigamer2950.accountguard.common.config;

public record Config(Database database) {
public record Config(String prefix, Database database) {

    public record Database(Type driver, MySQL mysql) {

        public enum Type {
            YAML,
            H2,
            SQLITE,
            MYSQL
        }

        public record MySQL(String hostname, int port, String database, String username, String password) { }
    }
}
