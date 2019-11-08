package ai.quod.challenge.metric.model;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class GithubMetric {

    private static DecimalFormat decimalFormat = new DecimalFormat("#.##");

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
