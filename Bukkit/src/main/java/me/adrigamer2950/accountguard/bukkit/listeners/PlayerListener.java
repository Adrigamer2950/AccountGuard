package me.adrigamer2950.accountguard.bukkit.listeners;

import me.adrigamer2950.accountguard.bukkit.AGBukkit;
import me.adrigamer2950.adriapi.api.colors.Colors;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class PlayerListener implements Listener {

    private final AGBukkit plugin;

    public PlayerListener(AGBukkit plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent e) {
        if (
                !this.plugin.getDatabase().getIPs(e.getUniqueId()).isEmpty()
                        && !this.plugin.getDatabase().hasIP(e.getUniqueId(), e.getAddress().getHostName())
        ) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Colors.translateColors(
                    plugin.messages.KICK_REASON
                            .replaceAll("%prefix%", plugin.config.PREFIX)
            ));
        }
    }
}
