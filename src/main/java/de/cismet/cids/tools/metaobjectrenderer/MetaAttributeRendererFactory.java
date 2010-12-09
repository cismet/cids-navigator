/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
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
 * DOCUMENT ME!
 *
 * @author   hell
 * @version  $Revision$, $Date$
 */
public class MetaAttributeRendererFactory {

    //~ Static fields/initializers ---------------------------------------------

    static MetaAttributeRendererFactory instance = null;

    //~ Instance fields --------------------------------------------------------

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of MetaObjectRendererFactory.
     */
    private MetaAttributeRendererFactory() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static MetaAttributeRendererFactory getInstance() {
        if (instance == null) {
            instance = new MetaAttributeRendererFactory();
        }
        return instance;
    }
    /**
     * DOCUMENT ME!
     *
     * @param   attr  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public JComponent getRenderer(final Attribute attr) {
        return getRenderer(attr, ""); // NOI18N
    }
    /**
     * DOCUMENT ME!
     *
     * @param   attr   DOCUMENT ME!
     * @param   title  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public JComponent getRenderer(final Attribute attr, final String title) {
        if (attr != null) {
            if (false) { // es existiert ein renderer
                return null;
            }
            // es existiert keiner aber substitute==false und es ist ein komplexes Attribut, deshalb wird der
            // ObjectRenderer genommen
            else if ((attr.getValue() instanceof MetaObject) && !attr.isSubstitute()) {
                return MetaObjectRendererFactory.getInstance().getSingleRenderer((MetaObject)attr.getValue(), title);
            } else {
                return new DefaultMetaAttributeRenderer(attr).getMetaAttributeRenderer();
            }
        } else {
            return null;
        }
    }
}
