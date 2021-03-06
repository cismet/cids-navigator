/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * Lagis.java
 *
 * Created on 16. M\u00E4rz 2007, 12:10
 */
package Sirius.navigator;

import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;

import net.infonode.docking.DockingWindow;
import net.infonode.docking.DockingWindowListener;
import net.infonode.docking.OperationAbortedException;
import net.infonode.docking.RootWindow;
import net.infonode.docking.SplitWindow;
import net.infonode.docking.TabWindow;
import net.infonode.docking.View;
import net.infonode.docking.mouse.DockingWindowActionMouseButtonListener;
import net.infonode.docking.properties.RootWindowProperties;
import net.infonode.docking.theme.DockingWindowsTheme;
import net.infonode.docking.theme.ShapedGradientDockingTheme;
import net.infonode.docking.util.DeveloperUtil;
import net.infonode.docking.util.DockingUtil;
import net.infonode.docking.util.PropertiesUtil;
import net.infonode.docking.util.StringViewMap;
import net.infonode.gui.componentpainter.AlphaGradientComponentPainter;
import net.infonode.gui.componentpainter.GradientComponentPainter;
import net.infonode.tabbedpanel.TabAreaVisiblePolicy;
import net.infonode.util.Direction;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import de.cismet.tools.gui.log4jquickconfig.Log4JQuickConfig;

/**
 * DOCUMENT ME!
 *
 * @author   Puhl
 * @version  $Revision$, $Date$
 */
public class NavigatorXOrig extends javax.swing.JFrame {

    //~ Static fields/initializers ---------------------------------------------

    private static final int ICON_SIZE = 8;
    private static final Icon VIEW_ICON = new Icon() {

            @Override
            public int getIconHeight() {
                return ICON_SIZE;
            }

            @Override
            public int getIconWidth() {
                return ICON_SIZE;
            }

            @Override
            public void paintIcon(final Component c, final Graphics g, final int x, final int y) {
                final Color oldColor = g.getColor();

                g.setColor(new Color(70, 70, 70));
                g.fillRect(x, y, ICON_SIZE, ICON_SIZE);

                g.setColor(new Color(100, 230, 100));
                g.fillRect(x + 1, y + 1, ICON_SIZE - 2, ICON_SIZE - 2);

                g.setColor(oldColor);
            }
        };

