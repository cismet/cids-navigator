/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui;

import Sirius.navigator.docking.CustomView;
import Sirius.navigator.plugin.interfaces.EmbededControlBar;
import Sirius.navigator.tools.NavigatorToolkit;
import Sirius.navigator.ui.widget.*;

import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.event.*;

import java.beans.*;

import java.util.Vector;

import javax.swing.*;

/**
 * DOCUMENT ME!
 *
 * @author   pascal
 * @version  $Revision$, $Date$
 */
public class MutableConstraints implements ComponentListener, PropertyChangeListener {

    //~ Static fields/initializers ---------------------------------------------

    public static final int ANY_INDEX = -1;

    public static final String P1 = "P1"; // NOI18N
    public static final String P2 = "P2"; // NOI18N
    public static final String P3 = "P3"; // NOI18N

    public static final String NONE = "none";                                              // NOI18N
    public static final String PANEL = "javax.swing.JPanel";                               // NOI18N
    public static final String SCROLLPANE = "javax.swing.JScrollPane";                     // NOI18N
    public static final String FLOATINGFRAME = "Sirius.navigator.ui.widget.FloatingFrame"; // NOI18N

    public static final String CENTER = "GridBabLayout"; // NOI18N
    public static final String FLOW = "FlowLayout";      // NOI18N
    public static final String FILL = "GridLayout";      // NOI18N

    protected static final Logger logger = Logger.getLogger(MutableConstraints.class);

    //~ Instance fields --------------------------------------------------------

    protected JComponent component = null;

    protected JComponent container = null;

    /** Holds value of property name. */
    private String name = "Component"; // NOI18N

    /** Utility field used by bound properties. */
    // private java.beans.PropertyChangeSupport propertyChangeSupport =  new java.beans.PropertyChangeSupport(this);
    private javax.swing.event.SwingPropertyChangeSupport propertyChangeSupport =
        new javax.swing.event.SwingPropertyChangeSupport(this);

    /** Holds value of property position. */
    private String position = P3;

    /** Holds value of property containerType. */
    private String containerType = NONE;

    /** Holds value of property componentEventsEnabled. */
    private boolean componentEventsEnabled = false;

    /** Holds value of property mutable. */
    private boolean mutable = false;

    /** Holds value of property icon. */
    private ImageIcon icon = null;

    /** Holds value of property preferredIndex. */
    private int preferredIndex = ANY_INDEX;

    /** Holds value of property toolTip. */
    private String toolTip = null;

    /** Holds value of property id. */
    private String id;

    private FloatingFrameConfigurator floatingFrameConfigurator = null;

    private CustomView constraintView = null;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new MutableConstraints object.
     */
    public MutableConstraints() {
        this(false);
    }

