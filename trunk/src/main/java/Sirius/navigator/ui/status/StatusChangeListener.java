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
public class StatusChangeListener implements PropertyChangeListener {

    //~ Static fields/initializers ---------------------------------------------

    public static final String STATUS_CHANGED = "STATUS_CHANGE"; // NOI18N

    //~ Instance fields --------------------------------------------------------

    private final MutableStatusBar statusBar;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of StatusListener.
     *
     * @param  statusBar  DOCUMENT ME!
     */
    public StatusChangeListener(final MutableStatusBar statusBar) {
        this.statusBar = statusBar;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * This method gets called when a bound property is changed.
     *
     * @param  evt  A PropertyChangeEvent object describing the event source and the property that has changed.
     */
    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        final Object object = evt.getNewValue();
        if (evt.getPropertyName().equals(STATUS_CHANGED) && (object instanceof Status)) {
            statusBar.setStatus((Status)object);
        } else {
            MutableStatusBar.logger.error("invalid status event '" + evt.getPropertyName()
                        + "' or invalid status object '" + object.getClass().toString() + "'"); // NOI18N
        }
    }
}
