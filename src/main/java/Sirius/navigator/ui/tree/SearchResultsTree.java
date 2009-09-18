package Sirius.navigator.ui.tree;

import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.plugin.PluginRegistry;
import Sirius.navigator.plugin.interfaces.PluginSupport;
import Sirius.navigator.types.iterator.TreeNodeIterator;
import Sirius.navigator.types.treenode.DefaultMetaTreeNode;
import Sirius.navigator.types.treenode.ObjectTreeNode;
import Sirius.navigator.types.treenode.RootTreeNode;
import Sirius.server.middleware.types.MetaObject;
import Sirius.server.middleware.types.MetaObjectNode;
import Sirius.server.middleware.types.Node;
import de.cismet.tools.CismetThreadPool;
import de.cismet.cids.utils.MetaTreeNodeVisualization;
import java.awt.EventQueue;


/*******************************************************************************
 *
 * Copyright (c)	:	EIG (Environmental Informatics Group)
 * http://www.htw-saarland.de/eig
 * Prof. Dr. Reiner Guettler
 * Prof. Dr. Ralf Denzer
 *
 * HTWdS
 * Hochschule fuer Technik und Wirtschaft des Saarlandes
 * Goebenstr. 40
 * 66117 Saarbruecken
 * Germany
 *
 * Programmers		:	Pascal
 *
 * Project			:	WuNDA 2
 * Filename		:
 * Version			:	1.0
 * Purpose			:
 * Created			:	27.04.2000
 * History			:
 *
 *******************************************************************************/
import java.util.*;
import javax.swing.tree.DefaultTreeModel;

/**
 * Der SearchTree dient zum Anzeigen von Suchergebnissen. Neben der Funktionalit\u00E4t,
 * die er von GenericMetaTree erbt, bietet er zusaetzlich noch die Moeglichkeit,
 * die Suchergebnisse schrittweise anzuzeigen. D.h. es wird immer nur ein kleiner
 * Ausschnitt fester Groesse aus der gesamten Ergebissmenge angezeigt. Um durch die
 * Ergebnissmenge zu navigieren stellt der SearchTree spezielle Methoden bereit.
 */
public class SearchResultsTree extends MetaCatalogueTree {

    private boolean empty = true;
    private boolean browseBack = false;
    private boolean browseForward = false;
    private Node[] resultNodes = null;
    private Node[] visibleResultNodes = null;
    private final RootTreeNode rootNode;
    private final int visibleNodes;
    // Position des LETZTEN Elements
    private int pos = 0;
    private int max = 0;
    private int rest = 0;
    private Thread runningNameLoader = null;
    private boolean syncWithMap = false;

    /**
     * Erzeugt einen neuen, leeren, SearchTree. Es werden jeweils
     * 50 Objekte angezeigt.
     */
    public SearchResultsTree() throws Exception {
        this(50, true, 2);
    }

    public SearchResultsTree(int visibleNodes, boolean useThread, int maxThreadCount) throws Exception {
        super(new RootTreeNode(), false, useThread, maxThreadCount);
        this.rootNode = (RootTreeNode) this.defaultTreeModel.getRoot();
        this.visibleNodes = visibleNodes;
        defaultTreeModel.setAsksAllowsChildren(true);
        this.defaultTreeModel.setAsksAllowsChildren(true);
    }

    /**
     * Blaettert in der Ergebnissmenge einen Schritt vor. Loest einen
     * PropertyChange Event ("browse") aus.
     *
     * @return true, bis das Ende der Ergebnissmenge erreicht wurde.
     */
    public boolean browseForward() {
        logger.info("[SearchResultsTree] browsing forward");
        if (resultNodes != null) {
            boolean full = true;

            if (pos + 1 > max) {
                browseForward = false;
                browseBack = true;
                //logger.debug("browseForward: " + browseForward + " browseBack: " + browseBack);
                //return full;
            } else if (visibleNodes >= max) {
                browseBack = false;
                browseForward = false;
                //logger.debug("browseForward: " + browseForward + " browseBack: " + browseBack);

                visibleResultNodes = new Node[max];
                visibleResultNodes = resultNodes;
            } else if ((pos + visibleNodes) < max) {
                int j = 0;
                for (int i = pos; i < (pos + visibleNodes); i++) {
                    visibleResultNodes[j] = resultNodes[i];
                    j++;
                }
                pos += visibleNodes;
                full = false;

                if (pos > visibleNodes) {
                    browseBack = true;
                } else {
                    browseBack = false;
                }
                browseForward = true;
                //logger.debug("browseForward: " + browseForward + " browseBack: " + browseBack);
            } else //if ((pos + visibleNodes) > max)
            {

                browseForward = false;
                browseBack = true;
                //logger.debug("browseForward: " + browseForward + " browseBack: " + browseBack);

                rest = max - pos;
                visibleResultNodes = new Node[rest];
                int j = 0;
                for (int i = pos; i < max; i++) {
                    visibleResultNodes[j] = resultNodes[i];
                    j++;
                }
                pos = max;
                full = true;
            }

            //logger.debug("pos: " + pos + " max: " + max);
            rootNode.removeChildren();

            try {
                rootNode.addChildren(visibleResultNodes);
            } catch (Exception exp) {
                logger.fatal("[SearchResultsTree] could not browse forward", exp);
            }


            firePropertyChange("browse", 0, 1);
            defaultTreeModel.nodeStructureChanged(rootNode);
            checkForDynamicNodes();
            return full;
        }

        System.gc();
        return true;
    }

