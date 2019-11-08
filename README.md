# README

## How to build

 1. Your machine should have `maven` and `java 8`. Check it here:
    1. <https://maven.apache.org/install.html>
    2. <https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html>
 2. Change directory to here
 3. run `mvn install`
 4. Check for output jar at `target/`, which should be `HealthScoreCalculator-1.0-jar-with-dependencies.jar`

## How to run

 1. Your machine should have `java 8`
 2. run `java -cp HealthScoreCalculator-1.0-jar-with-dependencies.jar ai.quod.challenge.HealthScoreCalculator <start_date> <end_date>`
    > - `<start_date>` & `<end_date>` should be in `ISO 8601 format`
    > - must fix pattern: `yyyy-MM-dd'T'HH:mm:ssX`.
    > - Ex: `2019-08-01T00:00:00Z`
 3. Output will be on your directory: `health_scores.csv`

## Technical decisions

 1. Should read file by hour to hour for small running, avoid consume too much resources.
 2. We can run parrallel using parrallel stream for quicker download.
 3. Use Gson for parsing event into DTO.
 4. GitHub's Event DTO is show in document as json. We should use <http://www.jsonschema2pojo.org/> for generating DTOs from provided json.
 5. Because lack of time, temporary skiping Unit Test for now.

## To improve later if we continue project

 1. Should use library for Dependency Inject. Ex: Dagger 2.
    > Better handling code, avoid boilerplate code
 2. Should add more UTs which would help in refactoring
 3. Should run separated download archive file and parsing archive file for better proformance.
    > For example, we could have various separated thread for download archive file. After each download, send a message to a queue. Another thread will consume this thread for parse archive file into GitHubMetric
 4. For each event type, should have separated DTO, separated handler for gathering Metric.
 5. Should ask GitHub Archive Team for their implemented DTO structure, which is not found in document.
 6. Should use `proto` for implementing DTO, avoid boilerplate code.
 7. Should handle more `human mistake` like throw exception when `<start_date>` is smaller than `<end_date>`
 8. Should refactor how we write CSV file, currently, there is no constrant between number of headers and number of collumn.
