/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.cids.tools.metaobjectrenderer;

import Sirius.server.middleware.types.MetaClass;
import Sirius.server.middleware.types.MetaObject;
import de.cismet.cids.dynamics.CidsBean;
import de.cismet.cids.editors.CidsObjectEditorFactory;
import de.cismet.cids.utils.ClassloadingHelper;
import de.cismet.tools.collections.TypeSafeCollections;
import de.cismet.tools.gui.ComponentWrapper;
import de.cismet.tools.gui.DoNotWrap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.JComponent;

/**
 *
 * @author thorsten
 * @author stefan
 */
public class CidsObjectRendererFactory {

    private static CidsObjectRendererFactory instance = null;
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
//    private static final Map<MetaClass, JComponent> singleRenderer = TypeSafeCollections.newHashMap();
//    private HashMap<MetaClass,JComponent> aggregationRenderer=new HashMap<MetaClass, JComponent>();
//    private static final boolean lazyClassFetching = true;
//    private static final String RENDERER_PREFIX = "de.cismet.cids.custom.objectrenderer.";
//    private static final String SINGLE_RENDERER_SUFFIX = "Renderer";
//    private static final String AGGREGATION_RENDERER_SUFFIX = "AggregationRenderer";
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

    public JComponent getSingleRenderer(final MetaObject mo, final String title) {
        log.debug("getSingleRenderer");//NOI18N
//        final boolean isEDT = EventQueue.isDispatchThread();
        JComponent result = null;

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

        JComponent componentReferenceHolder = null;
        try {
            final Class<?> rendererClass = ClassloadingHelper.getDynamicClass(mo.getMetaClass(), ClassloadingHelper.CLASS_TYPE.RENDERER);
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
                        throw new RuntimeException("Not a valid Renderer. The Renderer should be a CidsBeanRenderer or a MetaObjectRenderer");//NOI18N
                    }

//                    singleRenderer.put(mo.getMetaClass(), rendererComp);
                    if (cw != null && !(rendererComp instanceof DoNotWrap)) {
                        componentReferenceHolder = (JComponent) cw.wrapComponent(rendererComp);
                    } else {
                        componentReferenceHolder = rendererComp;
                    }
                } catch (Throwable t) {
                    throw new RuntimeException(t);
                }
            }
        } catch (Throwable e) {
            log.error("Error during creating the renderer.", e);//NOI18N
        }

        result = componentReferenceHolder;

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
                    componentReferenceHolder = (JComponent) cw.wrapComponent(comp);
                } else {
                    componentReferenceHolder = comp;
                }
//                    }
//                };
//                if (isEDT) {
//                    defaultRendererCreator.run();
//                } else {
//                    EventQueue.invokeAndWait(defaultRendererCreator);
//                }

            } catch (Throwable t) {
                log.error("Error while Exception handling ", t);//NOI18N
            }
            result = componentReferenceHolder;
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
            JComponent resultReferenceHolder = null;
            try {
                final Class<?> rendererClass = ClassloadingHelper.getDynamicClass(mc, ClassloadingHelper.CLASS_TYPE.AGGREGATION_RENDERER);
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
                            if (log.isDebugEnabled()) {
                                log.debug("Will return " + rendererComp);//NOI18N
                            }
                            if (cw != null && !(rendererComp instanceof DoNotWrap)) {
                                resultReferenceHolder = (JComponent) cw.wrapComponent((JComponent) rendererComp);
                            } else {
                                resultReferenceHolder = (JComponent) rendererComp;
                            }

                        } else if (rendererInstanceObject instanceof MetaObjectRenderer) {
                            final MetaObjectRenderer mor = (MetaObjectRenderer) rendererInstanceObject;
                            final JComponent comp = mor.getAggregationRenderer(moCollection, title);
                            if (cw != null && !(comp instanceof DoNotWrap)) {
                                resultReferenceHolder = (JComponent) cw.wrapComponent(comp);
                            } else {
                                resultReferenceHolder = comp;
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
                log.error("Error while creating the renderer.", e);//NOI18N
            }
            return resultReferenceHolder;
        }
    }
}
