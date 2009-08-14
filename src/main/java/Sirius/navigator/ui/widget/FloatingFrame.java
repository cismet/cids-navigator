package Sirius.navigator.ui.widget;

/*******************************************************************************
 *
 * Copyright (c)	:	EIG (Environmental Informatics Group)
 * http://www.htw-saarland.de/eig
 * Prof. Dr. Reiner Guettler
 * Prof. Dr. Ralf Denzer
 *
 * HTWdS
 * Hochschule fuer Technik und Wirtschaft des Saarlandes
 * Goebenstr. 40
 * 66117 Saarbruecken
 * Germany
 *
 * Programmers		:	Pascal
 *
 * Project			:	WuNDA 2
 * Version			:	3.2
 * Purpose			:
 * Created			:	01.02.2000
 * History			:
 *
 *******************************************************************************/
import java.util.*;
import java.beans.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.plaf.*;
import javax.swing.plaf.ToolBarUI;
import javax.swing.plaf.basic.BasicToolBarUI;
import javax.swing.plaf.metal.MetalToolBarUI;

import org.apache.log4j.Logger;

import Sirius.navigator.resource.*;
import Sirius.navigator.ui.embedded.*;


/**
 * FloatingFrame ist ein von JToolBar abgeleiter Container, der wie ein JPanel
 * bzw. JFrame verwendet werden kann. Mann kann ihn aus seinem Parent-Container
 * herauszuziehen und in einem separaten Fenster anzuzeigen.
 * Somit besitzt er die gleiche Funktionalitaet wie eine JToolBar, bei der
 * <b>setFloatable(boolean b)</b> auf <b>true</b> gesetzt wurde.
 * Er verwendet ein angepasstes ToolBarUI, dass das BaiscToolBarUI um eineige
 * Funktionen erweitert.<br><br>
 *
 * Der FloatingFrame kann auch eine MenuBar und eine ToolBar enthalten.
 * Menu- und Toolbar werden ausgeblendet, sobald sich der Frame im Zustand
 * "floating" befindet, d.h. wenn er in einem eigenen Fenster angezeigt wird.<br>
 * Die einzelen Elemente der Tool- und MenuBar (JButtons und JMenus) koennen
 * dann zu der Tool- und MenuBar des Parent-Frames hinzugefuegt werden. Hierzu
 * muessen aber erst die beiden abstrakten Methoden getMenus unf  getToolBarButtons
 * implementiert werden.
 *
 * Damit das Herausziehen auch korrekt funktioniert, sollte der Parent-Container
 * des FloatingFrames (z.B. ein JPanel oder ein JFrame) den LayoutManager
 * "GridLayout" mit nur einer Zelle verwenden und keine weiteren Children haben.<br><br>
 *
 *
 * @see javax.swing.JToolBar
 * @see javax.swing.plaf.basic.BasicToolBarUI
 */
public class FloatingFrame extends JToolBar
{
    private final static Logger logger = Logger.getLogger(FloatingFrame.class);
    
    // Moegliche Positionen der ToolBar
    public static final String NORTH = BorderLayout.NORTH;
    public static final String SOUTH = BorderLayout.SOUTH;
    public static final String EAST = BorderLayout.EAST;
    public static final String WEST = BorderLayout.WEST;
    
    public static final String FLOATING = "floating";
    
    //public static final String START_FLOATING = "floating started";
    //public static final String STOP_FLOATING = "floating stopped";
    
    private JRootPane rootPane = null;
    //private JPanel contentPane = new JPanel();
    
    //protected JPanel contentPanel = new JPanel();
    
    protected JMenuBar menuBar = null;
    protected EmbeddedToolBar toolBar = null;
    
    private FloatingFrameToolBar floatingFrameToolBar;
    private FloatingFrameMenuBar floatingFrameMenuBar;
    
    //protected Dimension menuBarSize;
    //protected Dimension toolBarSize;
    
    protected boolean frameResizable = true;
    //protected String toolBarPosition = NORTH;
    protected Dimension frameSize = null;
    protected Dimension panelSize = null;
    
