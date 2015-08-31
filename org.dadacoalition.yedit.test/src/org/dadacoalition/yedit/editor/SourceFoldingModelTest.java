package org.dadacoalition.yedit.editor;

import static org.junit.Assert.assertEquals;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.dadacoalition.yedit.test.TestUtils;
import org.dadacoalition.yedit.test.tasktagparser.TaskTagTestCase;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

@RunWith(Parameterized.class)
public class SourceFoldingModelTest {
	
    private static final String TEST_FILE_DIR = "source-folding-test-files/";
    
    private String inputFilename;
    private String testCaseFilename;
    
    private SourceFoldingModel sourceFoldingModel;
    
    public SourceFoldingModelTest(String inputFilename){    
        this.inputFilename = TEST_FILE_DIR + inputFilename + ".yaml";
        testCaseFilename = TEST_FILE_DIR + inputFilename + "-result.yaml";
        
        sourceFoldingModel = new SourceFoldingModel();
    }
    
    @Test
    public void verifyPositions(){
        String inputContent = TestUtils.readFile(inputFilename);       
        IDocument doc = new Document();
        doc.set(inputContent);
        
        List<Position> actual = sourceFoldingModel.structureToFoldingPositions(doc);        
        SourceFoldingModelTestCase testCase = loadTest(testCaseFilename);

        assertEquals( testCase.getName(), testCase.getExpectedPositions(), actual );
    }
    
    @Parameters
    public static Collection<Object[]> parameters() {
        
        Collection<Object[]> testCases = new ArrayList<Object[]>();
        testCases.add( new Object[]{ "empty-doc" } );
        testCases.add( new Object[]{ "scalar-only" } );
        testCases.add( new Object[]{ "simple-list" } );
        
        return testCases;
    } 
    
    public SourceFoldingModelTestCase loadTest(String filename) {

        // set up how Java types match to the YAML file
        Constructor testCaseConstructor = new Constructor(SourceFoldingModelTestCase.class);

        TypeDescription testCaseDesc = new TypeDescription(
                TaskTagTestCase.class);
        testCaseDesc.putListPropertyType("expectedPositions", Position.class);
        testCaseConstructor.addTypeDescription(testCaseDesc);

        TypeDescription positionDesc = new TypeDescription(Position.class);
        testCaseConstructor.addTypeDescription(positionDesc);

        Yaml yamlParser = new Yaml(testCaseConstructor);

        String tests = TestUtils.readFile(filename);
        SourceFoldingModelTestCase testCase = (SourceFoldingModelTestCase) yamlParser.load(new StringReader(tests));

        return testCase;

    }    
    
}
