package selenium.elements;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * User: YamStranger
 * Date: 5/14/15
 * Time: 4:01 PM
 */
public final class Loader {
    private final WebDriver driver;
    private final Search[] searches;

    public Loader(WebDriver driver, final Search... searches) {
        this.driver = driver;
        this.searches = searches;
    }

    public WebElement any() throws ElementNotFoundException {
        ElementNotFoundException exception = null;
        WebElement element = null;
        for (final Search search : searches) {
            try {
                element = search.one(driver);
            } catch (ElementNotFoundException elementNotFound) {
                exception = elementNotFound;
            }
        }
        if (element == null) {
            if (exception == null) {
                throw new RuntimeException("incorrect implementation");
            }
            throw exception;
        }
        return element;
    }

    public boolean isExist() {
        for (final Search search : searches) {
            try {
                if (!search.all(this.driver).isEmpty()) {
                    return true;
                }
            } catch (ElementNotFoundException elementNotFound) {
            }
        }
        return false;
    }

}
