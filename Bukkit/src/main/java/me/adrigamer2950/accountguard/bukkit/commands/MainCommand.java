package me.adrigamer2950.accountguard.bukkit.commands;

import me.adrigamer2950.accountguard.bukkit.commands.whitelist.AddIPSubCommand;
import me.adrigamer2950.accountguard.bukkit.commands.whitelist.ListIPsSubCommand;
import me.adrigamer2950.adriapi.api.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MainCommand extends Command {

    public MainCommand(@NotNull Plugin pl, @NotNull String name, @Nullable List<String> aliases) {
        super(pl, name, aliases);

        addSubCommand(new AddIPSubCommand(this, "add"));
        addSubCommand(new ListIPsSubCommand(this, "list"));
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        return parseSubCommands(commandSender, s, strings);
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] args) {
        if(args.length < 2)
            return parseSubCommandsTabCompleter(commandSender, s, args).stream()
                .filter(str -> str.toLowerCase().startsWith(args.length < 1 ? "" : args[0]))
                .toList();

        return parseSubCommandsTabCompleter(commandSender, s, args);
    }
}
