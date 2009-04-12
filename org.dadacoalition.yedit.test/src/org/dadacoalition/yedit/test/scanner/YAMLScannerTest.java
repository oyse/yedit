package org.dadacoalition.yedit.test.scanner;


import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.yaml.snakeyaml.Loader;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.constructor.Constructor;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

import org.dadacoalition.yedit.editor.*;
import org.dadacoalition.yedit.test.TestUtils;


public class YAMLScannerTest {

	private static final String TEST_FILES_DIR = "scanner-test-files/";
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
		
		String contentFile = "simple-sequence.yaml";
		String content = TestUtils.readFile( TEST_FILES_DIR + contentFile );	
		document.set(content);
		
		ArrayList<ScannerTestCase> tests = readTests( TEST_FILES_DIR + "simple-sequence-tests.yaml" );
		
		for( ScannerTestCase test : tests ){

			//configure the scanner for this test case
			int rangeLength = test.getRangeLength();
			if( -1 == rangeLength ){
				rangeLength = document.getLength();
			}							
			scanner.setRange(document, test.getStartOffset(), rangeLength );
			
			//do the actual scanning. It is crucial to update the range of the scanner
			//after each read token to prevent and infinite loop
			ArrayList<IToken> scannedTokens = new ArrayList<IToken>();
			IToken token = scanner.nextToken();
			while( token != Token.EOF ){
				scannedTokens.add(token);
				
				int newOffset = scanner.getTokenOffset() + scanner.getTokenLength();
				rangeLength = rangeLength - scanner.getTokenLength();
				scanner.setRange(document, newOffset, rangeLength);			
				
				token = scanner.nextToken();
			}			
					
			compareTokens( test.getName(), test.getYAMLTokens(), scannedTokens );
			
		}
		
	}
	
	
	@After
	public void tearDown() throws Exception {
	}

	public void compareTokens( String testname, List<YAMLToken> expectedTokens, List<IToken> receivedTokens ){
		
		org.junit.Assert.assertEquals(testname + " Number of tokens.", expectedTokens.size(), receivedTokens.size() );
		
		for( int i = 0; i < expectedTokens.size(); i++ ){
			org.junit.Assert.assertEquals(testname + " Token number: " + ( i + 1 ), expectedTokens.get(i), receivedTokens.get(i) );
		}
		
	}
	
	public ArrayList<ScannerTestCase> readTests( String filename ){
				
		//set up how Java types match to the YAML file
		Constructor testCaseConstructor = new Constructor(ScannerTestCase.class);
		
		TypeDescription testCaseDesc = new TypeDescription(ScannerTestCase.class);
		testCaseDesc.putListPropertyType("expectedTokens", ScannerToken.class);
		testCaseConstructor.addTypeDescription(testCaseDesc);
		
		TypeDescription tokenDesc = new TypeDescription(ScannerToken.class);
		testCaseConstructor.addTypeDescription(tokenDesc);		
		
		Loader yamlParser = new Loader( testCaseConstructor );		
		
		//read all the documents in the test file
		String tests = TestUtils.readFile( filename );				
		ArrayList<ScannerTestCase> testCases = new ArrayList<ScannerTestCase>(); 
		for( Object testCase : yamlParser.loadAll( new StringReader( tests ) ) ){
			testCases.add( (ScannerTestCase) testCase );
		}
		
		return testCases;
		
	}
		
}
