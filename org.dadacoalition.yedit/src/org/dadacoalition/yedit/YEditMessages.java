package org.dadacoalition.yedit;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class YEditMessages {

	private static final String RESOURCE_BUNDLE = "org.dadacoalition.yedit.YEditMessages";//$NON-NLS-1$

	private static ResourceBundle fgResourceBundle = ResourceBundle
			.getBundle(RESOURCE_BUNDLE);


	public static String getString(String key) {
		try {
			return fgResourceBundle.getString(key);
		} catch (MissingResourceException e) {
			return "!" + key + "!";
		}
	}

	public static ResourceBundle getResourceBundle() {
		return fgResourceBundle;
	}
}
