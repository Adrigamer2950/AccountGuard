package me.adrigamer2950.accountguard.bukkit.commands.whitelist;

import me.adrigamer2950.accountguard.bukkit.AGBukkit;
import me.adrigamer2950.accountguard.bukkit.permissions.BukkitUtil;
import me.adrigamer2950.accountguard.common.permissions.Permissions;
import me.adrigamer2950.adriapi.api.command.Command;
import me.adrigamer2950.adriapi.api.command.SubCommand;
import me.adrigamer2950.adriapi.api.user.User;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ListIPSubCommand extends SubCommand<AGBukkit> {

    public ListIPSubCommand(Command<AGBukkit> parent, String name) {
        super(parent, name);
    }

    @SuppressWarnings("ToArrayCallWithZeroLengthArrayArgument")
    @Override
    public boolean execute(User user, String label, String[] args) {
        if (!BukkitUtil.hasPermission(user, Permissions.LIST_IP_OWN)) {
            user.sendMessage("&cYou don't have permission to do that!");
            return true;
        }

        List<String> list = new ArrayList<>(Arrays.asList(args));
        list.remove(0);
        args = list.toArray(new String[list.size()]);

        String playerName = null;
        if (args.length >= 1)
            playerName = args[0];

        if (playerName == null && user.isConsole()) {
            user.sendMessage("&cYou must specify a Player's username if you executing from the console!");
            return true;
        }

        OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(
                (user.isPlayer() && playerName == null) ? user.getPlayerOrNull().getName() : Objects.requireNonNull(playerName)
        );

        if (player == null) {
            user.sendMessage("&cThat player has never joined this server!");
            return true;
        }

        if (playerName != null && !BukkitUtil.hasPermission(user, Permissions.LIST_IP_OTHER)) {
            user.sendMessage("&cYou don't have permissions to view other player's IP whitelist");
            return true;
        }

        List<String> ips = getPlugin().database.getIPs(player.getUniqueId()).stream().toList();

        user.sendMessage("&f&m                                          ");
        user.sendMessage("&6%s &fwhitelisted IP's:".formatted(player.getName()));

        for (String ip : ips) {
            user.sendMessage("&f| &e%s".formatted(ip));
        }

        user.sendMessage("&f&m                                          ");

        return true;
    }
}
