package Sirius.navigator.ui.tree;

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
 * Version			:	1.0
 * Purpose			:
 * Created			:	01.11.1999
 * History			:
 *
 *******************************************************************************/
import Sirius.navigator.connection.SessionManager;
import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;

import Sirius.server.middleware.types.*;
import Sirius.navigator.types.treenode.*;
import de.cismet.tools.BlacklistClassloading;
import java.util.HashMap;

public class MetaTreeNodeRenderer extends DefaultTreeCellRenderer {
    //private MetaTreeNode treeNode;
    //private LocalPureNode ln;
    //private	LocalClassNode lcn;
    //private	LocalObjectNode lon;
    //private java.lang.Object userObject;

    private static String CLASS_PREFIX = "de.cismet.cids.custom.treeicons.";//NOI18N
    private static String CLASS_POSTFIX = "IconFactory";//NOI18N
    HashMap<String, CidsTreeObjectIconFactory> iconFactories = new HashMap<String, CidsTreeObjectIconFactory>();
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(getClass());

    public MetaTreeNodeRenderer() {
        super();
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, java.lang.Object value, boolean selected,
            boolean expanded, boolean leaf, int row, boolean hasFocus) {
        DefaultMetaTreeNode treeNode = (DefaultMetaTreeNode) value;

        Node metaNode = null;
        if (treeNode != null) {
            metaNode = treeNode.getNode();
            //log.fatal(treeNode + "--> "+metaNode.getIconString());
        } else {
            //log.fatal("treeNode==null");
        }



        //this.setToolTipText(treeNode.getDescription());
        if (treeNode.isWaitNode()) {
            super.getTreeCellRendererComponent(tree, value, false, expanded, leaf, row, false);
            return this;
        }

        int cid = treeNode.getClassID();
        String domain = treeNode.getDomain();

        CidsTreeObjectIconFactory iconFactory = null;
        iconFactory = iconFactories.get(cid + "@" + domain);//NOI18N


        //TODO Iconfactory from DB
//        if (metaNode!=null && metaNode.getIconFactory()!=-1) {
//
//        }

        //Iconfactroy from classname
        if (iconFactory == null && cid != -1 && cid != 0 && domain != null) {
            try {
                MetaClass mc = SessionManager.getConnection().getMetaClass(SessionManager.getSession().getUser(), cid, domain);
                String tablename = mc.getTableName();
                tablename = tablename.substring(0, 1).toUpperCase() + tablename.substring(1).toLowerCase();
                String className = CLASS_PREFIX + domain.toLowerCase() + "." + tablename + CLASS_POSTFIX;//NOI18N
                Class iconFactoryClass = BlacklistClassloading.forName(className);
                if (iconFactoryClass != null) {
                    iconFactory = (CidsTreeObjectIconFactory) iconFactoryClass.getConstructor().newInstance();
                    iconFactories.put(cid + "@" + domain, iconFactory);//NOI18N
                }
            } catch (Exception e) {
                log.error("Could not load IconFactory for " + cid + "@" + domain, e);//NOI18N
            }
        }

        super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        this.setText(treeNode.toString());

        Icon expandedIco = null;
        Icon leafIco = null;
        Icon closedIco = null;

        if (metaNode != null && metaNode.getIconString() != null) {
            try {
                String baseIcon = metaNode.getIconString();
                String openIconString = baseIcon.substring(0, baseIcon.lastIndexOf(".")) + "Open" + baseIcon.substring(baseIcon.lastIndexOf("."));//NOI18N
                String closedIconString = baseIcon.substring(0, baseIcon.lastIndexOf(".")) + "Closed" + baseIcon.substring(baseIcon.lastIndexOf("."));//NOI18N
                String leafIconString = baseIcon.substring(0, baseIcon.lastIndexOf(".")) + "Leaf" + baseIcon.substring(baseIcon.lastIndexOf("."));//NOI18N

                javax.swing.ImageIcon base = new javax.swing.ImageIcon(getClass().getResource(baseIcon));
                try {
                    expandedIco = new javax.swing.ImageIcon(getClass().getResource(openIconString));
                } catch (Exception e) {
                    expandedIco = base;
                }
                try {
                    leafIco = new javax.swing.ImageIcon(getClass().getResource(leafIconString));
                } catch (Exception e) {
                    leafIco = base;
                }
                try {
                    closedIco = new javax.swing.ImageIcon(getClass().getResource(closedIconString));
                } catch (Exception e) {
                    closedIco = base;
                }
            } catch (Exception e) {
                log.error("Error during Iconstuff", e);//NOI18N
            }
        }

        if (iconFactory != null) {
            if (treeNode instanceof PureTreeNode) {
                if (expanded == true && iconFactory.getOpenPureNodeIcon((PureTreeNode) treeNode) != null) {
                    expandedIco = iconFactory.getOpenPureNodeIcon((PureTreeNode) treeNode);
                } else if (leaf == true && iconFactory.getLeafPureNodeIcon((PureTreeNode) treeNode) != null) {
                    leafIco = iconFactory.getLeafPureNodeIcon((PureTreeNode) treeNode);
                } else if (iconFactory.getClosedPureNodeIcon((PureTreeNode) treeNode) != null) {
                    closedIco = iconFactory.getClosedPureNodeIcon((PureTreeNode) treeNode);
                }

            } else if (treeNode instanceof ObjectTreeNode) {
                if (expanded == true && iconFactory.getOpenObjectNodeIcon((ObjectTreeNode) treeNode) != null) {
                    expandedIco = iconFactory.getOpenObjectNodeIcon((ObjectTreeNode) treeNode);
                } else if (leaf == true && iconFactory.getLeafObjectNodeIcon((ObjectTreeNode) treeNode) != null) {
                    leafIco = iconFactory.getLeafObjectNodeIcon((ObjectTreeNode) treeNode);
                } else if (iconFactory.getClosedObjectNodeIcon((ObjectTreeNode) treeNode) != null) {
                    closedIco = iconFactory.getClosedObjectNodeIcon((ObjectTreeNode) treeNode);
                }

            } else if (treeNode instanceof ClassTreeNode && iconFactory.getClassNodeIcon((ClassTreeNode) treeNode) != null) {
                expandedIco = iconFactory.getClassNodeIcon((ClassTreeNode) treeNode);
                leafIco = expandedIco;
                closedIco = expandedIco;
            }
        }

        if (expanded == true) {
            if (expandedIco != null) {
                this.setIcon(expandedIco);
            } else {
                this.setIcon(treeNode.getOpenIcon());
            }
        } else if (leaf == true) {
            if (leafIco != null) {
                this.setIcon(leafIco);
            } else {
                this.setIcon(treeNode.getLeafIcon());
            }
        } else {
            if (closedIco != null) {
                this.setIcon(closedIco);
            } else {
                this.setIcon(treeNode.getClosedIcon());
            }
        }

        return this;
    }
}
