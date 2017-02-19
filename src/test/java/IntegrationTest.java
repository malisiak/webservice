import org.junit.Test;
import serviceDatabase.transferObject.*;
import serviceDatabase.service.WebApplication;
import serviceDatabase.service.Wrapper;

import javax.ws.rs.core.Response;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Martyna on 03.12.2016.
 */
public class IntegrationTest {

    @Test
    public void userSerchTest() {
        String userEmail = "test";
        WebApplication webApplication = new WebApplication();
        Boolean result = webApplication.userSearch(userEmail);
        assertTrue(result);
    }

    @Test
    public void JerseyWebApplicationIT() {
        Wrapper wrapper = new Wrapper();

        wrapper.setActivities(Arrays.asList(new RecognizedActivity("Running", 1480785176461L, 112)));
        wrapper.setCalls(Arrays.asList(new CallLog("Incoming", 33, "Monika", 1480785176488L)));
        wrapper.setSms(Arrays.asList(new SmsLog("Inbox", "Monika",1480785176410L )));
        wrapper.setProcess(Arrays.asList(new ProcessLog("Website", 80, 1480785176490L)));
        wrapper.setLocation(Arrays.asList(new LocationLog(54.3678482, 18.6109754, 1480785176410L, "fused", 0.0f)));
        wrapper.setUser(new User("test"));

        WebApplication webApplication = new WebApplication();
        Response result = webApplication.postData(wrapper);
        int properResult = 200;
        assertEquals(properResult, result.getStatus());
    }

}
