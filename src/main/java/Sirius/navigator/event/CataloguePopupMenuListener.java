package Sirius.navigator.event;

import java.awt.*;
import java.awt.event.*;

import Sirius.navigator.ui.*;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

/**
 *
 * @author  pascal
 */
public class CataloguePopupMenuListener extends MouseAdapter {
    private final transient org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private final MutablePopupMenu popupMenu;

    /** Creates a new instance of CataloguePopupMenuListener */
    public CataloguePopupMenuListener(MutablePopupMenu popupMenu) {
        this.popupMenu = popupMenu;
    }

    /** Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     *
     */
    /* public void mouseClicked(MouseEvent e)
    {
    if(e.isPopupTrigger())
    {
    popupMenu.show((Component)e.getSource(), e.getX(), e.getY());
    }
    }*/
    public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger()) {
            try {
            if (e.getSource() instanceof JTree){
                JTree currentTree=(JTree)e.getSource();
                TreePath selPath = currentTree.getPathForLocation(e.getX(), e.getY());
                if (selPath!=null){
                    currentTree.setSelectionPath(selPath);
                }
            }
            }
            catch (Exception ex){
                log.error("Error during on-the-fly-selection",ex);
            }

            popupMenu.show((Component) e.getSource(), e.getX(), e.getY());
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) {
            popupMenu.show((Component) e.getSource(), e.getX(), e.getY());
        }
    }
}
