package ai.quod.challenge.event.dto;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;

public class EventTest {
    @Test
    public void should_aboutToLoadFromJson() {
        // Event template can be found here: https://developer.github.com/v3/activity/events/
        String testJson = "{\n"
            + "  \"type\": \"Event\",\n"
            + "  \"public\": true,\n"
            + "  \"payload\": {\n"
            + "  },\n"
            + "  \"repo\": {\n"
            + "    \"id\": 3,\n"
            + "    \"name\": \"octocat/Hello-World\",\n"
            + "    \"url\": \"https://api.github.com/repos/octocat/Hello-World\"\n"
            + "  },\n"
            + "  \"actor\": {\n"
            + "    \"id\": 1,\n"
            + "    \"login\": \"octocat\",\n"
            + "    \"gravatar_id\": \"\",\n"
            + "    \"avatar_url\": \"https://github.com/images/error/octocat_happy.gif\",\n"
            + "    \"url\": \"https://api.github.com/users/octocat\"\n"
            + "  },\n"
            + "  \"org\": {\n"
            + "    \"id\": 1,\n"
            + "    \"login\": \"github\",\n"
            + "    \"gravatar_id\": \"\",\n"
            + "    \"url\": \"https://api.github.com/orgs/github\",\n"
            + "    \"avatar_url\": \"https://github.com/images/error/octocat_happy.gif\"\n"
            + "  },\n"
            + "  \"created_at\": \"2011-09-06T17:26:27Z\",\n"
            + "  \"id\": \"12345\"\n"
            + "}";

        Gson gson = new Gson();
        Event event = gson.fromJson(testJson, Event.class);
        Assert.assertEquals(event.getType(), "Event");
        Assert.assertEquals(event.getPublic(), true);
        Assert.assertNotNull(event.getPayload());
        Assert.assertEquals(event.getRepo().getId(), Long.valueOf(3l));
        Assert.assertEquals(event.getRepo().getName(), "octocat/Hello-World");
        Assert.assertEquals(event.getRepo().getUrl(), "https://api.github.com/repos/octocat/Hello-World");
        Assert.assertEquals(event.getActor().getId(), Long.valueOf(1l));
        Assert.assertEquals(event.getActor().getLogin(), "octocat");
        Assert.assertEquals(event.getActor().getGravatarId(), "");
        Assert.assertEquals(event.getActor().getAvatarUrl(), "https://github.com/images/error/octocat_happy.gif");
        Assert.assertEquals(event.getActor().getUrl(), "https://api.github.com/users/octocat");
        Assert.assertEquals(event.getOrg().getId(), Long.valueOf(1l));
        Assert.assertEquals(event.getOrg().getLogin(), "github");
        Assert.assertEquals(event.getOrg().getGravatarId(), "");
        Assert.assertEquals(event.getOrg().getUrl(), "https://api.github.com/orgs/github");
        Assert.assertEquals(event.getOrg().getAvatarUrl(), "https://github.com/images/error/octocat_happy.gif");
        Assert.assertEquals(event.getCreatedAt(), "2011-09-06T17:26:27Z");
        Assert.assertEquals(event.getId(), "12345");
    }

}
