package ai.quod.challenge.metric;

import java.text.DecimalFormat;
import java.util.Optional;

class MetricRecordRaw {

    private static DecimalFormat decimalFormat = new DecimalFormat("#.##");

    String org;
    String repoName;
    Integer numCommits;
    Integer numContributors;
    Long sumHourIssuseRemainOpen;
    Integer numberOfIssue;

    MetricRecordRaw(String org, String repoName, Integer numCommits, Integer numContributors,
        Long sumHourIssuseRemainOpen, Integer numberOfIssue) {
        this.org = org;
        this.repoName = repoName;
        this.numCommits = numCommits;
        this.numContributors = numContributors;
        this.sumHourIssuseRemainOpen = sumHourIssuseRemainOpen;
        this.numberOfIssue = numberOfIssue;
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

    private Double calculateCommitRatio() {
        return this.numCommits * 1.0 / this.numContributors;
    }

    private Double calculateCommitsPerDay(int hourRange) {
        return this.numCommits * 1.0 / (hourRange / 24);
    }

    private Optional<Double> calculateAverageHourIssueRemainOpen() {
        if (this.numberOfIssue == 0 || this.sumHourIssuseRemainOpen == 0) {
            return Optional.empty();
        }

        return Optional.of(this.sumHourIssuseRemainOpen * 1.0 / this.numberOfIssue);
    }


    public MetricRecordCalculated calculate(
        int maxNumberOfCommits,
        int maxNumberOfContributors,
        double minOfHourIssueRemainOpen,
        int hourRange
    ) {
        double healthScore = calculateHealthScore(maxNumberOfCommits, maxNumberOfContributors,
            minOfHourIssueRemainOpen);

        return new MetricRecordCalculated(
            this.org,
            this.repoName,
            healthScore,
            this.numCommits,
            this.numContributors,
            calculateAverageHourIssueRemainOpen(),
            calculateCommitRatio(),
            calculateCommitsPerDay(hourRange)
        );
    }

    private double calculateHealthScore(int maxNumberOfCommits,
        int maxNumberOfContributors,
        double minOfHourIssueRemainOpen) {

        double healthScore = this.numCommits * 1.0 / maxNumberOfCommits
            + this.numContributors * 1.0 / maxNumberOfContributors;

        Optional<Double> averageHourIssueRemainOpen = calculateAverageHourIssueRemainOpen();

        if (averageHourIssueRemainOpen.isPresent()) {
            healthScore = healthScore +
                (minOfHourIssueRemainOpen / averageHourIssueRemainOpen.get());

            healthScore = healthScore / 3;
        } else {
            healthScore = healthScore / 2;
        }
        return healthScore;
    }
}
