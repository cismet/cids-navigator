/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * DefaultEditorLocatorDelegate.java
 *
 * Created on 20. August 2004, 16:34
 */
package Sirius.navigator.ui.attributes.editor.metaobject;

import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.plugin.*;
import Sirius.navigator.resource.*;
import Sirius.navigator.ui.attributes.editor.*;

import Sirius.server.localserver.attribute.Attribute;
import Sirius.server.middleware.types.*;

import org.apache.log4j.Logger;

import java.net.*;

import java.util.*;

import de.cismet.cids.server.connectioncontext.ClientConnectionContext;
import de.cismet.cids.server.connectioncontext.ConnectionContextProvider;

/**
 * Standardimplementierung des EditorLocator Interfaces, die eine Liste von Standard Editoren f\u00FCr bestimmte Klassen
 * bereith\u00E4lt.
 *
 * @author   Pascal
 * @version  $Revision$, $Date$
 */
public class MetaAttributeEditorLocator implements EditorLocator, ConnectionContextProvider {

    //~ Static fields/initializers ---------------------------------------------

    public static final String EDITOR_JAR_PATH = "/editor/editor.jar"; // NOI18N
    public static final Class DEFAULT_COMPLEX_EDITOR = DefaultComplexMetaAttributeEditor.class;
    public static final Class DEFAULT_COMPLEX_ARRAY_EDITOR = DefaultComplexMetaAttributeArrayEditor.class;
    public static final Class DEFAULT_READONLY_EDITOR = ReadOnlyMetaAttributeEditor.class;
    protected static ClassLoader classLoader;

    //~ Instance fields --------------------------------------------------------

    protected Logger logger;

    /** Holds value of property ignoreInvisibleAttributes. */
    private boolean ignoreInvisibleAttributes = false;

