package ai.quod.challenge.metric.model;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RepoMetric {
    Set<String> shaCommits = new HashSet<>();

    Set<String> contributors = new HashSet<>();

    Map<String, IssueMetric> issueMetric = new HashMap<>();

    public IssueMetric addIssue(String issueUri, Calendar createdAtCalendar, Calendar closedAtCalendar) {
        if (!issueMetric.containsKey(issueUri)) {
            issueMetric.put(issueUri, new IssueMetric(createdAtCalendar, closedAtCalendar));
        }
        return issueMetric.get(issueUri);
    }

    public void increaseNumberOfCommits(String sha) {
            this.shaCommits.add(sha);
    }

    public void increaseNumberOfContributors(String contributor) {
        this.contributors.add(contributor);
    }

    public Set<String> getShaCommits() {
        return shaCommits;
    }

    public Set<String> getContributors() {
        return contributors;
    }
}
