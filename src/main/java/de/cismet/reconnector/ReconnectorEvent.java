package de.cismet.reconnector;

import java.awt.Component;

/**
 *
 * @author jruiz
 */
public class ReconnectorEvent {

    final private Component component;

    public ReconnectorEvent(final Component panel) {
        this.component = panel;
    }

    public Component getComponent() {
        return component;
    }

}
