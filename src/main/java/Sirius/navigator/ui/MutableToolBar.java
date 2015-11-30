/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui;

import Sirius.navigator.exception.ExceptionManager;
import Sirius.navigator.method.MethodManager;
import Sirius.navigator.resource.PropertyManager;
import Sirius.navigator.resource.ResourceManager;
import Sirius.navigator.ui.embedded.AbstractEmbeddedComponentsMap;
import Sirius.navigator.ui.embedded.EmbeddedComponent;
import Sirius.navigator.ui.embedded.EmbeddedToolBar;

import com.jgoodies.looks.HeaderStyle;
import com.jgoodies.looks.Options;

import org.apache.log4j.Logger;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

/**
 * Eine Toolbar, zu der zur Laufzeit automatisch neue Buttons hinzugefuegt- und entfernt werden koennen.
 *
 * @version  $Revision$, $Date$
 */
public class MutableToolBar extends JToolBar {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger logger = Logger.getLogger(MutableToolBar.class);

    private static final ResourceManager resources = ResourceManager.getManager();

    //~ Instance fields --------------------------------------------------------

    private final JToolBar defaultToolBar;
    private final JToolBar pluginToolBar;
    private final JToolBar rightStickyToolBar;
    private final MoveableToolBarsMap moveableToolBars;
    private final PluginToolBarsMap pluginToolBars;

    private final boolean advancedLayout;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new MutableToolBar object.
     */
    public MutableToolBar() {
        this(false);
    }

