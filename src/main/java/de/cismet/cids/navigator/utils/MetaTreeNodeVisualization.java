/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.cismet.cids.navigator.utils;

import Sirius.navigator.types.treenode.DefaultMetaTreeNode;
import de.cismet.cids.utils.interfaces.DefaultMetaTreeNodeVisualizationService;
import java.util.Collection;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.Lookup.Template;

/**
 *
 * @author thorsten
 * 
 */
public class MetaTreeNodeVisualization implements DefaultMetaTreeNodeVisualizationService{
    private static MetaTreeNodeVisualization instance=null;

    private Lookup visLookup;
    private Collection<DefaultMetaTreeNodeVisualizationService> visServices;
    private Template visTemplate;
    private Result visResults;


    private MetaTreeNodeVisualization(){
        visLookup = Lookup.getDefault();
        visTemplate = new Template(DefaultMetaTreeNodeVisualizationService.class);
        visResults = visLookup.lookup(visTemplate);
        visServices = visResults.allInstances();
    }
    public static MetaTreeNodeVisualization getInstance() {
        if (instance==null){
            instance=new MetaTreeNodeVisualization();
        }
        return instance;
    }

    public void addVisualization(DefaultMetaTreeNode dmtn) throws Exception{
        for (DefaultMetaTreeNodeVisualizationService service:visServices){
            service.addVisualization(dmtn);
        }
    }

    public void addVisualization(Collection<DefaultMetaTreeNode> c) throws Exception{
        for (DefaultMetaTreeNodeVisualizationService service:visServices){
            service.addVisualization(c);
        }
    }

    public void removeVisualization(DefaultMetaTreeNode mon) throws Exception{
        for (DefaultMetaTreeNodeVisualizationService service:visServices){
            service.removeVisualization(mon);
        }
    }

    public void removeVisualization(Collection<DefaultMetaTreeNode> c) throws Exception{
        for (DefaultMetaTreeNodeVisualizationService service:visServices){
            service.removeVisualization(c);
        }
    }

}
