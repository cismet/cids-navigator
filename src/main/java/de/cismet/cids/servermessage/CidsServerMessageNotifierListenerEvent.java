/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.servermessage;

import java.util.EventObject;

import de.cismet.cids.server.messages.CidsServerMessage;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class CidsServerMessageNotifierListenerEvent extends EventObject {

    //~ Instance fields --------------------------------------------------------

    private final CidsServerMessage message;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new CidsServerMessageNotifierListenerEvent object.
     *
     * @param  source   DOCUMENT ME!
     * @param  message  DOCUMENT ME!
     */
    public CidsServerMessageNotifierListenerEvent(final CidsServerMessageNotifier source,
            final CidsServerMessage message) {
        super(source);

        this.message = message;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public CidsServerMessageNotifier getSource() {
        return (CidsServerMessageNotifier)super.getSource();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public CidsServerMessage getMessage() {
        return message;
    }
}
