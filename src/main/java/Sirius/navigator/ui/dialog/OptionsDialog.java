/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui.dialog;

/*******************************************************************************
 *
 * Copyright (c)        :       EIG (Environmental Informatics Group)
 * http://www.htw-saarland.de/eig
 * Prof. Dr. Reiner Guettler
 * Prof. Dr. Ralf Denzer
 *
 * HTWdS
 * Hochschule fuer Technik und Wirtschaft des Saarlandes
 * Goebenstr. 40
 * 66117 Saarbruecken
 * Germany
 *
 * Programmers          :       Pascal
 *
 * Project                      :       WuNDA 2
 * Filename             :
 * Version                      :       1.0
 * Purpose                      :
 * Created                      :       26.09.2000
 * History                      : 30.10.2001 changes by M. Derschang (vgl. MANU_NAV)
 *
 *******************************************************************************/
//import ISClient.ims.client.ISFloatingFrameModel;
//import Sirius.navigator.deprecated.NavigatorModel;

import Sirius.navigator.*;
//import Sirius.navigator.ui.widget.SingleLineListBox;
import Sirius.navigator.resource.*;

import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

// !!! MANU_NAV: !!!
//[ISDS PD 12062002]import Sirius.Model.ISDSClient.*;

//import Sirius.navigator.PlugIn.PlugInModel;

/**
 * Ein Options Dialog, ueber den alle Einstellungen des Grids, der ControlPoints, der Splines, etc. bequem eingestellt
 * werden koennen. Der OptionsDialog besitzt eine Vorschaufunktion und arbeitet mit eine Kopie des GridModels und des
 * ControlModels.
 *
 * @author   Pascal Dih&eacute;
 * @version  1.2
 * @see      JDialog
 * @see      GridModel
 * @see      ControlModel
 */
public class OptionsDialog extends JDialog implements ActionListener {

    //~ Instance fields --------------------------------------------------------

    private final Logger logger;

    // private NavigatorModel navigatorModel;
    // private ISFloatingFrameModel isFloatingFrameModel;

    private JPanel navigatorPanel; // , isFloatingFramePanel;

    // !!! MANU_NAV Start !!!
    // [ISDS PD 12062002]private ISDSFloatingFrameModel isdsFloatingFrameModel;
    // private JPanel isdsFloatingFramePanel;
    // private JTextField isdsCallServerIPTF, isdsCallServerPortTF, isdsCallServerNameTF;
    // !!! MANU_NAV Ende !!!

    private JTextField navCallServerIPTextField;
    // private SingleLineListBox navMaxConnectionsBox, navMaxSearchResultBox;
    private JSpinner maxConnectionsSpinner;
    private JSpinner maxSearchResultsSpinner;
    private JRadioButton optimizeLayout;
    private JRadioButton optimizeSpeed;
    // private JCheckBox navProportionalResizeCheck, navContinuousLayoutCheck, navSortChildrenCheck;
    private JCheckBox navSortChildrenCheck;
    private JComboBox navLookAndFeelBox;
    private JRadioButton sortAscendingOption;
    private JRadioButton sortDescendingOption;
    private JButton okButton;
    private JButton cancelButton;

    //~ Constructors -----------------------------------------------------------

