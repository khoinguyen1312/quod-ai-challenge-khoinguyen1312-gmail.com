package ai.quod.challenge.metric;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

class MetricExtractorRecordCalculated implements Comparable<MetricExtractorRecordCalculated> {

    private static DecimalFormat decimalFormat = new DecimalFormat("#.##");

    String org;
    String repoName;
    Double heatlhScore;
    Integer numCommits;
    Integer numContributors;
    Optional<Double> averageHoursSpendOnIssue;
    Double commitRatio;
    Double commitsPerDay;

    public MetricExtractorRecordCalculated(String org, String repoName, Double heatlhScore, Integer numCommits,
        Integer numContributors, Optional<Double> averageHoursSpendOnIssue, Double commitRatio,
        Double commitsPerDay) {
        this.org = org;
        this.repoName = repoName;
        this.heatlhScore = heatlhScore;
        this.numCommits = numCommits;
        this.numContributors = numContributors;
        this.averageHoursSpendOnIssue = averageHoursSpendOnIssue;
        this.commitRatio = commitRatio;
        this.commitsPerDay = commitsPerDay;
    }

    private String getOrg() {
        return org;
    }

    private String getRepoName() {
        return repoName;
    }

    private Double getHeatlhScore() {
        return heatlhScore;
    }

    private Integer getNumCommits() {
        return numCommits;
    }

    private Integer getNumContributors() {
        return numContributors;
    }

    private Optional<Double> getAverageHoursSpendOnIssue() {
        return averageHoursSpendOnIssue;
    }

    private Double getCommitRatio() {
        return commitRatio;
    }

    private Double getCommitsPerDay() {
        return commitsPerDay;
    }

    public List<String> toRow() {
        return Arrays.asList(
            org,
            repoName,
            decimalFormat.format(this.heatlhScore),
            numCommits.toString(),
            numContributors.toString(),
            this.averageHoursSpendOnIssue.map(decimalFormat::format).orElse(MetricService.NONE),
            decimalFormat.format(this.commitRatio),
            decimalFormat.format(this.commitsPerDay)
        );
    }

    @Override
    public int compareTo(MetricExtractorRecordCalculated metricExtractorRecordCalculated) {
        return Comparator.comparing(MetricExtractorRecordCalculated::getHeatlhScore)
            .thenComparing(MetricExtractorRecordCalculated::getCommitsPerDay)
            .thenComparing(MetricExtractorRecordCalculated::getCommitRatio)
            .thenComparing(MetricExtractorRecordCalculated::getNumCommits)
            .thenComparingInt(MetricExtractorRecordCalculated::getNumContributors)
            .compare(this, metricExtractorRecordCalculated);
    }
}
