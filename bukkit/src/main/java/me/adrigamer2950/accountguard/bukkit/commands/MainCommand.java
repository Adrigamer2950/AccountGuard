package me.adrigamer2950.accountguard.bukkit.commands;

import me.adrigamer2950.accountguard.bukkit.AGBukkit;
import me.adrigamer2950.adriapi.api.command.Command;
import me.adrigamer2950.adriapi.api.user.User;
import org.jetbrains.annotations.NotNull;

public class MainCommand extends Command<AGBukkit> {

    public MainCommand(@NotNull AGBukkit pl, @NotNull String name) {
        super(pl, name);
    }

    @Override
    public boolean execute(User user, String label, String[] args) {
        return parseSubCommands(user, label, args);
    }
}
