package me.adrigamer2950.accountguard.bukkit;

import me.adrigamer2950.accountguard.bukkit.database.Database;
import me.adrigamer2950.accountguard.bukkit.database.DatabaseType;
import me.adrigamer2950.adriapi.api.logger.APILogger;
import org.bukkit.plugin.java.JavaPlugin;

public final class AGBukkit extends JavaPlugin {

    public final APILogger LOGGER = new APILogger("&dAccount&aGuard", null);

    public Database database;

    @Override
    public void onEnable() {
        database = Database.getDatabase(this, DatabaseType.YAML);

        LOGGER.log("&aEnabled");
    }

    @Override
    public void onDisable() {
        database.saveData();

        this.database = null;

        LOGGER.log("&cDisabled");
    }
}
