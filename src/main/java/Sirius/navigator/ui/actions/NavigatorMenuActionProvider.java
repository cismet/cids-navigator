/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui.actions;

import Sirius.navigator.Navigator;
import Sirius.navigator.NavigatorX;
import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.exception.ConnectionException;
import Sirius.navigator.exception.ExceptionManager;
import Sirius.navigator.plugin.PluginRegistry;
import Sirius.navigator.plugin.interfaces.LayoutManager;
import Sirius.navigator.plugin.interfaces.PluginMethod;
import Sirius.navigator.plugin.interfaces.PluginSupport;
import Sirius.navigator.resource.ResourceManager;
import Sirius.navigator.types.treenode.RootTreeNode;
import Sirius.navigator.ui.ComponentRegistry;
import Sirius.navigator.ui.LayoutedContainer;

import org.apache.log4j.Logger;

import java.awt.event.ActionEvent;

import java.io.File;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import de.cismet.tools.JnlpSystemPropertyHelper;

import de.cismet.tools.gui.menu.CidsUiAction;
import de.cismet.tools.gui.menu.CidsUiActionProvider;

import static javax.swing.Action.ACCELERATOR_KEY;
import static javax.swing.Action.ACTION_COMMAND_KEY;
import static javax.swing.Action.NAME;
import static javax.swing.Action.SHORT_DESCRIPTION;
import static javax.swing.Action.SMALL_ICON;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
@org.openide.util.lookup.ServiceProvider(service = CidsUiActionProvider.class)
public class NavigatorMenuActionProvider implements CidsUiActionProvider {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(NavigatorMenuActionProvider.class);
    private static final ResourceManager RESOURCES = ResourceManager.getManager();
    private static Sirius.navigator.plugin.interfaces.LayoutManager layoutManager;
    private static String EXTENSION = JnlpSystemPropertyHelper.getProperty("directory.extension", "");

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  layoutManager  DOCUMENT ME!
     */
    public static void setLayeroutManager(final LayoutManager layoutManager) {
        NavigatorMenuActionProvider.layoutManager = layoutManager;
    }

