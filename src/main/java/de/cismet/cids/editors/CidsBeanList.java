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
package de.cismet.cids.editors;

import Sirius.navigator.types.treenode.DefaultMetaTreeNode;
import Sirius.navigator.types.treenode.ObjectTreeNode;
import Sirius.navigator.ui.dnd.MetaTreeNodeTransferable;

import Sirius.server.middleware.types.MetaObjectNode;

import java.awt.Component;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;

import de.cismet.cids.dynamics.CidsBean;

import de.cismet.connectioncontext.ClientConnectionContext;
import de.cismet.connectioncontext.ConnectionContext;
import de.cismet.connectioncontext.ConnectionContextProvider;

/**
 * A list that can display cidsBeans with their icon and name and allows the drag operation.
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class CidsBeanList extends JList implements DragSourceListener, DragGestureListener, ConnectionContextProvider {

    //~ Instance fields --------------------------------------------------------

    private DragSource ds;
    private MetaTreeNodeTransferable metaTransferable;
    private final ClientConnectionContext connectionContext;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new CidsBeanList object.
     *
     * @param  connectionContext  DOCUMENT ME!
     */
    public CidsBeanList(final ClientConnectionContext connectionContext) {
        this.connectionContext = connectionContext;
        ds = DragSource.getDefaultDragSource();
        final DragGestureRecognizer dgr = ds.createDefaultDragGestureRecognizer(
                this,
                DnDConstants.ACTION_COPY_OR_MOVE,
                this);

        // shows CidsBeans with their icon and name
        setCellRenderer(new DefaultListCellRenderer() {

                @Override
                public Component getListCellRendererComponent(final JList list,
                        final Object value,
                        final int index,
                        final boolean isSelected,
                        final boolean cellHasFocus) {
                    final Component result = super.getListCellRendererComponent(
                            list,
                            value,
                            index,
                            isSelected,
                            cellHasFocus);

                    if ((result instanceof JLabel) && (value instanceof CidsBean)) {
                        final CidsBean bean = (CidsBean)value;

                        if (bean.getMetaObject().getMetaClass().getObjectIconData() != null) {
                            ((JLabel)result).setIcon(
                                new ImageIcon(bean.getMetaObject().getMetaClass().getObjectIconData()));
                        }
                        ((JLabel)result).setText(value.toString());
                    }

                    return result;
                }
            });
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void dragEnter(final DragSourceDragEvent dsde) {
    }

    @Override
    public void dragOver(final DragSourceDragEvent dsde) {
    }

    @Override
    public void dropActionChanged(final DragSourceDragEvent dsde) {
    }

    @Override
    public void dragExit(final DragSourceEvent dse) {
    }

    @Override
    public void dragDropEnd(final DragSourceDropEvent dsde) {
    }

    @Override
    public void dragGestureRecognized(final DragGestureEvent dge) {
        final List<DefaultMetaTreeNode> selectedValues = new ArrayList<DefaultMetaTreeNode>();

        if (getSelectedValues() != null) {
            for (final Object bean : getSelectedValues()) {
                final MetaObjectNode mon = new MetaObjectNode((CidsBean)bean);
                final ObjectTreeNode metaTreeNode = new ObjectTreeNode(mon, getConnectionContext());

                selectedValues.add(metaTreeNode);
            }

            this.metaTransferable = new MetaTreeNodeTransferable(selectedValues);
            this.metaTransferable.setTransferAction(dge.getDragAction());
            this.ds.startDrag(
                dge,
                DragSource.DefaultMoveDrop,
                this.metaTransferable,
                this);
        }
    }

    @Override
    public ClientConnectionContext getConnectionContext() {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
                                                                       // Tools | Templates.
    }
}
