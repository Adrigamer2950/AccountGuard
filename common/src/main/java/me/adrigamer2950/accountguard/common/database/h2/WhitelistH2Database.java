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

        return super.getConnection();
    }

    @SneakyThrows
    @Override
    public void saveData() {
        super.saveSqlData("merge into ip_whitelist (uuid, ips) key (uuid) values (?, ?)");

        this.closeConnection();
    }
}
