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

import java.awt.EventQueue;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten.hell@cismet.de
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

        initComponents();

        xHTMLPanel1.getSharedContext().setUserAgentCallback(new WebAccessManagerUserAgent());

        showHTML();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    private void initComponents() {
        fSScrollPane1 = new org.xhtmlrenderer.simple.FSScrollPane();
        xHTMLPanel1 = new org.xhtmlrenderer.simple.XHTMLPanel();

        fSScrollPane1.setViewportView(xHTMLPanel1);

        add(fSScrollPane1, "html");
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void clear() {
        final Runnable clearRunnable = new Runnable() {

                @Override
                public void run() {
                    xHTMLPanel1.setDocumentFromString(blankPage, "", new XhtmlNamespaceHandler());
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
     * DOCUMENT ME!
     *
     * @param  page  DOCUMENT ME!
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
                setPageFromContent(blankPage);
            } else {
                xHTMLPanel1.setDocument(page);
            }
        } catch (Exception e) {
            LOG.error(org.openide.util.NbBundle.getMessage(
                    DescriptionPaneFS.class,
                    "DescriptionPaneFS.setPageFromURI(String).error",
                    page),
                e); // NOI18N

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
     * DOCUMENT ME!
     *
     * @param  markup  renderer DOCUMENT ME!
     */

    @Override
    public void setPageFromContent(final String markup) {
        try {
            xHTMLPanel1.setDocumentFromString(markup, "", new XhtmlNamespaceHandler());
        } catch (Exception e) {
            LOG.error(org.openide.util.NbBundle.getMessage(
                    DescriptionPaneFS.class,
                    "DescriptionPaneFS.setPageFromContent(String).error",
                    markup),
                e); // NOI18N

            statusChangeSupport.fireStatusChange(
                org.openide.util.NbBundle.getMessage(
                    DescriptionPaneFS.class,
                    "DescriptionPaneFS.setPageFromContent(String).error",
                    markup), // NOI18N
                Status.MESSAGE_POSITION_3,
                Status.ICON_DEACTIVATED,
                Status.ICON_ACTIVATED);
        }
    }
}
