/*
 * Copyright (C) 2013 cismet GmbH
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.cismet.cids.search;

import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.exception.ConnectionException;
import Sirius.navigator.ui.ComponentRegistry;
import de.cismet.cids.navigator.utils.CidsClientToolbarItem;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import org.jfree.util.Log;
import org.openide.util.Exceptions;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author mroncoroni
 */
@ServiceProvider(service = CidsClientToolbarItem.class)
public class QuerySearchToolbarItem  extends AbstractAction implements CidsClientToolbarItem {

    public QuerySearchToolbarItem() {
        putValue(Action.SHORT_DESCRIPTION, "QuerySearch");
        putValue(Action.SMALL_ICON, new ImageIcon(this.getClass().getResource("/de/cismet/cids/search/binocular.png")));
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String id = QuerySearch.class.getName();
        ComponentRegistry.getRegistry().getGUIContainer().select(id);
    }

    @Override
    public String getSorterString() {
        return "Z";
    }

    @Override
    public boolean isVisible() {
        try {
            return (SessionManager.getConnection()
                            .getConfigAttr(SessionManager.getSession().getUser(),
                                    "navigator.querybuilder.toolbaricon@"
                                    + SessionManager.getSession().getUser().getDomain())
                            != null) && (SessionManager.getConnection()
                            .getConfigAttr(SessionManager.getSession().getUser(),
                                    QuerySearch.ACTION_TAG
                                    + SessionManager.getSession().getUser().getDomain())
                            != null);
        } catch (ConnectionException ex) {
            Log.error(ex);
        }
        return false;
    }
    
}
