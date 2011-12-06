/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.navigator.utils;

import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.exception.ConnectionException;

import Sirius.server.middleware.types.MetaObject;
import Sirius.server.middleware.types.MetaObjectNode;
import Sirius.server.middleware.types.Node;

import org.apache.log4j.Logger;

import java.util.Comparator;

/**
 * Works like the MetaObjectNodeComparator. Additionally sort order can be set to be ascending or descending.
 *
 * @author   jweintraut
 * @version  $Revision$, $Date$
 */
public class DirectedMetaObjectNodeComparator implements Comparator<Node> {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(DirectedMetaObjectNodeComparator.class);

    //~ Instance fields --------------------------------------------------------

    private boolean ascending;
    private boolean cancelled = false;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DirectedMetaObjectNodeComparator object.
     *
     * @param  ascending  Sort ascending?
     */
    public DirectedMetaObjectNodeComparator(final boolean ascending) {
        this.ascending = ascending;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Since compare() connects to the server, the comparison is time consuming. Once called, Arrays.sort() invokes
     * compare() for every array entry, even if the user cancelled the SwingWorker which called Arrays.sort(). To reduce
     * resource consumption this method sets a flag which tells the comparator to abort the comparison. This is done by
     * returning a default value for every unsorted entry.
     */
    public void cancel() {
        cancelled = true;
    }

    @Override
    public int compare(final Node o1, final Node o2) {
        if (cancelled) {
            return 0;
        }

        if (!(o1 instanceof MetaObjectNode) || !(o2 instanceof MetaObjectNode)) {
            return 0;
        }
        if ((o1 == null) && (o2 == null)) {
            return 0;
        }
        if (o1 == null) {
            return ascending ? -1 : 1;
        }
        if (o2 == null) {
            return ascending ? 1 : -1;
        }

        final MetaObjectNode mon1 = (MetaObjectNode)o1;
        final MetaObjectNode mon2 = (MetaObjectNode)o2;

        String mos1 = mon1.toString();
        String mos2 = mon2.toString();

        if ((mos1 == null) || (mos1.trim().length() == 0)) {
            MetaObject mo1 = mon1.getObject();
            try {
                if (mo1 == null) {
                    mo1 = SessionManager.getProxy()
                                .getMetaObject(
                                        ((MetaObjectNode)o1).getObjectId(),
                                        o1.getClassId(),
                                        o1.getDomain());
                    mon1.setObject(mo1);
                    mon1.setName(mo1.toString());
                }
            } catch (final ConnectionException e) {
                LOG.error("Connection problem: ", e);
            }
            mos1 = mon1.toString();
        }
        if ((mos2 == null) || (mos2.trim().length() == 0)) {
            try {
                MetaObject mo2 = mon2.getObject();

                if (mo2 == null) {
                    mo2 = SessionManager.getProxy()
                                .getMetaObject(
                                        ((MetaObjectNode)o2).getObjectId(),
                                        o2.getClassId(),
                                        o2.getDomain());
                    mon2.setObject(mo2);
                    mon2.setName(mo2.toString());
                }
                mos2 = mon2.toString();
            } catch (final ConnectionException e) {
                LOG.error("Connection problem: ", e);
            }
        }

        final String class1 = (mon1.getObject() != null) ? mon1.getObject().getClassKey() : "";
        final String class2 = (mon2.getObject() != null) ? mon2.getObject().getClassKey() : "";

        if ((class1 != null) && (class2 != null)) {
            int comparison = class1.compareTo(class2);

            if ((comparison == 0) && (mos1 != null) && (mos2 != null)) {
                comparison = mos1.compareTo(mos2);
            }

            return ascending ? comparison : (-1 * comparison);
        }

        return 0;
    }
}
