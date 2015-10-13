/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui.tree.postfilter;

import Sirius.navigator.connection.SessionManager;
import Sirius.navigator.ui.tree.PostfilterEnabledSearchResultsTree;

import Sirius.server.middleware.types.MetaClass;
import Sirius.server.middleware.types.MetaObject;
import Sirius.server.middleware.types.MetaObjectNode;
import Sirius.server.middleware.types.Node;

import org.openide.util.lookup.ServiceProvider;

import java.awt.EventQueue;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.cismet.cids.dynamics.CidsBean;

import de.cismet.cids.navigator.utils.ClassCacheMultiple;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
@ServiceProvider(service = PostFilterGUI.class)
public class ExampleAdressPostFilterGUI extends AbstractPostFilterGUI {

    //~ Instance fields --------------------------------------------------------

    PostFilter f = new PostFilter() {

            @Override
            public Integer getFilterChainOrderKeyPrio() {
                return 100;
            }

            @Override
            public Collection<Node> filter(final Collection<Node> input) {
                if ((input != null)) {
                    final ArrayList<Node> ret = new ArrayList<Node>();
                    for (final Node n : input) {
                        if (n.getClassId() == adressClass.getId()) {
                            if (n instanceof MetaObjectNode) {
                                final MetaObjectNode mon = (MetaObjectNode)n;
                                try {
                                    final String nr = (String)mon.getObject().getBean().getProperty("hausnummer");
                                    final int nint = new Integer(nr).intValue();
                                    if ((nint >= sldMin.getValue()) && (nint <= sldMax.getValue())) {
                                        ret.add(n);
                                    }
                                } catch (Exception skip) {
                                    ret.add(n);
                                }
                            }
                        } else {
                            ret.add(n);
                        }
                    }
                    return ret;
                } else {
                    return null;
                }
            }
        };

    MetaClass adressClass;

