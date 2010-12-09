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
public class EmbeddedToolBar extends JToolBar implements EmbeddedComponent {

    //~ Static fields/initializers ---------------------------------------------

    protected static final Logger logger = Logger.getLogger(EmbeddedToolBar.class);

    //~ Instance fields --------------------------------------------------------

    protected final HashSet enabledComponents = new HashSet();

    protected String id;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new EmbeddedToolBar object.
     *
     * @param  id  DOCUMENT ME!
     */
    public EmbeddedToolBar(final String id) {
        this.id = id;
    }

    /**
     * Creates a new EmbeddedToolBar object.
     *
     * @param  id       DOCUMENT ME!
     * @param  buttons  DOCUMENT ME!
     */
    public EmbeddedToolBar(final String id, final Collection buttons) {
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
        return id;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  id  DOCUMENT ME!
     */
    public void setId(final String id) {
        this.id = id;
        ;
    }

    @Override
    public void setEnabled(final boolean enabled) {
        if (this.isEnabled() != enabled) {
            final Component[] components = this.getComponents();
            if ((components != null) && (components.length > 0)) {
                ;
            }
            {
                // disablen: status merken
                if (!enabled) {
                    this.enabledComponents.clear();
                    for (int i = 0; i < components.length; i++) {
                        if (components[i].isEnabled()) {
                            enabledComponents.add(components[i]);
                        }

                        components[i].setEnabled(false);
                    }
                }
                // enablen: status setzten
                else {
                    for (int i = 0; i < components.length; i++) {
                        if (this.enabledComponents.contains(components[i])) {
                            components[i].setEnabled(true);
                        }
                    }
                }
            }

            super.setEnabled(enabled);
        }
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
            logger.debug("adding '" + components.size() + "' toolbar buttons"); // NOI18N
        }
        final Iterator iterator = components.iterator();

        while (iterator.hasNext()) {
            this.addButton(iterator.next());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  button  DOCUMENT ME!
     */
    public void addButton(final Object button) {
        if (button instanceof JComponent) {
            /*if(((JComponent)button).isEnabled())
             * { this.enabledComponents.add(button);}*/

            if (button instanceof JButton) {
                ((JButton)button).setMargin(new Insets(0, 0, 0, 0));
                this.add((JButton)button);
            } else if (button instanceof JToggleButton) {
                ((JToggleButton)button).setMargin(new Insets(0, 0, 0, 0));
                this.add((JToggleButton)button);
            } else if (button instanceof JSeparator) {
                this.addSeparator();
            } else {
                logger.warn("button type '" + button.getClass().getName()
                            + "' found, 'javax.swing.JButton' or 'javax.swing.JSeparator' preferred"); // NOI18N
                this.add((JComponent)button);
            }
        } else {
            logger.error("invalid button type '" + button.getClass().getName()
                        + "', 'javax.swing.JComponent' expected");                                     // NOI18N
        }
    }
}
