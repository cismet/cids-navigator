package Sirius.navigator.types.treenode;

/*
// header - edit "Data/yourJavaHeader" to customize
// contents - edit "EventHandlers/Java file/onCreate" to customize
//
 */

import javax.swing.ImageIcon;
import javax.swing.tree.*;

import Sirius.server.middleware.types.*;
import Sirius.navigator.resource.*;

import java.util.ResourceBundle;

public class RootTreeNode extends DefaultMetaTreeNode
{
    private final TreeNodeLoader treeNodeLoader;
    private static final ResourceBundle I18N = ResourceBundle.getBundle("Sirius/navigator/resource/i18n/resources");
    
    public RootTreeNode() //throws Exception
    {
        super(null);
        this.treeNodeLoader = new DefaultTreeNodeLoader();
    }
    
    /**
     * Dieser Konstruktor erzeugt einen neue RootNode bzw. DefaultMetaTreeNode und heangt
     * auch schon einige Children an. Mit diesem Konstruktor ist es also moeglich
     * einen kompletten Baum in einen bestehenden Baum einzuhaengen.<br>
     * Als RootNode sollte er nicht angezeigt werden.
     * (JTree.setRootVisible(false);)
     *
     * @param topNodes ein Array von Nodes
     */
    public RootTreeNode(Node[] topNodes) //throws Exception
    {
        this(topNodes, new DefaultTreeNodeLoader());
    }
    
    public RootTreeNode(Node[] topNodes, TreeNodeLoader treeNodeLoader) //throws Exception
    {
        super(null);
        this.treeNodeLoader = treeNodeLoader;
        this.setAllowsChildren(true);
        
        this.addChildren(topNodes);
    }
    
    public void addChildren(Node[] topNodes)
    {
        this.removeAllChildren();
        try
        {
            this.treeNodeLoader.addChildren(this, topNodes);
        }
        catch(Exception exp)
        {
            logger.error("could not add children", exp);
        }   
    }
    
    public boolean isLeaf()
    {
        return false;
    }
    
    //----------------------------------------------------------------------------
    
    
    public TreeNodeLoader getTreeNodeLoader()
    {
        return this.treeNodeLoader;
    }
    
    //----------------------------------------------------------------------------
    
    public synchronized void explore() throws Exception
    {}
    
    public boolean isRootNode()
    {return true;}
    
    public boolean isWaitNode()
    {return false;}
    
    public boolean isPureNode()
    {return false;}
    
    public boolean isClassNode()
    {return false;}
    
    public boolean isObjectNode()
    {return false;}
    
    //----------------------------------------------------------------------------
    
    public boolean isExplored()
    {return true;}
    
    //----------------------------------------------------------------------------
    
    public String toString()
    { 
        return I18N.getString("Sirius.navigator.types.treenode.RootTreeNode.toString().returnValue");
    }
    
    public String getDescription()
    {
        return I18N.getString("Sirius.navigator.types.treenode.RootTreeNode.getDescription().returnValue");
    }
    
    /**
     * @deprecated
     */
    public boolean equalsNode(Node node)
    {
        //logger.warn("method 'equalsNode()' should not be called on RootNode");
        return false;
    }
    
    public boolean equals(DefaultMetaTreeNode node)
    {
        //logger.warn("method 'equals()' should not be called on RootNode");
        return false;
    }
    
    public ImageIcon getOpenIcon()
    {
        //logger.warn("method 'getOpenIcon()' should not be called on RootNode");
        return null;
    }
    
    public ImageIcon getClosedIcon()
    {
        //logger.warn("method 'getClosedIcon()' should not be called on RootNode");
        return null;
    }
    
    public ImageIcon getLeafIcon()
    {
        //logger.warn("method 'getLeafIcon()' should not be called on RootNode");
        return null;
    }
    
    public int getID()
    {
        logger.warn("method 'getID()' should not be called on RootNode");
        return -1;
    }
    
    public String getDomain()
    {
        logger.warn("method 'getDomain()' should not be called on RootNode");
        return null;
    }

    @Override
    public int getClassID() {
        return -1;
    }


    
    public String getKey() throws Exception 
    {
        return null;
    }
    
    private final static class DefaultTreeNodeLoader implements TreeNodeLoader
    {
        public boolean addChildren(DefaultMetaTreeNode node) throws Exception
        {
            return this.addChildren(node, node.getChildren());
        }
        
        public boolean addChildren(DefaultMetaTreeNode node, Node[] children) throws Exception
        {
            boolean explored = true;
            
           // if(logger.isDebugEnabled())logger.debug("[DefaultTreeNodeLoader] Begin addChildren("+children.length+")");
            // WaitNode entfernen!
            node.removeChildren();
            
            if(children == null)
                return false;
            
            for (int i = 0; i < children.length; i++)
            {
                if (children[i] instanceof MetaNode)
                {
                    node.add(new PureTreeNode((MetaNode)children[i]));
                    explored &= children[i].isValid();
                    //if(logger.isDebugEnabled())logger.debug("[DefaultTreeNodeLoader] PureNode Children added");
                }
                else if (children[i] instanceof MetaClassNode)
                {
                    node.add(new ClassTreeNode((MetaClassNode)children[i]));
                    explored &= children[i].isValid();
                    //if(logger.isDebugEnabled())logger.debug("[DefaultTreeNodeLoader] ClassNode Children added");
                }
                else if (children[i] instanceof MetaObjectNode)
                {
                    node.add(new ObjectTreeNode((MetaObjectNode)children[i]));
                    explored &= children[i].isValid();
                    //if(logger.isDebugEnabled())logger.debug("[DefaultTreeNodeLoader] ObjectNode Children added");
                }
                else
                {
                    logger.fatal("[DefaultTreeNodeLoader] Wrong Node Type: '" + children[i] + "'");
                    throw new Exception("[DDefaultTreeNodeLoader] Wrong Node Type: '" + children[i] + "'");
                }
            }
            
            return explored;
        }
    }   
}
