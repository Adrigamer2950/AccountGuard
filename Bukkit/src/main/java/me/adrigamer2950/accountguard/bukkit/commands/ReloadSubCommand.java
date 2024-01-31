package me.adrigamer2950.accountguard.bukkit.commands;

import me.adrigamer2950.accountguard.bukkit.AGBukkit;
import me.adrigamer2950.accountguard.bukkit.util.Permissions;
import me.adrigamer2950.adriapi.api.colors.Colors;
import me.adrigamer2950.adriapi.api.command.Command;
import me.adrigamer2950.adriapi.api.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ReloadSubCommand extends SubCommand {

    public ReloadSubCommand(Command parent, String name) {
        super(parent, name);
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        AGBukkit plugin = ((AGBukkit) getPlugin());

        if(!Permissions.hasPermission(commandSender, Permissions.RELOAD)) {
            commandSender.sendMessage(Colors.translateColors(plugin.getPluginConfig().Prefix() + plugin.getPluginMessages().NO_PERMISSION()));
            return true;
        }

        plugin.reloadConfig();
        plugin.reloadMessages();

        commandSender.sendMessage(Colors.translateColors(
                plugin.getPluginConfig().Prefix() + plugin.getPluginMessages().RELOAD_MESSAGE()
        ));

        return true;
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
        return List.of();
    }
}
