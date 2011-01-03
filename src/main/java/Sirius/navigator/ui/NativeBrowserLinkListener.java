/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import org.xhtmlrenderer.render.Box;
import org.xhtmlrenderer.swing.BasicPanel;
import org.xhtmlrenderer.swing.FSMouseListener;
import org.xhtmlrenderer.swing.LinkListener;

import java.awt.Desktop;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

import java.io.IOException;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * DOCUMENT ME!
 *
 * @author   jweintraut
 * @version  $Revision$, $Date$
 */
public class NativeBrowserLinkListener implements FSMouseListener {

    //~ Static fields/initializers ---------------------------------------------

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(
            NativeBrowserLinkListener.class);

    //~ Instance fields --------------------------------------------------------

    private long timeOfPressedEvent = 0L;
    private int modifiers = -1;
    private LinkListener bridgeToBasicPanelsetDocumentRelative = new LinkListener();
    private Desktop desktop = null;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new NativeBrowserLinkListener object.
     */
    public NativeBrowserLinkListener() {
        if (Desktop.isDesktopSupported()) {
            desktop = Desktop.getDesktop();
        } else {
            LOG.warn("Could not retrieve a desktop object to open links in user's browser.");
        }

        if ((desktop != null) && !desktop.isSupported(Desktop.Action.BROWSE)) {
            LOG.warn("Current desktop object doesn't allow browsing.");
        }
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void onMouseUp(final BasicPanel pnl, final Box box) {
        checkForLink(pnl, box);
    }

    @Override
    public void onMousePressed(final BasicPanel pnl, final MouseEvent me) {
        modifiers = me.getModifiers();
        timeOfPressedEvent = System.currentTimeMillis();
    }
    /**
     * TODO: Hack to call BasicPanel.setDocumentRelative() which is protected
     *
     * @param  panel  DOCUMENT ME!
     * @param  uri    DOCUMENT ME!
     */
    public void linkClicked(final BasicPanel panel, final String uri) {
        final boolean isCTRLPressed = (modifiers & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK;
        final boolean isPressAndUpEventDuringOneSecond = Math.abs(System.currentTimeMillis() - timeOfPressedEvent)
                    < 1000;

        if (isCTRLPressed && isPressAndUpEventDuringOneSecond
                    && ((desktop != null) && desktop.isSupported(Desktop.Action.BROWSE))) {
            try {
                desktop.browse(new URI(uri));
            } catch (IOException ex) {
                LOG.error("Referenced URI '" + uri + "' couldn't be read.", ex);
                bridgeToBasicPanelsetDocumentRelative.linkClicked(panel, uri);
            } catch (URISyntaxException ex) {
                LOG.error("Syntax of URI '" + uri + "' is broken.", ex);
                bridgeToBasicPanelsetDocumentRelative.linkClicked(panel, uri);
            }
        } else {
            bridgeToBasicPanelsetDocumentRelative.linkClicked(panel, uri);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  panel  DOCUMENT ME!
     * @param  box    DOCUMENT ME!
     */
    private void checkForLink(final BasicPanel panel, final Box box) {
        if ((box == null) || (box.getElement() == null)) {
            return;
        }

        final String uri = findLink(panel, box.getElement());

        if (uri != null) {
            linkClicked(panel, uri);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   panel  DOCUMENT ME!
     * @param   e      DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private String findLink(final BasicPanel panel, final Element e) {
        String uri = null;

        for (Node node = e; node.getNodeType() == Node.ELEMENT_NODE; node = node.getParentNode()) {
            uri = panel.getSharedContext().getNamespaceHandler().getLinkUri((Element)node);

            if (uri != null) {
                break;
            }
        }

        return uri;
    }

    @Override
    public void onMouseOver(final BasicPanel pnl, final Box box) {
    }

    @Override
    public void onMouseOut(final BasicPanel pnl, final Box box) {
    }

    @Override
    public void onMouseDragged(final BasicPanel pnl, final MouseEvent me) {
    }

    @Override
    public void reset() {
    }
}