    private final ClientConnectionContext connectionContext = ClientConnectionContext.create(getClass()
                    .getSimpleName());

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of DefaultEditorLocatorDelegate.
     */
    public MetaAttributeEditorLocator() {
        this.logger = Logger.getLogger(this.getClass().getName());
        this.ignoreInvisibleAttributes = false;

        if (classLoader == null) {
            final String editorJARPath = ResourceManager.getManager()
                        .pathToIURIString(PropertyManager.getManager().getBasePath() + EDITOR_JAR_PATH);

            try {
                if (logger.isInfoEnabled()) {
                    logger.info("MetaAttributeEditorLocator(): initializing and loading editor jar: " + editorJARPath); // NOI18N
                }
                final URL[] urls = new URL[] { new URL(editorJARPath) };
                this.classLoader = new PluginClassLoader(urls, this.getClass().getClassLoader());
            } catch (Throwable t) {
                logger.error("MetaAttributeEditorLocator(): could not load editor jar: " + editorJARPath);              // NOI18N
                this.classLoader = this.getClass().getClassLoader();
            }
        }
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Map getEditors(final Object value) {
        if (value instanceof Attribute) {
            if (logger.isDebugEnabled()) {
                logger.debug("getEditors(value) value is a meta attribute, retrieving meta object");     // NOI18N
            }
            final Attribute metaAttribute = (Attribute)value;
            return this.getEditors(metaAttribute.getValue());
        } else if (value instanceof MetaObject) {
            final MetaObject MetaObject = (MetaObject)value;
            if (logger.isDebugEnabled()) {
                logger.debug("getEditors(value) value is a meta object (" + MetaObject.getName() + ")"); // NOI18N
            }

            return this.getEditors(MetaObject.getAttributes());
        } else {
            logger.error("getEditor(value): " + value.getClass().getName()
                        + " is not supported by this editor locator"); // NOI18N
            return new HashMap();
        }
    }

    @Override
    public Map getEditors(final java.util.Map metaAttributes) {
        final LinkedHashMap editorsMap = new LinkedHashMap();
        final Iterator iterator = metaAttributes.keySet().iterator();

        while (iterator.hasNext()) {
            final Object key = iterator.next();
            final Attribute attribute = (Attribute)metaAttributes.get(key);

            if (logger.isDebugEnabled()) {
                logger.debug("hash map key: " + key + " | attribute key: " + attribute.getKey()); // NOI18N
            }
            final BasicEditor editor = this.getEditor(attribute);

            if (editor != null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("getEditors(MetaObject): adding new editor with id " + key);                    // NOI18N
                }
                editorsMap.put(key, editor);
            } else {
                logger.warn("no editor found for '" + attribute.getName() + "' using default read only editor"); // NOI18N
                final BasicEditor readOnlyEditor = this.createEditor(ReadOnlyMetaAttributeEditor.class);
                editorsMap.put(key, readOnlyEditor);
            }
        }

        if (editorsMap.size() == 0) {
            logger.warn("getEditors(metaAttributes): no attributes / editors found"); // NOI18N
        }
        return editorsMap;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   editorClassName  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected Class createEditorClass(final String editorClassName) {
        if (logger.isDebugEnabled()) {
            logger.debug("createEditorClass(): creating new editor '" + editorClassName + "'");                     // NOI18N
        }
        if (editorClassName != null) {
            try {
                final Class editorClass = classLoader.loadClass(editorClassName);
                return editorClass;
            } catch (Throwable t) {
                logger.error("createEditorClass(): could not create editor '" + editorClassName + "' instance", t); // NOI18N
            }
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   editorClass  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected BasicEditor createEditor(final Class editorClass) {
        if (editorClass != null) {
            try {
                final Object instance = editorClass.newInstance();
                return (BasicEditor)instance;
            } catch (Throwable t) {
                logger.error("createEditor(): could not create editor '" + editorClass.getName() + "' instance", t); // NOI18N
            }
        }

        return null;
    }

    @Override
    public BasicEditor getEditor(final Object value) {
        if (value instanceof Attribute) {
            final Sirius.server.localserver.attribute.ObjectAttribute objectAttribute =
                (Sirius.server.localserver.attribute.ObjectAttribute)value;
            if (logger.isInfoEnabled()) {
                logger.info("getEditor(): searching editor for meta attribute '" + objectAttribute.getName() + "' ("
                            + objectAttribute.getID() + ")");                                         // NOI18N
                logger.info("objectAttribute.getName() .is Visible: " + objectAttribute.isVisible()); // NOI18N
            }
            if (objectAttribute.isVisible() || !this.isIgnoreInvisibleAttributes()) {
                final BasicEditor simpleEditor = this.createEditor(this.createEditorClass(
                            objectAttribute.getSimpleEditor()));
                if ((simpleEditor != null) && objectAttribute.referencesObject())                     // && objectAttribute.isSubstitute())
                {
                    if (logger.isDebugEnabled()) {
                        logger.debug("getEditor(): attribute '" + objectAttribute.getName()
                                    + "' is complex (references object)");                            // NOI18N
                    }
                    Class complexEditorClass = null;

                    // xxx
                    if (objectAttribute.isArray()) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("getEditor(): attribute is an array, using "
                                        + MetaAttributeEditorLocator.DEFAULT_COMPLEX_ARRAY_EDITOR); // NOI18N
                        }

                        /*try
                         * { MetaClass metaClass =
                         * SessionManager.getProxy().getMetaClass(objectAttribute.getClassKey());
                         * if(metaClass.isArrayElementLink()) { if(logger.isDebugEnabled())logger.debug("getEditor():
                         * attribute is an array helper object, using default editor");     complexEditorClass =
                         * this.createEditorClass(objectAttribute.getComplexEditor()); } else {     complexEditorClass =
                         * MetaAttributeEditorLocator.DEFAULT_COMPLEX_ARRAY_EDITOR; } } catch(Throwable t) {
                         * logger.error("getEditor(): could not load meta class", t); complexEditorClass =
                         * this.createEditorClass(objectAttribute.getComplexEditor());}*/

                        complexEditorClass = MetaAttributeEditorLocator.DEFAULT_COMPLEX_ARRAY_EDITOR;
                    } else {
                        complexEditorClass = this.createEditorClass(objectAttribute.getComplexEditor());
                    }

                    if ((complexEditorClass == null) || !ComplexEditor.class.isAssignableFrom(complexEditorClass)) {
                        logger.error(complexEditorClass + " is no complax editor, selecting DefaultComplexEditor"); // NOI18N
                        complexEditorClass = DEFAULT_COMPLEX_EDITOR;
                    }

                    simpleEditor.setProperty(SimpleEditor.PROPERTY_COMLPEX_EDTIOR, complexEditorClass);
                }

                return simpleEditor;
            } else {
                logger.warn("getEditor(): attribute '" + objectAttribute.getName()
                            + "' is invisible, ignoring attribute"); // NOI18N
            }
        } else if (value instanceof MetaObject) {
            final MetaObject MetaObject = (MetaObject)value;
            if (logger.isInfoEnabled()) {
                logger.info("getEditor(): searching editor for meta object editorname=" + MetaObject.getComplexEditor()
                            + " ");                                  // NOI18N
            }
            /*BasicEditor simpleEditor = this.createEditor(this.createEditorClass(MetaObject.getSimpleEditor()));
             * Class complexEditor = this.createEditorClass(MetaObject.getComplexEditor()); if(simpleEditor != null &&
             * complexEditor != null) { simpleEditor.setProperty(editor.PROPERTY_COMLPEX_EDTIOR, complexEditor);}*/
            if (MetaObject.getComplexEditor() != null) {
                final BasicEditor editor = this.createEditor(this.createEditorClass(MetaObject.getComplexEditor()));
                return editor;
            } else {
                try {
                    final BasicEditor editor = this.createEditor(this.createEditorClass(
                                SessionManager.getProxy().getMetaClass(
                                    MetaObject.getClassKey(),
                                    getConnectionContext()).getComplexEditor()));
                    return editor;
                } catch (Exception e) {
                    logger.fatal("MetaObjectEditorsuche Exception", e); // NOI18N
                    return null;
                }
            }
        } else {
            logger.error("getEditor(): " + value.getClass().getName() + " is not supported by this editor locator"); // NOI18N
        }

        return null;
    }

    @Override
    public java.util.Map getEditors(final java.util.Collection collection) {
        logger.error(
            "the method 'getEditors(java.util.Collection collection)' ist not supported by this implementation"); // NOI18N
        return new java.util.HashMap();
    }

    /**
     * Getter for property ignoreInvisibleAttributes.
     *
     * @return  Value of property ignoreInvisibleAttributes.
     */
    public boolean isIgnoreInvisibleAttributes() {
        return this.ignoreInvisibleAttributes;
    }

    /**
     * Setter for property ignoreInvisibleAttributes.
     *
     * @param  ignoreInvisibleAttributes  New value of property ignoreInvisibleAttributes.
     */
    public void setIgnoreInvisibleAttributes(final boolean ignoreInvisibleAttributes) {
        this.ignoreInvisibleAttributes = ignoreInvisibleAttributes;
    }

    @Override
    public final ClientConnectionContext getConnectionContext() {
        return connectionContext;
    }
}
