package Sirius.navigator.event;

import java.awt.*;
import java.awt.event.*;

import Sirius.navigator.ui.*;

/**
 *
 * @author  pascal
 */
public class CataloguePopupMenuListener extends MouseAdapter
{
    private final MutablePopupMenu popupMenu;
    
    /** Creates a new instance of CataloguePopupMenuListener */
    public CataloguePopupMenuListener(MutablePopupMenu popupMenu)
    {
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
    
    public void mousePressed(MouseEvent e)
    {
        if(e.isPopupTrigger())
        {
            popupMenu.show((Component)e.getSource(), e.getX(), e.getY());
        }
    }
    
    
    
    public void mouseReleased(MouseEvent e)
    {
        if(e.isPopupTrigger())
        {
            popupMenu.show((Component)e.getSource(), e.getX(), e.getY());
        }
    }
}




