/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.plugin;

import Sirius.navigator.plugin.interfaces.*;
import Sirius.navigator.plugin.ui.*;
import Sirius.navigator.ui.*;
import Sirius.navigator.ui.widget.*;

import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.event.*;

import java.beans.*;

import javax.swing.*;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public final class PluginUIDescriptor extends MutableConstraints implements PropertyChangeListener {

    //~ Instance fields --------------------------------------------------------

    private PluginUI pluginUI = null;

    /** Holds value of property pluginComponentEventsEnabled. */
    private boolean pluginComponentEventsEnabled = false;

    /** Holds value of property iconName. */
    private String iconName = null;

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  pluginUI  DOCUMENT ME!
     */
    public void addAsComponent(final PluginUI pluginUI) {
        this.pluginUI = pluginUI;
        this.setContainerType(this.NONE);
        this.addAsComponent(pluginUI.getComponent());
        this.setComponentEventsEnabled(pluginComponentEventsEnabled);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  pluginUI  DOCUMENT ME!
     * @param  layout    DOCUMENT ME!
     */
    public void addAsPanel(final PluginUI pluginUI, final String layout) {
        this.pluginUI = pluginUI;
        this.setContainerType(this.PANEL);
        super.addAsPanel(pluginUI.getComponent(), pluginComponentEventsEnabled, layout);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  pluginUI  DOCUMENT ME!
     */
    public void addAsScrollPane(final PluginUI pluginUI) {
        this.pluginUI = pluginUI;
        this.setContainerType(this.SCROLLPANE);
        super.addAsScrollPane(pluginUI.getComponent(), pluginComponentEventsEnabled);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  pluginUI      DOCUMENT ME!
     * @param  configurator  DOCUMENT ME!
     */
    public void addAsFloatingFrame(final FloatingPluginUI pluginUI,
            final PluginFloatingFrameConfigurator configurator) {
        this.pluginUI = pluginUI;
        this.setContainerType(this.FLOATINGFRAME);

        configurator.setId(this.getId());
        configurator.setName(this.getName());
        configurator.setIcon(this.getIcon());

        if (configurator.isToolBarAvailable()) {
            configurator.setButtons(pluginUI.getButtons());
        }

        if (configurator.isMenuBarAvailable()) {
            configurator.setMenues(pluginUI.getMenus());
        }

        super.addAsFloatingFrame(pluginUI.getComponent(),
            pluginComponentEventsEnabled,
            configurator,
            configurator.isFloatingEventsEnabled());
    }

    // -------------------------------------------------------------------------

    /**
     * Getter for property pluginComponentEventsEnabled.
     *
     * @return  Value of property pluginComponentEventsEnabled.
     */
    public boolean isPluginComponentEventsEnabled() {
        return pluginComponentEventsEnabled;
    }

    /**
     * Setter for property pluginComponentEventsEnabled.
     *
     * @param  pluginComponentEventsEnabled  New value of property pluginComponentEventsEnabled.
     */
    public void setPluginComponentEventsEnabled(final boolean pluginComponentEventsEnabled) {
        this.pluginComponentEventsEnabled = pluginComponentEventsEnabled;
    }

    /**
     * Getter for property pluginIconId.
     *
     * @return  Value of property pluginIconId.
     */
    public String getIconName() {
        return this.iconName;
    }

    /**
     * Setter for property pluginIconId.
     *
     * @param  iconName  pluginIconId New value of property pluginIconId.
     */
    public void setIconName(final String iconName) {
        this.iconName = iconName;
    }

    // EventHandler ------------------------------------------------------------

    /**
     * Invoked when the component has been made invisible.
     *
     * @param  e  DOCUMENT ME!
     */
    @Override
    public void componentHidden(final ComponentEvent e) {
        pluginUI.hidden();
    }

    /**
     * Invoked when the component's position changes.
     *
     * @param  e  DOCUMENT ME!
     */
    @Override
    public void componentMoved(final ComponentEvent e) {
        pluginUI.moved();
    }

    /**
     * Invoked when the component's size changes.
     *
     * @param  e  DOCUMENT ME!
     */
    @Override
    public void componentResized(final ComponentEvent e) {
        pluginUI.resized();
    }

    /**
     * Invoked when the component has been made visible.
     *
     * @param  e  DOCUMENT ME!
     */
    @Override
    public void componentShown(final ComponentEvent e) {
        pluginUI.shown();
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param  evt  A PropertyChangeEvent object describing the event source and the property that has changed.
     */
    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        // if(evt.getPropertyName().equals(FloatingFrame.FLOATING))
        // {
        if (((Boolean)evt.getNewValue()).booleanValue()) {
            ((FloatingPluginUI)pluginUI).floatingStarted();
        } else {
            ((FloatingPluginUI)pluginUI).floatingStopped();
        }
        // }
    }
}
