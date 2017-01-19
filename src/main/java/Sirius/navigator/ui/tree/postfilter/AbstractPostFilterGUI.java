/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui.tree.postfilter;

import Sirius.navigator.resource.ResourceManager;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.JPanel;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public abstract class AbstractPostFilterGUI extends javax.swing.JPanel implements PostFilterGUI {

    //~ Instance fields --------------------------------------------------------

    @Getter @Setter protected boolean selected = false;
    final ResourceManager resources = ResourceManager.getManager();
    final Icon defaultIcon = resources.getIcon("funnel.png");
    private final ArrayList<PostFilterListener> pfListeners = new ArrayList<PostFilterListener>();

    //~ Methods ----------------------------------------------------------------

    @Override
    public void addPostFilterListener(final PostFilterListener pfl) {
        if (!pfListeners.contains(pfl)) {
            pfListeners.add(pfl);
        }
    }

    @Override
    public void removePostFilterListener(final PostFilterListener pfl) {
        pfListeners.remove(pfl);
    }

    @Override
    public void firePostFilterChanged() {
        final ArrayList<PostFilterListener> pfListenersCopy = new ArrayList<PostFilterListener>(pfListeners);

        for (final PostFilterListener pfl : pfListenersCopy) {
            pfl.filterContentChanged(getFilter());
        }
    }

    @Override
    public JPanel getGUI() {
        return this;
    }

    @Override
    public Icon getIcon() {
        return defaultIcon;
    }
}
