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
package Sirius.navigator.tools;

import org.apache.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.MalformedURLException;
import java.net.URL;

import de.cismet.security.WebAccessManager;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class StaticNavigatorTools {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(StaticNavigatorTools.class);

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   from  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  MalformedURLException  DOCUMENT ME!
     * @throws  IOException            DOCUMENT ME!
     */
    public static InputStream getInputStreamFromFileOrUrl(final String from) throws MalformedURLException, IOException {
        if ((from.indexOf("http://") == 0) || (from.indexOf("https://") == 0)
                    || (from.indexOf("file:/") == 0)) {
            final URL url = new URL(from);
            try {
                return WebAccessManager.getInstance().doRequest(url);
            } catch (Exception e) {
                LOG.error("Cannot use the WebAccessManager to retrieve an input stream from " + from, e);
                return url.openStream();
            }
        } else {
            return new BufferedInputStream(new FileInputStream(from));
        }
    }
}
