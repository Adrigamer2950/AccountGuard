package me.adrigamer2950.accountguard.velocity.commands;

import com.velocitypowered.api.command.CommandSource;
import me.adrigamer2950.accountguard.common.permissions.Permissions;
import me.adrigamer2950.accountguard.velocity.AGVelocity;
import me.adrigamer2950.accountguard.velocity.objects.Command;
import me.adrigamer2950.accountguard.velocity.objects.SubCommand;
import me.adrigamer2950.accountguard.velocity.util.VelocityPermissions;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.List;

public class ReloadSubCommand extends SubCommand {

    public ReloadSubCommand(AGVelocity plugin, Command parent, String name) {
        super(plugin, parent, name);
    }

    @Override
    public void execute(CommandSource source, String alias, String[] args) {
        if(!VelocityPermissions.hasPermission(source, Permissions.RELOAD)) {
            source.sendMessage(
                    LegacyComponentSerializer.legacy('&')
                            .deserialize(getPlugin().getPluginConfig().Prefix() + getPlugin().getPluginMessages().NO_PERMISSION())
            );
            return;
        }

        getPlugin().reloadConfig();
        getPlugin().reloadMessages();

        source.sendMessage(
                LegacyComponentSerializer.legacy('&')
                        .deserialize(getPlugin().getPluginConfig().Prefix() + getPlugin().getPluginMessages().RELOAD_MESSAGE())
        );
    }

    @Override
    public List<String> suggest(CommandSource source, String alias, String[] args) {
        return null;
    }
}
