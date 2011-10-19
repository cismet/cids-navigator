/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.editors;

import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.exception.ConnectionException;

import Sirius.server.localserver.attribute.MemberAttributeInfo;
import Sirius.server.middleware.types.MetaClass;
import Sirius.server.middleware.types.MetaClassStore;
import Sirius.server.middleware.types.MetaObject;

import org.jdesktop.beansbinding.Converter;
import org.jdesktop.beansbinding.Validator;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import de.cismet.cids.navigator.utils.ClassCacheMultiple;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class DefaultBindableCheckboxField extends JPanel implements Bindable, MetaClassStore, ActionListener {

    //~ Static fields/initializers ---------------------------------------------

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(
            DefaultBindableCheckboxField.class);

    //~ Instance fields --------------------------------------------------------

    Thread initThread = null;
    Thread refreshThread = null;

    private PropertyChangeSupport propertyChangeSupport;
    private List selectedElements = null;
    private MetaClass mc = null;
    private Map<JCheckBox, MetaObject> boxToObjectMapping = new HashMap<JCheckBox, MetaObject>();
    private volatile boolean initialised = false;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new CustomReferencedCheckboxField object.
     */
    public DefaultBindableCheckboxField() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public PropertyChangeSupport getPropertyChangeSupport() {
        if (propertyChangeSupport == null) {
            propertyChangeSupport = new PropertyChangeSupport(this);
        }
        return propertyChangeSupport;
    }

    /**
     * Add PropertyChangeListener.
     *
     * @param  listener  DOCUMENT ME!
     */
    @Override
    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        if (listener != null) {
            getPropertyChangeSupport().addPropertyChangeListener(listener);
        }
    }

    /**
     * Remove PropertyChangeListener.
     *
     * @param  listener  DOCUMENT ME!
     */
    @Override
    public void removePropertyChangeListener(final PropertyChangeListener listener) {
        getPropertyChangeSupport().removePropertyChangeListener(listener);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  elements  DOCUMENT ME!
     */
    public void setSelectedElements(final Object elements) {
        if (elements instanceof List) {
            this.selectedElements = (List)elements;
        }
        activateSelectedObjects();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Object getSelectedElements() {
        return selectedElements;
    }

    @Override
    public String getBindingProperty() {
        return "selectedElements";
    }

    @Override
    public Validator getValidator() {
        return null;
    }

    @Override
    public Converter getConverter() {
        return null;
    }

    @Override
    public Object getNullSourceValue() {
        return null;
    }

    @Override
    public Object getErrorSourceValue() {
        return null;
    }

    @Override
    public MetaClass getMetaClass() {
        return this.mc;
    }

    @Override
    public void setMetaClass(final MetaClass metaClass) {
        new Thread(new Runnable() {

                @Override
                public void run() {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("set meta class " + ((metaClass != null) ? metaClass.getName() : "null"));
                    }
                    if ((initThread != null) && initThread.isAlive()) {
                        initThread.interrupt();
                    }
                    while ((initThread != null) && initThread.isAlive()) {
                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException ex) {
                        }
                    }

                    mc = metaClass;
                    initThread = new Thread(new Runnable() {

                                @Override
                                public void run() {
                                    initBoxes();
                                    initialised = true;
                                    initThread = null;
                                }
                            });
                    initThread.start();
                }
            }).start();
    }

    /**
     * DOCUMENT ME!
     */
    private void initBoxes() {
        if (mc != null) {
            final MetaClass foreignClass = getReferencedClass(mc);
            final String query = "select " + foreignClass.getID() + ", " + foreignClass.getPrimaryKey() + " from "
                        + foreignClass.getTableName();

            try {
                final MetaObject[] metaObjects = SessionManager.getProxy().getMetaObjectByQuery(query, 0);
                JCheckBox box = null;
                setLayout(new GridLayout(metaObjects.length, 1));
                if (LOG.isDebugEnabled()) {
                    LOG.debug(metaObjects.length + "objects found.");
                }

                for (final MetaObject tmpMc : metaObjects) {
                    box = new JCheckBox(tmpMc.getBean().toString());
                    box.addActionListener(this);
                    box.setOpaque(false);
                    box.setContentAreaFilled(false);
                    add(box);
                    boxToObjectMapping.put(box, tmpMc);
                }
                activateSelectedObjects();
            } catch (ConnectionException e) {
                LOG.error("Error while loading the measurement object with query: " + query, e); // NOI18N
            }
        } else {
            LOG.error("The initBoxes method was invoked before the meta class was set.", new Throwable());
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void activateSelectedObjects() {
        if (selectedElements != null) {
            final Iterator<JCheckBox> it = boxToObjectMapping.keySet().iterator();

            while (it.hasNext()) {
                final JCheckBox tmp = it.next();
                final MetaObject mo = boxToObjectMapping.get(tmp);
                if ((mo != null) && selectedElements.contains(mo.getBean())) {
                    tmp.setSelected(true);
                } else {
                    tmp.setSelected(false);
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   mclass  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private MetaClass getReferencedClass(final MetaClass mclass) {
        MetaClass result = mclass;
        if (mclass.isArrayElementLink()) {
            final HashMap hm = mc.getMemberAttributeInfos();
            final Iterator it = hm.values().iterator();
            while (it.hasNext()) {
                final Object tmp = it.next();
                if (tmp instanceof MemberAttributeInfo) {
                    if (((MemberAttributeInfo)tmp).isForeignKey()) {
                        final int classId = ((MemberAttributeInfo)tmp).getForeignKeyClassId();
                        result = ClassCacheMultiple.getMetaClass(mclass.getDomain(), classId);
                    }
                }
            }
        } else {
            LOG.error("The given class " + mclass.getName() + " is no array element link.");
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  decider                 DOCUMENT ME!
     * @param  removeSelectedElements  DOCUMENT ME!
     */
    public void refreshCheckboxState(final FieldStateDecider decider, final boolean removeSelectedElements) {
        new Thread(new Runnable() {

                @Override
                public void run() {
                    while ((refreshThread != null) && refreshThread.isAlive()) {
                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException ex) {
                        }
                    }
                    refreshThread = new Thread(new Runnable() {

                                @Override
                                public void run() {
                                    while (!initialised) {
                                        try {
                                            Thread.sleep(50);
                                        } catch (final InterruptedException e) {
                                            // nothing to do
                                        }
                                    }
                                    if (LOG.isDebugEnabled()) {
                                        LOG.debug("refresh CheckboxState", new Exception());
                                    }

                                    final Iterator<JCheckBox> it = boxToObjectMapping.keySet().iterator();
                                    if (removeSelectedElements) {
                                        selectedElements.clear();
                                    }

                                    while (it.hasNext()) {
                                        final JCheckBox box = it.next();
                                        box.setEnabled(decider.isCheckboxForClassActive(boxToObjectMapping.get(box)));
                                        box.setSelected(false);
                                        if (Thread.currentThread().isInterrupted()) {
                                            return;
                                        }
                                    }
                                    activateSelectedObjects();
                                }
                            });

                    refreshThread.start();
                }
            }).start();
    }

    /**
     * DOCUMENT ME!
     */
    public void dispose() {
        if ((initThread != null) && initThread.isAlive()) {
            initThread.interrupt();
        }
        if ((refreshThread != null) && refreshThread.isAlive()) {
            refreshThread.interrupt();
        }
        selectedElements = null;
        mc = null;
        boxToObjectMapping = new HashMap<JCheckBox, MetaObject>();
        initialised = false;
        this.removeAll();
    }

    @Override
    public void actionPerformed(final ActionEvent ae) {
        final MetaObject mo = boxToObjectMapping.get((JCheckBox)ae.getSource());
        if (selectedElements.contains(mo.getBean())) {
            selectedElements.remove(mo.getBean());
        } else {
            selectedElements.add(mo.getBean());
        }
    }
}
