package de.cismet.reconnector;

import java.awt.Component;

/**
 *
 * @author jruiz
 */
public class ReconnectorException extends Exception {

    private Component component;

    public ReconnectorException(final String errorMsg) {
        component = new DefaultReconnectorErrorPanel(errorMsg);
    }

    public ReconnectorException(final Component component) {
        this.component = component;
    }

    public Component getComponent() {
        return component;
    }
}
