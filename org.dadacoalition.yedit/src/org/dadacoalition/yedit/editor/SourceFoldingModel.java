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
	
	private enum NodeType {
		SCALAR, SEQUENCE, MAPPING
	}
	
	private class SourceFoldingNode {
		protected int startLine;
		protected int endLine;
		protected NodeType nodeType;
		
		protected List<SourceFoldingNode> children = new ArrayList<>();
		
		protected SourceFoldingNode(int startLine,int endLine, NodeType nodeType){
			this.startLine = startLine;
			this.endLine = endLine;
			this.nodeType = nodeType;
		}
		
	}
	
	protected SourceFoldingModel(){
		seenNodes = new HashSet<>();
	}
	
	public List<Position> structureToFoldingPositions(IDocument document){
		
		seenNodes.clear();
		
		String content = document.get();
		List<SourceFoldingNode> foldingNodes = new ArrayList<>();
		for( Node rootNode : yamlParser.composeAll( new StringReader( content ) ) ){
			foldingNodes.add(yamlToFoldingNode(rootNode, document));				
		}		
		
		int highestAllowedEndLine = document.getNumberOfLines() - 1;
		for(int i = foldingNodes.size(); i > 0; i-- ){
			SourceFoldingNode node = foldingNodes.get(i - 1);
			highestAllowedEndLine = fixEndLineErrors(node, highestAllowedEndLine);
		}
		
		List<Position> positions = new ArrayList<>();
		try {
			for(int i = foldingNodes.size(); i > 0; i-- ){
				SourceFoldingNode node = foldingNodes.get(i - 1);
				positions.addAll(foldingNodeToPosition(node, document));
			}
		} catch (BadLocationException ex){
			YEditLog.logException(ex , "Failed to translate from document model to positions");
		}

		return positions;
	}
	
	private List<Position> foldingNodeToPosition(SourceFoldingNode node, IDocument document) throws BadLocationException {
		
		List<Position> positions = new ArrayList<>();
		if(node.startLine == node.endLine || node.nodeType == NodeType.SCALAR){
			return positions;
		}
		
		positions.add(getPosition(node, document));
		for(SourceFoldingNode child : node.children){
			positions.addAll(foldingNodeToPosition(child, document));
		}
		
		
		return positions;
		
	}
	
	/**
	 * The Yaml parser some times reports the wrong end line for a node so we need to fix that.
	 * 
	 * We cannot expect that this will be fixed in the Yaml parser since we are really abusing internal
	 * structures to get this to work.
	 * @param node The node to process
	 * @param highestAllowedEndLine The highest number that is allowed for the end line.
	 * 
	 * @return The new highest allowed end line for subsequent calls
	 */
	private int fixEndLineErrors(SourceFoldingNode node, int highestAllowedEndLine){
		
		int updatedHighest = highestAllowedEndLine;
		
		// traverse in reverse order of appearance in the document
		for(int i = node.children.size(); i > 0; i-- ){
			SourceFoldingNode child = node.children.get(i - 1);
			updatedHighest = fixEndLineErrors(child, updatedHighest);
		}
		
		if(node.endLine > highestAllowedEndLine){
			node.endLine = highestAllowedEndLine;
		}
		
		return node.startLine - 1;
		
	}

	private SourceFoldingNode yamlToFoldingNode(Node node, IDocument document){
		
		
		seenNodes.add(System.identityHashCode(node));
		
		if( node instanceof ScalarNode ){
			return new SourceFoldingNode(node.getStartMark().getLine(), node.getEndMark().getLine(), NodeType.SCALAR);
			//scalar nodes require no further action
		} else if( node instanceof SequenceNode ){
			
			SourceFoldingNode parent = new SourceFoldingNode(node.getStartMark().getLine(), node.getEndMark().getLine(), NodeType.SEQUENCE);
			
			SequenceNode sNode = (SequenceNode) node;
			
			List<Node> children = sNode.getValue();
			for( Node childNode : children ){
				
				// prevent infinite loops in case of recursive references
				if( this.seenNodes.contains(System.identityHashCode(childNode))){
					continue;
				}
				
				parent.children.add(yamlToFoldingNode(childNode, document));
			}
			return parent;
		} else if ( node instanceof MappingNode ){

			SourceFoldingNode parent = new SourceFoldingNode(node.getStartMark().getLine(), node.getEndMark().getLine(), NodeType.MAPPING);
			
			MappingNode mNode = (MappingNode) node;
			List<NodeTuple> children = mNode.getValue();
			for( NodeTuple childNode : children ){
				
				// prevent infinite loops in case of recursive references
				if( this.seenNodes.contains(System.identityHashCode(childNode.getValueNode()))){
					continue;
				}					
				
				parent.children.add(yamlToFoldingNode(childNode.getValueNode(), document));
			}
			
			return parent;
		}					
		
		throw new IllegalArgumentException("Invalid node type: " + node.getType());
	}
	
	// translate from the model to document positions
	private Position getPosition( SourceFoldingNode node, IDocument document ) throws BadLocationException {
		
		int startOffset = document.getLineOffset(node.startLine);
		int endOffset = document.getLineOffset(node.endLine);
		int endLineLength = document.getLineLength(node.endLine);
	
		int length = endOffset - startOffset + endLineLength;
		return new Position( startOffset, length ); 
	}
		
}
