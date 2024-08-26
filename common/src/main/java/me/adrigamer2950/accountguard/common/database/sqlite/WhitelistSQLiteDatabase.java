package me.adrigamer2950.accountguard.common.database.sqlite;

import me.adrigamer2950.accountguard.common.database.sql.SqlLikeDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class WhitelistSQLiteDatabase extends SqlLikeDatabase {

    public WhitelistSQLiteDatabase(String pluginDataPath) throws SQLException, ClassNotFoundException {
        super(
                "jdbc:sqlite:" + pluginDataPath + "/sqlite.db"
        );
    }

    @Override
    public Connection getConnection() throws ClassNotFoundException {
        Class.forName("me.adrigamer2950.accountguard.libs.sqlite.JDBC");

        if (this.connection != null)
            return this.connection;

        Connection connection;
        try {
            connection = DriverManager.getConnection(this.url);
        } catch (SQLException e) {
            throw new RuntimeException("Error while trying to connect to the H2 database", e);
        }

        this.connection = connection;

        return this.connection;
    }

    @Override
    public void saveData() {
        super.saveSqlData(
                "insert or replace into ip_whitelist (uuid, ips) values (?, ?)"
        );
    }
}
