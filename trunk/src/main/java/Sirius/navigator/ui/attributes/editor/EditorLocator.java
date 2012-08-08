/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * EditorLocator.java
 *
 * Created on 20. August 2004, 16:15
 */
package Sirius.navigator.ui.attributes.editor;

/**
 * Hilfsmethoden um f\u00FCr ein bestimmtes Objekt einen passenden Editor zu finden.
 *
 * <p>Die Regel nach der gesucht wird, legt die entsprechende Implementierung fest.</p>
 *
 * @author   Pascal
 * @version  $Revision$, $Date$
 */
public interface EditorLocator {

    //~ Methods ----------------------------------------------------------------

    /**
     * Untersucht ein komplexes Objekt (z.B. eine Collection, Java Bean, etc.) nach einer bestimmten Regel nach
     * einfachen und komplexen Editoren.
     *
     * <p>\u00DCber den key der Map wird im komplexen Editor (Container) des Objekts der entsprechende Wert gesetzt.
     * Wenn das komplexe Objekt z.B. eine Liste ist, k\u00F6nnte der Key z.B. ein Integer Objekt sein (-> index).
     * Einfachster Fall: das komplexe Objekt ist eine Map!</p>
     *
     * <p>Im Falle von Map und Collection soll an die beiden anderen getEditors Methoden delegiert werden.</p>
     *
     * @param   object  das komplexe Objekt das untersucht werden soll
     *
     * @return  eine Hasmap mit passenden Editoren
     *
     * @see     ComplexContainer.setValue(Object key, Object value)
     * @see     EditorLocator.getEditors(java.util.Collection collection)
     * @see     EditorLocator.getEditors(java.util.Map map)
     */
    java.util.Map getEditors(Object object);

    /**
     * Wenn das komplexe Object eine Collection ist, sollte diese Methode aufgerufen werden.
     *
     * @param   collection  DOCUMENT ME!
     *
     * @return  eine Hasmap mit passenden Editoren
     *
     * @see     EditorLocator.getEditors(Object object)
     * @pram    object das komplexe Objekt ist vom Typ java.util.Collection
     */
    java.util.Map getEditors(java.util.Collection collection);

    /**
     * Wenn das komplexe Object eine Hashmap ist, sollte diese Methode aufgerufen werden.
     *
     * @param   map  DOCUMENT ME!
     *
     * @return  eine Hasmap mit passenden Editoren
     *
     * @see     EditorLocator.getEditors(Object object)
     * @pram    object das komplexe Objekt ist vom Typ java.util.Map
     */
    java.util.Map getEditors(java.util.Map map);

    /**
     * Versucht einen passenden einfachen oder komplexen Editor f\u00FCr das Objekt zu finden.
     *
     * @param   object  DOCUMENT ME!
     *
     * @return  ein einfacher bzw. komplexer Editor, oder null falls kein passender Editor gefunden werden konnte.
     */
    BasicEditor getEditor(Object object);
}
