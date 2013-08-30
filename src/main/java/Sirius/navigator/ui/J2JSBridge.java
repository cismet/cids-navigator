/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui;

import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.exception.ConnectionException;

import Sirius.server.middleware.types.MetaClass;
import Sirius.server.middleware.types.MetaObject;

import org.openide.util.Exceptions;

import java.util.ArrayList;

import de.cismet.cids.dynamics.CidsBean;

import de.cismet.cids.navigator.utils.ClassCacheMultiple;

/**
 * DOCUMENT ME!
 *
 * @author   daniel
 * @version  $Revision$, $Date$
 */
public class J2JSBridge {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   domain  DOCUMENT ME!
     * @param   clazz   DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getAllBeansForClass(final String domain, final String clazz) {
        // ToDo: checken ob tabell immer gleich Klassenname
        if ((domain == null) || (clazz == null) || domain.equals(clazz)) {
            return "";
        }
        final MetaClass MB_MC = ClassCacheMultiple.getMetaClass(domain, clazz);
        String query = "SELECT " + MB_MC.getID() + ", " + MB_MC.getPrimaryKey() + " ";
        query += "FROM " + MB_MC.getTableName();
        try {
            final MetaObject[] metaObjects = SessionManager.getProxy().getMetaObjectByQuery(query, 0);
            final ArrayList<CidsBean> beans = new ArrayList<CidsBean>();

            for (final MetaObject mo : metaObjects) {
                beans.add(mo.getBean());
            }
            return CidsBean.toJSONString(false, beans);
        } catch (ConnectionException ex) {
            Exceptions.printStackTrace(ex);
        }
        return "";
    }
}
