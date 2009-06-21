package org.dadacoalition.yedit.editor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

/**
 * Scanner rule for matching keys in a mapping.
 * @author oysteto
 *
 */
public class KeyRule implements IRule {

    private IToken token;
    private Pattern keyPattern;
    
    private static String keyRegex = "(\\w[\\w\\s]*:\\s).*";
    
    public KeyRule( IToken token ){
        this.token = token;
        keyPattern = Pattern.compile( keyRegex, Pattern.DOTALL );
    }
        
    public IToken evaluate(ICharacterScanner scanner) {

        String stream = "";

        int c = scanner.read();
        int count = 1;

        while( c != ICharacterScanner.EOF  ){                    
            stream += (char) c;     
            
            //a key cannot span more than one line
            if( '\n' == c || '\r' == c ){
                break;
            }            
            c = scanner.read();
            count++;            
        }      
        
        Matcher m = keyPattern.matcher( stream );
        if( m.matches() ){
            String matchedText = m.group(1);
            
            //put the scanner back to after the matched text.
            count = count - matchedText.length();
            for( int i = 0; i < count; i++){
                scanner.unread();
            }
            return token;
        } else {
            //put the scanner back to the original position if no match
            for( int i = 0; i < count; i++){
                scanner.unread();
            }
            return Token.UNDEFINED;
        }
    }

}
