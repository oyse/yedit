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
		store.setDefault(PreferenceConstants.BOLD_DEFAULT, false);
        store.setDefault(PreferenceConstants.ITALIC_DEFAULT, false);
        store.setDefault(PreferenceConstants.UNDERLINE_DEFAULT, false);        
		
		PreferenceConverter.setDefault(store, PreferenceConstants.COLOR_COMMENT, new RGB(255,0,50 ) );
        store.setDefault(PreferenceConstants.BOLD_COMMENT, false);
        store.setDefault(PreferenceConstants.ITALIC_COMMENT, false);
        store.setDefault(PreferenceConstants.UNDERLINE_COMMENT, false);        

		PreferenceConverter.setDefault(store, PreferenceConstants.COLOR_KEY, new RGB(0,200,50 ) );
        store.setDefault(PreferenceConstants.BOLD_KEY, false);
        store.setDefault(PreferenceConstants.ITALIC_KEY, false);
        store.setDefault(PreferenceConstants.UNDERLINE_KEY, false);        

		PreferenceConverter.setDefault(store, PreferenceConstants.COLOR_DOCUMENT, new RGB(0,0,0 ) );
        store.setDefault(PreferenceConstants.BOLD_DOCUMENT, false);
        store.setDefault(PreferenceConstants.ITALIC_DOCUMENT, false);
        store.setDefault(PreferenceConstants.UNDERLINE_DOCUMENT, false);        

		PreferenceConverter.setDefault(store, PreferenceConstants.COLOR_SCALAR, new RGB(0,0,0));
        store.setDefault(PreferenceConstants.BOLD_SCALAR, false);
        store.setDefault(PreferenceConstants.ITALIC_SCALAR, false);
        store.setDefault(PreferenceConstants.UNDERLINE_SCALAR, false);        

		PreferenceConverter.setDefault(store, PreferenceConstants.COLOR_ANCHOR, new RGB(175,0,255));
        store.setDefault(PreferenceConstants.BOLD_ANCHOR, false);
        store.setDefault(PreferenceConstants.ITALIC_ANCHOR, false);
        store.setDefault(PreferenceConstants.UNDERLINE_ANCHOR, false);        

		PreferenceConverter.setDefault(store, PreferenceConstants.COLOR_ALIAS, new RGB(175,0,255));
        store.setDefault(PreferenceConstants.BOLD_ALIAS, false);
        store.setDefault(PreferenceConstants.ITALIC_ALIAS, false);
        store.setDefault(PreferenceConstants.UNDERLINE_ALIAS, false);        

		PreferenceConverter.setDefault(store, PreferenceConstants.COLOR_TAG_PROPERTY, new RGB(175,0,255));
        store.setDefault(PreferenceConstants.BOLD_TAG_PROPERTY, false);
        store.setDefault(PreferenceConstants.ITALIC_TAG_PROPERTY, false);
        store.setDefault(PreferenceConstants.UNDERLINE_TAG_PROPERTY, false);        

		PreferenceConverter.setDefault(store, PreferenceConstants.COLOR_INDICATOR_CHARACTER, new RGB(0,0,0));
        store.setDefault(PreferenceConstants.BOLD_INDICATOR_CHARACTER, false);
        store.setDefault(PreferenceConstants.ITALIC_INDICATOR_CHARACTER, false);
        store.setDefault(PreferenceConstants.UNDERLINE_INDICATOR_CHARACTER, false);        

		PreferenceConverter.setDefault(store, PreferenceConstants.COLOR_CONSTANT, new RGB(9, 79, 5));
        store.setDefault(PreferenceConstants.BOLD_CONSTANT, true);
        store.setDefault(PreferenceConstants.ITALIC_CONSTANT, false);
        store.setDefault(PreferenceConstants.UNDERLINE_CONSTANT, false);        
		
		store.setDefault(PreferenceConstants.SPACES_PER_TAB, 2);
		store.setDefault(PreferenceConstants.SECONDS_TO_REEVALUATE, 3);
		
		store.setDefault(PreferenceConstants.OUTLINE_SCALAR_MAX_LENGTH, 30);
		store.setDefault(PreferenceConstants.OUTLINE_SHOW_TAGS, true );
		
		store.setDefault(PreferenceConstants.SYMFONY_COMPATIBILITY_MODE, false);
		
        store.setDefault(PreferenceConstants.AUTO_EXPAND_OUTLINE, true);		
        store.setDefault(PreferenceConstants.VALIDATION, PreferenceConstants.SYNTAX_VALIDATION_ERROR);
		
        
        store.setDefault(PreferenceConstants.FORMATTER_EXPLICIT_END, false);
        store.setDefault(PreferenceConstants.FORMATTER_EXPLICIT_START, false);
        store.setDefault(PreferenceConstants.FORMATTER_FLOW_STYLE, "BLOCK");
        store.setDefault(PreferenceConstants.FORMATTER_SCALAR_STYLE, "PLAIN");
        store.setDefault(PreferenceConstants.FORMATTER_PRETTY_FLOW, true);
        store.setDefault(PreferenceConstants.FORMATTER_LINE_WIDTH, 80);
	}

}
