package org.dadacoalition.yedit.editor;


import java.io.StringReader;
import java.util.*;

import org.dadacoalition.yedit.Activator;
import org.dadacoalition.yedit.YEditLog;
import org.dadacoalition.yedit.preferences.PreferenceConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.DefaultPositionUpdater;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IPositionUpdater;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;
import org.yaml.snakeyaml.*;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.nodes.*;


public class YAMLContentOutlinePage extends ContentOutlinePage {

	private Object input;
	private IDocumentProvider documentProvider;
	private YEdit yamlEditor;
	
	public static final String YAMLSEGMENT = "_____YAML_Element";
	
	protected class ContentProvider implements ITreeContentProvider {

		protected Yaml yamlParser = new Yaml();
		protected List<YAMLOutlineElement> yamlDocuments = new ArrayList<YAMLOutlineElement>();
		protected IPositionUpdater positionUpdater = new DefaultPositionUpdater(YAMLSEGMENT);		
		
		public void parse(IDocument document){
			
			String content = document.get();
	        
			IPreferenceStore prefs = Activator.getDefault().getPreferenceStore();
			if( prefs.getBoolean(PreferenceConstants.SYMFONY_COMPATIBILITY_MODE ) ){
			    SymfonyCompatibilityMode sr = new SymfonyCompatibilityMode( yamlEditor.sourceViewerConfig.getScanner() );
		        content = sr.fixScalars(document);
	        }   			
			
			yamlDocuments.clear();
			
			try {
				
			    for( Node rootNode : yamlParser.composeAll( new StringReader( content ) ) ){
					YAMLOutlineElement ye = new YAMLOutlineElement( rootNode, document );
					yamlDocuments.add(ye);
				}						
			
			} catch ( YAMLException ex ) {
				YEditLog.logger.info( "Syntax error found during parsing for outlinew view. Parsing stopped." );
			}
		}
		
		
		public Object[] getChildren(Object element) {
			
			if( element instanceof YAMLOutlineElement ){
				return ((YAMLOutlineElement) element).children.toArray();
			}
			
			return null;
		}

		public Object getParent(Object element) {
			
			if( element instanceof YAMLOutlineElement ){
				return ((YAMLOutlineElement) element).parent;
			}
			
			return null;
		}

		public boolean hasChildren(Object element) {

			if( element instanceof YAMLOutlineElement ){
				return ( ((YAMLOutlineElement) element).children.size() == 0 ) ? false : true;
			}
			
			return false;
		}

		public Object[] getElements(Object arg0) {

			if( yamlDocuments != null ){
				return yamlDocuments.toArray();
			}
			return null;
		}

		public void dispose() {
			
			if( yamlDocuments != null ){
				yamlDocuments.clear();
				yamlDocuments = null;
			}
			
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

			YEditLog.logger.fine( "Input to the Outline view was changed." );
			
			if (oldInput != null) {
				IDocument document= documentProvider.getDocument(oldInput);
				if (document != null) {
					try {
						document.removePositionCategory(YAMLSEGMENT);
					} catch (BadPositionCategoryException x) {
					}
					document.removePositionUpdater(positionUpdater);
				}
			}			
			
			if( newInput != null ){
				IDocument document = documentProvider.getDocument(newInput);
				if( document != null ){
					document.addPositionCategory(YAMLSEGMENT);
					document.addPositionUpdater(positionUpdater);
					parse(document);
				}
				
			}						
		}
		
	}
	
	public YAMLContentOutlinePage(IDocumentProvider provider, YEdit editor ){
		super();
		documentProvider = provider;
		yamlEditor = editor;
	}
	
	
	public void createControl(Composite parent) {
	    
	    super.createControl(parent);

        IPreferenceStore prefs = Activator.getDefault().getPreferenceStore();
        
        int expandLevels = TreeViewer.ALL_LEVELS;
        if( !(prefs.getBoolean(PreferenceConstants.AUTO_EXPAND_OUTLINE) ) ){
            expandLevels = 0;
        }
	    
	    
		TreeViewer viewer= getTreeViewer();
		viewer.setContentProvider(new ContentProvider());
		viewer.setLabelProvider( new YEditStyledLabelProvider( yamlEditor.getColorManager() ) );
		viewer.addSelectionChangedListener(this);
		viewer.setAutoExpandLevel(expandLevels);

		if (input != null){
			setInput(input);
		}

	}	
	
	public void setInput(Object input) {
		this.input = input;
		update();
	}
	
	public void selectionChanged( SelectionChangedEvent event ){
		
		YEditLog.logger.info("Select in the outline view changed");
		
		super.selectionChanged(event);

		ISelection selection= event.getSelection();
		if (selection.isEmpty())
			yamlEditor.resetHighlightRange();
		else {
			YAMLOutlineElement segment= (YAMLOutlineElement) ((IStructuredSelection) selection).getFirstElement();
			int start= segment.position.getOffset();
			int length= segment.position.getLength();
			try {
				yamlEditor.setHighlightRange(start, length, true);
			} catch (IllegalArgumentException x) {
				yamlEditor.resetHighlightRange();
			}
		}		
	}
	
	public void update(){	    
		TreeViewer viewer= getTreeViewer();

		if (viewer != null) {
		    viewer.setInput(input);
		}		
	}
	
}
