package me.adrigamer2950.accountguard.velocity.commands;

import com.velocitypowered.api.command.CommandSource;
import me.adrigamer2950.accountguard.velocity.AGVelocity;
import me.adrigamer2950.accountguard.velocity.objects.Command;
import me.adrigamer2950.accountguard.velocity.objects.SubCommand;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.List;

public class ReloadSubCommand extends SubCommand<AGVelocity> {

    public ReloadSubCommand(Command<AGVelocity> parent, String name) {
        super(parent, name);
    }

    @Override
    public void execute(CommandSource source, String alias, String[] args) {
        getPlugin().reloadConfig();
        getPlugin().reloadMessages();

        source.sendMessage(
                LegacyComponentSerializer.legacyAmpersand().deserialize(
                        getPlugin().getMessages().RELOAD_MESSAGE()
                )
        );
    }

    @Override
    public List<String> suggest(CommandSource source, String alias, String[] args) {
        return null;
    }
}
