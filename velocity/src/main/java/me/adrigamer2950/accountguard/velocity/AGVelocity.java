package me.adrigamer2950.accountguard.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.dejvokep.boostedyaml.YamlDocument;
import lombok.SneakyThrows;
import me.adrigamer2950.accountguard.common.config.Config;
import me.adrigamer2950.accountguard.velocity.database.OfflinePlayerDatabase;
import me.adrigamer2950.accountguard.velocity.util.FileDownloader;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

@Plugin(
        id = "accountguard",
        name = "AccountGuard",
        version = BuildConstants.VERSION,
        description = BuildConstants.DESCRIPTION,
        authors = BuildConstants.AUTHOR,
        url = "https://github.com/Adrigamer2950/AccountGuard"
)
public class AGVelocity {

    @Inject
    private final Logger logger;
    private final ProxyServer proxy;


    private final YamlDocument configYaml;
    private Config config;

    private final OfflinePlayerDatabase opDatabase;

    @Inject
    public AGVelocity(ProxyServer proxy, Logger logger, @DataDirectory Path dataDirectory) throws IOException {
        this.logger = logger;
        this.proxy = proxy;

        File libsFolder = dataDirectory.resolve("libs").toFile();
        //noinspection ResultOfMethodCallIgnored
        libsFolder.mkdirs();

        File yamlLib = new File(dataDirectory.resolve("libs").toFile(), "boosted-yaml-1.3.6.jar");

        if (!yamlLib.exists()) {
            this.logger.info("Downloading libs");

            FileDownloader.downloadFromMavenCentral("dev.dejvokep", "boosted-yaml", "1.3.6", yamlLib.getParentFile().toPath());

            this.proxy.getPluginManager().addToClasspath(this, yamlLib.toPath());
        }

        File configFile = new File(dataDirectory.toFile(), "config.yml");

        this.configYaml = YamlDocument.create(
                configFile,
                Objects.requireNonNull(this.getClass().getResourceAsStream("config.yml"))
        );

        if (!configFile.exists())
            this.configYaml.save();

        this.reloadConfig();

        File opFile = new File(
                dataDirectory.toFile(),
                "offline_players.yml"
        );

        this.opDatabase = new OfflinePlayerDatabase(
            YamlDocument.create(opFile)
        );
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

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {

    }
}
