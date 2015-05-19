package selenium;

import date.Dates;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

/**
 * User: YamStranger
 * Date: 5/16/15
 * Time: 10:34 AM
 */
public class ManageableDriver implements WebDriver, JavascriptExecutor {
    private final WebDriver driver;
    private final WebDriverHub webDriverHub;
    private final boolean reusable;
    private final Dates created;

    public ManageableDriver(WebDriver driver, WebDriverHub hub, boolean reusable, Dates created) {
        this.driver = driver;
        this.webDriverHub = hub;
        this.reusable = reusable;
        this.created = created;
    }

    public int age() {
        return (int) this.created.difference(new Dates(), Calendar.SECOND);
    }
    protected WebDriver driver(){
        return driver;
    }

    @Override
    public Object executeScript(String s, Object... objects) {
        JavascriptExecutor jse = (JavascriptExecutor) this.driver;
        return jse.executeScript(s, objects);
    }

    @Override
    public Object executeAsyncScript(String s, Object... objects) {
        JavascriptExecutor jse = (JavascriptExecutor) this.driver;
        return jse.executeScript(s, objects);
    }

    @Override
    public Options manage() {
        return this.driver.manage();
    }

    @Override
    public Navigation navigate() {
        return this.driver.navigate();
    }

    @Override
    public TargetLocator switchTo() {
        return this.driver.switchTo();
    }

    @Override
    public String getWindowHandle() {
        return this.driver.getWindowHandle();
    }

    @Override
    public Set<String> getWindowHandles() {
        return this.driver.getWindowHandles();
    }

    @Override
    public void quit() {
        this.webDriverHub.release(this);
    }

    @Override
    public void close() {
        this.driver.close();
    }

    @Override
    public String getPageSource() {
        return this.driver.getPageSource();
    }

    @Override
    public WebElement findElement(By by) {
        return this.driver.findElement(by);
    }

    @Override
    public List<WebElement> findElements(By by) {
        return this.driver.findElements(by);
    }

    @Override
    public String getTitle() {
        return this.driver.getTitle();
    }

    @Override
    public String getCurrentUrl() {
        return this.driver.getCurrentUrl();
    }

    @Override
    public void get(String s) {
        this.driver.get(s);
    }
}
