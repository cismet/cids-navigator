/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.cismet.cids.utils.interfaces;

import Sirius.navigator.types.treenode.DefaultMetaTreeNode;
import java.util.Collection;

/**
 *
 * @author thorsten
 */
public interface DefaultMetaTreeNodeVisualizationService {
    public void removeVisualization(DefaultMetaTreeNode dmtn) throws Exception;
    public void removeVisualization(Collection<DefaultMetaTreeNode> c) throws Exception;
    public void addVisualization(DefaultMetaTreeNode DefaultMetaTreeNode) throws Exception;
    public void addVisualization(Collection<DefaultMetaTreeNode> c) throws Exception;
}
