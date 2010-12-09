/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui.widget;

/*******************************************************************************
 *
 * Copyright (c)        :       EIG (Environmental Informatics Group)
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
 * Programmers          :       Pascal
 *
 * Project                      :       WuNDA 2
 * Version                      :       3.2
 * Purpose                      :
 * Created                      :       01.02.2000
 * History                      :
 *
 *******************************************************************************/
import Sirius.navigator.resource.*;
import Sirius.navigator.ui.embedded.*;

import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.event.*;

import java.beans.*;

import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.plaf.*;
import javax.swing.plaf.ToolBarUI;
import javax.swing.plaf.basic.BasicToolBarUI;
import javax.swing.plaf.metal.MetalToolBarUI;

/**
 * FloatingFrame ist ein von JToolBar abgeleiter Container, der wie ein JPanel bzw. JFrame verwendet werden kann. Mann
 * kann ihn aus seinem Parent-Container herauszuziehen und in einem separaten Fenster anzuzeigen. Somit besitzt er die
 * gleiche Funktionalitaet wie eine JToolBar, bei der <b>setFloatable(boolean b)</b> auf <b>true</b> gesetzt wurde. Er
 * verwendet ein angepasstes ToolBarUI, dass das BaiscToolBarUI um eineige Funktionen erweitert.<br>
 * <br>
 *
 * <p>Der FloatingFrame kann auch eine MenuBar und eine ToolBar enthalten. Menu- und Toolbar werden ausgeblendet, sobald
 * sich der Frame im Zustand "floating" befindet, d.h. wenn er in einem eigenen Fenster angezeigt wird.<br>
 * Die einzelen Elemente der Tool- und MenuBar (JButtons und JMenus) koennen dann zu der Tool- und MenuBar des
 * Parent-Frames hinzugefuegt werden. Hierzu muessen aber erst die beiden abstrakten Methoden getMenus unf
 * getToolBarButtons implementiert werden.</p>
 *
 * <p>Damit das Herausziehen auch korrekt funktioniert, sollte der Parent-Container des FloatingFrames (z.B. ein JPanel
 * oder ein JFrame) den LayoutManager "GridLayout" mit nur einer Zelle verwenden und keine weiteren Children haben.<br>
 * <br>
 * </p>
 *
 * @version  $Revision$, $Date$
 * @see      javax.swing.JToolBar
 * @see      javax.swing.plaf.basic.BasicToolBarUI
 */
public class FloatingFrame extends JToolBar {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger logger = Logger.getLogger(FloatingFrame.class);
    private static final ResourceManager resource = ResourceManager.getManager();

    // Moegliche Positionen der ToolBar
    public static final String NORTH = BorderLayout.NORTH;
    public static final String SOUTH = BorderLayout.SOUTH;
    public static final String EAST = BorderLayout.EAST;
    public static final String WEST = BorderLayout.WEST;

    public static final String FLOATING = "floating"; // NOI18N

    //~ Instance fields --------------------------------------------------------

    // protected JPanel contentPanel = new JPanel();

    // NOI18N

    // protected JPanel contentPanel = new JPanel();

    protected JMenuBar menuBar = null;
    protected EmbeddedToolBar toolBar = null;

    // protected Dimension menuBarSize;
    // protected Dimension toolBarSize;

    protected boolean frameResizable = true;
    // protected String toolBarPosition = NORTH;
    protected Dimension frameSize = null;
    protected Dimension panelSize = null;

    // protected Vector dynamicButtons = null;
    // protected Vector dynamicMenus = null;

    protected FloatingFrameUI floatingFrameUI = new FloatingFrameUI();
    protected MetalFloatingFrameUI metalFloatingFrameUI = new MetalFloatingFrameUI();

    // public static final String START_FLOATING = "floating started";
    // public static final String STOP_FLOATING = "floating stopped";

    private JRootPane rootPane = null;
    // private JPanel contentPane = new JPanel();

    private FloatingFrameToolBar floatingFrameToolBar;
    private FloatingFrameMenuBar floatingFrameMenuBar;

    /** Holds value of property configurator. */
    private final FloatingFrameConfigurator configurator;

    private boolean allEnabled = false;

    private EnablingListener enablingListener = null;
    private final Component content;

