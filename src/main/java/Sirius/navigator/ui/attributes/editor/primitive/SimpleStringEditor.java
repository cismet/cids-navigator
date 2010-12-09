/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * SimpleTextEditor.java
 *
 * Created on 24. August 2004, 09:53
 */
package Sirius.navigator.ui.attributes.editor.primitive;

import Sirius.navigator.ui.attributes.editor.*;

import org.apache.log4j.Logger;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class SimpleStringEditor extends DefaultSimpleEditor {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new SimpleStringEditor object.
     */
    public SimpleStringEditor() {
        super();

        this.logger = Logger.getLogger(SimpleStringEditor.class);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    protected void initUI() {
        super.initUI();
        if (this.complexEditorButton != null) {
            this.complexEditorButton.removeActionListener(this.editorActivationDelegate);
            this.remove(this.complexEditorButton);

            this.complexEditorButton = null;
            this.editorActivationDelegate = null;
        }
    }
}
