package me.adrigamer2950.accountguard.velocity.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import lombok.RequiredArgsConstructor;
import me.adrigamer2950.accountguard.velocity.AGVelocity;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

@RequiredArgsConstructor
public class PlayerListener {

    private final AGVelocity plugin;

    @Subscribe
    public void onPlayerJoin(ServerPreConnectEvent e) {
        String ip = e.getPlayer().getRemoteAddress().getAddress().getHostAddress();

        if (!plugin.getWhitelistDatabase().getIPs(e.getPlayer().getUniqueId()).isEmpty() && !plugin.getWhitelistDatabase().getIPs(e.getPlayer().getUniqueId()).contains(ip)) {
            e.getPlayer().disconnect(
                    LegacyComponentSerializer.legacyAmpersand().deserialize(
                            "&cYour IP is not allowed to join this server as &a%s\n\n&6Powered by &cAccountGuard".formatted(e.getPlayer().getUsername())
                    )
            );
        }

        plugin.getOpDatabase().addUser(e.getPlayer().getUniqueId(), e.getPlayer().getUsername());
    }
}
