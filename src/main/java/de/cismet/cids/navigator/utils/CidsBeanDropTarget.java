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

import java.awt.Component;
import java.awt.HeadlessException;

import java.util.ArrayList;

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
}
