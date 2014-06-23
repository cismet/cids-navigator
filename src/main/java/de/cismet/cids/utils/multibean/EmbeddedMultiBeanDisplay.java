/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.utils.multibean;

import org.apache.log4j.Logger;

import org.jdesktop.swingx.JXDatePicker;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.text.JTextComponent;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class EmbeddedMultiBeanDisplay extends JLabel {

    //~ Static fields/initializers ---------------------------------------------

    private static final String DIFFERENT_VALUE = "<html><i>unterschiedliche Werte</i>";
    private static final String LOADING = "<html><i>wird geladen</i>";

    private static final int ALPHA_MAX = 255;
    private static final int ALPHA_MIN = 0;

    public static ImageIcon ICON_WARNING = new ImageIcon(MultiBeanHelper.class.getResource(
                "/de/cismet/cids/utils/multibean/warning.png"));

    //~ Instance fields --------------------------------------------------------

    private int alpha = ALPHA_MIN;
    private boolean enabled = true;
    private boolean editable = true;
    private final PropertyChangeListener enableListener = new PropertyChangeListener() {

            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("enabled")) {
                    enabled = (Boolean)evt.getNewValue();
                }
                if (evt.getPropertyName().equals("editable")) {
                    editable = (Boolean)evt.getNewValue();
                }
            }
        };

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new EmbeddedValidatorDisplay object.
     *
     * @param  component        DOCUMENT ME!
     * @param  propertyName     DOCUMENT ME!
     * @param  multiBeanHelper  DOCUMENT ME!
     */
    private EmbeddedMultiBeanDisplay(final JComponent component,
            final String propertyName,
            final MultiBeanHelper multiBeanHelper) {
        setHorizontalAlignment(RIGHT);
        setVerticalAlignment(TOP);
        setCursor(Cursor.getPredefinedCursor(0));
        setVisible(false);
        // setToolTipText("unterschiedliche Werte");

        setIcon(EmbeddedMultiBeanDisplay.ICON_WARNING);
        setText(DIFFERENT_VALUE);

        if ((component instanceof JTextField) || (component instanceof JTextArea)) {
            if (!(component.getLayout() instanceof BorderLayout)) {
                component.setLayout(new BorderLayout());
            }
            component.add(this, BorderLayout.WEST);
        } else if (component instanceof JComboBox) {
            setVisible(true);
            final ListCellRenderer rend = ((JComboBox)component).getRenderer();
            ((JComboBox)component).setRenderer(new DefaultListCellRenderer() {

                    @Override
                    public Component getListCellRendererComponent(final JList<?> list,
                            final Object value,
                            final int index,
                            final boolean isSelected,
                            final boolean cellHasFocus) {
                        final JLabel comp = (JLabel)rend.getListCellRendererComponent(
                                list,
                                value,
                                index,
                                isSelected,
                                cellHasFocus);
                        if ((value == null)
                                    && (multiBeanHelper.isLoading()
                                        || (!multiBeanHelper.getBeans().isEmpty() && !multiBeanHelper.isLoading()
                                            && !multiBeanHelper.isValuesAllEquals(propertyName)))) {
                            comp.setIcon(EmbeddedMultiBeanDisplay.this.getIcon());
                            comp.setText(EmbeddedMultiBeanDisplay.this.getText());
                        }
                        return comp;
                    }
                });
        } else if (component instanceof JXDatePicker) {
            final JFormattedTextField jft = ((JXDatePicker)component).getEditor();
            jft.setLayout(new BorderLayout());
            jft.add(this, BorderLayout.WEST);
        }

        multiBeanHelper.addListener(new MultiBeanHelperListener() {

                @Override
                public void refillAllEqualsMapStarted() {
                    setText(LOADING);
                    component.removePropertyChangeListener(enableListener);
                    if (component instanceof JTextComponent) {
                        ((JTextComponent)component).setEditable(false);
                    }
                    component.setEnabled(false);
                    component.addPropertyChangeListener(enableListener);
                    doOverlay(true);
                }

                @Override
                public void refillAllEqualsMapDone() {
                    setText(DIFFERENT_VALUE);
                    component.removePropertyChangeListener(enableListener);
                    component.setEnabled(enabled);
                    if (component instanceof JTextComponent) {
                        ((JTextComponent)component).setEditable(editable);
                    }
                    component.addPropertyChangeListener(enableListener);
                    doOverlay(!multiBeanHelper.isValuesAllEquals(propertyName));
                }

                @Override
                public void allEqualsChanged(final String propertyNameAllEqualsChanged, final boolean allEquals) {
                    if ((!multiBeanHelper.isLoading() && propertyNameAllEqualsChanged.equals(propertyName))
                                || multiBeanHelper.isLoading()) {
                        doOverlay(!allEquals);
                    }
                }
            });

        component.addPropertyChangeListener(enableListener);

        enabled = component.isEnabled();
        if (component instanceof JTextComponent) {
            editable = ((JTextComponent)component).isEditable();
        }
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  component        DOCUMENT ME!
     * @param  propertyName     DOCUMENT ME!
     * @param  multiBeanHelper  DOCUMENT ME!
     */
    public static void registerComponentForProperty(final JComponent component,
            final String propertyName,
            final MultiBeanHelper multiBeanHelper) {
        final EmbeddedMultiBeanDisplay overlay = new EmbeddedMultiBeanDisplay(component, propertyName, multiBeanHelper);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   component        DOCUMENT ME!
     * @param   multiBeanHelper  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Deprecated
    public static EmbeddedMultiBeanDisplay getEmbeddedDisplayFor(final JComponent component,
            final MultiBeanHelper multiBeanHelper) {
        return null;
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2 = (Graphics2D)g;
        final Composite oldComposite = g2.getComposite();
        g2.setComposite(AlphaComposite.SrcOver);
        final Color c = getParent().getBackground();
        final Color color = new Color(c.getRed(), c.getGreen(), c.getBlue(), this.alpha);
        g2.setColor(color);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setComposite(oldComposite);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  overlay  DOCUMENT ME!
     */
    public void doOverlay(final boolean overlay) {
        if (overlay) {
            setAlpha(ALPHA_MIN);
            setVisible(true);
        } else {
            setAlpha(ALPHA_MAX);
            setVisible(false);
        }
        repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  alpha  DOCUMENT ME!
     */
    public void setAlpha(int alpha) {
        if (alpha < ALPHA_MIN) {
            alpha = ALPHA_MIN;
        }
        if (alpha > ALPHA_MAX) {
            alpha = ALPHA_MAX;
        }
        this.alpha = alpha;
    }
}
