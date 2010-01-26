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
 * Created			:	27.04.2000
 * History			:	02.08.2000 added support for dynamic Menus
 *
 *******************************************************************************/
import Sirius.navigator.connection.SessionManager;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import java.awt.event.*;
import java.awt.Frame;
import org.apache.log4j.Logger;
//import Sirius.navigator.ui.embedded.*;
//import Sirius.navigator.*;
//import Sirius.navigator.Controls.*;
//import Sirius.navigator.Views.*;
//import Sirius.navigator.Views.Tree.*;
//import Sirius.navigator.Dialog.*;
//import Sirius.navigator.Dialog.Search.*;
//import Sirius.navigator.connection.ConnectionHandler;
//import Sirius.navigator.tools.ObjectManager;
//import Sirius.navigator.PlugIn.*;
//import Sirius.navigator.types.*;
import Sirius.navigator.method.*;
import Sirius.navigator.ui.embedded.*;
import Sirius.navigator.ui.dialog.*;
import Sirius.navigator.exception.*;
import Sirius.navigator.resource.*;
import Sirius.navigator.types.treenode.RootTreeNode;
import de.cismet.tools.gui.StaticSwingTools;
import javax.swing.tree.TreePath;

public class MutableMenuBar extends JMenuBar {

    ResourceManager resources = ResourceManager.getManager();
    private final static Logger logger = Logger.getLogger(MutableMenuBar.class);
    private final PluginMenuesMap pluginMenues;
    private final EmbeddedContainersMap moveableMenues;
    private Sirius.navigator.plugin.interfaces.LayoutManager layoutManager;
    private JMenu viewMenu = null;
    // Control Stuff
    //private ControlModel model;
    //private MethodManager methodManager;
    //private GenericMetaTree activeTree;
    // Default Menues
    //private JMenu navigatorMenu;
    //private JMenu functionsMenu;
    private JMenu pluginMenu;
    //private JMenu helpMenu;

    /*public MutableMenuBar(ControlModel model)
    {
    super();

    this.model = model;
    methodManager = new MethodManager(model);
    this.makeDefaultMenues();
    }*/
    public MutableMenuBar() {
        super();

        pluginMenues = new PluginMenuesMap();
        moveableMenues = new EmbeddedContainersMap();

        this.makeDefaultMenues();
    }

    // MOVEABLE MENUES ---------------------------------------------------------
    /**
     * Adds new moveable menues to this menu bar
     */
    public void addMoveableMenues(String id, Collection menues) {
        this.moveableMenues.add(new MoveableMenues(id, menues));
    }

    public void removeMoveableMenues(String id) {
        this.moveableMenues.remove(id);
    }

    public void setMoveableMenuesVisible(String id, boolean visible) {
        this.moveableMenues.setVisible(id, visible);
    }

    public boolean isMoveableMenuesVisible(String id) {
        return this.moveableMenues.isVisible(id);
    }

    public void setMoveableMenuesEnabled(String id, boolean enabled) {
        this.moveableMenues.setEnabled(id, enabled);
    }

    public boolean isMoveableMenuesEnabled(String id) {
        return this.moveableMenues.isEnabled(id);
    }

    public boolean isMoveableMenuesAvailable(String id) {
        return this.moveableMenues.isAvailable(id);
    }

    // PLUGIN MENUES -----------------------------------------------------------
    public void addPluginMenu(EmbeddedMenu menu) {
        if (menu.getItemCount() > 0) {
            this.pluginMenues.add(menu);
        } else if (logger.isDebugEnabled()) {
            logger.warn("menu '" + menu.getId() + "' does not contain any items, ignoring menu");
        }
    }

    public void removePluginMenu(String id) {
        this.pluginMenues.remove(id);
    }

    public void setPluginMenuEnabled(String id, boolean enabled) {
        this.pluginMenues.setEnabled(id, enabled);
    }

    public boolean isPluginMenuEnabled(String id) {
        return this.pluginMenues.isEnabled(id);
    }

    public boolean isPluginMenuAvailable(String id) {
        return this.pluginMenues.isAvailable(id);
    }

