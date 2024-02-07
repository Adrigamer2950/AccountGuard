package me.adrigamer2950.accountguard.velocity.commands;

import com.velocitypowered.api.command.CommandSource;
import me.adrigamer2950.accountguard.velocity.AGVelocity;
import me.adrigamer2950.accountguard.velocity.commands.whitelist.AddIPSubCommand;
import me.adrigamer2950.accountguard.velocity.commands.whitelist.ListIPsSubCommand;
import me.adrigamer2950.accountguard.velocity.commands.whitelist.RemoveIPSubCommand;
import me.adrigamer2950.accountguard.velocity.objects.Command;

import java.util.List;

public class MainCommand extends Command {

    public MainCommand(AGVelocity plugin, String name) {
        super(plugin, name);

        addSubCommand(new HelpSubCommand(plugin, this, "help"));
        addSubCommand(new ReloadSubCommand(plugin, this, "reload"));

        addSubCommand(new AddIPSubCommand(plugin, this, "add"));
        addSubCommand(new RemoveIPSubCommand(plugin, this, "remove"));
        addSubCommand(new ListIPsSubCommand(plugin, this, "list"));
    }

    @Override
    public void execute(CommandSource source, String alias, String[] args) {
        executeSubCommands(source, alias, args);
    }

    @Override
    public List<String> suggest(CommandSource source, String alias, String[] args) {
        return suggestSubCommands(source, alias, args);
    }
}
