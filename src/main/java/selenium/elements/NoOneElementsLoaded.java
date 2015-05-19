package selenium.elements;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * User: YamStranger
 * Date: 5/14/15
 * Time: 6:08 PM
 */
public class NoOneElementsLoaded implements ExpectedCondition<Boolean> {
    private final List<Search> searches;

    public NoOneElementsLoaded(Search... search) {
        this.searches = new LinkedList<>();
        this.searches.addAll(Arrays.asList(search));
    }

    public Boolean apply(final WebDriver driver) {
        Iterator<Search> iterator = searches.iterator();
        while (iterator.hasNext()) {
            try {
                if (!iterator.next().all(driver).isEmpty()) {
                    return false;
                }
            } catch (ElementNotFoundException |StaleElementReferenceException e) {
                //skip, because this element is not exist.
            }
        }
        return true;

    }
}
