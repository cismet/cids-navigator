/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.editors;

import Sirius.server.middleware.types.MetaClass;

import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.cismet.cids.dynamics.CidsBean;

/**
 * DOCUMENT ME!
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public class DefaultBeanInitializer implements BeanInitializer {

    //~ Static fields/initializers ---------------------------------------------

    protected static final String GEOM_TABLE_NAME = "geom";      // NOI18N
    protected static final String GEOM_FIELD_NAME = "geo_field"; // NOI18N

    protected static final Logger LOG = Logger.getLogger(DefaultBeanInitializer.class);

    //~ Instance fields --------------------------------------------------------

    protected final MetaClass metaClass;
    protected final String primaryKeyField;
    protected final Map<String, Object> simpleProperties;
    protected final Map<String, CidsBean> complexProperties;
    protected final Map<String, Collection<CidsBean>> arrayProperties;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DefaultBeanInitializer object.
     *
     * @param  template  DOCUMENT ME!
     */
    public DefaultBeanInitializer(final CidsBean template) {
        metaClass = template.getMetaObject().getMetaClass();
        primaryKeyField = metaClass.getPrimaryKey().toLowerCase();
        simpleProperties = new HashMap<String, Object>();
        complexProperties = new HashMap<String, CidsBean>();
        arrayProperties = new HashMap<String, Collection<CidsBean>>();
        initPropertyMaps(template);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  template  DOCUMENT ME!
     */
    protected void initPropertyMaps(final CidsBean template) {
        for (final String propertyName : template.getPropertyNames()) {
            final Object propertyValue = template.getProperty(propertyName);
            if (propertyValue instanceof CidsBean) {
                complexProperties.put(propertyName, (CidsBean)propertyValue);
            } else if (propertyValue instanceof Collection) {
                arrayProperties.put(propertyName, (Collection<CidsBean>)propertyValue);
            } else {
                simpleProperties.put(propertyName, propertyValue);
            }
        }
    }
//    protected void initPropertyMaps(CidsBean template) {
//        ObjectAttribute[] attributes = template.getMetaObject().getAttribs();
//        for (ObjectAttribute attr : attributes) {
//            if (!attr.isPrimaryKey() && !attr.isArray()) {
//                MemberAttributeInfo mai = attr.getMai();
//                String propertyName = mai.getFieldName().toLowerCase();
//                Object value = attr.getValue();
//                if (value instanceof MetaObject) {
//                    final MetaObject metaObject = (MetaObject) value;
//                    if (metaObject.getMetaClass().getTableName().equalsIgnoreCase("GEOM")) {
//                        complexProperties.put(propertyName, metaObject.getBean());
//                    }
//                } else {
//                    simpleProperties.put(propertyName, value);
//                }
//            }
//        }
//    }

    @Override
    public void initializeBean(final CidsBean beanToInit) throws Exception {
        initializeSimpleProperties(beanToInit);
        initializeComplexProperties(beanToInit);
        initializeArrayProperties(beanToInit);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   beanToInit  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    protected void initializeSimpleProperties(final CidsBean beanToInit) throws Exception {
        for (final Entry<String, Object> property : simpleProperties.entrySet()) {
            processSimpleProperty(beanToInit, property.getKey(), property.getValue());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   beanToInit  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    protected void initializeComplexProperties(final CidsBean beanToInit) throws Exception {
        for (final Entry<String, CidsBean> complexProperty : complexProperties.entrySet()) {
            processComplexProperty(beanToInit, complexProperty.getKey(), complexProperty.getValue());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   beanToInit  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    protected void initializeArrayProperties(final CidsBean beanToInit) throws Exception {
        for (final Entry<String, Collection<CidsBean>> arrayProperty : arrayProperties.entrySet()) {
            processArrayProperty(beanToInit, arrayProperty.getKey(), arrayProperty.getValue());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   beanToInit            DOCUMENT ME!
     * @param   propertyName          DOCUMENT ME!
     * @param   simpleValueToProcess  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    protected void processSimpleProperty(final CidsBean beanToInit,
            final String propertyName,
            final Object simpleValueToProcess) throws Exception {
        if (!propertyName.equals(primaryKeyField)) {
            beanToInit.setProperty(propertyName, simpleValueToProcess);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   beanToInit             DOCUMENT ME!
     * @param   propertyName           DOCUMENT ME!
     * @param   complexValueToProcess  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    protected void processComplexProperty(final CidsBean beanToInit,
            final String propertyName,
            final CidsBean complexValueToProcess) throws Exception {
        // default impl delivers deep copy of geom attributes and ignores all others
        if (complexValueToProcess.getMetaObject().getMetaClass().getTableName().equalsIgnoreCase(GEOM_TABLE_NAME)) {
            final CidsBean geomBean = complexValueToProcess.getMetaObject().getMetaClass().getEmptyInstance().getBean();
            geomBean.setProperty(GEOM_FIELD_NAME, complexValueToProcess.getProperty(GEOM_FIELD_NAME));
            beanToInit.setProperty(propertyName, geomBean);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   beanToInit           DOCUMENT ME!
     * @param   propertyName         DOCUMENT ME!
     * @param   arrayValueToProcess  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    protected void processArrayProperty(final CidsBean beanToInit,
            final String propertyName,
            final Collection<CidsBean> arrayValueToProcess) throws Exception {
        // does nothing but clear in default impl
        final Collection<CidsBean> collectionToInitialize = (Collection<CidsBean>)beanToInit.getProperty(propertyName);
        collectionToInitialize.clear();
    }
}
