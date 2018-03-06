/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cidsx.client.connection;

import Sirius.navigator.connection.RESTfulConnection;
import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.exception.ConnectionException;

import Sirius.server.localserver.attribute.ClassAttribute;
import Sirius.server.localserver.attribute.MemberAttributeInfo;
import Sirius.server.middleware.types.LightweightMetaObject;
import Sirius.server.middleware.types.MetaClass;
import Sirius.server.middleware.types.MetaClassNode;
import Sirius.server.middleware.types.MetaNode;
import Sirius.server.middleware.types.MetaObject;
import Sirius.server.middleware.types.MetaObjectNode;
import Sirius.server.middleware.types.Node;

import Sirius.util.image.ImageHashMap;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.openide.util.Lookup;

import java.awt.GraphicsEnvironment;

import java.net.URI;
import java.net.URL;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.Icon;

import javax.ws.rs.core.UriBuilder;

import de.cismet.cids.client.tools.DevelopmentTools;

import de.cismet.cids.dynamics.CidsBean;
import de.cismet.cids.dynamics.CidsBeanInfo;

import de.cismet.cids.server.CallServerService;
import de.cismet.cids.server.actions.DefaultScheduledServerActionTestImpl;
import de.cismet.cids.server.actions.ServerActionParameter;
import de.cismet.cids.server.ws.SSLConfig;
import de.cismet.cids.server.ws.SSLConfigProvider;
import de.cismet.cids.server.ws.rest.RESTfulSerialInterfaceConnector;

import de.cismet.cidsx.server.api.types.ActionTask;

import de.cismet.commons.security.AccessHandler;

import de.cismet.connectioncontext.ClientConnectionContext;
import de.cismet.connectioncontext.ConnectionContextProvider;

import de.cismet.netutil.Proxy;

import de.cismet.reconnector.Reconnector;

/**
 * The PureRESTfulConnection allows the cids navigator to use the new cids Pure REST API while providing backwards
 * compatibility with the old Connection interface.
 *
 * <p>This class extends the 'java-objects-over-http' RESTfulConnection and internally uses a new CallServerService
 * Implementation (PureRESTfulReconnector and RESTfulInterfaceConnector, respectively) that connects to the cids server
 * Pure REST API.</p>
 *
 * @author   Pascal DihÃ© <pascal.dihe@cismet.de>
 * @version  1.0 2015/04/17
 */
public class PureRESTfulConnection extends RESTfulConnection {

    //~ Instance fields --------------------------------------------------------

