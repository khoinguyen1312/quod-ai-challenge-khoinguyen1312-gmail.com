package ai.quod.challenge.metric;

import java.util.Arrays;
import java.util.List;

class MetricExtractorRecord {
    String org;
    String repoName;
    String healthScore;
    String numCommits;
    String numContributors;
    String averageHourIssueRemainOpen;
    String commitRatio;

    public MetricExtractorRecord(String org, String repoName, String healthScore, String numCommits,
        String numContributors, String averageHourIssueRemainOpen, String commitRatio) {
        this.org = org;
        this.repoName = repoName;
        this.healthScore = healthScore;
        this.numCommits = numCommits;
        this.numContributors = numContributors;
        this.averageHourIssueRemainOpen = averageHourIssueRemainOpen;
        this.commitRatio = commitRatio;
    }

    public List<String> toRow() {
        return Arrays.asList(org,
            repoName,
            healthScore,
            numCommits,
            numContributors,
            averageHourIssueRemainOpen,
            commitRatio);
    }
}
