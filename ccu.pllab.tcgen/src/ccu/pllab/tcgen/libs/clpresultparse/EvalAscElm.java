package ccu.pllab.tcgen.libs.clpresultparse;

 
import java.util.ArrayList;

import org.apache.commons.lang3.tuple.ImmutableTriple;

import ccu.pllab.tcgen.libs.clpresultparse.ResultParser.AscElmContext;
import ccu.pllab.tcgen.libs.pivotmodel.AscInstance;
import ccu.pllab.tcgen.libs.pivotmodel.Association;
import ccu.pllab.tcgen.libs.pivotmodel.AssociationEnd;
import ccu.pllab.tcgen.libs.pivotmodel.Model;

public class EvalAscElm extends ResultBaseVisitor<AscInstance> {
	Model mClsDiagInfo = null;

	public EvalAscElm(Model clsInfo) {
		mClsDiagInfo = clsInfo;
	}
 
	@Override
	public AscInstance visitAscElm(AscElmContext ctx) {
		AscInstance asc = new AscInstance();

		String ascName = ctx.STRUCTNAME().getText().replace("\"", "");

		asc.setName(ascName);

		// look up associated classinfo
		Association ascInfo = mClsDiagInfo.findAscInfo(ascName);

		// parsing attributes
		ArrayList<AssociationEnd> roleList = ascInfo.getRoleList();
		for (int i = 0; i < roleList.size(); i++) {
			AssociationEnd role = roleList.get(i);

			Integer id = Integer.valueOf(ctx.INTEGER(i).getText());

			ImmutableTriple<String, String, Integer> roleInsn = new ImmutableTriple<String, String, Integer>(role.getName(), role.getType(), id);
			asc.getRoleList().add(roleInsn);
		}

		return asc;
	}

}
