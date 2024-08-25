package me.adrigamer2950.accountguard.velocity.commands.whitelist;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;
import me.adrigamer2950.accountguard.common.permissions.Permissions;
import me.adrigamer2950.accountguard.common.util.IPUtil;
import me.adrigamer2950.accountguard.velocity.AGVelocity;
import me.adrigamer2950.accountguard.velocity.objects.Command;
import me.adrigamer2950.accountguard.velocity.objects.OfflinePlayer;
import me.adrigamer2950.accountguard.velocity.objects.SubCommand;
import me.adrigamer2950.accountguard.velocity.util.VelocityUtil;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.List;
import java.util.Objects;

public class RemoveIPSubCommand extends SubCommand<AGVelocity> {

    public RemoveIPSubCommand(Command<AGVelocity> parent, String name) {
        super(parent, name);
    }

    @Override
    public void execute(CommandSource source, String alias, String[] args) {
        if (!VelocityUtil.hasPermission(source, Permissions.REMOVE_IP_OWN)) {
            source.sendMessage(
                    LegacyComponentSerializer.legacyAmpersand().deserialize("&cYou don't have permission to do that!")
            );
            return;
        }

        if (args.length == 0) {
            source.sendMessage(
                    LegacyComponentSerializer.legacyAmpersand().deserialize("&cYou have to specify an IP!")
            );
            return;
        }

        String ip = args[0];

        if (!IPUtil.checkIP(ip)) {
            source.sendMessage(
                    LegacyComponentSerializer.legacyAmpersand().deserialize("&cInvalid IP!")
            );
            return;
        }

        String playerName = null;
        if (args.length >= 2)
            playerName = args[1];

        if (playerName == null && source instanceof ConsoleCommandSource) {
            source.sendMessage(
                    LegacyComponentSerializer.legacyAmpersand().deserialize("&cYou must specify a Player's username if you executing from the console!")
            );
            return;
        }

        OfflinePlayer player = getPlugin().getOpDatabase().getUser(
                (source instanceof Player && playerName == null) ? ((Player) source).getUsername() : Objects.requireNonNull(playerName)
        );

        if (player == null) {
            source.sendMessage(
                    LegacyComponentSerializer.legacyAmpersand().deserialize("&cThat player has never joined this server (at least since AccountGuard was added to this server)!")
            );
            return;
        }

        if (playerName != null && !VelocityUtil.hasPermission(source, Permissions.REMOVE_IP_OTHER)) {
            source.sendMessage(
                    LegacyComponentSerializer.legacyAmpersand().deserialize("&cYou don't have permissions to change other player's IP whitelist")
            );
            return;
        }

        if (getPlugin().getWhitelistDatabase().removeIP(player.getUuid(), ip))
            source.sendMessage(
                    LegacyComponentSerializer.legacyAmpersand().deserialize("&aIP removed from %s's IP Whitelist".formatted(player.getName()))
            );
        else
            source.sendMessage(
                    LegacyComponentSerializer.legacyAmpersand().deserialize("&cThat IP isn't in %s's IP Whitelist!".formatted(player.getName()))
            );

        getPlugin().getWhitelistDatabase().saveData();
    }

    @Override
    public List<String> suggest(CommandSource source, String alias, String[] args) {
        return null;
    }
}
