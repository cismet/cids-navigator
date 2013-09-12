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
import Sirius.server.middleware.types.MetaClass;
import Sirius.server.middleware.types.MetaObject;

import javafx.application.Platform;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.concurrent.Worker;

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

import javax.swing.SwingUtilities;

/**
 * An implementation of DescriptionPane which uses JavaFX WebKit Component to render XHTML content.
 *
 * @author   daniel
 * @version  $Revision$, $Date$
 */
public class DescriptionPaneFX extends DescriptionPane {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(DescriptionPaneFX.class);

    //~ Instance fields --------------------------------------------------------

    private FXBrowserPane browserPanel = new FXBrowserPane();
    private WebEngine webEng = null;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DescriptionPaneFX object.
     */
    public DescriptionPaneFX() {
        webEng = browserPanel.getWebEngine();
        add(browserPanel, "html");
    }

    //~ Methods ----------------------------------------------------------------

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
//                    InputStream is = WebAccessManager.getInstance().doRequest(new URL(uri));
//                    browserPanel.getWebEngine()().loadContent(page);
                    browserPanel.getWebEngine().load(page);
                    if (bean != null) {
                        /*
                         * If loading of the page is finished, we inject the cidsBean
                         */
                        browserPanel.getWebEngine()
                                .getLoadWorker()
                                .stateProperty()
                                .addListener(
                                    new ChangeListener<Worker.State>() {

                                        @Override
                                        public void changed(final ObservableValue<? extends Worker.State> ov,
                                                final Worker.State oldState,
                                                final Worker.State newState) {
                                            if (newState == Worker.State.FAILED) {
                                                startNoDescriptionRenderer();
                                            } else if ((newState == Worker.State.SUCCEEDED)
                                                && (oldState != Worker.State.SUCCEEDED)) {
                                                final boolean bridgeRegistered = browserPanel.registerJ2JSBridge();
                                                if (bridgeRegistered) {
                                                    browserPanel.injectCidsBean(bean);

                                                    browserPanel.getWebEngine()
                                                        .getLoadWorker()
                                                        .stateProperty()
                                                        .removeListener(this);
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
//                SwingUtilities.invokeLater(new Runnable() {
//                    @Override
//                    public void run() {
//                        DescriptionPaneFX.this.invalidate();
//                        DescriptionPaneFX.this.repaint();
//                    }
//                });
                }
            });
    }

    @Override
    public void setPageFromURI(final String page) {
        setPageFromURI(page, null);
    }

    @Override
    public void setPageFromContent(final String page) {
        Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    browserPanel.getWebEngine().loadContent(page);
                }
            });
    }

    @Override
    public void setPageFromContent(final String page,
            final String baseURL) {
        Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    browserPanel.getWebEngine().load(page);
                }
            });
    }
}
