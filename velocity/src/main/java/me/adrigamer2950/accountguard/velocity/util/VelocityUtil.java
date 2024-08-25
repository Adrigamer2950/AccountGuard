package me.adrigamer2950.accountguard.velocity.util;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import me.adrigamer2950.accountguard.common.permissions.Permissions;

public class VelocityUtil {

    public static boolean hasPermission(CommandSource source, String perm) {
        if (source.hasPermission(Permissions.ALL) && !source.hasPermission(perm))
            return false;

        return source instanceof ConsoleCommandSource
                || source.hasPermission(Permissions.ALL)
                || source.hasPermission(perm);
    }
}
