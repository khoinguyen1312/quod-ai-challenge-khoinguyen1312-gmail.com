package ai.quod.challenge.metric.model;

import java.util.Calendar;

public class IssueMetric {
    Calendar createdAt;
    Calendar closedAt;

    public IssueMetric(Calendar createdAt, Calendar closedAt) {
        this.createdAt = createdAt;
        this.closedAt = closedAt;
    }
}
