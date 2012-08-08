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

import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

import de.cismet.cids.dynamics.CidsBean;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public class DefaultBindableReferenceComboModel extends AbstractListModel implements ComboBoxModel {

    //~ Instance fields --------------------------------------------------------

    private Vector<CidsBean> beans;
    private Vector<ListDataListener> listeners = new Vector<ListDataListener>();
    private CidsBean selectedItem = null;
    private boolean nullable = false;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DefaultBindableReferenceComboModel object.
     */
    public DefaultBindableReferenceComboModel() {
        this.beans = new Vector<CidsBean>();
    }
    /**
     * Creates a new DefaultBindableReferenceComboModel object.
     *
     * @param  beans  DOCUMENT ME!
     */
    public DefaultBindableReferenceComboModel(final Vector<CidsBean> beans) {
        this.beans = beans;
    }
    /**
     * Creates a new DefaultBindableReferenceComboModel object.
     *
     * @param  beans     DOCUMENT ME!
     * @param  nullable  DOCUMENT ME!
     */
    public DefaultBindableReferenceComboModel(final Vector<CidsBean> beans, final boolean nullable) {
        this(beans);
        this.nullable = nullable;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Object getElementAt(final int index) {
        if ((index == beans.size()) && isNullable()) {
            return null;
        } else {
            return beans.get(index);
        }
    }

    @Override
    public int getSize() {
        final int size = beans.size();
        if (isNullable()) {
            return size + 1;
        } else {
            return size;
        }
    }

    @Override
    public Object getSelectedItem() {
        return selectedItem;
    }

    @Override
    public void setSelectedItem(final Object anItem) {
        if (anItem instanceof CidsBean) {
            selectedItem = (CidsBean)anItem;
        } else {
            selectedItem = null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isNullable() {
        return nullable;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  nullable  DOCUMENT ME!
     */
    public void setNullable(final boolean nullable) {
        this.nullable = nullable;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Vector<CidsBean> getContentBeans() {
        return beans;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  beans  DOCUMENT ME!
     */
    public void setContentBeans(final Vector<CidsBean> beans) {
        this.beans = beans;
        fireContentsChanged(this, 0, getSize() - 1);
    }
}