    /** FIXME: legacyConnector is used for operations that are currently not implemented by cids REST service */
    private transient CallServerService legacyConnector;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new PureRESTfulConnection object.
     */
    public PureRESTfulConnection() {
        super();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   restServerURL       DOCUMENT ME!
     * @param   proxy               DOCUMENT ME!
     * @param   compressionEnabled  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    private CallServerService createLegacyConnector(final String restServerURL,
            final Proxy proxy,
            final boolean compressionEnabled) throws ConnectionException {
        final SSLConfigProvider sslConfigProvider = Lookup.getDefault().lookup(SSLConfigProvider.class);
        final SSLConfig sslConfig = (sslConfigProvider == null) ? null : sslConfigProvider.getSSLConfig();

        try {
            final UriBuilder uriBuilder = UriBuilder.fromUri(restServerURL);
            if (LOG.isDebugEnabled()) {
                // FIXME:
                LOG.debug("building legacy callServerURL from restServerURL '" + restServerURL
                            + "', assuming default values for path (callserver/binary) and port (9986)");
            }
            final URI callServerURI = uriBuilder.port(9986).replacePath("callserver/binary").build();
            LOG.warn("creating additional legacy connection to service '" + callServerURI + "'");
            return new RESTfulSerialInterfaceConnector(callServerURI.toString(), proxy, sslConfig, compressionEnabled);
        } catch (Exception ex) {
            final String message = "could n ot build legacy callServerURL from restServerURL '" + restServerURL + "': "
                        + ex.getMessage();
            LOG.error(message, ex);
            throw new ConnectionException(message, ex);
        }
    }

    /**
     * Overridden to return a PureRESTfulReconnector that internally uses a RESTfulInterfaceConnector to interact with
     * the cids server Pure REST API.
     *
     * @param   callserverURL       DOCUMENT ME!
     * @param   proxy               DOCUMENT ME!
     * @param   compressionEnabled  DOCUMENT ME!
     *
     * @return  PureRESTfulReconnector
     */
    @Override
    protected Reconnector<CallServerService> createReconnector(final String callserverURL,
            final Proxy proxy,
            final boolean compressionEnabled) {
        reconnector = new PureRESTfulReconnector(CallServerService.class, callserverURL, proxy);
        reconnector.useDialog(!GraphicsEnvironment.getLocalGraphicsEnvironment().isHeadlessInstance(), null);
        return reconnector;
    }

    @Override
    public boolean connect(final String callserverURL, final Proxy proxy) throws ConnectionException {
        return connect(callserverURL, proxy, false);
    }

    @Override
    public boolean connect(final String callserverURL, final Proxy proxy, final boolean compressionEnabled)
            throws ConnectionException {
        this.connector = createReconnector(callserverURL, proxy, compressionEnabled).getProxy();

        // FIXME: remove when all methods implemented in pure RESTful Service
        this.legacyConnector = createLegacyConnector(callserverURL, proxy, compressionEnabled);

        try {
            this.getDomains(getConnectionContext());
        } catch (final Exception e) {
            final String message = "Could not connect cids PURE REST Service at '" + callserverURL + "' (proxy: "
                        + proxy + ")"; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }

        return true;
    }

    /**
     * FIXME: Operation currently delegated to legacy REST Connection! Implement in pure RESTful Service
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    @Override
    public ImageHashMap getDefaultIcons() throws ConnectionException {
        try {
            LOG.warn("delegating getDefaultIcons() to legacy REST Connection");
            return new ImageHashMap(this.legacyConnector.getDefaultIcons());
        } catch (final Exception e) {
            final String message = "cannot get default icons from legacy REST Server"; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    /**
     * FIXME: Operation currently delegated to legacy REST Connection! Implement in pure RESTful Service
     *
     * @param   name  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    @Override
    public Icon getDefaultIcon(final String name) throws ConnectionException {
        try {
            LOG.warn("delegating getDefaultIcon(" + name + ") to legacy REST Connection");
            return getDefaultIcons().get(name);
        } catch (final Exception e) {
            final String message = "cannot get default icon with name '" + name + "' from from legacy REST Server"; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  args  DOCUMENT ME!
     */
    public static void main(final String[] args) {
        try {
            // TEST GET ALL CLASSES --------------------------------------------
// DevelopmentTools.initSessionManagerFromPureRestfulConnectionOnLocalhost("SWITCHON",
// "Administratoren", "admin", "cismet");
//
// final ConnectionProxy connectionProxy = SessionManager.getProxy();
// final MetaClass[] metaClasses = connectionProxy.getClasses();
// for(final MetaClass metaClass:metaClasses) {
// System.out.println(metaClass.getKey() + ":" + metaClass.getTableName());
// }

            // final Class listClass = ClassUtils.getClass("java.util.List<java.util.List>");
            // System.out.println(listCla&ss);
            // System.exit(0);

            final ObjectMapper MAPPER = new ObjectMapper(new JsonFactory());
//          System.out.println(MAPPER.writeValueAsString("keyword:\"soil\" limit:\"5\""));
//          //System.out.println(MAPPER.writer().writeValueAsString("keyword:\"soil\" limit:\"5\""));

// MAPPER.writeValue(Writer,Object) with StringWriter and constructing String, but more efficient.

//            System.out.println(MAPPER.writeValueAsString("true"));
//            System.out.println(MAPPER.writeValueAsString(new Date()));
//            System.out.println(MAPPER.writer().writeValueAsString(new Date()));
//            Date date = (Date)MAPPER.convertValue(new Date(), Date.class);
//            System.out.println(date);

//            SearchParameter searchParameter = new SearchParameter("type", Byte.parseByte("123", 8));
//            System.out.println(MAPPER.writer().writeValueAsString(searchParameter));
//            SearchParameter[] searchParameters
//                    = (SearchParameter[])MAPPER.convertValue(new SearchParameter[]{searchParameter,searchParameter}, SearchParameter[].class);
//            System.out.println(MAPPER.writer().writeValueAsString(new SearchParameter[]{searchParameter}));
//            System.out.println(searchParameters[1].getValue().getClass());
//            System.out.println(searchParameters[1].getValue());
//
//            JsonNode node = MAPPER.valueToTree(searchParameter);
//
//            System.exit(0);

            final String domain = "SWITCHON";
            final int metaObjectId = 76;
            final int metaClassId = 4;
            final String[] representationFields = new String[] { "organisation", "email", "name", "role" };
            final String representationPattern = "%0$2s";

            // Test Server Actions ---------------------------------------------

            DevelopmentTools.initSessionManagerFromPureRestfulConnectionOnLocalhost(
                "SWITCHON",
                "Administratoren",
                "admin",
                "cismet");

            // fails with: java.lang.ClassCastException: [B cannot be cast to java.lang.String Object taskResult =
            // SessionManager.getProxy().executeTask("downloadFile", "SWITCHON", "C:\\pagefile.sys");

            final ActionTask actionTask = new ActionTask();
            actionTask.setActionKey("httpTunnelAction");
            final Map actionParameters = new HashMap<String, Object>();
            actionParameters.put("URL", new URL("http://www.cismet.de"));
            actionParameters.put("METHOD", AccessHandler.ACCESS_METHODS.GET_REQUEST);
            actionTask.setParameters(actionParameters);

            final ServerActionParameter actionParameterUrl = new ServerActionParameter(
                    "URL",
                    new URL("http://www.cismet.de"));
            final ServerActionParameter actionParameterMethod = new ServerActionParameter(
                    "METHOD",
                    AccessHandler.ACCESS_METHODS.GET_REQUEST);
            final ServerActionParameter actionParameterTest = new ServerActionParameter(
                    DefaultScheduledServerActionTestImpl.TESTPARAM,
                    "TEST STRING!!!!!!!");

            // fails with: Caused by: java.lang.ClassCastException: java.lang.String cannot be cast to java.net.URL
// Object taskResult = SessionManager.getProxy().executeTask(
// "httpTunnelAction", "SWITCHON", null, actionParameterUrl, actionParameterMethod);

            final Object taskResult = SessionManager.getProxy()
                        .executeTask(
                            "testAction",
                            "SWITCHON",
                            new PureRESTfulConnection().getConnectionContext(),
                            (Object)null,
                            actionParameterTest);
            System.out.println(taskResult);
            System.exit(0);

            // TEST Search -----------------------------------------------------

// DevelopmentTools.initSessionManagerFromPureRestfulConnectionOnLocalhost(
// "SWITCHON",
// "Administratoren",
// "admin",
// "cismet");
//
// final MetaObjectUniversalSearchStatement searchStatement = new MetaObjectUniversalSearchStatement();
// searchStatement.setQuery("keyword:\"soil\" limit:\"5\"");
//
// SearchParameter searchParameter = new SearchParameter();
// searchParameter.setKey("query");
// searchParameter.setValue("keyword:\"soil\" limit:\"5\"");
// System.out.println(MAPPER.writeValueAsString(searchParameter));
// System.out.println(((SearchParameter)MAPPER.readValue(
// MAPPER.writeValueAsString(searchParameter), SearchParameter.class)).getValue());
//
// final Collection nodeCollection = SessionManager.getProxy().customServerSearch(searchStatement);
// final Node[] nodesArray = (Node[])nodeCollection.toArray(new Node[nodeCollection.size()]);
//
// for (int i = 0; i < nodesArray.length; i++) {
// System.out.println("nodes[" + i + "].getName():\t'"
// + nodesArray[i].getName() + "'");
//
// System.out.println("nodes[" + i + "].toString():\t'"
// + nodesArray[i].toString() + "'");
//
// System.out.println("nodes[" + i + "].getArtificialId():\t'"
// + nodesArray[i].getArtificialId() + "'");
//
// System.out.println("nodes[" + i + "].getDescription():\t'"
// + nodesArray[i].getDescription() + "'");
//
// System.out.println("nodes[" + i + "].getDomain():\t'"
// + nodesArray[i].getDomain() + "'");
//
// System.out.println("nodes[" + i + "].getDynamicChildrenStatement():\t'"
// + nodesArray[i].getDynamicChildrenStatement() + "'");
//
// System.out.println("nodes[" + i + "].getGroup():\t'"
// + nodesArray[i].getGroup() + "'");
//
// System.out.println("nodes[" + i + "].getIconString():\t'"
// + nodesArray[i].getIconString() + "'");
//
// System.out.println("nodes[" + i + "].getClassId():\t'"
// + nodesArray[i].getClassId() + "'");
//
// System.out.println("nodes[" + i + "].getIconFactory():\t'"
// + nodesArray[i].getIconFactory() + "'");
//
// System.out.println("nodes[" + i + "].getClass().getName():\t'"
// + nodesArray[i].getClass().getName() + "'");
//
// System.out.println("nodes[" + i + "].getId():\t'"
// + nodesArray[i].getId() + "'");
//
// System.out.println("nodes[" + i + "].getPermissions():\t'"
// + nodesArray[i].getPermissions() + "'");
//
// System.out.println("nodes[" + i + "].isDerivePermissionsFromClass():\t'"
// + nodesArray[i].isDerivePermissionsFromClass() + "'");
//
// System.out.println("nodes[" + i + "].isDynamic():\t'"
// + nodesArray[i].isDynamic() + "'");
//
// System.out.println("nodes[" + i + "].isLeaf():\t'"
// + nodesArray[i].isLeaf() + "'");
//
// System.out.println("nodes[" + i + "].hashCode():\t'"
// + nodesArray[i].hashCode() + "'");
//
// System.out.println("nodes[" + i + "].isValid():\t'"
// + nodesArray[i].isValid() + "'");
//
// System.out.println("nodes[" + i + "].isSqlSort():\t'"
// + nodesArray[i].isSqlSort() + "'");
//
// System.out.println("nodes[" + i + "].isMetaObjectNode:\t'"
// + MetaObjectNode.class.isAssignableFrom(nodesArray[i].getClass()));
//
// if (MetaObjectNode.class.isAssignableFrom(nodesArray[i].getClass())) {
// final MetaObjectNode metaObjectNode = (MetaObjectNode)nodesArray[i];
//
// System.out.println("nodes[" + i + "].getObjectId():\t'"
// + metaObjectNode.getObjectId() + "'");
//
// System.out.println("nodes[" + i + "].hasMetaObject:\t'"
// + (metaObjectNode.getObject() != null) + "'");
// }
//
// System.out.println("nodes[" + i + "].isMetaClassNode:\t'"
// + MetaClassNode.class.isAssignableFrom(nodesArray[i].getClass()) + "'");
//
// System.out.println("nodes[" + i + "].isMetaNode:\t'"
// + MetaNode.class.isAssignableFrom(nodesArray[i].getClass()) + "'");
// }
//
// System.exit(0);

            // TEST getAllLightweightMetaObjectsForClass .......................
            // TEST getAllLightweightMetaObjectsbyQuery. .......................

            final String lwmoQuery = "SELECT * from relationship WHERE toresource = 8537";
            final int relationshipClassId = 18;
            final String[] lwmoRepresentationFields = new String[] { "name", "description", "type" };
            final String lwmoRepresentationPattern = "%0$1s";

            DevelopmentTools.initSessionManagerFromRestfulConnectionOnLocalhost(
                "SWITCHON",
                "Administratoren",
                "admin",
                "cismet");

//            final LightweightMetaObject[] lmoBinary = (LightweightMetaObject[])SessionManager.getProxy()
//                        .getAllLightweightMetaObjectsForClass(
//                                metaClassId,
//                                SessionManager.getSession().getUser(),
//                                representationFields,
//                                representationPattern);

            final LightweightMetaObject[] lmoBinary = (LightweightMetaObject[])SessionManager.getProxy()
                        .getLightweightMetaObjectsByQuery(
                                relationshipClassId,
                                SessionManager.getSession().getUser(),
                                lwmoQuery,
                                lwmoRepresentationFields,
                                lwmoRepresentationPattern,
                                ClientConnectionContext.createDeprecated());

            DevelopmentTools.initSessionManagerFromPureRestfulConnectionOnLocalhost(
                "SWITCHON",
                "Administratoren",
                "admin",
                "cismet");

//            final LightweightMetaObject[] lmoRest = (LightweightMetaObject[]) SessionManager.getProxy()
//                    .getAllLightweightMetaObjectsForClass(
//                            metaClassId,
//                            SessionManager.getSession().getUser(),
//                            representationFields,
//                            representationPattern);

            final LightweightMetaObject[] lmoRest = (LightweightMetaObject[])SessionManager.getProxy()
                        .getLightweightMetaObjectsByQuery(
                                relationshipClassId,
                                SessionManager.getSession().getUser(),
                                lwmoQuery,
                                lwmoRepresentationFields,
                                lwmoRepresentationPattern,
                                ClientConnectionContext.createDeprecated());

            if (lmoBinary.length != lmoRest.length) {
                throw new Exception("lmoBinary.length != lmoRest.length");
            }

            // legacy getLightweightMetaObjects returns  meta objects ordered descending
            // by id!
            final LinkedHashMap<Integer, LightweightMetaObject[]> orderedLmos =
                new LinkedHashMap<Integer, LightweightMetaObject[]>();

            for (int i = 0; i < lmoBinary.length; i++) {
                LightweightMetaObject[] lightweightMetaObjects;
                if (orderedLmos.containsKey(lmoBinary[i].getID())) {
                    lightweightMetaObjects = orderedLmos.get(lmoBinary[i].getID());
                    lightweightMetaObjects[0] = lmoBinary[i];
                } else {
                    lightweightMetaObjects = new LightweightMetaObject[2];
                    lightweightMetaObjects[0] = lmoBinary[i];
                    orderedLmos.put(lmoBinary[i].getID(), lightweightMetaObjects);
                }

                if (orderedLmos.containsKey(lmoRest[i].getID())) {
                    lightweightMetaObjects = orderedLmos.get(lmoRest[i].getID());
                    lightweightMetaObjects[1] = lmoRest[i];
                } else {
                    lightweightMetaObjects = new LightweightMetaObject[2];
                    lightweightMetaObjects[1] = lmoRest[i];
                    orderedLmos.put(lmoRest[i].getID(), lightweightMetaObjects);
                }
            }
            final Iterator<LightweightMetaObject[]> lmoIterator = orderedLmos.values().iterator();
            final int lmoArrayLength = (orderedLmos.size() > 5) ? 5 : orderedLmos.size();
            int j = 0;
            while (lmoIterator.hasNext() && (j < lmoArrayLength)) {
                final LightweightMetaObject[] lightweightMetaObjects = lmoIterator.next();

                System.out.println("LMO[" + j + "].toString()\t'"
                            + lightweightMetaObjects[0].toString() + "' == '"
                            + lightweightMetaObjects[1].toString() + "'");
                System.out.println("LMO[" + j + "].hashCode()\t'"
                            + lightweightMetaObjects[0].hashCode() + "' == '"
                            + lightweightMetaObjects[1].hashCode() + "'");
                System.out.println("LMO[" + j + "].getLWAttributes\t'"
                            + lightweightMetaObjects[0].getKnownAttributeNames().size() + "' == '"
                            + lightweightMetaObjects[1].getKnownAttributeNames().size() + "'");
//                System.out.println("LMO[" + j + "].getLWAttribute(\"email\")\t'"
//                            + lightweightMetaObjects[0].getLWAttribute("email") + "' == '"
//                            + lightweightMetaObjects[1].getLWAttribute("email") + "'");
//                System.out.println("LMO[" + j + "].getLWAttribute(\"organisation\")\t'"
//                            + lightweightMetaObjects[0].getLWAttribute("organisation") + "' == '"
//                            + lightweightMetaObjects[1].getLWAttribute("organisation") + "'");
//                System.out.println("LMO[" + j + "].getLWAttribute(\"role\")\t'"
//                            + lightweightMetaObjects[0].getLWAttribute("role") + "' == '"
//                            + lightweightMetaObjects[1].getLWAttribute("role") + "'");
                System.out.println("LMO[" + j + "].getLWAttribute(\"name\")\t'"
                            + lightweightMetaObjects[0].getLWAttribute("name") + "' == '"
                            + lightweightMetaObjects[1].getLWAttribute("name") + "'");
                System.out.println("LMO[" + j + "].getLWAttribute(\"description\")\t'"
                            + lightweightMetaObjects[0].getLWAttribute("description") + "' == '"
                            + lightweightMetaObjects[1].getLWAttribute("description") + "'");
                System.out.println("LMO[" + j + "].getLWAttribute(\"type\")\t'"
                            + lightweightMetaObjects[0].getLWAttribute("type") + "' == '"
                            + lightweightMetaObjects[1].getLWAttribute("type") + "'");
                System.out.println("LMO[" + j + "].getRealMetaObject().hashCode()\t'"
                            + lightweightMetaObjects[0].getRealMetaObject().hashCode() + "' == '"
                            + lightweightMetaObjects[1].getRealMetaObject().hashCode() + "'");
                System.out.println("LMO[" + j + "].getRealMetaObject().getID()\t'"
                            + lightweightMetaObjects[0].getRealMetaObject().getID() + "' == '"
                            + lightweightMetaObjects[1].getRealMetaObject().getID() + "'");
//                System.out.println("LMO[" + j + "].getAttributeByFieldName(\"role\").getValue()\t'"
//                            + lightweightMetaObjects[0].getRealMetaObject().getAttributeByFieldName("role").getValue()
//                            + "' == '"
//                            + lightweightMetaObjects[1].getRealMetaObject().getAttributeByFieldName("role").getValue()
//                            + "'");

                final CidsBean lmoBeanBinary = lightweightMetaObjects[0].getRealMetaObject().getBean();
                final CidsBean lmoBeanRest = lightweightMetaObjects[1].getRealMetaObject().getBean();

                System.out.println("LMOBean[" + j + "].getPropertyNames()()\t'"
                            + lmoBeanBinary.getPropertyNames().length + "' == '"
                            + lmoBeanRest.getPropertyNames().length + "'");

                j++;
            }

            // System.exit(0);

// TEST insertMetaObject ...........................................
// DevelopmentTools.initSessionManagerFromPureRestfulConnectionOnLocalhost(
// "SWITCHON",
// "Administratoren",
// "admin",
// "cismet");
//
// MetaClass metaClass = SessionManager.getProxy().getMetaClass(metaClassId, domain);
// MetaObject newMetaObject = SessionManager.getProxy().getInstance(metaClass);
// CidsBean newCidsBean = newMetaObject.getBean();
// newCidsBean.setProperty("name", "NEW CIDS BEAN");
// newCidsBean.setProperty("organisation", "NEW CIDS BEAN");
// newCidsBean.setProperty("email", "NEW CIDS BEAN");
// newCidsBean.setProperty("url", "NEW CIDS BEAN");
// newCidsBean.setProperty("role", SessionManager.getProxy().getMetaObject(221, 6, domain).getBean());
// newCidsBean.setProperty("description", "NEW CIDS BEAN");
// newMetaObject = SessionManager.getProxy().insertMetaObject(newMetaObject, domain);
// metaObjectId = newMetaObject.getId();

// TEST insertMetaObject ...........................................

            // TEST getMetaObject
            final String moQuery = "SELECT c.id as classId, t.ID as objectId \n"
                        + "FROM tag t, cs_class c, taggroup g\n"
                        + "WHERE t.taggroup = g.id AND g.name ilike '%protocol%' AND c.name = 'tag' LIMIT 1;";

            DevelopmentTools.initSessionManagerFromRestfulConnectionOnLocalhost(
                "SWITCHON",
                "Administratoren",
                "admin",
                "cismet");

            // SessionManager.getProxy().getSearchOptions();
            // SessionManager.getProxy().getClassTreeNodes();
            // MethodMap methods = SessionManager.getProxy().getMethods(SessionManager.getSession().getUser());
// final MetaObject metaObjectBinary = SessionManager.getProxy()
// .getMetaObject(metaObjectId, metaClassId, domain);
            final MetaObject[] metaObjectsBinary = SessionManager.getProxy()
                        .getMetaObjectByQuery(
                            SessionManager.getSession().getUser(),
                            moQuery,
                            new PureRESTfulConnection().getConnectionContext());
            final MetaObject metaObjectBinary = metaObjectsBinary[0];
            final CidsBean cidsBeanBinary = metaObjectBinary.getBean();
            final CidsBeanInfo cidsBeanInfoBinary = cidsBeanBinary.getCidsBeanInfo();
            final String[] propertyNamesBinary = cidsBeanBinary.getPropertyNames();

            DevelopmentTools.initSessionManagerFromPureRestfulConnectionOnLocalhost(
                "SWITCHON",
                "Administratoren",
                "admin",
                "cismet");
//
//            final MetaObject metaObjectRest = SessionManager.getProxy()
//                        .getMetaObject(metaObjectId, metaClassId, domain);

            final MetaObject[] metaObjectRests = SessionManager.getProxy()
                        .getMetaObjectByQuery(
                            SessionManager.getSession().getUser(),
                            moQuery,
                            new PureRESTfulConnection().getConnectionContext());
            final MetaObject metaObjectRest = metaObjectRests[0];
            final CidsBean cidsBeanRest = metaObjectRest.getBean();
            // TEST updateMetaObject ...........................................
            // cidsBeanRest.setProperty("name", cidsBeanRest.getProperty("organisation"));
            // final int result = SessionManager.getProxy().updateMetaObject(metaObjectRest, "SWITCHON");
            // System.out.println("update meta object: " + result);
            // metaObjectRest = SessionManager.getProxy().getMetaObject(76, 4, "SWITCHON");
            // cidsBeanRest = metaObjectRest.getBean();
            // TEST updateMetaObject ...........................................
            final CidsBeanInfo cidsBeanInfoRest = cidsBeanRest.getCidsBeanInfo();
            final String[] propertyNamesRest = cidsBeanRest.getPropertyNames();

            System.out.println("cidsBeanInfoBinary.equals(cidsBeanInfoRest): "
                        + cidsBeanInfoBinary.equals(cidsBeanInfoRest));

            System.out.println("cidsBeanInfo.hashCode():\t'"
                        + cidsBeanInfoBinary.hashCode() + "' == '"
                        + cidsBeanInfoRest.hashCode() + "'");

            System.out.println("cidsBeanInfo.getClassKey():\t'"
                        + cidsBeanInfoBinary.getClassKey() + "' == '"
                        + cidsBeanInfoRest.getClassKey() + "'");

            System.out.println("cidsBeanInfo.getDomainKey():\t'"
                        + cidsBeanInfoBinary.getDomainKey() + "' == '"
                        + cidsBeanInfoRest.getDomainKey() + "'");

            System.out.println("cidsBeanInfo.getJsonObjectKey():\t'"
                        + cidsBeanInfoBinary.getJsonObjectKey() + "' == '"
                        + cidsBeanInfoRest.getJsonObjectKey() + "'");

            System.out.println("cidsBeanInfo.getObjectKey():\t'"
                        + cidsBeanInfoBinary.getObjectKey() + "' == '"
                        + cidsBeanInfoRest.getObjectKey() + "'");

            System.out.println("cidsBeanInfo.toString():\t'"
                        + cidsBeanInfoBinary.toString() + "' == '"
                        + cidsBeanInfoRest.toString() + "'");

            System.out.println("cidsBeanBinary.equals(cidsBeanRest): "
                        + cidsBeanBinary.equals(cidsBeanRest));

            System.out.println("cidsBean.hashCode():\t'"
                        + cidsBeanBinary.hashCode() + "' == '"
                        + cidsBeanRest.hashCode() + "'");

            System.out.println("cidsBean.getPrimaryKeyFieldname():\t'"
                        + cidsBeanBinary.getPrimaryKeyFieldname() + "' == '"
                        + cidsBeanRest.getPrimaryKeyFieldname() + "'");

            System.out.println("cidsBean.getPrimaryKeyValue():\t'"
                        + cidsBeanBinary.getPrimaryKeyValue() + "' == '"
                        + cidsBeanRest.getPrimaryKeyValue() + "'");

            System.out.println("cidsBean.properties\t'"
                        + propertyNamesBinary.length + "' == '"
                        + propertyNamesRest.length + "'");

            if (propertyNamesBinary.length != propertyNamesRest.length) {
                throw new Exception("propertyNamesBinary.length != propertyNamesRest.length");
            }

            for (int i = 0; i < propertyNamesBinary.length; i++) {
                System.out.println("property[" + i + "]." + propertyNamesBinary[i] + "\t'"
                            + cidsBeanBinary.getProperty(propertyNamesBinary[i]) + "' == '"
                            + cidsBeanRest.getProperty(propertyNamesRest[i]) + "'");
            }

            // TEST deleteMetaObject after TEST insertMetaObject ................
            // SessionManager.getProxy().deleteMetaObject(newMetaObject, domain);
            // TEST deleteMetaObject after TEST insertMetaObject ................

            // System.exit(0);

            // TEST & COMPARE NODES  --------------------------------------------

            final String nodeQuery = "SELECT cs_class.id AS classId,\n"
                        + "       taggroup.id AS objectId,\n"
                        + " taggroup.name as name \n"
                        + "FROM taggroup,\n"
                        + "     cs_class\n"
                        + "WHERE cs_class.name = 'taggroup'\n"
                        + "ORDER BY taggroup.name;";

            DevelopmentTools.initSessionManagerFromRestfulConnectionOnLocalhost(
                "SWITCHON",
                "Administratoren",
                "admin",
                "cismet");
            // final Node[] nodesBinary = SessionManager.getProxy().getRoots();
// final Node nodeBinary = SessionManager.getProxy().getNode(7, "SWITCHON");
            // final Node[] nodesBinary = new Node[]{nodeBinary};
// final Node[] nodesBinary = SessionManager.getProxy()
// .getChildren(
// SessionManager.getProxy().getChildren(
// SessionManager.getProxy().getChildren(nodeBinary)[0])[0]);

            final Node[] nodesBinary = SessionManager.getConnection()
                        .getCallServerService()
                        .getMetaObjectNode(SessionManager.getSession().getUser(),
                            nodeQuery,
                            new PureRESTfulConnection().getConnectionContext());

            DevelopmentTools.initSessionManagerFromPureRestfulConnectionOnLocalhost(
                "SWITCHON",
                "Administratoren",
                "admin",
                "cismet");
//            final Node nodeRest = SessionManager.getProxy().getNode(7, "SWITCHON");
            // final Node[] nodesRest = new Node[]{nodeRest};
//            final Node[] nodesRest = SessionManager.getProxy()
//                        .getChildren(
//                            SessionManager.getProxy().getChildren(
//                                SessionManager.getProxy().getChildren(nodeRest)[0])[0]);

            final Node[] nodesRest = SessionManager.getConnection()
                        .getCallServerService()
                        .getMetaObjectNode(SessionManager.getSession().getUser(),
                            nodeQuery,
                            new PureRESTfulConnection().getConnectionContext());

            final int nodesArrayLength = (nodesBinary.length > 3) ? 3 : nodesBinary.length;

            System.out.println("# nodes:\t'"
                        + nodesBinary.length + "' == '" + nodesRest.length + "'");

            if (nodesBinary.length != nodesRest.length) {
                throw new Exception("# nodes.lenth missmatch:\t'"
                            + nodesBinary.length + "' == '" + nodesRest.length + "'");
            }

            for (int i = 0; i < nodesArrayLength; i++) {
                System.out.println("nodes[" + i + "].equals:\t'"
                            + nodesBinary[i].equals(nodesRest[i]) + "'");

                System.out.println("nodes[" + i + "].getName():\t'"
                            + nodesBinary[i].getName() + "' == '" + nodesRest[i].getName() + "'");

                System.out.println("nodes[" + i + "].toString():\t'"
                            + nodesBinary[i].toString() + "' == '" + nodesRest[i].toString() + "'");

                System.out.println("nodes[" + i + "].getArtificialId():\t'"
                            + nodesBinary[i].getArtificialId() + "' == '" + nodesRest[i].getArtificialId() + "'");

                System.out.println("nodes[" + i + "].getDescription():\t'"
                            + nodesBinary[i].getDescription() + "' == '" + nodesRest[i].getDescription() + "'");

                System.out.println("nodes[" + i + "].getDomain():\t'"
                            + nodesBinary[i].getDomain() + "' == '" + nodesRest[i].getDomain() + "'");

                System.out.println("nodes[" + i + "].getDynamicChildrenStatement():\t'"
                            + nodesBinary[i].getDynamicChildrenStatement() + "' \n==\n '"
                            + nodesRest[i].getDynamicChildrenStatement() + "'");

                System.out.println("nodes[" + i + "].getGroup():\t'"
                            + nodesBinary[i].getGroup() + "' == '" + nodesRest[i].getGroup() + "'");

                System.out.println("nodes[" + i + "].getIconString():\t'"
                            + nodesBinary[i].getIconString() + "' == '" + nodesRest[i].getIconString() + "'");

                System.out.println("nodes[" + i + "].getClassId():\t'"
                            + nodesBinary[i].getClassId() + "' == '" + nodesRest[i].getClassId() + "'");

                System.out.println("nodes[" + i + "].getIconFactory():\t'"
                            + nodesBinary[i].getIconFactory() + "' == '" + nodesRest[i].getIconFactory() + "'");

                System.out.println("nodes[" + i + "].getClass().getName():\t'"
                            + nodesBinary[i].getClass().getName() + "' == '" + nodesRest[i].getClass().getName() + "'");

                System.out.println("nodes[" + i + "]..getId():\t'"
                            + nodesBinary[i].getId() + "' == '" + nodesRest[i].getId() + "'");

                System.out.println("nodes[" + i + "].getPermissions():\t'"
                            + nodesBinary[i].getPermissions() + "' == '" + nodesRest[i].getPermissions() + "'");

                System.out.println("nodes[" + i + "].isDerivePermissionsFromClass():\t'"
                            + nodesBinary[i].isDerivePermissionsFromClass() + "' == '"
                            + nodesRest[i].isDerivePermissionsFromClass() + "'");

                System.out.println("nodes[" + i + "].isDynamic():\t'"
                            + nodesBinary[i].isDynamic() + "' == '" + nodesRest[i].isDynamic() + "'");

                System.out.println("nodes[" + i + "].isLeaf():\t'"
                            + nodesBinary[i].isLeaf() + "' == '" + nodesRest[i].isLeaf() + "'");

                System.out.println("nodes[" + i + "].hashCode():\t'"
                            + nodesBinary[i].hashCode() + "' == '" + nodesRest[i].hashCode() + "'");

                System.out.println("nodes[" + i + "].isValid():\t'"
                            + nodesBinary[i].isValid() + "' == '" + nodesRest[i].isValid() + "'");

                System.out.println("nodes[" + i + "].isSqlSort():\t'"
                            + nodesBinary[i].isSqlSort() + "' == '" + nodesRest[i].isSqlSort() + "'");

                System.out.println("nodes[" + i + "].isMetaObjectNode:\t'"
                            + MetaObjectNode.class.isAssignableFrom(nodesBinary[i].getClass())
                            + "' == '"
                            + MetaObjectNode.class.isAssignableFrom(nodesRest[i].getClass()) + "'");

                if (MetaObjectNode.class.isAssignableFrom(nodesBinary[i].getClass())
                            && MetaObjectNode.class.isAssignableFrom(nodesRest[i].getClass())) {
                    final MetaObjectNode metaObjectNodeBinary = (MetaObjectNode)nodesBinary[i];
                    final MetaObjectNode metaObjectNodeRest = (MetaObjectNode)nodesRest[i];

                    System.out.println("nodes[" + i + "].getObjectId():\t'"
                                + metaObjectNodeBinary.getObjectId() + "' == '" + metaObjectNodeRest.getObjectId()
                                + "'");

                    System.out.println("nodes[" + i + "].hasMetaObject:\t'"
                                + (metaObjectNodeBinary.getObject() != null) + "' == '"
                                + (metaObjectNodeRest.getObject() != null) + "'");
                }

                System.out.println("nodes[" + i + "].isMetaClassNode:\t'"
                            + MetaClassNode.class.isAssignableFrom(nodesBinary[i].getClass())
                            + "' == '"
                            + MetaClassNode.class.isAssignableFrom(nodesRest[i].getClass()) + "'");

                System.out.println("nodes[" + i + "].isMetaNode:\t'"
                            + MetaNode.class.isAssignableFrom(nodesBinary[i].getClass())
                            + "' == '"
                            + MetaNode.class.isAssignableFrom(nodesRest[i].getClass()) + "'");
            }

            System.exit(0);

            // TEST & COMPARE GET CLASS --------------------------------------------
            DevelopmentTools.initSessionManagerFromRestfulConnectionOnLocalhost(
                "SWITCHON",
                "Administratoren",
                "admin",
                "cismet");
            final MetaClass tagClassBinary = SessionManager.getConnection()
                        .getCallServerService()
                        .getClassByTableName(SessionManager.getSession().getUser(),
                            "tag",
                            "SWITCHON",
                            new PureRESTfulConnection().getConnectionContext());
//            final MetaClass tagClassBinary = SessionManager.getConnection().getCallServerService()
//                     .getClass(SessionManager.getSession().getUser(), 6, "SWITCHON");
//            System.out.println(".getKey(): " + tagClassBinary.getKey());

            DevelopmentTools.initSessionManagerFromPureRestfulConnectionOnLocalhost(
                "SWITCHON",
                "Administratoren",
                "admin",
                "cismet");
            final MetaClass tagClassRest = SessionManager.getConnection()
                        .getCallServerService()
                        .getClassByTableName(SessionManager.getSession().getUser(),
                            "TAG",
                            "SWITCHON",
                            new PureRESTfulConnection().getConnectionContext());

            System.out.println("tagClassBinary.equals(tagClassRest): "
                        + tagClassBinary.equals(tagClassRest));

            System.out.println(".toString():\t'"
                        + tagClassBinary.toString() + "' == '" + tagClassRest.toString() + "'");

            System.out.println(".hashCode():\t'"
                        + tagClassBinary.hashCode() + "' == '" + tagClassRest.hashCode() + "'");

            System.out.println(".getKey():\t'"
                        + tagClassBinary.getKey() + "' == '" + tagClassRest.getKey() + "'");

            System.out.println(".getComplexEditor():\t'"
                        + tagClassBinary.getComplexEditor() + "' == '" + tagClassRest.getComplexEditor() + "'");

            System.out.println(".getDescription():\t'"
                        + tagClassBinary.getDescription() + "' == '" + tagClassRest.getDescription() + "'");

            System.out.println(".getDomain():\t'"
                        + tagClassBinary.getDomain() + "' == '" + tagClassRest.getDomain() + "'");

            System.out.println(".getEditor():\t'"
                        + tagClassBinary.getEditor() + "' == '" + tagClassRest.getEditor() + "'");

            System.out.println(".getGetDefaultInstanceStmnt():\t'"
                        + tagClassBinary.getGetDefaultInstanceStmnt() + "' == '"
                        + tagClassRest.getGetDefaultInstanceStmnt() + "'");

            System.out.println(".getGetInstanceStmnt():\t'"
                        + tagClassBinary.getGetInstanceStmnt() + "' == '" + tagClassRest.getGetInstanceStmnt() + "'");

            System.out.println(".getGroup():\t'"
                        + tagClassBinary.getGroup() + "' == '" + tagClassRest.getGroup() + "'");

            System.out.println(".getName():\t'"
                        + tagClassBinary.getName() + "' == '" + tagClassRest.getName() + "'");

            System.out.println(".getPrimaryKey():\t'"
                        + tagClassBinary.getPrimaryKey() + "' == '" + tagClassRest.getPrimaryKey() + "'");

            System.out.println(".getRenderer():\t'"
                        + tagClassBinary.getRenderer() + "' == '" + tagClassRest.getRenderer() + "'");

            System.out.println(".getSQLFieldNames():\t'"
                        + tagClassBinary.getSQLFieldNames() + "' == '" + tagClassRest.getSQLFieldNames() + "'");

            System.out.println(".getSimpleEditor():\t'"
                        + tagClassBinary.getSimpleEditor() + "' == '" + tagClassRest.getSimpleEditor() + "'");

            System.out.println(".getTableName():\t'"
                        + tagClassBinary.getTableName() + "' == '" + tagClassRest.getTableName() + "'");

            System.out.println(".isArrayElementLink():\t'"
                        + tagClassBinary.isArrayElementLink() + "' == '" + tagClassRest.isArrayElementLink() + "'");

            System.out.println(".isIndexed():\t'"
                        + tagClassBinary.isIndexed() + "' == '" + tagClassRest.isIndexed() + "'");

            System.out.println(".getID():\t'"
                        + tagClassBinary.getID() + "' == '" + tagClassRest.getID() + "'");

            System.out.println(".getId():\t'"
                        + tagClassBinary.getId() + "' == '" + tagClassRest.getId() + "'");

            System.out.println(".getJavaClass():\t'"
                        + tagClassBinary.getJavaClass() + "' == '" + tagClassRest.getJavaClass() + "'");

//            System.out.println(".getIcon():\t'"
//                    + tagClassBinary.getIcon() + "' == '" + tagClassRest.getIcon() + "'");
//
//            System.out.println(".getObjectIcon():\t'"
//                    + tagClassBinary.getObjectIcon() + "' == '" + tagClassRest.getObjectIcon() + "'");
//
//            System.out.println(".getPolicy():\t'"
//                    + tagClassBinary.getPolicy() + "' == '" + tagClassRest.getPolicy() + "'");
            System.out.println(".getToStringConverter():\t'"
                        + tagClassBinary.getToStringConverter() + "' == '" + tagClassRest.getToStringConverter() + "'");

            System.out.println(".getAttributes():\t'"
                        + tagClassBinary.getAttributes().size() + "' == '" + tagClassRest.getAttributes().size() + "'");
            for (final ClassAttribute classAttributeBinary : tagClassBinary.getAttribs()) {
                final Collection collection = tagClassRest.getAttributeByName(classAttributeBinary.getName());
                final ClassAttribute classAttributeRest = ((collection != null) && (collection.size() == 1))
                    ? (ClassAttribute)collection.iterator().next() : null;
                if (classAttributeRest != null) {
                    System.out.println(".getAttributes('" + classAttributeBinary.getName() + "').getClassKey():\t'"
                                + classAttributeBinary.getClassKey() + "' == '" + classAttributeRest.getClassKey()
                                + "'");

                    System.out.println(".getAttributes('" + classAttributeBinary.getName() + "').getDescription():\t'"
                                + classAttributeBinary.getDescription() + "' == '" + classAttributeRest
                                .getDescription() + "'");

                    System.out.println(".getAttributes('" + classAttributeBinary.getName() + "').getID():\t'"
                                + classAttributeBinary.getID() + "' == '" + classAttributeRest.getID() + "'");

                    System.out.println(".getAttributes('" + classAttributeBinary.getName() + "').getJavaType():\t'"
                                + classAttributeBinary.getJavaType() + "' == '" + classAttributeRest.getJavaType()
                                + "'");

                    System.out.println(".getAttributes('" + classAttributeBinary.getName() + "').getName():\t'"
                                + classAttributeBinary.getName() + "' == '" + classAttributeRest.getName() + "'");

                    System.out.println(".getAttributes('" + classAttributeBinary.getName() + "').getClassID():\t'"
                                + classAttributeBinary.getClassID() + "' == '" + classAttributeRest.getClassID() + "'");

                    System.out.println(".getAttributes('" + classAttributeBinary.getName() + "').getKey():\t'"
                                + classAttributeBinary.getKey() + "' == '" + classAttributeRest.getKey() + "'");

                    System.out.println(".getAttributes('" + classAttributeBinary.getName()
                                + "').getToStringConverter():\t'"
                                + classAttributeBinary.getToStringConverter() + "' == '"
                                + classAttributeRest.getToStringConverter() + "'");

                    System.out.println(".getAttributes('" + classAttributeBinary.getName() + "').getTypeID():\t'"
                                + classAttributeBinary.getTypeID() + "' == '" + classAttributeRest.getTypeID() + "'");

                    System.out.println(".getAttributes('" + classAttributeBinary.getName() + "').getTypeId():\t'"
                                + classAttributeBinary.getTypeId() + "' == '" + classAttributeRest.getTypeId() + "'");

                    System.out.println(".getAttributes('" + classAttributeBinary.getName() + "').getValue():\t'"
                                + classAttributeBinary.getValue() + "' == '" + classAttributeRest.getValue() + "'");

                    System.out.println(".getAttributes('" + classAttributeBinary.getName() + "').isArray():\t'"
                                + classAttributeBinary.isArray() + "' == '" + classAttributeRest.isArray() + "'");

                    System.out.println(".getAttributes('" + classAttributeBinary.getName() + "').isChanged():\t'"
                                + classAttributeBinary.isChanged() + "' == '" + classAttributeRest.isChanged() + "'");

                    System.out.println(".getAttributes('" + classAttributeBinary.getName() + "').isOptional():\t'"
                                + classAttributeBinary.isOptional() + "' == '" + classAttributeRest.isOptional() + "'");

                    System.out.println(".getAttributes('" + classAttributeBinary.getName() + "').isSubstitute():\t'"
                                + classAttributeBinary.isSubstitute() + "' == '" + classAttributeRest.isSubstitute()
                                + "'");

                    System.out.println(".getAttributes('" + classAttributeBinary.getName() + "').isPrimaryKey():\t'"
                                + classAttributeBinary.isPrimaryKey() + "' == '" + classAttributeRest.isPrimaryKey()
                                + "'");

                    System.out.println(".getAttributes('" + classAttributeBinary.getName() + "').isVisible():\t'"
                                + classAttributeBinary.isVisible() + "' == '" + classAttributeRest.isVisible() + "'");
                } else {
                    throw new Exception(".getAttributes('" + classAttributeBinary.getName()
                                + "') not found in REST cids class!");
                }
            }

            System.out.println(".getMemberAttributeInfos():\t'"
                        + tagClassBinary.getMemberAttributeInfos().size() + "' == '"
                        + tagClassRest.getMemberAttributeInfos().size() + "'");
            for (final Object key : tagClassBinary.getMemberAttributeInfos().keySet()) {
                if (tagClassRest.getMemberAttributeInfos().containsKey(key)) {
                    final MemberAttributeInfo maiBinary = (MemberAttributeInfo)tagClassBinary.getMemberAttributeInfos()
                                .get(key);
                    final MemberAttributeInfo maiRest = (MemberAttributeInfo)tagClassRest.getMemberAttributeInfos()
                                .get(key);

                    System.out.println(".getMemberAttributeInfos('" + key + "').getArrayKeyFieldName():\t'"
                                + maiBinary.getArrayKeyFieldName() + "' == '" + maiRest.getArrayKeyFieldName() + "'");

                    System.out.println(".getMemberAttributeInfos('" + key + "').getComplexEditor():\t'"
                                + maiBinary.getComplexEditor() + "' == '" + maiRest.getComplexEditor() + "'");

                    System.out.println(".getMemberAttributeInfos('" + key + "').getDefaultValue():\t'"
                                + maiBinary.getDefaultValue() + "' == '" + maiRest.getDefaultValue() + "'");

                    System.out.println(".getMemberAttributeInfos('" + key + "').getEditor():\t'"
                                + maiBinary.getEditor() + "' == '" + maiRest.getEditor() + "'");

                    System.out.println(".getMemberAttributeInfos('" + key + "').getFieldName():\t'"
                                + maiBinary.getFieldName() + "' == '" + maiRest.getFieldName() + "'");

                    System.out.println(".getMemberAttributeInfos('" + key + "').getFromString():\t'"
                                + maiBinary.getFromString() + "' == '" + maiRest.getFromString() + "'");

                    System.out.println(".getMemberAttributeInfos('" + key + "').getJavaclassname():\t'"
                                + maiBinary.getJavaclassname() + "' == '" + maiRest.getJavaclassname() + "'");

                    System.out.println(".getMemberAttributeInfos('" + key + "').getName():\t'"
                                + maiBinary.getName() + "' == '" + maiRest.getName() + "'");

                    System.out.println(".getMemberAttributeInfos('" + key + "').getRenderer():\t'"
                                + maiBinary.getRenderer() + "' == '" + maiRest.getRenderer() + "'");

                    System.out.println(".getMemberAttributeInfos('" + key + "').getRenderer():\t'"
                                + maiBinary.getRenderer() + "' == '" + maiRest.getRenderer() + "'");

                    System.out.println(".getMemberAttributeInfos('" + key + "').toString():\t'"
                                + maiBinary.toString() + "' == '" + maiRest.toString() + "'");

                    System.out.println(".getMemberAttributeInfos('" + key + "').getToString():\t'"
                                + maiBinary.getToString() + "' == '" + maiRest.getToString() + "'");

                    System.out.println(".getMemberAttributeInfos('" + key + "').getClassId():\t'"
                                + maiBinary.getClassId() + "' == '" + maiRest.getClassId() + "'");

                    System.out.println(".getMemberAttributeInfos('" + key + "').getForeignKeyClassId():\t'"
                                + maiBinary.getForeignKeyClassId() + "' == '" + maiRest.getForeignKeyClassId() + "'");

                    System.out.println(".getMemberAttributeInfos('" + key + "').getId():\t'"
                                + maiBinary.getId() + "' == '" + maiRest.getId() + "'");

                    System.out.println(".getMemberAttributeInfos('" + key + "').getKey():\t'"
                                + maiBinary.getKey() + "' == '" + maiRest.getKey() + "'");

                    System.out.println(".getMemberAttributeInfos('" + key + "').getPosition():\t'"
                                + maiBinary.getPosition() + "' == '" + maiRest.getPosition() + "'");

                    System.out.println(".getMemberAttributeInfos('" + key + "').getTypeId():\t'"
                                + maiBinary.getTypeId() + "' == '" + maiRest.getTypeId() + "'");

                    System.out.println(".getMemberAttributeInfos('" + key + "').isArray():\t'"
                                + maiBinary.isArray() + "' == '" + maiRest.isArray() + "'");

                    System.out.println(".getMemberAttributeInfos('" + key + "').isExtensionAttribute():\t'"
                                + maiBinary.isExtensionAttribute() + "' == '" + maiRest.isExtensionAttribute() + "'");

                    System.out.println(".getMemberAttributeInfos('" + key + "').isForeignKey():\t'"
                                + maiBinary.isForeignKey() + "' == '" + maiRest.isForeignKey() + "'");

                    System.out.println(".getMemberAttributeInfos('" + key + "').isIndexed():\t'"
                                + maiBinary.isIndexed() + "' == '" + maiRest.isIndexed() + "'");

                    System.out.println(".getMemberAttributeInfos('" + key + "').isOptional():\t'"
                                + maiBinary.isOptional() + "' == '" + maiRest.isOptional() + "'");

                    System.out.println(".getMemberAttributeInfos('" + key + "').isSubstitute():\t'"
                                + maiBinary.isSubstitute() + "' == '" + maiRest.isSubstitute() + "'");

                    System.out.println(".getMemberAttributeInfos('" + key + "').isVirtual():\t'"
                                + maiBinary.isVirtual() + "' == '" + maiRest.isVirtual() + "'");

                    System.out.println(".getMemberAttributeInfos('" + key + "').isVisible():\t'"
                                + maiBinary.isVisible() + "' == '" + maiRest.isVisible() + "'");
                } else {
                    throw new Exception(".getMemberAttributeInfos('" + key + "') not found in REST cids class!");
                }
            }
        } catch (Throwable ex) {
            System.out.println(ex.getMessage());
            LOG.fatal(ex.getMessage(), ex);
            System.exit(1);
        }
    }
}
