package me.adrigamer2950.accountguard.bukkit.logger;

import me.adrigamer2950.accountguard.common.logger.APILogger;
import me.adrigamer2950.adriapi.api.colors.Colors;
import org.bukkit.Bukkit;

public class BukkitLogger extends APILogger {

    public BukkitLogger(String name, APILogger parent) {
        super(name, parent);
    }

    @Override
    public void info(String message) {
        Bukkit.getConsoleSender().sendMessage(
                Colors.translateColors(
                        String.format("[%s] %s", this.getName(), message)
                )
        );
    }
}
