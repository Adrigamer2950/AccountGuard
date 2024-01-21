package me.adrigamer2950.accountguard.bukkit;

import me.adrigamer2950.adriapi.api.logger.APILogger;
import org.bukkit.plugin.java.JavaPlugin;

public final class AGBukkit extends JavaPlugin {

    private final APILogger LOGGER = new APILogger("&dAccount&aGuard", null);

    @Override
    public void onEnable() {
        LOGGER.log("&aEnabled");
    }

    @Override
    public void onDisable() {
        LOGGER.log("&cDisabled");
    }
}
