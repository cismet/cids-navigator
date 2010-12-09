/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.plugin;

import Sirius.navigator.plugin.interfaces.*;
/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class PluginMethodDescriptor {

    //~ Instance fields --------------------------------------------------------

    /** Holds value of property id. */
    private final String id;

    /** Holds value of property name. */
    private final String name;

    /** Holds value of property description. */
    private final String description;

    /** Holds value of property method. */
    private final PluginMethod method;

    /** Holds value of property multithreaded. */
    private boolean multithreaded;

    /** Holds value of property availability. */
    private long availability;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new PluginMethodDescriptor object.
     *
     * @param  id           DOCUMENT ME!
     * @param  name         DOCUMENT ME!
     * @param  description  DOCUMENT ME!
     * @param  method       DOCUMENT ME!
     */
    public PluginMethodDescriptor(final String id,
            final String name,
            final String description,
            final PluginMethod method) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.multithreaded = false;
        this.availability = Long.MAX_VALUE;
        this.method = method;
    }

    /**
     * Creates a new instance of PluginMethodDescriptor.
     *
     * @param  id             DOCUMENT ME!
     * @param  name           DOCUMENT ME!
     * @param  description    DOCUMENT ME!
     * @param  multithreaded  DOCUMENT ME!
     * @param  availability   DOCUMENT ME!
     * @param  method         DOCUMENT ME!
     */
    public PluginMethodDescriptor(final String id,
            final String name,
            final String description,
            final boolean multithreaded,
            final long availability,
            final PluginMethod method) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.multithreaded = multithreaded;
        this.availability = availability;
        this.method = method;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Getter for property name.
     *
     * @return  Value of property name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter for property id.
     *
     * @return  Value of property id.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Getter for property description.
     *
     * @return  Value of property description.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Getter for property method.
     *
     * @return  Value of property method.
     */
    public PluginMethod getMethod() {
        return this.method;
    }

    /**
     * Getter for property multithreaded.
     *
     * @return  Value of property multithreaded.
     */
    public boolean isMultithreaded() {
        return this.multithreaded;
    }

    /**
     * Getter for property availability.
     *
     * @return  Value of property availability.
     */
    public long getAvailability() {
        return this.availability;
    }
}
