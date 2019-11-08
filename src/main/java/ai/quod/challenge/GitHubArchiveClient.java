package ai.quod.challenge;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.util.Calendar;
import org.apache.commons.io.FileUtils;

public class GitHubArchiveClient {
    private static final int CONNECT_TIMEOUT = 6000;
    private static final int READ_TIMEOUT = 6000;
    private static final String FILE_URL = "https://data.gharchive.org/%s.json.gz";

    private GZipReader gZipReader;

    public GitHubArchiveClient(GZipReader gZipReader) {
        this.gZipReader = gZipReader;
    }

    public Path getOneHourArchive(Calendar time) {
        String gitHubFileUrl = buildGitHubArchiveUrl(time);

        Path tempFile = Utils.buildTempFileWithReadWritePermission();

        System.out.println("Getting archive file: " + gitHubFileUrl + " to " + tempFile.toAbsolutePath());
        try {
            URL url = new URL(gitHubFileUrl);
            URLConnection connection = url.openConnection();
            FileUtils.copyURLToFile(connection.getURL(), tempFile.toFile());
        } catch (IOException e) {
            String message = String.format("Unable to get file from Git Hub Archive, url: %s", gitHubFileUrl);
            throw new RuntimeException(message, e);
        }

        System.out.println("Finish getting file: " + gitHubFileUrl);

        return gZipReader.unzipFileToTempFile(tempFile.toFile().getAbsolutePath());
    }

    private String buildGitHubArchiveUrl(Calendar time) {
        String formattedDate = Utils.formatToUtc(time);

        return String.format(FILE_URL, formattedDate);
    }

}
