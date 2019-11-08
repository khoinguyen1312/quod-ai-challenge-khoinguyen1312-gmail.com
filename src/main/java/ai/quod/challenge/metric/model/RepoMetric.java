package ai.quod.challenge.metric.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RepoMetric {
    Map<String, List<String>> contributorToShaCommits = Collections.synchronizedMap(new HashMap<>());

    Map<String, IssueMetric> issueMetric = Collections.synchronizedMap(new HashMap<>());

    public IssueMetric addIssue(String issueUri, Calendar createdAtCalendar, Calendar closedAtCalendar) {
        if (!issueMetric.containsKey(issueUri)) {
            issueMetric.put(issueUri, new IssueMetric(createdAtCalendar, closedAtCalendar));
        }
        return issueMetric.get(issueUri);
    }

    public void increaseNumberOfCommits(String contributor, String sha) {
        if (!contributorToShaCommits.containsKey(contributor)) {
            contributorToShaCommits.put(contributor, new ArrayList<>());
        }

        this.contributorToShaCommits.get(contributor).add(sha);
    }

    public Integer getNumberOfShaCommits() {
        return contributorToShaCommits.values().stream().mapToInt(List::size).sum();
    }

    public Integer getNumberOfContributors() {
        return contributorToShaCommits.size();
    }

    public Double getCommitPerDeveloperRatio() {
        return this.getNumberOfShaCommits() * 1.0 / this.getNumberOfContributors();
    }

    public Map<String, IssueMetric> getIssueMetric() {
        return issueMetric;
    }
}
