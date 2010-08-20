package Sirius.navigator.search;

import Sirius.server.middleware.types.MetaClass;
import Sirius.server.search.SearchOption;
import java.util.Collection;
import javax.swing.ImageIcon;

/**
 *
 * @author stefan
 */
public interface CidsSearch {

    Collection<MetaClass> getPossibleResultClasses();

    Collection<SearchOption> generateSearchStatement();

    String getName();

    ImageIcon getIcon();
}
