/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Sirius.navigator.ui;

import Sirius.navigator.Navigator;
import Sirius.navigator.docking.CustomView;
import Sirius.navigator.plugin.interfaces.LayoutManager;
import Sirius.navigator.ui.widget.FloatingFrame;
import Sirius.navigator.ui.widget.FloatingFrameConfigurator;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
import net.infonode.docking.DockingWindow;
import net.infonode.docking.DockingWindowListener;
import net.infonode.docking.OperationAbortedException;
import net.infonode.docking.RootWindow;
import net.infonode.docking.SplitWindow;
import net.infonode.docking.TabWindow;
import net.infonode.docking.View;
import net.infonode.docking.properties.RootWindowProperties;
import net.infonode.docking.theme.DockingWindowsTheme;
import net.infonode.docking.theme.ShapedGradientDockingTheme;
import net.infonode.docking.util.DeveloperUtil;
import net.infonode.docking.util.DockingUtil;
import net.infonode.docking.util.PropertiesUtil;
import net.infonode.docking.util.StringViewMap;
import net.infonode.gui.componentpainter.AlphaGradientComponentPainter;
import net.infonode.util.Direction;
import org.apache.log4j.Logger;

/**
 *
 * @author spuhl
 */
public class LayoutedContainer implements GUIContainer, LayoutManager {

    private final static Logger logger = Logger.getLogger(MutableContainer.class);
    private final Hashtable components = new Hashtable();    //private final JSplitPane rootSplitPane;
    //private final JSplitPane leftSplitPane;

    private final RootWindow rootWindow;
    private StringViewMap viewMap = new StringViewMap();
    private final Vector<View> p1Pane;
    private final Vector<View> p2Pane;
    private final Vector<View> p3Pane;
    private final ConstrainsChangeListener constrainsChangeListener;
    private final FloatingFrameListener floatingFrameListener;
    private boolean continuousLayout = false;
    private boolean oneTouchExpandable = false;
    private boolean proportionalResize = false;
    private boolean obeyMinimumSize = false;
    private final MutableToolBar toolBar;
    private final MutableMenuBar menuBar;
    public final static String DEFAULT_LAYOUT = Navigator.NAVIGATOR_HOME + "navigator.layout";

    public LayoutedContainer(MutableToolBar toolBar, MutableMenuBar menuBar) {
        this(toolBar, menuBar, false);
    }

    public LayoutedContainer(MutableToolBar toolBar, MutableMenuBar menuBar, boolean advancedLayout) {
        this(toolBar, menuBar, advancedLayout, advancedLayout, advancedLayout);
    }

