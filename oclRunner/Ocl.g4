/**Define a grammar called Ocl   
 * Write: 王建瓏 
 * 參考:http://www.yancy.org/education/phd/links/ocl-grammar-01b.pdf
 * */ 
 			
grammar Ocl;   

@header
{//這部分是就是java裏頭import的部分，因為.g4檔與OclParser.java是連動關係，所以不能直接在OclParser.java增添import，一旦存檔就會被洗掉，因此如果要import的時候，在@header裏頭加
	import ccu.pllab.tcgen.AbstractSyntaxTree.*;
	import java.util.ArrayList;
	import java.util.Set;
} 

/*code 寫法
 *仍是一條線讀取，只是為了美觀
 *某些語意規則底下搭配註解是特例，方便給初學者簡單觀念
 *困難的特例仍符合語法規則，但請自己推敲
*/

packageDeclarationCS returns[PackageExp astRoot]:
	'package' paNa=packageName 		
	{$astRoot=new PackageExp($paNa.name);}
	oclExp=oclExpressions			
	{//連接所有context
		for(AbstractSyntaxTreeNode node :$oclExp.contextNode)
		($astRoot).addTreeChildNode(node);
	} 
	'endpackage';
/*packageDeclarationCS  
 *example:
 *  package tcgen
 *  (Content)
 *  endpackage
 */






packageName returns[String name]://name是package name
	 phNa=pathName					
	 {$name=$phNa.name;};//ex: packageName=>tcgen




pathName returns[String name]: //package name
	NAME 							
	{$name=$NAME.text;}    
	( '::' NAME   					
	{$name+="::"+$NAME.text;}
	)*;
	
	
	

oclExpressions returns [ArrayList<AbstractSyntaxTreeNode> contextNode]: //所有context 
	cons=constraints				
	{($contextNode)=($cons.contextNode);};
/*this ocl file includes many context
 * context 1....
 * context 2...
 * ...
 * context n...
 */




constraints returns [ArrayList<AbstractSyntaxTreeNode> contextNode]://單獨一個context
		con=constraint				
		{
			$contextNode=new ArrayList<AbstractSyntaxTreeNode>();
			($contextNode).add($con.contextNode);
		} 
		cons=constraints		//這裡可以呼叫多個constrain	
		{($contextNode).addAll($cons.contextNode);}
		|
		{$contextNode=new ArrayList<AbstractSyntaxTreeNode>();};//或許可省略




constraint returns [AbstractSyntaxTreeNode contextNode]:
			coDe=contextDeclaration		(sType=stereotype 			
			(NAME {($sType.stereo).setException($NAME.text);})? ':' oclExp=oclExpression	
			{
				($sType.stereo).addTreeChildNode($oclExp.node);
				if($coDe.node instanceof OperationContext)
				{
					((OperationContext)($coDe.node)).setStereoType($sType.stereo);
					//if($co.stereo!=null)
					//((OperationContext)($coDe.node)).setStereoType($co.stereo);
				}
				else{
					((ClassifierContext)($coDe.node)).setStereoType($sType.stereo);
				}
			}
			
			)+	
			co=content					
			{
				//($sType.stereo).addTreeChildNode($oclExp.node);
				if($coDe.node instanceof OperationContext)
				{
				//	((OperationContext)($coDe.node)).setStereoType($sType.stereo);
					if($co.stereo!=null)
					((OperationContext)($coDe.node)).setStereoType($co.stereo);
				}
				//else{
				//	((ClassifierContext)($coDe.node)).setStereoType($sType.stereo);
				//}
				$contextNode=$coDe.node;
			} 
			//|('def' NAME? ':' letExpression*)
			; 
/*
 *context method1(...)
 * inv/pre/post  exception:
 * ..............
 */ 
 
 
 
 	
content returns [StereoType stereo]:sType=stereotype
		 NAME?':' oclExp=oclExpression  
		 {
		 	$stereo =$sType.stereo;
			($stereo).addTreeChildNode($oclExp.node);
		} 
		//|('def' NAME? ':' letExpression*) content
		|
		;




contextDeclaration returns[AbstractSyntaxTreeNode node]:
		'context'  
		( opCo=operationContext 		{$node=$opCo.operation;}//是method
		|clCo= classifierContext 		{$node=$clCo.classifier;}//是classifier
		); 




stereotype returns [StereoType stereo] :  
		'pre'							{$stereo=new StereoType("precondition");} 
		| 'post'						{$stereo=new StereoType("postcondition");} 
		| 'inv' 						{$stereo=new StereoType("invariant");};




