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
package org.dadacoalition.yedit.test;

/**
 * Base class used by test cases that are defined in YAML files. When SnakeYAML reads
 * the YAML file all test cases will be instances of this class.
 * @author oysteto
 *
 */
public class YEditTestCase {
	
	private String name;

	/**
	 * @return The base name of the test case. Often a test case will have
	 * several associated tests. In that case this name is used as the base
	 * name for the test case.
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
