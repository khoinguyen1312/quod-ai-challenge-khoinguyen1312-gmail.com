package ai.quod.challenge;

import java.util.Scanner;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HealthScoreCalculator {
    public static void main(String[] args) {
        Scanner argumentScanner = new Scanner(System.in);

//        String datetimeStart = argumentScanner.next();
//        String datetimeEnd = argumentScanner.next();

        Utils.activateCerificateForDownloadFromHttps();

        GitHubArchiveClient gitHubArchiveClient = new GitHubArchiveClient(new GZipReader());
        gitHubArchiveClient.getLastOneHourArchive();
    }
}
