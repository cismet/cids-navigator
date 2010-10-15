package Sirius.navigator.search;

import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.method.MethodManager;
import Sirius.navigator.search.dynamic.SearchProgressDialog;
import Sirius.navigator.ui.ComponentRegistry;
import Sirius.navigator.ui.status.DefaultStatusChangeSupport;
import Sirius.navigator.ui.status.StatusChangeListener;
import Sirius.server.middleware.types.Node;
import Sirius.server.search.CidsServerSearch;
import de.cismet.tools.CismetThreadPool;
import de.cismet.tools.gui.StaticSwingTools;
import java.awt.EventQueue;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.SwingWorker;
import javax.swing.UIManager;

/**
 *
 * @author stefan
 */
public final class CidsSearchExecutor {

    private static final DefaultStatusChangeSupport dscs = new DefaultStatusChangeSupport(new Object());
    private static final transient org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CidsSearchExecutor.class);
    private static SearchProgressDialog searchProgressDialog;

    public static SwingWorker<Node[], Void> executeCidsSearchAndDisplayResults(final CidsServerSearch search) {
        return executeCidsSearchAndDisplayResults(search, null);
    }

    public static SwingWorker<Node[], Void> executeCidsSearchAndDisplayResults(final CidsServerSearch search, PropertyChangeListener listener) {

        final SwingWorker<Node[], Void> worker = new SwingWorker<Node[], Void>() {
            PropertyChangeListener cancelListener=null;
            @Override
            protected Node[] doInBackground() throws Exception {

                EventQueue.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        cancelListener = new PropertyChangeListener() {

                            @Override
                            public void propertyChange(PropertyChangeEvent evt) {
                                log.fatal("CANCEL-->"+evt);
                                cancel(true);
                            }
                        };
                        dscs.addPropertyChangeListener(cancelListener);
                        getSearchProgressDialog().pack();
                        getSearchProgressDialog().setLocationRelativeTo(ComponentRegistry.getRegistry().getNavigator());
                        getSearchProgressDialog().setLabelAnimation(true);
                        getSearchProgressDialog().setVisible(true);
                    }
                });
                Collection res = SessionManager.getProxy().customServerSearch(SessionManager.getSession().getUser(), search);
                if (!isCancelled()) {
                    ArrayList<Node> aln = new ArrayList<Node>(res.size());
                    for (Object o : res) {
                        aln.add((Node) o);
                    }

                    Node[] ret = aln.toArray(new Node[0]);
                    if (!isCancelled()) {
                        MethodManager.getManager().showSearchResults(ret, false);
                    }
                    return ret;
                }
                return null;

            }

            @Override
            protected void done() {
                try {
                    if (!isCancelled()) {
                        Node[] res = get();

                        getSearchProgressDialog().setVisible(false);
                        getSearchProgressDialog().setLabelAnimation(false);

                    }
                } catch (Exception ex) {
                    log.fatal("suchproblem", ex);
                }
                dscs.removePropertyChangeListener(cancelListener);
            }
        };
        if (listener != null) {
            worker.addPropertyChangeListener(listener);
        }



        CismetThreadPool.execute(worker);
        

        return worker;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private static SearchProgressDialog getSearchProgressDialog() {
        if (searchProgressDialog == null) {
            searchProgressDialog = new SearchProgressDialog(StaticSwingTools.getFirstParentFrame(ComponentRegistry.getRegistry().getDescriptionPane()), dscs);
        }
        return searchProgressDialog;
    }
}
