package org.dadacoalition.yedit.test.formatter;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;

import org.dadacoalition.yedit.formatter.YamlFormatter;
import org.dadacoalition.yedit.test.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.yaml.snakeyaml.Yaml;

@RunWith(Parameterized.class)
public class YamlFormatterTest {

    private String inputFilename;
    private String outputFilename;
    
    private YamlFormatter formatter;
    
    public YamlFormatterTest(String inputFilename){    
        this.inputFilename = inputFilename + ".yaml";
        outputFilename = inputFilename + "-result.yaml";
        formatter = new YamlFormatter.Builder().explicitStart(true).explicitEnd(true).build();
    }
    
    @Test
    public void compareFiles(){
        String inputContent = TestUtils.readFile(inputFilename);
        String outputContent = TestUtils.readFile(outputFilename);
        Yaml yaml = new Yaml();
        Object doc = yaml.load(inputContent);
        String actual = formatter.formatDocument(doc);
        System.out.println("\n" + actual);
        assertEquals( "Formatting " + inputFilename, outputContent, actual );
    }
    
    @Parameters
    public static Collection<Object[]> parameters() {
        
        Collection<Object[]> testCases = new ArrayList<Object[]>();
        testCases.add( new String[]{ "formatter-test-files/empty-doc" } );
        testCases.add( new String[]{ "formatter-test-files/keys-with-extra-space" } );
        testCases.add( new String[]{ "formatter-test-files/uneven-indent" } );
        
        
        return testCases;
    }
}
