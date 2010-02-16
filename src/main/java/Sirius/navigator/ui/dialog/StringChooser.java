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
import java.util.*;

import org.apache.log4j.Logger;


import Sirius.navigator.resource.*;

public class StringChooser extends JDialog //implements ActionListener
{
    protected final static Logger logger = Logger.getLogger(StringChooser.class);
    private static final ResourceBundle I18N = ResourceBundle.getBundle("Sirius/navigator/resource/i18n/resources");
    
    protected JList stringList;
    protected JButton buttonAccept, buttonCancel;
    
    protected String infoMessage = null;
    //protected String naMessage = null;
    
    protected String selectedString = null;
    protected boolean accepted = false;
    
    /**
     * @deprecated naMessage not used anymore
     */
    public StringChooser(JFrame owner, String title, String infoMessage, String naMessage)
    {
        super(owner, title, true);
        this.infoMessage = infoMessage;
        //this.naMessage = naMessage;
        init();
    }
    
    public StringChooser(JFrame owner, String title)
    {
        super(owner, title, true);
        init();
    }
    
    public StringChooser(JDialog owner, String title, String infoMessage)
    {
        super(owner, title, true);
        this.infoMessage = infoMessage;
        //this.naMessage = naMessage;
        init();
    }
    
    public StringChooser(JDialog owner, String title)
    {
        super(owner, title, true);
        init();
    }
    
    protected void init()
    {
        ActionListener actionListener = new ButtonListener();
        ResourceManager resources = ResourceManager.getManager();
        
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        JPanel contentPane = new JPanel(new GridBagLayout());
        contentPane.setBorder(new EmptyBorder(10,10,8,10));
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridheight = 1;
        gbc.gridwidth = 2;
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weightx = 0.1;
        gbc.weighty = 0.1;
        JLabel infoLabel = new JLabel(infoMessage);
        infoLabel.setVerticalAlignment(JLabel.CENTER);
        infoLabel.setHorizontalAlignment(JLabel.CENTER);
        infoLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.add(infoLabel, gbc);
        
        gbc.insets = new Insets(0, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridy++;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        stringList = new JList();
        stringList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        contentPane.add(new JScrollPane(stringList), gbc);
   
        gbc.insets = new Insets(0, 0, 0, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.weightx = 0.1;
        gbc.weighty = 0.1;
        //_TA_buttonAccept = new JButton("Uebernehmen");
        //buttonAccept = new JButton(StringLoader.getString("STL@takeOn"));
        buttonAccept = new JButton(I18N.getString("Sirius.navigator.ui.dialog.StringChooser.buttonAccept.text"));
        buttonAccept.setMnemonic(I18N.getString("Sirius.navigator.ui.dialog.StringChooser.buttonAccept.mnemonic").charAt(0));
        buttonAccept.setToolTipText(I18N.getString("Sirius.navigator.ui.dialog.StringChooser.buttonAccept.tooltip"));
        buttonAccept.setActionCommand("accept");
        buttonAccept.addActionListener(actionListener);
        contentPane.add(buttonAccept, gbc);
        
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.gridx++;
        //_TA_buttonCancel = new JButton("Abbrechen");
        //buttonCancel = new JButton(StringLoader.getString("STL@cancel"));
        buttonCancel = new JButton(I18N.getString("Sirius.navigator.ui.dialog.StringChooser.buttonCancel.text"));
        buttonCancel.setMnemonic(I18N.getString("Sirius.navigator.ui.dialog.StringChooser.buttonCancel.mnemonic").charAt(0));
        buttonCancel.setToolTipText(I18N.getString("Sirius.navigator.ui.dialog.StringChooser.buttonCancel.tooltip"));
        buttonCancel.setActionCommand("cancel");
        buttonCancel.addActionListener(actionListener);
        contentPane.add(buttonCancel, gbc);
        
        this.setContentPane(contentPane);
        this.setSize(320,240);
    }
    
    public void show(String[] strings, String selectedString)
    {
        this.show(strings);
        this.setSelectedString(selectedString);
    }
    
    public void show(String[] strings)
    {
        if(strings != null && strings.length > 0)
        {
            stringList.setListData(strings);
            stringList.setSelectedIndex(-1);
        }
        else
        {
            stringList.removeAll();
            //stringList.setListData(new String[]{naMessage});
        }
        
        super.show();
    }
    
    public void show(Collection strings, String selectedString)
    {
       this.show(strings);
       this.setSelectedString(selectedString);
        //this.show((String[])strings.toArray(new String[strings.size()]), selectedString);
    }
    
    public void show(Collection strings)
    {
        stringList.setListData(new Vector(strings));
        stringList.setSelectedIndex(-1);
        super.show();
       //this.show((String[])strings.toArray(new String[strings.size()]));
    }
    
    public boolean isSelectionAccepted()
    {
        if(selectedString == null) // || selectedString.equals(naMessage))
        {
            return false;
        }
        else
        {
            return accepted;
        }
    }
    
    public void setSelectedString(String selectedString)
    {
         if(logger.isDebugEnabled())logger.debug("selecting string '" + selectedString + "'");
         if(((DefaultListModel)stringList.getModel()).indexOf(selectedString) != -1)
         {
            stringList.setSelectedValue(selectedString, true);
            accepted = true;
            this.selectedString = selectedString;
         }
         else
         {
             accepted = false;
             this.selectedString = null;
             logger.warn("string '" + selectedString + "' not found in list");
         }   
    }
    
    public String getSelectedString()
    {
        /*if(selectedString.equals(naMessage))
        {
            logger.warn("unavailable string '" + naMessage + "' selected, returning 'null'");
            return null;
        }
        else
        {
            return selectedString;
        }*/
        
        return selectedString;
    }
    
    protected class ButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if(e.getActionCommand().equals("accept"))
            {
                if(!stringList.isSelectionEmpty()) // && (String)stringList.getSelectedValue() != naMessage)
                {
                    accepted = true;
                    selectedString = stringList.getSelectedValue().toString().trim();
                    //NavigatorLogger.printMessage(selectedString);
                }
                else
                {
                    //if(logger.isDebugEnabled())logger.debug("unavailable string '" + naMessage + "' or nothing selected");
                    if(logger.isDebugEnabled())logger.debug("nothing selected");
                    accepted = false;
                    selectedString = null;
                }

                dispose();
            }
            else if(e.getActionCommand().equals("cancel"))
            {
                accepted = false;
                selectedString = null;
                dispose();
            }
        }
    }
}
