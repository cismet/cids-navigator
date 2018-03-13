/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.plugin;

import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.exception.ExceptionManager;
import Sirius.navigator.plugin.interfaces.PluginSupport;
import Sirius.navigator.resource.PropertyManager;
import Sirius.navigator.ui.ComponentRegistry;
import Sirius.navigator.ui.MutableConstraints;

import Sirius.server.newuser.User;
import Sirius.server.newuser.UserGroup;

import org.apache.log4j.Logger;

import java.util.*;

import de.cismet.connectioncontext.ConnectionContext;
import de.cismet.connectioncontext.ConnectionContextProvider;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class PluginRegistry implements ConnectionContextProvider {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(PluginRegistry.class);

    private static PluginRegistry REGISTRY = null;
    private static final Object BLOCKER = new Object();

    //~ Instance fields --------------------------------------------------------

    private final PluginFactory pluginFactory;
    private final Hashtable plugins;
    private final ConnectionContext connectionContext = ConnectionContext.create(
            ConnectionContext.Category.OTHER,
            getClass().getSimpleName());

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of PluginPool.
     */
    private PluginRegistry() {
        pluginFactory = new PluginFactory(getConnectionContext());
        plugins = new Hashtable();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static final PluginRegistry getRegistry() {
        synchronized (BLOCKER) {
            if (REGISTRY == null) {
                REGISTRY = new PluginRegistry();
            }

            return REGISTRY;
        }
    }

    /**
     * DOCUMENT ME!
     */
    public static final void destroy() {
        synchronized (BLOCKER) {
            LOG.warn("destroying singelton PluginRegistry instance"); // NOI18N

            try {
                PluginRegistry.getRegistry().deactivatePlugins();
            } catch (Throwable t) {
                LOG.error("could not deactivate plugins", t); // NOI18N
            }

            REGISTRY = null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public void preloadPlugins() throws Exception {
        final Iterator iterator = PropertyManager.getManager().getPluginList();
        if (iterator != null) {
            while (iterator.hasNext()) {
                final PluginDescriptor pluginDescriptor = new PluginDescriptor((String)iterator.next());
                if (LOG.isDebugEnabled()) {
                    LOG.debug("preloading new plugin"); // NOI18N
                }

                try {
                    pluginFactory.preloadPlugin(pluginDescriptor, true);
                    this.registerPlugin(pluginDescriptor);
                    if (LOG.isInfoEnabled()) {
                        LOG.info("plugin' " + pluginDescriptor.getMetaInfo().getName() + " ("
                                    + pluginDescriptor.getName() + ")' successfully registred");      // NOI18N
                    }
                } catch (Throwable t) {
                    LOG.error("could not load plugin '" + pluginDescriptor.getName() + "'", t);       // NOI18N
                    ExceptionManager.getManager()
                            .showExceptionDialog(
                                ExceptionManager.ERROR,
                                org.openide.util.NbBundle.getMessage(
                                    PluginRegistry.class,
                                    "PluginRegistry.preloadPlugins().ExceptionManager_anon.name"),    // NOI18N
                                org.openide.util.NbBundle.getMessage(
                                    PluginRegistry.class,
                                    "PluginRegistry.preloadPlugins().ExceptionManager_anon.message"), // NOI18N
                                t);
                    pluginDescriptor.setLoaded(false);
                }

                /*try
                 * { PluginDescriptor descriptor = new PluginDescriptor((String)iterator.next());
                 * if(logger.isDebugEnabled())logger.debug("preloading new plugin");
                 * pluginFactory.preloadPlugin(descriptor, true); this.registerPlugin(descriptor); logger.info("plugin'
                 * " + descriptor.getMetaInfo().getName() + " (" + descriptor.getName() + ")' successfully registred");
                 * } catch(Exception exp) { logger.fatal("could not load plugin", exp); System.exit(1);}*/
            }
        } else {
            if (LOG.isInfoEnabled()) {
                LOG.info("no plugins found"); // NOI18N
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   id  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public void loadPlugin(final String id) throws Exception {
        final PluginDescriptor descriptor = this.getPluginDescriptor(id);
        if (descriptor != null) {
            this.loadPlugin(descriptor);
        } else {
            LOG.error("plugin '" + id + "' not found"); // NOI18N
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void loadPlugins() // throws Exception
    {
        final Iterator iterator = this.getPluginDescriptors();
        if ((iterator != null) && iterator.hasNext()) {
            while (iterator.hasNext()) {
                final PluginDescriptor pluginDescriptor = (PluginDescriptor)iterator.next();

                if (LOG.isInfoEnabled()) {
                    LOG.info("users: " + pluginDescriptor.getUsers().size()); // NOI18N
                }
                final Iterator uIterator = pluginDescriptor.getUsers().iterator();
                while (uIterator.hasNext()) {
                    if (LOG.isInfoEnabled()) {
                        LOG.info("user: '" + uIterator.next() + "'");         // NOI18N
                    }
                }

                final User user = SessionManager.getSession().getUser();
                if (pluginDescriptor.getUsers().isEmpty() || pluginDescriptor.getUsers().contains(user.getName())) {
                    final UserGroup userGroup = user.getUserGroup();
                    boolean containsUserGroup = false;
                    if (userGroup != null) {
                        containsUserGroup = pluginDescriptor.getUsergroups().contains(userGroup.toString());
                    } else {
                        for (final UserGroup potentialUserGroup : user.getPotentialUserGroups()) {
                            if (pluginDescriptor.getUsergroups().contains(potentialUserGroup.toString())) {
                                containsUserGroup = true;
                                break;
                            }
                        }
                    }
                    if (pluginDescriptor.getUsergroups().isEmpty() || containsUserGroup) {
                        try {
                            this.loadPlugin(pluginDescriptor);
                        } catch (Throwable t) {
                            LOG.error("could not load plugin '" + pluginDescriptor.getName() + "'", t);    // NOI18N
                            ExceptionManager.getManager()
                                    .showExceptionDialog(
                                        ExceptionManager.ERROR,
                                        org.openide.util.NbBundle.getMessage(
                                            PluginRegistry.class,
                                            "PluginRegistry.loadPlugins().ExceptionManager_anon.name"),    // NOI18N
                                        org.openide.util.NbBundle.getMessage(
                                            PluginRegistry.class,
                                            "PluginRegistry.loadPlugins().ExceptionManager_anon.message"), // NOI18N
                                        t);
                            pluginDescriptor.setLoaded(false);
                        }
                    } else {
                        LOG.warn("plugin '" + pluginDescriptor.getName() + "' not loaded: no usergroup '" + userGroup
                                    + "'");                                                                // NOI18N
                    }
                } else {
                    LOG.warn("plugin '" + pluginDescriptor.getName() + "' not loaded: no user  '"
                                + SessionManager.getSession().getUser().getName() + "'");                  // NOI18N
                }
            }
        } else {
            if (LOG.isInfoEnabled()) {
                LOG.info("could not load any plugins: no plugins found or preloaded");                     // NOI18N
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   id  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public void activatePlugin(final String id) throws Exception {
        final PluginDescriptor descriptor = this.getPluginDescriptor(id);
        if (descriptor != null) {
            this.activatePlugin(descriptor);
        } else {
            LOG.error("plugin '" + id + "' not found"); // NOI18N
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   id  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public void deactivatePlugin(final String id) throws Exception {
        final PluginDescriptor descriptor = this.getPluginDescriptor(id);
        if (descriptor != null) {
            this.deactivatePlugin(descriptor);
        } else {
            LOG.error("plugin '" + id + "' not found"); // NOI18N
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public void activatePlugins() throws Exception {
        final Iterator iterator = this.getPluginDescriptors();
        if ((iterator != null) && iterator.hasNext()) {
            while (iterator.hasNext()) {
                this.activatePlugin((PluginDescriptor)iterator.next());
            }
        } else {
            if (LOG.isInfoEnabled()) {
                LOG.info("could not activate any plugins: no plugins found or preloaded"); // NOI18N
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public void deactivatePlugins() throws Exception {
        final Iterator iterator = this.getPluginDescriptors();
        if ((iterator != null) && iterator.hasNext()) {
            while (iterator.hasNext()) {
                this.deactivatePlugin((PluginDescriptor)iterator.next());
            }
        } else {
            if (LOG.isInfoEnabled()) {
                LOG.info("could not deactivate any plugins: no plugins found or preloaded"); // NOI18N
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  visible  DOCUMENT ME!
     */
    public void setPluginsVisible(final boolean visible) {
        final Iterator iterator = this.getPluginDescriptors();
        if ((iterator != null) && iterator.hasNext()) {
            while (iterator.hasNext()) {
                this.setPluginVisible((PluginDescriptor)iterator.next(), visible);
            }
        } else {
            if (LOG.isInfoEnabled()) {
                LOG.info("could not activate any plugins: no plugins found or preloaded"); // NOI18N
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Iterator getPluginDescriptors() {
        return plugins.values().iterator();
    }

    /**
     * DOCUMENT ME!
     *
     * @param   id  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public PluginSupport getPlugin(final String id) {
        final Object object = plugins.get(id);

        if (object != null) {
            return ((PluginDescriptor)object).getPlugin();
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   id  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public PluginDescriptor getPluginDescriptor(final String id) {
        final Object object = plugins.get(id);

        if (object != null) {
            return (PluginDescriptor)object;
        } else {
            return null;
        }
    }

    /**
     * helper methods ##########################################################
     *
     * @param  descriptor  DOCUMENT ME!
     */
    private void registerPlugin(final PluginDescriptor descriptor) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("register new plugin: name='" + descriptor.getName() + "', id='" + descriptor.getId() + "'"); // NOI18N
        }

        if (plugins.containsKey(descriptor.getId())) {
            LOG.fatal("duplicate plugin id '" + descriptor.getId() + "' detected in plugin '"
                        + descriptor.getMetaInfo().getName() + " (" + descriptor.getName() + ")'"); // NOI18N
        } else {
            plugins.put(descriptor.getId(), descriptor);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   descriptor  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    private void loadPlugin(final PluginDescriptor descriptor) throws Exception {
        if (!descriptor.isLoaded()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("loading plugin '" + descriptor.getName() + "'");    // NOI18N
            }
            pluginFactory.loadPlugin(descriptor);
            descriptor.setLoaded(true);
        } else if (LOG.isDebugEnabled()) {
            LOG.debug("plugin '" + descriptor.getName() + "' already loaded"); // NOI18N
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  descriptor  DOCUMENT ME!
     */
    private void activatePlugin(final PluginDescriptor descriptor)             // throws Exception
    {
        if (descriptor.isLoaded() && !descriptor.isActivated()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("activating plugin '" + descriptor.getName() + "'"); // NOI18N
            }

            // activate ui(s) ..................................................
            if (descriptor.isPluginToolBarAvailable()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("activating plugin '" + descriptor.getName() + "' toolbar"); // NOI18N
                }
                ComponentRegistry.getRegistry().getMutableToolBar().addPluginToolBar(descriptor.getPluginToolBar());
            }

            if (descriptor.isPluginMenuAvailable()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("activating plugin '" + descriptor.getName() + "' menu"); // NOI18N
                }
                ComponentRegistry.getRegistry().getMutableMenuBar().addPluginMenu(descriptor.getPluginMenu());
            }

            if (descriptor.isPluginPopupMenuAvailable()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("activating plugin '" + descriptor.getName() + "' popup menu"); // NOI18N
                }
                ComponentRegistry.getRegistry().getMutablePopupMenu().addPluginMenu(descriptor.getPluginPopupMenu());
            }

            if (descriptor.isPluginUIDescriptorsAvailable()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("activating plugin '" + descriptor.getName() + "' user interface"); // NOI18N
                }
                final Iterator iterator = descriptor.getPluginUIDescriptors();
                while (iterator.hasNext()) {
                    ComponentRegistry.getRegistry().getGUIContainer().add((MutableConstraints)iterator.next());
                }
            }

            descriptor.getPlugin().setActive(true);
            descriptor.setActivated(true);
        } else {
            if (!descriptor.isLoaded()) {
                LOG.warn("plugin '" + descriptor.getName() + "' could not be activated (not loaded)");         // NOI18N
            } else if (LOG.isDebugEnabled()) {
                LOG.debug("plugin '" + descriptor.getName() + "' could not be activated (already activated)"); // NOI18N
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  descriptor  DOCUMENT ME!
     */
    private void deactivatePlugin(final PluginDescriptor descriptor)             // throws Exception
    {
        if (descriptor.isDeactivateable() && descriptor.isLoaded() && descriptor.isActivated()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("deactivating plugin '" + descriptor.getName() + "'"); // NOI18N
            }

            // activate ui(s) ..................................................
            if (descriptor.isPluginToolBarAvailable()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("deactivating plugin '" + descriptor.getName() + "' toolbar"); // NOI18N
                }
                ComponentRegistry.getRegistry().getMutableToolBar().removePluginToolBar(descriptor.getId());
            }

            if (descriptor.isPluginMenuAvailable()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("deactivating plugin '" + descriptor.getName() + "' menu"); // NOI18N
                }
                ComponentRegistry.getRegistry().getMutableMenuBar().removePluginMenu(descriptor.getId());
            }

            if (descriptor.isPluginPopupMenuAvailable()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("deactivating plugin '" + descriptor.getName() + "' popup menu"); // NOI18N
                }
                ComponentRegistry.getRegistry().getMutablePopupMenu().removePluginMenu(descriptor.getId());
            }

            if (descriptor.isPluginUIDescriptorsAvailable()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("deactivating plugin '" + descriptor.getName() + "' user interface"); // NOI18N
                }
                final Iterator iterator = descriptor.getPluginUIDescriptors();
                while (iterator.hasNext()) {
                    ComponentRegistry.getRegistry()
                            .getGUIContainer()
                            .remove(((MutableConstraints)iterator.next()).getId());
                }
            }

            descriptor.getPlugin().setActive(false);
            descriptor.setActivated(false);
            System.gc();
        } else {
            if (!descriptor.isLoaded()) {
                LOG.warn("plugin '" + descriptor.getName() + "' could not be deactivated (not loaded)"); // NOI18N
            } else if (LOG.isDebugEnabled()) {
                LOG.debug("plugin '" + descriptor.getName()
                            + "' could not be deactivated (already deactivated or not deactivateable)"); // NOI18N
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  descriptor  DOCUMENT ME!
     * @param  visible     DOCUMENT ME!
     */
    private void setPluginVisible(final PluginDescriptor descriptor, final boolean visible) {
        if (descriptor.isActivated()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("setting plugin '" + descriptor.getName() + "' visible"); // NOI18N
            }
            descriptor.getPlugin().setVisible(visible);
        } else if (LOG.isDebugEnabled()) {
            LOG.debug("plugin '" + descriptor.getName() + "' is not activated");    // NOI18N
        }
    }

    @Override
    public ConnectionContext getConnectionContext() {
        return connectionContext;
    }
}
