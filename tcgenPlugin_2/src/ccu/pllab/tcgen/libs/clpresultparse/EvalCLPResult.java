package ccu.pllab.tcgen.libs.clpresultparse;

 
import ccu.pllab.tcgen.libs.CLPResult;
import ccu.pllab.tcgen.libs.CLPState;
import ccu.pllab.tcgen.libs.clpresultparse.ResultParser.ArgArgContext;
import ccu.pllab.tcgen.libs.clpresultparse.ResultParser.ArgRetContext;
import ccu.pllab.tcgen.libs.clpresultparse.ResultParser.ArgSelfContext;
import ccu.pllab.tcgen.libs.clpresultparse.ResultParser.ArgStrContext;
import ccu.pllab.tcgen.libs.clpresultparse.ResultParser.AscElmListContext;
import ccu.pllab.tcgen.libs.clpresultparse.ResultParser.LiteralContext;
import ccu.pllab.tcgen.libs.clpresultparse.ResultParser.ObjElmListContext;
import ccu.pllab.tcgen.libs.clpresultparse.ResultParser.PostStateStrContext;
import ccu.pllab.tcgen.libs.clpresultparse.ResultParser.PreStateStrContext;
import ccu.pllab.tcgen.libs.clpresultparse.ResultParser.ResultContext;

public class EvalCLPResult extends ResultBaseVisitor<String> {

	public static enum ResultType {
		ARG, POST_STATE, PRE_STATE
	}
 
	// keep the intermedia parsing results
	CLPResult mResult = null;

	ResultType mRT;

	public CLPResult getResult() {
		return mResult;
	}

	public void setResult(CLPResult mResult) {
		this.mResult = mResult;
	}

	@Override
	public String visitArgArg(ArgArgContext ctx) {

		String str = "";

		if (ctx.pairedLiteral() != null)
			str = ctx.pairedLiteral().getText();
		else if (ctx.pairedObj() != null)
			str = ctx.pairedObj().getText();

		mResult.getArg().getArgList().add(str);

		return str;
	}

	@Override
	public String visitArgRet(ArgRetContext ctx) {
		String str = "";

		if (ctx.literal() != null)
			str = ctx.literal().getText();
		else if (ctx.objElm() != null)
			str = ctx.objElm().getText();

		mResult.getArg().setRet(str);

		return str;
	}

	@Override
	public String visitArgSelf(ArgSelfContext ctx) {
		String str = ctx.pairedObj().getText();
		mResult.getArg().setSelf(str);

		return str;
	}

	@Override
	public String visitArgStr(ArgStrContext ctx) {
		mRT = ResultType.ARG;

		return super.visitArgStr(ctx);
	}

	@Override
	public String visitAscElmList(AscElmListContext ctx) {
		CLPState state = null;

		if (mRT == ResultType.POST_STATE)
			state = mResult.getPostState();
		else if (mRT == ResultType.PRE_STATE)
			state = mResult.getPreState();

		for (int i = 0; i < ctx.ascElm().size(); i++) {
			String str = ctx.ascElm(i).getText();
			if (mRT == ResultType.POST_STATE)
				state.addAsc(str);
			else if (mRT == ResultType.PRE_STATE)
				state.addAsc(str);
		}

		if (state == null)
			return "";

		return state.getAscList().toString();
	}

	@Override
	public String visitLiteral(LiteralContext ctx) {
		return super.visitLiteral(ctx);
	}

	@Override
	public String visitObjElmList(ObjElmListContext ctx) {

		CLPState state = null;

		if (mRT == ResultType.POST_STATE)
			state = mResult.getPostState();
		else if (mRT == ResultType.PRE_STATE)
			state = mResult.getPreState();

		for (int i = 0; i < ctx.objElm().size(); i++) {
			String str = ctx.objElm(i).getText();
			if (mRT == ResultType.POST_STATE)
				state.addObj(str);
			else if (mRT == ResultType.PRE_STATE)
				state.addObj(str);
		}

		if (state == null)
			return "";

		return state.getObjList().toString();
	}

	@Override
	public String visitPostStateStr(PostStateStrContext ctx) {
		mRT = ResultType.POST_STATE;

		return super.visitPostStateStr(ctx);
	}

	@Override
	public String visitPreStateStr(PreStateStrContext ctx) {
		mRT = ResultType.PRE_STATE;

		return super.visitPreStateStr(ctx);
	}

	@Override
	public String visitResult(ResultContext ctx) {
		mResult = new CLPResult();

		return super.visitResult(ctx);
	}
}