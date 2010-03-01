/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.cids.utils;

import de.cismet.cids.dynamics.CidsBean;
import java.util.ArrayList;

/**
 *
 * @author thorsten
 */
public interface CidsBeanDropListener {

    public void beansDropped(ArrayList<CidsBean> beans);
}
