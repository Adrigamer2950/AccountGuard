package me.adrigamer2950.accountguard.bukkit;

import me.adrigamer2950.accountguard.bukkit.commands.MainCommand;
import me.adrigamer2950.accountguard.bukkit.config.BukkitConfig;
import me.adrigamer2950.accountguard.bukkit.config.Messages;
import me.adrigamer2950.accountguard.bukkit.database.yaml.YAMLDatabase;
import me.adrigamer2950.accountguard.bukkit.listeners.PlayerListener;
import me.adrigamer2950.accountguard.bukkit.util.Metrics;
import me.adrigamer2950.accountguard.common.AccountGuard;
import me.adrigamer2950.accountguard.common.config.Config;
import me.adrigamer2950.accountguard.common.database.Database;
import me.adrigamer2950.accountguard.common.database.DatabaseType;
import me.adrigamer2950.accountguard.common.logger.APILogger;
import me.adrigamer2950.adriapi.api.command.manager.CommandManager;
import me.adrigamer2950.adriapi.api.config.manager.ConfigManager;
import me.adrigamer2950.adriapi.api.config.yaml.YamlConfig;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.List;

import static me.adrigamer2950.accountguard.common.database.DatabaseType.YAML;

public final class AGBukkit extends JavaPlugin implements AccountGuard {

    public final APILogger LOGGER = new APILogger("&dAccount&aGuard", null);

    private CommandManager commandManager;
    private ConfigManager configManager;
    private YamlConfig configFile;
    private YamlConfig messagesFile;

    private Database database;
    private Config config;
    public Messages messages;

    @Override
    public void onEnable() {
        this.configFile = new YamlConfig(this.getDataFolder().getAbsolutePath(), "config", this, false, true);
        this.messagesFile = new YamlConfig(this.getDataFolder().getAbsolutePath(), "messages", this, false, true);

        this.configManager = new ConfigManager(this);

        this.configManager.registerConfigFile(this.configFile);
        this.configManager.registerConfigFile(this.messagesFile);

        this.configManager.createConfigFiles();

        this.config = new BukkitConfig(this.configFile);

        this.messages = new Messages(this.messagesFile);

        switch (DatabaseType.valueOf(this.config.Database.DRIVER())) {
            case YAML -> this.database = new YAMLDatabase(this, YAML);
            default -> throw new IllegalArgumentException("Wrong database driver at config.yml");
        }

        this.commandManager = new CommandManager(this);
        this.commandManager.registerCommand(new MainCommand(this, "accountguard", List.of("ag")));

        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        new Metrics(this, 20823);

        LOGGER.info("&aEnabled");
    }

    @Override
    public void onDisable() {
        this.commandManager = null;

        this.config = null;
        this.messages = null;

        this.configManager = null;
        this.configFile = null;
        this.messagesFile = null;

        database.saveData();
        this.database = null;

        LOGGER.info("&cDisabled");
    }

    @Override
    public APILogger getAPILogger() {
        return this.LOGGER;
    }

    @Override
    public Database getDatabase() {
        return this.database;
    }

    @Override
    public Config getPluginConfig() {
        return this.config;
    }

    @Override
    public void reloadConfig() {
        try {
            this.configFile.loadConfig();

            this.config = new BukkitConfig(this.configFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void reloadMessages() {
        try {
            this.messagesFile.loadConfig();

            this.messages = new Messages(this.messagesFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
