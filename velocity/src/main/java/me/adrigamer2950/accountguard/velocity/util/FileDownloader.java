package me.adrigamer2950.accountguard.velocity.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;

public class FileDownloader {

    public static void downloadFromMavenCentral(String groupId, String artifactId, String version, Path destinationFolder) throws IOException {
        String url = "https://repo1.maven.org/maven2/%s/%s/%s/%s-%s.jar".formatted(
                groupId.replaceAll("\\.", "/"),
                artifactId,
                version,
                artifactId,
                version
        );
        Path destination = destinationFolder.resolve("%s-%s.jar".formatted(
                artifactId,
                version
        ));

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");

        con.connect();

        InputStream is = con.getInputStream();
        byte[] buffer = new byte[4096];
        int bytesRead;
        FileOutputStream fos = new FileOutputStream(destination.toAbsolutePath().toString());
        while ((bytesRead = is.read(buffer)) != -1) {
            fos.write(buffer, 0, bytesRead);
        }
        fos.close();
        is.close();
    }
}
