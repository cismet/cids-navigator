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
package Sirius.navigator.ui.actions;

import java.io.FileOutputStream;
import java.io.InputStream;

import de.cismet.cids.server.actions.PreparedAsyncByteAction;

import de.cismet.commons.security.WebDavClient;
import de.cismet.commons.security.WebDavHelper;

import de.cismet.netutil.ProxyHandler;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class PreparedAsyncDownloadHelper {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   result  DOCUMENT ME!
     * @param   out     DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public static void download(final PreparedAsyncByteAction result, final FileOutputStream out) throws Exception {
        download(result, out, null);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   result   DOCUMENT ME!
     * @param   out      DOCUMENT ME!
     * @param   monitor  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public static void download(final PreparedAsyncByteAction result,
            final FileOutputStream out,
            final PreparedAsyncDownloadMonitor monitor) throws Exception {
        final String server = result.getUrl();

        final WebDavHelper webdavHelper = new WebDavHelper();
        final WebDavClient webDavClient = new WebDavClient(ProxyHandler.getInstance().getProxy(), null, null);

        final InputStream iStream = webDavClient.getInputStream(server);
        final byte[] tmp = new byte[1024];
        long totalBytes = 0;
        int bytesRead;

        while ((bytesRead = iStream.read(tmp)) != -1) {
            out.write(tmp, 0, bytesRead);

            if (monitor != null) {
                totalBytes += bytesRead;
                monitor.progress(totalBytes);
            }
        }
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public abstract static class PreparedAsyncDownloadMonitor {

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @param  bytesRead  DOCUMENT ME!
         */
        public abstract void progress(long bytesRead);
    }
}
