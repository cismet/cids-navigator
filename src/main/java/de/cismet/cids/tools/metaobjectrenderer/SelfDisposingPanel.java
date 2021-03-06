/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.tools.metaobjectrenderer;

import Sirius.navigator.ui.DisposableAgent;
import Sirius.navigator.ui.RequestsFullSizeComponent;

import java.beans.PropertyChangeListener;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JComponent;
import javax.swing.JPanel;

import de.cismet.cids.dynamics.Disposable;
import de.cismet.cids.dynamics.DisposableCidsBeanStore;

import de.cismet.tools.gui.CoolEditor;

/**
 * DOCUMENT ME!
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public class SelfDisposingPanel extends JPanel {

    //~ Static fields/initializers ---------------------------------------------

    private static final int CHECK_INTERVAL = 7500;

    //~ Instance fields --------------------------------------------------------

    private final transient Disposable disposableBeanStore;
    private final transient Timer checkTimer;

    private transient PropertyChangeListener strongListenerReference;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new SelfDisposingPanel object.
     */
    public SelfDisposingPanel() {
        this((DisposableCidsBeanStore)null);
    }

    /**
     * Creates a new SelfDisposingPanel object.
     *
     * @param  disposableBeanStore  DOCUMENT ME!
     */
    public SelfDisposingPanel(final JComponent disposableBeanStore) {
        this((disposableBeanStore instanceof Disposable) ? (Disposable)disposableBeanStore : (Disposable)null);
    }

    /**
     * Creates a new SelfDisposingPanel object.
     *
     * @param  disposableBeanStore  DOCUMENT ME!
     */
    public SelfDisposingPanel(final Disposable disposableBeanStore) {
        this.disposableBeanStore = disposableBeanStore;
        if (disposableBeanStore == null) {
            checkTimer = null;
        } else {
            checkTimer = new Timer();
        }
        setOpaque(false);
        startChecking();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    private void startChecking() {
        if (checkTimer != null) {
            checkTimer.scheduleAtFixedRate(new TimerTask() {

                    @Override
                    public void run() {
                        // TODO: this should not only check for the parent but rather if the component is in the
                        // windows hierarchy!
                        if (DisposableAgent.getInstance().isRegistered(disposableBeanStore)) {
                            // just unregister and wait for the next cycle
                            DisposableAgent.getInstance().unregister(disposableBeanStore);
                        } else {
                            if (getParent() == null) {
                                checkTimer.cancel();
                                DisposableAgent.getInstance().dispose(disposableBeanStore);
                                setStrongListenerReference(null);
                                if (getParent() instanceof CoolEditor) {
                                    ((CoolEditor)getParent()).setOriginalComponent(null);
                                }
                            }
                        }
                    }
                }, 0, CHECK_INTERVAL);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  strongListenerReference  DOCUMENT ME!
     */
    public void setStrongListenerReference(final PropertyChangeListener strongListenerReference) {
        this.strongListenerReference = strongListenerReference;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public PropertyChangeListener getStrongListenerReference() {
        return strongListenerReference;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean requestsFullSize() {
        return disposableBeanStore instanceof RequestsFullSizeComponent;
    }
}
