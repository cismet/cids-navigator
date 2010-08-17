package Sirius.navigator.search;

import Sirius.server.middleware.types.MetaClass;
import Sirius.server.search.SearchOption;
import java.util.Collection;
import java.util.Map;
import javax.swing.ImageIcon;

/**
 *
 * @author stefan
 */
public abstract class AbstractCidsSearch implements CidsSearch {

    public AbstractCidsSearch(String name, ImageIcon icon, CidsSearchStatementGenerator stmntGen) {
        this.stmntGenerator = stmntGen;
        this.name = name;
        this.icon = icon;
    }
    private final String name;
    private final ImageIcon icon;
    private final CidsSearchStatementGenerator stmntGenerator;

    @Override
    public Collection<MetaClass> getPossibleResultClasses() {
        return stmntGenerator.getPossibleResultClasses();
    }

    @Override
    public Collection<SearchOption> generateSearchStatement() {
        return stmntGenerator.getParameterizedSearchStatement(getSearchParameter());
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public ImageIcon getIcon() {
        return this.icon;
    }

    public abstract Map<String, Object> getSearchParameter();
}
