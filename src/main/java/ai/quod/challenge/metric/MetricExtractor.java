package ai.quod.challenge.metric;

import ai.quod.challenge.Utils;
import ai.quod.challenge.metric.model.GithubMetric;
import ai.quod.challenge.metric.model.IssueMetric;
import ai.quod.challenge.metric.model.OrgMetric;
import ai.quod.challenge.metric.model.RepoMetric;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

class MetricExtractor {

    private GithubMetric githubMetric;

    MetricExtractor(GithubMetric githubMetric) {
        this.githubMetric = githubMetric;
    }

    void parseToRows(double daysRange) {
        List<MetricExtractorRecord> records = new ArrayList<>();

        for (Entry<String, OrgMetric> orgMetricEntry : githubMetric.getMetrics().entrySet()) {
            String org = orgMetricEntry.getKey();
            for (Entry<String, RepoMetric> repoMetricEntry : orgMetricEntry.getValue().getMetrics().entrySet()) {
                String repoName = repoMetricEntry.getKey();

                int numberOfCommits = repoMetricEntry.getValue().getNumberOfShaCommits();
                int numberOfContributors = repoMetricEntry.getValue().getNumberOfContributors();

                long sumHourIssueRemainOpen = repoMetricEntry.getValue()
                    .getIssueMetric()
                    .values()
                    .stream()
                    .mapToLong(IssueMetric::hoursIssueRemainOpen)
                    .sum();

                records.add(
                    new MetricExtractorRecord(
                        org,
                        repoName,
                        numberOfCommits,
                        numberOfContributors,
                        sumHourIssueRemainOpen,
                        repoMetricEntry.getValue().getIssueMetric().size()
                    )
                );
            }
        }

        List<MetricExtractorRecordCalculated> recordCalculateds = getRecordCalculateds(records, daysRange);

        printRecords(recordCalculateds);
    }

    private List<MetricExtractorRecordCalculated> getRecordCalculateds(List<MetricExtractorRecord> records,
        double daysRange) {
        int maxNumberOfCommits = records.stream()
            .mapToInt(MetricExtractorRecord::getNumCommits)
            .max()
            .getAsInt();

        int maxNumberOfContributors = records.stream()
            .mapToInt(MetricExtractorRecord::getNumContributors)
            .max()
            .getAsInt();

        double minAverageIssueRemainOpen = records.stream()
            .filter(record -> record.getNumberOfIssue() != 0)
            .mapToDouble(record -> record.getSumHourIssuseRemainOpen() * 1.0 / record.getNumberOfIssue())
            .min().getAsDouble();

        return records.stream()
            .map(record -> record
                .calculate(maxNumberOfCommits, maxNumberOfContributors, minAverageIssueRemainOpen, daysRange)
            )
            .collect(Collectors.toList());
    }

    private void printRecords(List<MetricExtractorRecordCalculated> calculatedRecords) {
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
                .limit(1000)
                .map(MetricExtractorRecordCalculated::toRow)
                .collect(Collectors.toList())
        );

        try {
            Utils.createCSVFile(new File("health_scores.csv"), rows);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
