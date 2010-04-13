/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.cids.navigator.utils;

import Sirius.navigator.types.treenode.DefaultMetaTreeNode;
import Sirius.navigator.types.treenode.ObjectTreeNode;
import Sirius.server.middleware.types.MetaClass;
import Sirius.server.middleware.types.MetaObject;
import de.cismet.cids.dynamics.CidsBean;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 *
 * @author thorsten
 */
public abstract class AbstractCidsBeanDropTarget extends DropTarget {

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    DataFlavor fromNavigatorNode = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class=" + DefaultMetaTreeNode.class.getName(), "a DefaultMetaTreeNode");//NOI18N
    DataFlavor fromNavigatorCollection = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class=" + java.util.Collection.class.getName(), "a java.util.Collection of Sirius.navigator.types.treenode.DefaultMetaTreeNode objects");//NOI18N

    public AbstractCidsBeanDropTarget(Component c) throws HeadlessException {
        super();
        setComponent(c);
    }

    @Override
    public synchronized void drop(DropTargetDropEvent dtde) {
        log.fatal("drop");//NOI18N
        try {
            ArrayList<CidsBean> beans = new ArrayList<CidsBean>();
            if (dtde.getTransferable().isDataFlavorSupported(fromNavigatorNode) && dtde.getTransferable().isDataFlavorSupported(fromNavigatorCollection)) {
                try {
                    Object object = dtde.getTransferable().getTransferData(fromNavigatorCollection);
                    if (object instanceof Collection) {
                        Collection c = (Collection) object;
                        for (Object o : c) {
                            if (o instanceof ObjectTreeNode) {
                                ObjectTreeNode otn = (ObjectTreeNode) o;
                                MetaObject mo = otn.getMetaObject();
                                beans.add(mo.getBean());
                            }
                        }

                    }
                } catch (Throwable t) {
                    log.fatal("Drop Problems occurred", t);//NOI18N
                }

            } else {
                log.fatal("Wrong transferable");//NOI18N
            }
            beansDropped(beans);

        } catch (Throwable ups) {
            log.error("Problem during the DnD Opertaion",ups);//NOI18N
        }
    }

    public abstract void beansDropped(ArrayList<CidsBean> beans);
}
