package me.adrigamer2950.accountguard.bukkit.commands;

import me.adrigamer2950.accountguard.bukkit.AGBukkit;
import me.adrigamer2950.adriapi.api.command.Command;
import me.adrigamer2950.adriapi.api.command.SubCommand;
import me.adrigamer2950.adriapi.api.user.User;

import java.util.List;

public class HelpSubCommand extends SubCommand<AGBukkit> {

    public HelpSubCommand(Command<AGBukkit> parent, String name) {
        super(parent, name);
    }

    @Override
    public boolean execute(User user, String label, String[] args) {
        List<String> l = List.of(
                "&m                                                      ",
                "&6<> &farguments are mandatory, &6[] &farguments are optional",
                "&7> &e/%s help &aShows this help message".formatted(label),
                "&7> &e/%s reload &aReload configuration files".formatted(label),
                "&7> &e/%s add <ip> [player] &aShows this help message".formatted(label),
                "&7> &e/%s remove <ip> [player] &aShows this help message".formatted(label),
                "&7> &e/%s list [player] &aShows this help message".formatted(label),
                "&m                                                      "
        );

        for (String s : l)
            user.sendMessage(s);

        return true;
    }
}
