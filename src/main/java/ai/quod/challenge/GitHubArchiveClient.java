package ai.quod.challenge;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.commons.io.FileUtils;

public class GitHubArchiveClient {
    private static final int CONNECT_TIMEOUT = 6000;
    private static final int READ_TIMEOUT = 6000;
    private static final String FILE_URL = "https://data.gharchive.org/%s.json.gz";

    private GZipReader gZipReader;

    public GitHubArchiveClient(GZipReader gZipReader) {
        this.gZipReader = gZipReader;
    }

    public Path getLastOneHourArchive() {

        Calendar timeToGetGitHubArchive = Calendar.getInstance();
        timeToGetGitHubArchive.add(Calendar.HOUR, -2);
        timeToGetGitHubArchive.add(Calendar.YEAR, -1);

        String gitHubFileUrl = buildGitHubArchiveUrl(timeToGetGitHubArchive);
        System.out.println("Getting archive file: " + gitHubFileUrl);

        Path tempFile = Utils.buildTempFileWithReadWritePermission();

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
        SimpleDateFormat gitHubDateFormat = new SimpleDateFormat("yyyy-MM-dd-H");
        String formattedDate = gitHubDateFormat.format(time.getTime());

        return String.format(FILE_URL, formattedDate);
    }

}
