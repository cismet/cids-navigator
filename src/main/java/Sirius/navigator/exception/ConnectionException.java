package Sirius.navigator.exception;

/*
// header - edit "Data/yourJavaHeader" to customize
// contents - edit "EventHandlers/Java file/onCreate" to customize
//
*/

import java.io.*;

public class ConnectionException extends NavigatorException
{
    public ConnectionException(String message)
    {
        super(message);
    }
    
    public ConnectionException(String message, int level)
    {
        super(message, level);
    }
    
    public ConnectionException(String message, Throwable cause)
    {
        super(message, cause);
    }  
    
    public ConnectionException(String message, int level, Throwable cause)
    {
        super(message, level, cause);
    } 
}

