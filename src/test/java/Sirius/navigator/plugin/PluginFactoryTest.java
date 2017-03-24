package Sirius.navigator.plugin;

import Sirius.navigator.plugin.interfaces.PluginSupport;
import Sirius.navigator.resource.PropertyManager;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import java.util.Iterator;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

/**
 *
 * @author pd
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(DataProviderRunner.class)
public class PluginFactoryTest {

    private final static Logger LOGGER = Logger.getLogger(PluginFactoryTest.class);
    private static boolean propertyManagerInitialised = false;

    public PluginFactoryTest() {

    }

    @BeforeClass
    public static void setUpClass() throws Exception {

        try {
            LOGGER.info("Configuring PropertyManager");
            PropertyManager.getManager().configure(
                    PluginFactoryTest.class.getResource("/client/config/navigator.cfg").toString(),
                    null,
                    PluginFactoryTest.class.getResource("/client/plugins").toString(),
                    PluginFactoryTest.class.getResource("/client/search").toString(),
                    PluginFactoryTest.class.getResource("/client/profiles").toString());
            propertyManagerInitialised = true;
        } catch (final Exception ex) {
            LOGGER.fatal(ex.getMessage(), ex);
            throw ex;
        }
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws Exception {
        assertTrue("propertyManager initialised", propertyManagerInitialised);

        if (!PluginRegistry.getRegistry().getPluginDescriptors().hasNext()) {
            try {
                LOGGER.info("preloading plugins!");
                PluginRegistry.getRegistry().preloadPlugins();
            } catch (final Exception ex) {
                LOGGER.fatal(ex.getMessage(), ex);
                throw ex;
            }
        }
    }

    @After
    public void tearDown() {
    }

    @Test
    public void test010printProperties() {
        LOGGER.info("BasePath: " + PropertyManager.getManager().getBasePath());
        LOGGER.info("PluginPath: " + PropertyManager.getManager().getPluginPath());
        LOGGER.info("ProfilesPath: " + PropertyManager.getManager().getProfilesPath());
        LOGGER.info("SearchFormPath: " + PropertyManager.getManager().getSearchFormPath());

        PropertyManager.getManager().print();
    }

    @Test
    public void test020printPluginPaths() {
        LOGGER.info("PluginListAvailable: " + PropertyManager.getManager().isPluginListAvailable());
        final Iterator pluginIterator = PropertyManager.getManager().getPluginList();
        while (pluginIterator.hasNext()) {
            LOGGER.info(pluginIterator.next());
        }
    }

    @Test
    public void test030printPluginDescriptors() throws Exception {
        
        assertTrue(PluginRegistry.getRegistry().getPluginDescriptors().hasNext());
        
        final Iterator pluginDescriptorIterator = PluginRegistry.getRegistry().getPluginDescriptors();
        while (pluginDescriptorIterator.hasNext()) {
            final PluginDescriptor pluginDescriptor = (PluginDescriptor) pluginDescriptorIterator.next();
            
            LOGGER.info(pluginDescriptor.getId() + ": " + pluginDescriptor.getName());
            
            assertNotNull(PluginRegistry.getRegistry().getPluginDescriptor(pluginDescriptor.getId()));
            assertEquals(pluginDescriptor, PluginRegistry.getRegistry().getPluginDescriptor(pluginDescriptor.getId()));
        }
    }

    @Test
    @UseDataProvider("getPluginIds")
    @Ignore
    public void test040loadPlugin(final String pluginId) throws Exception {
        try {
            LOGGER.info("loadPlugin: " + pluginId);
            assertNotNull(PluginRegistry.getRegistry().getPluginDescriptor(pluginId));
            LOGGER.info("loading plugin '" + pluginId + "' from " + PluginRegistry.getRegistry().getPluginDescriptor(pluginId).getPluginPath());

            PluginRegistry.getRegistry().loadPlugin(pluginId);

            final PluginSupport plugin = PluginRegistry.getRegistry().getPlugin(pluginId);
            assertNotNull(plugin);

        } catch (final Exception ex) {
            LOGGER.fatal(ex.getMessage(), ex);
            throw ex;
        }
    }

    @DataProvider
    public final static String[] getPluginIds() throws Exception {
        return new String[]{"cismap"};
    }
}
