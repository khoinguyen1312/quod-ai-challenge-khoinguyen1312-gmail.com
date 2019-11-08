package ai.quod.challenge.metric;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.OptionalDouble;

class MetricExtractorRecord implements Comparable<MetricExtractorRecord> {

    private static DecimalFormat decimalFormat = new DecimalFormat("#.##");

    String org;
    String repoName;
    Double healthScore;
    Integer numCommits;
    Integer numContributors;
    OptionalDouble averageHourIssueRemainOpen;
    Double commitRatio;

    public MetricExtractorRecord(String org, String repoName, Double healthScore, Integer numCommits,
        Integer numContributors, OptionalDouble averageHourIssueRemainOpen, Double commitRatio) {
        this.org = org;
        this.repoName = repoName;
        this.healthScore = healthScore;
        this.numCommits = numCommits;
        this.numContributors = numContributors;
        this.averageHourIssueRemainOpen = averageHourIssueRemainOpen;
        this.commitRatio = commitRatio;
    }

    public Double getHealthScore() {
        return healthScore;
    }

    public Integer getNumCommits() {
        return numCommits;
    }

    public Integer getNumContributors() {
        return numContributors;
    }

    public Double getCommitRatio() {
        return commitRatio;
    }

    public List<String> toRow() {

        String stringOfAverageHourIssueRemainOpen = MetricService.NONE;

        if (averageHourIssueRemainOpen.isPresent()) {
            stringOfAverageHourIssueRemainOpen = decimalFormat.format(averageHourIssueRemainOpen.getAsDouble());
        }

        return Arrays.asList(
            org,
            repoName,
            decimalFormat.format(healthScore),
            numCommits.toString(),
            numContributors.toString(),
            stringOfAverageHourIssueRemainOpen,
            decimalFormat.format(commitRatio));
    }

    @Override
    public int compareTo(MetricExtractorRecord metricExtractorRecord) {
        return Comparator.comparing(MetricExtractorRecord::getHealthScore)
            .thenComparing(MetricExtractorRecord::getCommitRatio)
            .thenComparing(MetricExtractorRecord::getNumCommits)
            .thenComparingInt(MetricExtractorRecord::getNumContributors)
            .compare(this, metricExtractorRecord);
    }
}
