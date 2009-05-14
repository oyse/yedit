package org.dadacoalition.yedit.editor;

import java.util.ArrayList;

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

        RGB keyColor = PreferenceConverter.getColor(store,
                PreferenceConstants.COLOR_KEY);
        IToken keyToken = new YAMLToken(new TextAttribute(colorManager
                .getColor(keyColor)), YAMLToken.KEY);

        RGB scalarColor = PreferenceConverter.getColor(store,
                PreferenceConstants.COLOR_SCALAR);
        IToken scalarToken = new YAMLToken(new TextAttribute(colorManager
                .getColor(scalarColor)), YAMLToken.SCALAR);

        RGB commentColor = PreferenceConverter.getColor(store,
                PreferenceConstants.COLOR_COMMENT);
        IToken commentToken = new YAMLToken(new TextAttribute(colorManager
                .getColor(commentColor)), YAMLToken.COMMENT);

        RGB documentColor = PreferenceConverter.getColor(store,
                PreferenceConstants.COLOR_DOCUMENT);
        IToken documentStartToken = new YAMLToken(new TextAttribute(
                colorManager.getColor(documentColor)), YAMLToken.DOCUMENT_START);
        IToken documentEndToken = new YAMLToken(new TextAttribute(colorManager
                .getColor(documentColor)), YAMLToken.DOCUMENT_END);

        RGB anchorColor = PreferenceConverter.getColor(store,
                PreferenceConstants.COLOR_ANCHOR);
        IToken anchorToken = new YAMLToken(new TextAttribute(colorManager
                .getColor(anchorColor)), YAMLToken.ANCHOR);

        RGB aliasColor = PreferenceConverter.getColor(store,
                PreferenceConstants.COLOR_ALIAS);
        IToken aliasToken = new YAMLToken(new TextAttribute(colorManager
                .getColor(aliasColor)), YAMLToken.ALIAS);

        RGB indicatorCharColor = PreferenceConverter.getColor(store,
                PreferenceConstants.COLOR_INDICATOR_CHARACTER);
        IToken indicatorCharToken = new YAMLToken(new TextAttribute(
                colorManager.getColor(indicatorCharColor)),
                YAMLToken.INDICATOR_CHARACTER);

        RGB tagPropColor = PreferenceConverter.getColor(store,
                PreferenceConstants.COLOR_TAG_PROPERTY);
        IToken tagPropToken = new YAMLToken(new TextAttribute(colorManager
                .getColor(tagPropColor)), YAMLToken.TAG_PROPERTY);

        RGB predefinedPropColor = PreferenceConverter.getColor(store,
                PreferenceConstants.COLOR_CONSTANT);
        IToken predefinedValToken = new YAMLToken(new TextAttribute(
                colorManager.getColor(predefinedPropColor)),
                YAMLToken.CONSTANT);

        IToken whitespaceToken = new YAMLToken(new TextAttribute(null),
                YAMLToken.WHITESPACE);

        ArrayList<IRule> rules = new ArrayList<IRule>();
        // IRule[] rules = new IRule[10];
        rules.add(new MultiLineRule("\"", "\"", scalarToken, '\\'));
        rules.add(new MultiLineRule("'", "'", scalarToken));
        rules.add(new EndOfLineRule("#", commentToken));
        rules.add(new EndOfLineRule("---", documentStartToken));
        rules.add(new EndOfLineRule("...", documentEndToken));
        rules.add(new RegexRule("\\s", whitespaceToken));
        rules.add(new RegexRule("\\{|\\}|\\[|\\]|,|-", indicatorCharToken));
        rules.add(new RegexRule("\\w[\\w\\t ]*:\\s", keyToken));
        rules
                .add(new WordPatternRule(new WordDetector(), "&", "",
                        anchorToken));
        rules.add(new WordPatternRule(new WordDetector(), "*", "", aliasToken));
        rules
                .add(new WordPatternRule(new WordDetector(), "!", "",
                        tagPropToken));

        String predefinedRegex = "true|True|TRUE";
        predefinedRegex += "|false|False|FALSE";
        predefinedRegex += "|\\.inf|\\.Inf|\\.INF";
        predefinedRegex += "|\\.nan|\\.NaN|\\.NAN";
        predefinedRegex += "|null|Null|NULL|~";
        rules.add(new RegexRule(predefinedRegex, predefinedValToken));

        IRule[] rulesArray = new IRule[rules.size()];
        rules.toArray(rulesArray);
        setRules(rulesArray);
        setDefaultReturnToken(scalarToken);

    }

}
