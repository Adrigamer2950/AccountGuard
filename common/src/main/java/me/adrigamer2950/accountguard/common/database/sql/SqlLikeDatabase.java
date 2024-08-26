package me.adrigamer2950.accountguard.common.database.sql;

import lombok.NonNull;
import lombok.SneakyThrows;
import me.adrigamer2950.accountguard.common.database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public abstract class SqlLikeDatabase extends Database {

    protected final String url;
    protected Connection connection;

    public SqlLikeDatabase(String url) throws SQLException, ClassNotFoundException {
        this.url = url;

        initDatabase();

        loadData();
    }

    @SneakyThrows
    public void saveSqlData(@NonNull String insertStatement) {
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
                statement = connection.prepareStatement(insertStatement);

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

    public void initDatabase() throws SQLException, ClassNotFoundException {
        Connection connection = getConnection();
        PreparedStatement statement;

        statement = connection.prepareStatement("create table if not exists ip_whitelist (uuid varchar not null unique, ips varchar, primary key (uuid));");

        statement.execute();
    }

    public abstract Connection getConnection() throws ClassNotFoundException;

    @SneakyThrows
    @Override
    public void loadData() {
        Connection connection = getConnection();

        PreparedStatement statement = connection.prepareStatement("select * from ip_whitelist");

        ResultSet result = statement.executeQuery();

        while (result.next()) {
            try {
                UUID uuid = UUID.fromString(result.getString(1));

                Set<String> ips = Arrays.stream(result.getString(2).split(",")).filter(Objects::nonNull).collect(Collectors.toCollection(HashSet::new));

                this.ips.put(uuid, ips);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    public void closeConnection() throws SQLException {
        if (this.connection != null)
            this.connection.close();
    }
}
