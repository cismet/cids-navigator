package Sirius.navigator.exception;

import Sirius.navigator.resource.PropertyManager;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import org.apache.log4j.Logger;

import Sirius.navigator.resource.ResourceManager;

//import Sirius.navigator.NavigatorLogger;
//import Sirius.navigator.connection.ConnectionHandler;

/**
 *
 * @author  pascal
 */
public class ExceptionManager
{
    private static final ResourceBundle I18N = ResourceBundle.getBundle("Sirius/navigator/resource/i18n/resources");
    private final static Logger logger = Logger.getLogger(ExceptionManager.class);
    private static ExceptionManager manager = null;
    private static final ResourceManager resource = ResourceManager.getManager();
    
    public final static int WARNING = 1;
    public final static int ERROR = 2;
    public final static int FATAL = 4;
    
    public final static int PLUGIN_WARNING = 8;
    public final static int PLUGIN_ERROR = 16;
    public final static int PLUGIN_FATAL = 32;
    
    private final JOptionPane exitOption;
    private ExceptionPane exceptionPane = null;
    
    /** Creates a new instance of ExceptionManager */
    private ExceptionManager()
    {
        logger.info("creating singleton exception manager instance");
        exitOption = new JOptionPane(
                I18N.getString("Sirius.navigator.exception.ExceptionManager.exitMessage"),
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.YES_NO_OPTION,
                null,
                new String[] {
                    I18N.getString("Sirius.navigator.exception.ExceptionManager.confirmButton.text"),
                    I18N.getString("Sirius.navigator.exception.ExceptionManager.cancelButton.text")},
                I18N.getString("Sirius.navigator.exception.ExceptionManager.cancelButton.text"));
    }
    
    public final static ExceptionManager getManager()
    {
        if(manager == null)
        {
            manager = new ExceptionManager();
        }
        
        return manager;
    }
    
    // . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
    
    public void showExceptionDialog(JFrame owner, NavigatorException exception)
    {
        showExceptionDialog(owner, exception.getLevel(), exception.getName(), exception.getMessage(), exception.getCause());
    }
    
    public void showExceptionDialog(JDialog owner, NavigatorException exception)
    {
        showExceptionDialog(owner, exception.getLevel(), exception.getName(), exception.getMessage(), exception.getCause());
    }
    
    public void showExceptionDialog(NavigatorException exception)
    {
        showExceptionDialog(exception.getLevel(), exception.getName(), exception.getMessage(), exception.getCause());
    }

    public void showExceptionDialog(JFrame owner, int level, String errorcode, Throwable exception)
    {
        JDialog exceptionDialog = new JDialog(owner, true);
        exceptionDialog.setLocationRelativeTo(owner);
        showExceptionDialog(exceptionDialog, level, resource.getExceptionName(errorcode), resource.getExceptionMessage(errorcode), exception);
    }
    
    public void showExceptionDialog(JDialog owner, int level, String errorcode, Throwable exception)
    {
        JDialog exceptionDialog = new JDialog(owner, true);
        exceptionDialog.setLocationRelativeTo(owner);
        showExceptionDialog(exceptionDialog, level, resource.getExceptionName(errorcode), resource.getExceptionMessage(errorcode), exception);
    }
    
    public void showExceptionDialog(int level, String errorcode, Throwable exception)
    {
        JDialog exceptionDialog = new JDialog(new JFrame(), true);
        exceptionDialog.setLocationRelativeTo(null);
        showExceptionDialog(exceptionDialog, level, resource.getExceptionName(errorcode), resource.getExceptionMessage(errorcode), exception);
    }
    
    // . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
    
    public void showExceptionDialog(JFrame owner, int level, String name, String message, Throwable exception)
    {
        JDialog exceptionDialog = new JDialog(owner, true);
        doShowExceptionDialog(exceptionDialog, level, name, message, exception);
    }
    
    public void showExceptionDialog(JDialog owner, int level, String name, String message, Throwable exception)
    {
        JDialog exceptionDialog = new JDialog(owner, true);
        doShowExceptionDialog(exceptionDialog, level, name, message, exception);
    }
    
    public void showExceptionDialog(int level, String name, String message, Throwable exception)
    {
        JDialog exceptionDialog = new JDialog(new JFrame(), true);
        doShowExceptionDialog(exceptionDialog, level, name, message, exception);
    }
    
    // . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
    
