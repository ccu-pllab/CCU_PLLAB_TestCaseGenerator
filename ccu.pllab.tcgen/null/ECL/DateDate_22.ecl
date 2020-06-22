:- lib(ic).
:- lib(timeout).
dateDate_22([],[Y_pre,M_pre,D_pre],[],[Y,M,D],[Result]):-
Y_pre#>1,
Y_pre#<3999,
M_pre#>=1,
M_pre#<12,
D_pre#>1,
M_pre#\=1,
Result="Exception",Y=Y_pre,M=M_pre,D=D_pre.
testDateDate(Obj_pre,Arg_pre,Obj,Arg,Result):-
[Y_pre,M_pre,D_pre]:: 0..32767,
[Y,M,D]:: 0..32767,
Arg_pre=[Y_pre,M_pre,D_pre],
Arg=[Y,M,D],
dateDate_22(Obj_pre,Arg_pre,Obj,Arg,Result),
labeling(Obj_pre),
labeling(Obj),
labeling(Arg_pre),
labeling(Arg).

