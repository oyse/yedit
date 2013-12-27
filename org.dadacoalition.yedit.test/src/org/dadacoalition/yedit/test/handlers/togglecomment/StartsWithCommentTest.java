package org.dadacoalition.yedit.test.handlers.togglecomment;

import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import org.dadacoalition.yedit.handlers.ToggleCommentHandler;
import org.dadacoalition.yedit.test.TestUtils;

@RunWith(Parameterized.class)
public class StartsWithCommentTest {

	private static final String TEST_FILES_DIR = "handlers-test-files/";

	private StartsWithCommentTestCase testCase;

	@Before
	public void setUp() throws Exception {

	}

	public StartsWithCommentTest(StartsWithCommentTestCase testCase) {
		this.testCase = testCase;
	}
	
	@Test
	public void verifyStartsWithComment(){
		
		boolean actual = ToggleCommentHandler.startsWithComment( testCase.getLine() );
		assertEquals( testCase.getName(), testCase.getExpected(), actual );
		
	}

	@Parameters
	public static Collection<Object[]> readTests() {

		String testfile = "starts-with-comment.yaml";

		Collection<Object[]> testCases = new ArrayList<Object[]>();
		ArrayList<StartsWithCommentTestCase> tests = readTests(TEST_FILES_DIR + testfile);
		for (StartsWithCommentTestCase stc : tests) {
			Object[] testcase = { stc };
			testCases.add(testcase);
		}

		return testCases;

	}

	public static ArrayList<StartsWithCommentTestCase> readTests(String filename) {

		// set up how Java types match to the YAML file
		Constructor testCaseConstructor = new Constructor(StartsWithCommentTestCase.class);

		TypeDescription testCaseDesc = new TypeDescription(StartsWithCommentTestCase.class);
		testCaseConstructor.addTypeDescription(testCaseDesc);

		Yaml yamlParser = new Yaml(testCaseConstructor);
		
		// read all the documents in the test file
		String tests = TestUtils.readFile(filename);
		ArrayList<StartsWithCommentTestCase> testCases = new ArrayList<StartsWithCommentTestCase>();
		for (Object testCase : yamlParser.loadAll(new StringReader(tests))) {
			testCases.add((StartsWithCommentTestCase) testCase);
		}

		return testCases;

	}

}
