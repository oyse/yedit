package org.dadacoalition.yedit.editor;


import java.io.StringReader;
import java.util.*;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.DefaultPositionUpdater;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IPositionUpdater;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;
import org.yaml.snakeyaml.*;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.nodes.*;


public class YAMLContentOutlinePage extends ContentOutlinePage {

	private Object input;
	private IDocumentProvider documentProvider;
	private ITextEditor textEditor;
	
	public static final String YAMLSEGMENT = "_____YAML_Element";
	
	protected class YAMLSegment {
		
		public static final int SCALAR = 1;
		public static final int MAPPINGITEM = 2;
		public static final int SEQUENCEITEM = 3;
		public static final int DOCUMENT = 4;
		public static final int MAPPINGSCALAR = 5;
	
		protected Node node;
		protected String key;
		protected String value;
		protected YAMLSegment parent;
		protected List<YAMLSegment> children = new ArrayList<YAMLSegment>();
		protected Position position;
		protected IDocument document;
		protected int type;
		
		protected YAMLSegment( Node node, IDocument document ){
			this( node, null, DOCUMENT, document );		
		}		
		
		protected YAMLSegment( Node node, YAMLSegment parent, int type, IDocument document ){
			this.node = node;
			this.parent = parent;
			this.type = type;
			this.document = document;
			
			parseData( node );
			position = getPosition( node );
			try {
				document.addPosition(YAMLSEGMENT, position);
			} catch (BadLocationException e) {
				e.printStackTrace();
			} catch (BadPositionCategoryException e) {
				e.printStackTrace();
			}			

		}
		
		private Position getPosition( Node node ){
			
			int startLine = node.getStartMark().getLine();
			int startColumn = node.getStartMark().getColumn();
			int endLine = node.getEndMark().getLine();
			int endColumn = node.getEndMark().getColumn();
			Position p = null;
			try {
				int offset = document.getLineOffset(startLine);
				offset += startColumn;
			
				int length;
				if( startLine < endLine ){
					length = document.getLineLength( startLine ) - startColumn;
				} else {
					length = endColumn - startColumn;
				}
				p = new Position( offset, length ); 
			} catch( BadLocationException ex) {
				
			}
			return p; 			
			
		}
		
		private void parseData( Node node ){
			
			if( node instanceof ScalarNode ){
				if( this.type == SEQUENCEITEM ){
					this.type = SCALAR;
				} else if( this.type == MAPPINGITEM ){
					this.type = MAPPINGSCALAR;
				}				
			} else if( node instanceof SequenceNode ){
				SequenceNode sNode = (SequenceNode) node;
				List<Node> children = sNode.getValue();
				for( Node childNode : children ){
					YAMLSegment child = new YAMLSegment( childNode, this, SEQUENCEITEM, document );
					this.children.add(child);					
				}
			} else if ( node instanceof MappingNode ){
				MappingNode mNode = (MappingNode) node;
				List<Node[]> children = mNode.getValue();
				for( Node[] childNode : children ){
					String key = childNode[0].getValue().toString();
					YAMLSegment child = new YAMLSegment( childNode[1], this, MAPPINGITEM, document );
					child.key = key;
					this.children.add(child);
				}
			}
			
//			if( data instanceof List ){
//								
//				List<Object> sequence = (List<Object>) data;
//				for( Object o : sequence ){
//					YAMLSegment child = new YAMLSegment( o, this, SEQUENCEITEM );
//					this.children.add(child);
//									
//				}				
//			} else if( data instanceof Map ){			
//											
//				Map<String,Object> map = (Map<String,Object>) data;
//				for ( String key : map.keySet() ){
//					Object o = map.get(key);
//					YAMLSegment child = new YAMLSegment( o, this, MAPPINGITEM );
//					child.key = key;
//					this.children.add(child);
//					
//				}				
//			} else {	
//
//				//this item has no children to change the type to an element that
//				//has no children
//				if( this.type == SEQUENCEITEM ){
//					this.type = SCALAR;
//				} else if( this.type == MAPPINGITEM ){
//					this.type = MAPPINGSCALAR;
//				}
//				 
//			}
			
		}
		
		public String toString(){
			if( type == YAMLSegment.DOCUMENT ){
				return "";
			} else if( type == YAMLSegment.SCALAR ){
				return node.getValue().toString();
			} else if( type == YAMLSegment.MAPPINGITEM ){
				return key;
			} else if( type == YAMLSegment.SEQUENCEITEM ){
				return "";
			} else if( type == MAPPINGSCALAR ){
				return key + ": " + node.getValue().toString();
			}
			
			return super.toString();
			
		}
		
	}
	
	protected class ContentProvider implements ITreeContentProvider {

		protected Loader yamlParser = new Loader();
		protected List<YAMLSegment> yamlDocuments = new ArrayList<YAMLSegment>();
		protected IPositionUpdater positionUpdater = new DefaultPositionUpdater(YAMLSEGMENT);		
		
		public void parse(IDocument document){
			
			String content = document.get();
			yamlDocuments.clear();
			
			try {
				
				for ( Node rootNode : yamlParser.loadAll2( new StringReader(content) ) ){
					YAMLSegment ye = new YAMLSegment( rootNode, document );
					yamlDocuments.add(ye);
				}						
			
			} catch ( YAMLException ex ) {
				System.out.println( ex.toString() );
			}
		}
		
		
		public Object[] getChildren(Object element) {
			
			if( element instanceof YAMLSegment ){
				return ((YAMLSegment) element).children.toArray();
			}
			
			return null;
		}

		public Object getParent(Object element) {
			
			if( element instanceof YAMLSegment ){
				return ((YAMLSegment) element).parent;
			}
			
			return null;
		}

		public boolean hasChildren(Object element) {

			if( element instanceof YAMLSegment ){
				return ( ((YAMLSegment) element).children.size() == 0 ) ? false : true;
			}
			
			return false;
		}

		public Object[] getElements(Object arg0) {
			// TODO Auto-generated method stub
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

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

			System.out.println( "Input changed" );
			
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
	
	public YAMLContentOutlinePage(IDocumentProvider provider, ITextEditor editor){
		super();
		documentProvider = provider;
		textEditor = editor;
	}
	
	
	public void createControl(Composite parent) {

		super.createControl(parent);

		TreeViewer viewer= getTreeViewer();
		viewer.setContentProvider(new ContentProvider());
		viewer.setLabelProvider(new YAMLContentOutlineLabelProvided());
		viewer.addSelectionChangedListener(this);

		if (input != null){
			viewer.setInput(input);
		}
	}	
	
	public void setInput(Object input) {
		this.input = input;
		update();
	}
	
	public void selectionChanged( SelectionChangedEvent event ){
		System.out.println( "Selection changed" );
		
		super.selectionChanged(event);

		ISelection selection= event.getSelection();
		if (selection.isEmpty())
			textEditor.resetHighlightRange();
		else {
			YAMLSegment segment= (YAMLSegment) ((IStructuredSelection) selection).getFirstElement();
			int start= segment.position.getOffset();
			int length= segment.position.getLength();
			try {
				textEditor.setHighlightRange(start, length, true);
			} catch (IllegalArgumentException x) {
				textEditor.resetHighlightRange();
			}
		}		
	}
	
	public void update(){
		TreeViewer viewer= getTreeViewer();

		if (viewer != null) {
			Control control= viewer.getControl();
			if (control != null && !control.isDisposed()) {
				control.setRedraw(false);
				viewer.setInput(input);
				viewer.expandAll();
				control.setRedraw(true);
			}
		}		
	}
	
}
