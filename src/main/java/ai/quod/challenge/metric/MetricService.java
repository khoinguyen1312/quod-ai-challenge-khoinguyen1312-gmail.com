package ai.quod.challenge.metric;

import ai.quod.challenge.GZipReader;
import ai.quod.challenge.GitHubArchiveClient;
import ai.quod.challenge.Utils;
import ai.quod.challenge.metric.dto.Event;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
