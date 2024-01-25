package me.adrigamer2950.accountguard.bukkit.commands;

import me.adrigamer2950.accountguard.bukkit.AGBukkit;
import me.adrigamer2950.accountguard.bukkit.commands.whitelist.AddIPSubCommand;
import me.adrigamer2950.accountguard.bukkit.commands.whitelist.ListIPsSubCommand;
import me.adrigamer2950.accountguard.bukkit.commands.whitelist.RemoveIPSubCommand;
import me.adrigamer2950.accountguard.bukkit.util.Permissions;
import me.adrigamer2950.adriapi.api.colors.Colors;
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
        addSubCommand(new RemoveIPSubCommand(this, "remove"));
        addSubCommand(new ListIPsSubCommand(this, "list"));
        addSubCommand(new ReloadSubCommand(this, "reload"));

        setHelpSubCommand(new HelpSubCommand(this, "help"));
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if(!Permissions.hasPermission(commandSender, Permissions.COMMAND)) {
            commandSender.sendMessage(Colors.translateColors(((AGBukkit) getPlugin()).messages.NO_PERMISSION));
            return true;
        }

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
