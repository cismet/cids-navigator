package Sirius.navigator.ui;

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
 * Filename		:
 * Version			:	2.0
 * Purpose			:
 * Created			:	28.04.2000
 * History			:	02.08.2000 added support for dynamic Buttons
 *
 *******************************************************************************/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import org.apache.log4j.Logger;

//import ISClient.ims.client.ISFloatingFrame;

//import Sirius.navigator.Views.*;
//import Sirius.navigator.Views.Tree.*;
//import Sirius.navigator.Dialog.*;
//import Sirius.navigator.Dialog.Search.*;
//import Sirius.navigator.connection.ConnectionHandler;
//import Sirius.navigator.SIMS.*;
import Sirius.navigator.ui.embedded.*;
import Sirius.navigator.resource.*;
import Sirius.navigator.method.*;
import Sirius.navigator.exception.*;
import com.jgoodies.looks.HeaderStyle;
import com.jgoodies.looks.Options;
import java.util.ResourceBundle;


/**
 * Eine Toolbar, zu der zur Laufzeit automatisch neue Buttons hinzugefuegt- und
 * entfernt werden koennen.
 */
public class MutableToolBar extends JToolBar //implements ActionListener
{
    private final static Logger logger = Logger.getLogger(MutableToolBar.class);
    private static final ResourceBundle I18N = ResourceBundle.getBundle("Sirius/navigator/resource/i18n/resources");
    
    private final ResourceManager resources;
    
    private final JToolBar defaultToolBar;
    private final MoveableToolBarsMap moveableToolBars;
    private final PluginToolBarsMap pluginToolBars;
    
    private final boolean advancedLayout;
     
    //protected ControlModel model;
    //protected MethodManager methodManager;
    //protected GenericMetaTree activeTree;
    
    // statische Buttons
    //protected JButton exitButton;
   // protected JButton searchButton;
    //protected JButton browseBackButton;
    //protected JButton browseForwardButton;
    //protected JButton toNavigatorButton;
    //protected JButton toSIMSButton;
    
    // dynamische Buttons
    //protected JButton[] dynamicButtons = null;
    //protected boolean hasDynamicButtons = false;
    
    // zum enablen und disablen der browse-Buttons
    //protected boolean browseBack = false;
    //protected boolean browseForward = false;
    
    // Navigator -> Karte
    //protected SICADObject tmpSICADObject = null;
    //protected SICADObject[] SICADObjects = null;
    
    
    /*public MutableToolBar(ControlModel model)
    {
        this.model = model;
        methodManager = new MethodManager(model);
        init();
    }*/
    
    public MutableToolBar()
    {
        this(false);
    }
    
    public MutableToolBar(boolean advancedLayout)
    {
        super(HORIZONTAL);
        
        this.advancedLayout = advancedLayout;
        
        resources = ResourceManager.getManager();
        
        moveableToolBars = new MoveableToolBarsMap();
        pluginToolBars = new PluginToolBarsMap();
        
        this.defaultToolBar = new JToolBar(
                I18N.getString("Sirius.navigator.ui.MutableToolBar.defaultToolBar.name"),
                HORIZONTAL);
        this.defaultToolBar.setFloatable(false);
        this.defaultToolBar.setRollover(advancedLayout);
        defaultToolBar.putClientProperty("JToolBar.isRollover", Boolean.TRUE);
        defaultToolBar.putClientProperty(Options.HEADER_STYLE_KEY, HeaderStyle.BOTH);
        defaultToolBar.putClientProperty(Options.HEADER_STYLE_KEY, HeaderStyle.BOTH);
        this.createDefaultButtons();
        this.add(defaultToolBar);
        putClientProperty("JToolBar.isRollover", Boolean.TRUE);
        putClientProperty(Options.HEADER_STYLE_KEY, HeaderStyle.BOTH);
        putClientProperty(Options.HEADER_STYLE_KEY, HeaderStyle.BOTH);
        this.setFloatable(false);
        //this.setFloatable(advancedLayout);
        
        /*if(advancedLayout)
        {
            // does not work
            //this.addPropertyChangeListener("orientation", new OrientationListener());
        }*/
        
        if(advancedLayout)
        {
            //this.setBorder(new javax.swing.border.EmptyBorder(1,1,1,1));
            this.setBorder(null);
        }
 
    }
    
 
    //-------------------------------------------------------------------------*/
    
