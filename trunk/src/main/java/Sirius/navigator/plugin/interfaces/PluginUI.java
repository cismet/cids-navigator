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

import javax.swing.JComponent;

/**
 * blah.
 *
 * @author   Pascal
 * @version  1.0 02/15/2003
 */
public interface PluginUI {

    //~ Methods ----------------------------------------------------------------

    /**
     * Callback Method.
     *
     * <p>This method call indicates that the plugin ui component was made visible. To recieve this event,</p>
     */
    void shown();

    /**
     * Callback Method.
     *
     * <p>This method call indicates that the component was rendered invisible.</p>
     */
    void hidden();

    /**
     * Callback Method.
     *
     * <p>This method call indicates that the plugin ui component's size changed.</p>
     */
    void resized();

    /**
     * Callback Method.
     *
     * <p>This method call indicates that the plugin ui component's position changed.</p>
     */
    void moved();

    /**
     * Callback Method, is called only once.
     *
     * <p>This method call indicates that the plugin ui component was made visible for the first time.</p>
     *
     * @param  visible  DOCUMENT ME!
     */
    void setVisible(boolean visible);

    /**
     * Should return a reference of the plugin ui component.
     *
     * @return  a reference of the plugin ui component.
     */
    JComponent getComponent();

    /**
     * Getter for property id.
     *
     * @return  Value of property id.
     */
    String getId();
}
