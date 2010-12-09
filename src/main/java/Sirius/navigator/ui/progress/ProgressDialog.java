/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * ProgressDialog.java
 *
 * Created on 18. Juni 2003, 11:13
 */
package Sirius.navigator.ui.progress;

import Sirius.navigator.method.*;

import java.awt.*;

import java.beans.*;

import javax.swing.*;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class ProgressDialog extends JDialog {

    //~ Instance fields --------------------------------------------------------

    protected ProgressPanel progressPanel;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of ProgressDialog.
     */
    public ProgressDialog() {
        super(new JFrame(), true);
        this.init();
    }

    /**
     * Creates a new instance of ProgressDialog.
     *
     * @param  owner  DOCUMENT ME!
     */
    public ProgressDialog(final JFrame owner) {
        super(owner, true);
        this.init();
    }

    /**
     * Creates a new ProgressDialog object.
     *
     * @param  owner  DOCUMENT ME!
     */
    public ProgressDialog(final JDialog owner) {
        super(owner, true);
        this.init();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    protected void init() {
        progressPanel = new ProgressPanel();
        progressPanel.addPropertyChangeListener(new DialogClosingListener());

        this.setUndecorated(true);
        this.getContentPane().setLayout(new GridLayout());
        this.getContentPane().add(progressPanel);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  method  DOCUMENT ME!
     */
    public void show(final MultithreadedMethod method) // throws Exception
    {
        this.pack();
        this.progressPanel.invokeMethod(method);
        super.show();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  method     DOCUMENT ME!
     * @param  arguments  DOCUMENT ME!
     */
    public void show(final MultithreadedMethod method, final Object arguments) // throws Exception
    {
        this.pack();
        this.progressPanel.invokeMethod(method, arguments);
        super.show();
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * -------------------------------------------------------------------------.
     *
     * @version  $Revision$, $Date$
     */
    private class DialogClosingListener implements PropertyChangeListener {

        //~ Methods ------------------------------------------------------------

        @Override
        public void propertyChange(final PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals("finished") || evt.getPropertyName().equals("interrupted")) // NOI18N
            {
                if (((Boolean)evt.getNewValue()).booleanValue()) {
                    if (progressPanel.logger.isDebugEnabled()) {
                        progressPanel.logger.debug("closing progress dialog");                           // NOI18N
                    }
                    ProgressDialog.this.setVisible(false);
                    ProgressDialog.this.dispose();
                }
            }
        }
    }

    // TEST ====================================================================

    /*private void test()
     * { ProgressObserver po = new ProgressObserver(10000, 100); po.setInterruptible(false); po.setIndeterminate(true);
     * final TimerTestMethod ttm = new TimerTestMethod(po);
     *
     * SwingUtilities.invokeLater(new Runnable()  {     public void run()     {         try         { show(ttm);         }
     *        catch(Exception exp)         {             exp.printStackTrace();         }     } }); }
     *
     * public static void main(String args[]) { org.apache.log4j.BasicConfigurator.configure();  ProgressDialog pd = new
     * ProgressDialog(); pd.test(); }
     *
     * private class TimerTestMethod extends MultithreadedMethod { private TimerTestMethod(ProgressObserver
     * progressObserver) {     super(progressObserver);    }  int i = 0; String message = "Fortschritt ";  protected
     * void doInvoke() {     progressObserver.setInterruptible(true);          while(i <
     * progressObserver.getMaxProgress())     {         try         {             if(progressObserver.isInterrupted())
     *     {                 throw new InterruptedException();             } //logger.info("warte 1000 ms");
     * Thread.currentThread().sleep(100); progressObserver.setProgress((i += 50), (message += "."));         }
     * catch(InterruptedException iexp)    {             this.interrupt();                          try             {
     * progressObserver.setMessage("unterbrochen");                 progressObserver.setInterrupted(true);             }
     *           catch(InterruptedException irexp) {}                          i = progressObserver.getMaxProgress();
     *     //logger.warn("interrupted");             //iexp.printStackTrace();         }     }          try     {
     * progressObserver.setMessage("fertig"); progressObserver.setProgress(progressObserver.getMaxProgress());
     * progressObserver.setFinished(true);
     * }     catch(InterruptedException iexp) {}          }  }*/
}
