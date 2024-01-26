package me.adrigamer2950.accountguard.velocity.objects;

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import me.adrigamer2950.accountguard.velocity.AGVelocity;

import java.util.*;
import java.util.stream.Collectors;

public abstract class Command implements SimpleCommand {

    private final AGVelocity plugin;
    private final String name;
    private final Set<SubCommand> subCommands;

    public Command(AGVelocity plugin, String name) {
        this.plugin = plugin;
        this.name = name;
        this.subCommands = new HashSet<>();
    }

    public final String getName() {
        return this.name;
    }

    public final AGVelocity getPlugin() {
        return this.plugin;
    }

    public final Set<SubCommand> getSubCommands() {
        return this.subCommands;
    }

    public final void addSubCommand(SubCommand subCommand) {
        Objects.requireNonNull(subCommand);

        this.subCommands.add(subCommand);
    }

    public abstract void execute(CommandSource source, String alias, String[] args);

    @Override
    public final void execute(Invocation invocation) {
        execute(invocation.source(), invocation.alias(), invocation.arguments());
    }

    public final void executeSubCommands(CommandSource source, String alias, String[] args) {
        for(String arg : args) {
            Optional<SubCommand> sc = this.subCommands.stream().filter(cmd -> arg.equalsIgnoreCase(cmd.getName())).findFirst();

            if(sc.isEmpty()) continue;

            sc.get().execute(source, alias, args);
        }
    }

    public abstract List<String> suggest(CommandSource source, String alias, String[] args);

    @Override
    public final List<String> suggest(Invocation invocation) {
        return suggest(invocation.source(), invocation.alias(), invocation.arguments());
    }

    public final List<String> suggestSubCommands(CommandSource source, String alias, String[] args) {

        for(String arg : args) {
            Optional<SubCommand> sc = this.subCommands.stream().filter(cmd -> arg.equalsIgnoreCase(cmd.getName())).findFirst();

            if(sc.isEmpty()) continue;

            return sc.get().suggest(source, alias, args);
        }

        return this.subCommands.stream().map(Command::getName).filter(str -> args[0].startsWith(str)).collect(Collectors.toList());
    }

    public final void register(CommandManager commandManager) {
        CommandMeta meta = commandManager.metaBuilder(this.getName())
                .plugin(plugin)
                .build();

        commandManager.register(meta, this);
    }
}
