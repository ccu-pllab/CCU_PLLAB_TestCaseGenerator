/**Define a grammar called Ocl   
 * Write: �����n 
 * �Ѧ�:http://www.yancy.org/education/phd/links/ocl-grammar-01b.pdf
 * */ 
 			
grammar Ocl;   

@header
{//�o�����O�N�Ojava���Yimport�������A�]��.g4�ɻPOclParser.java�O�s�����Y�A�ҥH���ઽ���bOclParser.java�W�Kimport�A�@���s�ɴN�|�Q�~���A�]���p�G�nimport���ɭԡA�b@header���Y�[
	import ccu.pllab.tcgen.AbstractSyntaxTree.*;
	import java.util.ArrayList;
	import java.util.Set;
} 

/*code �g�k
 *���O�@���uŪ���A�u�O���F���[
 *�Y�ǻy�N�W�h���U�f�t���ѬO�S�ҡA��K����Ǫ�²���[��
 *�x�����S�Ҥ��ŦX�y�k�W�h�A���Цۤv���V
*/

packageDeclarationCS returns[PackageExp astRoot]:
	'package' paNa=packageName 		
	{$astRoot=new PackageExp($paNa.name);}
	oclExp=oclExpressions			
	{//�s���Ҧ�context
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






packageName returns[String name]://name�Opackage name
	 phNa=pathName					
	 {$name=$phNa.name;};//ex: packageName=>tcgen




pathName returns[String name]: //package name
	NAME 							
	{$name=$NAME.text;}    
	( '::' NAME   					
	{$name+="::"+$NAME.text;}
	)*;
	
	
	

oclExpressions returns [ArrayList<AbstractSyntaxTreeNode> contextNode]: //�Ҧ�context 
	cons=constraints				
	{($contextNode)=($cons.contextNode);};
/*this ocl file includes many context
 * context 1....
 * context 2...
 * ...
 * context n...
 */




constraints returns [ArrayList<AbstractSyntaxTreeNode> contextNode]://��W�@��context
		con=constraint				
		{
			$contextNode=new ArrayList<AbstractSyntaxTreeNode>();
			($contextNode).add($con.contextNode);
		} 
		cons=constraints		//�o�̥i�H�I�s�h��constrain	
		{($contextNode).addAll($cons.contextNode);}
		|
		{$contextNode=new ArrayList<AbstractSyntaxTreeNode>();};//�γ\�i�ٲ�




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
		( opCo=operationContext 		{$node=$opCo.operation;}//�Omethod
		|clCo= classifierContext 		{$node=$clCo.classifier;}//�Oclassifier
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




formalParameterList returns[ArrayList<PropertyCallExp> list]: //�o�̴y�zoperation���Y���Ѽ�
		{$list=new ArrayList<PropertyCallExp> ();}
		( NAME ':' tySpec=typeSpecifier fpls=formalParameterLists			
			{
				PropertyCallExp var=new PropertyCallExp($NAME.text);
				var.setType($tySpec.value);
				($list).add(var);
				($list).addAll($fpls.list);
			}
		)?;
		
		
		
		
formalParameterLists returns[ArrayList<PropertyCallExp> list]://�o��list�u�e�\variableExp�A�]��operation���Y�Ovariable
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




typeSpecifier returns[String value]: //�o�̪�value�O������type
		sts=simpleTypeSpecifier		{$value=$sts.name;}
		| col=collectionKind{$value=$col.type;};
 
 
 
 
collectionType : collectionKind '(' simpleTypeSpecifier ')';//���B�z




oclExpression returns [AbstractSyntaxTreeNode  node] : 
		ex=expression					{$node=$ex.node;}
		;




letExpression returns [OperatorExp  operator]: 
		'let' NAME 		{PropertyCallExp var=new PropertyCallExp($NAME.text);}				
	//	( '(' fpl=formalParameterList ')'	)? //�ȮɥΤ���
	//	( ':' tySpec=typeSpecifier 		)? //�ȮɥΤ���			
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




expression returns[AbstractSyntaxTreeNode node]: //rowExpression�����O�@��B�⦡�l�A�B�O�ΨӦL�I�smethod call���ѼƬ��ӥΪ�
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
		logicOper=logicalOperator //	��and/or						
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
		addEx1=additiveExpression	{$node=$addEx1.node;}//��������
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
	:addOp=addOperator 			//�ᰵ�[��						
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
	mulOp=multiplyOperator		//��������						 
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
	unOp= unaryOperator //���t��
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
	priEx=primaryExpression //if�ιB����ܼƦ۵M�Ʃΰ�let�ΩI�sattribute
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
	literCol=literalCollection{$node=$literCol.node;}//�򥻫��A
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



enumLiteral : NAME '::' NAME ( '::' NAME )*;//���B�z

simpleTypeSpecifier returns [String name]: pn=pathName{$name=$pn.name;};//���B�z

literalCollection returns [AbstractSyntaxTreeNode node]: colKind=collectionKind{CollectionExp node1=new CollectionExp($colKind.type);} '{'( colItem=collectionItem[node1](',' collectionItem[node1] )*)?'}' '->' coMe=collectionMethod[node1] {if($coMe.node2 instanceof IterateExp) {$node=$coMe.node2;}else {((CollectionExp)$node).setMethodName(((PropertyCallExp)$coMe.node2).getVariable()); $node=node1;}};//���B�z 

collectionItem [AbstractSyntaxTreeNode node]: ex=expression {((CollectionExp)$node).setStart($ex.node);}('..' ex2=expression {((CollectionExp)$node).setEnd($ex2.node);})?;

collectionKind returns[String type]: 'Set' {$type="Set";}| 'Bag' {$type="Bag";}| 'Sequence'{$type="Sequence";} | 'OrderSet'{$type="OrderSet";}|'Integer[]'{$type="Intger[]";}|'Integer[][]'{$type="Intger[][]";};//���B�z

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




declarator ://method call���Y�Ѽ�//���Ψ�
	NAME 	
	( ',' NAME 	 )* 
	( ':' sts=simpleTypeSpecifier 	
	)?
	( ';' NAME ':' typeSpecifier '='expression)?
	'|'
	;

actualParameterList returns[ArrayList<AbstractSyntaxTreeNode> list]: //method call���Y�Ѽ�,list�O���h�֭ӰѼ�
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

actualParameterLists[AbstractSyntaxTreeNode ex1] returns[ArrayList<AbstractSyntaxTreeNode> list]://method call���Y�ѼơA���ܦh�ӰѼƮ�,list�O���h�֭ӰѼ�
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