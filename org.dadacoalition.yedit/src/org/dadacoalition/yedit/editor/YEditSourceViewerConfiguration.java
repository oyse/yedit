package org.dadacoalition.yedit.editor;

import org.dadacoalition.yedit.Activator;
import org.dadacoalition.yedit.preferences.PreferenceConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.DefaultAutoIndentStrategy;
import org.eclipse.jface.text.DefaultIndentLineAutoEditStrategy;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IAutoIndentStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TabsToSpacesConverter;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;


public class YEditSourceViewerConfiguration extends SourceViewerConfiguration {

	private YAMLScanner yamlScanner;
	private ColorManager colorManager;
	private YEdit yamlEditor;

	
	public YEditSourceViewerConfiguration( YEdit yamlEditor ){
		this.yamlEditor = yamlEditor;
		colorManager = new ColorManager();
	}
	
	protected void addDocumentIdleListener() {
		
		IDocumentIdleListener listener = new IDocumentIdleListener(){
			public void editorIdle( ISourceViewer sourceViewer ){
				sourceViewer.invalidateTextPresentation();
			}
		};
		
		yamlEditor.addDocumentIdleListener( listener );
		
	}
	
	public IPresentationReconciler getPresentationReconciler( ISourceViewer sourceViewer ){
		
		PresentationReconciler pr = new PresentationReconciler();
		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getScanner() );

		pr.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		pr.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);			
		
		return pr;
		
	}
	
	@Override
	public IAutoEditStrategy[] getAutoEditStrategies(ISourceViewer sourceViewer, String contentType ){		
		if( IDocument.DEFAULT_CONTENT_TYPE.equals(contentType) ){
						
			return new IAutoEditStrategy[] { 
					new TabsToSpacesConverter(),
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
	
	protected YAMLScanner getScanner(){
		
		if( yamlScanner == null ){
			yamlScanner = new YAMLScanner( colorManager );
		}
		return yamlScanner;
		
	}
	
	
}
