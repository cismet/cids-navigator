/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.search;

import de.cismet.cids.tools.search.clientstuff.Modifier;

/**
 * DOCUMENT ME!
 *
 * @author   jweintraut
 * @version  $Revision$, $Date$
 */
@org.openide.util.lookup.ServiceProvider(service = Modifier.class)
public class HereModifier extends Modifier {

    //~ Methods ----------------------------------------------------------------

    @Override
    public String getCommand() {
        return "here";
    }

    @Override
    public String getHint() {
        return "Sucht im aktuellen cismap-Ausschnitt";
    }
}