    /**
     * Um festzustellen, welcher Tree gerade aktiv ist.
     */
    /*public GenericMetaTree getActiveTree()
    {
    if(model.metaTree != null && (model.metaTree.hasFocus() || model.metaTree.isShowing()))
    return model.metaTree;
    else if(model.searchTree != null && (model.searchTree.hasFocus() || model.searchTree.isShowing()))
    return model.searchTree;
    else
    return null;
    }*/
    /**
     * Creates the default menues & menu entries
     */
    private void makeDefaultMenues() {
        if (logger.isDebugEnabled()) {
            logger.debug("creating default menues");
        }

        JMenu menu = null;
        JMenuItem item = null;
        MenuItemActionListener itemListener = new MenuItemActionListener();
        

        // NAVIGATOR MENU ======================================================
        menu = new JMenu(resources.getString("Sirius.navigator.ui.MutableMenuBar.navigatorMenu.title"));
        menu.setMnemonic(resources.getString("Sirius.navigator.ui.MutableMenuBar.navigatorMenu.mnemonic").charAt(0));
        this.add(menu);

        //LayoutControls
        //Gegenwärtiges Layout Speichern
        menu.add(item = new JMenuItem(resources.getString("Sirius.navigator.ui.MutableMenuBar.navigatorMenu.saveCurrentLayout.title")));
        item.setIcon(resources.getIcon("Sirius.navigator.ui.MutableMenuBar.navigatorMenu.saveCurrentLayout.icon"));
        item.setAccelerator(resources.getMenuAccelerator("Sirius.navigator.ui.MutableMenuBar.navigatorMenu.saveCurrentLayout.accelerator"));
//        item.setMnemonic(resources.getString("Sirius.navigator.ui.MutableMenuBar.navigatorMenu.saveCurrentLayout.mnemonic").charAt(0));
        item.setActionCommand("navigator.save.current.layout");
        item.addActionListener(itemListener);
        //Layout öffnen
        menu.add(item = new JMenuItem(resources.getString("Sirius.navigator.ui.MutableMenuBar.navigatorMenu.openLayout.title")));
        item.setIcon(resources.getIcon("Sirius.navigator.ui.MutableMenuBar.navigatorMenu.openLayout.icon"));
        item.setAccelerator(resources.getMenuAccelerator("Sirius.navigator.ui.MutableMenuBar.navigatorMenu.openLayout.accelerator"));
//        item.setMnemonic(resources.getString("Sirius.navigator.ui.MutableMenuBar.navigatorMenu.openLayout.mnemonic").charAt(0));
        item.setActionCommand("navigator.open.layout");
        item.addActionListener(itemListener);
        //Layout reseten
        menu.add(item = new JMenuItem(resources.getString("Sirius.navigator.ui.MutableMenuBar.navigatorMenu.resetLayout.title")));
        item.setIcon(resources.getIcon("Sirius.navigator.ui.MutableMenuBar.navigatorMenu.resetLayout.icon"));
        item.setAccelerator(resources.getMenuAccelerator("Sirius.navigator.ui.MutableMenuBar.navigatorMenu.resetLayout.accelerator"));
//        item.setMnemonic(resources.getString("Sirius.navigator.ui.MutableMenuBar.navigatorMenu.resetLayout.mnemonic").charAt(0));
        item.setActionCommand("navigator.reset.layout");
        item.addActionListener(itemListener);
        menu.add(new JSeparator());
        //Hell
        menu.add(item = new JMenuItem(resources.getString("Sirius.navigator.ui.MutableMenuBar.navigatorMenu.reloadCatalogue.title")));
        item.setMnemonic(resources.getString("Sirius.navigator.ui.MutableMenuBar.navigatorMenu.reloadCatalogue.mnemonic").charAt(0));
        //item.setIcon(resources.getIcon("Sirius.navigator.ui.MutableMenuBar.navigatorMenu.reloadCatalogue.icon")); // not set yet
        item.setAccelerator(resources.getMenuAccelerator("Sirius.navigator.ui.MutableMenuBar.navigatorMenu.reloadCatalogue.accelerator"));
        item.setActionCommand("tree.refresh");
        item.addActionListener(itemListener);
        menu.add(new JSeparator());
        // Beenden .............................................................
        menu.add(item = new JMenuItem(resources.getString("Sirius.navigator.ui.MutableMenuBar.navigatorMenu.exit.title")));
        item.setMnemonic(resources.getString("Sirius.navigator.ui.MutableMenuBar.navigatorMenu.exit.mnemonic").charAt(0));
        item.setIcon(resources.getIcon("Sirius.navigator.ui.MutableMenuBar.navigatorMenu.exit.icon"));
        item.setAccelerator(resources.getMenuAccelerator("Sirius.navigator.ui.MutableMenuBar.navigatorMenu.exit.accelerator"));
        item.setActionCommand("navigator.exit");
        item.addActionListener(itemListener);

        // SEARCH MENU =========================================================
        menu = new JMenu(resources.getString("Sirius.navigator.ui.MutableMenuBar.searchMenu.title"));
        menu.setMnemonic(resources.getString("Sirius.navigator.ui.MutableMenuBar.searchMenu.mnemonic").charAt(0));
        this.add(menu);
        // Suche ...............................................................
        menu.add(item = new JMenuItem(resources.getString("Sirius.navigator.ui.MutableMenuBar.searchMenu.search.title")));
        item.setMnemonic(resources.getString("Sirius.navigator.ui.MutableMenuBar.searchMenu.search.mnemonic").charAt(0));
        item.setIcon(resources.getIcon("Sirius.navigator.ui.MutableMenuBar.searchMenu.search.icon"));
        item.setAccelerator(resources.getMenuAccelerator("Sirius.navigator.ui.MutableMenuBar.searchMenu.search.accelerator"));
        item.setActionCommand("search.search");
        item.addActionListener(itemListener);
        menu.addSeparator();
        // show search results .................................................
        menu.add(item = new JMenuItem(resources.getString("Sirius.navigator.ui.MutableMenuBar.searchMenu.showResults.title")));
        item.setMnemonic(resources.getString("Sirius.navigator.ui.MutableMenuBar.searchMenu.showResults.mnemonic").charAt(0));
        item.setIcon(resources.getIcon("Sirius.navigator.ui.MutableMenuBar.searchMenu.showResults.icon"));
        //item.setAccelerator(resources.getMenuAccelerator("Sirius.navigator.ui.MutableMenuBar.searchMenu.showResults.accelerator")); // not set yet
        item.setActionCommand("search.show");
        item.addActionListener(itemListener);
        // search result profiles .....................................................
        menu.add(item = new JMenuItem(resources.getString("Sirius.navigator.ui.MutableMenuBar.searchMenu.searchResultProfiles.title")));
        item.setMnemonic(resources.getString("Sirius.navigator.ui.MutableMenuBar.searchMenu.searchResultProfiles.mnemonic").charAt(0));
        item.setIcon(resources.getIcon("Sirius.navigator.ui.MutableMenuBar.searchMenu.searchResultProfiles.icon"));
        //item.setAccelerator(resources.getMenuAccelerator("Sirius.navigator.ui.MutableMenuBar.searchMenu.searchResultProfiles.accelerator")); // not set yet
        item.setActionCommand("search.profiles.result");
        item.addActionListener(itemListener);
        // search profiles .....................................................
        menu.addSeparator();
        menu.add(item = new JMenuItem(resources.getString("Sirius.navigator.ui.MutableMenuBar.searchMenu.searchProfiles.title")));
        item.setMnemonic(resources.getString("Sirius.navigator.ui.MutableMenuBar.searchMenu.searchProfiles.mnemonic").charAt(0));
        item.setIcon(resources.getIcon("Sirius.navigator.ui.MutableMenuBar.searchMenu.searchProfiles.icon"));
        //item.setAccelerator(resources.getMenuAccelerator("Sirius.navigator.ui.MutableMenuBar.searchMenu.searchProfiles.accelerator")); // not set yet
        item.setActionCommand("search.profiles");
        item.addActionListener(itemListener);

        // TOOLS MENU ==========================================================
        menu = new JMenu(resources.getString("Sirius.navigator.ui.MutableMenuBar.toolsMenu.title"));
        menu.setMnemonic(resources.getString("Sirius.navigator.ui.MutableMenuBar.toolsMenu.mnemonic").charAt(0));
        this.add(menu);
        // password  ...............................................................
        menu.add(item = new JMenuItem(resources.getString("Sirius.navigator.ui.MutableMenuBar.toolsMenu.password.title")));
        item.setMnemonic(resources.getString("Sirius.navigator.ui.MutableMenuBar.toolsMenu.password.mnemonic").charAt(0));
        //item.setIcon(resources.getIcon("Sirius.navigator.ui.MutableMenuBar.toolsMenu.password.icon")); // not set yet
        //item.setAccelerator(resources.getMenuAccelerator("tSirius.navigator.ui.MutableMenuBar.toolsMenu.password.accelerator")); // not set yet
        item.setActionCommand("tools.password");
        item.addActionListener(itemListener);

        //this.add(new JSeparator(SwingConstants.HORIZONTAL));

        // Plugin Menu =========================================================
        menu = new JMenu(resources.getString("Sirius.navigator.ui.MutableMenuBar.pluginMenu.title"));
        menu.setMnemonic(resources.getString("Sirius.navigator.ui.MutableMenuBar.pluginMenu.mnemonic").charAt(0));
        menu.setEnabled(false); //HELL
        this.add(menu);
        this.pluginMenu = menu;
        // plugin manager ......................................................
        menu.add(item = new JMenuItem(resources.getString("Sirius.navigator.ui.MutableMenuBar.pluginMenu.pluginManager.title")));
        item.setMnemonic(resources.getString("Sirius.navigator.ui.MutableMenuBar.pluginMenu.pluginManager.mnemonic").charAt(0));
        item.setIcon(resources.getIcon("Sirius.navigator.ui.MutableMenuBar.pluginMenu.pluginManager.icon"));
        //item.setAccelerator(resources.getMenuAccelerator("Sirius.navigator.ui.MutableMenuBar.pluginMenu.pluginManager.accelerator")); // not set yet
        item.setActionCommand("plugin.manager");
        item.addActionListener(itemListener);
        menu.addSeparator();

        // Fenster Menu ======================================================
        menu = new JMenu(resources.getString("Sirius.navigator.ui.MutableMenuBar.windowMenu.title"));
        menu.setMnemonic(resources.getString("Sirius.navigator.ui.MutableMenuBar.windowMenu.mnemonic").charAt(0));
        this.add(menu);
        viewMenu = menu;


        // Help Menu ===========================================================
        menu = new JMenu(resources.getString("Sirius.navigator.ui.MutableMenuBar.helpMenu.title"));
        menu.setMnemonic(resources.getString("Sirius.navigator.ui.MutableMenuBar.helpMenu.mnemonic").charAt(0));
        this.add(menu);
        // plugin manager ......................................................
        menu.add(item = new JMenuItem(resources.getString("Sirius.navigator.ui.MutableMenuBar.helpMenu.info.title")));
        item.setMnemonic(resources.getString("Sirius.navigator.ui.MutableMenuBar.helpMenu.info.mnemonic").charAt(0));
        item.setIcon(resources.getIcon("Sirius.navigator.ui.MutableMenuBar.helpMenu.info.icon"));
        //item.setAccelerator(resources.getMenuAccelerator("Sirius.navigator.ui.MutableMenuBar.helpMenu.info.accelerator")); // not set yet
        item.setActionCommand("help.info");
        item.addActionListener(itemListener);
    }

