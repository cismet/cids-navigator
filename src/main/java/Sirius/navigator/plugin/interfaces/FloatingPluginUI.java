/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
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
 * blah.
 *
 * @author   Pascal
 * @version  1.0 02/15/2003
 * @see      FloatingFrame
 */
public interface FloatingPluginUI extends PluginUI {

    //~ Methods ----------------------------------------------------------------

    /**
     * Callback Method.
     *
     * <p>The <i>Plugin Manager</i> calls this method <b>after</b> the plugin ui component has started floating. See the
     * <code>FloatingFrame</code> documentation for further details.</p>
     */
    void floatingStarted();

    /**
     * Callback Method.
     *
     * <p>The <i>Plugin Manager</i> calls this method <b>after</b> the plugin ui component has stopped floating. See the
     * <code>FloatingFrame</code> documentation for further details. <i>Plugin Descriptor</i></p>
     */
    void floatingStopped();

    /**
     * this method should return null.
     *
     * @return  a list containg the <code>JComponents</code> (buttons & separators) to be added to the navigator
     *          toolbar, or null
     */
    java.util.Collection getButtons();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    java.util.Collection getMenus();
}
