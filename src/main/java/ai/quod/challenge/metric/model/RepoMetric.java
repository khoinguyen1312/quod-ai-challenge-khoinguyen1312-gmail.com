package ai.quod.challenge.metric.model;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class RepoMetric {
    Set<String> shaCommits = Collections.synchronizedSet(new HashSet<>());

    Set<String> contributors = Collections.synchronizedSet(new HashSet<>());

    Map<String, IssueMetric> issueMetric = Collections.synchronizedMap(new HashMap<>());

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

    public Map<String, IssueMetric> getIssueMetric() {
        return issueMetric;
    }
}
