grammar YAML;

options {
  language=Java;
}

@header{
package org.dadacoalition.yedit.parser;
}

@lexer::header{
package org.dadacoalition.yedit.parser;
}
 

file
  : document*;
  
document
  : DOCUMENT_START NEWLINE ( node NEWLINE)? DOCUMENT_END NEWLINE?; 

node 
  : scalar;  
  
scalar 
  : DOUBLEQUOTE ( STRING | WHITESPACE )* DOUBLEQUOTE
  | SINGLEQUOTE ( STRING | WHITESPACE )* SINGLEQUOTE ;
  

STRING
  : NORMAL_CHARS+;
 
NEWLINE
  : '\n'; 
  
DOCUMENT_START
  : '---';
  
DOCUMENT_END
  : '...';

DOUBLEQUOTE
  : '"';
  
SINGLEQUOTE
  : '\'';
  

fragment NORMAL_CHARS
  : 'a'..'z'|'A'..'Z'|'0'..'9' | '\\\'' | '\\"' | WHITESPACE;

fragment WHITESPACE
  : ' ' | '\t';

    