    public LayoutedContainer(MutableToolBar toolBar, MutableMenuBar menuBar, boolean continuousLayout, boolean oneTouchExpandable, boolean proportionalResize) {
        logger.debug("creating LayoutedContainer instance");

        this.toolBar = toolBar;
        this.menuBar = menuBar;
        this.continuousLayout = continuousLayout;
        this.oneTouchExpandable = oneTouchExpandable;
        this.proportionalResize = proportionalResize;

//        p1Pane = new JTabbedPane (JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
//        p2Pane = new JTabbedPane (JTabbedPane.BOTTOM, JTabbedPane.SCROLL_TAB_LAYOUT);
//        p3Pane = new JTabbedPane (JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);

//        p1Pane = new TabWindow();
//        p2Pane = new TabWindow();
//        p3Pane = new TabWindow();

        p1Pane = new Vector<View>();
        p2Pane = new Vector<View>();
        p3Pane = new Vector<View>();

        //this.leftSplitPane = new JSplitPane (JSplitPane.VERTICAL_SPLIT, this.continuousLayout, p1Pane, p2Pane);
        //this.rootSplitPane = new JSplitPane (JSplitPane.HORIZONTAL_SPLIT, this.continuousLayout, this.leftSplitPane, this.p3Pane);

        //this.leftSplitPane.setOneTouchExpandable(oneTouchExpandable);
        //this.rootSplitPane.setOneTouchExpandable(oneTouchExpandable);

//        if(this.proportionalResize)
//        {
//            //this.leftSplitPane.setResizeWeight(0.75);
//            //this.rootSplitPane.setResizeWeight(0.25);
//            
//            this.leftSplitPane.setResizeWeight(0.60);
//            this.rootSplitPane.setResizeWeight(0.30);
//        }

//        if(this.obeyMinimumSize)
//        {
//            p1Pane.setMinimumSize(new Dimension(240,200));
//        }

        if (Sirius.navigator.resource.PropertyManager.getManager().isAdvancedLayout()) {//            p1Pane.setBorder(new javax.swing.border.EmptyBorder(1,1,1,1));
//            p2Pane.setBorder(new javax.swing.border.EmptyBorder(1,1,1,1));
//            p3Pane.setBorder(new javax.swing.border.EmptyBorder(1,1,1,1));
//            this.leftSplitPane.setBorder(new javax.swing.border.EmptyBorder(1,1,1,1));
//            this.rootSplitPane.setBorder(new javax.swing.border.EmptyBorder(1,1,1,1));   
        }

        //this.leftSplitPane.setDividerLocation(600);
        //this.rootSplitPane.setDividerLocation(250);

        constrainsChangeListener = new ConstrainsChangeListener();
        floatingFrameListener = new FloatingFrameListener();

        rootWindow = DockingUtil.createRootWindow(viewMap, true);
        doConfigKeystrokes();

        //Cismap
//        rootWindow.addTabMouseButtonListener(DockingWindowActionMouseButtonListener.MIDDLE_BUTTON_CLOSE_LISTENER);
//        
//        DockingWindowsTheme theme = new ShapedGradientDockingTheme();
//        rootWindow.getRootWindowProperties().addSuperObject(
//                theme.getRootWindowProperties());
//
//        RootWindowProperties titleBarStyleProperties =
//                PropertiesUtil.createTitleBarStyleRootWindowProperties();
//
//        rootWindow.getRootWindowProperties().addSuperObject(
//                titleBarStyleProperties);
//
//        rootWindow.getRootWindowProperties().getDockingWindowProperties().setUndockEnabled(true);
//
//        AlphaGradientComponentPainter x = new AlphaGradientComponentPainter(java.awt.SystemColor.inactiveCaptionText, java.awt.SystemColor.activeCaptionText, java.awt.SystemColor.activeCaptionText, java.awt.SystemColor.inactiveCaptionText);
//        rootWindow.getRootWindowProperties().getDragRectangleShapedPanelProperties().setComponentPainter(x);

        DockingWindowsTheme theme = new ShapedGradientDockingTheme();
        rootWindow.getRootWindowProperties().addSuperObject(
                theme.getRootWindowProperties());

//        RootWindowProperties titleBarStyleProperties =
//                PropertiesUtil.createTitleBarStyleRootWindowProperties();

//        rootWindow.getRootWindowProperties().addSuperObject(
//                titleBarStyleProperties);

        rootWindow.getRootWindowProperties().getDockingWindowProperties().setUndockEnabled(true);
        //AlphaGradientComponentPainter x = new AlphaGradientComponentPainter(java.awt.SystemColor.inactiveCaptionText, java.awt.SystemColor.activeCaptionText, java.awt.SystemColor.activeCaptionText, java.awt.SystemColor.inactiveCaptionText);
        //AlphaGradientComponentPainter x = new AlphaGradientComponentPainter(java.awt.SystemColor.inactiveCaptionText, Color.blue, java.awt.SystemColor.activeCaptionText, Color.black);
        AlphaGradientComponentPainter x = new AlphaGradientComponentPainter(java.awt.SystemColor.inactiveCaptionText, java.awt.SystemColor.activeCaptionText, java.awt.SystemColor.activeCaptionText, java.awt.SystemColor.inactiveCaptionText);
        rootWindow.getRootWindowProperties().getDragRectangleShapedPanelProperties().setComponentPainter(x);

        //rootWindow.getRootWindowProperties().getViewProperties().getViewTitleBarProperties().getNormalProperties().getShapedPanelProperties().setComponentPainter(new GradientComponentPainter(new Color(124,160,221),new Color(236,233,216),new Color(124,160,221),new Color(236,233,216)));
        //LagisBroker.getInstance().setTitleBarComponentpainter(LagisBroker.DEFAULT_MODE_COLOR);
        //rootWindow.getRootWindowProperties().getViewProperties().getViewTitleBarProperties().setOrientation(Direction.UP);
        //rootWindow.getRootWindowProperties().getViewProperties().getViewTitleBarProperties().getNormalProperties().setIconVisible(false);
        //rootWindow.getRootWindowProperties().getViewProperties().getViewTitleBarProperties().getNormalProperties().setTitleVisible(false);
        //rootWindow.getRootWindowProperties().getViewProperties().getViewTitleBarProperties().setVisible(false);
        //rootWindow.getRootWindowProperties().getTabWindowProperties().getTabbedPanelProperties().getTabAreaProperties().getShapedPanelProperties().setDirection(Direction.DOWN);
        //rootWindow.getRootWindowProperties().getTabWindowProperties().getTabbedPanelProperties().setTabAreaOrientation(Direction.UP);
        rootWindow.getRootWindowProperties().getTabWindowProperties().getTabbedPanelProperties().setTabAreaOrientation(Direction.UP);
        //rootWindow.getRootWindowProperties().getTabWindowProperties().getTabbedPanelProperties().getDefaultProperties().getTabAreaComponentsProperties().
        rootWindow.getRootWindowProperties().getTabWindowProperties().getTabbedPanelProperties().setPaintTabAreaShadow(true);
        rootWindow.getRootWindowProperties().getTabWindowProperties().getTabbedPanelProperties().setShadowSize(10);
        rootWindow.getRootWindowProperties().getTabWindowProperties().getTabbedPanelProperties().setShadowStrength(0.8f);
        rootWindow.getRootWindowProperties().getTabWindowProperties().getMinimizeButtonProperties().setVisible(false);
    //rootWindow.getRootWindowProperties().getTabWindowProperties()
    //rootWindow.getRootWindowProperties().getTabWindowProperties().getTabbedPanelProperties().getContentPanelProperties().getComponentProperties().setBorder(new DropShadowBorder(Color.BLACK,5,5,0.5f,12,true,true,false,true));
    //rootWindow.getRootWindowProperties().getTabWindowProperties().getTabbedPanelProperties().getContentPanelProperties().getComponentProperties().setBorder();
    }

