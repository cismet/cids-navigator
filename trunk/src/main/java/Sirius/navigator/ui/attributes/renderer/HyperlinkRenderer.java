/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * HyplerlinkRenderer.java
 *
 * Created on 25. August 2004, 10:06
 */
package Sirius.navigator.ui.attributes.renderer;

import Sirius.navigator.ui.widget.*;

import Sirius.server.localserver.attribute.ObjectAttribute;
import Sirius.server.middleware.types.*;

import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.event.*;

import java.net.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;

import de.cismet.tools.BrowserLauncher;

/**
 * Ein einfacher Renderer f\u00FCr URLs.
 *
 * @author   Pascal
 * @version  $Revision$, $Date$
 */
public class HyperlinkRenderer implements TableCellRenderer {

    //~ Instance fields --------------------------------------------------------

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private final HyperlinkLabel hyperlinkLabel;
    private final Logger logger;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new HyperlinkRenderer object.
     */
    public HyperlinkRenderer() {
        this.logger = Logger.getLogger(this.getClass());
        this.hyperlinkLabel = new HyperlinkLabel();

        this.hyperlinkLabel.setOpaque(true);
        this.hyperlinkLabel.setBorder(new EmptyBorder(1, 1, 1, 1));

        this.hyperlinkLabel.setHyperlinkListener(new HyperlinkListener() {

                @Override
                public void hyperlinkUpdate(final HyperlinkEvent e) {
                    if (log.isDebugEnabled()) {
                        log.debug("hyperlinkUpdate");                            // NOI18N
                    }
                    if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("following link '" + e.getURL() + "'"); // NOI18N
                        }
                        //
                        // Sirius.navigator.resource.PropertyManager.getManager().getAppletContext().showDocument(e.getURL(),
                        // "_blank"); BrowserLauncher.openURL(e.getURL().toString());
                    }
                }
            });
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Component getTableCellRendererComponent(final JTable table,
            final Object value,
            final boolean isSelected,
            final boolean hasFocus,
            final int row,
            final int column) {
        this.hyperlinkLabel.setFont(table.getFont());

        if (isSelected) {
            // this.hyperlinkLabel.setForeground(table.getSelectionForeground());
            this.hyperlinkLabel.setBackground(table.getSelectionBackground());
        } else {
            // this.hyperlinkLabel.setForeground(table.getForeground());
            this.hyperlinkLabel.setBackground(table.getBackground());
        }

        if (value instanceof URL) {
            this.hyperlinkLabel.setUrl((URL)value);
        } else {
            try {
                this.hyperlinkLabel.setUrl(new URL(value.toString()));
            } catch (Throwable t) {
                logger.warn("no valid url: " + value + "(" + value.getClass() + ")"); // NOI18N
            }
        }

        return hyperlinkLabel;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Component getComponent() {
        return hyperlinkLabel;
    }
}
