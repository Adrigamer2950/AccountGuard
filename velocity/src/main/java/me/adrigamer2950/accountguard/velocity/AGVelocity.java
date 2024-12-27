package me.adrigamer2950.accountguard.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.dejvokep.boostedyaml.YamlDocument;
import lombok.Getter;
import lombok.SneakyThrows;
import me.adrigamer2950.accountguard.api.AccountGuard;
import me.adrigamer2950.accountguard.api.AccountGuardProvider;
import me.adrigamer2950.accountguard.common.AGLoader;
import me.adrigamer2950.accountguard.common.config.Config;
import me.adrigamer2950.accountguard.common.database.Database;
import me.adrigamer2950.accountguard.common.database.h2.WhitelistH2Database;
import me.adrigamer2950.accountguard.common.database.mysql.WhitelistMySQLDatabase;
import me.adrigamer2950.accountguard.common.database.sql.SqlLikeDatabase;
import me.adrigamer2950.accountguard.common.database.sqlite.WhitelistSQLiteDatabase;
import me.adrigamer2950.accountguard.common.database.yaml.WhitelistYAMLDatabase;
import me.adrigamer2950.accountguard.common.messages.Messages;
import me.adrigamer2950.accountguard.common.util.IPUtil;
import me.adrigamer2950.accountguard.velocity.commands.MainCommand;
import me.adrigamer2950.accountguard.velocity.database.OfflinePlayerDatabase;
import me.adrigamer2950.accountguard.velocity.listeners.PlayerListener;
import net.byteflux.libby.VelocityLibraryManager;
import org.bstats.velocity.Metrics;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Plugin(
        id = "accountguard",
        name = "AccountGuard",
        version = BuildConstants.VERSION,
        description = BuildConstants.DESCRIPTION,
        authors = BuildConstants.AUTHOR,
        url = "https://github.com/Adrigamer2950/AccountGuard"
)
public class AGVelocity implements AccountGuard {

    private final Logger logger;
    @Getter private final ProxyServer proxy;
    private final Path dataDirectory;
    private final Metrics.Factory bStatsFactory;

    @Getter private Config config;

    @Getter private Messages messages;

    @Getter private OfflinePlayerDatabase opDatabase;
    @Getter private Database whitelistDatabase;

    @Inject
    public AGVelocity(ProxyServer proxy, Logger logger, @DataDirectory Path dataDirectory, Metrics.Factory bStatsFactory) {
        this.logger = logger;
        this.proxy = proxy;
        this.dataDirectory = dataDirectory;
        this.bStatsFactory = bStatsFactory;
    }

    @SneakyThrows
    public void reloadConfig() {
        File configFile = new File(dataDirectory.toFile(), "config.yml");

        YamlDocument configYaml = YamlDocument.create(
                configFile,
                Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("config.yml"))
        );

        configYaml.reload();

        this.config = new Config(
                configYaml.getString("prefix"),
                new Config.Database(
                        Config.Database.Type.valueOf(configYaml.getString("database.driver")),
                        new Config.Database.MySQL(
                                configYaml.getString("database.mysql.hostname"),
                                configYaml.getInt("database.mysql.port"),
                                configYaml.getString("database.mysql.database"),
                                configYaml.getString("database.mysql.username"),
                                configYaml.getString("database.mysql.password")
                        )
                )
        );
    }

    @SneakyThrows
    public void reloadMessages() {
        YamlDocument messagesYaml = YamlDocument.create(
                new File(dataDirectory.toFile(), "messages.yml"),
                Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("messages.yml"))
        );

        messagesYaml.reload();

        this.messages = new Messages(
                messagesYaml.getString("NO_PERMISSION"),
                messagesYaml.getString("NO_CHANGE_OTHER_WHITELIST_PERMISSION"),
                messagesYaml.getString("NO_VIEW_OTHER_WHITELIST_PERMISSION"),
                messagesYaml.getString("IP_NOT_SPECIFIED"),
                messagesYaml.getString("INVALID_IP"),
                messagesYaml.getString("PLAYER_NAME_NOT_SPECIFIED_FROM_CONSOLE"),
                messagesYaml.getString("PLAYER_NOT_FOUND"),
                messagesYaml.getString("IP_ADDED_IN_WHITELIST"),
                messagesYaml.getString("IP_ALREADY_IN_WHITELIST"),
                messagesYaml.getString("IP_REMOVED_FROM_WHITELIST"),
                messagesYaml.getString("IP_NOT_IN_WHITELIST"),
                messagesYaml.getString("WHITELIST_IP_LIST"),
                messagesYaml.getString("RELOAD_MESSAGE"),
                messagesYaml.getString("KICK_MESSAGE")
        );
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) throws IOException, SQLException, ClassNotFoundException {
        AGLoader.loadLibraries(new VelocityLibraryManager<>(this.logger, this.dataDirectory, this.proxy.getPluginManager(), this));

        this.reloadConfig();

        switch (this.config.database().driver()) {
            case YAML -> this.whitelistDatabase = new WhitelistYAMLDatabase(
                    new File(dataDirectory.resolve("data").toFile(), "whitelist.yml")
            );
            case H2 -> this.whitelistDatabase = new WhitelistH2Database(
                    dataDirectory.resolve("data").toAbsolutePath().toString()
            );
            case SQLITE -> this.whitelistDatabase = new WhitelistSQLiteDatabase(
                    dataDirectory.resolve("data").toAbsolutePath().toString()
            );
            case MYSQL -> this.whitelistDatabase = new WhitelistMySQLDatabase(
                    "jdbc:mysql://%s:%s/%s".formatted(
                            this.config.database().mysql().hostname(),
                            this.config.database().mysql().port(),
                            this.config.database().mysql().database()
                    ),
                    this.config.database().mysql().username(),
                    this.config.database().mysql().password()
            );
        }

        this.reloadMessages();

        File opFile = new File(
                dataDirectory.toFile(),
                "offline_players.yml"
        );

        this.opDatabase = new OfflinePlayerDatabase(
                YamlDocument.create(opFile), this
        );

        proxy.getEventManager().register(this, new PlayerListener(this));

        new MainCommand(this, "agv")
                .register(getProxy().getCommandManager());

        bStatsFactory.make(this, 23194);

        AccountGuardProvider.register(this);

        this.logger.info("Plugin started");
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent e) throws SQLException {
        this.getWhitelistDatabase().saveData();

        if (this.getWhitelistDatabase() instanceof SqlLikeDatabase)
            ((SqlLikeDatabase) this.getWhitelistDatabase()).closeConnection();

        AccountGuardProvider.unRegister();
    }

    @Override
    public Set<String> getIPs(UUID uuid) {
        return getWhitelistDatabase().getIPs(uuid);
    }

    @Override
    public boolean isValidIP(String ip) {
        return IPUtil.checkIP(ip);
    }

    @Override
    public boolean addIP(UUID uuid, String ip) {
        return getWhitelistDatabase().addIP(uuid, ip);
    }

    @Override
    public boolean removeIP(UUID uuid, String ip) {
        return getWhitelistDatabase().removeIP(uuid, ip);
    }

    @Override
    public boolean hasIP(UUID uuid, String ip) {
        return getWhitelistDatabase().hasIP(uuid, ip);
    }
}
