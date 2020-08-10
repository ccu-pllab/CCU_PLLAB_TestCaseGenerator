:- lib(ic).
:- lib(timeout).
timeTime_1([],[],[],[],[Result_0]):-
H_0#>=0,
H_0#=<23,
M_0#>=0,
M_0#=<59,
S_0#>=0,
S_0#=<59,
Hour_0=H_0,
Minute_0=M_0,
Second_0=S_0.
testTimeTime(S_pre,Arg_pre,S,Arg,Result):-
timeTime_1(S_pre,Arg_pre,S,Arg,Result),
labeling(S_pre),
labeling(S),
labeling(Arg_pre),
labeling(Arg).

