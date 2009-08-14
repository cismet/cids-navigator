package Sirius.navigator.ui;


import Sirius.navigator.docking.CustomView;
import Sirius.navigator.plugin.interfaces.EmbededControlBar;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import javax.swing.*;

import org.apache.log4j.Logger;

import Sirius.navigator.ui.widget.*;
import Sirius.navigator.tools.NavigatorToolkit;
import java.util.Vector;

/**
 *
 * @author  pascal
 */
public class MutableConstraints implements ComponentListener, PropertyChangeListener
{
    public final static int ANY_INDEX = -1;
    
    public final static String P1 = "P1";
    public final static String P2 = "P2";
    public final static String P3 = "P3";
    
    public final static String NONE = "none";
    public final static String PANEL = "javax.swing.JPanel";
    public final static String SCROLLPANE = "javax.swing.JScrollPane";
    public final static String FLOATINGFRAME = "Sirius.navigator.ui.widget.FloatingFrame";
    
    public final static String CENTER = "GridBabLayout";
    public final static String FLOW = "FlowLayout";
    public final static String FILL = "GridLayout";
    
    protected final static Logger logger = Logger.getLogger(MutableConstraints.class);
    
    /** Holds value of property name. */
    private String name = "Component";
    
    /** Utility field used by bound properties. */
    //private java.beans.PropertyChangeSupport propertyChangeSupport =  new java.beans.PropertyChangeSupport(this);
    private javax.swing.event.SwingPropertyChangeSupport propertyChangeSupport =  new javax.swing.event.SwingPropertyChangeSupport(this);
    
    /** Holds value of property position. */
    private String position = P3;
    
    /** Holds value of property containerType. */
    private String containerType = NONE;
    
    /** Holds value of property componentEventsEnabled. */
    private boolean componentEventsEnabled = false;
        
    /** Holds value of property mutable. */
    private boolean mutable = false;
    
    protected JComponent component = null;
    
    protected JComponent container = null;
        
    /** Holds value of property icon. */
    private ImageIcon icon = null;
    
    /** Holds value of property preferredIndex. */
    private int preferredIndex = ANY_INDEX;
    
    /** Holds value of property toolTip. */
    private String toolTip = null;
    
    /** Holds value of property id. */
    private String id;
    
    private FloatingFrameConfigurator floatingFrameConfigurator = null;
    
    
    public MutableConstraints()
    {
        this(false);
    }
    
    /** Creates a new instance of ComponentProxy */
    public MutableConstraints(boolean mutable)
    {
        this.setMutable(mutable);
        this.setId(NavigatorToolkit.getToolkit().generateId());
    }
    
    /** Adds a PropertyChangeListener to the listener list.
     * @param l The listener to add.
     *
     */
    public void addPropertyChangeListener(java.beans.PropertyChangeListener l)
    {
        propertyChangeSupport.addPropertyChangeListener(l);
    }
    
    public void addPropertyChangeListener(String propertyName, java.beans.PropertyChangeListener l)
    {
        propertyChangeSupport.addPropertyChangeListener(propertyName, l);
    }
    
    /** Removes a PropertyChangeListener from the listener list.
     * @param l The listener to remove.
     *
     */
    public void removePropertyChangeListener(java.beans.PropertyChangeListener l)
    {
        propertyChangeSupport.removePropertyChangeListener(l);
    }
    
    public void removePropertyChangeListener(String propertyName, java.beans.PropertyChangeListener l)
    {
        propertyChangeSupport.removePropertyChangeListener(propertyName, l);
    }
       
    /** Getter for property container.
     * @return Value of property container.
     *
     */
    public JComponent getContainer()
    {
        return this.container;
    }
    
    private CustomView constraintView=null;
    public CustomView getView(){
        if(constraintView== null){            
            constraintView= new CustomView(getId(),getName(),getIcon(),getContainer());                      
            if(component instanceof EmbededControlBar){
               ((EmbededControlBar)component).setControlBarVisible(false);
               Vector<AbstractButton> customButtons = ((EmbededControlBar)component).getControlBarButtons();
               if(customButtons != null){
                   for(AbstractButton currentButton:customButtons){
                       constraintView.getCustomTabComponents().add(currentButton);
                   }
               }
               
            }
        } 
         return constraintView;         
    }
    
