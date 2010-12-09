/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.tools;
/**
 * // header - edit "Data/yourJavaHeader" to customize // contents - edit "EventHandlers/Java file/onCreate" to
 * customize //.
 *
 * @version  $Revision$, $Date$
 */
public final class SystemPropertyPrinter {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  args  DOCUMENT ME!
     */
    public static void main(final String[] args) {
        print();
    }

    /**
     * DOCUMENT ME!
     */
    public static void print() {
        System.out.println(getString());
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static String getString() {
        final StringBuffer buffer = new StringBuffer();
        final String[][] stringArray = getStringArray();

        for (int i = 0; i < stringArray.length; i++) {
            buffer.append(stringArray[i][0]);
            buffer.append(": \t"); // NOI18N
            buffer.append(stringArray[i][1]);
            buffer.append("\n");   // NOI18N
        }

        return buffer.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static String[][] getStringArray() {
        final String[][] pArray = new String[25][2];

        pArray[0][0] = "java.version"; // NOI18N
        pArray[0][1] = getProperty(pArray[0][0]);

        pArray[1][0] = "java.vendor"; // NOI18N
        pArray[1][1] = getProperty(pArray[1][0]);

        pArray[2][0] = "java.vendor.url"; // NOI18N
        pArray[2][1] = getProperty(pArray[2][0]);

        pArray[3][0] = "java.home"; // NOI18N
        pArray[3][1] = getProperty(pArray[3][0]);

        pArray[4][0] = "java.vm.specification.version"; // NOI18N
        pArray[4][1] = getProperty(pArray[4][0]);

        pArray[5][0] = "java.vm.specification.vendor"; // NOI18N
        pArray[5][1] = getProperty(pArray[5][0]);

        pArray[6][0] = "java.vm.specification.name"; // NOI18N
        pArray[6][1] = getProperty(pArray[6][0]);

        pArray[7][0] = "java.vm.version"; // NOI18N
        pArray[7][1] = getProperty(pArray[7][0]);

        pArray[8][0] = "java.vm.vender"; // NOI18N
        pArray[8][1] = getProperty(pArray[8][0]);

        pArray[9][0] = "java.vm.name"; // NOI18N
        pArray[9][1] = getProperty(pArray[9][0]);

        pArray[10][0] = "java.specification.version"; // NOI18N
        pArray[10][1] = getProperty(pArray[10][0]);

        pArray[11][0] = "java.specification.vendor"; // NOI18N
        pArray[11][1] = getProperty(pArray[11][0]);

        pArray[12][0] = "java.specification.name"; // NOI18N
        pArray[12][1] = getProperty(pArray[12][0]);

        pArray[13][0] = "java.class.version"; // NOI18N
        pArray[13][1] = getProperty(pArray[13][0]);

        pArray[14][0] = "java.class.path"; // NOI18N
        pArray[14][1] = getProperty(pArray[14][0]);

        pArray[15][0] = "java.ext.dirs"; // NOI18N
        pArray[15][1] = getProperty(pArray[15][0]);

        pArray[16][0] = "os.name"; // NOI18N
        pArray[16][1] = getProperty(pArray[16][0]);

        pArray[17][0] = "os.arch"; // NOI18N
        pArray[17][1] = getProperty(pArray[17][0]);

        pArray[18][0] = "os.version"; // NOI18N
        pArray[18][1] = getProperty(pArray[18][0]);

        pArray[19][0] = "file.separator"; // NOI18N
        pArray[19][1] = getProperty(pArray[19][0]);

        pArray[20][0] = "path.separator"; // NOI18N
        pArray[20][1] = getProperty(pArray[20][0]);

        pArray[21][0] = "line.separator"; // NOI18N
        pArray[21][1] = getProperty(pArray[21][0]);

        pArray[22][0] = "user.name"; // NOI18N
        pArray[22][1] = getProperty(pArray[22][0]);

        pArray[23][0] = "user.home"; // NOI18N
        pArray[23][1] = getProperty(pArray[23][0]);

        pArray[24][0] = "user.dir"; // NOI18N
        pArray[24][1] = getProperty(pArray[24][0]);

        return pArray;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   property  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private static String getProperty(final String property) {
        try {
            return System.getProperty(property, "null"); // NOI18N
        } catch (SecurityException se) {
            return "Access denied";                      // NOI18N
        } catch (Exception e) {
            return "null";                               // NOI18N
        }
    }
}
