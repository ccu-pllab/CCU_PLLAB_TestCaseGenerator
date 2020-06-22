:- lib(ic).
:- lib(timeout).
timeNext_4([],[],[],[],[Result_0]):-
Self_1[Hour_0]=Self_0[Hour_0],
Self_2[Minute_0]=Self_1[Minute_0],
Self_3[Second_0]=Self_2[Second_0],
Result_1[Hour_0]=Result_0[Hour_0],
Result_2[Minute_0]=Result_1[Minute_0],
Result_3[Second_0]=Result_2[Second_0],
Second_0#=59,
Minute_0#=59,
Hour_0#\=23,
Result_4[Hour_0]#=Self_3[Hour_0]+1,
Result_5[Minute_0]#=0,
Result_6[Second_0]#=0.
testTimeNext(S_pre,Arg_pre,S,Arg,Result):-
timeNext_4(S_pre,Arg_pre,S,Arg,Result),
labeling(S_pre),
labeling(S),
labeling(Arg_pre),
labeling(Arg).

