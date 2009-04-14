package org.dadacoalition.yedit.editor;

import java.util.HashMap;

import org.eclipse.jface.text.rules.Token;

public class YAMLToken extends Token {
	
	public static int KEY = 1;
	public static int SCALAR = 2;
	public static int DOCUMENT_START = 3;
	public static int DOCUMENT_END = 4;
	public static int COMMENT = 5;
	public static int ANCHOR = 6;
	public static int ALIAS = 7;
	public static int TAG_PROPERTY = 8;
	public static int FLOW_CHARACTER = 9;

	//helper mapping used by toString(). Should really be simple way to do this.
	private static final HashMap<Integer, String> TOKEN_NAMES = new HashMap<Integer, String>();
	static {
		TOKEN_NAMES.put( YAMLToken.DOCUMENT_START, "DOCUMENT_START" );
		TOKEN_NAMES.put( YAMLToken.DOCUMENT_END, "DOCUMENT_END" );
		TOKEN_NAMES.put( YAMLToken.COMMENT, "COMMENT" );
		TOKEN_NAMES.put( YAMLToken.KEY, "KEY" );
		TOKEN_NAMES.put( YAMLToken.SCALAR, "SCALAR" );
		TOKEN_NAMES.put( YAMLToken.ANCHOR,"ANCHOR" );
		TOKEN_NAMES.put( YAMLToken.ALIAS, "ALIAS" );
		TOKEN_NAMES.put( YAMLToken.TAG_PROPERTY, "TAG_PROPERTY" );
		TOKEN_NAMES.put( YAMLToken.FLOW_CHARACTER, "FLOW_CHARACTER" );
	}	
	
	
	protected int tokenType;
	
	public YAMLToken( Object data, int type ){
		super( data );
		this.tokenType = type;		
	}
		
	public String toString(){	
		return TOKEN_NAMES.get( tokenType );	
	}
	
	public boolean equals( Object o ){
		
		if( !( o instanceof YAMLToken ) ){
			return false;
		}
		
		YAMLToken t = ( YAMLToken ) o;
		if( t.tokenType == this.tokenType ){
			return true;
		}
		
		return false;
		
	}

}
