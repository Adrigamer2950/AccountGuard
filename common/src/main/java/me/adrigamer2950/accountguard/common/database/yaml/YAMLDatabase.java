package me.adrigamer2950.accountguard.common.database.yaml;

import dev.dejvokep.boostedyaml.YamlDocument;
import me.adrigamer2950.accountguard.common.database.Database;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class YAMLDatabase extends Database {

    protected final YamlDocument yaml;

    public YAMLDatabase(File file) {
        this(file, null);
    }

    public YAMLDatabase(File file, InputStream defaults) {
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            this.yaml = YamlDocument.create(
                    file,
                    defaults
            );

            this.loadData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public YAMLDatabase(YamlDocument yaml) {
        this.yaml = yaml;

        this.loadData();
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
}
