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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.application.Platform;

import javafx.beans.value.ObservableValue;

import javafx.concurrent.Worker;

import org.apache.log4j.Logger;

import java.awt.BorderLayout;
import java.awt.Dimension;

import java.io.IOException;
import java.io.InputStream;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

import de.cismet.cids.dynamics.CidsBean;
import de.cismet.cids.dynamics.DisposableCidsBeanStore;

import de.cismet.tools.gui.DoNotWrap;

/**
 * DOCUMENT ME!
 *
 * @author   daniel
 * @version  $Revision$, $Date$
 */
public class HtmlWidgetEditor extends JPanel implements DoNotWrap,
    RequestsFullSizeComponent,
    DisposableCidsBeanStore,
    EditorSaveListener {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(DescriptionPaneFX.class);

    //~ Instance fields --------------------------------------------------------

    boolean bridgeRegistered = false;
    private FXBrowserPane browserPanel = new FXBrowserPane();
    private CidsBean cb = null;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new HtmlWidgetEditor object.
     *
     * @param  url  DOCUMENT ME!
     */
    public HtmlWidgetEditor(final String url) {
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
//        ToDO: setting the change flag only if the bean has changed instead of always setting the change flag
        cb.setArtificialChangeFlag(true);
        Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    final double totalWork = browserPanel.getWebEngine().getLoadWorker().getTotalWork();
                    final double workDone = browserPanel.getWebEngine().getLoadWorker().getWorkDone();
                    if ((totalWork == workDone) && bridgeRegistered) {
                        browserPanel.injectCidsBean(cb);
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
                                                browserPanel.injectCidsBean(cb);
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

    /**
     * DOCUMENT ME!
     *
     * @param   args  beanToUpdate DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     * @throws  Exception    DOCUMENT ME!
     */
    /**
     * DOCUMENT ME!
     *
     * @param   args  DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     * @throws  Exception    DOCUMENT ME!
     */
    public static void main(final String[] args) throws IOException, Exception {
        final InputStream is = HtmlWidgetEditor.class.getClassLoader()
                    .getResourceAsStream("de/cismet/cids/editors/test.json");
        final ObjectMapper mapper = new ObjectMapper();
        final JsonNode json = mapper.readTree(is);
        final String s = json.toString();
        final CidsBean cb = CidsBean.createNewCidsBeanFromJSON(false, s);
        System.out.println("CidsBean " + cb);
    }
}
