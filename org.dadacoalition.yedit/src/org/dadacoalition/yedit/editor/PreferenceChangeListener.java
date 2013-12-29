package org.dadacoalition.yedit.editor;

import java.util.ArrayList;
import java.util.List;

import org.dadacoalition.yedit.preferences.PreferenceConstants;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

/**
 * Change listener used to update the editor when preferences changes so that
 * the editor does not have to restarted for changes to take effect.
 */
public class PreferenceChangeListener implements IPropertyChangeListener {

    private final YEdit editor;
    
    private static List<String> sourceConfigurationRelatedProperties = initSourceConfigurationRelatedProperties();   

    public PreferenceChangeListener(YEdit editor) {
        this.editor = editor;
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        String propertyName = event.getProperty();
        if (isMarkErrorRelatedProperty(propertyName)) {
            editor.markErrors();
            editor.updateContentOutline();
        }
        if (isOutlineRelatedProperty(propertyName)) {
            editor.updateContentOutline();
        }
        
        if(isSourceConfigurationRelatedProperty(propertyName)){
            editor.reinitialize();
        }
    }

    private boolean isMarkErrorRelatedProperty(String propertyName) {
        if (propertyName.equals(PreferenceConstants.VALIDATION)
                || propertyName.equals(PreferenceConstants.SYMFONY_COMPATIBILITY_MODE)) {
            return true;
        }
        return false;
    }

    private boolean isOutlineRelatedProperty(String propertyName) {
        if (propertyName.equals(PreferenceConstants.OUTLINE_SCALAR_MAX_LENGTH)
                || propertyName.equals(PreferenceConstants.OUTLINE_SHOW_TAGS)) {
            return true;
        }
        return false;
    }
    
    private boolean isSourceConfigurationRelatedProperty(String propertyName){
        return sourceConfigurationRelatedProperties.contains(propertyName);
    }
    
    private static List<String> initSourceConfigurationRelatedProperties() {

        List<String> relatedProperties = new ArrayList<String>();
        relatedProperties.add(PreferenceConstants.SECONDS_TO_REEVALUATE);
        relatedProperties.add(PreferenceConstants.SPACES_PER_TAB);
        
        relatedProperties.add(PreferenceConstants.COLOR_COMMENT);
        relatedProperties.add(PreferenceConstants.BOLD_COMMENT);
        relatedProperties.add(PreferenceConstants.ITALIC_COMMENT);
        relatedProperties.add(PreferenceConstants.UNDERLINE_COMMENT);
        
        relatedProperties.add(PreferenceConstants.COLOR_KEY);
        relatedProperties.add(PreferenceConstants.BOLD_KEY);
        relatedProperties.add(PreferenceConstants.ITALIC_KEY);
        relatedProperties.add(PreferenceConstants.UNDERLINE_KEY);
        
        relatedProperties.add(PreferenceConstants.COLOR_SCALAR);
        relatedProperties.add(PreferenceConstants.BOLD_SCALAR);
        relatedProperties.add(PreferenceConstants.ITALIC_SCALAR);
        relatedProperties.add(PreferenceConstants.UNDERLINE_SCALAR);
        
        relatedProperties.add(PreferenceConstants.COLOR_DEFAULT);
        relatedProperties.add(PreferenceConstants.BOLD_DEFAULT);
        relatedProperties.add(PreferenceConstants.ITALIC_DEFAULT);
        relatedProperties.add(PreferenceConstants.UNDERLINE_DEFAULT);
        
        relatedProperties.add(PreferenceConstants.COLOR_DOCUMENT);
        relatedProperties.add(PreferenceConstants.BOLD_DOCUMENT);
        relatedProperties.add(PreferenceConstants.ITALIC_DOCUMENT);
        relatedProperties.add(PreferenceConstants.UNDERLINE_DOCUMENT);
        
        relatedProperties.add(PreferenceConstants.COLOR_ANCHOR);
        relatedProperties.add(PreferenceConstants.BOLD_ANCHOR);
        relatedProperties.add(PreferenceConstants.ITALIC_ANCHOR);
        relatedProperties.add(PreferenceConstants.UNDERLINE_ANCHOR);
        
        relatedProperties.add(PreferenceConstants.COLOR_ALIAS);
        relatedProperties.add(PreferenceConstants.BOLD_ALIAS);
        relatedProperties.add(PreferenceConstants.ITALIC_ALIAS);
        relatedProperties.add(PreferenceConstants.UNDERLINE_ALIAS);
        
        relatedProperties.add(PreferenceConstants.COLOR_TAG_PROPERTY);
        relatedProperties.add(PreferenceConstants.BOLD_TAG_PROPERTY);
        relatedProperties.add(PreferenceConstants.ITALIC_TAG_PROPERTY);
        relatedProperties.add(PreferenceConstants.UNDERLINE_TAG_PROPERTY);
        
        relatedProperties.add(PreferenceConstants.COLOR_INDICATOR_CHARACTER);
        relatedProperties.add(PreferenceConstants.BOLD_INDICATOR_CHARACTER);
        relatedProperties.add(PreferenceConstants.ITALIC_INDICATOR_CHARACTER);
        relatedProperties.add(PreferenceConstants.UNDERLINE_INDICATOR_CHARACTER);
        
        relatedProperties.add(PreferenceConstants.COLOR_CONSTANT);
        relatedProperties.add(PreferenceConstants.BOLD_CONSTANT);
        relatedProperties.add(PreferenceConstants.ITALIC_CONSTANT);
        relatedProperties.add(PreferenceConstants.UNDERLINE_CONSTANT);        
        
        return relatedProperties;
    }    
    

}
