package selenium;

import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.concurrent.BlockingQueue;

/**
 * User: YamStranger
 * Date: 5/28/15
 * Time: 6:50 PM
 */
public class WebDriverHubConnector extends Thread {
    private final String request;
    private final BlockingQueue<RemoteWebDriver> drivers;
    private static final Logger logger = LoggerFactory.getLogger(WebDriverHubConnector.class);

    public WebDriverHubConnector(final String url, final BlockingQueue<RemoteWebDriver> drivers) {
        this.request = url;
        this.drivers = drivers;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                DesiredCapabilities chrome = DesiredCapabilities.chrome();
                chrome.setBrowserName("chrome");
                chrome.setPlatform(Platform.ANY);
                chrome.setVersion("ANY");
                RemoteWebDriver driver = new RemoteWebDriver(new URL(this.request), chrome);
                drivers.put(driver);
            } catch (InterruptedException e) {
                this.interrupt();
            } catch (Exception e) {
                logger.error("Cached exception during " + this.request + " sleep 1s", e);
                this.sleep();
            }
        }
    }

    private final void sleep() {
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            this.interrupt();
        }
    }

}
