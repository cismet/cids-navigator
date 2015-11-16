/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.stepprotocol.impl;

import lombok.Getter;
import lombok.Setter;

import de.cismet.cids.stepprotocol.AbstractStepProtocol;
import de.cismet.cids.stepprotocol.AbstractStepProtocolPanel;
import de.cismet.cids.stepprotocol.StepProtocolMetaInfo;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
@Getter
@Setter
public class CommentStepProtocol extends AbstractStepProtocol {

    //~ Instance fields --------------------------------------------------------

    private String message;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new CommentStepProtocol object.
     *
     * @param  message  DOCUMENT ME!
     */
    public CommentStepProtocol(final String message) {
        this.message = message;
    }

    /**
     * Creates a new CommentStepProtocol object.
     */
    private CommentStepProtocol() {
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    protected StepProtocolMetaInfo createMetaInfo() {
        return new StepProtocolMetaInfo(
                "comment",
                "comment step protocol",
                CommentStepProtocol.class.getCanonicalName());
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getMessage() {
        return message;
    }
    
    @Override
    public AbstractStepProtocolPanel visualize() {
        return new CommentStepProtocolPanel(this);
    }

    @Override
    protected void copyParams(final AbstractStepProtocol other) {
        super.copyParams(other);
        final CommentStepProtocol otherCommentStepProtocol = (CommentStepProtocol)other;
        setMessage(otherCommentStepProtocol.getMessage());
    }
        
}
