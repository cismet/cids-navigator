/*
 * DescriptionPane.java
 *
 * Created on 20. Oktober 2006, 11:06
 */
package Sirius.navigator.ui;

import Sirius.navigator.resource.ResourceManager;
import Sirius.navigator.types.treenode.DefaultMetaTreeNode;
import Sirius.navigator.types.treenode.ObjectTreeNode;
import Sirius.navigator.ui.status.DefaultStatusChangeSupport;
import Sirius.navigator.ui.status.Status;
import Sirius.navigator.ui.status.StatusChangeListener;
import Sirius.navigator.ui.status.StatusChangeSupport;
import Sirius.server.middleware.types.MetaClass;
import Sirius.server.middleware.types.MetaObject;
import calpa.html.CalCons;
import calpa.html.CalHTMLPane;
import calpa.html.CalHTMLPreferences;
import calpa.html.DefaultCalHTMLObserver;
import de.cismet.cids.editors.CidsObjectEditorFactory;
import de.cismet.cids.tools.metaobjectrenderer.CidsObjectRendererFactory;
import de.cismet.cids.tools.metaobjectrenderer.ScrollableFlowPanel;
import de.cismet.tools.CurrentStackTrace;
import de.cismet.tools.collections.MultiMap;
import de.cismet.tools.gui.ComponentWrapper;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Paint;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

/**
 *
 * @author  thorsten.hell@cismet.de
 */
public class DescriptionPane extends JPanel implements StatusChangeSupport {

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private final DefaultStatusChangeSupport statusChangeSupport;
    private String welcomePage;
    private PrintableJPanel rendererPanel;
    private JComponent wrappedWaitingPanel;
    CalHTMLPreferences htmlPrefs = new CalHTMLPreferences();
    JPanel panRenderer = new JPanel();
    GridBagConstraints gridBagConstraints;
    SwingWorker worker=null;
    DefaultCalHTMLObserver htmlObserver = new DefaultCalHTMLObserver() {

        public void statusUpdate(CalHTMLPane calHTMLPane, int status, URL uRL, int i0, String string) {
            super.statusUpdate(calHTMLPane, status, uRL, i0, string);
            // log.debug("DescriptionPane.log.StatusUpdate: Status:"+status+"  Url:"+uRL);
            if (status == 1) {
                htmlPane.showHTMLDocument("");
                statusChangeSupport.fireStatusChange(ResourceManager.getManager().getString("descriptionpane.status.error"), Status.MESSAGE_POSITION_3, Status.ICON_DEACTIVATED, Status.ICON_ACTIVATED);
            } else if (status == 10 || status == 11) {
                statusChangeSupport.fireStatusChange(ResourceManager.getManager().getString("descriptionpane.status.loading"), Status.MESSAGE_POSITION_3, Status.ICON_BLINKING, Status.ICON_DEACTIVATED);
            } else if (status == 14) {
                statusChangeSupport.fireStatusChange(ResourceManager.getManager().getString("descriptionpane.status.loaded"), Status.MESSAGE_POSITION_3, Status.ICON_ACTIVATED, Status.ICON_DEACTIVATED);
            }
        }

        public void linkActivatedUpdate(CalHTMLPane calHTMLPane, URL uRL, String string, String string0) {
            super.linkActivatedUpdate(calHTMLPane, uRL, string, string0);
        }

        public void linkFocusedUpdate(CalHTMLPane calHTMLPane, URL uRL) {
            super.linkFocusedUpdate(calHTMLPane, uRL);
        }
    };