    public void showExceptionDialog(JFrame owner, int level, String name, String message, Collection detailMessages)
    {
        JDialog exceptionDialog = new JDialog(owner, true);
        doShowExceptionDialog(exceptionDialog, level, name, message, detailMessages);
    }
    
    public void showExceptionDialog(JDialog owner, int level, String name, String message, Collection detailMessages)
    {
        JDialog exceptionDialog = new JDialog(owner, true);
        doShowExceptionDialog(exceptionDialog, level, name, message, detailMessages);
    }
    
    public void showExceptionDialog(int level, String name, String message, Collection detailMessages)
    {
        JDialog exceptionDialog = new JDialog(new JFrame(), true);
        doShowExceptionDialog(exceptionDialog, level, name, message, detailMessages);
    }
    
    // . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
    
    public boolean showExitDialog(JFrame owner)
    {
        if (PropertyManager.getManager().isAutoClose()){
            return true;
        }
        else {
        JDialog exitDialog = exitOption.createDialog(owner,
                I18N.getString("Sirius.navigator.exception.ExceptionManager.title"));
        return doShowExitDialog(exitDialog);
        }
    }
    
    public boolean showExitDialog(JDialog owner)
    {
        if (PropertyManager.getManager().isAutoClose()){
            return true;
        }
        else {
        JDialog exitDialog = exitOption.createDialog(owner,
                I18N.getString("Sirius.navigator.exception.ExceptionManager.title"));
        return doShowExitDialog(exitDialog);
        }

        }
    
    public boolean showExitDialog()
    {
        if (PropertyManager.getManager().isAutoClose()){
            return true;
        }
        else {
        JDialog exitDialog = exitOption.createDialog(null,
                I18N.getString("Sirius.navigator.exception.ExceptionManager.title"));
        return doShowExitDialog(exitDialog);
        }
    }
    
    // . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
    
    private boolean doShowExitDialog(JDialog exitDialog)
    {
        SwingUtilities.updateComponentTreeUI(exitDialog);
        exitDialog.setLocationRelativeTo(exitDialog.getOwner());
        exitDialog.show();
        
        if(exitOption.getValue().equals(
                I18N.getString("Sirius.navigator.exception.ExceptionManager.confirmButton.text")))
        {
            logger.debug("user wants to close program");
            //System.exit(0);
            return true;
        }
        else
        {
            return false;
        }
    }
    
    // . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
                    
    private void doShowExceptionDialog(JDialog exceptionDialog, int level, String name, String message, Throwable exception)
    {
        exceptionDialog.setAlwaysOnTop(true);
        if(exceptionPane == null)
        {
            exceptionPane = new ExceptionPane();
        }
        
        synchronized(exceptionPane)
        {
            exceptionPane.init(exceptionDialog, level, message, exception);
            exceptionDialog.setTitle(name);
            exceptionDialog.setResizable(false);
            exceptionDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            exceptionDialog.setContentPane(exceptionPane);
            exceptionDialog.pack();
            exceptionDialog.setLocationRelativeTo(exceptionDialog.getOwner());
            exceptionDialog.show();
        }
    }
    
    private void doShowExceptionDialog(JDialog exceptionDialog, int level, String name, String message, Collection detailMessages)
    {
        if(exceptionPane == null)
        {
            exceptionPane = new ExceptionPane();
        }
        
        synchronized(exceptionPane)
        {
            exceptionPane.init(exceptionDialog, level, message, detailMessages);
            exceptionDialog.setTitle(name);
            exceptionDialog.setResizable(false);
            exceptionDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            exceptionDialog.setContentPane(exceptionPane);
            exceptionDialog.pack();
            exceptionDialog.setLocationRelativeTo(exceptionDialog.getOwner());
            exceptionDialog.show();
        }
    }
    
    // . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
    
    // #########################################################################
    
    private class ExceptionPane extends JPanel
    {
        private JDialog parent = null;
        
        private JLabel messageLabel, exceptionIconLabel;
        private JPanel detailsPanel;
        private JTextArea detailsTextArea;
        private JButton ignoreButton, exitButton;
        private JToggleButton detailsButton;

        private ExceptionPane()
        {
            super(new GridBagLayout());
            init();
        }
        
