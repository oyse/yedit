package org.dadacoalition.yedit.editor.scanner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

/**
 * Scanner rule for matching keys in a mapping.
 */
public class KeyRule implements IRule {

    private IToken token;
    private Pattern keyPattern;
    
    public KeyRule( IToken token ){
        this.token = token;
        keyPattern = Pattern.compile( getKeyRegex(), Pattern.DOTALL | Pattern.COMMENTS );
    }
        
    public IToken evaluate(ICharacterScanner scanner) {

        String stream = "";

        int c = scanner.read();
        int count = 1;

        boolean foundColon = false;
        while( c != ICharacterScanner.EOF  ){                    
            stream += (char) c;     
            
            //a key cannot span more than one line
            if( '\n' == c || '\r' == c ){
                break;
            }
            
            // we found a ':', so do not need to read any longer
            if( ':' == c ){
            	foundColon = true;
            	stream += (char) scanner.read();
            	count++;
            	break;
            }
            
            c = scanner.read();
            count++;            
        }
        
        // no colon so guaranteed that is not a key
        if(!foundColon){
        	rewindScanner(scanner, count);
        	return Token.UNDEFINED;
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
            rewindScanner(scanner, count);
            return Token.UNDEFINED;
        }
    }
    
    //rewind the scanner back when no match is found
    private void rewindScanner(ICharacterScanner scanner, int stepsBack){
        for( int i = 0; i < stepsBack; i++){
            scanner.unread();
        }    	
    }
    
    protected String getKeyRegex(){
        return "([\\w \\- _ +] [\\w \\s \\. \\\\ \\- _ +]*:)\\s.*";
    }

}
