/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.plugin.listener;

import java.util.*;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public abstract class MetaAttributeSelectionListener {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of Sirius.server.localserver.attribute.AttributeSelectionListener.
     */
    public MetaAttributeSelectionListener() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  nodeSelection  DOCUMENT ME!
     */
    protected abstract void attributeSelectionChanged(Collection nodeSelection);
}
