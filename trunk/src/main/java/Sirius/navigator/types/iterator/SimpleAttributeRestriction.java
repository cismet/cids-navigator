/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * SimpleAttributeRestriction.java
 *
 * Created on 4. Mai 2004, 18:00
 */
package Sirius.navigator.types.iterator;

import Sirius.navigator.types.treenode.*;

import Sirius.server.middleware.types.*;

import java.util.Collection;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class SimpleAttributeRestriction implements AttributeRestriction {

    //~ Instance fields --------------------------------------------------------

    private final long typeRestriction;

    private final int visible;
    private final String id;
    private final String name;
    private final Class instance;

    //~ Constructors -----------------------------------------------------------

    /**
     * Restrict to any visible object attributes.
     */
    public SimpleAttributeRestriction() {
        this(OBJECT, TRUE, null, null, null);
    }

    /**
     * Creates a new SimpleAttributeRestriction object.
     *
     * @param  typeRestriction  DOCUMENT ME!
     * @param  visible          DOCUMENT ME!
     */
    public SimpleAttributeRestriction(final long typeRestriction, final int visible) {
        this(typeRestriction, visible, null, null, null);
    }

    /**
     * Creates a new SimpleAttributeRestriction object.
     *
     * @param  typeRestriction  DOCUMENT ME!
     * @param  visible          DOCUMENT ME!
     * @param  id               DOCUMENT ME!
     * @param  name             DOCUMENT ME!
     * @param  instance         DOCUMENT ME!
     */
    public SimpleAttributeRestriction(final long typeRestriction,
            final int visible,
            final String id,
            final String name,
            final Class instance) {
        this.typeRestriction = typeRestriction;
        this.visible = visible;
        this.id = id;
        this.name = name;
        this.instance = instance;
    }

    //~ Methods ----------------------------------------------------------------

    /*public AttributeRestriction(long typeRestriction, int visible, int coordinate, int id, String name)
     * { this.typeRestriction = typeRestriction; this.visible = visible; this.coordinate = coordinate; this.id = id;
     * this.name = name;}*/

    /*public Sirius.server.localserver.attribute.Attribute
     * applyRestriction(Sirius.server.localserver.attribute.Attribute attribute) { if( ((this.visible == IGNORE) ||
     * (this.visible == FALSE && !attribute.isVisible()) || (this.visible == TRUE && attribute.isVisible())) &&
     * ((this.coordinate == IGNORE) || (this.coordinate == FALSE && !attribute.isCoordinate()) || (this.coordinate ==
     * TRUE && attribute.isCoordinate())) &&     ((this.id == IGNORE) || this.id == attribute.getID()) && ((this.name ==
     * null) || this.name.equalsIgnoreCase(attribute.getName())))
     * {     return attribute; } else {     return null; }}*/

    @Override
    public Sirius.server.localserver.attribute.Attribute applyRestriction(
            final Sirius.server.localserver.attribute.Attribute attribute) {
        if (((this.visible == IGNORE) || ((this.visible == FALSE) && !attribute.isVisible())
                        || ((this.visible == TRUE) && attribute.isVisible()))
                    && ((this.id == null) || this.id.equals(attribute.getID()))) {
            if ((name == null) || name.equalsIgnoreCase(attribute.getName())) {
                if ((instance == null)
                            || ((attribute.getValue() != null)
                                && instance.isAssignableFrom(attribute.getValue().getClass()))) {
                    return attribute;
                }
            } else if ((name == null)
                        && ((instance == null)
                            || ((attribute.getValue() != null)
                                && instance.isAssignableFrom(attribute.getValue().getClass())))) {
                return attribute;
            }
            /*else
             * {  for(int i = 0; i < names.length; i++) {
             * if(attribute.getName().trim().equalsIgnoreCase((names[i])))     {         return attribute;     } }}*/
        }

        return null;
    }

    @Override
    public long getTypeRestriction() {
        return this.typeRestriction;
    }
}
