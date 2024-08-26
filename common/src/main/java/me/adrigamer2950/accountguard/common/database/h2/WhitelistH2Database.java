package me.adrigamer2950.accountguard.common.database.h2;

import lombok.SneakyThrows;
import me.adrigamer2950.accountguard.common.database.sql.SqlLikeDatabase;

import java.sql.*;
import java.util.*;

public class WhitelistH2Database extends SqlLikeDatabase {

    public WhitelistH2Database(String pluginDataPath) throws SQLException, ClassNotFoundException {
        super(
                "jdbc:h2:" + pluginDataPath + "/database"
        );
    }

    @Override
    public void initDatabase() throws SQLException, ClassNotFoundException {
        Connection connection = getConnection();
        PreparedStatement statement;

        statement = connection.prepareStatement("create table if not exists ip_whitelist (uuid varchar, ips varchar);");

        statement.execute();
    }

    @Override
    public Connection getConnection() throws ClassNotFoundException {
        Class.forName("org.h2.Driver");

        Connection connection;
        try {
            connection = DriverManager.getConnection(this.url);
        } catch (SQLException e) {
            throw new RuntimeException("Error while trying to connect to the H2 database", e);
        }

        return connection;
    }

    @SneakyThrows
    @Override
    public void saveData() {
        Connection connection = getConnection();

        PreparedStatement statement;

        for (UUID uuid : this.ips.keySet()) {
            Set<String> ips = this.ips.get(uuid);

            if (ips.isEmpty()) {
                statement = connection.prepareStatement(
                        "delete from ip_whitelist where uuid = ?"
                );

                statement.setString(1, uuid.toString());
            }
            else {
                statement = connection.prepareStatement(
                        "merge into ip_whitelist (uuid, ips) key (uuid) values (?, ?)"
                );

                //noinspection OptionalGetWithoutIsPresent
                String firstIP = ips.stream().findFirst().get();

                StringBuilder ipsString = new StringBuilder(firstIP);

                for (String ip : ips.stream().filter(ip -> !Objects.equals(ip, firstIP)).toList()) {
                    ipsString.append(",").append(ip);
                }

                statement.setString(1, uuid.toString());
                statement.setString(2, ipsString.toString());
            }

            statement.executeUpdate();
        }

        connection.close();
    }
}
