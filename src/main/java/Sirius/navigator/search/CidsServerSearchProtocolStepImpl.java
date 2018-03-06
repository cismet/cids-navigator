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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import de.cismet.cids.server.search.CidsServerSearch;

import de.cismet.commons.gui.protocol.AbstractProtocolStep;
import de.cismet.commons.gui.protocol.AbstractProtocolStepPanel;
import de.cismet.commons.gui.protocol.ProtocolStepMetaInfo;

import de.cismet.connectioncontext.ClientConnectionContext;
import de.cismet.connectioncontext.ConnectionContextProvider;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class CidsServerSearchProtocolStepImpl extends AbstractProtocolStep implements CidsServerSearchProtocolStep,
    ConnectionContextProvider {

    //~ Static fields/initializers ---------------------------------------------

    private static final ProtocolStepMetaInfo META_INFO = new ProtocolStepMetaInfo(
            "CidsSearch",
            "CidsSearch protocol step");

    //~ Instance fields --------------------------------------------------------

    @Getter @Setter @JsonIgnore private transient CidsServerSearchProtocolStepReexecutor reexecutor;

    @Getter @JsonIgnore private final transient CidsServerSearch search;

    @Getter @JsonIgnore private final transient List<MetaObjectNode> searchResultNodes;

    @JsonIgnore private final ClientConnectionContext connectionContext = ClientConnectionContext.create(getClass()
                    .getSimpleName());

    @Getter
    @JsonProperty(required = true)
    private List<CidsServerSearchMetaObjectNodeWrapper> searchResults;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new CidsSearchProtocolStep object.
     *
     * @param  searchResults  DOCUMENT ME!
     */
    @JsonCreator
    public CidsServerSearchProtocolStepImpl(final List<CidsServerSearchMetaObjectNodeWrapper> searchResults) {
        final List<MetaObjectNode> mons = new ArrayList<MetaObjectNode>();
        for (final CidsServerSearchMetaObjectNodeWrapper result : searchResults) {
            mons.add(new MetaObjectNode(
                    result.getDomain(),
                    result.getObjectId(),
                    result.getClassId(),
                    result.getName(),
                    null,
                    null)); // TODO: Check4CashedGeomAndLightweightJson
        }
        this.searchResults = searchResults;

        this.searchResultNodes = mons;
        this.search = null;
    }

    /**
     * Creates a new CidsSearchProtocolStep object.
     *
     * @param  search       DOCUMENT ME!
     * @param  resultNodes  DOCUMENT ME!
     */
    public CidsServerSearchProtocolStepImpl(final CidsServerSearch search, final List<MetaObjectNode> resultNodes) {
        this.search = search;
        this.searchResultNodes = resultNodes;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    @Override
    public void reExecuteSearch() {
        if (getReexecutor() != null) {
            getReexecutor().reExecuteSearch();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public boolean isReExecuteSearchEnabled() {
        if (getReexecutor() != null) {
            return getReexecutor().isReExecuteSearchEnabled();
        } else {
            return false;
        }
    }

    @Override
    public void initParameters() {
        final List<CidsServerSearchMetaObjectNodeWrapper> searchResults =
            new ArrayList<CidsServerSearchMetaObjectNodeWrapper>();
        for (final Object resultNode : searchResultNodes) {
            if (resultNode instanceof MetaObjectNode) {
                searchResults.add(new CidsServerSearchMetaObjectNodeWrapper((MetaObjectNode)resultNode));
            }
        }
        this.searchResults = searchResults;
    }

    @Override
    protected ProtocolStepMetaInfo createMetaInfo() {
        return META_INFO;
    }

    @Override
    public AbstractProtocolStepPanel visualize() {
        return new CidsServerSearchProtocolStepPanel(this, getConnectionContext());
    }

    @Override
    public ClientConnectionContext getConnectionContext() {
        return connectionContext;
    }
}
