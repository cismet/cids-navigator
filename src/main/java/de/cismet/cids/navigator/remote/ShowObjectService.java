/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.navigator.remote;

import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.plugin.PluginRegistry;
import Sirius.navigator.types.treenode.DefaultMetaTreeNode;
import Sirius.navigator.types.treenode.ObjectTreeNode;
import Sirius.navigator.ui.ComponentRegistry;

import Sirius.server.middleware.types.MetaClass;
import Sirius.server.middleware.types.MetaObject;
import Sirius.server.middleware.types.MetaObjectNode;
import Sirius.server.middleware.types.Node;

import org.openide.util.lookup.ServiceProvider;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.cismet.cids.navigator.utils.ClassCacheMultiple;
import de.cismet.cids.navigator.utils.MetaTreeNodeVisualization;

import de.cismet.cids.server.connectioncontext.ClientConnectionContext;
import de.cismet.cids.server.connectioncontext.ConnectionContextProvider;

import de.cismet.remote.AbstractRESTRemoteControlMethod;
import de.cismet.remote.RESTRemoteControlMethod;

//Swagger annotations are comented out, due to a incompatibility of slf4j
/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
@Path("/showObjects")
//@Api(
//    value = "/sayHello",
//    description = "says hello"
//)
@Produces({ MediaType.APPLICATION_JSON })
@ServiceProvider(service = RESTRemoteControlMethod.class)
public class ShowObjectService extends AbstractRESTRemoteControlMethod implements RESTRemoteControlMethod,
    ConnectionContextProvider {

    //~ Static fields/initializers ---------------------------------------------

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ShowObjectService.class);

    //~ Instance fields --------------------------------------------------------

    private final ClientConnectionContext connectionContext = ClientConnectionContext.create(getClass()
                    .getSimpleName());

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ShowObjectService object.
     */
    public ShowObjectService() {
        super(-1, "/showObject");
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   domain     DOCUMENT ME!
     * @param   tablename  DOCUMENT ME!
     * @param   oids       DOCUMENT ME!
     * @param   search     DOCUMENT ME!
     * @param   map        DOCUMENT ME!
     * @param   renderer   DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_HTML)
    public Response show(@QueryParam("domain") String domain,
            @QueryParam("class") final String tablename,
            @QueryParam("objectids") final String oids,
            @QueryParam("search") final Boolean search,
            @QueryParam("map") final Boolean map,
            @QueryParam("renderer") final Boolean renderer) {
        try {
            if (domain == null) {
                domain = SessionManager.getSession().getUser().getDomain();
            }
            final MetaClass mc = ClassCacheMultiple.getMetaClass(domain, tablename);
            if (mc == null) {
                LOG.warn("The Class with the tablename " + tablename + "was not dound in the domain " + domain + ".");
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            final StringBuilder query = new StringBuilder("select ").append(mc.getID())
                        .append(", ")
                        .append(mc.getPrimaryKey())
                        .append(" from ")
                        .append(mc.getTableName())
                        .append(" WHERE  ")
                        .append(mc.getPrimaryKey())
                        .append(" in (")
                        .append(oids)
                        .append(");"); // NOI18N

            final MetaObject[] metaObjects = SessionManager.getProxy()
                        .getMetaObjectByQuery(SessionManager.getSession().getUser(),
                            query.toString(),
                            domain,
                            getConnectionContext());

            if ((metaObjects != null) && (metaObjects.length == 0)) {
                LOG.info("The query " + query.toString() + "returned with no results");
                return Response.noContent().build();
            }

            final List<DefaultMetaTreeNode> defaultMetaTreeNodes = new ArrayList<DefaultMetaTreeNode>();
            final Node[] newNodes = new Node[metaObjects.length];
            int i = 0;
            for (final MetaObject mo : metaObjects) {
                final MetaObjectNode node = new MetaObjectNode(mo.getBean());
                newNodes[i++] = node;
                final ObjectTreeNode otn = new ObjectTreeNode(node, getConnectionContext());
                defaultMetaTreeNodes.add(otn);
            }

            if ((search == null) || search) {
                ComponentRegistry.getRegistry().getSearchResultsTree().setResultNodes(newNodes, false, null);
                ComponentRegistry.getRegistry().getGUIContainer().select(ComponentRegistry.SEARCHRESULTS_TREE);
            }

            if ((map != null) && map) {
                PluginRegistry.getRegistry()
                        .getPluginDescriptor("cismap")
                        .getUIDescriptor("cismap")
                        .getView()
                        .makeVisible();
                MetaTreeNodeVisualization.getInstance().addVisualization(defaultMetaTreeNodes);
            }
            if ((renderer != null) && renderer) {
                ComponentRegistry.getRegistry().getDescriptionPane().setNodesDescriptions(defaultMetaTreeNodes);
                ComponentRegistry.getRegistry().getGUIContainer().select(ComponentRegistry.DESCRIPTION_PANE);
            }
        } catch (Exception e) {
            LOG.error("Problem during remote-showing an object", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
        }
        return Response.ok("ok").build();
    }

    @Override
    public final ClientConnectionContext getConnectionContext() {
        return connectionContext;
    }
}