operationContext returns [OperationContext operation]:
		NAME '::' 
		opNa=operationName 				{
			$operation=new OperationContext($NAME.text,$opNa.name);
		}
		'(' fpl=formalParameterList		{($operation).setParameters($fpl.list);} 
		')' ( ':' rt=returnType			{($operation).setReturnType($rt.reType);}	
		)?;




classifierContext returns [ClassifierContext classifier]: 
		( NAME ':' NAME )
		|NAME{$classifier=new ClassifierContext($NAME.text);};




operationName returns [String name] : NAME{$name=$NAME.text;} | '=' | '+' | '-' | '<' | '<=' |'>=' | '>' | '/' | '*' | '<>' |'implies' | 'not' | 'or' | 'xor' | 'and'; 




returnType returns [String reType]: 
		ts=typeSpecifier				{$reType=$ts.value;}; 
/*
 * Context Date:function(parameter):Type
 * pre/post/inv (Exception)? :
 * (content)
 */




formalParameterList returns[ArrayList<PropertyCallExp> list]: //這裡描述operation裏頭的參數
		{$list=new ArrayList<PropertyCallExp> ();}
		( NAME ':' tySpec=typeSpecifier fpls=formalParameterLists			
			{
				PropertyCallExp var=new PropertyCallExp($NAME.text);
				var.setType($tySpec.value);
				($list).add(var);
				($list).addAll($fpls.list);
			}
		)?;
		
		
		
		
formalParameterLists returns[ArrayList<PropertyCallExp> list]://這個list只容許variableExp，因為operation裏頭是variable
	',' NAME ':' tySpec=typeSpecifier fpls=formalParameterLists		
	{
		$list=new ArrayList<PropertyCallExp> ();
		PropertyCallExp var=new PropertyCallExp($NAME.text);
		var.setType($tySpec.value);
		($list).add(var);
		($list).addAll($fpls.list);
	}
	|{$list=new ArrayList<PropertyCallExp> ();} 
	;




typeSpecifier returns[String value]: //這裡的value是指什麼type
		sts=simpleTypeSpecifier		{$value=$sts.name;}
		| col=collectionKind{$value=$col.type;};
 
 
 
 
collectionType : collectionKind '(' simpleTypeSpecifier ')';//未處理




oclExpression returns [AbstractSyntaxTreeNode  node] : 
		ex=expression					{$node=$ex.node;}
		;




letExpression returns [OperatorExp  operator]: 
		'let' NAME 		{PropertyCallExp var=new PropertyCallExp($NAME.text);}				
	//	( '(' fpl=formalParameterList ')'	)? //暫時用不到
	//	( ':' tySpec=typeSpecifier 		)? //暫時用不到			
		'='	ex=expression	'in'				
		{
			OperatorExp eq=new OperatorExp("=");
			eq.setOperand(var,$ex.node);
			$operator=new OperatorExp("and");
		}			
	//	(lets=letExpression		{($operator).setOperand(eq,$lets.operator);}		
		//| 
		ex=expression			{($operator).setOperand(eq,$ex.node);}		
		//)
		|'let' NAME ':' 'Array(Integer)' '=' 'RandomIndexArray{1..size@pre}'
		 {PropertyCallExp var=new PropertyCallExp("randomIndexArray");
		 	PropertyCallExp size=new PropertyCallExp("size");
		 	size.setTimeExpression();
		 	size.setType("int");
		 	PropertyCallExp index= new PropertyCallExp("arrayIndex");
		 	index.setType("int");
		 	var.addParameters(size,index);
		 }'in' ex=expression{
		 	OperatorExp and=new OperatorExp("and");
		 	and.setOperand(var,$ex.node);
		 	$operator=and;
		 }
		 |'let' NAME ':' 'Array(Integer)' '=' 'RandomIndexArray{1..'INTEGER'}'
		 {PropertyCallExp var=new PropertyCallExp("randomIndexArray");
		 	LiteralExp liter=new LiteralExp("Integer",$INTEGER.text);
		 	PropertyCallExp index= new PropertyCallExp("arrayIndex");
		 	index.setType("int");
		 	var.addParameters(liter,index);
		 }'in' ex=expression{
		 	OperatorExp and=new OperatorExp("and");
		 	and.setOperand(var,$ex.node);
		 	$operator=and;
		 }
		;