    @Override
    public List<CidsUiAction> getActions() {
        final List<CidsUiAction> actionList = new ArrayList<CidsUiAction>();

        actionList.add(new SaveCurrentLayoutAction());
        actionList.add(new LoadCurrentLayoutAction());
        actionList.add(new ReloadCatalogueAction());
        actionList.add(new ResetCurrentLayoutAction());
        actionList.add(new ExitAction());

        return actionList;
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private static class SaveCurrentLayoutAction extends AbstractAction implements CidsUiAction {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new SaveCurrentLayoutAction object.
         */
        public SaveCurrentLayoutAction() {
            initAction();
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         */
        private void initAction() {
            putValue(SMALL_ICON, RESOURCES.getIcon("layout.png"));
            putValue(
                NAME,
                org.openide.util.NbBundle.getMessage(
                    NavigatorMenuActionProvider.class,
                    "SaveCurrentLayoutAction.initAction.saveCurrentLayout.title"));
            putValue(
                SHORT_DESCRIPTION,
                org.openide.util.NbBundle.getMessage(
                    NavigatorMenuActionProvider.class,
                    "SaveCurrentLayoutAction.initAction.saveCurrentLayout.tooltip"));
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl S"));
            putValue(ACTION_COMMAND_KEY, "navigator.save.current.layout");
            putValue(CidsUiAction.CIDS_ACTION_KEY, "navigator.save.current.layout");
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("save layout"); // NOI18N
            }
            if (layoutManager != null) {
                layoutManager.saveCurrentLayout(ComponentRegistry.getRegistry().getMainWindow());
            } else {
                saveCurrentLayout((NavigatorX)ComponentRegistry.getRegistry().getMainWindow());
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param  parent  DOCUMENT ME!
         */
        public void saveCurrentLayout(final NavigatorX parent) {
            final JFileChooser fc = new JFileChooser(Navigator.NAVIGATOR_HOME);
            fc.setFileFilter(new FileFilter() {

                    @Override
                    public boolean accept(final File f) {
                        return f.isDirectory()
                                    || f.getName().toLowerCase().endsWith(".layout" + EXTENSION.toLowerCase()); // NOI18N
                    }

                    @Override
                    public String getDescription() {
                        return "Layout"; // NOI18N
                    }
                });
            fc.setMultiSelectionEnabled(false);
            final int state = fc.showSaveDialog(parent);
            if (LOG.isDebugEnabled()) {
                LOG.debug("state:" + state); // NOI18N
            }
            if (state == JFileChooser.APPROVE_OPTION) {
                final File file = fc.getSelectedFile();
                if (LOG.isDebugEnabled()) {
                    LOG.debug("file:" + file); // NOI18N
                }
                final String name = file.getAbsolutePath();
                if (name.toLowerCase().endsWith(".layout" + EXTENSION.toLowerCase())) { // NOI18N
                    parent.saveLayout(name, parent);
                } else {
                    parent.saveLayout(name + ".layout" + EXTENSION, parent); // NOI18N
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private static class LoadCurrentLayoutAction extends AbstractAction implements CidsUiAction {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new LoadCurrentLayoutAction object.
         */
        public LoadCurrentLayoutAction() {
            initAction();
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         */
        private void initAction() {
            putValue(SMALL_ICON, RESOURCES.getIcon("layout.png"));
            putValue(
                NAME,
                org.openide.util.NbBundle.getMessage(
                    NavigatorMenuActionProvider.class,
                    "LoadCurrentLayoutAction.initAction.saveCurrentLayout.title"));
            putValue(
                SHORT_DESCRIPTION,
                org.openide.util.NbBundle.getMessage(
                    NavigatorMenuActionProvider.class,
                    "LoadCurrentLayoutAction.initAction.saveCurrentLayout.tooltip"));
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl O"));
            putValue(ACTION_COMMAND_KEY, "navigator.open.layout");
            putValue(CidsUiAction.CIDS_ACTION_KEY, "navigator.open.layout");
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("open layout"); // NOI18N
            }
            if (layoutManager != null) {
                layoutManager.loadLayout(ComponentRegistry.getRegistry().getMainWindow());
            } else {
                loadLayout((NavigatorX)ComponentRegistry.getRegistry().getMainWindow());
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param  parent  DOCUMENT ME!
         */
        public void loadLayout(final NavigatorX parent) {
            final JFileChooser fc = new JFileChooser(Navigator.NAVIGATOR_HOME);
            fc.setFileHidingEnabled(false);
            fc.setFileFilter(new FileFilter() {

                    @Override
                    public boolean accept(final File f) {
                        return f.isDirectory()
                                    || f.getName().toLowerCase().endsWith(".layout" + EXTENSION.toLowerCase()); // NOI18N
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
                if (name.toLowerCase().endsWith(".layout" + EXTENSION.toLowerCase())) { // NOI18N
                    parent.loadLayout(name, false);
                } else {
                    JOptionPane.showMessageDialog(
                        parent,
                        org.openide.util.NbBundle.getMessage(
                            LayoutedContainer.class,
                            "LoadCurrentLayoutAction.loadLayout(Component).JOptionPane.message"), // NOI18N
                        org.openide.util.NbBundle.getMessage(
                            LayoutedContainer.class,
                            "LoadCurrentLayoutAction.loadLayout(Component).JOptionPane.title"), // NOI18N
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private static class ResetCurrentLayoutAction extends AbstractAction implements CidsUiAction {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new ResetCurrentLayoutAction object.
         */
        public ResetCurrentLayoutAction() {
            initAction();
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         */
        private void initAction() {
            putValue(SMALL_ICON, RESOURCES.getIcon("layout.png"));
            putValue(
                NAME,
                org.openide.util.NbBundle.getMessage(
                    NavigatorMenuActionProvider.class,
                    "ResetCurrentLayoutAction.initAction.resetLayout.title"));
            putValue(
                SHORT_DESCRIPTION,
                org.openide.util.NbBundle.getMessage(
                    NavigatorMenuActionProvider.class,
                    "ResetCurrentLayoutAction.initAction.resetLayout.tooltip"));
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl R"));
            putValue(ACTION_COMMAND_KEY, "navigator.reset.layout");
            putValue(CidsUiAction.CIDS_ACTION_KEY, "navigator.reset.layout");
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("reset layout"); // NOI18N
            }

            if (layoutManager != null) {
                layoutManager.resetLayout();
            } else {
                ((NavigatorX)ComponentRegistry.getRegistry().getMainWindow()).loadLayout(
                    LayoutedContainer.DEFAULT_LOCAL_LAYOUT,
                    true);
            }

            final PluginSupport cismapPlugin = PluginRegistry.getRegistry().getPlugin("cismap");
            if (cismapPlugin != null) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("reset layout of cismap plugin"); // NOI18N
                }
                final PluginMethod resetLayoutMethod = cismapPlugin.getMethod(
                        "de.cismet.cismap.navigatorplugin.CismapPlugin$ResetLayoutMethod");
                if (resetLayoutMethod != null) {
                    try {
                        resetLayoutMethod.invoke();
                    } catch (Exception ex) {
                        LOG.error("ResetLayoutMethod of cismap plugin failed: " + ex.getMessage(), ex);
                    }
                } else {
                    LOG.warn("ResetLayoutMethod of cismap plugin not available");
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private static class ReloadCatalogueAction extends AbstractAction implements CidsUiAction {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new ReloadCatalogueAction object.
         */
        public ReloadCatalogueAction() {
            initAction();
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         */
        private void initAction() {
            putValue(
                NAME,
                org.openide.util.NbBundle.getMessage(
                    NavigatorMenuActionProvider.class,
                    "ReloadCatalogueAction.initAction.reloadCatalogue.title"));
//            putValue(
//                MNEMONIC_KEY,
//                org.openide.util.NbBundle.getMessage(
//                    NavigatorMenuActionProvider.class,
//                    "ReloadCatalogueAction.initAction.reloadCatalogue.mnemonic"));
            putValue(
                SHORT_DESCRIPTION,
                org.openide.util.NbBundle.getMessage(
                    NavigatorMenuActionProvider.class,
                    "ReloadCatalogueAction.initAction.reloadCatalogue.tooltip"));
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("F5"));
            putValue(ACTION_COMMAND_KEY, "tree.refres");
            putValue(CidsUiAction.CIDS_ACTION_KEY, "tree.refresh");
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            try {
                final TreePath selectionPath = ComponentRegistry.getRegistry().getCatalogueTree().getSelectionPath();
                if ((selectionPath != null) && (selectionPath.getPath().length > 0)) {
                    final RootTreeNode rootTreeNode = new RootTreeNode(SessionManager.getProxy().getRoots());
                    ((DefaultTreeModel)ComponentRegistry.getRegistry().getCatalogueTree().getModel()).setRoot(
                        rootTreeNode);
                    ((DefaultTreeModel)ComponentRegistry.getRegistry().getCatalogueTree().getModel()).reload();
                    ComponentRegistry.getRegistry().getCatalogueTree().exploreSubtree(selectionPath);
                }
            } catch (ConnectionException ex) {
                LOG.error("Error while refreshing the tree", ex); // NOI18N
            } catch (RuntimeException ex) {
                LOG.error("Error while refreshing the tree", ex); // NOI18N
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private static class ExitAction extends AbstractAction implements CidsUiAction {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new ExitAction object.
         */
        public ExitAction() {
            initAction();
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         */
        private void initAction() {
            putValue(SMALL_ICON, RESOURCES.getIcon("stop16.gif"));
            putValue(
                NAME,
                org.openide.util.NbBundle.getMessage(
                    NavigatorMenuActionProvider.class,
                    "ExitAction.initAction.exit.title"));
//            putValue(
//                MNEMONIC_KEY,
//                org.openide.util.NbBundle.getMessage(
//                    NavigatorMenuActionProvider.class,
//                    "ExitAction.initAction.exit.mnemonic"));
            putValue(
                SHORT_DESCRIPTION,
                org.openide.util.NbBundle.getMessage(
                    NavigatorMenuActionProvider.class,
                    "ExitAction.initAction.exit.tooltip"));
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("alt X"));
            putValue(ACTION_COMMAND_KEY, "navigator.exit");
            putValue(CidsUiAction.CIDS_ACTION_KEY, "navigator.exit");
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            if (ExceptionManager.getManager().showExitDialog(ComponentRegistry.getRegistry().getMainWindow())) {
                LOG.info("closing program"); // NOI18N
                if (ComponentRegistry.getRegistry().getNavigator() == null) {
                    ComponentRegistry.getRegistry().getMainWindow().dispose();
                } else {
                    ComponentRegistry.getRegistry().getNavigator().dispose();
                }
                System.exit(0);
            }
        }
    }
}
