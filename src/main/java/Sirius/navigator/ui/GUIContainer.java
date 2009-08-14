/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Sirius.navigator.ui;

/**
 *
 * @author spuhl
 */
public interface GUIContainer {
    public void select(String id);
    public void remove(String id);
    public void add(MutableConstraints constraints);
}
