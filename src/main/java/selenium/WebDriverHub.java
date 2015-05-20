package selenium;

import date.Dates;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

/**
 * User: YamStranger
 * Date: 5/16/15
 * Time: 1:04 AM
 */
public class WebDriverHub {
    private static final Logger logger = LoggerFactory.getLogger(WebDriverHub.class);
    private static int count = 0;
    private Semaphore availability = new Semaphore(Integer.MAX_VALUE);
    private Queue<WebDriver> drivers = new LinkedList<>();
    private final boolean reuse;
    private final boolean remote;

    public WebDriverHub(boolean reuse, boolean remote) {
        this.reuse = reuse;
        this.remote = remote;

    }

    public void available(int drivers) {
        synchronized (this) {
            this.availability = new Semaphore(drivers);
        }
    }

    public WebDriver driver() {
        try {
            this.availability.acquire(1);
            logger.info("new browser request, left " + this.availability.availablePermits());
        } catch (InterruptedException e) {
        }
        WebDriver driver = null;
        if (reuse) {
            driver = this.drivers.poll();
            if (driver == null) {
                driver = init();
            }
        } else {
            driver = init();
        }
        return new ManageableDriver(driver, this, reuse, new Dates());
    }

    private WebDriver init() {

        WebDriver driver = null;
        if (remote) {
            boolean isFirefox = true;
            do {
                try {
                    if (isFirefox) {
                        DesiredCapabilities firefox = DesiredCapabilities.firefox();
                        firefox.setBrowserName("firefox");
                        firefox.setPlatform(Platform.ANY);
                        firefox.setVersion("ANY");
                        driver = new RemoteWebDriver(new URL("http://127.0.0.1:4444/wd/hub"), firefox);
                    } else {
                        DesiredCapabilities chrome = DesiredCapabilities.chrome();
                        chrome.setBrowserName("chrome");
                        chrome.setPlatform(Platform.ANY);
                        chrome.setVersion("ANY");
                        driver = new RemoteWebDriver(new URL("http://127.0.0.1:4444/wd/hub"), chrome);
                    }
                } catch (WebDriverException ee) {
                    if (ee.getMessage().contains("session cannot find")) {
                        isFirefox = !isFirefox;
                        logger.error("some exception during requesting browser, changing browser userFirefox=" + isFirefox, ee);
                    }
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                } catch (Exception e) {
                    logger.error("some exception during requesting browser", e);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ee) {
                        Thread.currentThread().interrupt();
                    }
                }
            } while (driver == null && !Thread.currentThread().isInterrupted());
        } else {
            driver = new ChromeDriver();
        }
        return driver;
    }


    public WebDriverWait driverWait(WebDriver driver) {
        return new WebDriverWait(driver, 30);
    }

    protected void release(ManageableDriver driver) {
        synchronized (this) {
            if (reuse) {
                boolean alive = true;
                if (driver.age() < 30) {
                    try {
                        driver.getCurrentUrl();
                    } catch (Exception e) {
                        logger.error("trying to reuse browser, which is not alive ", e);
                        alive = false;
                    }
                } else {
                    logger.info("Browser too old");
                    alive = false;
                }
                if (alive) {
                    this.drivers.add(driver);
                } else {
                    try {
                        driver.driver().quit();
                    } catch (Exception e) {
                        logger.error("trying to quit browser, cached error " + e);
                    }
                }
            } else {
                try {
                    driver.driver().quit();
                } catch (Exception e) {
                    logger.error("trying to quit browser, cached error " + e);
                }
            }
            this.availability.release(1);
        }
    }


    public void quit() {
        for (final WebDriver driver : drivers) {
            try {
                driver.quit();
            } catch (Exception e) {
                logger.error("trying to quit browser, cached error " + e);
            }
        }
    }
}
