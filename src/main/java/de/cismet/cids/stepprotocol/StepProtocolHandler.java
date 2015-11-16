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
import de.cismet.cids.stepprotocol.impl.EmptyStepProtocol;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class StepProtocolHandler {

    //~ Static fields/initializers ---------------------------------------------

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final transient org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(
            StepProtocolHandler.class);
    
    private static StepProtocolHandler INSTANCE;

    //~ Instance fields --------------------------------------------------------

    private boolean recordEnabled = false;
    private final LinkedList<StepProtocol> stepProtocolList = new LinkedList<StepProtocol>();

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new StepProtocolHandler object.
     */
    private StepProtocolHandler() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static StepProtocolHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new StepProtocolHandler();
        }
        return INSTANCE;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  recordEnabled  DOCUMENT ME!
     */
    public void setRecordEnabled(final boolean recordEnabled) {
        this.recordEnabled = recordEnabled;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isRecordEnabled() {
        return recordEnabled;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  stepProtocol  DOCUMENT ME!
     */
    public void recordStep(final StepProtocol stepProtocol) {
        stepProtocolList.add(stepProtocol);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public StepProtocol getLastStep() {
        return stepProtocolList.getLast();
    }
    
    public List<StepProtocol> getAllSteps() {
        return new ArrayList<StepProtocol>(stepProtocolList);
    }
    
    public String toJsonString() throws JsonProcessingException {
        return MAPPER.writeValueAsString(stepProtocolList);
    }
    
    public void fromJsonString(final String jsonString) throws IOException, ClassNotFoundException {
        final List<StepProtocol> newSteps = new ArrayList<StepProtocol>();
        final List<HashMap> list = (List)MAPPER.readValue(jsonString, List.class); 
        for (final HashMap item : list) {
            final String metaInfoJson = MAPPER.writeValueAsString(item.get("metaInfo"));
            final StepProtocolMetaInfo metaInfo = MAPPER.readValue(metaInfoJson, StepProtocolMetaInfo.class);
            final String javaCanonicalClassName = metaInfo.getJavaCanonicalClassName();
            final Class stepClass = Class.forName(javaCanonicalClassName);  
            
            final String itemJson = MAPPER.writeValueAsString(item);
            newSteps.add((StepProtocol)MAPPER.readValue(itemJson, stepClass));
        }
        
        stepProtocolList.clear();
        stepProtocolList.addAll(newSteps);
    }
}
