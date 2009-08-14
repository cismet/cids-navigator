package Sirius.navigator.ui.widget;

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
import javax.swing.*;


/**
 * Ein neues ComboBoxModel.
 */

public class CheckBoxModel extends DefaultComboBoxModel
{
    protected boolean allSelected = false;
    protected int firstSelectedIndex = 0;
    
    public CheckBoxModel()
    {
        super();
    }
    
    public CheckBoxModel(String[] names, boolean selectAll)
    {
        
        //_TA_JCheckBox checkBox = new JCheckBox("Alle", selectAll);
        // XXX fixme
        JCheckBox checkBox = new JCheckBox("all", selectAll);
        //_TA_checkBox.setToolTipText("alle selektieren");
        //checkBox.setToolTipText(StringLoader.getString("STL@selectAll"));
        this.addElement(checkBox);
        for(int i = 0; i < names.length; i++)
            this.addElement(new JCheckBox(names[i], selectAll));
    }
}

