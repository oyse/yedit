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
