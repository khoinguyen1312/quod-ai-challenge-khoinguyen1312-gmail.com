package ai.quod.challenge.metric;

import ai.quod.challenge.Utils;
import ai.quod.challenge.metric.model.GithubMetric;
import ai.quod.challenge.metric.model.IssueMetric;
import ai.quod.challenge.metric.model.OrgMetric;
import ai.quod.challenge.metric.model.RepoMetric;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

class MetricExtractor {

    int numberOfTopPrintingRecord;

    MetricExtractor(int numberOfTopPrintingRecord) {
        this.numberOfTopPrintingRecord = numberOfTopPrintingRecord;
    }

    File writeToOutput(GithubMetric githubMetric) {
        List<MetricRecordRaw> rawRecords = new ArrayList<>();

        for (Entry<String, OrgMetric> orgMetricEntry : githubMetric.getMetrics().entrySet()) {
            String orgName = orgMetricEntry.getKey();
            for (Entry<String, RepoMetric> repoMetricEntry : orgMetricEntry.getValue().getMetrics().entrySet()) {
                String repoName = repoMetricEntry.getKey();
                RepoMetric repoMetric = repoMetricEntry.getValue();

                MetricRecordRaw rawRecord = buildRawMetricRecord(orgName, repoName, repoMetric);

                rawRecords.add(
                    rawRecord
                );
            }
        }

        List<MetricRecordCalculated> calculatedRecords = calculateRawRecords(rawRecords, githubMetric.getHourRange());

        return printTopRecords(calculatedRecords, numberOfTopPrintingRecord);
    }

    private MetricRecordRaw buildRawMetricRecord(String orgName, String repoName, RepoMetric repoMetric) {
        int numberOfCommits = repoMetric.getNumberOfShaCommits();
        int numberOfContributors = repoMetric.getNumberOfContributors();

        long sumHourIssueRemainOpen = repoMetric
            .getIssueMetric()
            .values()
            .stream()
            .mapToLong(IssueMetric::hoursIssueRemainOpen)
            .sum();

        int numberOfIssues = repoMetric.getIssueMetric().size();

        return new MetricRecordRaw(
            orgName,
            repoName,
            numberOfCommits,
            numberOfContributors,
            sumHourIssueRemainOpen,
            numberOfIssues
        );
    }

    private List<MetricRecordCalculated> calculateRawRecords(List<MetricRecordRaw> rawRecords, int hourRange) {
        int maxNumberOfCommits = rawRecords.stream()
            .mapToInt(MetricRecordRaw::getNumCommits)
            .max()
            .getAsInt();

        int maxNumberOfContributors = rawRecords.stream()
            .mapToInt(MetricRecordRaw::getNumContributors)
            .max()
            .getAsInt();

        double minAverageIssueRemainOpen = rawRecords.stream()
            .filter(record -> record.getNumberOfIssue() != 0)
            .mapToDouble(record -> record.getSumHourIssuseRemainOpen() * 1.0 / record.getNumberOfIssue())
            .min().getAsDouble();

        return rawRecords.stream()
            .map(rawRecord ->
                rawRecord.calculate(maxNumberOfCommits, maxNumberOfContributors, minAverageIssueRemainOpen, hourRange)
            )
            .collect(Collectors.toList());
    }

    private File printTopRecords(List<MetricRecordCalculated> calculatedRecords, int numberOfTopPrintingRecord) {
        List<List<String>> rows = new ArrayList<>();
        rows.add(Arrays.asList(
            "org",
            "repo_name",
            "health_score",
            "num_commits",
            "contributors",
            "average_hour_issue_remain_open",
            "commit_ratio",
            "commit_per_day"));

        Collections.sort(calculatedRecords);
        Collections.reverse(calculatedRecords);

        rows.addAll(
            calculatedRecords.stream()
                .limit(numberOfTopPrintingRecord)
                .map(MetricRecordCalculated::toRow)
                .collect(Collectors.toList())
        );

        System.out.println("Writing to CSV File");
        File output = new File("health_scores.csv");

        try {
            Utils.createCSVFile(output, rows);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Finish writing to CSV File: " + output.getAbsolutePath());

        return output;
    }
}
