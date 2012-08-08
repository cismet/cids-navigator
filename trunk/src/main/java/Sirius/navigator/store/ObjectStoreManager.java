/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.store;

import Sirius.navigator.Navigator;
import org.apache.log4j.Logger;

import org.openide.util.Lookup;

import java.io.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * The ObjectStoreManager provides a convenient API to save and load serialized data
 * and to pass it to corresponding {@link ObjectStoreHandler}s.
 * 
 * In doing so, all data belonging to a given group (see {@link ObjectStoreHandler.Group})
 * are written and read from one given stream. This approach allows to handle related data 
 * e.g. with one file such as saving the layout of the {@link Navigator} and of the
 * cismap plugin in one layout file.
 * 
 *
 * @author   Benjamin Friedrich (benjamin.friedrich@cismet.de)
 * @version  $Revision$, $Date$
 */
public final class ObjectStoreManager {

    //~ Static fields/initializers ---------------------------------------------

    private static final String ERROR_SAVE = "an error occurred while saving objects from object stores";
    private static final String ERROR_LOAD = "an error occurred while loading objects from object stores";
    private static final Logger LOG = Logger.getLogger(ObjectStoreManager.class);

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ObjectStoreManager object.
     */
    private ObjectStoreManager() {
        // avoid object creation
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Helper method for quietly closing streams
     *
     * @param  o  stream to be closed
     */
    private static void closeStream(final Closeable o) {
        try {
            if (o != null) {
                o.close();
            }
        } catch (final IOException ex) {
            LOG.warn("a problem occurred while closing stream", ex);
        }
    }

    /**
     * Helper method for loading all available {@link ObjectStoreHandler}s belonging to the given group.
     *
     * @param   targetGroup  {@link ObjectStoreHandler} group
     *
     * @return  map of available {@link ObjectStoreHandler}s. NOTE: the map maps 
     *          {@link ObjectStoreHandler#getId() } to the corresponding {@link ObjectStoreHandler} instance
     */
    private static Map<Integer, ObjectStoreHandler> loadStores(final ObjectStoreHandler.Group targetGroup) {
        ObjectStoreManagerException.checkIfNull("targetGroup", targetGroup);

        final Collection<? extends ObjectStoreHandler> storeMgrs = Lookup.getDefault()
                    .lookupAll(ObjectStoreHandler.class);
        final HashMap<Integer, ObjectStoreHandler> groupStoresMap = new HashMap<Integer, ObjectStoreHandler>();

        if (!storeMgrs.isEmpty()) {
            Integer storeId;

            for (final ObjectStoreHandler store : storeMgrs) {
                if (targetGroup == store.getGroup()) {
                    storeId = store.getId();
                    if (groupStoresMap.containsKey(storeId)) {
                        LOG.warn("Store Group" + targetGroup + ": The object store id " + storeId
                                    + " is not unique -> already existing entry "
                                    + groupStoresMap.get(storeId) + " is replaced with " + store);
                    }

                    groupStoresMap.put(store.getId(), store);
                }
            }
        }

        return groupStoresMap;
    }

    /**
     * Saves all data of {@link ObjectStoreHandler}s belonging to the given group to
     * the given {@link OutputStream}.
     *
     * @param   out             {@link OutputStream} to which all data is written to
     * @param   groupToBeSaved  group of data which shall be saved to out
     *
     * @throws  ObjectStoreManagerException  if a problem occurs while saving data
     */
    public static void save(final OutputStream out, final ObjectStoreHandler.Group groupToBeSaved)
            throws ObjectStoreManagerException {
        ObjectStoreManagerException.checkIfNull("out", out);
        ObjectStoreManagerException.checkIfNull("groupToBeSaved", groupToBeSaved);

        final Map<Integer, ObjectStoreHandler> storeMap = loadStores(groupToBeSaved);
        if (storeMap == null) {
            LOG.warn("there is no object store registered for group " + groupToBeSaved);
            closeStream(out);
        } else {
            final HashMap<Integer, Serializable> dataMap = new HashMap<Integer, Serializable>(storeMap.size());

            for (final Map.Entry<Integer, ObjectStoreHandler> entry : storeMap.entrySet()) {
                dataMap.put(entry.getKey(), entry.getValue().getObjectToBeSaved());
            }

            final BufferedOutputStream bout = new BufferedOutputStream(out);
            ObjectOutputStream oout = null;

            try {
                oout = new ObjectOutputStream(bout);
                oout.writeObject(dataMap);
            } catch (final Exception e) {
                LOG.error(ERROR_SAVE, e);
                throw new ObjectStoreManagerException(ERROR_SAVE, e);
            } finally {
                closeStream(oout);
            }
        }
    }

    /**
     * Loads all data from the given {@link InputStream} and passees the loaded data to the
     * corresponding {@link ObjectStoreHandler}s belonging to the given group.
     *
     * @param   in               {@link InputStream} from which all data shall be read
     * @param   groupToBeLoaded  group of data which shall be handled
     *
     * @throws  ObjectStoreManagerException if a problem occurs while loading data
     */
    public static void load(final InputStream in, final ObjectStoreHandler.Group groupToBeLoaded)
            throws ObjectStoreManagerException {
        ObjectStoreManagerException.checkIfNull("in", in);
        ObjectStoreManagerException.checkIfNull("groupToBeLoaded", groupToBeLoaded);

        final Map<Integer, ObjectStoreHandler> storeMap = loadStores(groupToBeLoaded);
        if (storeMap == null) {
            LOG.warn("there is no object store registered for group " + groupToBeLoaded);
            closeStream(in);
        } else {
            final BufferedInputStream bin = new BufferedInputStream(in);
            ObjectInputStream oin = null;
            try {
                oin = new ObjectInputStream(bin);

                final Map<Integer, Serializable> dataMap = (Map<Integer, Serializable>)oin.readObject();

                ObjectStoreHandler store;
                for (final Map.Entry<Integer, Serializable> dataEntry : dataMap.entrySet()) {
                    store = storeMap.get(dataEntry.getKey());
                    if (store == null) {
                        LOG.warn("There is no object store for data entry with id " + dataEntry.getKey());
                    } else {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Found handler " + store.getId());
                        }
                        store.notifyAboutLoadedObject(dataEntry.getValue());
                    }
                }
            } catch (final EOFException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Unexpected EOF (most likely because the data source is empty)", e);
                }
            } catch (final Exception e) {
                LOG.error(ERROR_LOAD, e);
                throw new ObjectStoreManagerException(ERROR_LOAD, e);
            } finally {
                closeStream(oin);
            }
        }
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * Exception for problems occurring in {@link ObjectStoreManager}
     *
     * @version  $Revision$, $Date$
     */
    public static final class ObjectStoreManagerException extends RuntimeException {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new ObjectStoreManagerException object.
         *
         * @param  e  DOCUMENT ME!
         */
        public ObjectStoreManagerException(final Exception e) {
            super(e);
        }
        /**
         * Creates a new ObjectStoreManagerException object.
         *
         * @param  message  DOCUMENT ME!
         * @param  e        DOCUMENT ME!
         */
        public ObjectStoreManagerException(final String message, final Exception e) {
            super(message, e);
        }

        //~ Methods ------------------------------------------------------------

        /**
         * Convenient method for checking for null and throwing corresponding 
         * {@link ObjectStoreManagerException}s (wrapping a {@link NullPointerException}). 
         *
         * @param   varName  name of the variable which shall be checked
         * @param   o        data belonging to variable varName
         *
         * @throws  ObjectStoreManagerException  DOCUMENT ME!
         */
        public static void checkIfNull(final String varName, final Object o) throws ObjectStoreManagerException {
            if (o == null) {
                final NullPointerException e = new NullPointerException(varName + "must not be null");
                throw new ObjectStoreManagerException(e);
            }
        }
    }
}
