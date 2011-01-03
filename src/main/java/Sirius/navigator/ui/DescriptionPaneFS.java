/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui;

import Sirius.navigator.ui.status.Status;

import org.xhtmlrenderer.simple.extend.XhtmlNamespaceHandler;
import org.xhtmlrenderer.swing.FSMouseListener;
import org.xhtmlrenderer.swing.LinkListener;

import java.awt.EventQueue;

import java.util.Iterator;
import java.util.List;

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

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form DescriptionPane.
     */
    public DescriptionPaneFS() {
        super();

        System.setProperty("xr.load.xml-reader", "org.ccil.cowan.tagsoup.Parser");
        System.setProperty("xr.load.string-interning", "true");

        initComponents();

        final List listeners = xHTMLPanel1.getMouseTrackingListeners();
        final Iterator iter = listeners.iterator();
        while (iter.hasNext()) {
            final Object listener = iter.next();
            if (listener instanceof LinkListener) {
                xHTMLPanel1.removeMouseTrackingListener((FSMouseListener)listener);
            }
        }

        xHTMLPanel1.addMouseTrackingListener(new NativeBrowserLinkListener());

        xHTMLPanel1.getSharedContext().setUserAgentCallback(new WebAccessManagerUserAgent());

        showHTML();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Initialisation method of Matisse. Initializes visual components added by this subclass.
     */
    private void initComponents() {
        fSScrollPane1 = new org.xhtmlrenderer.simple.FSScrollPane();
        xHTMLPanel1 = new org.xhtmlrenderer.simple.XHTMLPanel();

        fSScrollPane1.setViewportView(xHTMLPanel1);

        add(fSScrollPane1, "html");
    }

    /**
     * Show the blank page.
     */
    @Override
    public void clear() {
        final Runnable clearRunnable = new Runnable() {

                @Override
                public void run() {
                    setPageFromContent(
                        blankPage,
                        getClass().getClassLoader().getResource("Sirius/navigator/resource/doc/blank.xhtml")
                                    .toString());
                    removeAndDisposeAllRendererFromPanel();
                    repaint();
                }
            };

        if (!EventQueue.isDispatchThread()) {
            EventQueue.invokeLater(clearRunnable);
        } else {
            clearRunnable.run();
        }
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
                setPageFromContent(
                    blankPage,
                    getClass().getClassLoader().getResource("Sirius/navigator/resource/doc/blank.xhtml").toString());
            } else {
                xHTMLPanel1.setDocument(page);
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
        try {
            xHTMLPanel1.setDocumentFromString(markup, baseURL, new XhtmlNamespaceHandler());
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
