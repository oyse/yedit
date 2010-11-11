package org.dadacoalition.yedit.editor;

import org.eclipse.jface.text.rules.IToken;

public class SingleQuotedKeyRule extends KeyRule {

    public SingleQuotedKeyRule( IToken token ){
        super(token);
    }    

    protected String getKeyRegex(){
        return "('[\\[ \\] \\w \\s \" + - / \\\\ \\. \\( \\) \\? \\@ \\$ _ < = > \\| \\{ \\} \\* ]*' \\s* : ) \\s.*";
    }

}
