/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.plugin.ui;

import Sirius.navigator.plugin.*;
import Sirius.navigator.plugin.interfaces.*;

import java.awt.event.*;

import javax.swing.*;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class PluginToolBarButton extends JButton {

    //~ Instance fields --------------------------------------------------------

    private final PluginMethod method;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of PluginToolbarButton.
     *
     * @param  method  DOCUMENT ME!
     */
    public PluginToolBarButton(final PluginMethod method) {
        this.method = method;
        this.addActionListener(new PluginMethodInvoker());
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getId() {
        return method.getId();
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
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
                PluginToolBarButton.this.method.invoke();
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        }
    }
}