    int min = Integer.MAX_VALUE;
    int max = 0;
    private ChangeListener cl = new ChangeListener() {

            @Override
            public void stateChanged(final ChangeEvent e) {
                firePostFilterChanged();
            }
        };

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JProgressBar prbLoading;
    private javax.swing.JSlider sldMax;
    private javax.swing.JSlider sldMin;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form ExampleAdressPostFilterGUI.
     */
    public ExampleAdressPostFilterGUI() {
        initComponents();
        prbLoading.setVisible(false);
        adressClass = ClassCacheMultiple.getMetaClass("WUNDA_BLAU", "ADRESSE");
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void initializeFilter(final Collection<Node> nodes) {
        EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    sldMin.removeChangeListener(cl);
                    sldMax.removeChangeListener(cl);
                    sldMax.setEnabled(false);
                    sldMin.setEnabled(false);
                    prbLoading.setVisible(true);
                }
            });

        final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                @Override
                protected Void doInBackground() throws Exception {
                    min = Integer.MAX_VALUE;
                    max = 0;
                    try {
                        for (final Node n : new ArrayList<Node>(nodes)) {
                            if ((n instanceof MetaObjectNode)
                                        && (((MetaObjectNode)n).getClassId() == adressClass.getId())) {
                                MetaObject mo = ((MetaObjectNode)n).getObject();
                                if (mo == null) {
                                    mo = SessionManager.getProxy()
                                                .getMetaObject(((MetaObjectNode)n).getObjectId(),
                                                        ((MetaObjectNode)n).getClassId(),
                                                        ((MetaObjectNode)n).getDomain());
                                    ((MetaObjectNode)n).setObject(mo);
                                }
                                final CidsBean cb = mo.getBean();
                                try {
                                    final String nr = (String)cb.getProperty("hausnummer");
                                    final int nint = new Integer(nr).intValue();
                                    if (nint < min) {
                                        min = nint;
                                    }
                                    if (nint > max) {
                                        max = nint;
                                    }
                                } catch (Exception skip) {
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return null;
                }

                @Override
                protected void done() {
                    sldMax.setEnabled(true);
                    sldMin.setEnabled(true);
                    prbLoading.setVisible(false);
                    sldMin.setMinimum(min - 1);
                    sldMin.setMaximum(max + 1);
                    sldMax.setMinimum(min - 1);
                    sldMax.setMaximum(max + 1);
                    sldMin.setValue(min);
                    sldMax.setValue(max);
                    sldMin.addChangeListener(cl);
                    sldMax.addChangeListener(cl);
                }
            };
        worker.execute();
    }

    @Override
    public void adjustFilter(final Collection<Node> nodes) {
    }

    @Override
    public boolean canHandle(final Collection<Node> nodes) {
        return PostfilterEnabledSearchResultsTree.getAllTableNamesForNodeCollection(nodes).contains("ADRESSE");
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public String getTitle() {
        return "Adressenfilter";
    }

    @Override
    public PostFilter getFilter() {
        return f;
    }

    @Override
    public Integer getDisplayOrderKeyPrio() {
        return 200;
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        sldMax = new javax.swing.JSlider();
        sldMin = new javax.swing.JSlider();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        prbLoading = new javax.swing.JProgressBar();

        sldMax.setPaintTicks(true);

        sldMin.setPaintTicks(true);

        org.openide.awt.Mnemonics.setLocalizedText(
            jLabel1,
            org.openide.util.NbBundle.getMessage(
                ExampleAdressPostFilterGUI.class,
                "ExampleAdressPostFilterGUI.jLabel1.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(
            jLabel2,
            org.openide.util.NbBundle.getMessage(
                ExampleAdressPostFilterGUI.class,
                "ExampleAdressPostFilterGUI.jLabel2.text")); // NOI18N

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
                org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
                sldMin,
                org.jdesktop.beansbinding.ELProperty.create("${value}"),
                jLabel3,
                org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
                org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
                sldMax,
                org.jdesktop.beansbinding.ELProperty.create("${value}"),
                jLabel4,
                org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        prbLoading.setIndeterminate(true);

        final javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                layout.createSequentialGroup().addGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                        layout.createSequentialGroup().addContainerGap().addGroup(
                            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                                layout.createSequentialGroup().addComponent(jLabel2).addPreferredGap(
                                    javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(
                                    sldMax,
                                    javax.swing.GroupLayout.PREFERRED_SIZE,
                                    javax.swing.GroupLayout.DEFAULT_SIZE,
                                    javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(
                                layout.createSequentialGroup().addComponent(jLabel1).addPreferredGap(
                                    javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(
                                    sldMin,
                                    javax.swing.GroupLayout.PREFERRED_SIZE,
                                    javax.swing.GroupLayout.DEFAULT_SIZE,
                                    javax.swing.GroupLayout.PREFERRED_SIZE))).addGap(76, 76, 76).addComponent(
                            prbLoading,
                            javax.swing.GroupLayout.PREFERRED_SIZE,
                            46,
                            javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(
                        layout.createSequentialGroup().addGap(216, 216, 216).addComponent(jLabel3)).addGroup(
                        layout.createSequentialGroup().addGap(218, 218, 218).addComponent(jLabel4))).addGap(
                    0,
                    295,
                    Short.MAX_VALUE)));
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                layout.createSequentialGroup().addContainerGap().addComponent(jLabel3).addGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                        layout.createSequentialGroup().addGap(1, 1, 1).addGroup(
                            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(
                                sldMin,
                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel1)).addGap(12, 12, 12)
                                    .addGroup(
                                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel2).addComponent(
                                            sldMax,
                                            javax.swing.GroupLayout.PREFERRED_SIZE,
                                            javax.swing.GroupLayout.DEFAULT_SIZE,
                                            javax.swing.GroupLayout.PREFERRED_SIZE))).addGroup(
                        layout.createSequentialGroup().addGap(33, 33, 33).addComponent(
                            prbLoading,
                            javax.swing.GroupLayout.PREFERRED_SIZE,
                            javax.swing.GroupLayout.DEFAULT_SIZE,
                            javax.swing.GroupLayout.PREFERRED_SIZE))).addPreferredGap(
                    javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel4).addContainerGap(
                    166,
                    Short.MAX_VALUE)));

        bindingGroup.bind();
    } // </editor-fold>//GEN-END:initComponents
}
