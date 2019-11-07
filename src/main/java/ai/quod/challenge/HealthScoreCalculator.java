package ai.quod.challenge;

import ai.quod.challenge.metric.MetricService;
import ai.quod.challenge.metric.dto.Event;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class HealthScoreCalculator {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner argumentScanner = new Scanner(System.in);

//        String datetimeStart = argumentScanner.next();
//        String datetimeEnd = argumentScanner.next();
        MetricService metricService = new MetricService();
        metricService.buildMetric();

    }
}