    /**
     * Creates a new instance of ComponentProxy.
     *
     * @param  mutable  DOCUMENT ME!
     */
    public MutableConstraints(final boolean mutable) {
        this.setMutable(mutable);
        this.setId(NavigatorToolkit.getToolkit().generateId());
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Adds a PropertyChangeListener to the listener list.
     *
     * @param  l  The listener to add.
     */
    public void addPropertyChangeListener(final java.beans.PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  propertyName  DOCUMENT ME!
     * @param  l             DOCUMENT ME!
     */
    public void addPropertyChangeListener(final String propertyName, final java.beans.PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(propertyName, l);
    }

    /**
     * Removes a PropertyChangeListener from the listener list.
     *
     * @param  l  The listener to remove.
     */
    public void removePropertyChangeListener(final java.beans.PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  propertyName  DOCUMENT ME!
     * @param  l             DOCUMENT ME!
     */
    public void removePropertyChangeListener(final String propertyName, final java.beans.PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(propertyName, l);
    }

    /**
     * Getter for property container.
     *
     * @return  Value of property container.
     */
    public JComponent getContainer() {
        return this.container;
    }
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public CustomView getView() {
        if (constraintView == null) {
            constraintView = new CustomView(getId(), getName(), getIcon(), getContainer());
            constraintView.setMenuItemTooltip(getToolTip());
            if (component instanceof EmbededControlBar) {
                ((EmbededControlBar)component).setControlBarVisible(false);
                final Vector<AbstractButton> customButtons = ((EmbededControlBar)component).getControlBarButtons();
                if (customButtons != null) {
                    for (final AbstractButton currentButton : customButtons) {
                        constraintView.getCustomTabComponents().add(currentButton);
                    }
                }
            }
        }
        return constraintView;
    }

    /**
     * Getter for property name.
     *
     * @return  Value of property name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Setter for property name.
     *
     * @param  name  New value of property name.
     */
    public void setName(final String name) {
        if (this.isMutable()) {
            final String oldName = this.name;
            this.name = name;
            propertyChangeSupport.firePropertyChange("name", oldName, name); // NOI18N
        } else {
            this.name = name;
        }
    }

    /**
     * Getter for property position.
     *
     * @return  Value of property position.
     */
    public String getPosition() {
        return this.position;
    }

    /**
     * Setter for property position.
     *
     * @param  position  New value of property position.
     */
    public void setPosition(String position) {
        if (!position.equals(P1) && !position.equals(P2) && !position.equals(P3)) {
            logger.warn("'" + position + "' is not a valid layout position, using default '" + P3 + "'"); // NOI18N
            position = P3;
        }

        if (this.isMutable()) {
            final String oldPosition = this.position;
            this.position = position;
            propertyChangeSupport.firePropertyChange("position", oldPosition, position); // NOI18N
        } else {
            this.position = position;
        }
    }

    /**
     * Getter for property containerType.
     *
     * @return  Value of property containerType.
     */
    public String getContainerType() {
        return this.containerType;
    }

    /**
     * Setter for property containerType.
     *
     * @param  containerType  New value of property containerType.
     */
    public void setContainerType(final String containerType) {
        if (!containerType.equals(PANEL) && !containerType.equals(SCROLLPANE) && !containerType.equals(FLOATINGFRAME)
                    && !containerType.equals(NONE)) {
            this.containerType = NONE;
        } else {
            this.containerType = containerType;
        }
    }

    /**
     * Getter for property componentEvents.
     *
     * @return  Value of property componentEvents.
     */
    public boolean isComponentEventsEnabled() {
        return this.componentEventsEnabled;
    }

    /**
     * Setter for property componentEvents.
     *
     * @param  componentEventsEnabled  New value of property componentEvents.
     */
    public void setComponentEventsEnabled(final boolean componentEventsEnabled) {
        if (container != null) {
            if (!this.componentEventsEnabled && componentEventsEnabled) {
                container.addComponentListener(this);
            } else if (this.componentEventsEnabled && !componentEventsEnabled) {
                container.removeComponentListener(this);
            }

            this.componentEventsEnabled = componentEventsEnabled;
        } else {
            logger.warn("could not set componentEventsEnabled to '" + componentEventsEnabled + "', container is null"); // NOI18N
        }
    }

    /**
     * Getter for property icon.
     *
     * @return  Value of property icon.
     */
    public ImageIcon getIcon() {
        return this.icon;
    }

    /**
     * Setter for property icon.
     *
     * @param  icon  New value of property icon.
     */
    public void setIcon(final ImageIcon icon) {
        if (this.isMutable()) {
            final Icon oldIcon = this.icon;
            this.icon = icon;
            propertyChangeSupport.firePropertyChange("icon", oldIcon, icon); // NOI18N
        } else {
            this.icon = icon;
        }
    }

    /**
     * Getter for property preferredIndex.
     *
     * @return  Value of property preferredIndex.
     */
    public int getPreferredIndex() {
        return this.preferredIndex;
    }

    /**
     * Setter for property preferredIndex.
     *
     * @param  preferredIndex  New value of property preferredIndex.
     */
    public void setPreferredIndex(final int preferredIndex) {
        if (this.isMutable()) {
            final int oldPreferredIndex = this.preferredIndex;
            this.preferredIndex = preferredIndex;
            propertyChangeSupport.firePropertyChange(
                "preferredIndex",
                new Integer(oldPreferredIndex),
                new Integer(preferredIndex)); // NOI18N
        } else {
            this.preferredIndex = preferredIndex;
        }
    }

    /**
     * Getter for property toolTip.
     *
     * @return  Value of property toolTip.
     */
    public String getToolTip() {
        return this.toolTip;
    }

    /**
     * Setter for property toolTip.
     *
     * @param  toolTip  New value of property toolTip.
     */
    public void setToolTip(final String toolTip) {
        if (this.isMutable()) {
            final String oldToolTip = this.toolTip;
            this.toolTip = toolTip;
            propertyChangeSupport.firePropertyChange("toolTip", oldToolTip, toolTip); // NOI18N
        } else {
            this.toolTip = toolTip;
        }
    }

    /**
     * Getter for property mutable.
     *
     * @return  Value of property mutable.
     */
    public boolean isMutable() {
        return this.mutable;
    }

    /**
     * Setter for property mutable.
     *
     * @param  mutable  New value of property mutable.
     */
    public void setMutable(final boolean mutable) {
        this.mutable = mutable;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  id              DOCUMENT ME!
     * @param  name            DOCUMENT ME!
     * @param  toolTip         DOCUMENT ME!
     * @param  icon            DOCUMENT ME!
     * @param  position        DOCUMENT ME!
     * @param  preferredIndex  DOCUMENT ME!
     * @param  containerType   DOCUMENT ME!
     */
    protected void setProperties(final String id,
            final String name,
            final String toolTip,
            final ImageIcon icon,
            final String position,
            final int preferredIndex,
            final String containerType) {
        this.setId(id);
        this.setName(name);
        this.setToolTip(toolTip);
        this.setIcon(icon);
        this.setPosition(position);
        this.setPreferredIndex(preferredIndex);
        this.setContainerType(containerType);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  component  DOCUMENT ME!
     */
    protected void addAsComponent(final JComponent component) {
        this.component = component;
        this.container = component;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  id              DOCUMENT ME!
     * @param  component       DOCUMENT ME!
     * @param  name            DOCUMENT ME!
     * @param  toolTip         DOCUMENT ME!
     * @param  icon            DOCUMENT ME!
     * @param  position        DOCUMENT ME!
     * @param  preferredIndex  DOCUMENT ME!
     */
    public void addAsComponent(final String id,
            final JComponent component,
            final String name,
            final String toolTip,
            final ImageIcon icon,
            final String position,
            final int preferredIndex) {
        this.setProperties(id, name, toolTip, icon, position, preferredIndex, NONE);
        this.addAsComponent(component);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  component               DOCUMENT ME!
     * @param  componentEventsEnabled  DOCUMENT ME!
     * @param  layout                  DOCUMENT ME!
     */
    protected void addAsPanel(final JComponent component, final boolean componentEventsEnabled, final String layout) {
        this.component = component;
        container = new JPanel();

        if (layout.equalsIgnoreCase(FLOW)) {
            container.setLayout(new FlowLayout());
        } else if (layout.equalsIgnoreCase(FILL)) {
            container.setLayout(new GridLayout(1, 1));
        } else {
            logger.warn("'" + layout + "' is not a valid layout, using default '" + FILL + "'"); // NOI18N
            container.setLayout(new GridLayout(1, 1));
        }

        if (Sirius.navigator.resource.PropertyManager.getManager().isAdvancedLayout()) {
            container.setBorder(new javax.swing.border.EmptyBorder(1, 1, 1, 1));
            component.setBorder(null);
        }

        container.add(component);
        this.setComponentEventsEnabled(componentEventsEnabled);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  id                      DOCUMENT ME!
     * @param  component               DOCUMENT ME!
     * @param  name                    DOCUMENT ME!
     * @param  toolTip                 DOCUMENT ME!
     * @param  icon                    DOCUMENT ME!
     * @param  position                DOCUMENT ME!
     * @param  preferredIndex          DOCUMENT ME!
     * @param  componentEventsEnabled  DOCUMENT ME!
     * @param  layout                  DOCUMENT ME!
     */
    public void addAsPanel(final String id,
            final JComponent component,
            final String name,
            final String toolTip,
            final ImageIcon icon,
            final String position,
            final int preferredIndex,
            final boolean componentEventsEnabled,
            final String layout) {
        this.setProperties(id, name, toolTip, icon, position, preferredIndex, PANEL);
        this.addAsPanel(component, componentEventsEnabled, layout);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  component               DOCUMENT ME!
     * @param  componentEventsEnabled  DOCUMENT ME!
     */
    protected void addAsScrollPane(final JComponent component, final boolean componentEventsEnabled) {
        this.component = component;
        container = new JPanel(new BorderLayout());
        final JScrollPane scrollPane = new JScrollPane(component);

        if (Sirius.navigator.resource.PropertyManager.getManager().isAdvancedLayout()) {
            container.setBorder(new javax.swing.border.EmptyBorder(1, 1, 1, 1));
            component.setBorder(null);
            scrollPane.setBorder(null);
            scrollPane.setViewportBorder(null);
        }

        container.add(scrollPane, BorderLayout.CENTER);
        this.setComponentEventsEnabled(componentEventsEnabled);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  id                      DOCUMENT ME!
     * @param  component               DOCUMENT ME!
     * @param  name                    DOCUMENT ME!
     * @param  toolTip                 DOCUMENT ME!
     * @param  icon                    DOCUMENT ME!
     * @param  position                DOCUMENT ME!
     * @param  preferredIndex          DOCUMENT ME!
     * @param  componentEventsEnabled  DOCUMENT ME!
     */
    public void addAsScrollPane(final String id,
            final JComponent component,
            final String name,
            final String toolTip,
            final ImageIcon icon,
            final String position,
            final int preferredIndex,
            final boolean componentEventsEnabled) {
        this.component = component;
        this.setProperties(id, name, toolTip, icon, position, preferredIndex, SCROLLPANE);
        this.addAsScrollPane(component, componentEventsEnabled);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  component               DOCUMENT ME!
     * @param  componentEventsEnabled  DOCUMENT ME!
     * @param  configurator            DOCUMENT ME!
     * @param  floatingEventsEnabled   DOCUMENT ME!
     */
    protected void addAsFloatingFrame(final JComponent component,
            final boolean componentEventsEnabled,
            final FloatingFrameConfigurator configurator,
            final boolean floatingEventsEnabled) {
        this.component = component;
        this.floatingFrameConfigurator = configurator;
        container = new FloatingFrame(component, configurator);
        this.setComponentEventsEnabled(componentEventsEnabled);

        if (Sirius.navigator.resource.PropertyManager.getManager().isAdvancedLayout()) {
            container.setBorder(new javax.swing.border.EmptyBorder(1, 1, 1, 1));
            component.setBorder(null);
        }

        if (floatingEventsEnabled) {
            this.addPropertyChangeListener(FloatingFrame.FLOATING, this);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  id                      DOCUMENT ME!
     * @param  component               DOCUMENT ME!
     * @param  name                    DOCUMENT ME!
     * @param  toolTip                 DOCUMENT ME!
     * @param  icon                    DOCUMENT ME!
     * @param  position                DOCUMENT ME!
     * @param  preferredIndex          DOCUMENT ME!
     * @param  componentEventsEnabled  DOCUMENT ME!
     * @param  configurator            DOCUMENT ME!
     * @param  floatingEventsEnabled   DOCUMENT ME!
     */
    public void addAsFloatingFrame(final String id,
            final JComponent component,
            final String name,
            final String toolTip,
            final ImageIcon icon,
            final String position,
            final int preferredIndex,
            final boolean componentEventsEnabled,
            final FloatingFrameConfigurator configurator,
            final boolean floatingEventsEnabled) {
        this.setProperties(id, name, toolTip, icon, position, preferredIndex, FLOATINGFRAME);
        addAsFloatingFrame(component, componentEventsEnabled, configurator, floatingEventsEnabled);
    }

    // ComponentListener implementation ----------------------------------------

    @Override
    public void componentShown(final ComponentEvent ce) {
        // logger.debug("dispatching event: " + ce);
        component.dispatchEvent(ce);
    }

    @Override
    public void componentHidden(final ComponentEvent ce) {
        // logger.debug("dispatching event: " + ce);
        component.dispatchEvent(ce);
    }

    @Override
    public void componentResized(final ComponentEvent ce) {
        // logger.debug("dispatching event: " + ce);
        component.dispatchEvent(ce);
    }

    @Override
    public void componentMoved(final ComponentEvent ce) {
        // logger.debug("dispatching event: " + ce);
        component.dispatchEvent(ce);
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param  evt  A PropertyChangeEvent object describing the event source and the property that has changed.
     */
    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        // logger.debug("dispatching event: " + evt);
        // if(evt.getPropertyName().equals(FloatingFrame.FLOATING))
        // {
        component.firePropertyChange(evt.getPropertyName(),
            ((Boolean)evt.getOldValue()).booleanValue(),
            ((Boolean)evt.getOldValue()).booleanValue());
        // }
    }

    /**
     * Getter for property id.
     *
     * @return  Value of property id.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Setter for property id.
     *
     * @param  id  New value of property id.
     */
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public FloatingFrameConfigurator getFloatingFrameConfigurator() {
        if (this.floatingFrameConfigurator == null) {
            logger.warn("unexpected call to 'getFloatingFrameConfigurator()': no FloatingFrame container"); // NOI18N
        }

        return floatingFrameConfigurator;
    }

    @Override
    public String toString() {
        return new String(" Id: '" + this.getId() + "'\n" // NOI18N
                        + " Name: '" + this.getName() + "'\n" // NOI18N
                        + " ToolTip: '" + this.getToolTip() + "'\n" // NOI18N
                        + " Position: '" + this.getPosition() + "'\n" // NOI18N
                        + " ContainerType: '" + this.getContainerType() + "'\n"); // NOI18N
    }

    /*public static void main(String args[])
     * { JFrame jf = new JFrame("TEST"); JSplitPane jsp = new JSP();  JButton right = new JButton("RIGHT");
     * right.setMinimumSize(new Dimension(300,300));  jsp.setResizeWeight(0.5); jsp.setRightComponent(right);
     * jsp.setLeftComponent(new JTree());  jf.getContentPane().setLayout(new GridLayout(1,1));
     * jf.getContentPane().add(jsp); jf.setSize(640,480); jf.setVisible(true); }
     *
     * static class JSP extends JSplitPane { public int getMinimumDividerLocation()  {     return 300; }  public int
     * getMaximumDividerLocation()  {     return 400; }}*/

}
