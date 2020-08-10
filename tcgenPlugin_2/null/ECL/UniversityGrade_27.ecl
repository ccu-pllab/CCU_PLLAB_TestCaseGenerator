:- lib(ic).
:- lib(timeout).
universityGrade_27([],[N_pre],[],[N],[Result]):-
N_pre#=0,N_pre#=<100,
N_pre#>59,
(N_pre#<60;N_pre#>69),
(N_pre#<70;N_pre#>79),
(N_pre#<80;N_pre#>89),
N_pre#>=90,
Result="A",N=N_pre.
testUniversityGrade(S_pre,Arg_pre,S,Arg,Result):-
[N_pre]:: -10..32767,
[N]:: -10..32767,
S_pre=[],
Arg_pre=[N_pre],
S=[],
Arg=[N],
universityGrade_27(S_pre,Arg_pre,S,Arg,Result),
labeling(S_pre),
labeling(S),
labeling(Arg_pre),
labeling(Arg).

