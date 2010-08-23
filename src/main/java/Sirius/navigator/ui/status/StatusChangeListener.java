package Sirius.navigator.ui.status;

import java.beans.*;

/**
 *
 * @author  pascal
 */
public class StatusChangeListener implements PropertyChangeListener
{
    public final static String STATUS_CHANGED = "STATUS_CHANGE";//NOI18N
    
    private final MutableStatusBar statusBar;
    
    /** Creates a new instance of StatusListener */
    public StatusChangeListener(MutableStatusBar statusBar)
    {
        this.statusBar = statusBar;
    }
    
    /** This method gets called when a bound property is changed.
     * @param evt A PropertyChangeEvent object describing the event source
     *   	and the property that has changed.
     *
     */
    public void propertyChange(PropertyChangeEvent evt)
    {
        Object object = evt.getNewValue();
        if(evt.getPropertyName().equals(STATUS_CHANGED) && object instanceof Status)
        {
            statusBar.setStatus((Status)object);
        }
        else
        {
            MutableStatusBar.logger.error("invalid status event '" + evt.getPropertyName() + "' or invalid status object '" + object.getClass().toString() + "'");//NOI18N
        }
    } 
}
