/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.cids.navigator.utils;

import Sirius.navigator.types.treenode.DefaultMetaTreeNode;
import Sirius.navigator.types.treenode.ObjectTreeNode;

import Sirius.server.middleware.types.MetaClass;
import Sirius.server.middleware.types.MetaObject;

import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;

import java.util.ArrayList;
import java.util.Collection;

import de.cismet.cids.dynamics.CidsBean;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public abstract class AbstractCidsBeanDropTarget extends DropTarget {

    //~ Instance fields --------------------------------------------------------

    DataFlavor fromNavigatorNode = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class="
                    + DefaultMetaTreeNode.class.getName(),
            "a DefaultMetaTreeNode");                                                                 // NOI18N
    DataFlavor fromNavigatorCollection = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class="
                    + java.util.Collection.class.getName(),
            "a java.util.Collection of Sirius.navigator.types.treenode.DefaultMetaTreeNode objects"); // NOI18N

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new AbstractCidsBeanDropTarget object.
     *
     * @param   c  DOCUMENT ME!
     *
     * @throws  HeadlessException  DOCUMENT ME!
     */
    public AbstractCidsBeanDropTarget(final Component c) throws HeadlessException {
        super();
        setComponent(c);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public synchronized void drop(final DropTargetDropEvent dtde) {
        log.fatal("drop");                                      // NOI18N
        try {
            final ArrayList<CidsBean> beans = new ArrayList<CidsBean>();
            if (dtde.getTransferable().isDataFlavorSupported(fromNavigatorNode)
                        && dtde.getTransferable().isDataFlavorSupported(fromNavigatorCollection)) {
                try {
                    final Object object = dtde.getTransferable().getTransferData(fromNavigatorCollection);
                    if (object instanceof Collection) {
                        final Collection c = (Collection)object;
                        for (final Object o : c) {
                            if (o instanceof ObjectTreeNode) {
                                final ObjectTreeNode otn = (ObjectTreeNode)o;
                                final MetaObject mo = otn.getMetaObject();
                                beans.add(mo.getBean());
                            }
                        }
                    }
                } catch (Throwable t) {
                    log.fatal("Drop Problems occurred", t);     // NOI18N
                }
            } else {
                log.fatal("Wrong transferable");                // NOI18N
            }
            beansDropped(beans);
        } catch (Throwable ups) {
            log.error("Problem during the DnD Opertaion", ups); // NOI18N
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  beans  DOCUMENT ME!
     */
    public abstract void beansDropped(ArrayList<CidsBean> beans);
}
