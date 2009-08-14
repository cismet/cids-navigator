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

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class MutablePanel extends JPanel
{
    //public final static String ENABLED = "enabledPanel";
    //public final static String DISABLED = "disabledPanel";
    
    //protected boolean disabled = false;
    
    protected JPanel enabledPanel;
    protected JPanel disabledPanel;
    protected JLabel disabledLabel;
    
    ////_TA_protected String disabledString = new String("Diese Widget ist z.Z. nicht anzeigbar");
    //protected String disabledString = new String(StringLoader.getString("STL@widgetNotAvailable"));
    
    public MutablePanel(JPanel enabledPanel)
    {
        this(enabledPanel, null);
    }
    
    public MutablePanel(JPanel enabledPanel, String disabledMessage)
    {
        super(new CardLayout());
        this.enabledPanel =  enabledPanel;
        
        disabledLabel = new JLabel(disabledMessage);
        disabledLabel.setVerticalAlignment(JLabel.CENTER);
        disabledLabel.setHorizontalAlignment(JLabel.CENTER);
        disabledLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
        disabledPanel = new JPanel(new BorderLayout());
        disabledPanel.add(disabledLabel, BorderLayout.CENTER);
        
        this.setEnabledPanel(enabledPanel);
        this.setDisabledPanel(disabledPanel);
    }
    
    public void setEnabled(final boolean enabled)
    {
        if(SwingUtilities.isEventDispatchThread())
        {
            super.setEnabled(enabled);
            if(enabled)
            {
                ((CardLayout)this.getLayout()).show(this, "enabledPanel");
            }
            else
            {
                ((CardLayout)this.getLayout()).show(this, "disabledPanel");
            }
        }
        else
        {
            SwingUtilities.invokeLater(new Runnable()
            {
                public void run()
                {
                    MutablePanel.this.setEnabled(enabled);
                }
            });
        }
    }

    /** Getter for property enabledPanel.
     * @return Value of property enabledPanel.
     *
     */
    public JPanel getEnabledPanel()
    {
        return this.enabledPanel;
    }
    
    /** Setter for property enabledPanel.
     * @param enabledPanel New value of property enabledPanel.
     *
     */
    public void setEnabledPanel(JPanel enabledPanel)
    {
        this.remove(this.enabledPanel);
        this.add(enabledPanel, "enabledPanel");
        this.enabledPanel = enabledPanel; 
    }
    
    /** Getter for property disabledPanel.
     * @return Value of property disabledPanel.
     *
     */
    public JPanel getDisabledPanel()
    {
        return this.disabledPanel;
    }
    
    /** Setter for property disabledPanel.
     * @param disabledPanel New value of property disabledPanel.
     *
     */
    public void setDisabledPanel(JPanel disabledPanel)
    {
        this.remove(this.disabledPanel);
        this.add(disabledPanel, "disabledPanel");
        this.disabledPanel = disabledPanel; 
    }  
    
    public void setDisabledMessage(String message)
    {
        this.disabledLabel.setText(message);
    }
}