    /**
     * Blaettert in der Ergebnissmenge einen Schritt zurueck. Loest einen
     * PropertyChange Event ("browse") aus.
     *
     * @return true, bis der Anfang der Ergebnissmenge erreicht wurde.
     */
    public boolean browseBack() {
        logger.info("[SearchResultsTree] browsing back");
        if (resultNodes != null) {
            boolean full = true;

            if (visibleNodes >= max) {
                browseForward = false;
                browseBack = true;
                logger.debug("browseForward: " + browseForward + " browseBack: " + browseBack);

                logger.debug("visibleNodes: " + visibleNodes + " >= max: " + max);
                visibleResultNodes = new Node[max];
                visibleResultNodes = resultNodes;
            } else if (pos < max && (pos - visibleNodes) >= visibleNodes) {
                logger.debug("pos: " + pos + " - visibleNodes: " + visibleNodes + " >= 0");
                pos -= visibleNodes;
                int j = 0;
                visibleResultNodes = new Node[visibleNodes];

                for (int i = (pos - visibleNodes); i < pos; i++) {
                    //logger.debug("i: " + i + "j: " + j);
                    visibleResultNodes[j] = resultNodes[i];
                    j++;
                }
                full = false;

                if (pos <= visibleNodes) {
                    browseBack = false;
                } else {
                    browseBack = true;
                }
                browseForward = true;
                logger.debug("browseForward: " + browseForward + " browseBack: " + browseBack);
            } else if (pos == max) {
                browseForward = true;
                browseBack = true;
                logger.debug("browseForward: " + browseForward + " browseBack: " + browseBack);
                logger.debug("pos: " + pos + " == max" + max);

                pos -= rest;
                int j = 0;
                visibleResultNodes = new Node[visibleNodes];

                for (int i = (pos - visibleNodes); i < pos; i++) {
                    logger.debug("i: " + i + "j: " + j);
                    visibleResultNodes[j] = resultNodes[i];
                    j++;
                }
                full = false;

            }

            if (pos == visibleNodes) {
                browseBack = false;
                //logger.debug("browseForward: " + browseForward + " browseBack: " + browseBack);
                //logger.debug("pos: " + pos + " == visibleNodes" + visibleNodes);
            }

            logger.debug("pos: " + pos + " max: " + max);
            rootNode.removeChildren();

            try {
                rootNode.addChildren(visibleResultNodes);
            } catch (Exception exp) {
                logger.fatal("[SearchResultsTree] could not browse back", exp);
            }

            firePropertyChange("browse", 0, 1);
            defaultTreeModel.nodeStructureChanged(rootNode);
            checkForDynamicNodes();
            return full;
        }
        checkForDynamicNodes();
        System.gc();
        return true;
    }

    /**
     * Setzt die ResultNodes fuer den Suchbaum, d.h. die Ergebnisse der Suche.
     *
     * @param nodes Ergebnisse, die im SearchTree angezeigt werden sollen.
     */
    public void setResultNodes(Node[] nodes) {
        logger.info("[SearchResultsTree] filling tree with '" + nodes.length + "' nodes");
        pos = 0;

        if (nodes == null || nodes.length < 1) {
            empty = true;
            browseBack = false;
            browseForward = false;
            firePropertyChange("browse", 0, 1);
        } else {
            resultNodes = nodes;
            max = resultNodes.length;
            visibleResultNodes = new Node[visibleNodes];
            this.browseForward();
            empty = false;
        }

        firePropertyChange("browse", 0, 1);
        syncWithMap();
        checkForDynamicNodes();
    }

    public void syncWithMap() {
        syncWithMap(isSyncWithMap());
    }

