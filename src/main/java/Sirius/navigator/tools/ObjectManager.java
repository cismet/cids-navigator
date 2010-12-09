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
        Version                 :       1.0
        Purpose                 :
        Created                 :       01.11.1999
        History                 :

*******************************************************************************/

import java.util.*;

/**
 * Der Object Manager verwaltet in einer Hashtable Referenzen auf Objekte. Ein Object kann sich beim ObjectManager mit
 * einem bestimmten Namen registrieren und andere Klassen koennen dann ueber den ObjectManager auf dieses Object
 * zugreifen.Der ObjectManager bietet hierzu enige static Funktionen an.<br>
 * Der Vorteil: Es koennen auch dann Referenzen auf Objekte gebildet werden, wenn das Objekt in einer bestimmten Klasse
 * nicht verfuegbar ist (keine Referenz auf dieses Objekt im Konstruktir, etc.). Besonders nuetzlich erweist sich das
 * beim Registrieren von Event Listenern. Der Nachteil: Wenn eine static Methode aufgerufen wird, bevor der
 * ObjektManager instantiiert wurde, kommt es zu einer NullPointerException.
 *
 * @author   Pascal Dih&eacute;
 * @version  1.0
 */
public final class ObjectManager {

    //~ Static fields/initializers ---------------------------------------------

    private static Hashtable objectPool = null;

    //~ Constructors -----------------------------------------------------------

    /**
     * Erzeugt einen neue Instanz des Objektmanagers.Der Objektmanagers sollte nur genau ein mal instantiiert werden.
     */
    public ObjectManager() {
        objectPool = new Hashtable(10);
    }

    /**
     * Erzeugt einen neue Instanz des Objektmanagers.Der Objektmanagers sollte nur genau ein mal instantiiert werden.
     *
     * @param  initialCapacity  Anfaengliche Groesse der Hashtable
     * @param  loadFactor       loadFactor der Hashtable
     */
    public ObjectManager(final int initialCapacity, final float loadFactor) {
        objectPool = new Hashtable(initialCapacity, loadFactor);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Registriert ein Objekt.
     *
     * @param  objStr  Name des Objekts
     * @param  objRef  Referenz auf das Objekt
     */
    public static void registerObject(final String objStr, final java.lang.Object objRef) {
        final java.lang.Object objectEntry = (java.lang.Object)objectPool.get(objStr);
        if (objectEntry == null) {
            objectPool.put(objStr, objRef);
            // else
            // Exception ...
        }
    }

    /**
     * Deregistriert ein Objekt.
     *
     * @param  objStr  Name des Objekts.
     */
    public static void unregisterObject(final String objStr) {
        final java.lang.Object objectEntry = (java.lang.Object)objectPool.get(objStr);
        if (objectEntry != null) {
            objectPool.remove(objStr);
        }
    }

    /**
     * Gibt ein Referenz auf ein registriertes Objekt zurueck.
     *
     * @param   objStr  Name des Objekts.
     *
     * @return  Referenz auf das Objekt oder null.
     */
    public static java.lang.Object getRegistredObject(final String objStr) {
        final java.lang.Object objectEntry = (java.lang.Object)objectPool.get(objStr);
        return objectEntry;
    }
}
