
package Sirius.navigator.ui.status;

public class Status
{
    public static final int MESSAGE_IGNORE      = -1;
    public static final int MESSAGE_POSITION_1  = 1;
    public static final int MESSAGE_POSITION_2  = 2;
    public static final int MESSAGE_POSITION_3  = 3;
    
    public static final int ICON_IGNORE         = -1;
    public static final int ICON_ACTIVATED      = 1;
    public static final int ICON_DEACTIVATED    = 0;
    public static final int ICON_BLINKING       = 2;
    
    /** Holds value of property redIconState. */
    private int redIconState;
    /** Holds value of property greenIconState. */
    private int greenIconState;
    /** Holds value of property statusMessage. */
    private String statusMessage;
    /** Holds value of property messagePosition. */
    private int messagePosition;
    
    public Status()
    {
        this.clear();
    }
    
    public Status(String statusMessage, int messagePosition)
    {
        this.setStatus(statusMessage, messagePosition);
    }
    
    public Status(String statusMessage, int messagePosition, int greenIconState, int redIconState)
    {
        this.setStatus(statusMessage, messagePosition, greenIconState, redIconState);
    }
    
    public void setStatus(String statusMessage, int messagePosition)
    {
        this.clear();
        this.setStatusMessage(statusMessage);
        this.setMessagePosition(messagePosition);
    }
    
    public void setStatus(String statusMessage, int messagePosition, int greenIconState, int redIconState)
    {
        this.setStatus(statusMessage, messagePosition);
        this.setGreenIconState(greenIconState);
        this.setRedIconState(redIconState);
    }
    
    /** Getter for property redIconState.
     * @return Value of property redIconState.
     *
     */
    public int getRedIconState()
    {
        return this.redIconState;
    }
    
    /** Setter for property redIconState.
     * @param redIconState New value of property redIconState.
     *
     */
    public void setRedIconState(int redIconState)
    {
        this.redIconState = redIconState;
    }
    
    /** Getter for property greenIconState.
     * @return Value of property greenIconState.
     *
     */
    public int getGreenIconState()
    {
        return this.greenIconState;
    }
    
    /** Setter for property greenIconState.
     * @param greenIconState New value of property greenIconState.
     *
     */
    public void setGreenIconState(int greenIconState)
    {
        this.greenIconState = greenIconState;
    }
    
    /** Getter for property statusMessage.
     * @return Value of property statusMessage.
     *
     */
    public String getStatusMessage()
    {
        return this.statusMessage;
    }
    
    /** Setter for property statusMessage.
     * @param statusMessage New value of property statusMessage.
     *
     */
    public void setStatusMessage(String statusMessage)
    {
        this.statusMessage = statusMessage;
    }
    
    /** Getter for property messagePosition.
     * @return Value of property messagePosition.
     *
     */
    public int getMessagePosition()
    {
        return this.messagePosition;
    }
    
    /** Setter for property messagePosition.
     * @param messagePosition New value of property messagePosition.
     *
     */
    public void setMessagePosition(int messagePosition)
    {
        this.messagePosition = messagePosition;
    }
    
    public void clear()
    {
        this.setStatusMessage("");//NOI18N
        this.setMessagePosition(MESSAGE_IGNORE);
        this.setGreenIconState(ICON_IGNORE);
        this.setRedIconState(ICON_IGNORE);
    }
    
    public String toString()
    {
        return new String(  "StatusMessage: '" + getStatusMessage() + "'\n" +
        "MessagePosition: '" + getMessagePosition() + "'\n" +
        "GreenIconState: '" +  getGreenIconState() + "'\n" +
        "RedIconState: '" +  getRedIconState() + "'");
    }
}
