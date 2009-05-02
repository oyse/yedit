package org.dadacoalition.yedit.test.scanner;

import java.util.HashMap;

import org.dadacoalition.yedit.editor.YAMLToken;

public class ScannerToken {
	
	private Integer times = 1;
	private String type;
	private int tokenType;

	private static final HashMap<String,Integer> TOKEN_TYPES = new HashMap<String,Integer>();
	
	static {
		TOKEN_TYPES.put("DOCUMENT_START", YAMLToken.DOCUMENT_START );
		TOKEN_TYPES.put("DOCUMENT_END", YAMLToken.DOCUMENT_END );
		TOKEN_TYPES.put("COMMENT", YAMLToken.COMMENT );
		TOKEN_TYPES.put("KEY", YAMLToken.KEY );
		TOKEN_TYPES.put("SCALAR", YAMLToken.SCALAR );
		TOKEN_TYPES.put("ANCHOR", YAMLToken.ANCHOR );
		TOKEN_TYPES.put("ALIAS", YAMLToken.ALIAS );
		TOKEN_TYPES.put("TAG_PROPERTY", YAMLToken.TAG_PROPERTY );
	}
	
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
		
		if( TOKEN_TYPES.containsKey( type.toUpperCase() ) ){
			tokenType = TOKEN_TYPES.get(type);
		} else {
			throw new RuntimeException( "'type' is an invalid token type" );
		}		
	}

	public int getTokenType(){
		return tokenType;
	}


}
