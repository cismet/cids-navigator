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
public class CaseSensitiveModifier extends Modifier {

    //~ Methods ----------------------------------------------------------------

    @Override
    public String getCommand() {
        String command = "gross-klein";

        try {
            command = NbBundle.getMessage(CaseSensitiveModifier.class, "CaseSensitiveModifier.command");
        } catch (MissingResourceException e) {
        }

        return command;
    }

    @Override
    public String getHint() {
        String hint = "Beachtet die Groß-/Kleinschreibung";

        try {
            hint = NbBundle.getMessage(CaseSensitiveModifier.class, "CaseSensitiveModifier.hint");
        } catch (MissingResourceException e) {
        }

        return hint;
    }
}
