package selenium.elements;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * User: YamStranger
 * Date: 5/14/15
 * Time: 3:46 PM
 */
public class TextField {
    private final WebDriver driver;
    private final Search[] searches;
    private final Loader loader;


    public TextField(WebDriver driver, final Search... searches) {
        this.driver = driver;
        this.searches = searches;
        loader = new Loader(this.driver, searches);
    }

    public void fill(String text) throws ElementNotFoundException {
        WebElement element=this.loader.any();
        element.clear();
        element.sendKeys(text);
    }

    public void enter() throws ElementNotFoundException {
        WebElement element=this.loader.any();
        element.sendKeys(Keys.ENTER);
    }


}
