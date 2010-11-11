package org.dadacoalition.yedit.editor;

import java.util.ArrayList;

import org.dadacoalition.yedit.Activator;
import org.dadacoalition.yedit.preferences.PreferenceConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;

public class YAMLScanner extends RuleBasedScanner {

    private ColorManager colorManager;

    public YAMLScanner(ColorManager colorManager) {

        this.colorManager = colorManager;
        IPreferenceStore store = Activator.getDefault().getPreferenceStore();

        TextAttribute keyAttr = tokenAttribute(
                store,
                PreferenceConstants.COLOR_KEY,
                PreferenceConstants.BOLD_KEY,
                PreferenceConstants.ITALIC_KEY,
                PreferenceConstants.UNDERLINE_KEY);

        IToken keyToken = new YAMLToken(keyAttr, YAMLToken.KEY);

        TextAttribute scalarAttr = tokenAttribute(
                store,
                PreferenceConstants.COLOR_SCALAR,
                PreferenceConstants.BOLD_SCALAR,
                PreferenceConstants.ITALIC_SCALAR,
                PreferenceConstants.UNDERLINE_SCALAR);
        IToken scalarToken = new YAMLToken(scalarAttr, YAMLToken.SCALAR);

        TextAttribute commentAttr = tokenAttribute(
                store,
                PreferenceConstants.COLOR_COMMENT,
                PreferenceConstants.BOLD_COMMENT,
                PreferenceConstants.ITALIC_COMMENT,
                PreferenceConstants.UNDERLINE_COMMENT);
        IToken commentToken = new YAMLToken(commentAttr, YAMLToken.COMMENT);

        TextAttribute documentAttr = tokenAttribute(
                store,
                PreferenceConstants.COLOR_DOCUMENT,
                PreferenceConstants.BOLD_DOCUMENT,
                PreferenceConstants.ITALIC_DOCUMENT,
                PreferenceConstants.UNDERLINE_DOCUMENT);

        IToken documentStartToken = new YAMLToken(documentAttr, YAMLToken.DOCUMENT_START);
        IToken documentEndToken = new YAMLToken(documentAttr, YAMLToken.DOCUMENT_END);

        TextAttribute anchorAttr = tokenAttribute(
                store,
                PreferenceConstants.COLOR_ANCHOR,
                PreferenceConstants.BOLD_ANCHOR,
                PreferenceConstants.ITALIC_ANCHOR,
                PreferenceConstants.UNDERLINE_ANCHOR);
        IToken anchorToken = new YAMLToken(anchorAttr, YAMLToken.ANCHOR);

        TextAttribute aliasAttr = tokenAttribute(
                store,
                PreferenceConstants.COLOR_ALIAS,
                PreferenceConstants.BOLD_ALIAS,
                PreferenceConstants.ITALIC_ALIAS,
                PreferenceConstants.UNDERLINE_ALIAS);
        IToken aliasToken = new YAMLToken(aliasAttr, YAMLToken.ALIAS);

        IToken indicatorCharToken = new YAMLToken(new TextAttribute(null),
                YAMLToken.INDICATOR_CHARACTER);

        TextAttribute tagAttr = tokenAttribute(
                store,
                PreferenceConstants.COLOR_TAG_PROPERTY,
                PreferenceConstants.BOLD_TAG_PROPERTY,
                PreferenceConstants.ITALIC_TAG_PROPERTY,
                PreferenceConstants.UNDERLINE_TAG_PROPERTY);
        IToken tagPropToken = new YAMLToken(tagAttr, YAMLToken.TAG_PROPERTY);

        TextAttribute constantAttr = tokenAttribute(
                store,
                PreferenceConstants.COLOR_CONSTANT,
                PreferenceConstants.BOLD_CONSTANT,
                PreferenceConstants.ITALIC_CONSTANT,
                PreferenceConstants.UNDERLINE_CONSTANT);
        IToken predefinedValToken = new YAMLToken(constantAttr, YAMLToken.CONSTANT);

        IToken whitespaceToken = new YAMLToken(new TextAttribute(null), YAMLToken.WHITESPACE);
        
        IToken directiveToken = new YAMLToken(new TextAttribute(null), YAMLToken.DIRECTIVE );

        ArrayList<IRule> rules = new ArrayList<IRule>();

        rules.add(new KeyRule(keyToken));        
        rules.add(new SingleQuotedKeyRule(keyToken));
        rules.add(new DoubleQuotedKeyRule(keyToken));
        rules.add(new MultiLineRule("\"", "\"", scalarToken, '\\'));
        rules.add(new MultiLineRule("'", "'", scalarToken));
        rules.add(new EndOfLineRule("#", commentToken));
        rules.add(new EndOfLineRule("%TAG", directiveToken));
        rules.add(new EndOfLineRule("%YAML", directiveToken));
        rules.add(new RegexRule("---", documentStartToken));
        rules.add(new RegexRule("\\.\\.\\.", documentEndToken));
        rules.add(new IndicatorCharacterRule(indicatorCharToken));
        rules.add(new WhitespaceRule(whitespaceToken));
        rules.add(new WordPatternRule(new AnchorWordDetector(), "&", "", anchorToken));
        rules.add(new WordPatternRule(new AnchorWordDetector(), "*", "", aliasToken));
        rules.add(new WordPatternRule(new TagWordDetector(), "!", "", tagPropToken));

        rules.add(new PredefinedValueRule(predefinedValToken));

        rules.add(new ScalarRule(scalarToken));

        IRule[] rulesArray = new IRule[rules.size()];
        rules.toArray(rulesArray);
        setRules(rulesArray);
        setDefaultReturnToken(scalarToken);

    }

    private TextAttribute tokenAttribute(IPreferenceStore store, String colorPrefs,
            String boldPrefs, String italicPrefs, String underlinePrefs) {

        int style = SWT.NORMAL;

        boolean isBold = store.getBoolean(boldPrefs);
        if (isBold) {
            style = style | SWT.BOLD;
        }

        boolean isItalic = store.getBoolean(italicPrefs);
        if (isItalic) {
            style = style | SWT.ITALIC;
        }

        boolean isUnderline = store.getBoolean(underlinePrefs);
        if (isUnderline) {
            style = style | TextAttribute.UNDERLINE;
        }

        RGB color = PreferenceConverter.getColor(store, colorPrefs);
        TextAttribute attr = new TextAttribute(colorManager.getColor(color), null, style);
        return attr;
    }

}
