/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui.status;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class Status {

    //~ Static fields/initializers ---------------------------------------------

    public static final int MESSAGE_IGNORE = -1;
    public static final int MESSAGE_POSITION_1 = 1;
    public static final int MESSAGE_POSITION_2 = 2;
    public static final int MESSAGE_POSITION_3 = 3;

    public static final int ICON_IGNORE = -1;
    public static final int ICON_ACTIVATED = 1;
    public static final int ICON_DEACTIVATED = 0;
    public static final int ICON_BLINKING = 2;

    //~ Instance fields --------------------------------------------------------

    /** Holds value of property redIconState. */
    private int redIconState;
    /** Holds value of property greenIconState. */
    private int greenIconState;
    /** Holds value of property statusMessage. */
    private String statusMessage;
    /** Holds value of property messagePosition. */
    private int messagePosition;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new Status object.
     */
    public Status() {
        this.clear();
    }

    /**
     * Creates a new Status object.
     *
     * @param  statusMessage    DOCUMENT ME!
     * @param  messagePosition  DOCUMENT ME!
     */
    public Status(final String statusMessage, final int messagePosition) {
        this.setStatus(statusMessage, messagePosition);
    }

    /**
     * Creates a new Status object.
     *
     * @param  statusMessage    DOCUMENT ME!
     * @param  messagePosition  DOCUMENT ME!
     * @param  greenIconState   DOCUMENT ME!
     * @param  redIconState     DOCUMENT ME!
     */
    public Status(final String statusMessage,
            final int messagePosition,
            final int greenIconState,
            final int redIconState) {
        this.setStatus(statusMessage, messagePosition, greenIconState, redIconState);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  statusMessage    DOCUMENT ME!
     * @param  messagePosition  DOCUMENT ME!
     */
    public void setStatus(final String statusMessage, final int messagePosition) {
        this.clear();
        this.setStatusMessage(statusMessage);
        this.setMessagePosition(messagePosition);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  statusMessage    DOCUMENT ME!
     * @param  messagePosition  DOCUMENT ME!
     * @param  greenIconState   DOCUMENT ME!
     * @param  redIconState     DOCUMENT ME!
     */
    public void setStatus(final String statusMessage,
            final int messagePosition,
            final int greenIconState,
            final int redIconState) {
        this.setStatus(statusMessage, messagePosition);
        this.setGreenIconState(greenIconState);
        this.setRedIconState(redIconState);
    }

    /**
     * Getter for property redIconState.
     *
     * @return  Value of property redIconState.
     */
    public int getRedIconState() {
        return this.redIconState;
    }

    /**
     * Setter for property redIconState.
     *
     * @param  redIconState  New value of property redIconState.
     */
    public void setRedIconState(final int redIconState) {
        this.redIconState = redIconState;
    }

    /**
     * Getter for property greenIconState.
     *
     * @return  Value of property greenIconState.
     */
    public int getGreenIconState() {
        return this.greenIconState;
    }

    /**
     * Setter for property greenIconState.
     *
     * @param  greenIconState  New value of property greenIconState.
     */
    public void setGreenIconState(final int greenIconState) {
        this.greenIconState = greenIconState;
    }

    /**
     * Getter for property statusMessage.
     *
     * @return  Value of property statusMessage.
     */
    public String getStatusMessage() {
        return this.statusMessage;
    }

    /**
     * Setter for property statusMessage.
     *
     * @param  statusMessage  New value of property statusMessage.
     */
    public void setStatusMessage(final String statusMessage) {
        this.statusMessage = statusMessage;
    }

    /**
     * Getter for property messagePosition.
     *
     * @return  Value of property messagePosition.
     */
    public int getMessagePosition() {
        return this.messagePosition;
    }

    /**
     * Setter for property messagePosition.
     *
     * @param  messagePosition  New value of property messagePosition.
     */
    public void setMessagePosition(final int messagePosition) {
        this.messagePosition = messagePosition;
    }

    /**
     * DOCUMENT ME!
     */
    public void clear() {
        this.setStatusMessage(""); // NOI18N
        this.setMessagePosition(MESSAGE_IGNORE);
        this.setGreenIconState(ICON_IGNORE);
        this.setRedIconState(ICON_IGNORE);
    }

    @Override
    public String toString() {
        return new String("StatusMessage: '" + getStatusMessage() + "'\n"
                        + "MessagePosition: '" + getMessagePosition() + "'\n"
                        + "GreenIconState: '" + getGreenIconState() + "'\n"
                        + "RedIconState: '" + getRedIconState() + "'");
    }
}
