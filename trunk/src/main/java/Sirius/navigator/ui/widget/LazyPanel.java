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
import java.awt.GridLayout;

import javax.swing.*;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class LazyPanel extends JPanel {

    //~ Instance fields --------------------------------------------------------

    private LazyGlassPane lazyGlassPane;
    private JRootPane rootPane;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new LazyPanel object.
     *
     * @param  blockEvents  DOCUMENT ME!
     */
    public LazyPanel(final boolean blockEvents) {
        super();
        lazyGlassPane = new LazyGlassPane(true);
        rootPane = new JRootPane();
        rootPane.setLayeredPane(new JLayeredPane());
        rootPane.setGlassPane(lazyGlassPane);
        rootPane.getGlassPane().setVisible(blockEvents);
        this.add(rootPane);
        this.setLayout(new GridLayout(1, 1));
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  content  DOCUMENT ME!
     */
    public void setContent(final JComponent content) {
        rootPane.setContentPane(content);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  blockEvents  DOCUMENT ME!
     */
    public void blockEvents(final boolean blockEvents) {
        rootPane.getGlassPane().setVisible(blockEvents);
    }
}
