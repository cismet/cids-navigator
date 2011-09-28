/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui;

import javafx.application.Platform;

import javafx.embed.swing.JFXPanel;

import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import net.infonode.docking.DockingWindow;
import net.infonode.docking.DockingWindowListener;
import net.infonode.docking.OperationAbortedException;
import net.infonode.docking.View;

import org.apache.log4j.Logger;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

/**
 * DOCUMENT ME!
 *
 * @author   jweintraut
 * @version  $Revision$, $Date$
 */
public class DescriptionPaneFX extends DescriptionPane implements DockingWindowListener {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(DescriptionPaneFX.class);

    //~ Instance fields --------------------------------------------------------

    private JFXPanel jfxPanel;

    private WebView wbvBrowser;
    private WebEngine wbeEngine;
    private Scene scene;
    private boolean sceneSet = false;

    private JPopupMenu popupMenu;
    private JMenuItem mnuItem_openInExternalBrowser;
    private String pageURI;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DescriptionPaneFX object.
     */
    public DescriptionPaneFX() {
        super();

        initComponents();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    private void initComponents() {
        LOG.fatal("Initialize components.");

        mnuItem_openInExternalBrowser = new JMenuItem(org.openide.util.NbBundle.getMessage(
                    DescriptionPaneFX.class,
                    "DescriptionPaneFX.btn_openInSystemBrowser.text"),
                new javax.swing.ImageIcon(
                    getClass().getResource("/Sirius/navigator/ui/world.png")));
        mnuItem_openInExternalBrowser.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(final ActionEvent e) {
                    if ((pageURI != null) && (pageURI.trim().length() > 0)) {
                        try {
                            de.cismet.tools.BrowserLauncher.openURL(pageURI);
                        } catch (Exception ex) {
                            LOG.error("Couldn't open URI '" + pageURI + "' in external browser.", ex);
                        }
                    }
                }
            });

        popupMenu = new JPopupMenu();
        popupMenu.add(mnuItem_openInExternalBrowser);

        jfxPanel = new JFXPanel();
        jfxPanel.setBackground(Color.red);
        jfxPanel.add(popupMenu);
        jfxPanel.addMouseListener(new PopupListener(popupMenu));

        add(jfxPanel, "html");

        // Build the scene in JavaFX Event Thread
        Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    LOG.fatal("Build scene.");
                    wbeEngine = new WebEngine();
                    wbvBrowser = new WebView(wbeEngine);

                    scene = new Scene(wbvBrowser);

                    /*LOG.fatal("Set stylesheet.");
                     * LOG.fatal(scene.getStylesheets()); wbvBrowser.setId("id-of-browser"); LOG.fatal("Bounds in Local:
                     * " + wbvBrowser.getBoundsInLocal()); LOG.fatal("Bounds in Parent: " +
                     * wbvBrowser.getBoundsInParent()); for (final String stylesheet : scene.getStylesheets()) {
                     * LOG.fatal(stylesheet); } scene.getStylesheets().clear(); scene.getStylesheets()     .add(
                     * getClass().getClassLoader().getResource("Sirius/navigator/javafx.css") .toExternalForm());
                     * scene.getStylesheets()     .add(
                     * "E:\\NetBeansProjects\\de\\cismet\\cids\\navigator\\cids-navigator\\trunk\\src\\main\\resources\\Sirius\\navigator\\javafx.css");
                     * for (final String stylesheet : scene.getStylesheets()) { LOG.fatal(stylesheet); }
                     * LOG.fatal(scene.getStylesheets());LOG.fatal("Stylesheet set.");*/

