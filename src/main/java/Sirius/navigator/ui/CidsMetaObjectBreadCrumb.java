/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Sirius.navigator.ui;

import Sirius.server.middleware.types.MetaObject;
import de.cismet.tools.gui.breadcrumb.BreadCrumb;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

/**
 *
 * @author thorsten
 */
public abstract class CidsMetaObjectBreadCrumb extends BreadCrumb {

    MetaObject metaObject;
    Component renderer;
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());

    public CidsMetaObjectBreadCrumb(MetaObject metaObject) {
        super(metaObject.toString());
        this.metaObject = metaObject;

        try {
            ImageIcon i = new ImageIcon(metaObject.getMetaClass().getObjectIcon().getImageData());
            setIcon(i);
        } catch (Exception e) {
            log.warn("Fehler beim setzen des Icons im BreadCrumb", e);
        }
    }

    public MetaObject getMetaObject() {
        return metaObject;
    }

    public Component getRenderer() {
        return renderer;
    }
    public void setRenderer(Component renderer) {
        this.renderer = renderer;
    }
}
