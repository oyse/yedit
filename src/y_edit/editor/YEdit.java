package y_edit.editor;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.*;

public class YEdit extends TextEditor {

	private ColorManager colorManager;
	private IdleTimer idleTimer;
	private YEditSourceViewerConfiguration sourceViewerConfig;
	private YAMLContentOutlinePage contentOutline;

	public YEdit() {
		super();
		colorManager = new ColorManager();

	}

	public void dispose() {

		try {
			colorManager.dispose();

			if (idleTimer != null) {
				idleTimer.dispose();
			}

			super.dispose();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	protected void addDocumentIdleListener(IDocumentIdleListener listener) {
		if (idleTimer != null) {
			idleTimer.addListener(listener);
		} else {
			System.out.println("Null for faen!");
		}
	}

	protected void initializeEditor() {
		super.initializeEditor();

		YEditSourceViewerConfiguration jsvc = new YEditSourceViewerConfiguration(this);
		setSourceViewerConfiguration(jsvc);
		sourceViewerConfig = jsvc;


	}

	public void createPartControl(Composite parent) {

		super.createPartControl(parent);
		
		idleTimer = new IdleTimer(getSourceViewer(), Display.getCurrent());
		idleTimer.start();

		sourceViewerConfig.addDocumentIdleListener();

	}
	
	public Object getAdapter(Class required) {
		if (IContentOutlinePage.class.equals(required)) {
			if (contentOutline == null) {
				contentOutline = new YAMLContentOutlinePage(getDocumentProvider(), this);
				if (getEditorInput() != null)
					contentOutline.setInput(getEditorInput());
			}
			return contentOutline;
		}
		
		return super.getAdapter(required);
	}
	

	public void doSave(IProgressMonitor monitor) {
		super.doSave(monitor);
		markErrors();
		if (contentOutline != null)
			contentOutline.update();
		
		
	}
	
	public void doSaveAs() {
		super.doSaveAs();
		markErrors();
		if (contentOutline != null)
			contentOutline.update();
	}

	
	private YAMLException checkForErrors(){
		
		IDocument document = this.getDocumentProvider().getDocument(this.getEditorInput());	
		String content = document.get();

		Yaml yamlParser = new Yaml();
		YAMLException parserError = null;
		try {			
			//parse all the YAML documents in the file
			for ( Object data : yamlParser.loadAll( content ) ){
			}						
		
		} catch ( YAMLException ex ) {
			parserError = ex;
			System.out.println( ex.toString() );
		}		

		return parserError;
		
	}
	
	private void markErrors() {
					
		IEditorInput editorInput = this.getEditorInput();
		
		//if the file is not part of a workspace it does not seems that it is a IFileEditorInput
		//but instead a FileStoreEditorInput. Unclear if markers are valid for such files.
		if( !( editorInput instanceof IFileEditorInput ) ){
			return;
		}

		IFile file = ( (IFileEditorInput) editorInput ).getFile();		
		
		//start by clearing all the old markers.
		int depth = IResource.DEPTH_INFINITE;
		try {
			file.deleteMarkers(IMarker.PROBLEM, true, depth);
		} catch (CoreException e) {
			System.out.println( e );
		}		

		YAMLException syntaxError = checkForErrors();
		
		if( syntaxError == null ){
			return;
		}		
		
		try {
			IMarker marker = file.createMarker(IMarker.PROBLEM);
			marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
			
			if( syntaxError instanceof MarkedYAMLException ){
				MarkedYAMLException ex = (MarkedYAMLException) syntaxError;
				marker.setAttribute(IMarker.MESSAGE, ex.getProblem() );
				marker.setAttribute(IMarker.LINE_NUMBER, ex.getProblemMark().getLine() );
			} else {
				marker.setAttribute(IMarker.MESSAGE, "General YAMLException from parser" );
			}

		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
}
