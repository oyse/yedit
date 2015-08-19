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
package org.dadacoalition.yedit.test.scanner;

import java.util.ArrayList;
import java.util.List;

import org.dadacoalition.yedit.editor.scanner.YAMLToken;
import org.dadacoalition.yedit.test.*;

public class ScannerTestCase extends YEditTestCase {

	private List<ScannerToken> expectedTokens;
	private int startOffset;
	private int rangeLength;
	private String contentFile;
		
	public List<ScannerToken> getExpectedTokens() {
		return expectedTokens;
	}
	
	public void setExpectedTokens(List<ScannerToken> expectedTokens) {
		this.expectedTokens = expectedTokens;
	}
	
	public int getStartOffset() {
		return startOffset;
	}
	public void setStartOffset(int offset) {
		this.startOffset = offset;
	}
	public int getRangeLength() {
		return rangeLength;
	}
	public void setRangeLength(int rangeLength) {
		this.rangeLength = rangeLength;
	}
	
	public String getContentFile() {
		return contentFile;
	}

	public void setContentFile(String contentFile) {
		this.contentFile = contentFile;
	}

	public ArrayList<YAMLToken> getYAMLTokens(){
		
		ArrayList<YAMLToken> yamlTokens = new ArrayList<YAMLToken>();
		for( ScannerToken st : expectedTokens ){
			for( int i = 0; i < st.getTimes(); i++ ){
				yamlTokens.add( new YAMLToken( new Object(), st.getTokenType() ) );
			}			
		}
		
		return yamlTokens;
		
	}
	
}
