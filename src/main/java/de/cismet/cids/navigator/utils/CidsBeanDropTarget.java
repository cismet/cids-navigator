/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.cids.navigator.utils;

import de.cismet.cids.dynamics.CidsBean;
import java.awt.Component;
import java.awt.HeadlessException;
import java.util.ArrayList;

/**
 *
 * @author thorsten
 */
public class CidsBeanDropTarget extends AbstractCidsBeanDropTarget {
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private CidsBeanDropListener dropListener;

    public CidsBeanDropTarget(CidsBeanDropListenerComponent c) throws HeadlessException {
        super(c);
        dropListener = c;
    }

    public CidsBeanDropTarget(Component c) throws HeadlessException {
        super(c);
        if (c instanceof CidsBeanDropListener) {
            dropListener = (CidsBeanDropListener) c;
        } else {
            throw new IllegalArgumentException("Cosntructor-Parameter has to be a CidsBeanDropListener and a Component");
        }
    }

    @Override
    public void beansDropped(ArrayList<CidsBean> beans) {
        dropListener.beansDropped(beans);
    }
}
