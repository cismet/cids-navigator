package Sirius.navigator;

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
 * Programmers		:	Pascal
 *
 * Project			:	WuNDA 2
 * Version			:	1.0
 * Purpose			:
 * Created			:	01.10.1999
 * History			:
 *
 *******************************************************************************/

//import java.util.*;

import java.util.ResourceBundle;
import org.apache.log4j.Logger;

/** 
 * @deprecated, use org.apache.log4j.Logger!
 */
public final class NavigatorLogger
{
    private final static Logger logger = Logger.getLogger(NavigatorLogger.class.getPackage().getName());
    
    //public static final boolean DEV = false;
    public static final boolean DEV = true;
    //public static final boolean VERBOSE = false;
    public static final boolean VERBOSE = true;
    public static final boolean CONNECTION_VERBOSE = true;
    public static final boolean GUI_VERBOSE = false;
    public static final boolean NET_VERBOSE = false;
    public static final boolean SEARCH_VERBOSE = false;
    public static final boolean SIMS_VERBOSE = true;
    public static final boolean TREE_VERBOSE = false;
    public static final boolean THREAD_VERBOSE = false;
    public static final boolean STL_VERBOSE = false;
    public static final boolean PLUGIN_VERBOSE = false;
    public static final boolean METHOD_VERBOSE = false;
    private static final ResourceBundle I18N = ResourceBundle.getBundle("Sirius/navigator/resource/i18n/resources");
    
    //private static final Calendar calendar = new GregorianCalendar();
    
    
    /**
     *@deprecated
     */
    public static final void printMessage(boolean type, String message)
    {
        if(type && logger.isDebugEnabled())
        {
            //NavigatorLogger.printMessage("[" + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND) + "] - " + message);
            //System.out.println(calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND) + "\t" + message);
            logger.debug(message);
        
        }
    }
    
    public static final void printMessage(String message)
    {
        //System.out.println(calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND) + "\t" + message);
        if(logger.isDebugEnabled())logger.debug(message);
    }
    
    
        /*public static void main(String args[])
        {
                printMessage(DEV, "[NavigatorLogger] TEST");
        }*/
}
