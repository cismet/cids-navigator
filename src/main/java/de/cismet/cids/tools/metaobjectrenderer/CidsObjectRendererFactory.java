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
package de.cismet.cids.tools.metaobjectrenderer;

import Sirius.server.middleware.types.MetaClass;
import Sirius.server.middleware.types.MetaObject;

import java.util.Collection;

import javax.swing.JComponent;

import de.cismet.cids.dynamics.CidsBean;

import de.cismet.cids.editors.CidsObjectEditorFactory;

import de.cismet.cids.server.connectioncontext.RendererConnectionContext;

import de.cismet.cids.utils.ClassloadingHelper;

import de.cismet.connectioncontext.AbstractConnectionContext.Category;

import de.cismet.connectioncontext.ConnectionContext;
import de.cismet.connectioncontext.ConnectionContextStore;

import de.cismet.tools.collections.TypeSafeCollections;

import de.cismet.tools.gui.ComponentWrapper;
import de.cismet.tools.gui.DoNotWrap;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @author   stefan
 * @version  $Revision$, $Date$
 */
public class CidsObjectRendererFactory {

    //~ Static fields/initializers ---------------------------------------------

    private static CidsObjectRendererFactory instance = null;

    //~ Instance fields --------------------------------------------------------

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
//    private static final Map<MetaClass, JComponent> singleRenderer = TypeSafeCollections.newHashMap();
//    private HashMap<MetaClass,JComponent> aggregationRenderer=new HashMap<MetaClass, JComponent>();
//    private static final boolean lazyClassFetching = true;
//    private static final String RENDERER_PREFIX = "de.cismet.cids.custom.objectrenderer.";
//    private static final String SINGLE_RENDERER_SUFFIX = "Renderer";
//    private static final String AGGREGATION_RENDERER_SUFFIX = "AggregationRenderer";
    private final ComponentWrapper cw;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new CidsObjectRendererFactory object.
     */
    private CidsObjectRendererFactory() {
        cw = CidsObjectEditorFactory.getInstance().getComponentWrapper();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static CidsObjectRendererFactory getInstance() {
        if (instance == null) {
            instance = new CidsObjectRendererFactory();
        }
        return instance;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   mo     DOCUMENT ME!
     * @param   title  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public JComponent getSingleRenderer(final MetaObject mo, final String title) {
        if (log.isDebugEnabled()) {
            log.debug("getSingleRenderer"); // NOI18N
        }
//        final boolean isEDT = EventQueue.isDispatchThread();
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
        // nicht mehr bebraucht wird) wieder dem cache zur verfuegung zu stellen. problem ist, das es noch keine
        // gemeinsame oberklasse für die renderer gibt

        // insert Ooops here

        final JComponent componentReferenceHolder;
        JComponent rendererComp = null;
        try {
            final MetaClass mc = mo.getMetaClass();
            final Class<?> rendererClass = ClassloadingHelper.getDynamicClass(
                    mc,
                    ClassloadingHelper.CLASS_TYPE.RENDERER);
            final CidsBean bean;
            if (rendererClass != null) {
                final Object o = rendererClass.newInstance();
                if (CidsBeanRenderer.class.isAssignableFrom(rendererClass)) {
                    bean = mo.getBean();
                } else {
                    bean = null;
                }
                if (bean != null) {
                    final CidsBeanRenderer renderer = (CidsBeanRenderer)o;
                    if (renderer instanceof ConnectionContextStore) {
                        final ConnectionContext rendererConnectionContext = ConnectionContext.create(
                            Category.RENDERER,
                            getClass().getSimpleName());
                        ((ConnectionContextStore)renderer).initWithConnectionContext(rendererConnectionContext);
                    }
                    renderer.setTitle(title);
                    renderer.setCidsBean(bean);
                    rendererComp = (JComponent)renderer;
                } else if (o instanceof MetaObjectRenderer) {
                    final MetaObjectRenderer mor = (MetaObjectRenderer)o;
                    rendererComp = mor.getSingleRenderer(mo, title);
                } else {
                    throw new RuntimeException(
                        "Not a valid Renderer. The Renderer should be a CidsBeanRenderer or a MetaObjectRenderer"); // NOI18N
                }
            }

            if (rendererComp == null) {
                rendererComp = new DefaultMetaObjectRenderer().getSingleRenderer(mo, title);
            }
        } catch (Throwable e) {
            log.error("Error during creating the renderer.", e); // NOI18N
            return new ErrorRenderer(e, mo, title);
        }
        if ((cw != null) && !(rendererComp instanceof DoNotWrap)) {
            componentReferenceHolder = (JComponent)cw.wrapComponent(rendererComp);
        } else {
            componentReferenceHolder = rendererComp;
        }

        return componentReferenceHolder;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   rendererClass  DOCUMENT ME!
     * @param   moCollection   DOCUMENT ME!
     * @param   title          DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  RuntimeException  DOCUMENT ME!
     */
    public JComponent getAggregationRenderer(final Class<?> rendererClass,
            final Collection<MetaObject> moCollection,
            final String title) {
        final Collection<CidsBean> beans;
        if (CidsBeanAggregationRenderer.class.isAssignableFrom(rendererClass)) {
            beans = TypeSafeCollections.newArrayList(moCollection.size());
            for (final MetaObject currentMO : moCollection) {
                beans.add(currentMO.getBean());
            }
        } else {
            beans = null;
        }
        try {
            final Object rendererInstanceObject = rendererClass.newInstance();
            if (beans != null) {
                final CidsBeanAggregationRenderer rendererComp = (CidsBeanAggregationRenderer)rendererInstanceObject;
                if (rendererComp instanceof ConnectionContextStore) {
                    final ConnectionContext rendererConnectionContext = new RendererConnectionContext(
                            rendererClass,
                            moCollection);
                    ((ConnectionContextStore)rendererComp).initWithConnectionContext(rendererConnectionContext);
                }
                rendererComp.setTitle(title);
                rendererComp.setCidsBeans(beans);
                if (log.isDebugEnabled()) {
                    log.debug("Will return " + rendererComp); // NOI18N
                }
                if ((cw != null) && !(rendererComp instanceof DoNotWrap)) {
                    return (JComponent)cw.wrapComponent((JComponent)rendererComp);
                } else {
                    return (JComponent)rendererComp;
                }
            } else if (rendererInstanceObject instanceof MetaObjectRenderer) {
                final MetaObjectRenderer mor = (MetaObjectRenderer)rendererInstanceObject;
                final JComponent comp = mor.getAggregationRenderer(moCollection, title);
                if ((cw != null) && !(comp instanceof DoNotWrap)) {
                    return (JComponent)cw.wrapComponent(comp);
                } else {
                    return comp;
                }
            }
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   moCollection  DOCUMENT ME!
     * @param   title         DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public JComponent getAggregationRenderer(final Collection<MetaObject> moCollection, final String title) {
        if (moCollection.size() == 1) {
            return getSingleRenderer(moCollection.iterator().next(), title);
        } else {
            final MetaObject mo = moCollection.iterator().next();
            final MetaClass mc = mo.getMetaClass();
            JComponent resultReferenceHolder = null;
            try {
                final Class<?> rendererClass = ClassloadingHelper.getDynamicClass(
                        mc,
                        ClassloadingHelper.CLASS_TYPE.AGGREGATION_RENDERER);
                if (rendererClass != null) {
                    resultReferenceHolder = getAggregationRenderer(rendererClass, moCollection, title);
                }
            } catch (Exception e) {
                log.error("Error while creating the renderer.", e); // NOI18N
            }
            return resultReferenceHolder;
        }
    }
}
