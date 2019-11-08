package ai.quod.challenge.metric.model;

import java.text.DecimalFormat;
import java.util.HashMap;

public class GithubMetric {

    private static DecimalFormat decimalFormat = new DecimalFormat("#.##");

    HashMap<String, OrgMetric> metrics;
    Integer hourRange;

    public GithubMetric(Integer hourRange) {
        this.metrics = new HashMap<>();
        this.hourRange = hourRange;
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

    public Integer getHourRange() {
        return hourRange;
    }
}
