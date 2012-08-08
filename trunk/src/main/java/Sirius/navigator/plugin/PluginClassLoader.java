/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * PluginClassLoader.java
 *
 * Created on 20. Mai 2003, 15:18
 */
package Sirius.navigator.plugin;

import java.net.*;

import java.security.*;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class PluginClassLoader extends URLClassLoader {

    //~ Instance fields --------------------------------------------------------

    private final Permissions allPermissions;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of PluginClassLoader.
     *
     * @param  urls    DOCUMENT ME!
     * @param  parent  DOCUMENT ME!
     */
    public PluginClassLoader(final URL[] urls, final ClassLoader parent) {
        super(urls, parent);
        this.allPermissions = new Permissions();
        this.allPermissions.add(new AllPermission());
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    protected PermissionCollection getPermissions(final CodeSource codesource) {
        return allPermissions;
    }
}