    public void addViewMenuItem(JMenuItem viewItem) {
        if (viewMenu != null) {
            viewMenu.add(viewItem);
        }
    }

    // INNERE KLASSEN ZUM BEARBEITEN DER EREIGNISSE ============================
    private class MenuItemActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("search.search")) {
                try {
                    MethodManager.getManager().showSearchDialog();
                } catch (Throwable t) {
                    logger.fatal("Error while processing search method", t);

                    ErrorDialog errorDialog = new ErrorDialog(
                            resources.getString("Sirius.navigator.ui.MutableMenuBar.MenuItemActionListener.actionPerformed().searchMenu.search.ErrorDialog.message"),
                            t.toString(), ErrorDialog.WARNING);
                    errorDialog.setLocationRelativeTo(ComponentRegistry.getRegistry().getMainWindow());
                    errorDialog.show();
                }
            } else if (e.getActionCommand().equals("tools.password")) {
                MethodManager.getManager().showPasswordDialog();
            } /*else if (e.getActionCommand().equals("navigator_logout"))
            {
            if(ExceptionManager.getManager().showExitDialog(ComponentRegistry.getRegistry().getMainWindow()))
            {
            logger.info("closing program");
            System.exit(0);
            }
            }*/ else if (e.getActionCommand().equals("navigator.exit")) {
                if (ExceptionManager.getManager().showExitDialog(ComponentRegistry.getRegistry().getMainWindow())) {
                    logger.info("closing program");
                    ComponentRegistry.getRegistry().getNavigator().dispose();
                    System.exit(0);
                }
            } else if (e.getActionCommand().equals("search.show")) {
                MethodManager.getManager().showSearchResults();
            } else if (e.getActionCommand().equals("search.profiles.result")) {
                MethodManager.getManager().showQueryResultProfileManager();
            } else if (e.getActionCommand().equals("search.profiles")) {
                MethodManager.getManager().showQueryProfilesManager();
            } else if (e.getActionCommand().equals("plugin.manager")) {
                MethodManager.getManager().showPluginManager();
            } else if (e.getActionCommand().equals("help.info")) {
                MethodManager.getManager().showAboutDialog();
            } else if (e.getActionCommand().equals("tree.refresh")) {
                try {
                    final TreePath selectionPath = ComponentRegistry.getRegistry().getCatalogueTree().getSelectionPath();
                    if (selectionPath != null && selectionPath.getPath().length > 0) {
                        RootTreeNode rootTreeNode = new RootTreeNode(SessionManager.getProxy().getRoots());
                        ((DefaultTreeModel) ComponentRegistry.getRegistry().getCatalogueTree().getModel()).setRoot(rootTreeNode);
                        ((DefaultTreeModel) ComponentRegistry.getRegistry().getCatalogueTree().getModel()).reload();
                        ComponentRegistry.getRegistry().getCatalogueTree().exploreSubtree(selectionPath);
                    }
                } catch (ConnectionException ex) {
                    logger.error("Error while refreshing the tree", ex);
                } catch (RuntimeException ex) {
                    logger.error("Error while refreshing the tree", ex);
                }
            } else if (e.getActionCommand().equals("navigator.reset.layout")) {
                logger.debug("reset layout");
                if (layoutManager != null) {
                    layoutManager.resetLayout();
                } else {
                    //TODO Meldung Benutzer
                }
            } else if (e.getActionCommand().equals("navigator.open.layout")) {
                logger.debug("open layout");
                if (layoutManager != null) {
                    layoutManager.loadLayout((java.awt.Component) StaticSwingTools.getParentFrame(MutableMenuBar.this));
                } else {
                    //TODO Meldung Benutzer
                }
            } else if (e.getActionCommand().equals("navigator.save.current.layout")) {
                logger.debug("save layout");
                if (layoutManager != null) {
                    layoutManager.saveCurrentLayout((java.awt.Component) StaticSwingTools.getParentFrame(MutableMenuBar.this));
                } else {
                    //TODO Meldung Benutzer
                }
            }
        }
    }
    /*if (e.getActionCommand().equals("functions_search"))
    {
    try
    {
    activeTree = getActiveTree();

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
    else if (e.getActionCommand().equals("functions_change_password"))
    {
    model.passwordDialog.show();
    }
    else if (e.getActionCommand().equals("navigator_logout"))
    {
    try
    {
    model.navigator.setVisible(false);

    // Das komplette Model musss geloescht werden, da der MetaTree
    // neue RootNodes erhaelt.
    model.metaTree.setModel(null);
    model.searchTree.clear();

    ConnectionHandler.reset();
    model.loginDialog.reset();
    System.gc();
    model.loginDialog.show();
    ConnectionHandler.refillCache();

    model.metaTree.setModel(new DefaultTreeModel(new MetaTreeNode(ConnectionHandler.getTopNodes(), true), true));
    System.gc();
    model.navigator.setVisible(true);
    }
    catch(Exception ex)
    {
    if(NavigatorLogger.DEV)
    {
    NavigatorLogger.printMessage("Fehler waehrend des Anmeldevorgangs: ");
    ex.printStackTrace();
    }

    ErrorDialog errorDialog = new ErrorDialog(StringLoader.getString("STL@loginError"), e.toString(), ErrorDialog.ERROR);
    errorDialog.setLocationRelativeTo(model.navigator);
    errorDialog.show();
    }
    }
    else if (e.getActionCommand().equals("navigator_exit"))
    {
    String message = StringLoader.getString("STL@shouldClose");
    JOptionPane optionPane = new JOptionPane(message, JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION, null, StringLoader.getStringArray("STL@yesNoOptionARRAY"), null);
    JDialog dialog = optionPane.createDialog(model.navigator, StringLoader.getString("STL@exitProgram"));
    dialog.show();

    if(optionPane.getValue().equals(StringLoader.getString("STL@yes")))
    {
    if(NavigatorLogger.VERBOSE)NavigatorLogger.printMessage("<NAV> Navigator closed()");
    model.navigator.dispose();
    System.exit(0);
    }
    }
    else if(e.getActionCommand().equals("functions_search_profiles"))
    {
    model.queryResultProfileManager.show();

    if(model.queryResultProfileManager.newNodesLoaded())
    {
    model.statusBar.setStatusString(model.searchTree.getResultNodes().length + StringLoader.getString("STL@objectsLoaded"), StatusBar.STATUS_2);
    model.searchTree.bringToFront();
    }
    }
    }*/

    /**
     * Local MoveableComponents implementation
     */
    /*
    private class MoveableMenues extends MoveableComponents
    {
    private MoveableMenues(String id, Collection menues)
    {
    super(id, menues);
    }

    protected void doSetComponentsVisible(boolean componentsVisible)
    {
    MoveableMenues.ComponentIterator iterator = this.getComponentIterator();

    if(this.componentsVisible !=  componentsVisible)
    {
    if(componentsVisible)
    {
    MutableMenuBar.this.add(new JSeparator(SwingConstants.VERTICAL));

    while(iterator.hasNext())
    {
    MutableMenuBar.this.add(iterator.next());
    }
    }
    else
    {
    while(iterator.hasNext())
    {
    MutableMenuBar.this.remove(iterator.next());
    }

    Component component = MutableMenuBar.this.getComponent(MutableMenuBar.this.getMenuCount()-1);
    if(component instanceof JSeparator)
    {
    MutableMenuBar.this.remove(component);
    }
    else
    {
    logger.warn("synchronization error: no separator found but component '" + component + "'");
    }

    }
    }
    else
    {
    logger.warn("synchronization error: componentsVisible = '" + componentsVisible + "' but '" + !componentsVisible + "' expected");
    }
    }*/
    //    }
    private class PluginMenuesMap extends AbstractEmbeddedComponentsMap {

        private PluginMenuesMap() {
            Logger.getLogger(PluginMenuesMap.class);
        }

        protected void doAdd(EmbeddedComponent component) {
            if (component instanceof EmbeddedMenu) {
                MutableMenuBar.this.pluginMenu.add((EmbeddedMenu) component);
            } else {
                this.logger.error("doAdd(): invalid object type '" + component.getClass().getName() + "', 'Sirius.navigator.EmbeddedMenu' expected");
            }
        }

        protected void doRemove(EmbeddedComponent component) {
            if (component instanceof EmbeddedMenu) {
                MutableMenuBar.this.pluginMenu.remove((EmbeddedMenu) component);
            } else {
                this.logger.error("doRemove(): invalid object type '" + component.getClass().getName() + "', 'Sirius.navigator.EmbeddedMenu' expected");
            }
        }
    }

    private class MoveableMenues extends EmbeddedContainer {

        public MoveableMenues(String id, Collection components) {
            super(id, components);
            super.setVisible(true);
        }

        public void setVisible(boolean visible) {
            if (this.isVisible() != visible) {
                super.setVisible(visible);

                if (visible) {
                    this.addComponents();
                } else {
                    this.removeComponents();
                }
            } else {
                this.logger.warn("unexpected call to 'setVisible()': '" + visible + "'");
            }
        }

        protected void addComponents() {
            Component component = MutableMenuBar.this.getComponent(MutableMenuBar.this.getComponentCount() - 1);
            if (!(component instanceof MutableMenuSeparator)) {
                MutableMenuBar.this.add(new MutableMenuSeparator());
            }

            ComponentIterator iterator = this.iterator();
            while (iterator.hasNext()) {
                component = iterator.next();
                if (component != null) {
                    if (component instanceof JMenu) {
                        MutableMenuBar.this.add((JMenu) component);
                    } else {
                        this.logger.error("addComponents(): invalid object type '" + component.getClass().getName() + "', 'javax.swing.JMenu' expected");
                    }
                }
            }

            MutableMenuBar.this.invalidate();
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    MutableMenuBar.this.validateTree();
                    MutableMenuBar.this.repaint();
                }
            });
        }

        protected void removeComponents() {
            Component component = null;
            ComponentIterator iterator = this.iterator();

            while (iterator.hasNext()) {
                component = iterator.next();
                if (component != null) {
                    if (component instanceof JMenu) {
                        MutableMenuBar.this.remove(component);
                    } else {
                        this.logger.error("removeComponents(): invalid object type '" + component.getClass().getName() + "', 'javax.swing.JMenu' expected");
                    }
                }
            }

            component = MutableMenuBar.this.getComponent(MutableMenuBar.this.getComponentCount() - 1);
            if (component instanceof MutableMenuSeparator) {
                MutableMenuBar.this.remove(component);
            }

            MutableMenuBar.this.invalidate();
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    MutableMenuBar.this.validateTree();
                    MutableMenuBar.this.repaint();
                }
            });
        }
    }

    private class MutableMenuSeparator extends JSeparator {

        public MutableMenuSeparator() {
            super(SwingConstants.VERTICAL);
            this.setMaximumSize(new Dimension(5, 60));
        }
    }

    public void registerLayoutManager(Sirius.navigator.plugin.interfaces.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }
}
