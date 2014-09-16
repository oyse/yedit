package org.dadacoalition.yedit.preferences;

import org.dadacoalition.yedit.Activator;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * Preference page for the Yaml formatter
 */
public class FormatterPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage{

    public FormatterPreferencePage() {
        super(GRID);
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
        setDescription("YAML Formatter Preferences");        
    }
    
    @Override
    protected void createFieldEditors() {
        
        String[][] flowStyles = {{"Block", "BLOCK"}, {"Flow", "FLOW"} };
        addField(new ComboFieldEditor(PreferenceConstants.FORMATTER_FLOW_STYLE, "Flow style", flowStyles, getFieldEditorParent()));
        addField(new BooleanFieldEditor(PreferenceConstants.FORMATTER_PRETTY_FLOW, "Pretty print in when in Flow style", BooleanFieldEditor.SEPARATE_LABEL, getFieldEditorParent()));
        
        String[][] scalarStyles = {{"Plain", "PLAIN"},{"Single quoted", "SINGLE_QUOTED"},{"Double quoted", "DOUBLE_QUOTED"} };
        addField(new ComboFieldEditor(PreferenceConstants.FORMATTER_SCALAR_STYLE, "Scalar style", scalarStyles, getFieldEditorParent()));
        
        addField(new BooleanFieldEditor(PreferenceConstants.FORMATTER_EXPLICIT_START, "Explicit document start", BooleanFieldEditor.SEPARATE_LABEL, getFieldEditorParent()));
        addField(new BooleanFieldEditor(PreferenceConstants.FORMATTER_EXPLICIT_END, "Explicit document end", BooleanFieldEditor.SEPARATE_LABEL, getFieldEditorParent()));
        
        addField(new IntegerFieldEditor(PreferenceConstants.FORMATTER_LINE_WIDTH, "The line width to use", getFieldEditorParent(), 4));

    }

    public void init(IWorkbench workbench) {
        // TODO Auto-generated method stub
        
    }

}
