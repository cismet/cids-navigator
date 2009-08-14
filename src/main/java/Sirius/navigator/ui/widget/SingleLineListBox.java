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
import javax.swing.*;
import javax.swing.event.*;

/**
 * Eine in eine JScrollPane eingebettete JListbox in der nur ein Element
 * angezeigt wird.<br>
 * Duch das Klicken auf die < und > Buttons der ScrollPane wird der
 * Wert der Listbox dekrementiert bzw. inkrementiert.
 *
 * @deprecated use javax.swing.JSpinner instead.
 */
public class SingleLineListBox extends JScrollPane implements ChangeListener
{
	protected int lastScrollBarPosition = 0;
	protected int	selectedIndex = 0;
	protected int visibleIndex = 0;
	
	protected JList listBox;
	protected JScrollBar thisScrollBar;
	protected BoundedRangeModel sourceScroll;
	
	protected boolean initVisibleCalled = false;
	protected boolean synchronisation = false;
	protected boolean enabled = true;
	
	

	/**
	* Konstruiert eine neue SingleLineListBox die mit einem Integer-Array
	* gefuellt wird. Das erste sichtbare Element hat den Index 0.
	*
	* @param value Das Interger-Array mit dem die ListBox gefuellt wird.
	*/
	public SingleLineListBox(Integer[] value)
	{
		listBox = new JList(value);
		listBox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listBox.setVisibleRowCount(1);
		this.setViewportView(listBox);
		//listBox.setFixedCellHeight(23);
		listBox.setSelectedIndex(listBox.getFirstVisibleIndex());
		//listBox.addListSelectionListener(this);

		initSingleLineListBox();
	}

	/**
	* Konstruiert eine neue SingleLineListBox die mit einem Integer-Array
	* gefuellt wird. Der Index des ersten sichtbaren Elements kann
	* angegeben werden.
	*
	* @param value Das Interger-Array mit dem die ListBox gefuellt wird.
	* @param visibleIndex Der erste sichtbare Wert.
	*/
	public SingleLineListBox(Integer[] value, int visibleIndex)
	{
		listBox = new JList(value);
		listBox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listBox.setVisibleRowCount(1);
		this.visibleIndex = visibleIndex;
		this.setViewportView(listBox);
		
		initSingleLineListBox();
	}
	
	public SingleLineListBox(int firstValue, int lastValue, int increment)
	{
		Integer[] value = new Integer[((lastValue-firstValue)/increment)+1];
		
		for (int i = 0; i < value.length; i++)
		{
			value[i] = new Integer(lastValue);
			lastValue-=increment;
		}
		
		listBox = new JList(value);
		listBox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listBox.setVisibleRowCount(1);
		this.setViewportView(listBox);
		listBox.setSelectedIndex(listBox.getFirstVisibleIndex());

		initSingleLineListBox();
	}
	
	public SingleLineListBox(int firstValue, int lastValue, int increment, int visibleIndex)
	{
		Integer[] value = new Integer[((lastValue-firstValue)/increment)+1];

		for (int i = 0; i < value.length; i++)
		{
			value[i] = new Integer(lastValue);
			lastValue-=increment;
		}
		
		listBox = new JList(value);
		listBox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listBox.setVisibleRowCount(1);
		this.visibleIndex = visibleIndex;
		this.setViewportView(listBox);

		initSingleLineListBox();
	}

	/**
	* Initialisierungsmethode<br>
	* Wird nur von den Konstruktoren aufgerufen.
	*/
	protected void initSingleLineListBox()
	{
		listBox.setSelectionBackground(listBox.getBackground());
	    listBox.setSelectionForeground(listBox.getForeground());
		selectedIndex = listBox.getSelectedIndex();
		thisScrollBar = this.getVerticalScrollBar();
		lastScrollBarPosition = thisScrollBar.getModel().getValue();
	}

