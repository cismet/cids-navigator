/*
 * SimpleFromStringMetaAttributeEditor.java
 *
 * Created on 3. Dezember 2004, 13:22
 */

package Sirius.navigator.ui.attributes.editor.metaobject;

import Sirius.navigator.resource.ResourceManager;
import Sirius.server.localserver.attribute.Attribute;
import java.awt.image.RescaleOp;
import org.apache.log4j.lf5.util.Resource;

/**
 *
 * @author  pascal
 */
public class SimpleFromStringMetaAttributeEditor extends SimpleStringMetaAttributeEditor
{
    
    /** Creates a new instance of SimpleFromStringMetaAttributeEditor */
    public SimpleFromStringMetaAttributeEditor()
    {
        super();
    }
    
    protected void initUI()
    {
        super.initUI();
        this.simpleValueField.setEnabled(this.isStringCreateable((Attribute)this.getValue()));
    }
    
    protected ValueChangeListener getValueChangeListener()
    {
        return new SimpleFromStringValueChangeListener();
    }
    
    /**
     * Der Wert wurde schon im ValueChanged Listener ver\u00E4ndert
     */
    protected Object getComponentValue()
    {
        return this.getValue();
    }
    
    protected class SimpleFromStringValueChangeListener extends SimpleStringMetaAttributeEditor.DefaultSimpleValueChangeListener
    {    
        protected void actionPerformed()
        {
            SimpleFromStringMetaAttributeEditor.this.setValueChanged(SimpleFromStringMetaAttributeEditor.this.isValueChanged() | this.isChanged());
            if(SimpleFromStringMetaAttributeEditor.this.isValueChanged())
            {
                try
                {
                    Sirius.server.localserver.attribute.Attribute attribute = (Sirius.server.localserver.attribute.Attribute)getValue();
                    if(SimpleFromStringMetaAttributeEditor.this.isStringCreateable(attribute))
                    {
                        SimpleFromStringMetaAttributeEditor.this.setValueFromString(attribute, this.getNewValue().toString());
                        if(logger.isDebugEnabled())logger.debug("actionPerformed(" + SimpleFromStringMetaAttributeEditor.this.getId() + "): speichere neue Eingabe");
                        SimpleFromStringMetaAttributeEditor.this.stopEditing();
                    }
                    else
                    {
                        logger.error("actionPerformed(" + SimpleFromStringMetaAttributeEditor.this.getId() + "): value is not from String createable");
                    }
                    
                }
                catch(Throwable t)
                {
                    logger.error("actionPerformed(" + SimpleFromStringMetaAttributeEditor.this.getId() + "): from String creation " + this.getNewValue() + " failed", t);
                    
                    // XXX i18n
                    javax.swing.JOptionPane.showMessageDialog(SimpleFromStringMetaAttributeEditor.this,
                            ResourceManager.getManager().getString("Sirius.navigator.ui.attributes.editor.metaobject.SimpleFromStringMetaAttributeEditor.actionPerformed.ErrorMessage1") + t.getMessage() + 
                            ResourceManager.getManager().getString("Sirius.navigator.ui.attributes.editor.metaobject.SimpleFromStringMetaAttributeEditor.actionPerformed.ErrorMessage2"),
                            ResourceManager.getManager().getString("Sirius.navigator.ui.attributes.editor.metaobject.SimpleFromStringMetaAttributeEditor.actionPerformed.ErrorTitle"), javax.swing.JOptionPane.ERROR_MESSAGE);
                    
                    // reset
                    setComponentValue(getValue());
                }
            }
        }
    } 
}