    //protected Vector dynamicButtons = null;
    //protected Vector dynamicMenus = null;
    
    protected FloatingFrameUI floatingFrameUI = new FloatingFrameUI();
    protected MetalFloatingFrameUI metalFloatingFrameUI = new MetalFloatingFrameUI();
    
    /** Holds value of property configurator. */
    private final FloatingFrameConfigurator configurator;
    
    private boolean allEnabled = false;
    
    private EnablingListener enablingListener = null;
    private final Component content;
    
    private boolean floating = false;
    
    private FloatingPanel floatingPanel = null;
    
    private TitleBar titleBar;
    
    
    // KONSTRUKTOREN ===========================================================
    
    
    public FloatingFrame(Component content, FloatingFrameConfigurator configurator)
    {
        super(configurator.getName());
        this.configurator = configurator;
        this.content = content;
        this.init();
    }
    
    private void init()
    {
        this.updateUI();
        this.setLayout(new BorderLayout());
        
        rootPane = new JRootPane();
        rootPane.setContentPane(new JPanel(new BorderLayout()));
        this.add(rootPane, BorderLayout.CENTER);
        
        if(configurator.isTitleBarEnabled())
        {
            titleBar= new TitleBar(configurator.getName(), ResourceManager.getManager().getIcon("floatingframe.gif"));
            this.add(titleBar, BorderLayout.NORTH);
        
        }
        
        if(configurator.getMenues() != null)
        {
            menuBar = new JMenuBar();
            if(configurator.isSwapMenuBar() || configurator.isDisableMenuBar())
            {
                floatingFrameMenuBar = new FloatingFrameMenuBar(configurator.getId(), configurator.getMenues());
                
                if(configurator.isDisableMenuBar());
                {
                    enablingListener = new EnablingListener();
                    this.addComponentListener(enablingListener);
                }
            }
            else
            {
                Iterator iterator = configurator.getMenues().iterator();
                while(iterator.hasNext())
                {
                    Object object = iterator.next();
                    if(object instanceof JMenu)
                    {
                        menuBar.add((JMenu)object);
                    }
                    else
                    {
                        logger.error("invalid object type '" + object.getClass().getName() + "', 'javax.swing.JMenu' expected");
                    }
                }
                
                rootPane.setJMenuBar(menuBar);
            }
            
            
        }
        
        if(configurator.getButtons() != null)
        {
            toolBar = new EmbeddedToolBar(configurator.getId(), configurator.getButtons());
            toolBar.setName(configurator.getName());
            toolBar.setRollover(configurator.isAdvancedLayout());
            toolBar.setFloatable(false);
            
            if(configurator.isSwapToolBar() || configurator.isDisableToolBar())
            {
                floatingFrameToolBar = new FloatingFrameToolBar(toolBar);
                
                if(configurator.isDisableToolBar() && enablingListener == null);
                {
                    enablingListener = new EnablingListener();
                    this.addComponentListener(enablingListener);
                }
            }
            else
            {
                rootPane.getContentPane().add(toolBar, BorderLayout.NORTH);
                
                /*if(rootPane != null)
                {
                    logger.info("rootPane.getContentPane().add(toolBar, BorderLayout.N O R T H)");
                    rootPane.getContentPane().add(toolBar, BorderLayout.NORTH);
                }
                else
                {
                    logger.info("this.add(toolBar, BorderLayout.NORTH)");
                    this.add(toolBar, BorderLayout.NORTH);
                }*/
            }
        }
        
        /*if(rootPane != null)
        {
            //rootPane.getContentPane().add(new JButton("tt"), BorderLayout.SOUTH);
            rootPane.getContentPane().add(this.content, BorderLayout.CENTER);
        }
        else
        {
            this.add(this.content, BorderLayout.CENTER);
        }*/
        
        rootPane.getContentPane().add(this.content, BorderLayout.CENTER);
        
        this.addPropertyChangeListener("ancestor", new FloatingListener());
    }
    
    public void setTileBarVisible(boolean isVisible){
        configurator.setTitleBarEnabled(isVisible);
        if(titleBar != null){
            titleBar.setVisible(false);
        }
    }
    
