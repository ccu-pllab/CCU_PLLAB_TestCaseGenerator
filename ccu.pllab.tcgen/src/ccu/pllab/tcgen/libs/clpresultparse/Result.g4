grammar Result;

 
 
result : preStateStr argStr postStateStr;

preStateStr : 'PRE' '=' stateList;

postStateStr : 'POST' '=' stateList;

argStr : 'ARG' '=' argList;

stateList : '[' elms (',' elms)* ']';

argList : '[' argRet ',' argSelf (',' argArg)* ']';

argRet : ('void'|literal|objElm);

argSelf : pairedObj;

argArg : (pairedLiteral|pairedObj);

pairedObj : '[' (objElm|'[]') ',' objElm ']';

pairedLiteral : '[' literal ',' literal ']';

elms : '[]'
	 | '[' (objElmList|ascElmList) ']';

objElmList : objElm (',' objElm)*;

ascElmList : ascElm (',' ascElm)*;

objElm : '(' 'uml_obj' ',' STRUCTNAME ',' INTEGER (',' (literal|objElm))* ')'
  ;
ascElm : '(' 'uml_asc' ',' STRUCTNAME ',' INTEGER ',' INTEGER (',' INTEGER)* ')'
  ;
literal : INTEGER 
	| FLOAT
	| list
  ;
STRUCTNAME : LETTER+(NUMBER|LETTER)*
  ;
ID : LETTER (NUMBER|LETTER)*  
  ;
INTEGER : '-'?[0-9]+
  ;
FLOAT :  [0-9]+('.'[0-9]+)('E'['+' | '-']? [0-9]+)?
  ;
list : '[' INTEGER ( ',' (INTEGER|list))* ']'
  ;
NUMBER : [0-9]
  ;
LETTER : [A-Za-z]
  ;
WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines
