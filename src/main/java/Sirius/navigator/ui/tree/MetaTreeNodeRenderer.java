/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui.tree;

/*******************************************************************************
 *
 * Copyright (c)        :       EIG (Environmental Informatics Group)
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
 * Programmers          :       Pascal
 *
 * Project                      :       WuNDA 2
 * Version                      :       1.0
 * Purpose                      :
 * Created                      :       01.11.1999
 * History                      :
 *
 *******************************************************************************/

import Sirius.navigator.types.treenode.*;

import Sirius.server.middleware.types.*;

import java.awt.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import de.cismet.cids.navigator.utils.ClassCacheMultiple;

import de.cismet.cids.utils.ClassloadingHelper;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class MetaTreeNodeRenderer extends DefaultTreeCellRenderer {

    //~ Static fields/initializers ---------------------------------------------

    // private MetaTreeNode treeNode;
    // private LocalPureNode ln;
    // private   LocalClassNode lcn;
    // private   LocalObjectNode lon;
    // private java.lang.Object userObject;

    private static String CLASS_PREFIX = "de.cismet.cids.custom.treeicons."; // NOI18N
    private static String CLASS_POSTFIX = "IconFactory";                     // NOI18N

    //~ Instance fields --------------------------------------------------------

    HashMap<String, CidsTreeObjectIconFactory> iconFactories = new HashMap<String, CidsTreeObjectIconFactory>();
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(getClass());

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new MetaTreeNodeRenderer object.
     */
    public MetaTreeNodeRenderer() {
        super();
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Component getTreeCellRendererComponent(final JTree tree,
            final java.lang.Object value,
            final boolean selected,
            final boolean expanded,
            final boolean leaf,
            final int row,
            final boolean hasFocus) {
        final DefaultMetaTreeNode treeNode = (DefaultMetaTreeNode)value;

        Node metaNode = null;
        if (treeNode != null) {
            metaNode = treeNode.getNode();
            // log.fatal(treeNode + "--> "+metaNode.getIconString());
        } else {
            // log.fatal("treeNode==null");
        }

        // this.setToolTipText(treeNode.getDescription());
        if (treeNode.isWaitNode()) {
            super.getTreeCellRendererComponent(tree, value, false, expanded, leaf, row, false);
            return this;
        }

        final int cid = treeNode.getClassID();
        final String domain = treeNode.getDomain();

        CidsTreeObjectIconFactory iconFactory = null;
        iconFactory = iconFactories.get(cid + "@" + domain); // NOI18N

        // TODO Iconfactory from DB
// if (metaNode!=null && metaNode.getIconFactory()!=-1) {
//
// }

        // Iconfactroy from classname
        if ((iconFactory == null) && (cid != -1) && (cid != 0) && (domain != null)) {
            try {
                final MetaClass mc = ClassCacheMultiple.getMetaClass(domain, cid);
                final Class<?> iconFactoryClass = ClassloadingHelper.getDynamicClass(
                        mc,
                        ClassloadingHelper.CLASS_TYPE.ICON_FACTORY);
                if (iconFactoryClass != null) {
                    iconFactory = (CidsTreeObjectIconFactory)iconFactoryClass.getConstructor().newInstance();
                    iconFactories.put(cid + "@" + domain, iconFactory);               // NOI18N
                }
            } catch (Exception e) {
                log.error("Could not load IconFactory for " + cid + "@" + domain, e); // NOI18N
            }
        }

        super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        this.setText(treeNode.toString());

        Icon expandedIco = null;
        Icon leafIco = null;
        Icon closedIco = null;

        if ((metaNode != null) && (metaNode.getIconString() != null)) {
            try {
                final String baseIcon = metaNode.getIconString();
                final String openIconString = baseIcon.substring(0, baseIcon.lastIndexOf(".")) + "Open"
                            + baseIcon.substring(baseIcon.lastIndexOf(".")); // NOI18N
                final String closedIconString = baseIcon.substring(0, baseIcon.lastIndexOf(".")) + "Closed"
                            + baseIcon.substring(baseIcon.lastIndexOf(".")); // NOI18N
                final String leafIconString = baseIcon.substring(0, baseIcon.lastIndexOf(".")) + "Leaf"
                            + baseIcon.substring(baseIcon.lastIndexOf(".")); // NOI18N

                final javax.swing.ImageIcon base = new javax.swing.ImageIcon(getClass().getResource(baseIcon));
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
                log.error("Error during Iconstuff" + metaNode.getIconString(), e); // NOI18N
            }
        }

        if (iconFactory != null) {
            if (treeNode instanceof PureTreeNode) {
                if ((expanded == true) && (iconFactory.getOpenPureNodeIcon((PureTreeNode)treeNode) != null)) {
                    expandedIco = iconFactory.getOpenPureNodeIcon((PureTreeNode)treeNode);
                } else if ((leaf == true) && (iconFactory.getLeafPureNodeIcon((PureTreeNode)treeNode) != null)) {
                    leafIco = iconFactory.getLeafPureNodeIcon((PureTreeNode)treeNode);
                } else if (iconFactory.getClosedPureNodeIcon((PureTreeNode)treeNode) != null) {
                    closedIco = iconFactory.getClosedPureNodeIcon((PureTreeNode)treeNode);
                }
            } else if (treeNode instanceof ObjectTreeNode) {
                if ((expanded == true) && (iconFactory.getOpenObjectNodeIcon((ObjectTreeNode)treeNode) != null)) {
                    expandedIco = iconFactory.getOpenObjectNodeIcon((ObjectTreeNode)treeNode);
                } else if ((leaf == true) && (iconFactory.getLeafObjectNodeIcon((ObjectTreeNode)treeNode) != null)) {
                    leafIco = iconFactory.getLeafObjectNodeIcon((ObjectTreeNode)treeNode);
                } else if (iconFactory.getClosedObjectNodeIcon((ObjectTreeNode)treeNode) != null) {
                    closedIco = iconFactory.getClosedObjectNodeIcon((ObjectTreeNode)treeNode);
                }
            } else if ((treeNode instanceof ClassTreeNode)
                        && (iconFactory.getClassNodeIcon((ClassTreeNode)treeNode) != null)) {
                expandedIco = iconFactory.getClassNodeIcon((ClassTreeNode)treeNode);
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
