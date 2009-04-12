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
