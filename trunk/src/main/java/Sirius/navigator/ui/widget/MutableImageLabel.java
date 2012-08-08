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
        Version                 :       1.0
        Purpose                 :
        Created                 :       16.02.2000
        History                 :       17.04.2003

*******************************************************************************/

import java.awt.event.*;

import java.net.URL;

import javax.swing.*;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class MutableImageLabel extends JLabel {

    //~ Instance fields --------------------------------------------------------

    private ImageIcon imageOff;
    private ImageIcon imageOn;
    private Timer timer;

    private boolean off = true;
    private boolean on = false;

    private int blinkQueue = 0;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new MutableImageLabel object.
     *
     * @param  imageOff  DOCUMENT ME!
     * @param  imageOn   DOCUMENT ME!
     */
    public MutableImageLabel(final ImageIcon imageOff, final ImageIcon imageOn) {
        super(imageOff);
        this.imageOff = imageOff;
        this.imageOn = imageOn;
        this.initImageLabel();
    }

    /**
     * Creates a new MutableImageLabel object.
     *
     * @param  imageOff  DOCUMENT ME!
     * @param  imageOn   DOCUMENT ME!
     */
    public MutableImageLabel(final String imageOff, final String imageOn) {
        this.imageOff = new ImageIcon(imageOff);
        this.imageOn = new ImageIcon(imageOn);
        this.setIcon(this.imageOff);
        this.initImageLabel();
    }

    /**
     * Creates a new MutableImageLabel object.
     *
     * @param  imageOff  DOCUMENT ME!
     * @param  imageOn   DOCUMENT ME!
     */
    public MutableImageLabel(final URL imageOff, final URL imageOn) {
        this.imageOff = new ImageIcon(imageOff);
        this.imageOn = new ImageIcon(imageOn);
        this.setIcon(this.imageOff);
        this.initImageLabel();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    protected void initImageLabel() {
        off = true;
        on = false;

        timer = new Timer(250, new ActionListener() {

                    @Override
                    public void actionPerformed(final ActionEvent evt) {
                        if (on) {
                            MutableImageLabel.this.imageOff();
                        } else {
                            MutableImageLabel.this.imageOn();
                        }
                    }
                });

        timer.setCoalesce(false);
    }

    /**
     * DOCUMENT ME!
     */
    protected synchronized void imageOff() {
        this.setIcon(imageOff);
        off = true;
        on = false;
    }

    /**
     * DOCUMENT ME!
     */
    protected synchronized void imageOn() {
        this.setIcon(imageOn);
        off = false;
        on = true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  stopBlinking  DOCUMENT ME!
     */
    public void switchOff(final boolean stopBlinking) {
        if (stopBlinking) {
            blinkQueue--;
        }

        if (blinkQueue < 1) {
            blinkQueue = 0;
            timer.stop();
            this.imageOff();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  stopBlinking  DOCUMENT ME!
     */
    public void switchOn(final boolean stopBlinking) {
        if (stopBlinking) {
            blinkQueue--;
        }

        if (blinkQueue < 1) {
            blinkQueue = 0;
            timer.stop();
            this.imageOn();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  msec  DOCUMENT ME!
     */
    public void blink(final int msec) {
        timer.setDelay(msec);
        timer.start();
        blinkQueue++;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isBlinking() {
        if (blinkQueue > 0) {
            return true;
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  imageOff  DOCUMENT ME!
     * @param  imageOn   DOCUMENT ME!
     */
    public void setImages(final ImageIcon imageOff, final ImageIcon imageOn) {
        blinkQueue = 0;
        this.imageOff = imageOff;
        this.imageOn = imageOn;
        this.switchOff(true);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public ImageIcon[] getImages() {
        return new ImageIcon[] { imageOff, imageOn };
    }
}
