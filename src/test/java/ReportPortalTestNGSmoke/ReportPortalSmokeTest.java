package ReportPortalTestNGSmoke;

import org.testng.Assert;
import org.testng.annotations.Test;

public class reportportalhelloworld {



    @Test
    public void helloWorldTest() {
        String hello = "Hello, world!";
        Assert.assertEquals(hello, "Hello, world!");
    }
}
