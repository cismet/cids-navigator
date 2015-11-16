/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.stepprotocol;

import org.apache.log4j.Logger;

import java.io.IOException;

import de.cismet.cids.stepprotocol.impl.CommentStepProtocol;

import de.cismet.tools.gui.log4jquickconfig.Log4JQuickConfig;
import java.util.List;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class StepProtocolTester {

    //~ Static fields/initializers ---------------------------------------------

    private static Logger LOG = Logger.getLogger(StepProtocolTester.class);

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new StepProtocolTester object.
     */
    private StepProtocolTester() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  args  DOCUMENT ME!
     */
    public static void main(final String[] args) {
        Log4JQuickConfig.configure4LumbermillOnLocalhost();
        final StepProtocolTester tester = new StepProtocolTester();
        try {
            tester.test();
        } catch (final Exception ex) {
            LOG.fatal(ex, ex);
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void test() throws Exception {
        log("========================");
        log("= single protocol test = ");
        log("========================");
        
        log("creating protocol...");        
        final CommentStepProtocol proto = new CommentStepProtocol("dies ist ein Test-Kommentar");
        log("protocol: " + proto);        
        log("* date: " + proto.getDate());
        log("* message: " + proto.getMessage());

        log("storing protocol to json...");
        final String jsonString = proto.toJsonString();
        log("json:" + jsonString);


        log("restoring protocol from json...");
        final CommentStepProtocol newProto = (CommentStepProtocol)AbstractStepProtocol.fromJsonString(jsonString, CommentStepProtocol.class);
        log("protocol: " + newProto);        
        log("* date: " + newProto.getDate());
        log("* message: " + newProto.getMessage());

        log("=========================");
        log("= protocol handler test =");
        log("=========================");
        
        log("recording some protocols...");        
        StepProtocolHandler.getInstance().recordStep(new CommentStepProtocol("Protollierungs-Test nummer eins..."));
        StepProtocolHandler.getInstance().recordStep(new CommentStepProtocol("...noch ein Test..."));
        StepProtocolHandler.getInstance().recordStep(new CommentStepProtocol("...es wird wie wild getestet..."));
        StepProtocolHandler.getInstance().recordStep(new CommentStepProtocol("...irgendwann reicht es aber auch !"));
        log("number of recorded protocols: " +  StepProtocolHandler.getInstance().getAllSteps().size());
        
        log("storing all protocols to json...");        
        final String allProtosJson = StepProtocolHandler.getInstance().toJsonString();        
        log("json: " + allProtosJson);        

        log("restoring all protocols from json...");
        StepProtocolHandler.getInstance().fromJsonString(allProtosJson);
        final List<StepProtocol> newList = StepProtocolHandler.getInstance().getAllSteps();
        log("size after jsonify: " + newList.size());
        log("protocolls after jsonify: ");
        for (final StepProtocol newProtoFromList : newList) {        
        log("protocol: " + newProtoFromList);        

        final CommentStepProtocol newProtoFromListCasted = (CommentStepProtocol)newProtoFromList;
            log("* date: " + newProtoFromListCasted.getDate());
            log("* message: " + newProtoFromListCasted.getMessage());
            log(" ----- ");
        }
    }
    
    private static void log(final String message) {
        LOG.debug(message);
        System.out.println(message);
    }
}