    public void syncWithMap(boolean sync) {
        if (sync) {
            logger.debug("syncWithMap");
            try {
                PluginSupport map = PluginRegistry.getRegistry().getPlugin("cismap");
                Vector<DefaultMetaTreeNode> v = new Vector<DefaultMetaTreeNode>();
                final DefaultTreeModel defaultTreeModel = (DefaultTreeModel) getModel();


                for (int i = 0; i < ((DefaultMetaTreeNode) defaultTreeModel.getRoot()).getChildCount(); ++i) {
//                    logger.debug("resultNodes:"+resultNodes[i]);
                    if (resultNodes[i] instanceof MetaObjectNode) {
                        //ObjectTreeNode otn=new ObjectTreeNode((MetaObjectNode)resultNodes[i]);
                        DefaultMetaTreeNode otn = (DefaultMetaTreeNode) ((DefaultMetaTreeNode) defaultTreeModel.getRoot()).getChildAt(i);
                        v.add(otn);
                    }
                }

                //((de.cismet.cismap.navigatorplugin.CismapPlugin) map).showInMap(v, false);
                MetaTreeNodeVisualization.getInstance().addVisualization(v);
            } catch (Throwable t) {
                logger.warn("Fehler beim synchronisieren der Suchergebnisse mit der Karte", t);
            }
        }
    }

    /**
     * Setzt die ResultNodes fuer den Suchbaum, d.h. die Ergebnisse der Suche.<br>
     * Diese Ergebnisse koennen an eine bereits vorhandene Ergebnissmenge angehaengt
     *werden
     *
     * @param nodes Ergebnisse, die im SearchTree angezeigt werden sollen.
     * @param append Ergebnisse anhaengen.
     */
    public void setResultNodes(Node[] nodes, boolean append) {
        logger.info("[SearchResultsTree] appending '" + nodes.length + "' nodes");
        if (append == true && (nodes == null || nodes.length < 1)) {
            return;
        } else if (append == false && (nodes == null || nodes.length < 1)) {
            this.clear();
            return;
        } else if (append == true && empty == false) {
            Node[] tmpNodes = new Node[resultNodes.length + nodes.length];
            int j = resultNodes.length;

            for (int i = 0; i < resultNodes.length; i++) {
                tmpNodes[i] = resultNodes[i];
            }

            this.clear();

            for (int i = 0; i < nodes.length; i++) {
                tmpNodes[j] = nodes[i];
                j++;
            }
            resultNodes = tmpNodes;
        } else {
            this.clear();
            resultNodes = nodes;

            //logger.debug("nodes.length      : " + nodes.length);
            //logger.debug("resultNodes.length: " + resultNodes.length);
        }

        max = resultNodes.length;
        pos = 0;
        visibleResultNodes = new Node[visibleNodes];
        this.browseForward();
        empty = false;
        firePropertyChange("browse", 0, 1);
        syncWithMap();
        checkForDynamicNodes();
    }

    private void checkForDynamicNodes() {
        final DefaultTreeModel defaultTreeModel = (DefaultTreeModel) getModel();
        final DefaultMetaTreeNode node = (DefaultMetaTreeNode) defaultTreeModel.getRoot();
        if (runningNameLoader != null) {
            runningNameLoader.interrupt();
        }

        Thread t = new Thread() {

            public void run() {
                final Thread parentThread = this;
                runningNameLoader = this;
                for (int i = 0; i < defaultTreeModel.getChildCount(node); ++i) {
//                                    try {
//                                        Thread.sleep(100);
//                                    } catch (InterruptedException ex) {
//                                        ex.printStackTrace();
//                                    }
                    if (interrupted()) {
                        break;
                    }
                    try {
                        final DefaultMetaTreeNode n = (DefaultMetaTreeNode) defaultTreeModel.getChild(node, i);

                        if (n != null && n.getNode().getName() == null && n.isObjectNode()) {

                            try {
                                final ObjectTreeNode on = ((ObjectTreeNode) n);
                                EventQueue.invokeLater(new Runnable() {

                                    public void run() {

                                        n.getNode().setName("Name wird geladen .....");
                                        defaultTreeModel.nodeChanged(on);

                                    }
                                });
                                if (logger.isDebugEnabled()) {
                                    logger.debug("caching object node");
                                }
                                final MetaObject MetaObject = SessionManager.getProxy().getMetaObject(on.getMetaObjectNode().getObjectId(), on.getMetaObjectNode().getClassId(), on.getMetaObjectNode().getDomain());
                                on.getMetaObjectNode().setObject(MetaObject);
                                EventQueue.invokeLater(new Runnable() {

                                    public void run() {

                                        n.getNode().setName(MetaObject.toString());
                                        defaultTreeModel.nodeChanged(on);

                                    }
                                });

                            } catch (Throwable t) {
                                logger.error("could not retrieve meta object of node '" + this + "'", t);
                            }
                        } else {
                            logger.debug("n.getNode().getName()!=null: " + n.getNode().getName() + ":");
                        }
                    } catch (Exception e) {
                        logger.error("Fehler beim Laden des Namen", e);
                    }
                }
                runningNameLoader = null;
            }
        };

        CismetThreadPool.execute(t);
    }

