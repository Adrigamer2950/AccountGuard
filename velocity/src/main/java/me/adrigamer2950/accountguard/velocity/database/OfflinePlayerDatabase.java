package me.adrigamer2950.accountguard.velocity.database;

import dev.dejvokep.boostedyaml.YamlDocument;
import me.adrigamer2950.accountguard.common.database.yaml.YAMLDatabase;
import me.adrigamer2950.accountguard.velocity.AGVelocity;
import me.adrigamer2950.accountguard.velocity.objects.OfflinePlayer;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class OfflinePlayerDatabase extends YAMLDatabase {

    public final HashMap<String, UUID> players;

    public OfflinePlayerDatabase(YamlDocument yaml, AGVelocity plugin) {
        super(yaml);

        this.players = new HashMap<>();

        plugin.getProxy().getScheduler().buildTask(plugin, this::saveData).repeat(60, TimeUnit.SECONDS).schedule();
    }

    @Override
    public void loadData() {
        super.loadData();

        for (String username : this.yaml.getRoutesAsStrings(false)) {
            try {
                UUID uuid = UUID.fromString(this.yaml.getString(username));

                this.players.put(username, uuid);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    @Override
    public void saveData() {
        this.yaml.clear();

        this.players.forEach((username, uuid) -> this.yaml.set(username, uuid.toString()));

        super.saveData();
    }

    public boolean userExists(String u1) {
        for (String u2 : this.players.keySet()) {
            if (u1.equalsIgnoreCase(u2)) return true;
        }

        return false;
    }

    public String getUsername(String u1) {
        for (String u2 : this.players.keySet()) {
            if (u1.equalsIgnoreCase(u2)) return u2;
        }

        return null;
    }

    public void addUser(UUID uuid, String username) {
        if (this.userExists(username)) return;

        this.players.put(username, uuid);
    }

    public OfflinePlayer getUser(String _username) {
        if (!this.userExists(_username))
            return null;

        String username = this.getUsername(_username);

        return new OfflinePlayer(this.players.get(username), username);
    }
}
