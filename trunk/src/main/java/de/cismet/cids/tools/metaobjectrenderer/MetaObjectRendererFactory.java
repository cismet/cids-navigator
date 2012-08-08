/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
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
 * DOCUMENT ME!
 *
 * @author   hell
 * @version  $Revision$, $Date$
 */
@Deprecated
public class MetaObjectRendererFactory {

    //~ Static fields/initializers ---------------------------------------------

    static MetaObjectRendererFactory instance = null;

    //~ Instance fields --------------------------------------------------------

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of MetaObjectRendererFactory.
     */
    private MetaObjectRendererFactory() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static MetaObjectRendererFactory getInstance() {
        if (instance == null) {
            instance = new MetaObjectRendererFactory();
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
        String renderer = null;
        try {
            renderer = mo.getMetaClass().getRenderer();
        } catch (Throwable e) {
            log.warn("Error while assigning the renderer", e); // NOI18N
        }
        if (renderer != null) {
            Class rendererClass = null;
            try {
                rendererClass = Class.forName(renderer);
                final Constructor constructor = rendererClass.getConstructor();
                final MetaObjectRenderer mor = (MetaObjectRenderer)constructor.newInstance();
                final JComponent comp = mor.getSingleRenderer(mo, title);
                comp.putClientProperty(MetaObjectRenderer.WIDTH_RATIO, mor.getWidthRatio());
                return comp;
            } catch (Throwable ex) {
                log.error("Error in renderer " + renderer, ex); // NOI18N
            }
        }
        try {
            final DefaultMetaObjectRenderer mor = new DefaultMetaObjectRenderer();
            final JComponent comp = mor.getSingleRenderer(mo, title);
            comp.putClientProperty(MetaObjectRenderer.WIDTH_RATIO, mor.getWidthRatio());
            return comp;
        } catch (Throwable t) {
            log.fatal("Error while exception handling ", t);   // NOI18N
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   mc     DOCUMENT ME!
     * @param   title  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public JComponent getAggregationRenderer(final Collection<MetaObject> mc, final String title) {
        if (mc.size() == 1) {
            return getSingleRenderer((MetaObject)mc.toArray()[0], title);
        } else {
            String renderer = null;
            try {
                renderer = ((MetaObject)mc.toArray()[0]).getMetaClass().getRenderer();
            } catch (Throwable e) {
                log.warn("Error while assigning the renderer", e); // NOI18N
            }
            if (renderer != null) {
                Class rendererClass = null;
                try {
                    rendererClass = Class.forName(renderer);
                    final Constructor constructor = rendererClass.getConstructor();
                    final MetaObjectRenderer mor = (MetaObjectRenderer)constructor.newInstance();
                    return mor.getAggregationRenderer(mc, title);
                } catch (Throwable ex) {
                    log.error("Error in Renderer " + renderer, ex); // NOI18N
                    return null;
                }
            } else {
                return null;
            }
        }
    }
}
