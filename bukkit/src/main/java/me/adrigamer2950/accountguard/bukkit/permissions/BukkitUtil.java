package me.adrigamer2950.accountguard.bukkit.permissions;

import me.adrigamer2950.accountguard.common.permissions.Permissions;
import me.adrigamer2950.adriapi.api.user.User;

public class BukkitUtil {

    public static boolean hasPermission(User user, String perm) {
        if (user.hasPermission(Permissions.ALL) && !user.hasPermission(perm))
            return false;

        return user.isConsole()
                || user.hasPermission(Permissions.ALL)
                || user.hasPermission(perm);
    }
}
