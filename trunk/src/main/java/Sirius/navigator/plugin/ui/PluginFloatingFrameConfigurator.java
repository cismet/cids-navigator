/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.plugin.ui;

import Sirius.navigator.ui.widget.*;

import javax.swing.*;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class PluginFloatingFrameConfigurator extends FloatingFrameConfigurator {

    //~ Instance fields --------------------------------------------------------

    /** Holds value of property toolBarAvailable. */
    private boolean toolBarAvailable;

    /** Holds value of property floatingEventsEnabled. */
    private boolean floatingEventsEnabled;

    /** Holds value of property menuBarAvailable. */
    private boolean menuBarAvailable;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of FloatingFrameProperties.
     */
    public PluginFloatingFrameConfigurator() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Getter for property toolBarAvailable.
     *
     * @return  Value of property toolBarAvailable.
     */
    public boolean isToolBarAvailable() {
        return this.toolBarAvailable;
    }

    /**
     * Setter for property toolBarAvailable.
     *
     * @param  toolBarAvailable  New value of property toolBarAvailable.
     */
    public void setToolBarAvailable(final boolean toolBarAvailable) {
        this.toolBarAvailable = toolBarAvailable;
    }

    /**
     * Getter for property floatingEventsEnabled.
     *
     * @return  Value of property floatingEventsEnabled.
     */
    public boolean isFloatingEventsEnabled() {
        return this.floatingEventsEnabled;
    }

    /**
     * Setter for property floatingEventsEnabled.
     *
     * @param  floatingEventsEnabled  New value of property floatingEventsEnabled.
     */
    public void setFloatingEventsEnabled(final boolean floatingEventsEnabled) {
        this.floatingEventsEnabled = floatingEventsEnabled;
    }

    /**
     * Getter for property menuBarAvailable.
     *
     * @return  Value of property menuBarAvailable.
     */
    public boolean isMenuBarAvailable() {
        return this.menuBarAvailable;
    }

    /**
     * Setter for property menuBarAvailable.
     *
     * @param  menuBarAvailable  New value of property menuBarAvailable.
     */
    public void setMenuBarAvailable(final boolean menuBarAvailable) {
        this.menuBarAvailable = menuBarAvailable;
    }
}
