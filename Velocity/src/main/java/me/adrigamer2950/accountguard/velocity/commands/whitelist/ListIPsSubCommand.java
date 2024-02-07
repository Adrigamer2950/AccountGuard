package me.adrigamer2950.accountguard.velocity.commands.whitelist;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import me.adrigamer2950.accountguard.common.permissions.Permissions;
import me.adrigamer2950.accountguard.common.util.IPUtil;
import me.adrigamer2950.accountguard.velocity.AGVelocity;
import me.adrigamer2950.accountguard.velocity.objects.Command;
import me.adrigamer2950.accountguard.velocity.objects.SubCommand;
import me.adrigamer2950.accountguard.velocity.util.VelocityPermissions;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ListIPsSubCommand extends SubCommand {

    public ListIPsSubCommand(AGVelocity plugin, Command parent, String name) {
        super(plugin, parent, name);
    }

    @Override
    public void execute(CommandSource source, String alias, String[] args) {
        if(!VelocityPermissions.hasPermission(source, Permissions.LIST_IPS)) {
            source.sendMessage(
                    LegacyComponentSerializer.legacy('&')
                            .deserialize(getPlugin().getPluginConfig().Prefix() + getPlugin().getPluginMessages().NO_PERMISSION())
            );
            return;
        }

        if (args.length < 2) {
            source.sendMessage(LegacyComponentSerializer.legacy('&')
                    .deserialize(getPlugin().getPluginConfig().Prefix() + getPlugin().getPluginMessages().PLAYER_NOT_SPECIFIED()));
            return;
        }

        UUID uuid = getPlugin().getOfflinePlayerDatabase().getUUID(args[1]);

        if(uuid == null) {
            source.sendMessage(LegacyComponentSerializer.legacy('&')
                    .deserialize(getPlugin().getPluginConfig().Prefix() + getPlugin().getPluginMessages().PLAYER_DOESNT_EXISTS()
                    .replaceAll("%player%", args[1])
            ));
            return;
        }

        source.sendMessage(LegacyComponentSerializer.legacy('&').deserialize(getPlugin().getPluginConfig().Prefix() + getPlugin().getPluginMessages().LIST_IPS_MESSAGE()
                .replaceAll("%player%", args[1])
        ));
        for (String ip : getPlugin().getDatabase().getIPs(uuid)) {
            source.sendMessage(
                    LegacyComponentSerializer.legacy('&').deserialize(
                            String.format("&7> &e%s", ip)
                    )
            );
        }
    }

    @Override
    public List<String> suggest(CommandSource source, String alias, String[] args) {
        if(args.length == 2)
            return AGVelocity.getProxy().getAllPlayers().stream().map(Player::getUsername).filter(name -> name.toLowerCase().startsWith(args[1])).toList();

        UUID uuid = getPlugin().getOfflinePlayerDatabase().getUUID(args[1]);

        if(uuid == null) return List.of();

        Set<String> ips = getPlugin().getDatabase().getIPs(uuid);

        if(!ips.isEmpty())
            return ips.stream().toList();

        return List.of();
    }
}
