/*
 * ProgressDialog.java
 *
 * Created on 18. Juni 2003, 11:13
 */

package Sirius.navigator.ui.progress;

import java.awt.*;
import javax.swing.*;
import java.beans.*;

import Sirius.navigator.method.*;

/**
 *
 * @author  pascal
 */
public class ProgressDialog extends JDialog
{
    protected ProgressPanel progressPanel;
    
    /** Creates a new instance of ProgressDialog */
    public ProgressDialog()
    {
        super(new JFrame(), true);
        this.init();
    }
    
    /** Creates a new instance of ProgressDialog */
    public ProgressDialog(JFrame owner)
    {
        super(owner, true);
        this.init();
    }
    
    public ProgressDialog(JDialog owner)
    {
        super(owner, true);
        this.init();
    }
    
    protected void init()
    {
        progressPanel = new ProgressPanel();
        progressPanel.addPropertyChangeListener(new DialogClosingListener());
        
        this.setUndecorated(true);
        this.getContentPane().setLayout(new GridLayout());
        this.getContentPane().add(progressPanel);
         
    } 
    
    public void show (MultithreadedMethod method) //throws Exception
    {
        this.pack();
        this.progressPanel.invokeMethod(method);
        super.show();
        
    }
    
    public void show (MultithreadedMethod method, Object arguments) //throws Exception
    {
        this.pack();
        this.progressPanel.invokeMethod(method, arguments);
        super.show();
        
    }
    
    // -------------------------------------------------------------------------
    
    private class DialogClosingListener implements PropertyChangeListener
    {
        public void propertyChange(PropertyChangeEvent evt)
        {
            if(evt.getPropertyName().equals("finished") || evt.getPropertyName().equals("interrupted"))
            {
                if(((Boolean)evt.getNewValue()).booleanValue())
                {
                    progressPanel.logger.debug("closing progress dialog");
                    ProgressDialog.this.setVisible(false);
                    ProgressDialog.this.dispose();
                }
            }
        } 
    }
    
    // TEST ====================================================================
    
    /*private void test()
    {
        ProgressObserver po = new ProgressObserver(10000, 100);
        po.setInterruptible(false);
        po.setIndeterminate(true);
        final TimerTestMethod ttm = new TimerTestMethod(po);

        SwingUtilities.invokeLater(new Runnable() 
        {
            public void run()
            {
                try
                {
                    show(ttm);
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
        
        ProgressDialog pd = new ProgressDialog();
        pd.test();
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
                    
                    //logger.info("warte 1000 ms");
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
                    //logger.warn("interrupted");
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
