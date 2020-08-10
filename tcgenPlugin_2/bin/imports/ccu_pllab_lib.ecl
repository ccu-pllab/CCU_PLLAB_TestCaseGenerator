:-lib(apply).
:-lib(apply_macros).
:-lib(lists).
:-lib(listut).
:-lib(ic).

ocl_iterate(Instances, Vars, CollectionPredicate, AccInitPredicate, AccIterPredicate, Result) :- 
	apply(CollectionPredicate, [Instances, Vars, Collection]),
	apply(AccInitPredicate, [Instances, Vars, AccInitValue]),
	ocl_iterate_helper(Instances, Vars, Collection, AccInitValue, AccIterPredicate, Result).
	
delay ocl_iterate_helper(_, _, Collection, _, _, _) if var(Collection).
ocl_iterate_helper(_, _, Collection, AccInitValue, _, AccInitValue) :- length(Collection, 0).
ocl_iterate_helper(Instances, Vars, Collection, AccInitValue, AccIterPredicate, Result) :- 
	( 
	  foreach(Elem, Collection),   
	  fromto(AccInitValue,AccPre,AccPost, Result),
	  param(Instances, Vars, AccIterPredicate)
        do
	  append(Vars, [[AccPre, AccPre], [Elem, Elem]], NewVars),
	  apply(AccIterPredicate, [Instances, NewVars, AccPost]) 
	).
	
iterate_test(_, [[AccPre, AccPre], [Elem, Elem]], Result) :-
	Result #= Elem + AccPre.

variable_state("precondition", [Pre, _], Pre).
variable_state("postcondition", [_, Post], Post).
variable_state("both", [Pre, _], Pre).

instance_state("precondition", [InstancePre, _], InstancePre).
instance_state("postcondition", [_, InstancePost], InstancePost).
instance_state("both", [InstancePre, _], InstancePre).

ocl_if(Instances, Vars, ConditionPredicate, ThenPredicate, ElsePredicate, Result) :-
        apply(ConditionPredicate, [Instances, Vars, Condition]),
        ocl_if_delay(Instances, Vars, Condition, ThenPredicate, ElsePredicate, Result).
        
delay ocl_if_delay(_, _, Condition, _, _, _) if nonground(Condition).         
ocl_if_delay(Instances, Vars, Condition, ThenPredicate, ElsePredicate, Result) :-
	(
        Condition = 1 ->
              apply(ThenPredicate, [Instances, Vars, Result]);
              (Condition = 0, apply(ElsePredicate, [Instances, Vars, Result]))
    ).

is_collection(L) :-
	is_list(L),
	nth1(1, L, E),
	(uml_obj ~= E),
	(uml_asc ~= E).


ocl_set_at(Collection, Index, Elem) :-
	ocl_sequence_at(Collection, Index, Elem).
	
stateSync(Pre, Post) :-
	length(Pre, N),
	(count(I, 1, N), param(Pre, Post) do
		nth1(I, Pre, PreInstances), nth1(I, Post, PostInstances),
		(
			((var(PostInstances), eclipse_language:delayed_goals_number(PostInstances, DN), DN = 0) -> PostInstances = PreInstances);
			(instanceType(I, uml_obj) -> syncClass(PreInstances, PostInstances); true)
		)
	).
	
syncClass(PreInstances, PostInstances) :-
	length(PreInstances, N),
	(count(I, 1, N), param(PreInstances, PostInstances) do
		nth1(I, PreInstances, PreObj), nth1(I, PostInstances, PostObj),
		syncObj(PreObj, PostObj)
	).

syncObj(Pre, Post) :-
	length(Pre, N),
	(count(I, 1, N), param(Pre, Post) do
		nth1(I, Pre, PreAtt), nth1(I, Post, PostAtt),
		((var(PostAtt), not is_solver_var(PostAtt)) -> PostAtt = PreAtt); true
	).

delay gen_seq_from_range(Start, Last, _) if (var(Start); var(Last)).	
gen_seq_from_range(Start, Last, Result) :-
	Domain :: Start..Last,
	get_domain_as_list(Domain, Result).
	
ocl_string_equals(_, _, X, Y, Result) :-
	(Result = 1 ; Result = 0) ,
	(Result = 1 ,X=Y;Result=0,compare_not_equals(X,Y)).

ocl_string_not_equals(_,_, X, Y, Result):-
	(Result = 1 ; Result = 0),
	(Result = 1 ->compare_not_equals(X,Y);X=Y).

delay compare_not_equals([X|_],[Y|_]) if var(Y);var(X).
compare_not_equals([A|X],[B|Y]):-
	(A\=B->true;compare_not_equals_word(X,Y)).
