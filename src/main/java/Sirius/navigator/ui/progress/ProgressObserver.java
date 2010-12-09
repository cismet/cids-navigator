/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package Sirius.navigator.ui.progress;

/*
 * ProgressObserver.java
 *
 * Created on 11. Mai 2003, 19:38
 */

/**
 * DOCUMENT ME!
 *
 * @author   Peter Alzheimer
 * @version  $Revision$, $Date$
 */
public class ProgressObserver {

    //~ Instance fields --------------------------------------------------------

    /** Holds value of property progress. */
    private int progress = 0;

    /** Holds value of property maxProgress. */
    private final int maxProgress;

    /** Holds value of property interrupted. */
    private boolean interrupted = false;

    /** Holds value of property finished. */
    private boolean finished = false;

    /** Holds value of property message. */
    private String message = null;

    /** Holds value of property interruptible. */
    private boolean interruptible = false;

    /** Holds value of property restartable. */
    private boolean restartable = false;

    /** Holds value of property delay. */
    private final int delay;

    /** Holds value of property name. */
    private String name = null;

    /** Holds value of property subProgressObserver. */
    private ProgressObserver subProgressObserver;

    /** Holds value of property indeterminate. */
    private boolean indeterminate = false;

    /** Utility field used by bound properties. */
    private final javax.swing.event.SwingPropertyChangeSupport propertyChangeSupport =
        new javax.swing.event.SwingPropertyChangeSupport(this);

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of ProgressObserver.
     */
    public ProgressObserver() {
        this(1000, 250);
    }

    /**
     * Creates a new ProgressObserver object.
     *
     * @param  maxProgress  DOCUMENT ME!
     * @param  delay        DOCUMENT ME!
     */
    public ProgressObserver(final int maxProgress, final int delay) {
        this.maxProgress = maxProgress;
        this.delay = delay;
        this.reset();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    public synchronized void reset() {
        this.progress = 0;
        this.interrupted = false;
        this.message = null;
        this.finished = false;
    }

    /**
     * Getter for property progress.
     *
     * @return  Value of property progress.
     */
    public synchronized int getProgress() {
        return this.progress;
    }

    /**
     * Setter for property progress.
     *
     * @param   progress  New value of property progress.
     *
     * @throws  InterruptedException  DOCUMENT ME!
     */
    public synchronized void setProgress(final int progress) throws InterruptedException {
        if (this.isInterrupted()) {
            throw new InterruptedException("the thread '" + Thread.currentThread().getName() + "'has been interrupted"); // NOI18N
        }

        if (progress >= this.maxProgress) {
            this.finished = true;
        }

        if (!this.indeterminate) {
            final int oldProgress = this.progress;
            this.progress = progress;

            this.propertyChangeSupport.firePropertyChange("progress", new Integer(oldProgress), new Integer(progress)); // NOI18N
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   progress  DOCUMENT ME!
     * @param   message   DOCUMENT ME!
     *
     * @throws  InterruptedException  DOCUMENT ME!
     */
    public synchronized void setProgress(final int progress, final String message) throws InterruptedException {
        this.setProgress(progress);
        this.setMessage(message);
    }

    /**
     * Getter for property maxProgress.
     *
     * @return  Value of property maxProgress.
     */
    public int getMaxProgress() {
        return this.maxProgress;
    }

    /**
     * Getter for property interrupted.
     *
     * @return  Value of property interrupted.
     */
    public synchronized boolean isInterrupted() {
        return this.interrupted;
    }

    /**
     * Setter for property interrupted.
     *
     * @param  interrupted  New value of property interrupted.
     */
    public synchronized void setInterrupted(final boolean interrupted) {
        this.interrupted = interrupted;
    }

    /**
     * Getter for property finished.
     *
     * @return  Value of property finished.
     */
    public synchronized boolean isFinished() {
        return this.finished;
    }

    /**
     * Setter for property finished.
     *
     * @param   finished  New value of property finished.
     *
     * @throws  InterruptedException  DOCUMENT ME!
     */
    public synchronized void setFinished(final boolean finished) throws InterruptedException {
        // this.progress = this.maxProgress;
        this.setProgress(this.maxProgress);
        this.finished = finished;
    }

    /**
     * Getter for property message.
     *
     * @return  Value of property message.
     */
    public synchronized String getMessage() {
        return this.message;
    }

    /**
     * Setter for property message.
     *
     * @param   message  New value of property message.
     *
     * @throws  InterruptedException  DOCUMENT ME!
     */
    public synchronized void setMessage(final String message) throws InterruptedException {
        if (this.isInterrupted()) {
            throw new InterruptedException("the thread '" + Thread.currentThread().getName() + "'has been interrupted"); // NOI18N
        }

        this.message = message;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public synchronized int getPercentage() {
        if (!this.isIndeterminate()) {
            return (int)Math.round(((double)progress / maxProgress) * 100);
        } else {
            return 0;
        }
    }

    /**
     * Getter for property interruptible.
     *
     * @return  Value of property interruptible.
     */
    public synchronized boolean isInterruptible() {
        return this.interruptible;
    }

    /**
     * Setter for property interruptible.
     *
     * @param  interruptible  New value of property interruptible.
     */
    public synchronized void setInterruptible(final boolean interruptible) {
        this.interruptible = interruptible;
    }

    /**
     * Getter for property restartable.
     *
     * @return  Value of property restartable.
     */
    public synchronized boolean isRestartable() {
        return this.restartable;
    }

    /**
     * Setter for property restartable.
     *
     * @param  restartable  New value of property restartable.
     */
    public synchronized void setRestartable(final boolean restartable) {
        this.restartable = restartable;
    }

    /**
     * Getter for property delay.
     *
     * @return  Value of property delay.
     */
    public synchronized int getDelay() {
        return this.delay;
    }

    /**
     * Getter for property name.
     *
     * @return  Value of property name.
     */
    public synchronized String getName() {
        return this.name;
    }

    /**
     * Setter for property name.
     *
     * @param  name  New value of property name.
     */
    public synchronized void setName(final String name) {
        this.name = name;
    }

    /**
     * Getter for property subProgressObserver.
     *
     * @return  Value of property subProgressObserver.
     */
    public synchronized ProgressObserver getSubProgressObserver() {
        return this.subProgressObserver;
    }

    /**
     * Setter for property subProgressObserver.
     *
     * @param  subProgressObserver  New value of property subProgressObserver.
     */
    public synchronized void setSubProgressObserver(final ProgressObserver subProgressObserver) {
        this.subProgressObserver = subProgressObserver;
    }

    /**
     * Getter for property indeterminate.
     *
     * @return  Value of property indeterminate.
     */
    public synchronized boolean isIndeterminate() {
        return this.indeterminate;
    }

    /**
     * Setter for property indeterminate.
     *
     * @param  indeterminate  New value of property indeterminate.
     */
    public synchronized void setIndeterminate(final boolean indeterminate) {
        this.reset();
        this.indeterminate = indeterminate;
    }

    /**
     * Adds a PropertyChangeListener to the listener list.
     *
     * @param  l  The listener to add.
     */
    public void addPropertyChangeListener(final java.beans.PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    /**
     * Removes a PropertyChangeListener from the listener list.
     *
     * @param  l  The listener to remove.
     */
    public void removePropertyChangeListener(final java.beans.PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }
}