expression returns[AbstractSyntaxTreeNode node]: //rowExpression指的是一行運算式子，且是用來印呼叫method call的參數為來用的
		logicExp=logicalExpression	
		{
			$node=$logicExp.node;
		}
		;
		
		
		

logicalExpression returns[AbstractSyntaxTreeNode node] :
		 reExp=relationalExpression  
		 relExp=relationalExpressions[$reExp.node]		
		 {
		 	$node=$relExp.node;
		};
		
		
		
		
relationalExpressions[AbstractSyntaxTreeNode reExp1] returns [AbstractSyntaxTreeNode node]: 
		logicOper=logicalOperator //	做and/or						
		reExp2=relationalExpression 
		reExps=relationalExpressions[$reExp2.node]		
		{
			($logicOper.operator).setOperand($reExp1,$reExps.node);
			$node=$logicOper.operator;
			}
		|	{$node=$reExp1;};




logicalOperator returns [String logic,OperatorExp operator]: 
		'and' 		{$logic="and";$operator=new OperatorExp("and");}
		|'or'		{$logic="or";$operator=new OperatorExp("or");} 
		|'xor'  	{$logic="xor";$operator=new OperatorExp("xor");}
		|'implies'	{$logic="implies";$operator=new OperatorExp("implies");};




relationalExpression 		returns [AbstractSyntaxTreeNode node]: 
		addEx1=additiveExpression	{$node=$addEx1.node;}//做不等式
		(relation=relationalOperator 		addEx2=additiveExpression  			
			{
				($relation.operator).setOperand ($addEx1.node,$addEx2.node);
				$node=$relation.operator;
			}
		)? 
		;




relationalOperator returns [String relation,OperatorExp operator]: 
		  '='	{$relation="=";$operator=new OperatorExp ("=");} 
		| '>'	{$relation=">";$operator=new OperatorExp (">");} 
		| '<'	{$relation="<";$operator=new OperatorExp ("<");} 
		| '>='  {$relation=">=";$operator=new OperatorExp (">=");}
		| '<='	{$relation="<=";$operator=new OperatorExp ("<=");} 
		| '<>'	{$relation="<>";$operator=new OperatorExp ("<>");};




additiveExpression 			returns [AbstractSyntaxTreeNode node]: 
	mulEx1=multiplicativeExpression 
	mulExs=multiplicativeExpressions[$mulEx1.node]				
	{
		$node=$mulExs.node;
	}
	;
	
	
	
	
multiplicativeExpressions[AbstractSyntaxTreeNode mulEx1] returns [AbstractSyntaxTreeNode node]
	:addOp=addOperator 			//後做加減						
	mulEx2=multiplicativeExpression
	mulExs=multiplicativeExpressions[$mulEx2.node] 		
	{
		($addOp.operator).setOperand($mulEx1,$mulExs.node);
		$node=$addOp.operator;
	} 
	|{$node=$mulEx1;}
	;




addOperator returns[String type,OperatorExp operator] : 
	'+'	 {$type="+";$operator=new OperatorExp ("+");} 
	| '-'{$type="-";$operator=new OperatorExp ("-");}
	;




multiplicativeExpression 	returns [AbstractSyntaxTreeNode node]: 
	unEx1=unaryExpression 
	unExs=unaryExpressions[$unEx1.node]					
	{
		$node=$unExs.node;
		};
	
	
	
	
unaryExpressions[AbstractSyntaxTreeNode unEx1] returns [AbstractSyntaxTreeNode node]: 
	mulOp=multiplyOperator		//先做乘除						 
	unEx2=unaryExpression 
	unExs=unaryExpressions[$unEx2.node]					
	{
		($mulOp.operator).setOperand($unEx1,$unExs.node);
		$node=$mulOp.operator;
	} 
	|{$node=$unEx1;};
			
			
			
																	
multiplyOperator returns[String type,OperatorExp operator]: 
	'*'  {$type="*";$operator=new OperatorExp ("*");} 
	| '/'{$type="/";$operator=new OperatorExp ("/");}
	;
	
	
	
																	
unaryExpression 			returns [AbstractSyntaxTreeNode node]: 
	unOp= unaryOperator //做負號
	poEx=postfixExpression				
	{
		($unOp.operator).setUnaryOperand($poEx.node);
		$node=$unOp.operator;
	} 
	|poEx= postfixExpression			
	{$node=$poEx.node;};




