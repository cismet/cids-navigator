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

import Sirius.server.middleware.types.LightweightMetaObject;

import org.apache.log4j.Logger;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

/**
 * This component represents a FastBindableReferenceCombo with categories.
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class CategorisedFastBindableReferenceCombo extends FastBindableReferenceCombo {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(CategorisedFastBindableReferenceCombo.class);

    //~ Instance fields --------------------------------------------------------

    private List<List> data;
    private int lastIndex = -1;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new CategorisedFastBindableReferenceCombo object.
     */
    public CategorisedFastBindableReferenceCombo() {
        super();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   value  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private boolean isCategory(final Object value) {
        return value instanceof String;
    }

    @Override
    protected void init() {
        try {
            final DefaultComboBoxModel mod = new DefaultComboBoxModel(createModelData());
            setModel(mod);
            setSelectedItem(cidsBean);
        } catch (Exception ex) {
            LOG.error(ex, ex);
        }
    }

    /**
     * Initialises the Combobox with the LightweightMetaObjects and the categories. The given list should contain lists
     * with the categories at the beginning and the LightweightMetaObject as the last element.
     *
     * @param  data  DOCUMENT ME!
     */
    public void init(final List<List> data) {
        this.data = data;
        sortData();

        // show the categories
        setRenderer(new DefaultListCellRenderer() {

                @Override
                public Component getListCellRendererComponent(final JList list,
                        final Object value,
                        final int index,
                        final boolean isSelected,
                        final boolean cellHasFocus) {
                    boolean isSel = isCategory(value) ? false : isSelected;

                    if ((value instanceof LightweightMetaObject) && (cidsBean != null)) {
                        isSel = (((LightweightMetaObject)value).getBean().equals(cidsBean) ? true : isSel);
                    }
                    final Component ret = super.getListCellRendererComponent(
                            list,
                            value,
                            index,
                            isSel,
                            cellHasFocus);
                    if (value == null) {
                        ((JLabel)ret).setText(getNullValueRepresentation());
                    }

                    if (value instanceof LightweightMetaObject) {
                        ((JLabel)ret).setText(((LightweightMetaObject)value).getName());
                    }
                    ((JLabel)ret).setText(spaces(getCategoryLevel(value)) + ((JLabel)ret).getText());

                    if (isCategory(value)) {
                        final JLabel label = (JLabel)ret;
                        final Font boldLabelFont = new Font(
                                label.getFont().getName(),
                                Font.BOLD,
                                label.getFont().getSize());

                        label.setFont(boldLabelFont);
                    }

                    return ret;
                }

                private String spaces(final int i) {
                    if (i == 0) {
                        return "";
                    }
                    final StringBuffer spaces = new StringBuffer(i * 2);

                    for (int n = 0; n < i; ++n) {
                        spaces.append("  ");
                    }

                    return spaces.toString();
                }

                private int getCategoryLevel(final Object value) {
                    if (isCategory(value)) {
                        for (final List tmp : data) {
                            for (int i = 0; i < (tmp.size() - 1); ++i) {
                                if (tmp.get(i).equals(value)) {
                                    return i;
                                }
                            }
                        }
                    } else {
                        for (final List tmp : data) {
                            if (tmp.get(tmp.size() - 1).equals(value)) {
                                for (int i = 0; i < tmp.size(); ++i) {
                                    if (tmp.get(i) == null) {
                                        return i;
                                    }
                                }
                                return tmp.size();
                            }
                        }
                    }

                    return 0;
                }
            });

        // prevent the selection of a category
        addItemListener(new ItemListener() {

                @Override
                public void itemStateChanged(final ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.DESELECTED) {
                        lastIndex = CategorisedFastBindableReferenceCombo.this.getIndexOf(e.getItem());
                    } else if (e.getStateChange() == ItemEvent.SELECTED) {
                        if (isCategory(e.getItem())) {
                            final int index = CategorisedFastBindableReferenceCombo.this.getIndexOf(e.getItem());
                            final Object nextSelection = getNextItem(
                                    e.getItem(),
                                    ((lastIndex != -1) && (lastIndex > index)));

                            if (nextSelection != null) {
                                EventQueue.invokeLater(new Runnable() {

                                        @Override
                                        public void run() {
                                            CategorisedFastBindableReferenceCombo.this.setSelectedItem(nextSelection);
                                        }
                                    });
                            }
                        }
                    }
                }

                private Object getNextItem(final Object value, final boolean rev) {
                    Object lastObject = null;

                    if (isCategory(value)) {
                        for (final List tmp : data) {
                            for (int i = 0; i < (tmp.size() - 1); ++i) {
                                if (tmp.get(i).equals(value)) {
                                    if (rev && (lastObject != null)) {
                                        return lastObject;
                                    } else {
                                        return tmp.get(tmp.size() - 1);
                                    }
                                }
                            }
                            lastObject = tmp.get(tmp.size() - 1);
                        }
                    }

                    return null;
                }
            });

        init();
    }

    @Override
    public void setSelectedItem(final Object anObject) {
        super.setSelectedItem(anObject); // To change body of generated methods, choose Tools | Templates.
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private Object[] createModelData() {
        final List<String> categories = new ArrayList<String>();
        final List<Object> dataArray = new ArrayList<Object>();

        if (isNullable()) {
            dataArray.add(null);
        }

        for (int i = 0; i < data.size(); ++i) {
            for (int n = 0; n < (data.get(i).size() - 1); ++n) {
                final String category = (String)data.get(i).get(n);

                if ((category != "") && !categories.contains(category)) {
                    dataArray.add(category);
                    categories.add(category);
                }
            }
            dataArray.add(data.get(i).get(data.get(i).size() - 1));
        }

        return dataArray.toArray(new Object[dataArray.size()]);
    }

    /**
     * DOCUMENT ME!
     */
    private void sortData() {
        Collections.sort(data, new Comparator<List>() {

                @Override
                public int compare(final List o1, final List o2) {
                    for (int i = 0; i < o1.size(); ++i) {
                        if ((o1.get(i) == null) && (o2.get(i) != null)) {
                            return 1;
                        } else if ((o2.get(i) == null) && (o1.get(i) != null)) {
                            return -1;
                        } else if ((o2.get(i) == null) && (o1.get(i) == null)) {
                        } else if (!o1.get(i).equals(o2.get(i))) {
                            return o1.toString().compareTo(o2.toString());
                        }
                    }

                    return 0;
                }
            });
    }
}
