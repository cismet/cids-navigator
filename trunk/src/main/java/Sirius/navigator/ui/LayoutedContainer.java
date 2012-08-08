/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Sirius.navigator.ui;

import Sirius.navigator.Navigator;
import Sirius.navigator.docking.CustomView;
import Sirius.navigator.plugin.interfaces.LayoutManager;
import Sirius.navigator.store.AbstractObjectStoreHandler;
import Sirius.navigator.store.ObjectStoreHandler;
import Sirius.navigator.store.ObjectStoreManager;
import Sirius.navigator.ui.widget.FloatingFrame;
import Sirius.navigator.ui.widget.FloatingFrameConfigurator;

import net.infonode.docking.DockingWindow;
import net.infonode.docking.DockingWindowAdapter;
import net.infonode.docking.DockingWindowListener;
import net.infonode.docking.RootWindow;
import net.infonode.docking.SplitWindow;
import net.infonode.docking.TabWindow;
import net.infonode.docking.View;
import net.infonode.docking.theme.DockingWindowsTheme;
import net.infonode.docking.theme.ShapedGradientDockingTheme;
import net.infonode.docking.util.DeveloperUtil;
import net.infonode.docking.util.DockingUtil;
import net.infonode.docking.util.StringViewMap;
import net.infonode.gui.componentpainter.AlphaGradientComponentPainter;
import net.infonode.util.Direction;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import org.openide.util.lookup.ServiceProvider;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.io.*;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

/**
 * DOCUMENT ME!
 *
 * @author   spuhl
 * @version  $Revision$, $Date$
 */
public class LayoutedContainer implements GUIContainer, LayoutManager {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger logger = Logger.getLogger(MutableContainer.class);
    public static final String DEFAULT_LAYOUT = Navigator.NAVIGATOR_HOME + "navigator.layout"; // NOI18N

    private static RootWindow rootWindow;

    //~ Instance fields --------------------------------------------------------

