package selenium.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * User: YamStranger
 * Date: 4/30/15
 * Time: 9:54 AM
 */

/**
 * If class name of searchable element contains  space - use cssSelector with
 * replaces spaces to dots.
 */
public class Condition {
    private final By by;
    private final static String REGEXP_ALL=".*";
    private final Pattern rule;
    private final List<Condition> filters = new LinkedList<>();

    public Condition(By by) {
        this(by, Condition.REGEXP_ALL);
    }

    public Condition(By by, String text) {
        this(by, text, new Condition[0]);
    }

    /**
     * @param by
     * @param filters condition for checking childrens
     */
    public Condition(By by, final Condition... filters) {
        this(by, Condition.REGEXP_ALL, filters);
    }

    /**
     * @param by
     * @param text
     * @param filters condition for checking childrens
     */
    public Condition(final By by, final String text, final Condition... filters) {
        this.by = by;
        this.rule = Pattern.compile(text);
        this.filters.addAll(Arrays.asList(filters));
    }

    public List<WebElement> load(final WebDriver driver) {
        List<WebElement> elements = driver.findElements(this.by);
        return this.filter(elements);
    }

    public List<WebElement> load(final List<WebElement> elements) {
        final List<WebElement> filtered = new LinkedList<>();
        for (final WebElement element : elements) {
            filtered.addAll(element.findElements(this.by));
        }
        return this.filter(filtered);
    }

    private List<WebElement> filter(final List<WebElement> elements) {
        final List<WebElement> filtered = new LinkedList<>();
        for (final WebElement element : elements) {
            if (element != null) {
                final String value = element.getText().replaceAll("[^\\S ]+","");
                if (Condition.REGEXP_ALL.equals(this.rule.pattern())
                        ||this.rule.matcher(value).matches()) {
                    boolean validness = true;
                    for (final Condition condition : this.filters) {
                        if (condition.load(Arrays.asList(element)).isEmpty()) {
                            validness = false;
                            break;
                        }
                    }
                    if (validness) {
                        filtered.add(element);
                    }
                }
            }
        }
        return filtered;
    }
}
