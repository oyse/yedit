package org.dadacoalition.yedit.editor;

import org.dadacoalition.yedit.Activator;
import org.dadacoalition.yedit.preferences.PreferenceConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.*;
import org.eclipse.swt.graphics.RGB;


public class YAMLScanner extends RuleBasedScanner {

	public YAMLScanner(ColorManager colorManager) {

		IPreferenceStore store = Activator.getDefault().getPreferenceStore();		
		RGB keyColor = PreferenceConverter.getColor(store, PreferenceConstants.COLOR_KEY);
			
		IToken keyToken = new YAMLToken( 
				new TextAttribute(colorManager.getColor(keyColor)),
				YAMLToken.KEY
				);
		
		RGB scalarColor = PreferenceConverter.getColor(store, PreferenceConstants.COLOR_SCALAR);
		IToken scalarToken = new YAMLToken(
				new TextAttribute( colorManager.getColor(scalarColor) ),
				YAMLToken.SCALAR
				);	

		RGB commentColor = PreferenceConverter.getColor(store, PreferenceConstants.COLOR_COMMENT);
		IToken commentToken = new YAMLToken(
				new TextAttribute(colorManager.getColor(commentColor)),
				YAMLToken.COMMENT
				);	

		RGB documentColor = PreferenceConverter.getColor(store, PreferenceConstants.COLOR_DOCUMENT);
		IToken documentToken = new YAMLToken(
				new TextAttribute(
						colorManager.getColor(documentColor)
					),
				YAMLToken.DOCUMENT
		);
						
		
		
		IRule[] rules = new IRule[5];
			
		rules[0] = new MultiLineRule( "\"", "\"", scalarToken, '\\' );
		rules[1] = new MultiLineRule( "'", "'", scalarToken );
		rules[2] = new EndOfLineRule( "#", commentToken );
		rules[3] = new EndOfLineRule( "---", documentToken );
		rules[4] = new RegexRule( "\\w[\\w\\t ]*:\\s", keyToken );
		
		setRules( rules );
		setDefaultReturnToken( scalarToken );

		
	}

}
