package ai.quod.challenge.metric;

import ai.quod.challenge.Utils;
import ai.quod.challenge.metric.model.GithubMetric;
import ai.quod.challenge.metric.model.OrgMetric;
import ai.quod.challenge.metric.model.RepoMetric;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

class MetricExtractor {

    private static DecimalFormat decimalFormat = new DecimalFormat("#.##");

    private GithubMetric githubMetric;

    MetricExtractor(GithubMetric githubMetric) {
        this.githubMetric = githubMetric;
    }

    void parseToRows() {
        List<List<String>> rows = new ArrayList<>();
        rows.add(Arrays.asList("org","repo_name","health_score","num_commits","contributors"));

        int maxNumberOfCommits = 0;
        int maxNumberOfContributors = 0;

        for (Entry<String, OrgMetric> orgMetricEntry : githubMetric.getMetrics().entrySet()) {
            for (Entry<String, RepoMetric> repoMetricEntry : orgMetricEntry.getValue().getMetrics().entrySet()) {
                int numberOfCommits = repoMetricEntry.getValue().getShaCommits().size();
                int numberOfContributors = repoMetricEntry.getValue().getContributors().size();

                if (numberOfCommits > maxNumberOfCommits) {
                    maxNumberOfCommits = numberOfCommits;
                }

                if (numberOfContributors > maxNumberOfContributors) {
                    maxNumberOfContributors = numberOfContributors;
                }
            }
        }


        for (Entry<String, OrgMetric> orgMetricEntry : githubMetric.getMetrics().entrySet()) {
            String org = orgMetricEntry.getKey();
            for (Entry<String, RepoMetric> repoMetricEntry : orgMetricEntry.getValue().getMetrics().entrySet()) {
                String repoName = repoMetricEntry.getKey();

                int numberOfCommits = repoMetricEntry.getValue().getShaCommits().size();
                int numberOfContributors = repoMetricEntry.getValue().getContributors().size();

                double healthScore = (numberOfCommits / maxNumberOfCommits) + (numberOfContributors / maxNumberOfContributors);

                List<String> row = Arrays.asList(
                    org,
                    repoName,
                    decimalFormat.format(healthScore),
                    Integer.valueOf(numberOfCommits).toString(),
                    Integer.valueOf(numberOfContributors).toString());
                rows.add(row);
            }
        }

        try {
            Utils.createCSVFile(new File("health_scores.csv"), rows);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
