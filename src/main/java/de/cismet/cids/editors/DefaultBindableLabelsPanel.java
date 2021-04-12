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
    private static final Comparator<CidsBean> BEAN_TOSTRING_COMPARATOR =
        new DefaultBindableReferenceCombo.BeanToStringComparator();

    //~ Instance fields --------------------------------------------------------

    private List selectedElements;
    @Getter private MetaClass metaClass = null;
    @Getter private final boolean editable;
    @Getter private ConnectionContext connectionContext = ConnectionContext.createDummy();

    @Getter @Setter private String sortingColumn;
    @Getter @Setter private Comparator<CidsBean> comparator;
    @Getter @Setter private boolean manageable;
    @Getter @Setter private String manageableButtonText;
    @Getter @Setter private String manageableProperty;
    @Getter @Setter private String where;
    @Getter @Setter private boolean initialized = false;
    @Getter @Setter private boolean newSelectionAdded = false;

    private final String title;
    private PropertyChangeSupport propertyChangeSupport;

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
        this(true, null);
    }

    /**
     * Creates a new DefaultBindableLabelsPanel object.
     *
     * @param  editable  DOCUMENT ME!
     * @param  title     DOCUMENT ME!
     * @param  options   DOCUMENT ME!
     */
    public DefaultBindableLabelsPanel(final boolean editable,
            final String title,
            final DefaultBindableReferenceCombo.Option... options) {
        MetaClass metaClass = null;
        boolean manageable = false;
        String manageableItemRepresentation = MESSAGE_MANAGEABLE_ITEM;
        String manageableProperty = "name";
        String where = null;
        String sortingColumn = null;
        Comparator<CidsBean> comparator = BEAN_TOSTRING_COMPARATOR;

        if (options != null) {
            for (final DefaultBindableReferenceCombo.Option option : options) {
                if (option != null) {
                    if (option.getClass().equals(DefaultBindableReferenceCombo.MetaClassOption.class)) {
                        metaClass = ((DefaultBindableReferenceCombo.MetaClassOption)option).getMetaClass();
                    } else if (option.getClass().equals(DefaultBindableReferenceCombo.ManageableOption.class)) {
                        manageable = true;
                        final String property = ((DefaultBindableReferenceCombo.ManageableOption)option).getProperty();
                        if (property != null) {
                            manageableProperty = property;
                        }
                        final String representation = ((DefaultBindableReferenceCombo.ManageableOption)option)
                                    .getRepresentation();
                        if (representation != null) {
                            manageableItemRepresentation = representation;
                        }
                    } else if (option.getClass().equals(DefaultBindableReferenceCombo.WhereOption.class)) {
                        where = ((DefaultBindableReferenceCombo.WhereOption)option).getWhere();
                    } else if (option.getClass().equals(DefaultBindableReferenceCombo.ComparatorOption.class)) {
                        comparator = ((DefaultBindableReferenceCombo.ComparatorOption)option).getComparator();
                    } else if (option.getClass().equals(DefaultBindableReferenceCombo.SortingColumnOption.class)) {
                        sortingColumn = ((DefaultBindableReferenceCombo.SortingColumnOption)option).getColumn();
                    } else if (option.getClass().equals(DefaultBindableReferenceCombo.WhereOption.class)) {
                        where = ((DefaultBindableReferenceCombo.WhereOption)option).getWhere();
                    }
                }
            }
        }

        this.title = title;
        this.editable = editable;
        this.metaClass = metaClass;
        this.where = where;
        setManageable(manageable);
        setComparator(comparator);
        setSortingColumn(sortingColumn);
        setManageableButtonText(manageableItemRepresentation);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void initWithConnectionContext(final ConnectionContext connectionContext) {
        this.connectionContext = connectionContext;

        initComponents();

        setInitialized(true);

        reload(false);
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
                final CidsBean cidsBean = metaClass.getEmptyInstance(getConnectionContext()).getBean();
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
        if (isInitialized()) {
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

    /**
     * DOCUMENT ME!
     *
     * @param   metaClass  DOCUMENT ME!
     * @param   orderBy    DOCUMENT ME!
     * @param   where      DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private static String createQuery(final MetaClass metaClass, final String orderBy, final String where) {
        final String template = "SELECT %d, %s FROM %s WHERE %s ORDER BY %s";
        final String query = String.format(
                template,
                metaClass.getID(),
                metaClass.getPrimaryKey(),
                metaClass.getTableName(),
                (where != null) ? where : "TRUE",
                (orderBy != null) ? orderBy : metaClass.getPrimaryKey());
        return query;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  forceReload  DOCUMENT ME!
     */
    public void reload(final boolean forceReload) {
        panToggles.removeAll();
        panLabels.removeAll();
        panLabels.add(new JLabel(MESSAGE_LOADING_ITEM));
        btnEdit.setEnabled(false);

        new SwingWorker<List<CidsBean>, Void>() {

                @Override
                protected List<CidsBean> doInBackground() throws Exception {
                    if (getMetaClass() != null) {
                        final MetaClass foreignClass = getReferencedClass(getMetaClass());
                        final String query = createQuery(foreignClass, getSortingColumn(), null);

                        final MetaObject[] mos = MetaObjectCache.getInstance()
                                    .getMetaObjectsByQuery(
                                        query,
                                        getMetaClass().getDomain(),
                                        forceReload,
                                        getConnectionContext());
                        if (mos != null) {
                            final List<CidsBean> cidsBeans = new ArrayList<>();
                            for (final MetaObject mo : mos) {
                                if (mo != null) {
                                    cidsBeans.add(mo.getBean());
                                }
                            }
                            return cidsBeans;
                        }
                    }
                    return null;
                }

                @Override
                protected void done() {
                    try {
                        final List<CidsBean> cidsBeans = get();
                        if (cidsBeans != null) {
                            update(cidsBeans);
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
     * @param  cidsBeans  DOCUMENT ME!
     */
    private void update(final List<CidsBean> cidsBeans) {
        final Comparator<CidsBean> comparator = getComparator();
        if (comparator != null) {
            cidsBeans.sort(comparator);
        }

        for (final CidsBean cidsBean : cidsBeans) {
            final Toggle toggle = new Toggle(cidsBean);
            panToggles.add(toggle);
        }

        refreshSelectedElements();
        setEnabled(isEditable());
    }

    @Override
    public void setMetaClass(final MetaClass metaClass) {
        this.metaClass = metaClass;

        reload(false);
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
                    final CidsBean cidsBean = toggle.getCidsBean();
                    if (selectedElements.contains(cidsBean)) {
                        toggle.setSelected(true);
                        panLabels.add(new JLabel(cidsBean.toString()));
                        panLabels.add(new JLabel(", "));
                    } else {
                        toggle.setSelected(false);
                    }
                }
            }
            final int compCount = panLabels.getComponentCount();
            if (compCount > 0) {
                panLabels.remove(compCount - 1);
            } else {
                panLabels.add(new JLabel("-"));
            }
        }
        revalidate();
        repaint();
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
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 0, 0);
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
        if (isEnabled()) {
            jDialog1.setSize(getSize().width, jDialog1.getSize().height);
            final int x = btnEdit.getLocationOnScreen().x - jDialog1.getBounds().width + btnEdit.getBounds().width;
            final int y = btnEdit.getLocationOnScreen().y;

            jButton1.setVisible(isManageable());

            jDialog1.setLocation(x + 2, y - 4);
            jDialog1.setVisible(true);
        }
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
     * @return  DOCUMENT ME!
     */
    public List<CidsBean> getElements() {
        final List<CidsBean> cidsBeans = new ArrayList<>();
        for (final Component component : panToggles.getComponents()) {
            if (component instanceof Toggle) {
                final Toggle toggle = (Toggle)component;
                final CidsBean cidsBean = toggle.getCidsBean();
                cidsBeans.add(cidsBean);
            }
        }
        return cidsBeans;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void btnApplyActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnApplyActionPerformed
        final List old = (selectedElements != null) ? new ArrayList(selectedElements) : null;
        if (selectedElements == null) {
            selectedElements = new ArrayList();
        }
        for (final Component component : panToggles.getComponents()) {
            if (component instanceof Toggle) {
                final Toggle toggle = (Toggle)component;
                final CidsBean cidsBean = toggle.getCidsBean();
                if (toggle.isSelected() && !selectedElements.contains(cidsBean)) {
                    selectedElements.add(cidsBean);
                } else if (!toggle.isSelected() && selectedElements.contains(cidsBean)) {
                    selectedElements.remove(cidsBean);
                }
            }
        }

//        refreshSelectedElements();
        getPropertyChangeSupport().firePropertyChange("selectedElements", old, selectedElements);
        reload(isNewSelectionAdded());
        setNewSelectionAdded(false);

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
                panToggles.add(new Toggle(persistedBean));
                panToggles.revalidate();
                setNewSelectionAdded(true);
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

        private final CidsBean cidsBean;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new Toggle object.
         *
         * @param  cidsBean  DOCUMENT ME!
         */
        public Toggle(final CidsBean cidsBean) {
            super(cidsBean.toString());
            this.cidsBean = cidsBean;
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
        public CidsBean getCidsBean() {
            return cidsBean;
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