    private void createDefaultButtons() 
    {
        logger.debug("creating default buttons");
        ResourceManager resources = ResourceManager.getManager();
        ActionListener toolBarListener = new ToolBarListener();
        JButton button = null;
        
        /*button = new JButton(resources.getButtonIcon("toolbar.exit"));
        button.setToolTipText(resources.getButtonTooltip("toolbar.exit"));
        button.setActionCommand("exit");
        button.setMargin(new Insets(0,0,0,0));
        button.addActionListener(toolBarListener);
        defaultToolBar.add(button);*/
        
        button = new JButton(resources.getButtonIcon("toolbar.search"));
        button.setToolTipText(I18N.getString("Sirius.navigator.ui.MutableToolBar.createDefaultButtons().defaultButton1.tooltip"));
        button.setActionCommand("search");
        //button.setMargin(new Insets(0,0,0,0));
        button.setMargin(new Insets(4,4,4,4));
        button.addActionListener(toolBarListener);
        defaultToolBar.add(button);
        
        /*button = new JButton(resources.getButtonIcon("toolbar.info"));
        button.setToolTipText(resources.getButtonTooltip("toolbar.info"));
        button.setActionCommand("info");
        button.setMargin(new Insets(0,0,0,0));
        button.addActionListener(toolBarListener);
        defaultToolBar.add(button);*/
        
        button = new JButton(resources.getButtonIcon("toolbar.plugin"));
        button.setToolTipText(I18N.getString("Sirius.navigator.ui.MutableToolBar.createDefaultButtons().defaultButton2.tooltip"));
        button.setActionCommand("plugin");
        button.setEnabled(false);//HELL
        //button.setMargin(new Insets(0,0,0,0));
        button.setMargin(new Insets(4,4,4,4));
        button.addActionListener(toolBarListener);
        defaultToolBar.add(button);
        
        
        /*JButton browseBackButton = new JButton(resources.getIcon("btn_back.gif"));
        browseBackButton.setToolTipText(resources.getButtonTooltip("back"));
        //browseBackButton.setMargin(new Insets(0,0,0,0));
        browseBackButton.setActionCommand("back");
        browseBackButton.addActionListener(toolBarListener);
        
        JButton browseForwardButton = new JButton(resources.getIcon("btn_forward.gif"));
        browseForwardButton.setToolTipText(resources.getButtonTooltip("forward"));
        //browseForwardButton.setMargin(new Insets(0,0,0,0));
        browseForwardButton.setActionCommand("forward");
        browseForwardButton.addActionListener(toolBarListener);*/
        
        //JButton toSIMSButton = new JButton(resources.getIcon("btn_toSIMS.gif"));
        //toSIMSButton.setToolTipText(resources.getString("deprecated"));
        //toSIMSButton.setMargin(new Insets(0,0,0,0));
        //toSIMSButton.setActionCommand("toSIMS");
        //toSIMSButton.addActionListener(toolBarListener);
        
        //JButton toNavigatorButton = new JButton(resources.getIcon("btn_toNavigator.gif"));
        //toNavigatorButton.setToolTipText(resources.getString("deprecated"));
        //toNavigatorButton.setMargin(new Insets(0,0,0,0));
        //toNavigatorButton.setActionCommand("toNavigator");
        //toNavigatorButton.addActionListener(toolBarListener);
        
        //defaultToolBar.add(exitButton);
        //defaultToolBar.addSeparator();
        //defaultToolBar.add(searchButton);
        //defaultToolBar.addSeparator();
        /*defaultToolBar.add(browseBackButton);
        defaultToolBar.addSeparator();
        defaultToolBar.add(browseForwardButton);
        defaultToolBar.addSeparator();*/
        //defaultToolBar.add(toSIMSButton);
        //defaultToolBar.addSeparator();
        //defaultToolBar.add(toNavigatorButton);
        //defaultToolBar.addSeparator();
        //defaultToolBar.add(new JSeparator(SwingConstants.VERTICAL));
    }
        
