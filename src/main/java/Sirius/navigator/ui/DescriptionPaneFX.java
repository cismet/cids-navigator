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

import javafx.concurrent.Worker.State;

import javafx.embed.swing.JFXPanel;

import javafx.geometry.Insets;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import org.apache.log4j.Logger;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLAnchorElement;

import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import de.cismet.tools.BrowserLauncher;

import de.cismet.tools.gui.FXWebViewPanel;

/**
 * An implementation of DescriptionPane which uses JavaFX WebKit Component to render XHTML content.
 *
 * @author   daniel
 * @version  $Revision$, $Date$
 */
public class DescriptionPaneFX extends DescriptionPane {

    //~ Static fields/initializers ---------------------------------------------

    private static FXWebViewPanel browserPanel;
    private static final Logger LOG = Logger.getLogger(DescriptionPaneFX.class);

    //~ Instance fields --------------------------------------------------------

    private JPopupMenu popupMenu;
    private JMenuItem mnuItem_openInExternalBrowser;
//    private NavigatorJsBridgeImpl bridge = new NavigatorJsBridgeImpl();

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DescriptionPaneFX object.
     */
    public DescriptionPaneFX() {
        Platform.setImplicitExit(false);
        browserPanel = new FXWebViewPanel();
        add(browserPanel, "html");
        popupMenu = new JPopupMenu();
        mnuItem_openInExternalBrowser = new JMenuItem(org.openide.util.NbBundle.getMessage(
                    DescriptionPaneFS.class,
                    "DescriptionPaneFS.btn_openInSystemBrowser.text"),
                new javax.swing.ImageIcon(
                    getClass().getResource("/Sirius/navigator/ui/world.png")));
        mnuItem_openInExternalBrowser.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent e) {
                    final WebEngine webEng = browserPanel.getWebEngine();
                    if (webEng != null) {
                        final String pageURI = webEng.getLocation();
                        if ((pageURI != null) && (pageURI.trim().length() > 0)) {
                            try {
                                de.cismet.tools.BrowserLauncher.openURL(pageURI);
                            } catch (Exception ex) {
                                LOG.error("Couldn't open URI '" + pageURI + "' in external browser.", ex);
                            }
                        }
                    }
                }
            });
        popupMenu.add(mnuItem_openInExternalBrowser);
        browserPanel.add(popupMenu);
        browserPanel.addMouseListener(new PopupListener(popupMenu));
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void setPageFromURI(final String page) {
        browserPanel.loadUrl(page);
        SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    DescriptionPaneFX.this.invalidate();
                    DescriptionPaneFX.this.repaint();
                }
            });
    }

    @Override
    public void setPageFromContent(final String page) {
        browserPanel.loadContent(page);
    }

    @Override
    public void setPageFromContent(final String page,
            final String baseURL) {
        browserPanel.loadContent(page);
    }
}
