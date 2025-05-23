/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui;

import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.exception.ConnectionException;
import Sirius.navigator.method.MethodManager;
import Sirius.navigator.plugin.interfaces.EmbededControlBar;
import Sirius.navigator.resource.ResourceManager;
import Sirius.navigator.types.treenode.DefaultMetaTreeNode;
import Sirius.navigator.types.treenode.ObjectTreeNode;
import Sirius.navigator.ui.attributes.renderer.NoDescriptionRenderer;
import Sirius.navigator.ui.status.DefaultStatusChangeSupport;
import Sirius.navigator.ui.status.Status;
import Sirius.navigator.ui.status.StatusChangeListener;
import Sirius.navigator.ui.status.StatusChangeSupport;
import Sirius.navigator.ui.tree.MetaCatalogueTree;

import Sirius.server.middleware.types.MetaClass;
import Sirius.server.middleware.types.MetaObject;
import Sirius.server.middleware.types.MetaObjectNode;
import Sirius.server.newuser.permission.PermissionHolder;

import org.openide.util.Lookup;
import org.openide.util.WeakListeners;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingWorker;

import de.cismet.cids.dynamics.CidsBean;

import de.cismet.cids.editors.CidsObjectEditorFactory;

import de.cismet.cids.navigator.utils.MetaTreeNodeStore;

import de.cismet.cids.tools.metaobjectrenderer.CidsBeanAggregationHandler;
import de.cismet.cids.tools.metaobjectrenderer.CidsBeanRenderer;
import de.cismet.cids.tools.metaobjectrenderer.CidsObjectRendererFactory;
import de.cismet.cids.tools.metaobjectrenderer.ScrollableFlowPanel;
import de.cismet.cids.tools.metaobjectrenderer.SelfDisposingPanel;

import de.cismet.connectioncontext.ConnectionContext;
import de.cismet.connectioncontext.ConnectionContextStore;

import de.cismet.tools.CismetThreadPool;

import de.cismet.tools.collections.MultiMap;

import de.cismet.tools.gui.ComponentWrapper;
import de.cismet.tools.gui.CoolEditor;
import de.cismet.tools.gui.WrappedComponent;
import de.cismet.tools.gui.breadcrumb.BreadCrumb;
import de.cismet.tools.gui.breadcrumb.DefaultBreadCrumbModel;
import de.cismet.tools.gui.breadcrumb.LinkStyleBreadCrumbGui;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten.hell@cismet.de
 * @version  $Revision$, $Date$
 */
