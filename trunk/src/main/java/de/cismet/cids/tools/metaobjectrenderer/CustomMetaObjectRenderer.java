/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * CustomMetaObjectRenderer.java
 *
 * Created on 24. Mai 2007, 16:42
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package de.cismet.cids.tools.metaobjectrenderer;

import Sirius.server.middleware.types.MetaObject;

import java.lang.reflect.Field;

import java.util.Collection;
import java.util.Vector;

import javax.swing.JComponent;

import de.cismet.cids.annotations.AggregationRenderer;
import de.cismet.cids.annotations.CidsAttribute;
import de.cismet.cids.annotations.CidsAttributeVector;
import de.cismet.cids.annotations.CidsRendererTitle;

import de.cismet.cids.tools.StaticCidsUtilities;

/**
 * DOCUMENT ME!
 *
 * @author   hell
 * @version  $Revision$, $Date$
 */
public abstract class CustomMetaObjectRenderer extends MetaObjectRenderer {

    //~ Static fields/initializers ---------------------------------------------

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(
            CustomMetaObjectRenderer.class);

    //~ Instance fields --------------------------------------------------------

    protected JComponent extraAggregationRendererComponent = null;
    protected JComponent extraRendererComponent = null;

    protected Collection<MetaObject> MetaObjectCollection = null;
    protected MetaObject MetaObject = null;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of CustomMetaObjectRenderer.
     */
    public CustomMetaObjectRenderer() {
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public JComponent getAggregationRenderer(final Collection<MetaObject> cm, final String title) {
        MetaObjectCollection = cm;
        if (this.getClass().isAnnotationPresent(AggregationRenderer.class)) {
            final Class customRenderer = this.getClass();
            final Field[] fields = customRenderer.getDeclaredFields();
            for (final Field f : fields) {
                if (f.isAnnotationPresent(CidsAttributeVector.class)) {
                    try {
                        final CidsAttributeVector cav = f.getAnnotation(CidsAttributeVector.class);
                        final String attributeName = cav.value();
                        final Vector v = new Vector();
                        for (final MetaObject o : cm) {
                            final Object value = StaticCidsUtilities.getValueOfAttributeByString(attributeName, o);
                            v.add(value);
                        }
                        f.set(this, v);
                    } catch (Exception e) {
                        log.error("Error while assigning attributes in the renderer: " + f, e); // NOI18N
                    }
                } else if (f.isAnnotationPresent(CidsRendererTitle.class)) {
                    try {
                        f.set(this, title);
                    } catch (Exception e) {
                        log.warn("Error while assigning the renderer title in the renderer", e); // NOI18N
                    }
                }
            }
            assignAggregation();
            if (extraAggregationRendererComponent != null) {
                return extraAggregationRendererComponent;
            } else {
                return this;
            }
        } else {
            return null;
        }
    }

    @Override
    public JComponent getSingleRenderer(final MetaObject mo, final String title) {
        MetaObject = mo;
        final Class customRenderer = this.getClass();
        final Field[] fields = customRenderer.getDeclaredFields();
        for (final Field f : fields) {
            if (f.isAnnotationPresent(CidsAttribute.class)) {
                try {
                    final CidsAttribute ca = f.getAnnotation(CidsAttribute.class);
                    final String attributeName = ca.value();
                    final Object value = StaticCidsUtilities.getValueOfAttributeByString(attributeName, mo);
                    f.set(this, value);
                } catch (Exception e) {
                    log.warn("Error while assigning attributes in the renderer", e); // NOI18N
                }
            } else if (f.isAnnotationPresent(CidsRendererTitle.class)) {
                try {
                    f.set(this, title);
                } catch (Exception e) {
                    log.warn("Error while assigning the renderer title in the renderer", e); // NOI18N
                }
            }
        }
        assignSingle();
        if (extraRendererComponent != null) {
            return extraRendererComponent;
        } else {
            return this;
        }
    }

    /**
     * DOCUMENT ME!
     */
    public abstract void assignSingle();
    /**
     * DOCUMENT ME!
     */
    public abstract void assignAggregation();
}
