package me.adrigamer2950.accountguard.velocity.database;

import dev.dejvokep.boostedyaml.YamlDocument;
import me.adrigamer2950.accountguard.common.database.yaml.YAMLDatabase;
import me.adrigamer2950.accountguard.velocity.objects.OfflinePlayer;

import java.util.HashMap;
import java.util.UUID;

public class OfflinePlayerDatabase extends YAMLDatabase {

    public final HashMap<String, UUID> players = new HashMap<>();

    public OfflinePlayerDatabase(YamlDocument yaml) {
        super(yaml);
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

    public void addUser(UUID uuid, String username) {
        if (this.players.containsKey(username)) return;

        this.players.put(username.toLowerCase(), uuid);
    }

    public OfflinePlayer getUser(String _username) {
        String username = _username.toLowerCase();

        if (!this.players.containsKey(username))
            return null;

        return new OfflinePlayer(this.players.get(username), _username);
    }
}
