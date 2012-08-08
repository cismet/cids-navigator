/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * EmbeddedContainersMap.java
 *
 * Created on 28. M\u00E4rz 2003, 11:38
 */
package Sirius.navigator.ui.embedded;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class EmbeddedContainersMap extends AbstractEmbeddedComponentsMap {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of EmbeddedContainersMap.
     */
    public EmbeddedContainersMap() {
        super();
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    protected void doAdd(final EmbeddedComponent component) {
        if (component instanceof EmbeddedContainer) {
            ((EmbeddedContainer)component).addComponents();
        } else {
            logger.error("doAdd(): object '" + component
                        + "' is not of type 'Sirius.navigator.ui.embedded.EmbeddedContainer' but '"
                        + component.getClass().getName() + "'"); // NOI18N
        }
    }

    @Override
    protected void doRemove(final EmbeddedComponent component) {
        if (component instanceof EmbeddedContainer) {
            ((EmbeddedContainer)component).removeComponents();
        } else {
            logger.error("doRemove(): object '" + component
                        + "' is not of type 'Sirius.navigator.ui.embedded.EmbeddedContainer' but '"
                        + component.getClass().getName() + "'"); // NOI18N
        }
    }
}
