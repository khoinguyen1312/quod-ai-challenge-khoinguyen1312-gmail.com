package ai.quod.challenge.metric.model;

import java.util.HashMap;
import java.util.Optional;

public class GithubMetric {

    HashMap<String, OrgMetric> metrics;

    public GithubMetric() {
        this.metrics = new HashMap<>();
    }


    public OrgMetric add(String orgName) {
        if (!metrics.containsKey(orgName)) {
            metrics.put(orgName, new OrgMetric());
        }

        return metrics.get(orgName);
    }

    public HashMap<String, OrgMetric> getMetrics() {
        return metrics;
    }
}
