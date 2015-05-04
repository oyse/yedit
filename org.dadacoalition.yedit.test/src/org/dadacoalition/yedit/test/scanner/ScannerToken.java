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

import org.dadacoalition.yedit.editor.scanner.YAMLToken;

public class ScannerToken {
	
	private Integer times = 1;
	private String type;
	private int tokenType;

	public Integer getTimes() {
		return times;
	}
	public void setTimes(Integer times) {
		this.times = times;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
		
		if( YAMLToken.VALID_TOKENS.containsKey( type.toUpperCase() ) ){
			tokenType = YAMLToken.VALID_TOKENS.get(type);
		} else {
			throw new RuntimeException( "'type' is an invalid token type" );
		}		
	}

	public int getTokenType(){
		return tokenType;
	}


}
