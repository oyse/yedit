/*
 * Created on 05.12.2003
 *
 */
package org.dadacoalition.yedit.editor;

import java.util.*;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Display;

/**
 * Monitors an editor's document for changes and broadcasts change
 * notifications to registered listeners. Not every change event is
 * reported: a configurable time period must pass after the most recent
 * change before a notification is sent out.
 * 
 * From EPIC Perl editor
 * 
 * @author luelljoc
 * @author jploski
 */
public class IdleTimer extends Thread
{
	private final ISourceViewer sourceViewer;
    private final Display display;
    private final List<IDocumentIdleListener> listeners = new ArrayList<IDocumentIdleListener>();

    private long lastChange = -1L;
	private int waitForTermination = 1000; // millis

    /**
     * @param sourceViewer  viewer monitored for document changes
     * @param display       display to use for broadcasting notifications
     */
	public IdleTimer(ISourceViewer sourceViewer, Display display)
    {
		super("IdleTimer");
        assert sourceViewer != null;
        assert display != null;
        
		this.sourceViewer = sourceViewer;
		this.display = display;
	}

    /**
     * Registers a listener to receive change notifications.
     * This operation triggers an immediate notification to all listeners,
     * regardless of whether the document has changed.
     */
	public synchronized void addListener(IDocumentIdleListener listener)
    {
        listeners.add(listener);
        lastChange = System.currentTimeMillis();
        notifyAll();
	}
    
    /**
     * Terminates the thread.
     */
    public void dispose() throws InterruptedException
    {
        this.interrupt();
        this.join(this.waitForTermination);
    }

    
	public void run()
    {
        IDocumentListener changeListener = new IDocumentListener() {
            public void documentAboutToBeChanged(DocumentEvent event) { }
            public void documentChanged(DocumentEvent event)
            {
                synchronized (IdleTimer.this)
                {
                    lastChange = System.currentTimeMillis();
                    IdleTimer.this.notifyAll();
                }
            } };
        
        sourceViewer.getDocument().addDocumentListener(changeListener);
        // we don't ever call removeDocumentListener because the listener
        // stays registered until the IDocument is disposed together with
        // the editor
        
        try { runImpl(); }
        catch (InterruptedException e) { /* normal termination */ }
    }
    
    private boolean isEditorVisible()
    {
        StyledText widget = ((SourceViewer) sourceViewer).getTextWidget();        
        return widget != null && widget.isVisible();
    }
    
    private void runImpl() throws InterruptedException
    {
        while (!Thread.interrupted())
        {
            synchronized (this)
            {
                while (lastChange == -1) wait();

                int sleep = 1000;

                // note that lastChange might be increased by document
                // changes occurring during the wait
                while (System.currentTimeMillis() < lastChange + sleep)
                    wait(sleep);

                lastChange = -1L;
            }

            try
            {
               IDocumentIdleListener[] _listeners = new IDocumentIdleListener[listeners.size()];
                
                synchronized (this)
                {
                    listeners.toArray(_listeners);
                }
                
                for (int i = 0; i < _listeners.length; i++)
                {
                    final IDocumentIdleListener listener = _listeners[i];                    
                    display.syncExec(new Runnable() {
                        public void run() {
                            if (isEditorVisible())
                                listener.editorIdle(sourceViewer);
                        } });
                }
            }
            catch (SWTException e)
            {
                // This might happen if display is no longer available
            }            
        }
    }
}
