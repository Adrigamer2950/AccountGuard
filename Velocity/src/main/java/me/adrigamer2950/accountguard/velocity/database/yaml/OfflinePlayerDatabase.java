package me.adrigamer2950.accountguard.velocity.database.yaml;

import me.adrigamer2950.accountguard.common.AccountGuard;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class OfflinePlayerDatabase extends YAMLDatabase {

    public OfflinePlayerDatabase(AccountGuard plugin) {
        super("playerdata", plugin);
    }

    @Override
    public void loadData() {
        try {
            this.yaml.reload();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveData() {
        try {
            this.yaml.save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setUUID(String username, UUID uuid) {
        if(uuid != null) this.yaml.set(username.toLowerCase(), uuid.toString());
        else this.yaml.remove(username.toLowerCase());

        this.saveData();
    }

    public UUID getUUID(String username) {
        String name = this.yaml.getString(username.toLowerCase());

        if(name == null)
            name = this.yaml.getString(username);

        return name == null ? null : UUID.fromString(name);
    }

    @Override
    public void addIP(UUID uuid, String ip) {}

    @Override
    public void removeIP(UUID uuid, String ip) {}

    @Override
    public boolean hasIP(UUID uuid, String ip) {
        return false;
    }

    @Override
    public final Set<String> getIPs(UUID uuid) {
        return null;
    }
}
