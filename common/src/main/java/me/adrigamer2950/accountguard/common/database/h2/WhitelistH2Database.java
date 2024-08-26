package me.adrigamer2950.accountguard.common.database.h2;

import lombok.SneakyThrows;
import me.adrigamer2950.accountguard.common.database.sql.SqlLikeDatabase;

import java.sql.*;

public class WhitelistH2Database extends SqlLikeDatabase {

    public WhitelistH2Database(String pluginDataPath) throws SQLException, ClassNotFoundException {
        super(
                "jdbc:h2:" + pluginDataPath + "/h2"
        );
    }

    @Override
    public Connection getConnection() throws ClassNotFoundException {
        Class.forName("me.adrigamer2950.accountguard.libs.h2.Driver");

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

    @SneakyThrows
    @Override
    public void saveData() {
        super.saveSqlData("merge into ip_whitelist (uuid, ips) key (uuid) values (?, ?)");

        this.closeConnection();
    }
}
