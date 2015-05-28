/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.connection;

import Sirius.navigator.connection.proxy.ConnectionProxy;
import Sirius.navigator.exception.ConnectionException;

import Sirius.server.localserver.attribute.ClassAttribute;
import Sirius.server.localserver.attribute.MemberAttributeInfo;
import Sirius.server.middleware.types.MetaClass;
import Sirius.server.middleware.types.MetaClassNode;
import Sirius.server.middleware.types.MetaNode;
import Sirius.server.middleware.types.MetaObjectNode;
import Sirius.server.middleware.types.Node;
import Sirius.server.newuser.User;
import Sirius.server.newuser.UserException;

import Sirius.util.image.ImageHashMap;

import org.openide.util.Exceptions;
import org.openide.util.Lookup;

import java.awt.GraphicsEnvironment;

import java.net.URI;

import java.rmi.RemoteException;

import java.util.Collection;
import java.util.Vector;

import javax.swing.Icon;

import javax.ws.rs.core.UriBuilder;

import de.cismet.cids.client.tools.DevelopmentTools;

import de.cismet.cids.server.CallServerService;
import de.cismet.cids.server.ws.SSLConfig;
import de.cismet.cids.server.ws.SSLConfigProvider;
import de.cismet.cids.server.ws.rest.RESTfulSerialInterfaceConnector;

import de.cismet.netutil.Proxy;

import de.cismet.reconnector.Reconnector;

import static Sirius.navigator.connection.RESTfulConnection.LOG;

