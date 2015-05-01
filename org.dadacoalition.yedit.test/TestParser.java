/*******************************************************************************
 * Copyright (c) 2015 Øystein Idema Torget 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Øystein Idema Torget
 *******************************************************************************/
import java.io.File;
import java.io.FileNotFoundException;
import java.io.StringReader;
import java.util.List;
import java.util.Scanner;

import org.yaml.snakeyaml.*;
import org.yaml.snakeyaml.nodes.*;


public class TestParser {


	
	public static void main( String[] args ) throws FileNotFoundException {
		
		Loader yamlParser = new Loader();	
		String content = readFile( "examples/test2.yaml" );
		
		for( Node document : yamlParser.loadAll2( new StringReader(content) ) ){
			dumpNode( document, 0 );
		}
		
		
	}
	
	public static void dumpNode( Node node, int depth ){
		
		int startLine = node.getStartMark().getLine();
		int startColumn = node.getStartMark().getColumn();
		int endLine = node.getEndMark().getLine();
		int endColumn = node.getEndMark().getColumn();
		
//		for( int i = 0; i < depth; i++ ){
//			System.out.print( " " );
//		}
		
		if( node instanceof ScalarNode ){
//			System.out.print( node.getValue() );
		} else if ( node instanceof MappingNode ){
			MappingNode mNode = (MappingNode) node;
			List<Node[]> value = mNode.getValue();
			for( Node[] child : value ){
				dumpNode( child[0], depth + 1);
				dumpNode( child[1], depth + 1);
			}
			
		} else if( node instanceof SequenceNode ){
			SequenceNode sNode = (SequenceNode) node;
	        List<Node> value = sNode.getValue();
	        for (Node child : value) {
	            dumpNode( child, depth + 1);
	        }
		} else {
//			System.out.print( "Else: " + node.getValue() );
		}
		
//		System.out.println( "(" + startLine + ", " + startColumn + ", " + endLine + ", " + endColumn + ")" );
		System.out.println( node );
	}
	
	
	public static String readFile( String filename ) throws FileNotFoundException {
		
		Scanner scanner = new Scanner( new File(filename) );
		String content = "";
		while( scanner.hasNextLine() ){
			content += scanner.nextLine() + "\n";
		}
		
		return content;		
	}
	
}
