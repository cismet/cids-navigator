/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.tools;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class MetaToolkit {

    //~ Static fields/initializers ---------------------------------------------

    private static Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();

    //~ Methods ----------------------------------------------------------------

    /**
     * Methode zentriert ein \u00FCbergebenes Fenster auf dem Bildschirm.
     *
     * @param  win  Window Dieses Fenster soll zentriert werden
     */
    public static void centerWindow(final Window win) {
        final Dimension winDim = win.getSize();
        // if larger than screen, reduce window width or height
        if (screenDim.width < winDim.width) {
            win.setSize(screenDim.width, winDim.height);
        }
        if (screenDim.height < winDim.height) {
            win.setSize(winDim.width, screenDim.height);
        }
        // center frame, dialogue or window on screen
        final int x = (screenDim.width - winDim.width) / 2;
        final int y = (screenDim.height - winDim.height) / 2;
        win.setLocation(x, y);
    }

    /**
     * DOCUMENT ME!
     */
    public void listCurrentThreads() {
        final ThreadGroup currentGroup = Thread.currentThread().getThreadGroup();
        final int numThreads = currentGroup.activeCount();
        final Thread[] listOfThreads = new Thread[numThreads];

        currentGroup.enumerate(listOfThreads);
        for (int i = 0; i < numThreads; i++) {
            System.out.println("Thread #" + i + " = " + listOfThreads[i].getName()); // NOI18N
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   m  DOCUMENT ME!
     * @param   n  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static double dRound(final double m, final int n) {
        double d = m;
        final int mult = (int)Math.pow(10, n);
        d *= mult;
        final long long_d = Math.round(d);
        d = (double)long_d / (double)mult;

        return d;
    }
}
