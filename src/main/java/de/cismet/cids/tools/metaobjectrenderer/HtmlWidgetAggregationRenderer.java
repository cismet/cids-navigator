/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.tools.metaobjectrenderer;

import Sirius.navigator.ui.ComponentRegistry;
import Sirius.navigator.ui.DescriptionPaneFX;
import Sirius.navigator.ui.FXBrowserPane;
import Sirius.navigator.ui.InitialisationLocker;

import javafx.application.Platform;

import javafx.beans.value.ObservableValue;

import javafx.concurrent.Worker;

import javafx.scene.SnapshotResult;

import javafx.util.Callback;

import org.apache.log4j.Logger;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;

import java.util.Collection;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.CountDownLatch;

import javax.swing.JComponent;
import javax.swing.JPanel;

import de.cismet.cids.dynamics.CidsBean;

import de.cismet.cids.editors.HtmlWidgetEditor;

/**
 * DOCUMENT ME!
 *
 * @author   daniel
 * @version  $Revision$, $Date$
 */
public class HtmlWidgetAggregationRenderer extends JPanel implements CidsBeanAggregationRenderer,
    InitialisationLocker,
    Observer {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(HtmlWidgetAggregationRenderer.class);

    //~ Instance fields --------------------------------------------------------

    boolean bridgeRegistered = false;
    private Collection<CidsBean> cidsBeans = null;
    private String title = "";
    private FXBrowserPane browserPanel;
    private boolean hasChanged = false;
    private final CardLayout cardLayout;
    private CountDownLatch initLatch = new CountDownLatch(1);

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new HtmlWidgetAggregationRenderer object.
     *
     * @param  url  DOCUMENT ME!
     */
    public HtmlWidgetAggregationRenderer(final String url) {
        this.setOpaque(false);
        final JComponent descPane = ComponentRegistry.getRegistry().getDescriptionPane();
        this.setSize(descPane.getSize());
        browserPanel = new FXBrowserPane(descPane.getSize().height, descPane.getSize().width);
        this.setPreferredSize(descPane.getPreferredSize());
        this.setMinimumSize(descPane.getMinimumSize());
        final Observer observer = new Observer() {

                @Override
                public void update(final Observable o, final Object arg) {
                    HtmlWidgetAggregationRenderer.this.update(o, arg);
                }
            };
        browserPanel.addBridgeObserver(observer);
        browserPanel.setOpaque(false);
        this.setMinimumSize(new Dimension(500, 500));
        this.setPreferredSize(new Dimension(5000, 5000));
        Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    browserPanel.getWebEngine()
                            .getLoadWorker()
                            .stateProperty()
                            .addListener(new javafx.beans.value.ChangeListener<Worker.State>() {

                                    @Override
                                    public void changed(final ObservableValue<? extends Worker.State> ov,
                                            final Worker.State oldState,
                                            final Worker.State newState) {
                                        if (!bridgeRegistered) {
                                            bridgeRegistered = browserPanel.registerJ2JSBridge();
                                            if ((cidsBeans != null) && bridgeRegistered) {
                                                browserPanel.injectCidsBeans(cidsBeans);
                                            }
                                        }
                                        if (newState == Worker.State.FAILED) {
                                            // ToDo: show an error page
                                            LOG.fatal("Can not load editor at " + url + ". showing default error page");
                                            // browserPanel.loadErrorPage();
                                        }
                                    }
                                });
                    browserPanel.getWebEngine().load(url);
                }
            });
        cardLayout = new CardLayout();
        final JPanel htmlPanel = new JPanel();
        htmlPanel.setLayout(new BorderLayout());
        htmlPanel.add(browserPanel, BorderLayout.CENTER);
        this.setLayout(cardLayout);
        add(htmlPanel, "htmlPanel");
        cardLayout.show(this, "htmlPanel");
        this.setOpaque(true);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Collection<CidsBean> getCidsBeans() {
        return cidsBeans;
    }

    @Override
    public void setCidsBeans(final Collection<CidsBean> beans) {
        // ToDo: needs to be implemented...
    }

    @Override
    public void dispose() {
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(final String title) {
        this.title = title;
    }

    @Override
    public CountDownLatch getInitialisationLatch() {
        return initLatch;
    }

    @Override
    public void update(final Observable o, final Object arg) {
        if (arg instanceof String) {
            if (arg.equals("showHTML")) {
                browserPanel.getWebView().snapshot(new Callback<SnapshotResult, Void>() {

                        @Override
                        public Void call(final SnapshotResult p) {
                            initLatch.countDown();
                            return null;
                        }
                    }, null, null);
            }
        }
    }
}
