package org.dadacoalition.yedit.editor;

import org.dadacoalition.yedit.Activator;

/**
 *
 */
public class ContentTypeIdForYaml {
	
	public final static String CONTENT_TYPE_ID_YAML = getConstantString();

	/**
	 * Don't allow instantiation.
	 */
	private ContentTypeIdForYaml() {
		super();
	}

	static String getConstantString() {
		return  Activator.PLUGIN_ID + ".yamlsource"; //$NON-NLS-1$
	}		

}
