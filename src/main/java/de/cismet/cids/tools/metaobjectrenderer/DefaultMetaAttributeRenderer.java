/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
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
 * DOCUMENT ME!
 *
 * @author   hell
 * @version  $Revision$, $Date$
 */
public class DefaultMetaAttributeRenderer {

    //~ Static fields/initializers ---------------------------------------------

    public static final Color FOREGROUND_COLOR = new TitledBorder("X").getTitleColor(); // NOI18N

    //~ Instance fields --------------------------------------------------------

    Attribute attr; //
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of DefaultMetaAttributeRenderer.
     *
     * @param  attr  DOCUMENT ME!
     */
    public DefaultMetaAttributeRenderer(final Attribute attr) {
        this.attr = attr;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public JComponent getMetaAttributeRenderer() {
        if (false) {
            return null;
        } else {
            final JLabel ret = new JLabel();
            final String value = "";                                // NOI18N
            if ((attr != null) && (attr.getValue() != null)) {
                try {
                    ret.setText(attr.toString());
                } catch (Throwable e) {
                    log.fatal("Error in MetaAttributeRenderer", e); // NOI18N
                }
            }

            ret.setForeground(FOREGROUND_COLOR);
            return ret;
        }
    }
}
