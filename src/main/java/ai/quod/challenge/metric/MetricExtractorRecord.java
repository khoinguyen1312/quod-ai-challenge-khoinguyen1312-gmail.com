package ai.quod.challenge.metric;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

class MetricExtractorRecord implements Comparable<MetricExtractorRecord> {

    private static DecimalFormat decimalFormat = new DecimalFormat("#.##");

    String org;
    String repoName;
    Integer numCommits;
    Integer numContributors;
    Long sumHourIssuseRemainOpen;
    Integer numberOfIssue;
    Double daysRange;

    Double healthScore;

    public MetricExtractorRecord(String org, String repoName, Integer numCommits, Integer numContributors,
        Long sumHourIssuseRemainOpen, Integer numberOfIssue, Double daysRange) {
        this.org = org;
        this.repoName = repoName;
        this.numCommits = numCommits;
        this.numContributors = numContributors;
        this.sumHourIssuseRemainOpen = sumHourIssuseRemainOpen;
        this.numberOfIssue = numberOfIssue;
        this.daysRange = daysRange;
    }

    public String getOrg() {
        return org;
    }

    public String getRepoName() {
        return repoName;
    }

    public Integer getNumCommits() {
        return numCommits;
    }

    public Integer getNumContributors() {
        return numContributors;
    }

    public Long getSumHourIssuseRemainOpen() {
        return sumHourIssuseRemainOpen;
    }

    public Integer getNumberOfIssue() {
        return numberOfIssue;
    }

    public Double getDaysRange() {
        return daysRange;
    }

    public Double getCommitRatio() {
        return this.numCommits * 1.0 / this.numContributors;
    }

    public Double getCommitsPerDay() {
        return this.numCommits * 1.0 / this.daysRange;
    }

    public Optional<Double> getAverageHourIssueRemainOpen() {
        if (this.numberOfIssue == 0) {
            Optional.empty();
        }

        return Optional.of(this.sumHourIssuseRemainOpen * 1.0 / this.numberOfIssue);
    }

    public void calculateHealthScore(
        int maxNumberOfCommits,
        int maxNumberOfContributors,
        double minOfHourIssueRemainOpen
    ) {
        double scoreWithoutIssueMetric = this.numCommits * 1.0 / maxNumberOfCommits
            + this.numContributors * 1.0 / maxNumberOfContributors;

        Optional<Double> averageHourIssueRemainOpen = getAverageHourIssueRemainOpen();

        if (averageHourIssueRemainOpen.isPresent()) {
            scoreWithoutIssueMetric = scoreWithoutIssueMetric +
                (minOfHourIssueRemainOpen / averageHourIssueRemainOpen.get());
        }

        this.healthScore = scoreWithoutIssueMetric;
    }

    public Double getHealthScore() {
        return this.healthScore;
    }

    public List<String> toRow() {
        return Arrays.asList(
            org,
            repoName,
            decimalFormat.format(healthScore),
            numCommits.toString(),
            numContributors.toString(),
            getAverageHourIssueRemainOpen().map(decimalFormat::format).orElse(MetricService.NONE),
            decimalFormat.format(getCommitRatio()),
            decimalFormat.format(getCommitsPerDay())
        );
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
