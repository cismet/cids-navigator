/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.tools;

/*******************************************************************************

        Copyright (c)   :       EIG (Environmental Informatics Group)
                                                http://www.htw-saarland.de/eig
                                                Prof. Dr. Reiner Guettler
                                                Prof. Dr. Ralf Denzer

                                                HTWdS
                                                Hochschule fuer Technik und Wirtschaft des Saarlandes
                                                Goebenstr. 40
                                                66117 Saarbruecken
                                                Germany

        Programmers             :

        Project                 :       WuNDA 2
        Filename                :
        Version                 :       1.0
        Purpose                 :
        Created                 :       26.06.2000
        History                 :       AdvancedSwing.Chapter1

*******************************************************************************/

import javax.swing.text.*;

/**
 * Eine Dokumentenklasse, die nur bestimmte Eingaben ein eine Textkomponente zulaesst.<br>
 * . Beispiel:<br>
 * JTextField tf1b = new JTextField(10);<br>
 * tf1b.setDocument(new InputValidator(InputValidator.NUMERIC));
 *
 * @version  $Revision$, $Date$
 */
public class InputValidator extends PlainDocument {

    //~ Static fields/initializers ---------------------------------------------

    public static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz\u00E4\u00F6\u00FC\u00DF"; // NOI18N
    public static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ\u00C4\u00D6\u00DC";       // NOI18N
    public static final String ALPHA = LOWERCASE + UPPERCASE;
    public static final String STREET = LOWERCASE + UPPERCASE + '.';
    public static final String NUMERIC = "0123456789";                                           // NOI18N
    public static final String FLOAT = NUMERIC + '.';
    public static final String COORDINATE = NUMERIC + ',';
    public static final String ALPHA_NUMERIC = ALPHA + NUMERIC;

    //~ Instance fields --------------------------------------------------------

    protected int coordinateCount = 3;
    protected int validatedCoordinateCount = 0;
    protected String acceptedChars = null;
    protected boolean negativeAccepted = false;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new InputValidator object.
     */
    public InputValidator() {
        this(ALPHA_NUMERIC);
    }

    /**
     * Creates a new InputValidator object.
     *
     * @param  acceptedchars  DOCUMENT ME!
     */
    public InputValidator(final String acceptedchars) {
        acceptedChars = acceptedchars;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  negativeaccepted  DOCUMENT ME!
     */
    public void setNegativeAccepted(final boolean negativeaccepted) {
        if (acceptedChars.equals(NUMERIC) || acceptedChars.equals(FLOAT) || acceptedChars.equals(ALPHA_NUMERIC)) {
            negativeAccepted = negativeaccepted;
            acceptedChars += "-"; // NOI18N
        }
    }

    @Override
    public void insertString(final int offset, String str, final AttributeSet attr) throws BadLocationException {
        if (str == null) {
            return;
        }

        final Content content = getContent();
        final int length = content.length();
        final int originalLength = length;
        final String targetString = content.getString(0, offset) + str + content.getString(offset, length - offset - 1);

        if (acceptedChars.equals(UPPERCASE)) {
            str = str.toUpperCase();
        } else if (acceptedChars.equals(LOWERCASE)) {
            str = str.toLowerCase();
        }

        for (int i = 0; i < str.length(); i++) {
            if (acceptedChars.indexOf(str.valueOf(str.charAt(i))) == -1) {
                return;
            }
        }

        if (acceptedChars.equals(FLOAT) || (acceptedChars.equals(FLOAT + "-") && negativeAccepted)) // NOI18N
        {
            if (str.indexOf(".") != -1)                                                             // NOI18N
            {
                if (getLength() == 0) {
                    return;
                }

                if (getText(0, getLength()).indexOf(".") != -1) { // NOI18N
                    return;
                }
            }
        }

        if (acceptedChars.equals(COORDINATE)) {
            if (str.indexOf(",") != -1) // NOI18N
            {
                if (getLength() == 0) {
                    return;
                }

                final char[] charArray = targetString.toCharArray();
                validatedCoordinateCount = 0;

                for (int i = 1; i < (charArray.length - 1); i++) {
                    if (charArray[i] == ',') {
                        if ((charArray[i - 1] == ',') || (charArray[i + 1] == ',')) {
                            return;
                        }

                        validatedCoordinateCount++;
                    }

                    if (validatedCoordinateCount >= coordinateCount) {
                        return;
                    }
                }
            }
        }

        if (negativeAccepted && (str.indexOf("-") != -1))   // NOI18N
        {
            if ((str.indexOf("-") != 0) || (offset != 0)) { // NOI18N
                return;
            }
        }

        super.insertString(offset, str, attr);
    }
}