    public void setDividerLocations(double rootSplitPaneDividerLocation, double leftSplitPaneDividerLocation) {
//        this.rootSplitPane.setDividerLocation(rootSplitPaneDividerLocation);
//        this.leftSplitPane.setDividerLocation(leftSplitPaneDividerLocation);
    }

    public synchronized void add(final MutableConstraints constraints) {
        logger.info("adding component '" + constraints.getName() + "' to mutable container at position '" + constraints.getPosition() + "'");
        if (logger.isDebugEnabled()) {
            logger.debug(constraints.toString());
        }
        if (!components.containsKey(constraints.getId())) {
            components.put(constraints.getId(), constraints);
            if (!this.rootWindow.isDisplayable() || SwingUtilities.isEventDispatchThread()) {
                doAdd(constraints);
            } else {
                logger.debug("add(): synchronizing method");
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        doAdd(constraints);
                    }
                });
            }

            if (constraints.isMutable()) {
                constraints.addPropertyChangeListener(constrainsChangeListener);
            }
        } else {
            logger.error("a component with the same id '" + constraints.getId() + "' is already in this container");
        }
    }

    private void doAdd(MutableConstraints constraints) {
        
        Vector<View> tabbedPane = this.getViewsAtPosition(constraints.getPosition());
        if (constraints.getPreferredIndex() != -1 && tabbedPane.size() > constraints.getPreferredIndex()) {
            if (logger.isDebugEnabled()) {
                logger.debug("inserting component at index '" + constraints.getPreferredIndex() + "'");
            }
            if (constraints.getContainerType().equals(MutableConstraints.FLOATINGFRAME)) {
                tabbedPane.add(constraints.getPreferredIndex(), constraints.getView());
                menuBar.addViewMenuItem(constraints.getView().getMenuItem());
                viewMap.addView(constraints.getName(), constraints.getView());
                doLayoutInfoNode();
                //TODO
                //tabbedPane.setSelectedIndex(constraints.getPreferredIndex());
                //((FloatingFrame) constraints.getContainer()).getCon;
                this.addFloatingFrame(constraints);
            } else {
                tabbedPane.add(constraints.getPreferredIndex(), constraints.getView());
                menuBar.addViewMenuItem(constraints.getView().getMenuItem());
                viewMap.addView(constraints.getName(), constraints.getView());
                doLayoutInfoNode();
            // TODO
            //tabbedPane.insertTab(constraints.getName(), constraints.getIcon(), constraints.getContainer(), constraints.getToolTip(), constraints.getPreferredIndex());
            //tabbedPane.setSelectedIndex(constraints.getPreferredIndex());
            }
        } else {
            if (constraints.getContainerType().equals(MutableConstraints.FLOATINGFRAME)) {
                tabbedPane.add(constraints.getView());
                menuBar.addViewMenuItem(constraints.getView().getMenuItem());
                viewMap.addView(constraints.getName(), constraints.getView());
                doLayoutInfoNode();
                //tabbedPane.addTab(constraints.getName(), constraints.getIcon(), ((FloatingFrame)constraints.getContainer()).getFloatingPanel(), constraints.getToolTip());
                this.addFloatingFrame(constraints);
            } else {
                tabbedPane.add(constraints.getView());
                menuBar.addViewMenuItem(constraints.getView().getMenuItem());
                viewMap.addView(constraints.getName(), constraints.getView());
                doLayoutInfoNode();
            //tabbedPane.addTab(constraints.getName(), constraints.getIcon(), constraints.getContainer(), constraints.getToolTip());
            }
        }
    }

    private void addFloatingFrame(MutableConstraints constraints) {
        logger.debug("adding FloatingFrame");
        FloatingFrameConfigurator configurator = constraints.getFloatingFrameConfigurator();
        ((FloatingFrame) constraints.getContainer()).setTileBarVisible(false);
        //configurator.setIcon(null);
        //configurator.setButtons(null);

        //logger.info("FloatingFrame constraints '" + constraints + "'");
        //logger.info("FloatingFrame constraints id: '" + constraints.getId() + "'");
        //logger.info("FloatingFrame configurator '" + configurator + "'");
        //logger.info("FloatingFrame configurator id: '" + configurator.getId() + "'");
        if (!configurator.getId().equals(constraints.getId())) {
            logger.warn("FloatingFrame constraints id: '" + constraints.getId() + "' != FloatingFrame configurator id: '" + configurator.getId() + "'");
        }

        if (configurator.isSwapMenuBar() || configurator.isSwapToolBar()) {
            if (logger.isDebugEnabled()) {
                logger.debug("enabling Floating Listener");
            }
            ((FloatingFrame) constraints.getContainer()).addPropertyChangeListener(FloatingFrame.FLOATING, floatingFrameListener);
            constraints.getView().addListener(dockingWindowListener);
        }

        if (configurator.isSwapMenuBar()) {
            logger.info("adding FloatingFrameMenuBar '" + configurator.getId() + "' to MutableMenuBar");
            menuBar.addMoveableMenues(configurator.getId(), configurator.getMenues());
        }

        if (configurator.isSwapToolBar()) {
            logger.info("adding FloatingFrameToolBar '" + configurator.getId() + "' to MutableToolBar");
            toolBar.addMoveableToolBar(((FloatingFrame) constraints.getContainer()).getToolBar());
        }
    }

    private void removeFloatingFrame(MutableConstraints constraints) {
        logger.debug("removing FloatingFrame");
        FloatingFrameConfigurator configurator = constraints.getFloatingFrameConfigurator();

        if (configurator.isSwapMenuBar() || configurator.isSwapToolBar()) {
            ((FloatingFrame) constraints.getContainer()).removePropertyChangeListener(floatingFrameListener);
        }

        if (configurator.isSwapMenuBar()) {
            logger.info("removing FloatingFrameMenuBar '" + configurator.getId() + "' from MutableMenuBar");
            menuBar.removeMoveableMenues(configurator.getId());
        }

        if (configurator.isSwapToolBar()) {
            logger.info("removing FloatingFrameToolBar '" + configurator.getId() + "' from MutableToolBar");
            toolBar.removeMoveableToolBar(configurator.getId());
        }
    }

    public synchronized void remove(String id) {
        logger.info("removing component '" + id + "'");
        if (components.containsKey(id)) {
            final MutableConstraints constraints = (MutableConstraints) components.remove(id);
            if (constraints.isMutable()) {
                constraints.removePropertyChangeListener(constrainsChangeListener);
            }

            if (SwingUtilities.isEventDispatchThread()) {
                doRemove(constraints);
            } else {
                logger.debug("remove(): synchronizing method");
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        doRemove(constraints);
                    }
                });
            }
        } else {
            logger.error("component '" + id + "' not found in this container");
        }
    }

    private void doRemove(MutableConstraints constraints) {
        if (logger.isDebugEnabled()) {
            logger.debug("removing component '" + constraints.getName() + "' at position '" + constraints.getPosition() + "'");
        }
        Vector<View> tabbedPane = this.getViewsAtPosition(constraints.getPosition());

        if (constraints.getContainerType().equals(MutableConstraints.FLOATINGFRAME)) {
            tabbedPane.remove(((FloatingFrame) constraints.getContainer()).getFloatingPanel());
            this.removeFloatingFrame(constraints);
        } else {
            tabbedPane.remove(constraints.getContainer());
        }
    }

    public synchronized void select(String id) {
        if (logger.isDebugEnabled()) {
            logger.debug("selecting component '" + id + "'");
        }
        if (components.containsKey(id)) {
            final MutableConstraints constraints = (MutableConstraints) components.get(id);
            if (SwingUtilities.isEventDispatchThread()) {
                doSelect(constraints);
            } else {
                logger.debug("select(): synchronizing method");
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        doSelect(constraints);
                    }
                });
            }
        } else {
            logger.error("component '" + id + "' not found in this container");
        }
    }

    private void doSelect(MutableConstraints constraints) {
        Vector<View> tabbedPane = this.getViewsAtPosition(constraints.getPosition());

//        if (constraints.getContainerType().equals(MutableConstraints.FLOATINGFRAME)) {
//            //tabbedPane.setSelectedTab(tabbedPane.getChildWindowIndex(constraints.getView()));
//            int index = -1;
//            if((index = tabbedPane.indexOf(constraints.getView())) != -1){
//                tabbedPane.get(index).restoreFocus();
//            };
//        //tabbedPane.setSelectedComponent(((FloatingFrame)constraints.getContainer()).getFloatingPanel());
//        } else {
//            tabbedPane.setSelectedTab(tabbedPane.getChildWindowIndex(constraints.getView()));
//        //tabbedPane.setSelectedComponent(constraints.getContainer());
//        }

        int index = -1;
        if ((index = tabbedPane.indexOf(constraints.getView())) != -1) {
            tabbedPane.get(index).restoreFocus();
        }
        ;
    }

    private Vector<View> getViewsAtPosition(String position) {
        if (position.equals(MutableConstraints.P1)) {
            return p1Pane;
        } else if (position.equals(MutableConstraints.P2)) {
            return p2Pane;
        } else if (position.equals(MutableConstraints.P3)) {
            return p3Pane;
        } else {
            logger.warn("unknown position '" + position + "', using default '" + MutableConstraints.P3 + "'");
            return p3Pane;
        }
    }

    public JComponent getContainer() {
        return rootWindow;
    }

    private class FloatingFrameListener implements PropertyChangeListener {

        /** This method gets called when a bound property is changed.
         * @param evt A PropertyChangeEvent object describing the event source
         *   	and the property that has changed.
         *
         */
        public void propertyChange(PropertyChangeEvent evt) {
            FloatingFrame floatingFrame = (FloatingFrame) evt.getSource();

            if (floatingFrame.getConfigurator().isSwapMenuBar()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("setting floating frame meneus visible: '" + !floatingFrame.isFloating() + "'");
                }
                menuBar.setMoveableMenuesVisible(floatingFrame.getConfigurator().getId(), !floatingFrame.isFloating());
            }

            if (floatingFrame.getConfigurator().isSwapToolBar()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("setting floating frame toolbar visible: '" + !floatingFrame.isFloating() + "'");
                }
                toolBar.setMoveableToolBarVisible(floatingFrame.getConfigurator().getId(), !floatingFrame.isFloating());
            }
        }
    }

    private class ConstrainsChangeListener implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent e) {
            if (e.getSource() instanceof MutableConstraints) {
                final MutableConstraints constraints = (MutableConstraints) e.getSource();
                if (logger.isDebugEnabled()) {
                    logger.debug("setting new value of property '" + e.getPropertyName() + "' of component '" + constraints.getId() + "'");//                TabWindow tabbedPane = getTabWindowAt(constraints.getPosition());                                
//                int index = tabbedPane.getChildWindowIndex(constraints.getView());                

                }
                View changedView = constraints.getView();
                if (e.getPropertyName().equals("name")) {
                    //tabbedPane.seTitleAt(index, constraints.getName());
                    changedView.getViewProperties().setTitle(constraints.getName());
                } else if (e.getPropertyName().equals("tooltip")) {
//                    tabbedPane.setToolTipTextAt(index, constraints.getToolTip());
//                    changedView.get
                    //TODO
                    logger.debug("Tooltip konnte nicht geändert werden, da nicht implementiert");
                } else if (e.getPropertyName().equals("icon")) {
                    changedView.getViewProperties().setIcon(constraints.getIcon());
                } else if (e.getPropertyName().equals("position") || e.getPropertyName().equals("preferredIndex")) {
                    // add() f\u00FChrt automatisch zu einem remove()
                    // doRemove(constraints);

                    // Extrawurst bei FLoatingFrame: Men\u00FCs und Toolbars entfernen
                    if (constraints.getContainerType().equals(MutableConstraints.FLOATINGFRAME)) {
                        removeFloatingFrame(constraints);
                    }

                    doAdd(constraints);
                    doSelect(constraints);
                } else {
                    logger.warn("unsupported property change of '" + e.getPropertyName() + "'");
                }
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("unexpected property change event '" + e.getPropertyName() + "'");
                }
            }
        }
    }

    public synchronized void doLayoutInfoNode() {
// default
//        rootWindow.setWindow(new SplitWindow(false, 0.38277513f,
//                p1Pane,
//                new SplitWindow(false, 0.4300518f,
//                p2Pane,
//                p3Pane)));

        try {
            //logger.fatal("p1Pane: "+p1Pane);
            //logger.fatal("p1 Childcount"+getTabWindowAt().getChildWindowCount());                
            logger.debug("entferne alle Listener");
            TabWindow p1 = null;
            if (p1Pane.size() != 0) {
                p1 = new TabWindow();
                p1.addListener(dockingWindowListener);
                for (View currentView : p1Pane) {
                    p1.addTab(currentView);
                }
            }
            TabWindow p2 = null;
            if (p2Pane.size() != 0) {
                p2 = new TabWindow();
                p2.addListener(dockingWindowListener);
                for (View currentView : p2Pane) {
                    p2.addTab(currentView);
                }
            }
            TabWindow p3 = null;
            if (p3Pane.size() != 0) {
                p3 = new TabWindow();
                p3.addListener(dockingWindowListener);
                //DockingWindow[] p3Array = p3Pane.toArray(new DockingWindow[1]);
                for (View currentView : p3Pane) {
                    p3.addTab(currentView);
                }
            }

            if (p1 != null && p2 != null && p3 == null) {
                rootWindow.setWindow(
                        new SplitWindow(false, 0.6032864f,
                        p1,
                        p2));
            } else if (p1 != null && p2 != null && p2 != null) {
                rootWindow.setWindow(new SplitWindow(true, 0.2505929f,
                        new SplitWindow(false, 0.6032864f,
                        p1,
                        p2),
                        p3));
            }

            if (p1 != null && p1.getChildWindow(0) != null) {
                p1.getChildWindow(0).restoreFocus();
            }
            if (p2 != null && p2.getChildWindow(0) != null) {
                p2.getChildWindow(0).restoreFocus();
            }
            if (p3 != null && p3.getChildWindow(0) != null) {
                p3.getChildWindow(0).restoreFocus();

            }

        } catch (Exception ex) {
            logger.warn("Fehler beim Layouten des Navigators", ex);
        }

    }

    public void doConfigKeystrokes() {
        KeyStroke showLayoutKeyStroke = KeyStroke.getKeyStroke('D', InputEvent.CTRL_MASK);
        Action showLayoutAction = new AbstractAction() {

            public void actionPerformed(
                    ActionEvent e) {
                java.awt.EventQueue.invokeLater(new Runnable() {

                    public void run() {
                        DeveloperUtil.createWindowLayoutFrame("Momentanes Layout", rootWindow).setVisible(true);
                    }
                });
            }
        };
        rootWindow.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(showLayoutKeyStroke, "SHOW_LAYOUT");
        rootWindow.getActionMap().put("SHOW_LAYOUT", showLayoutAction);
    //rootWindow.registerKeyboardAction(showLayoutAction,showLayoutKeyStroke,JComponent.WHEN_FOCUSED);
    }

    public void loadLayout(Component parent) {
        JFileChooser fc = new JFileChooser(Navigator.NAVIGATOR_HOME);
        fc.setFileHidingEnabled(false);
        fc.setFileFilter(new FileFilter() {

            public boolean accept(File f) {
                return f.getName().toLowerCase().endsWith(".layout");
            }

            public String getDescription() {
                return "Layout";
            }
        });
        fc.setMultiSelectionEnabled(false);
        int state = fc.showOpenDialog(parent);
        if (state == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            String name = file.getAbsolutePath();
            name = name.toLowerCase();
            if (name.endsWith(".layout")) {
                loadLayout(name, false, parent);
            } else {
                JOptionPane.showMessageDialog(parent, java.util.ResourceBundle.getBundle("de/cismet/cismap/navigatorplugin/Bundle").getString("CismapPlugin.InfoNode.format_failure_message"), java.util.ResourceBundle.getBundle("de/cismet/cismap/navigatorplugin/Bundle").getString("CismapPlugin.InfoNode.message_title"), JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    public void loadLayout(String file, boolean isInit, Component parent) {
        logger.debug("Load Layout.. from " + file);
        File layoutFile = new File(file);

        if (layoutFile.exists()) {
            logger.debug("Layout File exists");
            try {
                FileInputStream layoutInput = new FileInputStream(layoutFile);
                ObjectInputStream in = new ObjectInputStream(layoutInput);
                rootWindow.read(in);
                in.close();
                rootWindow.getWindowBar(Direction.LEFT).setEnabled(true);
                rootWindow.getWindowBar(Direction.RIGHT).setEnabled(true);
                if (isInit) {
                    int count = viewMap.getViewCount();
                    for (int i = 0; i < count; i++) {
                        View current = viewMap.getViewAtIndex(i);
                        if (current.isUndocked()) {
                            current.dock();
                        }
                    }
                }
                logger.debug("Loading Layout successfull");
            } catch (IOException ex) {
                logger.error("Layout File IO Exception --> loading default Layout", ex);
                if (isInit) {
                    JOptionPane.showMessageDialog(parent, java.util.ResourceBundle.getBundle("de/cismet/cismap/navigatorplugin/Bundle").getString("CismapPlugin.InfoNode.loading_layout_failure_message_init"), java.util.ResourceBundle.getBundle("de/cismet/cismap/navigatorplugin/Bundle").getString("CismapPlugin.InfoNode.message_title"), JOptionPane.INFORMATION_MESSAGE);
                    doLayoutInfoNode();
                } else {
                    JOptionPane.showMessageDialog(parent, java.util.ResourceBundle.getBundle("de/cismet/cismap/navigatorplugin/Bundle").getString("CismapPlugin.InfoNode.loading_layout_failure_message"), java.util.ResourceBundle.getBundle("de/cismet/cismap/navigatorplugin/Bundle").getString("CismapPlugin.InfoNode.message_title"), JOptionPane.INFORMATION_MESSAGE);
                }

            }
        } else {
            if (isInit) {
                logger.warn("Datei exitstiert nicht --> default layout (init)");
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        //UGLY WINNING --> Gefixed durch IDW Version 1.5
                        //setupDefaultLayout();
                        //DeveloperUtil.createWindowLayoutFrame("nach setup1",rootWindow).setVisible(true);
                        doLayoutInfoNode();
                    //DeveloperUtil.createWindowLayoutFrame("nach setup2",rootWindow).setVisible(true);
                    }
                });
            } else {
                logger.warn("Datei exitstiert nicht)");
                JOptionPane.showMessageDialog(parent, java.util.ResourceBundle.getBundle("de/cismet/cismap/navigatorplugin/Bundle").getString("CismapPlugin.InfoNode.layout_does_not_exist"), java.util.ResourceBundle.getBundle("de/cismet/cismap/navigatorplugin/Bundle").getString("CismapPlugin.InfoNode.message_title"), JOptionPane.INFORMATION_MESSAGE);
            }

        }
    }

    public void resetLayout() {
        doLayoutInfoNode();
    }

    public void saveCurrentLayout(Component parent) {
        JFileChooser fc = new JFileChooser(Navigator.NAVIGATOR_HOME);
        fc.setFileFilter(new FileFilter() {

            public boolean accept(File f) {
                return f.getName().toLowerCase().endsWith(".layout");
            }

            public String getDescription() {
                return "Layout";
            }
        });
        fc.setMultiSelectionEnabled(false);
        int state = fc.showSaveDialog(parent);
        logger.debug("state:" + state);
        if (state == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            logger.debug("file:" + file);
            String name = file.getAbsolutePath();
            name = name.toLowerCase();
            if (name.endsWith(".layout")) {
                saveLayout(name, parent);
            } else {
                saveLayout(name + ".layout", parent);
            }
        }
    }

    public void saveLayout(String file, Component parent) {
        logger.debug("Saving Layout.. to " + file);
        File layoutFile = new File(file);
        try {
            if (!layoutFile.exists()) {
                logger.debug("Saving Layout.. File does not exit");
                layoutFile.createNewFile();
            } else {
                logger.debug("Saving Layout.. File does exit");
            }
            FileOutputStream layoutOutput = new FileOutputStream(layoutFile);
            ObjectOutputStream out = new ObjectOutputStream(layoutOutput);
            rootWindow.write(out);
            out.flush();
            out.close();
            logger.debug("Saving Layout.. to " + file + " successfull");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(parent, java.util.ResourceBundle.getBundle("de/cismet/cismap/navigatorplugin/Bundle").getString("CismapPlugin.InfoNode.saving_layout_failure"), java.util.ResourceBundle.getBundle("de/cismet/cismap/navigatorplugin/Bundle").getString("CismapPlugin.InfoNode.message_title"), JOptionPane.INFORMATION_MESSAGE);
            logger.error("A failure occured during writing the layout file", ex);
        }
    }
    private final DockingWindowListener dockingWindowListener = new DockingWindowListener() {

        public void windowAdded(DockingWindow arg0, DockingWindow arg1) {
//            logger.fatal("windowadded" + arg0);
//            logger.fatal("addedwindow" + arg1);
//
//            if (arg0 instanceof TabWindow && arg1 instanceof View && ((TabWindow) arg0).getChildWindowCount() > 1) {
//                int count = ((TabWindow) arg0).getChildWindowCount();
//                for (int i = 0; i < count; i++) {
//                    DockingWindow child = ((TabWindow) arg0).getChildWindow(i);
//                    if (child instanceof CustomView) {
//                        ((View) child).getViewProperties().getViewTitleBarProperties().getNormalProperties().setTitle("");
//                        ((View) child).getViewProperties().getViewTitleBarProperties().getNormalProperties().setIcon(null);
//                    }
//                }

//                DockingWindow first = ((TabWindow) arg0).getChildWindow(0);
//                if (first instanceof CustomView) {
//                    logger.fatal("Title/Icon disabled");
//                    //((CustomView)first).getViewProperties().getViewTitleBarProperties().setVisible(false);
//                    ((View) first).getViewProperties().getViewTitleBarProperties().getNormalProperties().setTitle("");
//                    ((View) first).getViewProperties().getViewTitleBarProperties().getNormalProperties().setIcon(null);
//                }

            //}
        }

        public void windowRemoved(DockingWindow arg0, DockingWindow arg1) {
//            if (arg1 instanceof CustomView) {
//                ((View) arg1).getViewProperties().getViewTitleBarProperties().getNormalProperties().setTitle(((CustomView) arg1).getViewName());
//                ((View) arg1).getViewProperties().getViewTitleBarProperties().getNormalProperties().setIcon(((CustomView) arg1).getViewIcon());
//                if (arg0 instanceof TabWindow && ((TabWindow) arg0).getChildWindowCount() == 1) {
//                    DockingWindow first = ((TabWindow) arg0).getChildWindow(0);
//                    if (first instanceof CustomView) {
//                        logger.fatal("Title/Icon enabled");
//                        //((CustomView)first).getViewProperties().getViewTitleBarProperties().setVisible(false);
//                        ((View) first).getViewProperties().getViewTitleBarProperties().getNormalProperties().setTitle(((CustomView) first).getViewName());
//                        ((View) first).getViewProperties().getViewTitleBarProperties().getNormalProperties().setIcon(((CustomView) first).getViewIcon());
//                    }
//                }
//
//            }
            //throw new UnsupportedOperationException("Not supported yet.");
        }

        public void windowShown(DockingWindow arg0) {
            logger.debug("Docking window shown");
            try {
                if (arg0 instanceof CustomView) {
                    menuBar.setMoveableMenuesEnabled(((CustomView) arg0).getId(), true);
                    toolBar.setMoveableToolBarEnabled(((CustomView) arg0).getId(), true);
                }
            } catch (Exception ex) {
                logger.error("Fehler beim anschalten der MenuBar/Toolbar", ex);
            }
        }

        public void windowHidden(DockingWindow arg0) {
            logger.debug("Docking window hidden");
            try {
                if (arg0 instanceof CustomView) {
                    menuBar.setMoveableMenuesEnabled(((CustomView) arg0).getId(), false);
                    toolBar.setMoveableToolBarEnabled(((CustomView) arg0).getId(), false);
                }
            } catch (Exception ex) {
                logger.error("Fehler beim auschalten der MenuBar/Toolbar", ex);
            }
        }

        public void viewFocusChanged(View arg0, View arg1) {
        }

        public void windowClosing(DockingWindow arg0) throws OperationAbortedException {
        }

        public void windowClosed(DockingWindow arg0) {
        }

        public void windowUndocking(DockingWindow arg0) throws OperationAbortedException {
        }

        public void windowUndocked(DockingWindow arg0) {
            logger.debug("Docking window shown");
            try {
                menuBar.setMoveableMenuesEnabled(((CustomView) arg0).getId(), true);
                toolBar.setMoveableToolBarEnabled(((CustomView) arg0).getId(), true);
            } catch (Exception ex) {
                logger.error("Fehler beim anschalten der MenuBar/Toolbar", ex);
            }
        }

        public void windowDocking(DockingWindow arg0) throws OperationAbortedException {
        }

        public void windowDocked(DockingWindow arg0) {
        }

        public void windowMinimizing(DockingWindow arg0) throws OperationAbortedException {
        }

        public void windowMinimized(DockingWindow arg0) {
        }

        public void windowMaximizing(DockingWindow arg0) throws OperationAbortedException {
        }

        public void windowMaximized(DockingWindow arg0) {
        }

        public void windowRestoring(DockingWindow arg0) throws OperationAbortedException {
        }

        public void windowRestored(DockingWindow arg0) {
        }
    };
}
