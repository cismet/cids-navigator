package Sirius.navigator.search;

import Sirius.navigator.ui.ComponentRegistry;
import Sirius.navigator.ui.GUIContainer;
import Sirius.navigator.ui.MutableConstraints;
import Sirius.navigator.ui.MutableMenuBar;
import Sirius.navigator.ui.MutableToolBar;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import org.openide.util.Lookup;

/**
 * This class is responsibility is to lookup all relevant search components,
 * to initialize and to plug them into the navigator gui.
 * 
 * @author stefan
 */
public class CidsSearchInitializer {

    public CidsSearchInitializer() {
        initializeSearch();
    }

    private void initializeSearch() {
        Collection<? extends CidsToolbarSearch> toolbarSearches = Lookup.getDefault().lookupAll(CidsToolbarSearch.class);
        Collection<? extends CidsDialogSearch> dialogeSearches = Lookup.getDefault().lookupAll(CidsDialogSearch.class);
        Collection<? extends CidsWindowSearch> windowSearches = Lookup.getDefault().lookupAll(CidsWindowSearch.class);
        //
        MutableMenuBar menuBar = ComponentRegistry.getRegistry().getMutableMenuBar();
        MutableToolBar toolBar = ComponentRegistry.getRegistry().getMutableToolBar();
        GUIContainer guiContainer = ComponentRegistry.getRegistry().getGUIContainer();
        //Toolbar Searches
        if (!toolbarSearches.isEmpty()) {
            CidsSearchComboBar searchBar = new CidsSearchComboBar();
            searchBar.setSearches(toolbarSearches);
            JPanel innerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
            innerPanel.add(searchBar);
            toolBar.add(innerPanel, -1);
        }
        //Dialog Searches
        if (!dialogeSearches.isEmpty()) {
            JMenu menu = menuBar.getSearchMenu();
            menu.add(new JSeparator());
            for (CidsDialogSearch dialogSearch : dialogeSearches) {
                JMenuItem item = new JMenuItem(dialogSearch.getName());
                menu.add(item);
                item.setIcon(dialogSearch.getIcon());
                item.addActionListener(new DialogActionListener(dialogSearch));
            }
        }
        //Window Searches
        if (!windowSearches.isEmpty()) {
            for (CidsWindowSearch windowSearch : windowSearches) {
                MutableConstraints mutableConstraints = new MutableConstraints();
                String name = windowSearch.getName();
                mutableConstraints.addAsComponent(windowSearch.getClass().getName(), windowSearch.getSearchWindowComponent(), name, name, windowSearch.getIcon(), MutableConstraints.P1, MutableConstraints.ANY_INDEX);
                guiContainer.add(mutableConstraints);
            }
        }
    }

    static final class DialogActionListener implements ActionListener {

        public DialogActionListener(CidsDialogSearch search) {
            this.dialoge = search.getDialogComponent();
        }
        private final JDialog dialoge;

        @Override
        public void actionPerformed(ActionEvent e) {
            java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
            dialoge.setLocation((screenSize.width - dialoge.getWidth()) / 2, (screenSize.height - dialoge.getHeight()) / 2);
            dialoge.setVisible(true);
        }
    }
}
