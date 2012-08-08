/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * CoolObjectRenderer.java
 *
 * Created on 13. September 2007, 11:17
 */
package de.cismet.cids.tools.metaobjectrenderer;

import com.vividsolutions.jts.geom.Geometry;

import org.jdesktop.swingx.graphics.ReflectionRenderer;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import de.cismet.cismap.commons.BoundingBox;
import de.cismet.cismap.commons.raster.wms.simple.SimpleWMS;
import de.cismet.cismap.commons.retrieval.RetrievalEvent;
import de.cismet.cismap.commons.retrieval.RetrievalListener;

import de.cismet.tools.gui.Static2DTools;

/**
 * DOCUMENT ME!
 *
 * @author   hell
 * @version  $Revision$, $Date$
 */
public abstract class CoolObjectRenderer extends CustomMetaObjectRenderer {

    //~ Instance fields --------------------------------------------------------

    protected Image map;
    protected ImageIcon rechtsOben;
    protected SimpleWMS swms;
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());

    private Geometry geometry = null;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form CoolObjectRenderer.
     */
    public CoolObjectRenderer() {
        initComponents();
        try {
            final SAXBuilder builder = new SAXBuilder(false);

            final Document doc = builder.build(getClass().getResource("/coolobjectrenderer/backgroundWMS.xml")); // NOI18N
            final Element prefs = doc.getRootElement();
            swms = new SimpleWMS(prefs);
            swms.addRetrievalListener(new RetrievalListener() {

                    @Override
                    public void retrievalAborted(final RetrievalEvent retrievalEvent) {
                    }
                    @Override
                    public void retrievalComplete(final RetrievalEvent retrievalEvent) {
                        if (log.isDebugEnabled()) {
                            log.debug("completed"); // NOI18N
                        }
                        final Object o = retrievalEvent.getRetrievedObject();
                        if (o instanceof Image) {
                            map = (Image)o;
                            final float opacity = 0.8f;
                            final float fadeHeight = 0.3f;

                            final BufferedImage erg = new BufferedImage(
                                    map.getWidth(null),
                                    map.getHeight(null),
                                    BufferedImage.TYPE_4BYTE_ABGR);

                            final Graphics2D rg = erg.createGraphics();
                            rg.drawImage(map, null, null);
                            rg.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_IN));

                            rg.setPaint(
                                new GradientPaint(
                                    0,
                                    (float)(map.getHeight(null) / 2),
                                    new Color(0.0f, 0.0f, 0.0f, 0.0f),
                                    (float)(map.getWidth(null) / 2),
                                    (float)(map.getHeight(null) / 2),
                                    new Color(0.0f, 0.0f, 0.0f, .7f),
                                    true));
                            rg.fillRect(0, 0, map.getWidth(null), map.getHeight(null));
                            rg.setPaint(
                                new GradientPaint(
                                    (float)(map.getWidth(null) / 2),
                                    0,
                                    new Color(0.0f, 0.0f, 0.0f, 0.0f),
                                    (float)(map.getWidth(null) / 2),
                                    (float)(map.getHeight(null) / 2),
                                    new Color(0.0f, 0.0f, 0.0f, .7f),
                                    true));
                            rg.fillRect(0, 0, map.getWidth(null), map.getHeight(null));
                            rg.dispose();

                            map = erg;
                            repaint();
                        } else {
                            log.warn("no image"); // NOI18N
                        }
                    }
                    @Override
                    public void retrievalError(final RetrievalEvent retrievalEvent) {
                        log.error("error" + retrievalEvent.getErrorType()); // NOI18N
                    }
                    @Override
                    public void retrievalProgress(final RetrievalEvent retrievalEvent) {
                    }
                    @Override
                    public void retrievalStarted(final RetrievalEvent retrievalEvent) {
                        if (log.isDebugEnabled()) {
                            log.debug("retrievalStarted"); // NOI18N
                        }
                    }
                });
        } catch (Exception e) {
            log.error("Error during loading of the map info", e); // NOI18N
        }
        this.addComponentListener(new ComponentListener() {

                @Override
                public void componentHidden(final ComponentEvent e) {
                }
                @Override
                public void componentMoved(final ComponentEvent e) {
                }
                @Override
                public void componentResized(final ComponentEvent e) {
                    mapIt();
                }
                @Override
                public void componentShown(final ComponentEvent e) {
                    mapIt();
                }
            });
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  icon  DOCUMENT ME!
     */
    protected void setImageRechtsOben(final javax.swing.ImageIcon icon) {
        try {
            final ReflectionRenderer renderer = new ReflectionRenderer(0.5f, 0.4f, true);
            final BufferedImage tmp = new BufferedImage(icon.getIconWidth(),
                    icon.getIconHeight(),
                    BufferedImage.TYPE_4BYTE_ABGR);
            final Graphics g = tmp.createGraphics();
            g.drawImage(icon.getImage(), 0, 0, null);
            g.dispose();
            final BufferedImage ref = renderer.appendReflection(tmp);
            System.out.println(ref.getHeight());
            final ImageIcon bild2 = new ImageIcon(ref);
            rechtsOben = bild2;
        } catch (Exception e) {
            log.error("Error during loading of the LocationtypeImage", e); // NOI18N
            rechtsOben = null;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        final javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 400, Short.MAX_VALUE));
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 300, Short.MAX_VALUE));
    } // </editor-fold>//GEN-END:initComponents
    /**
     * DOCUMENT ME!
     */
    private void mapIt() {
        if (log.isDebugEnabled()) {
            log.debug("MAPIT"); // NOI18N
        }
        try {
            if (geometry != null) {
                BoundingBox bb = new BoundingBox(geometry.buffer(40d));
                final double midX = bb.getX1() + ((bb.getX2() - bb.getX1()) / 2);
                final double midY = bb.getY1() + ((bb.getY2() - bb.getY1()) / 2);
                double realWorldWidth = bb.getWidth();
                double realWorldHeight = bb.getHeight();
                final double widthToHeightRatio = getWidth() / getHeight();

                if ((widthToHeightRatio / (realWorldWidth / realWorldHeight)) > 1) {
                    // height is bestimmer ;-)
                    realWorldWidth = realWorldHeight * widthToHeightRatio;
                } else {
                    realWorldHeight = realWorldWidth * widthToHeightRatio;
                }
                bb = new BoundingBox(midX - (realWorldWidth / 2),
                        midY
                                - (realWorldHeight / 2),
                        midX
                                + (realWorldWidth / 2),
                        midY
                                + (realWorldHeight / 2));
                swms.setBoundingBox(bb);

                swms.setSize(getHeight(), getWidth());
                swms.retrieve(true);
            } else {
            }
        } catch (Exception e) {
            log.warn("Error while displaying the map.", e); // NOI18N
        }
    }
    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        if (map != null) {
            g.drawImage(map, 0, 0, null);
        } else {
            log.info("map==null"); // NOI18N
        }
        g.setColor(Color.WHITE);
        // g.drawLine(0,0,getWidth(),0);
        // g.drawLine(0,getHeight()-1,getWidth(),getHeight()-1);

        ((Graphics2D)g).setPaint(new GradientPaint(
                0,
                0,
                Static2DTools.getAlphaColor(this.getBackground().darker(), 150),
                0,
                60,
                Static2DTools.getAlphaColor(this.getBackground(), 150)));
        // g.setColor(new Color(230,230,230,150));

        g.fillRect(0, 5, getWidth(), 55);

        // ((Graphics2D)g).setPaint(new
        // GradientPaint(0,getHeight()-60,Static2DTools.getAlphaColor(this.getBackground().darker(),150),0,getHeight(),Static2DTools.getAlphaColor(this.getBackground(),150)));
        final Color to = Static2DTools.getAlphaColor(this.getBackground().darker(), 150);
        final Color from = Static2DTools.getAlphaColor(this.getBackground(), 150);
        ((Graphics2D)g).setPaint(new GradientPaint(0, getHeight() - 60, from, 0, getHeight(), to));
        g.fillRect(0, getHeight() - 60, getWidth(), 55);

        if (rechtsOben != null) {
            g.drawImage(rechtsOben.getImage(), getWidth() - rechtsOben.getIconWidth() - 10, 10, null);
        }
//        Rectangle r=StaticSwingTools.getComponentsExtent(lblStrasse,lblPLZOrt,lblTel,lblFax,jLabel3,jLabel4,jLabel5);
//
//        g.setColor(new Color(250,250,250,100));
//        g.fillRoundRect((int)r.getX()-5,(int)r.getY()-5,(int)r.getWidth()+10,(int)r.getHeight()+10,10,10);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Geometry getGeometry() {
        return geometry;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  geometry  DOCUMENT ME!
     */
    public void setGeometry(final Geometry geometry) {
        this.geometry = geometry;
    }
}
