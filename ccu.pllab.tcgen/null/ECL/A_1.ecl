:- lib(ic).
:- lib(timeout).
testpath1(Obj_pre, Arg_pre, Obj_post, Arg_post, Ret_val):-

aA(Prearg1, Poststate1, Postarg1),
Obj_pre = [[]|Pres1],
Arg_pre = [Prearg1|Prea1],
Obj_post = [Poststate1|Posts1],
Arg_post = [Postarg1|Posta1],
Ret_val = [[]|Re1],

aB(Poststate1, Prearg2, Poststate2, Postarg2, Return2),
Pres1 = [Poststate1|Pres2],
Prea1 = [Prearg2|Prea2],
Posts1 = [Poststate2|Posts2],
Posta1 = [Postarg2|Posta2],
Re1 = [Return2|Re2],

aD(Poststate2, Prearg3, Poststate3, Postarg3, Return3),
Pres2 = [Poststate2|Pres3],
Prea2 = [Prearg3|Prea3],
Posts2 = [Poststate3|Posts3],
Posta2 = [Postarg3|Posta3],
Re2 = [Return3|Re3],

aB(Poststate3, Prearg4, Poststate4, Postarg4, Return4),
Pres3 = [Poststate3|Pres4],
Prea3 = [Prearg4|Prea4],
Posts3 = [Poststate4|Posts4],
Posta3 = [Postarg4|Posta4],
Re3 = [Return4|Re4],

aD(Poststate4, Prearg5, Poststate5, Postarg5, Return5),
Pres4 = [Poststate4|Pres5],
Prea4 = [Prearg5|Prea5],
Posts4 = [Poststate5|Posts5],
Posta4 = [Postarg5|Posta5],
Re4 = [Return5|Re5],

aB(Poststate5, Prearg6, Poststate6, Postarg6, Return6),
Pres5 = [Poststate5|Pres6],
Prea5 = [Prearg6|Prea6],
Posts5 = [Poststate6|Posts6],
Posta5 = [Postarg6|Posta6],
Re5 = [Return6|Re6],

aD(Poststate6, Prearg7, Poststate7, Postarg7, Return7),
Pres6 = [Poststate6|Pres7],
Prea6 = [Prearg7|Prea7],
Posts6 = [Poststate7|Posts7],
Posta6 = [Postarg7|Posta7],
Re6 = [Return7|Re7],

aB(Poststate7, Prearg8, Poststate8, Postarg8, Return8),
Pres7 = [Poststate7|Pres8],
Prea7 = [Prearg8|Prea8],
Posts7 = [Poststate8|Posts8],
Posta7 = [Postarg8|Posta8],
Re7 = [Return8|Re8],

aD(Poststate8, Prearg9, Poststate9, Postarg9, Return9),
Pres8 = [Poststate8|Pres9],
Prea8 = [Prearg9|Prea9],
Posts8 = [Poststate9|Posts9],
Posta8 = [Postarg9|Posta9],
Re8 = [Return9|Re9],

aB(Poststate9, Prearg10, Poststate10, Postarg10, Return10),
Pres9 = [Poststate9|Pres10],
Prea9 = [Prearg10|Prea10],
Posts9 = [Poststate10|Posts10],
Posta9 = [Postarg10|Posta10],
Re9 = [Return10|Re10],

aD(Poststate10, Prearg11, Poststate11, Postarg11, Return11),
Pres10 = [Poststate10|Pres11],
Prea10 = [Prearg11|Prea11],
Posts10 = [Poststate11|Posts11],
Posta10 = [Postarg11|Posta11],
Re10 = [Return11|Re11],

aB(Poststate11, Prearg12, Poststate12, Postarg12, Return12),
Pres11 = [Poststate11|Pres12],
Prea11 = [Prearg12|Prea12],
Posts11 = [Poststate12|Posts12],
Posta11 = [Postarg12|Posta12],
Re11 = [Return12|Re12],

Poststate12 = [P12],
P12 #>0,
aE(Poststate12, Prearg13, Poststate13, Postarg13, Return13),
Pres12 = [Poststate12],
Prea12 = [Prearg13],
Posts12 = [Poststate13],
Posta12 = [Postarg13],
Re12 = [Return13].


