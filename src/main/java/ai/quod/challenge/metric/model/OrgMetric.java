package ai.quod.challenge.metric.model;

import java.util.HashMap;
import java.util.Optional;

public class OrgMetric {
    public static String NONE_ORG_NAME = "None";

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
