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
 * Eine Angebotsklasse f\u00FCr die FAQ f\u00FCr das Problem:
 * Frage: Ich m\u00F6chte in meine Swing-GUI gern JLabel einf\u00FCgen, die
 * aktive Hyperlinks enthalten. Wie kann ich vorgehen?
 * Antwort: Die untenstehende Klasse kann wie folgt genutzt werden
 *
 * @author  Andreas Jaeger <jaeger@ifgi.uni-muenster.de>, Pascal
 */
public class HyperlinkLabel extends JLabel implements MouseListener {
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private static Color linkColor = Color.blue;
    private static Color mouseOverColor = Color.magenta;
    private static Border emptyBorder =
            BorderFactory.createEmptyBorder(0, 0, 1, 0);
    private static Border mouseDownBorder =
            BorderFactory.createMatteBorder(0, 0, 1, 0, mouseOverColor);
    //private JLabel label;
    private URL url;
    private HyperlinkListener hyperlinkListener;
    private boolean dragging;

    public HyperlinkLabel(URL url, HyperlinkListener hyperlinkListener) {
        this();

        this.setUrl(url);
        this.setHyperlinkListener(hyperlinkListener);
    }

    public HyperlinkLabel() {
        super();
        this.setForeground(linkColor);
        this.setBorder(emptyBorder);
        this.setCursor(Cursor.getDefaultCursor());
        this.addMouseListener(this);
    //setLayout(new GridBagLayout());
    //add(label, new GridBagConstraints());
    }

    public void setUrl(URL url) {
        this.url = url;
        this.setText(url.toExternalForm());
    }

    public URL getUrl() {
        return this.url;
    }

    public void setHyperlinkListener(HyperlinkListener hyperlinkListener) {
        this.hyperlinkListener = hyperlinkListener;
    }

    public static void setDefaultLinkColor(Color c) {
        linkColor = c;
    }

    public static void setDefaultMouseOverColor(Color c) {
        mouseOverColor = c;
        mouseDownBorder = BorderFactory.createMatteBorder(0, 0, 1, 0,
                mouseOverColor);
    }

    public void mouseEntered(MouseEvent e) {
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

    public void mousePressed(MouseEvent e) {
        dragging = true;
        this.setBorder(mouseDownBorder);
    }

    public void mouseReleased(MouseEvent e) {
        this.setBorder(emptyBorder);
        dragging = false;
    }

    public void mouseClicked(MouseEvent e) {
        log.debug("HyperlinkLabelClicked()");//NOI18N
        if (e.getClickCount() != 1) {
            return;
        }
        if (hyperlinkListener != null) {
            hyperlinkListener.hyperlinkUpdate(new HyperlinkEvent(this,
                    HyperlinkEvent.EventType.ACTIVATED, url));
        }
    }

    public void mouseExited(MouseEvent e) {
        this.setCursor(Cursor.getDefaultCursor());
        this.setForeground(linkColor);
        this.setBorder(emptyBorder);
        if (hyperlinkListener != null) {
            hyperlinkListener.hyperlinkUpdate(new HyperlinkEvent(this,
                    HyperlinkEvent.EventType.EXITED, url));
        }
    }
}
