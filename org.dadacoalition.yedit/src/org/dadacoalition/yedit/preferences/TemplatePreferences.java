package org.dadacoalition.yedit.preferences;

import org.dadacoalition.yedit.Activator;
import org.dadacoalition.yedit.editor.YEditSourceViewerConfiguration;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.jface.text.templates.ContextTypeRegistry;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.texteditor.templates.TemplatePreferencePage;

public class TemplatePreferences extends TemplatePreferencePage implements IWorkbenchPreferencePage {

    protected static class YEditEditTemplateDialog extends TemplatePreferencePage.EditTemplateDialog {

        public YEditEditTemplateDialog(Shell arg0, Template arg1, boolean arg2, boolean arg3,
                ContextTypeRegistry arg4) {
            super(arg0, arg1, arg2, arg3, arg4);
        }
        
        protected SourceViewer createViewer(Composite parent) {

            SourceViewer viewer= new SourceViewer(parent, null, null, false, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
            SourceViewerConfiguration configuration = new YEditSourceViewerConfiguration();
            viewer.configure(configuration);         

            return viewer;
        }        
        
    }
    
    public TemplatePreferences(){
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
        setTemplateStore(Activator.getDefault().getTemplateStore());
        setContextTypeRegistry(Activator.getDefault().getContextTypeRegistry());
        
    }

    protected Template editTemplate(Template template, boolean edit, boolean isNameModifiable) {
        YEditEditTemplateDialog dialog= new YEditEditTemplateDialog(getShell(), template, edit, isNameModifiable, getContextTypeRegistry());
        if (dialog.open() == Window.OK) {
            return dialog.getTemplate();
        }
        return null;
    }
    
    protected boolean isShowFormatterSetting() {
        return false;
    }
       
    
}