unaryOperator returns[String type,OperatorExp operator]: 
	'-' 	{$type="-";$operator=new OperatorExp("-");}
	| 'not' {$type="not";$operator=new OperatorExp("not");}
	;




postfixExpression 	returns [AbstractSyntaxTreeNode node]: 
	priEx=primaryExpression //if或運算或變數自然數或做let或呼叫attribute
	pes=primaryExpressions[$priEx.node] ( cal=calculator[$pes.node]{$node=$cal.node;}|{$node=$pes.node;});
primaryExpressions [AbstractSyntaxTreeNode priEx]	returns [AbstractSyntaxTreeNode node]:	 
('.' {$node=new OperatorExp(".");}| '->'{$node=new OperatorExp("->");})prop=propertyCall 
 pes=primaryExpressions[$prop.property]{((OperatorExp)$node).setOperand($priEx,$pes.node);}
|{$node=$priEx;};
	
calculator[AbstractSyntaxTreeNode priEx] returns[AbstractSyntaxTreeNode node]:
'.add('{$node=new OperatorExp("+");} ex=expression {((OperatorExp)$node).setOperand($priEx,$ex.node);}')' cal=calculator[$node] {$node=$cal.node;}
|'.sub('{$node=new OperatorExp("-");} ex=expression {((OperatorExp)$node).setOperand($priEx,$ex.node);}')' cal=calculator[$node] {$node=$cal.node;}
|'.mul(' {$node=new OperatorExp("*");} ex=expression {((OperatorExp)$node).setOperand($priEx,$ex.node);}')' cal=calculator[$node] {$node=$cal.node;}
|'.div(' {$node=new OperatorExp("/");}ex=expression {((OperatorExp)$node).setOperand($priEx,$ex.node);}')' cal=calculator[$node] {$node=$cal.node;}
|'.mod(' {$node=new OperatorExp("%");}ex=expression {((OperatorExp)$node).setOperand($priEx,$ex.node);}')' cal=calculator[$node] {$node=$cal.node;}
|{$node=$priEx;}
;

primaryExpression returns[AbstractSyntaxTreeNode node]: 
	literCol=literalCollection{$node=$literCol.node;}//基本型態
	| Literal=literal 			
		{
  		$node=$Literal.liter;
  		} 
  	| prop=propertyCall			{$node=$prop.property;}	
	| '('ex= expression 		
		{
			$node=$ex.node;
		}
	')'
	| ifExp=ifExpression		{$node=$ifExp.node;}
	|letExp=letExpression 		{$node=$letExp.operator;}
	;

 


literal returns[LiteralExp liter]: 
	STRING					{$liter=new LiteralExp("String",$STRING.text);}
	|{boolean minus=false;}('-'{minus=true;})?INTEGER				{if(minus) $liter=new LiteralExp("Integer","-"+$INTEGER.text);else $liter=new LiteralExp("Integer",$INTEGER.text);}
	|INTEGER {String real=$INTEGER.text;}'.' INTEGER{real+="."+$INTEGER.text;$liter=new LiteralExp("Real",real);}
	|enumLiteral
	|boolExp=booleanExp		{$liter=new LiteralExp("Boolean",$boolExp.value);}
	;




booleanExp returns[String value]:
	'true'		{$value="true";}
	|'false'	{$value="false";}
	;




propertyCall returns[PropertyCallExp property]: 
	paNa= pathName{$property=new PropertyCallExp($paNa.name);}
( timeExpression {($property).setTimeExpression();})?
( qua=qualifiers {($property).setQualifier($qua.list);   if($qua.list2!=null){($property).setQualifier2($qua.list2);}  })?
( pcps=propertyCallParameters {($property).setParameters($pcps.list);})?
;





timeExpression : '@' 'pre';




propertyCallParameters returns[ArrayList<AbstractSyntaxTreeNode> list]: 
	'(' 						
	( de=declarator)?
	(apl= actualParameterList
		{
			$list=$apl.list;
		}
	)? ')'						
	; 
 
  

ifExpression returns[IfExp node] : 
	'if' condition=expression 'then' then=expression  ifs=ifExps[$condition.node,$then.node]		{$node=$ifs.node;}
	; 

 


ifExps[AbstractSyntaxTreeNode condition,AbstractSyntaxTreeNode then] returns [IfExp node]: 
	'else' elseExp=expression  'endif' 			
	{$node=new IfExp("If",$condition,$then,$elseExp.node);}
	|'endif'									
	{$node=new IfExp("If",$condition,$then);}
	; 



