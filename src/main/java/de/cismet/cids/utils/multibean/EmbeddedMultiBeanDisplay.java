/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.utils.multibean;

import org.apache.log4j.Logger;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class EmbeddedMultiBeanDisplay extends JLabel {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(EmbeddedMultiBeanDisplay.class);
    private static final int ALPHA_MAX = 255;
    private static final int ALPHA_MIN = 0;
    private static final Map<JComponent, EmbeddedMultiBeanDisplay> MB_MAP = new HashMap();

    //~ Instance fields --------------------------------------------------------

    private int alpha = ALPHA_MIN;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new EmbeddedValidatorDisplay object.
     *
     * @param  component  DOCUMENT ME!
     */
    private EmbeddedMultiBeanDisplay(final JComponent component) {
        setHorizontalAlignment(RIGHT);
        setVerticalAlignment(TOP);
        setCursor(Cursor.getPredefinedCursor(0));
        setVisible(false);
        // setToolTipText("unterschiedliche Werte");
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   component  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static EmbeddedMultiBeanDisplay getEmbeddedDisplayFor(final JComponent component) {
        if (!MB_MAP.containsKey(component)) {
            MB_MAP.put(component, new EmbeddedMultiBeanDisplay(component));
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
}
