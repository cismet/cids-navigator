/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * MetaObjectRenderer.java
 *
 * Created on 24. Mai 2007, 15:22
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package de.cismet.cids.tools.metaobjectrenderer;

import Sirius.server.middleware.types.MetaObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.management.Attribute;

import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * DOCUMENT ME!
 *
 * @author   hell
 * @version  $Revision$, $Date$
 */
public abstract class MetaObjectRenderer extends JPanel {

    //~ Static fields/initializers ---------------------------------------------

    public static final String WIDTH_RATIO = "WIDTH_RATIO"; // NOI18N

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of MetaObjectRenderer.
     */
    public MetaObjectRenderer() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   mo     DOCUMENT ME!
     * @param   title  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public abstract JComponent getSingleRenderer(MetaObject mo, String title);
    /**
     * DOCUMENT ME!
     *
     * @param   cm     DOCUMENT ME!
     * @param   title  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public abstract JComponent getAggregationRenderer(Collection<MetaObject> cm, String title);
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public abstract double getWidthRatio();
    /**
     * TODO es wird noch ein besserer Platz gesucht.
     *
     * @param   arrayLink  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static Vector<MetaObject> getAllMetaObjectsOfAnArray(final Attribute arrayLink) {
        // Sammeln der richtigen MetaObjects in einem Vector<MetaObject>
        final Vector<MetaObject> arrayObjects = new Vector<MetaObject>();
        final MetaObject artificialObject = (MetaObject)arrayLink.getValue();
        final HashMap artificialAttributes = artificialObject.getAttributes();
        final Iterator artificialKeySetIterator = artificialAttributes.keySet().iterator();
        while (artificialKeySetIterator.hasNext()) {
            final Attribute a = (Attribute)artificialAttributes.get(artificialKeySetIterator.next());
            final MetaObject referenceMetaObject = (MetaObject)a.getValue();
            // Es gibt nur ein Attribut in diesem Objekt das ein MetaObject ist
            final Attribute aa = (Attribute)referenceMetaObject.getAttributesByType(MetaObject.class).toArray()[0];
            arrayObjects.add((MetaObject)aa.getValue());
        }
        return arrayObjects;
    }
}
