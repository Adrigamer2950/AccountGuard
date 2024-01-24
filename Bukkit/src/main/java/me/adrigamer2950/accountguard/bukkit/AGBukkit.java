package me.adrigamer2950.accountguard.bukkit;

import me.adrigamer2950.accountguard.bukkit.commands.MainCommand;
import me.adrigamer2950.accountguard.bukkit.config.Config;
import me.adrigamer2950.accountguard.bukkit.database.Database;
import me.adrigamer2950.accountguard.bukkit.database.DatabaseType;
import me.adrigamer2950.adriapi.api.command.manager.CommandManager;
import me.adrigamer2950.adriapi.api.config.manager.ConfigManager;
import me.adrigamer2950.adriapi.api.config.yaml.YamlConfig;
import me.adrigamer2950.adriapi.api.logger.APILogger;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class AGBukkit extends JavaPlugin {

    public final APILogger LOGGER = new APILogger("&dAccount&aGuard", null);

    private CommandManager commandManager;
    private ConfigManager configManager;
    private YamlConfig configFile;

    public Database database;
    public Config config;

    @Override
    public void onEnable() {
        this.configFile = new YamlConfig(this.getDataFolder().getAbsolutePath(), "config", this, true, true);
        this.configManager = new ConfigManager(this);

        this.configManager.registerConfigFile(this.configFile);

        this.configManager.createConfigFiles();

        this.config = new Config(this.configFile);

        this.database = Database.getDatabase(this, DatabaseType.YAML);

        this.commandManager = new CommandManager(this);
        this.commandManager.registerCommand(new MainCommand(this, "accountguard", List.of("ag")));

        LOGGER.log("&aEnabled");
    }

    @Override
    public void onDisable() {
        this.commandManager = null;

        this.config = null;
        this.configManager = null;
        this.configFile = null;

        database.saveData();
        this.database = null;

        LOGGER.log("&cDisabled");
    }
}
