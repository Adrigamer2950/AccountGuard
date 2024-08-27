package me.adrigamer2950.accountguard.velocity.commands.whitelist;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;
import me.adrigamer2950.accountguard.common.permissions.Permissions;
import me.adrigamer2950.accountguard.velocity.AGVelocity;
import me.adrigamer2950.accountguard.velocity.objects.Command;
import me.adrigamer2950.accountguard.velocity.objects.OfflinePlayer;
import me.adrigamer2950.accountguard.velocity.objects.SubCommand;
import me.adrigamer2950.accountguard.velocity.util.VelocityUtil;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.List;
import java.util.Objects;

public class ListIPSubCommand extends SubCommand<AGVelocity> {

    public ListIPSubCommand(Command<AGVelocity> parent, String name) {
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

        String playerName = null;
        if (args.length >= 1)
            playerName = args[0];

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

        if (playerName != null && !VelocityUtil.hasPermission(source, Permissions.LIST_IP_OTHER)) {
            source.sendMessage(
                    LegacyComponentSerializer.legacyAmpersand().deserialize(getPlugin().getMessages().NO_VIEW_OTHER_WHITELIST_PERMISSION())
            );
            return;
        }

        List<String> ips = getPlugin().getWhitelistDatabase().getIPs(player.getUuid()).stream().toList();

        source.sendMessage(
                LegacyComponentSerializer.legacyAmpersand().deserialize("&m|                                          &r|")
        );
        source.sendMessage(
                LegacyComponentSerializer.legacyAmpersand().deserialize(
                        getPlugin().getMessages().WHITELIST_IP_LIST().replaceAll("%player%", Objects.requireNonNull(player.getName()))
                )
        );

        for (String ip : ips) {
            source.sendMessage(
                    LegacyComponentSerializer.legacyAmpersand().deserialize("&f| &e%s".formatted(ip))
            );
        }

        source.sendMessage(
                LegacyComponentSerializer.legacyAmpersand().deserialize("&m|                                          &r|")
        );
    }

    @Override
    public List<String> suggest(CommandSource source, String alias, String[] args) {
        return getPlugin().getProxy().getAllPlayers().stream().map(Player::getUsername).filter(s -> s.toLowerCase().startsWith(args[1].toLowerCase())).toList();
    }
}
