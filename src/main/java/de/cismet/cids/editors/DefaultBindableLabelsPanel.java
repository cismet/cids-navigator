/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.cids.editors;

import Sirius.navigator.tools.MetaObjectCache;

import Sirius.server.localserver.attribute.MemberAttributeInfo;
import Sirius.server.middleware.types.MetaClass;
import Sirius.server.middleware.types.MetaClassStore;
import Sirius.server.middleware.types.MetaObject;

import lombok.Getter;
import lombok.Setter;

import org.apache.log4j.Logger;

import org.jdesktop.beansbinding.Converter;
import org.jdesktop.beansbinding.Validator;

import org.openide.util.NbBundle;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingWorker;
import javax.swing.border.Border;

import de.cismet.cids.dynamics.CidsBean;

import de.cismet.cids.navigator.utils.ClassCacheMultiple;

import de.cismet.connectioncontext.ConnectionContext;
import de.cismet.connectioncontext.ConnectionContextStore;

import de.cismet.layout.WrapLayout;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class DefaultBindableLabelsPanel extends JPanel implements Bindable, MetaClassStore, ConnectionContextStore {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(DefaultBindableLabelsPanel.class);
    private static final String MESSAGE_LOADING_ITEM = NbBundle.getMessage(
            DefaultBindableReferenceCombo.class,
            "DefaultBindableReferenceCombo.loading");
    private static final String MESSAGE_MANAGEABLE_ITEM = NbBundle.getMessage(
            DefaultBindableReferenceCombo.class,
            "DefaultBindableReferenceCombo.item.manageable");
    private static final String MESSAGE_CREATEITEMDIALOG_TITLE = NbBundle.getMessage(
            DefaultBindableReferenceCombo.class,
            "DefaultBindableReferenceCombo.createitemdialog.title");

    //~ Instance fields --------------------------------------------------------

    private PropertyChangeSupport propertyChangeSupport;
    private List selectedElements = null;
    private MetaClass metaClass = null;
//    private final Map<JToggleButton, MetaObject> toggleToObjectMapping = new HashMap<>();

    @Getter @Setter private Comparator<MetaObject> comparator;
    @Getter @Setter private boolean manageable = true;
    @Getter @Setter private String manageableButtonText = MESSAGE_MANAGEABLE_ITEM;
    @Getter @Setter private String manageableProperty = "name";

    private ConnectionContext connectionContext = ConnectionContext.createDummy();
    private final boolean editable;
    private final String title;

    private boolean initialized = false;

    private boolean newSelectionAdded = false;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnApply;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnEdit;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JButton jButton1;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel panLabels;
    private javax.swing.JPanel panToggles;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DefaultBindableLabelsPanel object.
     */
    public DefaultBindableLabelsPanel() {
        this(null, true, null);
    }

    /**
     * Creates a new DefaultBindableLabelsPanel object.
     *
     * @param  editable  DOCUMENT ME!
     */
    public DefaultBindableLabelsPanel(final boolean editable) {
        this(null, editable, null);
    }

    /**
     * Creates a new DefaultBindableLabelsPanel object.
     *
     * @param  editable  DOCUMENT ME!
     * @param  title     DOCUMENT ME!
     */
    public DefaultBindableLabelsPanel(final boolean editable, final String title) {
        this(null, editable, title);
    }

    /**
     * Creates a new DefaultBindableLabelsPanel object.
     *
     * @param  comparator  DOCUMENT ME!
     * @param  editable    DOCUMENT ME!
     */
    public DefaultBindableLabelsPanel(final Comparator<MetaObject> comparator, final boolean editable) {
        this(null, editable, null);
    }

    /**
     * Creates a new DefaultBindableLabelsPanel object.
     *
     * @param  comparator  DOCUMENT ME!
     * @param  editable    DOCUMENT ME!
     * @param  title       DOCUMENT ME!
     */
    public DefaultBindableLabelsPanel(final Comparator<MetaObject> comparator,
            final boolean editable,
            final String title) {
        this.comparator = comparator;
        this.editable = editable;
        this.title = title;

        if (java.beans.Beans.isDesignTime()) {
            initComponents();
        }
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void initWithConnectionContext(final ConnectionContext connectionContext) {
        this.connectionContext = connectionContext;

        initComponents();

        initialized = true;

        refresh(false);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getTitle() {
        return String.format("Auswahl - %s", title);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public PropertyChangeSupport getPropertyChangeSupport() {
        if (propertyChangeSupport == null) {
            propertyChangeSupport = new PropertyChangeSupport(this);
        }
        return propertyChangeSupport;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    private CidsBean createNewInstance() throws Exception {
        final MetaClass metaClass = getReferencedClass(getMetaClass());
        if (metaClass != null) {
            final String manageableProperty = getManageableProperty();
            final String property = (manageableProperty != null) ? manageableProperty : "name";
            String name = null;
            for (final Object value : metaClass.getMemberAttributeInfos().values()) {
                if (value instanceof MemberAttributeInfo) {
                    final MemberAttributeInfo mai = (MemberAttributeInfo)value;
                    if (property.equals(mai.getFieldName())) {
                        name = mai.getName();
                        break;
                    }
                }
            }
            final String input = JOptionPane.showInputDialog(
                    this,
                    String.format("%s:", name),
                    String.format(MESSAGE_CREATEITEMDIALOG_TITLE, metaClass.getName()),
                    JOptionPane.QUESTION_MESSAGE);
            if (input != null) {
                final MetaObject metaObject = metaClass.getEmptyInstance(getConnectionContext());
                final CidsBean cidsBean = metaObject.getBean();
                cidsBean.setProperty(property, input);
                return cidsBean;
            }
        }
        return null;
    }

    @Override
    public void setOpaque(final boolean isOpaque) {
        super.setOpaque(isOpaque);
    }

    /**
     * Add PropertyChangeListener.
     *
     * @param  listener  DOCUMENT ME!
     */
    @Override
    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        if (listener != null) {
            getPropertyChangeSupport().addPropertyChangeListener(listener);
        }
    }

    /**
     * Remove PropertyChangeListener.
     *
     * @param  listener  DOCUMENT ME!
     */
    @Override
    public void removePropertyChangeListener(final PropertyChangeListener listener) {
        getPropertyChangeSupport().removePropertyChangeListener(listener);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  elements  DOCUMENT ME!
     */
    public void setSelectedElements(final Object elements) {
        if (elements instanceof List) {
            this.selectedElements = (List)elements;
        }
        if (initialized) {
            refreshSelectedElements();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Object getSelectedElements() {
        return selectedElements;
    }

    @Override
    public String getBindingProperty() {
        return "selectedElements";
    }

    @Override
    public Validator getValidator() {
        return null;
    }

    @Override
    public Converter getConverter() {
        return null;
    }

    @Override
    public Object getNullSourceValue() {
        return null;
    }

    @Override
    public Object getErrorSourceValue() {
        return null;
    }

    @Override
    public MetaClass getMetaClass() {
        return this.metaClass;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  forceReload  DOCUMENT ME!
     */
    private void refresh(final boolean forceReload) {
        panToggles.removeAll();
        panLabels.removeAll();
        panLabels.add(new JLabel(MESSAGE_LOADING_ITEM));
        btnEdit.setEnabled(false);

        new SwingWorker<MetaObject[], Void>() {

                @Override
                protected MetaObject[] doInBackground() throws Exception {
                    if (getMetaClass() != null) {
                        final MetaClass foreignClass = getReferencedClass(getMetaClass());
                        final String query = "select " + foreignClass.getID() + ", " + foreignClass.getPrimaryKey()
                                    + " from "
                                    + foreignClass.getTableName();

                        return MetaObjectCache.getInstance()
                                    .getMetaObjectsByQuery(
                                        query,
                                        getMetaClass().getDomain(),
                                        forceReload,
                                        getConnectionContext());
                    }
                    return null;
                }

                @Override
                protected void done() {
                    try {
                        final MetaObject[] metaObjects = get();
                        if (metaObjects != null) {
                            update(metaObjects);
                        }
                    } catch (final Exception e) {
                        LOG.error("Error while filling a togglebutton field.", e); // NOI18N
                    }
                }
            }.execute();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  metaObjects  DOCUMENT ME!
     */
    private void update(final MetaObject[] metaObjects) {
        final Comparator<MetaObject> comparator = getComparator();
        if (comparator != null) {
            Arrays.sort(metaObjects, comparator);
        }

        for (final MetaObject metaObject : metaObjects) {
            final Toggle toggle = new Toggle(metaObject);
            panToggles.add(toggle);
        }

        refreshSelectedElements();
        setEnabled(isEditable());
    }

    @Override
    public void setMetaClass(final MetaClass metaClass) {
        this.metaClass = metaClass;

        refresh(false);
    }

    /**
     * DOCUMENT ME!
     */
    private void refreshSelectedElements() {
        panLabels.removeAll();
        if (selectedElements != null) {
            for (final Component component : panToggles.getComponents()) {
                if (component instanceof Toggle) {
                    final Toggle toggle = (Toggle)component;
                    final MetaObject metaObject = toggle.getMetaObject();
                    if ((metaObject != null) && selectedElements.contains(metaObject.getBean())) {
                        toggle.setSelected(true);
                        panLabels.add(new JLabel(metaObject.getBean().toString()));
                        panLabels.add(new JLabel(", "));
                    } else {
                        toggle.setSelected(false);
                    }
                }
            }
            final int compCount = panLabels.getComponentCount();
            if (compCount > 0) {
                panLabels.remove(compCount - 1);
            }
        }
        revalidate();
    }

    /**
     * DOCUMENT ME!
     *
     * @param   metaClass  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private MetaClass getReferencedClass(final MetaClass metaClass) {
        MetaClass result = metaClass;
        if (metaClass.isArrayElementLink()) {
            final HashMap hm = metaClass.getMemberAttributeInfos();
            for (final Object tmp : hm.values()) {
                if (tmp instanceof MemberAttributeInfo) {
                    if (((MemberAttributeInfo)tmp).isForeignKey()) {
                        final int classId = ((MemberAttributeInfo)tmp).getForeignKeyClassId();
                        result = ClassCacheMultiple.getMetaClass(metaClass.getDomain(),
                                classId,
                                getConnectionContext());
                    }
                }
            }
        }
        return result;
    }

    /**
     * DOCUMENT ME!
     */
    public void dispose() {
    }

    @Override
    public void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);
        btnEdit.setEnabled(enabled);
        for (final Component component : panToggles.getComponents()) {
            if (component instanceof Toggle) {
                final Toggle toggle = (Toggle)component;
                toggle.setVisible(isEnabled() || toggle.isEnabled());
                // toggle.setEnabled(isEnabled() && toggle.isEnabled());
            }
        }
    }

    @Override
    public ConnectionContext getConnectionContext() {
        return connectionContext;
            // return ConnectionContextUtils.getFirstParentClientConnectionContext(this);
    }
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jDialog1 = new javax.swing.JDialog();
        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        panToggles = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0),
                new java.awt.Dimension(0, 0),
                new java.awt.Dimension(32767, 0));
        btnApply = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        panLabels = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnEdit = new javax.swing.JButton();

        jDialog1.setTitle(getTitle());
        jDialog1.setMinimumSize(new java.awt.Dimension(400, 300));
        jDialog1.setModal(true);
        jDialog1.getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel3.setLayout(new java.awt.GridBagLayout());

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jScrollPane1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        final javax.swing.GroupLayout panTogglesLayout = new javax.swing.GroupLayout(panToggles);
        panToggles.setLayout(panTogglesLayout);
        panTogglesLayout.setHorizontalGroup(
            panTogglesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(
                0,
                410,
                Short.MAX_VALUE));
        panTogglesLayout.setVerticalGroup(
            panTogglesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(
                0,
                251,
                Short.MAX_VALUE));

        jScrollPane1.setViewportView(panToggles);
        panToggles.setLayout(new WrapLayout(WrapLayout.LEFT));

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(jScrollPane1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(jPanel1, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(jButton1, getManageableButtonText());
        jButton1.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jButton1ActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        jPanel2.add(jButton1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanel2.add(filler1, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(
            btnApply,
            org.openide.util.NbBundle.getMessage(
                DefaultBindableLabelsPanel.class,
                "DefaultBindableLabelsPanel.btnApply.text")); // NOI18N
        btnApply.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    btnApplyActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        jPanel2.add(btnApply, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(
            btnCancel,
            org.openide.util.NbBundle.getMessage(
                DefaultBindableLabelsPanel.class,
                "DefaultBindableLabelsPanel.btnCancel.text")); // NOI18N
        btnCancel.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    btnCancelActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel2.add(btnCancel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        jPanel3.add(jPanel2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jDialog1.getContentPane().add(jPanel3, gridBagConstraints);

        jDialog1.getRootPane().setDefaultButton(btnApply);

        setBorder(isEditable() ? javax.swing.BorderFactory.createEtchedBorder() : null);
        setOpaque(isOpaque());
        addMouseListener(new java.awt.event.MouseAdapter() {

                @Override
                public void mouseClicked(final java.awt.event.MouseEvent evt) {
                    formMouseClicked(evt);
                }
            });
        setLayout(new java.awt.GridBagLayout());

        panLabels.setOpaque(false);
        panLabels.addMouseListener(new java.awt.event.MouseAdapter() {

                @Override
                public void mouseClicked(final java.awt.event.MouseEvent evt) {
                    panLabelsMouseClicked(evt);
                }
            });
        panLabels.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, MESSAGE_LOADING_ITEM);
        panLabels.add(jLabel1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        add(panLabels, gridBagConstraints);
        panLabels.setLayout(new WrapLayout(WrapLayout.LEFT, 0, 5));

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/cids/editors/icon-edit.png"))); // NOI18N
        btnEdit.setToolTipText(org.openide.util.NbBundle.getMessage(
                DefaultBindableLabelsPanel.class,
                "DefaultBindableLabelsPanel.btnEdit.toolTipText"));                                                  // NOI18N
        btnEdit.setBorderPainted(false);
        btnEdit.setContentAreaFilled(false);
        btnEdit.setMaximumSize(new java.awt.Dimension(24, 24));
        btnEdit.setMinimumSize(new java.awt.Dimension(24, 24));
        btnEdit.setPreferredSize(new java.awt.Dimension(24, 24));
        btnEdit.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    btnEditActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 0);
        add(btnEdit, gridBagConstraints);
        btnEdit.setVisible(isEditable());
    } // </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void btnEditActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnEditActionPerformed
//        StaticSwingTools.showDialog(jDialog1);

        jDialog1.setSize(getSize().width, jDialog1.getSize().height);
        final int x = btnEdit.getLocationOnScreen().x - jDialog1.getBounds().width + btnEdit.getBounds().width;
        final int y = btnEdit.getLocationOnScreen().y;

        jButton1.setVisible(isManageable());

        jDialog1.setLocation(x + 2, y - 4);
        jDialog1.setVisible(true);
    } //GEN-LAST:event_btnEditActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void btnCancelActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnCancelActionPerformed
        jDialog1.setVisible(false);
    }                                                                             //GEN-LAST:event_btnCancelActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void btnApplyActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnApplyActionPerformed
        final List old = new ArrayList(selectedElements);
        for (final Component component : panToggles.getComponents()) {
            if (component instanceof Toggle) {
                final Toggle toggle = (Toggle)component;
                final CidsBean cidsBean = toggle.getMetaObject().getBean();
                if (toggle.isSelected() && !selectedElements.contains(cidsBean)) {
                    selectedElements.add(cidsBean);
                } else if (!toggle.isSelected() && selectedElements.contains(cidsBean)) {
                    selectedElements.remove(cidsBean);
                }
            }
        }

//        refreshSelectedElements();
        propertyChangeSupport.firePropertyChange("selectedElements", old, selectedElements);
        refresh(newSelectionAdded);
        newSelectionAdded = false;

//        propertyChangeSupport.firePropertyChange("selectedElement", null, cidsBean);
        jDialog1.setVisible(false);
    } //GEN-LAST:event_btnApplyActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void panLabelsMouseClicked(final java.awt.event.MouseEvent evt) { //GEN-FIRST:event_panLabelsMouseClicked
        btnEditActionPerformed(null);
    }                                                                         //GEN-LAST:event_panLabelsMouseClicked

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void formMouseClicked(final java.awt.event.MouseEvent evt) { //GEN-FIRST:event_formMouseClicked
        btnEditActionPerformed(null);
    }                                                                    //GEN-LAST:event_formMouseClicked

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jButton1ActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jButton1ActionPerformed
        try {
            final CidsBean newBean = createNewInstance();
            if (newBean != null) {
                final List old = new ArrayList(selectedElements);
                final CidsBean persistedBean = newBean.persist(getConnectionContext());
                propertyChangeSupport.firePropertyChange("selectedElements", old, selectedElements);
                panToggles.add(new Toggle(persistedBean.getMetaObject()));
                panToggles.revalidate();
                newSelectionAdded = true;
            }
        } catch (final Exception ex) {
            LOG.error(ex, ex);
        }
    }                                                                            //GEN-LAST:event_jButton1ActionPerformed

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private static class RoundedBorder implements Border {

        //~ Instance fields ----------------------------------------------------

        private final int radius;
        private final Insets insets;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new RoundedBorder object.
         *
         * @param  radius  DOCUMENT ME!
         * @param  insets  DOCUMENT ME!
         */
        public RoundedBorder(final int radius, final Insets insets) {
            this.radius = radius;
            this.insets = insets;
        }

        /**
         * Creates a new RoundedBorder object.
         *
         * @param  radius  DOCUMENT ME!
         * @param  top     DOCUMENT ME!
         * @param  left    DOCUMENT ME!
         * @param  bottom  DOCUMENT ME!
         * @param  right   DOCUMENT ME!
         */
        public RoundedBorder(final int radius, final int top, final int left, final int bottom, final int right) {
            this(radius, new Insets(top, left, bottom, right));
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public Insets getBorderInsets(final Component c) {
            return insets;
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }

        @Override
        public void paintBorder(final Component c,
                final Graphics g,
                final int x,
                final int y,
                final int width,
                final int height) {
            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class Toggle extends JToggleButton implements ActionListener {

        //~ Instance fields ----------------------------------------------------

        private final MetaObject metaObject;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new Toggle object.
         *
         * @param  metaObject  DOCUMENT ME!
         */
        public Toggle(final MetaObject metaObject) {
            super(metaObject.getBean().toString());
            this.metaObject = metaObject;
            setOpaque(false);
            setFocusPainted(false);
            setSelected(false);
            addActionListener(this);
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public MetaObject getMetaObject() {
            return metaObject;
        }

        @Override
        public void setSelected(final boolean selected) {
            super.setSelected(selected);
            rendererSelected();
        }

        /**
         * DOCUMENT ME!
         */
        private void rendererSelected() {
//            if (isSelected()) {
            setBorder(new RoundedBorder(10, 2, 5, 2, 5));
//            } else {
//                setBorder(new EmptyBorder(2, 5, 2, 5));
//            }
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            rendererSelected();
        }
    }
}
