package y_edit.editor;

import org.eclipse.jface.text.rules.Token;

public class YAMLToken extends Token {
	
	public static int KEY = 1;
	
	public static int SCALAR = 2;
	
	public static int DOCUMENT = 3;
	
	public static int COMMENT = 4;
		
	private int tokenType;
	
	YAMLToken( Object data, int type ){
		super( data );
		this.tokenType = type;		
	}
		
	public String toString(){
		
		String typeString = "";
		switch(tokenType){
		case 1:
			typeString = "Key";
			break;
		case 2:
			typeString = "Scalar";
			break;
		case 3:
			typeString = "Document";
			break;
		case 4:
			typeString = "Comment";
			break;
		default:
			typeString = "Unknown";
		}
		
		return typeString;
	
	}

}
