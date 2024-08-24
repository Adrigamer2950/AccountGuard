package me.adrigamer2950.accountguard.velocity.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import lombok.RequiredArgsConstructor;
import me.adrigamer2950.accountguard.velocity.AGVelocity;

@RequiredArgsConstructor
public class PlayerListener {

    private final AGVelocity plugin;

    @Subscribe
    public void onPlayerJoin(ServerPreConnectEvent e) {
        plugin.getOpDatabase().addUser(e.getPlayer().getUniqueId(), e.getPlayer().getUsername());
    }
}
