/*
 * HyplerlinkRenderer.java
 *
 * Created on 25. August 2004, 10:06
 */
package Sirius.navigator.ui.attributes.renderer;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import javax.swing.border.*;
import javax.swing.event.*;

import org.apache.log4j.Logger;

import Sirius.navigator.ui.widget.*;
import Sirius.server.localserver.attribute.ObjectAttribute;
import Sirius.server.middleware.types.*;
import de.cismet.tools.BrowserLauncher;

/**
 * Ein einfacher Renderer f\u00FCr URLs
 *
 * @author  Pascal
 */
public class HyperlinkRenderer implements TableCellRenderer {
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private final HyperlinkLabel hyperlinkLabel;
    private final Logger logger;

    public HyperlinkRenderer() {
        this.logger = Logger.getLogger(this.getClass());
        this.hyperlinkLabel = new HyperlinkLabel();

        this.hyperlinkLabel.setOpaque(true);
        this.hyperlinkLabel.setBorder(new EmptyBorder(1, 1, 1, 1));

        this.hyperlinkLabel.setHyperlinkListener(new HyperlinkListener() {

            public void hyperlinkUpdate(HyperlinkEvent e) {
                log.debug("hyperlinkUpdate");
                if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("following link '" + e.getURL() + "'");
                    }
                //Sirius.navigator.resource.PropertyManager.getManager().getAppletContext().showDocument(e.getURL(), "_blank");
                //  BrowserLauncher.openURL(e.getURL().toString());
                }
            }
        });
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        this.hyperlinkLabel.setFont(table.getFont());

        if (isSelected) {
            //this.hyperlinkLabel.setForeground(table.getSelectionForeground());
            this.hyperlinkLabel.setBackground(table.getSelectionBackground());
        } else {
            //this.hyperlinkLabel.setForeground(table.getForeground());
            this.hyperlinkLabel.setBackground(table.getBackground());
        }

        if (value instanceof URL) {
            this.hyperlinkLabel.setUrl((URL) value);
        } else {
            try {
                this.hyperlinkLabel.setUrl(new URL(value.toString()));
            } catch (Throwable t) {
                logger.warn("no valid url: " + value + "(" + value.getClass() + ")");
            }
        }

        return hyperlinkLabel;
    }

    public Component getComponent() {
        return hyperlinkLabel;
    }
}
