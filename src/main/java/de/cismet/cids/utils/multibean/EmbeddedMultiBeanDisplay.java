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

import java.util.HashMap;
import java.util.Map;

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

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class EmbeddedMultiBeanDisplay extends JLabel implements PropertyChangeListener {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(EmbeddedMultiBeanDisplay.class);
    private static final String DIFFERENT_VALUE = "<html><i>unterschiedliche Werte</i>";

    private static final int ALPHA_MAX = 255;
    private static final int ALPHA_MIN = 0;
    private static final Map<JComponent, EmbeddedMultiBeanDisplay> MB_MAP = new HashMap();

    public static ImageIcon ICON_WARNING = new ImageIcon(MultiBeanHelper.class.getResource(
                "/de/cismet/cids/utils/multibean/warning.png"));
    private static final Map<String, EmbeddedMultiBeanDisplay> componentMap =
        new HashMap<String, EmbeddedMultiBeanDisplay>();

    //~ Instance fields --------------------------------------------------------

    private int alpha = ALPHA_MIN;
    private final MultiBeanHelper multiBeanHelper;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new EmbeddedValidatorDisplay object.
     *
     * @param  component        DOCUMENT ME!
     * @param  multiBeanHelper  DOCUMENT ME!
     */
    private EmbeddedMultiBeanDisplay(final JComponent component, final MultiBeanHelper multiBeanHelper) {
        setHorizontalAlignment(RIGHT);
        setVerticalAlignment(TOP);
        setCursor(Cursor.getPredefinedCursor(0));
        setVisible(false);
        // setToolTipText("unterschiedliche Werte");

        this.multiBeanHelper = multiBeanHelper;
        multiBeanHelper.addPropertyChangeListener(this);
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
        final EmbeddedMultiBeanDisplay overlay;
        if ((component instanceof JTextField) || (component instanceof JTextArea)) {
            overlay = EmbeddedMultiBeanDisplay.getEmbeddedDisplayFor(component, multiBeanHelper);
            overlay.setIcon(EmbeddedMultiBeanDisplay.ICON_WARNING);
            overlay.setText(DIFFERENT_VALUE);
            if (!(component.getLayout() instanceof BorderLayout)) {
                component.setLayout(new BorderLayout());
            }
            component.add(overlay, BorderLayout.WEST);
        } else if (component instanceof JComboBox) {
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
                        if (!multiBeanHelper.isValuesAllEquals(propertyName) && (value == null)
                                    && !multiBeanHelper.getBeans().isEmpty()) {
                            comp.setIcon(EmbeddedMultiBeanDisplay.ICON_WARNING);
                            comp.setText(DIFFERENT_VALUE);
                        }
                        return comp;
                    }
                });
            overlay = null;
        } else if (component instanceof JXDatePicker) {
            final JFormattedTextField jft = ((JXDatePicker)component).getEditor();
            overlay = EmbeddedMultiBeanDisplay.getEmbeddedDisplayFor(jft, multiBeanHelper);
            overlay.setIcon(EmbeddedMultiBeanDisplay.ICON_WARNING);
            overlay.setText(DIFFERENT_VALUE);
            jft.setLayout(new BorderLayout());
            jft.add(overlay, BorderLayout.WEST);
        } else {
            overlay = null;
        }
        componentMap.put(propertyName, overlay);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  propertyName  DOCUMENT ME!
     */
    private void refreshComponent(final String propertyName) {
        final EmbeddedMultiBeanDisplay component = componentMap.get(propertyName);
        // if (component instanceof JComponent) {
        if ((component != null) && !multiBeanHelper.isValuesAllEquals(propertyName)) {
            component.doOverlay(true);
        } else if (component != null) {
            component.doOverlay(false);
        }
        // }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   component        DOCUMENT ME!
     * @param   multiBeanHelper  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static EmbeddedMultiBeanDisplay getEmbeddedDisplayFor(final JComponent component,
            final MultiBeanHelper multiBeanHelper) {
        if (!MB_MAP.containsKey(component)) {
            MB_MAP.put(component, new EmbeddedMultiBeanDisplay(component, multiBeanHelper));
        }
        return (EmbeddedMultiBeanDisplay)MB_MAP.get(component);
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

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        if (multiBeanHelper.equals(evt.getSource()) && MultiBeanHelper.EVENT_NAME.equals(evt.getPropertyName())) {
            final String propertyName = (String)evt.getNewValue();
            refreshComponent(propertyName);
        }
    }
}
