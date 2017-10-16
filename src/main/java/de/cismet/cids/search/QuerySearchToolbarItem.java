/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.search;

import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.exception.ConnectionException;
import Sirius.navigator.ui.ComponentRegistry;

import org.jfree.util.Log;

import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import de.cismet.cids.navigator.utils.CidsClientToolbarItem;

import de.cismet.cids.server.connectioncontext.ClientConnectionContext;
import de.cismet.cids.server.connectioncontext.ClientConnectionContextProvider;

/**
 * DOCUMENT ME!
 *
 * @author   mroncoroni
 * @version  $Revision$, $Date$
 */
@ServiceProvider(service = CidsClientToolbarItem.class)
public class QuerySearchToolbarItem extends AbstractAction implements CidsClientToolbarItem,
    ClientConnectionContextProvider {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new QuerySearchToolbarItem object.
     */
    public QuerySearchToolbarItem() {
        putValue(
            Action.SHORT_DESCRIPTION,
            NbBundle.getMessage(QuerySearchToolbarItem.class, "QuerySearchToolbarItem.short_description"));
        putValue(Action.SMALL_ICON, new ImageIcon(this.getClass().getResource("/de/cismet/cids/search/binocular.png")));
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void actionPerformed(final ActionEvent e) {
        final String id = QuerySearch.class.getName();
        ComponentRegistry.getRegistry().getGUIContainer().select(id);
    }

    @Override
    public String getSorterString() {
        return "Z";
    }

    @Override
    public boolean isVisible() {
        try {
            return (SessionManager.getConnection().getConfigAttr(
                        SessionManager.getSession().getUser(),
                        "navigator.querybuilder.toolbaricon@"
                                + SessionManager.getSession().getUser().getDomain(),
                        getClientConnectionContext())
                            != null)
                        && (SessionManager.getConnection().getConfigAttr(
                                SessionManager.getSession().getUser(),
                                QuerySearch.ACTION_TAG
                                + SessionManager.getSession().getUser().getDomain(),
                                getClientConnectionContext())
                            != null);
        } catch (ConnectionException ex) {
            Log.error(ex);
        }
        return false;
    }

    @Override
    public ClientConnectionContext getClientConnectionContext() {
        return ClientConnectionContext.create(getClass().getSimpleName());
    }
}
