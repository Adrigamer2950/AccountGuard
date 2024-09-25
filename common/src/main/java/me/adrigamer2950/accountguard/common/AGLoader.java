package me.adrigamer2950.accountguard.common;

import net.byteflux.libby.Library;
import net.byteflux.libby.LibraryManager;
import net.byteflux.libby.relocation.Relocation;

public class AGLoader {

    public static void loadLibraries(LibraryManager libraryManager) {
        Library yaml = Library.builder()
                .groupId("dev{}dejvokep")
                .artifactId("boosted-yaml")
                .version("1.3.6")
                .relocate(new Relocation("dev{}dejvokep{}boostedyaml", "me{}adrigamer2950{}accountguard{}libs{}boosted-yaml"))
                .build();

        Library h2 = Library.builder()
                .groupId("com{}h2database")
                .artifactId("h2")
                .version("2.2.220")
                .relocate(new Relocation("org{}h2", "me{}adrigamer2950{}accountguard{}libs{}h2"))
                .build();

        Library sqlite = Library.builder()
                .groupId("org{}xerial")
                .artifactId("sqlite-jdbc")
                .version("3.46.1.0")
                .relocate(new Relocation("org{}sqlite", "me{}adrigamer2950{}accountguard{}libs{}sqlite"))
                .url("https://github.com/xerial/sqlite-jdbc/releases/download/3.46.1.0/sqlite-jdbc-3.46.1.0.jar")
                .build();

        libraryManager.addMavenCentral();

        libraryManager.loadLibrary(yaml);
        libraryManager.loadLibrary(h2);
        libraryManager.loadLibrary(sqlite);
    }
}
