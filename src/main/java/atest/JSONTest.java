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
import Sirius.navigator.connection.SessionManager;
import Sirius.server.middleware.types.MetaClass;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.FileUtils;

import java.io.File;

import de.cismet.cids.client.tools.DevelopmentTools;

import de.cismet.cids.dynamics.CidsBean;
import de.cismet.cids.navigator.utils.ClassCacheMultiple;

import de.cismet.tools.gui.log4jquickconfig.Log4JQuickConfig;
import java.util.ArrayList;

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
    static final int AMOUNT = 1;
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
            boolean wunda = false;

            boolean wrrl = false;
            if (wunda) {
                check(
                        "URL_BASE",
                        "URL",
                        "STRASSE",
                        "ADRESSE",
                        "KNOTEN",
                        "SEGMENTSEITE",
                        "SEGMENT",
                        "GEMARKUNG",
                        "FLURSTUECK",
                        "KAUFVERTRAGSFLURSTUECK",
                        "GEBAEUDE",
                        "KAUFVERTRAGSFLURSTUECKE",
                        "KAUFVERTRAGSGEBAEUDE",
                        "TIME_INTERVAL",
                        "TEILEIGENTUM",
                        "KAUFVERTRAGSTEILEIGENTUM",
                        "SACHWERTE",
                        "KAUFVERTRAG",
                        "GEOM",
                        "LUFTBILDSCHRAEGAUFNAHMEN",
                        "TIM_ALKIS",
                        "TIM_KART",
                        "TIM_LIEG",
                        "AUFTRAGSBUCH",
                        "SCHULUNG",
                        "KEHRBEZIRK",
                        "STRAKO",
                        "MASSNAHME",
                        "GUTACHTEN",
                        "EINZELHANDELSMIETEN",
                        "GMW_GEBAEUDE",
                        "WERBETRAEGER",
                        "WT_BAUART",
                        "ZAEHLUNG_REF",
                        "ZAEHLUNGSSTANDORT",
                        "ZAEHLUNGWETTER",
                        "ZAEHLUNGSSTADTTEIL",
                        "ZAEHLUNG",
                        "WBF_ANTRAG",
                        "WBF_MIET_TYP",
                        "BPLAN_VERFAHREN",
                        "BPLAN_PLAN",
                        "SCHULEN",
                        "BAUDENKMAL",
                        "MAUER",
                        "FNP_AENDERUNGEN",
                        "THEMA_KATEGORIE",
                        "THEMA_REALISIERUNGSSTAND",
                        "THEMA",
                        "THEMA_PERSON",
                        "THEMA_DATENBEREITSTELLUNG",
                        "THEMA_NUTZUNGSART",
                        "THEMA_DIENST",
                        "THEMA_WARTUNGSVERTRAG",
                        "THEMA_IMPLEMENTIERUNG",
                        "THEMA_GEOMETRIEMODELL",
                        "THEMA_WMS",
                        "THEMA_DATENQUELLE",
                        "AUFTRAGSBUCH_AUFTRAGSART",
                        "WBF_MASSNAHME",
                        "WBF_NUTZUNGSART",
                        "WBF_VORGAENGE",
                        "WBF_GEBAEUDE",
                        "WBF_VORGANG",
                        "ARC_AUFTRAGGEBER",
                        "ARC_FILMART",
                        "ARC_FOTOGRAF",
                        "ARC_STADTTEIL",
                        "ARC_STRASSE",
                        "ARC_SUCHWORT",
                        "ARC_STADTBILD",
                        "ARC_STADTBILDER_SUCHWORTE",
                        "POI_SPATIALREFERENCESYSTEMUSINGGEOGRAPHICIDENTIFIERS",
                        "POI_LOCATIONTYPE",
                        "POI_LINK_SR_LOCATIONTYPE",
                        "POI_ALT_GEO_IDENTIFIER_ARRRAY",
                        "POI_LOCATIONINSTANCE",
                        "POI_SIGNATUREN",
                        "POI_ALTERNATIVEGEOGRAPHICIDENTIFIER",
                        "POI_LINK_LOCATIONINSTANCE_LOCATIONTYPE",
                        "ALKIS_BUCHUNGSBLATT_LANDPARCEL",
                        "ALKIS_FLURSTUECK_TO_BUCHUNGSBLAETTER",
                        "ALKIS_BUCHUNGSBLATT_TO_BUCHUNGSBLATTLANDPARCELS",
                        "ALKIS_GEBAEUDE_TO_BUCHUNGSBLATTLANDPARCELS",
                        "ALKIS_GEBAEUDE",
                        "ALKIS_FLURSTUECKSADRESSEN",
                        "ALKIS_LANDPARCEL",
                        "ALKIS_ADRESSE",
                        "ALKIS_POINT",
                        "ALKIS_POINTTYPE",
                        "ALKIS_BUCHUNGSBLATT",
                        "ALB_BAULAST_TEXTBLATT_PAGES",
                        "ALB_BAULAST_ART",
                        "ALB_BAULAST",
                        "ALB_GEO_DOCUMENT_PAGE",
                        "ALB_BAULAST_BAULASTARTEN",
                        "ALB_BAULAST_FLURSTUECKE_BELASTET",
                        "ALB_FLURSTUECK_KICKER",
                        "ALB_BAULASTBLATT_BAULASTEN",
                        "ALB_BAULASTBLATT",
                        "ALB_BAULAST_LAGEPLAN_PAGES",
                        "ALB_BAULAST_FLURSTUECKE_BEGUENSTIGT",
                        "BERG_EINZELHANDEL",
                        "BAHN_FLURSTUECKE",
                        "BAHN_STATUS",
                        "BAHN_VERFAHREN",
                        "BAHN_ADMIN",
                        "ALB_BAULAST_ADMIN",
                        "ESW_HANDSTREUSTELLE",
                        "KST_SEGMENT",
                        "FNP_RESERVE",
                        "NIVELLEMENT_FESTLEGUNGSART",
                        "NIVELLEMENT_LAGEGENAUIGKEIT",
                        "NIVELLEMENT_PUNKT",
                        "BILLING");
            }


            if (wrrl) {
                check("MASSNAHMEN_MEAS_2021",
                        "MASSNAHMEN",
                        "QUERBAUWERKE",
                        "MASSNAHMEN_DE_MEAS_CD",
                        "ROHRLEITUNG",
                        "MASSNAHMEN_MEAS_2015",
                        "WIRKUNG_WK",
                        "PROJEKTE",
                        "PROJEKTE_AGGREGIERTER_MASSNAHMEN_TYP",
                        "LAWA",
                        "EX_DATE",
                        "EX_JUSTIFICATION",
                        "EX_TYP",
                        "EXCEMPTION",
                        "FOTO",
                        "FOTODOKUMENTATION_FOTOS",
                        "INDIKATOR",
                        "LA_LAWA_NR",
                        "MA_ART",
                        "MA_PRIORITAET",
                        "MA_TYP",
                        "PROJEKTE_INDIKATOREN",
                        "PROJEKTE_PROJEKTE_INDIKATOREN",
                        "QUERBAUWERKE_CATEGORIE_CODE",
                        "QUERBAUWERKE_DGK_WHY_CODE",
                        "QUERBAUWERKE_FOTODIRECTION_CODE",
                        "QUERBAUWERKE_INTENTION_CODE",
                        "QUERBAUWERKE_INTERVAL_CODE",
                        "QUERBAUWERKE_MASSN1_CODE",
                        "QUERBAUWERKE_MASSN2_CODE",
                        "QUERBAUWERKE_OEKO_DGK_CODE",
                        "QUERBAUWERKE_STARR_CODE",
                        "QUERBAUWERKE_SUBSTRAT_CODE",
                        "QUERBAUWERKE_TYPE_CODE",
                        "QUERBAUWERKE_WEHR1_CODE",
                        "QUERBAUWERKE_WEHR2_CODE",
                        "QUERBAUWERKE_YNO_CODE",
                        "ROHRLEITUNGEN_MASSN_CODE",
                        "ROHRLEITUNGEN_SEDIMENT_CODE",
                        "STAEUN",
                        "STALU",
                        "STATION_TEST",
                        "SWSTN_MONITORNET",
                        "TEST_JEAN",
                        "TEST_JEAN_STATIONEN",
                        "TEST_JEAN_WKTEILE",
                        "URL",
                        "URL_BASE",
                        "WFD.AB_CODE",
                        "WFD.AQUIFER_TYPE_CODE",
                        "WFD.COASTAL_WATER_DEPTH_CODE",
                        "WFD.COMPLIANCY_STATUS_CODE",
                        "WFD.CONFIDENCE_LEVEL_CODE",
                        "WFD.CONTINUA_CODE",
                        "WFD.COUNTRY_STATE_CODE",
                        "WFD.DE_COASTAL_WATER_TYPE_CODE",
                        "WFD.DE_EFFECTION_ON_CODE",
                        "WFD.DE_MEASURE_TYPE_CODE",
                        "WFD.DE_MEASURE_TYPE_CODE_AFTER2015",
                        "WFD.DE_RIVER_BODY_TYPE_CODE",
                        "WFD.DEPTH_RANGE_CODE",
                        "WFD.EU_COASTAL_WATER_TYPE_CODE",
                        "WFD.EU_MEASURE_TYPE_CODE",
                        "WFD.EXEMPTION_SOURCE_CODE",
                        "WFD.EXEMPTION_TYPE_CODE",
                        "WFD.EXTENDED_DEADLINE_CODE",
                        "WFD.EXTRACTION_CODE",
                        "WFD.GEOLOGIC_TYPE_CODE",
                        "WFD.GEOLOGICAL_FORMATION_CODE",
                        "WFD.GW_IMPACT_TYPE_CODE",
                        "WFD.INT_MONITOR_NET_CODE",
                        "WFD.LAKE_WATER_BODY_DEPTH_CODE",
                        "WFD.LAKE_WATER_BODY_SIZE_CODE",
                        "WFD.LAKE_WATER_BODY_TYPE_CODE",
                        "WFD.MEMBER_STATE_CODE",
                        "WFD.PLAN_UNI_CODE",
                        "MASSNAHMEN_UMSETZUNG_WIRKUNG_WK",
                        "GEO_HINT",
                        "WK_FG",
                        "AGGREGIERTER_MASSNAHMEN_TYP",
                        "SWSTN",
                        "OEG_EINZUGSGEBIET",
                        "UMSETZUNG_PROJEKTE_INDIKATOREN",
                        "MEER",
                        "SWSTN_QE_TYPES",
                        "WK_FG_ORDNUNG",
                        "CHEMIE_MST_MESSUNGEN",
                        "PRIORITY",
                        "WK_FG_ARTIFICIAL",
                        "OEG_EINZUGSGEBIETE",
                        "FOTODOKUMENTATION",
                        "CHEMIE_MST_STAMMDATEN",
                        "BIO_MST_MESSUNGEN",
                        "BIO_MST_STAMMDATEN",
                        "WK_SG",
                        "BEWIRTSCHAFTUNGSENDE",
                        "OEG_KUMMULIERT",
                        "OEG_KUMMULIERT_REF",
                        "WK_GW",
                        "QUERBAUWERKE_MASSN3_CODE",
                        "MASSNAHMEN_UMSETZUNG",
                        "PROJEKTE_MASSNAHMEN_UMSETZUNG",
                        "WK_KG",
                        "FGSK_BESONDERHEITEN_WASSERFUEHRUNG",
                        "FGSK_FLIESSGESCHWINDIGKEIT",
                        "FGSK_FLIESSRICHTUNG",
                        "FGSK_GEWAESSER",
                        "FGSK_GEWAESSERBREITE",
                        "FGSK_GEWAESSERSUBTYP",
                        "FGSK_GEWAESSERTYP",
                        "FGSK_GEWAESSERTYP_FGSK_GEWAESSERSUBTYP",
                        "FGSK_KRUEMMUNGSEROSION",
                        "FGSK_LAENGSBAENKE",
                        "WFD.POLLUTANT_TREND_TYPE_CODE",
                        "WFD.POLLUTION_TREND_CODE",
                        "WFD.PRESSURE_TYPE_CODE",
                        "WFD.QUALITY_ELEMENT_TYPE_CODE",
                        "WFD.QUALITY_STATUS_CODE",
                        "WFD.QUANTITY_STATUS_CODE",
                        "WFD.REASON_FOR_FAILURE_CHEM_CODE",
                        "WFD.REASON_FOR_FAILURE_QUANT_CODE",
                        "WFD.REGION_CDA_CODE",
                        "WFD.REGION_CDB_CODE",
                        "WFD.RISK_STATUS_CODE",
                        "WFD.RIVER_BASIN_DISTRICT_CODE",
                        "WFD.RIVER_CATEGORY",
                        "WFD.RIVER_WATER_BODY_SIZE_CODE",
                        "WFD.SALINITY_CODE",
                        "WFD.SUB_SITE_TYPE",
                        "WFD.SW_IMPACT_TYPE_CODE",
                        "WFD.TIDAL_CODE",
                        "WFD.VERTICAL_ORIENTATION_CODE",
                        "WFD.WATERBODY_TYPE_CODE",
                        "WFD.WORK_AREA_CODE",
                        "WFD.YN_CODE",
                        "WK_FG_EXCEMPTIONS",
                        "WK_FG_IMPACT_SRCS",
                        "WK_FG_IMPACTS",
                        "WK_FG_TEILE",
                        "WK_GROUP",
                        "WK_GROUP_AGGR",
                        "WK_GW_GB_PREDECS",
                        "WK_GW_IMPACTS",
                        "WK_GW_POOR_CHEMS",
                        "WK_GW_POOR_QUANTS",
                        "WK_GW_TREND_TYPES",
                        "WK_KG_IMPACT_SRCS",
                        "WK_KG_IMPACTS",
                        "WK_KG_WB_PREDECS",
                        "WK_KG_WHY_HMWBS",
                        "WK_SG_EXCEMPTIONS",
                        "WK_SG_IMPACT_SRCS",
                        "WK_SG_IMPACTS",
                        "WK_SG_TEILE",
                        "WK_TEIL",
                        "WK_TEIL_TEST",
                        "FGSK_LAUFKRUEMMUNG",
                        "FGSK_LAUFSTRUKTUREN",
                        "FGSK_SONDERFALL",
                        "FGSK_WASSERFUEHRUNG",
                        "AGGREGIERTER_MASSNAHMEN_TYP_WIRKUNG_WK",
                        "FGSK_BELASTUNG_SOHLE",
                        "FGSK_BREITENEROSION",
                        "FGSK_BREITENVARIANZ",
                        "FGSK_FLAECHENNUTZUNG",
                        "FGSK_GEWAESSERRANDSTREIFEN",
                        "FGSK_KARTIERABSCHNITT_FGSK_BESONDERHEITEN_WASSERFUEHRUNG",
                        "FGSK_KARTIERABSCHNITT_FGSK_GEWAESSERSUBTYP",
                        "FGSK_MESSUNGEN",
                        "FGSK_PROFILTYP",
                        "FGSK_SCHAEDLICHEUMFELDSTRUKTUREN",
                        "FGSK_SOHLENSTRUKTUREN",
                        "FGSK_SOHLENSUBSTRAT",
                        "FGSK_SOHLENVERBAU",
                        "FGSK_STROEMUNGSDIVERSITAET",
                        "FGSK_TIEFENEROSION",
                        "FGSK_TIEFENVARIANZ",
                        "FGSK_UFERBELASTUNGEN",
                        "FGSK_UFERSTRUKTUREN",
                        "FGSK_UFERVEGETATION",
                        "FGSK_UFERVERBAU",
                        "FGSK_UMFELDSTRUKTUREN",
                        "FGSK_Z_SOHLENVERBAU",
                        "FGSK_Z_UFERVERBAU",
                        "FGSK_KARTIERABSCHNITT",
                        "DE_MEASURE_TYPE_GROUP_TYPE",
                        "WFD.DE_MEASURE_TYPE_GROUP",
                        "KA",
                        "FS_MV",
                        "MASSNAHMEN_REALISIERUNG",
                        "PROJEKTE_STATUS",
                        "FLUSSGEBIETSEINHEIT",
                        "PLANUNGSEINHEIT",
                        "BEARBEITUNGSGEBIET",
                        "GUP_MASSNAHMENART",
                        "GUP_MATERIAL_VERBLEIB",
                        "GUP_MASSNAHMENINTERVALL",
                        "CCS_RESTRICTIONINFO_CLASSES",
                        "CS_CLASS",
                        "CS_DOMAIN",
                        "CS_UG",
                        "ENTWICKLUNGSZIEL_ROUTE",
                        "ENTWICKLUNGSZIEL_ROUTE_ENTWICKLUNGSZIEL",
                        "GUP_DOKUMENT",
                        "GUP_ENTWICKLUNGSZIEL",
                        "GUP_ENTWICKLUNGSZIEL_NAME",
                        "GUP_GEWAESSERABSCHNITT_MASSNAHME_SOHLE",
                        "GUP_GEWAESSERABSCHNITT_MASSNAHME_UFER_LINKS",
                        "GUP_GEWAESSERABSCHNITT_MASSNAHME_UFER_RECHTS",
                        "GUP_GEWAESSERABSCHNITT_MASSNAHME_UMFELD_LINKS",
                        "GUP_GEWAESSERABSCHNITT_MASSNAHME_UMFELD_RECHTS",
                        "GUP_GEWAESSERABSCHNITTE_DOKUMENT",
                        "GUP_GUP",
                        "GUP_HYDROLOG",
                        "GUP_KOMPARTIMENT",
                        "GUP_NATURSCHUTZART",
                        "GUP_NATURSCHUTZGEBIET",
                        "GUP_SEITE",
                        "GUP_UMLANDNUTZUNG",
                        "GUP_UMLANDNUTZUNGSART",
                        "GUP_UMLANDNUTZUNGSGRUPPE",
                        "GUP_UMLANDNUTZUNGSOBERGRUPPE",
                        "GUP_UMLANDNUTZUNGSOBERGRUPPE_GRUPPE",
                        "GUP_UNTERHALTUNGSERFORDERNIS",
                        "GUP_UNTERHALTUNGSERFORDERNIS_NAME",
                        "GUP_UNTERHALTUNGSMASSNAHME",
                        "GUP_UNTERHALTUNGSMASSNAHME_AUSFUEHRUNGSZEITPUNKT",
                        "UMLANDNUTZUNG_ROUTE_UMLANDNUTZUNG_LINKS",
                        "GUP_UNTERHALTUNGSERFORDERNIS_ROUTE_UNTERHALTUNGSERFORDERNIS",
                        "STATION_LINIE",
                        "GUP_MASSNAHMENBEZUG",
                        "ROUTE",
                        "GUP_JAHR",
                        "CCS_RESTRICTIONINFO",
                        "EX_CAT",
                        "EXCEMPTION_JUSTIFICATIONS",
                        "GEOM",
                        "GUP_OPERATIVES_ZIEL",
                        "GUP_OPERATIVES_ZIEL_ABSCHNITT",
                        "GUP_OPERATIVES_ZIEL_ROUTE",
                        "GUP_OPERATIVES_ZIEL_ROUTE_SOHLE",
                        "GUP_OPERATIVES_ZIEL_ROUTE_UFER_LINKS",
                        "GUP_OPERATIVES_ZIEL_ROUTE_UFER_RECHTS",
                        "GUP_OPERATIVES_ZIEL_ROUTE_UMFELD_LINKS",
                        "GUP_OPERATIVES_ZIEL_ROUTE_UMFELD_RECHTS",
                        "GUP_PLANUNGSABSCHNITT",
                        "GUP_UNTERHALTUNGSERFORDERNIS_ROUTE",
                        "HYDROLOG_ROUTE",
                        "HYDROLOG_ROUTE_HYDROLOG",
                        "PERSON",
                        "STATION",
                        "UMLANDNUTZER",
                        "UMLANDNUTZER_ROUTE",
                        "UMLANDNUTZER_ROUTE_UMLANDNUTZER_LINKS",
                        "UMLANDNUTZER_ROUTE_UMLANDNUTZER_RECHTS",
                        "UMLANDNUTZUNG_ROUTE",
                        "UMLANDNUTZUNG_ROUTE_UMLANDNUTZUNG_RECHTS",
                        "VERMESSUNG_BAND_ELEMENT");
            }
            checkAll();
            //check("GUP_PLANUNGSABSCHNITT");

        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }
    public static void checkAll() throws Exception {
        DevelopmentTools.initSessionManagerFromRMIConnectionOnLocalhost(DOMAIN, GROUP, USER, PASS);
        ArrayList allTables=new ArrayList(ClassCacheMultiple.getTableNameHashtableOfClassesForOneDomain(DOMAIN).keySet());
        String[] tables=new String[allTables.size()];
        for (int i=0;i<tables.length;++i){
            tables[i]=allTables.get(i).toString();
        }
        check(tables);
    }
    public static void check(String... tables) throws Exception {
        System.out.println("go:");
     
        for (String table : tables) {
            CidsBean[] testBeans = DevelopmentTools.createCidsBeansFromRMIConnectionOnLocalhost(DOMAIN,
                    GROUP,
                    USER,
                    PASS, table, AMOUNT);
            System.out.print(table+ ": ");
            for (CidsBean testBean : testBeans) {
                boolean test = checkBean(testBean);
                if (test) {
                    System.out.print("_");
                } else {
                    System.out.print("X");
                }
            }
            System.out.print("\n");


        }
    }
    
    public static boolean checkBean(CidsBean dbBean) throws Exception {
        long start = System.currentTimeMillis();
        final String first = dbBean.toJSONString();
        long dur = System.currentTimeMillis() - start;
        ObjectMapper om = new ObjectMapper();
        final CidsBean jsonBean = om.readValue(first, CidsBean.class);
        start = System.currentTimeMillis();
        final String jsonBeanJson = jsonBean.toJSONString();
        dur = System.currentTimeMillis() - start;
        //LOG.fatal(jsonBean.getMOString());
        if (!first.equals(jsonBeanJson)) {
            FileUtils.writeStringToFile(new File(FOLDER + dbBean.getCidsObjectKey().replaceAll("/", "") + ".dbBean.json"), first);
            FileUtils.writeStringToFile(new File(FOLDER + jsonBean.getCidsObjectKey().replaceAll("/", "") + ".jsonBean.json"), jsonBeanJson);
            return false;
        } else {
            return true;
        }
        
    }
}
