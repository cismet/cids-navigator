/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.search;

import Sirius.server.middleware.types.MetaObjectNode;

import java.util.List;

import de.cismet.commons.gui.protocol.ProtocolStep;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public interface CidsServerSearchProtocolStep extends ProtocolStep, CidsServerSearchProtocolStepReexecutor {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    List<MetaObjectNode> getSearchResultNodes();
}
