/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui.embedded;

import org.apache.log4j.Logger;

import java.util.*;

import javax.swing.*;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public abstract class EmbeddedContainer implements EmbeddedComponent {

    //~ Static fields/initializers ---------------------------------------------

    protected static final Logger logger = Logger.getLogger(EmbeddedContainer.class);

    //~ Instance fields --------------------------------------------------------

    private final String id;
    private final Collection components;
    private boolean enabled = false;
    private boolean visible = false;

    /** Holds value of property name. */
    private String name;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new EmbeddedContainer object.
     *
     * @param  id          DOCUMENT ME!
     * @param  components  DOCUMENT ME!
     */
    public EmbeddedContainer(final String id, final Collection components) {
        this.id = id;
        this.components = components;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    protected abstract void addComponents();

    /**
     * DOCUMENT ME!
     */
    protected abstract void removeComponents();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected ComponentIterator iterator() {
        return new ComponentIterator(components.iterator());
    }

    @Override
    public void setEnabled(final boolean enabled) {
        final ComponentIterator iterator = this.iterator();
        while (iterator.hasNext()) {
            final JComponent component = iterator.next();
            if (component != null) {
                component.setEnabled(enabled);
            }
        }

        this.enabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public void setVisible(final boolean visible) {
        if (this.isVisible() != visible) {
            final ComponentIterator iterator = this.iterator();
            while (iterator.hasNext()) {
                final JComponent component = iterator.next();
                if (component != null) {
                    component.setVisible(visible);
                }
            }

            this.visible = visible;
        } else {
            this.logger.warn("unexpected call to 'setVisible()': '" + visible + "'"); // NOI18N
        }
    }

    @Override
    public boolean isVisible() {
        return this.visible;
    }

    /**
     * Getter for property name.
     *
     * @return  Value of property name.
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Setter for property name.
     *
     * @param  name  New value of property name.
     */
    @Override
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Getter for property id.
     *
     * @return  Value of property id.
     */
    @Override
    public String getId() {
        return this.id;
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * Setter for property id.
     *
     * @param    id  New value of property id.
     *
     * @version  $Revision$, $Date$
     */
    /*public void setId(String id)
     * {  this.id = id; }*/

    protected final class ComponentIterator {

        //~ Instance fields ----------------------------------------------------

        Iterator iterator;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new ComponentIterator object.
         *
         * @param  iterator  DOCUMENT ME!
         */
        private ComponentIterator(final Iterator iterator) {
            this.iterator = iterator;
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public boolean hasNext() {
            return iterator.hasNext();
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public JComponent next() {
            final Object next = iterator.next();

            if (JComponent.class.isAssignableFrom(next.getClass())) {
                return (JComponent)next;
            } else {
                logger.error("object '" + next + "' is not of type 'javax.swing.JComponent' but '"
                            + next.getClass().getName() + "'"); // NOI18N
                iterator.remove();
                return null;
            }
        }
    }
}
