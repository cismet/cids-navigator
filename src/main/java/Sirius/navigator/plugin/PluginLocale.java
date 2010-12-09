/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * PluginLocale.java
 *
 * Created on 17. Juni 2003, 09:26
 */
package Sirius.navigator.plugin;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class PluginLocale {

    //~ Instance fields --------------------------------------------------------

    /** Holds value of property locale. */
    private final java.util.Locale locale;

    /** Holds value of property resourceFile. */
    private final String resourceFile;

    /** Holds value of property name. */
    private final String name;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of PluginLocale.
     *
     * @param  name          DOCUMENT ME!
     * @param  language      DOCUMENT ME!
     * @param  country       DOCUMENT ME!
     * @param  resourceFile  DOCUMENT ME!
     */
    public PluginLocale(final String name, final String language, final String country, final String resourceFile) {
        this.locale = new java.util.Locale(language, country);
        this.name = name;
        this.resourceFile = resourceFile;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Getter for property locale.
     *
     * @return  Value of property locale.
     */
    public java.util.Locale getLocale() {
        return this.locale;
    }

    /**
     * Getter for property resourceFile.
     *
     * @return  Value of property resourceFile.
     */
    public String getResourceFile() {
        return this.resourceFile;
    }

    /**
     * Getter for property name.
     *
     * @return  Value of property name.
     */
    public String getName() {
        return this.name;
    }
}