/**
 * The PureRESTfulConnection allows the cids navigator to use the new cids Pure REST API while providing backwards
 * compatibility with the old Connection interface.
 *
 * <p>This class extends the 'java-objects-over-http' RESTfulConnection and internally uses a new CallServerService
 * Implementation (PureRESTfulReconnector and RESTfulInterfaceConnector, respectively) that connects to the cids server
 * Pure REST API.</p>
 *
 * @author   Pascal Dihé <pascal.dihe@cismet.de>
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
     * @param   restServerURL  DOCUMENT ME!
     * @param   proxy          DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    private CallServerService createLegacyConnector(final String restServerURL, final Proxy proxy)
            throws ConnectionException {
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
            return new RESTfulSerialInterfaceConnector(callServerURI.toString(), proxy, sslConfig);
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
     * @param   callserverURL  DOCUMENT ME!
     * @param   proxy          DOCUMENT ME!
     *
     * @return  PureRESTfulReconnector
     */
    @Override
    protected Reconnector<CallServerService> createReconnector(final String callserverURL, final Proxy proxy) {
        reconnector = new PureRESTfulReconnector(CallServerService.class, callserverURL, proxy);
        reconnector.useDialog(!GraphicsEnvironment.getLocalGraphicsEnvironment().isHeadlessInstance(), null);
        return reconnector;
    }

    @Override
    public boolean connect(final String callserverURL, final Proxy proxy) throws ConnectionException {
        this.connector = createReconnector(callserverURL, proxy).getProxy();

        // FIXME: remove when all methods implemented in pure RESTful Service
        this.legacyConnector = createLegacyConnector(callserverURL, proxy);

        try {
            this.getDomains();
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
    public String[] getDomains() throws ConnectionException {
        try {
            LOG.warn("delegating getDomains() to legacy REST Connection");
            return this.legacyConnector.getDomains();
        } catch (final Exception e) {
            final String message = "cannot get domains: " + e.getMessage(); // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    /**
     * FIXME: Operation currently delegated to legacy REST Connection! Implement in pure RESTful Service
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    @Override
    public Vector getUserGroupNames() throws ConnectionException {
        try {
            LOG.warn("delegating getUserGroupNames() to legacy REST Connection");
            return this.legacyConnector.getUserGroupNames();
        } catch (final Exception e) {
            final String message = "could not get usergroup names: " + e.getMessage(); // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    /**
     * FIXME: Operation currently delegated to legacy REST Connection! Implement in pure RESTful Service
     *
     * @param   username  DOCUMENT ME!
     * @param   domain    DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     * @throws  UserException        DOCUMENT ME!
     */
    @Override
    public Vector getUserGroupNames(final String username, final String domain) throws ConnectionException,
        UserException {
        try {
            LOG.warn("delegating getUserGroupNames(" + username + ", " + domain + ") to legacy REST Connection");
            return this.legacyConnector.getUserGroupNames(username, domain);
        } catch (final Exception e) {
            final String message = "could not get usergroup names by username, domain: " + username + "@" + domain; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
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

    @Override
    public boolean changePassword(final User user, final String oldPassword, final String newPassword)
            throws ConnectionException, UserException {
        try {
            LOG.warn("delegating changePassword() to legacy REST Connection");
            return this.legacyConnector.changePassword(user, oldPassword, newPassword);
        } catch (final Exception e) {
            final String message = "could not change password: " + user + " :: " + oldPassword + " :: " + newPassword; // NOI18N
            LOG.error(message, e);
            throw new ConnectionException(message, e);
        }
    }

    /**
     * FIXME: Operation currently delegated to legacy REST Connection! Implement in pure RESTful Service
     *
     * @param   user  DOCUMENT ME!
     * @param   key   DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    @Override
    public String getConfigAttr(final User user, final String key) throws ConnectionException {
        try {
            LOG.warn("delegating getConfigAttr(" + user.getName() + ", " + key + ") to legacy REST Connection");
            return this.legacyConnector.getConfigAttr(user, key);
        } catch (final RemoteException e) {
            throw new ConnectionException("could not get config attr for user: " + user, e); // NOI18N
        }
    }

    /**
     * FIXME: Operation currently delegated to legacy REST Connection! Implement in pure RESTful Service.
     *
     * @param   user  DOCUMENT ME!
     * @param   key   DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ConnectionException  DOCUMENT ME!
     */
    @Override
    public boolean hasConfigAttr(final User user, final String key) throws ConnectionException {
        try {
            LOG.warn("delegating hasConfigAttr(" + user.getName() + ", " + key + ") to legacy REST Connection");
            return this.legacyConnector.hasConfigAttr(user, key);
        } catch (final RemoteException e) {
            throw new ConnectionException("could not check config attr for user: " + user, e); // NOI18N
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
            // TEST & COMPARE NODES CLASS --------------------------------------------
            DevelopmentTools.initSessionManagerFromRestfulConnectionOnLocalhost(
                "SWITCHON",
                "Administratoren",
                "admin",
                "cismet");
            // final Node[] nodesBinary = SessionManager.getProxy().getRoots();
            final Node nodeBinary = SessionManager.getProxy().getNode(7, "SWITCHON");
            // final Node[] nodesBinary = new Node[]{nodeBinary};
            final Node[] nodesBinary = SessionManager.getProxy().getChildren(nodeBinary);

            DevelopmentTools.initSessionManagerFromPureRestfulConnectionOnLocalhost(
                "SWITCHON",
                "Administratoren",
                "admin",
                "cismet");
            final Node nodeRest = SessionManager.getProxy().getNode(7, "SWITCHON");
            // final Node[] nodesRest = new Node[]{nodeRest};
            final Node[] nodesRest = SessionManager.getProxy().getChildren(nodeRest);

            System.out.println("# rootNodes:\t'"
                        + nodesBinary.length + "' == '" + nodesRest.length + "'");

            if (nodesBinary.length != nodesRest.length) {
                throw new Exception("# rootNodes.lenth missmatch:\t'"
                            + nodesBinary.length + "' == '" + nodesRest.length + "'");
            }

            for (int i = 0; i < nodesBinary.length; i++) {
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
                            + nodesBinary[i].getDynamicChildrenStatement() + "' == '"
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

                System.out.println("nodes[" + i + "].isValid():\t'"
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
                        .getClassByTableName(SessionManager.getSession().getUser(), "tag", "SWITCHON");
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
                        .getClassByTableName(SessionManager.getSession().getUser(), "TAG", "SWITCHON");

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
