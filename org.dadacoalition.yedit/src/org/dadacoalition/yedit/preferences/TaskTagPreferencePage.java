package org.dadacoalition.yedit.preferences;

import org.dadacoalition.yedit.Activator;
import org.dadacoalition.yedit.preferences.tasktag.IStatusChangeListener;
import org.dadacoalition.yedit.preferences.tasktag.TaskTagConfigurationBlock;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.preferences.IWorkbenchPreferenceContainer;

public class TaskTagPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

    public TaskTagPreferencePage(){
        
    }
    
    public void init(IWorkbench workbench) {

        setPreferenceStore(Activator.getDefault().getPreferenceStore());
        setDescription("YEdit Task tag preferences");
        
    }

    @Override
    protected Control createContents(Composite parent) {

        Composite composite= new Composite(parent, SWT.NONE);
        GridLayout layout= new GridLayout();
        layout.marginHeight= 0;
        layout.marginWidth= 0;
        composite.setLayout(layout);
        composite.setFont(parent.getFont());        
        
        IWorkbenchPreferenceContainer container = (IWorkbenchPreferenceContainer) getContainer();
        TaskTagConfigurationBlock configBlock = new TaskTagConfigurationBlock(getNewStatusChangedListener(), null, container, getPreferenceStore());

        configBlock.createContents(composite);
        return composite;
        
    }
    

    /**
     * Returns a new status change listener that calls {@link #setPreferenceContentStatus(IStatus)}
     * when the status has changed
     * @return The new listener
     */
    protected IStatusChangeListener getNewStatusChangedListener() {
        return new IStatusChangeListener() {
            public void statusChanged(IStatus status) {
                return;
            }
        };
    }    




}
