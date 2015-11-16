/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.stepprotocol;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public interface StepProtocol {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    StepProtocolMetaInfo getMetaInfo();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    String toJsonString() throws JsonProcessingException;

    /**
     * DOCUMENT ME!
     *
     * @param jsonString
     * @throws java.io.IOException
     */
    void fromJsonString(final String jsonString) throws IOException ;

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    AbstractStepProtocolPanel visualize();
}
