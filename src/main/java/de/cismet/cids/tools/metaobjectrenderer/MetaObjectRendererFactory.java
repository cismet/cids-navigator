/*
 * MetaObjectRendererFactory.java
 *
 * Created on 24. Mai 2007, 15:14
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package de.cismet.cids.tools.metaobjectrenderer;

import Sirius.server.middleware.types.MetaObject;
import java.lang.reflect.Constructor;
import java.util.Collection;
import javax.swing.JComponent;

/**
 *
 * @author hell
 */
@Deprecated
public class MetaObjectRendererFactory {
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    static MetaObjectRendererFactory instance=null;

    /** Creates a new instance of MetaObjectRendererFactory */
    private MetaObjectRendererFactory() {
    }
    
    static public MetaObjectRendererFactory getInstance() {
        if (instance==null) {
            instance=new MetaObjectRendererFactory();
        }
        return instance;
    }
    
    public JComponent getSingleRenderer(MetaObject mo,String title) {
        String renderer=null;
        try {
            renderer=mo.getMetaClass().getRenderer();
            
        } catch (Throwable  e) {
            log.warn("Fehler beim Zuweisen des Renderers",e);
        }
        if (renderer!=null) {
            Class rendererClass=null;
            try {
                rendererClass=Class.forName(renderer);
                Constructor constructor=rendererClass.getConstructor();
                MetaObjectRenderer mor=(MetaObjectRenderer)constructor.newInstance();
                JComponent comp=mor.getSingleRenderer(mo,title);
                comp.putClientProperty(MetaObjectRenderer.WIDTH_RATIO,mor.getWidthRatio());
                return comp;
                
            } catch (Throwable ex) {
                log.error("Fehler im Renderer "+renderer,ex);
            }
        }
        try {
            DefaultMetaObjectRenderer mor=new DefaultMetaObjectRenderer();
            JComponent comp=mor.getSingleRenderer(mo,title);
            comp.putClientProperty(MetaObjectRenderer.WIDTH_RATIO,mor.getWidthRatio());
            return comp;
        } catch (Throwable t) {
            log.fatal("Fehler im Exceptionhandling ",t);
            return null;
        }
        
    }
    
    public JComponent getAggregationRenderer(Collection<MetaObject> mc,String title) {
        if (mc.size()==1) {
            return getSingleRenderer((MetaObject)mc.toArray()[0],title);
        } else {
            String renderer=null;
            try {
                renderer=((MetaObject)mc.toArray()[0]).getMetaClass().getRenderer();
                
            } catch (Throwable e) {
                log.warn("Fehler beim Zuweisen des Renderers",e);
            }
            if (renderer!=null) {
                Class rendererClass=null;
                try {
                    rendererClass=Class.forName(renderer);
                    Constructor constructor=rendererClass.getConstructor();
                    MetaObjectRenderer mor=(MetaObjectRenderer)constructor.newInstance();
                    return mor.getAggregationRenderer(mc,title);
                    
                } catch (Throwable ex) {
                    log.error("Fehler im Renderer "+renderer,ex);
                    return null;
                }
                
            } else {
                return null;
            }
        }
    }
}
