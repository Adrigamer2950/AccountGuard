package me.adrigamer2950.accountguard.bukkit.commands;

import me.adrigamer2950.accountguard.bukkit.AGBukkit;
import me.adrigamer2950.accountguard.bukkit.commands.whitelist.AddIPSubCommand;
import me.adrigamer2950.accountguard.bukkit.commands.whitelist.ListIPSubCommand;
import me.adrigamer2950.accountguard.bukkit.commands.whitelist.RemoveIPSubCommand;
import me.adrigamer2950.adriapi.api.command.Command;
import me.adrigamer2950.adriapi.api.user.User;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MainCommand extends Command<AGBukkit> {

    public MainCommand(@NotNull AGBukkit pl, @NotNull String name) {
        super(pl, name);

        setHelpSubCommand(new HelpSubCommand(this, "help"));

        addSubCommand(new ReloadSubCommand(this, "reload"));

        addSubCommand(new AddIPSubCommand(this, "add"));
        addSubCommand(new RemoveIPSubCommand(this, "remove"));
        addSubCommand(new ListIPSubCommand(this, "list"));
    }

    @Override
    public boolean execute(User user, String label, String[] args) {
        return parseSubCommands(user, label, args);
    }

    @Override
    public List<String> tabComplete(@NotNull User user, @NotNull String label, @NotNull String[] args) {
        return parseSubCommandsTabCompleter(user, label, args);
    }
}
