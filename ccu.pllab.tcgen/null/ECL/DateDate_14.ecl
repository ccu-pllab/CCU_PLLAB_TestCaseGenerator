:- lib(ic).
:- lib(timeout).
dateDate_14([],[Y_pre,M_pre,D_pre],[Year,Month,Day],[Y,M,D],[]):-
Y_pre#>1,
Y_pre#<3999,
M_pre#>1,
M_pre#<12,
D_pre#>=1,
M_pre#\=1,
Year#=Y_pre,
Month#=M_pre,
Day#=D_pre,Y=Y_pre,M=M_pre,D=D_pre,Year#>=1,
Year#=<3999,
Month#>=1,
Month#=<12,
Day#>=1.
testDateDate(Obj_pre,Arg_pre,Obj,Arg,Result):-
[Y_pre,M_pre,D_pre]:: 0..32767,
[Year,Month,Day]:: 0..32767,
[Y,M,D]:: 0..32767,
Arg_pre=[Y_pre,M_pre,D_pre],
Obj=[Year,Month,Day],
Arg=[Y,M,D],
dateDate_14(Obj_pre,Arg_pre,Obj,Arg,Result),
labeling(Obj_pre),
labeling(Obj),
labeling(Arg_pre),
labeling(Arg).

