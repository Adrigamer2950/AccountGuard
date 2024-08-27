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

public class AddIPSubCommand extends SubCommand<AGVelocity> {

    public AddIPSubCommand(Command<AGVelocity> parent, String name) {
        super(parent, name);
    }

    @Override
    public void execute(CommandSource source, String alias, String[] args) {
        if (!VelocityUtil.hasPermission(source, Permissions.ADD_IP_OWN)) {
            source.sendMessage(
                    LegacyComponentSerializer.legacyAmpersand().deserialize(getPlugin().getMessages().NO_PERMISSION())
            );
            return;
        }

        if (args.length == 0) {
            source.sendMessage(
                    LegacyComponentSerializer.legacyAmpersand().deserialize(getPlugin().getMessages().IP_NOT_SPECIFIED())
            );
            return;
        }

        String ip = args[0];

        if (!IPUtil.checkIP(ip)) {
            source.sendMessage(
                    LegacyComponentSerializer.legacyAmpersand().deserialize(getPlugin().getMessages().INVALID_IP())
            );
            return;
        }

        String playerName = null;
        if (args.length >= 2)
            playerName = args[1];

        if (playerName == null && source instanceof ConsoleCommandSource) {
            source.sendMessage(
                    LegacyComponentSerializer.legacyAmpersand().deserialize(getPlugin().getMessages().PLAYER_NAME_NOT_SPECIFIED_FROM_CONSOLE())
            );
            return;
        }

        OfflinePlayer player = getPlugin().getOpDatabase().getUser(
                (source instanceof Player && playerName == null) ? ((Player) source).getUsername() : Objects.requireNonNull(playerName)
        );

        if (player == null) {
            source.sendMessage(
                    LegacyComponentSerializer.legacyAmpersand().deserialize(getPlugin().getMessages().PLAYER_NOT_FOUND())
            );
            return;
        }

        if (playerName != null && !VelocityUtil.hasPermission(source, Permissions.ADD_IP_OTHER)) {
            source.sendMessage(
                    LegacyComponentSerializer.legacyAmpersand().deserialize(getPlugin().getMessages().NO_CHANGE_OTHER_WHITELIST_PERMISSION())
            );
            return;
        }

        if (getPlugin().getWhitelistDatabase().addIP(player.getUuid(), ip))
            source.sendMessage(
                    LegacyComponentSerializer.legacyAmpersand().deserialize(
                            getPlugin().getMessages().IP_ADDED_IN_WHITELIST().replaceAll("%player%", Objects.requireNonNull(player.getName()))
                    )
            );
        else
            source.sendMessage(
                    LegacyComponentSerializer.legacyAmpersand().deserialize(
                            getPlugin().getMessages().IP_ALREADY_IN_WHITELIST().replaceAll("%player%", Objects.requireNonNull(player.getName()))
                    )
            );

        getPlugin().getWhitelistDatabase().saveData();
    }

    @Override
    public List<String> suggest(CommandSource source, String alias, String[] args) {
        return List.of();
    }
}
