/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 *  Copyright (C) 2010 srichter
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.cismet.cids.tools.metaobjectrenderer;

import Sirius.navigator.ui.RequestsFullSizeComponent;

import java.beans.PropertyChangeListener;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JComponent;
import javax.swing.JPanel;

import de.cismet.cids.dynamics.Disposable;
import de.cismet.cids.dynamics.DisposableCidsBeanStore;

/**
 * DOCUMENT ME!
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public class SelfDisposingPanel extends JPanel {

    //~ Static fields/initializers ---------------------------------------------

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SelfDisposingPanel.class);
    private static final int CHECK_INTERVAL = 7500;

    //~ Instance fields --------------------------------------------------------

// public JPanel getEncapsulatedPanel() {
// return encapsulatedPanel;
// }
// private final JPanel encapsulatedPanel;
    private final Disposable disposableBeanStore;
    private PropertyChangeListener strongListenerReference;
    private final Timer checkTimer;

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
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    public void startChecking() {
        if (checkTimer != null) {
            checkTimer.scheduleAtFixedRate(new TimerTask() {

                    @Override
                    public void run() {
                        // TODO: this should not only check for the parent but rather if the component is in the
                        // windows hierarchy!
                        if (getParent() == null) {
                            SelfDisposingPanel.this.disposableBeanStore.dispose();
                            SelfDisposingPanel.this.checkTimer.cancel();
                        }
                    }
                }, CHECK_INTERVAL, CHECK_INTERVAL);
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
