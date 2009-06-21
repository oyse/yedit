package org.dadacoalition.yedit.editor;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

/**
 * Scanner rule for matching one of the indicator characters.
 * @author oysteto
 *
 */
public class IndicatorCharacterRule implements IRule {

    private IToken token;
    private char[] indicatorCharacters = { '{', '}', '[', ']', ',', '-' };
    
    public IndicatorCharacterRule( IToken token ){
        this.token = token;
    }
    

    public IToken evaluate(ICharacterScanner scanner) {
       
        int c = scanner.read();

        for( char indicatorCharacter : indicatorCharacters ){
            if( indicatorCharacter == (char) c ){
                return token;
            }
        }
        
        scanner.unread(); //no match so must rewind the scanner
        return Token.UNDEFINED;

    }

}