    /** Getter for property name.
     * @return Value of property name.
     *
     */
    public String getName()
    {
        return this.name;
    }
    
    /** Setter for property name.
     * @param name New value of property name.
     *
     */
    public void setName(String name)
    {
        if(this.isMutable())
        {
            String oldName = this.name;
            this.name = name;
            propertyChangeSupport.firePropertyChange("name", oldName, name);
        }
        else
        {
            this.name = name;
        }
    }
    
    /** Getter for property position.
     * @return Value of property position.
     *
     */
    public String getPosition()
    {
        return this.position;
    }
    
    /** Setter for property position.
     * @param position New value of property position.
     *
     */
    public void setPosition(String position)
    {
        if(!position.equals(P1) && !position.equals(P2) && !position.equals(P3))
        {
            logger.warn("'" + position + "' is not a valid layout position, using default '" + P3 + "'");
            position = P3; 
        }
          
        if(this.isMutable())
        {
            String oldPosition = this.position;
            this.position = position;
            propertyChangeSupport.firePropertyChange("position", oldPosition, position);
        }
        else
        {
            this.position = position;
        }
    }
    
    /** Getter for property containerType.
     * @return Value of property containerType.
     *
     */
    public String getContainerType()
    {
        return this.containerType;
    }
    
    /** Setter for property containerType.
     * @param containerType New value of property containerType.
     *
     */
    public void setContainerType(String containerType)
    {
        if(!containerType.equals(PANEL) && !containerType.equals(SCROLLPANE) && !containerType.equals(FLOATINGFRAME) && !containerType.equals(NONE))
        {
            this.containerType = NONE;
        }
        else
        {
            this.containerType = containerType;
        }
        
        
    }
    
    /** Getter for property componentEvents.
     * @return Value of property componentEvents.
     *
     */
    public boolean isComponentEventsEnabled()
    {
        return this.componentEventsEnabled;
    }
    
    /** Setter for property componentEvents.
     * @param componentEvents New value of property componentEvents.
     *
     */
    public void setComponentEventsEnabled(boolean componentEventsEnabled)
    {
        if(container != null)
        {
            if(!this.componentEventsEnabled && componentEventsEnabled)
            {
                container.addComponentListener(this);
            }
            else if(this.componentEventsEnabled && !componentEventsEnabled) 
            {
                container.removeComponentListener(this);
            }
            
            this.componentEventsEnabled = componentEventsEnabled;
        }
        else
        {
            logger.warn("could not set componentEventsEnabled to '" + componentEventsEnabled + "', container is null");
        }
    }
    
    /** Getter for property icon.
     * @return Value of property icon.
     *
     */
    public ImageIcon getIcon()
    {
        return this.icon;
    }
    
    /** Setter for property icon.
     * @param icon New value of property icon.
     *
     */
    public void setIcon(ImageIcon icon)
    {
        if(this.isMutable())
        {
            Icon oldIcon = this.icon;
            this.icon = icon;
            propertyChangeSupport.firePropertyChange("icon", oldIcon, icon);
        }
        else
        {
            this.icon = icon;
        }  
    }
    
    /** Getter for property preferredIndex.
     * @return Value of property preferredIndex.
     *
     */
    public int getPreferredIndex()
    {
        return this.preferredIndex;
    }
    
    /** Setter for property preferredIndex.
     * @param preferredIndex New value of property preferredIndex.
     *
     */
    public void setPreferredIndex(int preferredIndex)
    {
        if(this.isMutable())
        {
            int oldPreferredIndex = this.preferredIndex;
            this.preferredIndex = preferredIndex;
            propertyChangeSupport.firePropertyChange("preferredIndex", new Integer(oldPreferredIndex), new Integer(preferredIndex));
        }
        else
        {
            this.preferredIndex = preferredIndex;
        }  
    }    
    
