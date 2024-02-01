package me.adrigamer2950.accountguard.bukkit.util;

import me.adrigamer2950.accountguard.common.permissions.Permissions;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class BukkitPermissions extends Permissions {

    @SuppressWarnings("BooleanMethodIsAlwaysInverted" )
    public static boolean hasPermission(CommandSender sender, String permission) {
        return sender instanceof ConsoleCommandSender
                || sender.hasPermission(permission)
                || sender.hasPermission(ALL);
    }
}
