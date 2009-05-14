package org.dadacoalition.yedit.preferences;

/**
 * Constant definitions for plug-in preferences
 */
public class PreferenceConstants {

	public static final String COLOR_COMMENT = "colorCommentPreference";
	public static final String COLOR_KEY = "colorKeyPreference";
	public static final String COLOR_SCALAR = "colorScalarPreference";
	public static final String COLOR_DEFAULT = "colorDefaultPreference";
	public static final String COLOR_DOCUMENT = "colorDocumentPreference";
	public static final String COLOR_ANCHOR = "colorAnchorPreferences";
	public static final String COLOR_ALIAS = "colorAliasPreferences";
	public static final String COLOR_TAG_PROPERTY = "colorTagPropertyPreferences";
	public static final String COLOR_INDICATOR_CHARACTER = "colorFlowCharacterPreferences";
	public static final String COLOR_CONSTANT = "colorConstantPreferences";
	
	public static final String SPACES_PER_TAB = "spacesPerTab";
	
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
	
}
