package y_edit.editor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;


public class YEditSourceViewerConfiguration extends SourceViewerConfiguration {

	private YAMLScanner jsonScanner;
	private ColorManager colorManager;
	private YEdit jsonEditor;

	
	public YEditSourceViewerConfiguration( YEdit jsonEditor ){
		this.jsonEditor = jsonEditor;
		colorManager = new ColorManager();
	}
	
	protected void addDocumentIdleListener() {
		
		IDocumentIdleListener listener = new IDocumentIdleListener(){
			public void editorIdle( ISourceViewer sourceViewer ){
				sourceViewer.invalidateTextPresentation();
			}
		};
		
		jsonEditor.addDocumentIdleListener( listener );
		
	}
	
	public IPresentationReconciler getPresentationReconciler( ISourceViewer sourceViewer ){
		
		PresentationReconciler pr = new PresentationReconciler();
		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getScanner() );

		pr.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		pr.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);			
		
		return pr;
		
	}
	
	protected YAMLScanner getScanner(){
		
		if( jsonScanner == null ){
			jsonScanner = new YAMLScanner( colorManager );
		}
		return jsonScanner;
		
	}
	
	
}
