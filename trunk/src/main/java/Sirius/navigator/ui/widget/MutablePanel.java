/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui.widget;

/*******************************************************************************
 *
 * Copyright (c)        :       EIG (Environmental Informatics Group)
 * http://www.htw-saarland.de/eig
 * Prof. Dr. Reiner Guettler
 * Prof. Dr. Ralf Denzer
 *
 * HTWdS
 * Hochschule fuer Technik und Wirtschaft des Saarlandes
 * Goebenstr. 40
 * 66117 Saarbruecken
 * Germany
 *
 * Programmers          :       Pascal
 *
 * Project                      :       WuNDA 2
 * Filename             :
 * Version                      :       1.0
 * Purpose                      :
 * Created                      :       01.10.1999
 * History                      :
 *
 *******************************************************************************/

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class MutablePanel extends JPanel {

    //~ Instance fields --------------------------------------------------------

    // public final static String ENABLED = "enabledPanel";
    // public final static String DISABLED = "disabledPanel";

    // protected boolean disabled = false;

    protected JPanel enabledPanel;
    protected JPanel disabledPanel;
    protected JLabel disabledLabel;

    //~ Constructors -----------------------------------------------------------

    /**
     * //_TA_protected String disabledString = new String("Diese Widget ist z.Z. nicht anzeigbar"); protected String
     * disabledString = new String(StringLoader.getString("STL@widgetNotAvailable"));
     *
     * @param  enabledPanel  DOCUMENT ME!
     */
    public MutablePanel(final JPanel enabledPanel) {
        this(enabledPanel, null);
    }

    /**
     * Creates a new MutablePanel object.
     *
     * @param  enabledPanel     DOCUMENT ME!
     * @param  disabledMessage  DOCUMENT ME!
     */
    public MutablePanel(final JPanel enabledPanel, final String disabledMessage) {
        super(new CardLayout());
        this.enabledPanel = enabledPanel;

        disabledLabel = new JLabel(disabledMessage);
        disabledLabel.setVerticalAlignment(JLabel.CENTER);
        disabledLabel.setHorizontalAlignment(JLabel.CENTER);
        disabledLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
        disabledPanel = new JPanel(new BorderLayout());
        disabledPanel.add(disabledLabel, BorderLayout.CENTER);

        this.setEnabledPanel(enabledPanel);
        this.setDisabledPanel(disabledPanel);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void setEnabled(final boolean enabled) {
        if (SwingUtilities.isEventDispatchThread()) {
            super.setEnabled(enabled);
            if (enabled) {
                ((CardLayout)this.getLayout()).show(this, "enabledPanel");  // NOI18N
            } else {
                ((CardLayout)this.getLayout()).show(this, "disabledPanel"); // NOI18N
            }
        } else {
            SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        MutablePanel.this.setEnabled(enabled);
                    }
                });
        }
    }

    /**
     * Getter for property enabledPanel.
     *
     * @return  Value of property enabledPanel.
     */
    public JPanel getEnabledPanel() {
        return this.enabledPanel;
    }

    /**
     * Setter for property enabledPanel.
     *
     * @param  enabledPanel  New value of property enabledPanel.
     */
    public void setEnabledPanel(final JPanel enabledPanel) {
        this.remove(this.enabledPanel);
        this.add(enabledPanel, "enabledPanel"); // NOI18N
        this.enabledPanel = enabledPanel;
    }

    /**
     * Getter for property disabledPanel.
     *
     * @return  Value of property disabledPanel.
     */
    public JPanel getDisabledPanel() {
        return this.disabledPanel;
    }

    /**
     * Setter for property disabledPanel.
     *
     * @param  disabledPanel  New value of property disabledPanel.
     */
    public void setDisabledPanel(final JPanel disabledPanel) {
        this.remove(this.disabledPanel);
        this.add(disabledPanel, "disabledPanel"); // NOI18N
        this.disabledPanel = disabledPanel;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  message  DOCUMENT ME!
     */
    public void setDisabledMessage(final String message) {
        this.disabledLabel.setText(message);
    }
}
