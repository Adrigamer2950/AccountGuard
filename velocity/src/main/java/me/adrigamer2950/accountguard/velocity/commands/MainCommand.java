package me.adrigamer2950.accountguard.velocity.commands;

import com.velocitypowered.api.command.CommandSource;
import me.adrigamer2950.accountguard.velocity.AGVelocity;
import me.adrigamer2950.accountguard.velocity.commands.whitelist.AddIPSubCommand;
import me.adrigamer2950.accountguard.velocity.commands.whitelist.ListIPSubCommand;
import me.adrigamer2950.accountguard.velocity.commands.whitelist.RemoveIPSubCommand;
import me.adrigamer2950.accountguard.velocity.objects.Command;

import java.util.List;

public class MainCommand extends Command<AGVelocity> {

    public MainCommand(AGVelocity plugin, String name) {
        super(plugin, name);

        addSubCommand(new HelpSubCommand(this, "help"));

        addSubCommand(new ReloadSubCommand(this, "reload"));

        addSubCommand(new AddIPSubCommand(this, "add"));
        addSubCommand(new RemoveIPSubCommand(this, "remove"));
        addSubCommand(new ListIPSubCommand(this, "list"));
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