    /*public void setBrowseButtonsEnabled(boolean enabled)
    {
        browseBackButton.setEnabled(enabled & browseBack);
        browseForwardButton.setEnabled(enabled & browseForward);
    }
    
    public void setCanBrowseBack(boolean browse)
    {
        browseBack = browse;
    }
    
    public void setCanBrowseForward(boolean browse)
    {
        browseForward = browse;
    }*/
    
    /**
     * Um festzustellen, welcher Tree gerade aktiv ist.
     */
    /*protected GenericMetaTree getActiveTree()
    {
        if(model.metaTree != null && (model.metaTree.hasFocus() || model.metaTree.isShowing()))
            return model.metaTree;
        else if(model.searchTree != null && (model.searchTree.hasFocus() || model.searchTree.isShowing()))
            return model.searchTree;
        else
            return null;
    }*/
    
    
    
    // MOVEABLED TOOLBARS ------------------------------------------------------
    
    public void addMoveableToolBar(EmbeddedToolBar toolBar)
    {
        toolBar.setRollover(this.advancedLayout);
        toolBar.putClientProperty("JToolBar.isRollover", Boolean.TRUE);
        toolBar.putClientProperty(Options.HEADER_STYLE_KEY, HeaderStyle.BOTH);
        toolBar.putClientProperty(Options.HEADER_STYLE_KEY, HeaderStyle.BOTH);
        this.moveableToolBars.add(toolBar);
    }
    
    public void removeMoveableToolBar(String id)
    {
        this.moveableToolBars.remove(id);
    }
    
    public void setMoveableToolBarVisible(String id, boolean visible)
    {
        this.moveableToolBars.setVisible(id, visible);
    }
    
    public boolean isMoveableToolBarVisible(String id)
    {
        return  this.moveableToolBars.isVisible(id);
    }
    
    public void setMoveableToolBarEnabled(String id, boolean enabled)
    {
        this.moveableToolBars.setEnabled(id, enabled);
    }
    
    public boolean isMoveableToolBarEnabled(String id)
    {
        return  this.moveableToolBars.isEnabled(id);
    }
    
    public boolean isMoveableToolBarAvailable(String id)
    {
        return  this.moveableToolBars.isAvailable(id);
    }
    
    // PLUGIN TOOLBARS ---------------------------------------------------------
    
    public void addPluginToolBar(EmbeddedToolBar toolBar)
    {
        toolBar.setRollover(this.advancedLayout);
        toolBar.putClientProperty("JToolBar.isRollover", Boolean.TRUE);
        toolBar.putClientProperty(Options.HEADER_STYLE_KEY, HeaderStyle.BOTH);
        toolBar.putClientProperty(Options.HEADER_STYLE_KEY, HeaderStyle.BOTH);
        this.pluginToolBars.add(toolBar);
    }
    
    public void removePluginToolBar(String id)
    {
        this.pluginToolBars.remove(id);
    }

    public void setPluginToolBarEnabled(String id, boolean enabled)
    {
        this.pluginToolBars.setEnabled(id, enabled);
    }
    
    public boolean isPluginToolBarEnabled(String id)
    {
        return  this.pluginToolBars.isEnabled(id);
    }
    
    public boolean isPluginToolBarAvailable(String id)
    {
        return  this.pluginToolBars.isAvailable(id);
    }


