package ReportPortalTestNGSmoke;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ReportPortalSmokeTest {



    @Test
    public void helloWorldTest() {
        String hello = "Hello, world!";
        System.out.println(hello);
        Assert.assertEquals(hello, "Hello, world!");
    }

    @Test
    public void failingOnPurpose() {
        String hello = "Hello, world!";
        System.out.println(hello);
        Assert.assertEquals(hello, "Nope its another galaxy!");
    }
}
