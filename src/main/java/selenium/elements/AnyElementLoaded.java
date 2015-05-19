package selenium.elements;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * User: YamStranger
 * Date: 5/3/15
 * Time: 8:37 AM
 */
public class AnyElementLoaded implements ExpectedCondition<Boolean> {
    private final List<Search> searches;

    public AnyElementLoaded(Search... search) {
        this.searches = new LinkedList<>();
        this.searches.addAll(Arrays.asList(search));
    }

    public Boolean apply(final WebDriver driver) {
        try {
            Iterator<Search> iterator = searches.iterator();
            while (iterator.hasNext()) {
                if (!iterator.next().all(driver).isEmpty()) {
                    return true;
                } else {
                    return false;
                }
            }
            return true;
        } catch (ElementNotFoundException e) {
            return false;
        }
    }
}
