package Sirius.navigator.exception;

import Sirius.navigator.resource.ResourceManager;

/**
 *
 * @author  pascal
 */
public class NavigatorException extends Exception
{
    public final static int WARNING = ExceptionManager.WARNING;
    public final static int ERROR = ExceptionManager.ERROR;
    public final static int FATAL = ExceptionManager.FATAL;
    
    protected final int level;
    protected final String name;
    
    private static final ResourceManager resource = ResourceManager.getManager();
    
    public NavigatorException(String message)
    {
        this(message, FATAL, null);
    }
    
    public NavigatorException(String message, Throwable cause)
    {
        this(message, FATAL, cause);
    }
    
    public NavigatorException(String message, int level)
    {
        this(message, level, null);
    }
    
    public NavigatorException(String message, int level, Throwable cause)
    {
        super(message, cause);
        this.level = level;
        this.name = this.getClass().getName();
    }
    
    public NavigatorException(int level, String errorcode)
    {
        super(resource.getExceptionMessage(errorcode));
        this.name = resource.getExceptionName(errorcode);
        this.level = level;
    }
    
    public NavigatorException(int level, String errorcode, Throwable cause)
    {
        super(resource.getExceptionMessage(errorcode), cause);
        this.name = resource.getExceptionName(errorcode);
        this.level = level;
    }
    
    public NavigatorException(int level, String errorcode, String[] values, Throwable cause)
    {
        super(resource.getExceptionMessage(errorcode, values), cause);
        this.name = resource.getExceptionName(errorcode);
        this.level = level;
    }
        
    public int getLevel()
    {
        return this.level;
    } 
    
    public String getName()
    {
        return this.name;
    }   
}
