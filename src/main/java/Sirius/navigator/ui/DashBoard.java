/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui;

import Sirius.navigator.ui.status.DefaultStatusChangeSupport;
import Sirius.navigator.ui.status.StatusChangeListener;
import Sirius.navigator.ui.status.StatusChangeSupport;

import org.openide.util.Lookup;

import java.awt.GridBagConstraints;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.*;

import de.cismet.cids.custom.widgets.AbstractDashBoardWidget;
import de.cismet.cids.custom.widgets.DashBoardWidgetWrapper;

import de.cismet.cids.dynamics.CidsBean;

import de.cismet.cids.tools.metaobjectrenderer.CidsBeanRenderer;

import de.cismet.tools.gui.FooterComponentProvider;
import de.cismet.tools.gui.TitleComponentProvider;

/**
 * DOCUMENT ME!
 *
 * @author   bfriedrich
 * @version  $Revision$, $Date$
 */
public class DashBoard extends javax.swing.JPanel implements CidsBeanRenderer,
    RequestsFullSizeComponent,
    TitleComponentProvider,
    FooterComponentProvider,
    StatusChangeSupport {

    //~ Static fields/initializers ---------------------------------------------

    private static final DashBoard INSTANCE = new DashBoard();

    //~ Instance fields --------------------------------------------------------

    protected final DefaultStatusChangeSupport statusChangeSupport = new DefaultStatusChangeSupport(this);

    private CidsBean cidsBean;
    private String title;

    private int numWidgets;

    private final ArrayList<DashBoardWidget> widgets = new ArrayList<DashBoardWidget>();
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblFooter;
    private javax.swing.JLabel lblHeader;
    private javax.swing.JLabel lblInfo;
    private javax.swing.JPanel panCenter;
    private javax.swing.JPanel panCenterFiller;
    private javax.swing.JPanel panContent;
    private javax.swing.JPanel panLeft;
    private javax.swing.JPanel panLeftFiller;
    private javax.swing.JPanel panRight;
    private javax.swing.JPanel panRightFiller;
    private javax.swing.JPanel pnlFooter;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlHeaderWidget;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form NoDescriptionRenderer.
     */
    private DashBoard() {
        initComponents();

        lookup();

        super.addHierarchyListener(new HierarchyListener() {

                @Override
                public void hierarchyChanged(final HierarchyEvent e) {
                    if (((HierarchyEvent.SHOWING_CHANGED & e.getChangeFlags()) != 0)
                                && DashBoard.super.isShowing()) {
                        SwingUtilities.invokeLater(new Runnable() {

                                @Override
                                public void run() {
                                    for (final DashBoardWidget w : widgets) {
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
        final Collection<? extends DashBoardWidget> lookupWidgets = lookUp.lookupAll(
                DashBoardWidget.class);

        for (final DashBoardWidget lookupWidget : lookupWidgets) {
            lookupWidget.init();

            if (lookupWidget.isHeaderWidget()) {
                addHeaderWidget((AbstractDashBoardWidget)lookupWidget);
            } else {
                addWidget(new DashBoardWidgetWrapper().wrapComponent((AbstractDashBoardWidget)lookupWidget),
                    lookupWidget.getX(),
                    lookupWidget.getY());
            }

            widgets.add(lookupWidget);
            numWidgets++;

            if (numWidgets == 9) {
                System.err.println("max 9 widgets accepted -> remaining widgets are ignored");
                break;
            }
        }
        panLeft.getComponent(2);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static DashBoard getInstance() {
        return INSTANCE;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  c  DOCUMENT ME!
     */
    public void addHeaderWidget(final JPanel c) {
        pnlHeaderWidget.add(c);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  widget  DOCUMENT ME!
     * @param  x       DOCUMENT ME!
     * @param  y       DOCUMENT ME!
     */
    private void addWidget(final JPanel widget, final int x, final int y) {
        final JPanel targetPanel;
        switch (x) {
            case 0: {
                targetPanel = panLeft;
                break;
            }
            case 1: {
                targetPanel = panCenter;
                break;
            }
            case 2: {
                targetPanel = panRight;
                break;
            }
            default: {
                targetPanel = null;
                System.err.println("UNKNOWN PANEL: " + x);
                addWidget(widget, x % 3, y);
            }
        }

        if (targetPanel != null) {
            final GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = targetPanel.getComponentCount() - 1;
            targetPanel.add(widget, gridBagConstraints);

            numWidgets++;

            targetPanel.revalidate();
            revalidate();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  args  DOCUMENT ME!
     */
    public static void main(final String[] args) {
        new JFrame().add(new DashBoard());
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
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        panContent = new javax.swing.JPanel();
        panLeft = new javax.swing.JPanel();
        panLeftFiller = new javax.swing.JPanel();
        panCenter = new javax.swing.JPanel();
        panCenterFiller = new javax.swing.JPanel();
        panRight = new javax.swing.JPanel();
        panRightFiller = new javax.swing.JPanel();

        pnlHeader.setOpaque(false);
        pnlHeader.setLayout(new java.awt.GridBagLayout());

        lblHeader.setFont(new java.awt.Font("DejaVu Sans", 1, 18));                                           // NOI18N
        lblHeader.setForeground(java.awt.Color.white);
        lblHeader.setText(org.openide.util.NbBundle.getMessage(DashBoard.class, "DashBoard.lblHeader.text")); // NOI18N
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

        lblInfo.setFont(new java.awt.Font("DejaVu Sans", 0, 12));                                         // NOI18N
        lblInfo.setForeground(java.awt.Color.white);
        lblInfo.setText(org.openide.util.NbBundle.getMessage(DashBoard.class, "DashBoard.lblInfo.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 6);
        pnlHeader.add(lblInfo, gridBagConstraints);

        pnlFooter.setOpaque(false);
        pnlFooter.setLayout(new java.awt.GridBagLayout());

        lblFooter.setFont(new java.awt.Font("DejaVu Sans", 1, 12));                                           // NOI18N
        lblFooter.setForeground(java.awt.Color.white);
        lblFooter.setText(org.openide.util.NbBundle.getMessage(DashBoard.class, "DashBoard.lblFooter.text")); // NOI18N
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
        setLayout(new java.awt.GridBagLayout());

        jScrollPane1.setOpaque(false);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        panContent.setBorder(null);
        panContent.setOpaque(false);
        panContent.setLayout(new java.awt.GridBagLayout());

        panLeft.setBorder(null);
        panLeft.setMaximumSize(new java.awt.Dimension(300, 1000000));
        panLeft.setMinimumSize(new java.awt.Dimension(290, 50));
        panLeft.setOpaque(false);
        panLeft.setLayout(new java.awt.GridBagLayout());

        panLeftFiller.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panLeft.add(panLeftFiller, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        panContent.add(panLeft, gridBagConstraints);

        panCenter.setBorder(null);
        panCenter.setMaximumSize(new java.awt.Dimension(300, 1000000));
        panCenter.setMinimumSize(new java.awt.Dimension(290, 50));
        panCenter.setOpaque(false);
        panCenter.setLayout(new java.awt.GridBagLayout());

        panCenterFiller.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panCenter.add(panCenterFiller, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        panContent.add(panCenter, gridBagConstraints);

        panRight.setBorder(null);
        panRight.setMaximumSize(new java.awt.Dimension(300, 1000000));
        panRight.setMinimumSize(new java.awt.Dimension(290, 50));
        panRight.setOpaque(false);
        panRight.setLayout(new java.awt.GridBagLayout());

        panRightFiller.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panRight.add(panRightFiller, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        panContent.add(panRight, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(panContent, gridBagConstraints);

        jScrollPane1.setViewportView(jPanel1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jScrollPane1, gridBagConstraints);
    } // </editor-fold>//GEN-END:initComponents

    @Override
    public CidsBean getCidsBean() {
        return cidsBean;
    }

    @Override
    public void setCidsBean(final CidsBean cidsBean) {
        this.cidsBean = cidsBean;
    }

    @Override
    public void dispose() {
        for (final DashBoardWidget w : widgets) {
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
        return pnlFooter;
    }

    @Override
    public void addStatusChangeListener(final StatusChangeListener listener) {
        statusChangeSupport.addStatusChangeListener(listener);
    }

    @Override
    public void removeStatusChangeListener(final StatusChangeListener listener) {
        statusChangeSupport.removeStatusChangeListener(listener);
    }
}
