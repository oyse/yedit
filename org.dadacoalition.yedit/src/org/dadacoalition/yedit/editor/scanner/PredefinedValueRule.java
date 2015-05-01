/*******************************************************************************
 * Copyright (c) 2015 Øystein Idema Torget 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Øystein Idema Torget
 *******************************************************************************/
package org.dadacoalition.yedit.editor.scanner;

import java.util.regex.Pattern;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

/**
 * Scanner rule for matching one of the predefined values/constants.
 * @author oysteto
 *
 */
public class PredefinedValueRule implements IRule {

    private IToken token;
    
    public PredefinedValueRule( IToken token ){
        this.token = token;
    }
    
    public IToken evaluate(ICharacterScanner scanner) {

        int c = scanner.read();
        
        //first possibility is a single char predefined value
        if( '~' == (char) c){
            return token;
        }
        
        //read the next three character for testing the 4 character predefined values
        String s = "" + (char) c;
        s += (char) scanner.read();
        s += (char) scanner.read();
        s += (char) scanner.read();

        String fourCharacterRegex = "true|True|TRUE";
        fourCharacterRegex += "|\\.inf|\\.Inf|\\.INF";
        fourCharacterRegex += "|\\.nan|\\.NaN|\\.NAN";
        fourCharacterRegex += "|null|Null|NULL|";
        
        if( Pattern.matches(fourCharacterRegex, s) ){
            return token;
        }
        
        //read the next character for testing 4 character predefined values
        s += (char) scanner.read();
        
        String fiveCharacterRegex = "|false|False|FALSE";
        if( Pattern.matches(fiveCharacterRegex, s) ){
            return token;
        }

        //no one of them matched so rewind the scanner
        for( int i = 0; i < 5; i++ ){
            scanner.unread();
        }
        
        return Token.UNDEFINED;
        
    }

}
