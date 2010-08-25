package de.cismet.reconnector;

import java.awt.BorderLayout;
import javax.swing.JPanel;

/**
 *
 * @author jruiz
 */
public class ReconnectorWrapperPanel extends JPanel implements ReconnectorListener {

    private final JPanel panel;
    private final ReconnectorPanel reconnectorPanel;

    public ReconnectorWrapperPanel(final JPanel panel, final ReconnectorPanel reconnectorPanel) {
        setLayout(new BorderLayout());
        this.panel = panel;
        this.reconnectorPanel = reconnectorPanel;
        //reconnectorPanel.setMinimumSize(panel.getMinimumSize());
        //reconnectorPanel.setMaximumSize(panel.getMaximumSize());
        //setPreferredSize(panel.getPreferredSize());
        showReconnector(false);
    }

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
    public void connectionFailed(ReconnectorEvent event) {
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
