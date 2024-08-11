package me.adrigamer2950.accountguard.bukkit;

import me.adrigamer2950.adriapi.api.APIPlugin;

public final class AGBukkit extends APIPlugin {

    @Override
    public void onPreLoad() {
        getApiLogger().info("&6Loading...");
    }

    @Override
    public void onPostLoad() {
        getApiLogger().info("&aEnabled!");
    }

    @Override
    public void onUnload() {
        getApiLogger().info("&cDisabled!");
    }
}
