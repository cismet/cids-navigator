/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Sirius.navigator.plugin.interfaces;

import java.awt.Component;

/**
 *
 * @author spuhl
 */
public interface LayoutManager {
    public void saveCurrentLayout(Component parent);
    public void loadLayout(Component parent);
    public void resetLayout();
}
