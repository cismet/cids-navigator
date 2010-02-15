/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.cids.tools.metaobjectrenderer;

import Sirius.server.middleware.types.MetaClass;
import Sirius.server.middleware.types.MetaObject;
import de.cismet.cids.dynamics.CidsBean;
import de.cismet.cids.editors.CidsObjectEditorFactory;
import de.cismet.cids.utils.FinalReference;
import de.cismet.tools.BlacklistClassloading;
import de.cismet.tools.collections.TypeSafeCollections;
import de.cismet.tools.gui.ComponentWrapper;
import de.cismet.tools.gui.DoNotWrap;
import java.util.Collection;
import java.util.Map;
import javax.swing.JComponent;

/**
 *
 * @author thorsten
 * @author stefan
 */
public class CidsObjectRendererFactory {

    private static CidsObjectRendererFactory instance = null;
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private static final Map<MetaClass, JComponent> singleRenderer = TypeSafeCollections.newHashMap();
//    private HashMap<MetaClass,JComponent> aggregationRenderer=new HashMap<MetaClass, JComponent>();
//    private static final boolean lazyClassFetching = true;
    private static final String RENDERER_PREFIX = "de.cismet.cids.custom.objectrenderer.";
    private static final String SINGLE_RENDERER_SUFFIX = "Renderer";
    private static final String AGGREGATION_RENDERER_SUFFIX = "AggregationRenderer";
    private final ComponentWrapper cw;

    private CidsObjectRendererFactory() {
        cw = CidsObjectEditorFactory.getInstance().getComponentWrapper();
    }

    static public CidsObjectRendererFactory getInstance() {
        if (instance == null) {
            instance = new CidsObjectRendererFactory();
        }
        return instance;
    }

    private final String getSingleRendererClassnameByConvention(MetaObject mo) {
        return getRendererClassnameByConvention(mo, RENDERER_PREFIX, SINGLE_RENDERER_SUFFIX);
    }

    private final String getAggregationRendererClassnameByConvention(MetaObject mo) {
        return getRendererClassnameByConvention(mo, RENDERER_PREFIX, AGGREGATION_RENDERER_SUFFIX);
    }

    private final String getRendererClassnameByConvention(MetaObject mo, String Prefix, String Suffix) {
        //Transform due to JavaCodeConventions
        String className = mo.getMetaClass().getTableName().toLowerCase();
        className = className.substring(0, 1).toUpperCase() + className.substring(1);
        className = Prefix + mo.getDomain().toLowerCase() + "." + className + Suffix;
        return className;
    }

    private final Class<?> loadRendererClass(String conventionClassName, String databaseClassName) {
        Class<?> rendererClass = null;
        if (conventionClassName != null) {
            rendererClass = BlacklistClassloading.forName(conventionClassName);

        }
        if (rendererClass == null && databaseClassName != null) {
            rendererClass = BlacklistClassloading.forName(databaseClassName); //Klasse laden die in der DB zugeordnet ist
        }
        return rendererClass;
    }

    public JComponent getSingleRenderer(final MetaObject mo, final String title) {
        log.debug("getSingleRenderer");
//        final boolean isEDT = EventQueue.isDispatchThread();
        JComponent result = null;
        final String overrideRendererClassName = System.getProperty(mo.getDomain().toLowerCase() + "." + mo.getMetaClass().getTableName().toLowerCase() + ".renderer");
        final String rendererClassNameByConvention = overrideRendererClassName == null ? getSingleRendererClassnameByConvention(mo) : overrideRendererClassName;
        final String rendererClassNameFromDB = mo.getMetaClass().getRenderer();
//        //Caching bleibt erstmal aus, da sonst nicht mehrere Editoren gleichzeitigt erzeugt werden
//         ret=singleRenderer.get(mo.getMetaClass());
//        if (ret!=null){
//            if (ret instanceof CidsBeanRenderer){
//                ((CidsBeanRenderer)ret).setCidsBean(mo.getBean());
//                return ret;
//            }
//        }

//        Eine Moeglickeit wäre eine bestimmte Anzahl von Renderern zur Verfügung zustellen
//        anstelle iner hashmap eine multimap und dann über addnotify die instanz des renderers aus dem cahe entferne und bei removenotify (wenn der renderer
        //nicht mehr bebraucht wird) wieder dem cache zur verfuegung zu stellen. problem ist, das es noch keine gemeinsame oberklasse für die renderer gibt

        final FinalReference<JComponent> componentReferenceHolder = new FinalReference<JComponent>();
        try {
            final Class<?> rendererClass = loadRendererClass(rendererClassNameByConvention, rendererClassNameFromDB);
            final CidsBean bean;
            if (rendererClass != null) {
                if (CidsBeanRenderer.class.isAssignableFrom(rendererClass)) {
                    bean = mo.getBean();
                } else {
                    bean = null;
                }
                try {
                    final Object o = rendererClass.newInstance();
                    JComponent rendererComp = null;
                    if (bean != null) {
                        final CidsBeanRenderer renderer = (CidsBeanRenderer) o;
                        renderer.setTitle(title);
                        renderer.setCidsBean(bean);
                        rendererComp = (JComponent) renderer;
                    } else if (o instanceof MetaObjectRenderer) {
                        final MetaObjectRenderer mor = (MetaObjectRenderer) o;
                        rendererComp = mor.getSingleRenderer(mo, title);
                    } else {
                        throw new RuntimeException("Not a valid Renderer. The Renderer should be a CidsBeanRenderer or a MetaObjectRenderer");
                    }

                    singleRenderer.put(mo.getMetaClass(), rendererComp);
                    if (cw != null && !(rendererComp instanceof DoNotWrap)) {
                        componentReferenceHolder.setObject((JComponent) cw.wrapComponent(rendererComp));
                    } else {
                        componentReferenceHolder.setObject(rendererComp);
                    }
                } catch (Throwable t) {
                    throw new RuntimeException(t);
                }
            }
        } catch (Throwable e) {
            log.error("Fehler beim Erzeugen des Renderers.", e);
        }

        result = componentReferenceHolder.getObject();

        if (result == null) {
            //Im Fehlerfall wird der DefaultRendererGeladen
            try {
//                final Runnable defaultRendererCreator = new Runnable() {
//
//                    @Override
//                    public void run() {
                final DefaultMetaObjectRenderer mor = new DefaultMetaObjectRenderer();
                final JComponent comp = mor.getSingleRenderer(mo, title);
                if (cw != null && !(comp instanceof DoNotWrap)) {
                    componentReferenceHolder.setObject((JComponent) cw.wrapComponent(comp));
                } else {
                    componentReferenceHolder.setObject(comp);
                }
//                    }
//                };
//                if (isEDT) {
//                    defaultRendererCreator.run();
//                } else {
//                    EventQueue.invokeAndWait(defaultRendererCreator);
//                }

            } catch (Throwable t) {
                log.error("Fehler im Exceptionhandling ", t);
            }
            result = componentReferenceHolder.getObject();
        }

        return result;
    }

