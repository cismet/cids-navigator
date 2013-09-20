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
