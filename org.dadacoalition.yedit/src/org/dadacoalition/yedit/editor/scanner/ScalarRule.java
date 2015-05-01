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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dadacoalition.yedit.Activator;
import org.dadacoalition.yedit.YEditLog;
import org.dadacoalition.yedit.preferences.PreferenceConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

/**
 * Scanner rule for matching scalars that are not inside double or single quotes. Scalars
 * inside double or single quotes are taken care of by other rules. Scalars matched by this
 * rule can contain whitespaces and still be part the same scalar token. The scalars cannot
 * span across lines.
 * @author oysteto
 *
 */
public class ScalarRule implements IRule {

    private IToken token;
    private String firstCharRegex = "\\w + - / \\. \\ \\( \\) \\? \\@ \\$ < = > \\|";
    private String otherCharRegex = "\\[ \\] ' \\w \\s + - / \\. \\\\ \\( \\) \\? \\@ \\$ < = > \\| \\{ \\}";
    private String endCharRegex = "\\w + - / \\. \\\\ \\( \\) \\? \\@ \\$ < = > \\| '";

    public ScalarRule( IToken token ){

        //when in Symfony compatibility mode % are allowed as part of a scalar
        IPreferenceStore prefs = Activator.getDefault().getPreferenceStore();
        if( prefs.getBoolean(PreferenceConstants.SYMFONY_COMPATIBILITY_MODE ) ){
            firstCharRegex += " %";
            otherCharRegex += " %";
            endCharRegex += " %";
        }

        this.token = token;
    }


    public IToken evaluate(ICharacterScanner scanner) {

        YEditLog.logger.info("Evaluating ScalarRule");

        int c = scanner.read();
        String firstChar = "" + (char) c;

        //the set of characters that are allowed to start a scalar are different than
        //characters that are allowed in the rest of the scalar, so do a check on the first
        //character first.
        if( !Pattern.matches( "[" + firstCharRegex + "]", firstChar)){
            scanner.unread();
            return Token.UNDEFINED;
        }

        
        // for some reason it seems like something in otherCharRegex absolutely wants to match against ','
        // so I explicitly remove the match here.
        Pattern p = Pattern.compile( "[" + otherCharRegex + "&&[^,]]", Pattern.COMMENTS );
        String previousChar = "";
        String totalString = "";
        while( c != ICharacterScanner.EOF  ){
            String character = "" + (char) c;


            Matcher m = p.matcher(character);

            //do not allow this type of scalars to span multiple lines.
            //I think this will make the result more consistent when it comes
            //to parse results between single line damage/repair and whole document
            //damage/repair
            if( '\n' == (char) c || '\r' == (char) c || !m.matches()){

                // some characters are not allowed to end a unquoted scalar so we rewind
                // the scanner one extra step. This is not needed for the syntax hightlighter
                if( !Pattern.matches( "[" + endCharRegex + "]", previousChar)){
                    YEditLog.logger.info( "Character '" + previousChar + "' is not valid end char" );
                    scanner.unread();
                }
                scanner.unread();
                break;
            }
            totalString += character;
            previousChar = character;
            c = scanner.read();
            
        }
        
        YEditLog.logger.info("Matched string " + totalString );

        return token;
    }

}
