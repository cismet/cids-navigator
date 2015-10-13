/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui.tree.postfilter;

import Sirius.server.middleware.types.Node;

import java.util.Collection;

import javax.swing.Icon;
import javax.swing.JPanel;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public interface PostFilterGUI {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  nodes  DOCUMENT ME!
     */
    void initializeFilter(Collection<Node> nodes);

    /**
     * DOCUMENT ME!
     *
     * @param  nodes  DOCUMENT ME!
     */
    void adjustFilter(Collection<Node> nodes);

    /**
     * DOCUMENT ME!
     *
     * @param   nodes  classKey DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    boolean canHandle(Collection<Node> nodes);
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    boolean isActive();
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    String getTitle();
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    JPanel getGUI();
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    PostFilter getFilter();

    /**
     * DOCUMENT ME!
     *
     * @param  pfl  DOCUMENT ME!
     */
    void addPostFilterListener(PostFilterListener pfl);
    /**
     * DOCUMENT ME!
     *
     * @param  pfl  DOCUMENT ME!
     */
    void removePostFilterListener(PostFilterListener pfl);
    /**
     * DOCUMENT ME!
     */
    void firePostFilterChanged();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Integer getDisplayOrderKeyPrio();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Icon getIcon();
}
