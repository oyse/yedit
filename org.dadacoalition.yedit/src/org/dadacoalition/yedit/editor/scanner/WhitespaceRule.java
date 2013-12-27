package org.dadacoalition.yedit.editor.scanner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

/**
 * Scanner rule for matching whitespace characters. The rule will match consecutive
 * whitespaces as a single whitespace token.
 * @author oysteto
 *
 */

public class WhitespaceRule implements IRule {

    private IToken token;
    
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s");
    
    public WhitespaceRule( IToken token ){
        this.token = token;
    }
    
    
    public IToken evaluate(ICharacterScanner scanner) {
     
        int c = scanner.read();
        boolean atLeastOneMatch = false;

        
        while( c != ICharacterScanner.EOF  ){                    
            String character = "" + (char) c;     

            Matcher m = WHITESPACE_PATTERN.matcher(character);
            if( !m.matches()){
                scanner.unread();                
                break;
            } else {
                atLeastOneMatch = true;
            }
            c = scanner.read();            
        }  

        //when at least one whitespace was found it means we have a whitespace
        //token
        if( atLeastOneMatch ){
            return token;
        } else {
            return Token.UNDEFINED;
        }
    }

}
