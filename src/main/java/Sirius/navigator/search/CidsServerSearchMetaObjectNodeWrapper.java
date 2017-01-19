/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.search;

import Sirius.server.middleware.types.MetaObjectNode;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
@Getter
public class CidsServerSearchMetaObjectNodeWrapper {

    //~ Instance fields --------------------------------------------------------

    @JsonProperty(required = true)
    private final String domain;
    @JsonProperty(required = true)
    private final int classId;
    @JsonProperty(required = true)
    private final int objectId;
    @JsonProperty(required = true)
    private final String name;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new CidsServerSearchMetaObjectNodeWrapper object.
     *
     * @param  mon  DOCUMENT ME!
     */
    public CidsServerSearchMetaObjectNodeWrapper(final MetaObjectNode mon) {
        this.domain = mon.getDomain();
        this.classId = mon.getClassId();
        this.objectId = mon.getObjectId();
        this.name = mon.getName();
    }

    /**
     * Creates a new SearchResult object.
     *
     * @param  domain    DOCUMENT ME!
     * @param  classId   DOCUMENT ME!
     * @param  objectId  DOCUMENT ME!
     * @param  name      DOCUMENT ME!
     */
    @JsonCreator
    public CidsServerSearchMetaObjectNodeWrapper(@JsonProperty("domain") final String domain,
            @JsonProperty("classId") final int classId,
            @JsonProperty("objectId") final int objectId,
            @JsonProperty("name") final String name) {
        this.domain = domain;
        this.classId = classId;
        this.objectId = objectId;
        this.name = name;
    }
}
