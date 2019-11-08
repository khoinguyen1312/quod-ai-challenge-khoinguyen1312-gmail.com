package ai.quod.challenge;

import ai.quod.challenge.metric.MetricService;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Scanner;

public class HealthScoreCalculator {
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

        MetricService metricService = new MetricService(gitHubArchiveClient);
        metricService.buildMetric(start, end);
    }
}
