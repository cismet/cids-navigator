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
public abstract class AbstractEmbeddedComponentsMap extends HashMap implements EmbeddedComponentsMap {

    //~ Instance fields --------------------------------------------------------

    protected Logger logger = Logger.getLogger(AbstractEmbeddedComponentsMap.class);

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of AbstractEmbeddedComponentsMap.
     */
    public AbstractEmbeddedComponentsMap() {
        super();
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public synchronized void add(final EmbeddedComponent component) {
        if (logger.isDebugEnabled()) {
            logger.debug("adding new component '" + component.getName() + "' : '" + component.getId() + "' ("
                        + component.getClass().getName() + ")"); // NOI18N
        }

        if (!this.isAvailable(component.getId())) {
            this.put(component.getId(), component);

            if (SwingUtilities.isEventDispatchThread()) {
                doAdd(component);
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("add(): synchronizing method"); // NOI18N
                }
                SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            doAdd(component);
                        }
                    });
            }
        } else {
            logger.warn("add(): component '" + component.getId() + "' already in map"); // NOI18N
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  component  DOCUMENT ME!
     */
    protected abstract void doAdd(EmbeddedComponent component);

    /**
     * Getter for property name.
     *
     * @param   id  DOCUMENT ME!
     *
     * @return  Value of property name.
     */
    @Override
    public String getName(final String id) {
        if (isAvailable(id)) {
            return this.get(id).getName();
        } else {
            logger.warn("getName(): component '" + id + "' not found"); // NOI18N
            return null;
        }
    }

    /**
     * Getter for property enabled.
     *
     * @param   id  DOCUMENT ME!
     *
     * @return  Value of property enabled.
     */
    @Override
    public boolean isEnabled(final String id) {
        if (isAvailable(id)) {
            return this.get(id).isEnabled();
        } else {
            logger.warn("isEnabled(): component '" + id + "' not found"); // NOI18N
            return false;
        }
    }

    /**
     * Getter for property visible.
     *
     * @param   id  DOCUMENT ME!
     *
     * @return  Value of property visible.
     */
    @Override
    public boolean isVisible(final String id) {
        if (isAvailable(id)) {
            return this.get(id).isVisible();
        } else {
            logger.warn("isVisible(): component '" + id + "' not found"); // NOI18N
            return false;
        }
    }

    @Override
    public synchronized void remove(final String id) {
        if (logger.isDebugEnabled()) {
            logger.debug("removing component '" + id + "'"); // NOI18N
        }

        if (this.isAvailable(id)) {
            final EmbeddedComponent component = (EmbeddedComponent)super.remove(id);
            if (SwingUtilities.isEventDispatchThread()) {
                doRemove(component);
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("remove(): synchronizing method"); // NOI18N
                }
                SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            doRemove(component);
                        }
                    });
            }
        } else {
            logger.warn("remove(): component '" + id + "' not found"); // NOI18N
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  component  DOCUMENT ME!
     */
    protected abstract void doRemove(EmbeddedComponent component);

    /**
     * Setter for property enabled.
     *
     * @param  id       DOCUMENT ME!
     * @param  enabled  New value of property enabled.
     */
    @Override
    public synchronized void setEnabled(final String id, final boolean enabled) {
        if (isAvailable(id)) {
            if (SwingUtilities.isEventDispatchThread()) {
                doSetEnabled(this.get(id), enabled);
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("setEnabled(): synchronizing method"); // NOI18N
                }
                SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            doSetEnabled(get(id), enabled);
                        }
                    });
            }
        } else {
            logger.warn("setEnabled(): component '" + id + "' not found"); // NOI18N
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  component  DOCUMENT ME!
     * @param  enabled    DOCUMENT ME!
     */
    protected void doSetEnabled(final EmbeddedComponent component, final boolean enabled) {
        component.setEnabled(enabled);
    }

    /**
     * Setter for property name.
     *
     * @param  id       name New value of property name.
     * @param  visible  DOCUMENT ME!
     */
    /*public synchronized void setName(final String id, final String name)
     * { if(isAvailable(id)) {     if(SwingUtilities.isEventDispatchThread())     {         doSetName(this.get(id),
     * name);     }     else     {         logger.debug("setName(): synchronizing method");
     * SwingUtilities.invokeLater(new Runnable()         {             public void run()             {
     * doSetName(get(id), name);             }         });     } } else {     logger.warn("setName(): component '" + id
     * + "' not found"); } }
     *
     * protected void doSetName(EmbeddedComponent component, String name) { component.setName(name);}*/

    /**
     * Setter for property visible.
     *
     * @param  id       DOCUMENT ME!
     * @param  visible  New value of property visible.
     */
    @Override
    public synchronized void setVisible(final String id, final boolean visible) {
        if (isAvailable(id)) {
            if (SwingUtilities.isEventDispatchThread()) {
                doSetVisible(this.get(id), visible);
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("setVisible(): synchronizing method"); // NOI18N
                }
                SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            doSetVisible(get(id), visible);
                        }
                    });
            }
        } else {
            logger.warn("setVisible(): component '" + id + "' not found"); // NOI18N
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  component  DOCUMENT ME!
     * @param  visible    DOCUMENT ME!
     */
    protected void doSetVisible(final EmbeddedComponent component, final boolean visible) {
        component.setVisible(visible);
    }

    @Override
    public EmbeddedComponent get(final String id) {
        if (logger.isDebugEnabled()) {
            logger.debug("retrieving component: '" + id + "'"); // NOI18N
        }

        if (isAvailable(id)) {
            return (EmbeddedComponent)super.get(id);
        } else {
            logger.warn("get(): component '" + id + "' not found"); // NOI18N
            return null;
        }
    }

    @Override
    public boolean isAvailable(final String id) {
        return this.containsKey(id);
    }

    @Override
    public Iterator getEmbeddedComponents() {
        return this.values().iterator();
    }
}
