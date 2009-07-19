package org.dadacoalition.yedit.editor;

import java.util.regex.*;

import org.eclipse.jface.text.rules.IWordDetector;

/**
 * Word detector used by the scanner rule for anchors and in the scanner rule
 * for aliases. 
 */
public class AnchorWordDetector implements IWordDetector {

	private Pattern characterPattern = Pattern.compile( "[\\w\\d]" );

	public boolean isWordPart(char c) {
		
		Matcher m = characterPattern.matcher( new Character(c).toString() );
		if( m.matches() ){
			return true;
		}
		
		return false;
	}

	public boolean isWordStart(char c) {

		Matcher m = characterPattern.matcher( new Character(c).toString() );
		if( m.matches() ){
			return true;
		}
		
		return false;

	}

}
