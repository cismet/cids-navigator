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
 * Programmers		:	Thorsten Hell
 * Pascal
 * Project			:	WuNDA 2
 * Version			:	2.0
 * Purpose			:
 * Created			:	27.05.1999
 * History			:
 *
 *******************************************************************************/

import Sirius.navigator.*;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.Toolkit;
import java.text.NumberFormat;
import java.text.*;
import java.text.ParseException;
import java.awt.event.*;


/**
 * Dies ist ein Intfeld das nur Zahleneingabe zulaesst.<BR>
 * Der Code wurde teilweise aus dem Onlinetutorial von SUN uebernommen.<BR>
 *
 * @version   1.0  erstellt am 27.05.1999
 * @since     letzte Aenderung am 01.03.2000
 * @author    Thorsten Hell
 *
 */

public class DateField extends JTextField implements FocusListener
{
    private Toolkit toolkit;
    //private NumberFormat integerFormatter;
    private boolean checked=false;
    private DecimalFormat integerFormatter;
    private int maxLength;
    private boolean bringFocus2Next=false;
    private DateField nextField;
    
    public DateField(int value, String formatString)
    {
        super(formatString.length());
        bringFocus2Next=false;
        maxLength=formatString.length();
        integerFormatter = new DecimalFormat(formatString);
        toolkit = Toolkit.getDefaultToolkit();
        //        integerFormatter = NumberFormat.getNumberInstance();//Locale.US);
        integerFormatter.setParseIntegerOnly(true);
        setValue(value);
        addFocusListener(this);
    }
    
        /*
         * Dem Konstruktor wird ein Formatstring uebergeben. Aus diesem Formatstring liesst das Textfeld heraus wieviele Ziffern maximal eingegeben werden koennen, und wie gross die Anzeige sein soll.
         */
    public DateField(String formatString)
    {
        super(formatString.length());
        bringFocus2Next=false;
        maxLength=formatString.length();
        integerFormatter = new DecimalFormat(formatString);
        toolkit = Toolkit.getDefaultToolkit();
        //        integerFormatter = NumberFormat.getNumberInstance();//Locale.US);
        integerFormatter.setParseIntegerOnly(true);
        addFocusListener(this);
    }
        /*
         * Mit dieser methode kann man ein IntFeld angeben, zu dem der Focus gehen soll, wenn die entsprechende Anzahl von Ziffern erreicht worden ist.
         */
    public void setNextField(DateField nf)
    {
        bringFocus2Next=true;
        nextField=nf;
    }
    
        /*
         * Mit dieser Methode kann der int-Wert des Feldes bestimmt werden.
         */
    public int getValue()
    {
        int retVal = 0;
        try
        {
            retVal = integerFormatter.parse(getText()).intValue();
        } catch (ParseException e)
        {
            // This should never happen because insertString allows
            // only properly formatted data to get in the field.
            toolkit.beep();
        }
        return retVal;
    }
    
        /*
         * Mit dieser Methode wird der Wert des Feldes mittels eines Integers gesetzt.
         */
    public void setValue(int value)
    {
        setText(integerFormatter.format(value));
    }
    
        /*
         * Diese Methode liefert zurueck ob sich das Feld seiner pruefung unterzogen hat.
         */
    public boolean checked()
    {
        return checked;
    }
    
        /*
         * Diese Methode prueft das Feld.
         */
    public void check()
    {
        boolean tmp= bringFocus2Next;
        bringFocus2Next=false;
        setValue(getValue());
        checked=true;
        bringFocus2Next=tmp;
    }
    
    
        /*
         * diese Methode wird aufgerufen wenn das Feld den Focus erhaelt
         */
    public void focusGained(FocusEvent e)
    {
        return;
    }
    
        /*
         * diese Methode wird aufgerufen wenn das Feld den Focus verliert
         */
    public void focusLost(FocusEvent e)
    {
        if (e.isTemporary())
        {
            return;
        }
        else
        {
            if (!checked())
            {
                check();
            }
        }
    }
    
    
    
        /*
         * diese methode liefert ein neues WholeNumberDocument.
         */
    protected Document createDefaultModel()
    {
        return new WholeNumberDocument();
    }
    
    /*
     * diese lokale Klasse braucht man, um die Eingaben in das Feld zu ueberpruefen.
     */
    protected class WholeNumberDocument extends PlainDocument
    {
        public void remove(int offs,int len) throws BadLocationException
        {
            checked=false;
            super.remove(offs,len);
        }
        
        public void insertString(int offs,
        String str,
        AttributeSet a)
        throws BadLocationException
        {
            //NavigatorLogger.printMessage("Offset:"+offs+" STr:"+str+"L:"+getLength()+"attr:"+a);
            
            
            if ((getLength()+str.length())<=maxLength)
            {
                char[] source = str.toCharArray();
                char[] result = new char[source.length];
                int j = 0;
                
                for (int i = 0; i < result.length; i++)
                {
                    if (Character.isDigit(source[i]))
                        result[j++] = source[i];
                    else
                    {
                        toolkit.beep();
                        System.err.println("insertString: " + source[i]);
                    }
                }
                super.insertString(offs, new String(result, 0, j), a);
                checked=false;
            }
            else
            {
                toolkit.beep();
            }
            if ((getLength())==maxLength)
            { // getLength() ist schon aktualisiert
                if 	(bringFocus2Next==true)
                {
                    checked=true;
                    nextField.requestFocus();
                }
                //NavigatorLogger.printMessage("Sprung");
                //NavigatorLogger.printMessage(nextField);
                
            }
        }
    }
}