        private void init()
        {
            ActionListener buttonListener = new ButtonListener();
           
            
            this.setBorder(new EmptyBorder(10,10,10,10));
            GridBagConstraints constraints = new GridBagConstraints();
            
            // ICON ================================================================
            constraints.anchor = GridBagConstraints.CENTER;
            constraints.fill = GridBagConstraints.NONE;
            constraints.gridheight = 1;
            constraints.gridwidth = 1;
            constraints.weightx = 0.5;
            constraints.weighty = 0.0;
            constraints.gridy = 0;
            constraints.gridx = 0;
            
            exceptionIconLabel = new JLabel();
            exceptionIconLabel.setIcon(UIManager.getIcon("OptionPane.errorIcon"));
            exceptionIconLabel.setBorder(new CompoundBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED), new EmptyBorder(10,10,10,10)));
            this.add(exceptionIconLabel, constraints);
            
            // MESSAGE =============================================================
            constraints.fill = GridBagConstraints.BOTH;
            constraints.weightx = 0.5;
            constraints.gridx++;
            messageLabel = new JLabel();
            messageLabel.setBorder(new EmptyBorder(20,20,20,20));
            this.add(messageLabel, constraints);
            
            // BUTTONS =============================================================
            constraints.insets = new Insets(20, 0, 10, 0);
            constraints.gridwidth = 2;
            constraints.gridy = 1;
            constraints.gridx = 0;
            JPanel buttonPanel = new JPanel(new GridLayout(1,3,10,10));
            
            ignoreButton = new JButton(I18N.getString("Sirius.navigator.exception.ExceptionManager.ignoreButton.text"));
            ignoreButton.setMnemonic(I18N.getString("Sirius.navigator.exception.ExceptionManager.ignoreButton.mnemonic").charAt(0));
            ignoreButton.setToolTipText(I18N.getString("Sirius.navigator.exception.ExceptionManager.ignoreButton.tooltip"));
            ignoreButton.setActionCommand("ignore");
            ignoreButton.addActionListener(buttonListener);
            buttonPanel.add(ignoreButton);
            
            exitButton = new JButton(I18N.getString("Sirius.navigator.exception.ExceptionManager.exitButton.text"));
            exitButton.setMnemonic(I18N.getString("Sirius.navigator.exception.ExceptionManager.exitButton.mnemonic").charAt(0));
            exitButton.setToolTipText(I18N.getString("Sirius.navigator.exception.ExceptionManager.exitButton.tooltip"));
            exitButton.setActionCommand("exit");
            exitButton.addActionListener(buttonListener);
            buttonPanel.add(exitButton);
            
            detailsButton = new JToggleButton(I18N.getString("Sirius.navigator.exception.ExceptionManager.detailsButton.text"));
            detailsButton.setMnemonic(I18N.getString("Sirius.navigator.exception.ExceptionManager.detailsButton.mnemonic").charAt(0));
            detailsButton.setToolTipText(I18N.getString("Sirius.navigator.exception.ExceptionManager.detailsButton.tooltip"));
            detailsButton.setActionCommand("details");
            detailsButton.addActionListener(buttonListener);
            buttonPanel.add(detailsButton);
            
            this.add(buttonPanel, constraints);
            
