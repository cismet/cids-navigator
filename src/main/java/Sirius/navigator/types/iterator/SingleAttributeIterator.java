/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.types.iterator;

import Sirius.navigator.types.treenode.*;

import Sirius.server.middleware.types.*;

import org.apache.log4j.Logger;

import java.util.*;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class SingleAttributeIterator implements AttributeIterator {

    //~ Instance fields --------------------------------------------------------

    private final AttributeRestriction restriction;
    private Sirius.server.localserver.attribute.Attribute nextElement = null;

    // private Sirius.server.localserver.attribute.Attribute[] attributes = null;
    // private Collection metaAttributes = null;
    private Iterator attributeIterator = null;
    // private int i = 0;

    private final HashSet hashSet;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new SingleAttributeIterator object.
     */
    public SingleAttributeIterator() {
        this(null, new SimpleAttributeRestriction(), false);
    }

    /**
     * Creates a new SingleAttributeIterator object.
     *
     * @param  treeNode  DOCUMENT ME!
     */
    public SingleAttributeIterator(final DefaultMetaTreeNode treeNode) {
        this(treeNode, new SimpleAttributeRestriction(), false);
    }

    /**
     * Creates a new SingleAttributeIterator object.
     *
     * @param  restriction  DOCUMENT ME!
     * @param  distinct     DOCUMENT ME!
     */
    public SingleAttributeIterator(final AttributeRestriction restriction, final boolean distinct) {
        this(null, restriction, distinct);
    }

    /**
     * Creates a new SingleAttributeIterator object.
     *
     * @param  treeNode     DOCUMENT ME!
     * @param  restriction  DOCUMENT ME!
     * @param  distinct     DOCUMENT ME!
     */
    public SingleAttributeIterator(final DefaultMetaTreeNode treeNode,
            final AttributeRestriction restriction,
            final boolean distinct) {
        this.restriction = restriction;

        if (distinct) {
            hashSet = new HashSet();
        } else {
            hashSet = null;
        }

        this.init(treeNode);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   object  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean init(final Object object) {
        this.nextElement = null;
        this.attributeIterator = null;

        if (object != null) {
            if (object instanceof DefaultMetaTreeNode) {
                return this.init((DefaultMetaTreeNode)object);
            }
            /*else if(object instanceof Sirius.server.localserver.attribute.Attribute[])
             * { return this.init((Sirius.server.localserver.attribute.Attribute[])object);}*/
            else if (java.util.Collection.class.isAssignableFrom(object.getClass())) {
                return this.init((Collection)object);
            }
        }

        return false;
    }

    /**
     * public boolean init(Sirius.server.localserver.attribute.Attribute[] attributes) { this.nextElement = null;
     * this.attributes = null; this.i = 0; this.attributes = attributes; return true; }.
     *
     * @param   metaAttributes  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean init(final Collection metaAttributes) {
        this.nextElement = null;
        this.attributeIterator = metaAttributes.iterator();

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   treeNode  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean init(final DefaultMetaTreeNode treeNode) {
        this.nextElement = null;
        this.attributeIterator = null;

        if (treeNode == null) {
            return false;
        }

        try {
            // class attributes || object attributes
            if ((Restriction.CLASS & restriction.getTypeRestriction()) != 0) {
                // System.out.println("[AttributeIterator] is class restriction");
                // only class attributes
                if (treeNode.isClassNode()) {
                    // System.out.println("[AttributeIterator] is class node");
                    // this.attributes = ((ClassTreeNode)treeNode).getMetaClassAttributes();

                    try {
                        // this.metaAttributes = ((ClassTreeNode)treeNode).getMetaClass().getAttributes();
                        this.attributeIterator = ((ClassTreeNode)treeNode).getMetaClass().getAttributes().iterator();
                    } catch (Exception exp) {
                        exp.printStackTrace();
                        return false;
                    }

                    return true;
                } else if (treeNode.isObjectNode()) {
                    // System.out.println("[AttributeIterator] is object node");
                    // object & class attributes of object nodes!
                    if ((Restriction.OBJECT & restriction.getTypeRestriction()) != 0) {
                        // System.out.println("[AttributeIterator] is object restriction");
                        // Sirius.server.localserver.attribute.Attribute[] classAttributes =
                        // ((ObjectTreeNode)treeNode).getMetaClassAttributes();
                        // Sirius.server.localserver.attribute.Attribute[] objectAttributes =
                        // ((ObjectTreeNode)treeNode).getObjectAttributes();

                        /*if(classAttributes == null || classAttributes.length == 0)
                         * { this.attributes = objectAttributes; } else if(objectAttributes == null ||
                         * objectAttributes.length == 0) { this.attributes = classAttributes; } else { this.attributes =
                         * new Sirius.server.localserver.attribute.Attribute[classAttributes.length +
                         * objectAttributes.length]; System.arraycopy(classAttributes, 0, this.attributes,  0,
                         * classAttributes.length); //System.out.println("[AttributeIterator] classAttributes.length: "
                         * + classAttributes.length); //System.out.println("[AttributeIterator] objectAttributes.length:
                         * " + objectAttributes.length); System.arraycopy(objectAttributes, 0, this.attributes,
                         * classAttributes.length,  objectAttributes.length);} */

                        final Collection metaAttributes = new LinkedList();

                        try {
                            metaAttributes.addAll(((ObjectTreeNode)treeNode).getMetaClass().getAttributes());
                        } catch (Exception exp) {
                            exp.printStackTrace();
                        }

                        metaAttributes.addAll(((ObjectTreeNode)treeNode).getMetaObject().getAttributes().values());

                        this.attributeIterator = metaAttributes.iterator();
                    } else {
                        // System.out.println("[AttributeIterator] is class restriction");
                        // this.metaAttributes = ((ObjectTreeNode)treeNode).getMetaClass().getAttributes();
                        this.attributeIterator = ((ObjectTreeNode)treeNode).getMetaClass().getAttributes().iterator();
                    }

                    // return this.metaAttributes.size() > 0 ? true : false;
                    return this.attributeIterator.hasNext();
                }
            } else if (treeNode.isObjectNode() && ((Restriction.OBJECT & restriction.getTypeRestriction()) != 0)) {
                // System.out.println("[AttributeIterator] is object restriction and is object node");
                // this.attributes = ((ObjectTreeNode)treeNode).getObjectAttributes();
                // this.metaAttributes = ((ObjectTreeNode)treeNode).getMetaObject().getAttributes();
                this.attributeIterator = ((ObjectTreeNode)treeNode).getMetaObject().getAttributes().values().iterator();
                return true;
            }
            /*else
             * { System.out.println("[AttributeIterator] unknown restriction '" + restriction.getTypeRestriction() +
             * "'");}*/
        } catch (Exception exp) {
            exp.printStackTrace();
        }

        return false;
    }

    /*
     * public AttributeIterator(Collection collection) { this(collection, new TreeNodeRestriction()); } public
     * AttributeIterator(TreeNodeIterator treeNodeIterator, AttributeRestriction restriction) { if(collection != null &&
     * collection.size() > 0) {     this.iterator = collection.iterator();     this.restriction = restriction; } else {
     * this.iterator = null;     this.restriction = null; } }
     *
     * public AttributeIterator(DefaultTreeNode treeNode, AttributeRestriction restriction) { if(collection != null &&
     * collection.size() > 0) {     this.iterator = collection.iterator();     this.restriction = restriction; } else {
     *    this.iterator = null;     this.restriction = null; }}*/

    @Override
    public boolean hasNext() {
        if (nextElement != null) {
            return true;
        }
        /*else if(attributes != null && i < attributes.length)
         * { // elemente herausfiltern while(i < attributes.length && (nextElement =
         * restriction.applyRestriction(attributes[i])) == null) {     i++; }     // duplikate herausfiltern
         * if(this.isDistinct() && !hashSet.add(nextElement)) {     nextElement = null;     this.hasNext(); }  i++;
         * return nextElement != null ? true : false;}*/
        else if ((this.attributeIterator != null) && this.attributeIterator.hasNext()) {
            while (this.attributeIterator.hasNext()
                        && ((nextElement = restriction.applyRestriction(
                                        (Sirius.server.localserver.attribute.Attribute)this.attributeIterator.next()))
                            == null)) {
                ;
            }

            // duplikate herausfiltern
            if (this.isDistinct() && !hashSet.add(nextElement)) {
                nextElement = null;
                this.hasNext();
            }

            return (nextElement != null) ? true : false;
        }

        return false;
    }

    @Override
    public Sirius.server.localserver.attribute.Attribute next() throws NoSuchElementException {
        if (this.hasNext()) {
            final Sirius.server.localserver.attribute.Attribute next = nextElement;
            nextElement = null;
            return next;
        } else {
            throw new NoSuchElementException("NoSuchElementException"); // NOI18N
        }
    }

    @Override
    public boolean isDistinct() {
        return (hashSet != null) ? true : false;
    }
}
