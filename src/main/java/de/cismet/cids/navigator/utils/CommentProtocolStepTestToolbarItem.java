/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.navigator.utils;

import org.openide.util.lookup.ServiceProvider;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import de.cismet.tools.StaticDebuggingTools;

import de.cismet.tools.gui.StaticSwingTools;

/**
 * DOCUMENT ME!
 *
 * @author   mroncoroni
 * @version  $Revision$, $Date$
 */
@ServiceProvider(service = CidsClientToolbarItem.class)
public class CommentProtocolStepTestToolbarItem extends AbstractAction implements CidsClientToolbarItem {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new QuerySearchToolbarItem object.
     */
    public CommentProtocolStepTestToolbarItem() {
        putValue(Action.SHORT_DESCRIPTION, "Comment Protocol Test");
        putValue(Action.NAME, "Comment Protocol Test");
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void actionPerformed(final ActionEvent e) {
        StaticSwingTools.showDialog(new CommentProtocolStepTestDialog());
    }

    @Override
    public String getSorterString() {
        return "Z";
    }

    @Override
    public boolean isVisible() {
        return StaticDebuggingTools.checkHomeForFile("cismetCommentProtocolTestEnabled");
    }
}
