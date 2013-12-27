package org.dadacoalition.yedit.editor;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

/**
 * Scanner rule to match the start and end of the document.
 */
public class DocumentStartAndEndRule implements IRule {

	private IToken token;
	
	private String matchString;
	
	/**
	 * @param matchString The string to match. Should either be '---' or '...'
	 * @param token The token that should be returned for a match.
	 */
	public DocumentStartAndEndRule(String matchString, IToken token){
		this.token = token;
		this.matchString = matchString;
	}
	
	@Override
	public IToken evaluate(ICharacterScanner scanner) {

        char c = (char) scanner.read();
        int count = 1;

        String chars = "" + c;
        while( c != ICharacterScanner.EOF && count < 3 ){                    

        	c = (char) scanner.read();
        	chars += c;        	
            count++;            
        }
        
        if( matchString.equals(chars)){
        	return token;
        } else {
        	//no match so need to rewind scanner.
        	for( int i = 0; i < count; i++ ){
        		scanner.unread();
        	}
        	return Token.UNDEFINED;
        }
	}

}
