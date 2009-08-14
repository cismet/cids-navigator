package Sirius.navigator.plugin.ui;


import java.awt.event.*;
import javax.swing.*;

import Sirius.navigator.plugin.*;
import Sirius.navigator.plugin.interfaces.*;

/**
 *
 * @author  pascal
 */
public class PluginToolBarButton extends JButton
{
    
    private final PluginMethod method;   
    
    /** Creates a new instance of PluginToolbarButton */
    public PluginToolBarButton(PluginMethod method)
    {
        this.method = method;
        this.addActionListener(new PluginMethodInvoker());
    }
    
    public String getId()
    {
        return method.getId();
    }
    
    private class PluginMethodInvoker implements ActionListener
    {
        /** 
         * Invoked when an method occurs.
         */
        public void actionPerformed(ActionEvent e)
        {
            try
            {
                PluginToolBarButton.this.method.invoke();
            }
            catch(Exception exp)
            {
                exp.printStackTrace();
            }
        }  
    } 
}
