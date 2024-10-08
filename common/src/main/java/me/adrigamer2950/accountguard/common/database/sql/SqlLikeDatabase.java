package me.adrigamer2950.accountguard.common.database.sql;

import lombok.NonNull;
import lombok.SneakyThrows;
import me.adrigamer2950.accountguard.common.database.Database;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public abstract class SqlLikeDatabase extends Database {

    protected final String url;
    protected Connection connection;

    protected final String username;
    protected final String password;

    public SqlLikeDatabase(String url) throws SQLException, ClassNotFoundException {
        this(url, null, null);
    }

    public SqlLikeDatabase(String url, String username, String password) throws SQLException, ClassNotFoundException {
        this.url = url;
        this.username = username;
        this.password = password;

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
                //noinspection SqlSourceToSinkFlow
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
        this.initDatabase("create table if not exists ip_whitelist (uuid varchar not null unique, ips varchar, primary key (uuid));");
    }

    public void initDatabase(String statementString) throws SQLException, ClassNotFoundException {
        Connection connection = getConnection();
        PreparedStatement statement;

        //noinspection SqlSourceToSinkFlow
        statement = connection.prepareStatement(statementString);

        statement.execute();
    }

    public Connection getConnection() throws ClassNotFoundException {
        if (this.connection != null)
            return this.connection;

        try {
            this.connection = DriverManager.getConnection(this.url, this.username, this.password);

            return this.connection;
        } catch (SQLException e) {
            throw new RuntimeException("Error while trying to connect to the H2 database", e);
        }
    }

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
