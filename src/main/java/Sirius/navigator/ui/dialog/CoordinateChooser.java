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
import Sirius.navigator.tools.InputValidator;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class CoordinateChooser extends JDialog implements ActionListener {

    //~ Instance fields --------------------------------------------------------

    protected int[] coordinates = new int[] { 0, 0, 0, 0 };
    // _TA_protected String infoString = "<html><center><p>Bitte geben Sie die Koordinaten des Interessensbereichs
    // an,</p><p>auf den Sie die Karte beschraenken wollen.</p></center></html>"; protected String infoString =
    // StringLoader.getString("STL@coordsOfInterests");

    protected boolean accepted = false;

    protected JTextField koordinatenRW1TextField;
    protected JTextField koordinatenHW1TextField;
    protected JTextField koordinatenRW2TextField;
    protected JTextField koordinatenHW2TextField;
    protected JButton buttonAccept;
    protected JButton buttonCancel;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new CoordinateChooser object.
     */
    public CoordinateChooser() {
        // _TA_super(new JFrame(), "Interessensbereich angeben", true); super(new JFrame(),
        // StringLoader.getString("STL@interests"), true); super(new JFrame(),
        // ResourceManager.getManager().getString("dialog.coordinate.title")); initCoordinateChooser();
    }

    /**
     * Creates a new CoordinateChooser object.
     *
     * @param  parent  DOCUMENT ME!
     */
    public CoordinateChooser(final JFrame parent) {
        // _TA_super(new JFrame(), "Interessensbereich angeben", true);
        super(parent, org.openide.util.NbBundle.getMessage(CoordinateChooser.class, "CoordinateChooser.title"), true); // NOI18N

        initCoordinateChooser();
    }

    /**
     * Creates a new CoordinateChooser object.
     *
     * @param  parent  DOCUMENT ME!
     */
    public CoordinateChooser(final JDialog parent) {
        // _TA_super(new JFrame(), "Interessensbereich angeben", true);
        super(parent, org.openide.util.NbBundle.getMessage(CoordinateChooser.class, "CoordinateChooser.title"), true); // NOI18N

        initCoordinateChooser();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * public CoordinateChooser(JDialog dialog, String title, String infoString) { super(dialog, title, true);
     * this.infoString = infoString; initCoordinateChooser(); }. public CoordinateChooser(JDialog dialog, String title)
     * { super(dialog, title, true); initCoordinateChooser(); }
     */
    protected void initCoordinateChooser() {
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        final JPanel contentPane = new JPanel(new GridBagLayout());
        contentPane.setBorder(new EmptyBorder(10, 10, 8, 10));
        final GridBagConstraints gbc = new GridBagConstraints();
        final GridBagConstraints constraints = new GridBagConstraints();

        gbc.insets = new Insets(0, 0, 15, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridheight = 1;
        gbc.gridwidth = 2;
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weightx = 0.1;
        gbc.weighty = 0.1;
        final JLabel infoLabel = new JLabel(org.openide.util.NbBundle.getMessage(
                    CoordinateChooser.class,
                    "CoordinateChooser.infoLabel.text")); // NOI18N
        infoLabel.setVerticalAlignment(JLabel.CENTER);
        infoLabel.setHorizontalAlignment(JLabel.CENTER);
        infoLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.add(infoLabel, gbc);

        // KOORDINATEN =========================================================
        gbc.insets = new Insets(0, 5, 20, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridy++;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        final JPanel koordinatenPanel = new JPanel(new GridBagLayout());
        koordinatenPanel.setBorder(new EtchedBorder());
        contentPane.add(koordinatenPanel, gbc);

        // Rechtswert(1):
        constraints.insets = new Insets(5, 5, 8, 8);
        constraints.anchor = GridBagConstraints.EAST;
        constraints.fill = GridBagConstraints.VERTICAL;
        constraints.gridheight = 1;
        constraints.gridwidth = 1;
        constraints.gridy = 0;
        constraints.gridx = 0;
        constraints.weightx = 0.0;
        constraints.weighty = 0.0;
        // _TA_koordinatenPanel.add(new JLabel("Rechtswert linksunten:"), constraints);
        koordinatenPanel.add(new JLabel(
                org.openide.util.NbBundle.getMessage(
                    CoordinateChooser.class,
                    "CoordinateChooser.koordinatenPanel.rwluLabel.text")), // NOI18N
            constraints);

        // Rechtswert 1 Textfield
        constraints.insets = new Insets(5, 0, 8, 5);
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridx = 1;
        koordinatenRW1TextField = new JTextField(6);
        koordinatenRW1TextField.setDocument(new InputValidator(InputValidator.NUMERIC));
        koordinatenPanel.add(koordinatenRW1TextField, constraints);

        // Hochwert(1):
        constraints.insets = new Insets(0, 5, 8, 8);
        constraints.anchor = GridBagConstraints.EAST;
        constraints.gridx = 0;
        constraints.gridy = 1;
        // _TA_koordinatenPanel.add(new JLabel("Hochwert linksunten:"), constraints);
        koordinatenPanel.add(new JLabel(
                org.openide.util.NbBundle.getMessage(
                    CoordinateChooser.class,
                    "CoordinateChooser.koordinatenPanel.hwluLabel.text")), // NOI18N
            constraints);
        // Hochwert 1 Textfield
        constraints.insets = new Insets(0, 0, 8, 5);
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridx = 1;
        koordinatenHW1TextField = new JTextField(6);
        koordinatenHW1TextField.setDocument(new InputValidator(InputValidator.NUMERIC));
        koordinatenPanel.add(koordinatenHW1TextField, constraints);

        // Rechtswert(2):
        constraints.insets = new Insets(0, 5, 8, 8);
        constraints.anchor = GridBagConstraints.EAST;
        constraints.gridx = 0;
        constraints.gridy = 2;
        // _TA_koordinatenPanel.add(new JLabel("Rechtswert rechtsoben:"), constraints);
        koordinatenPanel.add(new JLabel(
                org.openide.util.NbBundle.getMessage(
                    CoordinateChooser.class,
                    "CoordinateChooser.koordinatenPanel.rwroLabel.text")), // NOI18N
            constraints);
        // Rechtswert 2 Textfield
        constraints.insets = new Insets(0, 0, 8, 5);
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridx = 1;
        koordinatenRW2TextField = new JTextField(6);
        koordinatenRW2TextField.setDocument(new InputValidator(InputValidator.NUMERIC));
        koordinatenPanel.add(koordinatenRW2TextField, constraints);
        // Hochwert(2):
        constraints.insets = new Insets(0, 5, 8, 8);
        constraints.anchor = GridBagConstraints.EAST;
        constraints.gridx = 0;
        constraints.gridy = 3;
        // _TA_koordinatenPanel.add(new JLabel("Hochwert rechtsoben:"), constraints);
        koordinatenPanel.add(new JLabel(
                org.openide.util.NbBundle.getMessage(
                    CoordinateChooser.class,
                    "CoordinateChooser.koordinatenPanel.hwroLabel.text")), // NOI18N
            constraints);
        // Hochwert 1 Textfield
        constraints.insets = new Insets(0, 0, 5, 5);
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridx = 1;
        koordinatenHW2TextField = new JTextField(6);
        koordinatenHW2TextField.setDocument(new InputValidator(InputValidator.NUMERIC));
        koordinatenPanel.add(koordinatenHW2TextField, constraints);

        // ======================================

        gbc.insets = new Insets(0, 0, 0, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        buttonAccept = new JButton(org.openide.util.NbBundle.getMessage(
                    CoordinateChooser.class,
                    "CoordinateChooser.buttonAccept.text")); // NOI18N
        // _TA_buttonAccept.setMnemonic('U');
        buttonAccept.setMnemonic(org.openide.util.NbBundle.getMessage(
                CoordinateChooser.class,
                "CoordinateChooser.buttonAccept.mnemonic").charAt(0)); // NOI18N
        buttonAccept.setActionCommand("accept");                       // NOI18N
        buttonAccept.addActionListener(this);
        contentPane.add(buttonAccept, gbc);

        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.gridx++;
        // _TA_buttonCancel = new JButton("Ignorieren");
        buttonCancel = new JButton(org.openide.util.NbBundle.getMessage(
                    CoordinateChooser.class,
                    "CoordinateChooser.buttonCancel.text")); // NOI18N
        // _TA_buttonCancel.setMnemonic('I');
        buttonCancel.setMnemonic(org.openide.util.NbBundle.getMessage(
                CoordinateChooser.class,
                "CoordinateChooser.buttonCancel.mnemonic").charAt(0)); // NOI18N
        buttonCancel.setActionCommand("cancel");                       // NOI18N
        buttonCancel.addActionListener(this);
        contentPane.add(buttonCancel, gbc);

        this.setContentPane(contentPane);
        this.pack();
        this.setResizable(false);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  coordinates  DOCUMENT ME!
     */
    public void show(final int[] coordinates) {
        if ((coordinates != null) && (coordinates.length == 4)) {
            this.coordinates = coordinates;
        } else {
            this.coordinates = new int[] { 0, 0, 0, 0 };
        }

        this.show();
    }

    @Override
    public void show() {
        koordinatenRW1TextField.setText(String.valueOf(this.coordinates[0]));
        koordinatenHW1TextField.setText(String.valueOf(this.coordinates[1]));
        koordinatenRW2TextField.setText(String.valueOf(this.coordinates[2]));
        koordinatenHW2TextField.setText(String.valueOf(this.coordinates[3]));

        super.show();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isCoordinateAccepted() {
        return accepted;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int[] getCoordinate() {
        return coordinates;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        if (e.getActionCommand().equals("accept")) // NOI18N
        {
            accepted = true;

            if ((koordinatenRW1TextField.getText() == null) || (koordinatenRW1TextField.getText().length() < 1)
                        || (koordinatenRW2TextField.getText() == null)
                        || (koordinatenRW2TextField.getText().length() < 1)) {
                // _TA_JOptionPane.showMessageDialog(this, "Bitte geben Sie alle Rechtswerte an.", "Fehlerhafte
                // Eingabe", JOptionPane.WARNING_MESSAGE);
                JOptionPane.showMessageDialog(
                    this,
                    org.openide.util.NbBundle.getMessage(
                        CoordinateChooser.class,
                        "CoordinateChooser.actionPerformed(ActionEvent).missingRWOptionPane.message"), // NOI18N
                    org.openide.util.NbBundle.getMessage(
                        CoordinateChooser.class,
                        "CoordinateChooser.actionPerformed(ActionEvent).missingRWOptionPane.title"), // NOI18N
                    JOptionPane.WARNING_MESSAGE);
                accepted = false;
            }

            if ((koordinatenHW1TextField.getText() == null) || (koordinatenHW1TextField.getText().length() < 1)
                        || (koordinatenHW2TextField.getText() == null)
                        || (koordinatenHW2TextField.getText().length() < 1)) {
                // _TA_JOptionPane.showMessageDialog(this, "Bitte geben Sie alle Hochwerte an.", "Fehlerhafte Eingabe",
                // JOptionPane.WARNING_MESSAGE);

                JOptionPane.showMessageDialog(
                    this,
                    org.openide.util.NbBundle.getMessage(
                        CoordinateChooser.class,
                        "CoordinateChooser.actionPerformed(ActionEvent).missingHWOptionPane.message"), // NOI18N
                    org.openide.util.NbBundle.getMessage(
                        CoordinateChooser.class,
                        "CoordinateChooser.actionPerformed(ActionEvent).missingHWOptionPane.title"), // NOI18N
                    JOptionPane.WARNING_MESSAGE);
                accepted = false;
            }

            if (accepted) {
                this.coordinates[0] = Integer.parseInt(koordinatenRW1TextField.getText());
                this.coordinates[1] = Integer.parseInt(koordinatenHW1TextField.getText());
                this.coordinates[2] = Integer.parseInt(koordinatenRW2TextField.getText());
                this.coordinates[3] = Integer.parseInt(koordinatenHW2TextField.getText());
                dispose();
            }
        } else if (e.getActionCommand().equals("cancel")) // NOI18N
        {
            accepted = false;
            dispose();
        }
    }
}
