package de.cismet.cids.editors;

import Sirius.server.middleware.types.MetaClass;
import de.cismet.cids.dynamics.CidsBean;
import de.cismet.tools.collections.TypeSafeCollections;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author srichter
 */
public class DefaultBeanInitializer implements BeanInitializer {

    protected final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DefaultBeanInitializer.class);
    protected static final String GEOM_TABLE_NAME = "geom";
    protected static final String GEOM_FIELD_NAME = "geo_field";
    protected final MetaClass metaClass;
    protected final String primaryKeyField;
    protected final Map<String, Object> simpleProperties;
    protected final Map<String, CidsBean> complexProperties;
    protected final Map<String, Collection<CidsBean>> arrayProperties;

    public DefaultBeanInitializer(CidsBean template) {
        metaClass = template.getMetaObject().getMetaClass();
        primaryKeyField = metaClass.getPrimaryKey().toLowerCase();
        simpleProperties = TypeSafeCollections.newHashMap();
        complexProperties = TypeSafeCollections.newHashMap();
        arrayProperties = TypeSafeCollections.newHashMap();
        initPropertyMaps(template);
    }

    protected void initPropertyMaps(CidsBean template) {
        for (String propertyName : template.getPropertyNames()) {
            Object propertyValue = template.getProperty(propertyName);
            if (propertyValue instanceof CidsBean) {
                complexProperties.put(propertyName, (CidsBean) propertyValue);
            } else if (propertyValue instanceof Collection) {
                arrayProperties.put(propertyName, (Collection<CidsBean>) propertyValue);
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
    public void initializeBean(CidsBean beanToInit) throws Exception {
        initializeSimpleProperties(beanToInit);
        initializeComplexProperties(beanToInit);
        initializeArrayProperties(beanToInit);
    }

    protected void initializeSimpleProperties(CidsBean beanToInit) throws Exception {
        for (Entry<String, Object> property : simpleProperties.entrySet()) {
            processSimpleProperty(beanToInit, property.getKey(), property.getValue());
        }
    }

    protected void initializeComplexProperties(CidsBean beanToInit) throws Exception {
        for (Entry<String, CidsBean> complexProperty : complexProperties.entrySet()) {
            processComplexProperty(beanToInit, complexProperty.getKey(), complexProperty.getValue());
        }
    }

    protected void initializeArrayProperties(CidsBean beanToInit) throws Exception {
        for (Entry<String, Collection<CidsBean>> arrayProperty : arrayProperties.entrySet()) {
            processArrayProperty(beanToInit, arrayProperty.getKey(), arrayProperty.getValue());
        }
    }

    protected void processSimpleProperty(CidsBean beanToInit, String propertyName, Object simpleValueToProcess) throws Exception {
        if (!propertyName.equals(primaryKeyField)) {
            beanToInit.setProperty(propertyName, simpleValueToProcess);
        }
    }

    protected void processComplexProperty(CidsBean beanToInit, String propertyName, CidsBean complexValueToProcess) throws Exception {
        //default impl delivers deep copy of geom attributes and ignores all others
        if (complexValueToProcess.getMetaObject().getMetaClass().getTableName().equalsIgnoreCase(GEOM_TABLE_NAME)) {
            CidsBean geomBean = complexValueToProcess.getMetaObject().getMetaClass().getEmptyInstance().getBean();
            geomBean.setProperty(GEOM_FIELD_NAME, complexValueToProcess.getProperty(GEOM_FIELD_NAME));
            beanToInit.setProperty(propertyName, geomBean);
        }
    }

    protected void processArrayProperty(CidsBean beanToInit, String propertyName, Collection<CidsBean> arrayValueToProcess) throws Exception {
        //does nothing but clear in default impl
        Collection<CidsBean> collectionToInitialize = (Collection<CidsBean>) beanToInit.getProperty(propertyName);
        collectionToInitialize.clear();
    }
}
