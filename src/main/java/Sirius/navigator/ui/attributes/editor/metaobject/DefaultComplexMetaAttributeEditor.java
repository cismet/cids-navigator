/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * DefaultComplexMetaAttributeEditor.java
 *
 * Created on 26. August 2004, 15:35
 */
package Sirius.navigator.ui.attributes.editor.metaobject;

import Sirius.navigator.connection.*;
import Sirius.navigator.resource.ResourceManager;
import Sirius.navigator.ui.attributes.editor.*;

import Sirius.server.localserver.attribute.Attribute;
import Sirius.server.middleware.types.*;
import Sirius.server.newuser.User;
import Sirius.server.newuser.UserGroup;
import Sirius.server.newuser.permission.PermissionHolder;

import org.apache.log4j.Logger;

import java.awt.*;

import java.util.*;

import javax.swing.*;

import de.cismet.connectioncontext.ConnectionContext;
import de.cismet.connectioncontext.ConnectionContextProvider;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class DefaultComplexMetaAttributeEditor extends AbstractComplexMetaAttributeEditor
        implements ConnectionContextProvider {

    //~ Static fields/initializers ---------------------------------------------

    private static final ResourceManager RESOURCE = ResourceManager.getManager();

    //~ Instance fields --------------------------------------------------------

    private final ConnectionContext connectionContext = ConnectionContext.createDummy();

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton commitButton;
    private javax.swing.JPanel editorPanel;
    protected javax.swing.JPanel labelPanel;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form DefaultComplexMetaAttributeEditor.
     */
    public DefaultComplexMetaAttributeEditor() {
        this.logger = Logger.getLogger(this.getClass());

        this.editorActivationDelegate = new ComplexEditorActivationDelegate();
        this.editorUIDelegate = new ComplexEditorUIDelegate();
        this.editorHandler = new ComplexEditorHandler();
        this.editorLocator = new MetaAttributeEditorLocator();
        this.initComponents();
        this.init = true;
        // this.setProperty(PROPERTY_LOCALE, new Locale("de", "DE"));
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        statusLabel = new javax.swing.JLabel();
        titleLabel = new javax.swing.JLabel();
        labelPanel = new javax.swing.JPanel();
        editorPanel = new javax.swing.JPanel();
        statusPanel = new javax.swing.JPanel();
        commitButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        statusLabel.setBackground(java.awt.SystemColor.info);
        statusLabel.setFont(new java.awt.Font("MS Sans Serif", 1, 11)); // NOI18N
        statusLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        statusLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        statusLabel.setMaximumSize(null);
        statusLabel.setMinimumSize(new java.awt.Dimension(18, 18));
        statusLabel.setOpaque(true);
        statusLabel.setPreferredSize(new java.awt.Dimension(100, 18));

        titleLabel.setBackground(java.awt.SystemColor.info);
        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        titleLabel.setMinimumSize(new java.awt.Dimension(18, 18));
        titleLabel.setOpaque(true);
        titleLabel.setPreferredSize(new java.awt.Dimension(100, 18));

        setLayout(new java.awt.BorderLayout());

        labelPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 0, 10));
        labelPanel.setLayout(new java.awt.BorderLayout());
        add(labelPanel, java.awt.BorderLayout.NORTH);

        editorPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10),
                javax.swing.BorderFactory.createEtchedBorder()));
        editorPanel.setLayout(new java.awt.GridBagLayout());
        add(editorPanel, java.awt.BorderLayout.CENTER);

        statusPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 10, 10));
        statusPanel.setLayout(new java.awt.GridBagLayout());

        commitButton.setIcon(RESOURCE.getIcon("save_objekt.gif"));
        commitButton.setToolTipText(org.openide.util.NbBundle.getMessage(
                DefaultComplexMetaAttributeEditor.class,
                "DefaultComplexMetaAttributeEditor.commitButton.tooltip")); // NOI18N
        commitButton.setActionCommand("commit");                            // NOI18N
        commitButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        commitButton.setContentAreaFilled(false);
        commitButton.setEnabled(false);
        commitButton.setFocusPainted(false);
        commitButton.setMaximumSize(new java.awt.Dimension(16, 16));
        commitButton.setMinimumSize(new java.awt.Dimension(16, 16));
        commitButton.setPreferredSize(new java.awt.Dimension(16, 16));
        commitButton.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    commitButtonActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        statusPanel.add(commitButton, gridBagConstraints);

        cancelButton.setIcon(RESOURCE.getIcon("zurueck_objekt.gif"));
        cancelButton.setToolTipText(org.openide.util.NbBundle.getMessage(
                DefaultComplexMetaAttributeEditor.class,
                "DefaultComplexMetaAttributeEditor.cancelButton.tooltip")); // NOI18N
        cancelButton.setActionCommand("cancel");                            // NOI18N
        cancelButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        cancelButton.setContentAreaFilled(false);
        cancelButton.setEnabled(false);
        cancelButton.setFocusPainted(false);
        cancelButton.setMaximumSize(new java.awt.Dimension(16, 16));
        cancelButton.setMinimumSize(new java.awt.Dimension(16, 16));
        cancelButton.setPreferredSize(new java.awt.Dimension(16, 16));
        cancelButton.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    cancelButtonActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        statusPanel.add(cancelButton, gridBagConstraints);

        add(statusPanel, java.awt.BorderLayout.SOUTH);
    } // </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void commitButtonActionPerformed(final java.awt.event.ActionEvent evt) //GEN-FIRST:event_commitButtonActionPerformed
    {                                                                              //GEN-HEADEREND:event_commitButtonActionPerformed
        this.editorActivationDelegate.setPropertyChangeEnabled(true);
        this.stopEditing();
        this.editorActivationDelegate.setPropertyChangeEnabled(false);
    }                                                                              //GEN-LAST:event_commitButtonActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void cancelButtonActionPerformed(final java.awt.event.ActionEvent evt) //GEN-FIRST:event_cancelButtonActionPerformed
    {                                                                              //GEN-HEADEREND:event_cancelButtonActionPerformed
        this.editorActivationDelegate.setPropertyChangeEnabled(true);
        this.cancelCellEditing();
        this.editorActivationDelegate.setPropertyChangeEnabled(false);
    }                                                                              //GEN-LAST:event_cancelButtonActionPerformed

    @Override
    public void setValueChanged(final boolean valueChanged) {
        super.setValueChanged(valueChanged);
        /*if(valueChanged)
         * {  this.statusLabel.setText("\u00C4nderungen in Objekt '" + this.getName(this.getValue()) + "' festgestellt
         * ... "); } else { this.statusLabel.setText(null);}*/

        this.commitButton.setVisible(this.getParentContainer() != null);
        this.cancelButton.setVisible(this.getParentContainer() != null);
        this.commitButton.setEnabled((this.getParentContainer() != null) & this.isValueChanged());
        this.cancelButton.setEnabled(this.getParentContainer() != null);
    }

    // UI Methoden -------------------------------------------------------------

    @Override
    protected void initUI() {
        if (!this.init) {
            this.initComponents();
            this.init = true;
        } else if (logger.isDebugEnabled()) {
            logger.debug("initUI(" + this + "): ui already initialized"); // NOI18N
        }

        // das f\u00FCllt die editor map ....
        super.initUI();

        // alles neu:
        this.editorPanel.removeAll();
        this.titleLabel.setText(this.getName(this.getValue()));
        this.setValueChanged(false);

        final GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridy = -1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        gridBagConstraints.weighty = 0.0;

        final Map metaAttributes = this.getMetaObjectAttributes(this.getValue());
        if (metaAttributes != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("initUI(" + this + "): adding " + metaAttributes.size() + " to this complex editor ui"); // NOI18N
            }
            final Iterator iterator = metaAttributes.keySet().iterator();
            while (iterator.hasNext()) {
                final Object key = iterator.next();
                final Attribute attribute = (Attribute)metaAttributes.get(key);

                try {
                    /*if(attribute.referencesObject())
                     * { if(this.getAttributeValue(attribute) == null) {
                     * if(logger.isDebugEnabled())logger.warn("setValue(" + this + ") the value of this attribute is
                     * null, creating new empty meta object of type '" + attribute.getClassKey() + "'"); MetaClass
                     * metaClass = SessionManager.getProxy().getMetaClass(attribute.getClassKey());     Object
                     * MetaObject = SessionManager.getProxy().getInstance(metaClass);
                     * attribute.setValue(MetaObject); }}*/

                    this.addEditorUI(key, metaAttributes.get(key), gridBagConstraints);
                } catch (Throwable t) {
                    logger.error("setValue(" + this + ") could not create editor for attribute '" + attribute + "' ("
                                + key + ")",
                        t); // NOI18N
                }
            }
        } else {
            logger.error("initUI(" + this + "): no meta attributes found"); // NOI18N
        }
    }

    /**
     * Hilfsmethode, f\u00FCgt f\u00FCr jedes Attribut in diesem MetaObjekt einen neuen Editor hinzu.
     *
     * @param  id                  DOCUMENT ME!
     * @param  value               DOCUMENT ME!
     * @param  gridBagConstraints  DOCUMENT ME!
     */
    protected void addEditorUI(final Object id, final Object value, final GridBagConstraints gridBagConstraints) {
        Component editorComponent = null;

        if (this.getChildEditors().containsKey(id)) {
            final SimpleEditor simpleEditor = (SimpleEditor)this.getChildEditors().get(id);
            final Object complexEditorClass = simpleEditor.getProperty(SimpleEditor.PROPERTY_COMLPEX_EDTIOR);

            if (complexEditorClass != null) {
                if (logger.isInfoEnabled()) {
                    logger.info("addEditorUI(" + this + "): creating simple editor (" + id
                                + ") with complex editor support (" + complexEditorClass + ")"); // NOI18N
                }
                try {
                    final ComplexEditor complexEditor = (ComplexEditor)((Class)complexEditorClass).newInstance();
                    // complexEditor.setProperty(BasicEditor.PROPERTY_LOCALE,
                    // this.getProperty(BasicEditor.PROPERTY_LOCALE));
                    editorComponent = simpleEditor.getEditorComponent(this, complexEditor, id, value);
                } catch (Throwable t) {
                    logger.error("addEditorUI(" + this + "): could not register complex editor (" + id
                                + ") for simple editor",
                        t); // NOI18N
                    editorComponent = simpleEditor.getEditorComponent(this, id, value);
                }
            } else {
                if (logger.isInfoEnabled()) {
                    logger.info("addEditorUI(" + this + "): creating simple editor (" + id
                                + ") without complex editor support"); // NOI18N
                }
                editorComponent = simpleEditor.getEditorComponent(this, id, value);
            }

            simpleEditor.addEditorListener(this.editorHandler);
        } else {
            logger.error("addEditorUI(" + this + "): no editor found for object '" + id + "'"); // NOI18N
            editorComponent = new JLabel(org.openide.util.NbBundle.getMessage(
                        DefaultComplexMetaAttributeEditor.class,
                        "DefaultComplexMetaAttributeEditor.addEditorUI.editorComponent.NoEditorAvailableLabelText",
                        new Object[] { this }));                                                // NOI18N
        }

        gridBagConstraints.gridy++;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.2;
        this.editorPanel.add(new JLabel(this.getName(value)), gridBagConstraints);

        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.5;
        this.editorPanel.add(editorComponent, gridBagConstraints);
    }
    /**
     * XXX method not supported.
     *
     * @param  key    DOCUMENT ME!
     * @param  value  DOCUMENT ME!
     */
    @Override
    public void addValue(final java.lang.Object key, final java.lang.Object value) {
        logger.error("addValue() method not supported"); // NOI18N
    }
    /**
     * XXX method not supported.
     *
     * @param   key  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public java.lang.Object removeValue(final java.lang.Object key) {
        logger.error("removeValue() method not supported"); // NOI18N
        return null;
    }

    @Override
    public final ConnectionContext getConnectionContext() {
        return connectionContext;
    }

    /**
     * DefaultComplexMetaAttributeEditorMethoden ...........................................
     *
     * @param   anEvent    DOCUMENT ME!
     * @param   userGroup  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private boolean isEditable(final EventObject anEvent, final UserGroup userGroup) {
        final String key = userGroup.getKey().toString();

        try {
            if (this.getValue() instanceof Attribute) {
                // klasse besorgen

                final MetaClass metaClass = SessionManager.getProxy()
                            .getMetaClass(this.getMetaObject(this.getValue()).getClassKey(),
                                getConnectionContext());

                return (!this.readOnly & !((Attribute)this.getValue()).isPrimaryKey())
                            && metaClass.getPermissions().hasPermission(key, PermissionHolder.WRITEPERMISSION)
                            && this.getMetaObject(this.getValue()).getBean()
                            .hasObjectWritePermission(SessionManager.getSession().getUser());
                    // return !this.readOnly & !((Attribute)this.getValue()).isPrimaryKey() &
                    // ((Attribute)this.getValue()).getPermissions().hasPermission(key,Sirius.navigator.connection.SessionManager.getSession().getWritePermission());
            } else {
                final MetaClass metaClass = SessionManager.getProxy()
                            .getMetaClass(this.getMetaObject(this.getValue()).getClassKey(),
                                getConnectionContext());
                return (!this.readOnly
                                & metaClass.getPermissions().hasPermission(key, PermissionHolder.WRITEPERMISSION))
                            && this.getMetaObject(this.getValue()).getBean()
                            .hasObjectWritePermission(SessionManager.getSession().getUser());
            }
        } catch (Exception exp) {
            logger.error("isEditable() could not check permissions of object " + this.getValue(), exp); // NOI18N
        }

        return false;
    }

    @Override
    public boolean isEditable(final EventObject anEvent) {
        final User user = SessionManager.getSession().getUser();
        final UserGroup userGroup = user.getUserGroup();
        if (userGroup != null) {
            return isEditable(anEvent, userGroup);
        } else {
            for (final UserGroup potentialUserGroup : user.getPotentialUserGroups()) {
                if (isEditable(anEvent, potentialUserGroup)) {
                    return true;
                }
            }
            return false;
        }
    }
}