	/**
	* Initialisierungsmethode<br>
	* Wird nur einmal aufgerufen, nachdem die Komponente sichtbar gemacht wurde.
	*/
	protected synchronized void initVisible()
	{		
		if((!initVisibleCalled) && (this.getParent() != null))
		{		
			this.initVisibleCalled = true;
			this.setVisibleIndex(visibleIndex);	
			setEnabled(enabled);
			sourceScroll = thisScrollBar.getModel();
			sourceScroll.addChangeListener(this);
		}
	}

	public JList getListBox()
	{
		return listBox;
	}

	public int getSelectedIndex()
	{
		return listBox.getSelectedIndex();
	}

	public java.lang.Object getSelectedValue()
	{
		return listBox.getSelectedValue();
	}
	
	public int getSelectedIntValue()
	{
		return ((Integer)listBox.getSelectedValue()).intValue();
	}

	/**
	* Setzt das sichtbare Element der ListBox und waehlt es aus.
	*
	* @visibleIndex Der Index des sichtbaren Elements.
	*/
	public synchronized void setVisibleIndex(int visibleIndex)
	{
		synchronisation = true;
		
		// damit es keine java.lang.ArrayIndexOutOfBoundsException gibt
		if (visibleIndex <= (listBox.getModel().getSize() - 1) && visibleIndex >= 0)
		{
			this.selectedIndex = visibleIndex;
			listBox.setSelectedIndex(selectedIndex);
			this.validate();
			listBox.ensureIndexIsVisible(selectedIndex);
			lastScrollBarPosition = thisScrollBar.getModel().getValue();
			//NavigatorLogger.printMessage("lastScrollBarPosition: " + lastScrollBarPosition);

			if(listBox.getSelectedIndex() != listBox.getFirstVisibleIndex())
			{
				//NavigatorLogger.printMessage("setVisibleIndex(): " + listBox.getSelectedIndex() + " != " + listBox.getFirstVisibleIndex());
				//selectedIndex = listBox.getFirstVisibleIndex();
				listBox.ensureIndexIsVisible(selectedIndex);
				listBox.setSelectedIndex(selectedIndex);
			}
		}
		else
			listBox.setSelectedIndex(listBox.getFirstVisibleIndex());
		
		//this.validate();
		
		//NavigatorLogger.printMessage("synchronisation: " + synchronisation);
		//NavigatorLogger.printMessage("initVisibleCalled: " + initVisibleCalled);
		//NavigatorLogger.printMessage("============================================");
		//NavigatorLogger.printMessage("visibleIndex: " + visibleIndex);
		//NavigatorLogger.printMessage("SelectedIndex: " + listBox.getSelectedIndex());
		//NavigatorLogger.printMessage("SelectedValue: " + listBox.getSelectedValue());
		//NavigatorLogger.printMessage("============================================");

		synchronisation = false;
	}
	
	public void setSelectedValue(int value)
	{
		synchronisation = true;
		listBox.setSelectedValue(new Integer(value), true);
		selectedIndex = listBox.getFirstVisibleIndex();
		lastScrollBarPosition = thisScrollBar.getModel().getValue();
		//NavigatorLogger.printMessage("setSelectedValue: " + value);
		//NavigatorLogger.printMessage("setSelectedValue selectedIndex: " + selectedIndex);
		//NavigatorLogger.printMessage("setSelectedValue lastScrollBarPosition: " + lastScrollBarPosition);
		synchronisation = false;
	}
	
	public synchronized void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
		