    public EmbeddedToolBar getToolBar()
    {
        return this.toolBar;
    }
    
    /**
     * Setzt das Fenster des FloatingFrames auf eine fixe Groesse.
     *
     * @param size Die Groesse des Fensters.
     */
    public void setFixedFrameSize(Dimension size)
    {
        frameSize = size;
        frameResizable = false;
    }
    
    public FloatingPanel getFloatingPanel()
    {
        if(this.floatingPanel == null)
        {
            this.floatingPanel = new FloatingPanel();
        }
        
        return this.floatingPanel;
    }
    
    /**
     * Wird der FloatingFrame in einem eigenen Fenster angezeigt?
     */
    public boolean isFloating()
    {
        if(this.getUI() instanceof FloatingFrameUI)
            return ((FloatingFrameUI)this.getUI()).isFloating();
        else if(this.getUI() instanceof MetalFloatingFrameUI)
            return ((MetalFloatingFrameUI)this.getUI()).isFloating();
        
        return false;
    }
    
    public boolean isFrameResizable()
    {
        return frameResizable;
    }
    
    
    public int getOrientation()
    {
        return HORIZONTAL;
    }
    
    public Dimension getSize()
    {
        if(!isFrameResizable() && isFloating())
            return frameSize;
        
        return super.getSize();
    }
    
    public Dimension getPreferredSize()
    {
        if(this.isFloating())
            return this.getSize();
        
        return super.getPreferredSize();
    }
    
    /**
     * Ueberschreibt updateUI() in JToolBar und passt das UI fuer den
     * FloatingFrame an.
     */
    public void updateUI()
    {
        ComponentUI ui = UIManager.getUI(this);
        //if(logger.isDebugEnabled())logger.debug"<FF> (1) updateUI() currentUI: " + ui);
        //if(logger.isDebugEnabled())logger.debug"<FF> (1) updateUI() UIClassID: " +getUIClassID());
        //if(logger.isDebugEnabled())logger.debug"<FF> (1) updateUI() UIManager UIClass: " + UIManager.getDefaults().getUIClass(this.getUIClassID()).getName());
        
        if(ui instanceof MetalToolBarUI)
        {
            this.setUI(new MetalFloatingFrameUI());
        }
        else if(ui instanceof BasicToolBarUI)
        {
            this.setUI(new FloatingFrameUI());
        }
        else
        {
            //this.setUI(new MetalFloatingFrameUI());
            super.updateUI();
        }
        
        //if(logger.isDebugEnabled())logger.debug"<FF> (2) updateUI() newUI: " + UIManager.getUI(this));
        //if(logger.isDebugEnabled())logger.debug"<FF> (2) updateUI() UIClassID: " +getUIClassID());
        //if(logger.isDebugEnabled())logger.debug"<FF> (2) updateUI() UIManager UIClass: " + UIManager.getDefaults().getUIClass(this.getUIClassID()).getName());
    }
    
    public void setUI(MetalFloatingFrameUI newUI)
    {
        //if(logger.isDebugEnabled())logger.debug"<FF> setMetalFloatingFrameUI(): " + newUI);
        super.setUI(newUI);
    }
    
    public void setUI(FloatingFrameUI newUI)
    {
        //if(logger.isDebugEnabled())logger.debug"<FF> setFloatingFrameUI(): " + newUI);
        super.setUI(newUI);
    }
    
    public MetalFloatingFrameUI getMetalFloatingFrameUI()
    {
        return metalFloatingFrameUI;
    }
    
    public FloatingFrameUI getFloatingFrameUI()
    {
        return floatingFrameUI;
    }
    
    /** Getter for property configurator.
     * @return Value of property configurator.
     *
     */
    public FloatingFrameConfigurator getConfigurator()
    {
        return this.configurator;
    }
    
    // =========================================================================
    
