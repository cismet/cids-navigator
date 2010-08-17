package Sirius.navigator.search;

import Sirius.navigator.connection.SessionManager;
import Sirius.server.middleware.types.MetaClass;
import Sirius.server.newuser.User;
import Sirius.server.search.Query;
import Sirius.server.search.SearchOption;
import Sirius.server.search.SearchResult;
import Sirius.server.sql.SystemStatement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 *
 * @author stefan
 */
public class SimpleSearchStatementGenerator implements CidsSearchStatementGenerator {

    public SimpleSearchStatementGenerator(String statement, Collection<MetaClass> resultClasses) {
        this.statement = statement;
        this.resultClasses = resultClasses;
    }
    private final String statement;
    private final Collection<MetaClass> resultClasses;

    @Override
    public Collection<MetaClass> getPossibleResultClasses() {
        return resultClasses;
    }

    @Override
    public Collection<SearchOption> getParameterizedSearchStatement(Map<String, Object> parameterMap) {
        User user = SessionManager.getSession().getUser();
        String domain = user.getDomain();
        SystemStatement sysStmnt = new SystemStatement(true, -1, "", false, SearchResult.NODE, statement);
        Query query = new Query(sysStmnt, domain);
        List<SearchOption> searchOpts = new ArrayList<SearchOption>(1);
        SearchOption so = new SearchOption(query);
//        for (MetaClass mc : getPossibleResultClasses()) {
//            so.addClass(String.valueOf(mc.getID() + "@" + mc.getDomain()));
//        }
        //TODO set parameter!!!
        searchOpts.add(so);
        return searchOpts;
    }
}
