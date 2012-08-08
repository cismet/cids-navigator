/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.store;

import java.io.Serializable;

/**
 * Abstract implementation of the {@link ObjectStoreHandler} interface for 
 * your convience.
 *
 * @author   Benjamin Friedrich (benjamin.friedrich@cismet.de)
 * @version  $Revision$, $Date$
 */
public abstract class AbstractObjectStoreHandler implements ObjectStoreHandler {

    //~ Instance fields --------------------------------------------------------

    protected final Group group;
    protected final Integer id;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new AbstractObjectStoreHandler object.
     *
     * @param  group  DOCUMENT ME!
     * @param  id     DOCUMENT ME!
     */
    public AbstractObjectStoreHandler(final Group group, final Integer id) {
        this.group = group;
        this.id = id;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * {@inheritDoc }
     */
    @Override
    public Group getGroup() {
        return this.group;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Integer getId() {
        return this.id;
    }
}