    /**
     * PlugIn Support private PlugInModel plugInModel; private JPanel plugInPanel; private JComboBox plugInBox; public
     * OptionsDialog(NavigatorModel navigatorModel, PlugInModel plugInModel) { //_TA_super(new JFrame(), "Erweiterte
     * Optionen", true); super(new JFrame(), this.resources.getString("STL@moreOptions") , true); this.navigatorModel =
     * navigatorModel; this.plugInModel = plugInModel; initOptionsDialog(); } DEPRECATED
     * ---------------------------------------------------------------
     *
     * @deprecated  public OptionsDialog(NavigatorModel navigatorModel, ISFloatingFrameModel isFloatingFrameModel) {
     *              //_TA_super(new JFrame(), "Erweiterte Optionen", true); super(new JFrame(),
     *              this.resources.getString("STL@moreOptions") , true); this.navigatorModel = navigatorModel;
     *              this.isFloatingFrameModel = isFloatingFrameModel; initOptionsDialog(); }
     * @deprecated  !!! MANU_NAV Start !!! Zur Verwendung von ISDS!!! [ISDS PD 12062002] public
     *              OptionsDialog(NavigatorModel navigatorModel, ISFloatingFrameModel isFloatingFrameModel,
     *              ISDSFloatingFrameModel isdsFloatingFrameModel ) { //_TA_super(new JFrame(), "Erweiterte Optionen",
     *              true); super(new JFrame(), this.resources.getString("STL@moreOptions") , true); this.navigatorModel
     *              = navigatorModel; this.isFloatingFrameModel = isFloatingFrameModel; this.isdsFloatingFrameModel =
     *              isdsFloatingFrameModel; initOptionsDialog(); } !!! MANU_NAV Ende !!! DEPRECATED
     *              ---------------------------------------------------------------
     */
    public OptionsDialog() {
        this(null, org.openide.util.NbBundle.getMessage(OptionsDialog.class, "OptionsDialog.title")); // NOI18N
    }

