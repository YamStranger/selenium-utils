package selenium.elements;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * User: YamStranger
 * Date: 4/30/15
 * Time: 9:53 AM
 */
public class Search {
    //    private final WebDriver driver;
    private final List<Condition> conditions = new LinkedList<>();
    private String id = "";

    public Search(String id, Condition... conditions) {
        //      this.driver = driver;
        this.conditions.addAll(Arrays.asList(conditions));
        this.id = id;
    }

    public List<WebElement> all(final WebDriver driver) throws ElementNotFoundException {
        List<WebElement> elements = new LinkedList<>();
        for (final Condition condition : conditions) {
            if (elements.isEmpty()) {
                elements = condition.load(driver);
            } else {
                elements = condition.load(elements);
            }
        }
        return elements;
    }

    public WebElement one(final WebDriver driver) throws ElementNotFoundException {
        List<WebElement> elements = new LinkedList<>();
        for (final Condition condition : conditions) {
            if (elements.isEmpty()) {
                elements = condition.load(driver);
            } else {
                elements = condition.load(elements);
                if (elements.isEmpty()) {
                    break;
                }
            }
        }
        if (elements.size() > 1) {
            throw new ElementNotFoundException("Can not find exactly one element");
        } else if (elements.isEmpty()) {
            throw new ElementNotFoundException("Can not find any element");
        }
        return elements.get(0);
    }

    public WebElement one(final WebElement parent) throws ElementNotFoundException {
        List<WebElement> elements = new LinkedList<>();
        elements.add(parent);
        for (final Condition condition : conditions) {
            elements = condition.load(elements);
            if (elements.isEmpty()) {
                break;
            }
        }
        if (elements.size() > 1) {
            throw new ElementNotFoundException("Can not find exactly one element");
        } else if (elements.isEmpty()) {
            throw new ElementNotFoundException("Can not find any element");
        }
        return elements.get(0);
    }

    public List<WebElement> all(final WebElement parent) throws ElementNotFoundException {
        List<WebElement> elements = new LinkedList<>();
        elements.add(parent);
        for (final Condition condition : conditions) {
            elements = condition.load(elements);
        }
        return elements;
    }
}
