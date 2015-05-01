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
