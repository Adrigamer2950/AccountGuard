package me.adrigamer2950.accountguard.common.database.mysql;

import lombok.SneakyThrows;
import me.adrigamer2950.accountguard.common.database.sql.SqlLikeDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class WhitelistMySQLDatabase extends SqlLikeDatabase {

    public WhitelistMySQLDatabase(String url, String username, String password) throws SQLException, ClassNotFoundException {
        super(url, username, password);
    }

    @SneakyThrows
    @Override
    public void saveData() {
        super.saveSqlData(
                "insert into ip_whitelist (uuid, ips) " +
                        "values(?, ?) " +
                        "on duplicate key update " +
                        "uuid = values(uuid), ips = values(ips)"
        );

        this.closeConnection();
    }

    @Override
    public Connection getConnection() throws ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");

        return super.getConnection();
    }

    @Override
    public void initDatabase() throws SQLException, ClassNotFoundException {
        super.initDatabase("create table if not exists ip_whitelist (uuid varchar(36) not null, ips varchar(512), primary key (ips))");
    }
}
