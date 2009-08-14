/*
 * StatusChangeSupport.java
 *
 * Created on 17. April 2003, 12:02
 */

package Sirius.navigator.ui.status;

/**
 *
 * @author  pascal
 */
public interface StatusChangeSupport
{
    public void addStatusChangeListener(StatusChangeListener listener);
    
    public void removeStatusChangeListener(StatusChangeListener listener);
}
