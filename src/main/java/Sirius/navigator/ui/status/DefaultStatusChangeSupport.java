/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui.status;

import java.beans.*;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class DefaultStatusChangeSupport extends PropertyChangeSupport implements StatusChangeSupport {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DefaultStatusChangeSupport object.
     *
     * @param  source  DOCUMENT ME!
     */
    public DefaultStatusChangeSupport(final Object source) {
        super(source);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  status  DOCUMENT ME!
     */
    public void fireStatusChange(final Status status) {
        if (this.hasListeners()) {
            super.firePropertyChange(StatusChangeListener.STATUS_CHANGED, null, status);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  statusMessage    DOCUMENT ME!
     * @param  messagePosition  DOCUMENT ME!
     */
    public void fireStatusChange(final String statusMessage, final int messagePosition) {
        if (this.hasListeners()) {
            this.fireStatusChange(new Status(statusMessage, messagePosition));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  statusMessage    DOCUMENT ME!
     * @param  messagePosition  DOCUMENT ME!
     * @param  greenIconState   DOCUMENT ME!
     * @param  redIconState     DOCUMENT ME!
     */
    public void fireStatusChange(final String statusMessage,
            final int messagePosition,
            final int greenIconState,
            final int redIconState) {
        if (this.hasListeners()) {
            this.fireStatusChange(new Status(statusMessage, messagePosition, greenIconState, redIconState));
        }
    }

    @Override
    public void addStatusChangeListener(final StatusChangeListener listener) {
        if (MutableStatusBar.logger.isDebugEnabled()) {
            MutableStatusBar.logger.debug("register new status listener"); // NOI18N
        }
        this.addPropertyChangeListener(listener);
    }

    @Override
    public void removeStatusChangeListener(final StatusChangeListener listener) {
        if (MutableStatusBar.logger.isDebugEnabled()) {
            MutableStatusBar.logger.debug("unregister status listener"); // NOI18N
        }
        this.removePropertyChangeListener(listener);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean hasListeners() {
        return this.hasListeners(StatusChangeListener.STATUS_CHANGED);
    }
}
