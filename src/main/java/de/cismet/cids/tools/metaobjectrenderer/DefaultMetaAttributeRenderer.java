/*
 * DefaultMetaAttributeRenderer.java
 *
 * Created on 9. Mai 2007, 16:01
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package de.cismet.cids.tools.metaobjectrenderer;

import Sirius.server.localserver.attribute.Attribute;
import java.awt.Color;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;

/**
 *
 * @author hell 
 */
public class DefaultMetaAttributeRenderer{
   private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    Attribute attr; //
    public final static Color FOREGROUND_COLOR=new TitledBorder("X").getTitleColor();
    /**
     * Creates a new instance of DefaultMetaAttributeRenderer
     */
    public DefaultMetaAttributeRenderer(Attribute attr) {
       this.attr=attr;
    }

    public JComponent getMetaAttributeRenderer() {
        if (false)  {
            return null;
        }
        else {
            JLabel ret=new JLabel();
            String value="";
            if (attr!=null&&attr.getValue()!=null){
                try {
                    ret.setText(attr.toString());    
                }
                catch(Throwable e){
                    log.fatal("Fehler im MetaAttributeRenderer",e);
                }
            }
            
            ret.setForeground(FOREGROUND_COLOR);
            return ret;
        }
    }
    
}
