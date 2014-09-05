package org.dadacoalition.yedit.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

public class TestUtils {

	public static String readFile( String filename ) {
		
	    try {
            return IOUtils.toString(new FileInputStream(new File(filename)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
	}	
	
}
