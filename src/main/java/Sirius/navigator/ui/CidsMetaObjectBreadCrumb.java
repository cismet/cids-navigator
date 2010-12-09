/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Sirius.navigator.ui;

import Sirius.server.middleware.types.MetaObject;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import de.cismet.tools.gui.breadcrumb.BreadCrumb;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public abstract class CidsMetaObjectBreadCrumb extends BreadCrumb {

    //~ Instance fields --------------------------------------------------------

    MetaObject metaObject;
    Component renderer;
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new CidsMetaObjectBreadCrumb object.
     *
     * @param  metaObject  DOCUMENT ME!
     */
    public CidsMetaObjectBreadCrumb(final MetaObject metaObject) {
        super(metaObject.toString());
        this.metaObject = metaObject;

        try {
            final ImageIcon i = new ImageIcon(metaObject.getMetaClass().getObjectIcon().getImageData());
            setIcon(i);
        } catch (Exception e) {
            log.warn("Fehler beim setzen des Icons im BreadCrumb", e); // NOI18N
        }
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public MetaObject getMetaObject() {
        return metaObject;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Component getRenderer() {
        return renderer;
    }
    /**
     * DOCUMENT ME!
     *
     * @param  renderer  DOCUMENT ME!
     */
    public void setRenderer(final Component renderer) {
        this.renderer = renderer;
    }
}
