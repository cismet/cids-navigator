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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.cismet.cids.dynamics.CidsBean;

import de.cismet.cids.utils.CidsBeanDeepPropertyListener;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class MultiBeanHelper implements PropertyChangeListener {

    //~ Static fields/initializers ---------------------------------------------

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(MultiBeanHelper.class);
    public static final String EVENT_NAME = "refreshed";

    //~ Instance fields --------------------------------------------------------

    private final Collection<CidsBean> beans = new ArrayList<CidsBean>();
    private boolean isLocked = false;
    private final HashMap<String, CidsBeanDeepPropertyListener> cidsBeanFollowerMap =
        new HashMap<String, CidsBeanDeepPropertyListener>();
    private final HashMap<String, CidsBeanDeepPropertyListener> dummyBeanFollowerMap =
        new HashMap<String, CidsBeanDeepPropertyListener>();
    private final Map<String, ObservableListListener> listListenerMap = new HashMap<String, ObservableListListener>();
    private final Map<String, Object> valuesAllEqualsMap = new HashMap<String, Object>();
    private final Collection<String> attachedProperties = new ArrayList<String>();
    private final Collection<MultiBeanHelperListener> listeners = new ArrayList<MultiBeanHelperListener>();

    private CidsBean dummyBean;
    private boolean loading = false;

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  listener  DOCUMENT ME!
     */
    @Deprecated
    public void addPropertyChangeListener(final PropertyChangeListener listener) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param  listener  DOCUMENT ME!
     */
    @Deprecated
    public void removePropertyChangeListener(final PropertyChangeListener listener) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param  cidsBean  DOCUMENT ME!
     */
    public final void deattachCidsBeanTrigger(final CidsBean cidsBean) {
        for (final String property : getAttachedProperties()) {
            removeTriggerProperty(cidsBeanFollowerMap, property);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  cidsBean  DOCUMENT ME!
     */
    public final void attachCidsBeanTrigger(final CidsBean cidsBean) {
        for (final String attachedProperty : getAttachedProperties()) {
            addTriggerProperty(cidsBeanFollowerMap, attachedProperty, cidsBean);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  cidsBean  DOCUMENT ME!
     */
    public final void deattachDummyBeanTrigger(final CidsBean cidsBean) {
        for (final String property : new ArrayList<String>(getAttachedProperties())) {
            removeTriggerProperty(dummyBeanFollowerMap, property);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  cidsBean  DOCUMENT ME!
     */
    public final void attachDummyBeanTrigger(final CidsBean cidsBean) {
        for (final String attachedProperty : new ArrayList<String>(getAttachedProperties())) {
            addTriggerProperty(dummyBeanFollowerMap, attachedProperty, cidsBean);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  followerMap  DOCUMENT ME!
     * @param  property     DOCUMENT ME!
     * @param  cidsBean     DOCUMENT ME!
     */
    private void addTriggerProperty(final HashMap<String, CidsBeanDeepPropertyListener> followerMap,
            final String property,
            final CidsBean cidsBean) {
        final CidsBeanDeepPropertyListener follower = new CidsBeanDeepPropertyListener(cidsBean, property);
        followerMap.put(property, follower);
        follower.addPropertyChangeListener(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  followerMap  DOCUMENT ME!
     * @param  property     DOCUMENT ME!
     */
    public final void removeTriggerProperty(final HashMap<String, CidsBeanDeepPropertyListener> followerMap,
            final String property) {
        final CidsBeanDeepPropertyListener follower = followerMap.remove(property);
        if (follower != null) {
            follower.removePropertyChangeListener(this);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Collection<String> getAttachedProperties() {
        return attachedProperties;
    }

    /**
     * DOCUMENT ME!
     */
    private void refillAttachedProperties() {
        attachedProperties.clear();
        attachedProperties.addAll(createCidsBeanPropertiesPath(null, dummyBean));
    }

    /**
     * DOCUMENT ME!
     *
     * @param   path      DOCUMENT ME!
     * @param   cidsBean  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private static Collection<String> createCidsBeanPropertiesPath(final String path, final CidsBean cidsBean) {
        final Collection coll = new ArrayList();
        for (final String propertyName : cidsBean.getPropertyNames()) {
            final String subPath = (path == null) ? propertyName : (path + "." + propertyName);
            if (cidsBean.getProperty(propertyName) instanceof CidsBean) {
                coll.addAll(createCidsBeanPropertiesPath(subPath, (CidsBean)cidsBean.getProperty(propertyName)));
            } else {
                coll.add(subPath);
            }
        }
        return coll;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  dummyBean  DOCUMENT ME!
     */
    public void setDummyBean(final CidsBean dummyBean) {
        deattachDummyBeanTrigger(dummyBean);

        final CidsBean old = this.dummyBean;
        if (old != null) {
            for (final String propertyName : new ArrayList<String>(getAttachedProperties())) {
                if (listListenerMap.containsKey(propertyName)) {
                    listListenerMap.remove(propertyName);
                }
            }
        }

        this.dummyBean = dummyBean;

        if (dummyBean != null) {
            refillAttachedProperties();
            for (final String propertyName : new ArrayList<String>(getAttachedProperties())) {
                final Object value = dummyBean.getProperty(propertyName);
                if ((value != null) && (value instanceof ObservableList)) {
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

            attachDummyBeanTrigger(dummyBean);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   propertyName  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private boolean fillValuesAllEqualsMap(final String propertyName) {
        if (valuesAllEqualsMap.containsKey(propertyName)) {
            valuesAllEqualsMap.remove(propertyName);
        }
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
            fireAllEqualsChanged(propertyName, valuesAllEquals);
            return valuesAllEquals;
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void refillValuesAllEqualsMap() {
        fireRefillAllEqualsMapStarted();
        valuesAllEqualsMap.clear();
        if (dummyBean != null) {
            for (final String propertyName : getAttachedProperties()) {
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
        fireRefillAllEqualsMapDone();
    }

    /**
     * DOCUMENT ME!
     *
     * @param   propertyName  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isValuesAllEquals(final String propertyName) {
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
        for (final CidsBean bean : new ArrayList<CidsBean>(this.beans)) {
            deattachCidsBeanTrigger(bean);
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
            attachCidsBeanTrigger(bean);
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void clearDummy() {
        for (final String propertyName : getAttachedProperties()) {
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

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        if ((evt.getSource() instanceof CidsBeanDeepPropertyListener)
                    && cidsBeanFollowerMap.containsKey(evt.getPropertyName())) {
            final CidsBeanDeepPropertyListener follower = (CidsBeanDeepPropertyListener)evt.getSource();
            if (follower.getBean().equals(dummyBean)) {
                if (!isLocked) {
                    try {
                        isLocked = true;
                        for (final CidsBean bean : beans) {
                            try {
                                final String propertyName = evt.getPropertyName();
                                final Object value = evt.getNewValue();
                                if (!(value instanceof ObservableList)) {
                                    bean.setProperty(propertyName, value);
                                    fireAllEqualsChanged(propertyName, true);
                                }
                            } catch (final Exception ex) {
                                LOG.error("error while setting property on collection bean", ex);
                            }
                        }
                    } finally {
                        isLocked = false;
                    }
                }
            } else if (beans.contains(follower.getBean())) {
                if (!isLocked) {
                    try {
                        isLocked = true;
                        final String propertyName = evt.getPropertyName();
                        fillValuesAllEqualsMap(propertyName);
                    } catch (final Exception ex) {
                        LOG.error("error while setting property on dummybean", ex);
                    } finally {
                        isLocked = false;
                    }
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   listener  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean addListener(final MultiBeanHelperListener listener) {
        return listeners.add(listener);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   listener  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean removeListener(final MultiBeanHelperListener listener) {
        return listeners.remove(listener);
    }

    /**
     * DOCUMENT ME!
     */
    public void fireRefillAllEqualsMapStarted() {
        setLoading(true);
        for (final MultiBeanHelperListener listener : listeners) {
            listener.refillAllEqualsMapStarted();
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void fireRefillAllEqualsMapDone() {
        setLoading(false);
        for (final MultiBeanHelperListener listener : listeners) {
            listener.refillAllEqualsMapDone();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  propertyName  event DOCUMENT ME!
     * @param  allEquals     DOCUMENT ME!
     */
    public void fireAllEqualsChanged(final String propertyName, final boolean allEquals) {
        if (!isLoading()) {
            for (final MultiBeanHelperListener listener : listeners) {
                listener.allEqualsChanged(propertyName, allEquals);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isLoading() {
        return loading;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  loading  DOCUMENT ME!
     */
    private void setLoading(final boolean loading) {
        this.loading = loading;
    }
}
