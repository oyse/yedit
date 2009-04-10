package org.dadacoalition.yedit.preferences;

import org.dadacoalition.yedit.Activator;
import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;

/**
 * The UI for setting the Y-Edit preferences
 * @author oysteto
 *
 */
public class YEditPreferences
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	public YEditPreferences() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Y-Edit Preferences");
	}
	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors() {
				
		addField(new ColorFieldEditor(PreferenceConstants.COLOR_DEFAULT, "Default", getFieldEditorParent() ) );
		addField(new ColorFieldEditor(PreferenceConstants.COLOR_COMMENT, "Comments", getFieldEditorParent() ) );
		addField(new ColorFieldEditor(PreferenceConstants.COLOR_KEY, "Keys", getFieldEditorParent() ) );
		addField(new ColorFieldEditor(PreferenceConstants.COLOR_DOCUMENT, "Documents", getFieldEditorParent() ) );
		addField(new ColorFieldEditor(PreferenceConstants.COLOR_SCALAR, "Scalars", getFieldEditorParent() ) );		
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}
	
}