    /**
     * Verarbeitet die ActionEvents der Buttons
     */
    //public void actionPerformed(ActionEvent e)
    //{
        /*activeTree = getActiveTree();
        
        if (e.getActionCommand().equals("exit"))
        {
            String message = StringLoader.getString("STL@shouldClose");
            JOptionPane optionPane = new JOptionPane(message, JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION, null, StringLoader.getStringArray("STL@yesNoOptionARRAY"), null);
            //_TA_JOptionPane optionPane = new JOptionPane(message, JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION, null, new String[]{"Ja", "Nein"}, null);
            JDialog dialog = optionPane.createDialog(model.navigator, StringLoader.getString("STL@exitProgram"));
            dialog.show();*/
            
            //Integer result = (Integer)optionPane.getValue();
            //NavigatorLogger.printMessage("REsult:" + result);
            //int result = JOptionPane.showConfirmDialog(this, message, "Programm beenden", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            
            //_TA_if(optionPane.getValue().equals("Ja"))
           /* if(optionPane.getValue().equals(StringLoader.getString("STL@yes")))
            {
                if(NavigatorLogger.VERBOSE)NavigatorLogger.printMessage("<NAV> Navigator closed()");
                model.navigator.dispose();
                System.exit(0);
            }*/
            
                        /*
                        String message = "<html><p>Moechten Sie den</p><p>Navigator wirklich schliessen?</p></html>";
                        int result = JOptionPane.showConfirmDialog(model.navigator, message, "Programm beenden", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                         
                        if(result == JOptionPane.YES_OPTION)
                        {
                        if(NavigatorLogger.VERBOSE)NavigatorLogger.printMessage("<NAV> Navigator closed()");
                        System.exit(0);
                        }
                         */
        /*}
        else if (e.getActionCommand().equals("search"))
        {
            try
            {
                //NavigatorLogger.printMessage(activeTree);
                //methodManager.callSearch(activeTree, false);
                methodManager.callSearch(activeTree, true);
            }
            catch (Throwable t)
            {
                if(NavigatorLogger.DEV)
                {
                    NavigatorLogger.printMessage("Fehler bei der Verarbeitung der Suchmethode");
                    t.printStackTrace();
                }
                
                ErrorDialog errorDialog = new ErrorDialog(StringLoader.getString("STL@searchError"), t.toString(), ErrorDialog.WARNING);
                errorDialog.setLocationRelativeTo(model.navigator);
                errorDialog.show();
            }
        }
        else if (e.getActionCommand().equals("browseBack"))
            model.searchTree.browseBack();
        else if (e.getActionCommand().equals("browseForward"))
            model.searchTree.browseForward();
        else if (e.getActionCommand().equals("toNavigator"))
        {
            try
            {
                methodManager.callFromSIMS(activeTree);
            }
            catch (Throwable t)
            {
                if(NavigatorLogger.DEV)
                {
                    NavigatorLogger.printMessage("Fehler bei der Verarbeitung der Abfrage Karte->Navigator");
                    t.printStackTrace();
                }
                
                ErrorDialog errorDialog = new ErrorDialog(StringLoader.getString("STL@transferErrorMapToNav"), t.toString(), ErrorDialog.WARNING);
                errorDialog.setLocationRelativeTo(model.navigator);
                errorDialog.show();
            }
        }
        else if (e.getActionCommand().equals("toSIMS"))
        {
            try
            {
                methodManager.callToSIMS(activeTree);
            }
            catch (Throwable t)
            {
                if(NavigatorLogger.DEV)
                {
                    NavigatorLogger.printMessage("Fehler bei der Verarbeitung der Abfrage Navigator->Karte");
                    t.printStackTrace();
                }
                
                ErrorDialog errorDialog = new ErrorDialog(StringLoader.getString("STL@transferErrorNavToMap"), t.toString(), ErrorDialog.WARNING);
                errorDialog.setLocationRelativeTo(model.navigator);
                errorDialog.show();
            }
        }*/
    //}
    
    
    private class ToolBarListener implements ActionListener
    {
        
