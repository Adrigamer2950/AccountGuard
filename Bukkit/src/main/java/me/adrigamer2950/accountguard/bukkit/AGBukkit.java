package me.adrigamer2950.accountguard.bukkit;

import me.adrigamer2950.accountguard.bukkit.commands.MainCommand;
import me.adrigamer2950.accountguard.bukkit.database.Database;
import me.adrigamer2950.accountguard.bukkit.database.DatabaseType;
import me.adrigamer2950.adriapi.api.command.manager.CommandManager;
import me.adrigamer2950.adriapi.api.logger.APILogger;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class AGBukkit extends JavaPlugin {

    public final APILogger LOGGER = new APILogger("&dAccount&aGuard", null);

    private CommandManager commandManager;

    public Database database;

    @Override
    public void onEnable() {
        database = Database.getDatabase(this, DatabaseType.YAML);

        this.commandManager = new CommandManager(this);
        this.commandManager.registerCommand(new MainCommand(this, "accountguard", List.of("ag")));

        LOGGER.log("&aEnabled");
    }

    @Override
    public void onDisable() {
        this.commandManager = null;

        database.saveData();
        this.database = null;

        LOGGER.log("&cDisabled");
    }
}
