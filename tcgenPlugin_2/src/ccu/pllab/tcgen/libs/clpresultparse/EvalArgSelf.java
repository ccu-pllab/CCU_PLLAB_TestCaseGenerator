package ccu.pllab.tcgen.libs.clpresultparse;

 
import ccu.pllab.tcgen.libs.clpresultparse.ResultParser.ArgSelfContext;
import ccu.pllab.tcgen.libs.clpresultparse.ResultParser.ObjElmContext;
import ccu.pllab.tcgen.libs.clpresultparse.ResultParser.PairedObjContext;

// return the object ID
public class EvalArgSelf extends ResultBaseVisitor<Integer> {

	@Override
	public Integer visitArgSelf(ArgSelfContext ctx) {
		return visit(ctx.pairedObj());
	}
 
	@Override
	public Integer visitObjElm(ObjElmContext ctx) {
		String id = ctx.INTEGER().getText();

		return Integer.valueOf(id);
	}

	@Override
	public Integer visitPairedObj(PairedObjContext ctx) {
		// we only need the ID of the object
		return visit(ctx.objElm(0));
	}
}
