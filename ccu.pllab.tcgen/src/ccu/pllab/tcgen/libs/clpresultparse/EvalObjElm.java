package ccu.pllab.tcgen.libs.clpresultparse;

 
import java.util.ArrayList;

import org.apache.commons.lang3.tuple.ImmutableTriple;

import ccu.pllab.tcgen.libs.clpresultparse.ResultParser.ObjElmContext;
import ccu.pllab.tcgen.libs.pivotmodel.Attribute;
import ccu.pllab.tcgen.libs.pivotmodel.Model;
import ccu.pllab.tcgen.libs.pivotmodel.ObjectInstance;
import ccu.pllab.tcgen.libs.pivotmodel.UML2Class;

// this class evaluate instances data
public class EvalObjElm extends ResultBaseVisitor<ObjectInstance> {
	Model mClsDiagInfo = null;

	public EvalObjElm(Model clsInfo) {
		mClsDiagInfo = clsInfo;
	}
 
	@Override
	public ObjectInstance visitObjElm(ObjElmContext ctx) {
		ObjectInstance obj = new ObjectInstance();

		String clsName = ctx.STRUCTNAME().toString().replace("\"", "");

		obj.setName(clsName);
		obj.setOid(Integer.valueOf(ctx.INTEGER().getText()));

		// look up associated classinfo
		UML2Class clsInfo = mClsDiagInfo.findClassInfoByName(clsName);

		// parsing attributes
		// if attribute is another object, give the object id instead
		ArrayList<Attribute> attrList = clsInfo.getAttrList();
		for (int i = 0; i < attrList.size(); i++) {
			Attribute attr = attrList.get(i);
			String value = "";

			if (ctx.literal(i) != null)
				value = ctx.literal(i).getText();
			else if (ctx.objElm(i) != null) {
				EvalObjElm eval = new EvalObjElm(mClsDiagInfo);
				ObjectInstance inner_obj = eval.visit(ctx.objElm(i));
				value = String.valueOf(inner_obj.getOid());
			}

			ImmutableTriple<String, String, String> attrIns = new ImmutableTriple<String, String, String>(attr.getName(), attr.getType(), value);
			obj.getAttrValueList().add(attrIns);
		}

		return obj;
	}

}
