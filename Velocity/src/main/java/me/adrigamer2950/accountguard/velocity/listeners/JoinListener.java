package me.adrigamer2950.accountguard.velocity.listeners;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import me.adrigamer2950.accountguard.velocity.AGVelocity;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class JoinListener {

    private final AGVelocity plugin;

    public JoinListener(AGVelocity plugin) {
        this.plugin = plugin;
    }

    @Subscribe(order = PostOrder.NORMAL)
    public void onPlayerJoin(com.velocitypowered.api.event.connection.PostLoginEvent e) {
        if (
                !this.plugin.getDatabase().getIPs(e.getPlayer().getUniqueId()).isEmpty()
                        && !this.plugin.getDatabase().hasIP(e.getPlayer().getUniqueId(), e.getPlayer().getRemoteAddress().getAddress().getHostAddress())
        ) {
            e.getPlayer().disconnect(LegacyComponentSerializer.legacy('&').deserialize(
                    plugin.getPluginMessages().KICK_REASON()
                            .replace("%prefix%", plugin.getPluginConfig().Prefix())
            ));
        }
    }
}
