package me.adrigamer2950.accountguard.velocity.commands.whitelist;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;
import me.adrigamer2950.accountguard.velocity.AGVelocity;
import me.adrigamer2950.accountguard.velocity.objects.Command;
import me.adrigamer2950.accountguard.velocity.objects.OfflinePlayer;
import me.adrigamer2950.accountguard.velocity.objects.SubCommand;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.List;
import java.util.Objects;

public class ListIPSubCommand extends SubCommand<AGVelocity> {

    public ListIPSubCommand(Command<AGVelocity> parent, String name) {
        super(parent, name);
    }

    @Override
    public void execute(CommandSource source, String alias, String[] args) {
        String playerName = null;
        if (args.length >= 1)
            playerName = args[0];

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

        List<String> ips = getPlugin().getWhitelistDatabase().getIPs(player.getUuid()).stream().toList();

        source.sendMessage(
                LegacyComponentSerializer.legacyAmpersand().deserialize("&f&m                                          ")
        );
        source.sendMessage(
                LegacyComponentSerializer.legacyAmpersand().deserialize("&6%s &fwhitelisted IP's:".formatted(player.getName()))
        );

        for (String ip : ips) {
            source.sendMessage(
                    LegacyComponentSerializer.legacyAmpersand().deserialize("&f| &e%s".formatted(ip))
            );
        }

        source.sendMessage(
                LegacyComponentSerializer.legacyAmpersand().deserialize("&f&m                                          ")
        );
    }

    @Override
    public List<String> suggest(CommandSource source, String alias, String[] args) {
        return null;
    }
}
