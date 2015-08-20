package org.dadacoalition.yedit.test.tasktagparser;

import static org.junit.Assert.assertEquals;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.dadacoalition.yedit.editor.ColorManager;
import org.dadacoalition.yedit.editor.TaskTag;
import org.dadacoalition.yedit.editor.TaskTagParser;
import org.dadacoalition.yedit.editor.TaskTagPreference;
import org.dadacoalition.yedit.editor.scanner.YAMLScanner;
import org.dadacoalition.yedit.test.TestUtils;
import org.eclipse.core.resources.IMarker;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

@RunWith(Parameterized.class)
public class TaskTagParserTest {

    private static final String TEST_FILE_DIR = "todo-tag-parser-test-files/";
    
    private String inputFilename;
    private String testCaseFilename;
    
    private TaskTagParser parser;
    
    public TaskTagParserTest(String inputFilename){    
        this.inputFilename = TEST_FILE_DIR + inputFilename + ".yaml";
        testCaseFilename = TEST_FILE_DIR + inputFilename + "-result.yaml";
        
        List<TaskTagPreference> tagsToFind = new ArrayList<TaskTagPreference>();
        tagsToFind.add(new TaskTagPreference("TODO", "normal"));
        tagsToFind.add(new TaskTagPreference("FIXME", "high"));
        YAMLScanner scanner = new YAMLScanner(new ColorManager());
        parser = new TaskTagParser(tagsToFind, scanner);
    }
    
    @Test
    public void verifyTodoTags(){
        String inputContent = TestUtils.readFile(inputFilename);       
        IDocument doc = new Document();
        doc.set(inputContent);
        
        List<TaskTag> actual = parser.parseTags(doc);        
        TaskTagTestCase testCase = loadTest(testCaseFilename);

        assertEquals( testCase.getName(), testCase.getExpectedTags(), actual );
    }
    
    @Parameters
    public static Collection<Object[]> parameters() {
        
        Collection<Object[]> testCases = new ArrayList<Object[]>();
        testCases.add( new Object[]{ "empty-doc" } );
        testCases.add( new Object[]{ "no-todo" } );
        testCases.add( new Object[]{ "multi-todo" } );
        testCases.add( new Object[]{ "todo-in-scalar" } );

        
        return testCases;
    } 
    
    public TaskTagTestCase loadTest(String filename) {

        // set up how Java types match to the YAML file
        Constructor testCaseConstructor = new Constructor(TaskTagTestCase.class);

        TypeDescription testCaseDesc = new TypeDescription(
                TaskTagTestCase.class);
        testCaseDesc.putListPropertyType("expectedTags", TaskTag.class);
        testCaseConstructor.addTypeDescription(testCaseDesc);

        TypeDescription tokenDesc = new TypeDescription(TaskTag.class);
        testCaseConstructor.addTypeDescription(tokenDesc);

        Yaml yamlParser = new Yaml(testCaseConstructor);

        String tests = TestUtils.readFile(filename);
        TaskTagTestCase testCase = (TaskTagTestCase) yamlParser.load(new StringReader(tests));

        return testCase;

    }    
    
}
