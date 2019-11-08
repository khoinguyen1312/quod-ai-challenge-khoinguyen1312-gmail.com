package ai.quod.challenge.metric;

import ai.quod.challenge.GitHubArchiveClient;
import ai.quod.challenge.Utils;
import ai.quod.challenge.event.dto.Event;
import ai.quod.challenge.metric.model.GithubMetric;
import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.List;

public class MetricService {
    static final String NONE = "None";

    private GitHubArchiveClient gitHubArchiveClient;
    private EventParser eventParser;
    private MetricExtractor metricExtractor;

    public MetricService(GitHubArchiveClient gitHubArchiveClient) {
        this.gitHubArchiveClient = gitHubArchiveClient;
        this.eventParser = new EventParser();
        this.metricExtractor = new MetricExtractor();
    }


    public void buildMetric(Calendar start, Calendar end) {
        List<Calendar> hoursRange = Utils.buildGitHubTimeRange(start, end);
        System.out.println("Detech " + hoursRange.size() + " hour archive need to proceed");

        GithubMetric githubMetric = new GithubMetric(hoursRange.size());
        hoursRange.parallelStream().forEach(hour -> {
            this.parseOneHour(githubMetric, hour);
        });

        File output = metricExtractor.writeToOutput(githubMetric);

        System.out.println("Output at: " + output.getAbsolutePath());
    }

    private void parseOneHour(GithubMetric githubMetric, Calendar time) {
        Path lastOneHourArchive = gitHubArchiveClient.getOneHourArchive(time);

        Gson gson = new Gson();

        try {
            Utils.forEachLineInFile(lastOneHourArchive.toFile(), line -> {
                Event event = gson.fromJson(line, Event.class);

                eventParser.analyzeEventToMetric(event, githubMetric);
            });
        } catch (IOException e) {
            throw new RuntimeException("Can not access file");
        }

        System.out.println("Finish parsing archive at " + time.getTime());
    }

}
