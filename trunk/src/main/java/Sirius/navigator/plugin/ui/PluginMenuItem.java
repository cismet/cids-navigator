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

import Sirius.server.localserver.method.*;

import org.apache.log4j.*;

import java.awt.event.*;

import javax.swing.*;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class PluginMenuItem extends JMenuItem {

    //~ Instance fields --------------------------------------------------------

    protected PluginMethod pluginMethod = null;
    protected Method method = null;

    /** Holds value of property availability. */
    private long availability = 0;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new PluginMenuItem object.
     *
     * @param  availability  DOCUMENT ME!
     */
    public PluginMenuItem(final long availability) {
        this.availability = availability;
        this.addActionListener(new PluginMethodInvoker());
    }

    /**
     * Creates a new instance of PluginMenuItem.
     *
     * @param  pluginMethod  DOCUMENT ME!
     * @param  availability  DOCUMENT ME!
     */
    public PluginMenuItem(final PluginMethod pluginMethod, final long availability) {
        this(availability);
        this.pluginMethod = pluginMethod;
    }

    /**
     * Creates a new PluginMenuItem object.
     *
     * @param  pluginMethod  DOCUMENT ME!
     * @param  method        DOCUMENT ME!
     */
    public PluginMenuItem(final PluginMethod pluginMethod, final Method method) {
        this.pluginMethod = pluginMethod;
        this.method = method;

        this.availability = MethodManager.PURE_NODE + MethodManager.OBJECT_NODE + MethodManager.CLASS_NODE;
        if (method.isMultiple()) {
            this.availability += MethodManager.MULTIPLE;
        }
        if (method.isClassMultiple()) {
            this.availability += MethodManager.CLASS_MULTIPLE;
        }

        this.addActionListener(new PluginMethodInvoker());
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getId() {
        return this.pluginMethod.getId();
    }

    /**
     * Getter for property availability.
     *
     * @return  Value of property availability.
     */
    public long getAvailability() {
        return this.availability;
    }

    /**
     * Getter for property method.
     *
     * @return  Value of property method.
     */
    public Sirius.server.localserver.method.Method getMethod() {
        return this.method;
    }

    /**
     * Setter for property method.
     *
     * @param  method  New value of property method.
     */
    public void setMethod(final Sirius.server.localserver.method.Method method) {
        this.method = method;
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * public void setVisible(long availability) { if((this.availability & availability) > 0 ) { this.setVisible(true);
     * } else { this.setVisible(false); } }.
     *
     * @version  $Revision$, $Date$
     */
    private class PluginMethodInvoker implements ActionListener {

        //~ Methods ------------------------------------------------------------

        /**
         * Invoked when an method occurs.
         *
         * @param  e  DOCUMENT ME!
         */
        @Override
        public void actionPerformed(final ActionEvent e) {
            try {
                if (Logger.getLogger(this.getClass()).isDebugEnabled()) {
                    Logger.getLogger(this.getClass()).debug("invoking method " + PluginMenuItem.this.getName()); // NOI18N
                }
                PluginMenuItem.this.pluginMethod.invoke();
            } catch (Throwable t) {
                Logger.getLogger(this.getClass())
                        .error("invocation of plugin method '" + PluginMenuItem.this.getId() + "' failed", t);   // NOI18N

                // XXX i18n
                JOptionPane.showMessageDialog(
                    PluginMenuItem.this,
                    org.openide.util.NbBundle.getMessage(
                        PluginMenuItem.class,
                        "PluginMenuItem.PluginMethodInvoker.actionPerformed(ActionEvent).JOptionPane_anon.message",
                        t.getMessage()),                                                                           // NOI18N
                    org.openide.util.NbBundle.getMessage(
                        PluginMenuItem.class,
                        "PluginMenuItem.PluginMethodInvoker.actionPerformed(ActionEvent).JOptionPane_anon.title"), // NOI18N
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
