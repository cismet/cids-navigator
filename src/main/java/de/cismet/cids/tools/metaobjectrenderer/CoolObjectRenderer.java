/*
 * CoolObjectRenderer.java
 *
 * Created on 13. September 2007, 11:17
 */

package de.cismet.cids.tools.metaobjectrenderer;

import com.vividsolutions.jts.geom.Geometry;
import de.cismet.cids.tools.metaobjectrenderer.CustomMetaObjectRenderer;
import de.cismet.cismap.commons.BoundingBox;
import de.cismet.cismap.commons.raster.wms.simple.SimpleWMS;
import de.cismet.cismap.commons.retrieval.RetrievalEvent;
import de.cismet.cismap.commons.retrieval.RetrievalListener;
import de.cismet.tools.gui.Static2DTools;
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
import org.jdesktop.swingx.graphics.ReflectionRenderer;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 *
 * @author  hell
 */
public abstract class CoolObjectRenderer extends  CustomMetaObjectRenderer  {
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    protected Image map;
    protected ImageIcon rechtsOben;
    protected SimpleWMS swms;
   
    private Geometry geometry=null;
    /** Creates new form CoolObjectRenderer */
    public CoolObjectRenderer() {
        initComponents();
        try {
            SAXBuilder builder = new SAXBuilder(false);
            
            Document doc=builder.build(getClass().getResource("/coolobjectrenderer/backgroundWMS.xml"));
            Element prefs=doc.getRootElement();
            swms=new SimpleWMS(prefs);
            swms.addRetrievalListener(new RetrievalListener() {
                public void retrievalAborted(RetrievalEvent retrievalEvent) {
                }
                public void retrievalComplete(RetrievalEvent retrievalEvent) {
                    log.debug("FERTICH");
                    Object o=retrievalEvent.getRetrievedObject();
                    if (o instanceof Image){
                        map=(Image)o;
                        float opacity = 0.8f;
                        float fadeHeight = 0.3f;
                        
                        BufferedImage erg=new BufferedImage(map.getWidth(null), map.getHeight(null),BufferedImage.TYPE_4BYTE_ABGR);
                        
                        
                        Graphics2D rg = erg.createGraphics();
                        rg.drawImage( map, null,null );
                        rg.setComposite( AlphaComposite.getInstance( AlphaComposite.DST_IN ) );
                        
                        
                        
                        rg.setPaint(
                                new GradientPaint(
                                0,(float)(map.getHeight(null)/2), new Color( 0.0f, 0.0f, 0.0f, 0.0f ),
                                (float)(map.getWidth(null)/2), (float)(map.getHeight(null)/2), new Color( 0.0f, 0.0f, 0.0f, .7f )
                                ,true)
                                );
                        rg.fillRect( 0, 0, map.getWidth(null), map.getHeight(null) );
                        rg.setPaint(
                                new GradientPaint(
                                (float)(map.getWidth(null)/2), 0, new Color( 0.0f, 0.0f, 0.0f, 0.0f ),
                                (float)(map.getWidth(null)/2), (float)(map.getHeight(null)/2), new Color( 0.0f, 0.0f, 0.0f, .7f )
                                ,true)
                                );
                        rg.fillRect( 0, 0, map.getWidth(null), map.getHeight(null) );
                        rg.dispose();
                        
                        map=erg;
                        repaint();
                    } else {
                        log.warn("kein image");
                    }
                }
                public void retrievalError(RetrievalEvent retrievalEvent) {
                    System.out.println("fehler"+ retrievalEvent.getErrorType());
                }
                public void retrievalProgress(RetrievalEvent retrievalEvent) {
                }
                public void retrievalStarted(RetrievalEvent retrievalEvent) {
                    log.debug("retrievalStarted");
                }
            });
        } catch (Exception e) {
            log.error("Fehler beim Laden der KartenInfo",e);
        }
        this.addComponentListener(new ComponentListener() {
            public void componentHidden(ComponentEvent e) {
            }
            public void componentMoved(ComponentEvent e) {
            }
            public void componentResized(ComponentEvent e) {
                mapIt();
            }
            public void componentShown(ComponentEvent e) {
                mapIt();
            }
        });
    }
    
