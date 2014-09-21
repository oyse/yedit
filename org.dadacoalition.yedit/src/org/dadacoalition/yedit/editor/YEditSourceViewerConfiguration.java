package org.dadacoalition.yedit.editor;

import java.util.Arrays;
import java.util.Map;

import org.dadacoalition.yedit.Activator;
import org.dadacoalition.yedit.editor.scanner.YAMLScanner;
import org.dadacoalition.yedit.preferences.PreferenceConstants;
import org.dadacoalition.yedit.template.YEditCompletionProcessor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.DefaultIndentLineAutoEditStrategy;
import org.eclipse.jface.text.DefaultLineTracker;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.TabsToSpacesConverter;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.DefaultAnnotationHover;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;


public class YEditSourceViewerConfiguration extends TextSourceViewerConfiguration {

	protected YAMLScanner yamlScanner;
	protected ColorManager colorManager;
	
	public YEditSourceViewerConfiguration(){
		colorManager = new ColorManager();
	}
			
	public IAnnotationHover getAnnotationHover( ISourceViewer sourceViewer ){    
	    //defining this is necessary for getting hover messages on markers.
	    return new DefaultAnnotationHover(); 
	}

	public IPresentationReconciler getPresentationReconciler( ISourceViewer sourceViewer ){
		
		PresentationReconciler pr = new PresentationReconciler();
		DefaultDamagerRepairer dr = new YEditDamageRepairer(getScanner() );

		pr.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		pr.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);			
		
		return pr;
		
	}
	
	public IContentAssistant getContentAssistant( ISourceViewer sourceViewer ){
	    ContentAssistant ca = new ContentAssistant();
	    
	    IContentAssistProcessor cap = new YEditCompletionProcessor();
	    ca.setContentAssistProcessor(cap, IDocument.DEFAULT_CONTENT_TYPE);
	    ca.setInformationControlCreator(getInformationControlCreator(sourceViewer));	    
	    
	    ca.enableAutoInsert(true);
	    
	    return ca;
	    
	}
	
	public IAutoEditStrategy[] getAutoEditStrategies(ISourceViewer sourceViewer, String contentType ){		
		if( IDocument.DEFAULT_CONTENT_TYPE.equals(contentType) ){
		    
		    //If the TabsToSpacesConverter is not configured here the editor field in the template
		    //preference page will not work correctly. Without this configuration the tab key
		    //will be not work correctly and instead change the focus
            int tabWidth= getTabWidth(sourceViewer);
            TabsToSpacesConverter tabToSpacesConverter = new TabsToSpacesConverter();
            tabToSpacesConverter.setLineTracker(new DefaultLineTracker());
            tabToSpacesConverter.setNumberOfSpacesPerTab(tabWidth);		    
		    
		    return new IAutoEditStrategy[] { 
					tabToSpacesConverter,
					new DefaultIndentLineAutoEditStrategy(),
					};
		} else {
			return new IAutoEditStrategy[]{ new DefaultIndentLineAutoEditStrategy() };		
		}
	}
	
	public int getTabWidth( ISourceViewer sourceViewer ){   
		IPreferenceStore prefs = Activator.getDefault().getPreferenceStore();
		return prefs.getInt(PreferenceConstants.SPACES_PER_TAB);
	}
	
	/**
	 * This methods is necessary for proper implementation of Shift Left and Shift Right.
	 * 
	 * This implementation overrides the default implementation to ensure that only spaces
	 * are inserted and not tabs.
	 * 
	 * @returns An array of prefixes. The prefix at position 0 is used when shifting right.
	 * When shifting left all the prefixes are checked and one of the matches that prefix is
	 * removed from the line.
	 */
	public String[] getIndentPrefixes( ISourceViewer sourceViewer, String contentType ){
		int tabWidth = getTabWidth( sourceViewer );
		
		String[] indentPrefixes = new String[ tabWidth ];
		for( int prefixLength = 1; prefixLength <= tabWidth; prefixLength++  ){
			char[] spaceChars = new char[prefixLength];
			Arrays.fill(spaceChars, ' ');
			indentPrefixes[tabWidth - prefixLength] = new String(spaceChars);
		}
		
		return indentPrefixes;
	}
	
	protected YAMLScanner getScanner(){
		
		if( yamlScanner == null ){
			yamlScanner = new YAMLScanner( colorManager );
		}
		return yamlScanner;
		
	}
	
	/**
	 * Returns the prefixes used when doing prefix operations. For YEdit
	 * that means ToggleComment. Without overriding this method ToggleComment will not work.
	 */
	public String[] getDefaultPrefixes( ISourceViewer viewer, String contentType ){
		return new String[] { "#", "" };
	}
	
	
	@Override
	protected Map getHyperlinkDetectorTargets(ISourceViewer sourceViewer) {

		Map targets = super.getHyperlinkDetectorTargets(sourceViewer);
		targets.put(ContentTypeIdForYaml.CONTENT_TYPE_ID_YAML, null);
		return targets;
		
	}
	
	
	@Override
	public IHyperlinkDetector[] getHyperlinkDetectors(ISourceViewer sourceViewer) {

		//TODO: create preference page to enable/disable hyperlink detection
		// @see super.getHyperlinkDetectors();
		return getRegisteredHyperlinkDetectors(sourceViewer);
		
		
	}
	
	@Override
	public ITextDoubleClickStrategy getDoubleClickStrategy(ISourceViewer sourceViewer, String contentType){
	    return new YEditDoubleClickStrategy();
	}
}
