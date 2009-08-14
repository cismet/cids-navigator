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
 	Filename		:
	Version			:	1.0
 	Purpose			:
	Created			:	01.10.1999
	History			:

*******************************************************************************/
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
* LazyGlassPane ist ein GlassPane, das nicht auf Benutzereingaben reagiert.
* Es werden "dummy-Listener" hinzugefuegt, die keine Events verarbeiten.
*
* @see LazyPanel
*/

class LazyGlassPane extends JComponent
{
	private boolean eventsBlocked = false;
	private LazyMouseListener lazyMouseListener;
	private LazyKeyListener lazyKeyListener;
	//private LazyFocusListener lazyFocusListener;

	/**
	* Dieser Konstruktor erzeugt ein neues LazyGlassPane.
	*
	* @param blockEvents Bei true werden die Events sofort blockiert.
	*/
	public LazyGlassPane(boolean blockEvents)
	{
		super();
		lazyMouseListener = new LazyMouseListener();
		lazyKeyListener = new LazyKeyListener();
		//lazyFocusListener = new LazyFocusListener();
		this.blockEvents(blockEvents);
	}

	public void blockEvents(boolean blockEvents)
	{
		if(blockEvents && !eventsBlocked)
		{
			eventsBlocked = blockEvents;
			this.addMouseListener(lazyMouseListener);
			this.addKeyListener(lazyKeyListener);
			//this.addFocusListener(lazyFocusListener);
			this.setVisible(blockEvents);
			this.requestFocus();
		}
		else if(!blockEvents && eventsBlocked)
		{
			eventsBlocked = blockEvents;
			this.removeMouseListener(lazyMouseListener);
			this.removeKeyListener(lazyKeyListener);
			//this.removeFocusListener(lazyFocusListener);
			this.setVisible(blockEvents);
		}
	}

	
	/*public boolean hasFocus() 
	{
		return false;
	}
	
	public boolean isFocusCycleRoot()
	{
		return true;
	}*/
	
	//deprecated since 1.4:
	/*
	public boolean isFocusTraversable()
	{
		return false;
	}
	
	public boolean isManagingFocus()
	{
		return true;
	}*/
	
/*	public boolean isFocusable()
	{
		return false;
	}*/
	
	public boolean isFocusable()
	{
		return true;
	}
	
	public boolean hasFocus() 
	{
		return true;
	}
}



class LazyMouseListener implements MouseListener, MouseMotionListener
{
	public LazyMouseListener()
	{
		super();
	}

	public void mouseClicked(MouseEvent e) {}
	public void mouseDragged(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {}
}

class LazyKeyListener implements KeyListener
{
	public LazyKeyListener()
	{
		super();
	}

	public void keyPressed(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
}

