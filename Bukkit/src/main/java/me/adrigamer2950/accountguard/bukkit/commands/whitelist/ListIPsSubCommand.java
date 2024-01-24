package me.adrigamer2950.accountguard.bukkit.commands.whitelist;

import me.adrigamer2950.accountguard.bukkit.AGBukkit;
import me.adrigamer2950.accountguard.bukkit.util.Permissions;
import me.adrigamer2950.adriapi.api.colors.Colors;
import me.adrigamer2950.adriapi.api.command.Command;
import me.adrigamer2950.adriapi.api.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class ListIPsSubCommand extends SubCommand {

    public ListIPsSubCommand(Command parent, String name) {
        super(parent, name);
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] args) {
        if(!Permissions.hasPermission(commandSender, Permissions.LIST_IPS)) {
            commandSender.sendMessage(Colors.translateColors("&cYou don't have permissions to do that"));
            return true;
        }

        if(args.length < 2) {
            commandSender.sendMessage(Colors.translateColors("&cYou have to specify a player"));
            return true;
        }

        OfflinePlayer op;
        try {
            op = Bukkit.getOfflinePlayer(UUID.fromString(args[1]));
        } catch (IllegalArgumentException e) {
            //noinspection deprecation
            op = Bukkit.getOfflinePlayer(args[1]);
        }

        if(!op.hasPlayedBefore()) {
            commandSender.sendMessage(Colors.translateColors("&cThat player doesn't exist"));
            return true;
        }

        commandSender.sendMessage(Colors.translateColors(String.format("&6%s &aIPs:", op.getName())));
        for(String ip : ((AGBukkit) getPlugin()).database.getIPs(op.getUniqueId())) {
            commandSender.sendMessage(
                    Colors.translateColors(
                            String.format("&f| &e%s", ip)
                    )
            );
        }

        return true;
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] args) {
        if(args.length == 2)
            return Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).filter(p -> p.toLowerCase().startsWith(args[1])).toList();

        return List.of();
    }
}