enumLiteral : NAME '::' NAME ( '::' NAME )*;//未處理

simpleTypeSpecifier returns [String name]: pn=pathName{$name=$pn.name;};//未處理

literalCollection returns [AbstractSyntaxTreeNode node]: colKind=collectionKind{CollectionExp node1=new CollectionExp($colKind.type);} '{'( colItem=collectionItem[node1](',' collectionItem[node1] )*)?'}' '->' coMe=collectionMethod[node1] {if($coMe.node2 instanceof IterateExp) {$node=$coMe.node2;}else {((CollectionExp)$node).setMethodName(((PropertyCallExp)$coMe.node2).getVariable()); $node=node1;}};//未處理 

collectionItem [AbstractSyntaxTreeNode node]: ex=expression {((CollectionExp)$node).setStart($ex.node);}('..' ex2=expression {((CollectionExp)$node).setEnd($ex2.node);})?;

collectionKind returns[String type]: 'Set' {$type="Set";}| 'Bag' {$type="Bag";}| 'Sequence'{$type="Sequence";} | 'OrderSet'{$type="OrderSet";}|'Integer[]'{$type="Intger[]";}|'Integer[][]'{$type="Intger[][]";};//未處理

collectionMethod[AbstractSyntaxTreeNode node] returns [AbstractSyntaxTreeNode node2]:
{	$node2=new IterateExp(((CollectionExp)$node).getType());}
('iterate(it: Integer;'{((IterateExp)$node2).setIt("it");}|'iterate(it2: Integer;'{((IterateExp)$node2).setIt("it2");})  
pN=pathName{((IterateExp)$node2).setAccVariable($pN.name);} ':'
 {
 	((IterateExp)$node2).setStart(((CollectionExp)$node).getStart());
 	((IterateExp)$node2).setEnd(((CollectionExp)$node).getEnd());
 }('Integer = ' lit=literal
 	{((IterateExp)$node2).setAccType("Integer");
 	 ((CollectionExp)$node).setAccType("Integer");
 	 ((IterateExp)$node2).setAcc(($lit.liter).getValue());
 	 ((CollectionExp)$node).setAcc(($lit.liter).getValue());
 	}|'Boolean = '{((IterateExp)$node2).setAccType("Boolean");((CollectionExp)$node).setAccType("Boolean");}'true'{((IterateExp)$node2).setAcc("true");((CollectionExp)$node).setAcc("true");}
 ) '|' ex=expression {((IterateExp)$node2).setBody($ex.node);((CollectionExp)$node).setBody($ex.node);}')'
|'prepend('expression')'
|'subsequence('expression ','literalCollection')'
|;

qualifiers returns [ArrayList<AbstractSyntaxTreeNode> list,ArrayList<AbstractSyntaxTreeNode> list2]: '[' apl=actualParameterList {$list=$apl.list;$list2=null;}']'('[' apl2=actualParameterList {$list2=$apl2.list;}']')?;




declarator ://method call裏頭參數//未用到
	NAME 	
	( ',' NAME 	 )* 
	( ':' sts=simpleTypeSpecifier 	
	)?
	( ';' NAME ':' typeSpecifier '='expression)?
	'|'
	;

actualParameterList returns[ArrayList<AbstractSyntaxTreeNode> list]: //method call裏頭參數,list是有多少個參數
	ex1=expression  						
	{
		$list=new ArrayList<AbstractSyntaxTreeNode>();
		($list).add($ex1.node);
		
	}
	apls=actualParameterLists[$ex1.node]	
	{
		($list).addAll($apls.list);
		
	}
	;

actualParameterLists[AbstractSyntaxTreeNode ex1] returns[ArrayList<AbstractSyntaxTreeNode> list]://method call裏頭參數，當有很多個參數時,list是有多少個參數
	',' ex2=expression						
	{
		$list=new ArrayList<AbstractSyntaxTreeNode>();
		($list).add($ex2.node);
	} 
	apls=actualParameterLists[$ex2.node]	
	{
		($list).addAll($apls.list);
	}
	|	{
			$list=new ArrayList<AbstractSyntaxTreeNode>();
		}
	;



NAME : [a-zA-Z_]([a-zA-Z0-9_]) *;

INTEGER:[0-9]  ([0-9])*; 

STRING: '\'' .*? '\'' ;

WHITESPACE : ([\t]|[\r]|[\n]|' ')+ -> skip ;