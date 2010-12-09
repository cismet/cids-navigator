/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.plugin.ui;

import Sirius.navigator.method.*;
import Sirius.navigator.plugin.*;
import Sirius.navigator.plugin.interfaces.*;
import Sirius.navigator.ui.embedded.*;

import org.apache.log4j.Logger;

import java.awt.*;

import java.util.*;

import javax.swing.*;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class PluginMenu extends EmbeddedMenu {

    //~ Instance fields --------------------------------------------------------

    /** Holds value of property hideUnavailableItems. */
    private boolean hideUnavailableItems = false;

    /** Holds value of property disableUnavailableItems. */
    private boolean disableUnavailableItems = true;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new PluginMenu object.
     *
     * @param  id  DOCUMENT ME!
     */
    public PluginMenu(final String id) {
        super(id);
    }

    /**
     * Creates a new PluginMenu object.
     *
     * @param  id    DOCUMENT ME!
     * @param  name  DOCUMENT ME!
     */
    public PluginMenu(final String id, final String name) {
        this(id);
        this.setText(name);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Getter for property hideUnavailableItems.
     *
     * @return  Value of property hideUnavailableItems.
     */
    public boolean isHideUnavailableItems() {
        return this.hideUnavailableItems;
    }

    /**
     * Setter for property hideUnavailableItems.
     *
     * @param  hideUnavailableItems  New value of property hideUnavailableItems.
     */
    public void setHideUnavailableItems(final boolean hideUnavailableItems) {
        this.hideUnavailableItems = hideUnavailableItems;
    }

    /**
     * Getter for property disableUnavailableItems.
     *
     * @return  Value of property disableUnavailableItems.
     */
    public boolean isDisableUnavailableItems() {
        return this.disableUnavailableItems;
    }

    /**
     * Setter for property disableUnavailableItems.
     *
     * @param  disableUnavailableItems  New value of property disableUnavailableItems.
     */
    public void setDisableUnavailableItems(final boolean disableUnavailableItems) {
        this.disableUnavailableItems = disableUnavailableItems;
    }

    /**
     * Setter for property availability.
     *
     * @param  methodAvailability  New value of property availability.
     */
    public void setAvailability(final MethodAvailability methodAvailability) {
        if (logger.isDebugEnabled()) {
            logger.debug("setting plugin menu items availability '" + methodAvailability.getAvailability() + "' of '"
                        + this.getMenuComponentCount() + "' components"); // NOI18N
        }
        final Component[] components = this.getMenuComponents();

        for (int i = 0; i < components.length; i++) {
            if (components[i] instanceof PluginMenuItem) {
                final PluginMenuItem pluginMenuItem = (PluginMenuItem)components[i];
                // if(logger.isDebugEnabled())logger.debug("changing availability '" + pluginMenuItem.getAvailability()
                // + "' of '" + pluginMenuItem.getText() + "' to '" + availability + "': '"  +
                // (pluginMenuItem.getAvailability() & availability) + "'");

                boolean available = (pluginMenuItem.getAvailability() & methodAvailability.getAvailability()) > 0;
                if (logger.isDebugEnabled()) {
                    logger.debug(pluginMenuItem.getText() + " is available: " + available + " ("
                                + pluginMenuItem.getAvailability() + " & " + methodAvailability.getAvailability()
                                + " > 0)"); // NOI18N
                }

                if ((pluginMenuItem.getMethod() != null) && available) {
                    available = methodAvailability.containsClasses(pluginMenuItem.getMethod().getClassKeys());
                    if (logger.isDebugEnabled()) {
                        logger.debug(pluginMenuItem.getText() + " is available for selected classes: " + available); // NOI18N
                    }

                    Iterator iterator = methodAvailability.getClassKeys().iterator();
                    while (iterator.hasNext()) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("class key of selected nodes: " + iterator.next()); // NOI18N
                        }
                    }

                    iterator = pluginMenuItem.getMethod().getClassKeys().iterator();
                    while (iterator.hasNext()) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("class key of selected method: " + iterator.next()); // NOI18N
                        }
                    }
                }

                if (disableUnavailableItems) {
                    pluginMenuItem.setEnabled(available);
                } else if (hideUnavailableItems) {
                    pluginMenuItem.setVisible(available);
                }
            } else if (!(components[i] instanceof JSeparator)) {
                logger.warn("could not set availability of component '" + components[i].getClass().getName() + "'"); // NOI18N
            }
        }
    }
}
