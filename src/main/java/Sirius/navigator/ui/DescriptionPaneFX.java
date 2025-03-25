/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui;

import Sirius.navigator.resource.PropertyManager;

import javafx.application.Platform;

import javafx.scene.web.WebEngine;

import org.apache.log4j.Logger;

import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import de.cismet.tools.gui.FXWebViewPanel;
import de.cismet.tools.gui.GUIWindow;

/**
 * An implementation of DescriptionPane which uses JavaFX WebKit Component to render XHTML content.
 *
 * @author   daniel
 * @version  $Revision$, $Date$
 */
@org.openide.util.lookup.ServiceProvider(service = GUIWindow.class)
public class DescriptionPaneFX extends DescriptionPane implements GUIWindow {

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

    @Override
    public JComponent getGuiComponent() {
        return this;
    }

    @Override
    public String getPermissionString() {
        if (PropertyManager.getManager().getDescriptionPaneHtmlRenderer().equals(PropertyManager.FX_HTML_RENDERER)) {
            return GUIWindow.NO_PERMISSION;
        } else {
            return "DescriptionPaneFX";
        }
    }

    @Override
    public String getViewTitle() {
        return null;
    }

    @Override
    public Icon getViewIcon() {
        return null;
    }

    /**
     * DOCUMENT ME!
     */
    public void refresh() {
        browserPanel.refresh();
    }
}
