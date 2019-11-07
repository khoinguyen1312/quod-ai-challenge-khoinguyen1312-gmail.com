package ai.quod.challenge.metric.model;

import java.util.HashMap;

public class OrgMetric {
    HashMap<String, RepoMetric> metrics;

    public OrgMetric() {
        this.metrics = new HashMap<>();
    }
}
