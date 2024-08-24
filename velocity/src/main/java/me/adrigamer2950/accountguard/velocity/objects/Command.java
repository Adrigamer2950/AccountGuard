package me.adrigamer2950.accountguard.velocity.objects;

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.adrigamer2950.accountguard.velocity.AGVelocity;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public abstract class Command<T extends AGVelocity> implements SimpleCommand {

    private final T plugin;
    private final String name;
    private final Set<SubCommand<T>> subCommands = new HashSet<>();

    public final void addSubCommand(SubCommand<T> subCommand) {
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
            Optional<SubCommand<T>> sc = this.subCommands.stream().filter(cmd -> arg.equalsIgnoreCase(cmd.getName())).findFirst();

            if(sc.isEmpty()) continue;

            List<String> l = new ArrayList<>(Arrays.asList(args));
            l.remove(0);
            //noinspection ToArrayCallWithZeroLengthArrayArgument
            args = l.toArray(new String[l.size()]);

            sc.get().execute(source, alias, args);

            return;
        }

        Optional<SubCommand<T>> sc = this.subCommands.stream().filter(cmd -> cmd.getName().equals("help")).findFirst();

        if(sc.isEmpty()) return;

        if (args.length > 0) {
            List<String> l = new ArrayList<>(Arrays.asList(args));
            l.remove(0);
            //noinspection ToArrayCallWithZeroLengthArrayArgument
            args = l.toArray(new String[l.size()]);
        }

        sc.get().execute(source, alias, args);
    }

    public abstract List<String> suggest(CommandSource source, String alias, String[] args);

    @Override
    public final List<String> suggest(Invocation invocation) {
        return suggest(invocation.source(), invocation.alias(), invocation.arguments());
    }

    public final List<String> suggestSubCommands(CommandSource source, String alias, String[] args) {
        for(String arg : args) {
            Optional<SubCommand<T>> sc = this.subCommands.stream().filter(cmd -> arg.equalsIgnoreCase(cmd.getName())).findFirst();

            if(sc.isEmpty()) continue;

            return sc.get().suggest(source, alias, args);
        }

        return this.subCommands.stream().map(Command::getName).filter(str -> args.length != 1 || str.startsWith(args[0])).collect(Collectors.toList());
    }

    public final void register(CommandManager commandManager) {
        CommandMeta meta = commandManager.metaBuilder(this.getName())
                .plugin(plugin)
                .build();

        commandManager.register(meta, this);
    }
}