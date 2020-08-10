package ccu.pllab.tcgen.libs.clpresultparse;
 

import ccu.pllab.tcgen.libs.clpresultparse.ResultParser.ArgArgContext;
import ccu.pllab.tcgen.libs.pivotmodel.Model;
import ccu.pllab.tcgen.libs.pivotmodel.ObjectInstance;

public class EvalArgArg extends ResultBaseVisitor<String> {
	Model mClsDiagInfo = null;

	public EvalArgArg(Model clsInfo) {
		mClsDiagInfo = clsInfo;
	}
 
	// report only object id if the argument is an object
	@Override
	public String visitArgArg(ArgArgContext ctx) {
		String ret = "";

		if (ctx.pairedLiteral() != null)
			ret = ctx.pairedLiteral().getText();
		else if (ctx.pairedObj() != null) {
			EvalObjElm eval = new EvalObjElm(mClsDiagInfo);
			ObjectInstance obj = eval.visit(ctx.pairedObj().objElm(0));

			ret = String.valueOf(obj.getOid());
		}

		return ret;
	}

}