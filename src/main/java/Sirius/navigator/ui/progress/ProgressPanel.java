/*
 * ProgressPanel.java
 *
 * Created on 11. Mai 2003, 19:00
 */


package Sirius.navigator.ui.progress;

import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import org.apache.log4j.Logger;

import Sirius.navigator.method.*;

/**
 *
 * @author  Peter Alzheimer
 */
public class ProgressPanel extends javax.swing.JPanel
{
    protected final Logger logger;
    
    private final Timer progressTimer;
    private ProgressObserver progressObserver;
    
    private boolean locked = false;
    
    
    /** Creates new form ProgressPanel */
    public ProgressPanel()
    {
        this.progressTimer = new Timer(500, new TimerListener());
        this.logger = Logger.getLogger(this.getClass());
        
        initComponents();
    }
    
    // .........................................................................
    
    public void invokeMethod (MultithreadedMethod method) //throws Exception
    {
        this.invokeMethod(method, null);
    }
    
    public void invokeMethod (MultithreadedMethod method, Object arguments) //throws Exception
    {
        if(logger.isDebugEnabled())logger.debug("invoking new multithreaded method");//NOI18N
        if(!isLocked() && !progressTimer.isRunning() && method.getProgressObserver() != null)   
        {
            method.invoke(arguments);
            this.start(method.getProgressObserver());
        }
        else
        {
            //System.out.println("isLocked() '" + isLocked() + "'");   
            logger.error("can not invoke method: progress panel is locked (" + isLocked() + "), progress timer is running (" + progressTimer.isRunning() + ") or progress observer is null (" + method.getProgressObserver() + ")");//NOI18N
            throw new RuntimeException("can not invoke method: progress panel is locked (" + isLocked() + "), progress timer is running (" + progressTimer.isRunning() + ") or progress observer is null (" + method.getProgressObserver() + ")");//NOI18N
        }
    }
    
    public void start(ProgressObserver progressObserver)
    {
        if(logger.isDebugEnabled())logger.debug("starting progress observer '" + progressObserver.getName() + "' (" + progressObserver.getMessage() + ")");//NOI18N
        if(!isLocked() && !progressTimer.isRunning())   
        {
            setLocked(true);
            this.progressObserver = progressObserver;
            
            if(SwingUtilities.isEventDispatchThread())
            {
                this.reset();
            }
            else
            {
                try
                {
                    if(logger.isDebugEnabled())logger.debug("synchronizing method 'reset'");//NOI18N
                    SwingUtilities.invokeAndWait(new Runnable()
                    {
                        public void run()
                        {
                            reset();
                        }
                    });
                }
                catch(Exception exp)
                {
                    logger.warn("could not synchronize method 'reset':\n" + exp.getMessage());//NOI18N
                    this.reset();
                }
            }
            
            progressTimer.setDelay(progressObserver.getDelay());
            progressTimer.start();
        }
        else
        {
            //System.out.println("isLocked() '" + isLocked() + "'");   
            logger.error("can not start: progress panel is locked (" + isLocked() + ") or progress timer is running (" + progressTimer.isRunning() + ")");//NOI18N
        }
    }
    
    private void reset()
    {
        if(logger.isDebugEnabled())logger.debug("resetting progress panel");//NOI18N
        progressBar.setMinimum(0);
        progressBar.setMaximum(progressObserver.getMaxProgress());
        progressBar.setValue(0);
        progressBar.setString(null);
        progressBar.setIndeterminate(progressObserver.isIndeterminate());
        progressBar.setStringPainted(!progressObserver.isIndeterminate());

        titleLabel.setText(progressObserver.getName());
        progressLabel.setText(progressObserver.getMessage());

        cancelButton.setEnabled(progressObserver.isInterruptible());
        restartButton.setEnabled(progressObserver.isRestartable());
    }
    
    // .........................................................................
    
    protected void interrupted()
    {
        logger.warn("progress observer interrupted");//NOI18N
        progressBar.setIndeterminate(false);
        progressTimer.stop();
        this.setProgress();
        setLocked(false);
        
        propertyChangeSupport.firePropertyChange("interrupted", new Boolean(false), new Boolean(true));//NOI18N
    }
    
