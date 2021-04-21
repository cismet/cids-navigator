/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.tools;

/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/

import lombok.Getter;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.imageio.ImageIO;

import de.cismet.cismap.commons.Crs;
import de.cismet.cismap.commons.XBoundingBox;
import de.cismet.cismap.commons.gui.MappingComponent;
import de.cismet.cismap.commons.gui.layerwidget.ActiveLayerModel;
import de.cismet.cismap.commons.interaction.CismapBroker;

import de.cismet.connectioncontext.ConnectionContext;
import de.cismet.connectioncontext.ConnectionContextStore;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * DOCUMENT ME!
 *
 * @param    <C>
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public abstract class MapImageFactory<C extends MapImageFactoryConfiguration> implements ByteArrayFactory,
    ConnectionContextStore {

    //~ Instance fields --------------------------------------------------------

    @Getter private ConnectionContext connectionContext = ConnectionContext.createDummy();

    //~ Methods ----------------------------------------------------------------

    @Override
    public byte[] create(final String configuration) throws Exception {
        final C mapConfiguration = extractConfiguration(configuration);

        initMap(mapConfiguration);

        final BufferedImage bufferedImage = generateMap(mapConfiguration);

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", new File("/home/jruiz/test.png"));
        ImageIO.write(bufferedImage, "png", baos);
        return baos.toByteArray();
    }

    /**
     * DOCUMENT ME!
     *
     * @param   configuration  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    protected abstract C extractConfiguration(final String configuration) throws Exception;

    /**
     * DOCUMENT ME!
     *
     * @param   configuration  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    protected abstract BufferedImage generateMap(final C configuration) throws Exception;

    /**
     * DOCUMENT ME!
     *
     * @param  mapConfiguration  DOCUMENT ME!
     */
    protected void initMap(final C mapConfiguration) {
        final MappingComponent mappingComponent = new MappingComponent();
        final Dimension d = new Dimension(1, 1);
        mappingComponent.setPreferredSize(d);
        mappingComponent.setSize(d);

        final ActiveLayerModel mappingModel = new ActiveLayerModel();
        mappingModel.addHome(
            new XBoundingBox(
                mapConfiguration.getBbX1(),
                mapConfiguration.getBbY1(),
                mapConfiguration.getBbX2(),
                mapConfiguration.getBbY2(),
                mapConfiguration.getSrs(),
                false));
        final Crs crs = new Crs(mapConfiguration.getSrs(), "", "", true, true);
        mappingModel.setSrs(crs);
        mappingModel.setDefaultHomeSrs(crs);

        mappingComponent.setInteractionMode(MappingComponent.SELECT);
        mappingComponent.setMappingModel(mappingModel);
        mappingComponent.gotoInitialBoundingBox();
        mappingComponent.unlock();

        CismapBroker.getInstance().setMappingComponent(mappingComponent);
        CismapBroker.getInstance().setSrs(crs);
    }

    @Override
    public void initWithConnectionContext(final ConnectionContext connectionContext) {
        this.connectionContext = connectionContext;
    }
}
