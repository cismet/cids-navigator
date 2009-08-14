/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Sirius.navigator.plugin.interfaces;

import java.util.Vector;
import javax.swing.AbstractButton;

/**
 *
 * @author spuhl
 */
public interface EmbededControlBar {
    public void setControlBarVisible(boolean isVisible);
    public Vector<AbstractButton> getControlBarButtons();
}
