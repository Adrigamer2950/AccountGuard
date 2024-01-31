package me.adrigamer2950.accountguard.bukkit.commands.whitelist;

import me.adrigamer2950.accountguard.bukkit.AGBukkit;
import me.adrigamer2950.accountguard.bukkit.util.IPUtil;
import me.adrigamer2950.accountguard.bukkit.util.Permissions;
import me.adrigamer2950.adriapi.api.colors.Colors;
import me.adrigamer2950.adriapi.api.command.Command;
import me.adrigamer2950.adriapi.api.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class RemoveIPSubCommand extends SubCommand {

    public RemoveIPSubCommand(Command parent, String name) {
        super(parent, name);
    }

    @SuppressWarnings("DuplicatedCode" )
    @Override
    public boolean execute(CommandSender commandSender, String s, String[] args) {
        AGBukkit plugin = ((AGBukkit) getPlugin());

        if (!Permissions.hasPermission(commandSender, Permissions.REMOVE_IPS)) {
            commandSender.sendMessage(Colors.translateColors(plugin.config.PREFIX + plugin.messages.NO_PERMISSION));
            return true;
        }

        if (args.length < 2) {
            commandSender.sendMessage(Colors.translateColors(plugin.config.PREFIX + plugin.messages.PLAYER_NOT_SPECIFIED));
            return true;
        }

        OfflinePlayer op;
        try {
            op = Bukkit.getOfflinePlayer(UUID.fromString(args[1]));
        } catch (IllegalArgumentException e) {
            //noinspection deprecation
            op = Bukkit.getOfflinePlayer(args[1]);
        }

        if (!op.hasPlayedBefore()) {
            commandSender.sendMessage(Colors.translateColors(plugin.config.PREFIX + plugin.messages.PLAYER_DOESNT_EXISTS
                    .replaceAll("%player%", op.getName())
            ));
            return true;
        }

        if (args.length < 3) {
            commandSender.sendMessage(Colors.translateColors(plugin.config.PREFIX + plugin.messages.IP_NOT_SPECIFIED
                    .replaceAll("%player%", op.getName())
            ));
            return true;
        }

        String ip = args[2];

        if (!IPUtil.isValid(ip)) {
            commandSender.sendMessage(Colors.translateColors(plugin.config.PREFIX + plugin.messages.INVALID_IP
                    .replaceAll("%player%", op.getName())
                    .replaceAll("%ip%", ip)
            ));
            return true;
        }

            commandSender.sendMessage(Colors.translateColors(plugin.config.PREFIX + plugin.messages.IP_NOT_IN_WHITELIST
        if (!plugin.getDatabase().hasIP(op.getUniqueId(), ip)) {
                    .replaceAll("%player%", op.getName())
                    .replaceAll("%ip%", ip)));
            return true;
        }

        plugin.getDatabase().removeIP(op.getUniqueId(), ip);

        commandSender.sendMessage(Colors.translateColors(plugin.config.PREFIX + plugin.messages.IP_REMOVED_FROM_WHITELIST
                .replaceAll("%player%", op.getName())
                .replaceAll("%ip%", ip)));

        return true;
    }


    @Override
    public List<String> tabComplete(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] args) {
        if (args.length == 2)
            return Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).filter(p -> p.toLowerCase().startsWith(args[1])).toList();

        if (commandSender instanceof Player p)
            if (args.length == 3)
                return ((AGBukkit) getPlugin()).getDatabase().getIPs(p.getUniqueId()).stream().toList();

        return List.of();
    }
}
