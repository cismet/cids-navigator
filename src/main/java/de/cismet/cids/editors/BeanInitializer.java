package de.cismet.cids.editors;

import de.cismet.cids.dynamics.CidsBean;

/**
 *
 * @author srichter
 */
public interface BeanInitializer {

    void initializeBean(CidsBean beanToInit) throws Exception;
}
