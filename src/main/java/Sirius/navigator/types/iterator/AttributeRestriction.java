package Sirius.navigator.types.iterator;

import Sirius.navigator.types.treenode.*;
import Sirius.server.middleware.types.*;

import java.util.Collection;

/**
 *
 * @author  pascal
 */
public interface AttributeRestriction extends Restriction
{    
    public Sirius.server.localserver.attribute.Attribute applyRestriction(Sirius.server.localserver.attribute.Attribute attribute);
}