package org.dadacoalition.yedit.test;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

import org.dadacoalition.yedit.editor.*;

public class YAMLScannerTest {

	private String testFilesDir = "scanner-test-files/";
	private IDocument document;
	private YAMLScanner scanner;
	private ColorManager colorManager = new ColorManager();
	
	@Before
	public void setUp() throws Exception {
		
		this.document = new Document();
		this.scanner = new YAMLScanner( colorManager );
		
		
	}

	@Test
	public void simpleSequence(){
		
		String filename = "simple-sequence.yaml";
		String content = readFile( filename );
		
		document.set(content);
		int rangeLength = document.getLength();
		scanner.setRange(document, 0, rangeLength );

		
		Object dummy = new Object();
		Object[] expectedTokens = {
			new YAMLToken( dummy, YAMLToken.DOCUMENT ),
			new YAMLToken( dummy, YAMLToken.SCALAR ),
			new YAMLToken( dummy, YAMLToken.SCALAR ),
			new YAMLToken( dummy, YAMLToken.SCALAR ),
			new YAMLToken( dummy, YAMLToken.COMMENT ),				
		};

		LinkedList<IToken> scannedTokens = new LinkedList<IToken>();
		IToken token = scanner.nextToken();
		while( token != Token.EOF ){
			scannedTokens.add(token);
			
			int newOffset = scanner.getTokenOffset() + scanner.getTokenLength();
			rangeLength = rangeLength - scanner.getTokenLength();
			scanner.setRange(document, newOffset, rangeLength);			
			
			token = scanner.nextToken();
		}

		PrintWriter pw;
		try {
			pw = new PrintWriter(new FileWriter("testoutput.txt"));
			for( Object o : scannedTokens.toArray() ){
				pw.append(o.toString() + "\n" );
			}
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
		org.junit.Assert.assertArrayEquals( "Scanner test simple sequence", expectedTokens, scannedTokens.toArray() );



	}
	
	
	@After
	public void tearDown() throws Exception {
	}

	
	public String readFile( String filename ) {
		
		String path = testFilesDir + filename;
	
		Scanner scanner;
		try {
			scanner = new Scanner( new File(path) );
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "";
		}
		String content = "";
		while( scanner.hasNextLine() ){
			content += scanner.nextLine() + "\n";
		}
		
		return content;		
	}
}
