/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.search;

import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.method.MethodManager;
import Sirius.navigator.search.dynamic.SearchProgressDialog;
import Sirius.navigator.ui.ComponentRegistry;
import Sirius.navigator.ui.status.DefaultStatusChangeSupport;
import Sirius.navigator.ui.status.StatusChangeListener;

import Sirius.server.middleware.types.Node;
import Sirius.server.search.CidsServerSearch;

import java.awt.EventQueue;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.SwingWorker;
import javax.swing.UIManager;

import de.cismet.tools.CismetThreadPool;

import de.cismet.tools.gui.StaticSwingTools;

/**
 * DOCUMENT ME!
 *
 * @author   stefan
 * @version  $Revision$, $Date$
 */
public final class CidsSearchExecutor {

    //~ Static fields/initializers ---------------------------------------------

    private static final DefaultStatusChangeSupport dscs = new DefaultStatusChangeSupport(new Object());
    private static final transient org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(
            CidsSearchExecutor.class);
    private static SearchProgressDialog searchProgressDialog;

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   search  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static SwingWorker<Node[], Void> executeCidsSearchAndDisplayResults(final CidsServerSearch search) {
        return executeCidsSearchAndDisplayResults(search, null);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   search    DOCUMENT ME!
     * @param   listener  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static SwingWorker<Node[], Void> executeCidsSearchAndDisplayResults(final CidsServerSearch search,
            final PropertyChangeListener listener) {
        final SwingWorker<Node[], Void> worker = new SwingWorker<Node[], Void>() {

                PropertyChangeListener cancelListener = null;

                @Override
                protected Node[] doInBackground() throws Exception {
                    EventQueue.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                cancelListener = new PropertyChangeListener() {

                                        @Override
                                        public void propertyChange(final PropertyChangeEvent evt) {
                                            log.fatal("CANCEL-->" + evt);
                                            cancel(true);
                                        }
                                    };
                                dscs.addPropertyChangeListener(cancelListener);
                                getSearchProgressDialog().pack();
                                getSearchProgressDialog().setLocationRelativeTo(
                                    ComponentRegistry.getRegistry().getNavigator());
                                getSearchProgressDialog().setLabelAnimation(true);
                                getSearchProgressDialog().setVisible(true);
                            }
                        });

                    final Collection res = SessionManager.getProxy()
                                .customServerSearch(SessionManager.getSession().getUser(), search);
                    if (!isCancelled()) {
                        final ArrayList<Node> aln = new ArrayList<Node>(res.size());
                        for (final Object o : res) {
                            aln.add((Node)o);
                        }

                        final Node[] ret = aln.toArray(new Node[0]);
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
                            final Node[] res = get();

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
            searchProgressDialog = new SearchProgressDialog(StaticSwingTools.getFirstParentFrame(
                        ComponentRegistry.getRegistry().getDescriptionPane()),
                    dscs);
        }
        return searchProgressDialog;
    }
}
