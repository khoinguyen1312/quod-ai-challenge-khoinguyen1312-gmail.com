package ai.quod.challenge.metric.model;

import java.util.HashMap;

public class OrgMetric {
    HashMap<String, RepoMetric> metrics;

    public OrgMetric() {
        this.metrics = new HashMap<>();
    }

    public RepoMetric add(String repoName) {
        if (!metrics.containsKey(repoName)) {
            metrics.put(repoName, new RepoMetric());
        }

        return metrics.get(repoName);
    }
    
    public HashMap<String, RepoMetric> getMetrics() {
        return metrics;
    }
}
