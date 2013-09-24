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
import Sirius.navigator.types.treenode.DefaultMetaTreeNode;
import Sirius.navigator.types.treenode.ObjectTreeNode;

import Sirius.server.middleware.types.MetaClass;
import Sirius.server.middleware.types.MetaObject;
import Sirius.server.middleware.types.MetaObjectNode;
import Sirius.server.newuser.permission.Policy;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import netscape.javascript.JSObject;

import org.apache.log4j.Logger;

import org.jfree.util.Log;

import org.openide.util.Exceptions;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Observable;

import de.cismet.cids.client.tools.DevelopmentTools;

import de.cismet.cids.dynamics.CidsBean;

import de.cismet.cids.navigator.utils.ClassCacheMultiple;

/**
 * DOCUMENT ME!
 *
 * @author   daniel
 * @version  $Revision$, $Date$
 */
public class NavigatorJsBridgeImpl extends Observable implements NavigatorJsBridge {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(NavigatorJsBridgeImpl.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    //~ Methods ----------------------------------------------------------------

    /**
     * public void updateCidsBean(String jsonBean){ this.notifyObservers(jsonBean); }.
     *
     * @param  jsonBean  DOCUMENT ME!
     */
    public void updateCidsBean(final String jsonBean) {
        try {
            final CidsBean bean = CidsBean.createNewCidsBeanFromJSON(false, jsonBean);
            this.setChanged();
            this.notifyObservers(bean);
        } catch (Exception ex) {
            Log.error("Could not create cidsBean of jsonString: " + jsonBean, ex);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void showHTMLComponent() {
        LOG.fatal("Angular app completely initialised");
        this.setChanged();
        this.notifyObservers("showHTML");
    }

    /**
     * DOCUMENT ME!
     */
    public void setChangeFlag() {
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * DOCUMENT ME!
     *
     * @param   domain    DOCUMENT ME!
     * @param   classKey  DOCUMENT ME!
     * @param   options   DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Object getClass(final String domain, final String classKey, final JSObject options) {
        return this.getClass(domain, classKey, null, null);
    }

    @Override
    public Object getClass(final String domain,
            final String classKey,
            final String role,
            final String authorization) {
        if (((role != null) && !role.equals("")) || ((authorization != null) && !authorization.equals(""))) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("parameters role and authorization are not supported at the moment");
            }
        }
        final MetaClass mc = ClassCacheMultiple.getMetaClass(domain, classKey);
        /*
         * ToDo: Check if we need to convert the object in an JSON object. Advantage: returning the MetaClass directly :
         * only pulbic fields and methods are acessible Disadvantage:  type conversion problems, access?
         */
        return mc;
    }

    @Override
    public Object getAllClasses(final String domain,
            final int limit,
            final int offset,
            final String role,
            final String authorization) {
        if (((role != null) && !role.equals("")) || ((authorization != null) && !authorization.equals(""))) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("parameters role and authorization are not supported at the moment");
            }
        }
        final HashMap map = ClassCacheMultiple.getTableNameHashtableOfClassesForOneDomain(domain);
        // naive approach: split the map into parts with size of limit and return the partial map at position offset
        // Problem: needs a kind of sorting to work properly
        if ((limit > 0) && (offset > 0) && (map.size() > limit)) {
            int splitSize = 0;
            int arrPos = 0;
            final int arraySize = ((map.size() % limit) == 0) ? (map.size() / limit) : ((map.size() / limit) + 1);
            final HashMap[] splittedMap = new HashMap[arraySize];
            HashMap partialMap = new HashMap();
            for (final Object key : map.keySet()) {
                if (splitSize == limit) {
                    splittedMap[arrPos] = partialMap;
                    partialMap = new HashMap();
                    splitSize = 0;
                    arrPos++;
                }
                partialMap.put(key, map.get(key));
                splitSize++;
            }

            if (offset > splittedMap.length) {
                return splittedMap[splittedMap.length - 1];
            }
            return splittedMap[offset - 1];
        }
        /*
         * Disadvantage here: in java script we must call map.get(key) instead of map.key directly...
         */
// return map;

        String jsonMap = "";
        try {
            jsonMap = mapper.writeValueAsString(map);
        } catch (JsonGenerationException ex) {
            Exceptions.printStackTrace(ex);
        } catch (JsonMappingException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return jsonMap;
    }

    @Override
    public Object getAttribute(final String domain,
            final String classKey,
            final String attributeKey,
            final String role,
            final String authorization) {
        if (((role != null) && !role.equals("")) || ((authorization != null) && !authorization.equals(""))) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("parameters role and authorization are not supported at the moment");
            }
        }
        final MetaClass mc = (MetaClass)getClass(domain, classKey, role, authorization);
        final Collection c = mc.getAttributeByName(attributeKey);
        return c;
    }

    @Override
    public Object getEmptyInstanceOfClass(final String domain,
            final String classKey,
            final String role,
            final String authorization) {
        if (((role != null) && !role.equals("")) || ((authorization != null) && !authorization.equals(""))) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("parameters role and authorization are not supported at the moment");
            }
        }
        try {
            return CidsBean.constructNew(SessionManager.getProxy().getCallServerService(),
                    SessionManager.getSession().getUser(),
                    domain,
                    classKey);
        } catch (Exception ex) {
            LOG.error("Error during creation of new CidsBean", ex);
        }
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   domain    DOCUMENT ME!
     * @param   classKey  DOCUMENT ME!
     * @param   options   DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Object getAllObjectsOfClass(final String domain, final String classKey, final JSObject options) {
        return this.getAllObjectsOfClass(domain, classKey, null, 0, 0, null, null, null, null, null, false, null);
    }

    @Override
    public Object getAllObjectsOfClass(final String domain,
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
        if ((role != null) || (expand != null) || (level != null) || (fields != null)
                    || (profile != null)
                    || (filter != null)
                    || (authorization != null)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(
                    "at the moment only domain and classKey parameter are taken into account. ALL other parameters are ignored");
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("searching all Objects for domain / class: " + domain + "/" + classKey);
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
//            return beans.toArray();
        } catch (ConnectionException ex) {
            LOG.error("can not fetch meta objects / cidsBeans for class/domain " + domain + "/" + classKey, ex);
        }
        return "";
    }

    @Override
    public Object createNewObject(final String object,
            final String domain,
            final String classKey,
            final boolean requestResultingInstance,
            final String role,
            final String authorization) {
        if (((role != null) && !role.equals("")) || ((authorization != null) && !authorization.equals(""))) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("parameters role and authorization are not supported at the moment");
            }
        }
        try {
            final CidsBean newBean = CidsBean.constructNew(SessionManager.getProxy().getCallServerService(),
                    SessionManager.getSession().getUser(),
                    domain,
                    classKey);

            final CidsBean b = CidsBean.createNewCidsBeanFromJSON(false, object);
            final MetaObject metaObject = b.getMetaObject();
            metaObject.setStatus(MetaObject.NEW);
            final MetaObjectNode MetaObjectNode = new MetaObjectNode(
                    -1,
                    SessionManager.getSession().getUser().getDomain(),
                    metaObject,
                    null,
                    null,
                    true,
                    Policy.createWIKIPolicy(),
                    -1,
                    null,
                    false);
            final DefaultMetaTreeNode metaTreeNode = new ObjectTreeNode(MetaObjectNode);
            final CidsBean savedInstance = b.persist();
            ((ObjectTreeNode)metaTreeNode).setMetaObject(savedInstance.getMetaObject());

            if (requestResultingInstance) {
                return savedInstance;
            } else {
                return null;
            }

//
        } catch (JsonParseException ex) {
            Exceptions.printStackTrace(ex);
        } catch (JsonMappingException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object updateOrCreateObject(final String object,
            final String domain,
            final String classKey,
            final String objectId,
            final boolean requestResultingInstance,
            final String role,
            final String authorization) {
        if (((role != null) && !role.equals("")) || ((authorization != null) && !authorization.equals(""))) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("parameters role and authorization are not supported at the moment");
            }
        }
        try {
            final CidsBean b = CidsBean.createNewCidsBeanFromJSON(false, object);
            final CidsBean savedbean = b.persist();
            if (requestResultingInstance) {
                return savedbean;
            }
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
        return null;
    }

    @Override
    public Object deleteObject(final String domain,
            final String classKey,
            final String objectId,
            final String role,
            final String authorization) {
        if (((role != null) && !role.equals("")) || ((authorization != null) && !authorization.equals(""))) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("parameters role and authorization are not supported at the moment");
            }
        }
        try {
            final MetaClass mc = ClassCacheMultiple.getMetaClass(domain, classKey);
            final Integer objId = Integer.parseInt(objectId);
            final MetaObject mo = SessionManager.getProxy().getMetaObject(objId, mc.getId(), domain);
            return SessionManager.getProxy().deleteMetaObject(mo, domain);
        } catch (ConnectionException ex) {
            Exceptions.printStackTrace(ex);
        }
        return false;
    }

    @Override
    public Object getObject(final String domain,
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
        if (((role != null) && !role.equals("")) || ((authorization != null) && !authorization.equals(""))) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("parameters role and authorization are not supported at the moment");
            }
        }
        try {
            final MetaClass mc = ClassCacheMultiple.getMetaClass(domain, classKey);
            final Integer objId = Integer.parseInt(objectId);
            final MetaObject mo = SessionManager.getProxy().getMetaObject(objId, mc.getId(), domain);
            final CidsBean bean = mo.getBean();
            return CidsBean.getCidsBeanObjectMapper().writeValueAsString(bean);
                // Tools | Templates.
        } catch (ConnectionException ex) {
            LOG.error("Could not retrive object " + domain + "." + classKey + "." + objectId);
        } catch (JsonGenerationException ex) {
            Exceptions.printStackTrace(ex);
        } catch (JsonMappingException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  args  DOCUMENT ME!
     */
    public static void main(final String[] args) {
//        test the create Object Method...
        final NavigatorJsBridgeImpl bridge = new NavigatorJsBridgeImpl();
        final String className = "VERMESSUNG_RISS";
        final String domain = "WUNDA_BLAU";
        try {
            final CidsBean b = DevelopmentTools.createCidsBeanFromRMIConnectionOnLocalhost(
                    domain,
                    "Administratoren",
                    "admin",
                    "kif",
                    className,
                    10);
            final String jsonObj = "{\n"
                        + "  \"$self\" : \"/WUNDA_BLAU.VERMESSUNG_RISS/10\",\n"
                        + "  \"id\" : 10,\n"
                        + "  \"gemarkung\" : {\n"
                        + "    \"$self\" : \"/WUNDA_BLAU.VERMESSUNG_GEMARKUNG/0\",\n"
                        + "    \"id\" : 0,\n"
                        + "    \"name\" : \"Gemarkung nicht definiert\",\n"
                        + "    \"historisch\" : null\n"
                        + "  },\n"
                        + "  \"flur\" : \"000\",\n"
                        + "  \"blatt\" : \"868729\",\n"
                        + "  \"schluessel\" : \"505\",\n"
                        + "  \"jahr\" : null,\n"
                        + "  \"letzteaenderung_datum\" : \"2013-02-05\",\n"
                        + "  \"letzteaenderung_name\" : \"GoehrkeJ102\",\n"
                        + "  \"format\" : {\n"
                        + "    \"$self\" : \"/WUNDA_BLAU.VERMESSUNG_FORMAT/9\",\n"
                        + "    \"id\" : 9,\n"
                        + "    \"name\" : \"DIN A2\"\n"
                        + "  },\n"
                        + "  \"geometrie\" : {\n"
                        + "    \"$self\" : \"/WUNDA_BLAU.GEOM/600355771\",\n"
                        + "    \"id\" : 600355771,\n"
                        + "    \"geo_field\" : \"POLYGON ((377634.84346477 5686017.2300428655, 377735.8467138979 5686013.314708927, 377739.66336811316 5686138.315920094, 377639.9636269501 5686142.394419077, 377634.84346477 5686017.2300428655))\"\n"
                        + "  },\n"
                        + "  \"bild\" : \"Vermessungsrisse\\\\0000\\\\VR_505-0000-000-00868729\",\n"
                        + "  \"grenzniederschrift\" : null,\n"
                        + "  \"kennziffer\" : null,\n"
                        + "  \"geometrie_status\" : {\n"
                        + "    \"$self\" : \"/WUNDA_BLAU.VERMESSUNG_GEOMETRIE_STATUS/6\",\n"
                        + "    \"id\" : 6,\n"
                        + "    \"name\" : \"optimierte Geometrie\"\n"
                        + "  },\n"
                        + "  \"flurstuecksvermessung\" : [ {\n"
                        + "    \"$self\" : \"/WUNDA_BLAU.VERMESSUNG_FLURSTUECKSVERMESSUNG/3731\",\n"
                        + "    \"id\" : 3731,\n"
                        + "    \"flurstueck\" : {\n"
                        + "      \"$self\" : \"/WUNDA_BLAU.VERMESSUNG_FLURSTUECK_KICKER/3091\",\n"
                        + "      \"id\" : 3091,\n"
                        + "      \"gemarkung\" : {\n"
                        + "        \"$self\" : \"/WUNDA_BLAU.VERMESSUNG_GEMARKUNG/3487\",\n"
                        + "        \"id\" : 3487,\n"
                        + "        \"name\" : \"Nächstebreck\",\n"
                        + "        \"historisch\" : null\n"
                        + "      },\n"
                        + "      \"flur\" : \"390\",\n"
                        + "      \"zaehler\" : \"14\",\n"
                        + "      \"nenner\" : \"0\",\n"
                        + "      \"flurstueck\" : {\n"
                        + "        \"$self\" : \"/WUNDA_BLAU.FLURSTUECK/69417\",\n"
                        + "        \"id\" : 69417,\n"
                        + "        \"gemarkungs_nr\" : {\n"
                        + "          \"$self\" : \"/WUNDA_BLAU.GEMARKUNG/3487\",\n"
                        + "          \"gemarkungsnummer\" : 3487,\n"
                        + "          \"name\" : \"Nächstebreck\"\n"
                        + "        },\n"
                        + "        \"flur\" : \"390\",\n"
                        + "        \"fstnr_z\" : 14,\n"
                        + "        \"fstnr_n\" : 0,\n"
                        + "        \"alkis_id\" : \"053487-390-00014\",\n"
                        + "        \"dienststellen\" : \"privat\",\n"
                        + "        \"historisch\" : null,\n"
                        + "        \"umschreibendes_rechteck\" : {\n"
                        + "          \"$self\" : \"/WUNDA_BLAU.GEOM/706533601\",\n"
                        + "          \"id\" : 706533601,\n"
                        + "          \"geo_field\" : \"POLYGON ((377642.322999999 5686031.761, 377687.853 5686042.257, 377697.015000001 5686008.004, 377657.175000001 5685997.484, 377654.122000001 5685996.678, 377642.322999999 5686031.761))\"\n"
                        + "        },\n"
                        + "        \"fortfuehrungsnummer\" : null\n"
                        + "      },\n"
                        + "      \"historisch\" : null\n"
                        + "    },\n"
                        + "    \"veraenderungsart\" : {\n"
                        + "      \"$self\" : \"/WUNDA_BLAU.VERMESSUNG_ART/1\",\n"
                        + "      \"id\" : 1,\n"
                        + "      \"code\" : \"TV\",\n"
                        + "      \"name\" : \"Teilungsvermessung\"\n"
                        + "    }\n"
                        + "  }, {\n"
                        + "    \"$self\" : \"/WUNDA_BLAU.VERMESSUNG_FLURSTUECKSVERMESSUNG/3732\",\n"
                        + "    \"id\" : 3732,\n"
                        + "    \"flurstueck\" : {\n"
                        + "      \"$self\" : \"/WUNDA_BLAU.VERMESSUNG_FLURSTUECK_KICKER/3115\",\n"
                        + "      \"id\" : 3115,\n"
                        + "      \"gemarkung\" : {\n"
                        + "        \"$self\" : \"/WUNDA_BLAU.VERMESSUNG_GEMARKUNG/3487\",\n"
                        + "        \"id\" : 3487,\n"
                        + "        \"name\" : \"Nächstebreck\",\n"
                        + "        \"historisch\" : null\n"
                        + "      },\n"
                        + "      \"flur\" : \"390\",\n"
                        + "      \"zaehler\" : \"15\",\n"
                        + "      \"nenner\" : \"0\",\n"
                        + "      \"flurstueck\" : {\n"
                        + "        \"$self\" : \"/WUNDA_BLAU.FLURSTUECK/41521\",\n"
                        + "        \"id\" : 41521,\n"
                        + "        \"gemarkungs_nr\" : {\n"
                        + "          \"$self\" : \"/WUNDA_BLAU.GEMARKUNG/3487\",\n"
                        + "          \"gemarkungsnummer\" : 3487,\n"
                        + "          \"name\" : \"Nächstebreck\"\n"
                        + "        },\n"
                        + "        \"flur\" : \"390\",\n"
                        + "        \"fstnr_z\" : 15,\n"
                        + "        \"fstnr_n\" : 0,\n"
                        + "        \"alkis_id\" : \"053487-390-00015\",\n"
                        + "        \"dienststellen\" : \"privat\",\n"
                        + "        \"historisch\" : null,\n"
                        + "        \"umschreibendes_rechteck\" : {\n"
                        + "          \"$self\" : \"/WUNDA_BLAU.GEOM/706505705\",\n"
                        + "          \"id\" : 706505705,\n"
                        + "          \"geo_field\" : \"POLYGON ((377642.322999999 5686031.761, 377629.283 5686061.701, 377680.269000001 5686070.593, 377687.853 5686042.257, 377642.322999999 5686031.761))\"\n"
                        + "        },\n"
                        + "        \"fortfuehrungsnummer\" : null\n"
                        + "      },\n"
                        + "      \"historisch\" : null\n"
                        + "    },\n"
                        + "    \"veraenderungsart\" : {\n"
                        + "      \"$self\" : \"/WUNDA_BLAU.VERMESSUNG_ART/1\",\n"
                        + "      \"id\" : 1,\n"
                        + "      \"code\" : \"TV\",\n"
                        + "      \"name\" : \"Teilungsvermessung\"\n"
                        + "    }\n"
                        + "  }, {\n"
                        + "    \"$self\" : \"/WUNDA_BLAU.VERMESSUNG_FLURSTUECKSVERMESSUNG/3733\",\n"
                        + "    \"id\" : 3733,\n"
                        + "    \"flurstueck\" : {\n"
                        + "      \"$self\" : \"/WUNDA_BLAU.VERMESSUNG_FLURSTUECK_KICKER/3092\",\n"
                        + "      \"id\" : 3092,\n"
                        + "      \"gemarkung\" : {\n"
                        + "        \"$self\" : \"/WUNDA_BLAU.VERMESSUNG_GEMARKUNG/3487\",\n"
                        + "        \"id\" : 3487,\n"
                        + "        \"name\" : \"Nächstebreck\",\n"
                        + "        \"historisch\" : null\n"
                        + "      },\n"
                        + "      \"flur\" : \"390\",\n"
                        + "      \"zaehler\" : \"16\",\n"
                        + "      \"nenner\" : \"0\",\n"
                        + "      \"flurstueck\" : {\n"
                        + "        \"$self\" : \"/WUNDA_BLAU.FLURSTUECK/32297\",\n"
                        + "        \"id\" : 32297,\n"
                        + "        \"gemarkungs_nr\" : {\n"
                        + "          \"$self\" : \"/WUNDA_BLAU.GEMARKUNG/3487\",\n"
                        + "          \"gemarkungsnummer\" : 3487,\n"
                        + "          \"name\" : \"Nächstebreck\"\n"
                        + "        },\n"
                        + "        \"flur\" : \"390\",\n"
                        + "        \"fstnr_z\" : 16,\n"
                        + "        \"fstnr_n\" : 0,\n"
                        + "        \"alkis_id\" : \"053487-390-00016\",\n"
                        + "        \"dienststellen\" : \"privat\",\n"
                        + "        \"historisch\" : null,\n"
                        + "        \"umschreibendes_rechteck\" : {\n"
                        + "          \"$self\" : \"/WUNDA_BLAU.GEOM/706496481\",\n"
                        + "          \"id\" : 706496481,\n"
                        + "          \"geo_field\" : \"POLYGON ((377625.736000001 5686061.085, 377625.901999999 5686064.65, 377679.850000001 5686074.062, 377680.269000001 5686070.593, 377629.283 5686061.701, 377642.322999999 5686031.761, 377654.122000001 5685996.678, 377650.734000001 5685995.786, 377639.059999999 5686030.503, 377628.156999998 5686055.526, 377625.736000001 5686061.085))\"\n"
                        + "        },\n"
                        + "        \"fortfuehrungsnummer\" : null\n"
                        + "      },\n"
                        + "      \"historisch\" : null\n"
                        + "    },\n"
                        + "    \"veraenderungsart\" : {\n"
                        + "      \"$self\" : \"/WUNDA_BLAU.VERMESSUNG_ART/1\",\n"
                        + "      \"id\" : 1,\n"
                        + "      \"code\" : \"TV\",\n"
                        + "      \"name\" : \"Teilungsvermessung\"\n"
                        + "    }\n"
                        + "  }, {\n"
                        + "    \"$self\" : \"/WUNDA_BLAU.VERMESSUNG_FLURSTUECKSVERMESSUNG/3734\",\n"
                        + "    \"id\" : 3734,\n"
                        + "    \"flurstueck\" : {\n"
                        + "      \"$self\" : \"/WUNDA_BLAU.VERMESSUNG_FLURSTUECK_KICKER/3116\",\n"
                        + "      \"id\" : 3116,\n"
                        + "      \"gemarkung\" : {\n"
                        + "        \"$self\" : \"/WUNDA_BLAU.VERMESSUNG_GEMARKUNG/3487\",\n"
                        + "        \"id\" : 3487,\n"
                        + "        \"name\" : \"Nächstebreck\",\n"
                        + "        \"historisch\" : null\n"
                        + "      },\n"
                        + "      \"flur\" : \"390\",\n"
                        + "      \"zaehler\" : \"17\",\n"
                        + "      \"nenner\" : \"0\",\n"
                        + "      \"flurstueck\" : {\n"
                        + "        \"$self\" : \"/WUNDA_BLAU.FLURSTUECK/75896\",\n"
                        + "        \"id\" : 75896,\n"
                        + "        \"gemarkungs_nr\" : {\n"
                        + "          \"$self\" : \"/WUNDA_BLAU.GEMARKUNG/3487\",\n"
                        + "          \"gemarkungsnummer\" : 3487,\n"
                        + "          \"name\" : \"Nächstebreck\"\n"
                        + "        },\n"
                        + "        \"flur\" : \"390\",\n"
                        + "        \"fstnr_z\" : 17,\n"
                        + "        \"fstnr_n\" : 0,\n"
                        + "        \"alkis_id\" : \"053487-390-00017\",\n"
                        + "        \"dienststellen\" : \"privat\",\n"
                        + "        \"historisch\" : null,\n"
                        + "        \"umschreibendes_rechteck\" : {\n"
                        + "          \"$self\" : \"/WUNDA_BLAU.GEOM/706540080\",\n"
                        + "          \"id\" : 706540080,\n"
                        + "          \"geo_field\" : \"POLYGON ((377609.263 5686131.727, 377612.702 5686132.493, 377671.350000001 5686145.558, 377679.850000001 5686074.062, 377625.901999999 5686064.65, 377626.065000001 5686068.156, 377622.998 5686105.231, 377609.835000001 5686124.43, 377609.263 5686131.727))\"\n"
                        + "        },\n"
                        + "        \"fortfuehrungsnummer\" : null\n"
                        + "      },\n"
                        + "      \"historisch\" : null\n"
                        + "    },\n"
                        + "    \"veraenderungsart\" : {\n"
                        + "      \"$self\" : \"/WUNDA_BLAU.VERMESSUNG_ART/1\",\n"
                        + "      \"id\" : 1,\n"
                        + "      \"code\" : \"TV\",\n"
                        + "      \"name\" : \"Teilungsvermessung\"\n"
                        + "    }\n"
                        + "  }, {\n"
                        + "    \"$self\" : \"/WUNDA_BLAU.VERMESSUNG_FLURSTUECKSVERMESSUNG/3735\",\n"
                        + "    \"id\" : 3735,\n"
                        + "    \"flurstueck\" : {\n"
                        + "      \"$self\" : \"/WUNDA_BLAU.VERMESSUNG_FLURSTUECK_KICKER/3117\",\n"
                        + "      \"id\" : 3117,\n"
                        + "      \"gemarkung\" : {\n"
                        + "        \"$self\" : \"/WUNDA_BLAU.VERMESSUNG_GEMARKUNG/3487\",\n"
                        + "        \"id\" : 3487,\n"
                        + "        \"name\" : \"Nächstebreck\",\n"
                        + "        \"historisch\" : null\n"
                        + "      },\n"
                        + "      \"flur\" : \"390\",\n"
                        + "      \"zaehler\" : \"18\",\n"
                        + "      \"nenner\" : \"0\",\n"
                        + "      \"flurstueck\" : {\n"
                        + "        \"$self\" : \"/WUNDA_BLAU.FLURSTUECK/58093\",\n"
                        + "        \"id\" : 58093,\n"
                        + "        \"gemarkungs_nr\" : {\n"
                        + "          \"$self\" : \"/WUNDA_BLAU.GEMARKUNG/3487\",\n"
                        + "          \"gemarkungsnummer\" : 3487,\n"
                        + "          \"name\" : \"Nächstebreck\"\n"
                        + "        },\n"
                        + "        \"flur\" : \"390\",\n"
                        + "        \"fstnr_z\" : 18,\n"
                        + "        \"fstnr_n\" : 0,\n"
                        + "        \"alkis_id\" : \"053487-390-00018\",\n"
                        + "        \"dienststellen\" : \"privat\",\n"
                        + "        \"historisch\" : null,\n"
                        + "        \"umschreibendes_rechteck\" : {\n"
                        + "          \"$self\" : \"/WUNDA_BLAU.GEOM/706522277\",\n"
                        + "          \"id\" : 706522277,\n"
                        + "          \"geo_field\" : \"POLYGON ((377671.350000001 5686145.558, 377690.120999999 5686149.744, 377698.601999998 5686151.628, 377702.502999999 5686124.694, 377706.987 5686093.748, 377709.219000001 5686078.335, 377694.754000001 5686074.463, 377680.269000001 5686070.593, 377679.850000001 5686074.062, 377671.350000001 5686145.558))\"\n"
                        + "        },\n"
                        + "        \"fortfuehrungsnummer\" : null\n"
                        + "      },\n"
                        + "      \"historisch\" : null\n"
                        + "    },\n"
                        + "    \"veraenderungsart\" : {\n"
                        + "      \"$self\" : \"/WUNDA_BLAU.VERMESSUNG_ART/1\",\n"
                        + "      \"id\" : 1,\n"
                        + "      \"code\" : \"TV\",\n"
                        + "      \"name\" : \"Teilungsvermessung\"\n"
                        + "    }\n"
                        + "  }, {\n"
                        + "    \"$self\" : \"/WUNDA_BLAU.VERMESSUNG_FLURSTUECKSVERMESSUNG/3736\",\n"
                        + "    \"id\" : 3736,\n"
                        + "    \"flurstueck\" : {\n"
                        + "      \"$self\" : \"/WUNDA_BLAU.VERMESSUNG_FLURSTUECK_KICKER/3093\",\n"
                        + "      \"id\" : 3093,\n"
                        + "      \"gemarkung\" : {\n"
                        + "        \"$self\" : \"/WUNDA_BLAU.VERMESSUNG_GEMARKUNG/3487\",\n"
                        + "        \"id\" : 3487,\n"
                        + "        \"name\" : \"Nächstebreck\",\n"
                        + "        \"historisch\" : null\n"
                        + "      },\n"
                        + "      \"flur\" : \"390\",\n"
                        + "      \"zaehler\" : \"19\",\n"
                        + "      \"nenner\" : \"0\",\n"
                        + "      \"flurstueck\" : {\n"
                        + "        \"$self\" : \"/WUNDA_BLAU.FLURSTUECK/92674\",\n"
                        + "        \"id\" : 92674,\n"
                        + "        \"gemarkungs_nr\" : {\n"
                        + "          \"$self\" : \"/WUNDA_BLAU.GEMARKUNG/3487\",\n"
                        + "          \"gemarkungsnummer\" : 3487,\n"
                        + "          \"name\" : \"Nächstebreck\"\n"
                        + "        },\n"
                        + "        \"flur\" : \"390\",\n"
                        + "        \"fstnr_z\" : 19,\n"
                        + "        \"fstnr_n\" : 0,\n"
                        + "        \"alkis_id\" : \"053487-390-00019\",\n"
                        + "        \"dienststellen\" : \"privat\",\n"
                        + "        \"historisch\" : null,\n"
                        + "        \"umschreibendes_rechteck\" : {\n"
                        + "          \"$self\" : \"/WUNDA_BLAU.GEOM/706556858\",\n"
                        + "          \"id\" : 706556858,\n"
                        + "          \"geo_field\" : \"POLYGON ((377680.269000001 5686070.593, 377694.754000001 5686074.463, 377711.487999998 5686011.825, 377706.997000001 5686010.64, 377705.585000001 5686010.266, 377697.015000001 5686008.004, 377687.853 5686042.257, 377680.269000001 5686070.593))\"\n"
                        + "        },\n"
                        + "        \"fortfuehrungsnummer\" : null\n"
                        + "      },\n"
                        + "      \"historisch\" : null\n"
                        + "    },\n"
                        + "    \"veraenderungsart\" : {\n"
                        + "      \"$self\" : \"/WUNDA_BLAU.VERMESSUNG_ART/1\",\n"
                        + "      \"id\" : 1,\n"
                        + "      \"code\" : \"TV\",\n"
                        + "      \"name\" : \"Teilungsvermessung\"\n"
                        + "    }\n"
                        + "  }, {\n"
                        + "    \"$self\" : \"/WUNDA_BLAU.VERMESSUNG_FLURSTUECKSVERMESSUNG/3737\",\n"
                        + "    \"id\" : 3737,\n"
                        + "    \"flurstueck\" : {\n"
                        + "      \"$self\" : \"/WUNDA_BLAU.VERMESSUNG_FLURSTUECK_KICKER/3094\",\n"
                        + "      \"id\" : 3094,\n"
                        + "      \"gemarkung\" : {\n"
                        + "        \"$self\" : \"/WUNDA_BLAU.VERMESSUNG_GEMARKUNG/3487\",\n"
                        + "        \"id\" : 3487,\n"
                        + "        \"name\" : \"Nächstebreck\",\n"
                        + "        \"historisch\" : null\n"
                        + "      },\n"
                        + "      \"flur\" : \"390\",\n"
                        + "      \"zaehler\" : \"20\",\n"
                        + "      \"nenner\" : \"0\",\n"
                        + "      \"flurstueck\" : {\n"
                        + "        \"$self\" : \"/WUNDA_BLAU.FLURSTUECK/35061\",\n"
                        + "        \"id\" : 35061,\n"
                        + "        \"gemarkungs_nr\" : {\n"
                        + "          \"$self\" : \"/WUNDA_BLAU.GEMARKUNG/3487\",\n"
                        + "          \"gemarkungsnummer\" : 3487,\n"
                        + "          \"name\" : \"Nächstebreck\"\n"
                        + "        },\n"
                        + "        \"flur\" : \"390\",\n"
                        + "        \"fstnr_z\" : 20,\n"
                        + "        \"fstnr_n\" : 0,\n"
                        + "        \"alkis_id\" : \"053487-390-00020\",\n"
                        + "        \"dienststellen\" : \"privat\",\n"
                        + "        \"historisch\" : null,\n"
                        + "        \"umschreibendes_rechteck\" : {\n"
                        + "          \"$self\" : \"/WUNDA_BLAU.GEOM/706499245\",\n"
                        + "          \"id\" : 706499245,\n"
                        + "          \"geo_field\" : \"POLYGON ((377694.754000001 5686074.463, 377709.219000001 5686078.335, 377725.980999999 5686015.646, 377723.115000002 5686014.89, 377711.487999998 5686011.825, 377694.754000001 5686074.463))\"\n"
                        + "        },\n"
                        + "        \"fortfuehrungsnummer\" : null\n"
                        + "      },\n"
                        + "      \"historisch\" : null\n"
                        + "    },\n"
                        + "    \"veraenderungsart\" : {\n"
                        + "      \"$self\" : \"/WUNDA_BLAU.VERMESSUNG_ART/1\",\n"
                        + "      \"id\" : 1,\n"
                        + "      \"code\" : \"TV\",\n"
                        + "      \"name\" : \"Teilungsvermessung\"\n"
                        + "    }\n"
                        + "  }, {\n"
                        + "    \"$self\" : \"/WUNDA_BLAU.VERMESSUNG_FLURSTUECKSVERMESSUNG/3738\",\n"
                        + "    \"id\" : 3738,\n"
                        + "    \"flurstueck\" : {\n"
                        + "      \"$self\" : \"/WUNDA_BLAU.VERMESSUNG_FLURSTUECK_KICKER/3118\",\n"
                        + "      \"id\" : 3118,\n"
                        + "      \"gemarkung\" : {\n"
                        + "        \"$self\" : \"/WUNDA_BLAU.VERMESSUNG_GEMARKUNG/3487\",\n"
                        + "        \"id\" : 3487,\n"
                        + "        \"name\" : \"Nächstebreck\",\n"
                        + "        \"historisch\" : null\n"
                        + "      },\n"
                        + "      \"flur\" : \"390\",\n"
                        + "      \"zaehler\" : \"21\",\n"
                        + "      \"nenner\" : \"0\",\n"
                        + "      \"flurstueck\" : {\n"
                        + "        \"$self\" : \"/WUNDA_BLAU.FLURSTUECK/69844\",\n"
                        + "        \"id\" : 69844,\n"
                        + "        \"gemarkungs_nr\" : {\n"
                        + "          \"$self\" : \"/WUNDA_BLAU.GEMARKUNG/3487\",\n"
                        + "          \"gemarkungsnummer\" : 3487,\n"
                        + "          \"name\" : \"Nächstebreck\"\n"
                        + "        },\n"
                        + "        \"flur\" : \"390\",\n"
                        + "        \"fstnr_z\" : 21,\n"
                        + "        \"fstnr_n\" : 0,\n"
                        + "        \"alkis_id\" : \"053487-390-00021\",\n"
                        + "        \"dienststellen\" : \"privat\",\n"
                        + "        \"historisch\" : null,\n"
                        + "        \"umschreibendes_rechteck\" : {\n"
                        + "          \"$self\" : \"/WUNDA_BLAU.GEOM/706534028\",\n"
                        + "          \"id\" : 706534028,\n"
                        + "          \"geo_field\" : \"POLYGON ((377698.601999998 5686151.628, 377723.188000001 5686157.107, 377726.353 5686127.095, 377729.620999999 5686096.11, 377732.421 5686069.562, 377715.671999998 5686065.144, 377728.691 5686016.365, 377725.980999999 5686015.646, 377709.219000001 5686078.335, 377706.987 5686093.748, 377702.502999999 5686124.694, 377698.601999998 5686151.628))\"\n"
                        + "        },\n"
                        + "        \"fortfuehrungsnummer\" : null\n"
                        + "      },\n"
                        + "      \"historisch\" : null\n"
                        + "    },\n"
                        + "    \"veraenderungsart\" : {\n"
                        + "      \"$self\" : \"/WUNDA_BLAU.VERMESSUNG_ART/1\",\n"
                        + "      \"id\" : 1,\n"
                        + "      \"code\" : \"TV\",\n"
                        + "      \"name\" : \"Teilungsvermessung\"\n"
                        + "    }\n"
                        + "  }, {\n"
                        + "    \"$self\" : \"/WUNDA_BLAU.VERMESSUNG_FLURSTUECKSVERMESSUNG/3739\",\n"
                        + "    \"id\" : 3739,\n"
                        + "    \"flurstueck\" : {\n"
                        + "      \"$self\" : \"/WUNDA_BLAU.VERMESSUNG_FLURSTUECK_KICKER/3083\",\n"
                        + "      \"id\" : 3083,\n"
                        + "      \"gemarkung\" : {\n"
                        + "        \"$self\" : \"/WUNDA_BLAU.VERMESSUNG_GEMARKUNG/3487\",\n"
                        + "        \"id\" : 3487,\n"
                        + "        \"name\" : \"Nächstebreck\",\n"
                        + "        \"historisch\" : null\n"
                        + "      },\n"
                        + "      \"flur\" : \"390\",\n"
                        + "      \"zaehler\" : \"22\",\n"
                        + "      \"nenner\" : \"0\",\n"
                        + "      \"flurstueck\" : {\n"
                        + "        \"$self\" : \"/WUNDA_BLAU.FLURSTUECK/16169\",\n"
                        + "        \"id\" : 16169,\n"
                        + "        \"gemarkungs_nr\" : {\n"
                        + "          \"$self\" : \"/WUNDA_BLAU.GEMARKUNG/3487\",\n"
                        + "          \"gemarkungsnummer\" : 3487,\n"
                        + "          \"name\" : \"Nächstebreck\"\n"
                        + "        },\n"
                        + "        \"flur\" : \"390\",\n"
                        + "        \"fstnr_z\" : 22,\n"
                        + "        \"fstnr_n\" : 0,\n"
                        + "        \"alkis_id\" : \"053487-390-00022\",\n"
                        + "        \"dienststellen\" : \"privat\",\n"
                        + "        \"historisch\" : null,\n"
                        + "        \"umschreibendes_rechteck\" : {\n"
                        + "          \"$self\" : \"/WUNDA_BLAU.GEOM/706480353\",\n"
                        + "          \"id\" : 706480353,\n"
                        + "          \"geo_field\" : \"POLYGON ((377715.671999998 5686065.144, 377732.421 5686069.562, 377732.783 5686066.122, 377734.210000001 5686052.567, 377734.530999999 5686049.514, 377735.870999999 5686036.793, 377737.923999999 5686029.071, 377740.473999999 5686019.476, 377737.774 5686018.766, 377733.355 5686017.598, 377728.691 5686016.365, 377715.671999998 5686065.144))\"\n"
                        + "        },\n"
                        + "        \"fortfuehrungsnummer\" : null\n"
                        + "      },\n"
                        + "      \"historisch\" : null\n"
                        + "    },\n"
                        + "    \"veraenderungsart\" : {\n"
                        + "      \"$self\" : \"/WUNDA_BLAU.VERMESSUNG_ART/1\",\n"
                        + "      \"id\" : 1,\n"
                        + "      \"code\" : \"TV\",\n"
                        + "      \"name\" : \"Teilungsvermessung\"\n"
                        + "    }\n"
                        + "  }, {\n"
                        + "    \"$self\" : \"/WUNDA_BLAU.VERMESSUNG_FLURSTUECKSVERMESSUNG/3740\",\n"
                        + "    \"id\" : 3740,\n"
                        + "    \"flurstueck\" : {\n"
                        + "      \"$self\" : \"/WUNDA_BLAU.VERMESSUNG_FLURSTUECK_KICKER/3084\",\n"
                        + "      \"id\" : 3084,\n"
                        + "      \"gemarkung\" : {\n"
                        + "        \"$self\" : \"/WUNDA_BLAU.VERMESSUNG_GEMARKUNG/3487\",\n"
                        + "        \"id\" : 3487,\n"
                        + "        \"name\" : \"Nächstebreck\",\n"
                        + "        \"historisch\" : null\n"
                        + "      },\n"
                        + "      \"flur\" : \"390\",\n"
                        + "      \"zaehler\" : \"23\",\n"
                        + "      \"nenner\" : \"0\",\n"
                        + "      \"flurstueck\" : {\n"
                        + "        \"$self\" : \"/WUNDA_BLAU.FLURSTUECK/65791\",\n"
                        + "        \"id\" : 65791,\n"
                        + "        \"gemarkungs_nr\" : {\n"
                        + "          \"$self\" : \"/WUNDA_BLAU.GEMARKUNG/3487\",\n"
                        + "          \"gemarkungsnummer\" : 3487,\n"
                        + "          \"name\" : \"Nächstebreck\"\n"
                        + "        },\n"
                        + "        \"flur\" : \"390\",\n"
                        + "        \"fstnr_z\" : 23,\n"
                        + "        \"fstnr_n\" : 0,\n"
                        + "        \"alkis_id\" : \"053487-390-00023\",\n"
                        + "        \"dienststellen\" : \"privat\",\n"
                        + "        \"historisch\" : null,\n"
                        + "        \"umschreibendes_rechteck\" : {\n"
                        + "          \"$self\" : \"/WUNDA_BLAU.GEOM/706529975\",\n"
                        + "          \"id\" : 706529975,\n"
                        + "          \"geo_field\" : \"POLYGON ((377732.783 5686066.122, 377732.421 5686069.562, 377729.620999999 5686096.11, 377726.353 5686127.095, 377723.188000001 5686157.107, 377737.133000001 5686160.219, 377743.886 5686069.304, 377744.024 5686067.444, 377744.598999999 5686059.683, 377745.072999999 5686053.29, 377745.989 5686040.945, 377746.096000001 5686039.497, 377750.759999998 5686022.188, 377748.322000001 5686021.545, 377747.442000002 5686021.313, 377740.473999999 5686019.476, 377737.923999999 5686029.071, 377735.870999999 5686036.793, 377734.530999999 5686049.514, 377734.210000001 5686052.567, 377732.783 5686066.122))\"\n"
                        + "        },\n"
                        + "        \"fortfuehrungsnummer\" : null\n"
                        + "      },\n"
                        + "      \"historisch\" : null\n"
                        + "    },\n"
                        + "    \"veraenderungsart\" : {\n"
                        + "      \"$self\" : \"/WUNDA_BLAU.VERMESSUNG_ART/1\",\n"
                        + "      \"id\" : 1,\n"
                        + "      \"code\" : \"TV\",\n"
                        + "      \"name\" : \"Teilungsvermessung\"\n"
                        + "    }\n"
                        + "  }, {\n"
                        + "    \"$self\" : \"/WUNDA_BLAU.VERMESSUNG_FLURSTUECKSVERMESSUNG/3741\",\n"
                        + "    \"id\" : 3741,\n"
                        + "    \"flurstueck\" : {\n"
                        + "      \"$self\" : \"/WUNDA_BLAU.VERMESSUNG_FLURSTUECK_KICKER/3086\",\n"
                        + "      \"id\" : 3086,\n"
                        + "      \"gemarkung\" : {\n"
                        + "        \"$self\" : \"/WUNDA_BLAU.VERMESSUNG_GEMARKUNG/3487\",\n"
                        + "        \"id\" : 3487,\n"
                        + "        \"name\" : \"Nächstebreck\",\n"
                        + "        \"historisch\" : null\n"
                        + "      },\n"
                        + "      \"flur\" : \"390\",\n"
                        + "      \"zaehler\" : \"25\",\n"
                        + "      \"nenner\" : \"0\",\n"
                        + "      \"flurstueck\" : {\n"
                        + "        \"$self\" : \"/WUNDA_BLAU.FLURSTUECK/50024\",\n"
                        + "        \"id\" : 50024,\n"
                        + "        \"gemarkungs_nr\" : {\n"
                        + "          \"$self\" : \"/WUNDA_BLAU.GEMARKUNG/3487\",\n"
                        + "          \"gemarkungsnummer\" : 3487,\n"
                        + "          \"name\" : \"Nächstebreck\"\n"
                        + "        },\n"
                        + "        \"flur\" : \"390\",\n"
                        + "        \"fstnr_z\" : 25,\n"
                        + "        \"fstnr_n\" : 0,\n"
                        + "        \"alkis_id\" : \"053487-390-00025\",\n"
                        + "        \"dienststellen\" : \"privat\",\n"
                        + "        \"historisch\" : null,\n"
                        + "        \"umschreibendes_rechteck\" : {\n"
                        + "          \"$self\" : \"/WUNDA_BLAU.GEOM/706514208\",\n"
                        + "          \"id\" : 706514208,\n"
                        + "          \"geo_field\" : \"POLYGON ((377737.133000001 5686160.219, 377737.809 5686160.371, 377746.934 5686148.872, 377752.125 5686144.418, 377755.109999999 5686072.207, 377743.886 5686069.304, 377737.133000001 5686160.219))\"\n"
                        + "        },\n"
                        + "        \"fortfuehrungsnummer\" : null\n"
                        + "      },\n"
                        + "      \"historisch\" : null\n"
                        + "    },\n"
                        + "    \"veraenderungsart\" : {\n"
                        + "      \"$self\" : \"/WUNDA_BLAU.VERMESSUNG_ART/1\",\n"
                        + "      \"id\" : 1,\n"
                        + "      \"code\" : \"TV\",\n"
                        + "      \"name\" : \"Teilungsvermessung\"\n"
                        + "    }\n"
                        + "  }, {\n"
                        + "    \"$self\" : \"/WUNDA_BLAU.VERMESSUNG_FLURSTUECKSVERMESSUNG/3742\",\n"
                        + "    \"id\" : 3742,\n"
                        + "    \"flurstueck\" : {\n"
                        + "      \"$self\" : \"/WUNDA_BLAU.VERMESSUNG_FLURSTUECK_KICKER/3089\",\n"
                        + "      \"id\" : 3089,\n"
                        + "      \"gemarkung\" : {\n"
                        + "        \"$self\" : \"/WUNDA_BLAU.VERMESSUNG_GEMARKUNG/3487\",\n"
                        + "        \"id\" : 3487,\n"
                        + "        \"name\" : \"Nächstebreck\",\n"
                        + "        \"historisch\" : null\n"
                        + "      },\n"
                        + "      \"flur\" : \"390\",\n"
                        + "      \"zaehler\" : \"28\",\n"
                        + "      \"nenner\" : \"0\",\n"
                        + "      \"flurstueck\" : null,\n"
                        + "      \"historisch\" : null\n"
                        + "    },\n"
                        + "    \"veraenderungsart\" : {\n"
                        + "      \"$self\" : \"/WUNDA_BLAU.VERMESSUNG_ART/1\",\n"
                        + "      \"id\" : 1,\n"
                        + "      \"code\" : \"TV\",\n"
                        + "      \"name\" : \"Teilungsvermessung\"\n"
                        + "    }\n"
                        + "  } ],\n"
                        + "  \"optimiert_datum\" : \"2013-02-05\",\n"
                        + "  \"optimiert_name\" : \"GoehrkeJ102\"\n"
                        + "}";
            final Object createdObject = bridge.createNewObject(jsonObj, domain, className, true, null, null);
            System.out.println("new object: " + createdObject);
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
