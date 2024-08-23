package me.adrigamer2950.accountguard.velocity.database;

import dev.dejvokep.boostedyaml.YamlDocument;
import me.adrigamer2950.accountguard.common.database.yaml.YAMLDatabase;

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

        this.players.forEach((username, uuid) -> {
            this.yaml.set(username, uuid.toString());
        });

        super.saveData();
    }
}
