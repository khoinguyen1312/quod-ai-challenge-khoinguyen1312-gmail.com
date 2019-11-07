package ai.quod.challenge.metric;

import ai.quod.challenge.GitHubArchiveClient;
import ai.quod.challenge.Utils;
import ai.quod.challenge.event.dto.Commit;
import ai.quod.challenge.event.dto.Event;
import ai.quod.challenge.event.dto.Org;
import ai.quod.challenge.metric.model.GithubMetric;
import ai.quod.challenge.metric.model.OrgMetric;
import com.google.gson.Gson;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MetricService {

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

        System.out.println(githubMetric.getMetrics());
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
