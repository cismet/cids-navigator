/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.editors;

import Sirius.navigator.tools.MetaObjectCache;

import Sirius.server.localserver.attribute.MemberAttributeInfo;
import Sirius.server.middleware.types.MetaClass;
import Sirius.server.middleware.types.MetaClassStore;
import Sirius.server.middleware.types.MetaObject;

import org.apache.log4j.Logger;

import org.jdesktop.beansbinding.Converter;
import org.jdesktop.beansbinding.Validator;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import de.cismet.cids.client.tools.ClientConnectionContextUtils;

import de.cismet.cids.navigator.utils.ClassCacheMultiple;

import de.cismet.connectioncontext.ClientConnectionContext;
import de.cismet.connectioncontext.ConnectionContext;
import de.cismet.connectioncontext.ConnectionContextProvider;

import de.cismet.tools.CismetThreadPool;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class DefaultBindableCheckboxField extends JPanel implements Bindable,
    MetaClassStore,
    ActionListener,
    ConnectionContextProvider {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(DefaultBindableCheckboxField.class);

    //~ Instance fields --------------------------------------------------------

    private PropertyChangeSupport propertyChangeSupport;
    private List selectedElements = null;
    private MetaClass mc = null;
    private Map<JCheckBox, MetaObject> boxToObjectMapping = new HashMap<>();
    private volatile boolean threadRunning = false;
    private Color backgroundSelected = null;
    private Color backgroundUnselected = null;
    private boolean readOnly = false;
    private final Comparator<MetaObject> comparator;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new CustomReferencedCheckboxField object.
     */
    public DefaultBindableCheckboxField() {
        this(null);
    }

    /**
     * Creates a new CustomReferencedCheckboxField object.
     *
     * @param  comparator  DOCUMENT ME!
     */
    public DefaultBindableCheckboxField(final Comparator<MetaObject> comparator) {
        this.comparator = comparator;
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
        if (LOG.isDebugEnabled()) {
            LOG.debug("set meta class " + ((metaClass != null) ? metaClass.getName() : "null"));
        }
        CismetThreadPool.execute(new SwingWorker<MetaObject[], Void>() {

                @Override
                protected MetaObject[] doInBackground() throws Exception {
                    Thread.currentThread().setName("DefaultBindableCheckboxField setMetaClass()");
                    while (!setThreadRunning()) {
                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException ex) {
                        }
                    }

                    mc = metaClass;
                    boxToObjectMapping = new HashMap<JCheckBox, MetaObject>();

                    if (mc != null) {
                        final MetaClass foreignClass = getReferencedClass(mc);
                        final String query = "select " + foreignClass.getID() + ", " + foreignClass.getPrimaryKey()
                                    + " from "
                                    + foreignClass.getTableName();

                        return MetaObjectCache.getInstance()
                                    .getMetaObjectsByQuery(query, mc.getDomain(), getConnectionContext());
                    } else {
                        LOG.error("The meta class was not set.", new Throwable());
                    }

                    return null;
                }

                @Override
                protected void done() {
                    try {
                        final MetaObject[] metaObjects = get();
                        DefaultBindableCheckboxField.this.removeAll();

                        if (metaObjects != null) {
                            if (comparator != null) {
                                Arrays.sort(metaObjects, comparator);
                            }
                            JCheckBox box = null;
                            DefaultBindableCheckboxField.this.setLayout(new GridLayout(metaObjects.length, 1));
                            if (LOG.isDebugEnabled()) {
                                LOG.debug(metaObjects.length + "objects found.");
                            }

                            for (final MetaObject tmpMc : metaObjects) {
                                box = new JCheckBox(tmpMc.getBean().toString());
                                box.addActionListener(DefaultBindableCheckboxField.this);
                                box.setOpaque(false);
                                box.setContentAreaFilled(false);
                                box.setFocusPainted(false);
                                box.setBorder(new EmptyBorder(2, 2, 0, 2));
                                add(box);
                                boxToObjectMapping.put(box, tmpMc);
                            }
                            activateSelectedObjects();
                            setReadOnly(readOnly);
                        }
                    } catch (Exception e) {
                        LOG.error("Error while filling a checkbox field.", e); // NOI18N
                    } finally {
                        threadRunning = false;
                    }
                }
            });
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

                    if (backgroundSelected != null) {
                        tmp.setOpaque(true);
                        tmp.setContentAreaFilled(true);
                        tmp.setBackground(backgroundSelected);
                    }
                } else {
                    tmp.setSelected(false);

                    if (backgroundUnselected != null) {
                        tmp.setOpaque(true);
                        tmp.setContentAreaFilled(true);
                        tmp.setBackground(backgroundUnselected);
                    }
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
        refreshCheckboxState(decider, false, removeSelectedElements);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  decider                 DOCUMENT ME!
     * @param  hideElements            Elements which are not accepted by the decider should be shown as unenabled, if
     *                                 hideElements is false. Otherwise, the elements should not be shown, if they are
     *                                 not accepted by the decider.
     * @param  removeSelectedElements  DOCUMENT ME!
     */
    public void refreshCheckboxState(final FieldStateDecider decider,
            final boolean hideElements,
            final boolean removeSelectedElements) {
        CismetThreadPool.execute(new SwingWorker<Void, Void>() {

                @Override
                protected Void doInBackground() throws Exception {
                    Thread.currentThread().setName("DefaultBindableCheckBoxField refreshCheckboxState()");
                    while (!setThreadRunning()) {
                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException ex) {
                        }
                    }

                    return null;
                }

                @Override
                protected void done() {
                    try {
                        final JCheckBox[] boxes = boxToObjectMapping.keySet()
                                    .toArray(new JCheckBox[boxToObjectMapping.keySet().size()]);

                        if (comparator != null) {
                            Arrays.sort(boxes, new Comparator<JCheckBox>() {

                                    @Override
                                    public int compare(final JCheckBox o1, final JCheckBox o2) {
                                        final MetaObject m1 = boxToObjectMapping.get(o1);
                                        final MetaObject m2 = boxToObjectMapping.get(o2);

                                        return comparator.compare(m1, m2);
                                    }
                                });
                        }

                        if (removeSelectedElements) {
                            selectedElements.clear();
                        }

                        if (hideElements) {
                            removeAll();
                        }

                        for (final JCheckBox box : boxes) {
                            if (!hideElements) {
                                box.setEnabled(
                                    !readOnly
                                            && decider.isCheckboxForClassActive(boxToObjectMapping.get(box)));
                            } else {
                                if (decider.isCheckboxForClassActive(boxToObjectMapping.get(box))) {
                                    add(box);
                                }
                            }
                            box.setSelected(false);
                            if (Thread.currentThread().isInterrupted()) {
                                return;
                            }
                        }
                        activateSelectedObjects();
                    } finally {
                        threadRunning = false;
                    }
                }
            });
    }

    /**
     * DOCUMENT ME!
     */
    public void dispose() {
    }

    @Override
    public void actionPerformed(final ActionEvent ae) {
        final MetaObject mo = boxToObjectMapping.get((JCheckBox)ae.getSource());
        final List old = new ArrayList(selectedElements);
        if (selectedElements.contains(mo.getBean())) {
            selectedElements.remove(mo.getBean());
        } else {
            selectedElements.add(mo.getBean());
        }

        final JCheckBox box = (JCheckBox)ae.getSource();
        box.setOpaque(false);
        box.setContentAreaFilled(false);

        if (box.isSelected()) {
            if (backgroundSelected != null) {
                box.setOpaque(true);
                box.setContentAreaFilled(true);
                box.setBackground(backgroundSelected);
            }
        } else {
            if (backgroundUnselected != null) {
                box.setOpaque(true);
                box.setContentAreaFilled(true);
                box.setBackground(backgroundUnselected);
            }
        }

        propertyChangeSupport.firePropertyChange("selectedElements", old, selectedElements);
        propertyChangeSupport.firePropertyChange("selectedElements", null, mo.getBean());
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private synchronized boolean setThreadRunning() {
        if (threadRunning) {
            return false;
        } else {
            threadRunning = true;
            return true;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  the backgroundSelected
     */
    public Color getBackgroundSelected() {
        return backgroundSelected;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  backgroundSelected  the backgroundSelected to set
     */
    public void setBackgroundSelected(final Color backgroundSelected) {
        this.backgroundSelected = backgroundSelected;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  the backgroundUnselected
     */
    public Color getBackgroundUnselected() {
        return backgroundUnselected;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  backgroundUnselected  the backgroundUnselected to set
     */
    public void setBackgroundUnselected(final Color backgroundUnselected) {
        this.backgroundUnselected = backgroundUnselected;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  readOnly  DOCUMENT ME!
     */
    public void setReadOnly(final boolean readOnly) {
        this.readOnly = readOnly;
        final Iterator<JCheckBox> it = boxToObjectMapping.keySet().iterator();

        while (it.hasNext()) {
            final JCheckBox box = it.next();
            box.setEnabled(!readOnly && box.isEnabled());
        }
    }

    @Override
    public ClientConnectionContext getConnectionContext() {
        return ClientConnectionContextUtils.getFirstParentClientConnectionContext(this);
    }
}