    /** Getter for property toolTip.
     * @return Value of property toolTip.
     *
     */
    public String getToolTip()
    {
        return this.toolTip;
    }
    
    /** Setter for property toolTip.
     * @param toolTip New value of property toolTip.
     *
     */
    public void setToolTip(String toolTip)
    {
        if(this.isMutable())
        {
            String oldToolTip = this.toolTip;
            this.toolTip = toolTip;
            propertyChangeSupport.firePropertyChange("toolTip", oldToolTip, toolTip);
        }
        else
        {
            this.toolTip = toolTip;
        }     
    }
        
    /** Getter for property mutable.
     * @return Value of property mutable.
     *
     */
    public boolean isMutable()
    {
        return this.mutable;
    }
    
    /** Setter for property mutable.
     * @param mutable New value of property mutable.
     *
     */
    public void setMutable(boolean mutable)
    {
        this.mutable = mutable;
    }
    
    protected void setProperties(String id, String name, String toolTip, ImageIcon icon, String position, int preferredIndex, String containerType)
    {
        this.setId(id);
        this.setName(name);
        this.setToolTip(toolTip);
        this.setIcon(icon);
        this.setPosition(position);
        this.setPreferredIndex(preferredIndex);
        this.setContainerType(containerType);
    }
    
    protected void addAsComponent(JComponent component)
    {
        this.component = component;
        this.container = component;
    }
    
    public void addAsComponent(String id, JComponent component, String name, String toolTip, ImageIcon icon, String position, int preferredIndex)
    {
        this.setProperties(id, name, toolTip, icon, position, preferredIndex, NONE);
        this.addAsComponent(component);
    }
    
    protected void addAsPanel(JComponent component, boolean componentEventsEnabled, String layout)
    {
        this.component = component;
        container = new JPanel();
        
        if(layout.equalsIgnoreCase(FLOW))
        {
            container.setLayout(new FlowLayout());
        }
        else if (layout.equalsIgnoreCase(FILL))
        {
            container.setLayout(new GridLayout(1,1));
        }
        else
        {
            logger.warn("'" + layout + "' is not a valid layout, using default '" + FILL + "'");
            container.setLayout(new GridLayout(1,1));
        }
        
        if(Sirius.navigator.resource.PropertyManager.getManager().isAdvancedLayout())
        {
            container.setBorder(new javax.swing.border.EmptyBorder(1,1,1,1));
            component.setBorder(null);
        }
        
        container.add(component);
        this.setComponentEventsEnabled(componentEventsEnabled);
    }
    
    public void addAsPanel(String id, JComponent component, String name, String toolTip, ImageIcon icon, String position, int preferredIndex, boolean componentEventsEnabled, String layout)
    {
        this.setProperties(id, name, toolTip, icon, position, preferredIndex, PANEL);
        this.addAsPanel(component, componentEventsEnabled, layout);
    }
    
    protected void addAsScrollPane(JComponent component, boolean componentEventsEnabled)
    {
        this.component = component;
        container = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(component);
        
        if(Sirius.navigator.resource.PropertyManager.getManager().isAdvancedLayout())
        {
            container.setBorder(new javax.swing.border.EmptyBorder(1,1,1,1));
            component.setBorder(null);
            scrollPane.setBorder(null);
            scrollPane.setViewportBorder(null);
        }
         
        container.add(scrollPane, BorderLayout.CENTER);
        this.setComponentEventsEnabled(componentEventsEnabled);
    }
    
    public void addAsScrollPane(String id, JComponent component, String name, String toolTip, ImageIcon icon, String position, int preferredIndex, boolean componentEventsEnabled)
    {
        this.component = component;
        this.setProperties(id, name, toolTip, icon, position, preferredIndex, SCROLLPANE);
        this.addAsScrollPane(component, componentEventsEnabled);
    }
    
