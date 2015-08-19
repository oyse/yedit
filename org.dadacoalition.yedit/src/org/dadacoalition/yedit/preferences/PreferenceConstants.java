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
package org.dadacoalition.yedit.preferences;

/**
 * Constant definitions for plug-in preferences
 */
public class PreferenceConstants {

	public static final String COLOR_COMMENT = "colorCommentPreference";
	public static final String BOLD_COMMENT = "boldCommentPreference";
	public static final String ITALIC_COMMENT = "italicCommentPreference";
	public static final String UNDERLINE_COMMENT = "underlineCommentPreference";
	
	public static final String COLOR_KEY = "colorKeyPreference";
    public static final String BOLD_KEY = "boldKeyPreference";
    public static final String ITALIC_KEY = "italicKeyPreference";
    public static final String UNDERLINE_KEY = "underlineKeyPreference";

	public static final String COLOR_SCALAR = "colorScalarPreference";
    public static final String BOLD_SCALAR = "boldScalarPreference";
    public static final String ITALIC_SCALAR = "italicScalarPreference";
    public static final String UNDERLINE_SCALAR = "underlineScalarPreference";

	public static final String COLOR_DEFAULT = "colorDefaultPreference";
    public static final String BOLD_DEFAULT = "boldDefaultPreference";
    public static final String ITALIC_DEFAULT = "italicDefaultPreference";
    public static final String UNDERLINE_DEFAULT = "underlineDefaulPreference";

	public static final String COLOR_DOCUMENT = "colorDocumentPreference";
    public static final String BOLD_DOCUMENT = "boldDocumentPreference";
    public static final String ITALIC_DOCUMENT = "italicDocumentPreference";
    public static final String UNDERLINE_DOCUMENT = "underlineDocumentPreference";

	public static final String COLOR_ANCHOR = "colorAnchorPreferences";
    public static final String BOLD_ANCHOR = "boldAnchorPreference";
    public static final String ITALIC_ANCHOR = "italicAnchorPreference";
    public static final String UNDERLINE_ANCHOR = "underlineAnchorPreference";

	public static final String COLOR_ALIAS = "colorAliasPreferences";
    public static final String BOLD_ALIAS = "boldAliasPreference";
    public static final String ITALIC_ALIAS = "italicAliasPreference";
    public static final String UNDERLINE_ALIAS = "underlineAliasPreference";

	public static final String COLOR_TAG_PROPERTY = "colorTagPropertyPreferences";
    public static final String BOLD_TAG_PROPERTY = "boldTagPropertyPreference";
    public static final String ITALIC_TAG_PROPERTY = "italicTagPropertyPreference";
    public static final String UNDERLINE_TAG_PROPERTY = "underlineTagPropertyPreference";

	public static final String COLOR_INDICATOR_CHARACTER = "colorFlowCharacterPreferences";
    public static final String BOLD_INDICATOR_CHARACTER = "boldFlowCharacterPreference";
    public static final String ITALIC_INDICATOR_CHARACTER = "italicFlowCharacterPreference";
    public static final String UNDERLINE_INDICATOR_CHARACTER = "underlineFlowCharacterPreference";

	public static final String COLOR_CONSTANT = "colorConstantPreferences";
    public static final String BOLD_CONSTANT = "boldConstantPreference";
    public static final String ITALIC_CONSTANT = "italicConstantPreference";
    public static final String UNDERLINE_CONSTANT = "underlineConstantPreference";
	
	public static final String SPACES_PER_TAB = "spacesPerTab";
	
	public static final String AUTO_EXPAND_OUTLINE = "autoExpandOutline";
	
	public static final String SYNTAX_VALIDATION_ERROR = "syntaxValidationError";
	public static final String SYNTAX_VALIDATION_WARNING = "syntaxValidationWarning";
	public static final String SYNTAX_VALIDATION_IGNORE = "syntaxValidationIgnore";
	
	public static final String VALIDATION = "validation";
	
	/** the number of seconds that should pass between each time the the syntax highlighter reevaluates
	 * 	the entire YAML file. */
	public static final String SECONDS_TO_REEVALUATE = "secondsToReevaluate";
	
	/**
	 * Should the outline view show tag information or not.
	 */
	public static final String OUTLINE_SHOW_TAGS = "outlineShowTags";
	
	/**
	 * The maximum length that is shown of a scalar in the outline view.
	 */
	public static final String OUTLINE_SCALAR_MAX_LENGTH = "outlineScalarMaxLength";
	
	
	public static final String SYMFONY_COMPATIBILITY_MODE = "symfonyCompatibilityMode";
	
	
	public static final String FORMATTER_EXPLICIT_START = "formatterExplicitStart";
	public static final String FORMATTER_EXPLICIT_END = "formatterExplicitEmd";
	public static final String FORMATTER_LINE_WIDTH = "formatterLineWidth";
	public static final String FORMATTER_FLOW_STYLE = "formatterFlowStyle";
	public static final String FORMATTER_SCALAR_STYLE = "formatterScalarStyle";
	public static final String FORMATTER_PRETTY_FLOW = "formatterPrettyFlow";
	    
}
