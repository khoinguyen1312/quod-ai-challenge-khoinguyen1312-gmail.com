package ai.quod.challenge.metric;

import ai.quod.challenge.GitHubArchiveClient;
import ai.quod.challenge.Utils;
import ai.quod.challenge.event.dto.Event;
import ai.quod.challenge.metric.model.GithubMetric;
import com.google.gson.Gson;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.List;

public class MetricService {
    static final String NONE = "None";

    GitHubArchiveClient gitHubArchiveClient;
    GithubMetric githubMetric;
    EventParser eventParser;
    MetricExtractor metricExtractor;

    public MetricService(GitHubArchiveClient gitHubArchiveClient) {
        this.gitHubArchiveClient = gitHubArchiveClient;

        this.githubMetric = new GithubMetric();
        this.eventParser = new EventParser(this.githubMetric);
        this.metricExtractor = new MetricExtractor(this.githubMetric);
    }

    public void buildMetric(Calendar start, Calendar end) {
        List<Calendar> range = Utils.buildGitHubTimeRange(start, end);

        range.parallelStream().forEach(this::parseOneHour);

        metricExtractor.parseToRows();
    }

    private void parseOneHour(Calendar start) {
        Path lastOneHourArchive = gitHubArchiveClient.getOneHourArchive(start);

        Gson gson = new Gson();

        try {
            Utils.forEachLineInFile(lastOneHourArchive.toFile(), line -> {
                Event event = gson.fromJson(line, Event.class);

                eventParser.analyzeEventToMetric(event);
            });
        } catch (IOException e) {
            throw new RuntimeException("Can not access file");
        }
    }

}
