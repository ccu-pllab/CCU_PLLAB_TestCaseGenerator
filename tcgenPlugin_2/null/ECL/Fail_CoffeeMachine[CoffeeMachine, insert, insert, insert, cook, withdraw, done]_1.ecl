:- lib(ic).
:- lib(timeout).
testpath1(Obj_pre, Arg_pre, Obj_post, Arg_post, Ret_val):-

coffeemachineCoffeeMachine(Prearg1, Poststate1, Postarg1),
Obj_pre = [[]|Pres1],
Arg_pre = [Prearg1|Prea1],
Obj_post = [Poststate1|Posts1],
Arg_post = [Postarg1|Posta1],
Ret_val = [[]|Re1],

coffeemachineInsert(Poststate1, Prearg2, Poststate2, Postarg2, Return2),
Pres1 = [Poststate1|Pres2],
Prea1 = [Prearg2|Prea2],
Posts1 = [Poststate2|Posts2],
Posta1 = [Postarg2|Posta2],
Re1 = [Return2|Re2],

coffeemachineInsert(Poststate2, Prearg3, Poststate3, Postarg3, Return3),
Pres2 = [Poststate2|Pres3],
Prea2 = [Prearg3|Prea3],
Posts2 = [Poststate3|Posts3],
Posta2 = [Postarg3|Posta3],
Re2 = [Return3|Re3],

coffeemachineInsert(Poststate3, Prearg4, Poststate4, Postarg4, Return4),
Pres3 = [Poststate3|Pres4],
Prea3 = [Prearg4|Prea4],
Posts3 = [Poststate4|Posts4],
Posta3 = [Postarg4|Posta4],
Re3 = [Return4|Re4],

coffeemachineCook(Poststate4, Prearg5, Poststate5, Postarg5, Return5),
Pres4 = [Poststate4|Pres5],
Prea4 = [Prearg5|Prea5],
Posts4 = [Poststate5|Posts5],
Posta4 = [Postarg5|Posta5],
Re4 = [Return5|Re5],

coffeemachineWithdraw(Poststate5, Prearg6, Poststate6, Postarg6, Return6),
Pres5 = [Poststate5|Pres6],
Prea5 = [Prearg6|Prea6],
Posts5 = [Poststate6|Posts6],
Posta5 = [Postarg6|Posta6],
Re5 = [Return6|Re6],

Poststate6 = [P6],
P6 #= 0,

coffeemachineDone(Poststate6, Prearg7, Poststate7, Postarg7, Return7),
Pres6 = [Poststate6],
Prea6 = [Prearg7],
Posts6 = [Poststate7],
Posta6 = [Postarg7],
Re6 = [Return7].


