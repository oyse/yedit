package org.dadacoalition.yedit.editor;

import org.dadacoalition.yedit.YEditLog;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

/**
 * Implementation of the Symfony compatibility mode.
 * 
 * The configuration files for the PHP web-framework Symfony uses invalid YAML
 * causing problems for the SnakeYAML parser. The problem lies in the use of %VAR% and
 * %%VAR%% for special configuration variables. To get past this problem this class 
 * implements a pre-processor that quotes all scalars before they are sent to the
 * SnakeYAML parser.
 * 
 * Currently this only works for syntax checking and not the outline view since
 * when altering the text in this way there becomes a mismatch between the offset reported
 * by SnakeYAML for the different tokens and the offset in the actual document.  
 * @author oysteto
 *
 */
public class SymfonyCompatibilityMode {
    
    private YAMLScanner scanner;
    
    public SymfonyCompatibilityMode( YAMLScanner scanner ){
        this.scanner = scanner;
    }
    
    /**
     * Quote all scalars in the document that are not already quoted.
     * @param document The document with the content.
     * @return A string of the document content where all the scalars have
     * been quoted if they where not already quoted.
     */
    public String quoteScalars( IDocument document ){
        
        int rangeLength = document.getLength();
        scanner.setRange(document, 0, rangeLength);

        // Scan for tokens. It is crucial to update the range of the
        // scanner after each read token to prevent and infinite loop
        String replacedContent = "";
        IToken token = scanner.nextToken();
        while (token != Token.EOF) {

            String tokenString;
            try {                
                tokenString = document.get(scanner.getTokenOffset(), scanner.getTokenLength());

                if( token instanceof YAMLToken ){
                    YAMLToken yt = (YAMLToken) token;
                    if( yt.getTokenType() == YAMLToken.SCALAR && !startsWithQuote( tokenString ) ){
                        tokenString = "'" + tokenString + "'";
                    }
                }

                replacedContent += tokenString;
            
            } catch (BadLocationException e) {
                YEditLog.logException(e, "Quoting scalars failed" );
                
                //if quoting the scalars failed, just return the original text to prevent
                //more problems
                return document.get();
            }

            //update the range of the scanner
            int newOffset = scanner.getTokenOffset() + scanner.getTokenLength();
            rangeLength = rangeLength - scanner.getTokenLength();
            scanner.setRange(document, newOffset, rangeLength);

            token = scanner.nextToken();
        }
        
        return replacedContent;

    }
    
    protected boolean startsWithQuote( String s ){
        
        if( s.charAt(0) == '\'' || s.charAt(0) == '"' ){
            return true;
        }        
        return false;
    }
    
    
}
