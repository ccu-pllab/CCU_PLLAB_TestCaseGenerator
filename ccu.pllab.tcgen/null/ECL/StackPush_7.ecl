:- lib(ic).
:- lib(timeout).
stackPush_8([Data_pre,Bound_pre],[Value_pre],[Data,Bound],[Value],[Result]):-
Datapresize_pre#=3,
Bound_pre#=4,
Datapresize_pre#>=Bound_pre,
Result="Exception",Bound=Bound_pre,Value=Value_pre.
testStackPush(Obj_pre,Arg_pre,Obj,Arg,Result):-
[Bound_pre]:: -32768..32767,
[Value_pre]:: -32768..32767,
[Bound]:: -32768..32767,
[Value]:: -32768..32767,
Datapresize#=3,
intSequenceInstances(Datapresize,Data_pre),
length(Data,Datapresize),
List1_pre=Data_pre,
Obj_pre=[Data_pre,Bound_pre],
Arg_pre=[Value_pre],
Obj=[Data,Bound],
Arg=[Value],
stackPush_8(Obj_pre,Arg_pre,Obj,Arg,Result),
Obj=Obj_pre,
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

