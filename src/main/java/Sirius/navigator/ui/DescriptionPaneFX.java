/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui;


import javafx.application.Platform;

import javafx.scene.web.WebEngine;

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

// @Override
// protected void performSetNode(final DefaultMetaTreeNode n) {
// final String descriptionURL = n.getDescription();
// // besorge MO zum parametrisieren der URL
// if (n.isObjectNode()) {
// final MetaObject o = ((ObjectTreeNode)n).getMetaObject();
// final ClassAttribute widgetAttribute = o.getMetaClass().getClassAttribute("isHtmlWidget");
// if (widgetAttribute != null) {
// final String widgetUrl = (String)widgetAttribute.getValue();
// if (LOG.isDebugEnabled()) {
// LOG.debug("loading html widget from url '" + widgetUrl + "' for bean " + o.getBean().getMOString()); // NOI18N
// }
// // ToDo: set the WebView content based on Convention? or Configuartion?
// setPageFromURI(widgetUrl, o.getBean());
// } else {
// breadCrumbModel.startWithNewCrumb(new CidsMetaObjectBreadCrumb(o) {
//
// @Override
// public void crumbActionPerformed(final java.awt.event.ActionEvent e) {
// startSingleRendererWorker(o, n.toString());
// }
// });
// startSingleRendererWorker(n);
// }
// } else if (n.isPureNode() && (n.getDescription() != null)) {
// if (LOG.isDebugEnabled()) {
// LOG.debug("loading description from url '" + descriptionURL + "'"); // NOI18N
// }
// setPageFromURI(descriptionURL);
// showHTML();
// } else {
// startNoDescriptionRenderer();
// }
// showsWaitScreen = false;
// }
    @Override
    public void setPageFromURI(final String page) {
        Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    browserPanel.getWebEngine().load(page);
                }
            });
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