    protected void setImageRechtsOben(javax.swing.ImageIcon icon) {
        try {
            
            ReflectionRenderer renderer = new ReflectionRenderer(0.5f,0.4f,true);
            BufferedImage tmp=new BufferedImage(icon.getIconWidth(),icon.getIconHeight(),BufferedImage.TYPE_4BYTE_ABGR);
            Graphics g = tmp.createGraphics();
            g.drawImage(icon.getImage(),0,0,null);
            g.dispose();
            BufferedImage ref=renderer.appendReflection(tmp);
            System.out.println(ref.getHeight());
            ImageIcon bild2=new ImageIcon(ref);
            rechtsOben=bild2;
        } catch (Exception e){
            log.error("Fehler beim Laden des LocationtypeImage",e);
            rechtsOben=null;
        }
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    private void mapIt() {
        log.debug("MAPIT");
        try {
            if (geometry!=null) {
                BoundingBox bb=new BoundingBox(geometry.buffer(40d));
                double midX=bb.getX1()+(bb.getX2()-bb.getX1())/2;
                double midY=bb.getY1()+(bb.getY2()-bb.getY1())/2;
                double realWorldWidth=bb.getWidth();
                double realWorldHeight=bb.getHeight();
                double widthToHeightRatio=getWidth()/getHeight();
                
                
                if (widthToHeightRatio/(realWorldWidth/realWorldHeight)>1) {
                    //height is bestimmer ;-)
                    realWorldWidth=realWorldHeight*widthToHeightRatio;
                } else {
                    realWorldHeight=realWorldWidth*widthToHeightRatio;
                }
                bb=new BoundingBox(midX-realWorldWidth/2,midY-realWorldHeight/2,midX+realWorldWidth/2,midY+realWorldHeight/2);
                swms.setBoundingBox(bb);
                
                swms.setSize(getHeight(),getWidth());
                swms.retrieve(true);
            } else {
                
            }
        } catch(Exception e) {
            log.warn("Fehler beim Darstellen der Karte.",e);
        }
    }
    @Override
    public void paintComponent(Graphics g) {
        
        super.paintComponent(g);
        if (map!=null){
            g.drawImage(map,0,0,null);
        } else {
            log.info("map==null");
        }
        g.setColor(Color.WHITE);
        // g.drawLine(0,0,getWidth(),0);
        // g.drawLine(0,getHeight()-1,getWidth(),getHeight()-1);
        
        
        ((Graphics2D)g).setPaint(new GradientPaint(0,0,Static2DTools.getAlphaColor(this.getBackground().darker(),150),0,60,Static2DTools.getAlphaColor(this.getBackground(),150)));
        //g.setColor(new Color(230,230,230,150));
        
        g.fillRect(0,5,getWidth(),55);
        
        //((Graphics2D)g).setPaint(new GradientPaint(0,getHeight()-60,Static2DTools.getAlphaColor(this.getBackground().darker(),150),0,getHeight(),Static2DTools.getAlphaColor(this.getBackground(),150)));
        Color to=Static2DTools.getAlphaColor(this.getBackground().darker(),150);
        Color from=Static2DTools.getAlphaColor(this.getBackground(),150);
        ((Graphics2D)g).setPaint(new GradientPaint(0,getHeight()-60,from,0,getHeight(),to));
        g.fillRect(0,getHeight()-60,getWidth(),55);
        
        
        if (rechtsOben!=null) {
            g.drawImage(rechtsOben.getImage(),getWidth()-rechtsOben.getIconWidth()-10,10,null);
        }
//        Rectangle r=StaticSwingTools.getComponentsExtent(lblStrasse,lblPLZOrt,lblTel,lblFax,jLabel3,jLabel4,jLabel5);
//        
//        g.setColor(new Color(250,250,250,100));
//        g.fillRoundRect((int)r.getX()-5,(int)r.getY()-5,(int)r.getWidth()+10,(int)r.getHeight()+10,10,10);
        
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }
    
}
