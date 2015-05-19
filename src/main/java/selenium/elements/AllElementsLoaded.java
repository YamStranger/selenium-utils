package selenium.elements;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * User: YamStranger
 * Date: 4/30/15
 * Time: 6:52 PM
 */
public class AllElementsLoaded implements ExpectedCondition<Boolean> {
    private final List<Search> searches;

    public AllElementsLoaded(Search... search) {
        this.searches = new LinkedList<>();
        this.searches.addAll(Arrays.asList(search));
    }

    public Boolean apply(final WebDriver driver) {
        try {
            Iterator<Search> iterator = searches.iterator();
            while (iterator.hasNext()) {
                if (!iterator.next().all(driver).isEmpty()) {
                    iterator.remove();
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
