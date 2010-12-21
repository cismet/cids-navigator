/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 *  Copyright (C) 2010 jweintraut
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package Sirius.navigator.ui;

import org.xhtmlrenderer.resource.XMLResource;
import org.xhtmlrenderer.swing.NaiveUserAgent;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringBufferInputStream;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.cismet.security.WebAccessManager;

import de.cismet.security.exceptions.AccessMethodIsNotSupportedException;
import de.cismet.security.exceptions.MissingArgumentException;
import de.cismet.security.exceptions.NoHandlerForURLException;
import de.cismet.security.exceptions.RequestFailedException;

/**
 * DOCUMENT ME!
 *
 * @author   jweintraut
 * @version  $Revision$, $Date$
 */
public class WebAccessManagerUserAgent extends NaiveUserAgent {

    //~ Static fields/initializers ---------------------------------------------

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(
            WebAccessManagerUserAgent.class);

    //~ Instance fields --------------------------------------------------------

    private Pattern encodingPattern = Pattern.compile("(encoding|charset)=\"?(.*?)[;\" ]");
    private Pattern windowsEncodingPattern = Pattern.compile("windows-(\\d{4})");

    //~ Methods ----------------------------------------------------------------

    @Override
    protected InputStream resolveAndOpenStream(final String uri) {
        InputStream result = null;

        if ((uri != null) && (uri.trim().length() > 0)) {
            if (uri.startsWith("jar") || uri.startsWith("file")) {
                try {
                    result = new URL(uri).openStream();
                } catch (MalformedURLException ex) {
                    LOG.error("Can't load from URI '" + uri + "' since the resulting URL is malformed.", ex); // NOI18N
                } catch (IOException ex) {
                    LOG.error("Can't load from URI '" + uri + "'.", ex);                                      // NOI18N
                }
            } else {
                try {
                    result = WebAccessManager.getInstance().doRequest(new URL(uri));
                } catch (URISyntaxException ex) {
                    LOG.error("Can't load from URI '" + uri + "' since its syntax is broken.", ex);           // NOI18N
                } catch (MissingArgumentException ex) {
                    LOG.error("Can't load from URI '" + uri + "' since it couldn't be converted to a URL.", ex); // NOI18N
                } catch (AccessMethodIsNotSupportedException ex) {
                    LOG.error("Can't load from URI '" + uri + "' since the access method isn't supported.", ex); // NOI18N
                } catch (RequestFailedException ex) {
                    LOG.error("The request to load URI '" + uri + "' failed.", ex);                           // NOI18N
                } catch (NoHandlerForURLException ex) {
                    LOG.error("Can't load from URI '" + uri + "' since there is no matching handler.", ex);   // NOI18N
                } catch (Exception ex) {
                    LOG.error("Can't load from URI '" + uri + "' since an unexcpected exception occurred.", ex); // NOI18N
                }
            }
        }

        return result;
    }

    @Override
    public XMLResource getXMLResource(final String uri) {
        final InputStream inputStream = resolveAndOpenEncodedStream(uri);
        final XMLResource xmlResource;
        try {
            xmlResource = XMLResource.load(inputStream);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // swallow
                }
            }
        }
        return xmlResource;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   uri  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private InputStream resolveAndOpenEncodedStream(final String uri) {
        InputStream result = null;
        String encoding = null;

        final BufferedReader reader = new BufferedReader(new InputStreamReader(resolveAndOpenStream(uri)));
        Matcher matcher = null;
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                matcher = encodingPattern.matcher(line);
                if (matcher.find()) {
                    encoding = matcher.group(2);
                    break;
                }
            }
        } catch (IOException ex) {
            LOG.warn("Couldn't determine encoding of resource: " + uri, ex); // NOI18N
        }

        matcher = windowsEncodingPattern.matcher(encoding);
        if (matcher.find()) {
            encoding = "Cp" + matcher.group(1);                                  // NOI18N
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Encoding resource '" + uri + "' in '" + encoding + "'."); // NOI18N
        }

        result = resolveAndOpenStream(uri);
        final ByteArrayOutputStream bout = new ByteArrayOutputStream();
        final byte[] buffer = new byte[1024];
        int bufferUsed;

        try {
            while ((bufferUsed = result.read(buffer)) > 0) {
                bout.write(buffer, 0, bufferUsed);
            }

            result.close();

            result = new StringBufferInputStream(new String(bout.toByteArray(), encoding));
            bout.close();
        } catch (IOException ex) {
            LOG.error("Couldn't encode resource '" + uri + "' in '" + encoding + "'.", ex); // NOI18N
        }

        return result;
    }
}
