/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.reconnector;

import java.awt.BorderLayout;

import javax.swing.JPanel;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class ReconnectorWrapperPanel extends JPanel implements ReconnectorListener {

    //~ Instance fields --------------------------------------------------------

    private final JPanel panel;
    private final ReconnectorPanel reconnectorPanel;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ReconnectorWrapperPanel object.
     *
     * @param  panel             DOCUMENT ME!
     * @param  reconnectorPanel  DOCUMENT ME!
     */
    public ReconnectorWrapperPanel(final JPanel panel, final ReconnectorPanel reconnectorPanel) {
        setLayout(new BorderLayout());
        this.panel = panel;
        this.reconnectorPanel = reconnectorPanel;
        // reconnectorPanel.setMinimumSize(panel.getMinimumSize());
        // reconnectorPanel.setMaximumSize(panel.getMaximumSize());
        // setPreferredSize(panel.getPreferredSize());
        showReconnector(false);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  showReconnector  DOCUMENT ME!
     */
    public final void showReconnector(final boolean showReconnector) {
        removeAll();
        if (showReconnector) {
            add(reconnectorPanel, BorderLayout.CENTER);
        } else {
            add(panel, BorderLayout.CENTER);
        }
        revalidate();
        validate();
        repaint();
    }

    @Override
    public void connecting() {
        showReconnector(true);
    }

    @Override
    public void connectionFailed(final ReconnectorEvent event) {
        showReconnector(true);
    }

    @Override
    public void connectionCanceled() {
        showReconnector(false);
    }

    @Override
    public void connectionCompleted() {
        showReconnector(true);
    }
}