    /**
     * Creates a new OptionsDialog object.
     *
     * @param  owner  DOCUMENT ME!
     * @param  title  DOCUMENT ME!
     */
    public OptionsDialog(final JFrame owner, final String title) {
        super(owner, title, true);

        this.logger = Logger.getLogger(this.getClass());

        this.initOptionsDialog();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    private void initOptionsDialog() {
        final JPanel contentPane = new JPanel(new GridBagLayout());
        // JTabbedPane tabbedPane = new JTabbedPane();
        final JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        final GridBagConstraints gbc = new GridBagConstraints();

        buildNavigatorPanel();
        // _TA_tabbedPane.addTab("Navigator", navigatorPanel);
        // tabbedPane.addTab(this.resources.getString("STL@navigator"), navigatorPanel);

        // buildPlugInPanel();
        // tabbedPane.addTab(this.resources.getString("STL@OptionsDialog.plugInPaneTitle"), plugInPanel);

        // DEPRECATED ---------------------------------------------------------------
        /*//[ISDS PD 12062002]  if ( this.isdsFloatingFrameModel != null )
         *      {                     buildISFloatingFramePanel();                     //_TA_tabbedPane.addTab("Sicad
         * IMS Client", isFloatingFramePanel);
         * tabbedPane.addTab(this.resources.getString("STL@OptionsDialog.tabbedPaneTitle"), isFloatingFramePanel);
         * }*/

        // !!! MANU_NAV Start !!!
        // [ISDS PD 12062002]
/*      if ( this.isdsFloatingFrameModel != null )
                {
                        buildISDSFloatingFramePanel();
                        tabbedPane.addTab("ISDS Client", isdsFloatingFramePanel);
                }*/
        // !!! MANU_NAV Ende !!!
        // DEPRECATED ---------------------------------------------------------------

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        // contentPane.add(tabbedPane, gbc);
        contentPane.add(navigatorPanel, gbc);

        gbc.insets = new Insets(0, 20, 10, 20);
        gbc.gridy++;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        contentPane.add(buttonPanel, gbc);

        // _TA_okButton = new JButton("Uebernehmen");
        okButton = new JButton(org.openide.util.NbBundle.getMessage(
                    OptionsDialog.class,
                    "OptionsDialog.okButton.text")); // NOI18N
        // _TA_okButton.setMnemonic('U');
        okButton.setMnemonic(org.openide.util.NbBundle.getMessage(
                OptionsDialog.class,
                "OptionsDialog.okButton.mnemonics").charAt(0)); // NOI18N
        okButton.setActionCommand("apply");                     // NOI18N
        okButton.addActionListener(this);
        buttonPanel.add(okButton);

        // _TA_cancelButton = new JButton("Abbrechen");
        cancelButton = new JButton(org.openide.util.NbBundle.getMessage(
                    OptionsDialog.class,
                    "OptionsDialog.cancelButton.text")); // NOI18N
        // _TA_cancelButton.setMnemonic('A');
        cancelButton.setMnemonic(org.openide.util.NbBundle.getMessage(
                OptionsDialog.class,
                "OptionsDialog.cancelButton.mnemonics").charAt(0)); // NOI18N
        cancelButton.setActionCommand("cancel");                    // NOI18N
        cancelButton.addActionListener(this);
        buttonPanel.add(cancelButton);

        this.setContentPane(contentPane);
        // this.pack();
        this.setResizable(false);
    }
    /**
     * Erzeugt das Panel fuer die Einstellungen des NavigatorModels.<br>
     * Wird nur von der Initialisierungsmethode verwendet.
     */
    private void buildNavigatorPanel() {
        navigatorPanel = new JPanel(new GridBagLayout());
        navigatorPanel.setBorder(new EmptyBorder(10, 10, 0, 10));
        final GridBagConstraints gbc = new GridBagConstraints();
        final GridBagConstraints gbc2 = new GridBagConstraints();

        // SERVER ==============================================================
        // top, left, bottom, right
        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        // gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        final JPanel navigatorServerPanel = new JPanel(new GridBagLayout());
        // _TA_navigatorServerPanel.setBorder(new CompoundBorder(new TitledBorder(null, "Server", TitledBorder.LEFT,
        // TitledBorder.TOP), new EmptyBorder(2,5,5,5)));
        navigatorServerPanel.setBorder(new CompoundBorder(
                new TitledBorder(
                    null,
                    org.openide.util.NbBundle.getMessage(
                        OptionsDialog.class,
                        "OptionsDialog.navigatorServerPanel.border.title"), // NOI18N
                    TitledBorder.LEFT,
                    TitledBorder.TOP),
                new EmptyBorder(2, 5, 5, 5)));
        navigatorPanel.add(navigatorServerPanel, gbc);

        // LABELS
        gbc2.insets = new Insets(0, 0, 7, 5);
        gbc2.anchor = GridBagConstraints.WEST;
        gbc2.fill = GridBagConstraints.BOTH;
        gbc2.gridheight = 1;
        gbc2.gridwidth = 1;
        gbc2.gridy = 0;
        gbc2.gridy = 0;
        gbc2.weightx = 0.0;
        gbc2.weighty = 0.0;
        // _TA_navigatorServerPanel.add(new JLabel("CallServer IP Adresse:"), gbc2);
        // navigatorServerPanel.add(new JLabel(this.resources.getString("STL@callServerIP")), gbc2);
        navigatorServerPanel.add(new JLabel(
                org.openide.util.NbBundle.getMessage(
                    OptionsDialog.class,
                    "OptionsDialog.navigatorServerPanel.urlLabel.text")),
            gbc2); // NOI18N
        gbc2.gridy++;
        // _TA_navigatorServerPanel.add(new JLabel("max. Verbindungen:"), gbc2);
        navigatorServerPanel.add(new JLabel(
                org.openide.util.NbBundle.getMessage(
                    OptionsDialog.class,
                    "OptionsDialog.navigatorServerPanel.connectionsLabel.text")),
            gbc2); // NOI18N
        gbc2.insets = new Insets(0, 0, 0, 5);
        gbc2.gridy++;
        // _TA_navigatorServerPanel.add(new JLabel("max. Suchergebnisse:"), gbc2);
        navigatorServerPanel.add(new JLabel(
                org.openide.util.NbBundle.getMessage(
                    OptionsDialog.class,
                    "OptionsDialog.navigatorServerPanel.resultsLabel.text")),
            gbc2); // NOI18N

        // Eingabefelder
        gbc2.insets = new Insets(0, 0, 7, 0);
        gbc2.fill = GridBagConstraints.NONE;
        gbc2.gridx = 1;
        gbc2.gridy = 0;
        gbc2.weightx = 1.0;
        gbc2.weighty = 1.0;
        navCallServerIPTextField = new JTextField(15);
        navigatorServerPanel.add(navCallServerIPTextField, gbc2);

        gbc2.gridy++;
        gbc2.weightx = 0.0;
        gbc2.weighty = 0.0;
        // navMaxConnectionsBox = new SingleLineListBox(NavigatorModel.MIN_SERVER_THREADS,
        // NavigatorModel.MAX_SERVER_THREADS, 1);
        maxConnectionsSpinner = new JSpinner(new SpinnerNumberModel(
                    PropertyManager.MIN_SERVER_THREADS,
                    PropertyManager.MIN_SERVER_THREADS,
                    PropertyManager.MAX_SERVER_THREADS,
                    1));
        navigatorServerPanel.add(maxConnectionsSpinner, gbc2);

        gbc2.insets = new Insets(0, 0, 0, 0);
        gbc2.gridy++;
        // navMaxSearchResultBox = new SingleLineListBox(NavigatorModel.MIN_SEARCH_RESULTS,
        // NavigatorModel.MAX_SEARCH_RESULTS, 1);
        maxSearchResultsSpinner = new JSpinner(new SpinnerNumberModel(
                    PropertyManager.MIN_SEARCH_RESULTS,
                    PropertyManager.MIN_SEARCH_RESULTS,
                    PropertyManager.MAX_SEARCH_RESULTS,
                    10));
        navigatorServerPanel.add(maxSearchResultsSpinner, gbc2);

        // ANSICHT =============================================================
        // top, left, bottom, right
        gbc.insets = new Insets(0, 0, 8, 0);
        gbc.gridy++;
        final JPanel navigatorLayoutManagerPanel = new JPanel(new GridBagLayout());
        // _TA_navigatorLayoutManagerPanel.setBorder(new CompoundBorder(new TitledBorder(null, "Komponenten Ansicht",
        // TitledBorder.LEFT, TitledBorder.TOP), new EmptyBorder(2,5,5,5)));
        navigatorLayoutManagerPanel.setBorder(new CompoundBorder(
                new TitledBorder(
                    null,
                    org.openide.util.NbBundle.getMessage(
                        OptionsDialog.class,
                        "OptionsDialog.navigatorLayoutManagerPanel.border.title"), // NOI18N
                    TitledBorder.LEFT,
                    TitledBorder.TOP),
                new EmptyBorder(2, 5, 5, 5)));
        navigatorPanel.add(navigatorLayoutManagerPanel, gbc);

        gbc2.insets = new Insets(0, 0, 5, 0);
        gbc2.anchor = GridBagConstraints.WEST;
        gbc2.fill = GridBagConstraints.NONE;
        gbc2.gridheight = 1;
        gbc2.gridwidth = 1;
        gbc2.gridy = 0;
        gbc2.gridy = 0;
        gbc2.weightx = 1.0;
        gbc2.weighty = 1.0;
        // navLookAndFeelBox = new JComboBox(new String[]{PropertyManager.METAL, PropertyManager.WINDOWS,
        // PropertyManager.MOTIF}); navLookAndFeelBox = new JComboBox(new String[]{navigatorModel.METAL,
        // navigatorModel.WINDOWS, navigatorModel.MOTIF});
        navLookAndFeelBox = new JComboBox(new java.util.Vector(
                    Sirius.navigator.ui.LAFManager.getManager().getInstalledLookAndFeelNames()));
        navigatorLayoutManagerPanel.add(navLookAndFeelBox, gbc2);

        gbc2.gridy++;
        // _TA_navProportionalResizeCheck = new JCheckBox("Proportionale Groessenaenderung");
        // navProportionalResizeCheck = new JCheckBox(this.resources.getString("STL@propSizeChange"));
        // navProportionalResizeCheck = new JCheckBox("Optimierte Darstellung");
        // navigatorLayoutManagerPanel.add(navProportionalResizeCheck, gbc2);

        final ButtonGroup buttonGroup = new ButtonGroup();
        optimizeLayout = new JRadioButton(org.openide.util.NbBundle.getMessage(
                    OptionsDialog.class,
                    "OptionsDialog.optimizeLayout.text")); // NOI18N
        buttonGroup.add(optimizeLayout);
        navigatorLayoutManagerPanel.add(optimizeLayout, gbc2);

        gbc2.insets = new Insets(0, 0, 0, 0);
        gbc2.gridy++;
        // _TA_navContinuousLayoutCheck = new JCheckBox("Waehrend Groessenaenderung anzeigen");
        // navContinuousLayoutCheck = new JCheckBox(this.resources.getString("STL@showWhileSizeChange"));
        // navigatorLayoutManagerPanel.add(navContinuousLayoutCheck, gbc2);

        optimizeSpeed = new JRadioButton(org.openide.util.NbBundle.getMessage(
                    OptionsDialog.class,
                    "OptionsDialog.optimizeSpeed.text")); // NOI18N
        buttonGroup.add(optimizeSpeed);
        navigatorLayoutManagerPanel.add(optimizeSpeed, gbc2);

        // SORTIERUNG =============================================================
        // top, left, bottom, right
        gbc.insets = new Insets(0, 0, 8, 0);
        gbc.gridy++;
        final JPanel sortPanel = new JPanel(new GridBagLayout());
        // _TA_navigatorLayoutManagerPanel.setBorder(new CompoundBorder(new TitledBorder(null, "Komponenten Ansicht",
        // TitledBorder.LEFT, TitledBorder.TOP), new EmptyBorder(2,5,5,5))); _TA_2.sortPanel.setBorder(new
        // CompoundBorder(new TitledBorder(null, "Sortierung", TitledBorder.LEFT, TitledBorder.TOP), new
        // EmptyBorder(2,5,5,5)));
        sortPanel.setBorder(new CompoundBorder(
                new TitledBorder(
                    null,
                    org.openide.util.NbBundle.getMessage(OptionsDialog.class, "OptionsDialog.sortPanel.border.title"), // NOI18N
                    TitledBorder.LEFT,
                    TitledBorder.TOP),
                new EmptyBorder(2, 5, 5, 5)));
        navigatorPanel.add(sortPanel, gbc);

        gbc2.insets = new Insets(0, 0, 5, 0);
        gbc2.anchor = GridBagConstraints.WEST;
        gbc2.fill = GridBagConstraints.NONE;
        gbc2.gridheight = 1;
        gbc2.gridwidth = 1;
        gbc2.gridy = 0;
        gbc2.gridy = 0;
        gbc2.weightx = 1.0;
        gbc2.weighty = 1.0;

        // _TA_navSortChildrenCheck = new JCheckBox("Sortierung verwenden", true);
        navSortChildrenCheck = new JCheckBox(
                org.openide.util.NbBundle.getMessage(OptionsDialog.class, "OptionsDialog.navSortChildrenCheck.text"), // NOI18N
                true);
        navSortChildrenCheck.setActionCommand("sort"); // NOI18N
        navSortChildrenCheck.addActionListener(this);
        sortPanel.add(navSortChildrenCheck, gbc2);

        final ButtonGroup buttonGroup1 = new ButtonGroup();

        gbc2.gridy++;
        // _TA_sortAscendingOption = new JRadioButton("aufsteigend sortieren");
        sortAscendingOption = new JRadioButton(
                org.openide.util.NbBundle.getMessage(OptionsDialog.class, "OptionsDialog.sortAscendingOption.text")); // NOI18N
        buttonGroup1.add(sortAscendingOption);
        sortPanel.add(sortAscendingOption, gbc2);

        gbc2.insets = new Insets(0, 0, 0, 0);
        gbc2.gridy++;
        // _TA_sortDescendingOption = new JRadioButton("absteigend sortieren");
        sortDescendingOption = new JRadioButton(
                org.openide.util.NbBundle.getMessage(OptionsDialog.class, "OptionsDialog.sortDescendingOption.text")); // NOI18N
        buttonGroup1.add(sortDescendingOption);
        sortPanel.add(sortDescendingOption, gbc2);
    }

    /**
     * DOCUMENT ME!
     */
    private void updateNavigatorPanel() {
        if (logger.isDebugEnabled()) {
            logger.debug("loading properties"); // NOI18N
        }
        final PropertyManager properties = PropertyManager.getManager();

        navCallServerIPTextField.setText(properties.getConnectionInfo().getCallserverURL());
        // navMaxConnectionsBox.setSelectedValue(properties.getMaxConnections());
        // navMaxSearchResultBox.setSelectedValue(navigatorModel.getMaxSearchResults());
        ((SpinnerNumberModel)maxConnectionsSpinner.getModel()).setValue(new Integer(properties.getMaxConnections()));
        ((SpinnerNumberModel)maxSearchResultsSpinner.getModel()).setValue(new Integer(
                properties.getMaxSearchResults()));
        optimizeLayout.setSelected(properties.isAdvancedLayout());
        optimizeSpeed.setSelected(!properties.isAdvancedLayout());
        // navProportionalResizeCheck.setSelected(properties.isAdvancedLayout());
        // navContinuousLayoutCheck.setSelected(properties.isAdvancedLayout());
        navLookAndFeelBox.setSelectedItem(properties.getLookAndFeel());
        navSortChildrenCheck.setSelected(properties.isSortChildren());
        sortAscendingOption.setSelected(properties.isSortAscending());
        sortDescendingOption.setSelected(!properties.isSortAscending());
        sortAscendingOption.setEnabled(navSortChildrenCheck.isSelected());
        sortDescendingOption.setEnabled(navSortChildrenCheck.isSelected());

        // NavigatorLogger.printMessage(navigatorModel.getMaxConnections());
        // NavigatorLogger.printMessage(navigatorModel.getMaxSearchResults());
        // NavigatorLogger.printMessage(navMaxConnectionsBox.getSelectedIntValue());
        // NavigatorLogger.printMessage(navMaxSearchResultBox.getSelectedIntValue());
    }

    /**
     * DOCUMENT ME!
     */
    private void updateNavigatorModel() {
        if (logger.isDebugEnabled()) {
            logger.debug("saving properties"); // NOI18N
        }
        final PropertyManager properties = PropertyManager.getManager();

        properties.getConnectionInfo().setCallserverURL(navCallServerIPTextField.getText());
        properties.setMaxConnections(((SpinnerNumberModel)maxConnectionsSpinner.getModel()).getNumber().intValue());
        properties.setMaxSearchResults(((SpinnerNumberModel)maxSearchResultsSpinner.getModel()).getNumber().intValue());
        properties.setAdvancedLayout(optimizeLayout.isSelected());
        // properties.setAdvancedLayout(navContinuousLayoutCheck.isSelected());
        properties.setLookAndFeel((String)navLookAndFeelBox.getSelectedItem());
        properties.setSortChildren(navSortChildrenCheck.isSelected());
        properties.setSortAscending(sortAscendingOption.isSelected());

        // !!! MANU_NAV Start !!! [ISDS PD 12062002] if (isdsFloatingFrameModel != null )
        // isdsFloatingFrameModel.setCallServerProperties( (String)isdsCallServerIPTF.getText(),
        // (String)isdsCallServerPortTF.getText(), (String)isdsCallServerNameTF.getText() ); !!! MANU_NAV Ende !!!

    }

    /*private void buildPlugInPanel()
     * {      isFloatingFramePanel = new JPanel(new GridLayout(1,1,5,5));     isFloatingFramePanel.setBorder(new
     * CompoundBorder(new EmptyBorder(10,10,10,10), new EtchedBorder()));      String isfString = new String("<html>");
     * //<center><h2>IMS Parameter:</h2>");     String[] isfParameter = isFloatingFrameModel.toStringArray(); for(int i
     * = 0; i < isfParameter.length; i++)             isfString += ("<p>" + isfParameter[i] + "</p>"); isfString +=
     * "</html>";      JLabel isfLabel = new JLabel(isfString);     isfLabel.setBorder(new
     * EmptyBorder(5,5,5,5));     isFloatingFramePanel.add(isfLabel);}*/

    /*
     *@deprecated
     */
    /*private void buildISFloatingFramePanel()
     * {     isFloatingFramePanel = new JPanel(new GridLayout(1,1,5,5));     isFloatingFramePanel.setBorder(new
     * CompoundBorder(new EmptyBorder(10,10,10,10), new EtchedBorder()));      String isfString = new String("<html>");
     * //<center><h2>IMS Parameter:</h2>");     String[] isfParameter = isFloatingFrameModel.toStringArray(); for(int i
     * = 0; i < isfParameter.length; i++)             isfString += ("<p>" + isfParameter[i] + "</p>"); isfString +=
     * "</html>";      JLabel isfLabel = new JLabel(isfString);     isfLabel.setBorder(new
     * EmptyBorder(5,5,5,5));     isFloatingFramePanel.add(isfLabel);}*/

    // !!! MANU_NAV Start !!!
    /*
     *@deprecated
     */
    /*public void buildISDSFloatingFramePanel()
     * {     GridBagLayout isds_gbl = new GridBagLayout();     GridBagConstraints isds_gbc = new GridBagConstraints();
     * isdsFloatingFramePanel = new JPanel( isds_gbl );     isdsFloatingFramePanel.setBorder( new CompoundBorder( new
     * TitledBorder(null, "ISDS Options", TitledBorder.LEFT, TitledBorder.TOP), new EmptyBorder(2,5,5,5) ) );       //
     * LABELS      // bottom left right top     isds_gbc.insets = new Insets(0, 0, 7, 5);     isds_gbc.anchor =
     * GridBagConstraints.NORTHWEST;     isds_gbc.fill = GridBagConstraints.NORTH;      isds_gbc.gridheight = 1;
     * isds_gbc.gridwidth = 1;     isds_gbc.weightx = 0.0;     isds_gbc.weighty = 0.0;     isds_gbc.gridx = 0;
     * isds_gbc.gridy = 0;      //isdsFloatingFramePanel.add(new JLabel(this.resources.getString("STL@callServerIP")),
     * isds_gbc);     isdsFloatingFramePanel.add(new JLabel(this.resources.getString("STL@callServerIP")), isds_gbc );
     * isds_gbc.gridy++;     isdsFloatingFramePanel.add(new JLabel("Port:"), isds_gbc);     isds_gbc.gridy++;
     * isdsFloatingFramePanel.add(new JLabel("Servername:"), isds_gbc);     isds_gbc.gridy++;       isds_gbc.gridy = 0;
     * isds_gbc.gridx++;      isdsCallServerIPTF = new JTextField(9);     //[ISDS PD
     * 12062002]isdsCallServerIPTF.setText( (String)isdsFloatingFrameModel.getCallServerIP() );
     * isdsFloatingFramePanel.add(isdsCallServerIPTF, isds_gbc);     isds_gbc.gridy++;     isdsCallServerPortTF = new
     * JTextField(9);     //[ISDS PD 12062002]isdsCallServerPortTF.setText(
     * (String)isdsFloatingFrameModel.getCallServerPort() );     isdsFloatingFramePanel.add(isdsCallServerPortTF,
     * isds_gbc);     isds_gbc.gridy++;     isdsCallServerNameTF = new JTextField(9);     //[ISDS PD
     * 12062002]isdsCallServerNameTF.setText( (String)isdsFloatingFrameModel.getCallServerName() );
     * isdsFloatingFramePanel.add(isdsCallServerNameTF, isds_gbc); }*/

    // !!! MANU_NAV Ende !!!

    @Override
    public void show() {
        updateNavigatorPanel();
        this.pack();
        super.show();
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        if (e.getActionCommand().equals("sort"))          // NOI18N
        {
            sortDescendingOption.setEnabled(navSortChildrenCheck.isSelected());
            sortAscendingOption.setEnabled(navSortChildrenCheck.isSelected());
        } else if (e.getActionCommand().equals("cancel")) // NOI18N
        {
            dispose();
        } else if (e.getActionCommand().equals("apply"))  // NOI18N
        {
            updateNavigatorModel();
            // updateISFloatingFrameModel();
            dispose();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  args  DOCUMENT ME!
     */
    public static void main(final String[] args) {
        final OptionsDialog od = new OptionsDialog();
        od.show();
    }
}
