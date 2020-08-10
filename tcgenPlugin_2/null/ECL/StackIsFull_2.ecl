:- lib(ic).
:- lib(timeout).
stackIsFull_3([Data_pre,Bound_pre],[],[Data,Bound],[],[Result]):-
Datapresize_pre#=0,
Bound_pre#=1,
Datapresize_pre#=Bound_pre,
Bound_pre#>0,
Result=true,
Data=Data_pre,Bound=Bound_pre.
testStackIsFull(Obj_pre,Arg_pre,Obj,Arg,Result):-
[Bound_pre]:: -32768..32767,
[Bound]:: -32768..32767,
Datapresize#=0,
intSequenceInstances(Datapresize,Data_pre),
length(Data,Datapresize),
List1_pre=Data_pre,
Obj_pre=[Data_pre,Bound_pre],
Obj=[Data,Bound],
stackIsFull_3(Obj_pre,Arg_pre,Obj,Arg,Result),
labeling(Arg_pre),
labeling(Arg).
integerGen(N) :-
  random(R),
  mod(R, 32767, N).
intSequenceInstances(0, []).
intSequenceInstances(Size, [N|Seq]) :-
  Size > 0,
  Size1 is Size -1,
  intSequenceInstances(Size1, Seq),
  integerGen(N).

