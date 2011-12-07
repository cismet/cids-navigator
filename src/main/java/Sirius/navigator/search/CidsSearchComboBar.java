/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.search;

import Sirius.server.search.CidsServerSearch;
import Sirius.server.search.builtin.FullTextSearch;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.matchers.TextMatcherEditor;
import ca.odell.glazedlists.swing.AutoCompleteSupport;

import org.apache.log4j.Logger;

import org.jdom.Element;

import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.Utilities;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.text.BadLocationException;

import de.cismet.cids.tools.search.clientstuff.CidsToolbarSearch;
import de.cismet.cids.tools.search.clientstuff.Modifier;

import de.cismet.cismap.commons.interaction.CismapBroker;

import de.cismet.tools.configuration.Configurable;
import de.cismet.tools.configuration.NoWriteError;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class CidsSearchComboBar extends JPanel implements ActionListener, Configurable {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(CidsSearchComboBar.class);

    private static final String CONF_ROOT = "metaSearch";
    private static final String CONF_HISTORY = "history";
    private static final String CONF_HISTORY_ATTR_MAXSIZE = "size";
    private static final String CONF_HISTORY_ITEM = "historyItem";

    private static final String POPUPMENUITEM_SEARCH = "search";
    private static final String SEPARATOR_MODIFIER = "#";

    private static final ImageIcon ICON_BTNSEARCH = new ImageIcon(CidsSearchComboBar.class.getResource(
                "/Sirius/navigator/search/btnSearch.png"));
    private static final ImageIcon ICON_BTNSEARCH_CANCEL = new ImageIcon(CidsSearchComboBar.class.getResource(
                "/Sirius/navigator/search/btnSearch_cancel.png"));
    private static final ImageIcon ICON_LBLSEARCHTOPIC = new ImageIcon(CidsSearchComboBar.class.getResource(
                "/Sirius/navigator/search/lblSearchTopic.png"));
    private static final ImageIcon ICON_LBLSEARCHTOPIC_OVERLAY = new ImageIcon(CidsSearchComboBar.class.getResource(
                "/Sirius/navigator/search/lblSearchTopic_overlay.png"));
    private static int maxHistorySize = 10;

    //~ Instance fields --------------------------------------------------------

    private int width;
    private CidsToolbarSearch selectedSearch;
    private final Collection<CidsToolbarSearch> searches;
    private final EventList<String> history;
    private Map<CidsToolbarSearch, ImageIcon> icons;
    private Collection<String> searchHints;
    private Collection<? extends Modifier> modifiers;
    private SwingWorker searchThread;
    private AutoCompleteSupport supportInput;
    private final Timer animationTimer = new Timer(100, new AnimationTimerListener());
    private Color origForeground;
    private JLabel lblSearchTopic;
    private JPanel pnlSearch;
    private JComboBox cbbInput;
    private JTextField cbbInputEditorComponent;
    private JButton btnSearch;

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
        this.history = new BasicEventList<String>();
        this.icons = new HashMap<CidsToolbarSearch, ImageIcon>();
        this.searchHints = new LinkedList<String>();
        this.modifiers = Lookup.getDefault().lookupAll(Modifier.class);

        initComponents();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private static Color getTextBackground() {
        Color textB = UIManager.getColor("TextPane.background");

        if ("Aqua".equals(UIManager.getLookAndFeel().getID())) {
            textB = UIManager.getColor("NbExplorerView.background"); // NOI18N
        }

        return (textB != null) ? textB : Color.WHITE;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private static Color getComboBorderColor() {
        final Color shadow = UIManager.getColor(
                Utilities.isWindows() ? "Nb.ScrollPane.Border.color" : "TextField.shadow");
        return (shadow != null) ? shadow : getPopupBorderColor();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private static Color getPopupBorderColor() {
        final Color shadow = UIManager.getColor("controlShadow");
        return (shadow != null) ? shadow : Color.GRAY;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  searches  DOCUMENT ME!
     */
    public void setSearches(final Collection<? extends CidsToolbarSearch> searches) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Setting new searches: " + searches);
        }

        this.searches.clear();
        this.searches.addAll(searches);

        for (final CidsToolbarSearch search : searches) {
            addOverlayIcon(search);
            searchHints.add(search.getHint());
        }

        if (!this.searches.isEmpty()) {
            selectedSearch = this.searches.iterator().next();
            lblSearchTopic.setIcon(icons.get(selectedSearch));
            showSearchHint(true);

            if (LOG.isDebugEnabled()) {
                LOG.debug("Search '" + selectedSearch.getName() + "' selected.");
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  search  DOCUMENT ME!
     */
    public void addSearch(final CidsToolbarSearch search) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Adding new search: " + search);
        }

        searches.add(search);

        addOverlayIcon(search);
        searchHints.add(search.getHint());

        if (selectedSearch == null) {
            selectedSearch = search;
            lblSearchTopic.setIcon(icons.get(selectedSearch));
            showSearchHint(true);

            if (LOG.isDebugEnabled()) {
                LOG.debug("Search '" + selectedSearch.getName() + "' selected.");
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   search  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean removeSearch(final CidsToolbarSearch search) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Removing search: " + search);
        }

        final boolean result = searches.remove(search);

        if (selectedSearch.equals(search)) {
            selectedSearch = null;
        }

        if (!searches.isEmpty()) {
            selectedSearch = searches.iterator().next();

            if (LOG.isDebugEnabled()) {
                LOG.debug("Search '" + selectedSearch.getName() + "' selected.");
            }
        }

        removeOverlayIcon(search);
        searchHints.remove(search.getHint());
        showSearchHint(selectedSearch != null);

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  history  DOCUMENT ME!
     */
    public void setHistoryItems(final Collection<? extends String> history) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Setting new history: " + history);
        }

        this.history.clear();

        final Iterator<? extends String> historyIter = history.iterator();
        int i = 0;
        while (historyIter.hasNext() && (i < maxHistorySize)) {
            this.history.add(historyIter.next());
            i++;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  historyItem  DOCUMENT ME!
     */
    public void addHistoryItem(final String historyItem) {
        if (!history.contains(historyItem)) {
            if (history.size() == maxHistorySize) {
                history.remove(0);
            }
            history.add(historyItem);

            if (LOG.isDebugEnabled()) {
                LOG.debug("History item '" + historyItem + "' added.");
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   historyItem  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean removeHistoryItem(final String historyItem) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Remove history item '" + historyItem + "'.");
        }

        return history.remove(historyItem);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  search  DOCUMENT ME!
     */
    private void addOverlayIcon(final CidsToolbarSearch search) {
        icons.put(search, new OverlayIcon(generateIcon(search)));
    }

    /**
     * DOCUMENT ME!
     *
     * @param  search  DOCUMENT ME!
     */
    private void removeOverlayIcon(final CidsToolbarSearch search) {
        icons.remove(search);
    }

    /**
     * DOCUMENT ME!
     */
    private void initComponents() {
        final ActionListener performSearchListener = new PerformSearchListener();

        cbbInput = createInputComponent(performSearchListener);
        cbbInput.setName("cbbInput");

        java.awt.GridBagConstraints gridBagConstraints;

        pnlSearch = new JPanel();
        lblSearchTopic = new JLabel();
        btnSearch = new JButton();

        setLayout(new GridBagLayout());

        pnlSearch.setBackground(getTextBackground());
        pnlSearch.setBorder(BorderFactory.createLineBorder(getComboBorderColor()));
        pnlSearch.setName("pnlSearch"); // NOI18N
        pnlSearch.setLayout(new GridBagLayout());

        lblSearchTopic.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        lblSearchTopic.setName("lblSearchTopic"); // NOI18N
        lblSearchTopic.addMouseListener(new java.awt.event.MouseAdapter() {

                @Override
                public void mousePressed(final java.awt.event.MouseEvent evt) {
                    lblSearchTopicMousePressed(evt);
                }
            });

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(1, 1, 1, 7);
        pnlSearch.add(lblSearchTopic, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(0, 0, 0, 2);
        pnlSearch.add(cbbInput, gridBagConstraints);

        btnSearch.setIcon(ICON_BTNSEARCH);
        btnSearch.setBackground(getTextBackground());
        btnSearch.setOpaque(false);
        btnSearch.setContentAreaFilled(false);
        btnSearch.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnSearch.setFocusPainted(false);
        btnSearch.setName("btnSearch"); // NOI18N
        btnSearch.addActionListener(performSearchListener);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(1, 7, 1, 1);
        pnlSearch.add(btnSearch, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(pnlSearch, gridBagConstraints);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  showSearchHint  DOCUMENT ME!
     */
    private void showSearchHint(final boolean showSearchHint) {
        final Runnable setShowHint = new Runnable() {

                @Override
                public void run() {
                    final String currentInput = cbbInputEditorComponent.getText();
                    final boolean isCurrentInputAHint = (currentInput != null)
                                && ((currentInput.trim().length() == 0) || searchHints.contains(currentInput));

                    if (showSearchHint && (selectedSearch != null) && isCurrentInputAHint) {
                        cbbInputEditorComponent.setForeground(cbbInputEditorComponent.getDisabledTextColor());
                        cbbInputEditorComponent.setFont(cbbInputEditorComponent.getFont().deriveFont(Font.ITALIC));
                        cbbInputEditorComponent.setText(selectedSearch.getHint());
                        cbbInputEditorComponent.setCaretPosition(selectedSearch.getHint().length());
                        cbbInputEditorComponent.transferFocus();
                    } else if ((!showSearchHint || (selectedSearch == null)) && isCurrentInputAHint) {
                        cbbInputEditorComponent.setForeground(origForeground);
                        cbbInputEditorComponent.setFont(cbbInputEditorComponent.getFont().deriveFont(Font.PLAIN));
                        cbbInputEditorComponent.setCaretPosition(0);
                        cbbInputEditorComponent.setText("");
                    }
                }
            };

        if (EventQueue.isDispatchThread()) {
            setShowHint.run();
        } else {
            EventQueue.invokeLater(setShowHint);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void lblSearchTopicMousePressed(final MouseEvent evt) {
        showSearchesPopup(evt);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void showSearchesPopup(final MouseEvent evt) {
        if (((evt != null) && !SwingUtilities.isLeftMouseButton(evt)) || isSearchRunning()) {
            return;
        }

        final JPopupMenu popupMenu = new JPopupMenu();

        for (final CidsToolbarSearch search : searches) {
            final JRadioButtonMenuItem item = new JRadioButtonMenuItem(
                    search.getName(),
                    generateNullIcon(search),
                    search
                            == selectedSearch);
            item.putClientProperty(POPUPMENUITEM_SEARCH, search);
            item.addActionListener(this);
            popupMenu.add(item);
        }

        popupMenu.show(pnlSearch, 0, pnlSearch.getHeight() - 1);
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        final Object source = event.getSource();

        if (source instanceof JRadioButtonMenuItem) {
            final JRadioButtonMenuItem item = (JRadioButtonMenuItem)source;
            final Object search = item.getClientProperty(POPUPMENUITEM_SEARCH);

            if (search instanceof CidsToolbarSearch) {
                selectedSearch = (CidsToolbarSearch)search;
            }

            EventQueue.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        lblSearchTopic.setIcon(icons.get(selectedSearch));
                        showSearchHint(true);
                    }
                });
        }
    }

    @Override
    public void configure(final Element parent) {
        if (parent == null) {
            LOG.info("There is no local configuration for CidsSearchComboBar.");
            return;
        }

        final Element metaSearch = parent.getChild(CONF_ROOT);
        if (metaSearch == null) {
            LOG.info("There is no local configuration for CidsSearchComboBar.");
            return;
        }

        final Element history = metaSearch.getChild(CONF_HISTORY);
        if (history == null) {
            LOG.info("There is no history given in the local configuration of CidsSearchComboBar.");
            return;
        }

        final List historyItems = history.getChildren(CONF_HISTORY_ITEM);
        if ((historyItems == null) || historyItems.isEmpty()) {
            LOG.info("There is no history given in the local configuration of CidsSearchComboBar.");
            return;
        }

        final Collection<String> result = new LinkedList<String>();
        for (final Object historyItem : historyItems) {
            if (historyItem instanceof Element) {
                result.add(((Element)historyItem).getText());
            }
        }

        setHistoryItems(result);
    }

    @Override
    public void masterConfigure(final Element parent) {
        if (parent == null) {
            LOG.info(
                "There is no server configuration for CidsSearchComboBar. The maximum size of the history is set to '"
                        + CidsSearchComboBar.maxHistorySize
                        + "'.");
            return;
        }

        final Element metaSearch = parent.getChild(CONF_ROOT);
        if (metaSearch == null) {
            LOG.info(
                "There is no server configuration for CidsSearchComboBar. The maximum size of the history is set to '"
                        + CidsSearchComboBar.maxHistorySize
                        + "'.");
            return;
        }

        final Element history = metaSearch.getChild(CONF_HISTORY);
        if (history == null) {
            LOG.info(
                "There is no history configuration given in the server configuration of CidsSearchComboBar. The maximum size of the history is set to '"
                        + CidsSearchComboBar.maxHistorySize
                        + "'.");
            return;
        }

        final String maxHistorySize = history.getAttributeValue(CONF_HISTORY_ATTR_MAXSIZE);
        try {
            CidsSearchComboBar.maxHistorySize = Integer.parseInt(maxHistorySize);
        } catch (NumberFormatException e) {
            LOG.error("Could not parse given maximum size of history ('" + maxHistorySize
                        + "'). This value has to be an integer. The maximum size of the history is set to '"
                        + CidsSearchComboBar.maxHistorySize + "'.");
        }
    }

    @Override
    public Element getConfiguration() throws NoWriteError {
        final Element result = new Element(CONF_ROOT);
        final Element history = new Element(CONF_HISTORY);

        for (final String historyItem : this.history) {
            final Element historyItemElement = new Element(CONF_HISTORY_ITEM);

            historyItemElement.addContent(historyItem);

            history.addContent(historyItemElement);
        }

        result.addContent(history);
        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   search  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private ImageIcon generateIcon(final CidsToolbarSearch search) {
        ImageIcon result = search.getIcon();

        if ((result == null) || (result.getImage() == null)) {
            result = ICON_LBLSEARCHTOPIC;
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   search  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private ImageIcon generateNullIcon(final CidsToolbarSearch search) {
        ImageIcon result = search.getIcon();

        if ((result == null) || (result.getImage() == null)) {
            result = null;
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   actionListener  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private JComboBox createInputComponent(final ActionListener actionListener) {
        final JComboBox result = new DynamicWidthCB();

        result.setEditable(true);
        result.setBorder(BorderFactory.createEmptyBorder());
        result.addActionListener(actionListener);

        EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    supportInput = AutoCompleteSupport.install(
                            result,
                            history);
                    supportInput.setFilterMode(TextMatcherEditor.CONTAINS);

                    cbbInputEditorComponent = (JTextField)result.getEditor().getEditorComponent();
                    cbbInputEditorComponent.addFocusListener(new SearchHintListener());
                    cbbInputEditorComponent.getDocument().addDocumentListener(new SyntaxHintListener());

                    // For an explanation why we catch this low-level event additionally to the higher-level event
                    // please see the notice in the JavaDoc of PerformSearchListener.
                    cbbInputEditorComponent.addActionListener(actionListener);

                    origForeground = cbbInputEditorComponent.getForeground();
                }
            });

        return result;
    }

    /**
     * DOCUMENT ME!
     */
    private void displaySearchMode() {
        final Runnable displaySearchMode = new Runnable() {

                @Override
                public void run() {
                    cbbInput.setEnabled(false);

                    cbbInputEditorComponent.setEnabled(false);
                    lblSearchTopic.setEnabled(false);
                    btnSearch.setIcon(ICON_BTNSEARCH_CANCEL);

                    if ((animationTimer != null) && !animationTimer.isRunning()) {
                        animationTimer.start();
                    }
                }
            };

        if (EventQueue.isDispatchThread()) {
            displaySearchMode.run();
        } else {
            EventQueue.invokeLater(displaySearchMode);
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void displayNormalMode() {
        final Runnable displaySearchMode = new Runnable() {

                @Override
                public void run() {
                    cbbInput.setEnabled(true);
                    cbbInputEditorComponent.setEnabled(true);

                    lblSearchTopic.setEnabled(true);
                    btnSearch.setIcon(ICON_BTNSEARCH);

                    if ((animationTimer != null) && animationTimer.isRunning()) {
                        animationTimer.stop();
                        lblSearchTopic.setIcon(icons.get(selectedSearch));
                    }
                }
            };

        if (EventQueue.isDispatchThread()) {
            displaySearchMode.run();
        } else {
            EventQueue.invokeLater(displaySearchMode);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private boolean isSearchRunning() {
        return (searchThread != null) && !searchThread.isDone() && !searchThread.isCancelled();
    }

    /**
     * DOCUMENT ME!
     */
    private void parseInputAndPerformSearch() {
        final String input = cbbInputEditorComponent.getText();

        if (LOG.isDebugEnabled()) {
            LOG.debug("Performing search with input '" + input + "'.");
        }
        if ((input == null) || (input.trim().length() == 0)) {
            return;
        }

        final Collection<Modifier> appliedModifiers = new LinkedList<Modifier>();
        final StringBuilder searchParameter = new StringBuilder();

        for (final String wordFromInput : input.split("\\s")) {
            if (wordFromInput.startsWith(SEPARATOR_MODIFIER)) {
                for (final Modifier potentialModifier : modifiers) {
                    if (SEPARATOR_MODIFIER.concat(potentialModifier.getCommand()).equalsIgnoreCase(wordFromInput)) {
                        appliedModifiers.add(potentialModifier);

                        if (potentialModifier instanceof HereModifier) {
                            ((HereModifier)potentialModifier).setValue(CismapBroker.getInstance().getMappingComponent()
                                        .getCurrentBoundingBox().getGeometryFromTextLineString());
                        }

                        break;
                    }
                }
            } else if (wordFromInput.trim().length() > 0) {
                searchParameter.append(wordFromInput);
                searchParameter.append(" ");
            }
        }

        if ((searchParameter.toString().trim().length() == 0)) {
            return;
        }

        if (searchParameter.charAt(searchParameter.length() - 1) == ' ') {
            searchParameter.setLength(searchParameter.length() - 1);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Parsed '" + searchParameter.toString() + "' with modifiers '" + appliedModifiers
                        + "' from input '" + input + "'.");
        }

        addHistoryItem(searchParameter.toString());

        if ((selectedSearch != null) && (searchParameter.length() > 0) && !isSearchRunning()) {
            displaySearchMode();
            selectedSearch.setSearchParameter(searchParameter.toString());
            selectedSearch.applyModifiers(appliedModifiers);

            searchThread = CidsSearchExecutor.searchAndDisplayResults(selectedSearch.getServerSearch(),
                    // TODO: new instance on every call?
                    new SearchDoneListener());
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void cancelRunningSearch() {
        if (isSearchRunning()) {
            searchThread.cancel(true);
            displayNormalMode();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   args  DOCUMENT ME!
     *
     * @throws  UnsupportedOperationException  DOCUMENT ME!
     */
    public static void main(final String[] args) {
        final String[] LAFS = new String[] {
                "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel",
                "com.sun.java.swing.plaf.motif.MotifLookAndFeel",
                "com.sun.java.swing.plaf.windows.WindowsLookAndFeel",
                "com.jgoodies.looks.windows.WindowsLookAndFeel",
                "com.jgoodies.looks.plastic.PlasticLookAndFeel",
                "com.jgoodies.looks.plastic.Plastic3DLookAndFeel",
                "com.jgoodies.looks.plastic.PlasticXPLookAndFeel"
            };
        try {
            javax.swing.UIManager.setLookAndFeel(LAFS[4]);
        } catch (ClassNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        } catch (InstantiationException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IllegalAccessException ex) {
            Exceptions.printStackTrace(ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Exceptions.printStackTrace(ex);
        }

        final CidsSearchComboBar searchBar = new CidsSearchComboBar();

        searchBar.addSearch(new CidsToolbarSearch() {

                private String parameter;

                @Override
                public void setSearchParameter(final String toolbarSearchString) {
                    parameter = toolbarSearchString;
                }

                @Override
                public CidsServerSearch getServerSearch() {
                    return new FullTextSearch(parameter, false);
                }

                @Override
                public String getName() {
                    return "Volltext mit null-Icon";
                }

                @Override
                public ImageIcon getIcon() {
                    return null;
                }

                @Override
                public String getHint() {
                    return "Hinweis fürs null-Icon";
                }

                @Override
                public void applyModifiers(final Collection<? extends Modifier> modifiers) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
            });

        searchBar.addSearch(new CidsToolbarSearch() {

                private String parameter;

                @Override
                public void setSearchParameter(final String toolbarSearchString) {
                    parameter = toolbarSearchString;
                }

                @Override
                public CidsServerSearch getServerSearch() {
                    return new FullTextSearch(parameter, false);
                }

                @Override
                public String getName() {
                    return "Volltext mit leerem Icon";
                }

                @Override
                public ImageIcon getIcon() {
                    return new ImageIcon();
                }

                @Override
                public String getHint() {
                    return "Hinweis fürs leere Icon";
                }

                @Override
                public void applyModifiers(final Collection<? extends Modifier> modifiers) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
            });

        searchBar.addSearch(new CidsToolbarSearch() {

                private String parameter;

                @Override
                public void setSearchParameter(final String toolbarSearchString) {
                    parameter = toolbarSearchString;
                }

                @Override
                public CidsServerSearch getServerSearch() {
                    return new FullTextSearch(parameter, false);
                }

                @Override
                public String getName() {
                    return "Volltextsuche mit Icon";
                }

                @Override
                public ImageIcon getIcon() {
                    return ImageUtilities.loadImageIcon(
                            "/Sirius/navigator/search/progress_1.png",
                            false);
                }

                @Override
                public String getHint() {
                    return "Nachname Vorname";
                }

                @Override
                public void applyModifiers(final Collection<? extends Modifier> modifiers) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
            });

        final Collection<String> history = new LinkedList<String>();
        history.add("Aschenputtel");
        history.add("Buttercremetorte");
        history.add("Chronograph");
        history.add("Durst");
        history.add("Fitness");

        searchBar.setHistoryItems(history);

        final javax.swing.JFrame frame = new javax.swing.JFrame();
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new java.awt.BorderLayout());
        frame.add(searchBar);
        frame.add(new javax.swing.JButton("Something"), BorderLayout.PAGE_START);
        frame.add(new javax.swing.JButton("to"), BorderLayout.LINE_START);
        frame.add(new javax.swing.JButton("focus"), BorderLayout.LINE_END);
        frame.add(new javax.swing.JButton("on."), BorderLayout.PAGE_END);

        frame.pack();
        frame.setVisible(true);
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class DynamicWidthCB extends JComboBox {

        //~ Instance fields ----------------------------------------------------

        private Dimension preferredWidth;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new DynamicWidthCB object.
         */
        public DynamicWidthCB() {
            super();
            setUI(new BasicComboBoxUI() {

                    @Override
                    protected JButton createArrowButton() {
                        return new JButton() {

                                @Override
                                public int getWidth() {
                                    return 0;
                                }
                            };
                    }
                });
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public Dimension getPreferredSize() {
            if (preferredWidth == null) {
                final Dimension originalSize = super.getPreferredSize();
                preferredWidth = new Dimension(width, originalSize.height);
            }

            return preferredWidth;
        }

        @Override
        public Dimension getMinimumSize() {
            return getPreferredSize();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class AnimationTimerListener implements ActionListener {

        //~ Instance fields ----------------------------------------------------

        private ImageIcon[] icons;
        private int index = 0;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new AnimationTimerListener object.
         */
        public AnimationTimerListener() {
            icons = new ImageIcon[8];
            for (int i = 0; i < 8; i++) {
                icons[i] = ImageUtilities.loadImageIcon(
                        "Sirius/navigator/search/progress_"
                                + i
                                + ".png",
                        false);
            }
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public void actionPerformed(final ActionEvent e) {
            lblSearchTopic.setIcon(icons[index]);
            index = (index + 1) % 8;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public class OverlayIcon extends ImageIcon {

        //~ Instance fields ----------------------------------------------------

        private ImageIcon base;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new OverlayIcon object.
         *
         * @param  base  DOCUMENT ME!
         */
        public OverlayIcon(final ImageIcon base) {
            super();
            this.base = base;

            if (this.base.getImage() != null) {
                setImage(this.base.getImage());
            }
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public synchronized void paintIcon(final Component c, final Graphics g, final int x, final int y) {
            base.paintIcon(c, g, x, y);
            ICON_LBLSEARCHTOPIC_OVERLAY.paintIcon(
                c,
                g,
                x
                        + (base.getIconWidth() - ICON_LBLSEARCHTOPIC_OVERLAY.getIconWidth()),
                y
                        + (base.getIconHeight() - ICON_LBLSEARCHTOPIC_OVERLAY.getIconHeight()));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class SyntaxHintListener implements DocumentListener, ActionListener {

        //~ Static fields/initializers -----------------------------------------

        private static final String POPUPMENUITEM_SYNTAXHINT = "syntaxHint";

        //~ Instance fields ----------------------------------------------------

        private int offset;

        //~ Methods ------------------------------------------------------------

        @Override
        public void insertUpdate(final DocumentEvent e) {
            offset = e.getOffset();
            try {
                if ((e.getLength() == 1) && e.getDocument().getText(offset, 1).equals(SEPARATOR_MODIFIER)) {
                    showSyntaxHintsPopup();
                }
            } catch (BadLocationException ex) {
                LOG.error("Could not determine which text was inserted.", ex);
            }
        }

        @Override
        public void removeUpdate(final DocumentEvent e) {
        }

        @Override
        public void changedUpdate(final DocumentEvent e) {
        }

        /**
         * DOCUMENT ME!
         */
        private void showSyntaxHintsPopup() {
            final JPopupMenu popupMenu = new JPopupMenu();

            for (final Modifier modifier : modifiers) {
                final JMenuItem item = new JMenuItem(modifier.getCommand());
                item.setToolTipText(modifier.getHint());
                item.putClientProperty(POPUPMENUITEM_SYNTAXHINT, modifier);
                item.addActionListener(this);
                popupMenu.add(item);
            }

            final Runnable showPopup = new Runnable() {

                    @Override
                    public void run() {
                        popupMenu.show(cbbInput, 0, cbbInput.getHeight() - 1);
                    }
                };

            EventQueue.invokeLater(showPopup);
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            if (!(e.getSource() instanceof JMenuItem)) {
                return;
            }

            final JMenuItem item = (JMenuItem)e.getSource();
            if (!(item.getClientProperty(POPUPMENUITEM_SYNTAXHINT) instanceof Modifier)) {
                return;
            }

            final Modifier modifier = (Modifier)item.getClientProperty(POPUPMENUITEM_SYNTAXHINT);
            try {
                cbbInputEditorComponent.getDocument().insertString(offset + 1, modifier.getCommand(), null);
            } catch (BadLocationException ex) {
                LOG.error("Could not insert the selected modifier.", ex);
            }

            EventQueue.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        cbbInputEditorComponent.requestFocus();
                        cbbInputEditorComponent.setCaretPosition(offset + modifier.getCommand().length());
                    }
                });
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class SearchHintListener implements FocusListener {

        //~ Methods ------------------------------------------------------------

        @Override
        public void focusGained(final FocusEvent e) {
            showSearchHint(false);
        }

        @Override
        public void focusLost(final FocusEvent e) {
            showSearchHint(true);
        }
    }

    /**
     * This listener listens to all events that could start a search. At the moment it listens to ActionEvents fired by
     * the search button or by the JTextField that is contained in the input UI element.
     *
     * <p>Notice: Listening only for ActionEvents fired by the input UI element (a JComboBox) would not work, since
     * JComboBox fires ActionEvents if it loses focus and contains a value that is not in the ComboBoxModel (aka the
     * history). Listening only to ActionEvents fired by the JTextField would not work, too. Under certain circumstances
     * (especially if there is a modifier in the input) this blocks the JTextField in a way that the user can't enter
     * something until he opened and closed the history. It's not clear why exactly this happens, but it maybe caused by
     * the fact that one should not use the low-level events of a JComboBox or it is caused by some glitch in
     * GlazedLists. The workaround for those limitations is to listen for both ActionEvents. The low-level event
     * (JTextField) will be fired before the higher-level event (JComboBox). So we save the time of the last low-level
     * event and only start a search if the higher-level event is fired within a certain time frame. We think 500ms
     * should be enough for both events to be fired and not mix up with an ActionEvent fired by loosing the focus.</p>
     *
     * @version  $Revision$, $Date$
     */
    private class PerformSearchListener implements ActionListener {

        //~ Instance fields ----------------------------------------------------

        private long lastTextfieldEvent = 0L;

        //~ Methods ------------------------------------------------------------

        @Override
        public void actionPerformed(final ActionEvent e) {
            final Object source = e.getSource();
            if ((source instanceof JComboBox) && source.equals(cbbInput)) {
                if ("comboBoxEdited".equals(e.getActionCommand()) && !isSearchRunning()
                            && ((e.getWhen() - lastTextfieldEvent) < 500)) {
                    parseInputAndPerformSearch();
                }
            }
            if ((source instanceof JTextField) && source.equals(cbbInputEditorComponent)) {
                lastTextfieldEvent = e.getWhen();
            } else if ((source instanceof JButton) && source.equals(btnSearch)) {
                if (isSearchRunning()) {
                    cancelRunningSearch();
                } else {
                    parseInputAndPerformSearch();
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class SearchDoneListener implements PropertyChangeListener {

        //~ Methods ------------------------------------------------------------

        @Override
        public void propertyChange(final PropertyChangeEvent evt) {
            if (SwingWorker.StateValue.DONE == evt.getNewValue()) {
                displayNormalMode();
            }
        }
    }
}
