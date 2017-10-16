/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui.option;

import Sirius.navigator.connection.SessionManager;

import org.apache.log4j.Logger;

import org.jdom.Element;

import org.openide.util.lookup.ServiceProvider;

import de.cismet.cids.server.actions.CheckCidsServerMessageAction;
import de.cismet.cids.server.connectioncontext.ClientConnectionContext;
import de.cismet.cids.server.connectioncontext.ClientConnectionContextProvider;

import de.cismet.cids.servermessage.CidsServerMessageNotifier;

import de.cismet.lookupoptions.AbstractOptionsPanel;
import de.cismet.lookupoptions.OptionsPanelController;

import de.cismet.lookupoptions.options.GeneralOptionsCategory;

import de.cismet.tools.configuration.NoWriteError;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
@ServiceProvider(service = OptionsPanelController.class)
public class CidsServerMessagesOptionsPanel extends AbstractOptionsPanel implements ClientConnectionContextProvider {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient Logger LOG = Logger.getLogger(CidsServerMessagesOptionsPanel.class);

    //~ Instance fields --------------------------------------------------------

    private int intervallInMs = CidsServerMessageNotifier.DEFAULT_SCHEDULE_INTERVAL;
    private boolean stillConfigured = false;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler1;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JLabel lblDialogDescription;
    private javax.swing.JLabel lblIntervall;
    private javax.swing.JLabel lblSeconds;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form PasswordOptionsDialog.
     */
    public CidsServerMessagesOptionsPanel() {
        super(org.openide.util.NbBundle.getMessage(
                CidsServerMessagesOptionsPanel.class,
                "CidsServerMessagesOptionsDialog.title"), // NOI18N,
            GeneralOptionsCategory.class);
        try {
            initComponents();
        } catch (Exception e) {
            LOG.error("Erro during Creation of Password Dialog", e);
            ;
        }
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

        lblSeconds = new javax.swing.JLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0),
                new java.awt.Dimension(0, 0),
                new java.awt.Dimension(0, 32767));
        lblDialogDescription = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        lblIntervall = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setMaximumSize(new java.awt.Dimension(264, 177));
        setMinimumSize(new java.awt.Dimension(100, 177));
        setLayout(new java.awt.GridBagLayout());

        lblSeconds.setText(org.openide.util.NbBundle.getMessage(
                CidsServerMessagesOptionsPanel.class,
                "CidsServerMessagesOptionsPanel.lblSeconds.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(lblSeconds, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 0.1;
        add(filler1, gridBagConstraints);

        lblDialogDescription.setText(org.openide.util.NbBundle.getMessage(
                CidsServerMessagesOptionsPanel.class,
                "CidsServerMessagesOptionsPanel.lblDialogDescription.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 10, 4);
        add(lblDialogDescription, gridBagConstraints);

        jSpinner1.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));
        jSpinner1.setMinimumSize(new java.awt.Dimension(75, 28));
        jSpinner1.setPreferredSize(new java.awt.Dimension(75, 28));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        add(jSpinner1, gridBagConstraints);

        lblIntervall.setText(org.openide.util.NbBundle.getMessage(
                CidsServerMessagesOptionsPanel.class,
                "CidsServerMessagesOptionsPanel.lblIntervall.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        add(lblIntervall, gridBagConstraints);
    }                                                                 // </editor-fold>//GEN-END:initComponents

    @Override
    public void update() {
        intervallInMs = CidsServerMessageNotifier.getInstance().getScheduleIntervall();
        updateGui();
    }

    @Override
    public void applyChanges() {
        intervallInMs = ((Number)jSpinner1.getValue()).intValue() * 1000;
        CidsServerMessageNotifier.getInstance().setScheduleIntervall(intervallInMs);
    }

    @Override
    public boolean isChanged() {
        return (((Number)jSpinner1.getValue()).intValue() * 1000) != intervallInMs;
    }

    /**
     * DOCUMENT ME!
     */
    private void updateGui() {
        jSpinner1.setValue(Math.floor(intervallInMs / 1000f));
    }

    /**
     * Returns tooltip text.
     *
     * @return  tooltip text
     */
    @Override
    public String getTooltip() {
        return org.openide.util.NbBundle.getMessage(
                CidsServerMessagesOptionsPanel.class,
                "CidsServerMessagesOptionsDialog.tooltip"); // NOI18N
    }

    @Override
    public void configure(final Element parent) {
        if (!stillConfigured) {
            CidsServerMessageNotifier.getInstance().configure(parent);
            updateGui();

            stillConfigured = true;
        }

        applyChanges();
    }

    @Override
    public Element getConfiguration() throws NoWriteError {
        return CidsServerMessageNotifier.getInstance().getConfiguration();
    }

    @Override
    public boolean isEnabled() {
        try {
            return SessionManager.getSession()
                        .getConnection()
                        .hasConfigAttr(
                            SessionManager.getSession().getUser(),
                            "csa://"
                            + CheckCidsServerMessageAction.TASK_NAME,
                            getClientConnectionContext());
        } catch (final Exception ex) {
            LOG.warn("could not check csa://" + CheckCidsServerMessageAction.TASK_NAME
                        + ". CidsServerMessageOptionsPanel is now disabled",
                ex);
            return false;
        }
    }

    @Override
    public ClientConnectionContext getClientConnectionContext() {
        return ClientConnectionContext.create(getClass().getSimpleName());
    }
}
