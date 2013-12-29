package org.dadacoalition.yedit.editor;

import java.io.StringReader;

import org.dadacoalition.yedit.Activator;
import org.dadacoalition.yedit.YEditLog;
import org.dadacoalition.yedit.preferences.PreferenceConstants;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.util.IPropertyChangeListener;
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
	YEditSourceViewerConfiguration sourceViewerConfig;
	private YAMLContentOutlinePage contentOutline;
	
	private final IPropertyChangeListener propertyChangeListener = new PreferenceChangeListener(this);
	
	public static final String SOURCE_VIEWER_CONFIGURATION_CONTRIB_ID = 
		Activator.PLUGIN_ID + ".sourceViewerConfigurationContribution";

	public YEdit() {
		super();
		colorManager = new ColorManager();
		setKeyBindingScopes( new String[] { "org.dadacoalition.yedit.yeditScope" } );

	}

	public void dispose() {

		try {
			colorManager.dispose();

			if (idleTimer != null) {
				idleTimer.dispose();
			}
			
			Activator.getDefault().getPreferenceStore().removePropertyChangeListener(propertyChangeListener);

			super.dispose();
		} catch (InterruptedException e) {
			YEditLog.logException(e);
		}
	}

	protected void addDocumentIdleListener(IDocumentIdleListener listener) {
		if (idleTimer != null) {
			idleTimer.addListener(listener);
		} else {
			YEditLog.logError( "Failed adding listener for idle document since listener is null" );
			YEditLog.logger.severe( "listener is null" );
		}
	}

	protected void initializeEditor() {
		super.initializeEditor();
		
		YEditSourceViewerConfiguration jsvc = null;
		
		/*
		 * Check for custom YEditSourceViewerConfiguration contributed via
		 * extension point
		 */
		boolean contribFound = false;
		
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint point = registry.getExtensionPoint(SOURCE_VIEWER_CONFIGURATION_CONTRIB_ID);
		IConfigurationElement[] elts = point.getConfigurationElements();
		for(IConfigurationElement elt : elts) {
			if("sourceViewerConfiguration".equals(elt.getName())) {
				try {
					jsvc = (YEditSourceViewerConfiguration) elt.createExecutableExtension("class");
					contribFound = true;
					break;
				} catch (CoreException e) {}
			}
		}

		// otherwise default to the base YEditSourceViewerConfiguration
		if( !contribFound ) {
			jsvc = new YEditSourceViewerConfiguration();
		}
		
		setSourceViewerConfiguration(jsvc);
		sourceViewerConfig = jsvc;
	}

	public void createPartControl(Composite parent) {

		super.createPartControl(parent);
		
		idleTimer = new IdleTimer(getSourceViewer(), Display.getCurrent());
		idleTimer.start();

		IDocumentIdleListener listener = new IDocumentIdleListener(){
            public void editorIdle( ISourceViewer sourceViewer ){
                sourceViewer.invalidateTextPresentation();
            }
        };
        addDocumentIdleListener(listener);
        
        Activator.getDefault().getPreferenceStore().addPropertyChangeListener(propertyChangeListener);

	}
	
	public Object getAdapter(Class required) {
		
		if (IContentOutlinePage.class.equals(required)) {
			if (contentOutline == null) {
				contentOutline = new YAMLContentOutlinePage(getDocumentProvider(), this );
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
		updateContentOutline();
	}
	
	public void doSaveAs() {
		super.doSaveAs();
		markErrors();
		updateContentOutline();
	}

	void updateContentOutline(){
		if (contentOutline != null) {
			contentOutline.update();
		}
	}
	
	/**
	 * Re-initialize the editor after changes have been done to the preferences.
	 */
	void reinitialize(){
	    
        if( getSourceViewer() instanceof SourceViewer){
            ((SourceViewer) getSourceViewer()).unconfigure();
            initializeEditor();
            getSourceViewer().configure(sourceViewerConfig);
        } else {
            String msg = "Expected source viewer to be of type SourceViewer, but is wasn't. ";
            msg += "Might cause problems with preferences.";
            YEditLog.logger.warning(msg);
        }
        
	}
		
	/**
	 * Performs the syntax checking of the file using SnakeYAML.
	 * @return Returns null if not errors are found. If an error is found it returns the exception 
	 * thrown by the SnakeYAML parser.
	 */
	private YAMLException checkForErrors(){
		
		IDocument document = this.getDocumentProvider().getDocument(this.getEditorInput());	
		String content = document.get();

		//when in Symfony compatibility mode quote all scalars before sending the
		//content to SnakeYAML for syntax checking
		IPreferenceStore prefs = Activator.getDefault().getPreferenceStore();
		if( prefs.getBoolean(PreferenceConstants.SYMFONY_COMPATIBILITY_MODE ) ){
		    SymfonyCompatibilityMode sr = new SymfonyCompatibilityMode( sourceViewerConfig.getScanner() );
		    content = sr.fixScalars(document);
		}		
		
		Yaml yamlParser = new Yaml();
		YAMLException parserError = null;
		try {			
			//parse all the YAML documents in the file
			for ( Object data : yamlParser.composeAll( new StringReader(content) ) ){
			}						
		
		} catch ( YAMLException ex ) {
			parserError = ex;
			YEditLog.logger.info( "Encountered YAML syntax error:" + ex.toString() );
		}		

		return parserError;
		
	}
	
	/**
	 * Parses the file and adds error markers for any syntax errors found in the document.
	 * Old error markers are removed before any new markers are added.
	 */
	void markErrors() {
					
		IEditorInput editorInput = this.getEditorInput();
		
		//if the file is not part of a workspace it does not seems that it is a IFileEditorInput
		//but instead a FileStoreEditorInput. Unclear if markers are valid for such files.
		if( !( editorInput instanceof IFileEditorInput ) ){
			YEditLog.logError("Marking errors not supported for files outside of a project." );
			YEditLog.logger.info("editorInput is not a part of a project." );
			return;
		}

		IFile file = ( (IFileEditorInput) editorInput ).getFile();		
		
		//start by clearing all the old markers.
		int depth = IResource.DEPTH_INFINITE;
		try {
			file.deleteMarkers(IMarker.PROBLEM, true, depth);
		} catch (CoreException e) {
			YEditLog.logException(e);
			YEditLog.logger.warning("Failed to delete markers:\n" + e.toString() );
		}		
		
        IPreferenceStore prefs = Activator.getDefault().getPreferenceStore();
        
        String severity = prefs.getString(PreferenceConstants.VALIDATION);
        if( PreferenceConstants.SYNTAX_VALIDATION_IGNORE.equals(severity) ){
        	YEditLog.logger.info("Possible syntax errors ignored due to preference settings");
            return;
        }
		
        int markerSeverity = IMarker.SEVERITY_ERROR;
        
        if (PreferenceConstants.SYNTAX_VALIDATION_WARNING.equals(severity))
        	markerSeverity = IMarker.SEVERITY_WARNING;

		YAMLException syntaxError = checkForErrors();
		
		if( syntaxError == null ){
			YEditLog.logger.fine("No syntax errors");
			return;
		}		
		
		try {
			IMarker marker = file.createMarker(IMarker.PROBLEM);
			marker.setAttribute(IMarker.SEVERITY, markerSeverity);
			
			if( syntaxError instanceof MarkedYAMLException ){
				MarkedYAMLException ex = (MarkedYAMLException) syntaxError;
				marker.setAttribute(IMarker.MESSAGE, ex.getProblem() );
				
				//SnakeYAML uses a 0-based line number while IMarker uses a 1-based 
				//line number so must add 1 to the number reported by SnakeYAML.
				int lineNumber = ex.getProblemMark().getLine() + 1;
				marker.setAttribute(IMarker.LINE_NUMBER, lineNumber );
			} else {
				marker.setAttribute(IMarker.MESSAGE, "General YAMLException from parser" );
			}

		} catch (CoreException e) {
			YEditLog.logException(e);
			YEditLog.logger.warning("Failed to create marker for syntax error: \n" + e.toString() );
		
		}
	}
	
	/**
	 * This method overrides the corresponding method in AbstractDecoratedTextEditor.
	 * It is used to force tab to spaces conversion even if the the preference 
	 * EDITOR_SPACES_FOR_TABS is not set to true. If this method is not present
	 * pressing the tab key will cause the focus to jump out of the editor and on to button
	 * row if the EDITOR_SPACE_FOR_TABS is not true.
	 * @return Always returns true
	 */
	protected boolean isTabsToSpacesConversionEnabled(){
		return true;
	}
	
	protected ColorManager getColorManager(){
	    return colorManager;
	}
	
}
