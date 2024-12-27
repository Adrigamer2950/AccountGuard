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
                            this.plugin.getMessages().KICK_MESSAGE().replaceAll("%player%", e.getPlayer().getUsername())
                    )
            );
        }

        plugin.getOpDatabase().addUser(e.getPlayer().getUniqueId(), e.getPlayer().getUsername());
    }
}
