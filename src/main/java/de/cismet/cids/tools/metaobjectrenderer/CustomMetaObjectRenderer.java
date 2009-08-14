
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
import de.cismet.cids.annotations.AggregationRenderer;
import de.cismet.cids.annotations.CidsAttribute;
import de.cismet.cids.annotations.CidsAttributeVector;
import de.cismet.cids.annotations.CidsRendererTitle;
import de.cismet.cids.tools.StaticCidsUtilities;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Vector;
import javax.swing.JComponent;

/**
 *
 * @author hell
 */
public abstract class CustomMetaObjectRenderer extends MetaObjectRenderer{
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CustomMetaObjectRenderer.class);
    protected JComponent extraAggregationRendererComponent=null;
    protected JComponent extraRendererComponent=null;

    protected Collection<MetaObject> MetaObjectCollection=null;
    protected MetaObject MetaObject=null;

    /** Creates a new instance of CustomMetaObjectRenderer */
    public CustomMetaObjectRenderer() {
    }



    public JComponent getAggregationRenderer(Collection<MetaObject> cm, String title) {
        MetaObjectCollection=cm;
        if (this.getClass().isAnnotationPresent(AggregationRenderer.class)) {
            Class customRenderer=this.getClass();
            Field[] fields=customRenderer.getDeclaredFields();
            for (Field f:fields) {
                if (f.isAnnotationPresent(CidsAttributeVector.class)) {
                    try {
                        CidsAttributeVector cav = f.getAnnotation(CidsAttributeVector.class);
                        String attributeName=cav.value();
                        Vector v=new Vector();
                        for (MetaObject o:cm) {
                            Object value=StaticCidsUtilities.getValueOfAttributeByString(attributeName,o);
                            v.add(value);
                        }
                        f.set(this,v);
                    } catch (Exception e) {
                        log.error("Fehler beim Zuweisen im Renderer: "+f,e);
                    }
                } else if (f.isAnnotationPresent(CidsRendererTitle.class)){
                    try {
                        f.set(this,title);
                    } catch (Exception e) {
                        log.warn("Fehler beim Zuweisen von RendererTitle im Renderer",e);
                    }
                }
            }
            assignAggregation();
            if (extraAggregationRendererComponent!=null) {
                return extraAggregationRendererComponent;
            } else {
                return this;
            }
        } else {
            return null;
        }
    }
    
    
 
    
    
    public JComponent getSingleRenderer(MetaObject mo, String title) {
        MetaObject=mo;
        Class customRenderer=this.getClass();
        Field[] fields=customRenderer.getDeclaredFields();
        for (Field f:fields) {
            if (f.isAnnotationPresent(CidsAttribute.class)) {
                try {
                    CidsAttribute ca = f.getAnnotation(CidsAttribute.class);
                    String attributeName=ca.value();
                    Object value=StaticCidsUtilities.getValueOfAttributeByString(attributeName,mo);
                    f.set(this,value);
                } catch (Exception e) {
                    log.warn("Fehler beim Zuweisen im Renderer",e);
                }
            } else if (f.isAnnotationPresent(CidsRendererTitle.class)){
                try {
                    f.set(this,title);
                } catch (Exception e) {
                    log.warn("Fehler beim Zuweisen von RendererTitle im Renderer",e);
                }
            }
        }
        assignSingle();
        if (extraRendererComponent!=null) {
                return extraRendererComponent;
            } else {
                return this;
            }
        
    }
    
    public abstract void assignSingle();
    public abstract void assignAggregation();
    
    
    
    
    
}
