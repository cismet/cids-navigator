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
Created                 :       03.08.2000
History                 :

*******************************************************************************/
import java.util.Vector;

/**
 * Eine Klasse zum parsen bzw. zum parametrisieren von Strings
 *
 * @version  $Revision$, $Date$
 */
public class StringParameterizer {

    //~ Methods ----------------------------------------------------------------

    /**
     * Statische Methode zum parsen eines Strings.
     *
     * @param   string    Der zu parsende String
     * @param   tagOpen   markiert den Anfang eines tokens
     * @param   tagClose  tagOpen markiert das Ende eines tokens
     *
     * @return  Ein Array mit allen gefundenen Parametern
     */
    public static String[] parseString(final String string, final char tagOpen, final char tagClose) {
        int depth = 0;
        final Vector tokens = new Vector();
        StringBuffer currentToken = new StringBuffer();
        final StringBuffer buffer = new StringBuffer(string);

        for (int i = 0; i < buffer.length(); i++) {
            final char currentChar = buffer.charAt(i);

            if ((currentChar == tagOpen) && (++depth > 1)) {
                currentToken.append(currentChar);
            } else if ((currentChar == tagClose) && (--depth >= 1)) {
                currentToken.append(currentChar);
            } else if ((depth > 0) && (currentChar != tagOpen) && (currentChar != tagClose)) {
                currentToken.append(currentChar);
            }

            if ((depth == 0) && (currentToken.length() > 0)) {
                tokens.addElement(currentToken.toString());
                currentToken = new StringBuffer();
            }
        }

        return (String[])tokens.toArray(new String[tokens.size()]);
    }

    /**
     * Statische Methode zum parametrisieren eines Strings.<br>
     * Platzhalter werden durch die entsprechenden Werte ersetzt. Die Anzahl der Platzhalter muss gleich der Anzahl der
     * Werte sein, sonst liefert die Funktion null.
     *
     * @param   string  Der zu parametrisierende String.
     * @param   tokens  Die Platzhalter die ersetzt werden sollen.
     * @param   values  Die Werte, mit denen die Platzhalter ersetzt werden.
     *
     * @return  Der parametrisierte String, oder null.
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public static String parameterizeString(final String string, final String[] tokens, final String[] values)
            throws Exception {
        if ((string == null) || (tokens == null) || (values == null) || (tokens.length != values.length)) {
            throw new Exception("# of Tokens and Values is unequal"); // NOI18N
        }

        // Hier muss mit String und StringBuffer gearbeitet werden, da StringBuffer
        // keine Methode indexOf(...) und String kein Methode replace(...) hat. %o|

        String parameterizedString = string;
        final StringBuffer stringBuffer = new StringBuffer(string);
        int pos = 0;

        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i] == null) {
                throw new Exception("Token  is NULL"); // NOI18N
            }
            if (values[i] == null) {
                throw new Exception("Value is NULL");  // NOI18N
            }

            pos = parameterizedString.indexOf(tokens[i]);
            // NavigatorLogger.printMessage(pos + " : " + tokens[i] + " = " + values[i]);
            // -1 und +1 um die Delimeter zu entfernen
            stringBuffer.replace(pos - 1, pos + tokens[i].length() + 1, values[i]);
            parameterizedString = stringBuffer.toString();
        }

        return parameterizedString;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  args  DOCUMENT ME!
     */
    public static void main(final String[] args) {
//              try
//              {
////            String parseThis = "http://134.96.158.150:8080/sachdatenabfrage/abfrage1.pl?benutzer=$�$�&=$c.id�&objekt=$o.id�&attribut=$o_a_name.isba_nummer�";
////            System.out.println(parseThis);
////            String[] tokens = StringParameterizer.parseString(parseThis, '$' , '�' );
////            for(int i = 0; i < tokens.length; i++)
////                    System.out.println("Token " + i + ": " + tokens[i]);
////            //NavigatorLogger.printMessage(StringParameterizer.parameterizeString(parseThis, tokens, new String[] {"Penner", "unter der Bruecke", "666", "4711", "Assi"}));
////            }
////            catch (Exception e)
////            {
//                      e.printStackTrace();
//              }
    }
}
