package org.dadacoalition.yedit.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TestUtils {

	public static String readFile( String filename ) {
		
		Scanner scanner;
		try {
			scanner = new Scanner( new File(filename) );
		} catch (FileNotFoundException e) {		
			throw new RuntimeException(e);
		}
		
		String content = "";
		while( scanner.hasNextLine() ){
			content += scanner.nextLine() + "\n";
		}
		
		return content;		
	}	
	
}
