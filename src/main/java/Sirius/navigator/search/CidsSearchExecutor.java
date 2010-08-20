/*
 *  Copyright (C) 2010 stefan
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package Sirius.navigator.search;

import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.exception.ConnectionException;
import Sirius.navigator.method.MethodManager;
import Sirius.server.middleware.types.MetaClass;
import Sirius.server.newuser.User;
import Sirius.server.search.Query;
import Sirius.server.search.SearchOption;
import Sirius.server.search.SearchResult;
import Sirius.server.sql.SystemStatement;
import de.cismet.tools.CismetThreadPool;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import javax.swing.SwingWorker;
import org.openide.util.Exceptions;

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
                List<String> classIds = new ArrayList<String>(search.getPossibleResultClasses().size());
                for (MetaClass mc : search.getPossibleResultClasses()) {
                    classIds.add(String.valueOf(mc.getID() + "@" + mc.getDomain()));
                }
                return SessionManager.getProxy().search(classIds, search.generateSearchStatement());

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
