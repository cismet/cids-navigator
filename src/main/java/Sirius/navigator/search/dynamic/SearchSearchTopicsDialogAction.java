/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.search.dynamic;

import org.apache.log4j.Logger;

import org.openide.util.NbBundle;

import java.awt.event.ActionEvent;

import java.net.URL;

import java.util.MissingResourceException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JDialog;

import de.cismet.cids.navigator.utils.CidsClientToolbarItem;

import de.cismet.cismap.commons.interaction.CismapBroker;

/**
 * DOCUMENT ME!
 *
 * @author   jweintraut
 * @version  $Revision$, $Date$
 */
@org.openide.util.lookup.ServiceProvider(service = CidsClientToolbarItem.class)
public class SearchSearchTopicsDialogAction extends AbstractAction implements CidsClientToolbarItem {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(SearchSearchTopicsDialogAction.class);

    //~ Instance fields --------------------------------------------------------

    private JDialog dialog;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new SearchSearchTopicsDialogAction object.
     */
    public SearchSearchTopicsDialogAction() {
        final URL icon = getClass().getResource("/Sirius/navigator/search/dynamic/search.png");
        String name = "Suche";
        String tooltiptext = "Suche";
        String command = "cmdSearch";

        try {
            name = NbBundle.getMessage(SearchSearchTopicsDialogAction.class, "SearchSearchTopicsDialogAction.name");
            tooltiptext = NbBundle.getMessage(
                    SearchSearchTopicsDialogAction.class,
                    "SearchSearchTopicsDialogAction.tooltiptext");
            command = NbBundle.getMessage(
                    SearchSearchTopicsDialogAction.class,
                    "SearchSearchTopicsDialogAction.actionCommandKey");
        } catch (MissingResourceException e) {
            LOG.error("Couldn't find resources. Using fallback settings.", e);
        }

        if (icon != null) {
            putValue(SMALL_ICON, new javax.swing.ImageIcon(icon));
        }

        putValue(Action.NAME, name);
        putValue(Action.SHORT_DESCRIPTION, tooltiptext);
        putValue(Action.ACTION_COMMAND_KEY, command);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public String getSorterString() {
        return "000";
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        if (dialog == null) {
            if (CismapBroker.getInstance().getMetaSearch() == null) {
                return;
            }
            dialog = CismapBroker.getInstance().getMetaSearch().getSearchDialog();
        }

        dialog.setVisible(true);
        dialog.pack();
    }
}
