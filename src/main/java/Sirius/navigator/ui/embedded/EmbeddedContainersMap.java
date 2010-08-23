/*
 * EmbeddedContainersMap.java
 *
 * Created on 28. M\u00E4rz 2003, 11:38
 */

package Sirius.navigator.ui.embedded;

/**
 *
 * @author  pascal
 */
public class EmbeddedContainersMap extends AbstractEmbeddedComponentsMap
{
    
    /** Creates a new instance of EmbeddedContainersMap */
    public EmbeddedContainersMap()
    {
        super();
    }
    
    protected void doAdd(EmbeddedComponent component)
    {
        if(component instanceof EmbeddedContainer)
        {
            ((EmbeddedContainer)component).addComponents();
        }
        else
        {
            logger.error("doAdd(): object '" + component + "' is not of type 'Sirius.navigator.ui.embedded.EmbeddedContainer' but '" + component.getClass().getName() + "'");//NOI18N
        }
    }
    
    protected void doRemove(EmbeddedComponent component)
    {
        if(component instanceof EmbeddedContainer)
        {
            ((EmbeddedContainer)component).removeComponents();
        }
        else
        {
            logger.error("doRemove(): object '" + component + "' is not of type 'Sirius.navigator.ui.embedded.EmbeddedContainer' but '" + component.getClass().getName() + "'");//NOI18N
        }
    }   
}
