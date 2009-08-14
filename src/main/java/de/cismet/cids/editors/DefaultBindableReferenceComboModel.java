/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.cids.editors;

import de.cismet.cids.dynamics.CidsBean;
import java.util.Vector;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

/**
 *
 * @author thorsten
 */
public class DefaultBindableReferenceComboModel extends AbstractListModel implements ComboBoxModel {

    private Vector<CidsBean> beans;
    private Vector<ListDataListener> listeners = new Vector<ListDataListener>();
    private CidsBean selectedItem = null;
    private boolean nullable = false;

    public DefaultBindableReferenceComboModel() {
        this.beans = new Vector<CidsBean>();
    }
    public DefaultBindableReferenceComboModel(Vector<CidsBean> beans) {
        this.beans = beans;
    }
    public DefaultBindableReferenceComboModel(Vector<CidsBean> beans,boolean nullable) {
        this(beans);
        this.nullable=nullable;
    }

    public Object getElementAt(int index) {
        if (index == beans.size() && isNullable()) {
            return null;
        } else {
            return beans.get(index);
        }
    }

    public int getSize() {
        int size = beans.size();
        if (isNullable()) {
            return size + 1;
        } else {
            return size;
        }
    }

    public Object getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(Object anItem) {
        if (anItem instanceof CidsBean) {
            selectedItem = (CidsBean) anItem;
        } else {
            selectedItem = null;
        }
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public Vector<CidsBean> getContentBeans() {
        return beans;
    }

    public void setContentBeans(Vector<CidsBean> beans) {
        this.beans = beans;
        fireContentsChanged(this, 0, getSize()-1);
    }


}
