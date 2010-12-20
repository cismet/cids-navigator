/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.tools;

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
 * Programmers          :       Steven Spencer, Pascal
 * Control browsers from your Java application
 * http://www.javaworld.com/javaworld/javatips/jw-javatip66.html
 *
 * Project                      :       WuNDA 2
 * Filename             :
 * Version                      :       1.2
 * Purpose                      :
 * Created                      :       20.07.2000
 * History                      :       24.07.2000, added support for Windows NT
 *
 *******************************************************************************/
import org.apache.log4j.Logger;

import java.applet.*;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ColorModel;

import java.io.*;

import java.net.URL;

import java.util.Enumeration;

/**
 * Ein 'gefakter' AppletContext. Wird BrowserControl ohne AppletContext initialisiert, kann trotzdem ueber die Methode
 * showDocument( ...) eine Webseite im Standardbrowser angezeigt werden.
 *
 * @version  $Revision$, $Date$
 */
public class BrowserControl implements AppletContext {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger logger = Logger.getLogger(BrowserControl.class);

    private static final BrowserControl control = new BrowserControl();

    // Used to identify the windows platform.
    private static final String WIN_NT_ID = "Windows NT"; // NOI18N
    // Used to identify the windows platform.
    private static final String WIN_XP_ID = "Windows XP"; // NOI18N
    // Used to identify the windows platform.
    private static final String WIN_9X_ID = "Windows"; // NOI18N
    // The default system browser under windows.
    private static final String WIN_PATH = "rundll32"; // NOI18N
    // The flag to display a url.
    private static final String WIN_FLAG = "url.dll,FileProtocolHandler"; // NOI18N
    // The default browser under unix.
    private static final String UNIX_PATH = "netscape"; // NOI18N
    // The flag to display a url.
    private static final String UNIX_FLAG = "-remote openURL"; // NOI18N

    //~ Instance fields --------------------------------------------------------

    private AppletContext appletContext = null;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new BrowserControl object.
     *
     * @param  appletContext  DOCUMENT ME!
     */
    public BrowserControl(final AppletContext appletContext) {
        this.appletContext = appletContext;
    }

