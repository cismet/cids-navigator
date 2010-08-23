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
 * Programmers		:	Annen Thomas
 *
 * Project				:	WuNDA 2
 * Filename			:	StringLoader.java
 * Version				:	1.0
 * Purpose				:
 * Created				:	04.12.2000
 * History				:	12.04.2001
 *
 *******************************************************************************/

package Sirius.navigator.multilingual;

import java.util.*;
import java.io.PrintStream;
import java.io.FileOutputStream;
import java.io.File;
import Sirius.navigator.NavigatorLogger;
import Sirius.navigator.resource.PropertyManager;



/**
 * @version	1.0
 * @author	Annen Thomas
 * @deprecated migrate to netbeans i18n
 *
 */
public class StringLoader
{
    
    /**
     * @param	key	ID that represents the required string.
     * @return	String 		The string from the property-resource-file
     *						that match the key.
     *  @deprecated use ResourceManager.getString()
     */
    public static String getString(String key)
    {
        try
        {
            value = resourses.getString(key);
        }
        catch(MissingResourceException e)
        {
            error(key);
        }
        catch(Exception e)
        { e.printStackTrace(); }
        return value;
    }
        /*
         *
         * @ -> if no mnemonic char could been loaded.
         * @deprecated use ResourceManager.getMnemonic()
         */
    public static char getMnemonicChar(String key)
    {
        try
        {
            mnemonic = (resourses.getString(key)).charAt(FIRSTPOS);
        }
        catch(MissingResourceException e)
        {
            error(key);
        }
        catch(Exception e)
        { e.printStackTrace(); }
        return mnemonic;
    }
    /**
     * @param	key	ID that represents the required string-array.
     * @return	String 		The string-array from the property-resource-file
     *						that match the key.
     * @deprecated
     * @see ResourceManager
     */
    public static String[] getStringArray(String key)
    {
        try
        {
            values = resourses.getStringArray(key);
        }
        catch(MissingResourceException e)
        {
            error(key);
        }
        catch(Exception e)
        { e.printStackTrace(); }
        return values;
        
    }
    
    /**
     * @deprecated
     * @see ResourceManager
     */
    public static void error(String key)
    {
        NavigatorLogger.printMessage("<STL> MissingResourceException:");
        NavigatorLogger.printMessage("<STL> -------------------------");
        NavigatorLogger.printMessage("<STL> Invalid key found: "+key  );
        MISSINGRESSOURCEEXCEPTION++;
    }
    
    
    /**
     * Returns the number of MissingResourceException, that happens during a test
     * for ONE file
     *
     * @deprecated
     * @see ResourceManager
     * @return number of errors.
     */
    public static int numberOfMissingResourceExceptions()
    {
        int err = MISSINGRESSOURCEEXCEPTION;
        MISSINGRESSOURCEEXCEPTION = 0;
        return err;
    }
    
    private final static int						FIRSTPOS	=0;
    private				static int						MISSINGRESSOURCEEXCEPTION	=0;
    private 			static String[] 			values 		= null;
    private 			static char 					mnemonic 	= '%';
    private 			static String					value 		= "ERROR->NO_STRING";
    private 			static Locale					locale		;
    private 			static ResourceBundle resourses	;

    /**
     * @deprecated
     * @see ResourceManager
     */
    static
    {
                /*
                 * Load language-properties
                 */
        try
        {
            locale 		= Locale.getDefault();
            locale=PropertyManager.getManager().getLocale();
            resourses = ResourceBundle.getBundle("Sirius.navigator.multilingual.Resources", locale);
            if(NavigatorLogger.STL_VERBOSE)NavigatorLogger.printMessage("<STL> Loading finished!");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            if(NavigatorLogger.DEV)NavigatorLogger.printMessage("<STL> <FATAL_ERROR> !Can't load language-properties! ");
        }
    }
}

