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

import com.sun.javafx.scene.control.skin.ContextMenuSkin;

import javafx.scene.Group;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

/**
 * DOCUMENT ME!
 *
 * @author   daniel
 * @version  $Revision$, $Date$
 */
public class CustomContextMenuSkin extends ContextMenuSkin {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new CustomContextMenuSkin object.
     *
     * @param  cm  DOCUMENT ME!
     */
    public CustomContextMenuSkin(final ContextMenu cm) {
        super(cm);
        final ArrayList<MenuItem> newItems = new ArrayList<MenuItem>();
        final double width = cm.getPrefWidth() - 25;
        for (final MenuItem i : cm.getItems()) {
            newItems.add(new MenuItem(
                    "",
                    new Group(
                        new Rectangle((width <= 0) ? 100 : width, 1, Color.TRANSPARENT),
                        new Label(i.getText()))));
        }
        cm.getItems().clear();
        cm.getItems().addAll(newItems);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public ContextMenu getSkinnable() {
        System.out.println("");
        return super.getSkinnable(); // To change body of generated methods, choose Tools | Templates.
    }
}
