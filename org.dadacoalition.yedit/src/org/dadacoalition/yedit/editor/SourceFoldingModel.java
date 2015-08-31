package org.dadacoalition.yedit.editor;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import org.dadacoalition.yedit.YEditLog;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;

public class SourceFoldingModel {

	private Yaml yamlParser = new Yaml();
	
	private Set<Integer> seenNodes;
	
	protected SourceFoldingModel(){
		seenNodes = new HashSet<>();
	}
	
	public List<Position> structureToFoldingPositions(IDocument document){
		
		seenNodes.clear();
		
		String content = document.get();
		List<Position> positions = new ArrayList<>();
		
		for( Node rootNode : yamlParser.composeAll( new StringReader( content ) ) ){
			positions.addAll(nodeToPositions(rootNode, document));
		}		

		return positions;
	}
	
	private List<Position> nodeToPositions(Node node, IDocument document){
		
		List<Position> positions = new ArrayList<>();
		
		seenNodes.add(System.identityHashCode(node));
		
		if( node instanceof ScalarNode ){
			//scalar nodes require no further action
		} else if( node instanceof SequenceNode ){
			
			positions.add(getPosition(node, document));
			
			
			SequenceNode sNode = (SequenceNode) node;
			List<Node> children = sNode.getValue();
			for( Node childNode : children ){
				
				// prevent infinite loops in case of recursive references
				if( this.seenNodes.contains(System.identityHashCode(childNode))){
					continue;
				}
				
				positions.addAll(nodeToPositions(childNode, document));
			}
		} else if ( node instanceof MappingNode ){

			positions.add(getPosition(node, document));			
			
			MappingNode mNode = (MappingNode) node;
			List<NodeTuple> children = mNode.getValue();
			for( NodeTuple childNode : children ){
				
				// prevent infinite loops in case of recursive references
				if( this.seenNodes.contains(System.identityHashCode(childNode.getValueNode()))){
					continue;
				}					
				
				positions.addAll(nodeToPositions(childNode.getValueNode(), document));
			}
		}					
		
		
		return positions;
	}
	
	/**
	 * Get the position of the node within the document containing the YAML text.
	 * This position is necessary to associate clicking on the element in the
	 * outline view with moving the caret to the right position in the document.
	 * @param node The SnakeYAML node of the current element.
	 * @return
	 */
	private Position getPosition( Node node, IDocument document ){
		
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
		} catch( BadLocationException e) {
			YEditLog.logger.warning( e.toString() );
		}
		return p; 			
		
	}
		
}
