/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui;

import org.openide.util.Lookup;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JPanel;

import de.cismet.tools.gui.NavigatorStatusBarComponent;
import de.cismet.tools.gui.menu.CidsUiAction;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class NavigatorStatusBar extends javax.swing.JPanel {

    //~ Instance fields --------------------------------------------------------

    private Collection<? extends NavigatorStatusBarComponent> components = null;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form NavigatorToolbar.
     */
    public NavigatorStatusBar() {
        initComponents();
        init();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    private void init() {
        components = Lookup.getDefault().lookupAll(NavigatorStatusBarComponent.class);
        final List<NavigatorStatusBarComponent> leftBars = new ArrayList<NavigatorStatusBarComponent>();
        final List<NavigatorStatusBarComponent> rightBars = new ArrayList<NavigatorStatusBarComponent>();

        for (final NavigatorStatusBarComponent statusBarComponent : components) {
            if (statusBarComponent.getSide().equals(NavigatorStatusBarComponent.Side.LEFT)) {
                leftBars.add(statusBarComponent);
            } else {
                rightBars.add(statusBarComponent);
            }
        }

        int index = 0;
        Collections.sort(leftBars, new Comparator<NavigatorStatusBarComponent>() {

                @Override
                public int compare(final NavigatorStatusBarComponent o1, final NavigatorStatusBarComponent o2) {
                    final Double weight1 = o1.getWeight();
                    final Double weight2 = o2.getWeight();

                    return weight1.compareTo(weight2);
                }
            });

        Collections.sort(rightBars, new Comparator<NavigatorStatusBarComponent>() {

                @Override
                public int compare(final NavigatorStatusBarComponent o1, final NavigatorStatusBarComponent o2) {
                    final Double weight1 = o1.getWeight();
                    final Double weight2 = o2.getWeight();

                    return weight2.compareTo(weight1);
                }
            });

        for (final NavigatorStatusBarComponent bar : leftBars) {
            final GridBagConstraints gbc = new GridBagConstraints(
                    index++,
                    0,
                    1,
                    1,
                    0,
                    0,
                    GridBagConstraints.WEST,
                    GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 0, 0),
                    0,
                    0);
            this.add(bar.getComponent(), gbc);
        }

        // add filer
        final GridBagConstraints gbcFiller = new GridBagConstraints(
                index++,
                0,
                1,
                1,
                1,
                0,
                GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0),
                0,
                0);
        this.add(new JPanel(), gbcFiller);

        for (final NavigatorStatusBarComponent bar : rightBars) {
            final GridBagConstraints gbc = new GridBagConstraints(
                    index++,
                    0,
                    1,
                    1,
                    0,
                    0,
                    GridBagConstraints.EAST,
                    GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 0, 0),
                    0,
                    0);
            this.add(bar.getComponent(), gbc);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void initialize() {
        for (final NavigatorStatusBarComponent statusBarComponent : components) {
            statusBarComponent.initialize();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        setLayout(new java.awt.GridBagLayout());
    } // </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
