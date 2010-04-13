package Sirius.navigator.ui;


import java.awt.Component;
import javax.swing.*;
import java.util.*;



import org.apache.log4j.Logger;

public final class LAFManager
{
    private final static Logger logger = Logger.getLogger (LAFManager.class);
    private static LAFManager manager = null;
    
    private final LinkedHashMap installedLookAndFeels;
    
    
    private LAFManager ()
    {
        this.installLookAndFeels ();
        
        UIManager.LookAndFeelInfo[] lnfinfo = UIManager.getInstalledLookAndFeels ();
        
        this.installedLookAndFeels = new LinkedHashMap (lnfinfo.length);
        
        for(int i = 0; i < lnfinfo.length; i++)
        {
            if (logger.isDebugEnabled()) {
                logger.debug ("installed look and feel #" + i + ": '" + lnfinfo[i].getName () + "' (" + lnfinfo[i].getClassName () + ")");//NOI18N
            }
            this.installedLookAndFeels.put (lnfinfo[i].getName (), lnfinfo[i]);
        }

        if (logger.isDebugEnabled()) {
            logger.debug ("- SystemLookAndFeel class: '" + UIManager.getSystemLookAndFeelClassName () + "'");//NOI18N
            logger.debug ("- CrossPlatformLookAndFeel class: '" + UIManager.getCrossPlatformLookAndFeelClassName () + "'");//NOI18N
            logger.debug ("- Default look and feel: '" + this.getDefaultLookAndFeel () + "'");//NOI18N
            logger.debug ("- Current look and feel: '" + UIManager.getLookAndFeel () + "'"); //NOI18N
        }
    }
    
    public final static LAFManager getManager ()
    {
        if(manager == null)
        {
            manager = new LAFManager ();
        }
        
        return manager;
    }
    
    public UIManager.LookAndFeelInfo getDefaultLookAndFeel ()
    {
        return UIManager.getInstalledLookAndFeels ()[0];
    }
    
    public boolean isInstalledLookAndFeel (String lnfName)
    {
        return this.installedLookAndFeels.containsKey (lnfName);
    }
    
    public Collection getInstalledLookAndFeelNames ()
    {
        return this.installedLookAndFeels.keySet ();
    }
    
    public boolean changeLookAndFeel (String lnfName)
    {
        return this.changeLookAndFeel (lnfName, null);
    }
    
    public boolean changeLookAndFeel (String lnfName, final Component component)
    {
        UIManager.LookAndFeelInfo lnfinfo = null;
        
        if(this.isInstalledLookAndFeel (lnfName))
        {
            lnfinfo = (UIManager.LookAndFeelInfo)this.installedLookAndFeels.get (lnfName);
        }
        else
        {
            logger.warn ("could not change look & feel: unknown look and feel '" + lnfName + "'");//NOI18N
            lnfinfo = this.getDefaultLookAndFeel ();
        }
        
        try
        {
            if (logger.isInfoEnabled()) {
                logger.info ("changing look & feel to '" + lnfinfo + "' (" + lnfinfo.getClassName () + ")");//NOI18N
            }
            if(lnfinfo.getName ().equalsIgnoreCase ("Plastic 3D"))//NOI18N
            {
                logger.debug ("setting Plastic 3D Theme");//NOI18N
                //com.jgoodies.looks.plastic.Plastic3DLookAndFeel.setMyCurrentTheme (new com.jgoodies.looks.plastic.theme.SkyBluer());
            }

            UIManager.setLookAndFeel (lnfinfo.getClassName ());
      //      UIManager.setLookAndFeel(new SubstanceNebulaLookAndFeel());


            if(component != null)
            {
                SwingUtilities.updateComponentTreeUI (component);
                component.validate ();
            }
        }
        catch(Throwable t)
        {
            logger.error ("could not change look to '" + lnfName + "'", t);//NOI18N
            return false;
        }
        
        return true;
    }
    
    private void installLookAndFeels ()
    {
        try
        {
            logger.debug ("installing GTK+ Look & Feel");//NOI18N
            UIManager.installLookAndFeel ("GTK+", "com.sun.java.swing.plaf.gtk.GTKLookAndFeel");//NOI18N
        }
        catch(Throwable e)
        {
            logger.warn ("could not install GTK+ & Feel",e);//NOI18N
        }
        
        try
        {
            //com.jgoodies.clearlook.ClearLookManager.setMode(com.jgoodies.clearlook.ClearLookMode.ON);
            //javax.swing.UIManager.setLookAndFeel(new com.jgoodies.plaf.plastic.Plastic3DLookAndFeel());
            // javax.swing.UIManager.setLookAndFeel(new PlasticLookAndFeel());
            
            logger.debug ("installing Plastic 3D Look & Feel");//NOI18N
            UIManager.installLookAndFeel ("Plastic 3D", "com.jgoodies.looks.plastic.Plastic3DLookAndFeel");//NOI18N
          
            
            //com.jgoodies.looks.plastic.Plastic3DLookAndFeel.setMyCurrentTheme (new com.jgoodies.looks.plastic.theme.SkyBluer());
        }
        catch (Throwable e)
        {
            logger.warn ("could not install Plastic 3D Look & Feel",e);//NOI18N
        }
    }
}
