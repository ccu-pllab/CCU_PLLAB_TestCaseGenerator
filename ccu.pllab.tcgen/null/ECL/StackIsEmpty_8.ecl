:- lib(ic).
:- lib(timeout).
stackIsEmpty_9([Data_pre,Bound_pre],[],[Data,Bound],[],[Result]):-
Datapresize_pre#=4,
Bound_pre#=4,
Datapresize_pre#\=0,
Result=false,
Data=Data_pre,Bound=Bound_pre.
testStackIsEmpty(Obj_pre,Arg_pre,Obj,Arg,Result):-
[Bound_pre]:: -32768..32767,
[Bound]:: -32768..32767,
Datapresize#=4,
intSequenceInstances(Datapresize,Data_pre),
length(Data,Datapresize),
List1_pre=Data_pre,
Obj_pre=[Data_pre,Bound_pre],
Obj=[Data,Bound],
stackIsEmpty_9(Obj_pre,Arg_pre,Obj,Arg,Result),
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