    protected void finished()
    {
        if(logger.isDebugEnabled())logger.debug("progress observer finished");//NOI18N
        progressBar.setIndeterminate(false);
        progressTimer.stop();
        this.setProgress();
        setLocked(false);
        
        propertyChangeSupport.firePropertyChange("finished", new Boolean(false), new Boolean(true));//NOI18N
    }
    
    protected void setProgress()
    {
        progressLabel.setText(progressObserver.getMessage());
        
        if(!progressObserver.isIndeterminate())
        {
            progressBar.setValue(progressObserver.getProgress());
            //progressBar.setString(progressObserver.getPercentage() + "%");
        }
    }
    
    // .........................................................................
    
    protected boolean isLocked()
    {
        return this.locked;   
    }
    
    private void setLocked(boolean locked)
    {
        this.locked = locked;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        progressPanel = new javax.swing.JPanel();
        progressBar = new javax.swing.JProgressBar();
        progressLabel = new javax.swing.JLabel();
        buttonPanel = new javax.swing.JPanel();
        restartButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        titleLabel = new javax.swing.JLabel();

        setMinimumSize(new java.awt.Dimension(400, 175));
        setPreferredSize(new java.awt.Dimension(400, 175));
        setLayout(new java.awt.GridBagLayout());

        progressPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5), javax.swing.BorderFactory.createEtchedBorder()));
        progressPanel.setLayout(new java.awt.GridBagLayout());

        progressBar.setStringPainted(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 20, 15, 20);
        progressPanel.add(progressBar, gridBagConstraints);

        progressLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        progressLabel.setText(org.openide.util.NbBundle.getMessage(ProgressPanel.class, "ProgressPanel.progressLabel.initialText")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(15, 15, 1, 15);
        progressPanel.add(progressLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(progressPanel, gridBagConstraints);

        buttonPanel.setLayout(new java.awt.GridLayout(1, 1, 5, 0));

        restartButton.setText(org.openide.util.NbBundle.getMessage(ProgressPanel.class, "ProgressPanel.restartButton.text")); // NOI18N
        restartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                restartButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(restartButton);

        cancelButton.setText(org.openide.util.NbBundle.getMessage(ProgressPanel.class, "ProgressPanel.cancelButton.text")); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(cancelButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 5, 5, 5);
        add(buttonPanel, gridBagConstraints);

        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel.setText(org.openide.util.NbBundle.getMessage(ProgressPanel.class, "ProgressPanel.titleLabel.text")); // NOI18N
        titleLabel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        titleLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 15;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(titleLabel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cancelButtonActionPerformed
    {//GEN-HEADEREND:event_cancelButtonActionPerformed
        if(logger.isDebugEnabled())logger.debug("interrupting progess observer");//NOI18N
        if(isLocked())
        {
            progressTimer.stop();
            progressLabel.setText(org.openide.util.NbBundle.getMessage(ProgressPanel.class, "ProgressPanel.progressLabel.canceldText"));//NOI18N
            progressBar.setValue(progressObserver.getProgress());
            //progressBar.setString(progressObserver.getPercentage() + "%");
            progressObserver.setInterrupted(true);
        }
        else
        {
            logger.warn("can not cancel: progress panel is not locked (" + isLocked() + ") or progress timer is not running (" + progressTimer.isRunning() + ")");//NOI18N
        }
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void restartButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_restartButtonActionPerformed
    {//GEN-HEADEREND:event_restartButtonActionPerformed
        if(logger.isDebugEnabled())logger.debug("restarting progess observer");//NOI18N
        if(isLocked())
        {
            progressTimer.stop();
            progressLabel.setText("");//NOI18N
            progressBar.setValue(0);
            progressBar.setString(null);
            progressObserver.setInterrupted(true);
        }
        else
        {
            logger.warn("can not cancel: progress panel is not locked (" + isLocked() + ") or progress timer is not running (" + progressTimer.isRunning() + ")");//NOI18N
        }
    }//GEN-LAST:event_restartButtonActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel progressLabel;
    private javax.swing.JPanel progressPanel;
    private javax.swing.JButton restartButton;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
    
    /** Utility field used by bound properties. */
    private SwingPropertyChangeSupport propertyChangeSupport =  new SwingPropertyChangeSupport(this);
    
    private class TimerListener implements ActionListener 
    {
        public void actionPerformed(ActionEvent evt) 
        {
            setLocked(true);
            
            if(progressObserver != null)
            {
                if(progressObserver.isInterrupted())
                {
                    interrupted();
                }
                else if(progressObserver.isFinished())
                {
                    finished();   
                }
                else if(progressObserver.getMaxProgress() - progressObserver.getProgress() <= 0)
                {
                    finished();
                }
                else
                {
                    setProgress();
                }
            }
        }
    } 
    
    /** Adds a PropertyChangeListener to the listener list.
     * @param l The listener to add.
     *
     */
    public void addPropertyChangeListener(java.beans.PropertyChangeListener l)
    {
        propertyChangeSupport.addPropertyChangeListener(l);
    }    
    
    /** Removes a PropertyChangeListener from the listener list.
     * @param l The listener to remove.
     *
     */
    public void removePropertyChangeListener(java.beans.PropertyChangeListener l)
    {
        propertyChangeSupport.removePropertyChangeListener(l);
    }    
    
    /** Getter for property finished.
     * @return Value of property finished.
     *
     */
    protected boolean isFinished()
    {
        return this.progressObserver.isFinished();
    }
    
    /** Getter for property interrupted.
     * @return Value of property interrupted.
     *
     */
    protected boolean isInterrupted()
    {
        return this.progressObserver.isInterrupted();
    }
    
    // -------------------------------------------------------------------------
    // Testing Stuff
    // -------------------------------------------------------------------------
    
    /*private void test()
    {
        ProgressObserver po = new ProgressObserver(10000, 100);
        po.setInterruptible(false);
        po.setIndeterminate(true);
        final TimerTestMethod ttm = new TimerTestMethod(po);
        
        this.setBorder(new EtchedBorder());
        
        JFrame jf = new JFrame("ProgressPanel");
        jf.getContentPane().setLayout(new GridLayout());
        jf.getContentPane().add(this);
        jf.setLocationRelativeTo(null);
        jf.setUndecorated(true);
        
        jf.pack();
        jf.setVisible(true);
        
        SwingUtilities.invokeLater(new Runnable() 
        {
            public void run()
            {
                try
                {
                    invokeMethod(ttm);
                }
                catch(Exception exp)
                {
                    exp.printStackTrace();
                }
            }
        });
    }
    
    public static void main(String args[])
    {
        org.apache.log4j.BasicConfigurator.configure();
        
        ProgressPanel pp = new ProgressPanel();
        pp.test();
    }
    
    private class TimerTestMethod extends MultithreadedMethod
    {
        private TimerTestMethod(ProgressObserver progressObserver)
        {
            super(progressObserver);   
        }
        
        int i = 0;
        String message = "Fortschritt ";
        
        protected void doInvoke()
        {
            progressObserver.setInterruptible(true);
            
            while(i < progressObserver.getMaxProgress())
            {
                try
                {
                    if(progressObserver.isInterrupted())
                    {
                        throw new InterruptedException();
                    }
                    
                    logger.info("warte 1000 ms");
                    Thread.currentThread().sleep(100);
                    progressObserver.setProgress((i += 50), (message += "."));
                }
                catch(InterruptedException iexp)
                {
                    this.interrupt();
                    
                    try
                    {
                        progressObserver.setMessage("unterbrochen");
                        progressObserver.setInterrupted(true);
                    }
                    catch(InterruptedException irexp) {}
                    
                    i = progressObserver.getMaxProgress();
                    logger.warn("interrupted");
                    //iexp.printStackTrace();
                }
            }
            
            try
            {
                progressObserver.setMessage("fertig");
                progressObserver.setProgress(progressObserver.getMaxProgress());
                progressObserver.setFinished(true);
            }
            catch(InterruptedException iexp) {}
                
        }  
    }*/
}
