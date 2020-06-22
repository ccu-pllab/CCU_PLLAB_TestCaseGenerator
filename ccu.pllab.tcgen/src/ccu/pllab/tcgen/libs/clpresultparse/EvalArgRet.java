package ccu.pllab.tcgen.libs.clpresultparse;

 
import org.apache.commons.lang3.tuple.ImmutablePair;

import ccu.pllab.tcgen.libs.clpresultparse.ResultParser.ArgRetContext;
import ccu.pllab.tcgen.libs.pivotmodel.UML2Operation;

// evalute return value in the ARG array
// if the return value is a literal, return it;s value in string
// if the return value is an object, return it's ID in string
public class EvalArgRet extends ResultBaseVisitor<ImmutablePair<String, String>> {
	UML2Operation mMethodInfo = null;

	public EvalArgRet(UML2Operation mInfo) {
		mMethodInfo = mInfo;
	}
 
	@Override
	public ImmutablePair<String, String> visitArgRet(ArgRetContext ctx) {
		String type = mMethodInfo.getRetType();
		String value = "";

		if (ctx.literal() != null) {
			value = ctx.literal().getText();
		} else if (ctx.objElm() != null) {
			value = ctx.objElm().INTEGER().getText();
		}

		ImmutablePair<String, String> ret = new ImmutablePair<String, String>(type, value);

		return ret;
	}

}
