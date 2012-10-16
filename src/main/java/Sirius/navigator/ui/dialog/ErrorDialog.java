/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui.dialog;

/*******************************************************************************

        Copyright (c)   :       EIG (Environmental Informatics Group)
                                                http://www.htw-saarland.de/eig
                                                Prof. Dr. Reiner Guettler
                                                Prof. Dr. Ralf Denzer

                                                HTWdS
                                                Hochschule fuer Technik und Wirtschaft des Saarlandes
                                                Goebenstr. 40
                                                66117 Saarbruecken
                                                Germany

        Programmers             :       Pascal

        Project                 :       WuNDA 2
        Filename                :
        Version                 :       1.0
        Purpose                 :
        Created                 :       05.07.2000
        History                 :

*******************************************************************************/
import Sirius.navigator.ui.ComponentRegistry;

import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

import de.cismet.tools.gui.StaticSwingTools;
//import Sirius.navigator.connection.ConnectionHandler;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class ErrorDialog extends JDialog implements ActionListener {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger logger = Logger.getLogger(ErrorDialog.class);
    // _TA_public final static String WARNING = "Warnung";
    public static final String WARNING = org.openide.util.NbBundle.getMessage(ErrorDialog.class, "ErrorDialog.WARNING"); // NOI18N

    // _TA_public final static String ERROR = "Kritischer Fehler";
    public static final String ERROR = org.openide.util.NbBundle.getMessage(ErrorDialog.class, "ErrorDialog.ERROR"); // NOI18N

    //~ Instance fields --------------------------------------------------------

    // _TA_protected String errorMessage = "Es ist ein kritischer Fehler aufgetreten";

    // NOI18N

    // _TA_protected String errorMessage = "Es ist ein kritischer Fehler aufgetreten";
    protected String errorMessage = org.openide.util.NbBundle.getMessage(ErrorDialog.class, "ErrorDialog.errorMessage"); // NOI18N
    protected String stackTrace = null;
    protected String errorType = ERROR;

    protected JLabel errorLabel;
    protected JPanel detailsPanel;
    protected JTextArea detailsTextArea;
    protected JButton buttonIgnore;
    protected JButton buttonExit;
    protected JButton buttonDetails;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ErrorDialog object.
     */
    public ErrorDialog() {
        // _TA_super(new JFrame(), "Kritischer Fehler", true);
        super(new JFrame(), ERROR, true);
        initErrorDialog();
    }

    /**
     * Creates a new ErrorDialog object.
     *
     * @param  errorMessage  DOCUMENT ME!
     * @param  errorType     DOCUMENT ME!
     */
    public ErrorDialog(final String errorMessage, final String errorType) {
        super(new JFrame(), errorType, true);
        this.errorMessage = errorMessage;
        this.setErrorType(errorType);
        initErrorDialog();
    }

    /**
     * Creates a new ErrorDialog object.
     *
     * @param  errorMessage  DOCUMENT ME!
     * @param  stackTrace    DOCUMENT ME!
     * @param  errorType     DOCUMENT ME!
     */
    public ErrorDialog(final String errorMessage, final String stackTrace, final String errorType) {
        super(ComponentRegistry.isRegistred() ? ComponentRegistry.getRegistry().getMainWindow() : new JFrame(),
            errorType,
            true);
        this.errorMessage = errorMessage;
        this.stackTrace = stackTrace;
        this.setErrorType(errorType);
        initErrorDialog();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    protected void initErrorDialog() {
        // this.setLocationRelativeTo(this.getParent());
        this.setResizable(false);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        final JPanel contentPane = new JPanel(new GridBagLayout());
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        final GridBagConstraints constraints = new GridBagConstraints();

        // ICON ================================================================
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridheight = 1;
        constraints.gridwidth = 1;
        constraints.weightx = 0.5;
        constraints.weighty = 0.0;
        constraints.gridy = 0;
        constraints.gridx = 0;

        JLabel errorIcon;
        if (errorType.equals(ERROR)) {
            errorIcon = new JLabel(UIManager.getIcon("OptionPane.errorIcon"));   // NOI18N
        } else {
            errorIcon = new JLabel(UIManager.getIcon("OptionPane.warningIcon")); // NOI18N
        }

        errorIcon.setBorder(new CompoundBorder(
                new SoftBevelBorder(SoftBevelBorder.LOWERED),
                new EmptyBorder(10, 10, 10, 10)));
        contentPane.add(errorIcon, constraints);

        // MESSAGE =============================================================
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 0.5;
        constraints.gridx++;
        errorLabel = new JLabel(errorMessage);
        errorLabel.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPane.add(errorLabel, constraints);

        // BUTTONS =============================================================
        constraints.insets = new Insets(20, 0, 10, 0);
        constraints.gridwidth = 2;
        constraints.gridy = 1;
        constraints.gridx = 0;
        final JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));

        // _TA_buttonIgnore = new JButton("Ignorieren");
        buttonIgnore = new JButton(org.openide.util.NbBundle.getMessage(
                    ErrorDialog.class,
                    "ErrorDialog.buttonIgnore.title")); // NOI18N
        // _TA_buttonIgnore.setMnemonic('I');
        buttonIgnore.setMnemonic(org.openide.util.NbBundle.getMessage(
                ErrorDialog.class,
                "ErrorDialog.buttonIgnore.mnemonic").charAt(0)); // NOI18N
        buttonPanel.add(buttonIgnore);

        // _TA_buttonExit = new JButton("Beenden");
        buttonExit = new JButton(org.openide.util.NbBundle.getMessage(
                    ErrorDialog.class,
                    "ErrorDialog.buttonExit.title")); // NOI18N
        // _TA_buttonExit.setMnemonic('B');
        buttonExit.setMnemonic(org.openide.util.NbBundle.getMessage(
                ErrorDialog.class,
                "ErrorDialog.buttonExit.mnemonic").charAt(0)); // NOI18N
        buttonExit.setActionCommand("exit");                   // NOI18N
        buttonExit.addActionListener(this);
        buttonPanel.add(buttonExit);

        if (errorType.equals(WARNING)) {
            buttonIgnore.setActionCommand("ignore"); // NOI18N
            buttonIgnore.addActionListener(this);
        } else {
            buttonIgnore.setEnabled(false);
        }

        // _TA_buttonDetails = new JButton("Details");
        buttonDetails = new JButton(org.openide.util.NbBundle.getMessage(
                    ErrorDialog.class,
                    "ErrorDialog.buttonDetails.title")); // NOI18N
        // _TA_buttonDetails.setMnemonic('D');
        buttonDetails.setMnemonic(org.openide.util.NbBundle.getMessage(
                ErrorDialog.class,
                "ErrorDialog.buttonDetails.mnemonic").charAt(0)); // NOI18N
        buttonPanel.add(buttonDetails);

        contentPane.add(buttonPanel, constraints);

        // DETAILS =============================================================
        if (stackTrace != null) {
            buttonDetails.setActionCommand("details"); // NOI18N
            buttonDetails.addActionListener(this);

            constraints.insets = new Insets(0, 0, 0, 0);
            constraints.gridy++;
            constraints.weightx = 1.0;
            constraints.weighty = 1.0;
            detailsTextArea = new JTextArea(stackTrace, 4, 20);
            detailsPanel = new JPanel(new GridLayout(1, 1));
            // detailsPanel.setBorder(new EmptyBorder(10,10,10,10));
            detailsPanel.add(new JScrollPane(detailsTextArea));
            detailsPanel.setVisible(false);
            contentPane.add(detailsPanel, constraints);
        } else {
            buttonDetails.setEnabled(false);
        }

        this.setContentPane(contentPane);
        this.pack();

        Sirius.navigator.tools.MetaToolkit.centerWindow(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  errorType  DOCUMENT ME!
     */
    protected void setErrorType(final String errorType) {
        if (errorType.equals(WARNING) || errorType.equals(ERROR)) {
            this.errorType = errorType;
        } else {
            this.errorType = ERROR;
        }

        this.setTitle(errorType);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        if (e.getActionCommand().equals("exit")) // NOI18N
        {
            if (errorType.equals(ERROR)) {
                System.exit(1);
            } else {
                /*
                 * _TA_JOptionPane optionPane = new JOptionPane("<html><center><p>Moechten Sie den</p><p>Navigator
                 * wirklich schliessen?</p></center></html>", JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION,
                 * null, new String[]{"Ja", "Nein"}, null); _TA_JDialog dialog = optionPane.createDialog(this, "Programm
                 * beenden");
                 */
                final JOptionPane optionPane = new JOptionPane(
                        org.openide.util.NbBundle.getMessage(
                            ErrorDialog.class,
                            "ErrorDialog.actionPerformed(ActionEvent).optionPane.message"), // NOI18N
                        JOptionPane.QUESTION_MESSAGE,
                        JOptionPane.YES_NO_OPTION,
                        null,
                        new String[] {
                            org.openide.util.NbBundle.getMessage(
                                ErrorDialog.class,
                                "ErrorDialog.actionPerformed(ActionEvent).optionPane.yes"), // NOI18N
                            org.openide.util.NbBundle.getMessage(
                                ErrorDialog.class,
                                "ErrorDialog.actionPerformed(ActionEvent).optionPane.no")
                        },                                                                  // NOI18N
                        null);

                final JDialog dialog = optionPane.createDialog(
                        this,
                        org.openide.util.NbBundle.getMessage(
                            ErrorDialog.class,
                            "ErrorDialog.actionPerformed(ActionEvent).dialog.title")); // NOI18N
                StaticSwingTools.showDialog(dialog);

                // _TA_if(optionPane.getValue().equals("Ja"))
                if (optionPane.getValue().equals(
                                org.openide.util.NbBundle.getMessage(
                                    ErrorDialog.class,
                                    "ErrorDialog.actionPerformed(ActionEvent).optionPane.yes"))) // NOI18N
                {
                    if (logger.isDebugEnabled()) {
                        logger.debug("<NAV> Navigator closed()");                                // NOI18N
                    }
                    System.exit(1);
                }
            }
        } else if (e.getActionCommand().equals("ignore"))                                        // NOI18N
        {
            this.dispose();
        } else if (e.getActionCommand().equals("details"))                                       // NOI18N
        {
            buttonDetails.setEnabled(false);
            detailsPanel.setVisible(true);
            this.pack();
        }
    }
}
