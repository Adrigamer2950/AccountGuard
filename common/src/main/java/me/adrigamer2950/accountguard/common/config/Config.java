package me.adrigamer2950.accountguard.common.config;

public record Config(Database database) {

    public record Database(Type driver) {

        public enum Type {
            YAML,
            H2,
            SQLITE,
            MYSQL
        }
    }
}
