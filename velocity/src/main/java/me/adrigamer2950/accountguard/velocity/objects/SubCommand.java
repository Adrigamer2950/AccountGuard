package me.adrigamer2950.accountguard.velocity.objects;

import lombok.Getter;
import me.adrigamer2950.accountguard.velocity.AGVelocity;

@Getter
public abstract class SubCommand<T extends AGVelocity> extends Command<T> {

    private final Command<T> parent;

    public SubCommand(Command<T> parent, String name) {
        super(parent.getPlugin(), name);

        this.parent = parent;
    }
}