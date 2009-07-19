package org.dadacoalition.yedit.preferences;

import org.dadacoalition.yedit.Activator;
import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;

/**
 * The UI for setting the YEdit preferences
 * @author oysteto
 *
 */
public class YEditPreferences
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	public YEditPreferences() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("YEdit Preferences");
	}
	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors() {
		
		addField(new IntegerFieldEditor(PreferenceConstants.SPACES_PER_TAB, "Spaces per tab", getFieldEditorParent(), 2 ) );
		addField(new IntegerFieldEditor(PreferenceConstants.SECONDS_TO_REEVALUATE, "Seconds between syntax reevaluation", getFieldEditorParent(), 3 ) );
		
		addField(new IntegerFieldEditor(PreferenceConstants.OUTLINE_SCALAR_MAX_LENGTH, "Maximum display length of scalar", getFieldEditorParent(), 4 ) );
		addField(new BooleanFieldEditor(PreferenceConstants.OUTLINE_SHOW_TAGS, "Show tags in outline view", getFieldEditorParent() ) );

        //addField(new BooleanFieldEditor(PreferenceConstants.SYMFONY_COMPATIBILITY_MODE, "Symfony compatibility mode", getFieldEditorParent() ) );		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}
	
}