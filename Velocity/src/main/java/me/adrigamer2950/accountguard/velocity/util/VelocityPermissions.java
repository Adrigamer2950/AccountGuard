package me.adrigamer2950.accountguard.velocity.util;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import me.adrigamer2950.accountguard.common.permissions.Permissions;

public class VelocityPermissions extends Permissions {

    public static boolean hasPermission(CommandSource source, String permission) {
        return source instanceof ConsoleCommandSource
                || source.hasPermission(permission)
                || source.hasPermission(ALL);
    }
}
