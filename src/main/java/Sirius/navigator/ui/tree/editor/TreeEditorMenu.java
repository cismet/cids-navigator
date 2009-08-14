/*
 * TreeCommands.java
 *
 * Created on 30. August 2004, 13:40
 */
package Sirius.navigator.ui.tree.editor;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.event.*;

import Sirius.navigator.plugin.ui.*;
import Sirius.navigator.plugin.interfaces.PluginMethod;
import Sirius.navigator.ui.tree.*;
import Sirius.navigator.resource.*;
import Sirius.navigator.ui.*;
import Sirius.navigator.method.*;
import Sirius.navigator.types.treenode.*;
import Sirius.server.middleware.types.*;
import Sirius.navigator.connection.*;
import Sirius.server.newuser.permission.PermissionHolder;

import Sirius.server.newuser.permission.Policy;
import org.apache.log4j.Logger;

/**
 *
 * @author  pascal
 */
public class TreeEditorMenu extends PluginMenu {


    //Funktionalitaet wurde in MutablepopupMenu umgelagert
    //Diese Klasse kann gelöscht werden


//    private final MetaCatalogueTree metaCatalogueTree;
//    private final TreeNodeEditor treeNodeEditor;
//    private final ResourceManager resources;
//    private final Logger logger;

    /** Creates a new instance of TreeCommands */
    public TreeEditorMenu(JFrame mainWindow, MetaCatalogueTree metaCatalogueTree) {
        super(TreeEditorMenu.class.getName());
//
//        this.logger = Logger.getLogger(this.getClass());
//        this.resources = ResourceManager.getManager();
//
//        this.metaCatalogueTree = metaCatalogueTree;
//        this.treeNodeEditor = new TreeNodeEditor(mainWindow, true);
//
//        this.setText(ResourceManager.getManager().getString("tree.editor.menu.name"));
//        this.setIcon(ResourceManager.getManager().getIcon(ResourceManager.getManager().getString("tree.editor.menu.icon")));
//
//        Node node = metaCatalogueTree.getSelectedNode().getNode();
//        if (node.getId() != -1 && node.getDynamicChildrenStatement() == null) {
//            //Kein dynamischer Knoten, keine dynamischen Kinder
//            this.addItem(new NewNodeMethod());
//            this.addItem(new EditNodeMethod());
//            this.addItem(new DeleteNodeMethod());
//            this.addItem(new EditObjectMethod());
//        } else if (node instanceof MetaObjectNode && node.getId() == -1) {
//            //DynamicObjectNode
//
//            //EditObject
//            this.addItem(new EditObjectMethod());
//            //DeleteObject
//            this.addItem(new DeleteObjectMethod());
//        } else if (node instanceof MetaNode && node.getClassId() != -1) {
//            int classID = node.getClassId();
//            String domain = node.getDomain();
//            try {
//                this.addItem(new NewObjectMethod(classID, domain));
//            } catch (Exception e) {
//                logger.error("Error when adding the NewObjectMethodMenuItem",e);
//            }
//        }
//        this.addItem(new ExploreSubTreeMethod());
    }

//    protected class NewNodeMethod extends PluginMenuItem implements PluginMethod {
//
//        public NewNodeMethod() {
//            super(MethodManager.PURE_NODE);
//
//            this.pluginMethod = this;
//
//            this.setText(ResourceManager.getManager().getString("tree.editor.menu.new.name"));
//            this.setIcon(ResourceManager.getManager().getIcon(ResourceManager.getManager().getString("tree.editor.menu.new.icon")));
//        }
//
//        public String getId() {
//            return this.getClass().getName();
//        }
//
//        public void invoke() throws Exception {
//            DefaultMetaTreeNode selectedNode = metaCatalogueTree.getSelectedNode();
//
//            if (logger.isDebugEnabled()) {
//                logger.debug("NewNodeMethod: creating new node as parent of " + selectedNode);
//            }
//            if (selectedNode != null && selectedNode.isPureNode()) {
//                String key = SessionManager.getSession().getUser().getUserGroup().getKey().toString();
//                //Sirius.server.newuser.permission.Permission perm = SessionManager.getSession().getWritePermission();
//
//                if (selectedNode.getNode().getPermissions().hasPermission(key, PermissionHolder.WRITEPERMISSION)) {
//                    // knoten aufklappen
//                    if (!selectedNode.isLeaf() && !selectedNode.isExplored()) {
//                        if (logger.isDebugEnabled()) {
//                            logger.warn("NewNodeMethod: parent node is not explored");
//                        }
//                        try {
//                            metaCatalogueTree.expandPath(new TreePath(selectedNode.getPath()));
//                        //selectedNode.explore();
//                        //((DefaultTreeModel)metaCatalogueTree.getModel()).nodeStructureChanged(selectedNode);
//                        } catch (Exception exp) {
//                            logger.error("could not explore node: " + selectedNode, exp);
//                        }
//                    }
//
//                    treeNodeEditor.setLocationRelativeTo(ComponentRegistry.getRegistry().getMainWindow());
//                    DefaultMetaTreeNode metaTreeNode = treeNodeEditor.createTreeNode();
//
//                    if (metaTreeNode != null) {
//                        metaTreeNode.setNew(true);
//                        // damit beim Aufklappen nicht die explore methode aufgerufen wird
//                        metaTreeNode.setExplored(true);
//
//                        // das endg\u00FCltige Hinzuf\u00FCgen erledigt der Attribut Editor
//                        if (metaTreeNode.isObjectNode()) {
//                            if (!ComponentRegistry.getRegistry().getAttributeEditor().isChanged()) {
//                                MethodManager.getManager().addTreeNode(metaCatalogueTree, selectedNode, metaTreeNode);
//
//                                ComponentRegistry.getRegistry().showComponent(ComponentRegistry.ATTRIBUTE_EDITOR);
//                                ComponentRegistry.getRegistry().getAttributeEditor().setTreeNode(metaTreeNode);
//                            } else {
//                                logger.warn("could not create new object node: edited object still unsaved");
//                                JOptionPane.showMessageDialog(ComponentRegistry.getRegistry().getMainWindow(), "<html><p>Es kann kein neues Objekt erstellt werden, da z.Z. ein anderes Objekt bearbeitet wird.</p><p>Speichern sie das bearbeitete Objekt und versuchen sie es erneut.</p></html>", "Neues Objekt", JOptionPane.INFORMATION_MESSAGE);
//                            }
//                        } else {
//                            if (MethodManager.getManager().addNode(metaCatalogueTree, selectedNode, metaTreeNode)) {
//                                MethodManager.getManager().addTreeNode(metaCatalogueTree, selectedNode, metaTreeNode);
//                                metaTreeNode.setNew(false);
//                            } else if (logger.isDebugEnabled()) {
//                                logger.warn("addNode failed, omitting addTreeNode");
//                            }
//                        }
//                    }
//                } else {
//                    logger.warn("no permission to create node");
//                    JOptionPane.showMessageDialog(ComponentRegistry.getRegistry().getMainWindow(), ResourceManager.getManager().getString("tree.editor.menu.new.nopermission"), ResourceManager.getManager().getString("tree.editor.menu.new.nopermission.title"), JOptionPane.WARNING_MESSAGE);
//                }
//            } else {
//                logger.warn("parent node '" + selectedNode + "' is no pure node");
//                JOptionPane.showMessageDialog(ComponentRegistry.getRegistry().getMainWindow(), ResourceManager.getManager().getString("tree.editor.menu.new.nopurenode.1") + selectedNode + ResourceManager.getManager().getString("tree.editor.menu.new.nopurenode.2"), ResourceManager.getManager().getString("tree.editor.menu.new.nopurenode.title"), JOptionPane.WARNING_MESSAGE);
//            }
//        }
//    }
//
//
//    protected class NewObjectMethod extends PluginMenuItem implements PluginMethod {
//
//        int classID = -1;
//        String domain = null;
//        MetaClass metaClass=null;
//        public NewObjectMethod(int classID, String domain) throws Exception {
//            super(MethodManager.PURE_NODE);
//            this.classID = classID;
//            this.domain = domain;
//            this.pluginMethod = this;
//
//            metaClass = SessionManager.getProxy().getMetaClass(classID, domain);
//
//            this.setText(ResourceManager.getManager().getString("tree.editor.menu.newobject.name")+ " ("+metaClass.getName()+")");
//            this.setIcon(ResourceManager.getManager().getIcon(ResourceManager.getManager().getString("tree.editor.menu.newobject.icon")));
//            this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
//        }
//
//        public String getId() {
//            return this.getClass().getName();
//        }
//
//        public void invoke() throws Exception {
//            DefaultMetaTreeNode selectedNode = metaCatalogueTree.getSelectedNode();
//            if (metaClass.getPermissions().hasWritePermission(SessionManager.getSession().getUser().getUserGroup())){
//
//                MetaObject MetaObject=metaClass.getEmptyInstance();
//                MetaObject.setStatus(MetaObject.NEW);
//                MetaObjectNode MetaObjectNode = new MetaObjectNode(-1, SessionManager.getSession().getUser().getDomain(), MetaObject, null, null, true, Policy.createWIKIPolicy(), -1, null, false);
//                DefaultMetaTreeNode metaTreeNode = new ObjectTreeNode(MetaObjectNode);
//                ComponentRegistry.getRegistry().showComponent(ComponentRegistry.ATTRIBUTE_EDITOR);
//                ComponentRegistry.getRegistry().getAttributeEditor().setTreeNode(metaTreeNode);
//            }
//
//        }
//    }
//
//    protected class EditNodeMethod extends PluginMenuItem implements PluginMethod {
//
//        public EditNodeMethod() {
//            super(MethodManager.PURE_NODE + MethodManager.OBJECT_NODE + MethodManager.CLASS_NODE);
//
//            this.pluginMethod = this;
//
//            // XXX i18n
//            this.setText(ResourceManager.getManager().getString("Sirius.navigator.ui.tree.editor.TreeEditorMenu.EditNodeMethod.Text"));
//            this.setIcon(ResourceManager.getManager().getIcon(ResourceManager.getManager().getString("tree.editor.menu.edit.icon")));
//            this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
//        }
//
//        public String getId() {
//            return this.getClass().getName();
//        }
//
//        public void invoke() throws Exception {
//            DefaultMetaTreeNode selectedNode = metaCatalogueTree.getSelectedNode();
//            if (MethodManager.getManager().checkPermission(selectedNode.getNode(), PermissionHolder.WRITEPERMISSION)) {
//                if (selectedNode.getParent() != null && !(selectedNode.getParent() instanceof RootTreeNode)) {
//                    treeNodeEditor.editTreeNode(selectedNode);
//                    if (selectedNode.isChanged()) {
//                        MethodManager.getManager().updateNode(metaCatalogueTree, (DefaultMetaTreeNode) selectedNode.getParent(), selectedNode);
//
//                        selectedNode.setChanged(false);
//                        ((DefaultTreeModel) metaCatalogueTree.getModel()).nodeChanged(selectedNode);
//                    }
//                } else {
//                    logger.warn("can not rename top node " + selectedNode);
//                // XXX dialog ...
//                }
//            } else if (logger.isDebugEnabled()) {
//                logger.warn("insufficient permission to edit node " + selectedNode);
//            }
//        }
//    }
//
//    protected class EditObjectMethod extends PluginMenuItem implements PluginMethod {
//
//        public EditObjectMethod() {
//            super(MethodManager.OBJECT_NODE);
//
//            this.pluginMethod = this;
//
//            this.setText(ResourceManager.getManager().getString("tree.editor.menu.edit.object.name"));
//            this.setIcon(ResourceManager.getManager().getIcon(ResourceManager.getManager().getString("tree.editor.menu.edit.object.icon")));
//        }
//
//        public String getId() {
//            return this.getClass().getName();
//        }
//
//        public void invoke() throws Exception {
//            DefaultMetaTreeNode selectedNode = metaCatalogueTree.getSelectedNode();
//
//            MetaObjectNode mon = (MetaObjectNode) selectedNode.getNode();
////             SessionManager.getSession().getConnection().getMetaObject()
//
////            if()
//            if (MethodManager.getManager().checkPermission(mon, PermissionHolder.WRITEPERMISSION)) {
//                ComponentRegistry.getRegistry().showComponent(ComponentRegistry.ATTRIBUTE_EDITOR);
//                ComponentRegistry.getRegistry().getAttributeEditor().setTreeNode(selectedNode);
//            } else if (logger.isDebugEnabled()) {
//                logger.warn("insufficient permission to edit node " + selectedNode);
//            }
//        }
//    }
//
//    protected class DeleteNodeMethod extends PluginMenuItem implements PluginMethod {
//
//        public DeleteNodeMethod() {
//            super(MethodManager.PURE_NODE + MethodManager.CLASS_NODE + MethodManager.OBJECT_NODE);
//
//            this.pluginMethod = this;
//
//            this.setText(ResourceManager.getManager().getString("tree.editor.menu.delete.name"));
//            this.setIcon(ResourceManager.getManager().getIcon(ResourceManager.getManager().getString("tree.editor.menu.delete.icon")));
//        }
//
//        public String getId() {
//            return this.getClass().getName();
//        }
//
//        public void invoke() throws Exception {
//            DefaultMetaTreeNode selectedNode = metaCatalogueTree.getSelectedNode();
//            if (selectedNode != null && selectedNode.isLeaf()) {
//                if (MethodManager.getManager().checkPermission(selectedNode.getNode(), PermissionHolder.WRITEPERMISSION)) {
//                    MethodManager.getManager().deleteNode(metaCatalogueTree, selectedNode);
//                } else if (logger.isDebugEnabled()) {
//                    logger.warn("insufficient permission to delete node: " + selectedNode);
//                }
//            } else {
//                logger.warn("can not delete node, node is no leaf: " + selectedNode);
//                JOptionPane.showMessageDialog(ComponentRegistry.getRegistry().getMainWindow(), "Dieser Knoten kann nicht gel\u00F6scht werden, da er noch Kindknoten enth\u00E4lt", "Dieser Knoten kann nicht gel\u00F6scht werden", JOptionPane.INFORMATION_MESSAGE);
//            }
//        }
//    }
//
//    protected class DeleteObjectMethod extends PluginMenuItem implements PluginMethod {
//        //TODO es wird noch deleteNode aufgerufen
//
//        public DeleteObjectMethod() {
//            super(MethodManager.OBJECT_NODE);
//
//            this.pluginMethod = this;
//
//            this.setText(ResourceManager.getManager().getString("tree.editor.menu.deleteobject.name"));
//            this.setIcon(ResourceManager.getManager().getIcon(ResourceManager.getManager().getString("tree.editor.menu.deleteobject.icon")));
//        }
//
//        public String getId() {
//            return this.getClass().getName();
//        }
//
//        public void invoke() throws Exception {
//            DefaultMetaTreeNode selectedNode = metaCatalogueTree.getSelectedNode();
//            if (selectedNode != null && selectedNode.isLeaf()) {
//                if (MethodManager.getManager().checkPermission(selectedNode.getNode(), PermissionHolder.WRITEPERMISSION)) {
//                    MethodManager.getManager().deleteNode(metaCatalogueTree, selectedNode);
//                } else if (logger.isDebugEnabled()) {
//                    logger.warn("insufficient permission to delete node: " + selectedNode);
//                }
//            } else {
//                logger.warn("can not delete node, node is no leaf: " + selectedNode);
//                JOptionPane.showMessageDialog(ComponentRegistry.getRegistry().getMainWindow(), "Dieser Knoten kann nicht gel\u00F6scht werden, da er noch Kindknoten enth\u00E4lt", "Dieser Knoten kann nicht gel\u00F6scht werden", JOptionPane.INFORMATION_MESSAGE);
//            }
//        }
//    }
//
//    protected class ExploreSubTreeMethod extends PluginMenuItem implements PluginMethod {
//
//        public ExploreSubTreeMethod() {
//            super(Long.MAX_VALUE);
//
//            this.pluginMethod = this;
//
//            this.setText(ResourceManager.getManager().getString("tree.editor.menu.reload.name"));
//            this.setIcon(ResourceManager.getManager().getIcon(ResourceManager.getManager().getString("tree.editor.menu.reload.icon")));
//            this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
//        }
//
//        public String getId() {
//            return this.getClass().getName();
//        }
//
//        public void invoke() throws Exception {
//
//            final TreePath selectionPath = metaCatalogueTree.getSelectionPath();
//            RootTreeNode rootTreeNode = new RootTreeNode(SessionManager.getProxy().getRoots());
//
//            ((DefaultTreeModel) metaCatalogueTree.getModel()).setRoot(rootTreeNode);
//            ((DefaultTreeModel) metaCatalogueTree.getModel()).reload();
//
//            metaCatalogueTree.exploreSubtree(selectionPath);
//        }
//    }
}
