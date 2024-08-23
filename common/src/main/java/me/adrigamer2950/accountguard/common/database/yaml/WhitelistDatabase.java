package me.adrigamer2950.accountguard.common.database.yaml;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class WhitelistDatabase extends YAMLDatabase {

    public WhitelistDatabase(File file) {
        super(file);
    }

    @Override
    public void loadData() {
        super.loadData();

        if (!this.yaml.isSection()) return;

        for (String _uuid : this.yaml.getRoutesAsStrings(false)) {
            try {
                UUID uuid = UUID.fromString(_uuid);

                //noinspection unchecked
                List<String> ips = (List<String>) this.yaml.getList(_uuid);

                this.ips.put(uuid, new HashSet<>(ips));
            } catch (IllegalArgumentException ignored) {}
        }
    }

    @Override
    public void saveData() {
        this.ips.forEach((uuid, ips) -> this.yaml.set(uuid.toString(), ips.stream().toList()));

        super.saveData();
    }
}