public abstract class DescriptionPane extends JPanel implements EmbededControlBar,
    StatusChangeSupport,
    ConnectionContextStore {

    //~ Static fields/initializers ---------------------------------------------

    protected static final ResourceManager RESOURCE = ResourceManager.getManager();
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(DescriptionPane.class);

    //~ Instance fields --------------------------------------------------------

    protected final DefaultStatusChangeSupport statusChangeSupport;
    protected final JPanel panRenderer = new JPanel();
    protected final JComponent wrappedWaitingPanel;
    protected SwingWorker worker = null;
    protected GridBagConstraints gridBagConstraints;
    protected String welcomePage;
    protected String errorPage;
    protected boolean showsWaitScreen = false;
    protected DefaultBreadCrumbModel breadCrumbModel = new DefaultBreadCrumbModel();
    protected LinkStyleBreadCrumbGui breadCrumbGui;
    // will only be accessed in EDT !
    private transient boolean fullScreenRenderer;
    private final MultiMap sharedHandlersHM = new MultiMap();
    private DefaultMetaTreeNode lastNode;

    private ConnectionContext connectionContext = ConnectionContext.createDummy();

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel controlBar;
    private javax.swing.JButton editButton;
    protected javax.swing.JPanel jPanel2;
    protected javax.swing.JLabel lblRendererCreationWaitingLabel;
    protected javax.swing.JPanel panBreadCrump;
    protected javax.swing.JPanel panObjects;
    private javax.swing.JButton refreshButton;
    protected javax.swing.JScrollPane scpRenderer;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form DescriptionPane.
     */
    public DescriptionPane() {
        initComponents();

        breadCrumbGui = new LinkStyleBreadCrumbGui(breadCrumbModel);
        panBreadCrump.add(breadCrumbGui, BorderLayout.CENTER);
        this.statusChangeSupport = new DefaultStatusChangeSupport(this);
        this.fullScreenRenderer = false;
        BufferedReader reader = null;
        try {
            final StringBuffer welcomeBuffer = new StringBuffer();
            String welcomeString = null;
            reader = new BufferedReader(new InputStreamReader(
                        RESOURCE.getNavigatorJarResourceAsStream("doc/welcome.html"))); // NOI18N

            while ((welcomeString = reader.readLine()) != null) {
                welcomeBuffer.append(welcomeString);
            }
            this.welcomePage = welcomeBuffer.toString();

            final StringBuffer errorBuffer = new StringBuffer();
            String errorString = null;
            reader = new BufferedReader(new InputStreamReader(
                        RESOURCE.getNavigatorJarResourceAsStream("doc/error.xhtml"), // NOI18N
                        "UTF-8")); // NOI18N
            while ((errorString = reader.readLine()) != null) {
                errorBuffer.append(errorString);
            }
            this.errorPage = errorBuffer.toString();
        } catch (final IOException ioexp) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Couldn't read default pages.", ioexp); // NOI18N
            }
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    LOG.warn("Can't close reader.", ex); // NOI18N
                }
            }
        }

        scpRenderer.setViewportView(panRenderer);
        panRenderer.setLayout(new GridBagLayout());

        /*
         * Following try-catch-block only exists to enable the user to user DescriptionPaneTest which runs outside the
         * navigator. When running DescriptionPaneTest a RuntimeException will be thrown because no SessionManager is
         * available.
         */
        ComponentWrapper cw = null;
        try {
            cw = CidsObjectEditorFactory.getInstance().getComponentWrapper();
        } catch (Exception e) {
        }

        if (cw != null) {
            wrappedWaitingPanel = (JComponent)cw.wrapComponent(lblRendererCreationWaitingLabel);
        } else {
            wrappedWaitingPanel = null;
        }

        try {
            final Collection<? extends CidsBeanAggregationHandler> allSharedHandlers = Lookup.getDefault()
                        .lookupAll(CidsBeanAggregationHandler.class);
            for (final CidsBeanAggregationHandler sharedHandler : allSharedHandlers) {
                sharedHandlersHM.put(sharedHandler.getSourceMetaClassTablename().toLowerCase(), sharedHandler);
            }
            for (final String tableName : (Set<String>)sharedHandlersHM.keySet()) {
                Collections.sort((List<CidsBeanAggregationHandler>)sharedHandlersHM.get(tableName),
                    new Comparator<CidsBeanAggregationHandler>() {

                        @Override
                        public int compare(final CidsBeanAggregationHandler o1,
                                final CidsBeanAggregationHandler o2) {
                            if ((o1 != null) && (o2 == null)) {
                                return 1;
                            } else if ((o1 == null) && (o2 != null)) {
                                return -1;
                            } else if ((o1 != null) && (o2 != null)) {
                                return Integer.compare(o1.getPriority(), o2.getPriority());
                            } else { // (o1 == null) && (o2 == null)
                                return 0;
                            }
                        }
                    });
            }
        } catch (final Exception ex) {
            LOG.warn("error while setting up sharedHandlers", ex);
        }

        startNoDescriptionRenderer();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lblRendererCreationWaitingLabel = new javax.swing.JLabel();
        controlBar = new javax.swing.JPanel();
        refreshButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        panObjects = new javax.swing.JPanel();
        scpRenderer = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        panBreadCrump = new javax.swing.JPanel();

        lblRendererCreationWaitingLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblRendererCreationWaitingLabel.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/Sirius/navigator/resource/img/load.png"))); // NOI18N

        controlBar.setLayout(new java.awt.GridBagLayout());

        refreshButton.setIcon(RESOURCE.getIcon("reload16.gif"));
        refreshButton.setToolTipText(org.openide.util.NbBundle.getMessage(
                DescriptionPane.class,
                "DescriptionPane.refreshButton.toolTipText")); // NOI18N
        refreshButton.setActionCommand(org.openide.util.NbBundle.getMessage(
                DescriptionPane.class,
                "DescriptionPane.refreshButton.actionCommand",
                new Object[] {}));                             // NOI18N
        refreshButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        refreshButton.setContentAreaFilled(false);
        refreshButton.setEnabled(false);
        refreshButton.setFocusPainted(false);
        refreshButton.setMaximumSize(new java.awt.Dimension(16, 16));
        refreshButton.setMinimumSize(new java.awt.Dimension(16, 16));
        refreshButton.setPreferredSize(new java.awt.Dimension(16, 16));
        refreshButton.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    refreshButtonActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        controlBar.add(refreshButton, gridBagConstraints);

        editButton.setIcon(RESOURCE.getIcon("objekt_bearbeiten.gif"));
        editButton.setToolTipText(org.openide.util.NbBundle.getMessage(
                DescriptionPane.class,
                "DescriptionPane.editButton.toolTipText")); // NOI18N
        editButton.setActionCommand(org.openide.util.NbBundle.getMessage(
                DescriptionPane.class,
                "DescriptionPane.editButton.actionCommand",
                new Object[] {}));                          // NOI18N
        editButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        editButton.setContentAreaFilled(false);
        editButton.setEnabled(false);
        editButton.setFocusPainted(false);
        editButton.setMaximumSize(new java.awt.Dimension(16, 16));
        editButton.setMinimumSize(new java.awt.Dimension(16, 16));
        editButton.setPreferredSize(new java.awt.Dimension(16, 16));
        editButton.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    editButtonActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        controlBar.add(editButton, gridBagConstraints);

        setLayout(new java.awt.CardLayout());

        panObjects.setLayout(new java.awt.BorderLayout());

        scpRenderer.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        scpRenderer.setViewportView(jPanel2);

        panObjects.add(scpRenderer, java.awt.BorderLayout.CENTER);

        panBreadCrump.setLayout(new java.awt.BorderLayout());
        panObjects.add(panBreadCrump, java.awt.BorderLayout.PAGE_START);

        add(panObjects, "objects");
    } // </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void editButtonActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_editButtonActionPerformed
        if (lastNode != null) {
            final DefaultMetaTreeNode selectedNode = lastNode;
            LOG.info("evt.getModifiers():" + evt.getModifiers());                  // NOI18N

            final MetaCatalogueTree metaCatalogueTree = ComponentRegistry.getRegistry().getCatalogueTree();

            if (MethodManager.getManager().checkPermission(
                            (MetaObjectNode)selectedNode.getNode(),
                            PermissionHolder.WRITEPERMISSION)) {
                ComponentRegistry.getRegistry().showComponent(ComponentRegistry.ATTRIBUTE_EDITOR);

                if (ComponentRegistry.getRegistry().getActiveCatalogue() == metaCatalogueTree) {
                    ComponentRegistry.getRegistry()
                            .getAttributeEditor()
                            .setTreeNode(metaCatalogueTree.getSelectionPath(), selectedNode);
                } else {
                    ComponentRegistry.getRegistry().getAttributeEditor().setTreeNode(selectedNode);
                }
            } else {
                LOG.warn("insufficient permission to edit node " + selectedNode); // NOI18N
            }
        }
    }                                                                             //GEN-LAST:event_editButtonActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void refreshButtonActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_refreshButtonActionPerformed
        if (this instanceof DescriptionPaneFX) {
            ((DescriptionPaneFX)this).refresh();
        }
    }                                                                                 //GEN-LAST:event_refreshButtonActionPerformed

    /**
     * DOCUMENT ME!
     */
    protected void showHTML() {
        final Runnable htmlRunnable = new ShowCardRunnable(this, "html"); // NOI18N

        if (EventQueue.isDispatchThread()) {
            htmlRunnable.run();
        } else {
            EventQueue.invokeLater(htmlRunnable);
        }
    }

    /**
     * DOCUMENT ME!
     */
    protected void showObjects() {
        final Runnable showObjRunnable = new ShowCardRunnable(this, "objects"); // NOI18N

        if (EventQueue.isDispatchThread()) {
            showObjRunnable.run();
        } else {
            EventQueue.invokeLater(showObjRunnable);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void clear() {
        final Runnable clearRunnable = new Runnable() {

                @Override
                public void run() {
                    removeAndDisposeAllRendererFromPanel();
                }
            };

        if (!EventQueue.isDispatchThread()) {
            EventQueue.invokeLater(clearRunnable);
        } else {
            clearRunnable.run();
        }
    }

    @Override
    public void addStatusChangeListener(final StatusChangeListener listener) {
        this.statusChangeSupport.addStatusChangeListener(listener);
    }

    @Override
    public void removeStatusChangeListener(final StatusChangeListener listener) {
        this.statusChangeSupport.removeStatusChangeListener(listener);
    }

    @Override
    public void initWithConnectionContext(final ConnectionContext cc) {
        this.connectionContext = cc;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  page  DOCUMENT ME!
     */
    public abstract void setPageFromURI(final String page);

    /**
     * DOCUMENT ME!
     *
     * @param  page  DOCUMENT ME!
     */
    public abstract void setPageFromContent(final String page);

    /**
     * DOCUMENT ME!
     *
     * @param  page     DOCUMENT ME!
     * @param  baseURL  DOCUMENT ME!
     */
    public abstract void setPageFromContent(final String page, final String baseURL);

    /**
     * Multiple Objects.
     *
     * @param  objects  DOCUMENT ME!
     */
    public void setNodesDescriptions(final List<?> objects) {
        if (objects.size() == 1) {
            setNodeDescription(objects.get(0));
        } else {
            lastNode = null;
            editButton.setEnabled(false);
            refreshButton.setEnabled(false);
            new SwingWorker<List<ObjectTreeNode>, Void>() {

                    @Override
                    protected List<ObjectTreeNode> doInBackground() throws Exception {
                        final List<ObjectTreeNode> otns = new ArrayList<ObjectTreeNode>();
                        for (final Object object : objects) {
                            if (object instanceof DefaultMetaTreeNode) {
                                final DefaultMetaTreeNode node = (DefaultMetaTreeNode)object;
                                if (!node.isWaitNode() && !node.isRootNode() && !node.isPureNode()
                                            && node.isObjectNode()) {
                                    if (lastNode == null) {
                                        lastNode = node;
                                    }
                                    otns.add((ObjectTreeNode)node);
                                }
                            }
                        }
                        return otns;
                    }

                    @Override
                    protected void done() {
                        try {
                            final List<ObjectTreeNode> otns = get();
                            gotoObjectTreeNodes(otns.toArray(new ObjectTreeNode[0]));
                        } catch (final Exception ex) {
                            LOG.error("error while preparing MetaObjectNodes", ex);
                        }
                    }
                }.execute();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  mons  DOCUMENT ME!
     */
    public void gotoMetaObjectNodes(final MetaObjectNode[] mons) {
        final Collection<ObjectTreeNode> otns = new ArrayList<ObjectTreeNode>();
        for (final MetaObjectNode mon : mons) {
            otns.add(new ObjectTreeNode(mon, getConnectionContext()));
        }
        gotoObjectTreeNodes(otns.toArray(new ObjectTreeNode[0]));
    }

    /**
     * DOCUMENT ME!
     *
     * @param  otns  DOCUMENT ME!
     */
    public void gotoObjectTreeNodes(final ObjectTreeNode[] otns) {
        if (otns.length == 1) {
            gotoObjectTreeNode(otns[0]);
        } else {
            if (worker != null) {
                worker.cancel(true);
                worker = null;
            }
            clearBreadCrumb();
            showObjects();
            showWaitScreen();
            if ((otns != null) && (otns.length > 0)) {
                worker = new SwingWorker<SelfDisposingPanel, SelfDisposingPanel>() {

                        final List<JComponent> all = new ArrayList<JComponent>();
                        boolean multipleClasses = false;
                        boolean initialized = false;

                        @Override
                        protected SelfDisposingPanel doInBackground() throws Exception {
                            Thread.currentThread().setName("DescriptionPane setNodesDescriptions()");
                            final MultiMap objectsByClass = new MultiMap();
                            for (final ObjectTreeNode otn : otns) {
                                final MetaObject metaObject = otn.getMetaObject();
                                final MetaClass mc = otn.getMetaClass();

                                // look for sharedhandles
                                final Collection<CidsBeanAggregationHandler> sharedHandlers =
                                    (Collection<CidsBeanAggregationHandler>)sharedHandlersHM.get(mc.getTableName()
                                                .toLowerCase());

                                boolean consumed = false;
                                if (sharedHandlers != null) {
                                    for (final CidsBeanAggregationHandler sharedHandler : sharedHandlers) {
                                        if (mc.getTableName().equalsIgnoreCase(
                                                        sharedHandler.getSourceMetaClassTablename())) {
                                            final Collection<CidsBean> aggrBeans = sharedHandler.getAggregatedBeans(
                                                    metaObject.getBean());
                                            for (final CidsBean aggrBean : aggrBeans) {
                                                objectsByClass.put(sharedHandler.getTargetMetaClassTablename()
                                                            .toLowerCase(),
                                                    aggrBean.getMetaObject());
                                            }
                                            if (sharedHandler.consume()) {
                                                consumed = true;
                                                break;
                                            }
                                        }
                                    }
                                }

                                if (!consumed) {
                                    objectsByClass.put(mc.getTableName().toLowerCase(), metaObject);
                                }
                            }

                            final Iterator it = objectsByClass.keySet().iterator();
                            multipleClasses = objectsByClass.keySet().size() > 1;
                            while (it.hasNext() && !isCancelled()) {
                                final Object key = it.next();
                                final List<MetaObject> mos = (List)objectsByClass.get(key);

                                if (!mos.isEmpty()) {
                                    final MetaClass mc = mos.toArray(new MetaObject[0])[0].getMetaClass();

                                    // Hier wird schon der Aggregationsrenderer gebaut, weil Einzelrenderer angezeigt
                                    // werden fall getAggregationrenderer null lifert (keiner da, oder Fehler)
                                    JComponent aggrRendererTester = null;

                                    if (mos.size() > 1) {
                                        aggrRendererTester = CidsObjectRendererFactory.getInstance()
                                                    .getAggregationRenderer(
                                                            mos,
                                                            mc.getName()
                                                            + " ("
                                                            + mos.size()
                                                            + ")");                                        // NOI18N
                                    }
                                    if (aggrRendererTester == null) {
                                        LOG.warn("AggregationRenderer was null. Will use SingleRenderer"); // NOI18N
                                        for (final MetaObject mo : mos) {
                                            final SelfDisposingPanel comp = encapsulateInSelfDisposingPanel(
                                                    CidsObjectRendererFactory.getInstance().getSingleRenderer(
                                                        mo,
                                                        mo.getMetaClass().getName()
                                                                + ": "
                                                                + mo));
                                            final CidsBean bean = mo.getBean();
                                            final PropertyChangeListener propertyChangeListener =
                                                new PropertyChangeListener() {

                                                    @Override
                                                    public void propertyChange(final PropertyChangeEvent evt) {
                                                        comp.repaint();
                                                    }
                                                };
                                            bean.addPropertyChangeListener(WeakListeners.propertyChange(
                                                    propertyChangeListener,
                                                    bean));
                                            comp.setStrongListenerReference(propertyChangeListener);
                                            publish(comp);
                                        }
                                    } else {
                                        final SelfDisposingPanel comp = encapsulateInSelfDisposingPanel(
                                                aggrRendererTester);
                                        publish(comp);
                                    }
                                }
                            }
                            return null;
                        }

                        @Override
                        protected void done() {
                            all.clear();
                        }

                        @Override
                        protected void process(final List<SelfDisposingPanel> chunks) {
                            if (!isCancelled()) {
                                int y = all.size();

                                if (!initialized) {
                                    showsWaitScreen = false;
                                    removeAndDisposeAllRendererFromPanel();
                                    gridBagConstraints = new java.awt.GridBagConstraints();
                                    gridBagConstraints.gridx = 0;
                                    gridBagConstraints.gridy = 0;
                                    gridBagConstraints.weightx = 1;
                                    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
                                    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                                    initialized = true;
                                }

                                if ((chunks.size() == 1) && (y == 0)) {
                                    final SelfDisposingPanel sdp = chunks.get(0);
                                    if (sdp instanceof RequestsFullSizeComponent) {
                                        if (LOG.isInfoEnabled()) {
                                            LOG.info("Renderer is FullSize Component!"); // NOI18N
                                        }

                                        fullScreenRenderer = true;

                                        panObjects.remove(scpRenderer);
                                        panObjects.add(panRenderer, BorderLayout.CENTER);

                                        panRenderer.setLayout(new BorderLayout());
                                        panRenderer.add(sdp, BorderLayout.CENTER);
                                    } else {
                                        fullScreenRenderer = false;

                                        panObjects.remove(panRenderer);
                                        panObjects.add(scpRenderer, BorderLayout.CENTER);
                                        scpRenderer.setViewportView(panRenderer);

                                        panRenderer.setLayout(new GridBagLayout());
                                        panRenderer.add(sdp, gridBagConstraints);
                                    }
                                    panRenderer.revalidate();
                                    revalidate();
                                    repaint();
                                } else {
                                    if (fullScreenRenderer) {
                                        fullScreenRenderer = false;

                                        panObjects.remove(panRenderer);
                                        panObjects.add(scpRenderer, BorderLayout.CENTER);

                                        scpRenderer.setViewportView(panRenderer);

                                        panRenderer.setLayout(new GridBagLayout());

                                        // the first renderer was added with a BorderLayout constraint,
                                        // but now it needs GridBagConstraints
                                        if (panRenderer.getComponentCount() == 1) {
                                            final Component firstRenderer = panRenderer.getComponent(0);
                                            panRenderer.remove(firstRenderer);
                                            panRenderer.add(firstRenderer, gridBagConstraints);
                                        }
                                    }

                                    for (final SelfDisposingPanel comp : chunks) {
                                        final GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
                                        gridBagConstraints.gridx = 0;
                                        gridBagConstraints.gridy = y;
                                        gridBagConstraints.weightx = 1;
                                        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
                                        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                                        panRenderer.add(comp, gridBagConstraints);

                                        panRenderer.revalidate();
                                        panRenderer.repaint();

                                        y++;
                                    }
                                }
                                all.addAll(chunks);
                            }
                        }
                    };
                CismetThreadPool.execute(worker);
            } else {
                clear();
                startNoDescriptionRenderer();
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public CidsBeanRenderer currentRenderer() {
        final BreadCrumb bc = breadCrumbModel.getLastCrumb();

        if (bc instanceof CidsMetaObjectBreadCrumb) {
            final Component renderer = ((CidsMetaObjectBreadCrumb)bc).getRenderer();
            if (renderer instanceof CidsBeanRenderer) {
                return (CidsBeanRenderer)renderer;
            } else if (renderer instanceof CoolEditor) {
                final CoolEditor editor = (CoolEditor)renderer;
                final JComponent original = editor.getOriginalComponent();
                if (original instanceof CidsBeanRenderer) {
                    return (CidsBeanRenderer)original;
                } else {
                    LOG.warn("original component of CoolEditor is no CidsbeanRenderer");                        // NOI18N
                }
            } else {
                LOG.warn("renderer Component of CidsMetaOjectBreadCrumb is no CidsBeanRenderer or CoolEditor"); // NOI18N
            }
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("last breadcrumb is no CidsMetaObjectbreadCrumb");                                    // NOI18N
            }
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public MetaObject currentMetaObject() {
        final BreadCrumb bc = breadCrumbModel.getLastCrumb();

        if (bc instanceof CidsMetaObjectBreadCrumb) {
            return ((CidsMetaObjectBreadCrumb)bc).getMetaObject();
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("last breadcrumb is no CidsMetaObjectbreadCrumb"); // NOI18N
            }
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   renderer  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected SelfDisposingPanel encapsulateInSelfDisposingPanel(final JComponent renderer) {
        JComponent originalComponent = renderer;
        if (renderer instanceof WrappedComponent) {
            originalComponent = ((WrappedComponent)renderer).getOriginalComponent();
        }
        final SelfDisposingPanel sdp;
        if (originalComponent instanceof RequestsFullSizeComponent) {
            sdp = new SelfDisposingFullscreenPanel(originalComponent);
        } else {
            sdp = new SelfDisposingPanel(originalComponent);
        }
        sdp.setLayout(new BorderLayout());
        sdp.add(renderer, BorderLayout.CENTER);
        return sdp;
    }

    /**
     * DOCUMENT ME!
     */
    public void showWaitScreen() {
        if (!showsWaitScreen) {
            showsWaitScreen = true;
            final Runnable run = new Runnable() {

                    @Override
                    public void run() {
                        removeAndDisposeAllRendererFromPanel();
                        final GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
                        gridBagConstraints.gridx = 0;
                        gridBagConstraints.gridy = 0;
                        gridBagConstraints.weightx = 1;
                        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
                        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                        if (!(panRenderer.getLayout() instanceof GridBagLayout)) {
                            panRenderer.setLayout(new GridBagLayout());
                        }
                        if (wrappedWaitingPanel != null) {
                            panRenderer.add(wrappedWaitingPanel, gridBagConstraints);
                        }
                        repaint();
                    }
                };
            if (EventQueue.isDispatchThread()) {
                run.run();
            } else {
                EventQueue.invokeLater(run);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param       to             DOCUMENT ME!
     * @param       optionalTitle  DOCUMENT ME!
     *
     * @deprecated  use gotoMetaObjects(MetaObjectNode[]) instead
     */
    @Deprecated
    public void gotoMetaObjects(final MetaObject[] to, final String optionalTitle) {
        if (currentRenderer() == null) {
            // there is no renderer displayed
            if ((worker != null) && !worker.isDone()) {
                worker.cancel(true);
                worker = null;
            } else {
                showObjects();
                showWaitScreen();
            }
        }
        final List<MetaObjectNode> mons = new ArrayList<MetaObjectNode>();
        for (final MetaObject metaObject : to) {
            mons.add(new MetaObjectNode(metaObject.getBean()));
        }
        gotoMetaObjectNodes(mons.toArray(new MetaObjectNode[0]));
    }

    /**
     * DOCUMENT ME!
     *
     * @param       mon            DOCUMENT ME!
     * @param       optionalTitle  DOCUMENT ME!
     *
     * @deprecated  use gotoMetaObjectNode(MetaObjectNode) instead
     */
    @Deprecated
    public void gotoMetaObjectNode(final MetaObjectNode mon, final String optionalTitle) {
        gotoMetaObjectNode(mon);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  mon  DOCUMENT ME!
     */
    public void gotoMetaObjectNode(final MetaObjectNode mon) {
        gotoMetaObjectNode(mon, true);
    }

    /**
     * DOCUMENT ME!
     *
     * @param       mon              DOCUMENT ME!
     * @param       optionalTitle    DOCUMENT ME!
     * @param       clearBreadcrumb  DOCUMENT ME!
     *
     * @deprecated  use gotoMetaObjectNode(MetaObjectNode, boolean) instead
     */
    @Deprecated
    public void gotoMetaObjectNode(final MetaObjectNode mon,
            final String optionalTitle,
            final boolean clearBreadcrumb) {
        gotoMetaObjectNode(mon, clearBreadcrumb);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  mon              DOCUMENT ME!
     * @param  clearBreadcrumb  DOCUMENT ME!
     */
    public void gotoMetaObjectNode(final MetaObjectNode mon,
            final boolean clearBreadcrumb) {
        gotoObjectTreeNode(new ObjectTreeNode(mon, getConnectionContext()), clearBreadcrumb);
    }

    /**
     * DOCUMENT ME!
     *
     * @param       otn            DOCUMENT ME!
     * @param       optionalTitle  DOCUMENT ME!
     *
     * @deprecated  use gotoObjectTreeNode(ObjectTreeNode) instead
     */
    @Deprecated
    public void gotoObjectTreeNode(final ObjectTreeNode otn, final String optionalTitle) {
        gotoObjectTreeNode(otn);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  otn  DOCUMENT ME!
     */
    public void gotoObjectTreeNode(final ObjectTreeNode otn) {
        gotoObjectTreeNode(otn, true);
    }

    /**
     * DOCUMENT ME!
     *
     * @param       otn              DOCUMENT ME!
     * @param       optionalTitle    DOCUMENT ME!
     * @param       clearBreadcrumb  DOCUMENT ME!
     *
     * @deprecated  use gotoObjectTreeNode(ObjectTreeNode, boolean) instead
     */
    @Deprecated
    public void gotoObjectTreeNode(final ObjectTreeNode otn,
            final String optionalTitle,
            final boolean clearBreadcrumb) {
        gotoObjectTreeNode(otn, clearBreadcrumb);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  otn              DOCUMENT ME!
     * @param  clearBreadcrumb  DOCUMENT ME!
     */
    public void gotoObjectTreeNode(final ObjectTreeNode otn,
            final boolean clearBreadcrumb) {
        if (currentRenderer() == null) {
            // there is no renderer displayed
            if ((worker != null) && !worker.isDone()) {
                worker.cancel(true);
                worker = null;
            } else {
                showObjects();
                showWaitScreen();
            }
        }
        new SwingWorker<MetaObject, Void>() {

                @Override
                protected MetaObject doInBackground() throws Exception {
                    return otn.getMetaObject();
                }

                @Override
                protected void done() {
                    try {
                        final MetaObject metaobject = get();
                        final CidsMetaObjectBreadCrumb crumb = new CidsMetaObjectBreadCrumb(metaobject) {

                                @Override
                                public void crumbActionPerformed(final ActionEvent e) {
                                    startSingleRendererWorker(otn);
                                }
                            };
                        if (clearBreadcrumb) {
                            breadCrumbModel.startWithNewCrumb(crumb);
                        } else {
                            breadCrumbModel.appendCrumb(crumb);
                        }
                        startSingleRendererWorker(otn);
                    } catch (final InterruptedException e) {
                        LOG.error("Background Thread was interrupted", e);                // NOI18N
                    } catch (final ExecutionException e) {
                        LOG.error("Background Thread execution encountered an error", e); // NOI18N
                    }
                }
            }.execute();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  to             DOCUMENT ME!
     * @param  optionalTitle  DOCUMENT ME!
     */
    @Deprecated
    protected void showMetaObjectSingleRenderer(final MetaObject to, final String optionalTitle) {
        final CidsMetaObjectBreadCrumb crumb = new CidsMetaObjectBreadCrumb(to) {

                @Override
                public void crumbActionPerformed(final ActionEvent e) {
                    startSingleRendererWorker(to, optionalTitle);
                }
            };

        if (currentRenderer() == null) {
            // there is no renderer displayed
            if ((worker != null) && !worker.isDone()) {
                worker.cancel(true);
                worker = null;
            } else {
                showObjects();
                showWaitScreen();
            }
        }

        breadCrumbModel.appendCrumb(crumb);
        startSingleRendererWorker(to, optionalTitle);
    }

    /**
     * DOCUMENT ME!
     *
     * @param       to             DOCUMENT ME!
     * @param       optionalTitle  DOCUMENT ME!
     *
     * @deprecated  use gotoMetaObjectNode(MetaObjectNode, false) instead
     */
    @Deprecated
    public void gotoMetaObject(final MetaObject to, final String optionalTitle) {
        showMetaObjectSingleRenderer(to, optionalTitle);
    }

    @Override
    public final ConnectionContext getConnectionContext() {
        return connectionContext;
    }

    /**
     * DOCUMENT ME!
     *
     * @param       mc             DOCUMENT ME!
     * @param       toObjectId     DOCUMENT ME!
     * @param       optionalTitle  DOCUMENT ME!
     *
     * @deprecated  use gotoMetaObject(MetaObjectNode, String) instead
     */
    @Deprecated
    public void gotoMetaObject(final MetaClass mc, final int toObjectId, final String optionalTitle) {
        showWaitScreen();
        new SwingWorker<MetaObject, Void>() {

                @Override
                protected MetaObject doInBackground() throws Exception {
                    return SessionManager.getProxy()
                                .getMetaObject(toObjectId, mc.getId(), mc.getDomain(), getConnectionContext());
                }

                @Override
                protected void done() {
                    try {
                        final MetaObject result = get();
                        showMetaObjectSingleRenderer(result, optionalTitle);
                    } catch (final InterruptedException e) {
                        LOG.error("Background Thread was interrupted", e);                // NOI18N
                    } catch (final ExecutionException e) {
                        LOG.error("Background Thread execution encountered an error", e); // NOI18N
                    }
                }
            }.execute();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  node  DOCUMENT ME!
     */
    protected void startSingleRendererWorker(final DefaultMetaTreeNode node) {
        final MetaObject o = ((ObjectTreeNode)node).getMetaObject();

        startSingleRendererWorker(o, node, node.toString());
    }

    /**
     * DOCUMENT ME!
     *
     * @param  o      DOCUMENT ME!
     * @param  title  DOCUMENT ME!
     */
    protected void startSingleRendererWorker(final MetaObject o, final String title) {
        startSingleRendererWorker(o, null, title);
    }

    /**
     * DOCUMENT ME!
     */
    protected void startNoDescriptionRenderer() {
        startSingleRendererWorker(null, null, null);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  o      DOCUMENT ME!
     * @param  node   DOCUMENT ME!
     * @param  title  DOCUMENT ME!
     */
    protected void startSingleRendererWorker(final MetaObject o, final DefaultMetaTreeNode node, final String title) {
        worker = new javax.swing.SwingWorker<SelfDisposingPanel, Void>() {

                @Override
                protected SelfDisposingPanel doInBackground() throws Exception {
                    Thread.currentThread().setName("CidsSearchExecutor startSingleRendererWorker()");
                    final JComponent jComp;
                    final boolean isNoDescriptionRenderer = (o == null) && (node == null);
                    if (isNoDescriptionRenderer) {
                        jComp = NoDescriptionRenderer.getInstance();
                    } else {
                        jComp = CidsObjectRendererFactory.getInstance().getSingleRenderer(o, title);

                        if ((jComp instanceof MetaTreeNodeStore) && (node != null)) {
                            ((MetaTreeNodeStore)jComp).setMetaTreeNode(node);
                        } else if ((jComp instanceof WrappedComponent)
                                    && (((WrappedComponent)jComp).getOriginalComponent() instanceof MetaTreeNodeStore)
                                    && (node != null)) {
                            final JComponent originalComponent = ((WrappedComponent)jComp).getOriginalComponent();
                            ((MetaTreeNodeStore)originalComponent).setMetaTreeNode(node);
                        }
                    }
                    final PropertyChangeListener localListener = new PropertyChangeListener() {

                            @Override
                            public void propertyChange(final PropertyChangeEvent evt) {
                                jComp.repaint();
                            }
                        };

                    if (!isNoDescriptionRenderer) {
                        // set the renderer for the current breadcrumb
                        final BreadCrumb lastCrumb = breadCrumbModel.getLastCrumb();
                        if (lastCrumb instanceof CidsMetaObjectBreadCrumb) {
                            ((CidsMetaObjectBreadCrumb)lastCrumb).setRenderer(jComp);
                        }

                        final CidsBean bean = o.getBean();
                        bean.addPropertyChangeListener(WeakListeners.propertyChange(localListener, bean));
                    }

                    final SelfDisposingPanel sdp = encapsulateInSelfDisposingPanel(jComp);
                    sdp.setStrongListenerReference(localListener);
                    return sdp;
                }

                @Override
                protected void done() {
                    try {
                        final SelfDisposingPanel sdp = get();
                        if (!isCancelled()) {
                            showsWaitScreen = false;
                            removeAndDisposeAllRendererFromPanel();
                            gridBagConstraints = new java.awt.GridBagConstraints();
                            gridBagConstraints.gridx = 0;
                            gridBagConstraints.gridy = 0;
                            gridBagConstraints.weightx = 1;
                            gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
                            gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;

                            if (sdp instanceof RequestsFullSizeComponent) {
                                if (LOG.isInfoEnabled()) {
                                    LOG.info("Renderer is FullSize Component!"); // NOI18N
                                }

                                fullScreenRenderer = true;

                                panObjects.remove(scpRenderer);
                                panObjects.add(panRenderer, BorderLayout.CENTER);

                                panRenderer.setLayout(new BorderLayout());
                                panRenderer.add(sdp, BorderLayout.CENTER);
                            } else {
                                fullScreenRenderer = false;

                                panObjects.remove(panRenderer);
                                panObjects.add(scpRenderer, BorderLayout.CENTER);
                                scpRenderer.setViewportView(panRenderer);

                                panRenderer.setLayout(new GridBagLayout());
                                panRenderer.add(sdp, gridBagConstraints);
                            }
                            panRenderer.revalidate();
                            revalidate();
                            repaint();
                        } else {
                            if (LOG.isDebugEnabled()) {
                                LOG.debug("Worker canceled!"); // NOI18N
                            }
                        }
                    } catch (final InterruptedException iex) {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Worker canceled!");     // NOI18N
                        }
                    } catch (final ExecutionException e) {
                        LOG.error("Error during Renderer creation", e); // NOI18N
                    } catch (final CancellationException e) {
                        LOG.warn(
                            "get() throw a cancellation exception. This can happen if the construction of a renderer was aborted before it was displayed.",
                            e);
                    }
                    if (worker == this) {
                        worker = null;
                    }
                }
            };
        CismetThreadPool.execute(worker);
    }

    /**
     * DOCUMENT ME!
     */
    public void clearBreadCrumb() {
        breadCrumbModel.clear();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  n  DOCUMENT ME!
     */
    protected void performSetNode(final DefaultMetaTreeNode n) {
        final String descriptionURL = n.getDescription();
        // besorge MO zum parametrisieren der URL
        if (n.isObjectNode()) {
//            final MetaObject o = ().getMetaObject();
            gotoObjectTreeNode((ObjectTreeNode)n, n.toString());
        } else if (n.isPureNode() && (n.getDescription() != null)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("loading description from url '" + descriptionURL + "'"); // NOI18N
            }
            setPageFromURI(descriptionURL);
            showHTML();
        } else {
            startNoDescriptionRenderer();
        }
        showsWaitScreen = false;
    }

    /**
     * Single Object.
     *
     * @param  object  DOCUMENT ME!
     */
    public void setNodeDescription(final Object object) {
        if ((object != null) && !((DefaultMetaTreeNode)object).isWaitNode()
                    && !((DefaultMetaTreeNode)object).isRootNode()) {
            final DefaultMetaTreeNode n = (DefaultMetaTreeNode)object;
            if ((worker != null) && !worker.isDone()) {
                worker.cancel(true);
                worker = null;
            } else {
                showObjects();
                showWaitScreen();
            }
            performSetNode(n);
            lastNode = n;
            editButton.setEnabled(true);
            refreshButton.setEnabled(true);
        } else {
            lastNode = null;
            editButton.setEnabled(false);
            refreshButton.setEnabled(false);
            statusChangeSupport.fireStatusChange(
                org.openide.util.NbBundle.getMessage(
                    DescriptionPane.class,
                    "DescriptionPane.setNodeDescription(Object).status.nodescription"), // NOI18N
                Status.MESSAGE_POSITION_3,
                Status.ICON_DEACTIVATED,
                Status.ICON_DEACTIVATED);

            setPageFromContent(
                welcomePage,
                getClass().getClassLoader().getResource("Sirius/navigator/resource/doc/welcome.html").toString());
        }
    }

    /**
     * DOCUMENT ME!
     */
    protected void removeAndDisposeAllRendererFromPanel() {
        panRenderer.removeAll();
    }

    @Override
    public void paintComponent(final Graphics g) {
        final Graphics2D g2d = (Graphics2D)g;
        final GradientPaint gp = new GradientPaint(0, 0, getBackground(), getWidth(), getHeight(), Color.WHITE, false);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    /**
     * DOCUMENT ME!
     */
    public void prepareValueChanged() {
        if (worker != null) {
            worker.cancel(true);
        }
        showWaitScreen();
    }

    @Override
    public void setControlBarVisible(final boolean isVisible) {
        controlBar.setVisible(isVisible);
    }

    @Override
    public Vector<AbstractButton> getControlBarButtons() {
        boolean showEditButton = false;

        try {
            showEditButton = SessionManager.getConnection()
                        .hasConfigAttr(SessionManager.getSession().getUser(),
                                "navigator.descriptionPane.editButton",
                                connectionContext);
        } catch (ConnectionException e) {
            LOG.error("Connection exception", e);
        }

        if (showEditButton) {
            final Vector<AbstractButton> buttons = new Vector<AbstractButton>();
//            if (this instanceof DescriptionPaneFX) {
//                buttons.add(refreshButton);
//            }
            buttons.add(editButton);
            return buttons;
        } else {
            return null;
        }
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private static final class SelfDisposingFullscreenPanel extends SelfDisposingPanel
            implements RequestsFullSizeComponent {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new SelfDisposingFullscreenPanel object.
         *
         * @param  disposableBeanStore  DOCUMENT ME!
         */
        public SelfDisposingFullscreenPanel(final JComponent disposableBeanStore) {
            super(disposableBeanStore);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    class PrintableJPanel extends ScrollableFlowPanel implements Printable {

        //~ Methods ------------------------------------------------------------

        @Override
        public int print(final Graphics graphics, final PageFormat pageFormat, final int pageIndex)
                throws PrinterException {
            if (pageIndex > 0) {
                return (NO_SUCH_PAGE);
            } else {
                final Graphics2D g2d = (Graphics2D)graphics;
                g2d.scale(0.75, 0.75);
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
                // Turn off double buffering
                paint(g2d);
                // Turn double buffering back on
                return (PAGE_EXISTS);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    class ShowCardRunnable implements Runnable {

        //~ Instance fields ----------------------------------------------------

        private Container parent;
        private String cardToShow;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new ShowCardRunnable object.
         *
         * @param  parent      DOCUMENT ME!
         * @param  cardToShow  DOCUMENT ME!
         */
        public ShowCardRunnable(final Container parent, final String cardToShow) {
            this.parent = parent;
            this.cardToShow = cardToShow;
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public void run() {
            if (parent.getLayout() instanceof CardLayout) {
                ((CardLayout)parent.getLayout()).show(parent, cardToShow);
            }
        }
    }

    /**
     * Opens a given menu if user asks for. The menu is specified in the constructor.
     *
     * @version  $Revision$, $Date$
     */
    protected class PopupListener extends MouseAdapter {

        //~ Instance fields ----------------------------------------------------

        private JPopupMenu menu;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new PopupListener object.
         *
         * @param  menu  The menu to open.
         */
        public PopupListener(final JPopupMenu menu) {
            this.menu = menu;
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public void mousePressed(final MouseEvent e) {
            showPopup(e);
        }

        @Override
        public void mouseReleased(final MouseEvent e) {
            showPopup(e);
        }

        /**
         * Shows the popup menu.
         *
         * @param  e  The mouse event.
         */
        private void showPopup(final MouseEvent e) {
            if (e.isPopupTrigger() && (menu != null)) {
                menu.show(e.getComponent(),
                    e.getX(), e.getY());
            }
        }
    }
}
