/*
 * CloneHelper.java
 *
 * Created on 18. August 2004, 15:47
 */

package Sirius.navigator.tools;

import java.lang.reflect.*;
import java.io.*;

/**
 * Hilfsmethoden zum Klonen von beliebigen Objekten.
 *
 * @author  Pascal
 */
public class CloneHelper
{
    
    /**
     * Klont ein beliebiges Objekt, sofern dieses Cloneable implementiert und
     * eine Methode public Object clone() oder Serializable implementiert.
     *
     * @return ein Kopie des Objekts
     * @throws CloneNotSupportedException wenn das Objekt nicht cloneable ist
     */
    public static Object clone(Object toBeCloned) throws CloneNotSupportedException
    {
        CloneNotSupportedException cloneNotSupportedException;
        
        try
        {
            return cloneCloneable(toBeCloned);
        }
        catch(CloneNotSupportedException cnsexp)
        {
            cloneNotSupportedException = cnsexp;
            
            try
            {
                return cloneSerializable(toBeCloned);
            }
            catch(NotSerializableException nsexp)
            {
                throw cloneNotSupportedException;
            }
        } 
    }
    
    /**
     * Klont ein beliebiges Objekt, sofern dieses Serializable implementiert.
     *
     * @return ein Kopie des Objekts
     * @throws NotSerializableException wenn das Objekt nicht serialisierbar ist
     *
     */
    public static Object cloneSerializable(Object toBeCloned) throws NotSerializableException
    {
        Class toBeClonedClass =  toBeCloned.getClass();
        
        // cloning not necessary
        if(toBeClonedClass.isPrimitive() || CloneHelper.isImmutable(toBeClonedClass))
        {
            return toBeCloned;
        }
        else if(Serializable.class.isAssignableFrom(toBeClonedClass))
        {
            try
            {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos);
                oos.writeObject(toBeCloned);

                ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
                ObjectInputStream ois = new ObjectInputStream(bis);
                return ois.readObject();
            }
            catch(Exception exp)
            {
                throw new NotSerializableException(toBeClonedClass.getName() + " is not serializable: \n" + exp.getMessage());
            }  
        }
        else
        {
            throw new NotSerializableException(toBeClonedClass.getName() + " is not serializable");
        }
    }
    
    /**
     * Klont ein beliebiges Objekt, sofern dieses Cloneable implementiert und
     * eine Methode public Object clone() besitzt.
     *
     * @return ein Kopie des Objekts
     * @throws CloneNotSupportedException wenn das Objekt nicht cloneable ist
     *
     */
    public static Object cloneCloneable(Object toBeCloned) throws CloneNotSupportedException
    {
        Class toBeClonedClass =  toBeCloned.getClass();
        
        // cloning not necessary
        if(toBeClonedClass.isPrimitive() || CloneHelper.isImmutable(toBeClonedClass))
        {
            return toBeCloned;
        }
        else if(Cloneable.class.isAssignableFrom(toBeClonedClass))
        {
            try
            {
                Method method = toBeClonedClass.getMethod("clone", new Class[0]);
                
                try
                {
                    return method.invoke(toBeCloned, new Object[0]);
                }
                catch (InvocationTargetException e)
                {
                    Throwable t = e.getTargetException();
                    
                    if (t instanceof Error)
                    {
                        throw (Error) t;
                    }
                    else if (t instanceof RuntimeException)
                    {
                        throw (RuntimeException) t;
                    }
                    else if (t instanceof CloneNotSupportedException)
                    {
                        throw (CloneNotSupportedException) t;
                    }
                    else
                    {
                        throw new RuntimeException(t);
                    }
                }
            }
            catch (NoSuchMethodException ne)
            {
                throw new CloneNotSupportedException(toBeClonedClass.getName() + " is not cloneable: \n" + ne.getMessage());
            }
            catch (IllegalAccessException ie)
            {
                throw new CloneNotSupportedException(toBeClonedClass.getName() + " is not cloneable: \n" + ie.getMessage());
            }
        }
        else
        {
            throw new CloneNotSupportedException(toBeClonedClass.getName() + " is not cloneable");
        }
    }
    
    /**
     * Gibt an, ob es sich bei der \u00FCbergebenen Klasse um einen unver\u00E4nderliches
     * Java Object handelt, da\u00DF nicht geklont werden mu\u00DF.
     *
     * @return true/false
     */
    public static boolean isImmutable(Class clazz)
    {
        return  
        clazz == String.class ||
        clazz == Integer.class ||
        clazz == Long.class ||
        clazz == Short.class ||
        clazz == Byte.class ||
        clazz == Character.class ||
        clazz == Float.class ||
        clazz == Double.class ||
        clazz == Boolean.class;
    }
}
