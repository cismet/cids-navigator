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
package de.cismet.cids.custom.widgets.sirius;

import Sirius.navigator.ui.DashBoardWidget;

import calpa.html.CalCons;
import calpa.html.CalHTMLPane;
import calpa.html.CalHTMLPreferences;
import calpa.html.DefaultCalHTMLObserver;

import org.apache.log4j.Logger;

import org.openide.util.Exceptions;
import org.openide.util.lookup.ServiceProvider;

import org.xhtmlrenderer.simple.FSScrollPane;
import org.xhtmlrenderer.simple.XHTMLPanel;

import java.awt.*;

import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.net.ssl.*;

import javax.swing.*;

import de.cismet.tools.gui.xhtmlrenderer.WebAccessManagerUserAgent;

/**
 * DOCUMENT ME!
 *
 * @author   bfriedrich
 * @version  $Revision$, $Date$
 */
@ServiceProvider(service = DashBoardWidget.class)
public class AdminMessagesWidget extends javax.swing.JPanel implements DashBoardWidget {

    //~ Static fields/initializers ---------------------------------------------

    private static final String ADMIN_MSG_URL = "file:///home/bfriedrich/Dropbox/Public/admin_msg_test/messages.html";

    private static final Logger LOG = Logger.getLogger(AdminMessagesWidget.class);

    //~ Instance fields --------------------------------------------------------

// private org.xhtmlrenderer.simple.XHTMLPanel xHTMLPanel1;
    private final CalHTMLPane htmlPane;

    private final URL url;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnlContent;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form AdminNotifications.
     *
     * @throws  RuntimeException  DOCUMENT ME!
     */
    public AdminMessagesWidget() {
        initComponents();

        try {
            this.url = new URL(ADMIN_MSG_URL);
        } catch (MalformedURLException ex) {
            Exceptions.printStackTrace(ex);
            throw new RuntimeException(ex);
        }

        final CalHTMLPreferences htmlPrefs = new CalHTMLPreferences();
        htmlPrefs.setAutomaticallyFollowHyperlinks(false);
        htmlPrefs.setOptimizeDisplay(CalCons.OPTIMIZE_ALL);
        htmlPrefs.setDisplayErrorDialogs(false);
        htmlPrefs.setLoadImages(true);

        final DefaultCalHTMLObserver obs = new DefaultCalHTMLObserver() {

                @Override
                public void statusUpdate(final CalHTMLPane calHTMLPane,
                        final int status,
                        final URL uRL,
                        final int i0,
                        final String string) {
                    super.statusUpdate(calHTMLPane, status, uRL, i0, string);
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("STATUS: " + status + " url: " + url + "i0: " + i0 + " string: " + string);
                    }
                }

                @Override
                public void linkActivatedUpdate(final CalHTMLPane chtmlp,
                        final URL url,
                        final String string,
                        final String string1) {
                    super.linkActivatedUpdate(chtmlp, url, string, string1);

                    final Desktop desktop = Desktop.getDesktop();

                    if (desktop.isSupported(Desktop.Action.BROWSE)) {
                        try {
                            desktop.browse(url.toURI());
                        } catch (final Exception ex) {
                            LOG.error("an error occurred while opening url " + url, ex);
                        }
                    } else {
                        LOG.error("Opening browser is not supported -> url " + url + " can not be opened");
                    }
                }
            };

        this.htmlPane = new CalHTMLPane(htmlPrefs, obs, null);
        this.htmlPane.setDoubleBuffered(true);

        this.htmlPane.setScrollBarPolicy(CalCons.V_AUTO);

        this.pnlContent.add(this.htmlPane, BorderLayout.CENTER);
//
//        this.xHTMLPanel1 = new XHTMLPanel();
//        xHTMLPanel1.getSharedContext().setUserAgentCallback(new WebAccessManagerUserAgent());
//
//        final FSScrollPane fSScrollPane1 = new org.xhtmlrenderer.simple.FSScrollPane();
//        fSScrollPane1.setViewportView(xHTMLPanel1);
//
//        this.pnlContent.add(fSScrollPane1, BorderLayout.CENTER);
//
//        System.setProperty("xr.load.xml-reader", "org.ccil.cowan.tagsoup.Parser");
//        System.setProperty("xr.load.string-interning", "true");
//        System.setProperty("xr.use.listeners", "true");
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lblTitle = new javax.swing.JLabel();
        pnlContent = new javax.swing.JPanel();

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        setMaximumSize(new java.awt.Dimension(500, 120));
        setOpaque(false);
        setLayout(new java.awt.GridBagLayout());

        lblTitle.setFont(new java.awt.Font("DejaVu Sans", 1, 18)); // NOI18N
        lblTitle.setForeground(java.awt.Color.white);
        lblTitle.setText(org.openide.util.NbBundle.getMessage(
                AdminMessagesWidget.class,
                "AdminMessagesWidget.lblTitle.text"));             // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(lblTitle, gridBagConstraints);

        pnlContent.setMinimumSize(new java.awt.Dimension(200, 100));
        pnlContent.setOpaque(false);
        pnlContent.setPreferredSize(new java.awt.Dimension(200, 100));
        pnlContent.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        add(pnlContent, gridBagConstraints);
    } // </editor-fold>//GEN-END:initComponents

    @Override
    public void init() {
        SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
//                        xHTMLPanel1.setDocument(url.toURI().toString());
                    htmlPane.showHTMLDocument(url, null, true);
                }
            });
    }

    @Override
    public void refresh() {
        SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    htmlPane.reloadDocument();
                }
            });
    }

    @Override
    public Component getWidget() {
        return this;
    }

    @Override
    public boolean isHeaderWidget() {
        return false;
    }

    @Override
    public int getX() {
        return 0;
    }

    @Override
    public int getY() {
        return 0;
    }

    @Override
    public void dispose() {
    }
}
