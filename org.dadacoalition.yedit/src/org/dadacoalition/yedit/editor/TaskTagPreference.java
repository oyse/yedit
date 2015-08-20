package org.dadacoalition.yedit.editor;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.dadacoalition.yedit.preferences.PreferenceConstants;
import org.eclipse.jface.preference.IPreferenceStore;

public class TaskTagPreference {
    public String tag;
    public String severity;
    
    public TaskTagPreference(){
    	
    }
    
    public TaskTagPreference(String tag, String severity){
        this.tag = tag;
        this.severity = severity;
    }
    
    
    public static List<TaskTagPreference> getTaskTagPreferences(IPreferenceStore prefStore){
    	
        String currTags = prefStore.getString(PreferenceConstants.TODO_TASK_TAGS);    
        String currPrios = prefStore.getString(PreferenceConstants.TODO_TASK_PRIORITIES);
        String[] tags = getTokens(currTags, ","); //$NON-NLS-1$
        String[] prios = getTokens(currPrios, ","); //$NON-NLS-1$
        List<TaskTagPreference> elements = new ArrayList<>(tags.length);
        for (int i = 0; i < tags.length; i++) {
            String tag = tags[i].trim();
            String severity =(i < prios.length) ? prios[i] : PreferenceConstants.TASK_PRIORITY_NORMAL;
            elements.add(new TaskTagPreference(tag, severity));
        }  
        
        return elements;
    	
    }
    
    private static String[] getTokens(String text, String separator) {
        StringTokenizer tok= new StringTokenizer(text, separator); 
        int nTokens= tok.countTokens();
        String[] res= new String[nTokens];
        for (int i= 0; i < res.length; i++) {
            res[i]= tok.nextToken().trim();
        }
        return res;
    }    
    
}