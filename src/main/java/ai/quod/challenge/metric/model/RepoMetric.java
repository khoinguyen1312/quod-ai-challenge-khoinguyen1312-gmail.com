package ai.quod.challenge.metric.model;

import java.util.HashSet;
import java.util.Set;

public class RepoMetric {
    Set<String> shaCommits = new HashSet<>();
    Set<String> contributors = new HashSet<>();

    public void increaseNumberOfCommits(String sha) {
            this.shaCommits.add(sha);
    }

    public void increaseNumberOfContributors(String contributor) {
        this.contributors.add(contributor);
    }


}
