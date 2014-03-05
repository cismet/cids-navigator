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
package de.cismet.cids.utils.multibean;

import org.jdesktop.observablecollections.ObservableList;
import org.jdesktop.observablecollections.ObservableListListener;
import org.jdesktop.swingx.JXDatePicker;

import java.awt.BorderLayout;
import java.awt.Component;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

import de.cismet.cids.dynamics.CidsBean;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class MultiBeanHelper {

    //~ Static fields/initializers ---------------------------------------------

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(MultiBeanHelper.class);
    public static final String DIFFERENT_VALUE = "<html><i>unterschiedliche Werte</i>";

    //~ Instance fields --------------------------------------------------------

    private ImageIcon warning = new ImageIcon(getClass().getResource(
                "/de/cismet/cids/utils/multibean/warning.png"));

    private final Collection<CidsBean> beans = new ArrayList<CidsBean>();
    private final Lock lock = new ReentrantLock();
    private final PropertyChangeListener beansListener;
    private final PropertyChangeListener dummyListener;
    private final Map<String, ObservableListListener> listListenerMap = new HashMap<String, ObservableListListener>();
    private final Map<String, Object> valuesAllEqualsMap = new HashMap<String, Object>();
    private final Map<String, EmbeddedMultiBeanDisplay> componentMap = new HashMap<String, EmbeddedMultiBeanDisplay>();

    private CidsBean dummyBean;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new MultiBindingHelper object.
     */
    public MultiBeanHelper() {
        this.beansListener = new PropertyChangeListener() {

                @Override
                public void propertyChange(final PropertyChangeEvent evt) {
                    lock.lock();
                    try {
                        final String propertyName = evt.getPropertyName();
                        fillValuesAllEqualsMap(propertyName);
                    } catch (final Exception ex) {
                        LOG.error("error while setting property on dummybean", ex);
                    } finally {
                        lock.unlock();
                    }
                }
            };

        this.dummyListener = new PropertyChangeListener() {

                @Override
                public void propertyChange(final PropertyChangeEvent evt) {
                    if (evt.getSource().equals(dummyBean)) {
                        for (final CidsBean bean : beans) {
                            final String propertyName = evt.getPropertyName();
                            final Object value = evt.getNewValue();
                            if (!(value instanceof ObservableList)) {
                                try {
                                    bean.setProperty(propertyName, value);
                                } catch (final Exception ex) {
                                    LOG.error("error while setting property on collection bean", ex);
                                }
                            }
                        }
                    }
                }
            };
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  dummyBean  DOCUMENT ME!
     */
    public void setDummyBean(final CidsBean dummyBean) {
        final CidsBean old = this.dummyBean;
        if (old != null) {
            for (final String propertyName : old.getPropertyNames()) {
                if (listListenerMap.containsKey(propertyName)) {
                    listListenerMap.remove(propertyName);
                }
            }
            old.removePropertyChangeListener(dummyListener);
        }

        this.dummyBean = dummyBean;

        if (dummyBean != null) {
            for (final String propertyName : dummyBean.getPropertyNames()) {
                final Object value = dummyBean.getProperty(propertyName);
                if ((value != null) && (value instanceof ObservableList)) {
                    final ObservableList list = (ObservableList)value;
                    final ObservableListListener listener = new ObservableListListener() {

                            @Override
                            public void listElementsAdded(final ObservableList list,
                                    final int index,
                                    final int length) {
                                if (list.equals(dummyBean.getProperty(propertyName))) {
                                    for (final CidsBean bean : beans) {
                                        for (int i = index; i < (index + length); ++i) {
                                            final CidsBean listElement = (CidsBean)list.get(i);
                                            bean.getBeanCollectionProperty(propertyName).add(listElement);
                                        }
                                    }
                                }
                            }

                            @Override
                            public void listElementsRemoved(final ObservableList list,
                                    final int index,
                                    final List removedList) {
                                if (list.equals(dummyBean.getProperty(propertyName))) {
                                    for (final CidsBean bean : beans) {
                                        for (final CidsBean listElement : (Collection<CidsBean>)removedList) {
                                            bean.getBeanCollectionProperty(propertyName).remove(listElement);
                                        }
                                    }
                                }
                            }

                            @Override
                            public void listElementReplaced(final ObservableList ol, final int i, final Object o) {
                            }

                            @Override
                            public void listElementPropertyChanged(final ObservableList ol, final int i) {
                            }
                        };
                    listListenerMap.put(propertyName, listener);
                }
            }

            dummyBean.addPropertyChangeListener(dummyListener);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   propertyName  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private Boolean fillValuesAllEqualsMap(final String propertyName) {
        final Object propValue = dummyBean.getProperty(propertyName);
        if (propValue instanceof ObservableList) {
            return false;
        } else {
            boolean valuesAllEquals = true;
            boolean firstObject = true;
            Object value = null;
            for (final CidsBean bean : beans) {
                final boolean valuesEquals;
                if (bean.getProperty(propertyName) == null) {
                    valuesEquals = value == null;
                } else {
                    valuesEquals = bean.getProperty(propertyName).equals(value);
                }
                if (firstObject || valuesEquals) {
                    value = bean.getProperty(propertyName);
                } else {
                    valuesAllEquals = false;
                    break;
                }
                firstObject = false;
            }
            if (valuesAllEquals) {
                valuesAllEqualsMap.put(propertyName, value);
            }
            refreshComponent(propertyName);
            return valuesAllEquals;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  component     DOCUMENT ME!
     * @param  propertyName  DOCUMENT ME!
     */
    public void registerComponentForProperty(final JComponent component, final String propertyName) {
        if ((component instanceof JTextField) || (component instanceof JTextArea)) {
            final EmbeddedMultiBeanDisplay overlay = EmbeddedMultiBeanDisplay.getEmbeddedDisplayFor(component);
            overlay.setIcon(warning);
            overlay.setText(DIFFERENT_VALUE);
            component.setLayout(new BorderLayout());
            component.add(overlay, BorderLayout.WEST);
            componentMap.put(propertyName, overlay);
        } else if (component instanceof JComboBox) {
            final ListCellRenderer rend = ((JComboBox)component).getRenderer();
            ((JComboBox)component).setRenderer(new DefaultListCellRenderer() {

                    @Override
                    public Component getListCellRendererComponent(final JList<?> list,
                            final Object value,
                            final int index,
                            final boolean isSelected,
                            final boolean cellHasFocus) {
                        final JLabel comp = (JLabel)rend.getListCellRendererComponent(
                                list,
                                value,
                                index,
                                isSelected,
                                cellHasFocus);
                        if (!isValuesAllEquals(propertyName)) {
                            comp.setIcon(warning);
                        }
                        return comp;
                    }
                });
        } else if (component instanceof JXDatePicker) {
            final JFormattedTextField jft = ((JXDatePicker)component).getEditor();
            final EmbeddedMultiBeanDisplay overlay = EmbeddedMultiBeanDisplay.getEmbeddedDisplayFor(jft);
            overlay.setIcon(warning);
            overlay.setText("<unterschiedliche Werte>");
            jft.setLayout(new BorderLayout());
            jft.add(overlay, BorderLayout.WEST);
            componentMap.put(propertyName, overlay);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  propertyName  DOCUMENT ME!
     */
    private void refreshComponent(final String propertyName) {
        final EmbeddedMultiBeanDisplay component = componentMap.get(propertyName);
        // if (component instanceof JComponent) {
        if ((component != null) && !isValuesAllEquals(propertyName)) {
            component.doOverlay(true);
        } else if (component != null) {
            component.doOverlay(false);
        }
        // }
    }

    /**
     * DOCUMENT ME!
     */
    private void refillValuesAllEqualsMap() {
        valuesAllEqualsMap.clear();
        if (dummyBean != null) {
            for (final String propertyName : dummyBean.getPropertyNames()) {
                final Object value = dummyBean.getProperty(propertyName);
                if (value instanceof ObservableList) {
                    final ObservableList dummyList = (ObservableList)value;

                    for (final CidsBean bean : beans) {
                        dummyList.addAll(bean.getBeanCollectionProperty(propertyName));
                    }
                    dummyList.addObservableListListener(listListenerMap.get(propertyName));
                }
                fillValuesAllEqualsMap(propertyName);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   propertyName  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Boolean isValuesAllEquals(final String propertyName) {
        return valuesAllEqualsMap.containsKey(propertyName);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Collection<CidsBean> getBeans() {
        return new ArrayList<CidsBean>(beans);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  beans  DOCUMENT ME!
     */
    public void setBeans(final Collection<CidsBean> beans) {
        final Collection<CidsBean> old = this.beans;
        for (final CidsBean bean : old) {
            bean.removePropertyChangeListener(beansListener);
        }

        this.beans.clear();
        clearDummy();
        if (beans != null) {
            this.beans.addAll(beans);
        }
        refillValuesAllEqualsMap();

        // set values that are equals
        for (final String propertyName : valuesAllEqualsMap.keySet()) {
            try {
                dummyBean.setProperty(propertyName, valuesAllEqualsMap.get(propertyName));
            } catch (final Exception ex) {
                LOG.error(ex, ex);
            }
        }

        for (final CidsBean bean : beans) {
            bean.addPropertyChangeListener(beansListener);
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void clearDummy() {
        for (final String propertyName : dummyBean.getPropertyNames()) {
            if (!(dummyBean.getProperty(propertyName) instanceof ObservableList)) {
                try {
                    dummyBean.setProperty(propertyName, null);
                } catch (final Exception ex) {
                    LOG.error("error while setting property on dummybean", ex);
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public CidsBean getDummyBean() {
        return dummyBean;
    }
}
