package Sirius.navigator.ui.dialog;

/*******************************************************************************
 *
 * Copyright (c)	:	EIG (Environmental Informatics Group)
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
 * Programmers		:	Pascal
 *
 * Project			:	WuNDA 2
 * Filename		:
 * Version			:	1.0
 * Purpose			:
 * Created			:	01.10.1999
 * History			:
 *
 *******************************************************************************/
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.ResourceBundle;
import Sirius.navigator.tools.InputValidator;
import Sirius.navigator.resource.*;


public class CoordinateChooser extends JDialog implements ActionListener
{
    private static final ResourceBundle I18N = ResourceBundle.getBundle("Sirius/navigator/resource/i18n/resources");

    protected int coordinates[] = new int[]
    {0,0,0,0};
    //_TA_protected String infoString = "<html><center><p>Bitte geben Sie die Koordinaten des Interessensbereichs an,</p><p>auf den Sie die Karte beschraenken wollen.</p></center></html>";
    //protected String infoString = StringLoader.getString("STL@coordsOfInterests");
    
    protected boolean accepted = false;
    
    protected JTextField koordinatenRW1TextField, koordinatenHW1TextField, koordinatenRW2TextField, koordinatenHW2TextField;
    protected JButton buttonAccept, buttonCancel;
    
    public CoordinateChooser()
    {
        //_TA_super(new JFrame(), "Interessensbereich angeben", true);
        //super(new JFrame(), StringLoader.getString("STL@interests"), true);
        //super(new JFrame(), ResourceManager.getManager().getString("dialog.coordinate.title"));
        //initCoordinateChooser();
    }
    
    public CoordinateChooser(JFrame parent)
    {
        //_TA_super(new JFrame(), "Interessensbereich angeben", true);
        super(parent, I18N.getString("Sirius.navigator.ui.dialog.CoordinateChooser.title"), true);
        
        initCoordinateChooser();
    }
    
    public CoordinateChooser(JDialog parent)
    {
        //_TA_super(new JFrame(), "Interessensbereich angeben", true);
        super(parent, I18N.getString("Sirius.navigator.ui.dialog.CoordinateChooser.title"), true);
        
        initCoordinateChooser();
    }
    
        /*public CoordinateChooser(JDialog dialog, String title, String infoString)
        {
                super(dialog, title, true);
                this.infoString = infoString;
                initCoordinateChooser();
        }
         
        public CoordinateChooser(JDialog dialog, String title)
        {
                super(dialog, title, true);
                initCoordinateChooser();
        }*/
    