    public Node[] getResultNodes() {
        return resultNodes;
    }

    /**
     * Diese Funktion dient dazu, eine Selektion von Knoten aus
     * dem SearchTree zu loeschen.
     *
     * @param selectedNodes Die Knoten, die geloescht werden sollen.
     * @return true, wenn mindestens ein Knoten geloescht wurde.
     */
    public boolean removeResultNodes(DefaultMetaTreeNode[] selectedNodes) {
        logger.info("[SearchResultsTree] removing '" + selectedNodes + "' nodes");
        boolean deleted = false;

        if (selectedNodes == null || selectedNodes.length < 1) {
            return deleted;
        }

        Vector tmpNodeVector = new Vector();

        for (int i = 0; i < resultNodes.length; i++) {
            tmpNodeVector.addElement(resultNodes[i]);
        }

        for (int i = 0; i < tmpNodeVector.size(); i++) {
            //logger.debug("(1) tmpNodeVector.size(): " + tmpNodeVector.size());

            for (int j = 0; j < selectedNodes.length;) {
                //logger.debug("i: " + i + " j: " + j + " -------------------");

                if (i < tmpNodeVector.size() && selectedNodes[j].equalsNode((Node) tmpNodeVector.elementAt(i))) {
                    tmpNodeVector.removeElementAt(i);
                    deleted = true;
                    //logger.debug("Knoten " + i + " geloescht!");
                } else {
                    ++j;
                }
            }

            //logger.debug("(2) tmpNodeVector.size(): " + tmpNodeVector.size());
        }

        if (deleted) {
            this.setResultNodes((Node[]) tmpNodeVector.toArray(new Node[tmpNodeVector.size()]), false);
        }

        return deleted;
    }

    /*public void removeSelectedResultNodes()
    {
    Collection selectedNodes = this.getSelectedNodes();
    if(selectedNodes != null)
    {
    this.removeResultNodes((DefaultMetaTreeNode[])selectedNodes.toArray(new DefaultMetaTreeNode[selectedNodes.size()]));
    }
    }*/
    public void removeSelectedResultNodes() {
        Collection selectedNodes = this.getSelectedNodes();
        if (selectedNodes != null) {
            this.removeResultNodes(selectedNodes);
        }
    }

    public boolean removeResultNodes(Collection selectedNodes) {
        logger.info("[SearchResultsTree] removing '" + selectedNodes + "' nodes");

        boolean deleted = false;
        ArrayList tempList = new ArrayList(Arrays.asList(resultNodes));
        //java.util.List tempList = Arrays.asList(resultNodes);
        TreeNodeIterator iterator = new TreeNodeIterator(selectedNodes);

        while (iterator.hasNext()) {
            /*for (int i = 0; i < tempList.size(); i++)
            {
            if (i < tempList.size() && iterator.next().equalsNode((Node)tempList.get(i)))
            {
            tempList.remove(i);
            deleted = true;
            }
            }*/

            //logger.debug("iterator" + iterator);
            DefaultMetaTreeNode nextNode = iterator.next();
            Iterator nodeIterator = tempList.iterator();
            while (nodeIterator.hasNext()) {
                //logger.debug("nodeIterator" + nodeIterator);
                if (nextNode.equalsNode(((Node) nodeIterator.next()))) {
                    nodeIterator.remove();
                    deleted = true;
                }
            }
        }

        if (deleted) {
            this.setResultNodes((Node[]) tempList.toArray(new Node[tempList.size()]), false);
        }

        return deleted;
    }

    /**
     * Setzt den SearchTree komplett zurueck und entfernt alle Knoten
     */
    public void clear() {
        logger.info("[SearchResultsTree] removing all nodes");
        resultNodes = null;
        pos = 0;
        max = 0;
        empty = true;
        browseBack = false;
        browseForward = false;
        rootNode.removeAllChildren();
        firePropertyChange("browse", 0, 1);
        defaultTreeModel.nodeStructureChanged(rootNode);
        System.gc();
    }

    /**
     * Liefert true, wenn im SearchTree zurueck geblaettert werden kann.
     *
     * @return true/false
     */
    public boolean isBrowseBack() {
        return this.browseBack;
    }

    /**
     * Liefert true, wenn im SearchTree vorwaerts geblaettert werden kann.
     *
     * @return true/false
     */
    public boolean isBrowseForward() {
        return this.browseForward;
    }

    public int getVisibleNodes() {
        return this.visibleNodes;
    }

    public boolean isEmpty() {
        return this.empty;
    }

    public boolean isSyncWithMap() {
        return syncWithMap;
    }

    public void setSyncWithMap(boolean syncWithMap) {
        this.syncWithMap = syncWithMap;
    }
}
