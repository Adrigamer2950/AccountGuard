package me.adrigamer2950.accountguard.velocity.objects;

import me.adrigamer2950.accountguard.velocity.AGVelocity;

public abstract class SubCommand extends Command {

    public SubCommand(AGVelocity plugin, String name) {
        super(plugin, name);
    }
}
