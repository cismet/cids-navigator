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
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import de.cismet.cids.dynamics.CidsBean;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class DefaultBindableRadioButtonField extends JPanel implements Bindable, MetaClassStore, ActionListener {

    //~ Static fields/initializers ---------------------------------------------

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(
            DefaultBindableRadioButtonField.class);
    private static final String BINDING_PROPERTY = "selectedElements";

    //~ Instance fields --------------------------------------------------------

    private boolean enableLabels = true;
    private HashMap<Object, Icon> icons = null;
    private String iconProperty = null;
    private ButtonGroup bg;
    private CidsBean selectedElements = null;
    private MetaClass mc = null;
    private Map<JRadioButton, MetaObject> boxToObjectMapping = new HashMap<JRadioButton, MetaObject>();
    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new CustomReferencedCheckboxField object.
     */
    public DefaultBindableRadioButtonField() {
        bg = new ButtonGroup();
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        if (this.support != null) {
            this.support.addPropertyChangeListener(listener);
        }
    }

    @Override
    public void removePropertyChangeListener(final PropertyChangeListener listener) {
        if (this.support != null) {
            this.support.removePropertyChangeListener(listener);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  element  DOCUMENT ME!
     */
    public void setSelectedElements(final Object element) {
        if (element instanceof CidsBean) {
            this.selectedElements = (CidsBean)element;
        }

        if (selectedElements != null) {
            final Iterator<JRadioButton> it = boxToObjectMapping.keySet().iterator();

            while (it.hasNext()) {
                final JRadioButton tmp = it.next();
                final MetaObject mo = boxToObjectMapping.get(tmp);
                if ((mo != null) && selectedElements.equals(mo.getBean())) {
                    bg.setSelected(tmp.getModel(), true);
                }
            }
        }
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
        return BINDING_PROPERTY;
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
        this.mc = metaClass;
        initBoxes();
    }

    /**
     * DOCUMENT ME!
     */
    private void initBoxes() {
        if (mc != null) {
            final String query = "select " + mc.getID() + ", " + mc.getPrimaryKey() + " from "
                        + mc.getTableName();

            try {
                final MetaObject[] metaObjects = SessionManager.getProxy().getMetaObjectByQuery(query, 0);
                JRadioButton button = null;
                if (icons == null) {
                    setLayout(new GridLayout(metaObjects.length, 1));
                } else {
                    setLayout(new GridLayout(metaObjects.length, 2));
                }
                if (LOG.isDebugEnabled()) {
                    LOG.debug(metaObjects.length + "objects found.");
                }

                for (final MetaObject tmpMc : metaObjects) {
                    button = new JRadioButton();
                    button.addActionListener(this);
                    button.setOpaque(false);
                    button.setContentAreaFilled(false);
                    if (enableLabels) {
                        button.setText(tmpMc.getBean().toString());
                    }
                    bg.add(button);
                    if (icons != null) {
                        final Icon i = icons.get(tmpMc.getBean().getProperty(iconProperty));
                        final JLabel l = new JLabel();
                        if (i != null) {
                            l.setIcon(i);
                        }
                        l.setSize(i.getIconWidth(), i.getIconHeight());
                        add(l);
                    }
                    add(button);
                    boxToObjectMapping.put(button, tmpMc);
                }
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
    public void dispose() {
        this.removeAll();
        boxToObjectMapping.clear();
    }

    @Override
    public void actionPerformed(final ActionEvent ae) {
        final MetaObject mo = boxToObjectMapping.get((JRadioButton)ae.getSource());

        final Object old = selectedElements;
        selectedElements = mo.getBean();
        support.firePropertyChange(BINDING_PROPERTY, old, mo.getBean());
    }

    /**
     * DOCUMENT ME!
     *
     * @return  the icons
     */
    public HashMap<Object, Icon> getIcons() {
        return icons;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  icons         the icons to set
     * @param  iconProperty  DOCUMENT ME!
     */
    public void setIcons(final HashMap<Object, Icon> icons, final String iconProperty) {
        this.icons = icons;
        this.iconProperty = iconProperty;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  the enableLabels
     */
    public boolean isEnableLabels() {
        return enableLabels;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  enableLabels  the enableLabels to set
     */
    public void setEnableLabels(final boolean enableLabels) {
        this.enableLabels = enableLabels;
    }
}
