/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.plugin.interfaces;

import java.util.*;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public interface MetaSelectionListener {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  metaNodes  DOCUMENT ME!
     */
    void metaNodesSelected(Iterator metaNodes);

    /**
     * DOCUMENT ME!
     *
     * @param  metaAttributes  DOCUMENT ME!
     */
    void metaAttributesSelected(Iterator metaAttributes);
}
