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
package org.dadacoalition.yedit.test.handlers.togglecomment;

import org.dadacoalition.yedit.test.YEditTestCase;

public class StartsWithCommentTestCase extends YEditTestCase {
	
	private boolean expected;
	private String line;

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public boolean getExpected() {
		return expected;
	}

	public void setExpected(boolean expected) {
		this.expected = expected;
	}
	

}
