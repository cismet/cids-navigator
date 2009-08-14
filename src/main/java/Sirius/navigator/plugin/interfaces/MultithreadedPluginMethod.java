package Sirius.navigator.plugin.interfaces;

import Sirius.navigator.method.MultithreadedMethod;
import Sirius.navigator.plugin.context.*;

/**
 *
 * @author  pascal
 */
public abstract class MultithreadedPluginMethod extends MultithreadedMethod implements PluginMethod
{
    /** Creates a new instance of PluginMultithreadedMethod */
    public MultithreadedPluginMethod(PluginProgressObserver progressObserver)
    {
        super(progressObserver);
    }  
}