            constraints.insets = new Insets(0, 0, 0, 0);
            constraints.gridy++;
            constraints.weightx = 1.0;
            constraints.weighty = 1.0;
            detailsTextArea = new JTextArea(4, 20);
            detailsTextArea.setEditable(false);
            detailsPanel = new JPanel(new GridLayout(1,1));
            detailsPanel.add(new JScrollPane(detailsTextArea));
            detailsPanel.setVisible(false);
            this.add(detailsPanel, constraints);
        }
        
        private void init(JDialog parent, int level, String message)
        {
            this.parent = parent;
            this.exitButton.setEnabled(true);
            
            if(level == FATAL)
            {
                ignoreButton.setEnabled(false);
                exceptionIconLabel.setIcon(UIManager.getIcon("OptionPane.errorIcon"));
                if(parent.getTitle() == null)
                {
                    parent.setTitle(I18N.getString("Sirius.navigator.exception.ExceptionManager.title.fatal"));
                }
            }
            else if(level == ERROR)
            {
                ignoreButton.setEnabled(true);
                exceptionIconLabel.setIcon(UIManager.getIcon("OptionPane.errorIcon"));
                if(parent.getTitle() == null)
                {
                    parent.setTitle(I18N.getString("Sirius.navigator.exception.ExceptionManager.title.error"));
                }
            
            }
            else if(level == WARNING)
            {
                ignoreButton.setEnabled(true);
                exceptionIconLabel.setIcon(UIManager.getIcon("OptionPane.warningIcon"));
                if(parent.getTitle() == null)
                {
                    parent.setTitle(I18N.getString("Sirius.navigator.exception.ExceptionManager.title.warning"));
                }
            }
            else if(level == PLUGIN_ERROR)
            {
                ignoreButton.setEnabled(true);
                exitButton.setEnabled(false);
                exceptionIconLabel.setIcon(UIManager.getIcon("OptionPane.errorIcon"));
                if(parent.getTitle() == null)
                {
                    parent.setTitle(I18N.getString("Sirius.navigator.exception.ExceptionManager.title.error"));
                }
            
            }
            else if(level == PLUGIN_WARNING)
            {
                ignoreButton.setEnabled(true);
                exitButton.setEnabled(false);
                exceptionIconLabel.setIcon(UIManager.getIcon("OptionPane.warningIcon"));
                if(parent.getTitle() == null)
                {
                    parent.setTitle(I18N.getString("Sirius.navigator.exception.ExceptionManager.title.warning"));
                }
            }
            
            
            detailsButton.setSelected(false);
            detailsPanel.setVisible(false);
            messageLabel.setText(message);
        }
        
        private void init(JDialog parent, int level, String message, Throwable exception)
        {
            this.init(parent, level, message);
            
            if(exception != null)
            {
                detailsButton.setEnabled(true);
                StackTraceElement[] elements = exception.getStackTrace();
                StringBuffer buffer = new StringBuffer();
                for(int i = 0; i < elements.length; i++)
                {
                    buffer.append(elements[i].toString()).append('\n');
                }

                detailsTextArea.setText(buffer.toString());
            }
            else
            {
                detailsButton.setEnabled(false);
                detailsTextArea.setText("");
            }

            detailsButton.setSelected(false);
            detailsPanel.setVisible(false);
            messageLabel.setText(message);
        }
        
        private void init(JDialog parent, int level, String message, Collection detailMessages)
        {
            this.init(parent, level, message);

            if(detailMessages != null && detailMessages.size() > 0)
            {
                try
                {
                    StringBuffer stringBuffer = new StringBuffer();
                    Iterator iterator = detailMessages.iterator();
                    
                    while(iterator.hasNext())
                    {
                        stringBuffer.append(iterator.next().toString());
                        stringBuffer.append("\n");
                    }

                    detailsButton.setEnabled(true);
                    detailsTextArea.setText(stringBuffer.toString());
                }
                catch(Exception exp)
                {
                    logger.error("error initializing exception pane: " + exp.getMessage() + "'");
                }
            }
            else
            {
                detailsButton.setEnabled(false);
                detailsTextArea.setText("");
            }

            detailsButton.setSelected(false);
            detailsPanel.setVisible(false);
            messageLabel.setText(message);
        }
        
        private class ButtonListener implements ActionListener
        {
            public void actionPerformed(ActionEvent e)
            {
                if(e.getActionCommand().equals("exit"))
                {
                    if(ExceptionManager.this.showExitDialog(parent))
                    {
                        logger.info("closing program");
                        System.exit(1);
                    }
                }
                else if(e.getActionCommand().equals("ignore"))
                {
                    parent.dispose();
                    //parent = null;
                }
                else if(e.getActionCommand().equals("details"))
                {
                    if(detailsButton.isSelected())
                    {
                        detailsPanel.setVisible(true);
                        parent.pack();  
                    }
                    else
                    {
                        detailsPanel.setVisible(false);
                        parent.pack();
                    }
                }
                else
                {
                    logger.warn("unknown action '" + e.getActionCommand() + "'");
                }
            }
        }
    }

    // TEST ....................................................................
    
    /*public static void main(String args[])
    {
       org.apache.log4j.BasicConfigurator.configure();
        
        ExceptionManager manager = ExceptionManager.getManager();
        manager.showExceptionDialog(ExceptionManager.WARNING, "Warnung!", "Warnung, alles Unsinn!", new Exception("(T)ERROR"));
        //manager.showExceptionDialog(ExceptionManager.FATAL, "FATAL!", "Warnung, alles im A....!", new Exception("xxxxxxxxxxx xxxxxxxxxxxxxxxxxxx xxxxxxxxx xxxxxxx xxxxxxxxxxx xxxxx"));
        manager.showExceptionDialog(ExceptionManager.ERROR, "lx01", null);
        NavigatorException exception = new NavigatorException("Holla");
        manager.showExceptionDialog(exception);
    }*/
}
