/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sirius.navigator.ui;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.log4j.Logger;

import org.openide.util.Lookup;
import org.openide.util.NbBundle;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import de.cismet.cids.navigator.utils.CidsClientToolbarItem;

import de.cismet.cids.tools.search.clientstuff.CidsWindowSearch;

import de.cismet.cismap.commons.gui.ToolbarComponentsProvider;

import de.cismet.connectioncontext.ConnectionContext;
import de.cismet.connectioncontext.ConnectionContextStore;

import de.cismet.tools.gui.JPopupMenuButton;
import de.cismet.tools.gui.ToolbarSeparator;
import de.cismet.tools.gui.menu.ApplyIconFromSubAction;
import de.cismet.tools.gui.menu.CidsUiAction;
import de.cismet.tools.gui.menu.CidsUiActionProvider;
import de.cismet.tools.gui.menu.CidsUiComponent;
import de.cismet.tools.gui.menu.CidsUiMenuProvider;
import de.cismet.tools.gui.menu.CidsUiMenuProviderEvent;
import de.cismet.tools.gui.menu.CidsUiMenuProviderListener;
import de.cismet.tools.gui.menu.ConfiguredToolBar;
import de.cismet.tools.gui.menu.Item;
import de.cismet.tools.gui.menu.Menu;
import de.cismet.tools.gui.menu.Toolbar;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class ActionConfiguration {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(ActionConfiguration.class);

    //~ Instance fields --------------------------------------------------------

    private final Map<String, Action> windowSearchWithMenuItemActionMap = new HashMap<String, Action>();
    private final Map<String, Action> toolbarItemActionMap = new HashMap<String, Action>();
    private final Map<String, CidsUiAction> actionMap = new HashMap<String, CidsUiAction>();
    private final Map<String, Component> componentMap = new HashMap<String, Component>();
    private final Map<String, ButtonGroup> buttonGroupMap = new HashMap<String, ButtonGroup>();
    private final Map<String, CidsUiMenuProvider> menuMap = new HashMap<String, CidsUiMenuProvider>();
    private final Menu menu;
    private final ConnectionContext context;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ActionConfiguration object.
     *
     * @param  configurationFile  the path to the configuration
     * @param  additionalActions  DOCUMENT ME!
     * @param  context            DOCUMENT ME!
     */
    public ActionConfiguration(final String configurationFile,
            final Map<String, Action> additionalActions,
            final ConnectionContext context) {
        this.context = context;
        lookup();
        menu = readConfiguration(configurationFile);

        if (additionalActions != null) {
            toolbarItemActionMap.putAll(additionalActions);
        }
    }

    /**
     * Creates a new ActionConfiguration object.
     *
     * @param  configurationFile  the configuration as input stream
     * @param  additionalActions  DOCUMENT ME!
     * @param  context            DOCUMENT ME!
     */
    public ActionConfiguration(final InputStream configurationFile,
            final Map<String, Action> additionalActions,
            final ConnectionContext context) {
        this.context = context;
        lookup();
        menu = readConfiguration(configurationFile);

        if (additionalActions != null) {
            toolbarItemActionMap.putAll(additionalActions);
        }
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public List<ConfiguredToolBar> getToolbars() {
        if (menu != null) {
            final List<ConfiguredToolBar> toolbarList = new ArrayList<ConfiguredToolBar>();

            for (final Toolbar tb : menu.getToolbars()) {
                final ConfiguredToolBar newConfToolbar = new ConfiguredToolBar();
                final JToolBar newToolbar = new JToolBar();
                newConfToolbar.setToolbarDefinition(tb);

                newConfToolbar.setToolbar(newToolbar);
                toolbarList.add(newConfToolbar);
                configureToolbar(newToolbar, tb.getItems());
            }

            return toolbarList;
        }

        return null;
    }

    /**
     * Configures the given menubar.
     *
     * @param  menubar  DOCUMENT ME!
     */
    public void configureMainMenu(final JMenuBar menubar) {
        if (menu == null) {
            return;
        }
        final Item[] menuItems = menu.getMainMenu();

        for (final Item tmp : menuItems) {
            if (tmp.getActionKey() == null) {
                final JMenu m = new JMenu();

                if (tmp.getName() != null) {
                    final Character mnemonic = extractMnemonic(tmp.getName());
                    m.setText(nameWithoutMnemonic(tmp.getName()));

                    if (mnemonic != null) {
                        m.setMnemonic(mnemonic);
                    }
                } else if (tmp.getI18nKey() != null) {
                    final String name = NbBundle.getMessage(ActionConfiguration.class, tmp.getI18nKey());
                    final Character mnemonic = extractMnemonic(tmp.getName());
                    m.setText(nameWithoutMnemonic(name));

                    if (mnemonic != null) {
                        m.setMnemonic(mnemonic);
                    }
                }
                menubar.add(m);
                addMenuItems(tmp.getItems(), m);
            } else {
                final CidsUiMenuProvider provider = menuMap.get(tmp.getActionKey());

                if (provider != null) {
                    menubar.add(provider.getMenu());
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   name  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private Character extractMnemonic(final String name) {
        if (name.contains("&")) {
            return name.charAt(name.indexOf("&") + 1);
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   name  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private String nameWithoutMnemonic(final String name) {
        if (name.contains("&")) {
            return name.replace("&", "");
        } else {
            return name;
        }
    }

    /**
     * Add the given items to the given toolbar.
     *
     * @param  toolbar       the toolbar to configure
     * @param  toolbarItems  the items
     */
    private void configureToolbar(final JToolBar toolbar, final Item[] toolbarItems) {
        for (final Item tmp : toolbarItems) {
            if (tmp.getActionKey() != null) {
                CidsUiAction action = actionMap.get(tmp.getActionKey());
                final Component component = componentMap.get(tmp.getActionKey());
                final Action toolbarItemAction = toolbarItemActionMap.get(tmp.getActionKey());
                final Action windowSearchAction = windowSearchWithMenuItemActionMap.get(tmp.getActionKey());

                if ((action != null) || ((action == null) && hasSubItems(tmp))) {
                    final String name = getActionName(tmp);

                    if (hasSubItems(tmp)) {
                        // the toolbar element has a sub menu
                        final JPopupMenuButton button = new JPopupMenuButton();
                        if (action == null) {
                            action = new ApplyIconFromSubAction();
                        }
                        button.setAction(action);
                        addSubActionToButton(button, tmp.getItems());
                        toolbar.add(button);

                        if (name != null) {
                            button.setText(name);
                        }
                    } else {
                        final AbstractButton button = createButtonForItem(tmp, action);

                        toolbar.add(button);

                        if (name != null) {
                            button.setText(name);
                        }
                    }
                } else if (component != null) {
                    if (tmp.getActionKey().equals("ToolbarSeparator")) {
                        toolbar.add((new ToolbarSeparator()).getComponent());
                    } else {
                        toolbar.add(component);

                        if ((tmp.getRadio() != null) && !tmp.getRadio().isEmpty() && (component instanceof JButton)) {
                            ButtonGroup group = buttonGroupMap.get(tmp.getRadio());

                            if (group == null) {
                                group = new ButtonGroup();
                                buttonGroupMap.put(tmp.getRadio(), group);
                            }

                            group.add((JButton)component);
                        }
                    }
                } else if ((toolbarItemAction != null) || (windowSearchAction != null)) {
                    final String name = getActionName(tmp);
                    final AbstractButton button = ((toolbarItemAction != null)
                            ? createButtonForItem(tmp, toolbarItemAction)
                            : createButtonForItem(tmp, windowSearchAction));

                    toolbar.add(button);

                    if (name != null) {
                        button.setText(name);
                    }
                }
            }
        }
    }

    /**
     * Creates a button for the given item object and assigns the given action.
     *
     * @param   item    the item that defines the button
     * @param   action  the action to assign
     *
     * @return  a button defined by the given item object
     */
    private AbstractButton createButtonForItem(final Item item, final Action action) {
        AbstractButton button;

        if (item.isToggle()) {
            button = new JToggleButton(action);
        } else if ((item.getRadio() != null) && !item.getRadio().isEmpty()) {
            button = new JToggleButton(action);
            ButtonGroup group = buttonGroupMap.get(item.getRadio());

            if (group == null) {
                group = new ButtonGroup();
                buttonGroupMap.put(item.getRadio(), group);
                button.setSelected(true);
            }

            group.add(button);
        } else {
            button = new JButton(action);
            button.setText(null);
        }

        button.setBorderPainted(false);
        button.setFocusPainted(false);

        return button;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   item  DOCUMENT ME!
     *
     * @return  true, iff the given item has sub items
     */
    private boolean hasSubItems(final Item item) {
        return (item.getItems() != null) && (item.getItems().length > 0);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  button        DOCUMENT ME!
     * @param  toolbarItems  DOCUMENT ME!
     */
    private void addSubActionToButton(final JPopupMenuButton button, final Item[] toolbarItems) {
        final JPopupMenu popupMenu = new JPopupMenu();

        for (final Item tmp : toolbarItems) {
            if (tmp.getActionKey() == null) {
                final JMenu m = new JMenu(tmp.getName());
                popupMenu.add(m);
                addToolbarMenuItems(button, tmp.getItems(), m);
            } else {
                final String actionKey = tmp.getActionKey();
                final CidsUiAction action = actionMap.get(actionKey);
                final Action toolbarItemAction = toolbarItemActionMap.get(tmp.getActionKey());
                final Action windowSearchAction = windowSearchWithMenuItemActionMap.get(tmp.getActionKey());

                if ((action != null) || (toolbarItemAction != null) || (windowSearchAction != null)) {
                    final JMenuItem menuItem = createMenuForItem(tmp, action, toolbarItemAction, windowSearchAction);

                    popupMenu.add(menuItem);
                    final String name = getActionName(tmp);
                    menuItem.addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(final ActionEvent e) {
                                button.getAction()
                                        .actionPerformed(
                                            new ActionEvent(
                                                ((JMenuItem)e.getSource()).getAction(),
                                                ActionEvent.ACTION_FIRST,
                                                e.getActionCommand()));
                            }
                        });
                    if (name != null) {
                        menuItem.setText(name);
                    }
                }
            }
        }

        button.setPopupMenu(popupMenu);
    }

    /**
     * Add the given items to the given toolbar.
     *
     * @param  button  DOCUMENT ME!
     * @param  items   DOCUMENT ME!
     * @param  menu    DOCUMENT ME!
     */
    private void addToolbarMenuItems(final JPopupMenuButton button, final Item[] items, final JMenu menu) {
        for (final Item tmp : items) {
            if (tmp.getActionKey() == null) {
                final JMenu m = new JMenu(tmp.getName());
                menu.add(m);
                addToolbarMenuItems(button, tmp.getItems(), m);
            } else {
                final String actionKey = tmp.getActionKey();
                final CidsUiAction action = actionMap.get(actionKey);
                final Action toolbarItemAction = toolbarItemActionMap.get(tmp.getActionKey());
                final Action windowSearchAction = windowSearchWithMenuItemActionMap.get(tmp.getActionKey());

                if ((action != null) || (toolbarItemAction != null) || (windowSearchAction != null)) {
                    final JMenuItem menuItem = createMenuForItem(tmp, action, toolbarItemAction, windowSearchAction);
                    menu.add(menuItem);
                    final String name = getActionName(tmp);

                    menuItem.addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(final ActionEvent e) {
                                button.getAction()
                                        .actionPerformed(
                                            new ActionEvent(
                                                ((JMenuItem)e.getSource()).getAction(),
                                                ActionEvent.ACTION_FIRST,
                                                e.getActionCommand()));
                            }
                        });

                    if (name != null) {
                        menuItem.setText(name);
                    }
                }
            }
        }
    }

    /**
     * Add the given items to the given menu.
     *
     * @param  items  DOCUMENT ME!
     * @param  menu   DOCUMENT ME!
     */
    private void addMenuItems(final Item[] items, final JMenu menu) {
        for (final Item tmp : items) {
            if (tmp.getActionKey().equals("MenuSeparator")) {
                menu.add(new JSeparator());
            } else if (tmp.getActionKey() == null) {
                final JMenu m = new JMenu(tmp.getName());
                menu.add(m);
                addMenuItems(tmp.getItems(), m);
            } else {
                final String actionKey = tmp.getActionKey();
                final CidsUiAction action = actionMap.get(actionKey);
                final Action toolbarItemAction = toolbarItemActionMap.get(tmp.getActionKey());
                final Action windowSearchAction = windowSearchWithMenuItemActionMap.get(tmp.getActionKey());
                final CidsUiMenuProvider provider = menuMap.get(tmp.getActionKey());

                if ((action != null) || (toolbarItemAction != null) || (windowSearchAction != null)) {
                    final JMenuItem menuItem = createMenuForItem(tmp, action, toolbarItemAction, windowSearchAction);
                    menu.add(menuItem);
                    final String name = getActionName(tmp);

                    if (name != null) {
                        menuItem.setText(name);
                    }
                } else if (provider != null) {
                    final JMenu menuFromProvider = provider.getMenu();

                    if (menu != null) {
                        for (final Component comp : menuFromProvider.getMenuComponents()) {
                            menu.add(comp);
                        }
                    }

                    provider.addCidsUiMenuProviderListener(new CidsUiMenuProviderListener() {

                            @Override
                            public void menuItemAdded(final CidsUiMenuProviderEvent e) {
                                menu.add(e.getAffectedMenuItem());
                            }

                            @Override
                            public void menuItemRemoved(final CidsUiMenuProviderEvent e) {
                                menu.remove(e.getAffectedMenuItem());
                            }
                        });
                }
            }
        }
    }

    /**
     * Creates a JMenuItem for the given item object and assigns the given action.
     *
     * @param   item    the item that defines the JMenuItem object
     * @param   action  the action to assign
     *
     * @return  a JMenuItem defined by the given item object
     */
    private JMenuItem createMenuForItem(final Item item, final Action action) {
        JMenuItem menuItem;

        if (item.isToggle()) {
            menuItem = new JCheckBoxMenuItem(action);
        } else if ((item.getRadio() != null) && !item.getRadio().isEmpty()) {
            menuItem = new JRadioButtonMenuItem(action);
            ButtonGroup group = buttonGroupMap.get(item.getRadio());

            if (group == null) {
                group = new ButtonGroup();
                buttonGroupMap.put(item.getRadio(), group);
                menuItem.setSelected(true);
            }

            group.add(menuItem);
        } else {
            menuItem = new JMenuItem(action);
        }

        return menuItem;
    }

    /**
     * Creates a JMenuItem for the given item object and assigns the given action that is not null.
     *
     * @param   item                DOCUMENT ME!
     * @param   action              the first action that will be considered
     * @param   toolbarItemAction   the second action that will be considered
     * @param   windowSearchAction  the third action that will be considered
     *
     * @return  DOCUMENT ME!
     */
    private JMenuItem createMenuForItem(final Item item,
            final Action action,
            final Action toolbarItemAction,
            final Action windowSearchAction) {
        if (action != null) {
            return createMenuForItem(item, action);
        } else if (toolbarItemAction != null) {
            return createMenuForItem(item, toolbarItemAction);
        } else {
            return createMenuForItem(item, windowSearchAction);
        }
    }

    /**
     * Determines the name of the given item.
     *
     * @param   item  DOCUMENT ME!
     *
     * @return  the name of the given item
     */
    private String getActionName(final Item item) {
        String name = null;

        if (item.getName() != null) {
            name = item.getName();
        } else if (item.getI18nKey() != null) {
            name = NbBundle.getMessage(ActionConfiguration.class, item.getI18nKey());
        }

        return name;
    }

    /**
     * Search for all available actions and action groups and initialises the corresponding maps.
     */
    private void lookup() {
        final Collection<? extends CidsUiAction> actions = Lookup.getDefault().lookupAll(CidsUiAction.class);
        final Collection<? extends CidsUiComponent> components = Lookup.getDefault().lookupAll(CidsUiComponent.class);
        final Collection<? extends CidsUiActionProvider> actionProvider = Lookup.getDefault()
                    .lookupAll(CidsUiActionProvider.class);
        final Collection<? extends CidsClientToolbarItem> toolbarActions = Lookup.getDefault()
                    .lookupAll(CidsClientToolbarItem.class);
        final Collection<? extends CidsWindowSearch> windowSearchActions = Lookup.getDefault()
                    .lookupAll(CidsWindowSearch.class);
        final Collection<? extends CidsUiMenuProvider> menus = Lookup.getDefault().lookupAll(CidsUiMenuProvider.class);
        final Collection<? extends ToolbarComponentsProvider> toolbarProvider = Lookup.getDefault()
                    .lookupAll(ToolbarComponentsProvider.class);

        for (final CidsUiAction action : actions) {
            actionMap.put((String)action.getValue(CidsUiAction.CIDS_ACTION_KEY), action);
        }

        for (final CidsUiMenuProvider action : menus) {
            menuMap.put(action.getMenuKey(), action);
        }

        for (final CidsClientToolbarItem action : toolbarActions) {
            if (action.isVisible()) {
                if (action instanceof CidsUiComponent) {
                    final CidsUiComponent uiComp = (CidsUiComponent)action;
                    componentMap.put((String)uiComp.getValue(CidsUiAction.CIDS_ACTION_KEY), uiComp.getComponent());
                }
                if (action instanceof Action) {
                    toolbarItemActionMap.put((String)((Action)action).getValue(CidsUiAction.CIDS_ACTION_KEY),
                        (Action)action);
                }
            }
        }

        for (final ToolbarComponentsProvider tp : toolbarProvider) {
            if (tp instanceof CidsUiComponent) {
                final CidsUiComponent uiComp = (CidsUiComponent)tp;
                componentMap.put((String)uiComp.getValue(CidsUiAction.CIDS_ACTION_KEY), uiComp.getComponent());
            }
        }

        for (final CidsWindowSearch action : windowSearchActions) {
            if (action instanceof CidsUiAction) {
                windowSearchWithMenuItemActionMap.put((String)((Action)action).getValue(CidsUiAction.CIDS_ACTION_KEY),
                    new AbstractAction() {

                        {
                            putValue(NAME, action.getName());
                            putValue(SMALL_ICON, action.getIcon());
                            putValue(
                                CidsUiAction.CIDS_ACTION_KEY,
                                ((CidsUiAction)action).getValue(CidsUiAction.CIDS_ACTION_KEY));
                        }

                        @Override
                        public void actionPerformed(final ActionEvent e) {
                            ComponentRegistry.getRegistry().showComponent(action.getClass().getName());
                        }
                    });
            }
        }

        for (final CidsUiComponent comp : components) {
            componentMap.put((String)comp.getValue(CidsUiAction.CIDS_ACTION_KEY), comp.getComponent());
        }

        for (final CidsUiActionProvider provider : actionProvider) {
            for (final CidsUiAction action : provider.getActions()) {
                actionMap.put((String)action.getValue(CidsUiAction.CIDS_ACTION_KEY), action);
            }
        }

        final List<Object> allCreatedObjects = new ArrayList<>();

        allCreatedObjects.addAll(actionMap.values());
        allCreatedObjects.addAll(menuMap.values());
        allCreatedObjects.addAll(componentMap.values());
        allCreatedObjects.addAll(windowSearchWithMenuItemActionMap.values());
        allCreatedObjects.addAll(toolbarItemActionMap.values());

        for (final Object o : allCreatedObjects) {
            if (o instanceof ConnectionContextStore) {
                ((ConnectionContextStore)o).initWithConnectionContext(context);
            }
        }
    }

    /**
     * Read the json file with the menu configuration.
     *
     * @param   configurationFile  a json file with the menu configuration
     *
     * @return  a menu object representation of the given configuration file
     */
    private Menu readConfiguration(final String configurationFile) {
        final ObjectMapper mapper = new ObjectMapper();

        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(
                        this.getClass().getResourceAsStream(configurationFile)));
            return mapper.readValue(reader, Menu.class);
        } catch (IOException ex) {
            LOG.error("Error while parsing the menu configuration", ex);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    LOG.warn("Cannot close the menu configuration file", ex);
                }
            }
        }

        return null;
    }

    /**
     * Read the json input stream with the menu configuration.
     *
     * @param   configurationStream  a json input stream with the menu configuration
     *
     * @return  a menu object representation of the given configuration file
     */
    private Menu readConfiguration(final InputStream configurationStream) {
        final ObjectMapper mapper = new ObjectMapper();

        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(configurationStream));
            return mapper.readValue(reader, Menu.class);
        } catch (IOException ex) {
            LOG.error("Error while parsing the menu configuration", ex);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    LOG.warn("Cannot close the menu configuration file", ex);
                }
            }
        }

        return null;
    }
}
