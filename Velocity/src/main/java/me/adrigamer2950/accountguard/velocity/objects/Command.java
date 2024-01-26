package me.adrigamer2950.accountguard.velocity.objects;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import me.adrigamer2950.accountguard.velocity.AGVelocity;

import java.util.List;

public abstract class Command implements SimpleCommand {

    private final AGVelocity plugin;
    private final String name;

    public Command(AGVelocity plugin, String name) {
        this.plugin = plugin;
        this.name = name;
    }

    public final String getName() {
        return this.name;
    }

    public final AGVelocity getPlugin() {
        return this.plugin;
    }

    public abstract void execute(CommandSource source, String alias, String[] args);

    @Override
    public final void execute(Invocation invocation) {
        execute(invocation.source(), invocation.alias(), invocation.arguments());
    }

    public abstract List<String> suggest(CommandSource source, String alias, String[] args);

    @Override
    public final List<String> suggest(Invocation invocation) {
        return suggest(invocation.source(), invocation.alias(), invocation.arguments());
    }
}
