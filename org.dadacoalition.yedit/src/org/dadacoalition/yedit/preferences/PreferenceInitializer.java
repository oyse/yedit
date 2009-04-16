package org.dadacoalition.yedit.preferences;

import org.dadacoalition.yedit.Activator;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.RGB;


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
		PreferenceConverter.setDefault(store, PreferenceConstants.COLOR_ANCHOR, new RGB(175,0,255));
		PreferenceConverter.setDefault(store, PreferenceConstants.COLOR_ALIAS, new RGB(175,0,255));
		PreferenceConverter.setDefault(store, PreferenceConstants.COLOR_TAG_PROPERTY, new RGB(175,0,255));
		PreferenceConverter.setDefault(store, PreferenceConstants.COLOR_FLOW_CHARACTER, new RGB(0,0,0));

		store.setDefault(PreferenceConstants.SPACES_PER_TAB, 2);
		store.setDefault(PreferenceConstants.SECONDS_TO_REEVALUATE, 3);
	}

}
