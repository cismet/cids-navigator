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
package Sirius.navigator.docking;

import net.infonode.docking.View;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JMenuItem;

/**
 * DOCUMENT ME!
 *
 * @author   spuhl
 * @version  $Revision$, $Date$
 */
public class CustomView extends View {

    //~ Instance fields --------------------------------------------------------

    private String id;
    private String viewName;
    private Icon viewIcon;
    private JMenuItem item;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new CustomView object.
     *
     * @param  id        DOCUMENT ME!
     * @param  viewName  DOCUMENT ME!
     * @param  viewIcon  DOCUMENT ME!
     * @param  comp      DOCUMENT ME!
     */
    public CustomView(final String id, final String viewName, final Icon viewIcon, final Component comp) {
        super(viewName, viewIcon, comp);
        this.id = id;
        this.viewName = viewName;
        this.viewIcon = viewIcon;
        item = new JMenuItem(viewName, viewIcon);
        item.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(final ActionEvent e) {
                    if (CustomView.this.isClosable()) {
                        CustomView.this.close();
                    } else {
                        CustomView.this.restore();
                    }
                }
            });
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getId() {
        return id;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  id  DOCUMENT ME!
     */
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Icon getViewIcon() {
        return viewIcon;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  viewIcon  DOCUMENT ME!
     */
    public void setViewIcon(final Icon viewIcon) {
        this.viewIcon = viewIcon;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getViewName() {
        return viewName;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  viewName  DOCUMENT ME!
     */
    public void setViewName(final String viewName) {
        this.viewName = viewName;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public JMenuItem getMenuItem() {
        return item;
    }
}
