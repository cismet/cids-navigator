/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui;

import org.xhtmlrenderer.simple.XHTMLPanel;

import de.cismet.tools.BrowserLauncher;

/**
 * The FSXHtmlPanel is a panel based on the flying saucer XHTML. it is used to detect clicks on links in a html
 * document. If a link is clicked, this panel decides it the link represents a html or an other document. In case it is
 * no HTML document, the BrowserLauncher is used to show the link document.
 *
 * @author   daniel
 * @version  $Revision$, $Date$
 */
public class FSXHtmlPanel extends XHTMLPanel {

    //~ Methods ----------------------------------------------------------------

    @Override
    protected void setDocumentRelative(final String filename) {
        if (!filename.endsWith("html")) {
            final String currUrl = this.getURL().toString();
            final String baseUrl = currUrl.substring(0, currUrl.lastIndexOf("/") + 1);
            BrowserLauncher.openURLorFile(baseUrl + filename);
        } else {
            super.setDocumentRelative(filename); // To change body of generated methods, choose Tools | Templates.
        }
    }
}
