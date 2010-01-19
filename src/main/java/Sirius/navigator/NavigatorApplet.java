package Sirius.navigator;
/*******************************************************************************
 *
 * Copyright (c)	:	EIG (Environmental Informatics Group)
 * http://www.htw-saarland.de/eig
 * Prof. Dr. Reiner Guettler
 * Prof. Dr. Ralf Denzer
 *
 * HTWdS
 * Hochschule fuer Technik und Wirtschaft des Saarlandes
 * Goebenstr. 40
 * 66117 Saarbruecken
 * Germany
 *
 * Programmers		:	Pascal
 *
 * Project			:	WuNDA 2
 * Version			:	1.0
 * Purpose			:
 * Created			:	11.04.2000
 * History			:
 *
 *******************************************************************************/
import java.applet.AppletContext;
import java.awt.*;
import java.awt.event.*;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.*;
import java.security.*;
import java.util.Properties;
import java.io.*;
import java.net.*;

import org.apache.log4j.*;

import Sirius.navigator.*;
//import Sirius.navigator.Dialog.OptionsDialog;
//import ISClient.ims.client.ISFloatingFrameModel;
//import Sirius.navigator.tools.*;
import Sirius.navigator.ui.dialog.*;
import Sirius.navigator.resource.*;
import Sirius.navigator.method.*;
import Sirius.navigator.ui.progress.*;
import Sirius.navigator.ui.LAFManager;
import Sirius.navigator.exception.*;
import de.cismet.tools.CismetThreadPool;
import org.apache.log4j.lf5.util.Resource;

public class NavigatorApplet extends javax.swing.JApplet
{
    public final static int FIRSTPOS = 0;
    //private NavigatorModel navigatorModel;
    //private ISFloatingFrameModel isFloatingFrameModel;
    
    private JPanel firstContentPane, secondContentPane;
    private JProgressBar progressBar;
    private static final ResourceBundle I18N = ResourceBundle.getBundle("Sirius/navigator/resource/i18n/resources");
    private JProgressBar pluginProgressBar;
    private JLabel infoLabel, statusLabel, pluginInfoLabel, pluginStatusLabel;
    private JButton startButton, optionsButton, cancelButton, restartButton;
    private TitledBorder pluginBorder;
    
    //private NavigatorLoader navigatorLoader;
    private Navigator navigator;
    
    private Timer timer;
    private ButtonListener buttonListener;
    private OptionsDialog optionsDialog;
    
    private NavigatorLoader navigatorLoader;
    private ProgressObserver progressObserver;
    
    private Logger logger;
    
    public void init()
    {
        System.out.println("init()");
        
        try
        {
            // first, check permissions
            System.getSecurityManager().checkPermission(new AllPermission());
            
            // configure log4j
            // the property file is in the jar!
            try
            {
                String log4jProperties = this.getParameter("log4j");
                
                if(log4jProperties == null)
                {
                    PropertyConfigurator.configure(new URL(this.getCodeBase().toString() + "/config/log4j.properties"));
                    
                    logger = Logger.getLogger(NavigatorApplet.class);
                    logger.warn("no log4j properties file specified, using default file: /config/log4j.properties");
                }
                else
                {
                    if(log4jProperties.indexOf('/') != 0)
                    {
                         PropertyConfigurator.configure(new URL(this.getCodeBase().toString() + '/' + log4jProperties));
                    }
                    else
                    {
                        PropertyConfigurator.configure(new URL(this.getCodeBase().toString() + log4jProperties));
                    }
                    
                    logger = Logger.getLogger(NavigatorApplet.class);
                    logger.info("using log4j properties file: log4jProperties");
                        
                }
                
                /*if(this.getParameter("debug") != null && this.getParameter("debug").equalsIgnoreCase("true"))
                {
                    log4jProperties = new Properties();
                    log4jProperties.load(getClass().getResourceAsStream("resource/cfg/log4j.debug.properties"));
                }
                else
                {
                    log4jProperties = new Properties();
                    log4jProperties.load(getClass().getResourceAsStream("resource/cfg/log4j.release.properties"));
                }*/
                
                //PropertyConfigurator.configure(log4jProperties);
                
                
            }
            catch(Throwable t)
            {
                BasicConfigurator.configure();
                logger = Logger.getLogger(NavigatorApplet.class);
                logger.error("could not initialize the logging system", t);
            }
            
            // configure navigator properties
            PropertyManager.getManager().configure(this);
            ResourceManager.getManager().setLocale(PropertyManager.getManager().getLocale());
            //this.getHtmlParameter();
            
            // look and feel ...................................................
            logger.info ("current look and feel: '" + UIManager.getLookAndFeel () + "'");
            LAFManager.getManager().changeLookAndFeel(PropertyManager.getManager().getLookAndFeel(), this);
            logger.info ("current look and feel: '" + UIManager.getLookAndFeel () + "'");
            // look and feel ...................................................
            
            //progressObserver = new ProgressObserver();
            progressObserver = PropertyManager.getManager().getSharedProgressObserver();
            navigatorLoader = new NavigatorLoader(progressObserver);
            
            optionsDialog = new OptionsDialog();
            optionsDialog.setLocationRelativeTo(this);
            
            //navigatorLoader = new NavigatorLoader(navigatorModel, isFloatingFrameModel);
            buttonListener = new ButtonListener();
            
            buildFirstContentPane();
            buildSecondContentPane();
            
            this.setContentPane(firstContentPane);
            //this.setContentPane(secondContentPane);
            this.setSize(400, 350);
            
            timer = new Timer(100, new TimerListener());
        }
        catch(SecurityException sexp)
        {
            sexp.printStackTrace();
            JLabel accessDenied = new JLabel(I18N.getString("Sirius.navigator.NavigatorApplet.accessDenied.text1") + sexp.getMessage() + I18N.getString("Sirius.navigator.NavigatorApplet.accessDenied.text2"));
            accessDenied.setHorizontalTextPosition(JLabel.CENTER);
            JPanel content = new JPanel(new GridLayout(1,1));
            content.add(accessDenied);
            this.setContentPane(content);
        }
        
    }
    
