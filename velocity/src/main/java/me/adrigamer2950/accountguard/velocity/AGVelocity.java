package me.adrigamer2950.accountguard.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import org.slf4j.Logger;

@Plugin(
        id = "accountguard",
        name = "AccountGuard",
        version = BuildConstants.VERSION
)
public class AGVelocity {

    @Inject
    private Logger logger;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
    }
}
