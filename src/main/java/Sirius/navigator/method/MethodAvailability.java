/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * Availability.java
 *
 * Created on 11. August 2005, 15:10
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package Sirius.navigator.method;

import java.util.*;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class MethodAvailability {

    //~ Instance fields --------------------------------------------------------

    private final HashSet classKeys;
    private final long availability;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of Availability.
     *
     * @param  classKeys     DOCUMENT ME!
     * @param  availability  DOCUMENT ME!
     */
    public MethodAvailability(final HashSet classKeys, final long availability) {
        this.classKeys = classKeys;
        this.availability = availability;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Getter for property availability.
     *
     * @return  Value of property availability.
     */
    public long getAvailability() {
        return this.availability;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public HashSet getClassKeys() {
        return this.classKeys;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   classKeys  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean containsClasses(final Collection classKeys) {
        final Iterator iterator = this.classKeys.iterator();
        if (!iterator.hasNext()) {
            return false;
        }

        while (iterator.hasNext()) {
            if (!classKeys.contains(iterator.next())) {
                return false;
            }
        }

        return true;
    }
}
