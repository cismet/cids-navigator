package Sirius.navigator.tools;

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
 * Version			:	1.0
 * Purpose			:
 * Created			:	01.10.1999
 * History			:
 *
 *******************************************************************************/

import java.awt.*;
import javax.swing.*;


public class MetaToolkit
{
    private static Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
    /**
     * Methode zentriert ein \u00FCbergebenes Fenster auf dem Bildschirm
     * @param win Window Dieses Fenster soll zentriert werden
     */
    public static final void centerWindow(Window win)
    {
        Dimension winDim = win.getSize();
        // if larger than screen, reduce window width or height
        if (screenDim.width < winDim.width)
        {
            win.setSize(screenDim.width, winDim.height);
        }
        if (screenDim.height < winDim.height)
        {
            win.setSize(winDim.width, screenDim.height);
        }
        // center frame, dialogue or window on screen
        int x = (screenDim.width - winDim.width) / 2;
        int y = (screenDim.height - winDim.height) / 2;
        win.setLocation(x, y);
        
    }
    
    public void listCurrentThreads()
    {
        ThreadGroup currentGroup = Thread.currentThread().getThreadGroup();
        int numThreads = currentGroup.activeCount();
        Thread[] listOfThreads = new Thread[numThreads];
        
        currentGroup.enumerate(listOfThreads);
        for (int i = 0; i < numThreads; i++)
            System.out.println("Thread #" + i + " = " + listOfThreads[i].getName());
    }
    
    
    
    public static final double dRound(double m, int n)
    {
        double d = m;
        int  mult = (int)Math.pow(10, n);
        d *= mult;
        long long_d = Math.round(d);
        d = (double)long_d / (double)mult;
        return d;
    }
    
}

