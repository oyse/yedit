/*******************************************************************************
 * Copyright (c) 2015 Øystein Idema Torget and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Øystein Idema Torget and others
 *******************************************************************************/
package org.dadacoalition.yedit.editor.scanner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.text.rules.IWordDetector;

/**
 * Word detector used in the scanner rule for tags. 
 */
public class TagWordDetector implements IWordDetector {

    private Pattern characterPattern = Pattern.compile( "[\\w \\d ! < > \\. , :]", Pattern.COMMENTS );

    public boolean isWordPart(char c) {
        
        Matcher m = characterPattern.matcher( new Character(c).toString() );
        if( m.matches() ){
            return true;
        }
        
        return false;
    }

    public boolean isWordStart(char c) {

        Matcher m = characterPattern.matcher( new Character(c).toString() );
        if( m.matches() ){
            return true;
        }
        
        return false;

    }
}
