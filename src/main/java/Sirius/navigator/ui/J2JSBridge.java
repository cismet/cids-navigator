/**
 * *************************************************
 *
 * cismet GmbH, Saarbruecken, Germany
 * 
* ... and it just works.
 * 
***************************************************
 */
package Sirius.navigator.ui;

import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.exception.ConnectionException;
import Sirius.server.middleware.types.MetaClass;
import Sirius.server.middleware.types.MetaObject;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.cismet.cids.dynamics.CidsBean;
import de.cismet.cids.navigator.utils.ClassCacheMultiple;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import org.openide.util.Exceptions;

/**
 * DOCUMENT ME!
 *
 * @author daniel
 * @version $Revision$, $Date$
 */
public class J2JSBridge {

    public String getAllBeansForClass(final String domain, final String clazz) {

        //ToDo: checken ob tabell immer gleich Klassenname
        final MetaClass MB_MC = ClassCacheMultiple.getMetaClass(domain, clazz);
        String query = "SELECT " + MB_MC.getID() + ", " + MB_MC.getPrimaryKey() + " ";
        query += "FROM " + MB_MC.getTableName();
        try {
            final MetaObject[] metaObjects = SessionManager.getProxy().getMetaObjectByQuery(query, 0);
            final CidsBean[] beans = new CidsBean[metaObjects.length];

            for (int i = 0; i < metaObjects.length; i++) {
                beans[i] = metaObjects[i].getBean();
            }
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(beans);
        } catch (ConnectionException ex) {
            Exceptions.printStackTrace(ex);
        } catch (JsonGenerationException ex) {
            Exceptions.printStackTrace(ex);
        } catch (JsonMappingException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return "";
    }
}
