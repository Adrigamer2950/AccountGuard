package me.adrigamer2950.accountguard.bukkit.commands;

import me.adrigamer2950.accountguard.bukkit.AGBukkit;
import me.adrigamer2950.accountguard.bukkit.permissions.BukkitUtil;
import me.adrigamer2950.accountguard.common.permissions.Permissions;
import me.adrigamer2950.adriapi.api.command.Command;
import me.adrigamer2950.adriapi.api.command.SubCommand;
import me.adrigamer2950.adriapi.api.user.User;

public class ReloadSubCommand extends SubCommand<AGBukkit> {

    public ReloadSubCommand(Command<AGBukkit> parent, String name) {
        super(parent, name);
    }

    @Override
    public boolean execute(User user, String label, String[] args) {
        if (!BukkitUtil.hasPermission(user, Permissions.RELOAD)) {
            user.sendMessage("&cYou don't have permissions!");
            return true;
        }

        getPlugin().reloadConfig();

        user.sendMessage("&aConfiguration reloaded");

        return true;
    }
}
