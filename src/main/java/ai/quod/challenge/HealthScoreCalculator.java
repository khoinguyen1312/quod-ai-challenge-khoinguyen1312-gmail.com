package ai.quod.challenge;

import ai.quod.challenge.metric.MetricService;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class HealthScoreCalculator {
    public static void main(String[] args) throws FileNotFoundException {
        Utils.activateCerificateForDownloadFromHttps();

        GitHubArchiveClient gitHubArchiveClient = new GitHubArchiveClient(new GZipReader());

        Scanner argumentScanner = new Scanner(System.in);

//        String datetimeStart = argumentScanner.next();
//        String datetimeEnd = argumentScanner.next();
        MetricService metricService = new MetricService(gitHubArchiveClient);
        metricService.buildMetric();

    }
}
