package org.dadacoalition.yedit.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import org.apache.commons.io.IOUtils;

public class TestUtils {

	public static String readFile( String filename ) {
		
	    try {
            FileInputStream is = new FileInputStream(filename);
            return IOUtils.toString(is);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
	}	
	
}