    /**
     * PropertyChangeListener, der auf eine Zustandveraenderung des FloatingFrames
     * reagiert. Blendet Menu- und ToolBar ein und aus.
     */
    class FloatingListener implements PropertyChangeListener
    {
        public void propertyChange( PropertyChangeEvent e )
        {
            if(/*e.getPropertyName().equals("ancestor") && */ isFloatable() && (isFloating() != floating))
            {
                floating = isFloating();
                
                // Groe\u00DFe des Panels merken
                if (panelSize == null)
                {
                    panelSize = getPreferredSize();
                }
                
                // Das Panel wurde "herausgezogen"
                if (isFloating())
                {
                    logger.debug("isFloating() == true");
                    // Loest einen neunen PropertyChangeEvent aus
                    // Der PropertyChangeEvent "anchestor" sollte von anderen
                    // Widgets nicht mehr benutzt werden (um herauszufinden ob der
                    // Frame herausgezogen wird oder nicht), da es sonst zu
                    // Synchronistionsproblemen mit diesem Event kommt!
                    firePropertyChange(FLOATING, false, true);
                    
                    if(configurator.isSwapMenuBar())
                    {
                        floatingFrameMenuBar.setVisible(isFloating());
                    }
                    
                    if(configurator.isSwapToolBar())
                    {
                        floatingFrameToolBar.setVisible(isFloating());
                    }
                }
                // Das Fenster wurde geschlossen bzw. wieder "hereingezogen"
                else
                {
                    logger.debug("isFloating() == false");
                    // Urspruengliche Groesse des Panels wiederherstellen
                    setPreferredSize(panelSize);
                    
                    if(configurator.isSwapMenuBar())
                    {
                        floatingFrameMenuBar.setVisible(isFloating());
                    }
                    
                    if(configurator.isSwapToolBar())
                    {
                        floatingFrameToolBar.setVisible(isFloating());
                    }
                    // Loest einen neunen PropertyChangeEvent aus
                    // Der PropertyChangeEvent "anchestor" sollte von anderen
                    // Widgets nicht mehr benutzt werden (um herauszufinden ob der
                    // Frame herausgezogen wird oder nicht), da es sonst zu
                    // Synchronistionsproblemen mit diesem Event kommt!
                    firePropertyChange(FLOATING, true, false);
                }
            }
        }
    }
    
    private class EnablingListener extends ComponentAdapter
    {
        
        public void componentShown(ComponentEvent ce)
        {
            //NavigatorLogger.printMessage("ISF SHOWN");
            allEnabled = true;
            
            // Wenn der FloatingFrame sich nicht im Navigator befindet,
            // mussen auch die Buttons + Menues in der Navigator ToolBar
            // nicht disabled werden.
            if(!isFloating())
            {
                if(logger.isDebugEnabled())logger.debug("setting floating frame menu/toolbar enabled to 'true'");
                if(configurator.isDisableToolBar())
                {
                    floatingFrameToolBar.setEnabled(allEnabled);
                }
                
                if(configurator.isSwapMenuBar())
                {
                    floatingFrameMenuBar.setEnabled(allEnabled);
                }
            }
        }
        
        public void componentHidden(ComponentEvent ce)
        {
            allEnabled = false;
            
            // Wenn der FloatingFrame sich nicht im Navigator befindet,
            // mussen auch die Buttons + Menues in der Navigator ToolBar
            // nicht disabled werden.
            if(!isFloating())
            {
                if(logger.isDebugEnabled())logger.debug("setting floating frame menu/toolbar enabled to 'false'");
                if(configurator.isDisableToolBar())
                {
                    floatingFrameToolBar.setEnabled(allEnabled);
                }
                
                if(configurator.isSwapMenuBar())
                {
                    floatingFrameMenuBar.setEnabled(allEnabled);
                }
            }
        }
    }
    
    // -------------------------------------------------------------------------
    
    private class FloatingFrameMenuBar extends EmbeddedContainer
    {
        public FloatingFrameMenuBar(String id, Collection components)
        {
            super(id, components);
            this.addComponents();
        }
        
