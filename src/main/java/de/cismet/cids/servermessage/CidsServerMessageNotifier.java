/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.servermessage;

import Sirius.navigator.connection.SessionManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import de.cismet.cids.server.actions.CheckCidsServerMessageAction;
import de.cismet.cids.server.actions.ServerActionParameter;
import de.cismet.cids.server.messages.CidsServerMessage;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class CidsServerMessageNotifier {

    //~ Static fields/initializers ---------------------------------------------

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(
            CidsServerMessageNotifier.class);

    private static CidsServerMessageNotifier INSTANCE = null;

    private static final int SCHEDULE_INTERVAL = 5000;

    //~ Instance fields --------------------------------------------------------

    private final Timer timer = new Timer();
    private final int lastMessageId = -1;
    private boolean running;
    private final Map<String, Collection> subscribers = new HashMap<String, Collection>();

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new CidsServerMessageNotifier object.
     */
    private CidsServerMessageNotifier() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static CidsServerMessageNotifier getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CidsServerMessageNotifier();
        }
        return INSTANCE;
    }
    /**
     * DOCUMENT ME!
     */
    public void start() {
        synchronized (timer) {
            if (!running) {
                startTimer(lastMessageId, SCHEDULE_INTERVAL);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  listener  DOCUMENT ME!
     * @param  category  DOCUMENT ME!
     */
    public void subscribe(final CidsServerMessageNotifierListener listener, final String category) {
        if (!subscribers.containsKey(category)) {
            subscribers.put(category, new LinkedList<CidsServerMessageNotifierListener>());
        }
        subscribers.get(category).add(listener);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  listener  DOCUMENT ME!
     * @param  category  DOCUMENT ME!
     */
    public void unsubscribe(final CidsServerMessageNotifierListener listener, final String category) {
        if (subscribers.containsKey(category)) {
            subscribers.get(category).remove(listener);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  message   DOCUMENT ME!
     * @param  category  DOCUMENT ME!
     */
    private void publish(final CidsServerMessage message, final String category) {
        final Collection<CidsServerMessageNotifierListener> listeners = subscribers.containsKey(category)
            ? new ArrayList(subscribers.get(category)) : new ArrayList();

        // add allcat (cat == null) listeners ?
        if ((category != null) && (subscribers.containsKey(null))) {
            listeners.addAll(subscribers.get(null));
        }

        for (final CidsServerMessageNotifierListener listener : listeners) {
            listener.messageRetrieved(new CidsServerMessageNotifierListenerEvent(this, message));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  lastMessageId  DOCUMENT ME!
     * @param  scheduleMs     DOCUMENT ME!
     */
    private void startTimer(final int lastMessageId, final int scheduleMs) {
        running = true;
        synchronized (timer) {
            timer.schedule(new RetrieveTimerTask(lastMessageId, scheduleMs), scheduleMs);
        }
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    class RetrieveTimerTask extends TimerTask {

        //~ Instance fields ----------------------------------------------------

        private final int lastMessageId;
        private final int intervall;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new RetrieveTimerTask object.
         *
         * @param  lastMessageId  DOCUMENT ME!
         * @param  intervall      DOCUMENT ME!
         */
        public RetrieveTimerTask(final int lastMessageId, final int intervall) {
            this.lastMessageId = lastMessageId;
            this.intervall = intervall;
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public void run() {
            int newLastMesageId = lastMessageId;
            try {
                final ServerActionParameter<Integer> lastMessageIdParam = new ServerActionParameter<Integer>(
                        CheckCidsServerMessageAction.ParameterType.LAST_MESSAGE_ID.toString(),
                        lastMessageId);
                if (SessionManager.getSession().getConnection().hasConfigAttr(
                                SessionManager.getSession().getUser(),
                                "csa://"
                                + CheckCidsServerMessageAction.TASK_NAME)) {
                    final Object ret = SessionManager.getSession()
                                .getConnection()
                                .executeTask(
                                    SessionManager.getSession().getUser(),
                                    CheckCidsServerMessageAction.TASK_NAME,
                                    SessionManager.getSession().getUser().getDomain(),
                                    null,
                                    lastMessageIdParam);

                    if (ret instanceof List) {
                        final List<CidsServerMessage> cidsServerMessages = (List<CidsServerMessage>)ret;

                        Collections.sort(cidsServerMessages, new Comparator<CidsServerMessage>() {

                                @Override
                                public int compare(final CidsServerMessage o1, final CidsServerMessage o2) {
                                    final Integer id1 = (o1 == null) ? null : o1.getId();
                                    final Integer id2 = (o2 == null) ? null : o2.getId();

                                    if ((id1 == null) && (id2 == null)) {
                                        return 0;
                                    } else if (id2 == null) {
                                        return 1;
                                    } else if (id1 == null) {
                                        return -1;
                                    } else {
                                        return Integer.compare(id1, id2);
                                    }
                                }
                            });

                        for (final CidsServerMessage cidsServerMessage : cidsServerMessages) {
                            final int messageId = cidsServerMessage.getId();
                            if (messageId > newLastMesageId) {
                                newLastMesageId = messageId;
                            }
                            publish(cidsServerMessage, cidsServerMessage.getCategory());
                        }
                    }
                }
            } catch (final Exception ex) {
                LOG.fatal(ex, ex);
            } finally {
                synchronized (timer) {
                    startTimer(newLastMesageId, intervall);
                }
            }
        }
    }
}
