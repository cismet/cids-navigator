/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.cids.tools.metaobjectrenderer;

import Sirius.server.middleware.types.MetaClass;
import Sirius.server.middleware.types.MetaObject;
import de.cismet.cids.dynamics.CidsBean;
import de.cismet.cids.editors.CidsObjectEditorFactory;
import de.cismet.tools.gui.ComponentWrapper;
import de.cismet.tools.gui.DoNotWrap;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.JComponent;

/**
 *
 * @author thorsten
 */
public class CidsObjectRendererFactory {

    private static CidsObjectRendererFactory instance = null;
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private HashMap<MetaClass, JComponent> singleRenderer = new HashMap<MetaClass, JComponent>();
//    private HashMap<MetaClass,JComponent> aggregationRenderer=new HashMap<MetaClass, JComponent>();
    private boolean lazyClassFetching = true;
    private String rendererPrefix = "de.cismet.cids.custom.objectrenderer.";
    private String singleRendererPostfix = "Renderer";
    private String aggregationRendererPostfix = "AggregationRenderer";
    private ComponentWrapper cw = null;

    private CidsObjectRendererFactory() {
        cw = CidsObjectEditorFactory.getInstance().getComponentWrapper();
    }

    static public CidsObjectRendererFactory getInstance() {
        if (instance == null) {
            instance = new CidsObjectRendererFactory();
        }
        return instance;
    }

    public JComponent getSingleRenderer(MetaObject mo, String title) {
        String rendererInfo = null;
        JComponent ret = null;
        log.debug("getSingleRenderer");

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


        rendererInfo = mo.getMetaClass().getRenderer();

        //Erstmal ausschließlich lazy class loading
        Class rendererClass = null;

        try {

            //Transform due to JavaCodeConventions
            String className = mo.getMetaClass().getTableName().toLowerCase();
            className = className.substring(0, 1).toUpperCase() + className.substring(1);
            className = rendererPrefix + mo.getDomain().toLowerCase() + "." + className + singleRendererPostfix;

            try {
                rendererClass = Class.forName(className);
            } catch (ClassNotFoundException cnfe) {
                log.debug("Kein Renderer gefunden.", cnfe);
            }

            if (rendererClass == null && rendererInfo != null) {
                rendererClass = Class.forName(rendererInfo);
            }

            if (rendererClass != null) {
                Constructor constructor = rendererClass.getConstructor();
                Object o = constructor.newInstance();
                JComponent rendererComp = null;
                if (o instanceof MetaObjectRenderer) {
                    MetaObjectRenderer mor = (MetaObjectRenderer) o;
                    rendererComp = mor.getSingleRenderer(mo, title);
                } else if (o instanceof CidsBeanRenderer) {
                    CidsBeanRenderer renderer = (CidsBeanRenderer) o;
                    renderer.setTitle(title);
                    renderer.setCidsBean(mo.getBean());
                    rendererComp = (JComponent) renderer;
                } else {
                    throw new RuntimeException("Not a valid Renderer. The Renderer should be a CidsBeanRenderer or a MetaObjectRenderer");
                }

                singleRenderer.put(mo.getMetaClass(), rendererComp);
                if (cw != null && !(rendererComp instanceof DoNotWrap)) {
                    return (JComponent) cw.wrapComponent(rendererComp);
                } else {
                    return rendererComp;
                }

            }
        } catch (ClassNotFoundException e) {
            log.debug("Kein Renderer gefunden.", e);
        } catch (Throwable e) {
            log.error("Fehler beim Erzeugen des Renderers.", e);
        }



        //Im Fehlerfall wird der DefaultRendererGeladen
        try {
            DefaultMetaObjectRenderer mor = new DefaultMetaObjectRenderer();
            JComponent comp = mor.getSingleRenderer(mo, title);
            if (cw != null && !(comp instanceof DoNotWrap)) {
                return (JComponent) cw.wrapComponent(comp);
            } else {
                return comp;
            }
        } catch (Throwable t) {
            log.error("Fehler im Exceptionhandling ", t);
            return null;
        }

    }

    public JComponent getAggregationRenderer(Collection<MetaObject> moCollection, String title) {
        log.fatal("getAggregationRenderer");
        if (moCollection.size() == 1) {
            return getSingleRenderer((MetaObject) moCollection.toArray()[0], title);
        } else {
            String renderer = null;
            MetaClass mc = null;
            try {
                mc = ((MetaObject) moCollection.toArray()[0]).getMetaClass();
                //rendererComp=mc.getRenderer();
                renderer = mc.getTableName().toLowerCase();
                renderer = renderer.substring(0, 1).toUpperCase() + renderer.substring(1);
                renderer = rendererPrefix + mc.getDomain().toLowerCase() + "." + renderer + aggregationRendererPostfix;


            } catch (Throwable e) {
                log.warn("Fehler beim Zuweisen des Renderers", e);
            }
            log.debug("LazyClass:" + renderer);
            if (renderer != null) {
                Class rendererClass = null;
                try {
                    try {
                        rendererClass = Class.forName(renderer);
                    } catch (Exception e) {
                        log.debug("LazyClass " + renderer + " nicht gefunden. Versuche jetzt Datenbank.");
                        rendererClass = Class.forName(mc.getRenderer());
                    }

                    Constructor constructor = rendererClass.getConstructor();
                    Object o = constructor.newInstance();
                    if (o instanceof MetaObjectRenderer) {
                        MetaObjectRenderer mor = (MetaObjectRenderer) o;
                        JComponent comp = mor.getAggregationRenderer(moCollection, title);
                        if (cw != null && !(comp instanceof DoNotWrap)) {
                            return (JComponent) cw.wrapComponent(comp);
                        } else {
                            return comp;
                        }
                    } else if (o instanceof CidsBeanAggregationRenderer) {
                        Vector<CidsBean> beans = new Vector<CidsBean>();
                        for (MetaObject mo : moCollection) {
                            beans.add(mo.getBean());
                        }
                        CidsBeanAggregationRenderer rendererComp = (CidsBeanAggregationRenderer) o;
                        rendererComp.setTitle(title);
                        rendererComp.setCidsBeans(beans);

                        log.debug("Will return " + rendererComp);
                        if (cw != null && !(rendererComp instanceof DoNotWrap)) {
                            return (JComponent) cw.wrapComponent((JComponent) rendererComp);
                        } else {
                            return (JComponent) rendererComp;
                        }

                    }

                } catch (ClassNotFoundException e) {
                    log.debug("Kein Renderer gefunden.", e);
                } catch (Exception e) {
                    log.error("Fehler beim Erzeugen des Renderers.", e);
                }
            }
        }
        return null;
    }
}