    protected void buildFirstContentPane()
    {
        //GridBagConstraints gridBagConstraints = new GridBagConstraints();
        GridBagConstraints gridBagConstraints;
        firstContentPane = new JPanel(new GridBagLayout());
        firstContentPane.setBorder(new CompoundBorder(new EtchedBorder(), new EmptyBorder(15,15,15,15)));
        
        /*constraints.insets = new Insets(0, 0, 20, 0);
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridheight = 1;
        constraints.gridwidth = 1;
        constraints.gridy = 0;
        constraints.gridx = 0;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;*/
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        //gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        //gridBagConstraints.ipadx = 20;
        //gridBagConstraints.ipady = 20;
        gridBagConstraints.weightx = 1.0;
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new CompoundBorder(new EtchedBorder(), new EmptyBorder(30,25,25,25)));
        //_TA_JLabel infoLabel = new JLabel("<html><center>Willkommen beim<h2>"+navigatorModel.getTitle()+"</h2></center></html>");
        //JLabel welcomeLabel = new JLabel(StringLoader.getString("STL@welcomeTo")+navigatorModel.getTitle()+"</h2></center></html>");
        //JLabel welcomeLabel = new JLabel(StringLoader.getString("STL@welcomeTo")++"</h2></center></html>");
        
        // title label
        String title = this.getParameter("applet_title");
        if(title == null || title.length() == 0)
        {
            title = ResourceManager.getManager().getString("navigator.title");
        }
        JLabel welcomeLabel = new JLabel("<html><center>" + ResourceManager.getManager().getString("applet.welcome") + "<h2>" + title + "</h2></center></html>");
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        panel.add(welcomeLabel, BorderLayout.CENTER);
        firstContentPane.add(panel, gridBagConstraints);
        
