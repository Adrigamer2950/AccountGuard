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

public class RemoveIPSubCommand extends SubCommand<AGBukkit> {

    public RemoveIPSubCommand(Command<AGBukkit> parent, String name) {
        super(parent, name);
    }

    // /ag add <ip> <username>
    @SuppressWarnings({"ToArrayCallWithZeroLengthArrayArgument", "DuplicatedCode"})
    @Override
    public boolean execute(User user, String label, String[] args) {
        if (!BukkitUtil.hasPermission(user, Permissions.REMOVE_IP_OWN)) {
            user.sendMessage("&cYou don't have permission to do that!");
            return true;
        }

        List<String> list = new ArrayList<>(Arrays.asList(args));
        list.remove(0);
        args = list.toArray(new String[list.size()]);

        if (args.length == 0) {
            user.sendMessage("&cYou have to specify an IP!");
            return true;
        }

        String ip = args[0];

        if (!IPUtil.checkIP(ip)) {
            user.sendMessage("&cInvalid IP!");
            return true;
        }

        String playerName = null;
        if (args.length >= 2)
            playerName = args[1];

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

        if (playerName != null && !BukkitUtil.hasPermission(user, Permissions.REMOVE_IP_OTHER)) {
            user.sendMessage("&cYou don't have permissions to change other player's IP whitelist");
            return true;
        }

        if (getPlugin().database.removeIP(player.getUniqueId(), ip))
            user.sendMessage("&aIP removed from %s's IP Whitelist".formatted(player.getName()));
        else
            user.sendMessage("&cThat IP isn't in %s's IP Whitelist!".formatted(player.getName()));

        return true;
    }
}
