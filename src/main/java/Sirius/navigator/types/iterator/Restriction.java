/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.types.iterator;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public interface Restriction {

    //~ Instance fields --------------------------------------------------------

    long PURE = 1;
    long OBJECT = 2;
    long CLASS = 4;

    int TRUE = 1;
    int FALSE = 0;
    int IGNORE = -1;

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    long getTypeRestriction();
}