    /** Creates new form DescriptionPane */
    public DescriptionPane() {
        htmlPrefs.setAutomaticallyFollowHyperlinks(true);
        htmlPrefs.setOptimizeDisplay(CalCons.OPTIMIZE_ALL);
        htmlPrefs.setDisplayErrorDialogs(false);
        htmlPrefs.setLoadImages(true);

        //htmlPrefs.setDisplayErrorDialogs(true);
        initComponents();

        showHTML();

        this.statusChangeSupport = new DefaultStatusChangeSupport(this);
        try {
            StringBuffer buffer = new StringBuffer();
            String string = null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(ResourceManager.getManager().getNavigatorResourceAsStream(ResourceManager.getManager().getString("descriptionpane.html.welcome"))));

            while ((string = reader.readLine()) != null) {
                buffer.append(string);
            }

            this.welcomePage = buffer.toString();
        } catch (IOException ioexp) {
        }

        scpRenderer.setViewportView(panRenderer);
        panRenderer.setLayout(new GridBagLayout());
        ComponentWrapper cw = CidsObjectEditorFactory.getInstance().getComponentWrapper();
        if (cw != null) {
            wrappedWaitingPanel = (JComponent) cw.wrapComponent(lblRendererCreationWaitingLabel);
        }

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblRendererCreationWaitingLabel = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        scpRenderer = new javax.swing.JScrollPane();
        htmlPane = new CalHTMLPane(htmlPrefs,htmlObserver,"cismap");

        lblRendererCreationWaitingLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblRendererCreationWaitingLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Sirius/navigator/resource/img/load.png"))); // NOI18N

        jButton1.setForeground(javax.swing.UIManager.getDefaults().getColor("Button.background"));
        jButton1.setText("jButton1");

        setLayout(new java.awt.CardLayout());

        scpRenderer.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        add(scpRenderer, "objects");

        htmlPane.setDoubleBuffered(true);
        add(htmlPane, "html");
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private calpa.html.CalHTMLPane htmlPane;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel lblRendererCreationWaitingLabel;
    private javax.swing.JScrollPane scpRenderer;
    // End of variables declaration//GEN-END:variables