        /** Invoked when an action occurs.
         *
         */
        public void actionPerformed(ActionEvent e)
        {
            if(e.getActionCommand().equals("exit"))
            {
                if(ExceptionManager.getManager().showExitDialog(ComponentRegistry.getRegistry().getMainWindow()))
                {
                    logger.info("closing program");
                    ComponentRegistry.getRegistry().getNavigator().dispose();
                    System.exit(0);
                }
            }
            else if(e.getActionCommand().equals("search"))
            {
                try
                {
                    MethodManager.getManager().showSearchDialog();
                }
                catch (Throwable t)
                {
                    logger.fatal("Fehler bei der Verarbeitung der Suchmethode", t);
                    
                    //ErrorDialog errorDialog = new ErrorDialog("Fehler bei der Verarbeitung der Suchmethode", t.toString(), ErrorDialog.WARNING);
                    //errorDialog.setLocationRelativeTo(ComponentRegistry.getRegistry().getMainWindow());
                    //errorDialog.show();
                }
            }
            else if(e.getActionCommand().equals("plugin"))
            {
                MethodManager.getManager().showPluginManager();
            }
            else if(e.getActionCommand().equals("info"))
            {
                MethodManager.getManager().showAboutDialog();
            }
        }
    }
    
    // does not work:
    /*private class OrientationListener implements PropertyChangeListener
    {
        public void propertyChange(PropertyChangeEvent evt)
        {
            logger.debug("changing toolbar orientation");
            MutableToolBar.this.defaultToolBar.setOrientation(MutableToolBar.this.getOrientation());
            
            Iterator iterator = MutableToolBar.this.pluginToolBars.getEmbeddedComponents();
            while(iterator.hasNext())
            {
                //logger.fatal(iterator.next().getClass().getName());
                ((JToolBar)iterator.next()).setOrientation(MutableToolBar.this.getOrientation());
            }
            
            iterator = MutableToolBar.this.moveableToolBars.getEmbeddedComponents();
            while(iterator.hasNext())
            {
                //logger.fatal(iterator.next());
                ((JToolBar)iterator.next()).setOrientation(MutableToolBar.this.getOrientation());
            }
        }
    }
    */
    
    
    // EmbeddedComponentsMap implementations -----------------------------------
    
    private class PluginToolBarsMap extends AbstractEmbeddedComponentsMap
    {
        private PluginToolBarsMap()
        {
            Logger.getLogger(PluginToolBarsMap.class);
        }
        
        protected void doAdd(EmbeddedComponent component)
        {
            logger.debug("adding toolbar: '" + component + "'");
            if(component instanceof EmbeddedToolBar)
            {                
                MutableToolBar.this.add((EmbeddedToolBar)component);                
            }
            else
            {
                this.logger.error("doAdd(): invalid object type '" + component.getClass().getName() + "', 'Sirius.navigator.EmbeddedToolBar' expected");
            }
            
            //MutableToolBar.this.validateTree();
            MutableToolBar.this.invalidate();
            SwingUtilities.invokeLater(new Runnable(){
                public void run()
                {
                    MutableToolBar.this.validateTree();
                    MutableToolBar.this.repaint();
                }
            });
        }
        
        protected void doRemove(EmbeddedComponent component)
        {
            if(component instanceof EmbeddedToolBar)
            {
                MutableToolBar.this.remove((EmbeddedToolBar)component);
            }
            else
            {
                this.logger.error("doRemove(): invalid object type '" + component.getClass().getName() + "', 'Sirius.navigator.EmbeddedToolBar' expected");
            }
            
            MutableToolBar.this.invalidate();
            SwingUtilities.invokeLater(new Runnable(){
                public void run()
                {
                    MutableToolBar.this.validateTree();
                    MutableToolBar.this.repaint();
                }
            });
        }
    }
    
    private class MoveableToolBarsMap extends PluginToolBarsMap
    {
        private MoveableToolBarsMap()
        {
            Logger.getLogger(MoveableToolBarsMap.class);
        }
        
        protected void doSetVisible(EmbeddedComponent component, boolean visible)
        {
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
    
    
}
