/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.editors;

import Sirius.navigator.ui.DescriptionPaneFX;
import Sirius.navigator.ui.FXBrowserPane;
import Sirius.navigator.ui.RequestsFullSizeComponent;

import javafx.application.Platform;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.concurrent.Worker;

import org.apache.log4j.Logger;

import java.awt.BorderLayout;
import java.awt.Dimension;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import de.cismet.cids.dynamics.CidsBean;

import de.cismet.cids.tools.metaobjectrenderer.CidsBeanRenderer;

import de.cismet.tools.gui.DoNotWrap;

/**
 * DOCUMENT ME!
 *
 * @author   daniel
 * @version  $Revision$, $Date$
 */
public class HtmlWidgetEditor extends JPanel implements DoNotWrap,
    RequestsFullSizeComponent,
    CidsBeanRenderer,
    EditorSaveListener,
    PropertyChangeListener,
    Observer {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(DescriptionPaneFX.class);

    //~ Instance fields --------------------------------------------------------

    boolean bridgeRegistered = false;
    private FXBrowserPane browserPanel = new FXBrowserPane();
    private CidsBean cb = null;
    private boolean editable;
    private String title = "";
    private boolean hasChanged = false;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new HtmlWidgetEditor object.
     *
     * @param  url  DOCUMENT ME!
     */
    public HtmlWidgetEditor(final String url) {
        this(url, true);
    }

    /**
     * Creates a new HtmlWidgetEditor object.
     *
     * @param  url       DOCUMENT ME!
     * @param  editable  DOCUMENT ME!
     */
    public HtmlWidgetEditor(final String url, final boolean editable) {
        this.editable = editable;
        final Observer observer = new Observer() {

                @Override
                public void update(final Observable o, final Object arg) {
                    HtmlWidgetEditor.this.update(o, arg);
                }
            };
        browserPanel.addBridgeObserver(observer);
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(5000, 500));
        add(browserPanel, BorderLayout.CENTER);
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
                                        if (newState == Worker.State.FAILED) {
                                            // ToDo: show an error page
                                            LOG.fatal("Can not load editor at " + url + ". showing default error page");
                                            // browserPanel.loadErrorPage();
                                        } else if ((newState == Worker.State.SUCCEEDED)
                                            && (oldState != Worker.State.SUCCEEDED)) {
                                            bridgeRegistered = browserPanel.registerJ2JSBridge();
                                        }
                                    }
                                });
                    browserPanel.getWebEngine().load(url);
                }
            });
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
                        browserPanel.injectCidsBean(cb, editable);
                    } else {
                        browserPanel.getWebEngine()
                                .getLoadWorker()
                                .stateProperty()
                                .addListener(new javafx.beans.value.ChangeListener<Worker.State>() {

                                        @Override
                                        public void changed(final ObservableValue<? extends Worker.State> ov,
                                                final Worker.State oldState,
                                                final Worker.State newState) {
                                            if ((newState == Worker.State.SUCCEEDED)
                                                && (oldState != Worker.State.SUCCEEDED)) {
                                                if (!bridgeRegistered) {
                                                    bridgeRegistered = browserPanel.registerJ2JSBridge();
                                                }
                                                browserPanel.injectCidsBean(cb, editable);
                                                browserPanel.getWebEngine()
                                                .getLoadWorker()
                                                .stateProperty()
                                                .removeListener(this);
                                            }
                                        }
                                    });
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
        if (!hasChanged) {
//            if (arg instanceof CidsBean) {
//                final CidsBean newBean = (CidsBean)arg;
//                cb.bulkUpdate(newBean);
//            }
            cb.setArtificialChangeFlag(true);
            hasChanged = true;
        }
    }
}
