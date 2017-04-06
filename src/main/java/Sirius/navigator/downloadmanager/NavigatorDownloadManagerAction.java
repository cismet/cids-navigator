/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.downloadmanager;

import Sirius.navigator.ui.ComponentRegistry;

import de.cismet.cids.navigator.utils.CidsClientToolbarItem;

import de.cismet.tools.gui.downloadmanager.DownloadManagerAction;
import de.cismet.tools.gui.menu.CidsUiAction;

/**
 * DOCUMENT ME!
 *
 * @author   jweintraut
 * @version  $Revision$, $Date$
 */
@org.openide.util.lookup.ServiceProvider(service = CidsClientToolbarItem.class)
public class NavigatorDownloadManagerAction extends DownloadManagerAction implements CidsClientToolbarItem,
    CidsUiAction {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new NavigatorDownloadManagerAction object.
     */
    public NavigatorDownloadManagerAction() {
        super(ComponentRegistry.getRegistry().getMainWindow());
        putValue(CidsUiAction.CIDS_ACTION_KEY, "download.manager");
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public String getSorterString() {
        return "100";
    }

    @Override
    public boolean isVisible() {
        return true;
    }
}