        public void setVisible(final boolean visible)
        {
            logger.debug("FloatingFrameMenuBar:setVisible(" + visible + ")");
            if(SwingUtilities.isEventDispatchThread())
            {
                doSetVisible(visible);
            }
            else
            {
                logger.debug("setVisible(): synchronizing method");
                SwingUtilities.invokeLater(new Runnable()
                {
                    public void run()
                    {
                        doSetVisible(visible);
                    }
                });
            }
        }
        
        private void doSetVisible(boolean visible)
        {
            logger.debug("FloatingFrameMenuBar:doSetVisible(" + visible + ")");
            if(this.isVisible() != visible)
            {
                super.setVisible(visible);
                
                if(visible)
                {
                    this.addComponents();
                }
                else
                {
                    this.removeComponents();
                }
            }
            else
            {
                this.logger.warn("unexpected call to 'setVisible()': '" + visible + "'");
            }
        }
        
        protected void addComponents()
        {
            logger.debug("FloatingFrameMenuBar:addComponents()");
            ComponentIterator iterator = this.iterator();
            while(iterator.hasNext())
            {
                JComponent component = iterator.next();
                if(component != null)
                {
                    if(component instanceof JMenu)
                    {
                        FloatingFrame.this.menuBar.add((JMenu)component);
                    }
                    else
                    {
                        this.logger.error("addComponents(): invalid object type '" + component.getClass().getName() + "', 'javax.swing.JMenu' expected");
                    }
                }
            }
            
            FloatingFrame.this.rootPane.setJMenuBar(FloatingFrame.this.menuBar);
        }
        
        protected void removeComponents()
        {
            logger.debug("FloatingFrameMenuBar:removeComponents()");
            FloatingFrame.this.menuBar.removeAll();
            FloatingFrame.this.rootPane.setJMenuBar(null);
        }
    }
    
    private class FloatingFrameToolBar extends AbstractEmbeddedComponentsMap
    {
        public FloatingFrameToolBar(EmbeddedToolBar toolBar)
        {
            super();
            this.add(toolBar);
        }
        
        public void setVisible(boolean visible)
        {
            logger.debug("FloatingFrameToolBar:setVisible(" + visible + ")");
            this.setVisible(toolBar.getId(), visible);
        }
        
        public void setEnabled(boolean enabled)
        {
            logger.debug("FloatingFrameToolBar:setEnabled(" + enabled + ")");
            this.setEnabled(toolBar.getId(), enabled);
        }
        
        protected void doAdd(EmbeddedComponent component)
        {
            logger.debug("FloatingFrameToolBar:doAdd()");
            if(component instanceof EmbeddedToolBar)
            {
                if(FloatingFrame.this.rootPane != null)
                {
                    logger.info("rootPane");
                    FloatingFrame.this.rootPane.getContentPane().add((JToolBar)component, BorderLayout.NORTH);
                }
                else
                {
                    logger.info(component);
                    FloatingFrame.this.add((JToolBar)component, BorderLayout.NORTH);
                }
            }
            else
            {
                this.logger.error("doAdd(): invalid object type '" + component.getClass().getName() + "', 'Sirius.navigator.EmbeddedToolBar' expected");
            }
        }
        
        protected void doRemove(EmbeddedComponent component)
        {
            logger.debug("FloatingFrameToolBar:doRemove()");
            if(component instanceof EmbeddedToolBar)
            {
                /*if(FloatingFrame.this.rootPane != null)
                {
                    FloatingFrame.this.rootPane.getContentPane().remove((JToolBar)component);
                }
                else
                {
                    FloatingFrame.this.remove((JToolBar)component);
                }*/
                
                FloatingFrame.this.rootPane.getContentPane().remove((JToolBar)component);
            }
            else
            {
                this.logger.error("doRemove(): invalid object type '" + component.getClass().getName() + "', 'Sirius.navigator.EmbeddedToolBar' expected");
            }
        }
        
        protected void doSetVisible(EmbeddedComponent component, boolean visible)
        {
            logger.debug("FloatingFrameToolBar:doSetVisible()");
            if(component.isVisible() != visible)
            {
                super.doSetVisible(component, visible);
                
                if(visible)
                {
                    this.doAdd(component);
                }
                else
                {
                    this.doRemove(component);
                }
            }
            else
            {
                this.logger.warn("unexpected call to 'setVisible()': '" + visible + "'");
            }
        }
    }
    
