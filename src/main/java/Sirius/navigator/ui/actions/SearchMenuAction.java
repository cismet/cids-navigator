/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui.actions;

import Sirius.navigator.method.MethodManager;
import Sirius.navigator.resource.ResourceManager;
import Sirius.navigator.ui.MutableMenuBar;
import Sirius.navigator.ui.dialog.ErrorDialog;

import org.apache.log4j.Logger;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import de.cismet.tools.gui.StaticSwingTools;
import de.cismet.tools.gui.menu.CidsUiAction;

import static javax.swing.Action.ACCELERATOR_KEY;
import static javax.swing.Action.ACTION_COMMAND_KEY;
import static javax.swing.Action.MNEMONIC_KEY;
import static javax.swing.Action.NAME;
import static javax.swing.Action.SHORT_DESCRIPTION;
import static javax.swing.Action.SMALL_ICON;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
@org.openide.util.lookup.ServiceProvider(service = CidsUiAction.class)
public class SearchMenuAction extends AbstractAction implements CidsUiAction {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(NavigatorMenuActionProvider.class);

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new SearchMenuAction object.
     */
    public SearchMenuAction() {
        initAction();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    private void initAction() {
        putValue(SMALL_ICON, ResourceManager.getManager().getIcon("find16.gif"));
        putValue(
            NAME,
            org.openide.util.NbBundle.getMessage(
                MutableMenuBar.class,
                "SearchMenuAction.initAction.search.title"));
        putValue(
            MNEMONIC_KEY,
            org.openide.util.NbBundle.getMessage(
                MutableMenuBar.class,
                "SearchMenuAction.initAction.search.mnemonic"));
        putValue(
            SHORT_DESCRIPTION,
            org.openide.util.NbBundle.getMessage(
                MutableMenuBar.class,
                "SearchMenuAction.initAction.search.tooltip"));
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("alt S"));
        putValue(ACTION_COMMAND_KEY, "search.search");
        putValue(CidsUiAction.CIDS_ACTION_KEY, "search.search");
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        try {
            MethodManager.getManager().showSearchDialog();
        } catch (Exception ex) {
            LOG.fatal("Error while processing search method", ex); // NOI18N

            final ErrorDialog errorDialog = new ErrorDialog(
                    org.openide.util.NbBundle.getMessage(
                        MutableMenuBar.class,
                        "SearchMenuAction.actionPerformed(ActionEvent).ErrorDialog.message"), // NOI18N
                    ex.toString(),
                    ErrorDialog.WARNING);
            StaticSwingTools.showDialog(errorDialog);
        }
    }
}
