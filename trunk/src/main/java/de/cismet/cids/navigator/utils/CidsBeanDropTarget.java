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

import java.awt.Color;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;

import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;

import de.cismet.cids.dynamics.CidsBean;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public class CidsBeanDropTarget extends AbstractCidsBeanDropTarget {

    //~ Instance fields --------------------------------------------------------

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private CidsBeanDropListener dropListener;
    private Border old = null;
    private Border active = new LineBorder(Color.GRAY, 2);
    private Border etched = new EtchedBorder();

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new CidsBeanDropTarget object.
     *
     * @param   c  DOCUMENT ME!
     *
     * @throws  HeadlessException  DOCUMENT ME!
     */
    public CidsBeanDropTarget(final CidsBeanDropListenerComponent c) throws HeadlessException {
        super(c);
        dropListener = c;
    }

    /**
     * Creates a new CidsBeanDropTarget object.
     *
     * @param   c  DOCUMENT ME!
     *
     * @throws  HeadlessException         DOCUMENT ME!
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    public CidsBeanDropTarget(final Component c) throws HeadlessException {
        super(c);
        if (c instanceof CidsBeanDropListener) {
            dropListener = (CidsBeanDropListener)c;
        } else {
            throw new IllegalArgumentException(
                "Cosntructor-Parameter has to be a CidsBeanDropListener and a Component"); // NOI18N
        }
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void beansDropped(final ArrayList<CidsBean> beans) {
        dropListener.beansDropped(beans);
    }

    @Override
    public void dragOver(final DropTargetDragEvent dtde) {
        super.dragOver(dtde);
        if (getComponent() instanceof DropTargetListener) {
            ((DropTargetListener)getComponent()).dragOver(dtde);
        }
    }

    @Override
    public void dragExit(final DropTargetEvent dte) {
        super.dragExit(dte);
        if (getComponent() instanceof DropTargetListener) {
            ((DropTargetListener)getComponent()).dragExit(dte);
        }
    }

    @Override
    public void dragEnter(final DropTargetDragEvent dtde) {
        super.dragEnter(dtde);
        if (getComponent() instanceof DropTargetListener) {
            ((DropTargetListener)getComponent()).dragEnter(dtde);
        }
    }
}
