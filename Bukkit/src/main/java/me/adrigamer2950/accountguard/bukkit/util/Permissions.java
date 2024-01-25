package me.adrigamer2950.accountguard.bukkit.util;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class Permissions {

    public static final String ALL = "accountguard.*";
    public static final String RELOAD = "accountguard.reload";
    public static final String ADD_IPS = "accountguard.admin.add";
    public static final String REMOVE_IPS = "accountguard.admin.remove";
    public static final String LIST_IPS = "accountguard.admin.list";
    public static final String COMMAND = "accountguard.command";

    @SuppressWarnings("BooleanMethodIsAlwaysInverted" )
    public static boolean hasPermission(CommandSender sender, String permission) {
        return sender instanceof ConsoleCommandSender
                || sender.hasPermission(permission)
                || sender.hasPermission(ALL);
    }
}
