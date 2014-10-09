/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.search;

import org.apache.log4j.Logger;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

import org.openide.util.NbBundle;

import java.io.StringReader;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingWorker;

import de.cismet.cismap.commons.features.DefaultFeatureCollection;
import de.cismet.cismap.commons.features.Feature;
import de.cismet.cismap.commons.features.FeatureWithId;
import de.cismet.cismap.commons.featureservice.AbstractFeatureService;
import de.cismet.cismap.commons.featureservice.FeatureServiceUtilities;
import de.cismet.cismap.commons.featureservice.WebFeatureService;
import de.cismet.cismap.commons.featureservice.factory.AbstractFeatureFactory;
import de.cismet.cismap.commons.featureservice.factory.FeatureFactory;
import de.cismet.cismap.commons.gui.MappingComponent;
import de.cismet.cismap.commons.gui.piccolo.PFeature;
import de.cismet.cismap.commons.gui.piccolo.eventlistener.SelectionListener;
import de.cismet.cismap.commons.interaction.CismapBroker;

import de.cismet.commons.concurrency.CismetExecutors;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
@org.openide.util.lookup.ServiceProvider(service = QuerySearchMethod.class)
public class SelectQuerySearchMethod implements QuerySearchMethod {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(SelectQuerySearchMethod.class);

    //~ Instance fields --------------------------------------------------------

    private QuerySearch querySearch;
    private boolean searching = false;
    private SearchAndSelectThread searchThread;
    private Object lastLayer;

    //~ Methods ----------------------------------------------------------------

    @Override
    public void setQuerySearch(final QuerySearch querySearch) {
        this.querySearch = querySearch;
    }

    @Override
    public void actionPerformed(final Object layer, final String query) {
        if (LOG.isInfoEnabled()) {
            LOG.info((searching ? "Cancel" : "Search") + " button was clicked.");
        }

        if (searching) {
            if (searchThread != null) {
                if (lastLayer instanceof AbstractFeatureService) {
                    final FeatureFactory ff = ((AbstractFeatureService)lastLayer).getFeatureFactory();
                    if (ff instanceof AbstractFeatureFactory) {
                        ((AbstractFeatureFactory)ff).waitUntilInterruptedIsAllowed();
                    }
                }
                searchThread.cancel(true);
            }
        } else {
            lastLayer = layer;
            searchThread = new SearchAndSelectThread(layer, query);
            CismetExecutors.newSingleThreadExecutor().submit(searchThread);

            searching = true;
            querySearch.setControlsAccordingToState(searching);
        }
    }

    @Override
    public String toString() {
        return NbBundle.getMessage(SearchQuerySearchMethod.class, "SelectQuerySearchMethod.toString");
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class SearchAndSelectThread extends SwingWorker<List<FeatureWithId>, Void> {

        //~ Instance fields ----------------------------------------------------

        private Object layer;
        private String query;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new SearchAndSelectThread object.
         *
         * @param  layer  DOCUMENT ME!
         * @param  query  DOCUMENT ME!
         */
        public SearchAndSelectThread(final Object layer, final String query) {
            this.layer = layer;
            this.query = query;
        }

        //~ Methods ------------------------------------------------------------

        @Override
        protected List<FeatureWithId> doInBackground() throws Exception {
            List<FeatureWithId> features = null;

            if (layer instanceof WebFeatureService) {
                final WebFeatureService wfs = (WebFeatureService)layer;
                try {
                    final Element e = (Element)wfs.getQueryElement().clone();
                    final Element queryElement = e.getChild(
                            "Query",
                            Namespace.getNamespace("wfs", "http://www.opengis.net/wfs"));
                    queryElement.removeChild("Filter", Namespace.getNamespace("ogc", "http://www.opengis.net/ogc"));
                    final Element filterElement = new Element(
                            "Filter",
                            Namespace.getNamespace("ogc", "http://www.opengis.net/ogc"));
                    final SAXBuilder builder = new SAXBuilder();
                    final Document d = builder.build(new StringReader(query));
                    filterElement.addContent((Element)d.getRootElement().clone());
                    queryElement.addContent(0, filterElement);
                    features = wfs.getFeatureFactory()
                                .createFeatures(FeatureServiceUtilities.elementToString(e),
                                        CismapBroker.getInstance().getMappingComponent().getCurrentBoundingBox(),
                                        null,
                                        0,
                                        0,
                                        null);
                } catch (Exception ex) {
                    LOG.error("Error while retrieving features", ex);
                }
            } else if (layer instanceof AbstractFeatureService) {
                final AbstractFeatureService fs = (AbstractFeatureService)layer;
                features = fs.getFeatureFactory()
                            .createFeatures(
                                    query,
                                    CismapBroker.getInstance().getMappingComponent().getCurrentBoundingBox(),
                                    null,
                                    0,
                                    0,
                                    null);
            }

            return features;
        }

        @Override
        protected void done() {
            try {
                final List<FeatureWithId> features = get();

                if (isCancelled()) {
                    return;
                }

                if ((features != null) && (layer instanceof AbstractFeatureService)) {
                    final AbstractFeatureService service = (AbstractFeatureService)layer;
                    final List<Feature> toBeSelected = new ArrayList<Feature>();
                    final List<Feature> toBeUnselected = new ArrayList<Feature>();
                    for (final Object featureObject : service.getPNode().getChildrenReference()) {
                        final PFeature feature = (PFeature)featureObject;
                        final FeatureWithId featureWithId = (FeatureWithId)feature.getFeature();
                        if (isCancelled()) {
                            return;
                        }

                        if (isFeatureInList(featureWithId, features)) {
                            if (!feature.isSelected()) {
                                feature.setSelected(true);
                                final SelectionListener sl = (SelectionListener)CismapBroker.getInstance()
                                            .getMappingComponent()
                                            .getInputEventListener()
                                            .get(MappingComponent.SELECT);
                                sl.addSelectedFeature(feature);
                                toBeSelected.add(feature.getFeature());
                            }
                        } else {
                            if (feature.isSelected()) {
                                feature.setSelected(false);
                                final SelectionListener sl = (SelectionListener)CismapBroker.getInstance()
                                            .getMappingComponent()
                                            .getInputEventListener()
                                            .get(MappingComponent.SELECT);
                                sl.removeSelectedFeature(feature);
                                toBeUnselected.add(feature.getFeature());
                            }
                        }
                    }

                    ((DefaultFeatureCollection)CismapBroker.getInstance().getMappingComponent().getFeatureCollection())
                            .addToSelection(toBeSelected);
                    ((DefaultFeatureCollection)CismapBroker.getInstance().getMappingComponent().getFeatureCollection())
                            .unselect(toBeUnselected);
                }
            } catch (Exception e) {
                LOG.error("Error while selecting features", e);
            }
            searching = false;
            querySearch.setControlsAccordingToState(searching);
        }

        /**
         * DOCUMENT ME!
         *
         * @param   f     DOCUMENT ME!
         * @param   list  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        private boolean isFeatureInList(final FeatureWithId f, final List<FeatureWithId> list) {
            for (final FeatureWithId tmp : list) {
                if (tmp.getId() == f.getId()) {
                    return true;
                }
            }

            return false;
        }
    }
}
