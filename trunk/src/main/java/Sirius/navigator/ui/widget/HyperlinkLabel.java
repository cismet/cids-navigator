/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * HyperlinkLabel.java
 *
 * Created on 14. Januar 2004, 11:08
 */
package Sirius.navigator.ui.widget;

import java.awt.*;
import java.awt.event.*;

import java.net.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

/**
 * Eine Angebotsklasse f\u00FCr die FAQ f\u00FCr das Problem: Frage: Ich m\u00F6chte in meine Swing-GUI gern JLabel
 * einf\u00FCgen, die aktive Hyperlinks enthalten. Wie kann ich vorgehen? Antwort: Die untenstehende Klasse kann wie
 * folgt genutzt werden
 *
 * @author   Andreas Jaeger <jaeger@ifgi.uni-muenster.de>, Pascal
 * @version  $Revision$, $Date$
 */
public class HyperlinkLabel extends JLabel implements MouseListener {

    //~ Static fields/initializers ---------------------------------------------

    private static Color linkColor = Color.blue;
    private static Color mouseOverColor = Color.magenta;
    private static Border emptyBorder = BorderFactory.createEmptyBorder(0, 0, 1, 0);
    private static Border mouseDownBorder = BorderFactory.createMatteBorder(0, 0, 1, 0, mouseOverColor);

    //~ Instance fields --------------------------------------------------------

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    // private JLabel label;
    private URL url;
    private HyperlinkListener hyperlinkListener;
    private boolean dragging;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new HyperlinkLabel object.
     */
    public HyperlinkLabel() {
        super();
        this.setForeground(linkColor);
        this.setBorder(emptyBorder);
        this.setCursor(Cursor.getDefaultCursor());
        this.addMouseListener(this);
        // setLayout(new GridBagLayout());
        // add(label, new GridBagConstraints());
    }

    /**
     * Creates a new HyperlinkLabel object.
     *
     * @param  url                DOCUMENT ME!
     * @param  hyperlinkListener  DOCUMENT ME!
     */
    public HyperlinkLabel(final URL url, final HyperlinkListener hyperlinkListener) {
        this();

        this.setUrl(url);
        this.setHyperlinkListener(hyperlinkListener);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  url  DOCUMENT ME!
     */
    public void setUrl(final URL url) {
        this.url = url;
        this.setText(url.toExternalForm());
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public URL getUrl() {
        return this.url;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  hyperlinkListener  DOCUMENT ME!
     */
    public void setHyperlinkListener(final HyperlinkListener hyperlinkListener) {
        this.hyperlinkListener = hyperlinkListener;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  c  DOCUMENT ME!
     */
    public static void setDefaultLinkColor(final Color c) {
        linkColor = c;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  c  DOCUMENT ME!
     */
    public static void setDefaultMouseOverColor(final Color c) {
        mouseOverColor = c;
        mouseDownBorder = BorderFactory.createMatteBorder(0, 0, 1, 0,
                mouseOverColor);
    }

    @Override
    public void mouseEntered(final MouseEvent e) {
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.setForeground(mouseOverColor);
        if (hyperlinkListener != null) {
            hyperlinkListener.hyperlinkUpdate(new HyperlinkEvent(this,
                    HyperlinkEvent.EventType.ENTERED, url));
        }
        if (dragging) {
            this.setBorder(mouseDownBorder);
        }
    }

    @Override
    public void mousePressed(final MouseEvent e) {
        dragging = true;
        this.setBorder(mouseDownBorder);
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
        this.setBorder(emptyBorder);
        dragging = false;
    }

    @Override
    public void mouseClicked(final MouseEvent e) {
        if (log.isDebugEnabled()) {
            log.debug("HyperlinkLabelClicked()"); // NOI18N
        }
        if (e.getClickCount() != 1) {
            return;
        }
        if (hyperlinkListener != null) {
            hyperlinkListener.hyperlinkUpdate(new HyperlinkEvent(this,
                    HyperlinkEvent.EventType.ACTIVATED, url));
        }
    }

    @Override
    public void mouseExited(final MouseEvent e) {
        this.setCursor(Cursor.getDefaultCursor());
        this.setForeground(linkColor);
        this.setBorder(emptyBorder);
        if (hyperlinkListener != null) {
            hyperlinkListener.hyperlinkUpdate(new HyperlinkEvent(this,
                    HyperlinkEvent.EventType.EXITED, url));
        }
    }
}
