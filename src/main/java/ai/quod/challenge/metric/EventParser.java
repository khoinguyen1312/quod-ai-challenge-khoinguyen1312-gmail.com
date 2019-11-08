package ai.quod.challenge.metric;

import ai.quod.challenge.Utils;
import ai.quod.challenge.event.dto.Commit;
import ai.quod.challenge.event.dto.Event;
import ai.quod.challenge.event.dto.Issue;
import ai.quod.challenge.event.dto.Org;
import ai.quod.challenge.event.dto.Payload;
import ai.quod.challenge.event.dto.Repo;
import ai.quod.challenge.metric.model.GithubMetric;
import ai.quod.challenge.metric.model.OrgMetric;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Optional;

class EventParser {

    private GithubMetric githubMetric;

    EventParser(GithubMetric githubMetric) {
        this.githubMetric = githubMetric;
    }

    void analyzeEventToMetric(Event event) {
        Optional<Event> eventOptional = Optional.ofNullable(event);

        String orgName = eventOptional
            .map(Event::getOrg)
            .map(Org::getLogin)
            .orElse(MetricService.NONE);

        Optional<String> repoOptional = eventOptional
            .map(Event::getRepo)
            .map(Repo::getName);

        if (!repoOptional.isPresent()) {
            return;
        }

        String repoName = repoOptional.get();

        if (eventOptional.map(Event::getType).map("PushEvent"::equals).orElse(false)) {
            for (Commit commit : event.getPayload().getCommits()) {
                githubMetric
                    .add(orgName)
                    .add(repoName)
                    .increaseNumberOfCommits(commit.getSha());

                githubMetric
                    .add(orgName)
                    .add(repoName)
                    .increaseNumberOfContributors(commit.getAuthor().getEmail());
            }
        }

        if (eventOptional.map(Event::getType).map("IssuesEvent"::equals).orElse(false)
            && eventOptional.map(Event::getPayload).map(Payload::getAction).map("closed"::equals).orElse(false)) {

            Optional<Issue> issue = eventOptional
                .map(Event::getPayload)
                .map(Payload::getIssue);

            Optional<String> createdAtOptional = issue.map(Issue::getCreatedAt);
            Optional<String> closedAtOptional = issue.map(Issue::getClosedAt);
            Optional<String> issueUrlOptional = issue.map(Issue::getUrl);

            if (issueUrlOptional.isPresent() && createdAtOptional.isPresent() && closedAtOptional.isPresent()) {
                Calendar openedAt;
                Calendar closedAt;
                try {
                    openedAt = Utils.parseIso8601DateFormat(createdAtOptional.get());
                    closedAt = Utils.parseIso8601DateFormat(closedAtOptional.get());
                } catch (ParseException e) {
                    return;
                }

                githubMetric
                    .add(orgName)
                    .add(repoName)
                    .addIssue(issueUrlOptional.get(), openedAt, closedAt);
            }
        }
    }
}
