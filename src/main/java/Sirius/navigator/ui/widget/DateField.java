/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui.widget;

/*******************************************************************************
 *
 * Copyright (c)        :       EIG (Environmental Informatics Group)
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
 * Programmers          :       Thorsten Hell
 * Pascal
 * Project                      :       WuNDA 2
 * Version                      :       2.0
 * Purpose                      :
 * Created                      :       27.05.1999
 * History                      :
 *
 *******************************************************************************/

import Sirius.navigator.*;

import java.awt.Toolkit;
import java.awt.event.*;

import java.text.*;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.*;
import javax.swing.text.*;

/**
 * Dies ist ein Intfeld das nur Zahleneingabe zulaesst.<BR>
 * Der Code wurde teilweise aus dem Onlinetutorial von SUN uebernommen.<BR>
 *
 * @author   Thorsten Hell
 * @version  1.0 erstellt am 27.05.1999
 * @since    letzte Aenderung am 01.03.2000
 */

public class DateField extends JTextField implements FocusListener {

    //~ Instance fields --------------------------------------------------------

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private Toolkit toolkit;
    // private NumberFormat integerFormatter;
    private boolean checked = false;
    private DecimalFormat integerFormatter;
    private int maxLength;
    private boolean bringFocus2Next = false;
    private DateField nextField;

    //~ Constructors -----------------------------------------------------------

    /**
     * Dem Konstruktor wird ein Formatstring uebergeben. Aus diesem Formatstring liesst das Textfeld heraus wieviele
     * Ziffern maximal eingegeben werden koennen, und wie gross die Anzeige sein soll.
     *
     * @param  formatString  DOCUMENT ME!
     */
    public DateField(final String formatString) {
        super(formatString.length());
        bringFocus2Next = false;
        maxLength = formatString.length();
        integerFormatter = new DecimalFormat(formatString);
        toolkit = Toolkit.getDefaultToolkit();
        // integerFormatter = NumberFormat.getNumberInstance();//Locale.US);
        integerFormatter.setParseIntegerOnly(true);
        addFocusListener(this);
    }

    /**
     * Creates a new DateField object.
     *
     * @param  value         DOCUMENT ME!
     * @param  formatString  DOCUMENT ME!
     */
    public DateField(final int value, final String formatString) {
        super(formatString.length());
        bringFocus2Next = false;
        maxLength = formatString.length();
        integerFormatter = new DecimalFormat(formatString);
        toolkit = Toolkit.getDefaultToolkit();
        // integerFormatter = NumberFormat.getNumberInstance();//Locale.US);
        integerFormatter.setParseIntegerOnly(true);
        setValue(value);
        addFocusListener(this);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Mit dieser methode kann man ein IntFeld angeben, zu dem der Focus gehen soll, wenn die entsprechende Anzahl von
     * Ziffern erreicht worden ist.
     *
     * @param  nf  DOCUMENT ME!
     */
    public void setNextField(final DateField nf) {
        bringFocus2Next = true;
        nextField = nf;
    }
    /**
     * Mit dieser Methode kann der int-Wert des Feldes bestimmt werden.
     *
     * @return  DOCUMENT ME!
     */
    public int getValue() {
        int retVal = 0;
        try {
            retVal = integerFormatter.parse(getText()).intValue();
        } catch (ParseException e) {
            // This should never happen because insertString allows
            // only properly formatted data to get in the field.
            toolkit.beep();
        }
        return retVal;
    }
    /**
     * Mit dieser Methode wird der Wert des Feldes mittels eines Integers gesetzt.
     *
     * @param  value  DOCUMENT ME!
     */
    public void setValue(final int value) {
        setText(integerFormatter.format(value));
    }
    /**
     * Diese Methode liefert zurueck ob sich das Feld seiner pruefung unterzogen hat.
     *
     * @return  DOCUMENT ME!
     */
    public boolean checked() {
        return checked;
    }
    /**
     * Diese Methode prueft das Feld.
     */
    public void check() {
        final boolean tmp = bringFocus2Next;
        bringFocus2Next = false;
        setValue(getValue());
        checked = true;
        bringFocus2Next = tmp;
    }

    /*
     * diese Methode wird aufgerufen wenn das Feld den Focus erhaelt
     */
    @Override
    public void focusGained(final FocusEvent e) {
        return;
    }

    /*
     * diese Methode wird aufgerufen wenn das Feld den Focus verliert
     */
    @Override
    public void focusLost(final FocusEvent e) {
        if (e.isTemporary()) {
            return;
        } else {
            if (!checked()) {
                check();
            }
        }
    }

    /*
     * diese methode liefert ein neues WholeNumberDocument.
     */
    @Override
    protected Document createDefaultModel() {
        return new WholeNumberDocument();
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * diese lokale Klasse braucht man, um die Eingaben in das Feld zu ueberpruefen.
     *
     * @version  $Revision$, $Date$
     */
    protected class WholeNumberDocument extends PlainDocument {

        //~ Methods ------------------------------------------------------------

        @Override
        public void remove(final int offs, final int len) throws BadLocationException {
            checked = false;
            super.remove(offs, len);
        }

        @Override
        public void insertString(final int offs, final String str, final AttributeSet a) throws BadLocationException {
            // NavigatorLogger.printMessage("Offset:"+offs+" STr:"+str+"L:"+getLength()+"attr:"+a);

            if ((getLength() + str.length()) <= maxLength) {
                final char[] source = str.toCharArray();
                final char[] result = new char[source.length];
                int j = 0;

                for (int i = 0; i < result.length; i++) {
                    if (Character.isDigit(source[i])) {
                        result[j++] = source[i];
                    } else {
                        toolkit.beep();
                        if (log.isDebugEnabled()) {
                            log.debug("insertString: " + source[i]); // NOI18N
                        }
                    }
                }
                super.insertString(offs, new String(result, 0, j), a);
                checked = false;
            } else {
                toolkit.beep();
            }
            if ((getLength()) == maxLength) {                        // getLength() ist schon aktualisiert
                if (bringFocus2Next == true) {
                    checked = true;
                    nextField.requestFocus();
                }
                // NavigatorLogger.printMessage("Sprung");
                // NavigatorLogger.printMessage(nextField);
            }
        }
    }
}
