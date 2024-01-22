package me.adrigamer2950.accountguard.bukkit.commands;

import me.adrigamer2950.adriapi.api.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MainCommand extends Command {

    public MainCommand(@NotNull Plugin pl, @NotNull String name, @Nullable List<String> aliases) {
        super(pl, name, aliases);
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        return parseSubCommands(commandSender, s, strings);
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
        return parseSubCommandsTabCompleter(commandSender, s, strings);
    }
}