    private class FloatingPanel extends JPanel implements ComponentListener
    {
        public FloatingPanel()
        {
            super(new GridLayout(1,1));
            this.add(FloatingFrame.this);
            this.addComponentListener(this);
        }
        
        /** Invoked when the component has been made invisible.
         *
         */
        public void componentHidden(ComponentEvent e)
        {
            //logger.debug("dispatching event: " + e);
            FloatingFrame.this.dispatchEvent(e);
        }
        
        /** Invoked when the component's position changes.
         *
         */
        public void componentMoved(ComponentEvent e)
        {
            //logger.debug("ignoring event: " + e);
        }
        
        /** Invoked when the component's size changes.
         *
         */
        public void componentResized(ComponentEvent e)
        {
            //logger.debug("ignoring event: " + e);
        }
        
        /** Invoked when the component has been made visible.
         *
         */
        public void componentShown(ComponentEvent e)
        {
            //logger.debug("dispatching event: " + e);
            FloatingFrame.this.dispatchEvent(e);
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    // UI CLASSES
    // #############################################################################
    
    /**
     * FloatingFrameUI ist eine L&F Implementation fuer den FloatingFrame.
     * @author Pascal Dihe
     */
    class FloatingFrameUI extends BasicToolBarUI
    {
        /**
         * Diese Funktion wurde ueberschrieben um bestimmte Eigenschaften des
         * FloatingFrames zu aendern z.B. Groesse, Titel, etc.
         * @param toolBar
         * @return
         */
        protected JFrame createFloatingFrame(JToolBar toolBar)
        {
            //if(logger.isDebugEnabled())logger.debug"<FF> (BasicToolBarUI) createFloatingFrame: " + toolBar.getClass().getName());
            
            WindowListener wl = createFrameListener();
            JFrame frame = new JFrame(toolBar.getName());
            FloatingFrame ff;
            
            if(toolBar instanceof FloatingFrame)
            {
                ff = (FloatingFrame)toolBar;
                frame.setTitle(ff.getName());
                frame.setSize(ff.getSize());
                frame.setResizable(ff.isFrameResizable());
                frame.setIconImage(ff.getConfigurator().getIcon().getImage());
            }
            else
            {
                frame.setTitle(toolBar.getName());
                frame.setResizable(false);
            }
            
            frame.setResizable(true);
            
            frame.addWindowListener(wl);
            return frame;
        }
        
        /**
         * Diese Funktion wurde ueberschrieben um das DragWindow (wird beim
         * Herauszeihen des Frame angezeigt) korrekt darzustellen. Das DragWindow
         * hat nun die gleiche Groesse wie der FloatingFrame.
         */
        protected void dragTo(Point position, Point origin)
        {
            super.dragTo(position, origin);
            if(toolBar instanceof FloatingFrame && dragWindow != null)
                dragWindow.setSize(toolBar.getSize());
        }
        
        protected RootPaneContainer createFloatingWindow(JToolBar toolbar)
        {
            if(logger.isDebugEnabled())logger.debug("<FF> () createFloatingWindow(): " + toolBar.getClass().getName());
            
            FloatingFrame ff;
            class FloatingDialog extends JFrame //JDialog
            {
                public FloatingDialog()
                {
                    super();
                }
                
                /*public FloatingDialog(Frame owner, String title, boolean modal)
                {
                    super(owner, title, modal);
                }
                 
                public FloatingDialog(Dialog owner, String title, boolean modal)
                {
                    super(owner, title, modal);
                }*/
                
                // Override createRootPane() to automatically resize
                // the frame when contents change
                protected JRootPane createRootPane()
                {
                    JRootPane rootPane = new JRootPane()
                    {
                        private boolean packing = false;
                        
                        public void validate()
                        {
                            super.validate();
                            if (!packing)
                            {
                                packing = true;
                                pack();
                                packing = false;
                            }
                        }
                    };
                    
                    rootPane.setOpaque(true);
                    
                    return rootPane;
                }
            }
            
            //JDialog floatingDialog;
            
            JFrame floatingDialog;
            
        /*Window window = SwingUtilities.getWindowAncestor(toolbar);
         
        if (window instanceof Frame)
        {
            floatingDialog = new FloatingDialog((Frame)window, toolbar.getName(), false);
        }
        else if (window instanceof Dialog)
        {
            floatingDialog = new FloatingDialog((Dialog)window, toolbar.getName(), false);
        }
        else
        {
            floatingDialog = new FloatingDialog((Frame)null, toolbar.getName(), false);
        }*/
            
            floatingDialog = new FloatingDialog();
            
            if(toolBar instanceof FloatingFrame)
            {
                ff = (FloatingFrame)toolbar;
                floatingDialog.setTitle(ff.getName());
                floatingDialog.setSize(ff.getSize());
                floatingDialog.setResizable(ff.isFrameResizable());
            }
            else
            {
                floatingDialog.setTitle(toolBar.getName());
                floatingDialog.setResizable(false);
            }
            
            WindowListener wl = createFrameListener();
            floatingDialog.addWindowListener(wl);
            
            return floatingDialog;
        }
    }
    
    /**
     * MetalFloatingFrameUI ist eine L&F Implementation fuer den FloatingFrame.
     */
    class MetalFloatingFrameUI extends MetalToolBarUI
    {
        /**
         * Diese Funktion wurde ueberschrieben um bestimmte Eigenschaften des
         * FloatingFrames zu aendern z.B. Groesse, Titel, etc.
         * @param toolBar
         * @return
         */
        protected JFrame createFloatingFrame(JToolBar toolBar)
        {
            if(logger.isDebugEnabled())logger.debug("<FF> () createFloatingFrame: " + toolBar.getClass().getName());
            
            WindowListener wl = createFrameListener();
            JFrame frame = new JFrame(toolBar.getName());
            FloatingFrame ff;
            
            if(toolBar instanceof FloatingFrame)
            {
                ff = (FloatingFrame)toolBar;
                frame.setTitle(ff.getName());
                frame.setSize(ff.getSize());
                frame.setResizable(ff.isFrameResizable());
                frame.setIconImage(ff.getConfigurator().getIcon().getImage());
            }
            else
            {
                frame.setTitle(toolBar.getName());
                frame.setResizable(false);
            }
            
            frame.addWindowListener(wl);
            return frame;
        }
        
        /**
         * Diese Funktion wurde ueberschrieben um das DragWindow (wird beim
         * Herauszeihen des Frame angezeigt) korrekt darzustellen. Das DragWindow
         * hat nun die gleiche Groesse wie der FloatingFrame.
         */
        protected void dragTo(Point position, Point origin)
        {
            super.dragTo(position, origin);
            if(toolBar instanceof FloatingFrame && dragWindow != null)
                dragWindow.setSize(toolBar.getSize());
        }
        
        protected MouseInputListener createDockingListener()
        {
            return new FloatingFrameDockingListener(toolBar);
        }
        
        /**
         * Ein neuer DockingListener fuer den FloatingFrame.
         */
        protected class FloatingFrameDockingListener extends DockingListener
        {
            private boolean pressedInBumps = false;
            
            public FloatingFrameDockingListener(JToolBar t)
            {
                super(t);
            }
            
            public void mousePressed( MouseEvent e )
            {
                super.mousePressed( e );
                
                if (!toolBar.isEnabled())
                {
                    return;
                }
                
                pressedInBumps = false;
                
                // Zeichnet ein unsichtbares Rechteck ueber den Anfasser
                // auf der linken Seite des FloatingFrames. Nur wenn sich der
                // MousePointer innerhalb dieses Rechtecks befindet, werden die
                // <b>mouseDragged</b> Events verarbeitet.
                Rectangle bumpRect = new Rectangle();
                bumpRect.setBounds( 0, 0, 14, toolBar.getSize().height );
                
                if ( bumpRect.contains( e.getPoint()))
                {
                    pressedInBumps = true;
                    Point dragOffset = e.getPoint();
                    setDragOffset(dragOffset);
                }
            }
            
            public void mouseDragged( MouseEvent e )
            {
                if ( pressedInBumps )
                    super.mouseDragged( e );
            }
        }
        
        protected RootPaneContainer createFloatingWindow(JToolBar toolbar)
        {
            if(logger.isDebugEnabled())logger.debug("<FF> () createFloatingWindow(): " + toolBar.getClass().getName());
            
            FloatingFrame ff;
            class FloatingDialog extends JFrame //JDialog
            {
                public FloatingDialog()
                {
                    super();
                }
                
                /*public FloatingDialog(Frame owner, String title, boolean modal)
                {
                    super(owner, title, modal);
                }
                 
                public FloatingDialog(Dialog owner, String title, boolean modal)
                {
                    super(owner, title, modal);
                }*/
                
                // Override createRootPane() to automatically resize
                // the frame when contents change
                protected JRootPane createRootPane()
                {
                    JRootPane rootPane = new JRootPane()
                    {
                        private boolean packing = false;
                        
                        public void validate()
                        {
                            super.validate();
                            if (!packing)
                            {
                                packing = true;
                                pack();
                                packing = false;
                            }
                        }
                    };
                    
                    rootPane.setOpaque(true);
                    
                    return rootPane;
                }
            }
            
            //JDialog floatingDialog;
            
            JFrame floatingDialog;
            
        /*Window window = SwingUtilities.getWindowAncestor(toolbar);
         
        if (window instanceof Frame)
        {
            floatingDialog = new FloatingDialog((Frame)window, toolbar.getName(), false);
        }
        else if (window instanceof Dialog)
        {
            floatingDialog = new FloatingDialog((Dialog)window, toolbar.getName(), false);
        }
        else
        {
            floatingDialog = new FloatingDialog((Frame)null, toolbar.getName(), false);
        }*/
            
            floatingDialog = new FloatingDialog();
            
            if(toolBar instanceof FloatingFrame)
            {
                ff = (FloatingFrame)toolbar;
                floatingDialog.setTitle(ff.getName());
                floatingDialog.setSize(ff.getSize());
                floatingDialog.setResizable(ff.isFrameResizable());
            }
            else
            {
                floatingDialog.setTitle(toolBar.getName());
                floatingDialog.setResizable(false);
            }
            
            WindowListener wl = createFrameListener();
            floatingDialog.addWindowListener(wl);
            
            return floatingDialog;
        }
    }
    
    /*public static void main(String args[])
    {
        org.apache.log4j.BasicConfigurator.configure();
     
        JFrame jf = new JFrame("FloatingFrameTest");
        jf.setSize(640,480);
        jf.setLocationRelativeTo(null);
        jf.setContentPane(new JPanel(new BorderLayout()));
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     
        FloatingFrameConfigurator ffc = new FloatingFrameConfigurator();
        ffc.setName("FloatingFrame");
     
        JPanel content = new JPanel(new GridLayout(1,1));
        content.add(new JButton("FLOATING FRAME"));
     
        JButton jb = new JButton("Button");
        JToolBar jtb = new JToolBar("ToolBar");
        jtb.add(jb);
        jf.getContentPane().add(jtb, BorderLayout.SOUTH);
     
        ArrayList buttons = new ArrayList();
        buttons.add(jb);
        ffc.setButtons(buttons);
        ffc.setSwapToolBar(true);
     
        JMenu jm = new JMenu("Menu");
        JMenuBar jmb= new JMenuBar();
        jmb.add(jm);
        jf.setJMenuBar(jmb);
     
        ArrayList menues = new ArrayList();
        menues.add(jm);
        ffc.setMenues(menues);
        ffc.setSwapMenuBar(true);
     
        FloatingFrame ff = new FloatingFrame(content, ffc);
        JPanel jp = new JPanel(new GridLayout(1,1));
        jp.add(ff);
        jf.getContentPane().add(jp, BorderLayout.CENTER);
        jf.setVisible(true);
    }*/
}

