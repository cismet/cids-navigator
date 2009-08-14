/*
 * EmbeddedComponentsMap.java
 *
 * Created on 27. M\u00E4rz 2003, 09:43
 */

package Sirius.navigator.ui.embedded;

/**
 *
 * @author  pascal
 */
public interface EmbeddedComponentsMap
{
    
    /** Getter for property name.
     * @return Value of property name.
     *
     */
    public String getName(String id);
    
    /** Setter for property name.
     * @param name New value of property name.
     *
     */
    //public void setName(String id, String name);
    
    /** Getter for property visible.
     * @return Value of property visible.
     *
     */
    public boolean isVisible(String id);
    
    /** Setter for property visible.
     * @param visible New value of property visible.
     *
     */
    public void setVisible(String id, boolean visible);
    
    /** Getter for property enabled.
     * @return Value of property enabled.
     *
     */
    public boolean isEnabled(String id);
    
    /** Setter for property enabled.
     * @param enabled New value of property enabled.
     *
     */
    public void setEnabled(String id, boolean enabled);
    
    public boolean isAvailable(String id);
    
    public void add(EmbeddedComponent component);
    
    public void remove(String id);
    
    public EmbeddedComponent get(String id);
    
    public java.util.Iterator getEmbeddedComponents();
}
