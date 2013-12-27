package org.dadacoalition.yedit.test.symfony;

import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;

import org.dadacoalition.yedit.editor.ColorManager;
import org.dadacoalition.yedit.editor.SymfonyCompatibilityMode;
import org.dadacoalition.yedit.editor.scanner.YAMLScanner;
import org.dadacoalition.yedit.test.TestUtils;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.yaml.snakeyaml.Loader;
import org.yaml.snakeyaml.Yaml;

@RunWith(Parameterized.class)
public class SymfonyCompatibilityModeTest {

    private static final String TEST_FILES_DIR = "symfony-compatibility-test-files/";
    
    private SymfonyTestCase testCase;
    
    private IDocument document;
    private YAMLScanner scanner;
    private ColorManager colorManager = new ColorManager();
    private SymfonyCompatibilityMode sr;
    

    @Before
    public void setUp() throws Exception {
        this.document = new Document();
        this.scanner = new YAMLScanner(colorManager);
        this.sr = new SymfonyCompatibilityMode( this.scanner );
    }

    public SymfonyCompatibilityModeTest(SymfonyTestCase testCase) {        
        this.testCase = testCase;
    }
    
    @Parameters
    public static Collection<Object[]> readTests() {

        String[] testfiles = { 
                "replace-tests.yaml",
        };

        Collection<Object[]> testCases = new ArrayList<Object[]>();
        for (String testfile : testfiles) {
            ArrayList<SymfonyTestCase> tests = readTests(TEST_FILES_DIR
                    + testfile);
            for (SymfonyTestCase stc : tests) {
                Object[] testcase = { stc };
                testCases.add(testcase);
            }

        }

        return testCases;

    }

    public static ArrayList<SymfonyTestCase> readTests(String filename) {

        Loader loader = new Loader();
        Yaml yamlParser = new Yaml(loader);       
        
        String tests = TestUtils.readFile(filename);
        ArrayList<SymfonyTestCase> testCases = new ArrayList<SymfonyTestCase>();
        for (Object testCase : yamlParser.loadAll(new StringReader(tests))) {
            testCases.add((SymfonyTestCase) testCase);
        }
        
        return testCases;

    }
    
    @Test
    public void verifyReplace(){
        
        String originalContent = testCase.getContent();
        document.set(originalContent);
        String replacedContent = sr.fixScalars(document);
        
        assertEquals( testCase.getName(), testCase.getExpectedContent(), replacedContent );
        
    }
    
    
}
