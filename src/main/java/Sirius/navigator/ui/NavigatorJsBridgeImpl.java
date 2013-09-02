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
public class NavigatorJsBridgeImpl implements NavigatorJsBridge {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   domain  DOCUMENT ME!
     * @param   clazz   classKey clazz DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getAllBeansForClass(final String domain, final String clazz) {
        return getAllObjectsOfClass(domain, clazz, null, -1, -1, null, null, null, null, null, true, null);
    }

    @Override
    public String getClass(final String domain, final String classKey, final String role, final String authorization) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
        // Tools | Templates.
    }

    @Override
    public String getAllClasses(final String domain,
            final int limit,
            final int offset,
            final String role,
            final String authorization) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
        // Tools | Templates.
    }

    @Override
    public String getClassForAttributeKey(final String domain,
            final String classKey,
            final String attributeKey,
            final String role,
            final String authorization) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
        // Tools | Templates.
    }

    @Override
    public String getEmptyInstanceOfClasspublic(final String domain,
            final String classKey,
            final String role,
            final String authorization) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
        // Tools | Templates.
    }

    @Override
    public String getAllObjectsOfClass(final String domain,
            final String classKey,
            final String role,
            final int limit,
            final int offset,
            final String expand,
            final String level,
            final String fields,
            final String profile,
            final String filter,
            final boolean omitNullValues,
            final String authorization) {
        // ToDo: checken ob tabell immer gleich Klassenname
        if ((domain == null) || (classKey == null) || domain.equals(classKey)) {
            return "";
        }
        final MetaClass MB_MC = ClassCacheMultiple.getMetaClass(domain, classKey);
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

    @Override
    public String createNewObject(final String object,
            final String domain,
            final String classKey,
            final boolean requestResultingInstance,
            final String role,
            final String auhtorization) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
        // Tools | Templates.
    }

    @Override
    public String updateOrCreateObject(final String object,
            final String domain,
            final String classKey,
            final String objectId,
            final int requestResultingInstance,
            final String role,
            final String auhtorization) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
        // Tools | Templates.
    }

    @Override
    public String deleteObject(final String domain,
            final String classKey,
            final String objectId,
            final String role,
            final String authorization) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
        // Tools | Templates.
    }

    @Override
    public String getObject(final String domain,
            final String classKey,
            final String objectId,
            final String version,
            final String role,
            final String expand,
            final String level,
            final String fields,
            final String profile,
            final boolean omitNUllValues,
            final String authorization) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
        // Tools | Templates.
    }
}
