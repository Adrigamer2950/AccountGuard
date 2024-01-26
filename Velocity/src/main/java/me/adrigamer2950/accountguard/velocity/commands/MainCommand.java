package me.adrigamer2950.accountguard.velocity.commands;

import com.velocitypowered.api.command.CommandSource;
import me.adrigamer2950.accountguard.velocity.AGVelocity;
import me.adrigamer2950.accountguard.velocity.objects.Command;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.List;

public class MainCommand extends Command {

    public MainCommand(AGVelocity plugin, String name) {
        super(plugin, name);
    }

    @Override
    public void execute(CommandSource source, String alias, String[] args) {
        source.sendMessage(LegacyComponentSerializer.legacy('&').deserialize("&aTest"));
    }

    @Override
    public List<String> suggest(CommandSource source, String alias, String[] args) {
        return null;
    }
}
