/*******************************************************************************
 * Copyright (c) 2015 Øystein Idema Torget and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Øystein Idema Torget and others
 *******************************************************************************/
package org.dadacoalition.yedit.test.scanner;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.dadacoalition.yedit.editor.*;
import org.dadacoalition.yedit.editor.scanner.YAMLScanner;
import org.dadacoalition.yedit.editor.scanner.YAMLToken;
import org.dadacoalition.yedit.test.TestUtils;

@RunWith(Parameterized.class)
public class YAMLScannerTest {

	private static final String TEST_FILES_DIR = "scanner-test-files/";
	private IDocument document;
	private YAMLScanner scanner;
	private ColorManager colorManager = new ColorManager();

	private ScannerTestCase testCase;

	@Before
	public void setUp() throws Exception {

		this.document = new Document();
		this.scanner = new YAMLScanner(colorManager);

	}

	public YAMLScannerTest(ScannerTestCase testCase) {
		this.testCase = testCase;
	}

	@Parameters
	public static Collection<Object[]> readTests() {

		String[] testfiles = {
				"simple-sequence-tests.yaml",
				"document-tests.yaml",
				"anchor-alias-tests.yaml",
				"constant-tests.yaml",
				"comment-tests.yaml",
				"tag-tests.yaml",
				"scalar-test.yaml",
				"key-tests.yaml"
		};

		Collection<Object[]> testCases = new ArrayList<Object[]>();
		for (String testfile : testfiles) {
			ArrayList<ScannerTestCase> tests = readTests(TEST_FILES_DIR
					+ testfile);
			for (ScannerTestCase stc : tests) {
				Object[] testcase = { stc };
				testCases.add(testcase);
			}

		}

		return testCases;

	}

	@Test
	public void verifyTokens() {

		String contentFile = testCase.getContentFile();
		String content = TestUtils.readFile(TEST_FILES_DIR + contentFile);
		document.set(content);

		// configure the scanner for this testCase case
		int rangeLength = testCase.getRangeLength();
		if (-1 == rangeLength) {
			rangeLength = document.getLength() - testCase.getStartOffset();
		}

		scanner.setRange(document, testCase.getStartOffset(), rangeLength);

		// do the actual scanning. It is crucial to update the range of the
		// scanner
		// after each read token to prevent and infinite loop
		ArrayList<IToken> scannedTokens = new ArrayList<IToken>();
		IToken token = scanner.nextToken();
		while (token != Token.EOF) {
			scannedTokens.add(token);

			int newOffset = scanner.getTokenOffset() + scanner.getTokenLength();
			rangeLength = rangeLength - scanner.getTokenLength();
			scanner.setRange(document, newOffset, rangeLength);

			token = scanner.nextToken();
		}

		compareTokens(testCase.getName(), testCase.getYAMLTokens(),
				scannedTokens);

	}

	@After
	public void tearDown() throws Exception {
	}

	public void compareTokens(String testname, List<YAMLToken> expectedTokens,
			List<IToken> receivedTokens) {
	    //printTokens( receivedTokens );
		//org.junit.Assert.assertEquals(testname + " Number of tokens.",
		//		expectedTokens.size(), receivedTokens.size());

		for (int i = 0; i < expectedTokens.size(); i++) {
			org.junit.Assert.assertEquals(testname + " Token number: "
					+ (i + 1), expectedTokens.get(i), receivedTokens.get(i));
		}

	}

	public static ArrayList<ScannerTestCase> readTests(String filename) {

		// set up how Java types match to the YAML file
		Constructor testCaseConstructor = new Constructor(ScannerTestCase.class);

		TypeDescription testCaseDesc = new TypeDescription(
				ScannerTestCase.class);
		testCaseDesc.putListPropertyType("expectedTokens", ScannerToken.class);
		testCaseConstructor.addTypeDescription(testCaseDesc);

		TypeDescription tokenDesc = new TypeDescription(ScannerToken.class);
		testCaseConstructor.addTypeDescription(tokenDesc);

		Yaml yamlParser = new Yaml(testCaseConstructor);

		// read all the documents in the test file
		String tests = TestUtils.readFile(filename);
		ArrayList<ScannerTestCase> testCases = new ArrayList<ScannerTestCase>();
		for (Object testCase : yamlParser.loadAll(new StringReader(tests))) {
			testCases.add((ScannerTestCase) testCase);
		}

		return testCases;

	}

	private void printTokens( List<IToken> tokens ) {

	    for( IToken token : tokens ){
	        System.out.println( token );
	    }


	}

}
