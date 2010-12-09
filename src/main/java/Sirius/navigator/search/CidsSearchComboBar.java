/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.search;

import org.openide.util.ImageUtilities;
import org.openide.util.Utilities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.text.JTextComponent;

import de.cismet.cids.tools.search.clientstuff.CidsToolbarSearch;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class CidsSearchComboBar extends javax.swing.JPanel implements ActionListener {

    //~ Static fields/initializers ---------------------------------------------

    private static final String SEARCH = "search";

    //~ Instance fields --------------------------------------------------------

    private int width;
    private Color origForeground;
    private JTextComponent command;
    private CidsToolbarSearch currentSearch;
    private final Collection<CidsToolbarSearch> searches;
    private final ImageIcon findIcon = new javax.swing.ImageIcon(getClass().getResource(
                "/Sirius/navigator/search/find.png")); // NOI18N
    private final Timer animationTimer = new Timer(100, new ActionListener() {

                ImageIcon[] icons;
                int index = 0;

                @Override
                public void actionPerformed(final ActionEvent e) {
                    if (icons == null) {
                        icons = new ImageIcon[8];
                        for (int i = 0; i < 8; i++) {
                            icons[i] = ImageUtilities.loadImageIcon(
                                    "/Sirius/navigator/search/progress_"
                                            + i
                                            + ".png",
                                    false); // NOI18N
                        }
                    }
                    jLabel2.setIcon(icons[index]);
                    index = (index + 1) % 8;
                }
            });
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new CidsSearchComboBar object.
     */
    public CidsSearchComboBar() {
        this(300);
    }

    /**
     * Creates a new CidsSearchComboBar object.
     *
     * @param  width  DOCUMENT ME!
     */
    public CidsSearchComboBar(final int width) {
        this.width = width;
        this.searches = new ArrayList<CidsToolbarSearch>();
        initComponents();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  searches  DOCUMENT ME!
     */
    public void setSearches(final Collection<? extends CidsToolbarSearch> searches) {
        this.searches.clear();
        this.searches.addAll(searches);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  search  DOCUMENT ME!
     */
    public void addSearch(final CidsToolbarSearch search) {
        this.searches.add(search);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   search  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean removeSearch(final CidsToolbarSearch search) {
        return this.searches.remove(search);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    static Color getTextBackground() {
        Color textB = UIManager.getColor("TextPane.background");
        if ("Aqua".equals(UIManager.getLookAndFeel().getID()))       // NOI18N
        {
            textB = UIManager.getColor("NbExplorerView.background"); // NOI18N
        }
        return (textB != null) ? textB : Color.WHITE;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    static Color getComboBorderColor() {
        final Color shadow = UIManager.getColor(
                Utilities.isWindows() ? "Nb.ScrollPane.Border.color" : "TextField.shadow");
        return (shadow != null) ? shadow : getPopupBorderColor();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    static Color getPopupBorderColor() {
        final Color shadow = UIManager.getColor("controlShadow");
        return (shadow != null) ? shadow : Color.GRAY;
    }

    /**
     * DOCUMENT ME!
     */
    private void initComponents() {
        command = createCommandField();
        command.setName("command"); // NOI18N
        command.addFocusListener(new java.awt.event.FocusAdapter() {

                @Override
                public void focusGained(final java.awt.event.FocusEvent evt) {
                    setShowHint(false);
//                commandFocusGained(evt);
                }

                @Override
                public void focusLost(final java.awt.event.FocusEvent evt) {
                    setShowHint(true);
//                commandFocusLost(evt);
                }
            });
        command.addKeyListener(new java.awt.event.KeyAdapter() {

                @Override
                public void keyPressed(final java.awt.event.KeyEvent evt) {
                    commandKeyPressed(evt);
                }
            });

//        command.addMouseListener(new MouseAdapter() {
//
//            public
//            @Override
//            void mouseClicked(MouseEvent e) {
//                displayer.explicitlyInvoked();
//            }
//        });
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jSeparator1 = new javax.swing.JSeparator();

        setLayout(new java.awt.GridBagLayout());

        jPanel1.setBackground(getTextBackground());
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(getComboBorderColor()));
        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel2.setIcon(findIcon);
//        jLabel2.setToolTipText(org.openide.util.NbBundle.getMessage(QuickSearchComboBar.class, "QuickSearchComboBar.jLabel2.toolTipText")); // NOI18N
        jLabel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jLabel2.setName("jLabel2"); // NOI18N
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {

                @Override
                public void mousePressed(final java.awt.event.MouseEvent evt) {
                    jLabel2MousePressed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 1);
        jPanel1.add(jLabel2, gridBagConstraints);

        jScrollPane1.setBorder(null);
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane1.setViewportBorder(null);
        jScrollPane1.setMinimumSize(new java.awt.Dimension(2, 18));
        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jScrollPane1.setViewportView(command);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        jPanel1.add(jScrollPane1, gridBagConstraints);

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator1.setName("jSeparator1"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 3);
        jPanel1.add(jSeparator1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(jPanel1, gridBagConstraints);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  showHint  DOCUMENT ME!
     */
    private void setShowHint(final boolean showHint) {
        // remember orig color on first invocation
        if (origForeground == null) {
            origForeground = command.getForeground();
        }
        if (showHint) {
            command.setForeground(command.getDisabledTextColor());
            final StringBuilder hintBuilder = new StringBuilder("Suche: <");
            hintBuilder.append(currentSearch.getName());
            hintBuilder.append(">");
            command.setText(hintBuilder.toString());
        } else {
            command.setForeground(origForeground);
            command.setText("");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jLabel2MousePressed(final java.awt.event.MouseEvent evt) {
        maybeShowPopup(evt);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void maybeShowPopup(final MouseEvent evt) {
        if ((evt != null) && !SwingUtilities.isLeftMouseButton(evt)) {
            return;
        }

        final JPopupMenu pm = new JPopupMenu();
//        JRadioButtonMenuItem allCats = new JRadioButtonMenuItem("Alles finden!");
//        allCats.addActionListener(this);
//        pm.add(allCats);

        for (final CidsToolbarSearch search : searches) {
            final JRadioButtonMenuItem item = new JRadioButtonMenuItem(search.getName(),
                    search.getIcon(),
                    search
                            == currentSearch);
            item.putClientProperty(SEARCH, search);
            item.addActionListener(this);
            pm.add(item);
        }

        pm.show(getInnerComponent(), 0, getInnerComponent().getHeight() - 1);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private JTextComponent createCommandField() {
        final JTextArea res = new DynamicWidthTA();
        res.setRows(1);
        res.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        // disable default Swing's Ctrl+Shift+O binding to enable our global action
        InputMap curIm = res.getInputMap(JComponent.WHEN_FOCUSED);
        while (curIm != null) {
            curIm.remove(KeyStroke.getKeyStroke(
                    KeyEvent.VK_O,
                    KeyEvent.CTRL_DOWN_MASK
                            | KeyEvent.SHIFT_DOWN_MASK));
            curIm = curIm.getParent();
        }
        return res;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private JComponent getInnerComponent() {
        return jPanel1;
    }

    /**
     * DOCUMENT ME!
     */
    private void startProgressAnimation() {
        if ((animationTimer != null) && !animationTimer.isRunning()) {
            animationTimer.start();
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void stopProgressAnimation() {
        if ((animationTimer != null) && animationTimer.isRunning()) {
            animationTimer.stop();
            jLabel2.setIcon(findIcon);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private boolean isSearchRunning() {
        return animationTimer.isRunning();
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        final Object src = e.getSource();
        if (src instanceof JRadioButtonMenuItem) {
            final JRadioButtonMenuItem item = (JRadioButtonMenuItem)src;
            final Object searchObj = item.getClientProperty(SEARCH);
            if (searchObj instanceof CidsToolbarSearch) {
                currentSearch = (CidsToolbarSearch)searchObj;
            }
            setShowHint(!command.isFocusOwner());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void commandKeyPressed(final java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
//            displayer.selectNext();
            evt.consume();
        } else if (evt.getKeyCode() == KeyEvent.VK_UP) {
//            displayer.selectPrev();
            evt.consume();
        } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            evt.consume();
            performSelectedSearch();
        } else if ((evt.getKeyCode()) == KeyEvent.VK_ESCAPE) {
//            returnFocus(true);
//            displayer.clearModel();
        } else if ((evt.getKeyCode() == KeyEvent.VK_F10)
                    && evt.isShiftDown()) {
            maybeShowPopup(null);
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void performSelectedSearch() {
        final String searchString = command.getText();
        if ((currentSearch != null) && (searchString.length() > 0) && !isSearchRunning()) {
            startProgressAnimation();
            // TODO threading!
            currentSearch.setSearchParameter(searchString);
            CidsSearchExecutor.executeCidsSearchAndDisplayResults(currentSearch.getServerSearch(),
                new PropertyChangeListener() {

                    @Override
                    public void propertyChange(final PropertyChangeEvent evt) {
                        if (SwingWorker.StateValue.DONE == evt.getNewValue()) {
                            stopProgressAnimation();
                        }
                    }
                });
        }
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private final class DynamicWidthTA extends JTextArea {

        //~ Instance fields ----------------------------------------------------

        private Dimension prefWidth;

        //~ Methods ------------------------------------------------------------

        @Override
        public Dimension getPreferredSize() {
            if (prefWidth == null) {
                final Dimension orig = super.getPreferredSize();
                prefWidth = new Dimension(width, orig.height);
            }
            return prefWidth;
        }

        @Override
        public Dimension getMinimumSize() {
            return getPreferredSize();
        }
//        @Override
//        public Dimension getMaximumSize() {
//            return getPreferredSize();
//        }
    }
}