    /**
     * Creates a new BrowserControl object.
     */
    private BrowserControl() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static final BrowserControl getControl() {
        return control;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  appletContext  DOCUMENT ME!
     */
    public void setAppletContext(final AppletContext appletContext) {
        this.appletContext = appletContext;
    }

    @Override
    public AudioClip getAudioClip(final URL url) {
        if (appletContext != null) {
            return appletContext.getAudioClip(url);
        } else {
            logger.error("method 'getAudioClip()' not supported");               // NOI18N
            throw new RuntimeException("method 'getAudioClip()' not supported"); // NOI18N
        }
    }

    @Override
    public Image getImage(final URL url) {
        if (appletContext != null) {
            return appletContext.getImage(url);
        } else {
            logger.error("method 'getImage' not supported");               // NOI18N
            throw new RuntimeException("method 'getImage' not supported"); // NOI18N
        }
    }

    @Override
    public Applet getApplet(final String name) {
        if (appletContext != null) {
            return appletContext.getApplet(name);
        } else {
            logger.error("method 'getApplet()' not supported");               // NOI18N
            throw new RuntimeException("method 'getApplet()' not supported"); // NOI18N
        }
    }

    @Override
    public Enumeration getApplets() {
        if (appletContext != null) {
            return appletContext.getApplets();
        } else {
            logger.error("method 'getApplets()' not supported");               // NOI18N
            throw new RuntimeException("method 'getApplets()' not supported"); // NOI18N
        }
    }

    @Override
    public void showDocument(final URL url) {
        // NavigatorLogger.printMessage("showDocument: " + url + " " + appletContext);

        if (appletContext != null) {
            appletContext.showDocument(url, "_blank"); // NOI18N
        } else {
            this.displayURL(url);
        }
    }

    @Override
    public void showDocument(final URL url, final String target) {
        if (appletContext != null) {
            appletContext.showDocument(url, target);
        } else {
            this.displayURL(url);
        }
    }

    @Override
    public void showStatus(final String status) {
        if (appletContext != null) {
            appletContext.showStatus(status);
        } else {
            logger.error("method 'showStatus()' not supported"); // NOI18N
        }
    }

    @Override
    public String toString() {
        if (appletContext != null) {
            return "AppletContext";                                       // NOI18N
        } else {
            return "BrowserControl for " + System.getProperty("os.name"); // NOI18N
        }
    }

    @Override
    public void setStream(final String s, final InputStream i) throws IOException {
        if (appletContext != null) {
            appletContext.setStream(s, i);
        } else {
            logger.error("method 'setStream()' not supported");          // NOI18N
            throw new IOException("method 'setStream()' not supported"); // NOI18N
        }
    }

    @Override
    public InputStream getStream(final String s) {
        if (appletContext != null) {
            return appletContext.getStream(s);
        } else {
            logger.error("method 'getStream()' not supported");               // NOI18N
            throw new RuntimeException("method 'hetStream()' not supported"); // NOI18N
        }
    }

    @Override
    public java.util.Iterator getStreamKeys() {
        if (appletContext != null) {
            return appletContext.getStreamKeys();
        } else {
            logger.error("method 'getStreamKeys()' not supported");               // NOI18N
            throw new RuntimeException("method 'getStreamKeys()' not supported"); // NOI18N
        }
    }

    /**
     * =========================================================================.
     *
     * @param  url  DOCUMENT ME!
     */
    public void displayURL(final URL url) {
        displayURL(url.toString());
    }

    /**
     * DOCUMENT ME!
     *
     * @param  url  DOCUMENT ME!
     */
    public void displayURL(final String url) {
        if (logger.isDebugEnabled()) {
            logger.debug("showing url '" + url + "' in browser"); // NOI18N
        }

        try {
            BrowserLauncher.openURL(url);
        } catch (Throwable t) {
            logger.error("could not open url (" + url + "):\n" + t.getMessage(), t); // NOI18N
        }
    }
    /*
     *  try {               // Windows NT, 2000, ... ???     if (os != null && (os.startsWith(WIN_NT_ID) ||
     * os.startsWith(WIN_XP_ID)))     {         cmd = "cmd.exe /c start " + url;         Process p =
     * Runtime.getRuntime().exec(cmd);     }     // Windows 9x     else if (os != null && os.startsWith(WIN_9X_ID)) { //
     * Funktioniert nicht unter NT !!!!!         // cmd = 'rundll32 url.dll,FileProtocolHandler http://...'  //cmd =
     * WIN_PATH + " " + WIN_FLAG + " " + url;         //NavigatorLogger.printMessage(cmd);   cmd = "start " + url;
     * Process p = Runtime.getRuntime().exec(cmd);     }     else     {         // Under Unix, Netscape has to be
     * running for the "-remote"         // command to work. So, we try sending the command and         // check for an
     * exit value. If the exit command is 0,         // it worked, otherwise we need to start the browser.          //
     * cmd = 'netscape -remote openURL(http://www.javaworld.com)'         cmd = UNIX_PATH + " " + UNIX_FLAG + "(" + url
     * + ")";         Process p = Runtime.getRuntime().exec(cmd);    try         {             // wait for exit code --
     * if it's 0, command worked,             // otherwise we need to start the browser up.             int exitCode =
     * p.waitFor();                          if (exitCode != 0)            {                 // Command failed, start up
     * the browser                                  // cmd = 'netscape http://www.javaworld.com'         cmd = UNIX_PATH
     * + " " + url;                 p = Runtime.getRuntime().exec(cmd);             }         }
     * catch(InterruptedException x)         {  logger.error("Error bringing up browser, cmd='" + cmd + "'", x); }     }
     * } catch(IOException x) {
     * // couldn't exec browser     logger.error("Could not invoke browser, command=" + cmd, x); }} */
}
