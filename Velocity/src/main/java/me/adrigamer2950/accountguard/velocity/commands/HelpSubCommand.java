package me.adrigamer2950.accountguard.velocity.commands;

import com.velocitypowered.api.command.CommandSource;
import me.adrigamer2950.accountguard.velocity.AGVelocity;
import me.adrigamer2950.accountguard.velocity.objects.Command;
import me.adrigamer2950.accountguard.velocity.objects.SubCommand;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HelpSubCommand extends SubCommand {

    public HelpSubCommand(AGVelocity plugin, Command parent, String name) {
        super(plugin, parent, name);
    }

    @Override
    public void execute(CommandSource source, String alias, String[] args) {
        List<TextComponent> strs = Stream.of(
                "&a&m----------------------------------------------",
                "&7> &e/agv help &aShows this help message",
                "&7> &e/agv add <player> <ip> &aAdds an ip to a player's whitelist",
                "&7> &e/agv remove <player> <ip> &aRemoves an ip from a player's whitelist",
                "&7> &e/agv list <player> &aShows current IPs listed for a player",
                "&7> &e/agv reload &aReloads configurations and messages",
                "&a&m----------------------------------------------"
        ).map(str -> LegacyComponentSerializer.legacy('&').deserialize(str)).collect(Collectors.toList());

        for(TextComponent text : strs)
            source.sendMessage(text);
    }

    @Override
    public List<String> suggest(CommandSource source, String alias, String[] args) {
        return null;
    }
}
