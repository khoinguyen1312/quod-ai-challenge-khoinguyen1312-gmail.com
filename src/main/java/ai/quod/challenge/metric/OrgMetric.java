package ai.quod.challenge.metric;

import java.util.HashMap;

public class OrgMetric {
    HashMap<String, RepoMetric> metrics;

    public OrgMetric() {
        this.metrics = new HashMap<>();
    }
}