    /**
     * Creates a new MutableToolBar object.
     *
     * @param  advancedLayout  DOCUMENT ME!
     */
    public MutableToolBar(final boolean advancedLayout) {
        super(HORIZONTAL);

        this.advancedLayout = advancedLayout;

        this.defaultToolBar = new JToolBar(
                org.openide.util.NbBundle.getMessage(MutableToolBar.class, "MutableToolBar.defaultToolBar.name"), // NOI18N
                HORIZONTAL);
        this.pluginToolBar = new JToolBar(HORIZONTAL);
        this.rightStickyToolBar = new JToolBar(HORIZONTAL);

        this.moveableToolBars = new MoveableToolBarsMap(pluginToolBar);
        this.pluginToolBars = new PluginToolBarsMap(pluginToolBar);

        this.defaultToolBar.setFloatable(false);
        this.defaultToolBar.setRollover(advancedLayout);
        this.defaultToolBar.putClientProperty("JToolBar.isRollover", Boolean.TRUE); // NOI18N
        this.defaultToolBar.putClientProperty(Options.HEADER_STYLE_KEY, HeaderStyle.BOTH);

        this.pluginToolBar.setFloatable(false);
        this.pluginToolBar.setRollover(advancedLayout);
        this.pluginToolBar.putClientProperty("JToolBar.isRollover", Boolean.TRUE); // NOI18N
        this.pluginToolBar.putClientProperty(Options.HEADER_STYLE_KEY, HeaderStyle.BOTH);

        this.rightStickyToolBar.setFloatable(false);
        this.rightStickyToolBar.setRollover(advancedLayout);
        this.rightStickyToolBar.putClientProperty("JToolBar.isRollover", Boolean.TRUE); // NOI18N
        this.rightStickyToolBar.putClientProperty(Options.HEADER_STYLE_KEY, HeaderStyle.BOTH);

        final JPanel filler = new JPanel();
        filler.add(Box.createHorizontalGlue());
        this.rightStickyToolBar.add(filler);

        this.createDefaultButtons();

        this.add(defaultToolBar);
        this.add(pluginToolBar);
        this.add(rightStickyToolBar);

        putClientProperty("JToolBar.isRollover", Boolean.TRUE); // NOI18N
        putClientProperty(Options.HEADER_STYLE_KEY, HeaderStyle.BOTH);
        this.setFloatable(false);

        if (advancedLayout) {
            this.setBorder(null);
            pluginToolBar.setBorder(null);
        }
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    private void createDefaultButtons() {
        if (logger.isDebugEnabled()) {
            logger.debug("creating default buttons"); // NOI18N
        }

        final ActionListener toolBarListener = new ToolBarListener();
        final JButton button;

        if (PropertyManager.getManager().isEnableSearchDialog()) {
            button = new JButton(resources.getIcon("find24.gif"));            // NOI18N
            button.setToolTipText(org.openide.util.NbBundle.getMessage(
                    MutableToolBar.class,
                    "MutableToolBar.createDefaultButtons().search.tooltip")); // NOI18N
            button.setActionCommand("search");                                // NOI18N
            button.setMargin(new Insets(4, 4, 4, 4));
            button.addActionListener(toolBarListener);
            defaultToolBar.add(button);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  toolBar  DOCUMENT ME!
     */
    public void addMoveableToolBar(final EmbeddedToolBar toolBar) {
        toolBar.setRollover(this.advancedLayout);
        toolBar.putClientProperty("JToolBar.isRollover", Boolean.TRUE); // NOI18N
        toolBar.putClientProperty(Options.HEADER_STYLE_KEY, HeaderStyle.BOTH);
        toolBar.putClientProperty(Options.HEADER_STYLE_KEY, HeaderStyle.BOTH);
        this.moveableToolBars.add(toolBar);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  id  DOCUMENT ME!
     */
    public void removeMoveableToolBar(final String id) {
        this.moveableToolBars.remove(id);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  id       DOCUMENT ME!
     * @param  visible  DOCUMENT ME!
     */
    public void setMoveableToolBarVisible(final String id, final boolean visible) {
        this.moveableToolBars.setVisible(id, visible);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   id  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isMoveableToolBarVisible(final String id) {
        return this.moveableToolBars.isVisible(id);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  id       DOCUMENT ME!
     * @param  enabled  DOCUMENT ME!
     */
    public void setMoveableToolBarEnabled(final String id, final boolean enabled) {
        this.moveableToolBars.setEnabled(id, enabled);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   id  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isMoveableToolBarEnabled(final String id) {
        return this.moveableToolBars.isEnabled(id);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   id  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isMoveableToolBarAvailable(final String id) {
        return this.moveableToolBars.isAvailable(id);
    }

    /**
     * PLUGIN TOOLBARS ---------------------------------------------------------
     *
     * @param  toolBar  DOCUMENT ME!
     */
    public void addPluginToolBar(final EmbeddedToolBar toolBar) {
        toolBar.setRollover(this.advancedLayout);
        toolBar.putClientProperty("JToolBar.isRollover", Boolean.TRUE); // NOI18N
        toolBar.putClientProperty(Options.HEADER_STYLE_KEY, HeaderStyle.BOTH);
        toolBar.putClientProperty(Options.HEADER_STYLE_KEY, HeaderStyle.BOTH);
        this.pluginToolBars.add(toolBar);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  id  DOCUMENT ME!
     */
    public void removePluginToolBar(final String id) {
        this.pluginToolBars.remove(id);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  id       DOCUMENT ME!
     * @param  enabled  DOCUMENT ME!
     */
    public void setPluginToolBarEnabled(final String id, final boolean enabled) {
        this.pluginToolBars.setEnabled(id, enabled);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   id  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isPluginToolBarEnabled(final String id) {
        return this.pluginToolBars.isEnabled(id);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   id  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isPluginToolBarAvailable(final String id) {
        return this.pluginToolBars.isAvailable(id);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public JToolBar getDefaultToolBar() {
        return defaultToolBar;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public JToolBar getRightStickyToolBar() {
        return rightStickyToolBar;
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class ToolBarListener implements ActionListener {

        //~ Methods ------------------------------------------------------------

        /**
         * Invoked when an action occurs.
         *
         * @param  e  DOCUMENT ME!
         */
        @Override
        public void actionPerformed(final ActionEvent e) {
            if (e.getActionCommand().equals("exit"))          // NOI18N
            {
                if (ExceptionManager.getManager().showExitDialog(ComponentRegistry.getRegistry().getMainWindow())) {
                    logger.info("closing program");           // NOI18N
                    ComponentRegistry.getRegistry().getNavigator().dispose();
                    System.exit(0);
                }
            } else if (e.getActionCommand().equals("plugin")) // NOI18N
            {
                MethodManager.getManager().showPluginManager();
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class PluginToolBarsMap extends AbstractEmbeddedComponentsMap {

        //~ Instance fields ----------------------------------------------------

        protected final JToolBar toolbar;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new PluginToolBarsMap object.
         *
         * @param  toolbar  DOCUMENT ME!
         */
        private PluginToolBarsMap(final JToolBar toolbar) {
            this.toolbar = toolbar;
        }

        //~ Methods ------------------------------------------------------------

        @Override
        protected void doAdd(final EmbeddedComponent component) {
            if (logger.isDebugEnabled()) {
                logger.debug("adding toolbar: '" + component + "'");             // NOI18N
            }
            if (component instanceof EmbeddedToolBar) {
                toolbar.add((EmbeddedToolBar)component);
            } else {
                logger.error("doAdd(): invalid object type '" + component.getClass().getName()
                            + "', 'Sirius.navigator.EmbeddedToolBar' expected"); // NOI18N
            }

            toolbar.invalidate();
            SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        synchronized (getTreeLock()) {
                            validateTree();
                        }
                        repaint();
                    }
                });
        }

        @Override
        protected void doRemove(final EmbeddedComponent component) {
            if (component instanceof EmbeddedToolBar) {
                toolbar.remove((EmbeddedToolBar)component);
            } else {
                logger.error("doRemove(): invalid object type '" + component.getClass().getName()
                            + "', 'Sirius.navigator.EmbeddedToolBar' expected"); // NOI18N
            }

            toolbar.invalidate();
            SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        synchronized (getTreeLock()) {
                            validateTree();
                        }
                        toolbar.repaint();
                    }
                });
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class MoveableToolBarsMap extends PluginToolBarsMap {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new MoveableToolBarsMap object.
         *
         * @param  toolbar  DOCUMENT ME!
         */
        private MoveableToolBarsMap(final JToolBar toolbar) {
            super(toolbar);
        }

        //~ Methods ------------------------------------------------------------

        @Override
        protected void doSetVisible(final EmbeddedComponent component, final boolean visible) {
            if (component.isVisible() != visible) {
                super.doSetVisible(component, visible);

                if (visible) {
                    doAdd(component);
                } else {
                    doRemove(component);
                }
            } else {
                logger.warn("unexpected call to 'setVisible()': '" + visible + "'"); // NOI18N
            }
        }
    }
}
