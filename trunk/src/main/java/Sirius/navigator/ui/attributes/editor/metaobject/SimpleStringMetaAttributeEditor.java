/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * SimpleStringMetaAttributeEditor.java
 *
 * Created on 29. August 2004, 11:50
 */
package Sirius.navigator.ui.attributes.editor.metaobject;

/**
 * Ein Editor f\u00FCr String Attribute.
 *
 * @author   Pascal
 * @version  $Revision$, $Date$
 */
public class SimpleStringMetaAttributeEditor extends DefaultSimpleMetaAttributeEditor {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of SimpleStringMetaAttributeEditor.
     */
    public SimpleStringMetaAttributeEditor() {
        super();

        this.logger = org.apache.log4j.Logger.getLogger(this.getClass());
        this.readOnly = false;
    }
}
