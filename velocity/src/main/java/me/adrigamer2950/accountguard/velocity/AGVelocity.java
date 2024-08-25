package me.adrigamer2950.accountguard.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.dejvokep.boostedyaml.YamlDocument;
import lombok.Getter;
import lombok.SneakyThrows;
import me.adrigamer2950.accountguard.api.AccountGuard;
import me.adrigamer2950.accountguard.api.AccountGuardProvider;
import me.adrigamer2950.accountguard.common.config.Config;
import me.adrigamer2950.accountguard.common.database.Database;
import me.adrigamer2950.accountguard.common.database.yaml.WhitelistDatabase;
import me.adrigamer2950.accountguard.common.messages.Messages;
import me.adrigamer2950.accountguard.common.util.IPUtil;
import me.adrigamer2950.accountguard.velocity.commands.MainCommand;
import me.adrigamer2950.accountguard.velocity.database.OfflinePlayerDatabase;
import me.adrigamer2950.accountguard.velocity.listeners.PlayerListener;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
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

    @Inject
    private final Logger logger;
    @Getter private final ProxyServer proxy;
    private final Path dataDirectory;

    private final YamlDocument configYaml;
    @Getter private Config config;

    private final YamlDocument messagesYaml;
    @Getter private Messages messages;

    @Getter private OfflinePlayerDatabase opDatabase;
    @Getter private final Database whitelistDatabase;

    @Inject
    public AGVelocity(ProxyServer proxy, Logger logger, @DataDirectory Path dataDirectory) throws IOException {
        this.logger = logger;
        this.proxy = proxy;
        this.dataDirectory = dataDirectory;

        File configFile = new File(dataDirectory.toFile(), "config.yml");

        this.configYaml = YamlDocument.create(
                configFile,
                Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("config.yml"))
        );

        this.reloadConfig();

        switch (this.config.database().driver()) {
            case YAML -> this.whitelistDatabase = new WhitelistDatabase(
                    new File(dataDirectory.resolve("data").toFile(), "whitelist.yml")
            );
            default -> throw new IllegalArgumentException("Other types of databases are not available for now");
        }

        this.messagesYaml = YamlDocument.create(
                new File(dataDirectory.toFile(), "messages.yml"),
                Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("messages.yml"))
        );

        this.reloadMessages();

        AccountGuardProvider.register(this);
    }

    @SneakyThrows
    public void reloadConfig() {
        this.configYaml.reload();

        this.config = new Config(
                new Config.Database(
                        Config.Database.Type.valueOf(this.configYaml.getString("database.driver"))
                )
        );
    }

    @SneakyThrows
    public void reloadMessages() {
        this.messagesYaml.reload();

        this.messages = new Messages(
                this.messagesYaml.getString("NO_PERMISSION"),
                this.messagesYaml.getString("NO_CHANGE_OTHER_WHITELIST_PERMISSION"),
                this.messagesYaml.getString("NO_VIEW_OTHER_WHITELIST_PERMISSION"),
                this.messagesYaml.getString("IP_NOT_SPECIFIED"),
                this.messagesYaml.getString("INVALID_IP"),
                this.messagesYaml.getString("PLAYER_NAME_NOT_SPECIFIED_FROM_CONSOLE"),
                this.messagesYaml.getString("PLAYER_NOT_FOUND"),
                this.messagesYaml.getString("IP_ADDED_IN_WHITELIST"),
                this.messagesYaml.getString("IP_ALREADY_IN_WHITELIST"),
                this.messagesYaml.getString("IP_REMOVED_FROM_WHITELIST"),
                this.messagesYaml.getString("IP_NOT_IN_WHITELIST"),
                this.messagesYaml.getString("WHITELIST_IP_LIST"),
                this.messagesYaml.getString("RELOAD_MESSAGE")
        );
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) throws IOException {
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

        this.logger.info("Plugin started");
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
