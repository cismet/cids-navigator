package Sirius.navigator.ui.widget;

/*******************************************************************************

 	Copyright (c)	:	EIG (Environmental Informatics Group)
				http://www.htw-saarland.de/eig
				Prof. Dr. Reiner Guettler
				Prof. Dr. Ralf Denzer

				HTWdS
				Hochschule fuer Technik und Wirtschaft des Saarlandes
				Goebenstr. 40
 				66117 Saarbruecken
 				Germany

	Programmers		:	Pascal

 	Project			:	WuNDA 2
	Version			:	1.0
 	Purpose			:
	Created			:	16.02.2000
	History			:       17.04.2003

*******************************************************************************/

import java.net.URL;
import java.awt.event.*;
import javax.swing.*;

public class MutableImageLabel extends JLabel
{
    private ImageIcon imageOff;
    private ImageIcon imageOn;
    private Timer timer;
    
    private boolean off = true;
    private boolean on = false;
    
    private int blinkQueue = 0;
    
    public MutableImageLabel(ImageIcon imageOff, ImageIcon imageOn)
    {
        super(imageOff);
        this.imageOff = imageOff;
        this.imageOn = imageOn;
        this.initImageLabel();
    }
    
    public MutableImageLabel(String imageOff, String imageOn)
    {
        this.imageOff = new ImageIcon(imageOff);
        this.imageOn  = new ImageIcon(imageOn);
        this.setIcon(this.imageOff);
        this.initImageLabel();
    }
    
    public MutableImageLabel(URL imageOff, URL imageOn)
    {
        this.imageOff = new ImageIcon(imageOff);
        this.imageOn  = new ImageIcon(imageOn);
        this.setIcon(this.imageOff);
        this.initImageLabel();
    }
    
    protected void initImageLabel()
    {
        off = true;
        on = false;
        
        timer = new Timer(250, new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
                if (on)
                    MutableImageLabel.this.imageOff();
                else
                    MutableImageLabel.this.imageOn();
            }
        });
        
        timer.setCoalesce(false);
    }
    
    protected synchronized void imageOff()
    {
        this.setIcon(imageOff);
        off = true;
        on = false;
    }
    
    protected synchronized void imageOn()
    {
        this.setIcon(imageOn);
        off = false;
        on = true;
    }
    
    public void switchOff(boolean stopBlinking)
    {
        if(stopBlinking)
            blinkQueue--;
        
        if (blinkQueue < 1)
        {
            blinkQueue = 0;
            timer.stop();
            this.imageOff();
        }
    }
    
    public void switchOn(boolean stopBlinking)
    {
        if(stopBlinking)
            blinkQueue--;
        
        if (blinkQueue < 1)
        {
            blinkQueue = 0;
            timer.stop();
            this.imageOn();
        }
    }
    
    public void blink(int msec)
    {
        timer.setDelay(msec);
        timer.start();
        blinkQueue++;
    }
    
    public boolean isBlinking()
    {
        if(blinkQueue > 0)
            return true;
        
        return false;
    }
    
    public void setImages(ImageIcon imageOff, ImageIcon imageOn)
    {
        blinkQueue = 0;
        this.imageOff = imageOff;
        this.imageOn = imageOn;
        this.switchOff(true);
    }
    
    public ImageIcon[] getImages()
    {
        return new ImageIcon[]
        {imageOff, imageOn};
    }
}

