/*
 * DefaultComplexMetaAttributeArrayEditor.java
 *
 * Created on 21. Oktober 2004, 10:33
 */

package Sirius.navigator.ui.attributes.editor.metaobject;

import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import java.awt.*;

import org.apache.log4j.Logger;

import Sirius.navigator.ui.attributes.editor.*;
import Sirius.server.localserver.attribute.Attribute;
import Sirius.server.localserver.attribute.MemberAttributeInfo;
import Sirius.server.middleware.types.*;
import Sirius.navigator.connection.*;
import Sirius.server.localserver.attribute.ObjectAttribute;
import Sirius.server.newuser.permission.Policy;
import Sirius.navigator.resource.ResourceManager;

/**
 *
 * @author  pascal
 */
public class DefaultComplexMetaAttributeArrayEditor extends AbstractComplexMetaAttributeEditor
{
    private static final ResourceManager resource = ResourceManager.getManager();
    protected ActionListener addListener;
    protected ActionListener removeListener;
    
    protected Map arrayAttributeMap;
    
    /** Creates new form DefaultComplexMetaAttributeArrayEditor */
    public DefaultComplexMetaAttributeArrayEditor()
    {
        this.logger = Logger.getLogger(this.getClass());
        
        this.editorActivationDelegate = new ComplexEditorActivationDelegate();
        this.editorUIDelegate = new ComplexEditorUIDelegate();
        this.editorHandler = new ComplexEditorHandler();
        this.editorLocator = new MetaAttributeEditorLocator();
        
        this.addListener = new AddListener();
        this.removeListener = new RemoveListener();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        statusLabel = new javax.swing.JLabel();
        titleLabel = new javax.swing.JLabel();
        javax.swing.JPanel labelPanel = new javax.swing.JPanel();
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
        labelPanel.setLayout(new java.awt.GridLayout(1, 0));
        add(labelPanel, java.awt.BorderLayout.NORTH);

        editorPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10), javax.swing.BorderFactory.createEtchedBorder()));
        editorPanel.setLayout(new java.awt.GridBagLayout());
        add(editorPanel, java.awt.BorderLayout.CENTER);

        statusPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 10, 10));
        statusPanel.setLayout(new java.awt.GridBagLayout());

        commitButton.setIcon(resource.getIcon("save_objekt.gif"));
        commitButton.setToolTipText(org.openide.util.NbBundle.getMessage(DefaultComplexMetaAttributeArrayEditor.class, "DefaultComplexMetaAttributeArrayEditor.commitButton.tooltip")); // NOI18N
        commitButton.setActionCommand("commit"); // NOI18N
        commitButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        commitButton.setContentAreaFilled(false);
        commitButton.setEnabled(false);
        commitButton.setFocusPainted(false);
        commitButton.setMaximumSize(new java.awt.Dimension(16, 16));
        commitButton.setMinimumSize(new java.awt.Dimension(16, 16));
        commitButton.setPreferredSize(new java.awt.Dimension(16, 16));
        commitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                commitButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        statusPanel.add(commitButton, gridBagConstraints);

        cancelButton.setIcon(resource.getIcon("zurueck_objekt.gif"));
        cancelButton.setToolTipText(org.openide.util.NbBundle.getMessage(DefaultComplexMetaAttributeArrayEditor.class, "DefaultComplexMetaAttributeArrayEditor.cancelButton.tooltip")); // NOI18N
        cancelButton.setActionCommand("cancel"); // NOI18N
        cancelButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        cancelButton.setContentAreaFilled(false);
        cancelButton.setEnabled(false);
        cancelButton.setFocusPainted(false);
        cancelButton.setMaximumSize(new java.awt.Dimension(16, 16));
        cancelButton.setMinimumSize(new java.awt.Dimension(16, 16));
        cancelButton.setPreferredSize(new java.awt.Dimension(16, 16));
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        statusPanel.add(cancelButton, gridBagConstraints);

        add(statusPanel, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents
    
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cancelButtonActionPerformed
    {//GEN-HEADEREND:event_cancelButtonActionPerformed
        this.editorActivationDelegate.setPropertyChangeEnabled(true);
        this.cancelCellEditing();
        this.editorActivationDelegate.setPropertyChangeEnabled(false);
    }//GEN-LAST:event_cancelButtonActionPerformed
    
    private void commitButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_commitButtonActionPerformed
    {//GEN-HEADEREND:event_commitButtonActionPerformed
        this.editorActivationDelegate.setPropertyChangeEnabled(true);
        this.stopEditing();
        this.editorActivationDelegate.setPropertyChangeEnabled(false);
    }//GEN-LAST:event_commitButtonActionPerformed
    
    public void setValueChanged(boolean valueChanged)
    {
        super.setValueChanged(valueChanged);
        this.commitButton.setVisible(this.getParentContainer() != null);
        this.cancelButton.setVisible(this.getParentContainer() != null);
        this.commitButton.setEnabled(this.getParentContainer() != null & this.isValueChanged());
        this.cancelButton.setEnabled(this.getParentContainer() != null);
    }
    
    // UI Methoden -------------------------------------------------------------
    
    protected void initUI()
    {
        if(!this.init)
        {
            this.initComponents();
            this.init = true;
        }
        else if(logger.isDebugEnabled())
        {
            logger.debug("initUI(" + this + "): ui already initialized");//NOI18N
        }
        
        this.arrayAttributeMap = new ArrayAttributeMap(this.getMetaObject(this.getValue()));
        
        // das f\u00FCllt die editor map ............................................
        // super.initUI();
        // bei arrays ist alles ein bischen anders:
        
        if(this.getValue() != null)
        {
            if(this.getChildEditors() != null && this.getChildEditors().size() > 0 && this.editorHandler != null)
            {
                if(logger.isDebugEnabled())logger.debug("initUI(" + this.getId() + "): removing editor listeners from previous session");//NOI18N
                Iterator iterator = this.getChildEditors().values().iterator();
                while(iterator.hasNext())
                {
                    ((BasicEditor)iterator.next()).removeEditorListener(this.editorHandler);
                }
            }
            
            // Objekt nach editoren untersuchen
            // hier die array helper objekte aussortieren:
            this.childrenMap = this.editorLocator.getEditors(this.getMetaObjectAttributes(this.getValue()));
            if(logger.isDebugEnabled())logger.debug("initUI(): " + this.childrenMap.size() + " child editors initialized");//NOI18N
        }
        else
        {
            logger.warn("initUI(): value is null, no child editors available");//NOI18N
            this.childrenMap = new HashMap();
        }
        
        // das f\u00FCllt die editor map ............................................
        
        // alles neu:
        this.editorPanel.removeAll();
        this.titleLabel.setText(this.getName(this.getValue()));
        this.setValueChanged(false);
        
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridy = -1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        gridBagConstraints.weighty = 0.0;
        
        Map metaAttributes = this.getMetaObjectAttributes(this.getValue());
        if(metaAttributes != null)
        {
            if(logger.isDebugEnabled())logger.debug("initUI(" + this + "): adding " + metaAttributes.size() + " to this complex editor ui");//NOI18N
            Iterator iterator = metaAttributes.keySet().iterator();
            while(iterator.hasNext())
            {
                Object key = iterator.next();
                if(logger.isDebugEnabled())logger.debug("initUI(" + this + "): adding editor ui for attribute " + key);//NOI18N
                this.addEditorUI(key, metaAttributes.get(key), gridBagConstraints);
            }
            
            JButton addButton = new JButton(org.openide.util.NbBundle.getMessage(DefaultComplexMetaAttributeArrayEditor.class, "DefaultComplexMetaAttributeArrayEditor.initUI.addButton.text"));//NOI18N
            addButton.addActionListener(this.addListener);
            
            gridBagConstraints.gridy++;
            gridBagConstraints.gridx=2;
            gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
            gridBagConstraints.weightx = 0.0;
            this.editorPanel.add(addButton, gridBagConstraints);
        }
        else
        {
            logger.error("initUI(" + this + "): no meta attributes found");//NOI18N
        }
        
        this.editorPanel.validate();
        this.editorPanel.repaint();
    }
    
    /**
     * Hilfsmethode, f\u00FCgt f\u00FCr jedes Attribut in diesem MetaObjekt einen neuen
     * Editor hinzu.
     */
    protected void addEditorUI(Object id, Object value, GridBagConstraints gridBagConstraints)
    {
        Component editorComponent = null;
        
        if(this.getMetaObject(value) == null || (this.getMetaObject(value) != null && this.getMetaObject(value).getStatus() != MetaObject.TO_DELETE))
        {
            if(this.getChildEditors().containsKey(id))
            {
                SimpleEditor simpleEditor = (SimpleEditor)this.getChildEditors().get(id);
                Object complexEditorClass =  simpleEditor.getProperty(SimpleEditor.PROPERTY_COMLPEX_EDTIOR);
                
                if(complexEditorClass != null)
                {
                    if(logger.isDebugEnabled())logger.debug("addEditorUI(" + this + "): creating simple editor with complex editor support (" + complexEditorClass + ")");//NOI18N
                    try
                    {
                        ComplexEditor complexEditor = (ComplexEditor)((Class)complexEditorClass).newInstance();
                        //complexEditor.setProperty(BasicEditor.PROPERTY_LOCALE, this.getProperty(BasicEditor.PROPERTY_LOCALE));
                        
                        editorComponent = simpleEditor.getEditorComponent(this, complexEditor, id, value);
                    }
                    catch(Throwable t)
                    {
                        logger.error("addEditorUI(" + this + "): could not register complex editor for simple editor", t);//NOI18N
                        editorComponent = simpleEditor.getEditorComponent(this, id, value);
                    }
                }
                else
                {
                    if(logger.isDebugEnabled())logger.debug("addEditorUI(" + this + "): creating simple editor without complex editor support");//NOI18N
                    editorComponent = simpleEditor.getEditorComponent(this, id, value);
                }
                
                simpleEditor.addEditorListener(this.editorHandler);
            }
            else
            {
                logger.error("addEditorUI(" + this + "): no editor found for object '" + id + "'");//NOI18N
                editorComponent = new JLabel(org.openide.util.NbBundle.getMessage(DefaultComplexMetaAttributeArrayEditor.class, "DefaultComplexMetaAttributeArrayEditor.addEditorUI.editorComponent.NoEditorAvailableLabelText", new Object[]{this}));//NOI18N
            }
            
            JButton removeButton = new JButton(org.openide.util.NbBundle.getMessage(DefaultComplexMetaAttributeArrayEditor.class, "DefaultComplexMetaAttributeArrayEditor.addEditorUI.removeButton.text"));//NOI18N
            removeButton.setActionCommand(id.toString());
            removeButton.addActionListener(this.removeListener);
            
            gridBagConstraints.gridy++;
            gridBagConstraints.gridx = 0;
            gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
            gridBagConstraints.weightx = 0.2;
            this.editorPanel.add(new JLabel(this.getName(value)), gridBagConstraints);
            
            gridBagConstraints.gridx++;
            gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints.weightx = 0.8;
            this.editorPanel.add(editorComponent, gridBagConstraints);
            
            gridBagConstraints.gridx++;
            gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
            gridBagConstraints.weightx = 0.0;
            this.editorPanel.add(removeButton, gridBagConstraints);
        }
        else if(logger.isDebugEnabled())
        {
            logger.warn("attribute '" + id + "' is marked to be deleted, ignoring attribute in editor ui");//NOI18N
        }
    }
    
    public void addValue(java.lang.Object key, java.lang.Object value)
    {
        if(value instanceof ObjectAttribute)
        {
            Map MetaObjectAttributes = this.getMetaObjectAttributes(this.getValue());
            if(MetaObjectAttributes.containsKey(key))
            {
                logger.error("addValue(): editor " + key + " already in map");//NOI18N
            }
            else
            {
                if(logger.isDebugEnabled())logger.debug("addValue(): adding new array element " + key);//NOI18N
                
                try
                {
                    //this.getMetaObjectAttributes(this.getValue()).put(key, value);
                    this.getMetaObject(this.getValue()).addAttribute((ObjectAttribute)value);
                }
                catch(Throwable t)
                {
                    logger.error("addValue(): could not add attribute '" + key + "'", t);//NOI18N
                }
            }
        }
        else
        {
            logger.warn("addValue(): object '" + key + "' is no object attribute");//NOI18N
        }
    }
    
    public java.lang.Object removeValue(java.lang.Object key)
    {
        Map MetaObjectAttributes = this.getMetaObjectAttributes(this.getValue());
        if(!MetaObjectAttributes.containsKey(key))
        {
            logger.error("removeValue(): attribute " + key + "not in map");//NOI18N
        }
        else
        {
            if(logger.isDebugEnabled())logger.debug("remove array element " + key);//NOI18N
            return MetaObjectAttributes.remove(key);
            //return this.getMetaObject(this.getValue()).getAttributes().remove(key);
      
        }
        
        return null;
    }
    
    // DefaultComplexMetaAttributeEditorMethoden ...........................................
    
    public boolean isEditable(java.util.EventObject anEvent)
    {
        //hier m\u00F6glicherweise Berechtigungen abfragen
        return true;
    }
    
    protected Map getMetaObjectAttributes(Object value)
    {
        if(value == this.getValue())
        {
            if(logger.isDebugEnabled())logger.debug("getMetaObjectAttributes(): return array attributes");//NOI18N
            return this.arrayAttributeMap;
        }
        else
        {
            logger.warn("getMetaObjectAttributes(): return array helper attributes");//NOI18N
            return super.getMetaObjectAttributes(value);
        }
    }
    
    /*public void setValue(java.lang.Object key, java.lang.Object value)
    {
        super.setValue(key, value);
        this.initUI();
        this.setValueChanged(true);
    }*/
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton commitButton;
    private javax.swing.JPanel editorPanel;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
    
    
    protected class AddListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if(logger.isDebugEnabled())logger.info("AddListener: adding attribute");//NOI18N
            try
            {
                // root objekt
                MetaObject MetaObject = DefaultComplexMetaAttributeArrayEditor.this.getMetaObject(DefaultComplexMetaAttributeArrayEditor.this.getValue());
               
                // attribute das das arrayobjekt enth\u00E4lt
                ObjectAttribute attribute = (ObjectAttribute)DefaultComplexMetaAttributeArrayEditor.this.getValue();
                
                // besorge klasse der ArryElementReferenzen (referenztabelle)
                MetaClass metaClass = SessionManager.getProxy().getMetaClass(String.valueOf(attribute.getClassID())  + '@' + MetaObject.getDomain());
                
                // attributeinfo f\u00FCr arrayattribut
                MemberAttributeInfo memberAttributeInfo = (MemberAttributeInfo)metaClass.getMemberAttributeInfos().get(attribute.getKey());
                
                // klasse des referenzobjektes besorgen
                MetaClass arrayHelperClass = SessionManager.getProxy().getMetaClass(String.valueOf(memberAttributeInfo.getForeignKeyClassId()) + "@" + MetaObject.getDomain());//NOI18N
               
                // referenzObjekt besorgen
                MetaObject arrayHelperObject = DefaultComplexMetaAttributeArrayEditor.this.getMetaObjectInstance(arrayHelperClass.getKey());
                
                arrayHelperObject.setStatus(MetaObject.NEW);

                if (logger.isInfoEnabled()) {
                    logger.info("AddListener(): arrayHelperObject.hashCode(): " + arrayHelperObject.hashCode());//NOI18N
                }
                ObjectAttribute arrayHelperAttribute = new ObjectAttribute(String.valueOf(Math.random()), memberAttributeInfo, arrayHelperObject.getID(), arrayHelperObject,Policy.createWIKIPolicy());
                
                
                // id from setzen:
                Iterator iterator = arrayHelperObject.getAttributes().values().iterator();
                while(iterator.hasNext())
                {
                    Attribute attr = (Attribute)iterator.next();
                    
                    if(attr.referencesObject())// arrayElement
                    {
                        if(logger.isDebugEnabled())logger.debug("AddListener: adding array element object '" + arrayHelperObject.getName() + "'");//NOI18N
                        memberAttributeInfo = (MemberAttributeInfo)arrayHelperClass.getMemberAttributeInfos().get(attr.getKey());
                        MetaObject arrayElementObject = DefaultComplexMetaAttributeArrayEditor.this.getMetaObjectInstance(memberAttributeInfo.getForeignKeyClassId()  + "@" + MetaObject.getDomain());//NOI18N
                        
                        
                        arrayElementObject.setStatus(MetaObject.NEW);
                        //arrayElementObject.setStatus(MetaObject.NO_STATUS);
                        attr.setValue(arrayElementObject);
                        if (logger.isInfoEnabled()) {
                            logger.info("AddListener(): arrayElementAttribute.hashCode(): " + attr.hashCode());//NOI18N
                        }
                    }
                    else if(!attr.isPrimaryKey())
                    {
                        if(logger.isDebugEnabled())logger.debug("AddListener: setting value of id from attribute '" + attr.getName() + "' to " + MetaObject.getID());//NOI18N
                        attr.setValue(new Integer(MetaObject.getID()));
                    }
                }
                
                DefaultComplexMetaAttributeArrayEditor.this.addValue(arrayHelperAttribute.getID(), arrayHelperAttribute);
                DefaultComplexMetaAttributeArrayEditor.this.initUI();
                DefaultComplexMetaAttributeArrayEditor.this.setValueChanged(true);
            }
            catch(Throwable t)
            {
                logger.error("AddListener: could not add new attribute", t);//NOI18N
            }
        }
    }
    
    protected class RemoveListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            String key = e.getActionCommand();
            if(logger.isInfoEnabled())logger.info("RemoveListener: removing attribute " + key);//NOI18N
            
            if(DefaultComplexMetaAttributeArrayEditor.this.getChildEditors().containsKey(key))
            {
                DefaultComplexMetaAttributeArrayEditor.this.removeValue(key);
                DefaultComplexMetaAttributeArrayEditor.this.initUI();
                DefaultComplexMetaAttributeArrayEditor.this.setValueChanged(true);
            }
            else
            {
                logger.error("actionPerformed(): editor " + key + "not in map");//NOI18N
            }
        }
    }
    
    protected class ArrayAttributeMap extends LinkedHashMap
    {
        protected Logger logger;
        protected MetaObject metaArrayContainerObject;
        
    public ArrayAttributeMap(MetaObject metaArrayContainerObject)
        {
            this.logger = Logger.getLogger(this.getClass());
            this.metaArrayContainerObject = metaArrayContainerObject;
            
            Iterator arrayHelperIterator = metaArrayContainerObject.getAttributes().keySet().iterator();
            while(arrayHelperIterator.hasNext())
            {
                Object arrayHelperKey = arrayHelperIterator.next();
                Attribute arrayHelperAttribute = (Attribute)metaArrayContainerObject.getAttributes().get(arrayHelperKey);
                this.put(arrayHelperKey, arrayHelperAttribute);  
            }
        }
        
        public Object get(Object key)
        {
            //this.logger.debug("get(" + key + ")");
            return super.get(key);
        }
        
        public Object put(Object key, Object value)
        {
            Attribute arrayHelperAttribute = (Attribute)value;
            //this.logger.debug("put(" + key + ", " + value + ")");
            
            if(arrayHelperAttribute.getValue() != null && MetaObject.class.isAssignableFrom(arrayHelperAttribute.getValue().getClass()))
            {
                //if(this.logger.isDebugEnabled())this.logger.debug("ArrayAttributeMap(): array helper object " + arrayHelperKey + " (" + arrayHelperAttribute.getName() + "): '" + arrayHelperAttribute + "'");
                //logger.info("ArrayAttributeMap(): arrayHelperObject.hashCode(): " + arrayHelperAttribute.getValue().hashCode());
                MetaObject arrayHelperObject = (MetaObject)arrayHelperAttribute.getValue();
                Iterator arrayIterator = arrayHelperObject.getAttributes().keySet().iterator();
                
                while(arrayIterator.hasNext())
                {
                    Object arrayElementKey = arrayIterator.next();
                    //logger.debug("get arrayElementKey: " + arrayElementKey);
                    Attribute arrayAttribute = (Attribute)arrayHelperObject.getAttributes().get(arrayElementKey);
                    if(arrayAttribute != null && arrayAttribute.referencesObject() && arrayAttribute.getValue() != null)
                    {
                        if(this.logger.isDebugEnabled())this.logger.debug("ArrayAttributeMap(): array object " + arrayAttribute.getKey() + " (" + arrayAttribute.getName() + "): '" + arrayAttribute + "'");//NOI18N
                        logger.info("ArrayAttributeMap(): arrayElementAttribute.hashCode(): " + key + " = "+ arrayAttribute.hashCode());//NOI18N
                        
                        if(this.containsValue(arrayAttribute))
                        {
                            logger.error(arrayAttribute + " already in map");//NOI18N
                        }
                        
                        return super.put(key, arrayAttribute);
                    }
                }
            }
            else
            {
                this.logger.error("ArrayAttributeMap(): array helper object '" + arrayHelperAttribute.getName() + "' is null or not of type MetaObject");//NOI18N
            }
            
            return null;
        }
        
        public Object remove(Object key)
        {
            if(logger.isDebugEnabled())this.logger.debug("remove(): setting attribute to delete: " + key);//NOI18N
            
            MetaObject MetaObject = DefaultComplexMetaAttributeArrayEditor.this.getMetaObject(this.metaArrayContainerObject.getAttributes().get(key));
            if(MetaObject.getStatus() == MetaObject.NEW)
            {
                if(logger.isDebugEnabled())this.logger.debug("remove(): meta attribute is new, deleting meta attribute");//NOI18N
                this.metaArrayContainerObject.getAttributes().remove(key);
            }
            else
            {
                MetaObject.setStatus(MetaObject.TO_DELETE);
                DefaultComplexMetaAttributeArrayEditor.this.getMetaObject(this.get(key)).setStatus(MetaObject.TO_DELETE);
            }
            
            return super.remove(key);
        }
    }
}