    private final HashMap components = new HashMap();
    private StringViewMap viewMap = new StringViewMap();
    private final Vector<View> p1Pane;
    private final Vector<View> p2Pane;
    private final Vector<View> p3Pane;
    private final ConstrainsChangeListener constrainsChangeListener;
    private final FloatingFrameListener floatingFrameListener;
    private final MutableToolBar toolBar;
    private final MutableMenuBar menuBar;
    private final DockingWindowListener dockingWindowListener = new DockingWindowAdapter() {

            @Override
            public void windowShown(final DockingWindow arg0) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Docking window shown");                           // NOI18N
                }
                try {
                    if (arg0 instanceof CustomView) {
                        menuBar.setMoveableMenuesEnabled(((CustomView)arg0).getId(), true);
                        toolBar.setMoveableToolBarEnabled(((CustomView)arg0).getId(), true);
                    }
                } catch (Exception ex) {
                    logger.error("Error while activating the MenuBar/Toolbar", ex); // NOI18N
                }
            }

            @Override
            public void windowHidden(final DockingWindow arg0) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Docking window hidden");                            // NOI18N
                }
                try {
                    if (arg0 instanceof CustomView) {
                        menuBar.setMoveableMenuesEnabled(((CustomView)arg0).getId(), false);
                        toolBar.setMoveableToolBarEnabled(((CustomView)arg0).getId(), false);
                    }
                } catch (Exception ex) {
                    logger.error("Error while deactivating the MenuBar/Toolbar", ex); // NOI18N
                }
            }

            @Override
            public void windowUndocked(final DockingWindow arg0) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Docking window shown");                           // NOI18N
                }
                try {
                    menuBar.setMoveableMenuesEnabled(((CustomView)arg0).getId(), true);
                    toolBar.setMoveableToolBarEnabled(((CustomView)arg0).getId(), true);
                } catch (Exception ex) {
                    logger.error("Error while activating the MenuBar/Toolbar", ex); // NOI18N
                }
            }
        };

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new LayoutedContainer object.
     *
     * @param  toolBar  DOCUMENT ME!
     * @param  menuBar  DOCUMENT ME!
     */
    public LayoutedContainer(final MutableToolBar toolBar, final MutableMenuBar menuBar) {
        this(toolBar, menuBar, false);
    }

    /**
     * Creates a new LayoutedContainer object.
     *
     * @param  toolBar         DOCUMENT ME!
     * @param  menuBar         DOCUMENT ME!
     * @param  advancedLayout  DOCUMENT ME!
     */
    public LayoutedContainer(final MutableToolBar toolBar,
            final MutableMenuBar menuBar,
            final boolean advancedLayout) {
        this(toolBar, menuBar, advancedLayout, advancedLayout, advancedLayout);
    }

    /**
     * Creates a new LayoutedContainer object.
     *
     * @param  toolBar             DOCUMENT ME!
     * @param  menuBar             DOCUMENT ME!
     * @param  continuousLayout    DOCUMENT ME!
     * @param  oneTouchExpandable  DOCUMENT ME!
     * @param  proportionalResize  DOCUMENT ME!
     */
    public LayoutedContainer(final MutableToolBar toolBar,
            final MutableMenuBar menuBar,
            final boolean continuousLayout,
            final boolean oneTouchExpandable,
            final boolean proportionalResize) {
        if (logger.isDebugEnabled()) {
            logger.debug("creating LayoutedContainer instance"); // NOI18N
        }

        this.toolBar = toolBar;
        this.menuBar = menuBar;

        p1Pane = new Vector<View>();
        p2Pane = new Vector<View>();
        p3Pane = new Vector<View>();

        constrainsChangeListener = new ConstrainsChangeListener();
        floatingFrameListener = new FloatingFrameListener();

        rootWindow = DockingUtil.createRootWindow(viewMap, true);
        doConfigKeystrokes();

        final DockingWindowsTheme theme = new ShapedGradientDockingTheme();
        rootWindow.getRootWindowProperties().addSuperObject(
            theme.getRootWindowProperties());

        rootWindow.getRootWindowProperties().getDockingWindowProperties().setUndockEnabled(true);

        final AlphaGradientComponentPainter x = new AlphaGradientComponentPainter(
                java.awt.SystemColor.inactiveCaptionText,
                java.awt.SystemColor.activeCaptionText,
                java.awt.SystemColor.activeCaptionText,
                java.awt.SystemColor.inactiveCaptionText);
        rootWindow.getRootWindowProperties().getDragRectangleShapedPanelProperties().setComponentPainter(x);

        rootWindow.getRootWindowProperties()
                .getTabWindowProperties()
                .getTabbedPanelProperties()
                .setTabAreaOrientation(Direction.UP);
        // rootWindow.getRootWindowProperties().getTabWindowProperties().getTabbedPanelProperties().getDefaultProperties().getTabAreaComponentsProperties().
        rootWindow.getRootWindowProperties()
                .getTabWindowProperties()
                .getTabbedPanelProperties()
                .setPaintTabAreaShadow(true);
        rootWindow.getRootWindowProperties().getTabWindowProperties().getTabbedPanelProperties().setShadowSize(10);
        rootWindow.getRootWindowProperties()
                .getTabWindowProperties()
                .getTabbedPanelProperties()
                .setShadowStrength(0.8f);
        rootWindow.getRootWindowProperties().getTabWindowProperties().getMinimizeButtonProperties().setVisible(false);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public synchronized void add(final MutableConstraints constraints) {
        if (logger.isInfoEnabled()) {
            logger.info("adding component '" + constraints.getName() + "' to mutable container at position '"
                        + constraints.getPosition() + "'");      // NOI18N
        }
        if (logger.isDebugEnabled()) {
            logger.debug(constraints.toString());
        }
        if (!components.containsKey(constraints.getId())) {
            components.put(constraints.getId(), constraints);
            if (!this.rootWindow.isDisplayable() || SwingUtilities.isEventDispatchThread()) {
                doAdd(constraints);
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("add(): synchronizing method"); // NOI18N
                }
                SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            doAdd(constraints);
                        }
                    });
            }

            if (constraints.isMutable()) {
                constraints.addPropertyChangeListener(constrainsChangeListener);
            }
        } else {
            logger.error("a component with the same id '" + constraints.getId() + "' is already in this container"); // NOI18N
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  constraints  DOCUMENT ME!
     */
    private void doAdd(final MutableConstraints constraints) {
        final Vector<View> tabbedPane = this.getViewsAtPosition(constraints.getPosition());
        if ((constraints.getPreferredIndex() != -1) && (tabbedPane.size() > constraints.getPreferredIndex())) {
            if (logger.isDebugEnabled()) {
                logger.debug("inserting component at index '" + constraints.getPreferredIndex() + "'"); // NOI18N
            }
            if (constraints.getContainerType().equals(MutableConstraints.FLOATINGFRAME)) {
                tabbedPane.add(constraints.getPreferredIndex(), constraints.getView());
                menuBar.addViewMenuItem(constraints.getView().getMenuItem());
                viewMap.addView(constraints.getName(), constraints.getView());
                doLayoutInfoNode();
                this.addFloatingFrame(constraints);
            } else {
                tabbedPane.add(constraints.getPreferredIndex(), constraints.getView());
                menuBar.addViewMenuItem(constraints.getView().getMenuItem());
                viewMap.addView(constraints.getName(), constraints.getView());
                doLayoutInfoNode();
            }
        } else {
            if (constraints.getContainerType().equals(MutableConstraints.FLOATINGFRAME)) {
                tabbedPane.add(constraints.getView());
                menuBar.addViewMenuItem(constraints.getView().getMenuItem());
                viewMap.addView(constraints.getName(), constraints.getView());
                doLayoutInfoNode();
                this.addFloatingFrame(constraints);
            } else {
                tabbedPane.add(constraints.getView());
                menuBar.addViewMenuItem(constraints.getView().getMenuItem());
                viewMap.addView(constraints.getName(), constraints.getView());
                doLayoutInfoNode();
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  constraints  DOCUMENT ME!
     */
    private void addFloatingFrame(final MutableConstraints constraints) {
        if (logger.isDebugEnabled()) {
            logger.debug("adding FloatingFrame");                                                // NOI18N
        }
        final FloatingFrameConfigurator configurator = constraints.getFloatingFrameConfigurator();
        ((FloatingFrame)constraints.getContainer()).setTileBarVisible(false);
        if (!configurator.getId().equals(constraints.getId())) {
            logger.warn("FloatingFrame constraints id: '" + constraints.getId()
                        + "' != FloatingFrame configurator id: '" + configurator.getId() + "'"); // NOI18N
        }

        if (configurator.isSwapMenuBar() || configurator.isSwapToolBar()) {
            if (logger.isDebugEnabled()) {
                logger.debug("enabling Floating Listener"); // NOI18N
            }
            ((FloatingFrame)constraints.getContainer()).addPropertyChangeListener(
                FloatingFrame.FLOATING,
                floatingFrameListener);
            constraints.getView().addListener(dockingWindowListener);
        }

        if (configurator.isSwapMenuBar()) {
            if (logger.isInfoEnabled()) {
                logger.info("adding FloatingFrameMenuBar '" + configurator.getId() + "' to MutableMenuBar"); // NOI18N
            }
            menuBar.addMoveableMenues(configurator.getId(), configurator.getMenues());
        }

        if (configurator.isSwapToolBar()) {
            if (logger.isInfoEnabled()) {
                logger.info("adding FloatingFrameToolBar '" + configurator.getId() + "' to MutableToolBar"); // NOI18N
            }
            toolBar.addMoveableToolBar(((FloatingFrame)constraints.getContainer()).getToolBar());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  constraints  DOCUMENT ME!
     */
    private void removeFloatingFrame(final MutableConstraints constraints) {
        if (logger.isDebugEnabled()) {
            logger.debug("removing FloatingFrame"); // NOI18N
        }
        final FloatingFrameConfigurator configurator = constraints.getFloatingFrameConfigurator();

        if (configurator.isSwapMenuBar() || configurator.isSwapToolBar()) {
            ((FloatingFrame)constraints.getContainer()).removePropertyChangeListener(floatingFrameListener);
        }

        if (configurator.isSwapMenuBar()) {
            if (logger.isInfoEnabled()) {
                logger.info("removing FloatingFrameMenuBar '" + configurator.getId() + "' from MutableMenuBar"); // NOI18N
            }
            menuBar.removeMoveableMenues(configurator.getId());
        }

        if (configurator.isSwapToolBar()) {
            if (logger.isInfoEnabled()) {
                logger.info("removing FloatingFrameToolBar '" + configurator.getId() + "' from MutableToolBar"); // NOI18N
            }
            toolBar.removeMoveableToolBar(configurator.getId());
        }
    }

    @Override
    public synchronized void remove(final String id) {
        if (logger.isInfoEnabled()) {
            logger.info("removing component '" + id + "'"); // NOI18N
        }
        if (components.containsKey(id)) {
            final MutableConstraints constraints = (MutableConstraints)components.remove(id);
            if (constraints.isMutable()) {
                constraints.removePropertyChangeListener(constrainsChangeListener);
            }

            if (SwingUtilities.isEventDispatchThread()) {
                doRemove(constraints);
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("remove(): synchronizing method"); // NOI18N
                }
                SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            doRemove(constraints);
                        }
                    });
            }
        } else {
            logger.error("component '" + id + "' not found in this container"); // NOI18N
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  constraints  DOCUMENT ME!
     */
    private void doRemove(final MutableConstraints constraints) {
        if (logger.isDebugEnabled()) {
            logger.debug("removing component '" + constraints.getName() + "' at position '" + constraints.getPosition()
                        + "'"); // NOI18N
        }
        final Vector<View> tabbedPane = this.getViewsAtPosition(constraints.getPosition());

        if (constraints.getContainerType().equals(MutableConstraints.FLOATINGFRAME)) {
            tabbedPane.remove(((FloatingFrame)constraints.getContainer()).getFloatingPanel());
            this.removeFloatingFrame(constraints);
        } else {
            tabbedPane.remove(constraints.getContainer());
        }
    }

    @Override
    public synchronized void select(final String id) {
        if (logger.isDebugEnabled()) {
            logger.debug("selecting component '" + id + "'");       // NOI18N
        }
        if (components.containsKey(id)) {
            final MutableConstraints constraints = (MutableConstraints)components.get(id);
            if (SwingUtilities.isEventDispatchThread()) {
                doSelect(constraints);
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("select(): synchronizing method"); // NOI18N
                }
                SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            doSelect(constraints);
                        }
                    });
            }
        } else {
            logger.error("component '" + id + "' not found in this container"); // NOI18N
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  constraints  DOCUMENT ME!
     */
    private void doSelect(final MutableConstraints constraints) {
        if (!constraints.getView().isClosable()) {
            constraints.getView().restore();
        }
        final Vector<View> tabbedPane = this.getViewsAtPosition(constraints.getPosition());

        int index = -1;
        if ((index = tabbedPane.indexOf(constraints.getView())) != -1) {
            tabbedPane.get(index).restoreFocus();
        }
        ;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   position  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private Vector<View> getViewsAtPosition(final String position) {
        if (position.equals(MutableConstraints.P1)) {
            return p1Pane;
        } else if (position.equals(MutableConstraints.P2)) {
            return p2Pane;
        } else if (position.equals(MutableConstraints.P3)) {
            return p3Pane;
        } else {
            logger.warn("unknown position '" + position + "', using default '" + MutableConstraints.P3 + "'"); // NOI18N
            return p3Pane;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public JComponent getContainer() {
        return rootWindow;
    }

    /**
     * DOCUMENT ME!
     */
    public synchronized void doLayoutInfoNode() {
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("remove all listener"); // NOI18N
            }
            TabWindow p1 = null;
            if (p1Pane.size() != 0) {
                p1 = new TabWindow();
                p1.addListener(dockingWindowListener);
                for (final View currentView : p1Pane) {
                    p1.addTab(currentView);
                }
            }
            TabWindow p2 = null;
            if (p2Pane.size() != 0) {
                p2 = new TabWindow();
                p2.addListener(dockingWindowListener);
                for (final View currentView : p2Pane) {
                    p2.addTab(currentView);
                }
            }
            TabWindow p3 = null;
            if (p3Pane.size() != 0) {
                p3 = new TabWindow();
                p3.addListener(dockingWindowListener);
                for (final View currentView : p3Pane) {
                    p3.addTab(currentView);
                }
            }

            if ((p1 != null) && (p2 != null) && (p3 == null)) {
                rootWindow.setWindow(
                    new SplitWindow(false, 0.6032864f,
                        p1,
                        p2));
            } else if ((p1 != null) && (p2 != null) && (p2 != null)) {
                rootWindow.setWindow(new SplitWindow(true, 0.2505929f,
                        new SplitWindow(false, 0.6032864f,
                            p1,
                            p2),
                        p3));
            }

            if ((p1 != null) && (p1.getChildWindow(0) != null)) {
                p1.getChildWindow(0).restoreFocus();
            }
            if ((p2 != null) && (p2.getChildWindow(0) != null)) {
                p2.getChildWindow(0).restoreFocus();
            }
            if ((p3 != null) && (p3.getChildWindow(0) != null)) {
                p3.getChildWindow(0).restoreFocus();
            }
        } catch (Exception ex) {
            logger.warn("Error while layouting the Navigator", ex); // NOI18N
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void doConfigKeystrokes() {
        final KeyStroke showLayoutKeyStroke = KeyStroke.getKeyStroke('D', InputEvent.CTRL_MASK);
        final Action showLayoutAction = new AbstractAction() {

                @Override
                public void actionPerformed(final ActionEvent e) {
                    java.awt.EventQueue.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                DeveloperUtil.createWindowLayoutFrame(
                                        org.openide.util.NbBundle.getMessage(
                                            LayoutedContainer.class,
                                            "LayoutedContainer.doConfigKeystrokes.rootWindow.title"), // NOI18N
                                        rootWindow)
                                        .setVisible(true);
                            }
                        });
                }
            };
        rootWindow.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(showLayoutKeyStroke, "SHOW_LAYOUT"); // NOI18N
        rootWindow.getActionMap().put("SHOW_LAYOUT", showLayoutAction); // NOI18N
    }

    @Override
    public void loadLayout(final Component parent) {
        final JFileChooser fc = new JFileChooser(Navigator.NAVIGATOR_HOME);
        fc.setFileHidingEnabled(false);
        fc.setFileFilter(new FileFilter() {

                @Override
                public boolean accept(final File f) {
                    return f.isDirectory()
                                || f.getName().toLowerCase().endsWith(".layout"); // NOI18N
                }

                @Override
                public String getDescription() {
                    return "Layout"; // NOI18N
                }
            });
        fc.setMultiSelectionEnabled(false);
        final int state = fc.showOpenDialog(parent);
        if (state == JFileChooser.APPROVE_OPTION) {
            final File file = fc.getSelectedFile();
            final String name = file.getAbsolutePath();
            if (name.endsWith(".layout")) { // NOI18N
                loadLayout(name, false, parent);
            } else {
                JOptionPane.showMessageDialog(
                    parent,
                    org.openide.util.NbBundle.getMessage(
                        LayoutedContainer.class,
                        "LayoutedContainer.loadLayout(Component).JOptionPane.message"), // NOI18N
                    org.openide.util.NbBundle.getMessage(
                        LayoutedContainer.class,
                        "LayoutedContainer.loadLayout(Component).JOptionPane.title"), // NOI18N
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  file    DOCUMENT ME!
     * @param  isInit  DOCUMENT ME!
     * @param  parent  DOCUMENT ME!
     */
    public void loadLayout(final String file, final boolean isInit, final Component parent) {
        if (logger.isDebugEnabled()) {
            logger.debug("Load Layout.. from " + file); // NOI18N
        }
        final File layoutFile = new File(file);

        if (layoutFile.exists()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Layout File exists"); // NOI18N
            }
            try {
                ObjectStoreManager.load(new FileInputStream(layoutFile), ObjectStoreHandler.Group.LAYOUT);

                rootWindow.getWindowBar(Direction.LEFT).setEnabled(true);
                rootWindow.getWindowBar(Direction.RIGHT).setEnabled(true);
                if (isInit) {
                    final int count = viewMap.getViewCount();
                    for (int i = 0; i < count; i++) {
                        final View current = viewMap.getViewAtIndex(i);
                        if (current.isUndocked()) {
                            current.dock();
                        }
                    }
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("Loading Layout successfull");                                              // NOI18N
                }
            } catch (final Exception ex) {
                logger.error("an exception was thrown while loading layout --> loading default Layout", ex); // NOI18N
                if (isInit) {
                    JOptionPane.showMessageDialog(
                        parent,
                        org.openide.util.NbBundle.getMessage(
                            LayoutedContainer.class,
                            "LayoutedContainer.loadLayout(String,boolean,Component).message.reset"),         // NOI18N
                        org.openide.util.NbBundle.getMessage(
                            LayoutedContainer.class,
                            "LayoutedContainer.loadLayout(String,boolean,Component).title"),                 // NOI18N
                        JOptionPane.INFORMATION_MESSAGE);
                    doLayoutInfoNode();
                } else {
                    JOptionPane.showMessageDialog(
                        parent,
                        org.openide.util.NbBundle.getMessage(
                            LayoutedContainer.class,
                            "LayoutedContainer.loadLayout(String,boolean,Component).message"),               // NOI18N
                        org.openide.util.NbBundle.getMessage(
                            LayoutedContainer.class,
                            "LayoutedContainer.loadLayout(String,boolean,Component).title"),                 // NOI18N
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } else {
            if (isInit) {
                logger.warn("Datei exitstiert nicht --> default layout (init)");                             // NOI18N
                SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            // UGLY WINNING --> Gefixed durch IDW Version 1.5
                            // setupDefaultLayout();
                            // DeveloperUtil.createWindowLayoutFrame("nach setup1",rootWindow).setVisible(true);
                            doLayoutInfoNode();
                            // DeveloperUtil.createWindowLayoutFrame("nach setup2",rootWindow).setVisible(true);
                        }
                    });
            } else {
                logger.warn("Datei exitstiert nicht)");                                             // NOI18N
                JOptionPane.showMessageDialog(
                    parent,
                    org.openide.util.NbBundle.getMessage(
                        LayoutedContainer.class,
                        "LayoutedContainer.loadLayout(String,boolean,Component).message.notFound"), // NOI18N
                    org.openide.util.NbBundle.getMessage(
                        LayoutedContainer.class,
                        "LayoutedContainer.loadLayout(String,boolean,Component).title"),            // NOI18N
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    @Override
    public void resetLayout() {
        doLayoutInfoNode();
    }

    @Override
    public void saveCurrentLayout(final Component parent) {
        final JFileChooser fc = new JFileChooser(Navigator.NAVIGATOR_HOME);
        fc.setFileFilter(new FileFilter() {

                @Override
                public boolean accept(final File f) {
                    return f.isDirectory()
                                || f.getName().toLowerCase().endsWith(".layout"); // NOI18N
                }

                @Override
                public String getDescription() {
                    return "Layout"; // NOI18N
                }
            });
        fc.setMultiSelectionEnabled(false);
        final int state = fc.showSaveDialog(parent);

        if (logger.isDebugEnabled()) {
            logger.debug("state:" + state);   // NOI18N
        }
        if (state == JFileChooser.APPROVE_OPTION) {
            final File file = fc.getSelectedFile();
            if (logger.isDebugEnabled()) {
                logger.debug("file:" + file); // NOI18N
            }

            String name = file.getAbsolutePath();

            if (!name.endsWith(".layout")) { // NOI18N
                name = name + ".layout";
            }

            this.saveLayout(name, parent);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  file    DOCUMENT ME!
     * @param  parent  DOCUMENT ME!
     */
    public void saveLayout(final String file, final Component parent) {
        if (logger.isDebugEnabled()) {
            logger.debug("Saving Layout.. to " + file);                 // NOI18N
        }
        final File layoutFile = new File(file);
        try {
            if (!layoutFile.exists()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Saving Layout.. File does not exit"); // NOI18N
                }
                layoutFile.createNewFile();
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("Saving Layout.. File does exit");     // NOI18N
                }
            }

            try {
                ObjectStoreManager.save(new FileOutputStream(layoutFile), ObjectStoreHandler.Group.LAYOUT);
            } catch (final Exception ex) {
                logger.error("an error occurred while saving layout", ex);
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Saving Layout.. to " + file + " successfull");                                           // NOI18N
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(
                parent,
                org.openide.util.NbBundle.getMessage(
                    LayoutedContainer.class,
                    "LayoutedContainer.saveLayout().message"),                                                         // NOI18N
                org.openide.util.NbBundle.getMessage(LayoutedContainer.class, "LayoutedContainer.saveLayout().title"), // NOI18N
                JOptionPane.INFORMATION_MESSAGE);
            logger.error("A failure occured during writing the layout file", ex);                                      // NOI18N
        }
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class FloatingFrameListener implements PropertyChangeListener {

        //~ Methods ------------------------------------------------------------

        /**
         * This method gets called when a bound property is changed.
         *
         * @param  evt  A PropertyChangeEvent object describing the event source and the property that has changed.
         */
        @Override
        public void propertyChange(final PropertyChangeEvent evt) {
            final FloatingFrame floatingFrame = (FloatingFrame)evt.getSource();

            if (floatingFrame.getConfigurator().isSwapMenuBar()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("setting floating frame meneus visible: '" + !floatingFrame.isFloating() + "'"); // NOI18N
                }
                menuBar.setMoveableMenuesVisible(floatingFrame.getConfigurator().getId(), !floatingFrame.isFloating());
            }

            if (floatingFrame.getConfigurator().isSwapToolBar()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("setting floating frame toolbar visible: '" + !floatingFrame.isFloating() + "'"); // NOI18N
                }
                toolBar.setMoveableToolBarVisible(floatingFrame.getConfigurator().getId(), !floatingFrame.isFloating());
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class ConstrainsChangeListener implements PropertyChangeListener {

        //~ Methods ------------------------------------------------------------

        @Override
        public void propertyChange(final PropertyChangeEvent e) {
            if (e.getSource() instanceof MutableConstraints) {
                final MutableConstraints constraints = (MutableConstraints)e.getSource();
                if (logger.isDebugEnabled()) {
                    logger.debug("setting new value of property '" + e.getPropertyName() + "' of component '"
                                + constraints.getId() + "'"); // TabWindow tabbedPane =
                                                              // getTabWindowAt(constraints.getPosition());   //NOI18N
                                                              // int index =
                                                              // tabbedPane.getChildWindowIndex(constraints.getView());
                }
                final View changedView = constraints.getView();
                if (e.getPropertyName().equals("name")) {     // NOI18N
                    // tabbedPane.seTitleAt(index, constraints.getName());
                    changedView.getViewProperties().setTitle(constraints.getName());
                } else if (e.getPropertyName().equals("tooltip")) {                                                  // NOI18N
                    if (logger.isDebugEnabled()) {
                        logger.debug("Tooltip konnte nicht geändert werden, da nicht implementiert");                // NOI18N
                    }
                } else if (e.getPropertyName().equals("icon")) {                                                     // NOI18N
                    changedView.getViewProperties().setIcon(constraints.getIcon());
                } else if (e.getPropertyName().equals("position") || e.getPropertyName().equals("preferredIndex")) { // NOI18N

                    // Extrawurst bei FLoatingFrame: Men\u00FCs und Toolbars entfernen
                    if (constraints.getContainerType().equals(MutableConstraints.FLOATINGFRAME)) {
                        removeFloatingFrame(constraints);
                    }

                    doAdd(constraints);
                    doSelect(constraints);
                } else {
                    logger.warn("unsupported property change of '" + e.getPropertyName() + "'"); // NOI18N
                }
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("unexpected property change event '" + e.getPropertyName() + "'"); // NOI18N
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    @ServiceProvider(service = ObjectStoreHandler.class)
    public static final class LayoutStore extends AbstractObjectStoreHandler {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new LayoutStore object.
         */
        public LayoutStore() {
            super(Group.LAYOUT, LayoutStore.class.getName().hashCode());
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public Serializable getObjectToBeSaved() {
            final ByteArrayOutputStream bout = new ByteArrayOutputStream(2048);
            ObjectOutputStream oout = null;

            try {
                if (rootWindow == null) {
                    logger.warn("rootWindow is null -> layout not saved");
                } else {
                    if (logger.isDebugEnabled()) {
                        logger.debug("saving layout...");
                    }
                    oout = new ObjectOutputStream(bout);
                    LayoutedContainer.rootWindow.write(oout);
                    if (logger.isDebugEnabled()) {
                        logger.debug("layout has been saved successfully");
                    }
                }
            } catch (final Exception e) {
                logger.error("an error occurred while preparing object to be saved", e);

                SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            JOptionPane.showMessageDialog(
                                LayoutedContainer.rootWindow,
                                org.openide.util.NbBundle.getMessage(
                                    LayoutedContainer.class,
                                    "LayoutedContainer.saveLayout().message"), // NOI18N
                                org.openide.util.NbBundle.getMessage(
                                    LayoutedContainer.class,
                                    "LayoutedContainer.saveLayout().title"), // NOI18N
                                JOptionPane.INFORMATION_MESSAGE);
                        }
                    });
            } finally {
                IOUtils.closeQuietly(oout);
            }

            return bout.toByteArray();
        }

        @Override
        public void notifyAboutLoadedObject(final Serializable in) {
            if (in instanceof byte[]) {
                final ByteArrayInputStream bin = new ByteArrayInputStream((byte[])in);
                ObjectInputStream oin = null;

                try {
                    if (rootWindow == null) {
                        logger.warn("rootWindow is null -> layout not loaded");
                    } else {
                        if (logger.isDebugEnabled()) {
                            logger.debug("loading layout...");
                        }
                        oin = new ObjectInputStream(bin);
                        LayoutedContainer.rootWindow.read(oin);
                        if (logger.isDebugEnabled()) {
                            logger.debug("layout has been loaded successfully");
                        }
                    }
                } catch (final Exception e) {
                    logger.error("an error occurred while initializing rootWindow", e);
                } finally {
                    IOUtils.closeQuietly(oin);
                }
            } else {
                logger.error("wrong type of loaded object. Expected " + byte[].class.getName()
                            + " got: " + in.getClass().getName());
            }
        }
    }
}
