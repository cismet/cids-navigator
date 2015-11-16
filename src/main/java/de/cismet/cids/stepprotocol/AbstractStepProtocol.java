/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.stepprotocol;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;

import java.io.IOException;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;
import lombok.Setter;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
@XmlRootElement
@Getter
@Setter
public abstract class AbstractStepProtocol implements StepProtocol {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(
            AbstractStepProtocol.class);
    protected static final transient ObjectMapper MAPPER = new ObjectMapper();

    //~ Instance fields --------------------------------------------------------

    private final StepProtocolMetaInfo metaInfo;

    private Date date;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new AbstractStepProtocol object.
     */
    protected AbstractStepProtocol() {
        this.metaInfo = createMetaInfo();
        this.date = new Date();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected abstract StepProtocolMetaInfo createMetaInfo();

    @Override
    public String toJsonString() throws JsonProcessingException {
        synchronized (MAPPER) {
            return MAPPER.writeValueAsString(this);
        }
    }

    @Override
    public void fromJsonString(String jsonString) throws IOException {
        final AbstractStepProtocol stepProtocol = (AbstractStepProtocol)fromJsonString(jsonString, getClass());
        copyParams(stepProtocol);
    }
    
    /**
     * DOCUMENT ME!
     *
     * @param   jsonString    DOCUMENT ME!
     * @param   stepProtocol  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    public static StepProtocol fromJsonString(final String jsonString,
            final Class<? extends AbstractStepProtocol> stepProtocol) throws IOException {
        synchronized (MAPPER ){
            return (AbstractStepProtocol)MAPPER.readValue(jsonString, stepProtocol);
        }
    }

    @Override
    public abstract AbstractStepProtocolPanel visualize();
    
    protected void copyParams(final AbstractStepProtocol other) {
        setDate(other.getDate());
    }
}
