/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui;

import Sirius.navigator.ui.status.Status;

import calpa.html.CalCons;
import calpa.html.CalHTMLPane;
import calpa.html.CalHTMLPreferences;
import calpa.html.DefaultCalHTMLObserver;

import java.awt.EventQueue;

import java.net.URL;

/**
 * DOCUMENT ME!
 *
 * @author   jweintraut
 * @version  $Revision$, $Date$
 */
public class DescriptionPaneCalpa extends DescriptionPane {

    //~ Static fields/initializers ---------------------------------------------

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(DescriptionPaneFS.class);

    //~ Instance fields --------------------------------------------------------

    private final CalHTMLPreferences htmlPrefs = new CalHTMLPreferences();
    private CalHTMLPane htmlPane;
    DefaultCalHTMLObserver htmlObserver = new DefaultCalHTMLObserver() {

            @Override
            public void statusUpdate(final CalHTMLPane calHTMLPane,
                    final int status,
                    final URL uRL,
                    final int i0,
                    final String string) {
                super.statusUpdate(calHTMLPane, status, uRL, i0, string);
                if (status == 1) {
                    htmlPane.showHTMLDocument("");                                                               // NOI18N
                    statusChangeSupport.fireStatusChange(org.openide.util.NbBundle.getMessage(
                            DescriptionPaneCalpa.class,
                            "DescriptionPaneCalpa.statusUpdate(CalHTMLPane,int,URL,int,String).status.error"),   // NOI18N
                        Status.MESSAGE_POSITION_3,
                        Status.ICON_DEACTIVATED,
                        Status.ICON_ACTIVATED);
                } else if ((status == 10) || (status == 11)) {
                    statusChangeSupport.fireStatusChange(org.openide.util.NbBundle.getMessage(
                            DescriptionPaneCalpa.class,
                            "DescriptionPaneCalpa.statusUpdate(CalHTMLPane,int,URL,int,String).status.loading"), // NOI18N
                        Status.MESSAGE_POSITION_3,
                        Status.ICON_BLINKING,
                        Status.ICON_DEACTIVATED);
                } else if (status == 14) {
                    statusChangeSupport.fireStatusChange(org.openide.util.NbBundle.getMessage(
                            DescriptionPaneCalpa.class,
                            "DescriptionPaneCalpa.statusUpdate(CalHTMLPane,int,URL,int,String).status.loaded"),  // NOI18N
                        Status.MESSAGE_POSITION_3,
                        Status.ICON_ACTIVATED,
                        Status.ICON_DEACTIVATED);
                }
            }

            @Override
            public void linkActivatedUpdate(final CalHTMLPane calHTMLPane,
                    final URL uRL,
                    final String string,
                    final String string0) {
                super.linkActivatedUpdate(calHTMLPane, uRL, string, string0);
            }

            @Override
            public void linkFocusedUpdate(final CalHTMLPane calHTMLPane, final URL uRL) {
                super.linkFocusedUpdate(calHTMLPane, uRL);
            }
        };

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form DescriptionPane.
     */
    public DescriptionPaneCalpa() {
        super();

        htmlPrefs.setAutomaticallyFollowHyperlinks(true);
        htmlPrefs.setOptimizeDisplay(CalCons.OPTIMIZE_ALL);
        htmlPrefs.setDisplayErrorDialogs(false);
        htmlPrefs.setLoadImages(true);

        initComponents();

        showHTML();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    private void initComponents() {
        htmlPane = new CalHTMLPane(htmlPrefs, htmlObserver, "cismap");

        htmlPane.setDoubleBuffered(true);

        add(htmlPane, "html");
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void clear() {
        final Runnable clearRunnable = new Runnable() {

                @Override
                public void run() {
                    htmlPane.showHTMLDocument("");
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
                    DescriptionPaneCalpa.class,
                    "DescriptionPaneCalpa.setPageFromURI(String).info",
                    page));
        }

        try {
            if ((page == null) || (page.trim().length() <= 0)) {
                setPageFromContent(blankPage);
            } else {
                htmlPane.stopAll();
                htmlPane.showHTMLDocument(new URL(page));
            }
        } catch (Exception e) {
            LOG.error(org.openide.util.NbBundle.getMessage(
                    DescriptionPaneCalpa.class,
                    "DescriptionPaneCalpa.setPageFromURI(String).error",
                    page),
                e); // NOI18N

            htmlPane.showHTMLDocument(""); // NOI18N

            statusChangeSupport.fireStatusChange(
                org.openide.util.NbBundle.getMessage(
                    DescriptionPaneCalpa.class,
                    "DescriptionPaneCalpa.setPageFromURI(String).error",
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
            htmlPane.showHTMLDocument(markup);
        } catch (Exception e) {
            LOG.error(org.openide.util.NbBundle.getMessage(
                    DescriptionPaneCalpa.class,
                    "DescriptionPaneCalpa.setPageFromContent(String).error"),
                e); // NOI18N

            statusChangeSupport.fireStatusChange(
                org.openide.util.NbBundle.getMessage(
                    DescriptionPaneCalpa.class,
                    "DescriptionPaneCalpa.setPageFromContent(String).error"), // NOI18N
                Status.MESSAGE_POSITION_3,
                Status.ICON_DEACTIVATED,
                Status.ICON_ACTIVATED);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  markup   renderer DOCUMENT ME!
     * @param  baseURL  DOCUMENT ME!
     */

    @Override
    public void setPageFromContent(final String markup, final String baseURL) {
        setPageFromContent(markup);
    }
}