    protected void initCoordinateChooser()
    {
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        JPanel contentPane = new JPanel(new GridBagLayout());
        contentPane.setBorder(new EmptyBorder(10,10,8,10));
        GridBagConstraints gbc = new GridBagConstraints();
        GridBagConstraints constraints = new GridBagConstraints();
        
        
        gbc.insets = new Insets(0, 0, 15, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridheight = 1;
        gbc.gridwidth = 2;
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weightx = 0.1;
        gbc.weighty = 0.1;
        JLabel infoLabel = new JLabel(I18N.getString("dialog.coordinate.info"));
        infoLabel.setVerticalAlignment(JLabel.CENTER);
        infoLabel.setHorizontalAlignment(JLabel.CENTER);
        infoLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.add(infoLabel, gbc);
        
        // KOORDINATEN =========================================================
        gbc.insets = new Insets(0, 5, 20, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridy++;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        JPanel koordinatenPanel = new JPanel(new GridBagLayout());
        koordinatenPanel.setBorder(new EtchedBorder());
        contentPane.add(koordinatenPanel, gbc);
        
        // Rechtswert(1):
        constraints.insets = new Insets(5, 5, 8, 8);
        constraints.anchor = GridBagConstraints.EAST;
        constraints.fill = GridBagConstraints.VERTICAL;
        constraints.gridheight = 1;
        constraints.gridwidth = 1;
        constraints.gridy = 0;
        constraints.gridx = 0;
        constraints.weightx = 0.0;
        constraints.weighty = 0.0;
        //_TA_koordinatenPanel.add(new JLabel("Rechtswert linksunten:"), constraints);
        koordinatenPanel.add(new JLabel(
                I18N.getString("Sirius.navigator.ui.dialog.CoordinateChooser.koordinatenPanel.rwluLabel.text")),
                constraints);
        
        // Rechtswert 1 Textfield
        constraints.insets = new Insets(5, 0, 8, 5);
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridx = 1;
        koordinatenRW1TextField = new JTextField(6);
        koordinatenRW1TextField.setDocument(new InputValidator(InputValidator.NUMERIC));
        koordinatenPanel.add(koordinatenRW1TextField, constraints);
        
        // Hochwert(1):
        constraints.insets = new Insets(0, 5, 8, 8);
        constraints.anchor = GridBagConstraints.EAST;
        constraints.gridx = 0;
        constraints.gridy = 1;
        //_TA_koordinatenPanel.add(new JLabel("Hochwert linksunten:"), constraints);
        koordinatenPanel.add(new JLabel(
                I18N.getString("Sirius.navigator.ui.dialog.CoordinateChooser.koordinatenPanel.hwluLabel.text")),
                constraints);
        // Hochwert 1 Textfield
        constraints.insets = new Insets(0, 0, 8, 5);
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridx = 1;
        koordinatenHW1TextField = new JTextField(6);
        koordinatenHW1TextField.setDocument(new InputValidator(InputValidator.NUMERIC));
        koordinatenPanel.add(koordinatenHW1TextField, constraints);
        
        // Rechtswert(2):
        constraints.insets = new Insets(0, 5, 8, 8);
        constraints.anchor = GridBagConstraints.EAST;
        constraints.gridx = 0;
        constraints.gridy = 2;
        //_TA_koordinatenPanel.add(new JLabel("Rechtswert rechtsoben:"), constraints);
        koordinatenPanel.add(new JLabel(
                I18N.getString("Sirius.navigator.ui.dialog.CoordinateChooser.koordinatenPanel.rwroLabel.text")),
                constraints);
        //  Rechtswert 2 Textfield
        constraints.insets = new Insets(0, 0, 8, 5);
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridx = 1;
        koordinatenRW2TextField = new JTextField(6);
        koordinatenRW2TextField.setDocument(new InputValidator(InputValidator.NUMERIC));
        koordinatenPanel.add(koordinatenRW2TextField, constraints);
        // Hochwert(2):
        constraints.insets = new Insets(0, 5, 8, 8);
        constraints.anchor = GridBagConstraints.EAST;
        constraints.gridx = 0;
        constraints.gridy = 3;
        //_TA_koordinatenPanel.add(new JLabel("Hochwert rechtsoben:"), constraints);
        koordinatenPanel.add(new JLabel(
                I18N.getString("Sirius.navigator.ui.dialog.CoordinateChooser.koordinatenPanel.hwroLabel.text")),
                constraints);
        // Hochwert 1 Textfield
        constraints.insets = new Insets(0, 0, 5, 5);
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridx = 1;
        koordinatenHW2TextField = new JTextField(6);
        koordinatenHW2TextField.setDocument(new InputValidator(InputValidator.NUMERIC));
        koordinatenPanel.add(koordinatenHW2TextField, constraints);
        
        
        // ======================================
        
        gbc.insets = new Insets(0, 0, 0, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        buttonAccept = new JButton(I18N.getString("Sirius.navigator.ui.dialog.CoordinateChooser.buttonAccept.text"));
        //_TA_buttonAccept.setMnemonic('U');
        buttonAccept.setMnemonic(I18N.getString("Sirius.navigator.ui.dialog.CoordinateChooser.buttonAccept.mnemonic").charAt(0));
        buttonAccept.setActionCommand("accept");
        buttonAccept.addActionListener(this);
        contentPane.add(buttonAccept, gbc);
        
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.gridx++;
        //_TA_buttonCancel = new JButton("Ignorieren");
        buttonCancel = new JButton(I18N.getString("Sirius.navigator.ui.dialog.CoordinateChooser.buttonCancel.text"));
        //_TA_buttonCancel.setMnemonic('I');
        buttonCancel.setMnemonic(I18N.getString("Sirius.navigator.ui.dialog.CoordinateChooser.buttonCancel.mnemonic").charAt(0));
        buttonCancel.setActionCommand("cancel");
        buttonCancel.addActionListener(this);
        contentPane.add(buttonCancel, gbc);
        
        this.setContentPane(contentPane);
        this.pack();
        this.setResizable(false);
        
    }
    
    public void show(int[] coordinates)
    {
        if(coordinates != null && coordinates.length == 4)
            this.coordinates = coordinates;
        else
            this.coordinates = new int[]
            {0,0,0,0};
            
            this.show();
    }
    
    
    public void show()
    {
        koordinatenRW1TextField.setText(String.valueOf(this.coordinates[0]));
        koordinatenHW1TextField.setText(String.valueOf(this.coordinates[1]));
        koordinatenRW2TextField.setText(String.valueOf(this.coordinates[2]));
        koordinatenHW2TextField.setText(String.valueOf(this.coordinates[3]));
        
        super.show();
    }
    
    public boolean isCoordinateAccepted()
    {
        return accepted;
    }
    
    public int[] getCoordinate()
    {
        return coordinates;
    }
    
    public void actionPerformed(ActionEvent e)
    {
        if(e.getActionCommand().equals("accept"))
        {
            accepted = true;
            
            if(koordinatenRW1TextField.getText() == null || koordinatenRW1TextField.getText().length() < 1 || koordinatenRW2TextField.getText() == null || koordinatenRW2TextField.getText().length() < 1)
            {
                //_TA_JOptionPane.showMessageDialog(this, "Bitte geben Sie alle Rechtswerte an.", "Fehlerhafte Eingabe", JOptionPane.WARNING_MESSAGE);
                JOptionPane.showMessageDialog(this,
                        I18N.getString("Sirius.navigator.ui.dialog.CoordinateChooser.actionPerformed().missingRWOptionPane.message"),
                        I18N.getString("Sirius.navigator.ui.dialog.CoordinateChooser.actionPerformed().missingRWOptionPane.title"),
                        JOptionPane.WARNING_MESSAGE);
                accepted = false;
            }
            
            if(koordinatenHW1TextField.getText() == null || koordinatenHW1TextField.getText().length() < 1 || koordinatenHW2TextField.getText() == null || koordinatenHW2TextField.getText().length() < 1)
            {
                //_TA_JOptionPane.showMessageDialog(this, "Bitte geben Sie alle Hochwerte an.", "Fehlerhafte Eingabe", JOptionPane.WARNING_MESSAGE);

                JOptionPane.showMessageDialog(this,
                        I18N.getString("Sirius.navigator.ui.dialog.CoordinateChooser.actionPerformed().missingHWOptionPane.message"),
                        I18N.getString("Sirius.navigator.ui.dialog.CoordinateChooser.actionPerformed().missingHWOptionPane.title"),
                        JOptionPane.WARNING_MESSAGE);
                accepted = false;
            }
            
            if(accepted)
            {
                this.coordinates[0] = Integer.parseInt(koordinatenRW1TextField.getText());
                this.coordinates[1] = Integer.parseInt(koordinatenHW1TextField.getText());
                this.coordinates[2] = Integer.parseInt(koordinatenRW2TextField.getText());
                this.coordinates[3] = Integer.parseInt(koordinatenHW2TextField.getText());
                dispose();
            }
        }
        else if(e.getActionCommand().equals("cancel"))
        {
            accepted = false;
            dispose();
        }
    }
}

