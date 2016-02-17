/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.cidsservermessages;

import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.exception.ConnectionException;

import org.openide.util.Exceptions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

import de.cismet.cids.navigator.utils.CidsClientToolbarItem;

import de.cismet.cids.server.actions.PublishCidsServerMessageAction;
import de.cismet.cids.server.actions.ServerActionParameter;

/**
 * DOCUMENT ME!
 *
 * @author   jweintraut
 * @version  $Revision$, $Date$
 */
@org.openide.util.lookup.ServiceProvider(service = CidsClientToolbarItem.class)
public class AddCidsServerMessageAction extends AbstractAction implements CidsClientToolbarItem {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new NavigatorDownloadManagerAction object.
     */
    public AddCidsServerMessageAction() {
        putValue(Action.NAME, "CSM");
        putValue(Action.SHORT_DESCRIPTION, "AddCidsServerMessageAction");
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public String getSorterString() {
        return "100";
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        final String message = JOptionPane.showInputDialog("Message ?");

        if (message != null) {
            try {
//                SessionManager.getSession()
//                        .getConnection()
//                        .sendMessage(SessionManager.getSession().getUser(),
//                            SessionManager.getSession().getUser().getDomain(),
//                            "FooCat",
//                            message,
//                            null,
//                            null);
                final ServerActionParameter<String> messageParam = new ServerActionParameter<String>(
                        PublishCidsServerMessageAction.ParameterType.MESSAGE.toString(),
                        message);
                final ServerActionParameter<String> categoryParam = new ServerActionParameter<String>(
                        PublishCidsServerMessageAction.ParameterType.CATEGORY.toString(),
                        "FooCat");
                SessionManager.getSession()
                        .getConnection()
                        .executeTask(
                            SessionManager.getSession().getUser(),
                            PublishCidsServerMessageAction.TASK_NAME,
                            SessionManager.getSession().getUser().getDomain(),
                            null,
                            messageParam,
                            categoryParam);
//            } catch (ConnectionException ex) {
//                Exceptions.printStackTrace(ex);
//            }
            } catch (final ConnectionException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }
}
