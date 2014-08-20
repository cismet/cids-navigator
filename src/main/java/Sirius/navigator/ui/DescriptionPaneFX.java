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

/**
 * An implementation of DescriptionPane which uses JavaFX WebKit Component to render XHTML content.
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
    private Pane browserPane;
    private JPopupMenu popupMenu;
    private JMenuItem mnuItem_openInExternalBrowser;
//    private NavigatorJsBridgeImpl bridge = new NavigatorJsBridgeImpl();

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
                    browserFxPanel.setScene(scene);
                }
            });
        add(browserFxPanel, "html");
        popupMenu = new JPopupMenu();
        mnuItem_openInExternalBrowser = new JMenuItem(org.openide.util.NbBundle.getMessage(
                    DescriptionPaneFS.class,
                    "DescriptionPaneFS.btn_openInSystemBrowser.text"),
                new javax.swing.ImageIcon(
                    getClass().getResource("/Sirius/navigator/ui/world.png")));
        mnuItem_openInExternalBrowser.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent e) {
                    final String pageURI = webEng.getLocation();
                    if ((pageURI != null) && (pageURI.trim().length() > 0)) {
                        try {
                            de.cismet.tools.BrowserLauncher.openURL(pageURI);
                        } catch (Exception ex) {
                            LOG.error("Couldn't open URI '" + pageURI + "' in external browser.", ex);
                        }
                    }
                }
            });
        popupMenu.add(mnuItem_openInExternalBrowser);
        browserFxPanel.add(popupMenu);
        browserFxPanel.addMouseListener(new PopupListener(popupMenu));
    }

    //~ Methods ----------------------------------------------------------------

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
        // every time a new document was loaded, we need to add listeners to the a elements in that document, that check
        // if that link represents a non hml document we want to open in the system browser
        webEng.getLoadWorker().stateProperty().addListener(
            new ChangeListener<State>() {

                @Override
                public void changed(final ObservableValue ov, final State oldState, final State newState) {
                    if (newState == State.SUCCEEDED) {
                        addClickListenerToLinks();
                    }
                }
            });

        final BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(5));
        pane.setCenter(webView);
        return pane;
    }

    /**
     * this method adds a EventListener to each link element in the loaded document. The added EventListner checks if
     * the href of the link points to a non html document or anchor and if so opens he url in the external browser
     */
    private void addClickListenerToLinks() {
        final NodeList nodeList = webEng.getDocument().getElementsByTagName("a");
        for (int i = 0; i < nodeList.getLength(); i++) {
            final Node node = nodeList.item(i);
            final EventTarget eventTarget = (EventTarget)node;
            eventTarget.addEventListener("click", new EventListener() {

                    @Override
                    public void handleEvent(final Event evt) {
                        final EventTarget target = evt.getCurrentTarget();
                        final HTMLAnchorElement anchorElement = (HTMLAnchorElement)target;
                        final String href = anchorElement.getHref();

                        if ((href != null) && !href.endsWith("html") && !href.contains("#")) {
                            openInSystemBrowser(href);
                            evt.preventDefault();
                        }
                    }
                }, false);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  url  DOCUMENT ME!
     */
    private void openInSystemBrowser(final String url) {
        try {
            if (url != null) {
                BrowserLauncher.openURL(url);
            }
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    @Override
    public void setPageFromURI(final String page) {
        Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    webEng.load(page);
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
