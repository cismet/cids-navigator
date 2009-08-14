/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.cids.editors;

import java.util.HashMap;


/**
 *
 * @author thorsten
 */
public interface BindingInformationProvider {

    public void addControlInformation(String name, Bindable component);

    public Bindable getControlByName(String name);

    public HashMap<String, Bindable> getAllControls();

  

}
