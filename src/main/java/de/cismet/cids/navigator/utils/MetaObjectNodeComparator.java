/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 *  Copyright (C) 2011 therter
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.cismet.cids.navigator.utils;

import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.exception.ConnectionException;

import Sirius.server.middleware.types.MetaObject;
import Sirius.server.middleware.types.MetaObjectNode;
import Sirius.server.middleware.types.Node;

import org.apache.log4j.Logger;

import java.util.Comparator;

import de.cismet.cids.server.connectioncontext.ClientConnectionContext;
import de.cismet.cids.server.connectioncontext.ConnectionContextProvider;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class MetaObjectNodeComparator implements Comparator<Node>, ConnectionContextProvider {

    //~ Instance fields --------------------------------------------------------

    protected final Logger LOG = Logger.getLogger(MetaObjectNodeComparator.class);

    private final ClientConnectionContext connectionContext;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new MetaObjectNodeComparator object.
     */
    @Deprecated
    public MetaObjectNodeComparator() {
        this(ClientConnectionContext.createDeprecated());
    }

    /**
     * Creates a new MetaObjectNodeComparator object.
     *
     * @param  connectionContext  DOCUMENT ME!
     */
    public MetaObjectNodeComparator(final ClientConnectionContext connectionContext) {
        this.connectionContext = connectionContext;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public int compare(final Node o1, final Node o2) {
        if (!(o1 instanceof MetaObjectNode) || !(o2 instanceof MetaObjectNode)) {
            return 0;
        }
        if ((o1 == null) && (o2 == null)) {
            return 0;
        }
        if (o1 == null) {
            return -1;
        }
        if (o2 == null) {
            return 1;
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
                                        o1.getDomain(),
                                        getConnectionContext());
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
                                        o2.getDomain(),
                                        getConnectionContext());
                    mon2.setObject(mo2);
                    mon2.setName(mo2.toString());
                }
                mos2 = mon2.toString();
            } catch (final ConnectionException e) {
                LOG.error("Connection problem: ", e);
            }
        }
        if ((mos1 != null) && (mos2 != null)) {
            return mos1.compareTo(mos2);
        } else {
            return 0;
        }
    }

    @Override
    public final ClientConnectionContext getConnectionContext() {
        return connectionContext;
    }
}
