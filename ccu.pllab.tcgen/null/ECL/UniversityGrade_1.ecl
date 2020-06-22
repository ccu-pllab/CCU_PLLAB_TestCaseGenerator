:- lib(ic).
:- lib(timeout).
universityGrade_1([],[N_pre],[],[N],[Result]):-
(N_pre#<0;N_pre#>100),
Result="Exception",N=N_pre.
testUniversityGrade(S_pre,Arg_pre,S,Arg,Result):-
[N_pre]:: -10..32767,
[N]:: -10..32767,
S_pre=[],
Arg_pre=[N_pre],
S=[],
Arg=[N],
universityGrade_1(S_pre,Arg_pre,S,Arg,Result),
labeling(S_pre),
labeling(S),
labeling(Arg_pre),
labeling(Arg).

