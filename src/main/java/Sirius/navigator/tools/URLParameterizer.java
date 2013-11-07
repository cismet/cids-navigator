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

Programmers             :       Pascal

Project                 :       WuNDA 2
Filename                :
Version                 :       1.0
Purpose                 :
Created                 :       03.08.2000
History                 :       10.04.2001 / ATTRIBUTE_NAME

*******************************************************************************/
import Sirius.server.middleware.types.*;
import Sirius.server.newuser.*;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class URLParameterizer extends StringParameterizer {

    //~ Static fields/initializers ---------------------------------------------

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(URLParameterizer.class);
    public static char OPEN_TAG = '$';  // NOI18N
    public static char CLOSE_TAG = '§'; // NOI18N
    public static char ARRAY_SEPARATOR = ';';

    public static String CLASS_ID = "c.id";                  // NOI18N
    public static String CLASS_NAME = "c.name";              // NOI18N
    public static String CLASS_ATTRIBUTE_ID = "c_a.";        // NOI18N
    public static String CLASS_ATTRIBUTE_NAME = "c_a_name."; // NOI18N

    public static String OBJECT_ID = "o.id";                  // NOI18N
    public static String OBJECT_NAME = "o.name";              // NOI18N
    public static String OBJECT_ATTRIBUTE_ID = "o_a.";        // NOI18N
    public static String OBJECT_ATTRIBUTE_NAME = "o_a_name."; // NOI18N

    public static String USER_ID = "u.id";           // NOI18N
    public static String USER_NAME = "u.name";       // NOI18N
    public static String USER_LS_NAME = "u.ls_name"; // NOI18N
    public static String USER_ORDER_NUM = "u.order"; // NOI18N

    public static String USERGROUP_ID = "g.id";           // NOI18N
    public static String USERGROUP_NAME = "g.name";       // NOI18N
    public static String USERGROUP_LS_NAME = "g.ls_name"; // NOI18N

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   url          DOCUMENT ME!
     * @param   classArray   DOCUMENT ME!
     * @param   objectArray  DOCUMENT ME!
     * @param   user         DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public static String parameterizeURL(final String url,
            final MetaClass[] classArray,
            final MetaObject[] objectArray,
            final User user) throws Exception {
        return parameterizeString(url, getTokens(url), getValues(url, classArray, objectArray, user));
    }

    /**
     * DOCUMENT ME!
     *
     * @param   url   DOCUMENT ME!
     * @param   c     DOCUMENT ME!
     * @param   o     DOCUMENT ME!
     * @param   user  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public static String parameterizeURL(final String url, final MetaClass c, final MetaObject o, final User user)
            throws Exception {
        MetaClass[] _c = { c };

        if (c == null) {
            _c = null;
        }

        MetaObject[] _o = { o };

        if (o == null) {
            _o = null;
        }

        return parameterizeURL(url, _c, _o, user);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   url  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static String[] getTokens(final String url) {
        return parseString(url, OPEN_TAG, CLOSE_TAG);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   url          DOCUMENT ME!
     * @param   classArray   DOCUMENT ME!
     * @param   objectArray  DOCUMENT ME!
     * @param   user         DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public static String[] getValues(final String url,
            final MetaClass[] classArray,
            final MetaObject[] objectArray,
            final User user) throws Exception {
        final String[] tokens = getTokens(url);
        final String[] values = new String[tokens.length];
        final Sirius.server.localserver.attribute.Attribute[] attributeArray = null;
        final UserGroup userGroup;

        if (user != null) {
            UserGroup tmpUserGroup = user.getUserGroup();
            if (tmpUserGroup == null) {
                for (final UserGroup potentialUserGroup : user.getPotentialUserGroups()) {
                    tmpUserGroup = potentialUserGroup;
                    break;
                }
            }
            userGroup = tmpUserGroup;
        } else {
            userGroup = null;
        }

        for (int i = 0; i < tokens.length; i++) {
            // CLASS ...

            if ((classArray != null) && (classArray.length > 0)
                        && (tokens[i].equals(CLASS_ID) || tokens[i].equals(CLASS_NAME)
                            || (tokens[i].indexOf(CLASS_ATTRIBUTE_ID) != -1)
                            || ((tokens[i].indexOf(CLASS_ATTRIBUTE_NAME) != -1) && (classArray != null)
                                && (classArray.length > 0)))) {
                String value = null;

                for (int j = 0; j < classArray.length; j++) {
                    value = getClassValue(classArray[j], tokens[i]);

                    if (value != null) {
                        if (values[i] != null) {
                            values[i] += (ARRAY_SEPARATOR + value);
                        } else {
                            values[i] = value;
                        }
                    }
                }
            }

            // OBJECT ...
            else if ((objectArray != null) && (objectArray.length > 0)
                        && (tokens[i].equals(OBJECT_ID) || tokens[i].equals(OBJECT_NAME)
                            || (tokens[i].indexOf(OBJECT_ATTRIBUTE_ID) != -1)
                            || ((tokens[i].indexOf(OBJECT_ATTRIBUTE_NAME) != -1) && (objectArray != null)
                                && (objectArray.length > 0)))) {
                String value = null;

                for (int j = 0; j < objectArray.length; j++) {
                    value = getObjectValue(objectArray[j], tokens[i]);
                    // NavigatorLogger.printMessage(j + ": " + value);

                    if (value != null) {
                        if (values[i] != null) {
                            values[i] += (ARRAY_SEPARATOR + value);
                        } else {
                            values[i] = value;
                        }
                    }
                }
            }
            // USER ...
            else if ((user != null) && tokens[i].equals(USER_ID)) {
                values[i] = String.valueOf(user.getId());
            } else if ((user != null) && tokens[i].equals(USER_NAME)) {
                values[i] = user.getName();
            } else if ((user != null) && tokens[i].equals(USER_LS_NAME)) {
                values[i] = user.getDomain();
            } else if ((user != null) && tokens[i].equals(USER_ORDER_NUM)) {
                ; // values[i] = String.valueOf(user.getOder());
            }
            // USERGROUP ...
            else if ((userGroup != null) && tokens[i].equals(USERGROUP_ID)) {
                values[i] = String.valueOf(userGroup.getId());
            } else if ((userGroup != null) && tokens[i].equals(USERGROUP_NAME)) {
                values[i] = userGroup.getName();
            } else if ((userGroup != null) && tokens[i].equals(USERGROUP_LS_NAME)) {
                values[i] = userGroup.getDomain();
            }
        }

        return values;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   cls       DOCUMENT ME!
     * @param   clsToken  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static String getClassValue(final MetaClass cls, final String clsToken) {
        final String value = null;
        /*Sirius.server.localserver.attribute.Attribute[] attributeArray = null;
         *
         *
         * if(clsToken.equals(CLASS_ID) && cls != null) {     //if(value == null)             value =
         * String.valueOf(cls.getID());     //else     //      value += (ARRAY_SEPARATOR + String.valueOf(cls.getID()));
         * } else if(clsToken.equals(CLASS_NAME) && cls != null) {     //if(value == null)             value =
         * cls.getName().trim();     //else     //      value += (ARRAY_SEPARATOR + cls.getName().trim());      } else
         * if(clsToken.indexOf(CLASS_ATTRIBUTE_ID) != -1 && cls != null) {     attributeArray = cls.getAttribs();
         * if(attributeArray != null && attributeArray.length > 0)     {             for(int k = 0; k <
         * attributeArray.length; k++)             {                     int id =
         * Integer.parseInt(clsToken.substring(CLASS_ATTRIBUTE_ID.length(), clsToken.length()));
         * //NavigatorLogger.printMessage("Attribute: " + id + " : " + attributeArray[k].getID());
         * if(attributeArray[k].getID() == id)                     {            //if(value == null)      value =
         * attributeArray[k].getValue().toString(); //else                             //  value += (ARRAY_SEPARATOR +
         * attributeArray[k].getValue());              }             }                 attributeArray = null;     } }
         * else if(clsToken.indexOf(CLASS_ATTRIBUTE_NAME) != -1 && cls != null) {     attributeArray = cls.getAttribs();
         * if(attributeArray != null && attributeArray.length > 0)  {             for(int k = 0; k <
         * attributeArray.length; k++)             {                     String  name =
         * clsToken.substring(CLASS_ATTRIBUTE_NAME.length(), clsToken.length());
         * if(attributeArray[k].getName().equals(name))                     {                             //if(value ==
         * null)                                     value = attributeArray[k].getValue().toString();         //else //
         * value += (ARRAY_SEPARATOR + attributeArray[k].getValue()); }     }
         *                                                                             attributeArray = null;     }}*/

        return value;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   object       DOCUMENT ME!
     * @param   objectToken  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static String getObjectValue(final MetaObject object, final String objectToken) {
        String value = null;
        Sirius.server.localserver.attribute.Attribute[] attributeArray = null;

        if (objectToken.equals(OBJECT_ID) && (object != null)) {
            // if(value == null)
            value = String.valueOf(object.getID());
            // else
            // value += (ARRAY_SEPARATOR + String.valueOf(object.getID()));
        } else if (objectToken.equals(OBJECT_NAME) && (object != null)) {
            // if(value == null)
            value = object.getName().trim();
            // else
            // value += (ARRAY_SEPARATOR + object.getName().trim());
        } else if ((objectToken.indexOf(OBJECT_ATTRIBUTE_ID) != -1) && (object != null)) {
            attributeArray = object.getAttribs();

            if ((attributeArray != null) && (attributeArray.length > 0)) {
                for (int k = 0; k < attributeArray.length; k++) {
                    final int id = Integer.parseInt(objectToken.substring(
                                OBJECT_ATTRIBUTE_ID.length(),
                                objectToken.length()));
                    if (attributeArray[k].getID().equalsIgnoreCase(id + "")) // NOI18N
                    {
                        // if(value == null)
                        value = attributeArray[k].getValue().toString();
                        // else value += (ARRAY_SEPARATOR + attributeArray[k].getValue());
                    }
                }

                attributeArray = null;
            }
        } else if ((objectToken.indexOf(OBJECT_ATTRIBUTE_NAME) != -1) && (object != null)) {
            attributeArray = object.getAttribs();

            if ((attributeArray != null) && (attributeArray.length > 0)) {
                for (int k = 0; k < attributeArray.length; k++) {
                    final String name = objectToken.substring(OBJECT_ATTRIBUTE_NAME.length(), objectToken.length());
                    if (attributeArray[k].getName().equalsIgnoreCase(name)) {
                        // if(value == null)
                        value = attributeArray[k].getValue().toString();
                        // else value += (ARRAY_SEPARATOR + attributeArray[k].getValue());
                    }
                }

                attributeArray = null;
            }
        }

        return value;
    }

    /*
     * public static void main(String[] args) {     String parseThis =
     * "http://134.96.158.150:8080/sachdatenabfrage/abfrage1.pl?benutzer=$u.name�&heimat=$u.ls_name�&thema=$c.id�&objectArray=$o.id�&attribut=$o_a.5�";
     * NavigatorLogger.printMessage(parseThis);     String[] tokens = StringParameterizer.parseString(parseThis, '$' ,
     * '�' );     for(int i = 0; i < tokens.length; i++)              NavigatorLogger.printMessage("Token " + i + ": " +
     * tokens[i]);                 int num = Integer.parseInt(tokens[4].substring(OBJECT_ATTRIBUTE_ID.length(),
     * tokens[4].length()));     String values[] = new String[] {"Penner", "unter der Bruecke", "besoffen", "flasche",
     * "schmutzig"};          NavigatorLogger.printMessage("num: " + num);
     * //NavigatorLogger.printMessage(tokens[4].indexOf(OBJECT_ATTRIBUTE_ID));          try     {
     * NavigatorLogger.printMessage(StringParameterizer.parameterizeString(parseThis, tokens, values));     }
     * catch(Throwable t)     {             t.printStackTrace();     }}*/
}
