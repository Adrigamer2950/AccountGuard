package me.adrigamer2950.accountguard.velocity.database.yaml;

import dev.dejvokep.boostedyaml.YamlDocument;
import me.adrigamer2950.accountguard.common.AccountGuard;
import me.adrigamer2950.accountguard.common.database.Database;
import me.adrigamer2950.accountguard.common.database.DatabaseType;
import me.adrigamer2950.accountguard.velocity.AGVelocity;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class YAMLDatabase extends Database {

    private final YamlDocument yaml;

    public YAMLDatabase(AccountGuard plugin, DatabaseType type) {
        super(plugin, type);

        try {
            this.yaml = YamlDocument.create(
                    new File("plugins/accountguard/data.yml")
            );

            this.yaml.reload();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        loadData();
    }

    @Override
    public void loadData() {
        try {
            this.yaml.reload();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (String uuidS : this.yaml.getRoot().getKeys().stream().map(Object::toString).collect(Collectors.toSet())) {
            UUID uuid;
            try {
                uuid = UUID.fromString(uuidS);
            } catch (IllegalArgumentException e) {
                continue;
            }

            Set<String> ips = new HashSet<>(this.yaml.getStringList(uuidS));

            this.ips.put(uuid, ips);
        }
    }

    @Override
    public void saveData() {
        for(UUID uuid : this.ips.keySet()) {
            Set<String> ips = this.ips.get(uuid);

            if (ips.isEmpty()) {
                this.ips.remove(uuid);
                this.yaml.remove(uuid.toString());
            } else
                this.yaml.set(uuid.toString(), ips.stream().toList());
        }

        try {
            this.yaml.save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addIP(UUID uuid, String ip) {
        super.addIP(uuid, ip);

        this.saveData();
    }

    @Override
    public void removeIP(UUID uuid, String ip) {
        super.removeIP(uuid, ip);

        this.saveData();
    }
}
