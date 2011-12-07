/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.search;

import org.openide.util.NbBundle;

import java.util.MissingResourceException;

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
        String command = "hier";

        try {
            command = NbBundle.getMessage(HereModifier.class, "HereModifier.command");
        } catch (MissingResourceException e) {
        }

        return command;
    }

    @Override
    public String getHint() {
        String hint = "Sucht im aktuellen cismap-Ausschnitt";

        try {
            hint = NbBundle.getMessage(HereModifier.class, "HereModifier.hint");
        } catch (MissingResourceException e) {
        }

        return hint;
    }
}
