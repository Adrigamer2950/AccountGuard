package me.adrigamer2950.accountguard.bukkit.listeners;

import lombok.RequiredArgsConstructor;
import me.adrigamer2950.accountguard.bukkit.AGBukkit;
import me.adrigamer2950.adriapi.api.colors.Colors;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

@RequiredArgsConstructor
public class PlayerListener implements Listener {

    private final AGBukkit plugin;

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerPreJoin(AsyncPlayerPreLoginEvent e) {
        String ip = e.getAddress().getHostAddress();

        if (!this.plugin.database.getIPs(e.getUniqueId()).isEmpty() && !this.plugin.database.getIPs(e.getUniqueId()).contains(ip)) {
            e.setLoginResult(
                    AsyncPlayerPreLoginEvent.Result.KICK_OTHER
            );
            //noinspection deprecation
            e.setKickMessage(
                    Colors.translateColors(
                            this.plugin.messages.KICK_MESSAGE().replaceAll("%player%", e.getName())
                    )
            );
        }
    }
}