        // logo label
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(20, 20, 20, 20);
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        JLabel logoLabel = new JLabel();
        String logoName = this.getParameter("applet_logo");
        if(logoName == null || logoName.length() == 0)
        {
            logoName = "cismet.gif";
        }
        try
        {
            if(logger.isDebugEnabled())logger.debug("loading logo '" + this.getCodeBase() + logoName + "'");
            ImageIcon imageIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(new URL(this.getCodeBase().toString() + logoName)));
            logoLabel.setIcon(imageIcon);
        }
        catch(Exception exp)
        {
            logger.error("could not load logo '" + logoName + "'");
        }
        firstContentPane.add(logoLabel, gridBagConstraints);
        
        
        //constraints.fill = GridBagConstraints.HORIZONTAL;
        //constraints.anchor = GridBagConstraints.CENTER;
        //constraints.insets = new Insets(0, 50, 10, 50);
        //constraints.weightx = 0.5;
        //constraints.gridy++;
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 10);
        gridBagConstraints.weightx = 1.0;
        //_TA_startButton = new JButton("Navigator starten");
        startButton = new JButton(ResourceManager.getManager().getString("navigator.applet.startNavigator"));
        //_TA_startButton.setMnemonic('s');
        startButton.setMnemonic(ResourceManager.getManager().getString("navigator.applet.SMnemonic").charAt(FIRSTPOS));
        startButton.setActionCommand("start");
        startButton.addActionListener(buttonListener);
        firstContentPane.add(startButton, gridBagConstraints);
        
        //constraints.anchor = GridBagConstraints.CENTER;
        //constraints.insets = new Insets(0, 50, 0, 50);
        //constraints.gridy++;
                /*
                _TA_
                optionsButton = new JButton("Erweiterte Optionen");
                optionsButton.setMnemonic('O');
                _TA_
                 */
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        gridBagConstraints.weightx = 1.0;
        optionsButton = new JButton(ResourceManager.getManager().getString("navigator.applet.moreOptions"));
        optionsButton.setMnemonic(ResourceManager.getManager().getString("navigator.applet.OMnemonic").charAt(FIRSTPOS));
        
        optionsButton.setActionCommand("options");
        optionsButton.addActionListener(buttonListener);
        firstContentPane.add(optionsButton, gridBagConstraints);
    }
    
    protected void buildSecondContentPane()
    {
        GridBagConstraints constraints = new GridBagConstraints();
        secondContentPane = new JPanel(new GridBagLayout());
        secondContentPane.setBorder(new CompoundBorder(new EtchedBorder(), new EmptyBorder(15,15,15,15)));
        statusLabel = new JLabel(); //navigatorLoader.startMessage);
        //secondContentPane.setBorder(new EmptyBorder(15,15,15,15));
        
        constraints.insets = new Insets(0, 0, 20, 0);
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridheight = 1;
        constraints.gridwidth = 2;
        constraints.gridy = 0;
        constraints.gridx = 0;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new CompoundBorder(new EtchedBorder(), new EmptyBorder(10,10,10,10)));
        //_TA_JLabel infoLabel = new JLabel("CallServer auf " + navigatorModel.getCallServerIP());
        //infoLabel = new JLabel(StringLoader.getString("STL@callServerOn") + navigatorModel.getCallServerIP());
        infoLabel = new JLabel(ResourceManager.getManager().getString("navigator.applet.callServerOn") + PropertyManager.getManager().getConnectionInfo().getCallserverURL());
        infoLabel.setHorizontalAlignment(JLabel.CENTER);
        panel.add(infoLabel, BorderLayout.CENTER);
        secondContentPane.add(panel, constraints);
        
        constraints.insets = new Insets(0, 0, 20, 0);
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridy++;
        constraints.gridx = 0;
        constraints.weightx = 1.0;
        constraints.weighty = 0.0;
        
        // progress panel
        JPanel progressPanel = new JPanel();
        progressPanel.setLayout(new GridLayout(2,1,5,5));
        progressPanel.setBorder(new CompoundBorder(new EtchedBorder(), new EmptyBorder(10,10,10,10)));
        statusLabel = new JLabel(); //navigatorLoader.startMessage);
        statusLabel.setHorizontalAlignment(JLabel.CENTER);
        progressPanel.add(statusLabel);
        //progressBar = new JProgressBar(0, navigatorLoader.max);
        progressBar = new JProgressBar(0, progressObserver.getMaxProgress());
        progressBar.setStringPainted(true);
        progressBar.setBorderPainted(true);
        progressPanel.add(progressBar);
        secondContentPane.add(progressPanel, constraints);
        
        // plugin progress panel
        JPanel pluginProgressPanel = new JPanel();
        pluginProgressPanel.setLayout(new GridLayout(2,1,5,5));
        pluginBorder = new TitledBorder(I18N.getString("Sirius.navigator.NavigatorApplet.pluginBorder.text"));
        pluginBorder.setTitleJustification(TitledBorder.CENTER);
        
        pluginProgressPanel.setBorder(new CompoundBorder(pluginBorder, new EmptyBorder(10,10,10,10)));
        pluginStatusLabel = new JLabel(); //navigatorLoader.startMessage);
        pluginStatusLabel.setHorizontalAlignment(JLabel.CENTER);
        pluginProgressPanel.add(pluginStatusLabel);
        //progressBar = new JProgressBar(0, navigatorLoader.max);
        pluginProgressBar = new JProgressBar(0, progressObserver.getMaxProgress());
        pluginProgressBar.setStringPainted(true);
        pluginProgressBar.setBorderPainted(true);
        pluginProgressPanel.add(pluginProgressBar);
        constraints.gridy++;
        //constraints.gridx--;
        //progressPanel.add(pluginProgressPanel);
        secondContentPane.add(pluginProgressPanel, constraints);
        
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(0, 10, 0, 10);
        constraints.weightx = 0.5;
        constraints.gridwidth = 1;
        constraints.gridy++;
                /*
                _TA_
                cancelButton = new JButton("Abbrechen");
                cancelButton.setMnemonic('A');
                _TA_
                 */
        cancelButton = new JButton(ResourceManager.getManager().getString("navigator.applet.cancel"));
        cancelButton.setMnemonic(ResourceManager.getManager().getString("navigator.applet.AMnemonic").charAt(FIRSTPOS));
        cancelButton.setActionCommand("cancel");
        cancelButton.addActionListener(buttonListener);
        secondContentPane.add(cancelButton, constraints);
        
        constraints.anchor = GridBagConstraints.EAST;
        constraints.insets = new Insets(0, 10, 0, 10);
        constraints.gridx++;
                /*
                _TA_
                restartButton = new JButton("Neustart");
                restartButton.setMnemonic('N');
                _TA_
                 */
        restartButton = new JButton(ResourceManager.getManager().getString("navigator.applet.restart"));
        restartButton.setMnemonic(ResourceManager.getManager().getString("navigator.applet.NMnemonic").charAt(FIRSTPOS));
        restartButton.setActionCommand("restart");
        restartButton.addActionListener(buttonListener);
        secondContentPane.add(restartButton, constraints);
    }
    
    
    public void start()
    {
        System.out.println("start()");
        
        //NavigatorLogger.printMessage("Applet started");
        if (navigator == null)
        {
            //this.restart();
            this.repaint();
        }
    }
    
    protected void allDone()
    {
        repaint();
        //navigator = navigatorLoader.getNavigator();
        
        if (this.getNavigator() != null)
        {
            //MetaToolkit.centerWindow(navigator);
            this.getNavigator().setVisible(true);
        }
        else
        {
            //_TA_statusLabel.setText("Es ist ein unbekannter Fehler aufgetreten");
            statusLabel.setText(ResourceManager.getManager().getString("navigator.applet.unknownError"));
        }
        
        repaint();
    }
    
    protected void cancel()
    {
        logger.debug("canceling navigator");
        timer.stop();
        navigatorLoader.interrupt();
        //navigatorLoader.cancel();
        //statusLabel.setText(navigatorLoader.cancelMessage);
        
        if (navigator != null)
        {
            navigator.dispose();
            navigator = null;
        }
        
        restartButton.setEnabled(true);
        cancelButton.setEnabled(false);
        
        
        System.gc();
        
    }
    
    
    protected void restart()
    {
        progressObserver.reset();
        logger.debug("restart(): restarting navigator");
        if (this.getNavigator() != null)
        {
            this.getNavigator().dispose();
            this.setNavigator(null);
        }
        
        validate();
        progressBar.setValue(0);
        //statusLabel.setText(navigatorLoader.startMessage);
        statusLabel.setText("");
        
        pluginBorder.setTitle("");
        pluginProgressBar.setValue(0);
        pluginStatusLabel.setText("");
        
        restartButton.setEnabled(false);
        cancelButton.setEnabled(true);
        navigatorLoader.invoke(null);
        timer.start();
    }
    
    /*protected void getHtmlParameter()
    {
        navigatorModel = new NavigatorModel();
        isFloatingFrameModel = new ISFloatingFrameModel();
        BrowserControl browserControl = new BrowserControl(getAppletContext());
     
        // NAVIGATOR PARAMETER =================================================
        navigatorModel.setCallServerIP(getParameter("nav_callServerIP"));
        navigatorModel.setMaxConnections(getParameter("nav_maxConnections"));
        navigatorModel.setBrowserControl(browserControl);
        navigatorModel.setTitle(getParameter("nav_title"));
        navigatorModel.setSize(getParameter("nav_width"), getParameter("nav_height"));
        navigatorModel.setGlobalMethods(getParameter("global_methods"));
     
        navigatorModel.print();
     
        // SICAD IMS (ISFloatingFrame) PARAMETER ===============================
        isFloatingFrameModel.setDefaultISserver(getParameter("isserver"));
        isFloatingFrameModel.setDefaultDataSource(getParameter("datasource"));
        isFloatingFrameModel.setLanguage(getParameter("language"));
        isFloatingFrameModel.setAppletCodeBase(getCodeBase().toString());
        isFloatingFrameModel.setAppletHtmlBase(getDocumentBase().toString());
        isFloatingFrameModel.setAppletContext((AppletContext)browserControl);
        isFloatingFrameModel.setRedirectionURL(getParameter("redirectionurl"));
        isFloatingFrameModel.setDefaultLayer(getParameter("layer"));
        //isFloatingFrameModel.setDefaultScale(new Double(getParameter("scale")).doubleValue());
        //isFloatingFrameModel.setDefaultXCenter(new Double(getParameter("xcenter")).doubleValue());
        //isFloatingFrameModel.setDefaultXCenter(new Double(getParameter("ycenter")).doubleValue());
        //isFloatingFrameModel.setFrameWidth(new Integer(getParameter("iswidth")).intValue());
        //isFloatingFrameModel.setFrameHeight(new Integer(getParameter("isheight")).intValue());
        isFloatingFrameModel.setDefaultAddParameter(getParameter("addparameter"));
     
        isFloatingFrameModel.print();
    }*/
    
    protected void startAction()
    {
        invalidate();
        //infoLabel.setText(StringLoader.getString("STL@callServerOn") + navigatorModel.getCallServerIP());
        infoLabel.setText(ResourceManager.getManager().getString("navigator.applet.callServerOn") + PropertyManager.getManager().getConnectionInfo().getCallserverURL());
        setContentPane(secondContentPane);
        firstContentPane = null;
        optionsDialog = null;
        restart();
    }
    
    private synchronized void setNavigator(Navigator navigator)
    {
        this.navigator = navigator;
    }
    
    private synchronized Navigator getNavigator()
    {
        return this.navigator;
    }
    
    // #########################################################################
    
    /**
     * The actionPerformed method in this class
     * is called when the user presses the start button.
     */
    private class ButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if (e.getActionCommand().equals("start"))
            {
                startAction();
            }
            else if (e.getActionCommand().equals("options"))
            {
                optionsDialog.show();
                startAction();
            }
            else if (e.getActionCommand().equals("restart"))
            {
                //cancel();
                restart();
            }
            else if (e.getActionCommand().equals("cancel"))
            {
                cancel();
            }
            
        }
    }
    
    private class TimerListener implements ActionListener
    {
        public void actionPerformed(ActionEvent evt)
        {
            //try
            //{
            //progressBar.setValue(navigatorLoader.getCurrentProgress());
            //statusLabel.setText(navigatorLoader.getStatusMessage());
            progressBar.setValue(progressObserver.getProgress());
            statusLabel.setText(progressObserver.getMessage());
            
            //plugin progress
            ProgressObserver pluginPogressObserver = progressObserver.getSubProgressObserver();
            if(pluginPogressObserver != null)
            {
                pluginBorder.setTitle(pluginPogressObserver.getName());
                pluginProgressBar.setValue(pluginPogressObserver.getProgress());
                pluginStatusLabel.setText(pluginPogressObserver.getMessage());
            }
            
            repaint();
            
            if (progressObserver.isFinished())
            {
                logger.info("TimerListener: finished");
                NavigatorApplet.this.allDone();
                progressBar.setValue(progressObserver.getMaxProgress());
                pluginProgressBar.setValue(progressObserver.getMaxProgress());
                statusLabel.setText(progressObserver.getMessage());
                restartButton.setEnabled(true);
                cancelButton.setEnabled(false);
                timer.stop();
                repaint();
            }
            /*}
            catch (Throwable t)
            {
                t.printStackTrace();
                //progressBar.setValue(navigatorLoader.max);
                statusLabel.setText(navigatorLoader.errorMessage);
                restartButton.setEnabled(true);
                cancelButton.setEnabled(false);
                Toolkit.getDefaultToolkit().beep();
                navigator = null;
                timer.stop();
                repaint();
            }*/
        }
    }
    
    private final class NavigatorLoader extends MultithreadedMethod
    {
        private NavigatorLoader(ProgressObserver progressObserver)
        {
            super(progressObserver);
        }
        
        protected void doInvoke()
        {
            NavigatorApplet.this.setNavigator(null);
            
            try
            {
                Navigator navigator = new Navigator(progressObserver);
                NavigatorApplet.this.setNavigator(navigator);
            }
            catch(Exception exp)
            {
                logger.fatal("could not create navigator instance", exp);
                ExceptionManager.getManager().showExceptionDialog(ExceptionManager.FATAL, ResourceManager.getManager().getExceptionName("nx01"), ResourceManager.getManager().getExceptionMessage("nx01"), exp);
            }
        }
    }
    
    /*public static void main(String args[])
    {
        JFrame jf = new JFrame("Navigator Plugin");
        NavigatorApplet na = new NavigatorApplet();
        na.progressObserver = new ProgressObserver();
        na.buildSecondContentPane();
     
        jf.setContentPane(na.secondContentPane);
        jf.pack();
        jf.setLocationRelativeTo(null);
        jf.setVisible(true);
    }*/
}


