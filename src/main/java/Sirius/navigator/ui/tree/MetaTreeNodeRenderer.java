/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui.tree;

/**
 * *****************************************************************************
 *
 * Copyright (c) : EIG (Environmental Informatics Group)
 * http://www.htw-saarland.de/eig Prof. Dr. Reiner Guettler Prof. Dr. Ralf
 * Denzer
 *
 * HTWdS Hochschule fuer Technik und Wirtschaft des Saarlandes Goebenstr. 40
 * 66117 Saarbruecken Germany
 *
 * Programmers : Pascal
 *
 * Project : WuNDA 2 Version : 1.0 Purpose : Created : 01.11.1999 History :
 *
 ******************************************************************************
 */
import Sirius.navigator.types.treenode.*;

import Sirius.server.middleware.types.*;

import java.awt.Component;

import java.util.HashMap;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import de.cismet.cids.navigator.utils.ClassCacheMultiple;

import de.cismet.cids.utils.ClassloadingHelper;
import de.cismet.connectioncontext.ConnectionContext;
import de.cismet.connectioncontext.ConnectionContextProvider;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class MetaTreeNodeRenderer extends DefaultTreeCellRenderer implements ConnectionContextProvider {

    //~ Static fields/initializers ---------------------------------------------

    // private MetaTreeNode treeNode;
    // private LocalPureNode ln;
    // private   LocalClassNode lcn;
    // private   LocalObjectNode lon;
    // private java.lang.Object userObject;
    private static final String CLASS_PREFIX = "de.cismet.cids.custom.treeicons."; // NOI18N
    private static final String CLASS_POSTFIX = "IconFactory";                     // NOI18N
    private static final CidsTreeObjectIconFactory NO_ICON_FACTORY = new CidsTreeObjectIconFactory() {

            @Override
            public Icon getClosedPureNodeIcon(final PureTreeNode ptn) {
                throw new UnsupportedOperationException("I am just a dummy!");
            }

            @Override
            public Icon getOpenPureNodeIcon(final PureTreeNode ptn) {
                throw new UnsupportedOperationException("I am just a dummy!");
            }

            @Override
            public Icon getLeafPureNodeIcon(final PureTreeNode ptn) {
                throw new UnsupportedOperationException("I am just a dummy!");
            }

            @Override
            public Icon getOpenObjectNodeIcon(final ObjectTreeNode otn) {
                throw new UnsupportedOperationException("I am just a dummy!");
            }

            @Override
            public Icon getClosedObjectNodeIcon(final ObjectTreeNode otn) {
                throw new UnsupportedOperationException("I am just a dummy!");
            }

            @Override
            public Icon getLeafObjectNodeIcon(final ObjectTreeNode otn) {
                throw new UnsupportedOperationException("I am just a dummy!");
            }

            @Override
            public Icon getClassNodeIcon(final ClassTreeNode dmtn) {
                throw new UnsupportedOperationException("I am just a dummy!");
            }
        };

    static HashMap<String, Icon> iconCache = new HashMap<String, Icon>();
    static HashMap<String, CidsTreeObjectIconFactory> iconFactories = new HashMap<String, CidsTreeObjectIconFactory>();

    //~ Instance fields --------------------------------------------------------

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(getClass());

    private final ConnectionContext connectionContext;
    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new MetaTreeNodeRenderer object.
     */
    public MetaTreeNodeRenderer(final ConnectionContext connectionContext) {
        super();
        this.connectionContext = connectionContext;
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
        final int cid = treeNode.getClassID();
        final String domain = treeNode.getDomain();
        final String key = cid + "@" + domain;
        CidsTreeObjectIconFactory iconFactory = iconFactories.get(key);

        if (treeNode.isWaitNode()) {
            super.getTreeCellRendererComponent(tree, value, false, expanded, leaf, row, false);
        } else {
            Icon expandedIco = null;
            Icon leafIco = null;
            Icon closedIco = null;
            if (treeNode != null) {
                final Node metaNode = treeNode.getNode();
                if ((metaNode != null) && (metaNode.getIconString() != null)) {
                    try {
                        final String baseIcon = metaNode.getIconString();
                        final String openIconString = baseIcon.substring(0, baseIcon.lastIndexOf(".")) + "Open"
                                    + baseIcon.substring(baseIcon.lastIndexOf(".")); // NOI18N
                        final String closedIconString = baseIcon.substring(0, baseIcon.lastIndexOf(".")) + "Closed"
                                    + baseIcon.substring(baseIcon.lastIndexOf(".")); // NOI18N
                        final String leafIconString = baseIcon.substring(0, baseIcon.lastIndexOf(".")) + "Leaf"
                                    + baseIcon.substring(baseIcon.lastIndexOf(".")); // NOI18N

                        Icon base = null;
                        base = iconCache.get(baseIcon);
                        expandedIco = iconCache.get(openIconString);
                        leafIco = iconCache.get(leafIconString);
                        closedIco = iconCache.get(closedIconString);

                        if (base == null) {
                            base = new javax.swing.ImageIcon(getClass().getResource(baseIcon));
                            iconCache.put(baseIcon, base);
                        }

                        if (expandedIco == null) {
                            try {
                                expandedIco = new javax.swing.ImageIcon(getClass().getResource(openIconString));
                            } catch (Exception e) {
                                expandedIco = base;
                            } finally {
                                iconCache.put(openIconString, expandedIco);
                            }
                        }
                        if (leafIco == null) {
                            try {
                                leafIco = new javax.swing.ImageIcon(getClass().getResource(leafIconString));
                            } catch (Exception e) {
                                leafIco = base;
                            } finally {
                                iconCache.put(leafIconString, leafIco);
                            }
                        }
                        if (closedIco == null) {
                            try {
                                closedIco = new javax.swing.ImageIcon(getClass().getResource(closedIconString));
                            } catch (Exception e) {
                                closedIco = base;
                            } finally {
                                iconCache.put(closedIconString, closedIco);
                            }
                        }
                    } catch (Exception e) {
                        log.error("Error during Iconstuff" + metaNode.getIconString(), e); // NOI18N
                    }
                }
            }

            if (leafIco == null) {
                // Iconfactroy from classname
                if ((iconFactory == null) && (cid > 0) && (domain != null)) {
                    try {
                        final MetaClass mc = ClassCacheMultiple.getMetaClass(domain, cid, getConnectionContext());
                        final Class<?> iconFactoryClass = ClassloadingHelper.getDynamicClass(
                                mc,
                                ClassloadingHelper.CLASS_TYPE.ICON_FACTORY);
                        if (iconFactoryClass != null) {
                            iconFactory = (CidsTreeObjectIconFactory)iconFactoryClass.getConstructor().newInstance();
                        }
                    } catch (Exception e) {
                        log.error("Could not load IconFactory for " + key, e); // NOI18N
                    }
                    if (iconFactory != null) {
                        iconFactories.put(key, iconFactory);                   // NOI18N
                    } else {
                        iconFactories.put(key, NO_ICON_FACTORY);               // NOI18N
                    }
                }

                if ((iconFactory != null) && (iconFactory != NO_ICON_FACTORY)) {
                    if (treeNode instanceof PureTreeNode) {
                        if ((expanded == true) && (iconFactory.getOpenPureNodeIcon((PureTreeNode)treeNode) != null)) {
                            expandedIco = iconFactory.getOpenPureNodeIcon((PureTreeNode)treeNode);
                        } else if ((leaf == true)
                                    && (iconFactory.getLeafPureNodeIcon((PureTreeNode)treeNode) != null)) {
                            leafIco = iconFactory.getLeafPureNodeIcon((PureTreeNode)treeNode);
                        } else if (iconFactory.getClosedPureNodeIcon((PureTreeNode)treeNode) != null) {
                            closedIco = iconFactory.getClosedPureNodeIcon((PureTreeNode)treeNode);
                        }
                    } else if (treeNode instanceof ObjectTreeNode) {
                        if ((expanded == true)
                                    && (iconFactory.getOpenObjectNodeIcon((ObjectTreeNode)treeNode) != null)) {
                            expandedIco = iconFactory.getOpenObjectNodeIcon((ObjectTreeNode)treeNode);
                        } else if ((leaf == true)
                                    && (iconFactory.getLeafObjectNodeIcon((ObjectTreeNode)treeNode) != null)) {
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
            }
            super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
            this.setText(treeNode.toString());

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
        }
        return this;
    }

    @Override
    public ConnectionContext getConnectionContext() {
        return connectionContext;
    }
}
