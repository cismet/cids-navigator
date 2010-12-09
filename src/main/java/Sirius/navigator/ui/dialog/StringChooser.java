/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui.dialog;

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
import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.event.*;

import java.util.*;

import javax.swing.*;
import javax.swing.border.*;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class StringChooser extends JDialog // implements ActionListener
{

    //~ Static fields/initializers ---------------------------------------------

    protected static final Logger logger = Logger.getLogger(StringChooser.class);

    //~ Instance fields --------------------------------------------------------

    protected JList stringList;
    protected JButton buttonAccept;
    protected JButton buttonCancel;

    protected String infoMessage = null;
    // protected String naMessage = null;

    protected String selectedString = null;
    protected boolean accepted = false;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new StringChooser object.
     *
     * @param  owner  DOCUMENT ME!
     * @param  title  DOCUMENT ME!
     */
    public StringChooser(final JFrame owner, final String title) {
        super(owner, title, true);
        init();
    }

    /**
     * Creates a new StringChooser object.
     *
     * @param  owner  DOCUMENT ME!
     * @param  title  DOCUMENT ME!
     */
    public StringChooser(final JDialog owner, final String title) {
        super(owner, title, true);
        init();
    }

    /**
     * Creates a new StringChooser object.
     *
     * @param  owner        DOCUMENT ME!
     * @param  title        DOCUMENT ME!
     * @param  infoMessage  DOCUMENT ME!
     */
    public StringChooser(final JDialog owner, final String title, final String infoMessage) {
        super(owner, title, true);
        this.infoMessage = infoMessage;
        // this.naMessage = naMessage;
        init();
    }

    /**
     * Creates a new StringChooser object.
     *
     * @param       owner        DOCUMENT ME!
     * @param       title        DOCUMENT ME!
     * @param       infoMessage  DOCUMENT ME!
     * @param       naMessage    DOCUMENT ME!
     *
     * @deprecated  naMessage not used anymore
     */
    public StringChooser(final JFrame owner, final String title, final String infoMessage, final String naMessage) {
        super(owner, title, true);
        this.infoMessage = infoMessage;
        // this.naMessage = naMessage;
        init();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    protected void init() {
        final ActionListener actionListener = new ButtonListener();

        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        final JPanel contentPane = new JPanel(new GridBagLayout());
        contentPane.setBorder(new EmptyBorder(10, 10, 8, 10));
        final GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridheight = 1;
        gbc.gridwidth = 2;
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weightx = 0.1;
        gbc.weighty = 0.1;
        final JLabel infoLabel = new JLabel(infoMessage);
        infoLabel.setVerticalAlignment(JLabel.CENTER);
        infoLabel.setHorizontalAlignment(JLabel.CENTER);
        infoLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.add(infoLabel, gbc);

        gbc.insets = new Insets(0, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridy++;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        stringList = new JList();
        stringList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        contentPane.add(new JScrollPane(stringList), gbc);

        gbc.insets = new Insets(0, 0, 0, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.weightx = 0.1;
        gbc.weighty = 0.1;
        // _TA_buttonAccept = new JButton("Uebernehmen");
        // buttonAccept = new JButton(StringLoader.getString("STL@takeOn"));
        buttonAccept = new JButton(org.openide.util.NbBundle.getMessage(
                    StringChooser.class,
                    "StringChooser.buttonAccept.text"));           // NOI18N
        buttonAccept.setMnemonic(org.openide.util.NbBundle.getMessage(
                StringChooser.class,
                "StringChooser.buttonAccept.mnemonic").charAt(0)); // NOI18N
        buttonAccept.setToolTipText(org.openide.util.NbBundle.getMessage(
                StringChooser.class,
                "StringChooser.buttonAccept.tooltip"));            // NOI18N
        buttonAccept.setActionCommand("accept");                   // NOI18N
        buttonAccept.addActionListener(actionListener);
        contentPane.add(buttonAccept, gbc);

        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.gridx++;
        // _TA_buttonCancel = new JButton("Abbrechen");
        // buttonCancel = new JButton(StringLoader.getString("STL@cancel"));
        buttonCancel = new JButton(org.openide.util.NbBundle.getMessage(
                    StringChooser.class,
                    "StringChooser.buttonCancel.text"));           // NOI18N
        buttonCancel.setMnemonic(org.openide.util.NbBundle.getMessage(
                StringChooser.class,
                "StringChooser.buttonCancel.mnemonic").charAt(0)); // NOI18N
        buttonCancel.setToolTipText(org.openide.util.NbBundle.getMessage(
                StringChooser.class,
                "StringChooser.buttonCancel.tooltip"));            // NOI18N
        buttonCancel.setActionCommand("cancel");                   // NOI18N
        buttonCancel.addActionListener(actionListener);
        contentPane.add(buttonCancel, gbc);

        this.setContentPane(contentPane);
        this.setSize(320, 240);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  strings         DOCUMENT ME!
     * @param  selectedString  DOCUMENT ME!
     */
    public void show(final String[] strings, final String selectedString) {
        this.show(strings);
        this.setSelectedString(selectedString);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  strings  DOCUMENT ME!
     */
    public void show(final String[] strings) {
        if ((strings != null) && (strings.length > 0)) {
            stringList.setListData(strings);
            stringList.setSelectedIndex(-1);
        } else {
            stringList.removeAll();
            // stringList.setListData(new String[]{naMessage});
        }

        super.show();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  strings         DOCUMENT ME!
     * @param  selectedString  DOCUMENT ME!
     */
    public void show(final Collection strings, final String selectedString) {
        this.show(strings);
        this.setSelectedString(selectedString);
        // this.show((String[])strings.toArray(new String[strings.size()]), selectedString);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  strings  DOCUMENT ME!
     */
    public void show(final Collection strings) {
        stringList.setListData(new Vector(strings));
        stringList.setSelectedIndex(-1);
        super.show();
        // this.show((String[])strings.toArray(new String[strings.size()]));
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isSelectionAccepted() {
        if (selectedString == null) // || selectedString.equals(naMessage))
        {
            return false;
        } else {
            return accepted;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  selectedString  DOCUMENT ME!
     */
    public void setSelectedString(final String selectedString) {
        if (logger.isDebugEnabled()) {
            logger.debug("selecting string '" + selectedString + "'");        // NOI18N
        }
        if (((DefaultListModel)stringList.getModel()).indexOf(selectedString) != -1) {
            stringList.setSelectedValue(selectedString, true);
            accepted = true;
            this.selectedString = selectedString;
        } else {
            accepted = false;
            this.selectedString = null;
            logger.warn("string '" + selectedString + "' not found in list"); // NOI18N
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getSelectedString() {
        /*if(selectedString.equals(naMessage))
         * { logger.warn("unavailable string '" + naMessage + "' selected, returning 'null'"); return null; } else {
         * return selectedString;}*/

        return selectedString;
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    protected class ButtonListener implements ActionListener {

        //~ Methods ------------------------------------------------------------

        @Override
        public void actionPerformed(final ActionEvent e) {
            if (e.getActionCommand().equals("accept")) // NOI18N
            {
                if (!stringList.isSelectionEmpty())    // && (String)stringList.getSelectedValue() != naMessage)
                {
                    accepted = true;
                    selectedString = stringList.getSelectedValue().toString().trim();
                    // NavigatorLogger.printMessage(selectedString);
                } else {
                    // if(logger.isDebugEnabled())logger.debug("unavailable string '" + naMessage + "' or nothing
                    // selected");
                    if (logger.isDebugEnabled()) {
                        logger.debug("nothing selected"); // NOI18N
                    }
                    accepted = false;
                    selectedString = null;
                }

                dispose();
            } else if (e.getActionCommand().equals("cancel")) // NOI18N
            {
                accepted = false;
                selectedString = null;
                dispose();
            }
        }
    }
}
