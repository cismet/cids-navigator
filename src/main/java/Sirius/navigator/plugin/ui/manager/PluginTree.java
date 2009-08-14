/*
 * PluginTree.java
 *
 * Created on 11. Mai 2003, 16:49
 */

package Sirius.navigator.plugin.ui.manager;

import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;

import Sirius.navigator.resource.*;
import Sirius.navigator.plugin.*;

/**
 *
 * @author  Peter Alzheimer
 */
public class PluginTree extends JTree
{   
    private final PluginTreeNode rootNode;
    
    /** Holds value of property initialized. (lazy initialization) */
    private boolean initialized;
    
    /** Creates a new instance of PluginTree */
    public PluginTree() 
    {
        super();
        
        rootNode = new PluginTreeNode();
        this.setModel(new DefaultTreeModel(rootNode));
        this.setCellRenderer(new PluginTreeNodeRenderer());
        this.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        this.setEditable(false);
        this.setRootVisible(true);
    }
    
    protected void init()
    {
        rootNode.removeAllChildren();
        
        Iterator iterator = PluginRegistry.getRegistry().getPluginDescriptors();
        while(iterator.hasNext())
        {
            rootNode.add(new PluginTreeNode((PluginDescriptor)iterator.next()));
        }
        
        if(SwingUtilities.isEventDispatchThread())
        {
            ((DefaultTreeModel)this.getModel()).nodeStructureChanged(rootNode);
        }
        else
        {
            SwingUtilities.invokeLater(new Runnable()
            {
               public void run()
               {
                   ((DefaultTreeModel)PluginTree.this.getModel()).nodeStructureChanged(rootNode);
               }
            });
        }
        
        this.setInitialized(true);
    }
    
    public PluginTreeNode getSelectedNode()
    {
        Object object = null;
        TreePath treePath = this.getSelectionPath();

        if(treePath != null && ((object = treePath.getLastPathComponent()) != null))
        {
            return (PluginTreeNode)object;
        }
        
        return null;
    }
   
    /** Getter for property initialized.
     * @return Value of property initialized.
     *
     */
    public boolean isInitialized()
    {
        return this.initialized;
    }
    
    /** Setter for property initialized.
     * @param initialized New value of property initialized.
     *
     */
    private void setInitialized(boolean initialized)
    {
        this.initialized = initialized;
    }
    
    private class PluginTreeNodeRenderer extends DefaultTreeCellRenderer
    {
        public Component getTreeCellRendererComponent(JTree tree, java.lang.Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus)
        {
            super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

            if(value instanceof PluginTreeNode)
            {
                this.setIcon(((PluginTreeNode)value).getIcon());
            }
            
            return this;
        }   
    }
    
    protected class PluginTreeNode extends DefaultMutableTreeNode
    {
        private final Icon icon;
        private final PluginDescriptor pluginDescriptor;
        private final PluginMethodDescriptor methodDescriptor;
        
        
        public PluginTreeNode()
        {
            super(ResourceManager.getManager().getString("plugin.tree.rootnode"));
            this.pluginDescriptor = null;
            this.methodDescriptor = null;
            this.icon = ResourceManager.getManager().getIcon("plugin_node_root.gif");
        }
        
        public PluginTreeNode(PluginDescriptor pluginDescriptor)
        {
            super(pluginDescriptor.getName());
            this.pluginDescriptor = pluginDescriptor;
            this.methodDescriptor = null;
            this.icon = ResourceManager.getManager().getIcon("plugin_node_plugin.gif");
            
            if(pluginDescriptor.isPluginMethodsAvailable())
            {
                this.addMethods();
            }
        }
        
        public PluginTreeNode(PluginMethodDescriptor methodDescriptor)
        {
            super(methodDescriptor.getName());
            this.pluginDescriptor = null;
            this.methodDescriptor = methodDescriptor;
            this.icon = ResourceManager.getManager().getIcon("plugin_node_method.gif");
        }
        
        protected void addMethods()
        {
            Iterator iterator = this.pluginDescriptor.getMethodDescriptors();
            while(iterator.hasNext())
            {
                this.add(new PluginTreeNode((PluginMethodDescriptor)iterator.next()));
            }
        }
        
        // .....................................................................
        
        
        public Icon getIcon()
        {
            return this.icon;
        }
        
        public boolean isPluginNode()
        {
            return this.pluginDescriptor != null ? true : false;
        }
        
        public boolean isPluginMethodNode()
        {
            return this.methodDescriptor != null ? true : false;
        }
        
        public PluginDescriptor getPluginDescriptor()
        {
            return this.pluginDescriptor;
        }
        
        public PluginMethodDescriptor getPluginMethodDescriptor()
        {
            return this.methodDescriptor;
        }
    }    
}
