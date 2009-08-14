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
 *
 * @author hell
 */
public abstract class MetaObjectRenderer extends JPanel{
    public static final String WIDTH_RATIO="WIDTH_RATIO";
    /** Creates a new instance of MetaObjectRenderer */
    public MetaObjectRenderer() {
    }
    public abstract JComponent getSingleRenderer(MetaObject mo,String title);
    public abstract JComponent getAggregationRenderer(Collection<MetaObject> cm,String title);
    public abstract double getWidthRatio();
    
    //TODO
    //es wird noch ein besserer Platz gesucht
    public static Vector<MetaObject> getAllMetaObjectsOfAnArray(Attribute arrayLink) {
        //Sammeln der richtigen MetaObjects in einem Vector<MetaObject>
        Vector<MetaObject> arrayObjects=new Vector<MetaObject>();
        MetaObject artificialObject=(MetaObject)arrayLink.getValue();
        HashMap artificialAttributes=artificialObject.getAttributes();
        Iterator artificialKeySetIterator=artificialAttributes.keySet().iterator();
        while(artificialKeySetIterator.hasNext()) {
            Attribute a=(Attribute)artificialAttributes.get(artificialKeySetIterator.next());
            MetaObject referenceMetaObject=(MetaObject)a.getValue();
            //Es gibt nur ein Attribut in diesem Objekt das ein MetaObject ist
            Attribute aa=(Attribute)referenceMetaObject.getAttributesByType(MetaObject.class).toArray()[0];
            arrayObjects.add((MetaObject)aa.getValue());
        }
        return arrayObjects;
    }
}
