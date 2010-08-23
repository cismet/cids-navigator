package Sirius.navigator.ui.widget;

/*******************************************************************************

Copyright (c)	:	EIG (Environmental Informatics Group)
http://www.enviromatics.net
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
Created			:	12.01.2001
History			:

*******************************************************************************/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

public class DynamicLongField extends JPanel implements AdjustmentListener
{
	protected int initialValue = 0;
	protected int changeValue = 1;

	// fuer die Multilingualitaet
	// protected String errorTitle = "Eingabefehler";
	// protected String errorString = "<html><p>Dieser Wert liegt ausserhalb</p><p>des gueltigen Bereichs!</p></html>";

	protected LongField longField;
	protected JScrollBar scrollBar;

	public DynamicLongField()
	{
		super(new GridBagLayout());

		longField = new LongField("0", 3);//NOI18N

		initDynamicLongField();
	}

	public DynamicLongField(long initialValue, int changeValue)
	{
		super(new GridBagLayout());

		this.changeValue = changeValue;
		longField = new LongField(String.valueOf(initialValue), 3);

		initDynamicLongField();
	}

	public DynamicLongField(long initialValue, int changeValue, int columns)
	{
		super(new GridBagLayout());

		this.changeValue = changeValue;
		longField = new LongField(String.valueOf(initialValue), columns);

		initDynamicLongField();
	}

	protected void initDynamicLongField()
	{
		scrollBar = new JScrollBar(JScrollBar.VERTICAL, 1, 0, 0, 2);
		scrollBar.setPreferredSize(new Dimension((int)scrollBar.getPreferredSize().getWidth(), (int)longField.getPreferredSize().getHeight()));
		scrollBar.addAdjustmentListener(this);

		GridBagConstraints gbc = new GridBagConstraints();

		gbc.anchor = GridBagConstraints.EAST;
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.gridy = 0;
		gbc.gridx = 0;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;

		this.add(longField, gbc);

		gbc.anchor = GridBagConstraints.WEST;
		//gbc.fill = GridBagConstraints.NONE;
		gbc.gridx++;
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;

		this.add(scrollBar, gbc);
	}

	public void setValue(long value)
	{
		try
		{
			longField.setText(String.valueOf(value));
		}
		catch(Exception e)
		{
			//if(Sirius.navigator.NavigatorLogger.DEV)e.printStackTrace();
			//JOptionPane.showMessageDialog(this, errorString, errorTitle, JOptionPane.ERROR_MESSAGE);

			longField.setText("0");//NOI18N
		}
	}

	public long getValue()
	{
		try
		{
			return Long.parseLong(longField.getText());
		}
		catch(Exception e)
		{
			//if(Sirius.navigator.NavigatorLogger.DEV)e.printStackTrace();
			//JOptionPane.showMessageDialog(this, errorString, errorTitle, JOptionPane.ERROR_MESSAGE);

			longField.setText("0");//NOI18N
			return 0;
		}
	}

	public void setChangeValue(int changeValue)
	{
		this.changeValue = changeValue;
	}

	public int getChangeValue()
	{
		return this.changeValue;
	}

	protected synchronized long incrementValue(int increment)
	{
		long inc = this.getValue();
		inc += increment;

		//NavigatorLogger.printMessage("<DLF> incremented: " + inc);

		this.setValue(inc);
		return inc;
	}

	protected synchronized long decrementValue(int decrement)
	{
		long dec = this.getValue();
		dec -= (dec >= decrement) ? decrement : 0;

		//NavigatorLogger.printMessage("<DLF> decremented: " + dec);

		this.setValue(dec);
		return dec;
	}

	public long incrementValue()
	{
		return incrementValue(changeValue);
	}

	public long decrementValue()
	{
		return decrementValue(changeValue);
	}

	public void adjustmentValueChanged(AdjustmentEvent e)
	{
		if(scrollBar.getValue() == 0)
		{
			//NavigatorLogger.printMessage("<DLF> increment: " + changeValue);
			incrementValue(changeValue);
		}
		if(scrollBar.getValue() == 2)
		{
			//NavigatorLogger.printMessage("<DLF> decrement: " + changeValue);
			decrementValue(changeValue);
		}

		// Reset the BoundedRangeModel
		scrollBar.setValue(1);
	}
}

/**
* Ein JTextField, dass nur die Eingabe von ganzen Zahlen erlaubt.
*/
class LongField extends JTextField
{
	public LongField()
	{
		super();
	}

	public LongField(int cols)
	{
		super(cols);
	}

	public LongField(String text, int cols)
	{
		super(text, cols);
    }

	protected Document createDefaultModel()
	{
		return new NumericDocument();
	}

	/**
	* Dieses PlainDocument nimmt nur Ganzzahlen.
	*/
	static class NumericDocument extends PlainDocument
	{
		protected final static String LONG = "0123456789";//NOI18N

		public void insertString(int offs, String str, AttributeSet a) throws BadLocationException
		{
			if (str == null)
			{
				remove(0, getLength());
				super.insertString(0, "0", a);//NOI18N
				return;
			}

			for (int i = 0; i < str.length(); i++)
			{
				if (LONG.indexOf(str.valueOf(str.charAt(i))) == -1)
				{
					remove(0, getLength());
					super.insertString(0, "0", a);//NOI18N
					return;
				}
			}

			super.insertString(offs, str, a);
		}
	}
}
