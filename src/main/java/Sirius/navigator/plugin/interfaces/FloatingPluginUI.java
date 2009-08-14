package Sirius.navigator.plugin.interfaces;

/*******************************************************************************

  Copyright (c)     :       EIG (Environmental Informatics Group)
                            http://www.htw-saarland.de/eig
                            Prof. Dr. Reiner Guettler
                            Prof. Dr. Ralf Denzer

                            HTWdS
                            Hochschule fuer Technik und Wirtschaft des Saarlandes
                            Goebenstr. 40
                            66117 Saarbruecken
                            Germany

  Programmers       :       Pascal <pascal.dihe@enviromatics.net>

  Project           :       WuNDA 2
  Version           :       1.0
  Purpose           :
  Created           :       02/15/2003
  History           :       


*******************************************************************************/


//import Sirius.navigator.plugin.exceptions.*;
import java.util.ArrayList;

/**
 * blah<p>
 *
 * @version 1.0 02/15/2003
 * @author Pascal
 * @see FloatingFrame
 */
public interface FloatingPluginUI extends PluginUI
{
    /**
     * Callback Method.<p>
     * The <i>Plugin Manager</i> calls this method <b>after</b> the plugin ui 
     * component has started floating. See the <code>FloatingFrame</code>
     * documentation for further details.
     */
    public void floatingStarted();
    
    /**
     * Callback Method.<p>
     * The <i>Plugin Manager</i> calls this method <b>after</b> the plugin ui 
     * component has stopped floating. See the <code>FloatingFrame</code>
     * documentation for further details. <i>Plugin Descriptor</i>
     */
    public void floatingStopped();
    
    /**
     * this method should return null
     *
     * @return a list containg the <code>JComponents</code> (buttons & separators) to be added to the navigator toolbar, or null
     */
    public java.util.Collection getButtons();
    
    public java.util.Collection getMenus();
   
}
