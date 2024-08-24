package me.adrigamer2950.accountguard.velocity.commands;

import com.velocitypowered.api.command.CommandSource;
import me.adrigamer2950.accountguard.velocity.AGVelocity;
import me.adrigamer2950.accountguard.velocity.objects.Command;
import me.adrigamer2950.accountguard.velocity.objects.SubCommand;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.List;

public class HelpSubCommand extends SubCommand<AGVelocity> {

    public HelpSubCommand(Command<AGVelocity> parent, String name) {
        super(parent, name);
    }

    @Override
    public void execute(CommandSource source, String alias, String[] args) {
        List<String> l = List.of(
                "&m                                                      ",
                "&6<> &farguments are mandatory, &6[] &farguments are optional except in console",
                "&7> &e/%s help &aShows this help message".formatted(alias),
                "&7> &e/%s reload &aReload configuration files".formatted(alias),
                "&7> &e/%s add <ip> [player] &aShows this help message".formatted(alias),
                "&7> &e/%s remove <ip> [player] &aShows this help message".formatted(alias),
                "&7> &e/%s list [player] &aShows this help message".formatted(alias),
                "&m                                                      "
        );

        for (String s : l)
            source.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(s));
    }

    @Override
    public List<String> suggest(CommandSource source, String alias, String[] args) {
        return null;
    }
}
