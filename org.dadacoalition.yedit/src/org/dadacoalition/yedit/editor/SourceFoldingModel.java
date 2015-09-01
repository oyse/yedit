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

		if(!relevantPosition(node)){
			return positions;
		}
		
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
	
	private boolean relevantPosition(Node node){
		int startLine = node.getStartMark().getLine();
		int endLine = node.getEndMark().getLine();
		
		return startLine < endLine;
	}
	
	/**
	 * Get the position of the node within the document. Since we are using the position for
	 * the source folding we measure positions from the start of the line where the
	 * node begins to the end of the line where the node stops.
	 * @param node The SnakeYAML node of the current element.
	 * @return
	 */
	private Position getPosition( Node node, IDocument document ){
		
		int startLine = node.getStartMark().getLine();
		int endLine = node.getEndMark().getLine();
		Position p = null;
		try {
			int startOffset = document.getLineOffset(startLine);
			int endOffset = document.getLineOffset(endLine);
			int endLineLength = document.getLineLength(endLine);
		
			int length = endOffset - startOffset + endLineLength;
			p = new Position( startOffset, length ); 
		} catch( BadLocationException e) {
			YEditLog.logger.warning( e.toString() );
		}
		return p; 			
		
	}
		
}
