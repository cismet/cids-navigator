/*
 * PluginClassLoader.java
 *
 * Created on 20. Mai 2003, 15:18
 */

package Sirius.navigator.plugin;

import java.net.*;
import java.security.*;



/**
 *
 * @author  pascal
 */
public class PluginClassLoader extends URLClassLoader
{
    private final Permissions allPermissions;
    
    /** Creates a new instance of PluginClassLoader */
    public PluginClassLoader(URL[] urls, ClassLoader parent)
    {
        super(urls, parent);
        this.allPermissions = new Permissions();
        this.allPermissions.add(new AllPermission());
    }
    
    protected PermissionCollection getPermissions(CodeSource codesource)
    {
        return allPermissions;
    }
}