    //~ Instance fields --------------------------------------------------------

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());

    private RootWindow rootWindow;

    // Panels

    // Views
    private View vCatalogue;
    private View vSearchResults;
    private View vObjectInfo;
    private View vDescription;
    private View vEditor;

    private View vKarte;
    private View vNKF;
    private View vRechteDetail;
    private View vRessort;

    // Icons & Image
    private Icon icoDescr = new javax.swing.ImageIcon(getClass().getResource(
                "/Sirius/navigator/resource/imgx/descriptionpane_icon.gif"));    // NOI18N
    private Icon icoCatalogue = new javax.swing.ImageIcon(getClass().getResource(
                "/Sirius/navigator/resource/imgx/catalogue_tree_icon.gif"));     // NOI18N
    private Icon icoAttributetable = new javax.swing.ImageIcon(getClass().getResource(
                "/Sirius/navigator/resource/imgx/attributetable_icon.gif"));     // NOI18N
    private Icon icoSearchresults = new javax.swing.ImageIcon(getClass().getResource(
                "/Sirius/navigator/resource/imgx/searchresults_tree_icon.gif")); // NOI18N

    private StringViewMap viewMap = new StringViewMap();

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton16;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JPanel panAll;
    private javax.swing.JPanel panMain;
    private javax.swing.JPanel panStatusbar;
    private javax.swing.JPanel panToolbar;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form Lagis.
     */
    public NavigatorXOrig() {
        Log4JQuickConfig.configure4LumbermillOnLocalhost();
        try {
            javax.swing.UIManager.setLookAndFeel(new PlasticXPLookAndFeel());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        initComponents();
        initPanels();
        initInfoNode();
        doLayoutInfoNode();
        doConfigKeystrokes();

        // adding infonode(seperate panel) to the frame
        panMain.add(rootWindow, BorderLayout.CENTER);
        // getContentPane().add(pFlurstueck);
        // pFlurstueck.setVisible(true);
        // setVisible(true);
        // getContentPane().add(panMain);
        // setSize(1000,750);
        // setTitle("Lagis Prototyp");
        rootWindow.addListener(new DockingWindowListener() {

                @Override
                public void viewFocusChanged(final View view, final View view0) {
                }
                @Override
                public void windowAdded(final DockingWindow dockingWindow, final DockingWindow dockingWindow0) {
                    if (log.isDebugEnabled()) {
                        log.debug("windowAdded"); // NOI18N
                    }
                }
                @Override
                public void windowClosed(final DockingWindow dockingWindow) {
                }
                @Override
                public void windowClosing(final DockingWindow dockingWindow) throws OperationAbortedException {
                }
                @Override
                public void windowDocked(final DockingWindow dockingWindow) {
                    if (log.isDebugEnabled()) {
                        log.debug("windowDocked"); // NOI18N
                    }
                }
                @Override
                public void windowDocking(final DockingWindow dockingWindow) throws OperationAbortedException {
                    if (log.isDebugEnabled()) {
                        log.debug("windowDocking"); // NOI18N
                    }
                }
                @Override
                public void windowHidden(final DockingWindow dockingWindow) {
                }
                @Override
                public void windowMaximized(final DockingWindow dockingWindow) {
                }
                @Override
                public void windowMaximizing(final DockingWindow dockingWindow) throws OperationAbortedException {
                }
                @Override
                public void windowMinimized(final DockingWindow dockingWindow) {
                }
                @Override
                public void windowMinimizing(final DockingWindow dockingWindow) throws OperationAbortedException {
                }
                @Override
                public void windowRemoved(final DockingWindow dockingWindow, final DockingWindow dockingWindow0) {
                }
                @Override
                public void windowRestored(final DockingWindow dockingWindow) {
                    if (log.isDebugEnabled()) {
                        log.debug("windowRestored"); // NOI18N
                    }
                }
                @Override
                public void windowRestoring(final DockingWindow dockingWindow) throws OperationAbortedException {
                    if (log.isDebugEnabled()) {
                        log.debug("windowRestoring"); // NOI18N
                    }
                }
                @Override
                public void windowShown(final DockingWindow dockingWindow) {
                    if (log.isDebugEnabled()) {
                        log.debug("windowShown"); // NOI18N
                    }
                    if (log.isInfoEnabled()) {
                        log.info(
                            "dockingWindow.getParent().getBounds().getY():"
                                    + dockingWindow.getParent().getBounds().getY()); // NOI18N
                    }
                }
                @Override
                public void windowUndocked(final DockingWindow dockingWindow) {
                }
                @Override
                public void windowUndocking(final DockingWindow dockingWindow) throws OperationAbortedException {
                }
            });
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    private void initPanels() {
//        pFlurstueck = new FlurstueckPanel();
//        pVertraege= new VertraegePanel();
//        pNKFOverview= new NKFOverviewPanel();
//        pDMS= new DMSPanel();
//        pKarte= new KartenPanel();
//        pNKF= new NKFPanel();
//        pRechteDetail= new RechteDetailPanel();
//        pRessort= new RessortPanel();
    }

    /**
     * DOCUMENT ME!
     */
    private void initInfoNode() {
        vCatalogue = new View(org.openide.util.NbBundle.getMessage(
                    NavigatorXOrig.class,
                    "NavigatorXOrig.vCatalogue.title"),
                icoCatalogue,
                null); // NOI18N

        viewMap.addView("Katalog", vCatalogue); // NOI18N

        vObjectInfo = new View(org.openide.util.NbBundle.getMessage(
                    NavigatorXOrig.class,
                    "NavigatorXOrig.vObjectInfo.title"),
                icoAttributetable,
                null);                                             // NOI18N
        viewMap.addView("Informationene zum Objekt", vObjectInfo); // NOI18N

        vDescription = new View(org.openide.util.NbBundle.getMessage(
                    NavigatorXOrig.class,
                    "NavigatorXOrig.vDescription.title"),
                icoDescr,
                null);                                 // NOI18N
        viewMap.addView("Beschreibung", vDescription); // NOI18N

        vEditor = new View(org.openide.util.NbBundle.getMessage(NavigatorXOrig.class, "NavigatorXOrig.vEditor.title"),
                icoAttributetable,
                null);                              // NOI18N
        viewMap.addView("Attributeditor", vEditor); // NOI18N

        vSearchResults = new View(org.openide.util.NbBundle.getMessage(
                    NavigatorXOrig.class,
                    "NavigatorXOrig.vSearchResults.title"),
                icoSearchresults,
                null);                                     // NOI18N
        viewMap.addView("Suchergebnisse", vSearchResults); // NOI18N

//        vNKF= new View("Nutzung",null,null);
//        viewMap.addView("Nutzung",vNKF);
//
//        vRechteDetail= new View("Rechte & Belastungen",null,null);
//        viewMap.addView("Rechte & Belastungen",vRechteDetail);
//
//        vRessort= new View("Ressort",null,null);
//        viewMap.addView("Ressort",vRessort);
//
        rootWindow = DockingUtil.createRootWindow(viewMap, true);

        // InfoNode configuration
        rootWindow.addTabMouseButtonListener(DockingWindowActionMouseButtonListener.MIDDLE_BUTTON_CLOSE_LISTENER);
        // DockingWindowsTheme theme = new
        // ShapedGradientDockingTheme(0f,0.5f,UIManagerColorProvider.TABBED_PANE_DARK_SHADOW,new
        // FixedColorProvider(Color.BLUE),true);
        final DockingWindowsTheme theme = new ShapedGradientDockingTheme();
        rootWindow.getRootWindowProperties().addSuperObject(
            theme.getRootWindowProperties());

        final RootWindowProperties titleBarStyleProperties = PropertiesUtil.createTitleBarStyleRootWindowProperties();

        rootWindow.getRootWindowProperties().addSuperObject(
            titleBarStyleProperties);

        rootWindow.getRootWindowProperties().getDockingWindowProperties().setUndockEnabled(true);
        final AlphaGradientComponentPainter x = new AlphaGradientComponentPainter(
                java.awt.SystemColor.inactiveCaptionText,
                java.awt.SystemColor.activeCaptionText,
                java.awt.SystemColor.activeCaptionText,
                java.awt.SystemColor.inactiveCaptionText);
        rootWindow.getRootWindowProperties().getDragRectangleShapedPanelProperties().setComponentPainter(x);

        rootWindow.getRootWindowProperties()
                .getViewProperties()
                .getViewTitleBarProperties()
                .getNormalProperties()
                .getShapedPanelProperties()
                .setComponentPainter(new GradientComponentPainter(
                        new Color(124, 160, 221),
                        new Color(236, 233, 216),
                        new Color(124, 160, 221),
                        new Color(236, 233, 216)));
    }

    /**
     * DOCUMENT ME!
     */
    public void doLayoutInfoNode() {
        rootWindow.getRootWindowProperties()
                .getTabWindowProperties()
                .getTabbedPanelProperties()
                .getTabAreaProperties()
                .setTabAreaVisiblePolicy(TabAreaVisiblePolicy.ALWAYS);

        final TabWindow cat = new TabWindow(
                new DockingWindow[] {
                    vCatalogue,
                    vSearchResults
                });

        final TabWindow main = new TabWindow(
                new DockingWindow[] {
                    vDescription,
                    vEditor
                });

        cat.getTabWindowProperties().getTabbedPanelProperties().setTabAreaOrientation(Direction.UP);
        main.getTabWindowProperties().getTabbedPanelProperties().setTabAreaOrientation(Direction.UP);
        rootWindow.setWindow(new SplitWindow(
                true,
                0.23710318f,
                new SplitWindow(false, 0.6081461f,
                    cat,
                    vObjectInfo),
                main));

        vEditor.restoreFocus();
        vSearchResults.restoreFocus();
    }

    /**
     * DOCUMENT ME!
     */
    public void doConfigKeystrokes() {
        final KeyStroke configLoggerKeyStroke = KeyStroke.getKeyStroke('L', InputEvent.CTRL_MASK);
        final Action configAction = new AbstractAction() {

                @Override
                public void actionPerformed(final ActionEvent e) {
                    java.awt.EventQueue.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                DeveloperUtil.createWindowLayoutFrame(
                                        org.openide.util.NbBundle.getMessage(
                                            NavigatorXOrig.class,
                                            "NavigatorXOrig.doConfigKeystrokes().JFrame_anon1.title"), // NOI18N
                                        rootWindow)
                                        .setVisible(true);
                            }
                        });
                }
            };
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(configLoggerKeyStroke, "SHOW_LAYOUT"); // NOI18N
        getRootPane().getActionMap().put("SHOW_LAYOUT", configAction); // NOI18N

        final KeyStroke layoutKeyStroke = KeyStroke.getKeyStroke('S', InputEvent.CTRL_MASK); // NOI18N
        final Action layoutAction = new AbstractAction() {

                @Override
                public void actionPerformed(final ActionEvent e) {
                    java.awt.EventQueue.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                doLayoutInfoNode();
                            }
                        });
                }
            };
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(layoutKeyStroke, "RESET_LAYOUT"); // NOI18N
        getRootPane().getActionMap().put("RESET_LAYOUT", layoutAction);                                    // NOI18N
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        jToolBar3 = new javax.swing.JToolBar();
        jButton16 = new javax.swing.JButton();
        panAll = new javax.swing.JPanel();
        panMain = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        panStatusbar = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        panToolbar = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();

        jButton16.setText(org.openide.util.NbBundle.getMessage(NavigatorXOrig.class, "NavigatorXOrig.jButton16.text")); // NOI18N
        jButton16.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jButton16ActionPerformed(evt);
                }
            });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(org.openide.util.NbBundle.getMessage(NavigatorXOrig.class, "NavigatorXOrig.title")); // NOI18N

        panAll.setLayout(new java.awt.BorderLayout());

        panMain.setLayout(new java.awt.BorderLayout());

        jButton1.setText(org.openide.util.NbBundle.getMessage(NavigatorXOrig.class, "NavigatorXOrig.jButton1.text")); // NOI18N
        jToolBar1.add(jButton1);

        panMain.add(jToolBar1, java.awt.BorderLayout.PAGE_START);

        panAll.add(panMain, java.awt.BorderLayout.CENTER);

        panStatusbar.setLayout(new java.awt.BorderLayout());

        jLabel1.setText(org.openide.util.NbBundle.getMessage(NavigatorXOrig.class, "NavigatorXOrig.jLabel1.text")); // NOI18N
        panStatusbar.add(jLabel1, java.awt.BorderLayout.CENTER);

        panAll.add(panStatusbar, java.awt.BorderLayout.SOUTH);

        panToolbar.setLayout(new java.awt.BorderLayout());
        panAll.add(panToolbar, java.awt.BorderLayout.NORTH);

        getContentPane().add(panAll, java.awt.BorderLayout.CENTER);

        jMenu1.setText(org.openide.util.NbBundle.getMessage(NavigatorXOrig.class, "NavigatorXOrig.jMenu1.text")); // NOI18N
        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        setSize(new java.awt.Dimension(1024, 768));
        setLocationRelativeTo(null);
    } // </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jButton16ActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jButton16ActionPerformed
// TODO add your handling code here:
    } //GEN-LAST:event_jButton16ActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  args  the command line arguments
     */
    public static void main(final String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    new NavigatorXOrig().setVisible(true);
                }
            });
    }
}
