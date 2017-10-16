/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.servermessage;

import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.exception.ConnectionException;

import org.jdom.Attribute;
import org.jdom.Element;

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
import de.cismet.cids.server.connectioncontext.ClientConnectionContext;
import de.cismet.cids.server.connectioncontext.ClientConnectionContextProvider;
import de.cismet.cids.server.messages.CidsServerMessage;

import de.cismet.tools.configuration.Configurable;
import de.cismet.tools.configuration.NoWriteError;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class CidsServerMessageNotifier implements Configurable, ClientConnectionContextProvider {

    //~ Static fields/initializers ---------------------------------------------

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(
            CidsServerMessageNotifier.class);

    private static final String CONFIGURATION = "CidsServerMessages";
    private static final String CONF_ELEM_INTERVALL = "intervall";
    private static final String CONF_ELEM_LAST_MESSAGE_IDS = "last_message_ids";
    private static final String CONF_ELEM_LAST_MESSAGE_ID = "last_message_id";
    private static final String CONF_ATTR_LAST_MESSAGE_ID_CATEGORY = "category";

    private static CidsServerMessageNotifier INSTANCE = null;

    public static final int DEFAULT_SCHEDULE_INTERVAL = 10000;

    //~ Instance fields --------------------------------------------------------

    private final Map<String, Integer> lastMessageIds = new HashMap<String, Integer>();

    private final Timer timer = new Timer();
    private boolean running;
    private final Map<String, Collection> subscribers = new HashMap<String, Collection>();
    private int scheduleIntervall = DEFAULT_SCHEDULE_INTERVAL;

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
        try {
            if (SessionManager.getSession().getConnection().hasConfigAttr(
                            SessionManager.getSession().getUser(),
                            "csa://"
                            + CheckCidsServerMessageAction.TASK_NAME,
                            getClientConnectionContext())) {
                synchronized (timer) {
                    if (!running) {
                        startTimer(true);
                    }
                }
            }
        } catch (ConnectionException ex) {
            LOG.info("error checking csa://"
                        + CheckCidsServerMessageAction.TASK_NAME + ". no cids server messages",
                ex);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  scheduleIntervall  DOCUMENT ME!
     */
    public void setScheduleIntervall(final int scheduleIntervall) {
        this.scheduleIntervall = scheduleIntervall;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getScheduleIntervall() {
        return scheduleIntervall;
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
            new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            listener.messageRetrieved(
                                new CidsServerMessageNotifierListenerEvent(CidsServerMessageNotifier.this, message));
                        } catch (final Exception ex) {
                            LOG.warn("error while invoking listener.messageRetrieved(...)", ex);
                        }
                    }
                }).start();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  firstStart  DOCUMENT ME!
     */
    private void startTimer(final boolean firstStart) {
        running = true;
        synchronized (timer) {
            timer.schedule(new RetrieveTimerTask(), firstStart ? 1000 : getScheduleIntervall());
        }
    }

    @Override
    public void configure(final Element parent) {
        try {
            String elementIntervall = "";
            if (parent != null) {
                final Element conf = parent.getChild(CONFIGURATION);
                if (conf != null) {
                    elementIntervall = conf.getChildText(CONF_ELEM_INTERVALL);

                    lastMessageIds.clear();
                    for (final Object child : conf.getChild(CONF_ELEM_LAST_MESSAGE_IDS).getChildren()) {
                        final Element lastMessageIdElement = (Element)child;
                        final String category = lastMessageIdElement.getAttributeValue(
                                CONF_ATTR_LAST_MESSAGE_ID_CATEGORY);
                        final Integer lastMessageId = Integer.valueOf(lastMessageIdElement.getText());
                        lastMessageIds.put(category, lastMessageId);
                    }
                }
            }
            setScheduleIntervall(Integer.valueOf(elementIntervall));
        } catch (final Exception ex) {
            LOG.warn("Fehler beim Konfigurieren des CidsServerMessagesNotifier", ex);
        }
    }

    @Override
    public Element getConfiguration() throws NoWriteError {
        final Element conf = new Element(CONFIGURATION);

        final Element intervallElement = new Element(CONF_ELEM_INTERVALL);
        intervallElement.addContent(Long.toString(getScheduleIntervall()));
        conf.addContent(intervallElement);

        final Element lastMessageIdsElement = new Element(CONF_ELEM_LAST_MESSAGE_IDS);
        for (final String category : lastMessageIds.keySet()) {
            final Integer lastMessageId = lastMessageIds.get(category);
            if (lastMessageId != null) {
                final Element lastMessageIdElement = new Element(CONF_ELEM_LAST_MESSAGE_ID);
                lastMessageIdElement.setAttribute(new Attribute(CONF_ATTR_LAST_MESSAGE_ID_CATEGORY, category));
                lastMessageIdElement.setText(Integer.toString(lastMessageId));
                lastMessageIdsElement.addContent(lastMessageIdElement);
            }
        }
        conf.addContent(lastMessageIdsElement);

        return conf;
    }

    @Override
    public void masterConfigure(final Element parent) {
        configure(parent);
    }

    @Override
    public ClientConnectionContext getClientConnectionContext() {
        return ClientConnectionContext.create(getClass().getSimpleName());
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    class RetrieveTimerTask extends TimerTask {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new RetrieveTimerTask object.
         */
        public RetrieveTimerTask() {
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public void run() {
            boolean errorWhileCheck = false;
            try {
                if (SessionManager.getSession().getConnection().hasConfigAttr(
                                SessionManager.getSession().getUser(),
                                "csa://"
                                + CheckCidsServerMessageAction.TASK_NAME,
                                getClientConnectionContext())) {
                    final ServerActionParameter<Map> lastMessageIdParam = new ServerActionParameter<Map>(
                            CheckCidsServerMessageAction.Parameter.LAST_MESSAGE_IDS.toString(),
                            lastMessageIds);
                    final ServerActionParameter<Integer> intervallParam = new ServerActionParameter<Integer>(
                            CheckCidsServerMessageAction.Parameter.INTERVALL.toString(),
                            scheduleIntervall);
                    final Object ret = SessionManager.getSession()
                                .getConnection()
                                .executeTask(SessionManager.getSession().getUser(),
                                    CheckCidsServerMessageAction.TASK_NAME,
                                    SessionManager.getSession().getUser().getDomain(),
                                    ClientConnectionContext.create(CidsServerMessage.class.getSimpleName()),
                                    (Object)null,
                                    lastMessageIdParam,
                                    intervallParam);

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
                            final String category = cidsServerMessage.getCategory();
                            final Integer lastMessageId = lastMessageIds.get(category);
                            if ((lastMessageId == null) || (messageId > lastMessageId)) {
                                lastMessageIds.put(category, messageId);
                            }
                            publish(cidsServerMessage, cidsServerMessage.getCategory());
                        }
                    }
                }
            } catch (final Exception ex) {
                LOG.error("error while checking message. abort retrieving new messages.", ex);
                errorWhileCheck = true;
            } finally {
                if (!errorWhileCheck) {
                    synchronized (timer) {
                        startTimer(false);
                    }
                }
            }
        }
    }
}
