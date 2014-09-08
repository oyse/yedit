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
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.DumperOptions.ScalarStyle;
import org.yaml.snakeyaml.Yaml;

@RunWith(Parameterized.class)
public class YamlFormatterTest {

    private static final String TEST_FILE_DIR = "formatter-test-files/";
    
    private String inputFilename;
    private String outputFilename;
    
    private YamlFormatter formatter;
    
    public YamlFormatterTest(String inputFilename, YamlFormatter formatter){    
        this.inputFilename = TEST_FILE_DIR + inputFilename + ".yaml";
        outputFilename = TEST_FILE_DIR + inputFilename + "-result.yaml";
        this.formatter = formatter;
    }
    
    @Test
    public void compareFiles(){
        String inputContent = TestUtils.readFile(inputFilename);
        String outputContent = TestUtils.readFile(outputFilename);
        Yaml yaml = new Yaml();
        Iterable<Object> doc = yaml.loadAll(inputContent);
        String actual = formatter.formatDocuments(doc);
        assertEquals( "Formatting " + inputFilename, outputContent, actual );
    }
    
    @Parameters
    public static Collection<Object[]> parameters() {
        
        Collection<Object[]> testCases = new ArrayList<Object[]>();
        testCases.add( new Object[]{ "empty-doc", new YamlFormatter.Builder().explicitStart(true).explicitEnd(true).build() } );
        testCases.add( new Object[]{ "keys-with-extra-space", new YamlFormatter.Builder().explicitStart(true).explicitEnd(true).build()} );
        testCases.add( new Object[]{ "uneven-indent", new YamlFormatter.Builder().explicitStart(true).explicitEnd(true).build() } );
        testCases.add( new Object[]{ "flow-to-block", new YamlFormatter.Builder().explicitStart(true).explicitEnd(true).build() } );
        testCases.add( new Object[]{ "pretty-flow", new YamlFormatter.Builder().explicitStart(false).explicitEnd(false).flowStyle(FlowStyle.FLOW).prettyFlow(true).build() } );
        testCases.add( new Object[]{ "block-to-flow", new YamlFormatter.Builder().explicitStart(false).explicitEnd(false).flowStyle(FlowStyle.FLOW).prettyFlow(false).build() } );
        testCases.add( new Object[]{ "to-plain-scalar", new YamlFormatter.Builder().explicitStart(false).explicitEnd(false).build() } );
        testCases.add( new Object[]{ "to-single-quoted-scalar", new YamlFormatter.Builder().explicitStart(false).explicitEnd(false).scalarStyle(ScalarStyle.SINGLE_QUOTED).build() } );
        testCases.add( new Object[]{ "to-double-quoted-scalar", new YamlFormatter.Builder().explicitStart(false).explicitEnd(false).scalarStyle(ScalarStyle.DOUBLE_QUOTED).build() } );        
        testCases.add( new Object[]{ "multi-doc", new YamlFormatter.Builder().explicitStart(true).explicitEnd(true).scalarStyle(ScalarStyle.DOUBLE_QUOTED).build() } );

        
        return testCases;
    }
}