    private void showHTML() {
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                ((CardLayout) getLayout()).show(DescriptionPane.this, "html");
            }
        });
    }

    private void showObjects() {
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                ((CardLayout) getLayout()).show(DescriptionPane.this, "objects");
            }
        });
    }

    public void clear() {
        Runnable r = new Runnable() {

            public void run() {
                log.fatal("clear",new CurrentStackTrace());
                htmlPane.showHTMLDocument("");
                panRenderer.removeAll();
                repaint();
            }
        };

        if (!EventQueue.isDispatchThread()) {
            EventQueue.invokeLater(r);
        } else {
            r.run();
        }
    }

    public void addStatusChangeListener(StatusChangeListener listener) {
        this.statusChangeSupport.addStatusChangeListener(listener);
    }

    public void removeStatusChangeListener(StatusChangeListener listener) {
        this.statusChangeSupport.removeStatusChangeListener(listener);
    }

    public void setPage(String page) {
        try {
            log.info("setPage:" + page);
            htmlPane.stopAll();
            htmlPane.showHTMLDocument(new URL(page));
        } catch (Exception e) {
            log.info("Fehler bei setPage", e);
            htmlPane.showHTMLDocument("");
            statusChangeSupport.fireStatusChange(ResourceManager.getManager().getString("descriptionpane.status.error"), Status.MESSAGE_POSITION_3, Status.ICON_DEACTIVATED, Status.ICON_ACTIVATED);

        }
    }


    //Multiple Objects
    public void setNodesDescription(final Vector objects) {


        showObjects();
        clear();

        if (worker!=null){
            worker.cancel(true);
        }
        worker=new SwingWorker<JComponent, JComponent>() {

            List<JComponent> all = new Vector<JComponent>();

            @Override
            protected JComponent doInBackground() throws Exception {
                Vector filteredObjects = new Vector(objects);
                MultiMap objectsByClass = new MultiMap();
                for (Object object : objects) {
                    if (object != null && !((DefaultMetaTreeNode) object).isWaitNode() && !((DefaultMetaTreeNode) object).isRootNode() && !((DefaultMetaTreeNode) object).isPureNode() && ((DefaultMetaTreeNode) object).isObjectNode()) {
                        try {
                            ObjectTreeNode n = (ObjectTreeNode) object;
                            objectsByClass.put(n.getMetaClass(), n);
                        } catch (Throwable t) {
                            log.warn("Fehler beim Vorbereiten der Darstellung der Objekte", t);
                        }
                    }
                }
                int y = 0;
                Iterator it = objectsByClass.keySet().iterator();


                //splMain.setDividerLocation(1.0d);
                while (it.hasNext()) {
                    // JSeparator sep=new JSeparator(JSeparator.HORIZONTAL);
                    Object key = it.next();
                    List l = (List) objectsByClass.get(key);

                    Vector<MetaObject> v = new Vector<MetaObject>();
                    for (Object o : l) {
                        v.add(((ObjectTreeNode) o).getMetaObject());
                    }
                    MetaClass mc = ((MetaObject) v.toArray()[0]).getMetaClass();


                    //Hier wird schon der Aggregationsrenderer gebaut, weil Einzelrenderer angezeigt werden fall getAggregationrenderer null lifert (keiner da, oder Fehler)
                    JComponent aggrRendererTester = null;

                    if (l.size() > 1) {
                        //aggrRendererTester = MetaObjectrendererFactory.getInstance().getAggregationRenderer(v, mc.getName() + " (" + v.size() + ")");
                        aggrRendererTester = CidsObjectRendererFactory.getInstance().getAggregationRenderer(v, mc.getName() + " (" + v.size() + ")");
                    }
                    if (aggrRendererTester == null) {
                        log.warn("AggregationRenderer was null. Will use SingleRenderer");
                        for (Object object : l) {
                            ObjectTreeNode otn = (ObjectTreeNode) object;
                            //final JComponent comp = MetaObjectrendererFactory.getInstance().getSingleRenderer(otn.getMetaObject(), otn.getMetaClass().getName() + ": " + otn);
                            final JComponent comp = CidsObjectRendererFactory.getInstance().getSingleRenderer(otn.getMetaObject(), otn.getMetaClass().getName() + ": " + otn);
                            otn.getMetaObject().getBean().addPropertyChangeListener(new PropertyChangeListener() {

                                public void propertyChange(PropertyChangeEvent evt) {
                                    comp.repaint();
                                }
                            });
                            publish(comp);
                        }
                    } else {
                        publish(aggrRendererTester);
                    }
                }
                return null;
            }

            @Override
            protected void done() {
                all.clear();
                worker=null;
            }

            @Override
            protected void process(List<JComponent> chunks) {
                int y = all.size();
                for (JComponent comp : chunks) {
                    try {

                        GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
                        gridBagConstraints.gridx = 0;
                        gridBagConstraints.gridy = y;
                        gridBagConstraints.weightx = 1;
                        gridBagConstraints.fill = gridBagConstraints.HORIZONTAL;
                        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                        panRenderer.add(comp, gridBagConstraints);
                        panRenderer.revalidate();
                        panRenderer.repaint();

                        y++;
                    } catch (Throwable t) {
                        log.error("Fehler beim Rendern des MetaObjectrenderer", t);
                    }
                }
                all.addAll(chunks);
            }
        };
        if (worker!=null){
            worker.execute();
        }
        

    }

    //Single Object
    public void setNodeDescription(final Object object) {
        if (object != null && !((DefaultMetaTreeNode) object).isWaitNode() && !((DefaultMetaTreeNode) object).isRootNode()) {
            //colPane.setCollapsed(true);
            showObjects();

            EventQueue.invokeLater(new Runnable() {

                public void run() {
                    panRenderer.removeAll();
                    GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
                    gridBagConstraints.gridx = 0;
                    gridBagConstraints.gridy = 0;
                    gridBagConstraints.weightx = 1;
                    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
                    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                    panRenderer.add(wrappedWaitingPanel, gridBagConstraints);
                    repaint();
                }
            });


            final DefaultMetaTreeNode n = (DefaultMetaTreeNode) object;

            final String descriptionURL = n.getDescription();


            // besorge MO zum parametrisieren der URL
            if (n.isObjectNode()) {
                if (worker!=null){
                    worker.cancel(true);
                }
                worker=new javax.swing.SwingWorker<JComponent, Void>() {

                    @Override
                    protected JComponent doInBackground() throws Exception {
                        MetaObject o = null;
                        MetaClass c = null;

                        o = ((ObjectTreeNode) n).getMetaObject();


                        log.debug("setNodeDescription");
                        try {
                            c = ((ObjectTreeNode) n).getMetaClass();
                        } catch (Throwable t) {
                            log.error("kein Klasse f\u00FCr Objectreenode", t);
                        }
                        
                        //final JComponent comp = MetaObjectrendererFactory.getInstance().getSingleRenderer(o, n.toString());
                        final JComponent jComp = CidsObjectRendererFactory.getInstance().getSingleRenderer(o, n.toString());
                        o.getBean().addPropertyChangeListener(new PropertyChangeListener() {

                            public void propertyChange(PropertyChangeEvent evt) {
                                jComp.repaint();
                            }
                        });

                        return jComp;

                    }

                    @Override
                    protected void done() {
                        try {
                            JComponent comp = get();
                            //splMain.setDividerLocation(finalWidthRatio);
                            panRenderer.removeAll();//log.fatal("All removed");
                            gridBagConstraints = new java.awt.GridBagConstraints();
                            gridBagConstraints.gridx = 0;
                            gridBagConstraints.gridy = 0;
                            gridBagConstraints.weightx = 1;
                            gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
                            gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                            panRenderer.add(comp, gridBagConstraints);//log.fatal("Comp added");
                            panRenderer.revalidate();
                            revalidate();
                            repaint();
                            worker=null;
                        } catch (Exception e) {
                            log.error("Exception occurred during renderercreation", e);
                        }
                    }
                };
                if (worker!=null) {
                    worker.execute();
                }
                


            } else if (n.isClassNode()) {
//                try {
//                    c = ((ClassTreeNode) n).getMetaClass();
//                } catch (Throwable t) {
//                    log.error(t);
//                }
            } else if (n.isPureNode()) {
                showHTML();
            //splMain.setDividerLocation(0d);
            }


//            try {
//                descriptionURL = URLParameterizer.parameterizeURL(descriptionURL, c, o, Sirius.navigator.connection.SessionManager.getSession().getUser());
//            } catch (Throwable t) {
//                log.info("keine Parametrisierung m\u00F6glich url wie unparametrisiert verwendet", t);
//            }

            if (log.isDebugEnabled()) {
                log.debug("loading description from url '" + descriptionURL + "'");
            }


            this.setPage(descriptionURL);
        } else {
            //if(logger.isDebugEnabled())logger.debug("no description url available");
            statusChangeSupport.fireStatusChange(ResourceManager.getManager().getString("descriptionpane.status.nodescription"), Status.MESSAGE_POSITION_3, Status.ICON_DEACTIVATED, Status.ICON_DEACTIVATED);

            //this.setText("<html><body><h3>" + ResourceManager.getManager().getString("descriptionpane.welcome") + "</h3></body></html>");
            htmlPane.showHTMLDocument(welcomePage);

        }
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Paint p = g2d.getPaint();
        GradientPaint gp = new GradientPaint(0, 0, getBackground(), getWidth(), getHeight(), Color.WHITE, false);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    //super.paintComponent(g2d);
    }

    class PrintableJPanel extends ScrollableFlowPanel implements Printable {

        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
            if (pageIndex > 0) {
                return (NO_SUCH_PAGE);
            } else {
                Graphics2D g2d = (Graphics2D) graphics;
                g2d.scale(0.75, 0.75);
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
                // Turn off double buffering
                paint(g2d);
                // Turn double buffering back on
                return (PAGE_EXISTS);
            }
        }
    }
}
