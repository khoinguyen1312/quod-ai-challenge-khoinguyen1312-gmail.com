package ai.quod.challenge;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Scanner;

public class HealthScoreCalculator {
    public static void main(String[] args) {
        Utils.activateCerificateForDownloadFromHttps();

        GitHubArchiveClient gitHubArchiveClient = new GitHubArchiveClient(new GZipReader());

        Scanner argumentScanner = new Scanner(System.in);

        String startArg = argumentScanner.next();
        String endArg = argumentScanner.next();

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



    }
}
