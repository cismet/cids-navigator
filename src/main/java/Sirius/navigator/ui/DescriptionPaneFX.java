/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui;

import Sirius.navigator.types.treenode.DefaultMetaTreeNode;
import Sirius.navigator.types.treenode.ObjectTreeNode;

import Sirius.server.localserver.attribute.ClassAttribute;
import Sirius.server.middleware.types.MetaObject;

import javafx.application.Platform;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.concurrent.Worker;

import javafx.embed.swing.JFXPanel;

import javafx.geometry.Insets;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import netscape.javascript.JSException;
import netscape.javascript.JSObject;

import org.apache.log4j.Logger;

import javax.swing.SwingUtilities;

import de.cismet.cids.dynamics.CidsBean;

/**
 * ToDo adapt comment An implementation of DescriptionPane which uses JavaFX WebKit Component to render XHTML content.
 * The retrieval of XHTML documents is done by WebAccessManager. Therefore FLying Saucer is configured to use
 * WebAccessUserAgent which acts as an adapter for WebAccessManager. In order to read invalid XHTML documents or HTML
 * documents, Tagsoup is used as XMLReader.
 *
 * @author   daniel
 * @version  $Revision$, $Date$
 */
public class DescriptionPaneFX extends DescriptionPane {

    //~ Static fields/initializers ---------------------------------------------

    private static JFXPanel browserFxPanel;
    private static final Logger LOG = Logger.getLogger(DescriptionPaneFX.class);

    //~ Instance fields --------------------------------------------------------

    private WebEngine webEng = null;
    private WebView webView;
    private JSObject cidsBeanService;
    private Pane browserPane;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DescriptionPaneFX object.
     */
    public DescriptionPaneFX() {
        Platform.setImplicitExit(false);
        browserFxPanel = new JFXPanel();
        Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    browserPane = createBrowser();
                    final Scene scene = new Scene(browserPane);
                    scene.getStylesheets().add(this.getClass().getResource("javaFxContextMenu.css").toExternalForm());
                    browserFxPanel.setScene(scene);
                }
            });
        add(browserFxPanel, "html");

        /*
         *  shortcut mouse listener for enabling firefox
         */

    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  engine  DOCUMENT ME!
     */
    private static void enableFirebug(final WebEngine engine) {
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

        final BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(5));
        pane.setCenter(webView);
        return pane;
    }

    @Override
    protected void performSetNode(final DefaultMetaTreeNode n) {
        final String descriptionURL = n.getDescription();
        // besorge MO zum parametrisieren der URL
        if (n.isObjectNode()) {
            final MetaObject o = ((ObjectTreeNode)n).getMetaObject();
            final ClassAttribute widgetAttribute = o.getMetaClass().getClassAttribute("isHtmlWidget");
            if (widgetAttribute != null) {
                final String widgetUrl = (String)widgetAttribute.getValue();
                if (LOG.isDebugEnabled()) {
                    LOG.debug("loading html widget from url '" + widgetUrl + "' for bean " + o.getBean().getMOString()); // NOI18N
                }
                // ToDo: set the WebView content based on Convention? or Configuartion?
                setPageFromURI(widgetUrl, o.getBean());
            } else {
                breadCrumbModel.startWithNewCrumb(new CidsMetaObjectBreadCrumb(o) {

                        @Override
                        public void crumbActionPerformed(final java.awt.event.ActionEvent e) {
                            startSingleRendererWorker(o, n.toString());
                        }
                    });
                startSingleRendererWorker(n);
            }
        } else if (n.isPureNode() && (n.getDescription() != null)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("loading description from url '" + descriptionURL + "'"); // NOI18N
            }
            setPageFromURI(descriptionURL);
            showHTML();
        } else {
            startNoDescriptionRenderer();
        }
        showsWaitScreen = false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  page  DOCUMENT ME!
     * @param  bean  DOCUMENT ME!
     */
    private void setPageFromURI(final String page, final CidsBean bean) {
        Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    webEng.load(page);
                    if (bean != null) {
                        /*
                         * If loading of the page is finished, we inject the cidsBean
                         */
                        webEng.getLoadWorker().stateProperty().addListener(
                            new ChangeListener<Worker.State>() {

                                @Override
                                public void changed(final ObservableValue<? extends Worker.State> ov,
                                        final Worker.State oldState,
                                        final Worker.State newState) {
                                    if ((newState == Worker.State.SUCCEEDED) && (oldState != Worker.State.SUCCEEDED)) {
                                        try {
                                            final boolean bridgeRegistered = registerJ2JSBridge();
                                            if (bridgeRegistered) {
                                                injectCidsBean(bean);

                                                webEng.getLoadWorker().stateProperty().removeListener(this);
                                            }
                                        } catch (JSException ex) {
                                            if (LOG.isDebugEnabled()) {
                                                LOG.debug(
                                                    "Could not register Bridge Object for communication between Java and JavaScript",
                                                    ex); // NOI18N
                                            }
                                        }
                                        SwingUtilities.invokeLater(new Runnable() {

                                                @Override
                                                public void run() {
                                                    showHTML();
                                                }
                                            });
                                    }
                                }
                            });
                    }
                    SwingUtilities.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                DescriptionPaneFX.this.invalidate();
                                DescriptionPaneFX.this.repaint();
                            }
                        });
                }
            });
    }

    @Override
    public void setPageFromURI(final String page) {
        setPageFromURI(page, null);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private boolean registerJ2JSBridge() {
        /*
         * per convention we assume that there is an Object with name beanManager ToDo: maybe we can add the Bridge
         * Object directyl to the window with a conventional name check out what is better
         */
        cidsBeanService = (JSObject)webEng.executeScript("ci.beanManager");
        cidsBeanService.setMember("jBridge", new J2JSBridge());
        return cidsBeanService != null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  bean  DOCUMENT ME!
     */
    private void injectCidsBean(final CidsBean bean) {
        try {
            /*
             * per convention we assume that the object we bind the bridge to has an method injectBean see the comment
             * for registerJ2JSBridge
             */
            cidsBeanService.call("injectBean", bean.toJSONString(false));
        } catch (Exception e) {
            LOG.fatal("could not inject bean in HTML 5 Widget", e);
        }
    }

    @Override
    public void setPageFromContent(final String page) {
        Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    webEng.loadContent(page);
                }
            });
    }

    @Override
    public void setPageFromContent(final String page,
            final String baseURL) {
        Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    webEng.load(page);
                }
            });
    }
}
