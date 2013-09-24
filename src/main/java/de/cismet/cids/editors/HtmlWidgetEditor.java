/**
 * *************************************************
 *
 * cismet GmbH, Saarbruecken, Germany
 * 
* ... and it just works.
 * 
***************************************************
 */
package de.cismet.cids.editors;

import Sirius.navigator.ui.ComponentRegistry;
import Sirius.navigator.ui.DescriptionPaneFX;
import Sirius.navigator.ui.FXBrowserPane;
import Sirius.navigator.ui.InitialisationLocker;
import Sirius.navigator.ui.RequestsFullSizeComponent;

import javafx.application.Platform;

import javafx.beans.value.ObservableValue;

import javafx.concurrent.Worker;

import javafx.scene.SnapshotResult;

import javafx.util.Callback;

import org.apache.log4j.Logger;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.CountDownLatch;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.cismet.cids.dynamics.CidsBean;

import de.cismet.cids.tools.metaobjectrenderer.CidsBeanRenderer;

import de.cismet.tools.gui.DoNotWrap;

/**
 * DOCUMENT ME!
 *
 * @author daniel
 * @version $Revision$, $Date$
 */
public class HtmlWidgetEditor extends JPanel implements DoNotWrap,
        RequestsFullSizeComponent,
        CidsBeanRenderer,
        EditorSaveListener,
        PropertyChangeListener,
        Observer,
        InitialisationLocker {

    //~ Static fields/initializers ---------------------------------------------
    private static final Logger LOG = Logger.getLogger(DescriptionPaneFX.class);
    //~ Instance fields --------------------------------------------------------
    protected javax.swing.JLabel lblRendererCreationWaitingLabel;
    boolean bridgeRegistered = false;
    private FXBrowserPane browserPanel;
    private CidsBean cb = null;
    private boolean editable;
    private String title = "";
    private boolean hasChanged = false;
    private final CardLayout cardLayout;
    private CountDownLatch initLatch = new CountDownLatch(1);
//    private boolean keepOnMovin = false;

    //~ Constructors -----------------------------------------------------------
    /**
     * Creates a new HtmlWidgetEditor object.
     *
     * @param url DOCUMENT ME!
     */
    public HtmlWidgetEditor(final String url) {
        this(url, true);
    }

    /**
     * Creates a new HtmlWidgetEditor object.
     *
     * @param url DOCUMENT ME!
     * @param editable DOCUMENT ME!
     */
    public HtmlWidgetEditor(final String url, final boolean editable) {
        this.editable = editable;
        this.setOpaque(false);
        final JComponent descPane = ComponentRegistry.getRegistry().getDescriptionPane();
        this.setSize(descPane.getSize());
        browserPanel = new FXBrowserPane(descPane.getSize().height, descPane.getSize().width);
        this.setPreferredSize(descPane.getPreferredSize());
        this.setMinimumSize(descPane.getMinimumSize());
        final Observer observer = new Observer() {
            @Override
            public void update(final Observable o, final Object arg) {
                HtmlWidgetEditor.this.update(o, arg);
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
                            if ((cb != null) && bridgeRegistered) {
                                browserPanel.injectCidsBean(cb, editable);
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
        final JPanel waitPanel = new JPanel();

        waitPanel.setLayout(new GridBagLayout());
//        waitPanel.setBackground(Color.GREEN);
        lblRendererCreationWaitingLabel = new javax.swing.JLabel();
        lblRendererCreationWaitingLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblRendererCreationWaitingLabel.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/Sirius/navigator/resource/img/load.png"))); // NOI18N
//        final ComponentWrapper cw = CidsObjectEditorFactory.getInstance().getComponentWrapper();
//        final JComponent wrappedComp = (JComponent)cw.wrapComponent(lblRendererCreationWaitingLabel);
        final GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        waitPanel.add(new JLabel(""), gridBagConstraints);
//        add(waitPanel, "waitPanel");
        add(htmlPanel, "htmlPanel");
//        cardLayout.show(this, "waitPanel");
        cardLayout.show(this, "htmlPanel");
//        this.setLayout(new BorderLayout());
//        this.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.setOpaque(true);
//        this.setBackground(Color.red);
//        add(browserPanel, BorderLayout.CENTER);
    }

    //~ Methods ----------------------------------------------------------------
    @Override
    public CidsBean getCidsBean() {
        return cb;
    }

    @Override
    public void setCidsBean(final CidsBean cidsBean) {
        this.cb = cidsBean;
        this.cb.addPropertyChangeListener(this);
//        ToDO: setting the change flag only if the bean has changed instead of always setting the change flag
//        cb.setArtificialChangeFlag(true);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                final double totalWork = browserPanel.getWebEngine().getLoadWorker().getTotalWork();
                final double workDone = browserPanel.getWebEngine().getLoadWorker().getWorkDone();
                if ((totalWork == workDone) && bridgeRegistered) {
//                        browserPanel.injectCidsBean(cb, editable);
                }
            }
        });
    }

    @Override
    public void dispose() {
//        browserPanel.dispose();
    }

    @Override
    public void editorClosed(final EditorClosedEvent event) {
    }

    @Override
    public boolean prepareForSave() {
        cb.bulkUpdate(browserPanel.getCidsBean());
        LOG.fatal((cb.getMOString()));
        return true;
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
    public void propertyChange(final PropertyChangeEvent evt) {
        if (bridgeRegistered) {
            browserPanel.injectCidsBean(cb, editable);
        }
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
        if (!hasChanged) {
            cb.setArtificialChangeFlag(true);
            hasChanged = true;
        }
    }

    @Override
    public CountDownLatch getInitialisationLatch() {
        return initLatch;
    }
}
