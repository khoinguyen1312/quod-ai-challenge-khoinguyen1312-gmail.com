package ai.quod.challenge.metric.model;

import java.time.temporal.ChronoUnit;
import java.util.Calendar;

public class IssueMetric {
    Calendar createdAt;
    Calendar closedAt;

    public IssueMetric(Calendar createdAt, Calendar closedAt) {
        this.createdAt = createdAt;
        this.closedAt = closedAt;
    }

    public Long hoursIssueRemainOpen() {
        return ChronoUnit.HOURS.between(createdAt.toInstant(), closedAt.toInstant());
    }
}
