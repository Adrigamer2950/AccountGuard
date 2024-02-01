package me.adrigamer2950.accountguard.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import me.adrigamer2950.accountguard.common.AccountGuard;
import me.adrigamer2950.accountguard.common.config.Config;
import me.adrigamer2950.accountguard.common.config.Messages;
import me.adrigamer2950.accountguard.common.database.Database;
import me.adrigamer2950.accountguard.common.database.DatabaseType;
import me.adrigamer2950.accountguard.common.logger.APILogger;
import me.adrigamer2950.accountguard.velocity.commands.MainCommand;
import me.adrigamer2950.accountguard.velocity.database.yaml.OfflinePlayerDatabase;
import me.adrigamer2950.accountguard.velocity.database.yaml.YAMLDatabase;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Plugin(
        id = "accountguard",
        name = "AccountGuard",
        version = BuildConstants.VERSION,
        authors = "Adrigamer2950",
        description = "Protect your users/staff accounts from being hacked and misused on your server as easily as ever",
        url = "https://github.com/Adrigamer2950/AccountGuard"
)
public class AGVelocity implements AccountGuard {

    private final APILogger logger;
    private static ProxyServer proxy;

    public static ProxyServer getProxy() {
        return proxy;
    }

    private YamlDocument configYaml;
    private YamlDocument messagesYaml;

    private Config config;
    private Messages messages;
    private final Database database;
    private final OfflinePlayerDatabase offlinePlayerDatabase;

    @Inject
    public AGVelocity(ProxyServer proxy) throws IOException {
        AGVelocity.proxy = proxy;
        this.logger = new APILogger("AccountGuard", null);

        YamlDocument configYaml = YamlDocument.create(
                new File("plugins/accountguard/config.yml"),
                Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("config.yml")),
                GeneralSettings.DEFAULT,
                LoaderSettings.builder().setAutoUpdate(true).build(),
                DumperSettings.DEFAULT,
                UpdaterSettings.DEFAULT
        );

        this.config = new Config(
                configYaml.getString("prefix"),
                new Config.DatabaseConfig(
                        configYaml.getString("database.driver"),
                        configYaml.getString("database.mysql.hostname"),
                        configYaml.getInt("database.mysql.port"),
                        configYaml.getString("database.mysql.database"),
                        configYaml.getString("database.mysql.username"),
                        configYaml.getString("database.mysql.password"),
                        configYaml.getString("database.redis.hostname"),
                        configYaml.getInt("database.redis.port"),
                        configYaml.getString("database.redis.username"),
                        configYaml.getString("database.redis.password")
                )
        );

        switch (DatabaseType.valueOf(this.config.Database().DRIVER())) {
            case YAML -> this.database = new YAMLDatabase(this);
            default -> throw new IllegalArgumentException("Wrong database driver at config.yml");
        }

        this.offlinePlayerDatabase = new OfflinePlayerDatabase(this);

        logger.info("§aPlugin has been constructed");
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        new MainCommand(this, "agv").register(getProxy().getCommandManager());

        logger.info("Plugin has been initialised");
    }

    @Override
    public APILogger getAPILogger() {
        return this.logger;
    }

    @Override
    public Database getDatabase() {
        return this.database;
    }

    public OfflinePlayerDatabase getOfflinePlayerDatabase() {
        return this.offlinePlayerDatabase;
    }

    @Override
    public Config getPluginConfig() {
        return this.config;
    }

    @Override
    public Messages getPluginMessages() {
        return this.messages;
    }

    @Override
    public void reloadConfig() {
        try {
            if (this.configYaml == null)
                this.configYaml = YamlDocument.create(
                        new File("plugins/accountguard/config.yml"),
                        Objects.requireNonNull(this.getClass().getResourceAsStream("config.yml")),
                        GeneralSettings.DEFAULT,
                        LoaderSettings.builder().setAutoUpdate(true).build(),
                        DumperSettings.DEFAULT,
                        UpdaterSettings.builder().setVersioning(new BasicVersioning("config-version")
                        ).build());
            else
                this.configYaml.reload();

            this.config = new Config(
                    this.configYaml.getString("prefix"),
                    new Config.DatabaseConfig(
                            this.configYaml.getString("database.driver"),
                            this.configYaml.getString("database.mysql.hostname"),
                            this.configYaml.getInt("database.mysql.port"),
                            this.configYaml.getString("database.mysql.database"),
                            this.configYaml.getString("database.mysql.username"),
                            this.configYaml.getString("database.mysql.password"),
                            this.configYaml.getString("database.redis.hostname"),
                            this.configYaml.getInt("database.redis.port"),
                            this.configYaml.getString("database.redis.username"),
                            this.configYaml.getString("database.redis.password")
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void reloadMessages() {
        try {
            if (this.messagesYaml == null)
                this.messagesYaml = YamlDocument.create(
                        new File("plugins/accountguard/messages.yml"),
                        Objects.requireNonNull(this.getClass().getResourceAsStream("messages.yml")),
                        GeneralSettings.DEFAULT,
                        LoaderSettings.builder().setAutoUpdate(true).build(),
                        DumperSettings.DEFAULT,
                        UpdaterSettings.builder().setVersioning(new BasicVersioning("messages-version")
                        ).build());
            else
                this.messagesYaml.reload();

            this.messages = new Messages(
                    this.messagesYaml.getString("no-permission"),
                    this.messagesYaml.getString("kick-reason"),
                    this.messagesYaml.getString("player-not-specified"),
                    this.messagesYaml.getString("player-doesnt-exists"),
                    this.messagesYaml.getString("ip-not-specified"),
                    this.messagesYaml.getString("invalid-ip"),
                    this.messagesYaml.getString("ip-added-to-whitelist"),
                    this.messagesYaml.getString("ip-already-in-whitelist"),
                    this.messagesYaml.getString("ip-removed-from-whitelist"),
                    this.messagesYaml.getString("ip-not-in-whitelist"),
                    this.messagesYaml.getString("reload-message"),
                    this.messagesYaml.getString("list-ips-message")
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
