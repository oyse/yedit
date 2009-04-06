package y_edit.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.RGB;

import y_edit.Activator;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		PreferenceConverter.setDefault(store, PreferenceConstants.COLOR_DEFAULT, new RGB(0,0,0) );
		PreferenceConverter.setDefault(store, PreferenceConstants.COLOR_COMMENT, new RGB(255,0,50 ) );
		PreferenceConverter.setDefault(store, PreferenceConstants.COLOR_KEY, new RGB(0,200,50 ) );
		PreferenceConverter.setDefault(store, PreferenceConstants.COLOR_DOCUMENT, new RGB(0,0,0 ) );
		PreferenceConverter.setDefault(store, PreferenceConstants.COLOR_SCALAR, new RGB(0,0,255));		
	}

}
