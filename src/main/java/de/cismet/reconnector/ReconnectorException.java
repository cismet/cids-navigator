package de.cismet.reconnector;

import java.awt.Component;
import javax.swing.JLabel;

/**
 *
 * @author jruiz
 */
public class ReconnectorException extends Exception {

    private Component component;

    public ReconnectorException(final String errorMsg) {
        component = new JLabel(errorMsg);
    }

    public ReconnectorException(final Component component) {
        this.component = component;
    }

    public Component getComponent() {
        return component;
    }
}
