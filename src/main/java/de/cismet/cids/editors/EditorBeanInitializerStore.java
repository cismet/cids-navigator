package de.cismet.cids.editors;

import Sirius.server.middleware.types.MetaClass;
import de.cismet.cids.dynamics.CidsBean;
import de.cismet.tools.collections.TypeSafeCollections;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 *
 * @author srichter
 */
public class EditorBeanInitializerStore {

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(EditorBeanInitializerStore.class);
    private static final EditorBeanInitializerStore INSTANCE = new EditorBeanInitializerStore();

    public static EditorBeanInitializerStore getInstance() {
        return INSTANCE;
    }

    private EditorBeanInitializerStore() {
        initializerStore = TypeSafeCollections.newHashMap();
    }
    private final Map<MetaClass, BeanInitializer> initializerStore;

    public void registerInitializer(MetaClass metaClass, BeanInitializer initializer) {
        initializerStore.put(metaClass, initializer);
    }

    public boolean unregisterInitializer(MetaClass metaClass) {
        return initializerStore.remove(metaClass) != null;
    }

    public BeanInitializer getInitializer(MetaClass metaClass) {
        return initializerStore.get(metaClass);
    }

    public Collection<MetaClass> getAllRegisteredClasses() {
        return Collections.unmodifiableCollection(initializerStore.keySet());
    }

    public void initialize(CidsBean toInitialize) throws Exception {
        if (toInitialize != null) {
            BeanInitializer initializer = initializerStore.get(toInitialize.getMetaObject().getMetaClass());
            if (initializer != null) {
                initializer.initializeBean(toInitialize);
            }
        }
        throw new IllegalArgumentException("Bean to initialize was null!");
    }
}
