package me.adrigamer2950.accountguard.common.database.yaml;

import dev.dejvokep.boostedyaml.YamlDocument;
import me.adrigamer2950.accountguard.common.database.Database;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class YAMLDatabase extends Database {

    private final YamlDocument yaml;

    public YAMLDatabase(File file) {
        this(file, null);
    }

    public YAMLDatabase(File file, InputStream defaults) {
        try {
            this.yaml = YamlDocument.create(
                    file,
                    defaults
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.loadData();
    }

    @Override
    public void loadData() {
        try {
            this.yaml.reload();

            if (!this.yaml.isSection()) return;

            for (String _uuid : this.yaml.getRoutesAsStrings(false)) {
                try {
                    UUID uuid = UUID.fromString(_uuid);

                    //noinspection unchecked
                    List<String> ips = (List<String>) this.yaml.getList(_uuid);

                    this.ips.put(uuid, new HashSet<>(ips));
                } catch (IllegalArgumentException ignored) {}
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveData() {
        try {
            this.ips.forEach((uuid, ips) -> this.yaml.set(uuid.toString(), ips.stream().toList()));

            this.yaml.save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
