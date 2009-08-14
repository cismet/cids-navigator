/*
 * EmbeddedComponent.java
 *
 * Created on 27. M\u00E4rz 2003, 09:39
 */

package Sirius.navigator.ui.embedded;

/**
 *
 * @author  pascal
 */
public interface EmbeddedComponent
{   
    /** Getter for property visible.
     * @return Value of property visible.
     *
     */
    public boolean isVisible();
    
    /** Setter for property visible.
     * @param visible New value of property visible.
     *
     */
    public void setVisible(boolean visible);
    
    
   
    /** Getter for property enabled.
     * @return Value of property enabled.
     *
     */
    public boolean isEnabled();
    
    /** Setter for property enabled.
     * @param enabled New value of property enabled.
     *
     */
    public void setEnabled(boolean enabled);
    
    /** Getter for property id.
     * @return Value of property id.
     *
     */
    public String getId();
    
    /** Setter for property id.
     * @param id New value of property id.
     *
     */
    //public void setId(String id);
    
    /** Getter for property name.
     * @return Value of property name.
     *
     */
    public String getName();
    
    /** Setter for property name.
     * @param name New value of property name.
     *
     */
    public void setName(String name);
    
}
