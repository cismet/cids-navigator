/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 *  Copyright (C) 2010 stefan
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package Sirius.navigator.search;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;

/**
 * DOCUMENT ME!
 *
 * @author   stefan
 * @version  $Revision$, $Date$
 */
public class Test {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  args  DOCUMENT ME!
     */
    public static void main(final String[] args) {
        final JFrame frame = new JFrame();
        final JToolBar bar = new JToolBar();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        final CidsSearchComboBar sbar = new CidsSearchComboBar();
        sbar.setMaximumSize(new Dimension(350, 20));
//        sbar.setAlignmentX(CidsSearchComboBar.RIGHT_ALIGNMENT);
//        bar.add(Box.createHorizontalStrut(300));
//        JPanel innerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//        innerPanel.add(sbar);

//        bar.add(innerPanel, -1);
//        bar.add(new JButton("Test"));
        bar.add(sbar);
        frame.getContentPane().add(sbar, BorderLayout.NORTH);
//        frame.add(sbar, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }
}
