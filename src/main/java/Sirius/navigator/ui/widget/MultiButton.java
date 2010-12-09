/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * MultiButton.java
 *
 * Created on 18. Juni 2004, 15:05
 */
package Sirius.navigator.ui.widget;

import java.awt.*;
import java.awt.event.*;

import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class MultiButton extends JButton {

    //~ Instance fields --------------------------------------------------------

    private final LinkedHashMap actionMap;
    private final ActionMenu actionMenu;

    private String activeAction = null;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of MultiButton.
     */
    public MultiButton() {
        this.actionMap = new LinkedHashMap();
        this.actionMenu = new ActionMenu();

        this.addMouseListener(new PopupListener());
    }

    /**
     * Creates a new MultiButton object.
     *
     * @param  actions  DOCUMENT ME!
     */
    public MultiButton(final Collection actions) {
        this.actionMap = new LinkedHashMap();
        this.actionMenu = new ActionMenu();

        this.addMouseListener(new PopupListener());
        this.setActions(actions);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  actions  DOCUMENT ME!
     */
    private void setActions(final Collection actions) {
        final Iterator iterator = actions.iterator();
        while (iterator.hasNext()) {
            final Action action = (Action)iterator.next();
            this.actionMap.put(action.getValue(Action.ACTION_COMMAND_KEY), action);

            if (this.getAction() == null) {
                this.setActiveAction(action.getValue(Action.ACTION_COMMAND_KEY).toString());
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  activeAction  DOCUMENT ME!
     */
    private void setActiveAction(final String activeAction) {
        if (!activeAction.equals(this.activeAction) && this.actionMap.containsKey(activeAction)) {
            this.activeAction = activeAction;
            this.setAction((Action)this.actionMap.get(activeAction));
        }
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * public static void main(String args[]) {
     * Sirius.navigator.resource.PropertyManager.getManager().setLookAndFeel("Plastic 3D");
     * Sirius.navigator.ui.LAFManager.getManager().changeLookAndFeel(Sirius.navigator.resource.PropertyManager.getManager().getLookAndFeel());
     * JToolBar jt = new JToolBar(); MultiButton mb = new MultiButton(); mb.setMargin(new Insets(5,5,5,5)); jt.add(mb);
     * ArrayList actions = new ArrayList(3); Action action = new AbstractAction() { public void
     * actionPerformed(ActionEvent e) { System.out.println(this.getValue(Action.ACTION_COMMAND_KEY)); } };
     * action.putValue(Action.ACTION_COMMAND_KEY, "a"); action.putValue(Action.SMALL_ICON,
     * Sirius.navigator.resource.ResourceManager.getManager().getIcon("save24.gif")); actions.add(action); action = new
     * AbstractAction() { public void actionPerformed(ActionEvent e) {
     * System.out.println(this.getValue(Action.ACTION_COMMAND_KEY)); } }; action.putValue(Action.ACTION_COMMAND_KEY,
     * "b"); action.putValue(Action.SMALL_ICON,
     * Sirius.navigator.resource.ResourceManager.getManager().getIcon("information24.gif")); actions.add(action); action
     * = new AbstractAction() { public void actionPerformed(ActionEvent e) {
     * System.out.println(this.getValue(Action.ACTION_COMMAND_KEY)); } }; action.putValue(Action.ACTION_COMMAND_KEY,
     * "c"); action.putValue(Action.SMALL_ICON,
     * Sirius.navigator.resource.ResourceManager.getManager().getIcon("back24.gif")); actions.add(action);
     * mb.setActions(actions); JFrame jf = new JFrame("MultiButtonTest"); jf.getContentPane().add(jt,
     * BorderLayout.NORTH); jf.getContentPane().add(new JButton(":o)"), BorderLayout.CENTER);
     * jf.setDefaultCloseOperation(jf.EXIT_ON_CLOSE); jf.setSize(640,480); jf.setLocationRelativeTo(null);
     * jf.setVisible(true); jf.setDefaultCloseOperation(jf.EXIT_ON_CLOSE); }.
     *
     * @version  $Revision$, $Date$
     */
    private class PopupListener extends MouseAdapter {

        //~ Methods ------------------------------------------------------------

        @Override
        public void mousePressed(final MouseEvent e) {
            if (e.isPopupTrigger()) {
                actionMenu.show(MultiButton.this, -1, MultiButton.this.getHeight());
            }
        }

        @Override
        public void mouseReleased(final MouseEvent e) {
            if (e.isPopupTrigger() && !actionMenu.isShowing()) {
                actionMenu.show(MultiButton.this, -1, MultiButton.this.getHeight());
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class ActionMenu extends JPopupMenu implements PopupMenuListener, ActionListener {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new ActionMenu object.
         */
        private ActionMenu() {
            this.addPopupMenuListener(this);
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public void popupMenuCanceled(final PopupMenuEvent e) {
        }

        @Override
        public void popupMenuWillBecomeInvisible(final PopupMenuEvent e) {
            this.removeAll();
        }

        @Override
        public void popupMenuWillBecomeVisible(final PopupMenuEvent e) {
            final Iterator iterator = actionMap.values().iterator();
            while (iterator.hasNext()) {
                final Action action = (Action)iterator.next();
                if (!action.getValue(Action.ACTION_COMMAND_KEY).equals(activeAction)) {
                    final JMenuItem item = this.add(action);

                    item.addActionListener(this);
                    item.setMargin(MultiButton.this.getMargin());
                    // item.setBorder(MultiButton.this.getBorder());
                    item.setPreferredSize(MultiButton.this.getPreferredSize());
                }
            }

            this.pack();
            // this.setPreferredSize(new Dimension(MultiButton.this.getWidth(), MultiButton.this.getHeight() *
            // (MultiButton.this.actionMap.size() - 1)));
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            MultiButton.this.setActiveAction(e.getActionCommand());
        }
    }
}
