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
 * blah<p>
 *
 * @version 1.0 02/15/2003
 * @author Pascal
 */
public interface PluginUI
{
    
    /**
     * Callback Method<p>
     * This method call indicates that the plugin ui component was made visible.
     * To recieve this event, 
     */
    public void shown();
    
    /**
     * Callback Method<p>
     * This method call indicates that the component was rendered invisible.
     */
    public void hidden();
    
    /**
     * Callback Method<p>
     * This method call indicates that the plugin ui component's size changed.
     */
    public void resized();
    
    /**
     * Callback Method<p>
     * This method call indicates that the plugin ui component's position changed.
     */
    public void moved();
    
    /**
     * Callback Method, is called only once<p>
     * This method call indicates that the plugin ui component was made visible
     * for the first time.
     */
    public void setVisible(boolean visible);
    
    /**
     * Should return a reference of the plugin ui component.
     *
     * @return a reference of the plugin ui component.
     */
    public JComponent getComponent();
    
    /** Getter for property id.
     * @return Value of property id.
     *
     */
    public String getId();
        
}