    private boolean floating = false;

    private FloatingPanel floatingPanel = null;

    private TitleBar titleBar;

    //~ Constructors -----------------------------------------------------------

    /**
     * KONSTRUKTOREN ===========================================================.
     *
     * @param  content       DOCUMENT ME!
     * @param  configurator  DOCUMENT ME!
     */
    public FloatingFrame(final Component content, final FloatingFrameConfigurator configurator) {
        super(configurator.getName());
        this.configurator = configurator;
        this.content = content;
        this.init();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    private void init() {
        this.updateUI();
        this.setLayout(new BorderLayout());

        rootPane = new JRootPane();
        rootPane.setContentPane(new JPanel(new BorderLayout()));
        this.add(rootPane, BorderLayout.CENTER);

        if (configurator.isTitleBarEnabled()) {
            titleBar = new TitleBar(configurator.getName(),
                    resource.getIcon("floatingframe.gif"));
            this.add(titleBar, BorderLayout.NORTH);
        }

        if (configurator.getMenues() != null) {
            menuBar = new JMenuBar();
            if (configurator.isSwapMenuBar() || configurator.isDisableMenuBar()) {
                floatingFrameMenuBar = new FloatingFrameMenuBar(configurator.getId(), configurator.getMenues());

                if (configurator.isDisableMenuBar()) {
                    ;
                }
                {
                    enablingListener = new EnablingListener();
                    this.addComponentListener(enablingListener);
                }
            } else {
                final Iterator iterator = configurator.getMenues().iterator();
                while (iterator.hasNext()) {
                    final Object object = iterator.next();
                    if (object instanceof JMenu) {
                        menuBar.add((JMenu)object);
                    } else {
                        logger.error("invalid object type '" + object.getClass().getName()
                                    + "', 'javax.swing.JMenu' expected"); // NOI18N
                    }
                }

                rootPane.setJMenuBar(menuBar);
            }
        }

        if (configurator.getButtons() != null) {
            toolBar = new EmbeddedToolBar(configurator.getId(), configurator.getButtons());
            toolBar.setName(configurator.getName());
            toolBar.setRollover(configurator.isAdvancedLayout());
            toolBar.setFloatable(false);

            if (configurator.isSwapToolBar() || configurator.isDisableToolBar()) {
                floatingFrameToolBar = new FloatingFrameToolBar(toolBar);

                if (configurator.isDisableToolBar() && (enablingListener == null)) {
                    ;
                }
                {
                    enablingListener = new EnablingListener();
                    this.addComponentListener(enablingListener);
                }
            } else {
                rootPane.getContentPane().add(toolBar, BorderLayout.NORTH);

                /*if(rootPane != null)
                 * { logger.info("rootPane.getContentPane().add(toolBar, BorderLayout.N O R T H)");
                 * rootPane.getContentPane().add(toolBar, BorderLayout.NORTH); } else { logger.info("this.add(toolBar,
                 * BorderLayout.NORTH)"); this.add(toolBar, BorderLayout.NORTH);}*/
            }
        }

        /*if(rootPane != null)
         * { //rootPane.getContentPane().add(new JButton("tt"), BorderLayout.SOUTH);
         * rootPane.getContentPane().add(this.content, BorderLayout.CENTER); } else { this.add(this.content,
         * BorderLayout.CENTER);}*/

        rootPane.getContentPane().add(this.content, BorderLayout.CENTER);

        this.addPropertyChangeListener("ancestor", new FloatingListener()); // NOI18N
    }

    /**
     * DOCUMENT ME!
     *
     * @param  isVisible  DOCUMENT ME!
     */
    public void setTileBarVisible(final boolean isVisible) {
        configurator.setTitleBarEnabled(isVisible);
        if (titleBar != null) {
            titleBar.setVisible(false);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public EmbeddedToolBar getToolBar() {
        return this.toolBar;
    }

    /**
     * Setzt das Fenster des FloatingFrames auf eine fixe Groesse.
     *
     * @param  size  Die Groesse des Fensters.
     */
    public void setFixedFrameSize(final Dimension size) {
        frameSize = size;
        frameResizable = false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public FloatingPanel getFloatingPanel() {
        if (this.floatingPanel == null) {
            this.floatingPanel = new FloatingPanel();
        }

        return this.floatingPanel;
    }

    /**
     * Wird der FloatingFrame in einem eigenen Fenster angezeigt?
     *
     * @return  DOCUMENT ME!
     */
    public boolean isFloating() {
        if (this.getUI() instanceof FloatingFrameUI) {
            return ((FloatingFrameUI)this.getUI()).isFloating();
        } else if (this.getUI() instanceof MetalFloatingFrameUI) {
            return ((MetalFloatingFrameUI)this.getUI()).isFloating();
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isFrameResizable() {
        return frameResizable;
    }

    @Override
    public int getOrientation() {
        return HORIZONTAL;
    }

    @Override
    public Dimension getSize() {
        if (!isFrameResizable() && isFloating()) {
            return frameSize;
        }

        return super.getSize();
    }

    @Override
    public Dimension getPreferredSize() {
        if (this.isFloating()) {
            return this.getSize();
        }

        return super.getPreferredSize();
    }

    /**
     * Ueberschreibt updateUI() in JToolBar und passt das UI fuer den FloatingFrame an.
     */
    @Override
    public void updateUI() {
        final ComponentUI ui = UIManager.getUI(this);
        // if(logger.isDebugEnabled())logger.debug"<FF> (1) updateUI() currentUI: " + ui);
        // if(logger.isDebugEnabled())logger.debug"<FF> (1) updateUI() UIClassID: " +getUIClassID());
        // if(logger.isDebugEnabled())logger.debug"<FF> (1) updateUI() UIManager UIClass: " +
        // UIManager.getDefaults().getUIClass(this.getUIClassID()).getName());

        if (ui instanceof MetalToolBarUI) {
            this.setUI(new MetalFloatingFrameUI());
        } else if (ui instanceof BasicToolBarUI) {
            this.setUI(new FloatingFrameUI());
        } else {
            // this.setUI(new MetalFloatingFrameUI());
            super.updateUI();
        }

        // if(logger.isDebugEnabled())logger.debug"<FF> (2) updateUI() newUI: " + UIManager.getUI(this));
        // if(logger.isDebugEnabled())logger.debug"<FF> (2) updateUI() UIClassID: " +getUIClassID());
        // if(logger.isDebugEnabled())logger.debug"<FF> (2) updateUI() UIManager UIClass: " +
        // UIManager.getDefaults().getUIClass(this.getUIClassID()).getName());
    }

    /**
     * DOCUMENT ME!
     *
     * @param  newUI  DOCUMENT ME!
     */
    public void setUI(final MetalFloatingFrameUI newUI) {
        // if(logger.isDebugEnabled())logger.debug"<FF> setMetalFloatingFrameUI(): " + newUI);
        super.setUI(newUI);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  newUI  DOCUMENT ME!
     */
    public void setUI(final FloatingFrameUI newUI) {
        // if(logger.isDebugEnabled())logger.debug"<FF> setFloatingFrameUI(): " + newUI);
        super.setUI(newUI);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public MetalFloatingFrameUI getMetalFloatingFrameUI() {
        return metalFloatingFrameUI;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public FloatingFrameUI getFloatingFrameUI() {
        return floatingFrameUI;
    }

    /**
     * Getter for property configurator.
     *
     * @return  Value of property configurator.
     */
    public FloatingFrameConfigurator getConfigurator() {
        return this.configurator;
    }

    // =========================================================================

    //~ Inner Classes ----------------------------------------------------------

    /**
     * PropertyChangeListener, der auf eine Zustandveraenderung des FloatingFrames reagiert. Blendet Menu- und ToolBar
     * ein und aus.
     *
     * @version  $Revision$, $Date$
     */
    class FloatingListener implements PropertyChangeListener {

        //~ Methods ------------------------------------------------------------

        @Override
        public void propertyChange(final PropertyChangeEvent e) {
            if ( /*e.getPropertyName().equals("ancestor") && */isFloatable() && (isFloating() != floating)) {
                floating = isFloating();

                // Groe\u00DFe des Panels merken
                if (panelSize == null) {
                    panelSize = getPreferredSize();
                }

                // Das Panel wurde "herausgezogen"
                if (isFloating()) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("isFloating() == true"); // NOI18N
                    }
                    // Loest einen neunen PropertyChangeEvent aus
                    // Der PropertyChangeEvent "anchestor" sollte von anderen
                    // Widgets nicht mehr benutzt werden (um herauszufinden ob der
                    // Frame herausgezogen wird oder nicht), da es sonst zu
                    // Synchronistionsproblemen mit diesem Event kommt!
                    firePropertyChange(FLOATING, false, true);

                    if (configurator.isSwapMenuBar()) {
                        floatingFrameMenuBar.setVisible(isFloating());
                    }

                    if (configurator.isSwapToolBar()) {
                        floatingFrameToolBar.setVisible(isFloating());
                    }
                }
                // Das Fenster wurde geschlossen bzw. wieder "hereingezogen"
                else {
                    if (logger.isDebugEnabled()) {
                        logger.debug("isFloating() == false"); // NOI18N
                    }
                    // Urspruengliche Groesse des Panels wiederherstellen
                    setPreferredSize(panelSize);

                    if (configurator.isSwapMenuBar()) {
                        floatingFrameMenuBar.setVisible(isFloating());
                    }

                    if (configurator.isSwapToolBar()) {
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

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class EnablingListener extends ComponentAdapter {

        //~ Methods ------------------------------------------------------------

        @Override
        public void componentShown(final ComponentEvent ce) {
            // NavigatorLogger.printMessage("ISF SHOWN");
            allEnabled = true;

            // Wenn der FloatingFrame sich nicht im Navigator befindet,
            // mussen auch die Buttons + Menues in der Navigator ToolBar
            // nicht disabled werden.
            if (!isFloating()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("setting floating frame menu/toolbar enabled to 'true'"); // NOI18N
                }
                if (configurator.isDisableToolBar()) {
                    floatingFrameToolBar.setEnabled(allEnabled);
                }

                if (configurator.isSwapMenuBar()) {
                    floatingFrameMenuBar.setEnabled(allEnabled);
                }
            }
        }

        @Override
        public void componentHidden(final ComponentEvent ce) {
            allEnabled = false;

            // Wenn der FloatingFrame sich nicht im Navigator befindet,
            // mussen auch die Buttons + Menues in der Navigator ToolBar
            // nicht disabled werden.
            if (!isFloating()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("setting floating frame menu/toolbar enabled to 'false'"); // NOI18N
                }
                if (configurator.isDisableToolBar()) {
                    floatingFrameToolBar.setEnabled(allEnabled);
                }

                if (configurator.isSwapMenuBar()) {
                    floatingFrameMenuBar.setEnabled(allEnabled);
                }
            }
        }
    }

    /**
     * -------------------------------------------------------------------------.
     *
     * @version  $Revision$, $Date$
     */
    private class FloatingFrameMenuBar extends EmbeddedContainer {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new FloatingFrameMenuBar object.
         *
         * @param  id          DOCUMENT ME!
         * @param  components  DOCUMENT ME!
         */
        public FloatingFrameMenuBar(final String id, final Collection components) {
            super(id, components);
            this.addComponents();
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public void setVisible(final boolean visible) {
            if (logger.isDebugEnabled()) {
                logger.debug("FloatingFrameMenuBar:setVisible(" + visible + ")"); // NOI18N
            }
            if (SwingUtilities.isEventDispatchThread()) {
                doSetVisible(visible);
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("setVisible(): synchronizing method");           // NOI18N
                }
                SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            doSetVisible(visible);
                        }
                    });
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param  visible  DOCUMENT ME!
         */
        private void doSetVisible(final boolean visible) {
            if (logger.isDebugEnabled()) {
                logger.debug("FloatingFrameMenuBar:doSetVisible(" + visible + ")"); // NOI18N
            }
            if (this.isVisible() != visible) {
                super.setVisible(visible);

                if (visible) {
                    this.addComponents();
                } else {
                    this.removeComponents();
                }
            } else {
                this.logger.warn("unexpected call to 'setVisible()': '" + visible + "'"); // NOI18N
            }
        }

        @Override
        protected void addComponents() {
            if (logger.isDebugEnabled()) {
                logger.debug("FloatingFrameMenuBar:addComponents()");     // NOI18N
            }
            final ComponentIterator iterator = this.iterator();
            while (iterator.hasNext()) {
                final JComponent component = iterator.next();
                if (component != null) {
                    if (component instanceof JMenu) {
                        FloatingFrame.this.menuBar.add((JMenu)component);
                    } else {
                        this.logger.error("addComponents(): invalid object type '" + component.getClass().getName()
                                    + "', 'javax.swing.JMenu' expected"); // NOI18N
                    }
                }
            }

            FloatingFrame.this.rootPane.setJMenuBar(FloatingFrame.this.menuBar);
        }

        @Override
        protected void removeComponents() {
            if (logger.isDebugEnabled()) {
                logger.debug("FloatingFrameMenuBar:removeComponents()"); // NOI18N
            }
            FloatingFrame.this.menuBar.removeAll();
            FloatingFrame.this.rootPane.setJMenuBar(null);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class FloatingFrameToolBar extends AbstractEmbeddedComponentsMap {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new FloatingFrameToolBar object.
         *
         * @param  toolBar  DOCUMENT ME!
         */
        public FloatingFrameToolBar(final EmbeddedToolBar toolBar) {
            super();
            this.add(toolBar);
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @param  visible  DOCUMENT ME!
         */
        public void setVisible(final boolean visible) {
            if (logger.isDebugEnabled()) {
                logger.debug("FloatingFrameToolBar:setVisible(" + visible + ")"); // NOI18N
            }
            this.setVisible(toolBar.getId(), visible);
        }

        /**
         * DOCUMENT ME!
         *
         * @param  enabled  DOCUMENT ME!
         */
        public void setEnabled(final boolean enabled) {
            if (logger.isDebugEnabled()) {
                logger.debug("FloatingFrameToolBar:setEnabled(" + enabled + ")"); // NOI18N
            }
            this.setEnabled(toolBar.getId(), enabled);
        }

        @Override
        protected void doAdd(final EmbeddedComponent component) {
            if (logger.isDebugEnabled()) {
                logger.debug("FloatingFrameToolBar:doAdd()");                    // NOI18N
            }
            if (component instanceof EmbeddedToolBar) {
                if (FloatingFrame.this.rootPane != null) {
                    logger.info("rootPane");                                     // NOI18N
                    FloatingFrame.this.rootPane.getContentPane().add((JToolBar)component, BorderLayout.NORTH);
                } else {
                    logger.info(component);                                      // NOI18N
                    FloatingFrame.this.add((JToolBar)component, BorderLayout.NORTH);
                }
            } else {
                this.logger.error("doAdd(): invalid object type '" + component.getClass().getName()
                            + "', 'Sirius.navigator.EmbeddedToolBar' expected"); // NOI18N
            }
        }

        @Override
        protected void doRemove(final EmbeddedComponent component) {
            if (logger.isDebugEnabled()) {
                logger.debug("FloatingFrameToolBar:doRemove()"); // NOI18N
            }
            if (component instanceof EmbeddedToolBar) {
                /*if(FloatingFrame.this.rootPane != null)
                 * { FloatingFrame.this.rootPane.getContentPane().remove((JToolBar)component); } else {
                 * FloatingFrame.this.remove((JToolBar)component);}*/

                FloatingFrame.this.rootPane.getContentPane().remove((JToolBar)component);
            } else {
                this.logger.error("doRemove(): invalid object type '" + component.getClass().getName()
                            + "', 'Sirius.navigator.EmbeddedToolBar' expected"); // NOI18N
            }
        }

        @Override
        protected void doSetVisible(final EmbeddedComponent component, final boolean visible) {
            if (logger.isDebugEnabled()) {
                logger.debug("FloatingFrameToolBar:doSetVisible()"); // NOI18N
            }
            if (component.isVisible() != visible) {
                super.doSetVisible(component, visible);

                if (visible) {
                    this.doAdd(component);
                } else {
                    this.doRemove(component);
                }
            } else {
                this.logger.warn("unexpected call to 'setVisible()': '" + visible + "'"); // NOI18N
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class FloatingPanel extends JPanel implements ComponentListener {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new FloatingPanel object.
         */
        public FloatingPanel() {
            super(new GridLayout(1, 1));
            this.add(FloatingFrame.this);
            this.addComponentListener(this);
        }

        //~ Methods ------------------------------------------------------------

        /**
         * Invoked when the component has been made invisible.
         *
         * @param  e  DOCUMENT ME!
         */
        @Override
        public void componentHidden(final ComponentEvent e) {
            // logger.debug("dispatching event: " + e);
            FloatingFrame.this.dispatchEvent(e);
        }

        /**
         * Invoked when the component's position changes.
         *
         * @param  e  DOCUMENT ME!
         */
        @Override
        public void componentMoved(final ComponentEvent e) {
            // logger.debug("ignoring event: " + e);
        }

        /**
         * Invoked when the component's size changes.
         *
         * @param  e  DOCUMENT ME!
         */
        @Override
        public void componentResized(final ComponentEvent e) {
            // logger.debug("ignoring event: " + e);
        }

        /**
         * Invoked when the component has been made visible.
         *
         * @param  e  DOCUMENT ME!
         */
        @Override
        public void componentShown(final ComponentEvent e) {
            // logger.debug("dispatching event: " + e);
            FloatingFrame.this.dispatchEvent(e);
        }
    }

    // UI CLASSES
    // #############################################################################

    /**
     * FloatingFrameUI ist eine L&F Implementation fuer den FloatingFrame.
     *
     * @author   Pascal Dihe
     * @version  $Revision$, $Date$
     */
    class FloatingFrameUI extends BasicToolBarUI {

        //~ Methods ------------------------------------------------------------

        /**
         * Diese Funktion wurde ueberschrieben um bestimmte Eigenschaften des FloatingFrames zu aendern z.B. Groesse,
         * Titel, etc.
         *
         * @param   toolBar  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        @Override
        protected JFrame createFloatingFrame(final JToolBar toolBar) {
            // if(logger.isDebugEnabled())logger.debug"<FF> (BasicToolBarUI) createFloatingFrame: " +
            // toolBar.getClass().getName());

            final WindowListener wl = createFrameListener();
            final JFrame frame = new JFrame(toolBar.getName());
            final FloatingFrame ff;

            if (toolBar instanceof FloatingFrame) {
                ff = (FloatingFrame)toolBar;
                frame.setTitle(ff.getName());
                frame.setSize(ff.getSize());
                frame.setResizable(ff.isFrameResizable());
                frame.setIconImage(ff.getConfigurator().getIcon().getImage());
            } else {
                frame.setTitle(toolBar.getName());
                frame.setResizable(false);
            }

            frame.setResizable(true);

            frame.addWindowListener(wl);
            return frame;
        }

        /**
         * Diese Funktion wurde ueberschrieben um das DragWindow (wird beim Herauszeihen des Frame angezeigt) korrekt
         * darzustellen. Das DragWindow hat nun die gleiche Groesse wie der FloatingFrame.
         *
         * @param  position  DOCUMENT ME!
         * @param  origin    DOCUMENT ME!
         */
        @Override
        protected void dragTo(final Point position, final Point origin) {
            super.dragTo(position, origin);
            if ((toolBar instanceof FloatingFrame) && (dragWindow != null)) {
                dragWindow.setSize(toolBar.getSize());
            }
        }

        @Override
        protected RootPaneContainer createFloatingWindow(final JToolBar toolbar) {
            if (logger.isDebugEnabled()) {
                logger.debug("<FF> () createFloatingWindow(): " + toolBar.getClass().getName()); // NOI18N
            }

            final FloatingFrame ff;

            /**
             * DOCUMENT ME!
             *
             * @version  $Revision$, $Date$
             */
            class FloatingDialog extends JFrame // JDialog
            {

                /**
                 * Creates a new FloatingDialog object.
                 */
                public FloatingDialog() {
                    super();
                }

                /*public FloatingDialog(Frame owner, String title, boolean modal)
                 * { super(owner, title, modal); } public FloatingDialog(Dialog owner, String title, boolean modal) {
                 * super(owner, title, modal);}*/

                // Override createRootPane() to automatically resize
                // the frame when contents change
                @Override
                protected JRootPane createRootPane() {
                    final JRootPane rootPane = new JRootPane() {

                            private boolean packing = false;

                            @Override
                            public void validate() {
                                super.validate();
                                if (!packing) {
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

            // JDialog floatingDialog;

            final JFrame floatingDialog;

            /*Window window = SwingUtilities.getWindowAncestor(toolbar);
             * if (window instanceof Frame) { floatingDialog = new FloatingDialog((Frame)window, toolbar.getName(),
             * false); } else if (window instanceof Dialog) { floatingDialog = new FloatingDialog((Dialog)window,
             * toolbar.getName(), false); } else { floatingDialog = new FloatingDialog((Frame)null, toolbar.getName(),
             * false);}*/

            floatingDialog = new FloatingDialog();

            if (toolBar instanceof FloatingFrame) {
                ff = (FloatingFrame)toolbar;
                floatingDialog.setTitle(ff.getName());
                floatingDialog.setSize(ff.getSize());
                floatingDialog.setResizable(ff.isFrameResizable());
            } else {
                floatingDialog.setTitle(toolBar.getName());
                floatingDialog.setResizable(false);
            }

            final WindowListener wl = createFrameListener();
            floatingDialog.addWindowListener(wl);

            return floatingDialog;
        }
    }

    /**
     * MetalFloatingFrameUI ist eine L&F Implementation fuer den FloatingFrame.
     *
     * @version  $Revision$, $Date$
     */
    class MetalFloatingFrameUI extends MetalToolBarUI {

        //~ Methods ------------------------------------------------------------

        /**
         * Diese Funktion wurde ueberschrieben um bestimmte Eigenschaften des FloatingFrames zu aendern z.B. Groesse,
         * Titel, etc.
         *
         * @param   toolBar  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        @Override
        protected JFrame createFloatingFrame(final JToolBar toolBar) {
            if (logger.isDebugEnabled()) {
                logger.debug("<FF> () createFloatingFrame: " + toolBar.getClass().getName()); // NOI18N
            }

            final WindowListener wl = createFrameListener();
            final JFrame frame = new JFrame(toolBar.getName());
            final FloatingFrame ff;

            if (toolBar instanceof FloatingFrame) {
                ff = (FloatingFrame)toolBar;
                frame.setTitle(ff.getName());
                frame.setSize(ff.getSize());
                frame.setResizable(ff.isFrameResizable());
                frame.setIconImage(ff.getConfigurator().getIcon().getImage());
            } else {
                frame.setTitle(toolBar.getName());
                frame.setResizable(false);
            }

            frame.addWindowListener(wl);
            return frame;
        }

        /**
         * Diese Funktion wurde ueberschrieben um das DragWindow (wird beim Herauszeihen des Frame angezeigt) korrekt
         * darzustellen. Das DragWindow hat nun die gleiche Groesse wie der FloatingFrame.
         *
         * @param  position  DOCUMENT ME!
         * @param  origin    DOCUMENT ME!
         */
        @Override
        protected void dragTo(final Point position, final Point origin) {
            super.dragTo(position, origin);
            if ((toolBar instanceof FloatingFrame) && (dragWindow != null)) {
                dragWindow.setSize(toolBar.getSize());
            }
        }

        @Override
        protected MouseInputListener createDockingListener() {
            return new FloatingFrameDockingListener(toolBar);
        }

        @Override
        protected RootPaneContainer createFloatingWindow(final JToolBar toolbar) {
            if (logger.isDebugEnabled()) {
                logger.debug("<FF> () createFloatingWindow(): " + toolBar.getClass().getName()); // NOI18N
            }

            final FloatingFrame ff;

            /**
             * DOCUMENT ME!
             *
             * @version  $Revision$, $Date$
             */
            class FloatingDialog extends JFrame // JDialog
            {

                /**
                 * Creates a new FloatingDialog object.
                 */
                public FloatingDialog() {
                    super();
                }

                /*public FloatingDialog(Frame owner, String title, boolean modal)
                 * { super(owner, title, modal); } public FloatingDialog(Dialog owner, String title, boolean modal) {
                 * super(owner, title, modal);}*/

                /**
                 * Override createRootPane() to automatically resize the frame when contents change.
                 *
                 * @return  DOCUMENT ME!
                 */
                @Override
                protected JRootPane createRootPane() {
                    final JRootPane rootPane = new JRootPane() {

                            private boolean packing = false;

                            @Override
                            public void validate() {
                                super.validate();
                                if (!packing) {
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

            // JDialog floatingDialog;

            final JFrame floatingDialog;

            /*Window window = SwingUtilities.getWindowAncestor(toolbar);
             * if (window instanceof Frame) { floatingDialog = new FloatingDialog((Frame)window, toolbar.getName(),
             * false); } else if (window instanceof Dialog) { floatingDialog = new FloatingDialog((Dialog)window,
             * toolbar.getName(), false); } else { floatingDialog = new FloatingDialog((Frame)null, toolbar.getName(),
             * false);}*/

            floatingDialog = new FloatingDialog();

            if (toolBar instanceof FloatingFrame) {
                ff = (FloatingFrame)toolbar;
                floatingDialog.setTitle(ff.getName());
                floatingDialog.setSize(ff.getSize());
                floatingDialog.setResizable(ff.isFrameResizable());
            } else {
                floatingDialog.setTitle(toolBar.getName());
                floatingDialog.setResizable(false);
            }

            final WindowListener wl = createFrameListener();
            floatingDialog.addWindowListener(wl);

            return floatingDialog;
        }

        //~ Inner Classes ------------------------------------------------------

        /**
         * Ein neuer DockingListener fuer den FloatingFrame.
         *
         * @version  $Revision$, $Date$
         */
        protected class FloatingFrameDockingListener extends DockingListener {

            //~ Instance fields ------------------------------------------------

            private boolean pressedInBumps = false;

            //~ Constructors ---------------------------------------------------

            /**
             * Creates a new FloatingFrameDockingListener object.
             *
             * @param  t  DOCUMENT ME!
             */
            public FloatingFrameDockingListener(final JToolBar t) {
                super(t);
            }

            //~ Methods --------------------------------------------------------

            @Override
            public void mousePressed(final MouseEvent e) {
                super.mousePressed(e);

                if (!toolBar.isEnabled()) {
                    return;
                }

                pressedInBumps = false;

                // Zeichnet ein unsichtbares Rechteck ueber den Anfasser
                // auf der linken Seite des FloatingFrames. Nur wenn sich der
                // MousePointer innerhalb dieses Rechtecks befindet, werden die
                // <b>mouseDragged</b> Events verarbeitet.
                final Rectangle bumpRect = new Rectangle();
                bumpRect.setBounds(0, 0, 14, toolBar.getSize().height);

                if (bumpRect.contains(e.getPoint())) {
                    pressedInBumps = true;
                    final Point dragOffset = e.getPoint();
                    setDragOffset(dragOffset);
                }
            }

            @Override
            public void mouseDragged(final MouseEvent e) {
                if (pressedInBumps) {
                    super.mouseDragged(e);
                }
            }
        }
    }

    /*public static void main(String args[])
     * { org.apache.log4j.BasicConfigurator.configure();  JFrame jf = new JFrame("FloatingFrameTest");
     * jf.setSize(640,480); jf.setLocationRelativeTo(null); jf.setContentPane(new JPanel(new BorderLayout()));
     * jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  FloatingFrameConfigurator ffc = new
     * FloatingFrameConfigurator(); ffc.setName("FloatingFrame");  JPanel content = new JPanel(new GridLayout(1,1));
     * content.add(new JButton("FLOATING FRAME"));  JButton jb = new JButton("Button"); JToolBar jtb = new
     * JToolBar("ToolBar"); jtb.add(jb); jf.getContentPane().add(jtb, BorderLayout.SOUTH);  ArrayList buttons = new
     * ArrayList(); buttons.add(jb); ffc.setButtons(buttons); ffc.setSwapToolBar(true);  JMenu jm = new JMenu("Menu");
     * JMenuBar jmb= new JMenuBar(); jmb.add(jm); jf.setJMenuBar(jmb);  ArrayList menues = new ArrayList();
     * menues.add(jm); ffc.setMenues(menues); ffc.setSwapMenuBar(true);  FloatingFrame ff = new FloatingFrame(content,
     * ffc); JPanel jp = new JPanel(new GridLayout(1,1)); jp.add(ff); jf.getContentPane().add(jp, BorderLayout.CENTER);
     * jf.setVisible(true);}*/
}
