/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui;

/**
 * DOCUMENT ME!
 *
 * @author   daniel
 * @version  $Revision$, $Date$
 */
public interface NavigatorJsBridge {

    //~ Methods ----------------------------------------------------------------

    /**
     * methods of /classes resource of cids rest api.
     *
     * @param   domain         DOCUMENT ME!
     * @param   classKey       DOCUMENT ME!
     * @param   role           DOCUMENT ME!
     * @param   authorization  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Object getClass(String domain, String classKey, String role, String authorization);

    /**
     * DOCUMENT ME!
     *
     * @param   domain         DOCUMENT ME!
     * @param   limit          DOCUMENT ME!
     * @param   offset         DOCUMENT ME!
     * @param   role           DOCUMENT ME!
     * @param   authorization  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Object getAllClasses(String domain, int limit, int offset, String role, String authorization);

    /**
     * DOCUMENT ME!
     *
     * @param   domain         DOCUMENT ME!
     * @param   classKey       DOCUMENT ME!
     * @param   attributeKey   DOCUMENT ME!
     * @param   role           DOCUMENT ME!
     * @param   authorization  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Object getAttribute(String domain,
            String classKey,
            String attributeKey,
            String role,
            String authorization);
    // methods of /enitities resource of cids rest api

    /**
     * DOCUMENT ME!
     *
     * @param   domain         DOCUMENT ME!
     * @param   classKey       DOCUMENT ME!
     * @param   role           DOCUMENT ME!
     * @param   authorization  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Object getEmptyInstanceOfClass(String domain, String classKey, String role, String authorization);

    /**
     * DOCUMENT ME!
     *
     * @param   domain          DOCUMENT ME!
     * @param   classKey        DOCUMENT ME!
     * @param   role            DOCUMENT ME!
     * @param   limit           DOCUMENT ME!
     * @param   offset          DOCUMENT ME!
     * @param   expand          DOCUMENT ME!
     * @param   level           DOCUMENT ME!
     * @param   fields          DOCUMENT ME!
     * @param   profile         DOCUMENT ME!
     * @param   filter          DOCUMENT ME!
     * @param   omitNullValues  DOCUMENT ME!
     * @param   authorization   DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Object getAllObjectsOfClass(String domain,
            String classKey,
            String role,
            int limit,
            int offset,
            String expand,
            String level,
            String fields,
            String profile,
            String filter,
            boolean omitNullValues,
            String authorization);

    /**
     * DOCUMENT ME!
     *
     * @param   object                    DOCUMENT ME!
     * @param   domain                    DOCUMENT ME!
     * @param   classKey                  DOCUMENT ME!
     * @param   requestResultingInstance  DOCUMENT ME!
     * @param   role                      DOCUMENT ME!
     * @param   auhtorization             DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Object createNewObject(String object,
            String domain,
            String classKey,
            boolean requestResultingInstance,
            String role,
            String auhtorization);

    /**
     * DOCUMENT ME!
     *
     * @param   object                    DOCUMENT ME!
     * @param   domain                    DOCUMENT ME!
     * @param   classKey                  DOCUMENT ME!
     * @param   objectId                  DOCUMENT ME!
     * @param   requestResultingInstance  DOCUMENT ME!
     * @param   role                      DOCUMENT ME!
     * @param   auhtorization             DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Object updateOrCreateObject(String object,
            String domain,
            String classKey,
            String objectId,
            boolean requestResultingInstance,
            String role,
            String auhtorization);

    /**
     * DOCUMENT ME!
     *
     * @param   domain         DOCUMENT ME!
     * @param   classKey       DOCUMENT ME!
     * @param   objectId       DOCUMENT ME!
     * @param   role           DOCUMENT ME!
     * @param   authorization  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Object deleteObject(String domain, String classKey, String objectId, String role, String authorization);

    /**
     * DOCUMENT ME!
     *
     * @param   domain          DOCUMENT ME!
     * @param   classKey        DOCUMENT ME!
     * @param   objectId        DOCUMENT ME!
     * @param   version         DOCUMENT ME!
     * @param   role            DOCUMENT ME!
     * @param   expand          DOCUMENT ME!
     * @param   level           DOCUMENT ME!
     * @param   fields          DOCUMENT ME!
     * @param   profile         DOCUMENT ME!
     * @param   omitNUllValues  DOCUMENT ME!
     * @param   authorization   DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Object getObject(String domain,
            String classKey,
            String objectId,
            String version,
            String role,
            String expand,
            String level,
            String fields,
            String profile,
            boolean omitNUllValues,
            String authorization);
}
