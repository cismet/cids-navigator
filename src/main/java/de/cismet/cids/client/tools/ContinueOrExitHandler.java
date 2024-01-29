/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.client.tools;

import Sirius.navigator.connection.SessionManager;

import lombok.Getter;
import lombok.Setter;

import java.awt.Component;
import java.awt.Frame;

import de.cismet.connectioncontext.AbstractConnectionContext;
import de.cismet.connectioncontext.ConnectionContext;
import de.cismet.connectioncontext.ConnectionContextProvider;

import de.cismet.tools.gui.ContinueOrExitDialog;
import de.cismet.tools.gui.StaticSwingTools;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class ContinueOrExitHandler implements ConnectionContextProvider {

    //~ Static fields/initializers ---------------------------------------------

    private static final String CONF_ATTR_PREFIX = "ContinueOrExit";

    //~ Instance fields --------------------------------------------------------

    @Getter private final ConnectionContext connectionContext = ConnectionContext.create(
            AbstractConnectionContext.Category.STATIC,
            ContinueOrExitHandler.class.getCanonicalName());

    @Getter @Setter private String confAttrPrefix = CONF_ATTR_PREFIX;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ContinueOrExitHandler object.
     */
    private ContinueOrExitHandler() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static ContinueOrExitHandler getInstance() {
        return LazyInitialiser.INSTANCE;
    }

    /**
     * DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public void showFromConfAttr() throws Exception {
        showFromConfAttr((Object)null);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   parent  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public void showFromConfAttr(final Frame parent) throws Exception {
        showFromConfAttr((Object)parent);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   parent  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public void showFromConfAttr(final Component parent) throws Exception {
        showFromConfAttr((Object)parent);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public boolean isEnabled() throws Exception {
        return SessionManager.getProxy()
                    .getConfigAttr(SessionManager.getSession().getUser(),
                            String.format("%s.%s", getConfAttrPrefix(), "enabled"),
                            getConnectionContext()) != null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   parent  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    private void showFromConfAttr(final Object parent) throws Exception {
        if (isEnabled()) {
            if (parent instanceof Frame) {
                StaticSwingTools.showDialog(configureDialog(new ContinueOrExitDialog((Frame)parent)), true);
            } else if (parent instanceof Component) {
                StaticSwingTools.showDialog(configureDialog(new ContinueOrExitDialog((Component)parent)), true);
            } else {
                StaticSwingTools.showDialog(configureDialog(new ContinueOrExitDialog()), true);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   dialog  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    private ContinueOrExitDialog configureDialog(final ContinueOrExitDialog dialog) throws Exception {
        final String confAttrPrefix = getConfAttrPrefix();

        final String title = SessionManager.getProxy()
                    .getConfigAttr(SessionManager.getSession().getUser(),
                        String.format("%s.%s", confAttrPrefix, "title"),
                        getConnectionContext());
        final String text = SessionManager.getProxy()
                    .getConfigAttr(SessionManager.getSession().getUser(),
                        String.format("%s.%s", confAttrPrefix, "text"),
                        getConnectionContext());
        final String html = SessionManager.getProxy()
                    .getConfigAttr(SessionManager.getSession().getUser(),
                        String.format("%s.%s", confAttrPrefix, "html"),
                        getConnectionContext());
        final String continueButtonText = SessionManager.getProxy()
                    .getConfigAttr(SessionManager.getSession().getUser(),
                        String.format("%s.%s", confAttrPrefix, "continueButtonText"),
                        getConnectionContext());
        final String exitButtonText = SessionManager.getProxy()
                    .getConfigAttr(SessionManager.getSession().getUser(),
                        String.format("%s.%s", confAttrPrefix, "exitButtonText"),
                        getConnectionContext());

        final String content = (text != null)
            ? text
            : ((html != null)
                ? String.format(
                    "<html><head><style>body { font-family: 'Arial', sans-serif; }</style></head><body>%s</body></html>",
                    html) : null);

        dialog.setContentTitle(title);
        dialog.setContent(content);
        dialog.setContinueButtonText(continueButtonText);
        dialog.setExitButtonText(exitButtonText);

        // avoiding to have a non closable modal dialog when wrongly configured
        if (continueButtonText == null && exitButtonText == null) {            
            dialog.setExitButtonText("exit");
            dialog.setContinueButtonText("continue");
        }
        
        return dialog;
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private static final class LazyInitialiser {

        //~ Static fields/initializers -----------------------------------------

        private static final ContinueOrExitHandler INSTANCE = new ContinueOrExitHandler();

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new LazyInitialiser object.
         */
        private LazyInitialiser() {
        }
    }
}