    public JComponent getAggregationRenderer(final Collection<MetaObject> moCollection, final String title) {
        if (moCollection.size() == 1) {

            return getSingleRenderer(moCollection.iterator().next(), title);
        } else {
//            final boolean isEDT = EventQueue.isDispatchThread();
            final MetaObject mo = moCollection.iterator().next();
            final MetaClass mc = mo.getMetaClass();
            final String overrideRendererClassName = System.getProperty(mc.getDomain().toLowerCase() + "." + mc.getTableName().toLowerCase() + ".aggregationrenderer");
            final String rendererClassNameByConvention = overrideRendererClassName == null ? getAggregationRendererClassnameByConvention(mo) : overrideRendererClassName;
            final String rendererClassNameFromDB = mc.getRenderer();
            final FinalReference<JComponent> resultReferenceHolder = new FinalReference<JComponent>();
            log.debug("LazyClass:" + rendererClassNameByConvention);
            try {
                final Class<?> rendererClass = loadRendererClass(rendererClassNameByConvention, rendererClassNameFromDB);
                if (rendererClass != null) {
                    final Collection<CidsBean> beans;
                    if (CidsBeanAggregationRenderer.class.isAssignableFrom(rendererClass)) {
                        beans = TypeSafeCollections.newArrayList(moCollection.size());
                        for (final MetaObject currentMO : moCollection) {
                            beans.add(currentMO.getBean());
                        }
                    } else {
                        beans = null;
                    }
//                final Runnable aggregationRendererCreator = new Runnable() {
//
//                    @Override
//                    public void run() {
                    try {
                        final Object rendererInstanceObject = rendererClass.newInstance();
                        if (beans != null) {
                            final CidsBeanAggregationRenderer rendererComp = (CidsBeanAggregationRenderer) rendererInstanceObject;
                            rendererComp.setTitle(title);
                            rendererComp.setCidsBeans(beans);
                            log.debug("Will return " + rendererComp);
                            if (cw != null && !(rendererComp instanceof DoNotWrap)) {
                                resultReferenceHolder.setObject((JComponent) cw.wrapComponent((JComponent) rendererComp));
                            } else {
                                resultReferenceHolder.setObject((JComponent) rendererComp);
                            }

                        } else if (rendererInstanceObject instanceof MetaObjectRenderer) {
                            final MetaObjectRenderer mor = (MetaObjectRenderer) rendererInstanceObject;
                            final JComponent comp = mor.getAggregationRenderer(moCollection, title);
                            if (cw != null && !(comp instanceof DoNotWrap)) {
                                resultReferenceHolder.setObject((JComponent) cw.wrapComponent(comp));
                            } else {
                                resultReferenceHolder.setObject(comp);
                            }
                        }
                    } catch (Throwable t) {
                        throw new RuntimeException(t);
                    }
//                    }
//                };
//                if (isEDT) {
//                    aggregationRendererCreator.run();
//                } else {
//                    EventQueue.invokeAndWait(aggregationRendererCreator);
//                }
                }
            } catch (Exception e) {
                log.error("Fehler beim Erzeugen des Renderers.", e);
            }
            return resultReferenceHolder.getObject();
        }
    }
}
