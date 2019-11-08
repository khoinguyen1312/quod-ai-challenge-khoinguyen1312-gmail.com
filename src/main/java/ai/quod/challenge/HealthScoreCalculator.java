package ai.quod.challenge;

import ai.quod.challenge.metric.MetricService;
import java.text.ParseException;
import java.util.Calendar;

public class HealthScoreCalculator {
    private static final int NUMBER_OF_TOP_RECORD_WOULD_LIKE_TO_PRINT = 1000;

    public static void main(String[] args) {
        Utils.activateCerificateForDownloadFromHttps();

        GitHubArchiveClient gitHubArchiveClient = new GitHubArchiveClient(new GZipReader());

        String startArg = args[0];
        String endArg = args[1];

        Calendar start;
        Calendar end;
        try {
            start = Utils.parseIso8601DateFormat(startArg);
            end = Utils.parseIso8601DateFormat(endArg);
        } catch (ParseException e) {
            throw new RuntimeException("Unable to parse input argument", e);
        }

        System.out.println(start.getTime());
        System.out.println(end.getTime());

        MetricService metricService = new MetricService(NUMBER_OF_TOP_RECORD_WOULD_LIKE_TO_PRINT, gitHubArchiveClient);
        metricService.buildMetric(start, end);

        System.out.println("Finish");
    }
}
