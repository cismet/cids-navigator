package Sirius.navigator.ui.status;

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
 * Created			:	16.02.2000
 * History			:
 *
 *******************************************************************************/
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

import org.apache.log4j.Logger;

import Sirius.navigator.ui.widget.MutableImageLabel;
import Sirius.navigator.resource.*;

public class MutableStatusBar extends JPanel
{
    protected final static Logger logger = Logger.getLogger(MutableStatusBar.class);
    
    private JLabel status_1;
    private JLabel status_2;
    private JLabel status_3;
    
    private MutableImageLabel greenStatusIcon;
    private MutableImageLabel redStatusIcon;
    
    public MutableStatusBar()
    {
        super();
        status_1 = new JLabel("");
        status_2 = new JLabel("");
        status_3 = new JLabel("");
        this.init();
    }
    
    /*public MutableStatusBar(String s1, String s2, String s3)
    {
        super();
        status_1 = new JLabel(s1);
        status_2 = new JLabel(s2);
        status_3 = new JLabel(s3);
        this.init();
    }*/
    
    protected void init()
    {
        this.setLayout(new GridBagLayout());
        this.setBorder(BorderFactory.createEmptyBorder(3, 2, 1, 1));
        GridBagConstraints constraints = new GridBagConstraints();
        
        
        //status_1.setHorizontalAlignment(JLabel.CENTER);
        status_1.setBorder(new CompoundBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED), new EmptyBorder(0,2,0,2)));
        status_1.setPreferredSize(new Dimension(180, 16));
        
        status_2.setBorder(new CompoundBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED), new EmptyBorder(0,2,0,2)));
        status_2.setPreferredSize(new Dimension(200, 16));
        
        status_3.setBorder(new CompoundBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED), new EmptyBorder(0,2,0,2)));
        status_3.setPreferredSize(new Dimension(300, 16));
        
        greenStatusIcon = new MutableImageLabel(ResourceManager.getManager().getIcon("green_off.gif"), ResourceManager.getManager().getIcon("green_on.gif"));
        greenStatusIcon.setBorder(new EmptyBorder(2,2,2,1));
        
        redStatusIcon = new MutableImageLabel(ResourceManager.getManager().getIcon("red_off.gif"), ResourceManager.getManager().getIcon("red_on.gif"));
        redStatusIcon.setBorder(new EmptyBorder(2,1,2,3));
        // =====================================================================
        
        constraints.insets = new Insets(0, 0, 0, 4);
        constraints.anchor = GridBagConstraints.EAST;
        constraints.fill = GridBagConstraints.VERTICAL;
        
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        this.add(status_1, constraints);
        
        constraints.weightx = 0.0;
        constraints.gridx++;
        this.add(status_2, constraints);
        
        constraints.gridx = 2;
        this.add(status_3, constraints);
        
        constraints.gridx++;
        if (greenStatusIcon != null)
            this.add(greenStatusIcon, constraints);
        
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.gridx++;
        if (redStatusIcon != null)
            this.add(redStatusIcon, constraints);
    }
    
    public  void setStatusMessage(String statusMessage, int messagePosition)
    {
       //if(logger.isDebugEnabled())logger.debug("setStatusMessage: '" + statusMessage + "' @position: '" + messagePosition + "'");
       switch (messagePosition)
        {
            case Status.MESSAGE_IGNORE:         
            break;
            case Status.MESSAGE_POSITION_1:	status_1.setText(statusMessage);
            break;
            case Status.MESSAGE_POSITION_2:	status_2.setText(statusMessage);
            break;
            case Status.MESSAGE_POSITION_3:	status_3.setText(statusMessage);
            break;
        }
    }
    
    public  void setGreenIconStatus(int greenIconStatus)
    {
        //if(logger.isDebugEnabled())logger.debug("setGreenIconStatus: '" + greenIconStatus + "'");
        switch (greenIconStatus)
        {
            case Status.ICON_IGNORE:        
            break;
            case Status.ICON_ACTIVATED:     greenStatusIcon.switchOn(true);
            break;
            case Status.ICON_DEACTIVATED:   greenStatusIcon.switchOff(true);
            break;
            case Status.ICON_BLINKING:      greenStatusIcon.blink(500);
            break;
            default:                        greenStatusIcon.switchOff(true);
        }
    }
    
    public void setRedIconStatus(int redIconStatus)
    {
        //if(logger.isDebugEnabled())logger.debug("setRedIconStatus: '" + redIconStatus + "'");
        switch (redIconStatus)
        {
            case Status.ICON_IGNORE:        
            break;
            case Status.ICON_ACTIVATED:     redStatusIcon.switchOn(true);
            break;
            case Status.ICON_DEACTIVATED:   redStatusIcon.switchOff(true);
            break;
            case Status.ICON_BLINKING:      redStatusIcon.blink(500);
            break;
            default:                        redStatusIcon.switchOn(true);
        }
    }
    
    public void setStatus(Status status)
    {
        this.setStatusMessage(status.getStatusMessage(), status.getMessagePosition());
        this.setRedIconStatus(status.getRedIconState());
        this.setGreenIconStatus(status.getGreenIconState());
    }
    
    public boolean isGreenStatusIconBlinking()
    {
        return greenStatusIcon.isBlinking();
    }
    
    public boolean isRedStatusIconBlinking()
    {
        return redStatusIcon.isBlinking();
    }
}
