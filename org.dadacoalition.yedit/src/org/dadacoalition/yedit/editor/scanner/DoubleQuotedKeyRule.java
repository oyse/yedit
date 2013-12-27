package org.dadacoalition.yedit.editor.scanner;

import org.eclipse.jface.text.rules.IToken;

public class DoubleQuotedKeyRule extends KeyRule {

    public DoubleQuotedKeyRule( IToken token ){
        super(token);
    }    

    protected String getKeyRegex(){
        return "( \" [ \\[ \\] ' \\w \\s + - / \\\\ \\. \\( \\) \\? \\@ \\$ _ < = > \\| \\{ \\} \\* ]*\" \\s* : ) \\s.*";
    }
    
}
