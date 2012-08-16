/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.custom.objectrenderer.sirius;

import Sirius.navigator.ui.RequestsFullSizeComponent;

import org.openide.util.Lookup;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import javax.swing.*;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.cismet.cids.dynamics.CidsBean;

import de.cismet.cids.editors.CidsObjectEditorFactory;

import de.cismet.cids.tools.metaobjectrenderer.CidsBeanRenderer;
import de.cismet.cids.tools.metaobjectrenderer.SelfDisposingPanel;

import de.cismet.tools.gui.ComponentWrapper;
import de.cismet.tools.gui.FooterComponentProvider;
import de.cismet.tools.gui.TitleComponentProvider;
import de.cismet.tools.gui.WrappedComponent;

/**
 * DOCUMENT ME!
 *
 * @author   bfriedrich
 * @version  $Revision$, $Date$
 */
public class NoDescriptionRenderer extends javax.swing.JPanel implements CidsBeanRenderer,
    RequestsFullSizeComponent,
    TitleComponentProvider,
    FooterComponentProvider {

    //~ Static fields/initializers ---------------------------------------------

    private static final NoDescriptionRenderer INSTANCE = new NoDescriptionRenderer();

    //~ Instance fields --------------------------------------------------------

    private CidsBean cidsBean;
    private String title;

    private int numWidgets;

    private final ArrayList<NoDescriptionWidget> widgets;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.JLabel lblFooter;
    private javax.swing.JLabel lblHeader;
    private javax.swing.JLabel lblInfo;
    private javax.swing.JPanel pnlCenter;
    private javax.swing.JPanel pnlContent;
    private javax.swing.JPanel pnlFooter;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlHeaderWidget;
    private javax.swing.JPanel pnlLeft;
    private javax.swing.JPanel pnlRight;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form NoDescriptionRenderer.
     */
    private NoDescriptionRenderer() {
        initComponents();

        this.widgets = new ArrayList<NoDescriptionWidget>();
        this.lookup();

        JPanel p;
        for (int i = this.numWidgets; i < 9; i++) {
            p = new JPanel();
//            p.setOpaque(false);
            p.setMinimumSize(new Dimension(100, 100));
            this.addWidget(p, i % 3, (i - 1) % 3); // TODO: implement relative positioning
        }

        super.addHierarchyListener(new HierarchyListener() {

                @Override
                public void hierarchyChanged(final HierarchyEvent e) {
                    if (((HierarchyEvent.SHOWING_CHANGED & e.getChangeFlags()) != 0)
                                && NoDescriptionRenderer.super.isShowing()) {
                        SwingUtilities.invokeLater(new Runnable() {

                                @Override
                                public void run() {
                                    for (final NoDescriptionWidget w : widgets) {
                                        w.refresh();
                                    }
                                }
                            });
                    }
                }
            });
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    private void lookup() {
        final Lookup lookUp = Lookup.getDefault();
        final Collection<? extends NoDescriptionWidget> widgets = lookUp.lookupAll(NoDescriptionWidget.class);

        int numWidgets = 0;

        Component widget;
        for (final NoDescriptionWidget w : widgets) {
            w.init();

            widget = w.getWidget();

            if (w.isHeaderWidget()) {
                this.addHeaderWidget(widget);
            } else {
                this.addWidget(widget, w.getX(), w.getY());
            }

            this.widgets.add(w);
            numWidgets++;

            if (numWidgets == 9) {
                System.err.println("max 9 widgets accepted -> remaining widgets are ignored");
                break;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static NoDescriptionRenderer getInstance() {
        return INSTANCE;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  c  DOCUMENT ME!
     */
    public void addHeaderWidget(final Component c) {
        this.pnlHeaderWidget.add(c);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  widget  DOCUMENT ME!
     * @param  x       DOCUMENT ME!
     * @param  y       DOCUMENT ME!
     */
    public void addWidget(final Component widget, final int x, final int y) {
        JPanel targetPanel = null;
        switch (x) {
            case 0: {
                targetPanel = this.pnlLeft;
                break;
            }
            case 1: {
                targetPanel = this.pnlCenter;
                break;
            }
            case 2: {
                targetPanel = this.pnlRight;
                break;
            }
            default: {
                // TODO: introduce logging
                System.err.println("UNKNOWN PANEL: " + x);
                this.addWidget(widget, x % 3, y);
            }
        }

//        final ComponentWrapper cw = CidsObjectEditorFactory.getInstance().getComponentWrapper();

        if (targetPanel != null) {
//            targetPanel.add((JComponent)cw.wrapComponent((JComponent)widget));
            targetPanel.add(widget);
            targetPanel.add(Box.createVerticalStrut(8));

            this.numWidgets++;

            targetPanel.revalidate();
            this.revalidate();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlHeader = new javax.swing.JPanel();
        lblHeader = new javax.swing.JLabel();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0),
                new java.awt.Dimension(0, 0),
                new java.awt.Dimension(32767, 0));
        pnlHeaderWidget = new javax.swing.JPanel();
        lblInfo = new javax.swing.JLabel();
        pnlFooter = new javax.swing.JPanel();
        lblFooter = new javax.swing.JLabel();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0),
                new java.awt.Dimension(0, 0),
                new java.awt.Dimension(32767, 0));
        pnlContent = new javax.swing.JPanel();
        pnlLeft = new javax.swing.JPanel();
        pnlCenter = new javax.swing.JPanel();
        pnlRight = new javax.swing.JPanel();

        pnlHeader.setOpaque(false);
        pnlHeader.setLayout(new java.awt.GridBagLayout());

        lblHeader.setFont(new java.awt.Font("DejaVu Sans", 1, 18)); // NOI18N
        lblHeader.setForeground(java.awt.Color.white);
        lblHeader.setText(org.openide.util.NbBundle.getMessage(
                NoDescriptionRenderer.class,
                "NoDescriptionRenderer.lblHeader.text"));           // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 6);
        pnlHeader.add(lblHeader, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0.2;
        pnlHeader.add(filler2, gridBagConstraints);

        pnlHeaderWidget.setOpaque(false);
        pnlHeaderWidget.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(0, 14, 0, 0);
        pnlHeader.add(pnlHeaderWidget, gridBagConstraints);

        lblInfo.setFont(new java.awt.Font("DejaVu Sans", 0, 12)); // NOI18N
        lblInfo.setForeground(java.awt.Color.white);
        lblInfo.setText(org.openide.util.NbBundle.getMessage(
                NoDescriptionRenderer.class,
                "NoDescriptionRenderer.lblInfo.text"));           // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 6);
        pnlHeader.add(lblInfo, gridBagConstraints);

        pnlFooter.setOpaque(false);
        pnlFooter.setLayout(new java.awt.GridBagLayout());

        lblFooter.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lblFooter.setForeground(java.awt.Color.white);
        lblFooter.setText(org.openide.util.NbBundle.getMessage(
                NoDescriptionRenderer.class,
                "NoDescriptionRenderer.lblFooter.text"));           // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 6);
        pnlFooter.add(lblFooter, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0.2;
        pnlFooter.add(filler3, gridBagConstraints);

        setOpaque(false);
        setLayout(new java.awt.BorderLayout());

        pnlContent.setBorder(null);
        pnlContent.setOpaque(false);
        pnlContent.setLayout(new java.awt.GridBagLayout());

        pnlLeft.setBorder(null);
        pnlLeft.setMaximumSize(new java.awt.Dimension(300, 1000000));
        pnlLeft.setMinimumSize(new java.awt.Dimension(290, 50));
        pnlLeft.setOpaque(false);
        pnlLeft.setLayout(new javax.swing.BoxLayout(pnlLeft, javax.swing.BoxLayout.Y_AXIS));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.weighty = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        pnlContent.add(pnlLeft, gridBagConstraints);

        pnlCenter.setBorder(null);
        pnlCenter.setMaximumSize(new java.awt.Dimension(300, 1000000));
        pnlCenter.setMinimumSize(new java.awt.Dimension(290, 50));
        pnlCenter.setOpaque(false);
        pnlCenter.setLayout(new javax.swing.BoxLayout(pnlCenter, javax.swing.BoxLayout.Y_AXIS));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.weighty = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        pnlContent.add(pnlCenter, gridBagConstraints);

        pnlRight.setBorder(null);
        pnlRight.setMaximumSize(new java.awt.Dimension(300, 1000000));
        pnlRight.setMinimumSize(new java.awt.Dimension(290, 50));
        pnlRight.setOpaque(false);
        pnlRight.setLayout(new javax.swing.BoxLayout(pnlRight, javax.swing.BoxLayout.Y_AXIS));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.weighty = 0.3;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        pnlContent.add(pnlRight, gridBagConstraints);

        add(pnlContent, java.awt.BorderLayout.CENTER);
    } // </editor-fold>//GEN-END:initComponents

    @Override
    public CidsBean getCidsBean() {
        return this.cidsBean;
    }

    @Override
    public void setCidsBean(final CidsBean cidsBean) {
        this.cidsBean = cidsBean;
    }

    @Override
    public void dispose() {
        for (final NoDescriptionWidget w : this.widgets) {
            w.dispose();
        }
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(final String title) {
        this.title = title;
    }

    @Override
    public JComponent getTitleComponent() {
        return pnlHeader;
    }

    @Override
    public JComponent getFooterComponent() {
        return this.pnlFooter;
    }
}
