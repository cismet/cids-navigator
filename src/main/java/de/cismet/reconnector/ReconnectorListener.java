package de.cismet.reconnector;

/**
 *
 * @author jruiz
 */
public interface ReconnectorListener {

    public void connecting();

    public void connectionFailed(ReconnectorEvent event);

    public void connectionCanceled();

    public void connectionCompleted();
    
}
