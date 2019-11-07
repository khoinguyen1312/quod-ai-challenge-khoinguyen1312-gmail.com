package ai.quod.challenge.metric;

import ai.quod.challenge.GitHubArchiveClient;
import ai.quod.challenge.Utils;
import ai.quod.challenge.event.dto.Commit;
import ai.quod.challenge.event.dto.Event;
import ai.quod.challenge.event.dto.Org;
import ai.quod.challenge.metric.model.GithubMetric;
import ai.quod.challenge.metric.model.OrgMetric;
import ai.quod.challenge.metric.model.RepoMetric;
import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

public class MetricService {

    private static DecimalFormat decimalFormat = new DecimalFormat("#.##");

    GitHubArchiveClient gitHubArchiveClient;
    GithubMetric githubMetric;

    public MetricService(GitHubArchiveClient gitHubArchiveClient) {
        this.gitHubArchiveClient = gitHubArchiveClient;
        this.githubMetric = new GithubMetric();
    }

    public void buildMetric() {
        Path lastOneHourArchive = gitHubArchiveClient.getLastOneHourArchive();

        Gson gson = new Gson();

        try {
            Utils.forEachLineInFile(lastOneHourArchive.toFile(), line -> {
                Event event = gson.fromJson(line, Event.class);

                analyzeEventToMetric(event);
            });
        } catch (IOException e) {
            throw new RuntimeException("Can not access file");
        }

        parseToRows();
    }

    private void parseToRows() {
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

                double healthScore = (numberOfCommits / maxNumberOfCommits) +
                    (numberOfContributors / maxNumberOfContributors);

                rows.add(Arrays.asList(
                    org,
                    repoName,
                    decimalFormat.format(healthScore),
                    Integer.valueOf(numberOfCommits).toString(),
                    Integer.valueOf(numberOfContributors).toString()));
            }
        }

        try {
            Utils.createCSVFile(new File("health_scores.csv"), rows);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void analyzeEventToMetric(Event event) {
        try {
            if (event.getType().equals("PushEvent")) {
                for (Commit commit : event.getPayload().getCommits()) {
                    String orgName = Optional.ofNullable(event.getOrg()).map(Org::getLogin)
                        .orElse(OrgMetric.NONE_ORG_NAME);

                    githubMetric
                        .add(orgName)
                        .add(event.getRepo().getName())
                        .increaseNumberOfCommits(commit.getSha());

                    githubMetric
                        .add(orgName)
                        .add(event.getRepo().getName())
                        .increaseNumberOfContributors(commit.getAuthor().getEmail());
                }
            }
        } catch (Exception e) {
            System.out.println();
        }
    }
}
