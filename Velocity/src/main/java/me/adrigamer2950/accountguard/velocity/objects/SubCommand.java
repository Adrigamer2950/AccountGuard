package me.adrigamer2950.accountguard.velocity.objects;

import me.adrigamer2950.accountguard.velocity.AGVelocity;

public abstract class SubCommand extends Command {

    private final Command parent;

    public SubCommand(AGVelocity plugin, Command parent, String name) {
        super(plugin, name);

        this.parent = parent;
    }

    public final Command getParent() {
        return this.parent;
    }
}
