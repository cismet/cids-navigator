/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * SearchTopicsPanel.java
 *
 * Created on 30.11.2011, 13:33:09
 */
package Sirius.navigator.search.dynamic;

import org.apache.log4j.Logger;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JLabel;

import de.cismet.cismap.commons.gui.metasearch.SearchTopic;

/**
 * DOCUMENT ME!
 *
 * @author   jweintraut
 * @version  $Revision$, $Date$
 */
public class SearchTopicsPanel extends javax.swing.JPanel {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(SearchTopicsPanel.class);

    //~ Instance fields --------------------------------------------------------

    private Set<SearchTopic> searchTopics;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form SearchTopicsPanel.
     */
    public SearchTopicsPanel() {
        searchTopics = new HashSet<SearchTopic>();
        initComponents();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Using this method you can specify which search topics are to be displayed by this panel.
     * setSearchTopics(Collection<SearchTopic>) creates the UI elements and layouts them.
     *
     * @param  searchTopics  The search topics to display.
     */
    public void setSearchTopics(final Collection<SearchTopic> searchTopics) {
        if ((searchTopics == null) || searchTopics.isEmpty()) {
            return;
        }

        this.searchTopics.clear();
        removeAll();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Setting search topics: " + searchTopics);
        }

        int maxRowWidth = 0;
        int rowHeight = 0;

        final Insets insetsIcon = new Insets(0, 5, 0, 2);
        final Insets insetsCheckbox = new Insets(0, 2, 0, 5);
        final SearchTopic[] searchTopicsArray = searchTopics.toArray(new SearchTopic[0]);
        for (int i = 0; i < searchTopicsArray.length; i++) {
            final SearchTopic searchTopic = searchTopicsArray[i];

            if (!this.searchTopics.add(searchTopic)) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Search topic '" + searchTopic.getName() + " - " + searchTopic.getDescription()
                                + "' couldn't be added. Maybe it's defined twice.");
                }
                continue;
            }

            final JLabel lblIcon = new JLabel(searchTopic.getIcon());
            final JCheckBox chkSearchTopic = new JCheckBox(searchTopic.getName());

            lblIcon.setToolTipText(searchTopic.getDescription());
            chkSearchTopic.setBackground(getBackground());
            chkSearchTopic.setToolTipText(searchTopic.getDescription());
            chkSearchTopic.setSelected(searchTopic.isSelected());
            chkSearchTopic.addItemListener(new ItemListener() {

                    @Override
                    public void itemStateChanged(final ItemEvent e) {
                        searchTopic.setSelected(!searchTopic.isSelected());
                    }
                });

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.anchor = GridBagConstraints.LINE_START;
            constraints.gridx = 0;
            constraints.gridy = this.searchTopics.size() - 1;

            if (i == 0) {
                constraints.insets = new Insets(2, 5, 0, 2);
            } else if (i == (searchTopicsArray.length - 1)) {
                constraints.insets = new Insets(0, 5, 2, 2);
            } else {
                constraints.insets = insetsIcon;
            }

            add(lblIcon, constraints);

            constraints = new GridBagConstraints();
            constraints.anchor = GridBagConstraints.LINE_START;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.gridx = 1;
            constraints.gridy = this.searchTopics.size() - 1;

            if (i == 0) {
                constraints.insets = new Insets(2, 2, 0, 5);
            } else if (i == (searchTopicsArray.length - 1)) {
                constraints.insets = new Insets(0, 2, 2, 5);
            } else {
                constraints.insets = insetsCheckbox;
            }

            constraints.weightx = 1.0;
            add(chkSearchTopic, constraints);

            final int rowWidth = lblIcon.getWidth() + chkSearchTopic.getWidth() + 10;
            if (rowWidth > maxRowWidth) {
                maxRowWidth = rowWidth;
            }

            rowHeight = chkSearchTopic.getHeight();

            if (LOG.isDebugEnabled()) {
                LOG.debug("Added '" + searchTopic.getName() + " - " + searchTopic.getDescription() + "' on position "
                            + (this.searchTopics.size() - 1));
            }
        }

        setMinimumSize(new Dimension(maxRowWidth, 20 * rowHeight));

        final Component gluFiller = Box.createGlue();
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;
        constraints.gridy = this.searchTopics.size();
        constraints.weighty = 1.0;
        add(gluFiller, constraints);

        revalidate();
    }

    /**
     * Adds the given ItemListener to every checkbox representing a search topic.
     *
     * @param  itemListener  The ItemListener to add.
     */
    public void registerItemListener(final ItemListener itemListener) {
        for (final Component component : getComponents()) {
            if (component instanceof JCheckBox) {
                final JCheckBox checkbox = (JCheckBox)component;
                checkbox.addItemListener(itemListener);
            }
        }
    }

    /**
     * Removes the given ItemListener from every checkbox representing a search topic.
     *
     * @param  itemListener  The ItemListener to remove.
     */
    public void unregisterItemListener(final ItemListener itemListener) {
        for (final Component component : getComponents()) {
            if (component instanceof JCheckBox) {
                final JCheckBox checkbox = (JCheckBox)component;
                checkbox.removeItemListener(itemListener);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        setBackground(javax.swing.UIManager.getDefaults().getColor("List.background"));
        setLayout(new java.awt.GridBagLayout());
    } // </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
