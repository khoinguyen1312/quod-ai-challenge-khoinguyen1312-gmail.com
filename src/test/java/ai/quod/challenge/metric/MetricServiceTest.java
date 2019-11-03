package ai.quod.challenge.metric;

import org.junit.Before;
import org.junit.Test;

public class MetricServiceTest {
    MetricService service;

    @Before
    public void init() {
        service = new MetricService();
    }

    @Test
    public void buildMetric_should_ableToCollectOrgAndRepoName() {
        service.buildMetric();
    }
}
