/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui.embedded;

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
public class EmbeddedMenu extends JMenu implements EmbeddedComponent {

    //~ Static fields/initializers ---------------------------------------------

    protected static final Logger logger = Logger.getLogger(EmbeddedMenu.class);

    //~ Instance fields --------------------------------------------------------

    protected final String id;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new EmbeddedMenu object.
     *
     * @param  id  DOCUMENT ME!
     */
    public EmbeddedMenu(final String id) {
        this.id = id;
    }

    /**
     * Creates a new EmbeddedMenu object.
     *
     * @param  id       DOCUMENT ME!
     * @param  buttons  DOCUMENT ME!
     */
    public EmbeddedMenu(final String id, final Collection buttons) {
        this.id = id;
        this.add(buttons);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Getter for property id.
     *
     * @return  Value of property id.
     */
    @Override
    public String getId() {
        return this.id;
    }

    /**
     * Setter for property id.
     *
     * @param  enabled  id New value of property id.
     */
    /*public void setId(String id)
     * { this.id = id;}*/

    @Override
    public void setEnabled(final boolean enabled) {
        final Component[] components = this.getComponents();
        if ((components != null) && (components.length > 0)) {
            ;
        }
        for (int i = 0; i < components.length; i++) {
            components[i].setEnabled(enabled);
        }

        super.setEnabled(enabled);
    }

    @Override
    public void setVisible(final boolean visible) {
        final Component[] components = this.getComponents();
        if ((components != null) && (components.length > 0)) {
            ;
        }
        for (int i = 0; i < components.length; i++) {
            components[i].setVisible(visible);
        }

        super.setVisible(visible);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  components  DOCUMENT ME!
     */
    protected void add(final Collection components) {
        if (logger.isDebugEnabled()) {
            logger.debug("adding '" + components.size() + "' menu items"); // NOI18N
        }
        final Iterator iterator = components.iterator();

        while (iterator.hasNext()) {
            this.addItem(iterator.next());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  item  DOCUMENT ME!
     */
    public void addItem(final Object item) {
        if (item instanceof JComponent) {
            if (item instanceof JMenuItem) {
                this.add((JMenuItem)item);
            } else if (item instanceof JSeparator) {
                this.addSeparator();
            } else {
                logger.warn("item type '" + item.getClass().getName()
                            + "' found, 'javax.swing.JMenuItem' or 'javax.swing.JSeparator' preferred"); // NOI18N
                this.add((JComponent)item);
            }
        } else {
            logger.error("invalid item type '" + item.getClass().getName() + "', 'javax.swing.JComponent' expected"); // NOI18N
        }
    }
}
