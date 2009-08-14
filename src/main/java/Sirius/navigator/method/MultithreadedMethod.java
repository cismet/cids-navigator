package Sirius.navigator.method;

import javax.swing.SwingUtilities;

import Sirius.navigator.ui.progress.*;

/**
 * This is the 3rd version of SwingWorker (also known as
 * SwingWorker 3), an abstract class that you subclass to
 * perform GUI-related work in a dedicated thread.  For
 * instructions on using this class, see:
 *
 * http://java.sun.com/docs/books/tutorial/uiswing/misc/threads.html
 *
 * Note that the API changed slightly in the 3rd version:
 * You must now invoke start() on the SwingWorker after
 * creating it.
 */
public abstract class MultithreadedMethod
{
    private ThreadVar threadVar;
    
    protected ProgressObserver progressObserver;
    
    
    public MultithreadedMethod(ProgressObserver progressObserver)
    {
        this.progressObserver = progressObserver;
        this.threadVar = new ThreadVar();
    }
    
    /**
     * Start a thread that will call the <code>construct</code> method
     * and then exit.
     */
    public MultithreadedMethod()
    {
        this(null);
    }
    
    public final ProgressObserver getProgressObserver()
    {
        return this.progressObserver;
    }
    
    public final boolean isProgressObservable()
    {
        return this.progressObserver != null ? true : false;
    }
    /**
     * Start the worker thread.
     */
    public final void invoke(Object object)
    {
        Thread thread = threadVar.get();

        if(threadVar.get() == null)
        {
            threadVar.set(new Thread(new DoInvokeThread())); //, "MultithreadedMethod"));
            this.init(object);
            threadVar.get().start();
        }
    }
    
    /**
     * A new method that interrupts the worker thread.  Call this method
     * to force the worker to stop what it's doing.
     */
    public final void interrupt()
    {
        Thread thread = threadVar.get();
        
        if (thread != null)
        {
            thread.interrupt();
        }
        
        if(this.isProgressObservable())
        {
            progressObserver.setInterrupted(true);
        }
        
        threadVar.clear();
    }
    
    // #########################################################################
    
    /**
     * Place your code that should be executed before the thread starts here
     */
    protected void init(Object object)
    {
        
    }
    
    // .........................................................................
    
    /**
     * Place your code that should be executed in a new thread here
     */
    protected abstract void doInvoke();
    
    // .........................................................................
    
    /**
     * Place your code that should be executed after the thread has finished
     * here. This method is called on the event dispatching thread.
     */
    protected void finish()
    {
        
    }
    
    // #########################################################################
    
    /**
     * Class to maintain reference to current worker thread
     * under separate synchronization control.
     */
    private final static class ThreadVar
    {
        private Thread thread = null;
        
        /*ThreadVar(Thread thread)
        {
            this.thread = thread;
        }*/
        
        synchronized Thread get()
        {
            return thread;
        }
        
        synchronized void clear()
        {
            thread = null;
        }
        
        synchronized void set(Thread thread)
        {
            if(this.thread == null)
            {
                this.thread = thread;
            }
        }
    }
    
    // #########################################################################
    
    private final class DoInvokeThread implements Runnable
    {
        public void run()
        {
            try
            {
                doInvoke();
            }
            finally
            {
                threadVar.clear();
            }
            
            SwingUtilities.invokeLater(new UpdateUIThread());
        }
        
        private final class UpdateUIThread implements Runnable
        {
            public void run()
            {
                finish();
            }
        }
    }   
}
