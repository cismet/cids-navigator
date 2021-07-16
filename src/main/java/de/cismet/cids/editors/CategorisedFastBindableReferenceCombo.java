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

import Sirius.server.middleware.types.MetaObject;

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

    private final boolean selfStructuring;
    private String splitBy = " - ";

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new CategorisedFastBindableReferenceCombo object.
     */
    public CategorisedFastBindableReferenceCombo() {
        this(true);
    }

    /**
     * Creates a new CategorisedFastBindableReferenceCombo object.
     *
     * @param  selfStructuring  DOCUMENT ME!
     */
    public CategorisedFastBindableReferenceCombo(final boolean selfStructuring) {
        super();
        this.selfStructuring = selfStructuring;
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
    public DefaultComboBoxModel createModelForMetaClass(final boolean nullable) throws Exception {
        if (selfStructuring) {
            final MetaObjectComboBoxModel origMod = (MetaObjectComboBoxModel)super.createModelForMetaClass(nullable);
            if (origMod == null) {
                return null;
            }

            final List<List> data = new ArrayList<>();

            int maxElements = 0;

            // determine the max number of sub categories
            for (int moIndex = 0; moIndex <= origMod.getSize(); moIndex++) {
                final MetaObject mo = (MetaObject)origMod.getElementAt(moIndex);
                if (mo != null) {
                    if (mo.toString().split(splitBy).length > maxElements) {
                        maxElements = mo.toString().split(splitBy).length - 1;
                    }
                }
            }

            for (int moIndex = 0; moIndex <= origMod.getSize(); moIndex++) {
                final MetaObject mo = (MetaObject)origMod.getElementAt(moIndex);
                if (mo != null) {
                    final String[] splitted = mo.toString().split(splitBy);
                    final List sub = new ArrayList(maxElements);
                    for (int splitIndex = 0; splitIndex < maxElements; splitIndex++) {
                        sub.add((splitIndex < (splitted.length - 1)) ? splitted[splitIndex] : null);
                    }

                    sub.add(mo);
                    data.add(sub);
                }
            }
            return new DefaultComboBoxModel(createModelData(data));
        } else {
            return (DefaultComboBoxModel)getModel();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  splitBy  DOCUMENT ME!
     */
    public void setSplitBy(final String splitBy) {
        this.splitBy = splitBy;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  data  DOCUMENT ME!
     */
    private void initRenderer(final List<List> data) {
        // show the categories
        setRenderer(new DefaultListCellRenderer() {

                @Override
                public Component getListCellRendererComponent(final JList list,
                        final Object value,
                        final int index,
                        final boolean isSelected,
                        final boolean cellHasFocus) {
                    boolean isSel = isCategory(value) ? false : isSelected;

                    if ((value instanceof MetaObject) && (cidsBean != null)) {
                        isSel = (((MetaObject)value).getBean().equals(cidsBean) ? true : isSel);
                    }
                    final Component ret = super.getListCellRendererComponent(
                            list,
                            value,
                            index,
                            isSel,
                            cellHasFocus);
                    final String text;
                    if (value == null) {
                        text = getNullValueRepresentation();
                    } else {
                        final int lastIndex = value.toString().lastIndexOf(splitBy);
                        final String name = (lastIndex < 0) ? value.toString()
                                                            : value.toString().substring(lastIndex + splitBy.length());
                        text = spaces(getCategoryLevel(value)) + name;
                    }
                    ((JLabel)ret).setText(text);
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
                                final Object o = tmp.get(i);
                                if ((o != null) && o.equals(value)) {
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

                private int lastIndex = -1;

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
                                if ((tmp.get(i) != null) && tmp.get(i).equals(value)) {
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
    }

    /**
     * Initialises the Combobox with the LightweightMetaObjects and the categories. The given list should contain lists
     * with the categories at the beginning and the LightweightMetaObject as the last element.
     *
     * @param  data  DOCUMENT ME!
     */
    public void init(final List<List> data) {
        sortData(data);

        try {
            final DefaultComboBoxModel mod = new DefaultComboBoxModel(createModelData(data));
            setModel(mod);
            setSelectedItem(cidsBean);
        } catch (Exception ex) {
            LOG.error(ex, ex);
        }
    }

    @Override
    public void setSelectedItem(final Object anObject) {
        super.setSelectedItem(anObject); // To change body of generated methods, choose Tools | Templates.
    }

    /**
     * DOCUMENT ME!
     *
     * @param   data  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private Object[] createModelData(final List<List> data) {
        final List<String> categories = new ArrayList<>();
        final List<Object> dataArray = new ArrayList<>();

        if (isNullable()) {
            dataArray.add(null);
        }

        for (int i = 0; i < data.size(); ++i) {
            for (int n = 0; n < (data.get(i).size() - 1); ++n) {
                final String category = (String)data.get(i).get(n);

                if ((category != null) && !category.isEmpty() && !categories.contains(category)) {
                    dataArray.add(category);
                    categories.add(category);
                }
            }
            dataArray.add(data.get(i).get(data.get(i).size() - 1));
        }

        initRenderer(data);
        return dataArray.toArray(new Object[dataArray.size()]);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   data  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private List sortData(final List data) {
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
        return data;
    }
}
