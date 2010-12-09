/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * CloneHelper.java
 *
 * Created on 18. August 2004, 15:47
 */
package Sirius.navigator.tools;

import java.io.*;

import java.lang.reflect.*;

/**
 * Hilfsmethoden zum Klonen von beliebigen Objekten.
 *
 * @author   Pascal
 * @version  $Revision$, $Date$
 */
public class CloneHelper {

    //~ Methods ----------------------------------------------------------------

    /**
     * Klont ein beliebiges Objekt, sofern dieses Cloneable implementiert und eine Methode public Object clone() oder
     * Serializable implementiert.
     *
     * @param   toBeCloned  DOCUMENT ME!
     *
     * @return  ein Kopie des Objekts
     *
     * @throws  CloneNotSupportedException  wenn das Objekt nicht cloneable ist
     */
    public static Object clone(final Object toBeCloned) throws CloneNotSupportedException {
        final CloneNotSupportedException cloneNotSupportedException;

        try {
            return cloneCloneable(toBeCloned);
        } catch (CloneNotSupportedException cnsexp) {
            cloneNotSupportedException = cnsexp;

            try {
                return cloneSerializable(toBeCloned);
            } catch (NotSerializableException nsexp) {
                throw cloneNotSupportedException;
            }
        }
    }

    /**
     * Klont ein beliebiges Objekt, sofern dieses Serializable implementiert.
     *
     * @param   toBeCloned  DOCUMENT ME!
     *
     * @return  ein Kopie des Objekts
     *
     * @throws  NotSerializableException  wenn das Objekt nicht serialisierbar ist
     */
    public static Object cloneSerializable(final Object toBeCloned) throws NotSerializableException {
        final Class toBeClonedClass = toBeCloned.getClass();

        // cloning not necessary
        if (toBeClonedClass.isPrimitive() || CloneHelper.isImmutable(toBeClonedClass)) {
            return toBeCloned;
        } else if (Serializable.class.isAssignableFrom(toBeClonedClass)) {
            try {
                final ByteArrayOutputStream bos = new ByteArrayOutputStream();
                final ObjectOutputStream oos = new ObjectOutputStream(bos);
                oos.writeObject(toBeCloned);

                final ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
                final ObjectInputStream ois = new ObjectInputStream(bis);
                return ois.readObject();
            } catch (Exception exp) {
                throw new NotSerializableException(toBeClonedClass.getName() + " is not serializable: \n"
                            + exp.getMessage()); // NOI18N
            }
        } else {
            throw new NotSerializableException(toBeClonedClass.getName() + " is not serializable"); // NOI18N
        }
    }

    /**
     * Klont ein beliebiges Objekt, sofern dieses Cloneable implementiert und eine Methode public Object clone()
     * besitzt.
     *
     * @param   toBeCloned  DOCUMENT ME!
     *
     * @return  ein Kopie des Objekts
     *
     * @throws  CloneNotSupportedException  wenn das Objekt nicht cloneable ist
     */
    public static Object cloneCloneable(final Object toBeCloned) throws CloneNotSupportedException {
        final Class toBeClonedClass = toBeCloned.getClass();

        // cloning not necessary
        if (toBeClonedClass.isPrimitive() || CloneHelper.isImmutable(toBeClonedClass)) {
            return toBeCloned;
        } else if (Cloneable.class.isAssignableFrom(toBeClonedClass)) {
            try {
                final Method method = toBeClonedClass.getMethod("clone", new Class[0]); // NOI18N

                try {
                    return method.invoke(toBeCloned, new Object[0]);
                } catch (InvocationTargetException e) {
                    final Throwable t = e.getTargetException();

                    if (t instanceof Error) {
                        throw (Error)t;
                    } else if (t instanceof RuntimeException) {
                        throw (RuntimeException)t;
                    } else if (t instanceof CloneNotSupportedException) {
                        throw (CloneNotSupportedException)t;
                    } else {
                        throw new RuntimeException(t);
                    }
                }
            } catch (NoSuchMethodException ne) {
                throw new CloneNotSupportedException(toBeClonedClass.getName() + " is not cloneable: \n"
                            + ne.getMessage()); // NOI18N
            } catch (IllegalAccessException ie) {
                throw new CloneNotSupportedException(toBeClonedClass.getName() + " is not cloneable: \n"
                            + ie.getMessage()); // NOI18N
            }
        } else {
            throw new CloneNotSupportedException(toBeClonedClass.getName() + " is not cloneable"); // NOI18N
        }
    }

    /**
     * Gibt an, ob es sich bei der \u00FCbergebenen Klasse um einen unver\u00E4nderliches Java Object handelt, da\u00DF
     * nicht geklont werden mu\u00DF.
     *
     * @param   clazz  DOCUMENT ME!
     *
     * @return  true/false
     */
    public static boolean isImmutable(final Class clazz) {
        return (clazz == String.class)
                    || (clazz == Integer.class)
                    || (clazz == Long.class)
                    || (clazz == Short.class)
                    || (clazz == Byte.class)
                    || (clazz == Character.class)
                    || (clazz == Float.class)
                    || (clazz == Double.class)
                    || (clazz == Boolean.class);
    }
}
