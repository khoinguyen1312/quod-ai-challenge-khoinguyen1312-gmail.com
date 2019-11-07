package ai.quod.challenge.metric;

import ai.quod.challenge.GitHubArchiveClient;
import ai.quod.challenge.Utils;
import ai.quod.challenge.event.dto.Event;
import com.google.gson.Gson;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MetricService {
    GitHubArchiveClient gitHubArchiveClient;

    public MetricService(GitHubArchiveClient gitHubArchiveClient) {
        this.gitHubArchiveClient = gitHubArchiveClient;
    }

    public void buildMetric() {
        Path lastOneHourArchive = gitHubArchiveClient.getLastOneHourArchive();

        Gson gson = new Gson();

        List<Event> events = new ArrayList<>();

        try {
            Utils.forEachLineInFile(lastOneHourArchive.toFile(), line -> {
                events.add(gson.fromJson(line, Event.class));
            });
        } catch (IOException e) {
            throw new RuntimeException("Can not access file");
        }

        System.out.println("End");

    }
}
