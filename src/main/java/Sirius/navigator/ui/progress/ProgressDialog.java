/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui.progress;

import Sirius.navigator.method.MultithreadedMethod;

import java.awt.GridLayout;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDialog;
import javax.swing.JFrame;

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
    public void show(final MultithreadedMethod method) {
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
    public void show(final MultithreadedMethod method, final Object arguments) {
        this.pack();
        this.progressPanel.invokeMethod(method, arguments);
        super.show();
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
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
}
