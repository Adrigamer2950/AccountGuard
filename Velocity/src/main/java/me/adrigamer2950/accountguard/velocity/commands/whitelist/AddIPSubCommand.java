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
import java.util.UUID;

public class AddIPSubCommand extends SubCommand {

    public AddIPSubCommand(AGVelocity plugin, Command parent, String name) {
        super(plugin, parent, name);
    }

    @Override
    public void execute(CommandSource source, String alias, String[] args) {
        if(!VelocityPermissions.hasPermission(source, Permissions.ADD_IPS)) {
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

        if (args.length < 3) {
            source.sendMessage(LegacyComponentSerializer.legacy('&')
                    .deserialize(getPlugin().getPluginConfig().Prefix() + getPlugin().getPluginMessages().IP_NOT_SPECIFIED()
                    .replaceAll("%player%", args[1])
            ));
            return;
        }

        String ip = args[2];

        if (!IPUtil.isValid(ip)) {
            source.sendMessage(LegacyComponentSerializer.legacy('&')
                    .deserialize(getPlugin().getPluginConfig().Prefix() + getPlugin().getPluginMessages().INVALID_IP()
                    .replaceAll("%player%", args[1])
                    .replaceAll("%ip%", ip)
            ));
            return;
        }

        if (getPlugin().getDatabase().hasIP(uuid, ip)) {
            source.sendMessage(LegacyComponentSerializer.legacy('&')
                    .deserialize(getPlugin().getPluginConfig().Prefix() + getPlugin().getPluginMessages().IP_ALREADY_IN_WHITELIST()
                    .replaceAll("%player%", args[1])
                    .replaceAll("%ip%", ip)
            ));
            return;
        }

        getPlugin().getDatabase().addIP(uuid, ip);

        source.sendMessage(LegacyComponentSerializer.legacy('&')
                .deserialize(getPlugin().getPluginConfig().Prefix() + getPlugin().getPluginMessages().IP_ADDED_TO_WHITELIST()
                .replaceAll("%player%", args[1])
                .replaceAll("%ip%", ip)
        ));
    }

    @Override
    public List<String> suggest(CommandSource source, String alias, String[] args) {
        if(args.length == 2)
            return AGVelocity.getProxy().getAllPlayers().stream().map(Player::getUsername).filter(name -> name.toLowerCase().startsWith(args[1])).toList();

        return List.of();
    }
}
