/*
 * SimpleStringMetaAttributeEditor.java
 *
 * Created on 29. August 2004, 11:50
 */

package Sirius.navigator.ui.attributes.editor.metaobject;

/**
 * Ein Editor f\u00FCr String Attribute.
 *
 * @author  Pascal
 */
public class SimpleStringMetaAttributeEditor extends DefaultSimpleMetaAttributeEditor
{
    /** Creates a new instance of SimpleStringMetaAttributeEditor */
    public SimpleStringMetaAttributeEditor()
    {
        super();
        
        this.logger = org.apache.log4j.Logger.getLogger(this.getClass());
        this.readOnly = false;
    }
}
