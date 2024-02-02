package me.adrigamer2950.accountguard.velocity.listeners;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import me.adrigamer2950.accountguard.velocity.AGVelocity;

public final class OfflinePlayerListener {

    private final AGVelocity plugin;
    public OfflinePlayerListener(AGVelocity plugin) {
        this.plugin = plugin;
    }

    @Subscribe(order = PostOrder.FIRST)
    public void onPlayerJoin(com.velocitypowered.api.event.connection.PostLoginEvent e) {
        this.plugin.getOfflinePlayerDatabase().setUUID(e.getPlayer().getUsername(), e.getPlayer().getUniqueId());
    }
}
