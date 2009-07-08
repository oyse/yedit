package org.dadacoalition.yedit.preferences;

import org.dadacoalition.yedit.Activator;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.texteditor.templates.TemplatePreferencePage;

public class TemplatePreferences extends TemplatePreferencePage implements IWorkbenchPreferencePage {

    public TemplatePreferences(){
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
        setTemplateStore(Activator.getDefault().getTemplateStore());
        setContextTypeRegistry(Activator.getDefault().getContextTypeRegistry());
        
    }
    
    protected boolean isShowFormatterSetting() {
        return true;
    }
       
    
}
