package org.dadacoalition.yedit.editor.scanner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.text.rules.IWordDetector;

/**
 * Word detector used in the scanner rule for tags. 
 */
public class TagWordDetector implements IWordDetector {

    private Pattern characterPattern = Pattern.compile( "[\\w \\d ! < > \\. , :]", Pattern.COMMENTS );

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