                    LOG.fatal("Scene built.");
                }
            });

        LOG.fatal("Components initialized.");
    }

    @Override
    public void clear() {
        setScene();

        LOG.error("clear");

        Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    wbeEngine.loadContent(blankPage);
                }
            });
    }

    @Override
    public void setPageFromURI(final String page) {
        setScene();

        if ((page == null) || (page.trim().length() <= 0)) {
            setPageFromContent(
                blankPage,
                getClass().getClassLoader().getResource("Sirius/navigator/resource/doc/blank.xhtml").toString());
            return;
        }

        LOG.error("setPageFromURI(" + page + ")");

        Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    wbeEngine.load(page);
                    SwingUtilities.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                pageURI = page;
                                mnuItem_openInExternalBrowser.setEnabled(true);
                            }
                        });
                }
            });
    }

    @Override
    public void setPageFromContent(final String page) {
        setScene();

        LOG.error("setPageFromContent(" + page + ")");

        Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    wbeEngine.loadContent(page);
                    SwingUtilities.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                pageURI = null;
                                mnuItem_openInExternalBrowser.setEnabled(false);
                            }
                        });
                }
            });
    }

    @Override
    public void setPageFromContent(final String page, final String baseURL) {
        setScene();

        LOG.error("setPageFromContent(" + page + ", " + baseURL + ")");

        Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    wbeEngine.loadContent(page);
                    SwingUtilities.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                pageURI = null;
                                mnuItem_openInExternalBrowser.setEnabled(false);
                            }
                        });
                }
            });
    }

    /**
     * DOCUMENT ME!
     */
    private void initJFXPanelAgain() {
        LOG.fatal("Initialize JFXPanel.");
        remove(jfxPanel);
        jfxPanel = new JFXPanel();
        jfxPanel.add(popupMenu);
        jfxPanel.addMouseListener(new PopupListener(popupMenu));
        add(jfxPanel, "html");
        sceneSet = false;
        LOG.fatal("JFXPanel initialized.");
    }

    /**
     * DOCUMENT ME!
     */
    private void setScene() {
        LOG.fatal("Set scene.");
        if (!sceneSet) {
            jfxPanel.setScene(scene);
            sceneSet = true;
            LOG.fatal("Scene set.");
        }
    }

    @Override
    public void windowAdded(final DockingWindow addedToWindow, final DockingWindow addedWindow) {
        printStackTrace("windowAdded");
    }

    @Override
    public void windowRemoved(final DockingWindow removedFromWindow, final DockingWindow removedWindow) {
        printStackTrace("windowRemoved");
    }

    @Override
    public void windowShown(final DockingWindow window) {
        printStackTrace("windowShown");
        setScene();
    }

    @Override
    public void windowHidden(final DockingWindow window) {
        printStackTrace("windowHidden");
    }

    @Override
    public void viewFocusChanged(final View previouslyFocusedView, final View focusedView) {
        printStackTrace("viewFocusChanged");
    }

    @Override
    public void windowClosing(final DockingWindow window) throws OperationAbortedException {
        printStackTrace("windowClosing");
    }

    @Override
    public void windowClosed(final DockingWindow window) {
        printStackTrace("windowClosed");
    }

    @Override
    public void windowUndocking(final DockingWindow window) throws OperationAbortedException {
        printStackTrace("windowUndocking");

        initJFXPanelAgain();
    }

    @Override
    public void windowUndocked(final DockingWindow window) {
        printStackTrace("windowUndocked");

        setScene();
        if (!showsWaitScreen) {
            showHTML();
        }
    }

    @Override
    public void windowDocking(final DockingWindow window) throws OperationAbortedException {
        printStackTrace("windowDocking");

        initJFXPanelAgain();
    }

    @Override
    public void windowDocked(final DockingWindow window) {
        printStackTrace("windowDocked");

        setScene();
    }

    @Override
    public void windowMinimizing(final DockingWindow window) throws OperationAbortedException {
        printStackTrace("windowMinimizing");
    }

    @Override
    public void windowMinimized(final DockingWindow window) {
        printStackTrace("windowMinimized");
    }

    @Override
    public void windowMaximizing(final DockingWindow window) throws OperationAbortedException {
        printStackTrace("windowMaximizing");

        initJFXPanelAgain();
    }

    @Override
    public void windowMaximized(final DockingWindow window) {
        printStackTrace("windowMaximized");

        setScene();
    }

    @Override
    public void windowRestoring(final DockingWindow window) throws OperationAbortedException {
        printStackTrace("windowRestoring");
    }

    @Override
    public void windowRestored(final DockingWindow window) {
        printStackTrace("windowRestored");
    }

    /**
     * DOCUMENT ME!
     *
     * @param  message  DOCUMENT ME!
     */
    private void printStackTrace(final String message) {
        LOG.fatal(message, new Exception(Thread.currentThread().toString()));
    }
}
