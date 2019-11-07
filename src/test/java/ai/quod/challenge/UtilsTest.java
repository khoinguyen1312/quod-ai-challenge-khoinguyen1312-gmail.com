package ai.quod.challenge;

import java.util.Calendar;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class UtilsTest {

    @Test
    public void Should_beAbleToGetTimeRange() {
        Calendar start = Calendar.getInstance();
        start.set(Calendar.YEAR, 2018);
        start.set(Calendar.MONTH, 2);
        start.set(Calendar.DAY_OF_MONTH, 2);
        start.set(Calendar.HOUR, 5);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);

        Calendar end = Calendar.getInstance();
        end.set(Calendar.YEAR, 2018);
        end.set(Calendar.MONTH, 2);
        end.set(Calendar.DAY_OF_MONTH, 2);
        end.set(Calendar.HOUR, 7);
        end.set(Calendar.MINUTE, 0);
        end.set(Calendar.SECOND, 0);

        List<Calendar> calendars = Utils.buildGitHubTimeRange(start, end);

        Assert.assertEquals(3, calendars.size());


        Assert.assertEquals(5, calendars.get(0).get(Calendar.HOUR));
        Assert.assertEquals(6, calendars.get(1).get(Calendar.HOUR));
        Assert.assertEquals(7, calendars.get(2).get(Calendar.HOUR));

//        Assert.assertEquals(5, calendars.size());
//        Assert.assertEquals(5, calendars.size());
//        Assert.assertEquals(5, calendars.size());
//        Assert.assertEquals(5, calendars.size());
//        Assert.assertEquals(5, calendars.size());
    }
}