		if(isVisible() && initVisibleCalled)
		{
			super.setEnabled(enabled);
			listBox.setEnabled(enabled);
			thisScrollBar.setEnabled(enabled);
		}
	}
	
	public void addListSelectionListener(ListSelectionListener li)
	{
		listBox.addListSelectionListener(li);
	}
	
	
	/**
	* Musste ueberschrieben werden, da die Hoehe der SingleLineListBox
	* immer gleich bleiben muss!
	*/
	
	public Dimension getMaximumSize()
	{
		return new Dimension((int)super.getMaximumSize().getWidth(), (int)getPreferredSize().getHeight());
	}

	
	public void paintComponents(Graphics g)
	{
		super.paintComponents(g);
		initVisible();
	}

	public void paintAll(Graphics g)
	{
		super.paintAll(g);
		initVisible();
	}

	public void repaint()
	{
		super.repaint();
		initVisible();
	}

	public void repaint(long l)
	{
		super.repaint(l);
		initVisible();
	}

	public void repaint(int i1, int i2, int i3, int i4)
	{
		super.repaint(i1, i2, i3, i4);
		initVisible();
	}

	public void repaint(long l, int i1, int i2, int i3, int i4)
	{
		super.repaint(l, i1, i2, i3, i4);
		initVisible();
	}

	public void update(Graphics g)
	{
		super.update(g);
		initVisible();
	}
	
	

	// CHANGE LISTENER =====================================================/**/
	
	public synchronized void stateChanged(ChangeEvent ce)
	{
		// Wenn ein Element mit setVisibleIndex(int visibleIndex) ausgewaehlt wird
		// darf der ChangeListener nicht gleichzeitig den aktuellen Index veraendern
		// Deshalb ist manuelle Synchronisation notwendig.
		if (!synchronisation && initVisibleCalled)
		{
			synchronisation = true;
			BoundedRangeModel sourceScroll = (BoundedRangeModel)ce.getSource();
			int scrollBarPosition = sourceScroll.getValue();

			//NavigatorLogger.printMessage("stateChanged: lastScrollBarPosition: " + lastScrollBarPosition);
			//NavigatorLogger.printMessage("stateChanged: scrollBarPosition: " + scrollBarPosition);
			
			if ((lastScrollBarPosition - scrollBarPosition) < 0)
				selectedIndex++;
			else if  ((lastScrollBarPosition - scrollBarPosition) > 0)
				selectedIndex--;
		
			// Falls irgendetwas schiefgeht, werden die beiden Indizes synchronisiert
			// Duerfte theoretisch nicht passieren ;o)
			if(listBox.getSelectedIndex() != listBox.getFirstVisibleIndex())
			{
				//NavigatorLogger.printMessage("stateChanged Fehler: FirstVisibleIndex: " + listBox.getFirstVisibleIndex());
				//NavigatorLogger.printMessage("stateChanged Fehler: SelectedIndex: " + listBox.getSelectedIndex());
				//NavigatorLogger.printMessage("stateChanged Fehler: SelectedValue: " + listBox.getSelectedValue());
				//NavigatorLogger.printMessage("==================================================================");
				selectedIndex = listBox.getFirstVisibleIndex();
				listBox.setSelectedIndex(selectedIndex);
				listBox.ensureIndexIsVisible(selectedIndex);
				scrollBarPosition = sourceScroll.getValue();
				//NavigatorLogger.printMessage("stateChanged Fehler: FirstVisibleIndex: " + listBox.getFirstVisibleIndex());
				//NavigatorLogger.printMessage("stateChanged Fehler: SelectedIndex: " + listBox.getSelectedIndex());
				//NavigatorLogger.printMessage("stateChanged Fehler: SelectedValue: " + listBox.getSelectedValue());
			}
			else
			{
				listBox.setSelectedIndex(selectedIndex);
				listBox.ensureIndexIsVisible(selectedIndex);
				//NavigatorLogger.printMessage("stateChanged: FirstVisibleIndex: " + listBox.getFirstVisibleIndex());
				//NavigatorLogger.printMessage("stateChanged: SelectedIndex: " + listBox.getSelectedIndex());
				//NavigatorLogger.printMessage("stateChanged: SelectedValue: " + listBox.getSelectedValue());
			}
			
			lastScrollBarPosition = scrollBarPosition;
			synchronisation = false;
		}			
	}
}

