package Sirius.navigator.ui.status;

import java.beans.*;

/**
 *
 * @author  pascal
 */
public class DefaultStatusChangeSupport extends PropertyChangeSupport implements StatusChangeSupport
{    
    public DefaultStatusChangeSupport(Object source) 
    {
        super(source);
    }
    
    public void fireStatusChange(Status status) 
    {
        if(this.hasListeners())
        {
            super.firePropertyChange(StatusChangeListener.STATUS_CHANGED, null, status);
        }
    }
    
    public void fireStatusChange(String statusMessage, int messagePosition) 
    {
        if(this.hasListeners())
        {
            this.fireStatusChange(new Status(statusMessage, messagePosition));
        }
    }
    
    public void fireStatusChange(String statusMessage, int messagePosition, int greenIconState, int redIconState) 
    {
        if(this.hasListeners())
        {
            this.fireStatusChange(new Status(statusMessage, messagePosition, greenIconState, redIconState));
        }
    }

    public void addStatusChangeListener(StatusChangeListener listener)
    {
        if(MutableStatusBar.logger.isDebugEnabled())MutableStatusBar.logger.debug("register new status listener");
        this.addPropertyChangeListener(listener);
    }
    
    public void removeStatusChangeListener(StatusChangeListener listener)
    {
        if(MutableStatusBar.logger.isDebugEnabled())MutableStatusBar.logger.debug("unregister status listener");
        this.removePropertyChangeListener(listener);
    }
    
    public boolean hasListeners() 
    {
        return this.hasListeners(StatusChangeListener.STATUS_CHANGED);
    }
}