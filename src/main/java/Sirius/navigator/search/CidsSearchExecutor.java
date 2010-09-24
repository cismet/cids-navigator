package Sirius.navigator.search;

import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.method.MethodManager;
import Sirius.server.middleware.types.MetaClass;
import Sirius.server.search.SearchResult;
import de.cismet.tools.CismetThreadPool;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;

/**
 *
 * @author stefan
 */
public final class CidsSearchExecutor {

//    public static SearchResult executeCidsSearch(CidsSearch search) throws ConnectionException {
//
//        List<String> classIds = new ArrayList<String>(search.getPossibleResultClasses().size());
//        for (MetaClass mc : search.getPossibleResultClasses()) {
//            classIds.add(String.valueOf(mc.getID() + "@" + mc.getDomain()));
//        }
//        SearchResult matchingObjects = SessionManager.getProxy().search(classIds, search.generateSearchStatement());
////        SearchResult result = new SearchResult(matchingObjects);
//        try {
//            MethodManager.getManager().showSearchResults(matchingObjects.getNodes(), true);
//        } catch (Exception ex) {
//            //nop
//        }
//        return matchingObjects;
//    }
    public static SwingWorker<SearchResult, Void> executeCidsSearch(final CidsSearch search) {
        return executeCidsSearch(search, null);
    }

    public static SwingWorker<SearchResult, Void> executeCidsSearch(final CidsSearch search, PropertyChangeListener listener) {
        SwingWorker<SearchResult, Void> worker = new SwingWorker<SearchResult, Void>() {

            @Override
            protected SearchResult doInBackground() throws Exception {
                List<String> classKeys = new ArrayList<String>(search.getPossibleResultClasses().size());
                for (MetaClass mc : search.getPossibleResultClasses()) {
                    classKeys.add(mc.getID() + "@" + mc.getDomain());
                }
                return SessionManager.getProxy().search(classKeys, search.generateSearchStatement());

            }

            @Override
            protected void done() {
                try {
                    if (!isCancelled()) {
                        SearchResult res = get();
                        MethodManager.getManager().showSearchResults(res.getNodes(), true);
                    }
                } catch (InterruptedException ex) {
                } catch (ExecutionException ex) {
                } catch (Exception ex) {
                }
            }
        };
        if (listener != null) {
            worker.addPropertyChangeListener(listener);
        }
        CismetThreadPool.execute(worker);
        return worker;
    }
}
