/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui;

import javafx.application.Platform;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.embed.swing.JFXPanel;

import javafx.event.EventHandler;

import javafx.geometry.Insets;

import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import netscape.javascript.JSException;
import netscape.javascript.JSObject;

import org.apache.log4j.Logger;

import org.jfree.util.Log;

import org.openide.util.Exceptions;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

import de.cismet.cids.dynamics.CidsBean;

/**
 * DOCUMENT ME!
 *
 * @author   daniel
 * @version  $Revision$, $Date$
 */
public class FXBrowserPane extends JPanel {

    //~ Static fields/initializers ---------------------------------------------

    private static JFXPanel browserFxPanel;
    private static final Logger LOG = Logger.getLogger(FXBrowserPane.class);

    //~ Instance fields --------------------------------------------------------

    private WebEngine webEng = null;
    private WebView webView;
    private JSObject cidsJs;
    private Pane browserPane;
    private NavigatorJsBridgeImpl bridge = new NavigatorJsBridgeImpl();

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new FXBrowserPane object.
     */
    public FXBrowserPane() {
        Platform.setImplicitExit(false);
        browserFxPanel = new JFXPanel();
        Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    browserPane = createBrowser();
                    final Scene scene = new Scene(browserPane);
                    /*
                     *  shortcut mouse listener for enabling firefox ToDo. make the shortcut configurable
                     */
                    scene.setOnMouseClicked(new EventHandler<MouseEvent>() {

                            @Override
                            public void handle(final MouseEvent event) {
                                // TODO Auto-generated method stub
                                if (event.isControlDown() && event.isAltDown()
                                            && event.getButton().equals(MouseButton.SECONDARY)) {
                                    enableFirebug(webEng);
                                }
                            }
                        });
                    scene.getStylesheets().add(this.getClass().getResource("javaFxContextMenu.css").toExternalForm());
                    browserFxPanel.setScene(scene);
                }
            });
//        this.setPreferredSize(new Dimension(5000, 5000));
//        this.setMinimumSize(new Dimension(5000, 5000));
        this.setLayout(new BorderLayout());
        add(browserFxPanel, BorderLayout.CENTER);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  engine  DOCUMENT ME!
     */
    private void enableFirebug(final WebEngine engine) {
//        final MetaClass classMap = bridge.getClass("WUNDA_BLAU", "VERMESSUNG_RISS", null, null);
//        LOG.fatal("result of getClass: " + classMap);
        engine.executeScript(
            "if (!document.getElementById('FirebugLite')){E = document['createElement' + 'NS'] && document.documentElement.namespaceURI;E = E ? document['createElement' + 'NS'](E, 'script') : document['createElement']('script');E['setAttribute']('id', 'FirebugLite');E['setAttribute']('src', 'https://getfirebug.com/' + 'firebug-lite-debug.js' + '#startOpened');E['setAttribute']('FirebugLite', '4');(document['getElementsByTagName']('head')[0] || document['getElementsByTagName']('body')[0]).appendChild(E);E = new Image;E['setAttribute']('src', 'https://getfirebug.com/firebug-lite-debug.js' + '#startOpened');}");
    }

    /**
     * can only be called on FX application Thread !
     *
     * @return  DOCUMENT ME!
     */
    private Pane createBrowser() {
        webView = new WebView();
//         disabling the context menue
        // custom context menue are not possible atm see https://javafx-jira.kenai.com/browse/RT-20306
        webView.setContextMenuEnabled(false);

        // disabling scoll bars
// view.getChildrenUnmodifiable().addListener(new ListChangeListener<Node>() {
// public void onChanged(ListChangeListener.Change<? extends Node> change) {
// Set<Node> deadSeaScrolls = view.lookupAll(".scroll-bar");
// for (Node scroll : deadSeaScrolls) {
// scroll.setVisible(false);
// }
// }
// });

        webEng = webView.getEngine();
        webEng.getLoadWorker().exceptionProperty().addListener(new ChangeListener<Throwable>() {

                @Override
                public void changed(final ObservableValue<? extends Throwable> ov,
                        final Throwable t,
                        final Throwable t1) {
                    LOG.error("Error in Wb Engine Load Worker", t);
                }
            });

        final BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(5));
        pane.setCenter(webView);
        return pane;
    }

    /**
     * Must be called on FX Application Thread.
     *
     * @return  DOCUMENT ME!
     */
    public boolean registerJ2JSBridge() {
        /*
         * per convention we assume that there is an Object with name beanManager ToDo: maybe we can add the Bridge
         * Object directyl to the window with a conventional name check out what is better
         */
        try {
            cidsJs = (JSObject)webEng.executeScript("ci");
//        cidsJs.setMember("jBridge", bridge);
            cidsJs.call("setBackend", bridge);
            return cidsJs != null;
        } catch (JSException ex) {
            LOG.error(
                "Could not register Bridge Object for communication between Java and JavaScript",
                ex); // NOI18N
        }
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  bean  DOCUMENT ME!
     */
    public void injectCidsBean(final CidsBean bean) {
        Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    try {
                        /*
                         * per convention we assume that the object we bind the bridge to (CidsJS) has an method
                         * injectBean see the comment for registerJ2JSBridge
                         */
                        cidsJs.call("injectBean", CidsBean.getCidsBeanObjectMapper().writeValueAsString(bean));
                    } catch (Exception e) {
                        LOG.error("could not inject bean in HTML 5 Widget", e);
                    }
                }
            });
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public WebEngine getWebEngine() {
        return webEng;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public CidsBean getCidsBean() {
        final String jsonBean = (String)cidsJs.call("retrieveBean");
        if (LOG.isDebugEnabled()) {
            LOG.debug("changed bean in widget: " + jsonBean);
        }
        try {
            final CidsBean cb = CidsBean.createNewCidsBeanFromJSON(false, jsonBean);
//            final CidsBean cb = CidsBean.createNewCidsBeanFromJSON(false, jsonBean);
            return cb;
        } catch (Exception ex) {
            LOG.fatal("Could not parse json representation of cidsBean: " + jsonBean, ex);
        }

        return null;
    }
}
