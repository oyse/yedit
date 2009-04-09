package org.dadacoalition.yedit.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;

public class YAMLOutlineElement {
		
		public static final int SCALAR = 1;
		public static final int MAPPINGITEM = 2;
		public static final int SEQUENCEITEM = 3;
		public static final int DOCUMENT = 4;
		public static final int MAPPINGSCALAR = 5;
	
		protected Node node;
		protected String key;
		protected String value;
		protected YAMLOutlineElement parent;
		protected List<YAMLOutlineElement> children = new ArrayList<YAMLOutlineElement>();
		protected Position position;
		protected IDocument document;
		protected int type;
		
		protected YAMLOutlineElement( Node node, IDocument document ){
			this( node, null, DOCUMENT, document );		
		}		
		
		protected YAMLOutlineElement( Node node, YAMLOutlineElement parent, int type, IDocument document ){
			this.node = node;
			this.parent = parent;
			this.type = type;
			this.document = document;
			
			parseData( node );
			position = getPosition( node );
			try {
				document.addPosition(YAMLContentOutlinePage.YAMLSEGMENT, position);
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
					YAMLOutlineElement child = new YAMLOutlineElement( childNode, this, SEQUENCEITEM, document );
					this.children.add(child);					
				}
			} else if ( node instanceof MappingNode ){
				MappingNode mNode = (MappingNode) node;
				List<Node[]> children = mNode.getValue();
				for( Node[] childNode : children ){
					String key = childNode[0].getValue().toString();
					YAMLOutlineElement child = new YAMLOutlineElement( childNode[1], this, MAPPINGITEM, document );
					child.key = key;
					this.children.add(child);
				}
			}		
			
		}
		
		public String toString(){
			if( type == YAMLOutlineElement.DOCUMENT ){
				return "";
			} else if( type == YAMLOutlineElement.SCALAR ){
				return node.getValue().toString();
			} else if( type == YAMLOutlineElement.MAPPINGITEM ){
				return key;
			} else if( type == YAMLOutlineElement.SEQUENCEITEM ){
				return "";
			} else if( type == YAMLOutlineElement.MAPPINGSCALAR ){
				return key + ": " + node.getValue().toString();
			}
			
			return super.toString();
			
		}			
	
}
