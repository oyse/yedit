package org.dadacoalition.yedit.editor;

import org.eclipse.jface.text.IDocument;
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
	
	protected YAMLScanner getScanner(){
		
		if( yamlScanner == null ){
			yamlScanner = new YAMLScanner( colorManager );
		}
		return yamlScanner;
		
	}
	
	
}
