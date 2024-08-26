package me.adrigamer2950.accountguard.common.database.sql;

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

    public SqlLikeDatabase(String url) throws SQLException, ClassNotFoundException {
        this.url = url;

        initDatabase();

        loadData();
    }

    public abstract void initDatabase() throws SQLException, ClassNotFoundException;

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

        connection.close();
    }
}
