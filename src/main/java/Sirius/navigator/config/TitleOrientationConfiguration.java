/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sirius.navigator.config;

import net.infonode.util.Direction;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class TitleOrientationConfiguration {

    //~ Instance fields --------------------------------------------------------

    private boolean up = false;
    private boolean down = true;
    private boolean left = true;
    private boolean right = true;

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  the up
     */
    public boolean isUp() {
        return up;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  up  the up to set
     */
    public void setUp(final boolean up) {
        this.up = up;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  the down
     */
    public boolean isDown() {
        return down;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  down  the down to set
     */
    public void setDown(final boolean down) {
        this.down = down;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  the left
     */
    public boolean isLeft() {
        return left;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  left  the left to set
     */
    public void setLeft(final boolean left) {
        this.left = left;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  the right
     */
    public boolean isRight() {
        return right;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  right  the right to set
     */
    public void setRight(final boolean right) {
        this.right = right;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   dir  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean showTitleForDirection(final Direction dir) {
        return (dir.equals(Direction.UP) && up)
                    || (dir.equals(Direction.DOWN) && down)
                    || (dir.equals(Direction.LEFT) && left)
                    || (dir.equals(Direction.RIGHT) && right);
    }
}
