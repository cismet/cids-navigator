/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.exception;

import Sirius.navigator.resource.PropertyManager;
import Sirius.navigator.resource.ResourceManager;
import Sirius.navigator.ui.ComponentRegistry;

import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.event.*;

import java.util.*;

import javax.swing.*;
import javax.swing.border.*;

import de.cismet.tools.gui.StaticSwingTools;

//import Sirius.navigator.NavigatorLogger;
//import Sirius.navigator.connection.ConnectionHandler;
/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class ExceptionManager {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger logger = Logger.getLogger(ExceptionManager.class);
    private static ExceptionManager manager = null;
    private static final ResourceManager resource = ResourceManager.getManager();

    public static final int WARNING = 1;
    public static final int ERROR = 2;
    public static final int FATAL = 4;

    public static final int PLUGIN_WARNING = 8;
    public static final int PLUGIN_ERROR = 16;
    public static final int PLUGIN_FATAL = 32;

    //~ Instance fields --------------------------------------------------------

    private final JOptionPane exitOption;
    private ExceptionPane exceptionPane = null;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of ExceptionManager.
     */
    private ExceptionManager() {
        if (logger.isInfoEnabled()) {
            logger.info("creating singleton exception manager instance");                                            // NOI18N
        }
        exitOption = new JOptionPane(
                org.openide.util.NbBundle.getMessage(ExceptionManager.class, "ExceptionManager.exitOption.message"), // NOI18N
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.YES_NO_OPTION,
                null,
                new String[] {
                    org.openide.util.NbBundle.getMessage(
                        ExceptionManager.class,
                        "ExceptionManager.exitOption.option.confirm"),                                               // NOI18N
                    org.openide.util.NbBundle.getMessage(
                        ExceptionManager.class,
                        "ExceptionManager.exitOption.option.cancel")
                },                                                                                                   // NOI18N
                org.openide.util.NbBundle.getMessage(
                    ExceptionManager.class,
                    "ExceptionManager.exitOption.option.confirm"));                                                  // NOI18N
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static final ExceptionManager getManager() {
        if (manager == null) {
            manager = new ExceptionManager();
        }

        return manager;
    }

    /**
     * . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
     *
     * @param  owner      DOCUMENT ME!
     * @param  exception  DOCUMENT ME!
     */
    public void showExceptionDialog(final JFrame owner, final NavigatorException exception) {
        showExceptionDialog(
            owner,
            exception.getLevel(),
            exception.getName(),
            exception.getMessage(),
            exception.getCause());
    }

    /**
     * DOCUMENT ME!
     *
     * @param  owner      DOCUMENT ME!
     * @param  exception  DOCUMENT ME!
     */
    public void showExceptionDialog(final JDialog owner, final NavigatorException exception) {
        showExceptionDialog(
            owner,
            exception.getLevel(),
            exception.getName(),
            exception.getMessage(),
            exception.getCause());
    }

    /**
     * DOCUMENT ME!
     *
     * @param  exception  DOCUMENT ME!
     */
    public void showExceptionDialog(final NavigatorException exception) {
        showExceptionDialog(exception.getLevel(), exception.getName(), exception.getMessage(), exception.getCause());
    }

    /**
     * DOCUMENT ME!
     *
     * @param       owner      DOCUMENT ME!
     * @param       level      DOCUMENT ME!
     * @param       errorcode  DOCUMENT ME!
     * @param       exception  DOCUMENT ME!
     *
     * @deprecated  DOCUMENT ME!
     */
    public void showExceptionDialog(final JFrame owner,
            final int level,
            final String errorcode,
            final Throwable exception) {
        final JDialog exceptionDialog = new JDialog(owner, true);
        exceptionDialog.setLocationRelativeTo(owner);
        showExceptionDialog(
            exceptionDialog,
            level,
            resource.getExceptionName(errorcode),
            resource.getExceptionMessage(errorcode),
            exception);
    }

    /**
     * DOCUMENT ME!
     *
     * @param       owner      DOCUMENT ME!
     * @param       level      DOCUMENT ME!
     * @param       errorcode  DOCUMENT ME!
     * @param       exception  DOCUMENT ME!
     *
     * @deprecated  DOCUMENT ME!
     */
    public void showExceptionDialog(final JDialog owner,
            final int level,
            final String errorcode,
            final Throwable exception) {
        final JDialog exceptionDialog = new JDialog(owner, true);
        exceptionDialog.setLocationRelativeTo(owner);
        showExceptionDialog(
            exceptionDialog,
            level,
            resource.getExceptionName(errorcode),
            resource.getExceptionMessage(errorcode),
            exception);
    }

    /**
     * DOCUMENT ME!
     *
     * @param       level      DOCUMENT ME!
     * @param       errorcode  DOCUMENT ME!
     * @param       exception  DOCUMENT ME!
     *
     * @deprecated  DOCUMENT ME!
     */
    public void showExceptionDialog(final int level, final String errorcode, final Throwable exception) {
        final JDialog exceptionDialog = new JDialog(new JFrame(), true);
        exceptionDialog.setLocationRelativeTo(null);
        showExceptionDialog(
            exceptionDialog,
            level,
            resource.getExceptionName(errorcode),
            resource.getExceptionMessage(errorcode),
            exception);
    }

    /**
     * . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
     *
     * @param  owner      DOCUMENT ME!
     * @param  level      DOCUMENT ME!
     * @param  name       DOCUMENT ME!
     * @param  message    DOCUMENT ME!
     * @param  exception  DOCUMENT ME!
     */
    public void showExceptionDialog(final JFrame owner,
            final int level,
            final String name,
            final String message,
            final Throwable exception) {
        final JDialog exceptionDialog = new JDialog(owner, true);
        doShowExceptionDialog(exceptionDialog, level, name, message, exception);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  owner      DOCUMENT ME!
     * @param  level      DOCUMENT ME!
     * @param  name       DOCUMENT ME!
     * @param  message    DOCUMENT ME!
     * @param  exception  DOCUMENT ME!
     */
    public void showExceptionDialog(final JDialog owner,
            final int level,
            final String name,
            final String message,
            final Throwable exception) {
        final JDialog exceptionDialog = new JDialog(owner, true);
        doShowExceptionDialog(exceptionDialog, level, name, message, exception);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  level      DOCUMENT ME!
     * @param  name       DOCUMENT ME!
     * @param  message    DOCUMENT ME!
     * @param  exception  DOCUMENT ME!
     */
    public void showExceptionDialog(final int level,
            final String name,
            final String message,
            final Throwable exception) {
        final JFrame parentFrame = ComponentRegistry.isRegistred() ? ComponentRegistry.getRegistry().getMainWindow()
                                                                   : new JFrame();
        final JDialog exceptionDialog = new JDialog(parentFrame, true);
        doShowExceptionDialog(exceptionDialog, level, name, message, exception);
    }

    /**
     * . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
     *
     * @param  owner           DOCUMENT ME!
     * @param  level           DOCUMENT ME!
     * @param  name            DOCUMENT ME!
     * @param  message         DOCUMENT ME!
     * @param  detailMessages  DOCUMENT ME!
     */
    public void showExceptionDialog(final JFrame owner,
            final int level,
            final String name,
            final String message,
            final Collection detailMessages) {
        final JDialog exceptionDialog = new JDialog(owner, true);
        doShowExceptionDialog(exceptionDialog, level, name, message, detailMessages);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  owner           DOCUMENT ME!
     * @param  level           DOCUMENT ME!
     * @param  name            DOCUMENT ME!
     * @param  message         DOCUMENT ME!
     * @param  detailMessages  DOCUMENT ME!
     */
    public void showExceptionDialog(final JDialog owner,
            final int level,
            final String name,
            final String message,
            final Collection detailMessages) {
        final JDialog exceptionDialog = new JDialog(owner, true);
        doShowExceptionDialog(exceptionDialog, level, name, message, detailMessages);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  level           DOCUMENT ME!
     * @param  name            DOCUMENT ME!
     * @param  message         DOCUMENT ME!
     * @param  detailMessages  DOCUMENT ME!
     */
    public void showExceptionDialog(final int level,
            final String name,
            final String message,
            final Collection detailMessages) {
        final JDialog exceptionDialog = new JDialog(new JFrame(), true);
        doShowExceptionDialog(exceptionDialog, level, name, message, detailMessages);
    }

    /**
     * . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
     *
     * @param   owner  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean showExitDialog(final JFrame owner) {
        if (PropertyManager.getManager().isAutoClose()) {
            return true;
        } else {
            final JDialog exitDialog = exitOption.createDialog(
                    owner,
                    org.openide.util.NbBundle.getMessage(
                        ExceptionManager.class,
                        "ExceptionManager.showExitDialog(JFrame).exitDialog.title")); // NOI18N
            return doShowExitDialog(exitDialog);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   owner  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean showExitDialog(final JDialog owner) {
        if (PropertyManager.getManager().isAutoClose()) {
            return true;
        } else {
            final JDialog exitDialog = exitOption.createDialog(
                    owner,
                    org.openide.util.NbBundle.getMessage(
                        ExceptionManager.class,
                        "ExceptionManager.showExitDialog(JDialog).exitDialog.title")); // NOI18N
            return doShowExitDialog(exitDialog);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean showExitDialog() {
        if (PropertyManager.getManager().isAutoClose()) {
            return true;
        } else {
            final JDialog exitDialog = exitOption.createDialog(
                    null,
                    org.openide.util.NbBundle.getMessage(
                        ExceptionManager.class,
                        "ExceptionManager.showExitDialog().exitDialog.title")); // NOI18N
            return doShowExitDialog(exitDialog);
        }
    }

    /**
     * . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
     *
     * @param   exitDialog  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private boolean doShowExitDialog(final JDialog exitDialog) {
        SwingUtilities.updateComponentTreeUI(exitDialog);
        exitDialog.setLocationRelativeTo(exitDialog.getOwner());
        StaticSwingTools.showDialog(exitDialog);

        if (exitOption.getValue().equals(
                        org.openide.util.NbBundle.getMessage(
                            ExceptionManager.class,
                            "ExceptionManager.exitOption.option.confirm"))) // NOI18N
        {
            if (logger.isDebugEnabled()) {
                logger.debug("user wants to close program");                // NOI18N
            }
            // System.exit(0);
            return true;
        } else {
            return false;
        }
    }

    /**
     * . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
     *
     * @param  exceptionDialog  DOCUMENT ME!
     * @param  level            DOCUMENT ME!
     * @param  name             DOCUMENT ME!
     * @param  message          DOCUMENT ME!
     * @param  exception        DOCUMENT ME!
     */
    private void doShowExceptionDialog(final JDialog exceptionDialog,
            final int level,
            final String name,
            final String message,
            final Throwable exception) {
        exceptionDialog.setAlwaysOnTop(true);
        if (exceptionPane == null) {
            exceptionPane = new ExceptionPane();
        }

        synchronized (exceptionPane) {
            exceptionPane.init(exceptionDialog, level, message, exception);
            exceptionDialog.setTitle(name);
            exceptionDialog.setResizable(true);
            exceptionDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            exceptionDialog.setContentPane(exceptionPane);
            exceptionDialog.pack();
            StaticSwingTools.showDialog(exceptionDialog);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  exceptionDialog  DOCUMENT ME!
     * @param  level            DOCUMENT ME!
     * @param  name             DOCUMENT ME!
     * @param  message          DOCUMENT ME!
     * @param  detailMessages   DOCUMENT ME!
     */
    private void doShowExceptionDialog(final JDialog exceptionDialog,
            final int level,
            final String name,
            final String message,
            final Collection detailMessages) {
        if (exceptionPane == null) {
            exceptionPane = new ExceptionPane();
        }

        synchronized (exceptionPane) {
            exceptionPane.init(exceptionDialog, level, message, detailMessages);
            exceptionDialog.setTitle(name);
            exceptionDialog.setResizable(true);
            exceptionDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            exceptionDialog.setContentPane(exceptionPane);
            exceptionDialog.pack();
            exceptionDialog.setLocationRelativeTo(exceptionDialog.getOwner());
            StaticSwingTools.showDialog(exceptionDialog);
        }
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
     * #########################################################################
     *
     * @version  $Revision$, $Date$
     */
    private class ExceptionPane extends JPanel {

        //~ Static fields/initializers -----------------------------------------

        private static final String MESSAGE_TEMPLATE =
            "<html><table width=\"500\" border=0><tr><td>%s</td></tr></table></html>";

        //~ Instance fields ----------------------------------------------------

        private JDialog parent = null;

        private JLabel messageLabel;
        private JLabel exceptionIconLabel;
        private JPanel detailsPanel;
        private JTextArea detailsTextArea;
        private JButton ignoreButton;
        private JButton exitButton;
        private JToggleButton detailsButton;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new ExceptionPane object.
         */
        private ExceptionPane() {
            super(new GridBagLayout());
            init();
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         */
        private void init() {
            final ActionListener buttonListener = new ButtonListener();

            this.setBorder(new EmptyBorder(10, 10, 10, 10));
            final GridBagConstraints constraints = new GridBagConstraints();

            // ICON ================================================================
            constraints.anchor = GridBagConstraints.CENTER;
            constraints.fill = GridBagConstraints.NONE;
            constraints.gridheight = 1;
            constraints.gridwidth = 1;
            constraints.weightx = 0;
            constraints.weighty = 0.0;
            constraints.gridy = 0;
            constraints.gridx = 0;

            exceptionIconLabel = new JLabel();
            exceptionIconLabel.setIcon(UIManager.getIcon("OptionPane.errorIcon")); // NOI18N
            exceptionIconLabel.setBorder(new CompoundBorder(
                    new SoftBevelBorder(SoftBevelBorder.LOWERED),
                    new EmptyBorder(10, 10, 10, 10)));
            this.add(exceptionIconLabel, constraints);

            // MESSAGE =============================================================
            constraints.fill = GridBagConstraints.BOTH;
            constraints.weightx = 1;
            constraints.gridx++;
            messageLabel = new JLabel();
            messageLabel.setBorder(new EmptyBorder(20, 20, 20, 20));
            this.add(messageLabel, constraints);

            // BUTTONS =============================================================
            constraints.insets = new Insets(20, 0, 10, 0);
            constraints.gridwidth = 2;
            constraints.gridy = 1;
            constraints.gridx = 0;
            final JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));

            ignoreButton = new JButton(org.openide.util.NbBundle.getMessage(
                        ExceptionManager.class,
                        "ExceptionManager.ignoreButton.text"));           // NOI18N
            ignoreButton.setMnemonic(org.openide.util.NbBundle.getMessage(
                    ExceptionManager.class,
                    "ExceptionManager.ignoreButton.mnemonic").charAt(0)); // NOI18N
            ignoreButton.setToolTipText(org.openide.util.NbBundle.getMessage(
                    ExceptionManager.class,
                    "ExceptionManager.ignoreButton.tooltip"));            // NOI18N
            ignoreButton.setActionCommand("ignore");                      // NOI18N
            ignoreButton.addActionListener(buttonListener);
            buttonPanel.add(ignoreButton);

            exitButton = new JButton(org.openide.util.NbBundle.getMessage(
                        ExceptionManager.class,
                        "ExceptionManager.exitButton.text"));           // NOI18N
            exitButton.setMnemonic(org.openide.util.NbBundle.getMessage(
                    ExceptionManager.class,
                    "ExceptionManager.exitButton.mnemonic").charAt(0)); // NOI18N
            exitButton.setToolTipText(org.openide.util.NbBundle.getMessage(
                    ExceptionManager.class,
                    "ExceptionManager.exitButton.tooltip"));            // NOI18N
            exitButton.setActionCommand("exit");                        // NOI18N
            exitButton.addActionListener(buttonListener);
            buttonPanel.add(exitButton);

            detailsButton = new JToggleButton(org.openide.util.NbBundle.getMessage(
                        ExceptionManager.class,
                        "ExceptionManager.detailsButton.text"));           // NOI18N
            detailsButton.setMnemonic(org.openide.util.NbBundle.getMessage(
                    ExceptionManager.class,
                    "ExceptionManager.detailsButton.mnemonic").charAt(0)); // NOI18N
            detailsButton.setToolTipText(org.openide.util.NbBundle.getMessage(
                    ExceptionManager.class,
                    "ExceptionManager.detailsButton.tooltip"));            // NOI18N
            detailsButton.setActionCommand("details");                     // NOI18N
            detailsButton.addActionListener(buttonListener);
            buttonPanel.add(detailsButton);

            this.add(buttonPanel, constraints);

            constraints.insets = new Insets(0, 0, 0, 0);
            constraints.gridy++;
            constraints.weightx = 1.0;
            constraints.weighty = 1.0;
            detailsTextArea = new JTextArea(4, 20);
            detailsTextArea.setEditable(false);
            detailsPanel = new JPanel(new GridLayout(1, 1));
            detailsPanel.add(new JScrollPane(detailsTextArea));
            detailsPanel.setVisible(false);
            this.add(detailsPanel, constraints);
        }

        /**
         * DOCUMENT ME!
         *
         * @param  parent   DOCUMENT ME!
         * @param  level    DOCUMENT ME!
         * @param  message  DOCUMENT ME!
         */
        private void init(final JDialog parent, final int level, final String message) {
            this.parent = parent;
            this.exitButton.setEnabled(true);

            if (level == FATAL) {
                ignoreButton.setEnabled(false);
                exceptionIconLabel.setIcon(UIManager.getIcon("OptionPane.errorIcon"));   // NOI18N
                if (parent.getTitle() == null) {
                    parent.setTitle(org.openide.util.NbBundle.getMessage(
                            ExceptionManager.class,
                            "ExceptionManager.title.fatal"));                            // NOI18N
                }
            } else if (level == ERROR) {
                ignoreButton.setEnabled(true);
                exceptionIconLabel.setIcon(UIManager.getIcon("OptionPane.errorIcon"));   // NOI18N
                if (parent.getTitle() == null) {
                    parent.setTitle(org.openide.util.NbBundle.getMessage(
                            ExceptionManager.class,
                            "ExceptionManager.title.error"));                            // NOI18N
                }
            } else if (level == WARNING) {
                ignoreButton.setEnabled(true);
                exceptionIconLabel.setIcon(UIManager.getIcon("OptionPane.warningIcon")); // NOI18N
                if (parent.getTitle() == null) {
                    parent.setTitle(org.openide.util.NbBundle.getMessage(
                            ExceptionManager.class,
                            "ExceptionManager.title.warning"));                          // NOI18N
                }
            } else if (level == PLUGIN_ERROR) {
                ignoreButton.setEnabled(true);
                exitButton.setEnabled(false);
                exceptionIconLabel.setIcon(UIManager.getIcon("OptionPane.errorIcon"));   // NOI18N
                if (parent.getTitle() == null) {
                    parent.setTitle(org.openide.util.NbBundle.getMessage(
                            ExceptionManager.class,
                            "ExceptionManager.title.error"));                            // NOI18N
                }
            } else if (level == PLUGIN_WARNING) {
                ignoreButton.setEnabled(true);
                exitButton.setEnabled(false);
                exceptionIconLabel.setIcon(UIManager.getIcon("OptionPane.warningIcon")); // NOI18N
                if (parent.getTitle() == null) {
                    parent.setTitle(org.openide.util.NbBundle.getMessage(
                            ExceptionManager.class,
                            "ExceptionManager.title.warning"));                          // NOI18N
                }
            }

            detailsButton.setSelected(false);
            detailsPanel.setVisible(false);
            if (message.startsWith("<html>")) {
                messageLabel.setText(message);
            } else {
                messageLabel.setText(String.format(MESSAGE_TEMPLATE, message));
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param  parent     DOCUMENT ME!
         * @param  level      DOCUMENT ME!
         * @param  message    DOCUMENT ME!
         * @param  exception  DOCUMENT ME!
         */
        private void init(final JDialog parent, final int level, final String message, final Throwable exception) {
            this.init(parent, level, message);

            if (exception != null) {
                detailsButton.setEnabled(true);
                final StackTraceElement[] elements = exception.getStackTrace();
                final StringBuffer buffer = new StringBuffer();
                for (int i = 0; i < elements.length; i++) {
                    buffer.append(elements[i].toString()).append('\n');
                }

                detailsTextArea.setText(buffer.toString());
            } else {
                detailsButton.setEnabled(false);
                detailsTextArea.setText(""); // NOI18N
            }

            detailsButton.setSelected(false);
            detailsPanel.setVisible(false);
            if (message.startsWith("<html>")) {
                messageLabel.setText(message);
            } else {
                messageLabel.setText(String.format(MESSAGE_TEMPLATE, message));
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param  parent          DOCUMENT ME!
         * @param  level           DOCUMENT ME!
         * @param  message         DOCUMENT ME!
         * @param  detailMessages  DOCUMENT ME!
         */
        private void init(final JDialog parent,
                final int level,
                final String message,
                final Collection detailMessages) {
            this.init(parent, level, message);

            if ((detailMessages != null) && (detailMessages.size() > 0)) {
                try {
                    final StringBuffer stringBuffer = new StringBuffer();
                    final Iterator iterator = detailMessages.iterator();

                    while (iterator.hasNext()) {
                        stringBuffer.append(iterator.next().toString());
                        stringBuffer.append("\n"); // NOI18N
                    }

                    detailsButton.setEnabled(true);
                    detailsTextArea.setText(stringBuffer.toString());
                } catch (Exception exp) {
                    logger.error("error initializing exception pane: " + exp.getMessage() + "'"); // NOI18N
                }
            } else {
                detailsButton.setEnabled(false);
                detailsTextArea.setText("");                                                      // NOI18N
            }

            detailsButton.setSelected(false);
            detailsPanel.setVisible(false);
            if (message.startsWith("<html>")) {
                messageLabel.setText(message);
            } else {
                messageLabel.setText(String.format(MESSAGE_TEMPLATE, message));
            }
        }

        //~ Inner Classes ------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @version  $Revision$, $Date$
         */
        private class ButtonListener implements ActionListener {

            //~ Methods --------------------------------------------------------

            @Override
            public void actionPerformed(final ActionEvent e) {
                if (e.getActionCommand().equals("exit"))                          // NOI18N
                {
                    if (ExceptionManager.this.showExitDialog(parent)) {
                        if (logger.isInfoEnabled()) {
                            logger.info("closing program");                       // NOI18N
                        }
                        System.exit(1);
                    }
                } else if (e.getActionCommand().equals("ignore"))                 // NOI18N
                {
                    parent.dispose();
                    // parent = null;
                } else if (e.getActionCommand().equals("details"))                // NOI18N
                {
                    if (detailsButton.isSelected()) {
                        detailsPanel.setVisible(true);
                        parent.pack();
                    } else {
                        detailsPanel.setVisible(false);
                        parent.pack();
                    }
                } else {
                    logger.warn("unknown action '" + e.getActionCommand() + "'"); // NOI18N
                }
            }
        }
    }

    // TEST ....................................................................

    /*public static void main(String args[])
     * { org.apache.log4j.BasicConfigurator.configure();  ExceptionManager manager = ExceptionManager.getManager();
     * manager.showExceptionDialog(ExceptionManager.WARNING, "Warnung!", "Warnung, alles Unsinn!", new
     * Exception("(T)ERROR")); //manager.showExceptionDialog(ExceptionManager.FATAL, "FATAL!", "Warnung, alles im
     * A....!", new Exception("xxxxxxxxxxx xxxxxxxxxxxxxxxxxxx xxxxxxxxx xxxxxxx xxxxxxxxxxx xxxxx"));
     * manager.showExceptionDialog(ExceptionManager.ERROR, "lx01", null); NavigatorException exception = new
     * NavigatorException("Holla"); manager.showExceptionDialog(exception);}*/
}
