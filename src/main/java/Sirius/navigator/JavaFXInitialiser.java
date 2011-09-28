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
package Sirius.navigator;

import javafx.application.Application;

import javafx.stage.Stage;

import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * DOCUMENT ME!
 *
 * @author   jweintraut
 * @version  $Revision$, $Date$
 */
public class JavaFXInitialiser {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(JavaFXInitialiser.class);

    private static final String NB_MAIN_CLASS = "de.cismet.cids.navigator.starter.NavigatorStarter";

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  args  DOCUMENT ME!
     */
    public static void main(final String[] args) {
        // Load dlls
        /*LOG.fatal("Loading dlls ...");
         * System.err.println("Loading dlls ...");
         *
         * System.loadLibrary("decora-d3d"); System.loadLibrary("decora-sse"); System.loadLibrary("fxplugins");
         * System.loadLibrary("glib-lite"); System.loadLibrary("gstreamer-lite"); System.loadLibrary("javafx-font");
         * System.loadLibrary("javafx-iio"); System.loadLibrary("jfxmedia"); System.loadLibrary("libxml2");
         * System.loadLibrary("mat"); System.loadLibrary("msvcr100");
         * System.loadLibrary("prism-d3d");System.loadLibrary("WebPaneJava");*/

        // Start JavaFX 2.0
        LOG.fatal("Launch JavaFX ...");
        System.err.println("Launch JavaFX ...");

        final long ms = System.currentTimeMillis();
        // Application.launch(JavaFXInitialiser.class, args); // This is the main start up for JavaFX 2.0
        TmpFxLauncher.launch();
        LOG.fatal("Launched Java FX in " + (System.currentTimeMillis() - ms) + "ms.");
        System.err.println("Launched Java FX in " + (System.currentTimeMillis() - ms) + "ms.");

        // Hand control back to NetBeans
        final ClassLoader classloader = Thread.currentThread().getContextClassLoader();

        try {
            final Class<?> mainClass = Class.forName(NB_MAIN_CLASS, true, classloader);

            final Object mainObject = mainClass.newInstance();
            final Method mainMethod = mainClass.getDeclaredMethod("main", new Class[] { String[].class });
            mainMethod.invoke(mainObject, (Object)args);
        } catch (IllegalAccessException ex) {
            LOG.error(ex);
            ex.printStackTrace();
        } catch (IllegalArgumentException ex) {
            LOG.error(ex);
            ex.printStackTrace();
        } catch (InvocationTargetException ex) {
            LOG.error(ex);
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            LOG.error(ex);
            ex.printStackTrace();
        } catch (InstantiationException ex) {
        } catch (SecurityException ex) {
            LOG.error(ex);
            ex.printStackTrace();
        } catch (NoSuchMethodException ex) {
            LOG.error(ex);
            ex.printStackTrace();
        }

        LOG.fatal("NetBeans started.");
        System.err.println("NetBeans started.");
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * NOTE: this method of launching the JavaFX runtime is temporary. It will become unnecessary in a subsequent beta
     * update.
     *
     * @version  $Revision$, $Date$
     */
    public static class TmpFxLauncher extends Application {

        //~ Methods ------------------------------------------------------------

        @Override
        public void start(final Stage primaryStage) {
        }

        /**
         * DOCUMENT ME!
         */
        private static void launch() {
            Application.launch(null);
        }
    }
}
