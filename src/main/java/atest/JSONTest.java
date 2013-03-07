/**
 * *************************************************
 *
 * cismet GmbH, Saarbruecken, Germany
 * 
* ... and it just works.
 * 
***************************************************
 */
package atest;

/**
 * *************************************************
 *
 * cismet GmbH, Saarbruecken, Germany
 *
 * ... and it just works.
 *
 ***************************************************
 */

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.FileUtils;

import java.io.File;

import java.util.ArrayList;

import de.cismet.cids.client.tools.DevelopmentTools;

import de.cismet.cids.dynamics.CidsBean;

import de.cismet.cids.navigator.utils.ClassCacheMultiple;

import de.cismet.tools.gui.log4jquickconfig.Log4JQuickConfig;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * DOCUMENT ME!
 *
 * @author thorsten
 * @version $Revision$, $Date$
 */
public class JSONTest {

    //~ Static fields/initializers ---------------------------------------------
    private static final transient org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(JSONTest.class);
    static ObjectMapper mapper = new ObjectMapper();
    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    static final String DOMAIN = "WRRL_DB_MV";
    static final String GROUP = "Administratoren";
    static final String USER = "admin";
    static final String PASS = "kif";
    static final int AMOUNT = 10;
    static final String FOLDER = "/Users/thorsten/tmp/jsontest/";

    //~ Methods ----------------------------------------------------------------
    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static void main(final String[] args) throws Exception {
        Log4JQuickConfig.configure4LumbermillOnLocalhost();
        try {
//            checkAll();
//            check("GUP_PLANUNGSABSCHNITT");
            check("GUP_POI_ROUTE");
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static void checkAll() throws Exception {
        DevelopmentTools.initSessionManagerFromRMIConnectionOnLocalhost(DOMAIN, GROUP, USER, PASS);
        final ArrayList allTables = new ArrayList(ClassCacheMultiple.getTableNameHashtableOfClassesForOneDomain(
                DOMAIN).keySet());
        final String[] tables = new String[allTables.size()];
        for (int i = 0; i < tables.length; ++i) {
            tables[i] = allTables.get(i).toString();
        }
        check(tables);
    }

    /**
     * DOCUMENT ME!
     *
     * @param tables DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static void check(final String... tables) throws Exception {
        System.out.println("go:");

        for (final String table : tables) {
            final CidsBean[] testBeans = DevelopmentTools.createCidsBeansFromRMIConnectionOnLocalhost(
                    DOMAIN,
                    GROUP,
                    USER,
                    PASS,
                    table,
                    AMOUNT);
            System.out.print(table + ": ");
            for (final CidsBean testBean : testBeans) {
                final boolean test = checkBean(testBean);
                if (test) {
                    System.out.print("_");
                } else {
                    System.out.print("X");
                }
            }
            System.out.print("\n");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param dbBean DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static boolean checkBean(final CidsBean dbBean) throws Exception {
        long start = System.currentTimeMillis();
        final String first = dbBean.toJSONString();
        long dur = System.currentTimeMillis() - start;
        final ObjectMapper om = new ObjectMapper();
        final CidsBean jsonBean = om.readValue(first, CidsBean.class);
        start = System.currentTimeMillis();
        final String jsonBeanJson = jsonBean.toJSONString();
        dur = System.currentTimeMillis() - start;
        LOG.fatal(jsonBean.getMOString());
        if (true || !first.equals(jsonBeanJson)) {
            FileUtils.writeStringToFile(new File(
                    FOLDER
                    + dbBean.getCidsObjectKey().replaceAll("/", "")
                    + ".dbBean.json"),
                    first);
            FileUtils.writeStringToFile(new File(
                    FOLDER
                    + jsonBean.getCidsObjectKey().replaceAll("/", "")
                    + ".jsonBean.json"),
                    jsonBeanJson);
            return false;
        } else {
            return true;
        }
    }
}
