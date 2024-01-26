package me.adrigamer2950.accountguard.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import me.adrigamer2950.accountguard.velocity.commands.MainCommand;
import org.slf4j.Logger;

@Plugin(
        id = "accountguard",
        name = "AccountGuard",
        version = BuildConstants.VERSION,
        authors = "Adrigamer2950",
        description = "Protect your users/staff accounts from being hacked and misused on your server as easily as ever",
        url = "https://github.com/Adrigamer2950/AccountGuard"
)
public class AGVelocity {

    @Inject
    private final Logger logger;
    private static ProxyServer proxy;
    public static ProxyServer getProxy() {
        return proxy;
    }

    @Inject
    public AGVelocity(ProxyServer proxy, Logger logger) {
        AGVelocity.proxy = proxy;
        this.logger = logger;

        logger.info("Plugin has been constructed");
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        new MainCommand(this, "agv").register(getProxy().getCommandManager());

        logger.info("Plugin has been initialised");
    }
}
