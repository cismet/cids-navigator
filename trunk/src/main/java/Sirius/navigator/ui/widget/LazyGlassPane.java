/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui.widget;

/*******************************************************************************

        Copyright (c)   :       EIG (Environmental Informatics Group)
                                                http://www.htw-saarland.de/eig
                                                Prof. Dr. Reiner Guettler
                                                Prof. Dr. Ralf Denzer

                                                HTWdS
                                                Hochschule fuer Technik und Wirtschaft des Saarlandes
                                                Goebenstr. 40
                                                66117 Saarbruecken
                                                Germany

        Programmers             :       Pascal

        Project                 :       WuNDA 2
        Filename                :
        Version                 :       1.0
        Purpose                 :
        Created                 :       01.10.1999
        History                 :

*******************************************************************************/
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * LazyGlassPane ist ein GlassPane, das nicht auf Benutzereingaben reagiert. Es werden "dummy-Listener" hinzugefuegt,
 * die keine Events verarbeiten.
 *
 * @version  $Revision$, $Date$
 * @see      LazyPanel
 */

class LazyGlassPane extends JComponent {

    //~ Instance fields --------------------------------------------------------

    private boolean eventsBlocked = false;
    private LazyMouseListener lazyMouseListener;
    private LazyKeyListener lazyKeyListener;
    // private LazyFocusListener lazyFocusListener;

    //~ Constructors -----------------------------------------------------------

    /**
     * Dieser Konstruktor erzeugt ein neues LazyGlassPane.
     *
     * @param  blockEvents  Bei true werden die Events sofort blockiert.
     */
    public LazyGlassPane(final boolean blockEvents) {
        super();
        lazyMouseListener = new LazyMouseListener();
        lazyKeyListener = new LazyKeyListener();
        // lazyFocusListener = new LazyFocusListener();
        this.blockEvents(blockEvents);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  blockEvents  DOCUMENT ME!
     */
    public void blockEvents(final boolean blockEvents) {
        if (blockEvents && !eventsBlocked) {
            eventsBlocked = blockEvents;
            this.addMouseListener(lazyMouseListener);
            this.addKeyListener(lazyKeyListener);
            // this.addFocusListener(lazyFocusListener);
            this.setVisible(blockEvents);
            this.requestFocus();
        } else if (!blockEvents && eventsBlocked) {
            eventsBlocked = blockEvents;
            this.removeMouseListener(lazyMouseListener);
            this.removeKeyListener(lazyKeyListener);
            // this.removeFocusListener(lazyFocusListener);
            this.setVisible(blockEvents);
        }
    }

    /*public boolean hasFocus()
     * {     return false; }
     *
     * public boolean isFocusCycleRoot() {     return true;}*/

    // deprecated since 1.4:
    /*
     * public boolean isFocusTraversable() {     return false; }
     *
     * public boolean isManagingFocus() {     return true;}*/

/*      public boolean isFocusable()
        {
                return false;
        }*/

    @Override
    public boolean isFocusable() {
        return true;
    }

    @Override
    public boolean hasFocus() {
        return true;
    }
}

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
class LazyMouseListener implements MouseListener, MouseMotionListener {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new LazyMouseListener object.
     */
    public LazyMouseListener() {
        super();
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void mouseClicked(final MouseEvent e) {
    }
    @Override
    public void mouseDragged(final MouseEvent e) {
    }
    @Override
    public void mouseEntered(final MouseEvent e) {
    }
    @Override
    public void mouseExited(final MouseEvent e) {
    }
    @Override
    public void mousePressed(final MouseEvent e) {
    }
    @Override
    public void mouseReleased(final MouseEvent e) {
    }
    @Override
    public void mouseMoved(final MouseEvent e) {
    }
}

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
class LazyKeyListener implements KeyListener {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new LazyKeyListener object.
     */
    public LazyKeyListener() {
        super();
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void keyPressed(final KeyEvent e) {
    }
    @Override
    public void keyReleased(final KeyEvent e) {
    }
    @Override
    public void keyTyped(final KeyEvent e) {
    }
}
