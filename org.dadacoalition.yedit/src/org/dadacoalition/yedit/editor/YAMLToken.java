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
	public static int INDICATOR_CHARACTER = 9;
	public static int DIRECTIVE = 10;
	public static int CONSTANT = 11;
	public static int WHITESPACE = 12;

	//helper mapping used by toString(). Should really be easier way to do this.
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
		TOKEN_NAMES.put( YAMLToken.INDICATOR_CHARACTER, "INDICATOR_CHARACTER" );
		TOKEN_NAMES.put( YAMLToken.DIRECTIVE, "DIRECTIVE" );
		TOKEN_NAMES.put( YAMLToken.CONSTANT, "CONSTANT" );
        TOKEN_NAMES.put( YAMLToken.WHITESPACE, "WHITESPACE" );		
	}	
	
	//helper mapping used for the scanner tests
	public static final HashMap<String,Integer> VALID_TOKENS = new HashMap<String, Integer>();
	static {
        VALID_TOKENS.put( "DOCUMENT_START", YAMLToken.DOCUMENT_START );
        VALID_TOKENS.put( "DOCUMENT_END", YAMLToken.DOCUMENT_END );
        VALID_TOKENS.put( "COMMENT", YAMLToken.COMMENT );
        VALID_TOKENS.put( "KEY", YAMLToken.KEY );
        VALID_TOKENS.put( "SCALAR", YAMLToken.SCALAR );
        VALID_TOKENS.put( "ANCHOR", YAMLToken.ANCHOR );
        VALID_TOKENS.put( "ALIAS", YAMLToken.ALIAS );
        VALID_TOKENS.put( "TAG_PROPERTY", YAMLToken.TAG_PROPERTY );
        VALID_TOKENS.put( "INDICATOR_CHARACTER", YAMLToken.INDICATOR_CHARACTER );
        VALID_TOKENS.put( "DIRECTIVE", YAMLToken.DIRECTIVE );
        VALID_TOKENS.put( "CONSTANT", YAMLToken.CONSTANT );	   
        VALID_TOKENS.put( "WHITESPACE", YAMLToken.WHITESPACE );        
	}
	
	
	protected int tokenType;
	
	public YAMLToken( Object data, int type ){
		super( data );
		this.tokenType = type;		
	}
	
	public int getTokenType(){
	    return this.tokenType;
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
