/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 *  Copyright (C) 2011 thorsten
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.cismet.cids.utils.abstracts;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import de.cismet.cids.dynamics.CidsBean;

import de.cismet.cids.utils.interfaces.CidsBeanAction;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public abstract class AbstractCidsBeanAction extends AbstractAction implements CidsBeanAction {

    //~ Instance fields --------------------------------------------------------

    private CidsBean source;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new AbstractCidsBeanAction object.
     */
    public AbstractCidsBeanAction() {
        super();
    }

    /**
     * Creates a new AbstractCidsBeanAction object.
     *
     * @param  name  DOCUMENT ME!
     */
    public AbstractCidsBeanAction(final String name) {
        super(name);
    }

    /**
     * Creates a new AbstractCidsBeanAction object.
     *
     * @param  name  DOCUMENT ME!
     * @param  icon  DOCUMENT ME!
     */
    public AbstractCidsBeanAction(final String name, final Icon icon) {
        super(name, icon);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public CidsBean getCidsBean() {
        return source;
    }

    @Override
    public void setCidsBean(final CidsBean cidsBean) {
        source = cidsBean;
    }
}