    protected void addAsFloatingFrame(JComponent component, boolean componentEventsEnabled, FloatingFrameConfigurator configurator, boolean floatingEventsEnabled)
    {
        this.component = component;
        this.floatingFrameConfigurator = configurator;
        container = new FloatingFrame(component, configurator);
        this.setComponentEventsEnabled(componentEventsEnabled);
        
        if(Sirius.navigator.resource.PropertyManager.getManager().isAdvancedLayout())
        {
            container.setBorder(new javax.swing.border.EmptyBorder(1,1,1,1));
            component.setBorder(null);
        }
        
        if(floatingEventsEnabled)
        {
            this.addPropertyChangeListener(FloatingFrame.FLOATING, this);
        } 
    }
    
    public void addAsFloatingFrame(String id, JComponent component, String name, String toolTip, ImageIcon icon, String position, int preferredIndex, boolean componentEventsEnabled, FloatingFrameConfigurator configurator,  boolean floatingEventsEnabled)
    {
        this.setProperties(id, name, toolTip, icon, position, preferredIndex, FLOATINGFRAME);
        addAsFloatingFrame(component, componentEventsEnabled, configurator, floatingEventsEnabled);
    }
        
    // ComponentListener implementation ----------------------------------------
    
    public void componentShown (ComponentEvent ce) 
    {
        //logger.debug("dispatching event: " + ce);
        component.dispatchEvent(ce);
    }

    public void componentHidden (ComponentEvent ce)
    {
        //logger.debug("dispatching event: " + ce);
        component.dispatchEvent(ce);
    }

    public void componentResized (ComponentEvent ce)
    {
        //logger.debug("dispatching event: " + ce);
        component.dispatchEvent(ce);
    }

    public void componentMoved (ComponentEvent ce)
    {
        //logger.debug("dispatching event: " + ce);
        component.dispatchEvent(ce);
    }  
    
    /** This method gets called when a bound property is changed.
     * @param evt A PropertyChangeEvent object describing the event source
     *   	and the property that has changed.
     *
     */
    public void propertyChange(PropertyChangeEvent evt)
    { 
        //logger.debug("dispatching event: " + evt);
        //if(evt.getPropertyName().equals(FloatingFrame.FLOATING))
        //{
            component.firePropertyChange(evt.getPropertyName(), ((Boolean)evt.getOldValue()).booleanValue(), ((Boolean)evt.getOldValue()).booleanValue());
        //} 
    }
    
    /** Getter for property id.
     * @return Value of property id.
     *
     */
    public String getId() 
    {
        return this.id;
    }
    
    /** Setter for property id.
     * @param id New value of property id.
     *
     */
    public void setId(String id) 
    {
        this.id = id;
    }
    
    public FloatingFrameConfigurator getFloatingFrameConfigurator()
    {
        if(this.floatingFrameConfigurator == null)
        {
            logger.warn("unexpected call to 'getFloatingFrameConfigurator()': no FloatingFrame container");
        }
        
        return floatingFrameConfigurator;
    }
    
    public String toString()
    {
        return new String(  " Id: '" + this.getId() + "'\n" + 
                            " Name: '" + this.getName() + "'\n" + 
                            " ToolTip: '" + this.getToolTip() + "'\n" + 
                            " Position: '" + this.getPosition() + "'\n" + 
                            " ContainerType: '" + this.getContainerType() + "'\n");
    }
    
    /*public static void main(String args[])
    {
        JFrame jf = new JFrame("TEST");
        JSplitPane jsp = new JSP();
        
        JButton right = new JButton("RIGHT");
        right.setMinimumSize(new Dimension(300,300));
        
        jsp.setResizeWeight(0.5);
        jsp.setRightComponent(right);
        jsp.setLeftComponent(new JTree());
        
        jf.getContentPane().setLayout(new GridLayout(1,1));
        jf.getContentPane().add(jsp);
        jf.setSize(640,480);
        jf.setVisible(true);
    }
    
    static class JSP extends JSplitPane
    {
        public int getMinimumDividerLocation() 
        {
            return 300;
        }
        
        public int getMaximumDividerLocation() 
        {
            return 400;
        }
    }*/
    
}
