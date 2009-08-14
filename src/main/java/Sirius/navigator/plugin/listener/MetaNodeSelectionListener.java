package Sirius.navigator.plugin.listener;

import javax.swing.tree.*;
import javax.swing.event.*;
import java.util.*;

/**
 *
 * @author  pascal
 */
public abstract class MetaNodeSelectionListener implements TreeSelectionListener
{
    // maby later
    //public final static int CATALOGUE_TREE = 0;
    //public final static int SEARCH_RESULTS_TREE = 1;
    
    //private final int 
    
    /** Creates a new instance of MetaNodeSelectionListener */
    public MetaNodeSelectionListener()
    {
        
    }
    
    /**
     * Called whenever the value of the selection changes.
     * @param e the event that characterizes the change.
     *
     */
    public void valueChanged(TreeSelectionEvent e)
    {
        ArrayList nodeSelection;
        TreePath[] selectedPaths = e.getPaths();
        
        if (selectedPaths != null && selectedPaths.length > 0)
        {
            nodeSelection = new ArrayList(selectedPaths.length);
            for (int i = 0; i < selectedPaths.length; i++)
            {
                nodeSelection.add(selectedPaths[i].getLastPathComponent());
            }
            
            this.nodeSelectionChanged(nodeSelection);
        }
    }  
    
    protected abstract void nodeSelectionChanged(Collection nodeSelection);    
}