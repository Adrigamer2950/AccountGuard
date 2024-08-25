package me.adrigamer2950.accountguard.bukkit.commands.whitelist;

import me.adrigamer2950.accountguard.bukkit.AGBukkit;
import me.adrigamer2950.accountguard.bukkit.permissions.BukkitUtil;
import me.adrigamer2950.accountguard.common.permissions.Permissions;
import me.adrigamer2950.accountguard.common.util.IPUtil;
import me.adrigamer2950.adriapi.api.command.Command;
import me.adrigamer2950.adriapi.api.command.SubCommand;
import me.adrigamer2950.adriapi.api.user.User;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AddIPSubCommand extends SubCommand<AGBukkit> {

    public AddIPSubCommand(Command<AGBukkit> parent, String name) {
        super(parent, name);
    }

    // /ag add <ip> <username>
    @SuppressWarnings({"ToArrayCallWithZeroLengthArrayArgument", "DuplicatedCode"})
    @Override
    public boolean execute(User user, String label, String[] args) {
        if (!BukkitUtil.hasPermission(user, Permissions.ADD_IP_OWN)) {
            user.sendMessage(getPlugin().messages.NO_PERMISSION());
            return true;
        }

        List<String> list = new ArrayList<>(Arrays.asList(args));
        list.remove(0);
        args = list.toArray(new String[list.size()]);

        if (args.length == 0) {
            user.sendMessage(getPlugin().messages.IP_NOT_SPECIFIED());
            return true;
        }

        String ip = args[0];

        if (!IPUtil.checkIP(ip)) {
            user.sendMessage(getPlugin().messages.INVALID_IP());
            return true;
        }

        String playerName = null;
        if (args.length >= 2)
            playerName = args[1];

        if (playerName == null && user.isConsole()) {
            user.sendMessage(getPlugin().messages.PLAYER_NAME_NOT_SPECIFIED_FROM_CONSOLE());
            return true;
        }

        OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(
                (user.isPlayer() && playerName == null) ? user.getPlayerOrNull().getName() : Objects.requireNonNull(playerName)
        );

        if (playerName != null && !BukkitUtil.hasPermission(user, Permissions.ADD_IP_OTHER)) {
            user.sendMessage(getPlugin().messages.NO_CHANGE_OTHER_WHITELIST_PERMISSION());
            return true;
        }

        if (player == null) {
            user.sendMessage(getPlugin().messages.PLAYER_NOT_FOUND());
            return true;
        }

        if (getPlugin().database.addIP(player.getUniqueId(), ip))
            user.sendMessage(getPlugin().messages.IP_ADDED_IN_WHITELIST().replaceAll("%player%", Objects.requireNonNull(player.getName())));
        else
            user.sendMessage(getPlugin().messages.IP_ALREADY_IN_WHITELIST().replaceAll("%player%", Objects.requireNonNull(player.getName())));

        return true;
    }
}