compare_not_equals_word([X|Xs],[Y|Ys]):-
	(X#\=Y);(Y=X,compare_not_equals_word(Xs,Ys)).

ocl_string_greater_than(_,_, X, Y, Result):-
	(Result = 1 ; Result = 0),
	(Result = 1 ->Z#>0;Z#=<0),
	compareTo(X,Y,Z).

ocl_string_greater_equal(_,_, X, Y, Result):-
	(Result = 1 ; Result = 0),
	(Result = 1 ->Z#>=0;Z#<0),
	compareTo(X,Y,Z).
	
ocl_string_less_than(_,_, X, Y, Result):-
	(Result = 1 ; Result = 0),
	(Result = 1 ->Z#<0;Z#>=0),
	compareTo(X,Y,Z).

ocl_string_less_equal(_,_, X, Y, Result):-
	(Result = 1 ; Result = 0),
	(Result = 1 ->Z#=<0;Z#>0),
	compareTo(X,Y,Z).

ocl_string_size(_,_, [Length|_], Result) :-
	Result=Length.   

ocl_string_plus(_,_, X, Y,Result):-
	ocl_string_concat(_,_,X,Y,Result).


ocl_string_concat(_,_, [LengthX|StringX], [LengthY|StringY], [LengthResult|StringResult]) :-
	LengthResult#=LengthX+LengthY,
	concat_string([LengthX|StringX], [LengthY|StringY], [LengthResult|StringResult]).

delay concat_string([LengthX|_],_,_) if var(LengthX).	
concat_string([LengthX|StringX],[_|StringY],[_|StringResult]):-
	length(StringX,LengthX),
	append(StringX,StringY,StringResult).
	

   
ocl_string_substring(_,_, [LengthX|StringX], Position, Z, [LengthResult|StringResult]):-
	Position#>=1,
	LengthResult#=Z-Position+1,
	LengthX#>=Position,
	LengthX#>=Z,
	Z#>=Position,
	LengthResult#>=0,
	test_sub_B(StringX,StringResult,Position,LengthResult).
	
delay test_sub_B(_,_,Position,LengthResult) if var(Position);var(LengthResult).	
test_sub_B([A|X],[B|Y],Position,LengthResult):-
	(Position > 1 -> N#=Position-1,test_sub_B(X,[B|Y],N,LengthResult);(LengthResult #> 1 -> W#=LengthResult-1,A=B,test_sub_B(X,Y,Position,W);Y=[],A=B)).

	
ocl_string_toBoolean(_,_, X, Result):-
	X=[4,116,114,117,101], %true
	Result#=1.
	
ocl_string_toBoolean(_,_, X, Result):-
	X=[5,102,97,108,115,101], %false
	Result#=0.
	
ocl_string_indexOf(_,_, [LengthX|StringX], [LengthY|StringY], Result):-
	Result#>=0,
	(LengthX=0 , Result=0;LengthX#\=0,(LengthY=0,Result=1;LengthY#\=0,(
	Z#=LengthY+Result-1,ocl_string_substring(_,_, [LengthX|StringX], Result, Z, [LengthY|StringY])))).

ocl_string_toUpperCase(_,_, [LengthX|StringX],  [LengthResult|StringResult]):-
	LengthX#=LengthResult,
	toUpperCase_B(StringX,StringResult).

delay toUpperCase_B(X,Y) if var(X),var(Y).
toUpperCase_B([],[]).
toUpperCase_B([A|X],[B|Y]):-
	(A::97..122,B#=A-32;A::65..90,B#=A),toUpperCase_B(X,Y).		

ocl_string_toLowerCase(_,_, [LengthX|StringX],  [LengthResult|StringResult]):-
	LengthX#=LengthResult,
	toLowerCase_B(StringX,StringResult).
	
delay toLowerCase_B(X,Y) if var(X),var(Y).
toLowerCase_B([],[]).
toLowerCase_B([A|X],[B|Y]):-
	(A::97..122,B#=A;A::65..90,B#=A+32),toLowerCase_B(X,Y).	

	
ocl_string_at(_,_, X, Position, [LengthResult|StringResult]):-
	LengthResult = 1,
	ocl_string_substring(_,_,X, Position, Position, [LengthResult|StringResult]).
	
ocl_string_characters(_,_, X, Result):-
	Result=X.
	
ocl_string_equalsIgnoreCase(_,_, [LengthX|StringX], [LengthY|StringY], Result):-
	Result::0..1,
	(Result#=1,LengthX#=LengthY,equalsIgnoreCase_B(StringX,StringY,1))
	;(Result#=0,(LengthX#\=LengthY;equalsIgnoreCase_B(StringX,StringY,0))).

delay equalsIgnoreCase_B(X,Y,_) if var(X),var(Y).
equalsIgnoreCase_B([],[],1).
equalsIgnoreCase_B([A|X],[B|Y],1):-
	A::[65..90,97..122],
	B::[65..90,97..122],
	(A#=B;A#=B-32;A#=B+32),
	equalsIgnoreCase_B(X,Y,1).
equalsIgnoreCase_B([A|X],[B|Y],0):-
	A::[65..90,97..122],
	B::[65..90,97..122],
	((A#\=B,A#\=B-32,A#\=B+32);
	(A#=B;A#=B-32;A#=B+32),equalsIgnoreCase_B(X,Y,0)).
	
compareTo([A|X],[B|Y],Result):-
	(Result#=0,X=Y,A=B);(Result#>0,compare_larger_length([A|X],[B|Y],Result));(Result#<0,compare_small_length([A|X],[B|Y],Result)).
	
delay compare_larger_length([X|_],[Y|_],Result) if var(Y);var(X).
compare_larger_length([A|X],[B|Y],Result):-
	(A>B->Result#=A-B;compare_larger_word(X,Y,Result)).
compare_larger_word([X|Xs],[Y|Ys],Result):-
	(X#>Y,Result#=X-Y);(Y=X,compare_larger_word(Xs,Ys,Result)).
	
delay compare_small_length([X|_],[Y|_],Result) if var(X);var(Y).
compare_small_length([A|X],[B|Y],Result):-
	(B>A->Result#=A-B;compare_small_word(X,Y,Result)).
compare_small_word([X|Xs],[Y|Ys],Result):-
	(Y#>X,Result#=X-Y);(Y=X,compare_small_word(Xs,Ys,Result)).
	
parameterOfCollection(_, [_|_]).
parameterOfSequence(_, Seq) :- parameterOfCollection(_, Seq).
parameterOfSet(_, Set) :- parameterOfCollection(_, Set).
parameterOfOrderSet(_, Set) :- parameterOfCollection(_, Set).
parameterOfBag(_, Bag) :- parameterOfCollection(_, Bag).

nth1_var(A,[B|C],D):-(var(B)->A#=D;N#=D+1,nth1_var(A,C,N)).

stringDeclDomain(Char,LengthMin,LengthMax,[Length|X]):-
Length#>=0,
Length#>=LengthMin,
Length#=<LengthMax,
createChars_Length(X,Length),
createChars_Body(X,Char,Length).

delay createChars_Length(List,Length) if var(Length),not is_list(List).
createChars_Length(List,Length):-
length(List,Length).

delay createChars_Body(List,_,_) if var(List).
createChars_Body([],_,0).
createChars_Body([R | Rs],Char,Length) :-
	Length#>=1,
	N#=Length-1,
	R::Char,
	createChars_Body(Rs,Char,N).
		
collection_IntegerDeclDomain(Unique,Min,Max,LengthMin,LengthMax,[Length|List]):-
Length#>=0,
Length#>=LengthMin,
Length#=<LengthMax,
createIntegerList_Length(List,Length,Unique),
createIntegerList_Body(List,Min,Max,Length).

delay createIntegerList_Length(List,Length,_) if var(Length),not is_list(List).
createIntegerList_Length(List,Length,Unique):-
length(List,Length),
(Unique -> alldifferent(List);true).

delay createIntegerList_Body(List,_,_,_) if var(List).
createIntegerList_Body([],_,_,0).
createIntegerList_Body([A|B],Min,Max,Length):-
	Length#>=1,
	N#=Length-1,
	ic:'::'(A,Min .. Max),
	createIntegerList_Body(B,Min,Max,N).
	
collection_StringDeclDomain(Unique,Char,StringLengthMin,StringLengthMax,LengthMin,LengthMax,[Length|List]):-
Length#>=0,
Length#>=LengthMin,
Length#=<LengthMax,
createStringList_Length(List,Length,Unique),
createStringList_Body(List,Char,StringLengthMin,StringLengthMax,Length).

delay createStringList_Length(List,Length,_) if var(Length),not is_list(List).
createStringList_Length(List,Length,Unique):-
length(List,Length),
(Unique -> differentList(dif,List);true).

delay createStringList_Body(List,_,_,_,_) if var(List).
createStringList_Body([],_,_,_,_).
createStringList_Body([A|B],Char,StringLengthMin,StringLengthMax,Length):-
	Length#>=1,
	N#=Length-1,
	stringDeclDomain(Char,StringLengthMin,StringLengthMax,A),
	createStringList_Body(B,Char,StringLengthMin,StringLengthMax,N).
	
allIntegerIndomain(Instances):-
	flatten(Instances, AllVariables),
	selectlist(is_solver_type, AllVariables, ToBeLabeling),
	ic:'labeling'(ToBeLabeling),
	flatten(Instances, AllVariables2),
	selectlist(is_solver_type, AllVariables2, ToBeLabeling2),
	(ground(ToBeLabeling2)->true;allIntegerIndomain(Instances)).