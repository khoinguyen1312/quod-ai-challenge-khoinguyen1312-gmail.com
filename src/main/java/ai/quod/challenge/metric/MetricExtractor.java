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

    void parseToRows() {
        int maxNumberOfCommits = 0;
        int maxNumberOfContributors = 0;

        for (Entry<String, OrgMetric> orgMetricEntry : githubMetric.getMetrics().entrySet()) {
            for (Entry<String, RepoMetric> repoMetricEntry : orgMetricEntry.getValue().getMetrics().entrySet()) {
                int numberOfCommits = repoMetricEntry.getValue().getNumberOfShaCommits();
                int numberOfContributors = repoMetricEntry.getValue().getNumberOfContributors();

                if (numberOfCommits > maxNumberOfCommits) {
                    maxNumberOfCommits = numberOfCommits;
                }

                if (numberOfContributors > maxNumberOfContributors) {
                    maxNumberOfContributors = numberOfContributors;
                }
            }
        }

        List<MetricExtractorRecord> records = new ArrayList<>();

        for (Entry<String, OrgMetric> orgMetricEntry : githubMetric.getMetrics().entrySet()) {
            String org = orgMetricEntry.getKey();
            for (Entry<String, RepoMetric> repoMetricEntry : orgMetricEntry.getValue().getMetrics().entrySet()) {
                String repoName = repoMetricEntry.getKey();

                int numberOfCommits = repoMetricEntry.getValue().getNumberOfShaCommits();
                int numberOfContributors = repoMetricEntry.getValue().getNumberOfContributors();

                OptionalDouble averageHourIssueRemainOpen = repoMetricEntry.getValue()
                    .getIssueMetric()
                    .values()
                    .stream()
                    .mapToLong(IssueMetric::hoursIssueRemainOpen)
                    .average();

                double healthScore =
                    (numberOfCommits / maxNumberOfCommits) + (numberOfContributors / maxNumberOfContributors);

                records.add(new MetricExtractorRecord(
                    org,
                    repoName,
                    healthScore,
                    numberOfCommits,
                    numberOfContributors,
                    averageHourIssueRemainOpen,
                    repoMetricEntry.getValue().getCommitPerDeveloperRatio()));
            }
        }

        List<List<String>> rows = new ArrayList<>();
        rows.add(Arrays.asList(
            "org",
            "repo_name",
            "health_score",
            "num_commits",
            "contributors",
            "average_hour_issue_remain_open",
            "commit_ratio"));

        Collections.sort(records);

        rows.addAll(
            records.stream()
                .limit(1000)
                .map(MetricExtractorRecord::toRow)
                .collect(Collectors.toList())
        );

        try {
            Utils.createCSVFile(new File("health_scores.csv"), rows);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
