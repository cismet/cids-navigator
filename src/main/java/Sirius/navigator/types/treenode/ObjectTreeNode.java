/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.types.treenode;

import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.resource.ResourceManager;

import Sirius.server.middleware.types.MetaClass;
import Sirius.server.middleware.types.MetaObject;
import Sirius.server.middleware.types.MetaObjectNode;
import Sirius.server.middleware.types.Node;

import com.vividsolutions.jts.geom.Geometry;

import org.apache.log4j.Logger;

import java.util.HashMap;

import javax.swing.ImageIcon;

import de.cismet.connectioncontext.ClientConnectionContext;
import de.cismet.connectioncontext.ConnectionContextProvider;

import de.cismet.tools.CurrentStackTrace;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class ObjectTreeNode extends DefaultMetaTreeNode {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient Logger LOG = Logger.getLogger(ObjectTreeNode.class);
    private static final ResourceManager resource = ResourceManager.getManager();
    private static final HashMap<String, ImageIcon> iconCache = new HashMap<String, ImageIcon>();

    //~ Instance fields --------------------------------------------------------

    protected Geometry cashedGeometry;
    protected String lightweightJson;

    /**
     * DOCUMENT ME!
     *
     * @param   autoload  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected ImageIcon nodeIcon;
    /**
     * DOCUMENT ME!
     *
     * @param   autoload  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    volatile Boolean metaObjectFilled = false;
    /**
     * DOCUMENT ME!
     *
     * @param   autoload  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private MetaClass metaClass;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ObjectTreeNode object.
     *
     * @param  metaObjectNode     DOCUMENT ME!
     * @param  connectionContext  DOCUMENT ME!
     */
    public ObjectTreeNode(final MetaObjectNode metaObjectNode, final ClientConnectionContext connectionContext) {
        super(metaObjectNode, connectionContext);

        try {
            final MetaClass myMetaClass = this.getMetaClass();
            final String classKey = myMetaClass.getID() + "@" + myMetaClass.getDomain();

            nodeIcon = iconCache.get(classKey);
            if (nodeIcon == null) {
                if ((myMetaClass != null) && (myMetaClass.getObjectIconData().length > 0)) {
                    this.nodeIcon = new ImageIcon(myMetaClass.getObjectIconData());
                } else {
                    this.nodeIcon = resource.getIcon("ObjectNodeIcon.gif"); // NOI18N
                }
                iconCache.put(classKey, nodeIcon);
            }
        } catch (Exception exp) {
            LOG.warn("could not load object icon: " + exp.getMessage(), exp);
            this.nodeIcon = resource.getIcon("ObjectNodeIcon.gif");         // NOI18N
        }
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public MetaObjectNode getMetaObjectNode() {
        return (MetaObjectNode)this.userObject;
    }

    @Override
    public TreeNodeLoader getTreeNodeLoader() {
        return ((DefaultMetaTreeNode)this.getParent()).getTreeNodeLoader();
    }

    // --------------------------------------------------------------------------
    @Override
    public final synchronized void explore() throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("[ObjectNode] Begin explore()"); // NOI18N
        }

        if (!isExplored() && !this.getMetaObjectNode().isLeaf()) {
            this.explored = this.getTreeNodeLoader().addChildren(this);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("[ObjectNode] End explore()"); // NOI18N
        }
    }

    @Override
    public final boolean isRootNode() {
        return false;
    }

    @Override
    public final boolean isWaitNode() {
        return false;
    }

    @Override
    public final boolean isPureNode() {
        return false;
    }

    @Override
    public final boolean isObjectNode() {
        return true;
    }

    @Override
    public final boolean isClassNode() {
        return false;
    }

    @Override
    public final String toString() {
        String toString = getMetaObjectNode().getName();
        if (toString == null) {
            final MetaObject mo = getMetaObjectNode().getObject();
            if (mo != null) {
                toString = mo.toString();
                getNode().setName(toString);
            } else {
                // Implicitly stores toString in getNode().setName(...) for future use.
                // See implementation of getMetaObject().
                final MetaObject m = getMetaObject();

                if (m != null) {
                    toString = m.toString();
                } else {
                    toString = "null";
                }
            }
        }
        return toString;
    }

    @Override
    public final String getDescription() {
        return this.getMetaObjectNode().getDescription();
    }

    @Override
    public final ImageIcon getOpenIcon() {
        return this.nodeIcon;
    }

    @Override
    public final ImageIcon getClosedIcon() {
        return this.nodeIcon;
    }

    @Override
    public final ImageIcon getLeafIcon() {
        return this.nodeIcon;
    }

    @Override
    public final boolean equals(final DefaultMetaTreeNode node) {
        if (node.isObjectNode() && (this.getID() == node.getID()) && this.getDomain().equals(node.getDomain())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public final boolean equalsNode(final Node node) {
        if ((node instanceof MetaObjectNode) && this.getMetaObjectNode().getDomain().equals(node.getDomain())
                    && (this.getMetaObjectNode().getId() == node.getId())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public final int getID() {
        return this.getMetaObjectNode().getId();
    }

    @Override
    public final String getDomain() {
        return this.getMetaObjectNode().getDomain();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public final MetaObject getMetaObject() {
        return getMetaObject(true);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   autoload  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public final MetaObject getMetaObject(final boolean autoload) {
        final MetaObjectNode mon = (MetaObjectNode)this.userObject;
        MetaObject mo = mon.getObject();

        if (!autoload) {
            return mo;
        }

        if (mo == null) {
            synchronized (this) {
                mo = mon.getObject();
                if (mo == null) {
                    try {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("caching object node", new CurrentStackTrace()); // NOI18N
                        }
                        final int oid = mon.getObjectId();
                        final int cid = mon.getClassId();
                        final String domain = mon.getDomain();
                        final MetaObject metaObject = SessionManager.getProxy()
                                    .getMetaObject(oid, cid, domain, getConnectionContext());
                        mon.setObject(metaObject);
                        metaObjectFilled = true;
                        if ((mon.getName() == null) || mon.getName().equals("NameWirdGeladen")) {
                            mon.setName(metaObject.toString());
                        }
                        mo = metaObject;
                    } catch (final Throwable t) {
                        String nodeName = String.valueOf(userObject);

                        if (mon != null) {
                            nodeName += " (class: " + mon.getClassId() + ", object: " + mon.getObjectId() + ")";
                        }

                        LOG.error("could not retrieve meta object of node '" + nodeName + "'", t);
                    }
                }
            }
        }

        return mo;
    }

    /**
     * Setzt ein neues MetaObject, bzw. die ver\u00E4nderte Kopie des alten MetaObjects dieser Node.
     *
     * @param  metaObject  DOCUMENT ME!
     */
    public final void setMetaObject(final MetaObject metaObject) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("setting mo from " + getMetaObject() + " to " + metaObject); // NOI18N
        }
        this.getMetaObjectNode().setObject(metaObject);
        this.setChanged(true);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public final MetaClass getMetaClass() throws Exception {
        if (metaClass == null) {
            metaClass = SessionManager.getProxy()
                        .getMetaClass(this.getMetaObjectNode().getClassId(),
                                this.getDomain(),
                                getConnectionContext());
        }
        return metaClass;
    }

    @Override
    public String getKey() throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("getkey"); // NOI18N
        }
        return this.getMetaObject().getKey().toString();
    }

    @Override
    public int getClassID() {
        return this.getMetaObjectNode().getClassId();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Boolean isMetaObjectFilled() {
        return metaObjectFilled;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Geometry getCashedGeometry() {
        return cashedGeometry;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getLightweightJson() {
        return lightweightJson;
    }
}
