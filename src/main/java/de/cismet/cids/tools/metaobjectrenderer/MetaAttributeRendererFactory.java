/*
 * MetaAttributeRendererFactory.java
 *
 * Created on 24. Mai 2007, 15:14
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package de.cismet.cids.tools.metaobjectrenderer;

import Sirius.server.localserver.attribute.Attribute;
import Sirius.server.middleware.types.MetaObject;
import javax.swing.JComponent;

/**
 *
 * @author hell
 */
public class MetaAttributeRendererFactory {
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    static MetaAttributeRendererFactory instance=null;
    /** Creates a new instance of MetaObjectRendererFactory */
    private MetaAttributeRendererFactory() {
    }
    
    static public MetaAttributeRendererFactory getInstance() {
        if (instance==null) {
            instance=new MetaAttributeRendererFactory();
        }
        return instance;
    }
    public JComponent getRenderer(Attribute attr) {
        return getRenderer(attr,"");//NOI18N
    }
    public JComponent getRenderer(Attribute attr,String title) {
        if (attr!=null) {
            if (false) { //es existiert ein renderer
                return null;
            }
            //es existiert keiner aber substitute==false und es ist ein komplexes Attribut, deshalb wird der ObjectRenderer genommen
            else if (attr.getValue() instanceof MetaObject&&!attr.isSubstitute()){
                return MetaObjectRendererFactory.getInstance().getSingleRenderer((MetaObject)attr.getValue(),title);
            } else {
                return new DefaultMetaAttributeRenderer(attr).getMetaAttributeRenderer();
            }
        } else {
            return null;
        }
    }
}
