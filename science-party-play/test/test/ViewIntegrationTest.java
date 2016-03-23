package test;

import org.junit.*;
import play.libs.F;
import play.test.TestBrowser;

import static play.test.Helpers.*;
import static play.test.Helpers.HTMLUNIT;

/**
 * Created by ewolf on 18.03.2016.
 */
public class ViewIntegrationTest {

    /**
     * add your integration test here
     * in this example we just check if the welcome page is being shown
     */
    @Test
    public void renderLoginPageTest() {
//        running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new F.Callback<TestBrowser>() {
//            public void invoke(TestBrowser browser) {
//                browser.goTo("http://localhost:3333");
//                assertTrue(browser.pageSource().contains("Your new application is ready."));
//            }
//        });
    }
}
