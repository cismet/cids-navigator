/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui;

import Sirius.navigator.ui.status.Status;

import org.xhtmlrenderer.extend.NamespaceHandler;
import org.xhtmlrenderer.simple.extend.XhtmlNamespaceHandler;
import org.xhtmlrenderer.swing.BasicPanel;
import org.xhtmlrenderer.swing.LinkListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import de.cismet.tools.gui.xhtmlrenderer.WebAccessManagerUserAgent;

/**
 * An implementation of DescriptionPane which uses Flying Saucer to render XHTML content. The retrieval of XHTML
 * documents is done by WebAccessManager. Therefore FLying Saucer is configured to use WebAccessUserAgent which acts as
 * an adapter for WebAccessManager. In order to read invalid XHTML documents or HTML documents, Tagsoup is used as
 * XMLReader.
 *
 * @version  $Revision$, $Date$
 */
public class DescriptionPaneFS extends DescriptionPane {

    //~ Static fields/initializers ---------------------------------------------

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(DescriptionPaneFS.class);

    //~ Instance fields --------------------------------------------------------

    private org.xhtmlrenderer.simple.FSScrollPane fSScrollPane1;
    private org.xhtmlrenderer.simple.XHTMLPanel xHTMLPanel1;
    private JPopupMenu popupMenu;
    private JMenuItem mnuItem_openInExternalBrowser;
    private String pageURI;

    private NamespaceHandler namespaceHandler;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form DescriptionPane.
     */
    public DescriptionPaneFS() {
        super();

        System.setProperty("xr.load.xml-reader", "org.ccil.cowan.tagsoup.Parser");
        System.setProperty("xr.load.string-interning", "true");
        System.setProperty("xr.use.listeners", "true");

        initComponents();

        namespaceHandler = new XhtmlNamespaceHandler();
        xHTMLPanel1.getSharedContext().setUserAgentCallback(new WebAccessManagerUserAgent());
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Initialisation method of Matisse. Initializes visual components added by this subclass.
     */
    private void initComponents() {
        fSScrollPane1 = new org.xhtmlrenderer.simple.FSScrollPane();
        xHTMLPanel1 = new org.xhtmlrenderer.simple.XHTMLPanel();
        popupMenu = new JPopupMenu();
        final LinkListener ll = new LinkListener() {

                @Override
                public void linkClicked(final BasicPanel panel, final String uri) {
                    if (!pageURI.endsWith("html")) {
                        pageURI = panel.getURL().toString();
                    }
                }
            };
        ((BasicPanel)xHTMLPanel1).addMouseTrackingListener(ll);

        fSScrollPane1.setViewportView(xHTMLPanel1);
        mnuItem_openInExternalBrowser = new JMenuItem(org.openide.util.NbBundle.getMessage(
                    DescriptionPaneFS.class,
                    "DescriptionPaneFS.btn_openInSystemBrowser.text"),
                new javax.swing.ImageIcon(
                    getClass().getResource("/Sirius/navigator/ui/world.png")));
        mnuItem_openInExternalBrowser.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(final ActionEvent e) {
                    if ((pageURI != null) && (pageURI.trim().length() > 0)) {
                        try {
                            de.cismet.tools.BrowserLauncher.openURL(pageURI);
                        } catch (Exception ex) {
                            LOG.error("Couldn't open URI '" + pageURI + "' in external browser.", ex);
                        }
                    }
                }
            });
        popupMenu.add(mnuItem_openInExternalBrowser);

        xHTMLPanel1.add(popupMenu);
        xHTMLPanel1.addMouseListener(new PopupListener(popupMenu));

        add(fSScrollPane1, "html");
    }

    /**
     * Loads the given URI and renders the referenced XHTML document. Shows an error page if loading causes an error.
     *
     * @param  page  An URI to an XHTML document.
     */
    @Override
    public void setPageFromURI(final String page) {
        if (LOG.isInfoEnabled()) {
            LOG.info(org.openide.util.NbBundle.getMessage(
                    DescriptionPaneFS.class,
                    "DescriptionPaneFS.setPageFromURI(String).info",
                    page));
        }

        try {
            if ((page == null) || (page.trim().length() <= 0)) {
                startNoDescriptionRenderer();
            } else {
                pageURI = page;
                xHTMLPanel1.setDocument(page);
                mnuItem_openInExternalBrowser.setEnabled(true);
            }
        } catch (Exception e) {
            LOG.error(org.openide.util.NbBundle.getMessage(
                    DescriptionPaneFS.class,
                    "DescriptionPaneFS.setPageFromURI(String).error",
                    page),
                e); // NOI18N

            setPageFromContent(
                errorPage,
                getClass().getClassLoader().getResource("Sirius/navigator/resource/doc/blank.xhtml").toString());

            statusChangeSupport.fireStatusChange(
                org.openide.util.NbBundle.getMessage(
                    DescriptionPaneFS.class,
                    "DescriptionPaneFS.setPageFromURI(String).error",
                    page), // NOI18N
                Status.MESSAGE_POSITION_3,
                Status.ICON_DEACTIVATED,
                Status.ICON_ACTIVATED);
        }
    }

    /**
     * Renders the given markup.
     *
     * @param  markup  XHTML markup to render.
     */
    @Override
    public void setPageFromContent(final String markup) {
        setPageFromContent(markup, "");
    }

    /**
     * Renders the given markup using baseURL as base for relative paths in the markup.
     *
     * @param  markup   XHTML markup to render
     * @param  baseURL  Base path for relative paths used in markup
     */
    @Override
    public void setPageFromContent(final String markup, final String baseURL) {
        pageURI = null;
        mnuItem_openInExternalBrowser.setEnabled(false);

        try {
            xHTMLPanel1.setDocumentFromString(markup, baseURL, namespaceHandler);
        } catch (Exception e) {
            LOG.error(org.openide.util.NbBundle.getMessage(
                    DescriptionPaneFS.class,
                    "DescriptionPaneFS.setPageFromContent(String).error"),
                e); // NOI18N

            statusChangeSupport.fireStatusChange(
                org.openide.util.NbBundle.getMessage(
                    DescriptionPaneFS.class,
                    "DescriptionPaneFS.setPageFromContent(String).error"), // NOI18N
                Status.MESSAGE_POSITION_3,
                Status.ICON_DEACTIVATED,
                Status.ICON_ACTIVATED);
        }
    }
}
