/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.types.iterator;

import Sirius.navigator.types.treenode.*;

import Sirius.server.middleware.types.*;

import java.util.Collection;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public interface AttributeRestriction extends Restriction {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   attribute  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Sirius.server.localserver.attribute.Attribute applyRestriction(
            Sirius.server.localserver.attribute.Attribute attribute);
}
