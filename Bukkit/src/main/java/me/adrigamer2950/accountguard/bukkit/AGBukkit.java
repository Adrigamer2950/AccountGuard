package me.adrigamer2950.accountguard.bukkit;

import me.adrigamer2950.accountguard.bukkit.commands.MainCommand;
import me.adrigamer2950.accountguard.bukkit.config.Config;
import me.adrigamer2950.accountguard.bukkit.config.Messages;
import me.adrigamer2950.accountguard.bukkit.database.Database;
import me.adrigamer2950.accountguard.bukkit.database.DatabaseType;
import me.adrigamer2950.accountguard.bukkit.listeners.PlayerListener;
import me.adrigamer2950.accountguard.bukkit.util.Metrics;
import me.adrigamer2950.adriapi.api.command.manager.CommandManager;
import me.adrigamer2950.adriapi.api.config.manager.ConfigManager;
import me.adrigamer2950.adriapi.api.config.yaml.YamlConfig;
import me.adrigamer2950.adriapi.api.logger.APILogger;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.List;

public final class AGBukkit extends JavaPlugin {
public final class AGBukkit extends JavaPlugin implements AccountGuard {

    public final APILogger LOGGER = new APILogger("&dAccount&aGuard", null);

    private CommandManager commandManager;
    private ConfigManager configManager;
    private YamlConfig configFile;
    private YamlConfig messagesFile;

    public Database database;
    public Config config;
    public Messages messages;

    @Override
    public void onEnable() {
        this.configFile = new YamlConfig(this.getDataFolder().getAbsolutePath(), "config", this, false, true);
        this.messagesFile = new YamlConfig(this.getDataFolder().getAbsolutePath(), "messages", this, false, true);

        this.configManager = new ConfigManager(this);

        this.configManager.registerConfigFile(this.configFile);
        this.configManager.registerConfigFile(this.messagesFile);

        this.configManager.createConfigFiles();

        this.config = new Config(this.configFile);

        this.messages = new Messages(this.messagesFile);

        this.database = Database.getDatabase(this, DatabaseType.YAML);

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

        LOGGER.log("&cDisabled");
        LOGGER.info("&cDisabled");
    }

    @Override
    public APILogger getAPILogger() {
        return this.LOGGER;
    }

    @Override
    public void reloadConfig() {
        try {
            this.configFile.loadConfig();

            this.config = new Config(this.configFile);
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
