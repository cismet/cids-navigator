/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator;

import Sirius.navigator.exception.ExceptionManager;
import Sirius.navigator.method.MultithreadedMethod;
import Sirius.navigator.ui.progress.ProgressObserver;

import org.apache.log4j.Logger;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import de.cismet.tools.CismetThreadPool;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class NavigatorSplashScreen extends JFrame {

    //~ Instance fields --------------------------------------------------------

    private final ProgressObserver progressObserver;
    private final NavigatorLoader navigatorLoader;
    private final Logger logger;
    private ProgressObserver pluginProgressObserver;
    private Timer timer;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnApply;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel logoLabel;
    private javax.swing.JPanel panButtons;
    private javax.swing.JPanel panCenter;
    private javax.swing.JPanel panConnection;
    private javax.swing.JPanel panProgress;
    private javax.swing.JPanel panProxy;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JProgressBar progressBarPlugin;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form JWindow.
     *
     * @param  progressObserver  DOCUMENT ME!
     * @param  logo              DOCUMENT ME!
     */
    public NavigatorSplashScreen(final ProgressObserver progressObserver, final Icon logo) {
        this.setUndecorated(true);
        // this.setAlwaysOnTop(true);
        this.initComponents();

        this.logger = Logger.getLogger(this.getClass());

        this.panConnection.setVisible(false);
        this.progressObserver = progressObserver;
        pluginProgressObserver = progressObserver.getSubProgressObserver();

        this.progressObserver.addPropertyChangeListener(new ProgressListener(progressObserver, progressBar));

        this.navigatorLoader = new NavigatorLoader(this.progressObserver);

        this.logoLabel.setIcon(logo);
        this.logoLabel.setPreferredSize(new Dimension(logo.getIconWidth(), logo.getIconHeight()));
        timer = new Timer(100, new TimerListener());
        progressBarPlugin.setVisible(false);
        final int[] pixels = new int[1];
        final BufferedImage img = new BufferedImage(logo.getIconWidth(),
                logo.getIconHeight(),
                BufferedImage.TYPE_INT_ARGB); // you can change the type as needed

        final Graphics2D g = img.createGraphics();
        logo.paintIcon(new JPanel(), g, 0, 0);

        final int cCode = img.getRGB(0, logo.getIconHeight() - 1);
        final Color col = new Color(cCode);
        progressBar.setForeground(col);
        progressBarPlugin.setForeground(col);
        panCenter.setBackground(col);

        timer.start();
        pack();
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void show() {
        super.show();

        this.navigatorLoader.invoke(null);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        panCenter = new javax.swing.JPanel();
        logoLabel = new javax.swing.JLabel();
        panProgress = new javax.swing.JPanel();
        progressBar = new javax.swing.JProgressBar();
        progressBarPlugin = new javax.swing.JProgressBar();
        panConnection = new javax.swing.JPanel();
        panButtons = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        btnApply = new javax.swing.JButton();
        panProxy = new javax.swing.JPanel();

        addWindowListener(new java.awt.event.WindowAdapter() {

                @Override
                public void windowClosing(final java.awt.event.WindowEvent evt) {
                    exitForm(evt);
                }
            });

        panCenter.setLayout(new java.awt.BorderLayout());

        logoLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        logoLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        logoLabel.setVerifyInputWhenFocusTarget(false);
        panCenter.add(logoLabel, java.awt.BorderLayout.CENTER);

        panProgress.setLayout(new java.awt.BorderLayout());

        progressBar.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        progressBar.setBorderPainted(false);
        progressBar.setDoubleBuffered(true);
        progressBar.setFocusable(false);
        progressBar.setStringPainted(true);
        progressBar.setVerifyInputWhenFocusTarget(false);
        panProgress.add(progressBar, java.awt.BorderLayout.NORTH);

        progressBarPlugin.setMaximum(1000);
        progressBarPlugin.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        progressBarPlugin.setBorderPainted(false);
        progressBarPlugin.setDoubleBuffered(true);
        progressBarPlugin.setFocusable(false);
        progressBarPlugin.setString(org.openide.util.NbBundle.getMessage(
                NavigatorSplashScreen.class,
                "NavigatorSplashSceen.progressBarPlugin.string")); // NOI18N
        progressBarPlugin.setStringPainted(true);
        progressBarPlugin.setVerifyInputWhenFocusTarget(false);
        panProgress.add(progressBarPlugin, java.awt.BorderLayout.SOUTH);

        panCenter.add(panProgress, java.awt.BorderLayout.PAGE_END);

        getContentPane().add(panCenter, java.awt.BorderLayout.CENTER);

        panConnection.setLayout(new java.awt.BorderLayout());

        jButton1.setText("Abbrechen");
        jButton1.setPreferredSize(new java.awt.Dimension(100, 29));
        jButton1.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jButton1ActionPerformed(evt);
                }
            });
        panButtons.add(jButton1);

        btnApply.setText("Anwenden");
        btnApply.setPreferredSize(new java.awt.Dimension(100, 29));
        panButtons.add(btnApply);

        panConnection.add(panButtons, java.awt.BorderLayout.SOUTH);
        panConnection.add(panProxy, java.awt.BorderLayout.CENTER);

        getContentPane().add(panConnection, java.awt.BorderLayout.EAST);

        pack();
    } // </editor-fold>//GEN-END:initComponents

    /**
     * Exit the Application.
     *
     * @param  evt  DOCUMENT ME!
     */
    private void exitForm(final java.awt.event.WindowEvent evt) //GEN-FIRST:event_exitForm
    {
        System.exit(0);
    }                                                           //GEN-LAST:event_exitForm

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jButton1ActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jButton1ActionPerformed
        System.exit(0);
    }                                                                            //GEN-LAST:event_jButton1ActionPerformed

    /**
     * Sets the ProxyOption panel by adding it at the CENTER position of the panConnection.
     *
     * @param  panProxyOptions  DOCUMENT ME!
     */
    public void setProxyOptionsPanel(final JPanel panProxyOptions) {
        panConnection.add(panProxyOptions, BorderLayout.CENTER);
    }

    /**
     * Adds an ActionListner to the "Apply"-button of the ProxyOptions panel.
     *
     * @param  al  DOCUMENT ME!
     */
    public void addApplyButtonActionListener(final ActionListener al) {
        btnApply.addActionListener(al);
    }

    /**
     * Shows or hides the ProxyOptions panel.
     *
     * @param  isVisible  DOCUMENT ME!
     */
    public void setProxyOptionsVisible(final boolean isVisible) {
        panConnection.setVisible(isVisible);
        panConnection.validate();
        pack();
    }

    /**
     * Returns if the ProxyOptions panel is visible or not.
     *
     * @return  true if ProxyOptions panel is visible, else false
     */
    public boolean isProxyOptionsVisible() {
        return panConnection.isVisible();
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private final class ProgressListener implements PropertyChangeListener {

        //~ Instance fields ----------------------------------------------------

        ProgressObserver observer;
        JProgressBar bar;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new ProgressListener object.
         *
         * @param  observer  DOCUMENT ME!
         * @param  bar       DOCUMENT ME!
         */
        public ProgressListener(final ProgressObserver observer, final JProgressBar bar) {
            this.observer = observer;
            this.bar = bar;
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public void propertyChange(final PropertyChangeEvent evt) {
            bar.setValue(progressObserver.getPercentage());
            bar.setString(progressObserver.getMessage());
            bar.repaint();

            if (observer.isInterrupted() || observer.isFinished()) {
                timer.stop();
                timer = null;
                dispose();
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private final class NavigatorLoader extends MultithreadedMethod {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new NavigatorLoader object.
         *
         * @param  progressObserver  DOCUMENT ME!
         */
        private NavigatorLoader(final ProgressObserver progressObserver) {
            super(progressObserver);
        }

        //~ Methods ------------------------------------------------------------

        @Override
        protected void doInvoke() {
            final Thread t = new Thread() {

                    @Override
                    public void run() {
                        try {
                            if (logger.isInfoEnabled()) {
                                logger.info("creating navigator instance");    // NOI18N
                            }
                            final Navigator navigator = new Navigator(
                                    NavigatorLoader.this.progressObserver,
                                    NavigatorSplashScreen.this);
                            if (logger.isInfoEnabled()) {
                                logger.info("new navigator instance created"); // NOI18N
                            }
                            SwingUtilities.invokeLater(new Runnable() {

                                    @Override
                                    public void run() {
                                        navigator.setVisible(true);
                                    }
                                });
                        } catch (Throwable t) {
                            logger.fatal("could not create navigator instance", t);                                            // NOI18N
                            ExceptionManager.getManager()
                                    .showExceptionDialog(
                                        ExceptionManager.FATAL,
                                        org.openide.util.NbBundle.getMessage(
                                            NavigatorSplashScreen.class,
                                            "NavigatorSplashScreen.NavigatorLoader.doInvoke().ExceptionManager_anon.name"),    // NOI18N
                                        org.openide.util.NbBundle.getMessage(
                                            NavigatorSplashScreen.class,
                                            "NavigatorSplashScreen.NavigatorLoader.doInvoke().ExceptionManager_anon.message"), // NOI18N
                                        t);
                            System.exit(1);
                        }
                    }
                };
            CismetThreadPool.execute(t);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class TimerListener implements ActionListener {

        //~ Methods ------------------------------------------------------------

        @Override
        public void actionPerformed(final ActionEvent evt) {
            // plugin progress
            final ProgressObserver pluginPogressObserver = progressObserver.getSubProgressObserver();
            if (pluginPogressObserver != null) {
                // pluginBorder.setTitle(pluginPogressObserver.getName());
                if (!progressBarPlugin.isVisible()) {
                    progressBarPlugin.setVisible(true);
                    pack();
                }

                progressBarPlugin.setValue(pluginPogressObserver.getProgress());
                String msg = ""; // NOI18N
                if (pluginPogressObserver.getMessage() != null) {
                    msg = pluginPogressObserver.getMessage();
                }

                progressBarPlugin.setString(msg);
                // progressBarPlugin.setValue(pluginPogressObserver.getMessage());
            }

            repaint();

            if (progressObserver.isFinished()) {
                timer.stop();
                timer = null;
                repaint();
            }
            /*}
             * catch (Throwable t) { t.printStackTrace(); //progressBar.setValue(navigatorLoader.max);
             * statusLabel.setText(navigatorLoader.errorMessage); restartButton.setEnabled(true);
             * cancelButton.setEnabled(false); Toolkit.getDefaultToolkit().beep(); navigator = null; timer.stop();
             * repaint();}*/
        }
    }
}
